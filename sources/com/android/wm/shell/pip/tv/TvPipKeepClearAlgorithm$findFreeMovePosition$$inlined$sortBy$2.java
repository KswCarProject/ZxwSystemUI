package com.android.wm.shell.pip.tv;

import android.graphics.Rect;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class TvPipKeepClearAlgorithm$findFreeMovePosition$$inlined$sortBy$2<T> implements Comparator {
    public final /* synthetic */ Rect $pipAnchorBounds$inlined;
    public final /* synthetic */ TvPipKeepClearAlgorithm this$0;

    public TvPipKeepClearAlgorithm$findFreeMovePosition$$inlined$sortBy$2(TvPipKeepClearAlgorithm tvPipKeepClearAlgorithm, Rect rect) {
        this.this$0 = tvPipKeepClearAlgorithm;
        this.$pipAnchorBounds$inlined = rect;
    }

    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValues(Integer.valueOf(this.this$0.candidateCost((Rect) t, this.$pipAnchorBounds$inlined)), Integer.valueOf(this.this$0.candidateCost((Rect) t2, this.$pipAnchorBounds$inlined)));
    }
}
