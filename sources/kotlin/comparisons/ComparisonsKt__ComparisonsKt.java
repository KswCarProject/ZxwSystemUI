package kotlin.comparisons;

import java.util.Comparator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Comparisons.kt */
public class ComparisonsKt__ComparisonsKt {
    public static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(T t, T t2, Function1<? super T, ? extends Comparable<?>>[] function1Arr) {
        int length = function1Arr.length;
        int i = 0;
        while (i < length) {
            Function1<? super T, ? extends Comparable<?>> function1 = function1Arr[i];
            i++;
            int compareValues = compareValues((Comparable) function1.invoke(t), (Comparable) function1.invoke(t2));
            if (compareValues != 0) {
                return compareValues;
            }
        }
        return 0;
    }

    public static final <T extends Comparable<?>> int compareValues(@Nullable T t, @Nullable T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return t.compareTo(t2);
    }

    @NotNull
    public static final <T> Comparator<T> compareBy(@NotNull Function1<? super T, ? extends Comparable<?>>... function1Arr) {
        if (function1Arr.length > 0) {
            return new ComparisonsKt__ComparisonsKt$compareBy$1(function1Arr);
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }
}
