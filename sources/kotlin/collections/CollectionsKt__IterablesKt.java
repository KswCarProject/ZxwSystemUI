package kotlin.collections;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Iterables.kt */
public class CollectionsKt__IterablesKt extends CollectionsKt__CollectionsKt {
    @Nullable
    public static final <T> Integer collectionSizeOrNull(@NotNull Iterable<? extends T> iterable) {
        if (iterable instanceof Collection) {
            return Integer.valueOf(((Collection) iterable).size());
        }
        return null;
    }

    public static final <T> int collectionSizeOrDefault(@NotNull Iterable<? extends T> iterable, int i) {
        return iterable instanceof Collection ? ((Collection) iterable).size() : i;
    }
}
