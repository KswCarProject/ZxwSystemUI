package com.android.systemui.controls.ui;

import com.android.systemui.R$color;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: RenderInfo.kt */
public final class RenderInfoKt$deviceColorMap$1 extends Lambda implements Function1<Integer, Pair<? extends Integer, ? extends Integer>> {
    public static final RenderInfoKt$deviceColorMap$1 INSTANCE = new RenderInfoKt$deviceColorMap$1();

    public RenderInfoKt$deviceColorMap$1() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    @NotNull
    public final Pair<Integer, Integer> invoke(int i) {
        return new Pair<>(Integer.valueOf(R$color.control_foreground), Integer.valueOf(R$color.control_enabled_default_background));
    }
}