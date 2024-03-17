package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$reload$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ ViewGroup $parent;
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$reload$1(ControlsUiControllerImpl controlsUiControllerImpl, ViewGroup viewGroup) {
        this.this$0 = controlsUiControllerImpl;
        this.$parent = viewGroup;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        this.this$0.controlViewsById.clear();
        this.this$0.controlsById.clear();
        ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
        ViewGroup viewGroup = this.$parent;
        Runnable access$getOnDismiss$p = controlsUiControllerImpl.onDismiss;
        Context context = null;
        if (access$getOnDismiss$p == null) {
            access$getOnDismiss$p = null;
        }
        Context access$getActivityContext$p = this.this$0.activityContext;
        if (access$getActivityContext$p != null) {
            context = access$getActivityContext$p;
        }
        controlsUiControllerImpl.show(viewGroup, access$getOnDismiss$p, context);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.$parent, "alpha", new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(new DecelerateInterpolator(1.0f));
        ofFloat.setDuration(200);
        ofFloat.start();
    }
}
