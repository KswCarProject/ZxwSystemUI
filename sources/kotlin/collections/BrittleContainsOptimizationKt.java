package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/* compiled from: BrittleContainsOptimization.kt */
public final class BrittleContainsOptimizationKt {
    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperationWith(@NotNull Iterable<? extends T> iterable, @NotNull Iterable<? extends T> iterable2) {
        if (iterable instanceof Set) {
            return (Collection) iterable;
        }
        if (!(iterable instanceof Collection)) {
            return CollectionSystemProperties.brittleContainsOptimizationEnabled ? CollectionsKt___CollectionsKt.toHashSet(iterable) : CollectionsKt___CollectionsKt.toList(iterable);
        }
        if ((iterable2 instanceof Collection) && ((Collection) iterable2).size() < 2) {
            return (Collection) iterable;
        }
        Collection<T> collection = (Collection) iterable;
        return safeToConvertToSet(collection) ? CollectionsKt___CollectionsKt.toHashSet(iterable) : collection;
    }

    public static final <T> boolean safeToConvertToSet(Collection<? extends T> collection) {
        return CollectionSystemProperties.brittleContainsOptimizationEnabled && collection.size() > 2 && (collection instanceof ArrayList);
    }
}
