package com.android.wifitrackerlib;

import com.android.wifitrackerlib.SavedNetworkTracker;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ SavedNetworkTracker.SavedNetworkTrackerCallback f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda8(SavedNetworkTracker.SavedNetworkTrackerCallback savedNetworkTrackerCallback) {
        this.f$0 = savedNetworkTrackerCallback;
    }

    public final void run() {
        this.f$0.onSubscriptionWifiEntriesChanged();
    }
}
