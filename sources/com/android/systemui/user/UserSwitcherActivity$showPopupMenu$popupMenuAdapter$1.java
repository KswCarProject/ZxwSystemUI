package com.android.systemui.user;

import com.android.systemui.statusbar.policy.UserSwitcherController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1 extends Lambda implements Function1<UserSwitcherController.UserRecord, String> {
    public final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1(UserSwitcherActivity userSwitcherActivity) {
        super(1);
        this.this$0 = userSwitcherActivity;
    }

    @NotNull
    public final String invoke(@NotNull UserSwitcherController.UserRecord userRecord) {
        return this.this$0.adapter.getName(this.this$0, userRecord);
    }
}
