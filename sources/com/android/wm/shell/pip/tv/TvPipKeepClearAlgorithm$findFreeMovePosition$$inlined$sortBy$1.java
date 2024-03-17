package com.android.wm.shell.pip.tv;

import android.graphics.Rect;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class TvPipKeepClearAlgorithm$findFreeMovePosition$$inlined$sortBy$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValues(Integer.valueOf(-((Rect) t).left), Integer.valueOf(-((Rect) t2).left));
    }
}
