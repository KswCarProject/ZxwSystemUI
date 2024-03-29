package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: LightSourceDrawable.kt */
public final class LightSourceDrawable$active$1$2 extends AnimatorListenerAdapter {
    public boolean cancelled;
    public final /* synthetic */ LightSourceDrawable this$0;

    public LightSourceDrawable$active$1$2(LightSourceDrawable lightSourceDrawable) {
        this.this$0 = lightSourceDrawable;
    }

    public void onAnimationCancel(@Nullable Animator animator) {
        this.cancelled = true;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        if (!this.cancelled) {
            this.this$0.rippleData.setProgress(0.0f);
            this.this$0.rippleData.setAlpha(0.0f);
            this.this$0.rippleAnimation = null;
            this.this$0.invalidateSelf();
        }
    }
}
