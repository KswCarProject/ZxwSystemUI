package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class TakeSequence$iterator$1 implements Iterator<T>, KMappedMarker {
    @NotNull
    public final Iterator<T> iterator;
    public int left;
    public final /* synthetic */ TakeSequence<T> this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public TakeSequence$iterator$1(TakeSequence<T> takeSequence) {
        this.this$0 = takeSequence;
        this.left = takeSequence.count;
        this.iterator = takeSequence.sequence.iterator();
    }

    public T next() {
        int i = this.left;
        if (i != 0) {
            this.left = i - 1;
            return this.iterator.next();
        }
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        return this.left > 0 && this.iterator.hasNext();
    }
}
