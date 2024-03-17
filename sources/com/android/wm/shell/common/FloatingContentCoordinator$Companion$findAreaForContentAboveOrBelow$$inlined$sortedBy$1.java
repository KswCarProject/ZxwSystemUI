package com.android.wm.shell.common;

import android.graphics.Rect;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1<T> implements Comparator {
    public final /* synthetic */ boolean $findAbove$inlined;

    public FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1(boolean z) {
        this.$findAbove$inlined = z;
    }

    public final int compare(T t, T t2) {
        boolean z = this.$findAbove$inlined;
        int i = ((Rect) t).top;
        if (z) {
            i = -i;
        }
        Rect rect = (Rect) t2;
        return ComparisonsKt__ComparisonsKt.compareValues(Integer.valueOf(i), Integer.valueOf(this.$findAbove$inlined ? -rect.top : rect.top));
    }
}