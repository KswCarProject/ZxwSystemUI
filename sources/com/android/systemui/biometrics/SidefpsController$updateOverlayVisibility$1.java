package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: SidefpsController.kt */
public final class SidefpsController$updateOverlayVisibility$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ View $view;
    public final /* synthetic */ SidefpsController this$0;

    public SidefpsController$updateOverlayVisibility$1(View view, SidefpsController sidefpsController) {
        this.$view = view;
        this.this$0 = sidefpsController;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        this.$view.setVisibility(8);
        this.this$0.overlayHideAnimator = null;
    }
}
