package kotlin.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: MutableCollectionsJVM.kt */
public class CollectionsKt__MutableCollectionsJVMKt extends CollectionsKt__IteratorsKt {
    public static final <T extends Comparable<? super T>> void sort(@NotNull List<T> list) {
        if (list.size() > 1) {
            Collections.sort(list);
        }
    }

    public static final <T> void sortWith(@NotNull List<T> list, @NotNull Comparator<? super T> comparator) {
        if (list.size() > 1) {
            Collections.sort(list, comparator);
        }
    }
}
