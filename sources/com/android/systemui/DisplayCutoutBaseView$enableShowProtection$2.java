package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.NotNull;

/* compiled from: DisplayCutoutBaseView.kt */
public final class DisplayCutoutBaseView$enableShowProtection$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ DisplayCutoutBaseView this$0;

    public DisplayCutoutBaseView$enableShowProtection$2(DisplayCutoutBaseView displayCutoutBaseView) {
        this.this$0 = displayCutoutBaseView;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        this.this$0.cameraProtectionAnimator = null;
        DisplayCutoutBaseView displayCutoutBaseView = this.this$0;
        if (!displayCutoutBaseView.showProtection) {
            displayCutoutBaseView.requestLayout();
        }
    }
}
