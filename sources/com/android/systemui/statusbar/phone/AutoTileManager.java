package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$array;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.SafetyController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AutoTileManager implements UserAwareController {
    public final ArrayList<AutoAddSetting> mAutoAddSettingList = new ArrayList<>();
    public final AutoAddTracker mAutoTracker;
    @VisibleForTesting
    public final CastController.Callback mCastCallback = new CastController.Callback() {
        public void onCastDevicesChanged() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("cast")) {
                boolean z = false;
                Iterator<CastController.CastDevice> it = AutoTileManager.this.mCastController.getCastDevices().iterator();
                while (true) {
                    if (it.hasNext()) {
                        int i = it.next().state;
                        if (i != 2) {
                            if (i == 1) {
                                break;
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                z = true;
                if (z) {
                    AutoTileManager.this.mHost.addTile("cast");
                    AutoTileManager.this.mAutoTracker.setTileAdded("cast");
                    AutoTileManager.this.mHandler.post(new AutoTileManager$7$$ExternalSyntheticLambda0(this));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onCastDevicesChanged$0() {
            AutoTileManager.this.mCastController.removeCallback(AutoTileManager.this.mCastCallback);
        }
    };
    public final CastController mCastController;
    public final Context mContext;
    public UserHandle mCurrentUser;
    public final DataSaverController mDataSaverController;
    public final DataSaverController.Listener mDataSaverListener = new DataSaverController.Listener() {
        public void onDataSaverChanged(boolean z) {
            if (!AutoTileManager.this.mAutoTracker.isAdded("saver") && z) {
                AutoTileManager.this.mHost.addTile("saver");
                AutoTileManager.this.mAutoTracker.setTileAdded("saver");
                AutoTileManager.this.mHandler.post(new AutoTileManager$2$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDataSaverChanged$0() {
            AutoTileManager.this.mDataSaverController.removeCallback(AutoTileManager.this.mDataSaverListener);
        }
    };
    public final DeviceControlsController.Callback mDeviceControlsCallback = new DeviceControlsController.Callback() {
        public void onControlsUpdate(Integer num) {
            if (!AutoTileManager.this.mAutoTracker.isAdded("controls")) {
                if (num != null) {
                    AutoTileManager.this.mHost.addTile("controls", num.intValue());
                }
                AutoTileManager.this.mAutoTracker.setTileAdded("controls");
                AutoTileManager.this.mHandler.post(new AutoTileManager$4$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onControlsUpdate$0() {
            AutoTileManager.this.mDeviceControlsController.removeCallback();
        }
    };
    public final DeviceControlsController mDeviceControlsController;
    public final Handler mHandler;
    public final QSTileHost mHost;
    public final HotspotController.Callback mHotspotCallback = new HotspotController.Callback() {
        public void onHotspotChanged(boolean z, int i) {
            if (!AutoTileManager.this.mAutoTracker.isAdded("hotspot") && z) {
                AutoTileManager.this.mHost.addTile("hotspot");
                AutoTileManager.this.mAutoTracker.setTileAdded("hotspot");
                AutoTileManager.this.mHandler.post(new AutoTileManager$3$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onHotspotChanged$0() {
            AutoTileManager.this.mHotspotController.removeCallback(AutoTileManager.this.mHotspotCallback);
        }
    };
    public final HotspotController mHotspotController;
    public boolean mInitialized;
    public final boolean mIsReduceBrightColorsAvailable;
    public final ManagedProfileController mManagedProfileController;
    @VisibleForTesting
    public final NightDisplayListener.Callback mNightDisplayCallback = new NightDisplayListener.Callback() {
        public void onActivated(boolean z) {
            if (z) {
                addNightTile();
            }
        }

        public void onAutoModeChanged(int i) {
            if (i == 1 || i == 2) {
                addNightTile();
            }
        }

        public final void addNightTile() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("night")) {
                AutoTileManager.this.mHost.addTile("night");
                AutoTileManager.this.mAutoTracker.setTileAdded("night");
                AutoTileManager.this.mHandler.post(new AutoTileManager$5$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$addNightTile$0() {
            AutoTileManager.this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
        }
    };
    public final NightDisplayListener mNightDisplayListener;
    public final ManagedProfileController.Callback mProfileCallback = new ManagedProfileController.Callback() {
        public void onManagedProfileRemoved() {
        }

        public void onManagedProfileChanged() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("work") && AutoTileManager.this.mManagedProfileController.hasActiveProfile()) {
                AutoTileManager.this.mHost.addTile("work");
                AutoTileManager.this.mAutoTracker.setTileAdded("work");
            }
        }
    };
    @VisibleForTesting
    public final ReduceBrightColorsController.Listener mReduceBrightColorsCallback = new ReduceBrightColorsController.Listener() {
        public void onActivated(boolean z) {
            if (z) {
                addReduceBrightColorsTile();
            }
        }

        public final void addReduceBrightColorsTile() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("reduce_brightness")) {
                AutoTileManager.this.mHost.addTile("reduce_brightness");
                AutoTileManager.this.mAutoTracker.setTileAdded("reduce_brightness");
                AutoTileManager.this.mHandler.post(new AutoTileManager$6$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$addReduceBrightColorsTile$0() {
            AutoTileManager.this.mReduceBrightColorsController.removeCallback((ReduceBrightColorsController.Listener) this);
        }
    };
    public final ReduceBrightColorsController mReduceBrightColorsController;
    @VisibleForTesting
    public final SafetyController.Listener mSafetyCallback = new SafetyController.Listener() {
        public void onSafetyCenterEnableChanged(boolean z) {
            if (AutoTileManager.this.mSafetySpec != null) {
                if (z) {
                    AutoTileManager autoTileManager = AutoTileManager.this;
                    if (!autoTileManager.mAutoTracker.isAdded(autoTileManager.mSafetySpec)) {
                        AutoTileManager.this.initSafetyTile();
                        return;
                    }
                }
                if (!z) {
                    AutoTileManager autoTileManager2 = AutoTileManager.this;
                    if (autoTileManager2.mAutoTracker.isAdded(autoTileManager2.mSafetySpec)) {
                        AutoTileManager autoTileManager3 = AutoTileManager.this;
                        autoTileManager3.mHost.removeTile(CustomTile.getComponentFromSpec(autoTileManager3.mSafetySpec));
                        AutoTileManager autoTileManager4 = AutoTileManager.this;
                        autoTileManager4.mHost.unmarkTileAsAutoAdded(autoTileManager4.mSafetySpec);
                    }
                }
            }
        }
    };
    public final SafetyController mSafetyController;
    public final String mSafetySpec;
    public final SecureSettings mSecureSettings;
    public final WalletController mWalletController;

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0099, code lost:
        if (r1.length() == 0) goto L_0x009b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AutoTileManager(android.content.Context r5, com.android.systemui.qs.AutoAddTracker.Builder r6, com.android.systemui.qs.QSTileHost r7, android.os.Handler r8, com.android.systemui.util.settings.SecureSettings r9, com.android.systemui.statusbar.policy.HotspotController r10, com.android.systemui.statusbar.policy.DataSaverController r11, com.android.systemui.statusbar.phone.ManagedProfileController r12, android.hardware.display.NightDisplayListener r13, com.android.systemui.statusbar.policy.CastController r14, com.android.systemui.qs.ReduceBrightColorsController r15, com.android.systemui.statusbar.policy.DeviceControlsController r16, com.android.systemui.statusbar.policy.WalletController r17, com.android.systemui.statusbar.policy.SafetyController r18, boolean r19) {
        /*
            r4 = this;
            r0 = r4
            r4.<init>()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r0.mAutoAddSettingList = r1
            com.android.systemui.statusbar.phone.AutoTileManager$1 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$1
            r1.<init>()
            r0.mProfileCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$2 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$2
            r1.<init>()
            r0.mDataSaverListener = r1
            com.android.systemui.statusbar.phone.AutoTileManager$3 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$3
            r1.<init>()
            r0.mHotspotCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$4 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$4
            r1.<init>()
            r0.mDeviceControlsCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$5 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$5
            r1.<init>()
            r0.mNightDisplayCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$6 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$6
            r1.<init>()
            r0.mReduceBrightColorsCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$7 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$7
            r1.<init>()
            r0.mCastCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$8 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$8
            r1.<init>()
            r0.mSafetyCallback = r1
            r1 = r5
            r0.mContext = r1
            r2 = r7
            r0.mHost = r2
            r3 = r9
            r0.mSecureSettings = r3
            android.content.Context r2 = r7.getUserContext()
            android.os.UserHandle r2 = r2.getUser()
            r0.mCurrentUser = r2
            int r2 = r2.getIdentifier()
            r3 = r6
            com.android.systemui.qs.AutoAddTracker$Builder r2 = r6.setUserId(r2)
            com.android.systemui.qs.AutoAddTracker r2 = r2.build()
            r0.mAutoTracker = r2
            r2 = r8
            r0.mHandler = r2
            r2 = r10
            r0.mHotspotController = r2
            r2 = r11
            r0.mDataSaverController = r2
            r2 = r12
            r0.mManagedProfileController = r2
            r2 = r13
            r0.mNightDisplayListener = r2
            r2 = r14
            r0.mCastController = r2
            r2 = r15
            r0.mReduceBrightColorsController = r2
            r2 = r19
            r0.mIsReduceBrightColorsAvailable = r2
            r2 = r16
            r0.mDeviceControlsController = r2
            r2 = r17
            r0.mWalletController = r2
            r2 = r18
            r0.mSafetyController = r2
            r2 = 0
            android.content.res.Resources r1 = r5.getResources()     // Catch:{ NotFoundException | NullPointerException -> 0x009b }
            int r3 = com.android.systemui.R$string.safety_quick_settings_tile_class     // Catch:{ NotFoundException | NullPointerException -> 0x009b }
            java.lang.String r1 = r1.getString(r3)     // Catch:{ NotFoundException | NullPointerException -> 0x009b }
            int r3 = r1.length()     // Catch:{ NotFoundException | NullPointerException -> 0x009b }
            if (r3 != 0) goto L_0x009c
        L_0x009b:
            r1 = r2
        L_0x009c:
            if (r1 == 0) goto L_0x00b1
            android.content.ComponentName r2 = new android.content.ComponentName
            android.content.Context r3 = r0.mContext
            android.content.pm.PackageManager r3 = r3.getPackageManager()
            java.lang.String r3 = r3.getPermissionControllerPackageName()
            r2.<init>(r3, r1)
            java.lang.String r2 = com.android.systemui.qs.external.CustomTile.toSpec(r2)
        L_0x00b1:
            r0.mSafetySpec = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.AutoTileManager.<init>(android.content.Context, com.android.systemui.qs.AutoAddTracker$Builder, com.android.systemui.qs.QSTileHost, android.os.Handler, com.android.systemui.util.settings.SecureSettings, com.android.systemui.statusbar.policy.HotspotController, com.android.systemui.statusbar.policy.DataSaverController, com.android.systemui.statusbar.phone.ManagedProfileController, android.hardware.display.NightDisplayListener, com.android.systemui.statusbar.policy.CastController, com.android.systemui.qs.ReduceBrightColorsController, com.android.systemui.statusbar.policy.DeviceControlsController, com.android.systemui.statusbar.policy.WalletController, com.android.systemui.statusbar.policy.SafetyController, boolean):void");
    }

    public void init() {
        if (this.mInitialized) {
            Log.w("AutoTileManager", "Trying to re-initialize");
            return;
        }
        this.mAutoTracker.initialize();
        populateSettingsList();
        startControllersAndSettingsListeners();
        this.mInitialized = true;
    }

    public void startControllersAndSettingsListeners() {
        if (!this.mAutoTracker.isAdded("hotspot")) {
            this.mHotspotController.addCallback(this.mHotspotCallback);
        }
        if (!this.mAutoTracker.isAdded("saver")) {
            this.mDataSaverController.addCallback(this.mDataSaverListener);
        }
        if (!this.mAutoTracker.isAdded("work")) {
            this.mManagedProfileController.addCallback(this.mProfileCallback);
        }
        if (!this.mAutoTracker.isAdded("night") && ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            this.mNightDisplayListener.setCallback(this.mNightDisplayCallback);
        }
        if (!this.mAutoTracker.isAdded("cast")) {
            this.mCastController.addCallback(this.mCastCallback);
        }
        if (!this.mAutoTracker.isAdded("reduce_brightness") && this.mIsReduceBrightColorsAvailable) {
            this.mReduceBrightColorsController.addCallback(this.mReduceBrightColorsCallback);
        }
        if (!this.mAutoTracker.isAdded("controls")) {
            this.mDeviceControlsController.setCallback(this.mDeviceControlsCallback);
        }
        if (!this.mAutoTracker.isAdded("wallet")) {
            initWalletController();
        }
        String str = this.mSafetySpec;
        if (str != null) {
            if (!this.mAutoTracker.isAdded(str)) {
                initSafetyTile();
            }
            this.mSafetyController.addCallback(this.mSafetyCallback);
        }
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            if (!this.mAutoTracker.isAdded(this.mAutoAddSettingList.get(i).mSpec)) {
                this.mAutoAddSettingList.get(i).setListening(true);
            }
        }
    }

    public void stopListening() {
        this.mHotspotController.removeCallback(this.mHotspotCallback);
        this.mDataSaverController.removeCallback(this.mDataSaverListener);
        this.mManagedProfileController.removeCallback(this.mProfileCallback);
        if (ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
        }
        if (this.mIsReduceBrightColorsAvailable) {
            this.mReduceBrightColorsController.removeCallback(this.mReduceBrightColorsCallback);
        }
        this.mCastController.removeCallback(this.mCastCallback);
        this.mDeviceControlsController.removeCallback();
        if (this.mSafetySpec != null) {
            this.mSafetyController.removeCallback(this.mSafetyCallback);
        }
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            this.mAutoAddSettingList.get(i).setListening(false);
        }
    }

    public final void populateSettingsList() {
        try {
            for (String str : this.mContext.getResources().getStringArray(R$array.config_quickSettingsAutoAdd)) {
                String[] split = str.split(":");
                if (split.length == 2) {
                    this.mAutoAddSettingList.add(new AutoAddSetting(this.mSecureSettings, this.mHandler, split[0], this.mCurrentUser.getIdentifier(), split[1]));
                } else {
                    Log.w("AutoTileManager", "Malformed item in array: " + str);
                }
            }
        } catch (Resources.NotFoundException unused) {
            Log.w("AutoTileManager", "Missing config resource");
        }
    }

    /* renamed from: changeUser */
    public void lambda$changeUser$0(UserHandle userHandle) {
        if (!this.mInitialized) {
            throw new IllegalStateException("AutoTileManager not initialized");
        } else if (!Thread.currentThread().equals(this.mHandler.getLooper().getThread())) {
            this.mHandler.post(new AutoTileManager$$ExternalSyntheticLambda0(this, userHandle));
        } else if (userHandle.getIdentifier() != this.mCurrentUser.getIdentifier()) {
            stopListening();
            this.mCurrentUser = userHandle;
            int size = this.mAutoAddSettingList.size();
            for (int i = 0; i < size; i++) {
                this.mAutoAddSettingList.get(i).setUserId(userHandle.getIdentifier());
            }
            this.mAutoTracker.changeUser(userHandle);
            startControllersAndSettingsListeners();
        }
    }

    public void unmarkTileAsAutoAdded(String str) {
        this.mAutoTracker.setTileRemoved(str);
    }

    public final void initWalletController() {
        Integer walletPosition;
        if (!this.mAutoTracker.isAdded("wallet") && (walletPosition = this.mWalletController.getWalletPosition()) != null) {
            this.mHost.addTile("wallet", walletPosition.intValue());
            this.mAutoTracker.setTileAdded("wallet");
        }
    }

    public final void initSafetyTile() {
        String str = this.mSafetySpec;
        if (str != null && !this.mAutoTracker.isAdded(str)) {
            this.mHost.addTile(CustomTile.getComponentFromSpec(this.mSafetySpec), true);
            this.mAutoTracker.setTileAdded(this.mSafetySpec);
        }
    }

    @VisibleForTesting
    public SettingObserver getSecureSettingForKey(String str) {
        Iterator<AutoAddSetting> it = this.mAutoAddSettingList.iterator();
        while (it.hasNext()) {
            SettingObserver next = it.next();
            if (Objects.equals(str, next.getKey())) {
                return next;
            }
        }
        return null;
    }

    public class AutoAddSetting extends SettingObserver {
        public final String mSpec;

        public AutoAddSetting(SecureSettings secureSettings, Handler handler, String str, int i, String str2) {
            super(secureSettings, handler, str, i);
            this.mSpec = str2;
        }

        public void handleValueChanged(int i, boolean z) {
            if (AutoTileManager.this.mAutoTracker.isAdded(this.mSpec)) {
                AutoTileManager.this.mHandler.post(new AutoTileManager$AutoAddSetting$$ExternalSyntheticLambda0(this));
            } else if (i != 0) {
                if (this.mSpec.startsWith("custom(")) {
                    AutoTileManager.this.mHost.addTile(CustomTile.getComponentFromSpec(this.mSpec), true);
                } else {
                    AutoTileManager.this.mHost.addTile(this.mSpec);
                }
                AutoTileManager.this.mAutoTracker.setTileAdded(this.mSpec);
                AutoTileManager.this.mHandler.post(new AutoTileManager$AutoAddSetting$$ExternalSyntheticLambda1(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$handleValueChanged$0() {
            setListening(false);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$handleValueChanged$1() {
            setListening(false);
        }
    }
}
