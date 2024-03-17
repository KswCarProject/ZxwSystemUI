package com.android.launcher3.util;

public interface FlagOp {
    public static final FlagOp NO_OP = new FlagOp$$ExternalSyntheticLambda0();

    static /* synthetic */ int lambda$static$0(int i) {
        return i;
    }

    int apply(int i);

    /* access modifiers changed from: private */
    /* synthetic */ int lambda$addFlag$1(int i, int i2) {
        return apply(i2) | i;
    }

    FlagOp addFlag(int i) {
        return new FlagOp$$ExternalSyntheticLambda1(this, i);
    }

    /* access modifiers changed from: private */
    /* synthetic */ int lambda$removeFlag$2(int i, int i2) {
        return apply(i2) & (~i);
    }

    FlagOp removeFlag(int i) {
        return new FlagOp$$ExternalSyntheticLambda2(this, i);
    }

    FlagOp setFlag(int i, boolean z) {
        return z ? addFlag(i) : removeFlag(i);
    }
}
