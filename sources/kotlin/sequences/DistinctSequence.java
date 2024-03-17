package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class DistinctSequence<T, K> implements Sequence<T> {
    @NotNull
    public final Function1<T, K> keySelector;
    @NotNull
    public final Sequence<T> source;

    public DistinctSequence(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends K> function1) {
        this.source = sequence;
        this.keySelector = function1;
    }

    @NotNull
    public Iterator<T> iterator() {
        return new DistinctIterator(this.source.iterator(), this.keySelector);
    }
}
