package com.android.systemui.controls.management;

import com.android.systemui.controls.ControlsServiceInfo;
import java.util.Comparator;

/* compiled from: Comparisons.kt */
public final class AppAdapter$callback$1$onServicesUpdated$1$run$$inlined$compareBy$1<T> implements Comparator {
    public final /* synthetic */ Comparator $comparator;

    public AppAdapter$callback$1$onServicesUpdated$1$run$$inlined$compareBy$1(Comparator comparator) {
        this.$comparator = comparator;
    }

    public final int compare(T t, T t2) {
        return this.$comparator.compare(((ControlsServiceInfo) t).loadLabel(), ((ControlsServiceInfo) t2).loadLabel());
    }
}
