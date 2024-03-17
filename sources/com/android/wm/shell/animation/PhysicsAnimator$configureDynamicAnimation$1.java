package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;

/* compiled from: PhysicsAnimator.kt */
public final class PhysicsAnimator$configureDynamicAnimation$1 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ FloatPropertyCompat<? super T> $property;
    public final /* synthetic */ PhysicsAnimator<T> this$0;

    public PhysicsAnimator$configureDynamicAnimation$1(PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T> floatPropertyCompat) {
        this.this$0 = physicsAnimator;
        this.$property = floatPropertyCompat;
    }

    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
        int size = this.this$0.getInternalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell().size();
        for (int i = 0; i < size; i++) {
            this.this$0.getInternalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell().get(i).onInternalAnimationUpdate$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(this.$property, f, f2);
        }
    }
}
