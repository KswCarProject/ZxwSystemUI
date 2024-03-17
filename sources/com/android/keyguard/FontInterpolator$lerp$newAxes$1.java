package com.android.keyguard;

import android.util.MathUtils;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FontInterpolator.kt */
public final class FontInterpolator$lerp$newAxes$1 extends Lambda implements Function3<String, Float, Float, Float> {
    public final /* synthetic */ float $progress;
    public final /* synthetic */ FontInterpolator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FontInterpolator$lerp$newAxes$1(FontInterpolator fontInterpolator, float f) {
        super(3);
        this.this$0 = fontInterpolator;
        this.$progress = f;
    }

    @NotNull
    public final Float invoke(@NotNull String str, @Nullable Float f, @Nullable Float f2) {
        float f3;
        float f4;
        float f5;
        if (Intrinsics.areEqual((Object) str, (Object) "wght")) {
            FontInterpolator fontInterpolator = this.this$0;
            float f6 = 400.0f;
            if (f == null) {
                f5 = 400.0f;
            } else {
                f5 = f.floatValue();
            }
            if (f2 != null) {
                f6 = f2.floatValue();
            }
            f3 = fontInterpolator.adjustWeight(MathUtils.lerp(f5, f6, this.$progress));
        } else if (Intrinsics.areEqual((Object) str, (Object) "ital")) {
            FontInterpolator fontInterpolator2 = this.this$0;
            float f7 = 0.0f;
            if (f == null) {
                f4 = 0.0f;
            } else {
                f4 = f.floatValue();
            }
            if (f2 != null) {
                f7 = f2.floatValue();
            }
            f3 = fontInterpolator2.adjustItalic(MathUtils.lerp(f4, f7, this.$progress));
        } else {
            if ((f == null || f2 == null) ? false : true) {
                f3 = MathUtils.lerp(f.floatValue(), f2.floatValue(), this.$progress);
            } else {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unable to interpolate due to unknown default axes value : ", str).toString());
            }
        }
        return Float.valueOf(f3);
    }
}
