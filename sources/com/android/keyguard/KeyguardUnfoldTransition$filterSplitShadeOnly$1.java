package com.android.keyguard;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnfoldTransition.kt */
public final class KeyguardUnfoldTransition$filterSplitShadeOnly$1 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ KeyguardUnfoldTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardUnfoldTransition$filterSplitShadeOnly$1(KeyguardUnfoldTransition keyguardUnfoldTransition) {
        super(0);
        this.this$0 = keyguardUnfoldTransition;
    }

    @NotNull
    public final Boolean invoke() {
        return Boolean.valueOf(!this.this$0.getStatusViewCentered());
    }
}
