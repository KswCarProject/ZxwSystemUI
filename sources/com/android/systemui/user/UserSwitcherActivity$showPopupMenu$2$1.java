package com.android.systemui.user;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.user.UserSwitcherActivity;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$showPopupMenu$2$1 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ Ref$ObjectRef<UserSwitcherActivity.ItemAdapter> $popupMenuAdapter;
    public final /* synthetic */ UserSwitcherPopupMenu $this_apply;
    public final /* synthetic */ UserSwitcherActivity this$0;

    public UserSwitcherActivity$showPopupMenu$2$1(UserSwitcherActivity userSwitcherActivity, Ref$ObjectRef<UserSwitcherActivity.ItemAdapter> ref$ObjectRef, UserSwitcherPopupMenu userSwitcherPopupMenu) {
        this.this$0 = userSwitcherActivity;
        this.$popupMenuAdapter = ref$ObjectRef;
        this.$this_apply = userSwitcherPopupMenu;
    }

    public final void onItemClick(@NotNull AdapterView<?> adapterView, @NotNull View view, int i, long j) {
        if (!this.this$0.falsingManager.isFalseTap(1) && view.isEnabled()) {
            UserSwitcherController.UserRecord userRecord = (UserSwitcherController.UserRecord) ((UserSwitcherActivity.ItemAdapter) this.$popupMenuAdapter.element).getItem(i - 1);
            if (Intrinsics.areEqual((Object) userRecord, (Object) this.this$0.manageUserRecord)) {
                this.this$0.startActivity(new Intent().setAction("android.settings.USER_SETTINGS"));
            } else {
                this.this$0.adapter.onUserListItemClicked(userRecord);
            }
            this.$this_apply.dismiss();
            this.this$0.popupMenu = null;
            if (!userRecord.isAddUser) {
                this.this$0.finish();
            }
        }
    }
}
