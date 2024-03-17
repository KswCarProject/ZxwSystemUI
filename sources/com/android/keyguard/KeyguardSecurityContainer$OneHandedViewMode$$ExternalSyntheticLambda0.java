package com.android.keyguard;

import android.animation.ValueAnimator;
import android.view.animation.Interpolator;
import com.android.keyguard.KeyguardSecurityContainer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardSecurityContainer$OneHandedViewMode$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ KeyguardSecurityContainer.OneHandedViewMode f$0;
    public final /* synthetic */ Interpolator f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ Interpolator f$4;
    public final /* synthetic */ float f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ Interpolator f$7;
    public final /* synthetic */ int f$8;
    public final /* synthetic */ boolean f$9;

    public /* synthetic */ KeyguardSecurityContainer$OneHandedViewMode$$ExternalSyntheticLambda0(KeyguardSecurityContainer.OneHandedViewMode oneHandedViewMode, Interpolator interpolator, int i, boolean z, Interpolator interpolator2, float f, int i2, Interpolator interpolator3, int i3, boolean z2) {
        this.f$0 = oneHandedViewMode;
        this.f$1 = interpolator;
        this.f$2 = i;
        this.f$3 = z;
        this.f$4 = interpolator2;
        this.f$5 = f;
        this.f$6 = i2;
        this.f$7 = interpolator3;
        this.f$8 = i3;
        this.f$9 = z2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateSecurityViewLocation$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, valueAnimator);
    }
}
