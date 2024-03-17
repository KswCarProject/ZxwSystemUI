package com.android.systemui.statusbar.phone.userswitcher;

import android.content.Intent;
import android.os.UserHandle;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.flags.Flags;
import com.android.systemui.user.UserSwitcherActivity;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarUserSwitcherController.kt */
public final class StatusBarUserSwitcherControllerImpl$onViewAttached$1 implements View.OnClickListener {
    public final /* synthetic */ StatusBarUserSwitcherControllerImpl this$0;

    public StatusBarUserSwitcherControllerImpl$onViewAttached$1(StatusBarUserSwitcherControllerImpl statusBarUserSwitcherControllerImpl) {
        this.this$0 = statusBarUserSwitcherControllerImpl;
    }

    public final void onClick(@NotNull View view) {
        if (!this.this$0.falsingManager.isFalseTap(1)) {
            if (this.this$0.featureFlags.isEnabled(Flags.FULL_SCREEN_USER_SWITCHER)) {
                Intent intent = new Intent(this.this$0.getContext(), UserSwitcherActivity.class);
                intent.addFlags(335544320);
                this.this$0.activityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) null, true, UserHandle.SYSTEM);
                return;
            }
            this.this$0.userSwitcherDialogController.showDialog(view);
        }
    }
}
