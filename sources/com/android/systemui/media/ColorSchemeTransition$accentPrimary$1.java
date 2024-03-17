package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorSchemeTransition.kt */
public /* synthetic */ class ColorSchemeTransition$accentPrimary$1 extends FunctionReferenceImpl implements Function1<ColorScheme, Integer> {
    public static final ColorSchemeTransition$accentPrimary$1 INSTANCE = new ColorSchemeTransition$accentPrimary$1();

    public ColorSchemeTransition$accentPrimary$1() {
        super(1, MediaColorSchemesKt.class, "accentPrimaryFromScheme", "accentPrimaryFromScheme(Lcom/android/systemui/monet/ColorScheme;)I", 1);
    }

    @NotNull
    public final Integer invoke(@NotNull ColorScheme colorScheme) {
        return Integer.valueOf(MediaColorSchemesKt.accentPrimaryFromScheme(colorScheme));
    }
}
