package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Sequences.kt */
public final class TakeWhileSequence$iterator$1 implements Iterator<T>, KMappedMarker {
    @NotNull
    public final Iterator<T> iterator;
    @Nullable
    public T nextItem;
    public int nextState = -1;
    public final /* synthetic */ TakeWhileSequence<T> this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public TakeWhileSequence$iterator$1(TakeWhileSequence<T> takeWhileSequence) {
        this.this$0 = takeWhileSequence;
        this.iterator = takeWhileSequence.sequence.iterator();
    }

    public final void calcNext() {
        if (this.iterator.hasNext()) {
            T next = this.iterator.next();
            if (((Boolean) this.this$0.predicate.invoke(next)).booleanValue()) {
                this.nextState = 1;
                this.nextItem = next;
                return;
            }
        }
        this.nextState = 0;
    }

    public T next() {
        if (this.nextState == -1) {
            calcNext();
        }
        if (this.nextState != 0) {
            T t = this.nextItem;
            this.nextItem = null;
            this.nextState = -1;
            return t;
        }
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        if (this.nextState == -1) {
            calcNext();
        }
        return this.nextState == 1;
    }
}
