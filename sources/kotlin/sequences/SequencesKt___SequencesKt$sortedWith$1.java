package kotlin.sequences;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: _Sequences.kt */
public final class SequencesKt___SequencesKt$sortedWith$1 implements Sequence<T> {
    public final /* synthetic */ Comparator<? super T> $comparator;
    public final /* synthetic */ Sequence<T> $this_sortedWith;

    public SequencesKt___SequencesKt$sortedWith$1(Sequence<? extends T> sequence, Comparator<? super T> comparator) {
        this.$this_sortedWith = sequence;
        this.$comparator = comparator;
    }

    @NotNull
    public Iterator<T> iterator() {
        List<T> mutableList = SequencesKt___SequencesKt.toMutableList(this.$this_sortedWith);
        CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, this.$comparator);
        return mutableList.iterator();
    }
}
