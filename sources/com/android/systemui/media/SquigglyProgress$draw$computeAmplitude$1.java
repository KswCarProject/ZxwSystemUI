package com.android.systemui.media;

import android.util.MathUtils;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SquigglyProgress.kt */
public final class SquigglyProgress$draw$computeAmplitude$1 extends Lambda implements Function2<Float, Float, Float> {
    public final /* synthetic */ float $transitionLength;
    public final /* synthetic */ float $waveEnd;
    public final /* synthetic */ SquigglyProgress this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SquigglyProgress$draw$computeAmplitude$1(SquigglyProgress squigglyProgress, float f, float f2) {
        super(2);
        this.this$0 = squigglyProgress;
        this.$waveEnd = f;
        this.$transitionLength = f2;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return invoke(((Number) obj).floatValue(), ((Number) obj2).floatValue());
    }

    @NotNull
    public final Float invoke(float f, float f2) {
        float access$getHeightFraction$p = f2 * this.this$0.heightFraction * this.this$0.getLineAmplitude();
        float f3 = this.$waveEnd;
        return Float.valueOf(access$getHeightFraction$p * MathUtils.lerpInvSat(f3, f3 - this.$transitionLength, f));
    }
}
