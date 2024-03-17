package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.FrameCallbackScheduler;
import com.android.wm.shell.animation.PhysicsAnimator;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: PhysicsAnimator.kt */
public final class PhysicsAnimator$startInternal$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ FloatPropertyCompat<? super T> $animatedProperty;
    public final /* synthetic */ float $currentValue;
    public final /* synthetic */ PhysicsAnimator.FlingConfig $flingConfig;
    public final /* synthetic */ T $target;
    public final /* synthetic */ PhysicsAnimator<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PhysicsAnimator$startInternal$1(PhysicsAnimator.FlingConfig flingConfig, PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T> floatPropertyCompat, T t, float f) {
        super(0);
        this.$flingConfig = flingConfig;
        this.this$0 = physicsAnimator;
        this.$animatedProperty = floatPropertyCompat;
        this.$target = t;
        this.$currentValue = f;
    }

    public final void invoke() {
        PhysicsAnimator.FlingConfig flingConfig = this.$flingConfig;
        float f = this.$currentValue;
        flingConfig.setMin(Math.min(f, flingConfig.getMin()));
        flingConfig.setMax(Math.max(f, flingConfig.getMax()));
        this.this$0.cancel(this.$animatedProperty);
        FlingAnimation access$getFlingAnimation = this.this$0.getFlingAnimation(this.$animatedProperty, this.$target);
        FrameCallbackScheduler access$getCustomScheduler$p = this.this$0.customScheduler;
        if (access$getCustomScheduler$p == null) {
            access$getCustomScheduler$p = access$getFlingAnimation.getScheduler();
        }
        access$getFlingAnimation.setScheduler(access$getCustomScheduler$p);
        this.$flingConfig.applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(access$getFlingAnimation);
        access$getFlingAnimation.start();
    }
}
