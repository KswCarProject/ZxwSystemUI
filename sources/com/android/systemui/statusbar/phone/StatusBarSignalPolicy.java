package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.util.ArraySet;
import android.util.Log;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class StatusBarSignalPolicy implements SignalCallback, SecurityController.SecurityControllerCallback, TunerService.Tunable {
    public static final boolean DEBUG = Log.isLoggable("StatusBarSignalPolicy", 3);
    public boolean mActivityEnabled;
    public ArrayList<CallIndicatorIconState> mCallIndicatorStates = new ArrayList<>();
    public final CarrierConfigTracker mCarrierConfigTracker;
    public final Context mContext;
    public final FeatureFlags mFeatureFlags;
    public final Handler mHandler = Handler.getMain();
    public boolean mHideAirplane;
    public boolean mHideEthernet;
    public boolean mHideMobile;
    public boolean mHideWifi;
    public final StatusBarIconController mIconController;
    public boolean mInitialized;
    public boolean mIsAirplaneMode = false;
    public boolean mIsWifiEnabled = false;
    public ArrayList<MobileIconState> mMobileStates = new ArrayList<>();
    public final NetworkController mNetworkController;
    public final SecurityController mSecurityController;
    public final String mSlotAirplane;
    public final String mSlotCallStrength;
    public final String mSlotEthernet;
    public final String mSlotMobile;
    public final String mSlotNoCalling;
    public final String mSlotVpn;
    public final String mSlotWifi;
    public final TunerService mTunerService;
    public WifiIconState mWifiIconState = new WifiIconState();

    public void setMobileDataEnabled(boolean z) {
    }

    public void setNoSims(boolean z, boolean z2) {
    }

    public StatusBarSignalPolicy(Context context, StatusBarIconController statusBarIconController, CarrierConfigTracker carrierConfigTracker, NetworkController networkController, SecurityController securityController, TunerService tunerService, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mIconController = statusBarIconController;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mNetworkController = networkController;
        this.mSecurityController = securityController;
        this.mTunerService = tunerService;
        this.mFeatureFlags = featureFlags;
        this.mSlotAirplane = context.getString(17041555);
        this.mSlotMobile = context.getString(17041573);
        this.mSlotWifi = context.getString(17041589);
        this.mSlotEthernet = context.getString(17041566);
        this.mSlotVpn = context.getString(17041588);
        this.mSlotNoCalling = context.getString(17041576);
        this.mSlotCallStrength = context.getString(17041559);
        this.mActivityEnabled = context.getResources().getBoolean(R$bool.config_showActivity);
    }

    public void init() {
        if (!this.mInitialized) {
            this.mInitialized = true;
            this.mTunerService.addTunable(this, "icon_blacklist");
            this.mNetworkController.addCallback(this);
            this.mSecurityController.addCallback(this);
        }
    }

    public final void updateVpn() {
        boolean isVpnEnabled = this.mSecurityController.isVpnEnabled();
        this.mIconController.setIcon(this.mSlotVpn, currentVpnIconId(this.mSecurityController.isVpnBranded()), this.mContext.getResources().getString(R$string.accessibility_vpn_on));
        this.mIconController.setIconVisibility(this.mSlotVpn, isVpnEnabled);
    }

    public final int currentVpnIconId(boolean z) {
        return z ? R$drawable.stat_sys_branded_vpn : R$drawable.stat_sys_vpn_ic;
    }

    public void onStateChanged() {
        this.mHandler.post(new StatusBarSignalPolicy$$ExternalSyntheticLambda0(this));
    }

    public void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(this.mContext, str2);
            boolean contains = iconHideList.contains(this.mSlotAirplane);
            boolean contains2 = iconHideList.contains(this.mSlotMobile);
            boolean contains3 = iconHideList.contains(this.mSlotWifi);
            boolean contains4 = iconHideList.contains(this.mSlotEthernet);
            if (contains != this.mHideAirplane || contains2 != this.mHideMobile || contains4 != this.mHideEthernet || contains3 != this.mHideWifi) {
                this.mHideAirplane = contains;
                this.mHideMobile = contains2;
                this.mHideEthernet = contains4;
                this.mHideWifi = contains3;
                this.mNetworkController.removeCallback(this);
                this.mNetworkController.addCallback(this);
            }
        }
    }

    public void setWifiIndicators(WifiIndicators wifiIndicators) {
        boolean z;
        if (DEBUG) {
            Log.d("StatusBarSignalPolicy", "setWifiIndicators: " + wifiIndicators);
        }
        boolean z2 = false;
        boolean z3 = wifiIndicators.statusIcon.visible && !this.mHideWifi;
        boolean z4 = wifiIndicators.activityIn && this.mActivityEnabled && z3;
        boolean z5 = wifiIndicators.activityOut && this.mActivityEnabled && z3;
        this.mIsWifiEnabled = wifiIndicators.enabled;
        WifiIconState copy = this.mWifiIconState.copy();
        WifiIconState wifiIconState = this.mWifiIconState;
        boolean z6 = wifiIconState.noDefaultNetwork;
        if (z6 && wifiIconState.noNetworksAvailable && !this.mIsAirplaneMode) {
            copy.visible = true;
            copy.resId = R$drawable.ic_qs_no_internet_unavailable;
        } else if (!z6 || wifiIconState.noNetworksAvailable || ((z = this.mIsAirplaneMode) && (!z || !this.mIsWifiEnabled))) {
            copy.visible = z3;
            IconState iconState = wifiIndicators.statusIcon;
            copy.resId = iconState.icon;
            copy.activityIn = z4;
            copy.activityOut = z5;
            copy.contentDescription = iconState.contentDescription;
            MobileIconState firstMobileState = getFirstMobileState();
            if (!(firstMobileState == null || firstMobileState.typeId == 0)) {
                z2 = true;
            }
            copy.signalSpacerVisible = z2;
        } else {
            copy.visible = true;
            copy.resId = R$drawable.ic_qs_no_internet_available;
        }
        copy.slot = this.mSlotWifi;
        copy.airplaneSpacerVisible = this.mIsAirplaneMode;
        updateWifiIconWithState(copy);
        this.mWifiIconState = copy;
    }

    public final void updateShowWifiSignalSpacer(WifiIconState wifiIconState) {
        MobileIconState firstMobileState = getFirstMobileState();
        wifiIconState.signalSpacerVisible = (firstMobileState == null || firstMobileState.typeId == 0) ? false : true;
    }

    public final void updateWifiIconWithState(WifiIconState wifiIconState) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("WifiIconState: ");
            sb.append(wifiIconState);
            Log.d("StatusBarSignalPolicy", sb.toString() == null ? "" : wifiIconState.toString());
        }
        if (!wifiIconState.visible || wifiIconState.resId <= 0) {
            this.mIconController.setIconVisibility(this.mSlotWifi, false);
            return;
        }
        this.mIconController.setSignalIcon(this.mSlotWifi, wifiIconState);
        this.mIconController.setIconVisibility(this.mSlotWifi, true);
    }

    public void setCallIndicator(IconState iconState, int i) {
        if (DEBUG) {
            Log.d("StatusBarSignalPolicy", "setCallIndicator: statusIcon = " + iconState + ",subId = " + i);
        }
        CallIndicatorIconState noCallingState = getNoCallingState(i);
        if (noCallingState != null) {
            int i2 = iconState.icon;
            if (i2 == R$drawable.ic_qs_no_calling_sms) {
                noCallingState.isNoCalling = iconState.visible;
                noCallingState.noCallingDescription = iconState.contentDescription;
            } else {
                noCallingState.callStrengthResId = i2;
                noCallingState.callStrengthDescription = iconState.contentDescription;
            }
            if (this.mCarrierConfigTracker.getCallStrengthConfig(i)) {
                this.mIconController.setCallStrengthIcons(this.mSlotCallStrength, CallIndicatorIconState.copyStates(this.mCallIndicatorStates));
            } else {
                this.mIconController.removeIcon(this.mSlotCallStrength, i);
            }
            this.mIconController.setNoCallingIcons(this.mSlotNoCalling, CallIndicatorIconState.copyStates(this.mCallIndicatorStates));
        }
    }

    public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
        boolean z = DEBUG;
        if (z) {
            Log.d("StatusBarSignalPolicy", "setMobileDataIndicators: " + mobileDataIndicators);
        }
        MobileIconState state = getState(mobileDataIndicators.subId);
        if (state != null) {
            int i = mobileDataIndicators.statusType;
            int i2 = state.typeId;
            boolean z2 = true;
            boolean z3 = i != i2 && (i == 0 || i2 == 0);
            IconState iconState = mobileDataIndicators.statusIcon;
            state.visible = iconState.visible && !this.mHideMobile;
            state.strengthId = iconState.icon;
            state.typeId = i;
            state.contentDescription = iconState.contentDescription;
            state.typeContentDescription = mobileDataIndicators.typeContentDescription;
            state.showTriangle = mobileDataIndicators.showTriangle;
            state.roaming = mobileDataIndicators.roaming;
            state.activityIn = mobileDataIndicators.activityIn && this.mActivityEnabled;
            if (!mobileDataIndicators.activityOut || !this.mActivityEnabled) {
                z2 = false;
            }
            state.activityOut = z2;
            state.volteId = mobileDataIndicators.volteIcon;
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append("MobileIconStates: ");
                ArrayList<MobileIconState> arrayList = this.mMobileStates;
                sb.append(arrayList == null ? "" : arrayList.toString());
                Log.d("StatusBarSignalPolicy", sb.toString());
            }
            this.mIconController.setMobileIcons(this.mSlotMobile, MobileIconState.copyStates(this.mMobileStates));
            if (z3) {
                WifiIconState copy = this.mWifiIconState.copy();
                updateShowWifiSignalSpacer(copy);
                if (!Objects.equals(copy, this.mWifiIconState)) {
                    updateWifiIconWithState(copy);
                    this.mWifiIconState = copy;
                }
            }
        }
    }

    public final CallIndicatorIconState getNoCallingState(int i) {
        Iterator<CallIndicatorIconState> it = this.mCallIndicatorStates.iterator();
        while (it.hasNext()) {
            CallIndicatorIconState next = it.next();
            if (next.subId == i) {
                return next;
            }
        }
        Log.e("StatusBarSignalPolicy", "Unexpected subscription " + i);
        return null;
    }

    public final MobileIconState getState(int i) {
        Iterator<MobileIconState> it = this.mMobileStates.iterator();
        while (it.hasNext()) {
            MobileIconState next = it.next();
            if (next.subId == i) {
                return next;
            }
        }
        Log.e("StatusBarSignalPolicy", "Unexpected subscription " + i);
        return null;
    }

    public final MobileIconState getFirstMobileState() {
        if (this.mMobileStates.size() > 0) {
            return this.mMobileStates.get(0);
        }
        return null;
    }

    public void setSubs(List<SubscriptionInfo> list) {
        boolean z;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("setSubs: ");
            sb.append(list == null ? "" : list.toString());
            Log.d("StatusBarSignalPolicy", sb.toString());
        }
        if (!hasCorrectSubs(list)) {
            this.mIconController.removeAllIconsForSlot(this.mSlotMobile);
            this.mIconController.removeAllIconsForSlot(this.mSlotNoCalling);
            this.mIconController.removeAllIconsForSlot(this.mSlotCallStrength);
            this.mMobileStates.clear();
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.mCallIndicatorStates);
            this.mCallIndicatorStates.clear();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                this.mMobileStates.add(new MobileIconState(list.get(i).getSubscriptionId()));
                Iterator it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) it.next();
                    if (callIndicatorIconState.subId == list.get(i).getSubscriptionId()) {
                        this.mCallIndicatorStates.add(callIndicatorIconState);
                        z = false;
                        break;
                    }
                }
                if (z) {
                    this.mCallIndicatorStates.add(new CallIndicatorIconState(list.get(i).getSubscriptionId()));
                }
            }
        }
    }

    public final boolean hasCorrectSubs(List<SubscriptionInfo> list) {
        int size = list.size();
        if (size != this.mMobileStates.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.mMobileStates.get(i).subId != list.get(i).getSubscriptionId()) {
                return false;
            }
        }
        return true;
    }

    public void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
        if (this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            if (DEBUG) {
                Log.d("StatusBarSignalPolicy", "setConnectivityStatus: noDefaultNetwork = " + z + ",noValidatedNetwork = " + z2 + ",noNetworksAvailable = " + z3);
            }
            WifiIconState copy = this.mWifiIconState.copy();
            copy.noDefaultNetwork = z;
            copy.noValidatedNetwork = z2;
            copy.noNetworksAvailable = z3;
            copy.slot = this.mSlotWifi;
            boolean z4 = this.mIsAirplaneMode;
            copy.airplaneSpacerVisible = z4;
            if (z && z3 && !z4) {
                copy.visible = true;
                copy.resId = R$drawable.ic_qs_no_internet_unavailable;
            } else if (!z || z3 || (z4 && (!z4 || !this.mIsWifiEnabled))) {
                copy.visible = false;
                copy.resId = 0;
            } else {
                copy.visible = true;
                copy.resId = R$drawable.ic_qs_no_internet_available;
            }
            updateWifiIconWithState(copy);
            this.mWifiIconState = copy;
        }
    }

    public void setEthernetIndicators(IconState iconState) {
        if (iconState.visible) {
            boolean z = this.mHideEthernet;
        }
        int i = iconState.icon;
        String str = iconState.contentDescription;
        if (i > 0) {
            this.mIconController.setIcon(this.mSlotEthernet, i, str);
            this.mIconController.setIconVisibility(this.mSlotEthernet, true);
            return;
        }
        this.mIconController.setIconVisibility(this.mSlotEthernet, false);
    }

    public void setIsAirplaneMode(IconState iconState) {
        String str;
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("setIsAirplaneMode: icon = ");
            if (iconState == null) {
                str = "";
            } else {
                str = iconState.toString();
            }
            sb.append(str);
            Log.d("StatusBarSignalPolicy", sb.toString());
        }
        boolean z = iconState.visible && !this.mHideAirplane;
        this.mIsAirplaneMode = z;
        int i = iconState.icon;
        String str2 = iconState.contentDescription;
        if (!z || i <= 0) {
            this.mIconController.setIconVisibility(this.mSlotAirplane, false);
            return;
        }
        this.mIconController.setIcon(this.mSlotAirplane, i, str2);
        this.mIconController.setIconVisibility(this.mSlotAirplane, true);
    }

    public static class CallIndicatorIconState {
        public String callStrengthDescription;
        public int callStrengthResId;
        public boolean isNoCalling;
        public String noCallingDescription;
        public int noCallingResId;
        public int subId;

        public CallIndicatorIconState(int i) {
            this.subId = i;
            this.noCallingResId = R$drawable.ic_qs_no_calling_sms;
            this.callStrengthResId = TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[0];
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CallIndicatorIconState callIndicatorIconState = (CallIndicatorIconState) obj;
            if (this.isNoCalling == callIndicatorIconState.isNoCalling && this.noCallingResId == callIndicatorIconState.noCallingResId && this.callStrengthResId == callIndicatorIconState.callStrengthResId && this.subId == callIndicatorIconState.subId && this.noCallingDescription == callIndicatorIconState.noCallingDescription && this.callStrengthDescription == callIndicatorIconState.callStrengthDescription) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Boolean.valueOf(this.isNoCalling), Integer.valueOf(this.noCallingResId), Integer.valueOf(this.callStrengthResId), Integer.valueOf(this.subId), this.noCallingDescription, this.callStrengthDescription});
        }

        public final void copyTo(CallIndicatorIconState callIndicatorIconState) {
            callIndicatorIconState.isNoCalling = this.isNoCalling;
            callIndicatorIconState.noCallingResId = this.noCallingResId;
            callIndicatorIconState.callStrengthResId = this.callStrengthResId;
            callIndicatorIconState.subId = this.subId;
            callIndicatorIconState.noCallingDescription = this.noCallingDescription;
            callIndicatorIconState.callStrengthDescription = this.callStrengthDescription;
        }

        public static List<CallIndicatorIconState> copyStates(List<CallIndicatorIconState> list) {
            ArrayList arrayList = new ArrayList();
            for (CallIndicatorIconState next : list) {
                CallIndicatorIconState callIndicatorIconState = new CallIndicatorIconState(next.subId);
                next.copyTo(callIndicatorIconState);
                arrayList.add(callIndicatorIconState);
            }
            return arrayList;
        }
    }

    public static abstract class SignalIconState {
        public boolean activityIn;
        public boolean activityOut;
        public String contentDescription;
        public String slot;
        public boolean visible;

        public SignalIconState() {
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SignalIconState signalIconState = (SignalIconState) obj;
            if (this.visible == signalIconState.visible && this.activityOut == signalIconState.activityOut && this.activityIn == signalIconState.activityIn && Objects.equals(this.contentDescription, signalIconState.contentDescription) && Objects.equals(this.slot, signalIconState.slot)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Boolean.valueOf(this.visible), Boolean.valueOf(this.activityOut), this.slot});
        }

        public void copyTo(SignalIconState signalIconState) {
            signalIconState.visible = this.visible;
            signalIconState.activityIn = this.activityIn;
            signalIconState.activityOut = this.activityOut;
            signalIconState.slot = this.slot;
            signalIconState.contentDescription = this.contentDescription;
        }
    }

    public static class WifiIconState extends SignalIconState {
        public boolean airplaneSpacerVisible;
        public boolean noDefaultNetwork;
        public boolean noNetworksAvailable;
        public boolean noValidatedNetwork;
        public int resId;
        public boolean signalSpacerVisible;

        public WifiIconState() {
            super();
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            WifiIconState wifiIconState = (WifiIconState) obj;
            if (this.resId == wifiIconState.resId && this.airplaneSpacerVisible == wifiIconState.airplaneSpacerVisible && this.signalSpacerVisible == wifiIconState.signalSpacerVisible && this.noDefaultNetwork == wifiIconState.noDefaultNetwork && this.noValidatedNetwork == wifiIconState.noValidatedNetwork && this.noNetworksAvailable == wifiIconState.noNetworksAvailable) {
                return true;
            }
            return false;
        }

        public void copyTo(WifiIconState wifiIconState) {
            super.copyTo(wifiIconState);
            wifiIconState.resId = this.resId;
            wifiIconState.airplaneSpacerVisible = this.airplaneSpacerVisible;
            wifiIconState.signalSpacerVisible = this.signalSpacerVisible;
            wifiIconState.noDefaultNetwork = this.noDefaultNetwork;
            wifiIconState.noValidatedNetwork = this.noValidatedNetwork;
            wifiIconState.noNetworksAvailable = this.noNetworksAvailable;
        }

        public WifiIconState copy() {
            WifiIconState wifiIconState = new WifiIconState();
            copyTo(wifiIconState);
            return wifiIconState;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), Integer.valueOf(this.resId), Boolean.valueOf(this.airplaneSpacerVisible), Boolean.valueOf(this.signalSpacerVisible), Boolean.valueOf(this.noDefaultNetwork), Boolean.valueOf(this.noValidatedNetwork), Boolean.valueOf(this.noNetworksAvailable)});
        }

        public String toString() {
            return "WifiIconState(resId=" + this.resId + ", visible=" + this.visible + ")";
        }
    }

    public static class MobileIconState extends SignalIconState {
        public boolean needsLeadingPadding;
        public boolean roaming;
        public boolean showTriangle;
        public int strengthId;
        public int subId;
        public CharSequence typeContentDescription;
        public int typeId;
        public int volteId;

        public MobileIconState(int i) {
            super();
            this.subId = i;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
                return false;
            }
            MobileIconState mobileIconState = (MobileIconState) obj;
            if (this.subId == mobileIconState.subId && this.strengthId == mobileIconState.strengthId && this.typeId == mobileIconState.typeId && this.showTriangle == mobileIconState.showTriangle && this.roaming == mobileIconState.roaming && this.needsLeadingPadding == mobileIconState.needsLeadingPadding && Objects.equals(this.typeContentDescription, mobileIconState.typeContentDescription) && this.volteId == mobileIconState.volteId) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), Integer.valueOf(this.subId), Integer.valueOf(this.strengthId), Integer.valueOf(this.typeId), Boolean.valueOf(this.showTriangle), Boolean.valueOf(this.roaming), Boolean.valueOf(this.needsLeadingPadding), this.typeContentDescription});
        }

        public MobileIconState copy() {
            MobileIconState mobileIconState = new MobileIconState(this.subId);
            copyTo(mobileIconState);
            return mobileIconState;
        }

        public void copyTo(MobileIconState mobileIconState) {
            super.copyTo(mobileIconState);
            mobileIconState.subId = this.subId;
            mobileIconState.strengthId = this.strengthId;
            mobileIconState.typeId = this.typeId;
            mobileIconState.showTriangle = this.showTriangle;
            mobileIconState.roaming = this.roaming;
            mobileIconState.needsLeadingPadding = this.needsLeadingPadding;
            mobileIconState.typeContentDescription = this.typeContentDescription;
            mobileIconState.volteId = this.volteId;
        }

        public static List<MobileIconState> copyStates(List<MobileIconState> list) {
            ArrayList arrayList = new ArrayList();
            for (MobileIconState next : list) {
                MobileIconState mobileIconState = new MobileIconState(next.subId);
                next.copyTo(mobileIconState);
                arrayList.add(mobileIconState);
            }
            return arrayList;
        }

        public String toString() {
            return "MobileIconState(subId=" + this.subId + ", strengthId=" + this.strengthId + ", showTriangle=" + this.showTriangle + ", roaming=" + this.roaming + ", typeId=" + this.typeId + ", volteId=" + this.volteId + ", visible=" + this.visible + ")";
        }
    }
}
