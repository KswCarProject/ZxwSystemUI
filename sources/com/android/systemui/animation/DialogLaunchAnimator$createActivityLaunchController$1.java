package com.android.systemui.animation;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DialogLaunchAnimator.kt */
public final class DialogLaunchAnimator$createActivityLaunchController$1 implements ActivityLaunchAnimator.Controller {
    public final /* synthetic */ ActivityLaunchAnimator.Controller $$delegate_0;
    public final /* synthetic */ AnimatedDialog $animatedDialog;
    public final /* synthetic */ ActivityLaunchAnimator.Controller $controller;
    public final /* synthetic */ Dialog $dialog;
    public final boolean isDialogLaunch = true;

    @NotNull
    public LaunchAnimator.State createAnimatorState() {
        return this.$$delegate_0.createAnimatorState();
    }

    @NotNull
    public ViewGroup getLaunchContainer() {
        return this.$$delegate_0.getLaunchContainer();
    }

    @Nullable
    public View getOpeningWindowSyncView() {
        return this.$$delegate_0.getOpeningWindowSyncView();
    }

    public void onLaunchAnimationProgress(@NotNull LaunchAnimator.State state, float f, float f2) {
        this.$$delegate_0.onLaunchAnimationProgress(state, f, f2);
    }

    public void setLaunchContainer(@NotNull ViewGroup viewGroup) {
        this.$$delegate_0.setLaunchContainer(viewGroup);
    }

    public DialogLaunchAnimator$createActivityLaunchController$1(ActivityLaunchAnimator.Controller controller, Dialog dialog, AnimatedDialog animatedDialog) {
        this.$controller = controller;
        this.$dialog = dialog;
        this.$animatedDialog = animatedDialog;
        this.$$delegate_0 = controller;
    }

    public boolean isDialogLaunch() {
        return this.isDialogLaunch;
    }

    public void onIntentStarted(boolean z) {
        this.$controller.onIntentStarted(z);
        if (!z) {
            this.$dialog.dismiss();
        }
    }

    public void onLaunchAnimationCancelled() {
        this.$controller.onLaunchAnimationCancelled();
        enableDialogDismiss();
        this.$dialog.dismiss();
    }

    public void onLaunchAnimationStart(boolean z) {
        this.$controller.onLaunchAnimationStart(z);
        disableDialogDismiss();
        AnimatedDialog animatedDialog = this.$animatedDialog;
        animatedDialog.setTouchSurface(animatedDialog.prepareForStackDismiss());
        this.$dialog.getWindow().clearFlags(2);
    }

    public void onLaunchAnimationEnd(boolean z) {
        this.$controller.onLaunchAnimationEnd(z);
        this.$dialog.hide();
        enableDialogDismiss();
        this.$dialog.dismiss();
    }

    public final void disableDialogDismiss() {
        this.$dialog.setDismissOverride(DialogLaunchAnimator$createActivityLaunchController$1$disableDialogDismiss$1.INSTANCE);
    }

    public final void enableDialogDismiss() {
        this.$dialog.setDismissOverride(new DialogLaunchAnimator$createActivityLaunchController$1$enableDialogDismiss$1(this.$animatedDialog));
    }
}
