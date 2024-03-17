package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorSchemeTransition.kt */
public final class ColorSchemeTransition$colorSeamless$1 extends Lambda implements Function1<ColorScheme, Integer> {
    public final /* synthetic */ ColorSchemeTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ColorSchemeTransition$colorSeamless$1(ColorSchemeTransition colorSchemeTransition) {
        super(1);
        this.this$0 = colorSchemeTransition;
    }

    @NotNull
    public final Integer invoke(@NotNull ColorScheme colorScheme) {
        int i;
        if ((this.this$0.context.getResources().getConfiguration().uiMode & 48) == 32) {
            i = colorScheme.getAccent1().get(2).intValue();
        } else {
            i = colorScheme.getAccent1().get(3).intValue();
        }
        return Integer.valueOf(i);
    }
}
