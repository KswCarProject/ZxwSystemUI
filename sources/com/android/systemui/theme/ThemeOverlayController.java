package com.android.systemui.theme;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.FabricatedOverlay;
import android.content.om.OverlayIdentifier;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.CoreStartable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.monet.Style;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;

public class ThemeOverlayController extends CoreStartable {
    public boolean mAcceptColorEvents = true;
    public final Executor mBgExecutor;
    public final Handler mBgHandler;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean equals = "android.intent.action.MANAGED_PROFILE_ADDED".equals(intent.getAction());
            boolean isManagedProfile = ThemeOverlayController.this.mUserManager.isManagedProfile(intent.getIntExtra("android.intent.extra.user_handle", 0));
            if (equals) {
                if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() || !isManagedProfile) {
                    Log.d("ThemeOverlayController", "Updating overlays for user switch / profile added.");
                    ThemeOverlayController.this.reevaluateSystemTheme(true);
                    return;
                }
                Log.i("ThemeOverlayController", "User setup not finished when " + intent.getAction() + " was received. Deferring... Managed profile? " + isManagedProfile);
            } else if (!"android.intent.action.WALLPAPER_CHANGED".equals(intent.getAction())) {
            } else {
                if (intent.getBooleanExtra("android.service.wallpaper.extra.FROM_FOREGROUND_APP", false)) {
                    ThemeOverlayController.this.mAcceptColorEvents = true;
                    Log.i("ThemeOverlayController", "Wallpaper changed, allowing color events again");
                    return;
                }
                Log.i("ThemeOverlayController", "Wallpaper changed from background app, keep deferring color events. Accepting: " + ThemeOverlayController.this.mAcceptColorEvents);
            }
        }
    };
    public ColorScheme mColorScheme;
    public final SparseArray<WallpaperColors> mCurrentColors = new SparseArray<>();
    public boolean mDeferredThemeEvaluation;
    public final SparseArray<WallpaperColors> mDeferredWallpaperColors = new SparseArray<>();
    public final SparseIntArray mDeferredWallpaperColorsFlags = new SparseIntArray();
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() {
        public void onUserSetupChanged() {
            if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() && ThemeOverlayController.this.mDeferredThemeEvaluation) {
                Log.i("ThemeOverlayController", "Applying deferred theme");
                ThemeOverlayController.this.mDeferredThemeEvaluation = false;
                ThemeOverlayController.this.reevaluateSystemTheme(true);
            }
        }
    };
    public final boolean mIsMonetEnabled;
    public final Executor mMainExecutor;
    public int mMainWallpaperColor = 0;
    public boolean mNeedsOverlayCreation;
    public FabricatedOverlay mNeutralOverlay;
    public final WallpaperManager.OnColorsChangedListener mOnColorsChangedListener = new WallpaperManager.OnColorsChangedListener() {
        public void onColorsChanged(WallpaperColors wallpaperColors, int i) {
            throw new IllegalStateException("This should never be invoked, all messages should arrive on the overload that has a user id");
        }

        public void onColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
            boolean z = i2 == ThemeOverlayController.this.mUserTracker.getUserId();
            if (!z || ThemeOverlayController.this.mAcceptColorEvents || ThemeOverlayController.this.mWakefulnessLifecycle.getWakefulness() == 0) {
                if (z && wallpaperColors != null) {
                    ThemeOverlayController.this.mAcceptColorEvents = false;
                    ThemeOverlayController.this.mDeferredWallpaperColors.put(i2, (Object) null);
                    ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(i2, 0);
                }
                ThemeOverlayController.this.handleWallpaperColors(wallpaperColors, i, i2);
                return;
            }
            ThemeOverlayController.this.mDeferredWallpaperColors.put(i2, wallpaperColors);
            ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(i2, i);
            Log.i("ThemeOverlayController", "colors received; processing deferred until screen off: " + wallpaperColors + " user: " + i2);
        }
    };
    public final Resources mResources;
    public FabricatedOverlay mSecondaryOverlay;
    public final SecureSettings mSecureSettings;
    public boolean mSkipSettingChange;
    public final ThemeOverlayApplier mThemeManager;
    public Style mThemeStyle = Style.TONAL_SPOT;
    public final UserManager mUserManager;
    public final UserTracker mUserTracker;
    public final UserTracker.Callback mUserTrackerCallback = new UserTracker.Callback() {
        public void onUserChanged(int i, Context context) {
            boolean isManagedProfile = ThemeOverlayController.this.mUserManager.isManagedProfile(i);
            if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() || !isManagedProfile) {
                Log.d("ThemeOverlayController", "Updating overlays for user switch / profile added.");
                ThemeOverlayController.this.reevaluateSystemTheme(true);
                return;
            }
            Log.i("ThemeOverlayController", "User setup not finished when new user event was received. Deferring... Managed profile? " + isManagedProfile);
        }
    };
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WallpaperManager mWallpaperManager;

    public final int getLatestWallpaperType(int i) {
        return this.mWallpaperManager.getWallpaperIdForUser(2, i) > this.mWallpaperManager.getWallpaperIdForUser(1, i) ? 2 : 1;
    }

    public final boolean isSeedColorSet(JSONObject jSONObject, WallpaperColors wallpaperColors) {
        String str;
        if (wallpaperColors == null || (str = (String) jSONObject.opt("android.theme.customization.system_palette")) == null) {
            return false;
        }
        if (!str.startsWith("#")) {
            str = "#" + str;
        }
        int parseColor = Color.parseColor(str);
        for (Integer intValue : ColorScheme.getSeedColors(wallpaperColors)) {
            if (intValue.intValue() == parseColor) {
                Log.d("ThemeOverlayController", "Same as previous set system palette: " + str);
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0108, code lost:
        if (r8.has("android.theme.customization.system_palette") != false) goto L_0x010a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void handleWallpaperColors(android.app.WallpaperColors r12, int r13, int r14) {
        /*
            r11 = this;
            java.lang.String r0 = "android.theme.customization.accent_color"
            java.lang.String r1 = "android.theme.customization.color_source"
            com.android.systemui.settings.UserTracker r2 = r11.mUserTracker
            int r2 = r2.getUserId()
            android.util.SparseArray<android.app.WallpaperColors> r3 = r11.mCurrentColors
            java.lang.Object r3 = r3.get(r14)
            r4 = 0
            r5 = 1
            if (r3 == 0) goto L_0x0016
            r3 = r5
            goto L_0x0017
        L_0x0016:
            r3 = r4
        L_0x0017:
            int r6 = r11.getLatestWallpaperType(r14)
            r6 = r6 & r13
            java.lang.String r7 = "ThemeOverlayController"
            if (r6 == 0) goto L_0x0041
            android.util.SparseArray<android.app.WallpaperColors> r8 = r11.mCurrentColors
            r8.put(r14, r12)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "got new colors: "
            r8.append(r9)
            r8.append(r12)
            java.lang.String r9 = " where: "
            r8.append(r9)
            r8.append(r13)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r7, r8)
        L_0x0041:
            if (r14 == r2) goto L_0x0068
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r13 = "Colors "
            r11.append(r13)
            r11.append(r12)
            java.lang.String r12 = " for user "
            r11.append(r12)
            r11.append(r14)
            java.lang.String r12 = ". Not for current user: "
            r11.append(r12)
            r11.append(r2)
            java.lang.String r11 = r11.toString()
            android.util.Log.d(r7, r11)
            return
        L_0x0068:
            com.android.systemui.statusbar.policy.DeviceProvisionedController r8 = r11.mDeviceProvisionedController
            if (r8 == 0) goto L_0x00cb
            boolean r8 = r8.isCurrentUserSetup()
            if (r8 != 0) goto L_0x00cb
            if (r3 == 0) goto L_0x008b
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "Wallpaper color event deferred until setup is finished: "
            r13.append(r14)
            r13.append(r12)
            java.lang.String r12 = r13.toString()
            android.util.Log.i(r7, r12)
            r11.mDeferredThemeEvaluation = r5
            return
        L_0x008b:
            boolean r8 = r11.mDeferredThemeEvaluation
            if (r8 == 0) goto L_0x00a4
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r13 = "Wallpaper color event received, but we already were deferring eval: "
            r11.append(r13)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            android.util.Log.i(r7, r11)
            return
        L_0x00a4:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "During user setup, but allowing first color event: had? "
            r8.append(r9)
            r8.append(r3)
            java.lang.String r3 = " has? "
            r8.append(r3)
            android.util.SparseArray<android.app.WallpaperColors> r3 = r11.mCurrentColors
            java.lang.Object r14 = r3.get(r14)
            if (r14 == 0) goto L_0x00c0
            r14 = r5
            goto L_0x00c1
        L_0x00c0:
            r14 = r4
        L_0x00c1:
            r8.append(r14)
            java.lang.String r14 = r8.toString()
            android.util.Log.i(r7, r14)
        L_0x00cb:
            com.android.systemui.util.settings.SecureSettings r14 = r11.mSecureSettings
            java.lang.String r3 = "theme_customization_overlay_packages"
            java.lang.String r14 = r14.getStringForUser(r3, r2)
            r2 = 3
            if (r13 != r2) goto L_0x00d8
            r2 = r5
            goto L_0x00d9
        L_0x00d8:
            r2 = r4
        L_0x00d9:
            if (r14 != 0) goto L_0x00e1
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0160 }
            r8.<init>()     // Catch:{ JSONException -> 0x0160 }
            goto L_0x00e6
        L_0x00e1:
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0160 }
            r8.<init>(r14)     // Catch:{ JSONException -> 0x0160 }
        L_0x00e6:
            java.lang.String r9 = "preset"
            java.lang.String r10 = r8.optString(r1)     // Catch:{ JSONException -> 0x0160 }
            boolean r9 = r9.equals(r10)     // Catch:{ JSONException -> 0x0160 }
            if (r9 != 0) goto L_0x0166
            if (r6 == 0) goto L_0x0166
            boolean r12 = r11.isSeedColorSet(r8, r12)     // Catch:{ JSONException -> 0x0160 }
            if (r12 != 0) goto L_0x0166
            r11.mSkipSettingChange = r5     // Catch:{ JSONException -> 0x0160 }
            boolean r12 = r8.has(r0)     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r5 = "android.theme.customization.system_palette"
            if (r12 != 0) goto L_0x010a
            boolean r12 = r8.has(r5)     // Catch:{ JSONException -> 0x0160 }
            if (r12 == 0) goto L_0x0115
        L_0x010a:
            r8.remove(r0)     // Catch:{ JSONException -> 0x0160 }
            r8.remove(r5)     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r12 = "android.theme.customization.color_index"
            r8.remove(r12)     // Catch:{ JSONException -> 0x0160 }
        L_0x0115:
            java.lang.String r12 = "android.theme.customization.color_both"
            if (r2 == 0) goto L_0x011c
            java.lang.String r0 = "1"
            goto L_0x011e
        L_0x011c:
            java.lang.String r0 = "0"
        L_0x011e:
            r8.put(r12, r0)     // Catch:{ JSONException -> 0x0160 }
            r12 = 2
            if (r13 != r12) goto L_0x0127
            java.lang.String r12 = "lock_wallpaper"
            goto L_0x0129
        L_0x0127:
            java.lang.String r12 = "home_wallpaper"
        L_0x0129:
            r8.put(r1, r12)     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r12 = "_applied_timestamp"
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ JSONException -> 0x0160 }
            r8.put(r12, r0)     // Catch:{ JSONException -> 0x0160 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0160 }
            r12.<init>()     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r13 = "Updating theme setting from "
            r12.append(r13)     // Catch:{ JSONException -> 0x0160 }
            r12.append(r14)     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r13 = " to "
            r12.append(r13)     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r13 = r8.toString()     // Catch:{ JSONException -> 0x0160 }
            r12.append(r13)     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r12 = r12.toString()     // Catch:{ JSONException -> 0x0160 }
            android.util.Log.d(r7, r12)     // Catch:{ JSONException -> 0x0160 }
            com.android.systemui.util.settings.SecureSettings r12 = r11.mSecureSettings     // Catch:{ JSONException -> 0x0160 }
            java.lang.String r13 = r8.toString()     // Catch:{ JSONException -> 0x0160 }
            r14 = -2
            r12.putStringForUser(r3, r13, r14)     // Catch:{ JSONException -> 0x0160 }
            goto L_0x0166
        L_0x0160:
            r12 = move-exception
            java.lang.String r13 = "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES."
            android.util.Log.i(r7, r13, r12)
        L_0x0166:
            r11.reevaluateSystemTheme(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.theme.ThemeOverlayController.handleWallpaperColors(android.app.WallpaperColors, int, int):void");
    }

    public ThemeOverlayController(Context context, BroadcastDispatcher broadcastDispatcher, Handler handler, Executor executor, Executor executor2, ThemeOverlayApplier themeOverlayApplier, SecureSettings secureSettings, WallpaperManager wallpaperManager, UserManager userManager, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, DumpManager dumpManager, FeatureFlags featureFlags, Resources resources, WakefulnessLifecycle wakefulnessLifecycle) {
        super(context);
        this.mIsMonetEnabled = featureFlags.isEnabled(Flags.MONET);
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mUserManager = userManager;
        this.mBgExecutor = executor2;
        this.mMainExecutor = executor;
        this.mBgHandler = handler;
        this.mThemeManager = themeOverlayApplier;
        this.mSecureSettings = secureSettings;
        this.mWallpaperManager = wallpaperManager;
        this.mUserTracker = userTracker;
        this.mResources = resources;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        dumpManager.registerDumpable("ThemeOverlayController", this);
    }

    public void start() {
        Log.d("ThemeOverlayController", "Start");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        intentFilter.addAction("android.intent.action.WALLPAPER_CHANGED");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter, this.mMainExecutor, UserHandle.ALL);
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("theme_customization_overlay_packages"), false, (ContentObserver) new ContentObserver(this.mBgHandler) {
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                Log.d("ThemeOverlayController", "Overlay changed for user: " + i2);
                if (ThemeOverlayController.this.mUserTracker.getUserId() == i2) {
                    if (!ThemeOverlayController.this.mDeviceProvisionedController.isUserSetup(i2)) {
                        Log.i("ThemeOverlayController", "Theme application deferred when setting changed.");
                        ThemeOverlayController.this.mDeferredThemeEvaluation = true;
                    } else if (ThemeOverlayController.this.mSkipSettingChange) {
                        Log.d("ThemeOverlayController", "Skipping setting change");
                        ThemeOverlayController.this.mSkipSettingChange = false;
                    } else {
                        ThemeOverlayController.this.reevaluateSystemTheme(true);
                    }
                }
            }
        }, -1);
        if (this.mIsMonetEnabled) {
            this.mUserTracker.addCallback(this.mUserTrackerCallback, this.mMainExecutor);
            this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
            if (this.mIsMonetEnabled) {
                ThemeOverlayController$$ExternalSyntheticLambda1 themeOverlayController$$ExternalSyntheticLambda1 = new ThemeOverlayController$$ExternalSyntheticLambda1(this);
                if (!this.mDeviceProvisionedController.isCurrentUserSetup()) {
                    themeOverlayController$$ExternalSyntheticLambda1.run();
                } else {
                    this.mBgExecutor.execute(themeOverlayController$$ExternalSyntheticLambda1);
                }
                this.mWallpaperManager.addOnColorsChangedListener(this.mOnColorsChangedListener, (Handler) null, -1);
                this.mWakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer() {
                    public void onFinishedGoingToSleep() {
                        int userId = ThemeOverlayController.this.mUserTracker.getUserId();
                        WallpaperColors wallpaperColors = (WallpaperColors) ThemeOverlayController.this.mDeferredWallpaperColors.get(userId);
                        if (wallpaperColors != null) {
                            int i = ThemeOverlayController.this.mDeferredWallpaperColorsFlags.get(userId);
                            ThemeOverlayController.this.mDeferredWallpaperColors.put(userId, (Object) null);
                            ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(userId, 0);
                            ThemeOverlayController.this.handleWallpaperColors(wallpaperColors, i, userId);
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$start$1() {
        ThemeOverlayController$$ExternalSyntheticLambda2 themeOverlayController$$ExternalSyntheticLambda2 = new ThemeOverlayController$$ExternalSyntheticLambda2(this, this.mWallpaperManager.getWallpaperColors(getLatestWallpaperType(this.mUserTracker.getUserId())));
        if (this.mDeviceProvisionedController.isCurrentUserSetup()) {
            this.mMainExecutor.execute(themeOverlayController$$ExternalSyntheticLambda2);
        } else {
            themeOverlayController$$ExternalSyntheticLambda2.run();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0(WallpaperColors wallpaperColors) {
        Log.d("ThemeOverlayController", "Boot colors: " + wallpaperColors);
        this.mCurrentColors.put(this.mUserTracker.getUserId(), wallpaperColors);
        reevaluateSystemTheme(false);
    }

    public final void reevaluateSystemTheme(boolean z) {
        int i;
        WallpaperColors wallpaperColors = this.mCurrentColors.get(this.mUserTracker.getUserId());
        if (wallpaperColors == null) {
            i = 0;
        } else {
            i = getNeutralColor(wallpaperColors);
        }
        if (this.mMainWallpaperColor != i || z) {
            this.mMainWallpaperColor = i;
            if (this.mIsMonetEnabled) {
                Style fetchThemeStyleFromSetting = fetchThemeStyleFromSetting();
                this.mThemeStyle = fetchThemeStyleFromSetting;
                this.mSecondaryOverlay = getOverlay(this.mMainWallpaperColor, 1, fetchThemeStyleFromSetting);
                this.mNeutralOverlay = getOverlay(this.mMainWallpaperColor, 0, this.mThemeStyle);
                this.mNeedsOverlayCreation = true;
                Log.d("ThemeOverlayController", "fetched overlays. accent: " + this.mSecondaryOverlay + " neutral: " + this.mNeutralOverlay);
            }
            updateThemeOverlays();
        }
    }

    public int getNeutralColor(WallpaperColors wallpaperColors) {
        return ColorScheme.getSeedColor(wallpaperColors);
    }

    public FabricatedOverlay getOverlay(int i, int i2, Style style) {
        String str;
        ColorScheme colorScheme = new ColorScheme(i, (this.mResources.getConfiguration().uiMode & 48) == 32, style);
        this.mColorScheme = colorScheme;
        List<Integer> allAccentColors = i2 == 1 ? colorScheme.getAllAccentColors() : colorScheme.getAllNeutralColors();
        String str2 = i2 == 1 ? "accent" : "neutral";
        int size = this.mColorScheme.getAccent1().size();
        FabricatedOverlay.Builder builder = new FabricatedOverlay.Builder(ThemeOverlayApplier.SYSUI_PACKAGE, str2, ThemeOverlayApplier.ANDROID_PACKAGE);
        for (int i3 = 0; i3 < allAccentColors.size(); i3++) {
            int i4 = i3 % size;
            int i5 = (i3 / size) + 1;
            if (i4 == 0) {
                str = "android:color/system_" + str2 + i5 + "_10";
            } else if (i4 != 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("android:color/system_");
                sb.append(str2);
                sb.append(i5);
                sb.append("_");
                sb.append(i4 - 1);
                sb.append("00");
                str = sb.toString();
            } else {
                str = "android:color/system_" + str2 + i5 + "_50";
            }
            builder.setResourceValue(str, 28, ColorUtils.setAlphaComponent(allAccentColors.get(i3).intValue(), 255));
        }
        return builder.build();
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean colorSchemeIsApplied(java.util.Set<android.os.UserHandle> r6) {
        /*
            r5 = this;
            android.util.ArraySet r0 = new android.util.ArraySet
            r0.<init>(r6)
            android.os.UserHandle r6 = android.os.UserHandle.SYSTEM
            r0.add(r6)
            java.util.Iterator r6 = r0.iterator()
        L_0x000e:
            boolean r0 = r6.hasNext()
            if (r0 == 0) goto L_0x00cb
            java.lang.Object r0 = r6.next()
            android.os.UserHandle r0 = (android.os.UserHandle) r0
            boolean r1 = r0.isSystem()
            r2 = 0
            if (r1 == 0) goto L_0x0024
            android.content.res.Resources r0 = r5.mResources
            goto L_0x002e
        L_0x0024:
            android.content.Context r1 = r5.mContext
            android.content.Context r0 = r1.createContextAsUser(r0, r2)
            android.content.res.Resources r0 = r0.getResources()
        L_0x002e:
            r1 = 17170494(0x106003e, float:2.4612087E-38)
            android.content.Context r3 = r5.mContext
            android.content.res.Resources$Theme r3 = r3.getTheme()
            int r1 = r0.getColor(r1, r3)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getAccent1()
            r4 = 6
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            r1 = 17170507(0x106004b, float:2.4612123E-38)
            android.content.Context r3 = r5.mContext
            android.content.res.Resources$Theme r3 = r3.getTheme()
            int r1 = r0.getColor(r1, r3)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getAccent2()
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            r1 = 17170520(0x1060058, float:2.461216E-38)
            android.content.Context r3 = r5.mContext
            android.content.res.Resources$Theme r3 = r3.getTheme()
            int r1 = r0.getColor(r1, r3)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getAccent3()
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            r1 = 17170468(0x1060024, float:2.4612014E-38)
            android.content.Context r3 = r5.mContext
            android.content.res.Resources$Theme r3 = r3.getTheme()
            int r1 = r0.getColor(r1, r3)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getNeutral1()
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            r1 = 17170481(0x1060031, float:2.461205E-38)
            android.content.Context r3 = r5.mContext
            android.content.res.Resources$Theme r3 = r3.getTheme()
            int r0 = r0.getColor(r1, r3)
            com.android.systemui.monet.ColorScheme r1 = r5.mColorScheme
            java.util.List r1 = r1.getNeutral2()
            java.lang.Object r1 = r1.get(r4)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r0 == r1) goto L_0x000e
        L_0x00ca:
            return r2
        L_0x00cb:
            r5 = 1
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.theme.ThemeOverlayController.colorSchemeIsApplied(java.util.Set):boolean");
    }

    public final void updateThemeOverlays() {
        FabricatedOverlay fabricatedOverlay;
        FabricatedOverlay fabricatedOverlay2;
        int userId = this.mUserTracker.getUserId();
        String stringForUser = this.mSecureSettings.getStringForUser("theme_customization_overlay_packages", userId);
        Log.d("ThemeOverlayController", "updateThemeOverlays. Setting: " + stringForUser);
        ArrayMap arrayMap = new ArrayMap();
        if (!TextUtils.isEmpty(stringForUser)) {
            try {
                JSONObject jSONObject = new JSONObject(stringForUser);
                for (String next : ThemeOverlayApplier.THEME_CATEGORIES) {
                    if (jSONObject.has(next)) {
                        arrayMap.put(next, new OverlayIdentifier(jSONObject.getString(next)));
                    }
                }
            } catch (JSONException e) {
                Log.i("ThemeOverlayController", "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
            }
        }
        OverlayIdentifier overlayIdentifier = (OverlayIdentifier) arrayMap.get("android.theme.customization.system_palette");
        if (this.mIsMonetEnabled && overlayIdentifier != null && overlayIdentifier.getPackageName() != null) {
            try {
                String lowerCase = overlayIdentifier.getPackageName().toLowerCase();
                if (!lowerCase.startsWith("#")) {
                    lowerCase = "#" + lowerCase;
                }
                int parseColor = Color.parseColor(lowerCase);
                this.mNeutralOverlay = getOverlay(parseColor, 0, this.mThemeStyle);
                this.mSecondaryOverlay = getOverlay(parseColor, 1, this.mThemeStyle);
                this.mNeedsOverlayCreation = true;
                arrayMap.remove("android.theme.customization.system_palette");
                arrayMap.remove("android.theme.customization.accent_color");
            } catch (Exception e2) {
                Log.w("ThemeOverlayController", "Invalid color definition: " + overlayIdentifier.getPackageName(), e2);
            }
        } else if (!this.mIsMonetEnabled && overlayIdentifier != null) {
            try {
                arrayMap.remove("android.theme.customization.system_palette");
                arrayMap.remove("android.theme.customization.accent_color");
            } catch (NumberFormatException unused) {
            }
        }
        if (!arrayMap.containsKey("android.theme.customization.system_palette") && (fabricatedOverlay2 = this.mNeutralOverlay) != null) {
            arrayMap.put("android.theme.customization.system_palette", fabricatedOverlay2.getIdentifier());
        }
        if (!arrayMap.containsKey("android.theme.customization.accent_color") && (fabricatedOverlay = this.mSecondaryOverlay) != null) {
            arrayMap.put("android.theme.customization.accent_color", fabricatedOverlay.getIdentifier());
        }
        HashSet hashSet = new HashSet();
        for (UserInfo userInfo : this.mUserManager.getEnabledProfiles(userId)) {
            if (userInfo.isManagedProfile()) {
                hashSet.add(userInfo.getUserHandle());
            }
        }
        if (colorSchemeIsApplied(hashSet)) {
            Log.d("ThemeOverlayController", "Skipping overlay creation. Theme was already: " + this.mColorScheme);
            return;
        }
        Log.d("ThemeOverlayController", "Applying overlays: " + ((String) arrayMap.keySet().stream().map(new ThemeOverlayController$$ExternalSyntheticLambda0(arrayMap)).collect(Collectors.joining(", "))));
        if (this.mNeedsOverlayCreation) {
            this.mNeedsOverlayCreation = false;
            this.mThemeManager.applyCurrentUserOverlays(arrayMap, new FabricatedOverlay[]{this.mSecondaryOverlay, this.mNeutralOverlay}, userId, hashSet);
            return;
        }
        this.mThemeManager.applyCurrentUserOverlays(arrayMap, (FabricatedOverlay[]) null, userId, hashSet);
    }

    public static /* synthetic */ String lambda$updateThemeOverlays$2(Map map, String str) {
        return str + " -> " + map.get(str);
    }

    public final Style fetchThemeStyleFromSetting() {
        Style style = Style.TONAL_SPOT;
        List asList = Arrays.asList(new Style[]{Style.EXPRESSIVE, Style.SPRITZ, style, Style.FRUIT_SALAD, Style.RAINBOW, Style.VIBRANT});
        Style style2 = this.mThemeStyle;
        String stringForUser = this.mSecureSettings.getStringForUser("theme_customization_overlay_packages", this.mUserTracker.getUserId());
        if (TextUtils.isEmpty(stringForUser)) {
            return style2;
        }
        try {
            Style valueOf = Style.valueOf(new JSONObject(stringForUser).getString("android.theme.customization.theme_style"));
            if (asList.contains(valueOf)) {
                style = valueOf;
            }
            return style;
        } catch (IllegalArgumentException | JSONException e) {
            Log.i("ThemeOverlayController", "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
            return Style.TONAL_SPOT;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("mSystemColors=" + this.mCurrentColors);
        printWriter.println("mMainWallpaperColor=" + Integer.toHexString(this.mMainWallpaperColor));
        printWriter.println("mSecondaryOverlay=" + this.mSecondaryOverlay);
        printWriter.println("mNeutralOverlay=" + this.mNeutralOverlay);
        printWriter.println("mIsMonetEnabled=" + this.mIsMonetEnabled);
        printWriter.println("mColorScheme=" + this.mColorScheme);
        printWriter.println("mNeedsOverlayCreation=" + this.mNeedsOverlayCreation);
        printWriter.println("mAcceptColorEvents=" + this.mAcceptColorEvents);
        printWriter.println("mDeferredThemeEvaluation=" + this.mDeferredThemeEvaluation);
        printWriter.println("mThemeStyle=" + this.mThemeStyle);
    }
}
