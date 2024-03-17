package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.Html;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.wifi.WifiStatusTracker;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import java.io.PrintWriter;

public class WifiSignalController extends SignalController<WifiState, SignalIcon$IconGroup> {
    public final Handler mBgHandler;
    public final SignalIcon$MobileIconGroup mCarrierMergedWifiIconGroup = TelephonyIcons.CARRIER_MERGED_WIFI;
    public final SignalIcon$IconGroup mDefaultWifiIconGroup;
    public final boolean mHasMobileDataFeature;
    public final SignalIcon$IconGroup mUnmergedWifiIconGroup = WifiIcons.UNMERGED_WIFI;
    public final SignalIcon$IconGroup mWifi4IconGroup;
    public final SignalIcon$IconGroup mWifi5IconGroup;
    public final SignalIcon$IconGroup mWifi6IconGroup;
    public final WifiManager mWifiManager;
    public final WifiStatusTracker mWifiTracker;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public WifiSignalController(Context context, boolean z, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, WifiManager wifiManager, WifiStatusTrackerFactory wifiStatusTrackerFactory, Handler handler) {
        super("WifiSignalController", context, 1, callbackHandler, networkControllerImpl);
        WifiManager wifiManager2 = wifiManager;
        Handler handler2 = handler;
        this.mBgHandler = handler2;
        this.mWifiManager = wifiManager2;
        WifiStatusTracker createTracker = wifiStatusTrackerFactory.createTracker(new WifiSignalController$$ExternalSyntheticLambda1(this), handler2);
        this.mWifiTracker = createTracker;
        createTracker.setListening(true);
        this.mHasMobileDataFeature = z;
        if (wifiManager2 != null) {
            wifiManager2.registerTrafficStateCallback(context.getMainExecutor(), new WifiTrafficStateCallback());
        }
        int[][] iArr = WifiIcons.WIFI_SIGNAL_STRENGTH;
        int[][] iArr2 = WifiIcons.QS_WIFI_SIGNAL_STRENGTH;
        int[] iArr3 = AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH;
        int i = AccessibilityContentDescriptions.WIFI_NO_CONNECTION;
        SignalIcon$IconGroup signalIcon$IconGroup = new SignalIcon$IconGroup("Wi-Fi Icons", iArr, iArr2, iArr3, 17302912, 17302912, 17302912, 17302912, i);
        this.mDefaultWifiIconGroup = signalIcon$IconGroup;
        int[] iArr4 = iArr3;
        int i2 = i;
        this.mWifi4IconGroup = new SignalIcon$IconGroup("Wi-Fi 4 Icons", WifiIcons.WIFI_4_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_4_SIGNAL_STRENGTH, iArr4, 17302912, 17302912, 17302912, 17302912, i2);
        this.mWifi5IconGroup = new SignalIcon$IconGroup("Wi-Fi 5 Icons", WifiIcons.WIFI_5_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_5_SIGNAL_STRENGTH, iArr4, 17302912, 17302912, 17302912, 17302912, i2);
        this.mWifi6IconGroup = new SignalIcon$IconGroup("Wi-Fi 6 Icons", WifiIcons.WIFI_6_SIGNAL_STRENGTH, WifiIcons.QS_WIFI_6_SIGNAL_STRENGTH, iArr4, 17302912, 17302912, 17302912, 17302912, i2);
        ((WifiState) this.mLastState).iconGroup = signalIcon$IconGroup;
        ((WifiState) this.mCurrentState).iconGroup = signalIcon$IconGroup;
    }

    public WifiState cleanState() {
        return new WifiState();
    }

    public void refreshLocale() {
        this.mWifiTracker.refreshLocale();
    }

    public void notifyListeners(SignalCallback signalCallback) {
        T t = this.mCurrentState;
        if (!((WifiState) t).isCarrierMerged) {
            notifyListenersForNonCarrierWifi(signalCallback);
        } else if (((WifiState) t).isDefault || !this.mNetworkController.isRadioOn()) {
            notifyListenersForCarrierWifi(signalCallback);
        }
    }

