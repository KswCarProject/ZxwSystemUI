package com.android.wm.shell.bubbles;

import android.graphics.PointF;
import android.view.View;
import com.android.wm.shell.animation.Interpolators;

/* compiled from: StackEducationView.kt */
public final class StackEducationView$show$1 implements Runnable {
    public final /* synthetic */ int $stackPadding;
    public final /* synthetic */ PointF $stackPosition;
    public final /* synthetic */ StackEducationView this$0;

    public StackEducationView$show$1(StackEducationView stackEducationView, int i, PointF pointF) {
        this.this$0 = stackEducationView;
        this.$stackPadding = i;
        this.$stackPosition = pointF;
    }

    public final void run() {
        this.this$0.requestFocus();
        View access$getView = this.this$0.getView();
        StackEducationView stackEducationView = this.this$0;
        int i = this.$stackPadding;
        PointF pointF = this.$stackPosition;
        if (access$getView.getResources().getConfiguration().getLayoutDirection() == 0) {
            access$getView.setPadding(stackEducationView.positioner.getBubbleSize() + i, access$getView.getPaddingTop(), access$getView.getPaddingRight(), access$getView.getPaddingBottom());
        } else {
            access$getView.setPadding(access$getView.getPaddingLeft(), access$getView.getPaddingTop(), stackEducationView.positioner.getBubbleSize() + i, access$getView.getPaddingBottom());
            if (stackEducationView.positioner.isLargeScreen() || stackEducationView.positioner.isLandscape()) {
                access$getView.setTranslationX((float) ((stackEducationView.positioner.getScreenRect().right - access$getView.getWidth()) - i));
            } else {
                access$getView.setTranslationX(0.0f);
            }
        }
        access$getView.setTranslationY((pointF.y + ((float) (stackEducationView.positioner.getBubbleSize() / 2))) - ((float) (access$getView.getHeight() / 2)));
        this.this$0.animate().setDuration(this.this$0.ANIMATE_DURATION).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
    }
}
