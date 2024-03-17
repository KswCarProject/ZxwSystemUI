package com.android.systemui.media.dialog;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GradientDrawable f$0;
    public final /* synthetic */ GradientDrawable f$1;

    public /* synthetic */ MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda3(GradientDrawable gradientDrawable, GradientDrawable gradientDrawable2) {
        this.f$0 = gradientDrawable;
        this.f$1 = gradientDrawable2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        MediaOutputBaseAdapter.MediaDeviceBaseViewHolder.lambda$animateCornerAndVolume$0(this.f$0, this.f$1, valueAnimator);
    }
}
