package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class TransformingIndexedSequence<T, R> implements Sequence<R> {
    @NotNull
    public final Sequence<T> sequence;
    @NotNull
    public final Function2<Integer, T, R> transformer;

    public TransformingIndexedSequence(@NotNull Sequence<? extends T> sequence2, @NotNull Function2<? super Integer, ? super T, ? extends R> function2) {
        this.sequence = sequence2;
        this.transformer = function2;
    }

    @NotNull
    public Iterator<R> iterator() {
        return new TransformingIndexedSequence$iterator$1(this);
    }
}
