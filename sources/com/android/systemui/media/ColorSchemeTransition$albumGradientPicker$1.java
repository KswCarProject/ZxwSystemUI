package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import com.android.systemui.util.ColorUtilKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorSchemeTransition.kt */
public final class ColorSchemeTransition$albumGradientPicker$1 extends Lambda implements Function1<ColorScheme, Integer> {
    public final /* synthetic */ Function1<ColorScheme, Integer> $inner;
    public final /* synthetic */ float $targetAlpha;
    public final /* synthetic */ ColorSchemeTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ColorSchemeTransition$albumGradientPicker$1(ColorSchemeTransition colorSchemeTransition, Function1<? super ColorScheme, Integer> function1, float f) {
        super(1);
        this.this$0 = colorSchemeTransition;
        this.$inner = function1;
        this.$targetAlpha = f;
    }

    @NotNull
    public final Integer invoke(@NotNull ColorScheme colorScheme) {
        return Integer.valueOf(this.this$0.isGradientEnabled ? ColorUtilKt.getColorWithAlpha(this.$inner.invoke(colorScheme).intValue(), this.$targetAlpha) : 0);
    }
}
