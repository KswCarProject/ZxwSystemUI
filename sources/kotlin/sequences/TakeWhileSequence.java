package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class TakeWhileSequence<T> implements Sequence<T> {
    @NotNull
    public final Function1<T, Boolean> predicate;
    @NotNull
    public final Sequence<T> sequence;

    public TakeWhileSequence(@NotNull Sequence<? extends T> sequence2, @NotNull Function1<? super T, Boolean> function1) {
        this.sequence = sequence2;
        this.predicate = function1;
    }

    @NotNull
    public Iterator<T> iterator() {
        return new TakeWhileSequence$iterator$1(this);
    }
}
