package com.android.systemui.settings;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import java.util.List;

/* compiled from: UserTrackerImpl.kt */
public final class UserTrackerImpl$handleSwitchUser$$inlined$notifySubscribers$1 implements Runnable {
    public final /* synthetic */ Context $ctx$inlined;
    public final /* synthetic */ DataItem $it;
    public final /* synthetic */ int $newUser$inlined;
    public final /* synthetic */ List $profiles$inlined;

    public UserTrackerImpl$handleSwitchUser$$inlined$notifySubscribers$1(DataItem dataItem, int i, Context context, List list) {
        this.$it = dataItem;
        this.$newUser$inlined = i;
        this.$ctx$inlined = context;
        this.$profiles$inlined = list;
    }

    public final void run() {
        UserTracker.Callback callback = (UserTracker.Callback) this.$it.getCallback().get();
        if (callback != null) {
            callback.onUserChanged(this.$newUser$inlined, this.$ctx$inlined);
            callback.onProfilesChanged(this.$profiles$inlined);
        }
    }
}
