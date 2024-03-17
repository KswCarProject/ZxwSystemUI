package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.SpringAnimation;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* compiled from: PhysicsAnimator.kt */
public /* synthetic */ class PhysicsAnimator$startInternal$2 extends FunctionReferenceImpl implements Function0<Unit> {
    public PhysicsAnimator$startInternal$2(Object obj) {
        super(0, obj, SpringAnimation.class, "start", "start()V", 0);
    }

    public final void invoke() {
        ((SpringAnimation) this.receiver).start();
    }
}
