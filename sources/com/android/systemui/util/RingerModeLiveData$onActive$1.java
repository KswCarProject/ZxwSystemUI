package com.android.systemui.util;

/* compiled from: RingerModeTrackerImpl.kt */
public final class RingerModeLiveData$onActive$1 implements Runnable {
    public final /* synthetic */ RingerModeLiveData this$0;

    public RingerModeLiveData$onActive$1(RingerModeLiveData ringerModeLiveData) {
        this.this$0 = ringerModeLiveData;
    }

    public final void run() {
        RingerModeLiveData ringerModeLiveData = this.this$0;
        ringerModeLiveData.postValue(ringerModeLiveData.getter.invoke());
    }
}
