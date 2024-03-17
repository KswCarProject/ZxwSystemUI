package com.android.wm.shell.pip.tv;

import com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class TvPipKeepClearAlgorithm$sweepLineFindEarliestGap$$inlined$sortBy$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValues(Integer.valueOf(-((TvPipKeepClearAlgorithm.SweepLineEvent) t).getPos()), Integer.valueOf(-((TvPipKeepClearAlgorithm.SweepLineEvent) t2).getPos()));
    }
}
