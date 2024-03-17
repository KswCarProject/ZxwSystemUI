package com.android.systemui.monet;

import java.util.Comparator;
import java.util.Map;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValues((Double) ((Map.Entry) t2).getValue(), (Double) ((Map.Entry) t).getValue());
    }
}
