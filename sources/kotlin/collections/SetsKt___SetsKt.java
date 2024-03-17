package kotlin.collections;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/* compiled from: _Sets.kt */
public class SetsKt___SetsKt extends SetsKt__SetsKt {
    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> set, @NotNull Iterable<? extends T> iterable) {
        Iterable iterable2 = set;
        Collection<? extends T> convertToSetForSetOperationWith = BrittleContainsOptimizationKt.convertToSetForSetOperationWith(iterable, iterable2);
        if (convertToSetForSetOperationWith.isEmpty()) {
            return CollectionsKt___CollectionsKt.toSet(iterable2);
        }
        if (convertToSetForSetOperationWith instanceof Set) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            for (Object next : iterable2) {
                if (!convertToSetForSetOperationWith.contains(next)) {
                    linkedHashSet.add(next);
                }
            }
            return linkedHashSet;
        }
        LinkedHashSet linkedHashSet2 = new LinkedHashSet(set);
        linkedHashSet2.removeAll(convertToSetForSetOperationWith);
        return linkedHashSet2;
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> set, @NotNull Iterable<? extends T> iterable) {
        Integer collectionSizeOrNull = CollectionsKt__IterablesKt.collectionSizeOrNull(iterable);
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(collectionSizeOrNull == null ? set.size() * 2 : collectionSizeOrNull.intValue() + set.size()));
        linkedHashSet.addAll(set);
        CollectionsKt__MutableCollectionsKt.addAll(linkedHashSet, iterable);
        return linkedHashSet;
    }
}