    public final void notifyListenersForNonCarrierWifi(SignalCallback signalCallback) {
        int i;
        boolean z = this.mContext.getResources().getBoolean(R$bool.config_showWifiIndicatorWhenEnabled);
        T t = this.mCurrentState;
        boolean z2 = ((WifiState) t).enabled && ((((WifiState) t).connected && ((WifiState) t).inetCondition == 1) || !this.mHasMobileDataFeature || ((WifiState) t).isDefault || z);
        IconState iconState = null;
        String str = ((WifiState) t).connected ? ((WifiState) t).ssid : null;
        boolean z3 = z2 && ((WifiState) t).ssid != null;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            charSequence = charSequence + "," + this.mContext.getString(R$string.data_connection_no_internet);
        }
        IconState iconState2 = new IconState(z2, getCurrentIconId(), charSequence);
        if (((WifiState) this.mCurrentState).isDefault || (!this.mNetworkController.isRadioOn() && !this.mNetworkController.isEthernetDefault())) {
            boolean z4 = ((WifiState) this.mCurrentState).connected;
            if (this.mWifiTracker.isCaptivePortal) {
                i = R$drawable.ic_qs_wifi_disconnected;
            } else {
                i = getQsCurrentIconId();
            }
            iconState = new IconState(z4, i, charSequence);
        }
        IconState iconState3 = iconState;
        T t2 = this.mCurrentState;
        signalCallback.setWifiIndicators(new WifiIndicators(((WifiState) t2).enabled, iconState2, iconState3, z3 && ((WifiState) t2).activityIn, z3 && ((WifiState) t2).activityOut, str, ((WifiState) t2).isTransient, ((WifiState) t2).statusLabel));
    }

    public final void notifyListenersForCarrierWifi(SignalCallback signalCallback) {
        int i;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = this.mCarrierMergedWifiIconGroup;
        String charSequence = getTextIfExists(getContentDescription()).toString();
        CharSequence textIfExists = getTextIfExists(signalIcon$MobileIconGroup.dataContentDescription);
        String obj = Html.fromHtml(textIfExists.toString(), 0).toString();
        if (((WifiState) this.mCurrentState).inetCondition == 0) {
            obj = this.mContext.getString(R$string.data_connection_no_internet);
        }
        String str = obj;
        T t = this.mCurrentState;
        boolean z = ((WifiState) t).enabled && ((WifiState) t).connected && ((WifiState) t).isDefault;
        IconState iconState = new IconState(z, getCurrentIconIdForCarrierWifi(), charSequence);
        int i2 = z ? signalIcon$MobileIconGroup.dataType : 0;
        IconState iconState2 = null;
        if (z) {
            i = signalIcon$MobileIconGroup.dataType;
            iconState2 = new IconState(((WifiState) this.mCurrentState).connected, getQsCurrentIconIdForCarrierWifi(), charSequence);
        } else {
            i = 0;
        }
        String networkNameForCarrierWiFi = this.mNetworkController.getNetworkNameForCarrierWiFi(((WifiState) this.mCurrentState).subId);
        T t2 = this.mCurrentState;
        signalCallback.setMobileDataIndicators(new MobileDataIndicators(iconState, iconState2, i2, i, ((WifiState) t2).activityIn, ((WifiState) t2).activityOut, 0, str, textIfExists, networkNameForCarrierWiFi, ((WifiState) t2).subId, false, true));
    }

    public final int getCurrentIconIdForCarrierWifi() {
        int i = ((WifiState) this.mCurrentState).level;
        boolean z = true;
        int maxSignalLevel = this.mWifiManager.getMaxSignalLevel() + 1;
        T t = this.mCurrentState;
        if (((WifiState) t).inetCondition != 0) {
            z = false;
        }
        if (((WifiState) t).connected) {
            return SignalDrawable.getState(i, maxSignalLevel, z);
        }
        if (((WifiState) t).enabled) {
            return SignalDrawable.getEmptyState(maxSignalLevel);
        }
        return 0;
    }

    public final int getQsCurrentIconIdForCarrierWifi() {
        return getCurrentIconIdForCarrierWifi();
    }

    public final void updateIconGroup() {
        T t = this.mCurrentState;
        if (((WifiState) t).wifiStandard == 4) {
            ((WifiState) t).iconGroup = this.mWifi4IconGroup;
        } else if (((WifiState) t).wifiStandard == 5) {
            ((WifiState) t).iconGroup = this.mWifi5IconGroup;
        } else if (((WifiState) t).wifiStandard == 6) {
            ((WifiState) t).iconGroup = this.mWifi6IconGroup;
        } else {
            ((WifiState) t).iconGroup = this.mDefaultWifiIconGroup;
        }
    }

    public void fetchInitialState() {
        doInBackground(new WifiSignalController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchInitialState$0() {
        this.mWifiTracker.fetchInitialState();
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    public void handleBroadcast(Intent intent) {
        doInBackground(new WifiSignalController$$ExternalSyntheticLambda0(this, intent));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleBroadcast$1(Intent intent) {
        this.mWifiTracker.handleBroadcast(intent);
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    public final void handleStatusUpdated() {
        doInBackground(new WifiSignalController$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleStatusUpdated$2() {
        copyWifiStates();
        notifyListenersIfNecessary();
    }

    public final void doInBackground(Runnable runnable) {
        if (Thread.currentThread() != this.mBgHandler.getLooper().getThread()) {
            this.mBgHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public final void copyWifiStates() {
        Preconditions.checkState(this.mBgHandler.getLooper().isCurrentThread());
        T t = this.mCurrentState;
        WifiStatusTracker wifiStatusTracker = this.mWifiTracker;
        ((WifiState) t).enabled = wifiStatusTracker.enabled;
        ((WifiState) t).isDefault = wifiStatusTracker.isDefaultNetwork;
        ((WifiState) t).connected = wifiStatusTracker.connected;
        ((WifiState) t).ssid = wifiStatusTracker.ssid;
        ((WifiState) t).rssi = wifiStatusTracker.rssi;
        int i = ((WifiState) t).level;
        int i2 = wifiStatusTracker.level;
        boolean z = i != i2;
        ((WifiState) t).level = i2;
        ((WifiState) t).statusLabel = wifiStatusTracker.statusLabel;
        ((WifiState) t).isCarrierMerged = wifiStatusTracker.isCarrierMerged;
        ((WifiState) t).subId = wifiStatusTracker.subId;
        ((WifiState) t).wifiStandard = wifiStatusTracker.wifiStandard;
        updateIconGroup();
        if (z) {
            this.mNetworkController.notifyWifiLevelChange(((WifiState) this.mCurrentState).level);
        }
    }

    public boolean isCarrierMergedWifi(int i) {
        T t = this.mCurrentState;
        return ((WifiState) t).isDefault && ((WifiState) t).isCarrierMerged && ((WifiState) t).subId == i;
    }

    @VisibleForTesting
    public void setActivity(int i) {
        T t = this.mCurrentState;
        boolean z = false;
        ((WifiState) t).activityIn = i == 3 || i == 1;
        WifiState wifiState = (WifiState) t;
        if (i == 3 || i == 2) {
            z = true;
        }
        wifiState.activityOut = z;
        notifyListenersIfNecessary();
    }

    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        this.mWifiTracker.dump(printWriter);
        dumpTableData(printWriter);
    }

    public class WifiTrafficStateCallback implements WifiManager.TrafficStateCallback {
        public WifiTrafficStateCallback() {
        }

        public void onStateChanged(int i) {
            WifiSignalController.this.setActivity(i);
        }
    }
}
