package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class FlatteningSequence<T, R, E> implements Sequence<E> {
    @NotNull
    public final Function1<R, Iterator<E>> iterator;
    @NotNull
    public final Sequence<T> sequence;
    @NotNull
    public final Function1<T, R> transformer;

    public FlatteningSequence(@NotNull Sequence<? extends T> sequence2, @NotNull Function1<? super T, ? extends R> function1, @NotNull Function1<? super R, ? extends Iterator<? extends E>> function12) {
        this.sequence = sequence2;
        this.transformer = function1;
        this.iterator = function12;
    }

    @NotNull
    public Iterator<E> iterator() {
        return new FlatteningSequence$iterator$1(this);
    }
}
