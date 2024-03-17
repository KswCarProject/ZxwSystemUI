package com.android.systemui.monet;

import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class CoreSpec {
    @NotNull
    public final TonalSpec a1;
    @NotNull
    public final TonalSpec a2;
    @NotNull
    public final TonalSpec a3;
    @NotNull
    public final TonalSpec n1;
    @NotNull
    public final TonalSpec n2;

    public CoreSpec(@NotNull TonalSpec tonalSpec, @NotNull TonalSpec tonalSpec2, @NotNull TonalSpec tonalSpec3, @NotNull TonalSpec tonalSpec4, @NotNull TonalSpec tonalSpec5) {
        this.a1 = tonalSpec;
        this.a2 = tonalSpec2;
        this.a3 = tonalSpec3;
        this.n1 = tonalSpec4;
        this.n2 = tonalSpec5;
    }

    @NotNull
    public final TonalSpec getA1() {
        return this.a1;
    }

    @NotNull
    public final TonalSpec getA2() {
        return this.a2;
    }

    @NotNull
    public final TonalSpec getA3() {
        return this.a3;
    }

    @NotNull
    public final TonalSpec getN1() {
        return this.n1;
    }

    @NotNull
    public final TonalSpec getN2() {
        return this.n2;
    }
}
