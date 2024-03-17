package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class MergingSequence$iterator$1 implements Iterator<V>, KMappedMarker {
    @NotNull
    public final Iterator<T1> iterator1;
    @NotNull
    public final Iterator<T2> iterator2;
    public final /* synthetic */ MergingSequence<T1, T2, V> this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public MergingSequence$iterator$1(MergingSequence<T1, T2, V> mergingSequence) {
        this.this$0 = mergingSequence;
        this.iterator1 = mergingSequence.sequence1.iterator();
        this.iterator2 = mergingSequence.sequence2.iterator();
    }

    public V next() {
        return this.this$0.transform.invoke(this.iterator1.next(), this.iterator2.next());
    }

    public boolean hasNext() {
        return this.iterator1.hasNext() && this.iterator2.hasNext();
    }
}
