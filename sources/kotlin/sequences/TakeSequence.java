package kotlin.sequences;

import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class TakeSequence<T> implements Sequence<T>, DropTakeSequence<T> {
    public final int count;
    @NotNull
    public final Sequence<T> sequence;

    public TakeSequence(@NotNull Sequence<? extends T> sequence2, int i) {
        this.sequence = sequence2;
        this.count = i;
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("count must be non-negative, but was " + i + '.').toString());
        }
    }

    @NotNull
    public Sequence<T> take(int i) {
        return i >= this.count ? this : new TakeSequence(this.sequence, i);
    }

    @NotNull
    public Iterator<T> iterator() {
        return new TakeSequence$iterator$1(this);
    }
}
