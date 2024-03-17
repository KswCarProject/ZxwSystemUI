package com.android.wm.shell.pip.tv;

import android.graphics.Rect;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: TvPipKeepClearAlgorithm.kt */
public final class TvPipKeepClearAlgorithm$findFreeMovePosition$2 extends Lambda implements Function1<Rect, Boolean> {
    public final /* synthetic */ int $minLeft;
    public final /* synthetic */ TvPipKeepClearAlgorithm this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TvPipKeepClearAlgorithm$findFreeMovePosition$2(TvPipKeepClearAlgorithm tvPipKeepClearAlgorithm, int i) {
        super(1);
        this.this$0 = tvPipKeepClearAlgorithm;
        this.$minLeft = i;
    }

    @NotNull
    public final Boolean invoke(@NotNull Rect rect) {
        return Boolean.valueOf(rect.left - this.this$0.getPipAreaPadding() > this.$minLeft);
    }
}
