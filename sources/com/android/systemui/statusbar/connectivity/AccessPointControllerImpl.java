package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IndentingPrintWriter;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.settingslib.wifi.WifiTracker;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class AccessPointControllerImpl implements AccessPointController, WifiPickerTracker.WifiPickerTrackerCallback, LifecycleOwner {
    public static final boolean DEBUG = Log.isLoggable("AccessPointController", 3);
    public static final int[] ICONS = WifiIcons.WIFI_FULL_ICONS;
    public final ArrayList<AccessPointController.AccessPointCallback> mCallbacks = new ArrayList<>();
    public final WifiEntry.ConnectCallback mConnectCallback = new WifiEntry.ConnectCallback() {
        public void onConnectResult(int i) {
            if (i == 0) {
                if (AccessPointControllerImpl.DEBUG) {
                    Log.d("AccessPointController", "connect success");
                }
            } else if (AccessPointControllerImpl.DEBUG) {
                Log.d("AccessPointController", "connect failure reason=" + i);
            }
        }
    };
    public int mCurrentUser;
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    public final Executor mMainExecutor;
    public final UserManager mUserManager;
    public final UserTracker mUserTracker;
    public WifiPickerTracker mWifiPickerTracker;
    public WifiPickerTrackerFactory mWifiPickerTrackerFactory;

    public void onNumSavedNetworksChanged() {
    }

    public void onNumSavedSubscriptionsChanged() {
    }

    public AccessPointControllerImpl(UserManager userManager, UserTracker userTracker, Executor executor, WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        this.mUserManager = userManager;
        this.mUserTracker = userTracker;
        this.mCurrentUser = userTracker.getUserId();
        this.mMainExecutor = executor;
        this.mWifiPickerTrackerFactory = wifiPickerTrackerFactory;
        executor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public void init() {
        if (this.mWifiPickerTracker == null) {
            this.mWifiPickerTracker = this.mWifiPickerTrackerFactory.create(getLifecycle(), this);
        }
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$finalize$1() {
        this.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    public void finalize() throws Throwable {
        this.mMainExecutor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda0(this));
        super.finalize();
    }

    public boolean canConfigWifi() {
        if (!this.mWifiPickerTrackerFactory.isSupported()) {
            return false;
        }
        return !this.mUserManager.hasUserRestriction("no_config_wifi", new UserHandle(this.mCurrentUser));
    }

    public boolean canConfigMobileData() {
        return !this.mUserManager.hasUserRestriction("no_config_mobile_networks", UserHandle.of(this.mCurrentUser)) && this.mUserTracker.getUserInfo().isAdmin();
    }

    public void onUserSwitched(int i) {
        this.mCurrentUser = i;
    }

    public void addAccessPointCallback(AccessPointController.AccessPointCallback accessPointCallback) {
        if (accessPointCallback != null && !this.mCallbacks.contains(accessPointCallback)) {
            if (DEBUG) {
                Log.d("AccessPointController", "addCallback " + accessPointCallback);
            }
            this.mCallbacks.add(accessPointCallback);
            if (this.mCallbacks.size() == 1) {
                this.mMainExecutor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda3(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addAccessPointCallback$2() {
        this.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
    }

    public void removeAccessPointCallback(AccessPointController.AccessPointCallback accessPointCallback) {
        if (accessPointCallback != null) {
            if (DEBUG) {
                Log.d("AccessPointController", "removeCallback " + accessPointCallback);
            }
            this.mCallbacks.remove(accessPointCallback);
            if (this.mCallbacks.isEmpty()) {
                this.mMainExecutor.execute(new AccessPointControllerImpl$$ExternalSyntheticLambda2(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAccessPointCallback$3() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public void scanForAccessPoints() {
        WifiPickerTracker wifiPickerTracker = this.mWifiPickerTracker;
        if (wifiPickerTracker == null) {
            fireAccessPointsCallback(Collections.emptyList());
            return;
        }
        List<WifiEntry> wifiEntries = wifiPickerTracker.getWifiEntries();
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry != null) {
            wifiEntries.add(0, connectedWifiEntry);
        }
        fireAccessPointsCallback(wifiEntries);
    }

    public MergedCarrierEntry getMergedCarrierEntry() {
        WifiPickerTracker wifiPickerTracker = this.mWifiPickerTracker;
        if (wifiPickerTracker != null) {
            return wifiPickerTracker.getMergedCarrierEntry();
        }
        fireAccessPointsCallback(Collections.emptyList());
        return null;
    }

    public final void fireAccessPointsCallback(List<WifiEntry> list) {
        Iterator<AccessPointController.AccessPointCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onAccessPointsChanged(list);
        }
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.println("AccessPointControllerImpl:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Callbacks: " + Arrays.toString(this.mCallbacks.toArray()));
        indentingPrintWriter.println("WifiPickerTracker: " + this.mWifiPickerTracker.toString());
        if (this.mWifiPickerTracker != null && !this.mCallbacks.isEmpty()) {
            indentingPrintWriter.println("Connected: " + this.mWifiPickerTracker.getConnectedWifiEntry());
            indentingPrintWriter.println("Other wifi entries: " + Arrays.toString(this.mWifiPickerTracker.getWifiEntries().toArray()));
        } else if (this.mWifiPickerTracker != null) {
            indentingPrintWriter.println("WifiPickerTracker not started, cannot get reliable entries");
        }
        indentingPrintWriter.decreaseIndent();
    }

    public void onWifiStateChanged() {
        scanForAccessPoints();
    }

    public void onWifiEntriesChanged() {
        scanForAccessPoints();
    }

    public static class WifiPickerTrackerFactory {
        public final Clock mClock = new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        public final ConnectivityManager mConnectivityManager;
        public final Context mContext;
        public final Handler mMainHandler;
        public final WifiManager mWifiManager;
        public final Handler mWorkerHandler;

        /* JADX WARNING: type inference failed for: r0v0, types: [java.time.Clock, com.android.systemui.statusbar.connectivity.AccessPointControllerImpl$WifiPickerTrackerFactory$1] */
        public WifiPickerTrackerFactory(Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2) {
            this.mContext = context;
            this.mWifiManager = wifiManager;
            this.mConnectivityManager = connectivityManager;
            this.mMainHandler = handler;
            this.mWorkerHandler = handler2;
        }

        public final boolean isSupported() {
            return this.mWifiManager != null;
        }

        public WifiPickerTracker create(Lifecycle lifecycle, WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
            WifiManager wifiManager = this.mWifiManager;
            if (wifiManager == null) {
                return null;
            }
            return new WifiPickerTracker(lifecycle, this.mContext, wifiManager, this.mConnectivityManager, this.mMainHandler, this.mWorkerHandler, this.mClock, WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS, 10000, wifiPickerTrackerCallback);
        }
    }
}
