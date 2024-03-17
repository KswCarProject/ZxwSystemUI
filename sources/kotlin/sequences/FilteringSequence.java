package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class FilteringSequence<T> implements Sequence<T> {
    @NotNull
    public final Function1<T, Boolean> predicate;
    public final boolean sendWhen;
    @NotNull
    public final Sequence<T> sequence;

    public FilteringSequence(@NotNull Sequence<? extends T> sequence2, boolean z, @NotNull Function1<? super T, Boolean> function1) {
        this.sequence = sequence2;
        this.sendWhen = z;
        this.predicate = function1;
    }

    @NotNull
    public Iterator<T> iterator() {
        return new FilteringSequence$iterator$1(this);
    }
}
