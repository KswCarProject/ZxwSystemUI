package com.android.keyguard;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnfoldTransition.kt */
public final class KeyguardUnfoldTransition$filterNever$1 extends Lambda implements Function0<Boolean> {
    public static final KeyguardUnfoldTransition$filterNever$1 INSTANCE = new KeyguardUnfoldTransition$filterNever$1();

    public KeyguardUnfoldTransition$filterNever$1() {
        super(0);
    }

    @NotNull
    public final Boolean invoke() {
        return Boolean.TRUE;
    }
}
