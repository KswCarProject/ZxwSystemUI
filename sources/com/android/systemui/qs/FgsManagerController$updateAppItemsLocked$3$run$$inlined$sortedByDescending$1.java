package com.android.systemui.qs;

import com.android.systemui.qs.FgsManagerController;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class FgsManagerController$updateAppItemsLocked$3$run$$inlined$sortedByDescending$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(((FgsManagerController.RunningApp) t2).getTimeStarted()), Long.valueOf(((FgsManagerController.RunningApp) t).getTimeStarted()));
    }
}
