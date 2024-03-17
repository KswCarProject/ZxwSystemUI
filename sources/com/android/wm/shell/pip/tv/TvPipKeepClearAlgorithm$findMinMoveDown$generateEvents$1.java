package com.android.wm.shell.pip.tv;

import android.graphics.Rect;
import com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: TvPipKeepClearAlgorithm.kt */
public final class TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1 extends Lambda implements Function1<Boolean, Function1<? super Rect, ? extends Unit>> {
    public final /* synthetic */ List<TvPipKeepClearAlgorithm.SweepLineEvent> $events;
    public final /* synthetic */ Rect $pipBounds;
    public final /* synthetic */ TvPipKeepClearAlgorithm this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1(TvPipKeepClearAlgorithm tvPipKeepClearAlgorithm, Rect rect, List<TvPipKeepClearAlgorithm.SweepLineEvent> list) {
        super(1);
        this.this$0 = tvPipKeepClearAlgorithm;
        this.$pipBounds = rect;
        this.$events = list;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Boolean) obj).booleanValue());
    }

    @NotNull
    public final Function1<Rect, Unit> invoke(final boolean z) {
        final TvPipKeepClearAlgorithm tvPipKeepClearAlgorithm = this.this$0;
        final Rect rect = this.$pipBounds;
        final List<TvPipKeepClearAlgorithm.SweepLineEvent> list = this.$events;
        return new Function1<Rect, Unit>() {
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((Rect) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(@NotNull Rect rect) {
                if (tvPipKeepClearAlgorithm.intersectsX(rect, rect)) {
                    list.add(new TvPipKeepClearAlgorithm.SweepLineEvent(true, -rect.top, z, false, 8, (DefaultConstructorMarker) null));
                    list.add(new TvPipKeepClearAlgorithm.SweepLineEvent(false, -rect.bottom, z, false, 8, (DefaultConstructorMarker) null));
                }
            }
        };
    }
}
