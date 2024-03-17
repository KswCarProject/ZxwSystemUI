package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;

/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$currentUserTracker$1 extends CurrentUserTracker {
    public final int startingUser;
    public final /* synthetic */ ControlsFavoritingActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlsFavoritingActivity$currentUserTracker$1(ControlsFavoritingActivity controlsFavoritingActivity, BroadcastDispatcher broadcastDispatcher) {
        super(broadcastDispatcher);
        this.this$0 = controlsFavoritingActivity;
        this.startingUser = controlsFavoritingActivity.controller.getCurrentUserId();
    }

    public void onUserSwitched(int i) {
        if (i != this.startingUser) {
            stopTracking();
            this.this$0.finish();
        }
    }
}
