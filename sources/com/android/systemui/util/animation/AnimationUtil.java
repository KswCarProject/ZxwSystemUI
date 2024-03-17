package com.android.systemui.util.animation;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.math.MathKt__MathJVMKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: AnimationUtil.kt */
public final class AnimationUtil {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* compiled from: AnimationUtil.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final long getMsForFrames(int i) {
            if (i >= 0) {
                return MathKt__MathJVMKt.roundToLong((((float) i) * 1000.0f) / 60.0f);
            }
            throw new IllegalArgumentException("numFrames must be >= 0");
        }

        public final long getFrames(int i) {
            return getMsForFrames(i);
        }
    }
}
