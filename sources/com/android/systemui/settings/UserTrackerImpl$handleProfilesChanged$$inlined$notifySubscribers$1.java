package com.android.systemui.settings;

import com.android.systemui.settings.UserTracker;
import java.util.List;

/* compiled from: UserTrackerImpl.kt */
public final class UserTrackerImpl$handleProfilesChanged$$inlined$notifySubscribers$1 implements Runnable {
    public final /* synthetic */ DataItem $it;
    public final /* synthetic */ List $profiles$inlined;

    public UserTrackerImpl$handleProfilesChanged$$inlined$notifySubscribers$1(DataItem dataItem, List list) {
        this.$it = dataItem;
        this.$profiles$inlined = list;
    }

    public final void run() {
        UserTracker.Callback callback = (UserTracker.Callback) this.$it.getCallback().get();
        if (callback != null) {
            callback.onProfilesChanged(this.$profiles$inlined);
        }
    }
}
