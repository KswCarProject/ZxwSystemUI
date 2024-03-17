package kotlin.comparisons;

import java.util.Comparator;
import kotlin.jvm.functions.Function1;

/* compiled from: Comparisons.kt */
public final class ComparisonsKt__ComparisonsKt$compareBy$1<T> implements Comparator {
    public final /* synthetic */ Function1<T, Comparable<?>>[] $selectors;

    public ComparisonsKt__ComparisonsKt$compareBy$1(Function1<? super T, ? extends Comparable<?>>[] function1Arr) {
        this.$selectors = function1Arr;
    }

    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValuesByImpl$ComparisonsKt__ComparisonsKt(t, t2, this.$selectors);
    }
}
