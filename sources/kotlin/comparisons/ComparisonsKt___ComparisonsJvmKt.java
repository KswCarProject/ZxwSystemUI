package kotlin.comparisons;

import org.jetbrains.annotations.NotNull;

/* compiled from: _ComparisonsJvm.kt */
public class ComparisonsKt___ComparisonsJvmKt extends ComparisonsKt__ComparisonsKt {
    public static final float maxOf(float f, @NotNull float... fArr) {
        int length = fArr.length;
        int i = 0;
        while (i < length) {
            float f2 = fArr[i];
            i++;
            f = Math.max(f, f2);
        }
        return f;
    }

    public static final int minOf(int i, @NotNull int... iArr) {
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length) {
            int i3 = iArr[i2];
            i2++;
            i = Math.min(i, i3);
        }
        return i;
    }
}
