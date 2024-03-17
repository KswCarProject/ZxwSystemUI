package com.android.systemui.decor;

import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

/* compiled from: DecorProvider.kt */
public final class DecorProviderKt {
    @NotNull
    public static final Pair<List<DecorProvider>, List<DecorProvider>> partitionAlignedBound(@NotNull List<? extends DecorProvider> list, int i) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object next : list) {
            if (((DecorProvider) next).getAlignedBounds().contains(Integer.valueOf(i))) {
                arrayList.add(next);
            } else {
                arrayList2.add(next);
            }
        }
        return new Pair<>(arrayList, arrayList2);
    }
}
