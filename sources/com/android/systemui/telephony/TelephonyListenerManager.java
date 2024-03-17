package com.android.systemui.telephony;

import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import java.util.concurrent.Executor;

public class TelephonyListenerManager {
    public final Executor mExecutor;
    public boolean mListening = false;
    public final TelephonyCallback mTelephonyCallback;
    public final TelephonyManager mTelephonyManager;

    public TelephonyListenerManager(TelephonyManager telephonyManager, Executor executor, TelephonyCallback telephonyCallback) {
        this.mTelephonyManager = telephonyManager;
        this.mExecutor = executor;
        this.mTelephonyCallback = telephonyCallback;
    }

    public void addActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mTelephonyCallback.addActiveDataSubscriptionIdListener(activeDataSubscriptionIdListener);
        updateListening();
    }

    public void removeActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mTelephonyCallback.removeActiveDataSubscriptionIdListener(activeDataSubscriptionIdListener);
        updateListening();
    }

    public void addCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        this.mTelephonyCallback.addCallStateListener(callStateListener);
        updateListening();
    }

    public void addServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mTelephonyCallback.addServiceStateListener(serviceStateListener);
        updateListening();
    }

    public void removeServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mTelephonyCallback.removeServiceStateListener(serviceStateListener);
        updateListening();
    }

    public final void updateListening() {
        if (!this.mListening && this.mTelephonyCallback.hasAnyListeners()) {
            this.mListening = true;
            this.mTelephonyManager.registerTelephonyCallback(this.mExecutor, this.mTelephonyCallback);
        } else if (this.mListening && !this.mTelephonyCallback.hasAnyListeners()) {
            this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
            this.mListening = false;
        }
    }
}
