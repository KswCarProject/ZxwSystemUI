package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class TransformingSequence<T, R> implements Sequence<R> {
    @NotNull
    public final Sequence<T> sequence;
    @NotNull
    public final Function1<T, R> transformer;

    public TransformingSequence(@NotNull Sequence<? extends T> sequence2, @NotNull Function1<? super T, ? extends R> function1) {
        this.sequence = sequence2;
        this.transformer = function1;
    }

    @NotNull
    public Iterator<R> iterator() {
        return new TransformingSequence$iterator$1(this);
    }

    @NotNull
    public final <E> Sequence<E> flatten$kotlin_stdlib(@NotNull Function1<? super R, ? extends Iterator<? extends E>> function1) {
        return new FlatteningSequence(this.sequence, this.transformer, function1);
    }
}
