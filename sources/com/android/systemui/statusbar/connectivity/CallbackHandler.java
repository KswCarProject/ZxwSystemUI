package com.android.systemui.statusbar.connectivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.connectivity.NetworkController;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallbackHandler extends Handler implements NetworkController.EmergencyListener, SignalCallback {
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public final ArrayList<NetworkController.EmergencyListener> mEmergencyListeners = new ArrayList<>();
    public final String[] mHistory = new String[64];
    public int mHistoryIndex;
    public String mLastCallback;
    public final ArrayList<SignalCallback> mSignalCallbacks = new ArrayList<>();

    @VisibleForTesting
    public CallbackHandler(Looper looper) {
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 0:
                Iterator<NetworkController.EmergencyListener> it = this.mEmergencyListeners.iterator();
                while (it.hasNext()) {
                    it.next().setEmergencyCallsOnly(message.arg1 != 0);
                }
                return;
            case 1:
                Iterator<SignalCallback> it2 = this.mSignalCallbacks.iterator();
                while (it2.hasNext()) {
                    it2.next().setSubs((List) message.obj);
                }
                return;
            case 2:
                Iterator<SignalCallback> it3 = this.mSignalCallbacks.iterator();
                while (it3.hasNext()) {
                    it3.next().setNoSims(message.arg1 != 0, message.arg2 != 0);
                }
                return;
            case 3:
                Iterator<SignalCallback> it4 = this.mSignalCallbacks.iterator();
                while (it4.hasNext()) {
                    it4.next().setEthernetIndicators((IconState) message.obj);
                }
                return;
            case 4:
                Iterator<SignalCallback> it5 = this.mSignalCallbacks.iterator();
                while (it5.hasNext()) {
                    it5.next().setIsAirplaneMode((IconState) message.obj);
                }
                return;
            case 5:
                Iterator<SignalCallback> it6 = this.mSignalCallbacks.iterator();
                while (it6.hasNext()) {
                    it6.next().setMobileDataEnabled(message.arg1 != 0);
                }
                return;
            case 6:
                if (message.arg1 != 0) {
                    this.mEmergencyListeners.add((NetworkController.EmergencyListener) message.obj);
                    return;
                } else {
                    this.mEmergencyListeners.remove((NetworkController.EmergencyListener) message.obj);
                    return;
                }
            case 7:
                if (message.arg1 != 0) {
                    this.mSignalCallbacks.add((SignalCallback) message.obj);
                    return;
                } else {
                    this.mSignalCallbacks.remove((SignalCallback) message.obj);
                    return;
                }
            default:
                return;
        }
    }

    public void setWifiIndicators(WifiIndicators wifiIndicators) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + wifiIndicators);
        post(new CallbackHandler$$ExternalSyntheticLambda1(this, wifiIndicators));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setWifiIndicators$0(WifiIndicators wifiIndicators) {
        Iterator<SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setWifiIndicators(wifiIndicators);
        }
    }

    public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + mobileDataIndicators);
        post(new CallbackHandler$$ExternalSyntheticLambda3(this, mobileDataIndicators));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setMobileDataIndicators$1(MobileDataIndicators mobileDataIndicators) {
        Iterator<SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setMobileDataIndicators(mobileDataIndicators);
        }
    }

    public void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
        String str = "setConnectivityStatus: " + "noDefaultNetwork=" + z + "," + "noValidatedNetwork=" + z2 + "," + "noNetworksAvailable=" + z3;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        post(new CallbackHandler$$ExternalSyntheticLambda0(this, z, z2, z3));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setConnectivityStatus$2(boolean z, boolean z2, boolean z3) {
        Iterator<SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setConnectivityStatus(z, z2, z3);
        }
    }

    public void setCallIndicator(IconState iconState, int i) {
        String str = "setCallIndicator: " + "statusIcon=" + iconState + "," + "subId=" + i;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        post(new CallbackHandler$$ExternalSyntheticLambda2(this, iconState, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setCallIndicator$3(IconState iconState, int i) {
        Iterator<SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setCallIndicator(iconState, i);
        }
    }

    public void setSubs(List<SubscriptionInfo> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("setSubs: ");
        sb.append("subs=");
        sb.append(list == null ? "" : list.toString());
        String sb2 = sb.toString();
        if (!sb2.equals(this.mLastCallback)) {
            this.mLastCallback = sb2;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + sb2 + ",");
        }
        obtainMessage(1, list).sendToTarget();
    }

    public void setNoSims(boolean z, boolean z2) {
        obtainMessage(2, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
    }

    public void setMobileDataEnabled(boolean z) {
        obtainMessage(5, z ? 1 : 0, 0).sendToTarget();
    }

    public void setEmergencyCallsOnly(boolean z) {
        obtainMessage(0, z ? 1 : 0, 0).sendToTarget();
    }

    public void setEthernetIndicators(IconState iconState) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + "setEthernetIndicators: " + "icon=" + iconState);
        obtainMessage(3, iconState).sendToTarget();
    }

    public void setIsAirplaneMode(IconState iconState) {
        String str = "setIsAirplaneMode: " + "icon=" + iconState;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        obtainMessage(4, iconState).sendToTarget();
    }

    public void setListening(SignalCallback signalCallback, boolean z) {
        obtainMessage(7, z ? 1 : 0, 0, signalCallback).sendToTarget();
    }

    public void recordLastCallback(String str) {
        String[] strArr = this.mHistory;
        int i = this.mHistoryIndex;
        strArr[i] = str;
        this.mHistoryIndex = (i + 1) % 64;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("  - CallbackHandler -----");
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 >= (this.mHistoryIndex + 64) - i) {
                printWriter.println("  Previous Callback(" + ((this.mHistoryIndex + 64) - i3) + "): " + this.mHistory[i3 & 63]);
            } else {
                return;
            }
        }
    }
}
