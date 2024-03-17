package com.android.systemui.controls.ui;

import com.android.systemui.R$drawable;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: RenderInfo.kt */
public final class RenderInfoKt$deviceIconMap$1 extends Lambda implements Function1<Integer, Integer> {
    public static final RenderInfoKt$deviceIconMap$1 INSTANCE = new RenderInfoKt$deviceIconMap$1();

    public RenderInfoKt$deviceIconMap$1() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    @NotNull
    public final Integer invoke(int i) {
        return Integer.valueOf(R$drawable.ic_device_unknown);
    }
}
