package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import java.util.ArrayList;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: PhysicsAnimator.kt */
public final class PhysicsAnimator$configureDynamicAnimation$2 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ DynamicAnimation<?> $anim;
    public final /* synthetic */ FloatPropertyCompat<? super T> $property;
    public final /* synthetic */ PhysicsAnimator<T> this$0;

    public PhysicsAnimator$configureDynamicAnimation$2(PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T> floatPropertyCompat, DynamicAnimation<?> dynamicAnimation) {
        this.this$0 = physicsAnimator;
        this.$property = floatPropertyCompat;
        this.$anim = dynamicAnimation;
    }

    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        ArrayList<PhysicsAnimator<T>.InternalListener> internalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell = this.this$0.getInternalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
        final FloatPropertyCompat<? super T> floatPropertyCompat = this.$property;
        final DynamicAnimation<?> dynamicAnimation2 = this.$anim;
        final boolean z2 = z;
        final float f3 = f;
        final float f4 = f2;
        CollectionsKt__MutableCollectionsKt.removeAll(internalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell, new Function1<PhysicsAnimator<T>.InternalListener, Boolean>() {
            @NotNull
            public final Boolean invoke(@NotNull PhysicsAnimator<T>.InternalListener internalListener) {
                return Boolean.valueOf(internalListener.onInternalAnimationEnd$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(floatPropertyCompat, z2, f3, f4, dynamicAnimation2 instanceof FlingAnimation));
            }
        });
        if (Intrinsics.areEqual(this.this$0.springAnimations.get(this.$property), (Object) this.$anim)) {
            this.this$0.springAnimations.remove(this.$property);
        }
        if (Intrinsics.areEqual(this.this$0.flingAnimations.get(this.$property), (Object) this.$anim)) {
            this.this$0.flingAnimations.remove(this.$property);
        }
    }
}
