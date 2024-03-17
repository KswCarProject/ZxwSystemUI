package kotlin.sequences;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

/* compiled from: SequencesJVM.kt */
public final class ConstrainedOnceSequence<T> implements Sequence<T> {
    @NotNull
    public final AtomicReference<Sequence<T>> sequenceRef;

    public ConstrainedOnceSequence(@NotNull Sequence<? extends T> sequence) {
        this.sequenceRef = new AtomicReference<>(sequence);
    }

    @NotNull
    public Iterator<T> iterator() {
        Sequence andSet = this.sequenceRef.getAndSet((Object) null);
        if (andSet != null) {
            return andSet.iterator();
        }
        throw new IllegalStateException("This sequence can be consumed only once.");
    }
}
