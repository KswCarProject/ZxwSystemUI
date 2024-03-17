package com.android.systemui.qs.user;

import android.content.DialogInterface;
import com.android.systemui.qs.QSUserSwitcherEvent;

/* compiled from: UserSwitchDialogController.kt */
public final class UserSwitchDialogController$showDialog$1$1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ UserSwitchDialogController this$0;

    public UserSwitchDialogController$showDialog$1$1(UserSwitchDialogController userSwitchDialogController) {
        this.this$0 = userSwitchDialogController;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.this$0.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_DETAIL_CLOSE);
    }
}
