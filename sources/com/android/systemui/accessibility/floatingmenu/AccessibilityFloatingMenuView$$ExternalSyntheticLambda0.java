package com.android.systemui.accessibility.floatingmenu;

import android.animation.ValueAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AccessibilityFloatingMenuView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AccessibilityFloatingMenuView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ AccessibilityFloatingMenuView$$ExternalSyntheticLambda0(AccessibilityFloatingMenuView accessibilityFloatingMenuView, int i, int i2) {
        this.f$0 = accessibilityFloatingMenuView;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$snapToLocation$5(this.f$1, this.f$2, valueAnimator);
    }
}
