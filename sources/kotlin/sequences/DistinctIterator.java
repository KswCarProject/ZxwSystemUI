package kotlin.sequences;

import java.util.HashSet;
import java.util.Iterator;
import kotlin.collections.AbstractIterator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class DistinctIterator<T, K> extends AbstractIterator<T> {
    @NotNull
    public final Function1<T, K> keySelector;
    @NotNull
    public final HashSet<K> observed = new HashSet<>();
    @NotNull
    public final Iterator<T> source;

    public DistinctIterator(@NotNull Iterator<? extends T> it, @NotNull Function1<? super T, ? extends K> function1) {
        this.source = it;
        this.keySelector = function1;
    }

    public void computeNext() {
        while (this.source.hasNext()) {
            T next = this.source.next();
            if (this.observed.add(this.keySelector.invoke(next))) {
                setNext(next);
                return;
            }
        }
        done();
    }
}
