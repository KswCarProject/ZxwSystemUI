package com.android.systemui.user;

import android.view.View;
import com.android.systemui.statusbar.policy.UserSwitcherController;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$buildUserViews$2 implements View.OnClickListener {
    public final /* synthetic */ UserSwitcherController.UserRecord $item;
    public final /* synthetic */ UserSwitcherActivity this$0;

    public UserSwitcherActivity$buildUserViews$2(UserSwitcherActivity userSwitcherActivity, UserSwitcherController.UserRecord userRecord) {
        this.this$0 = userSwitcherActivity;
        this.$item = userRecord;
    }

    public final void onClick(View view) {
        if (!this.this$0.falsingManager.isFalseTap(1) && view.isEnabled()) {
            UserSwitcherController.UserRecord userRecord = this.$item;
            if (!userRecord.isCurrent || userRecord.isGuest) {
                this.this$0.adapter.onUserListItemClicked(this.$item);
            }
        }
    }
}
