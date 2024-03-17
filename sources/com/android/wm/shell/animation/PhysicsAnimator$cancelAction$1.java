package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: PhysicsAnimator.kt */
public /* synthetic */ class PhysicsAnimator$cancelAction$1 extends FunctionReferenceImpl implements Function1<Set<? extends FloatPropertyCompat<? super T>>, Unit> {
    public PhysicsAnimator$cancelAction$1(Object obj) {
        super(1, obj, PhysicsAnimator.class, "cancelInternal", "cancelInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(Ljava/util/Set;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Set) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull Set<? extends FloatPropertyCompat<? super T>> set) {
        ((PhysicsAnimator) this.receiver).cancelInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(set);
    }
}
