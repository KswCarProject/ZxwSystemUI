package com.android.systemui.statusbar.policy;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.PermissionChecker;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationControllerImpl extends BroadcastReceiver implements LocationController, AppOpsController.Callback {
    public final AppOpsController mAppOpsController;
    public boolean mAreActiveLocationRequests;
    public final Handler mBackgroundHandler;
    public final BootCompleteCache mBootCompleteCache;
    public final ContentObserver mContentObserver;
    public final Context mContext;
    public final DeviceConfigProxy mDeviceConfigProxy;
    public final H mHandler;
    public final PackageManager mPackageManager;
    public final SecureSettings mSecureSettings;
    public boolean mShouldDisplayAllAccesses = getAllAccessesSetting();
    public boolean mShowSystemAccessesFlag = getShowSystemFlag();
    public boolean mShowSystemAccessesSetting = getShowSystemSetting();
    public final UiEventLogger mUiEventLogger;
    public final UserTracker mUserTracker;

    public LocationControllerImpl(Context context, AppOpsController appOpsController, DeviceConfigProxy deviceConfigProxy, Looper looper, Handler handler, BroadcastDispatcher broadcastDispatcher, BootCompleteCache bootCompleteCache, UserTracker userTracker, PackageManager packageManager, UiEventLogger uiEventLogger, SecureSettings secureSettings) {
        this.mContext = context;
        this.mAppOpsController = appOpsController;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mBootCompleteCache = bootCompleteCache;
        H h = new H(looper);
        this.mHandler = h;
        this.mUserTracker = userTracker;
        this.mUiEventLogger = uiEventLogger;
        this.mSecureSettings = secureSettings;
        this.mBackgroundHandler = handler;
        this.mPackageManager = packageManager;
        AnonymousClass1 r4 = new ContentObserver(handler) {
            public void onChange(boolean z) {
                LocationControllerImpl locationControllerImpl = LocationControllerImpl.this;
                locationControllerImpl.mShowSystemAccessesSetting = locationControllerImpl.getShowSystemSetting();
            }
        };
        this.mContentObserver = r4;
        secureSettings.registerContentObserverForUser("locationShowSystemOps", (ContentObserver) r4, -1);
        Objects.requireNonNull(handler);
        deviceConfigProxy.addOnPropertiesChangedListener("privacy", new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), new LocationControllerImpl$$ExternalSyntheticLambda0(this));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.MODE_CHANGED");
        broadcastDispatcher.registerReceiverWithHandler(this, intentFilter, h, UserHandle.ALL);
        appOpsController.addCallback(new int[]{0, 1, 42}, this);
        handler.post(new LocationControllerImpl$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        this.mShouldDisplayAllAccesses = getAllAccessesSetting();
        this.mShowSystemAccessesFlag = getShowSystemSetting();
        updateActiveLocationRequests();
    }

    public void addCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(3, locationChangeCallback).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    public void removeCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(4, locationChangeCallback).sendToTarget();
    }

    public boolean setLocationEnabled(boolean z) {
        int userId = this.mUserTracker.getUserId();
        if (isUserLocationRestricted(userId)) {
            return false;
        }
        Utils.updateLocationEnabled(this.mContext, z, userId, 2);
        return true;
    }

    public boolean isLocationEnabled() {
        return this.mBootCompleteCache.isBootComplete() && ((LocationManager) this.mContext.getSystemService("location")).isLocationEnabledForUser(this.mUserTracker.getUserHandle());
    }

    public boolean isLocationActive() {
        return this.mAreActiveLocationRequests;
    }

    public final boolean isUserLocationRestricted(int i) {
        return ((UserManager) this.mContext.getSystemService("user")).hasUserRestriction("no_share_location", UserHandle.of(i));
    }

    public final boolean getAllAccessesSetting() {
        return this.mDeviceConfigProxy.getBoolean("privacy", "location_indicators_small_enabled", false);
    }

    public final boolean getShowSystemFlag() {
        return this.mDeviceConfigProxy.getBoolean("privacy", "location_indicators_show_system", false);
    }

    public final boolean getShowSystemSetting() {
        return this.mSecureSettings.getIntForUser("locationShowSystemOps", 0, -2) == 1;
    }

    public boolean areActiveHighPowerLocationRequests() {
        List<AppOpItem> activeAppOps = this.mAppOpsController.getActiveAppOps();
        int size = activeAppOps.size();
        for (int i = 0; i < size; i++) {
            if (activeAppOps.get(i).getCode() == 42) {
                return true;
            }
        }
        return false;
    }

    public void areActiveLocationRequests() {
        if (this.mShouldDisplayAllAccesses) {
            boolean z = this.mAreActiveLocationRequests;
            boolean z2 = this.mShowSystemAccessesFlag || this.mShowSystemAccessesSetting;
            List<AppOpItem> activeAppOps = this.mAppOpsController.getActiveAppOps();
            List<UserInfo> userProfiles = this.mUserTracker.getUserProfiles();
            int size = activeAppOps.size();
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            for (int i = 0; i < size; i++) {
                if (activeAppOps.get(i).getCode() == 1 || activeAppOps.get(i).getCode() == 0) {
                    boolean isSystemApp = isSystemApp(userProfiles, activeAppOps.get(i));
                    if (isSystemApp) {
                        z4 = true;
                    } else {
                        z5 = true;
                    }
                    z3 = z2 || z3 || !isSystemApp;
                }
            }
            boolean areActiveHighPowerLocationRequests = areActiveHighPowerLocationRequests();
            this.mAreActiveLocationRequests = z3;
            if (z3 != z) {
                this.mHandler.sendEmptyMessage(2);
            }
            if (z) {
                return;
            }
            if (areActiveHighPowerLocationRequests || z4 || z5) {
                if (areActiveHighPowerLocationRequests) {
                    this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_MONITOR_HIGH_POWER);
                }
                if (z4) {
                    this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_SYSTEM_APP);
                }
                if (z5) {
                    this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_NON_SYSTEM_APP);
                }
            }
        }
    }

    public final boolean isSystemApp(List<UserInfo> list, AppOpItem appOpItem) {
        String opToPermission = AppOpsManager.opToPermission(appOpItem.getCode());
        UserHandle userHandleForUid = UserHandle.getUserHandleForUid(appOpItem.getUid());
        int size = list.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            if (list.get(i).getUserHandle().equals(userHandleForUid)) {
                z = true;
            }
        }
        if (!z) {
            return true;
        }
        int permissionFlags = this.mPackageManager.getPermissionFlags(opToPermission, appOpItem.getPackageName(), userHandleForUid);
        if (PermissionChecker.checkPermissionForPreflight(this.mContext, opToPermission, -1, appOpItem.getUid(), appOpItem.getPackageName()) == 0) {
            if ((permissionFlags & 256) == 0) {
                return true;
            }
            return false;
        } else if ((permissionFlags & 512) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public final void updateActiveLocationRequests() {
        if (this.mShouldDisplayAllAccesses) {
            this.mBackgroundHandler.post(new LocationControllerImpl$$ExternalSyntheticLambda2(this));
            return;
        }
        boolean z = this.mAreActiveLocationRequests;
        boolean areActiveHighPowerLocationRequests = areActiveHighPowerLocationRequests();
        this.mAreActiveLocationRequests = areActiveHighPowerLocationRequests;
        if (areActiveHighPowerLocationRequests != z) {
            this.mHandler.sendEmptyMessage(2);
            if (this.mAreActiveLocationRequests) {
                this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_MONITOR_HIGH_POWER);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if ("android.location.MODE_CHANGED".equals(intent.getAction())) {
            this.mHandler.locationSettingsChanged();
        }
    }

    public void onActiveStateChanged(int i, int i2, String str, boolean z) {
        updateActiveLocationRequests();
    }

    public final class H extends Handler {
        public ArrayList<LocationController.LocationChangeCallback> mSettingsChangeCallbacks = new ArrayList<>();

        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                locationSettingsChanged();
            } else if (i == 2) {
                locationActiveChanged();
            } else if (i == 3) {
                this.mSettingsChangeCallbacks.add((LocationController.LocationChangeCallback) message.obj);
            } else if (i == 4) {
                this.mSettingsChangeCallbacks.remove((LocationController.LocationChangeCallback) message.obj);
            }
        }

        public final void locationActiveChanged() {
            com.android.systemui.util.Utils.safeForeach(this.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$locationActiveChanged$0(LocationController.LocationChangeCallback locationChangeCallback) {
            locationChangeCallback.onLocationActiveChanged(LocationControllerImpl.this.mAreActiveLocationRequests);
        }

        public final void locationSettingsChanged() {
            com.android.systemui.util.Utils.safeForeach(this.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda0(LocationControllerImpl.this.isLocationEnabled()));
        }
    }

    public enum LocationIndicatorEvent implements UiEventLogger.UiEventEnum {
        LOCATION_INDICATOR_MONITOR_HIGH_POWER(935),
        LOCATION_INDICATOR_SYSTEM_APP(936),
        LOCATION_INDICATOR_NON_SYSTEM_APP(937);
        
        private final int mId;

        /* access modifiers changed from: public */
        LocationIndicatorEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
