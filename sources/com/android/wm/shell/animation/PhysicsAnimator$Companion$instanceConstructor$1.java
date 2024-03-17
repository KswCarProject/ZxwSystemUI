package com.android.wm.shell.animation;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: PhysicsAnimator.kt */
public /* synthetic */ class PhysicsAnimator$Companion$instanceConstructor$1 extends FunctionReferenceImpl implements Function1<Object, PhysicsAnimator<Object>> {
    public static final PhysicsAnimator$Companion$instanceConstructor$1 INSTANCE = new PhysicsAnimator$Companion$instanceConstructor$1();

    public PhysicsAnimator$Companion$instanceConstructor$1() {
        super(1, PhysicsAnimator.class, "<init>", "<init>(Ljava/lang/Object;)V", 0);
    }

    @NotNull
    public final PhysicsAnimator<Object> invoke(@NotNull Object obj) {
        return new PhysicsAnimator<>(obj, (DefaultConstructorMarker) null);
    }
}
