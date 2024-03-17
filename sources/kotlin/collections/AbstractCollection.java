package kotlin.collections;

import java.util.Collection;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: AbstractCollection.kt */
public abstract class AbstractCollection<E> implements Collection<E>, KMappedMarker {
    public boolean add(E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public abstract int getSize();

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final /* bridge */ int size() {
        return getSize();
    }

    public boolean containsAll(@NotNull Collection<? extends Object> collection) {
        Iterable<Object> iterable = collection;
        if (((Collection) iterable).isEmpty()) {
            return true;
        }
        for (Object contains : iterable) {
            if (!contains(contains)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @NotNull
    public String toString() {
        return CollectionsKt___CollectionsKt.joinToString$default(this, ", ", "[", "]", 0, (CharSequence) null, new AbstractCollection$toString$1(this), 24, (Object) null);
    }

    @NotNull
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] tArr) {
        return CollectionToArray.toArray(this, tArr);
    }

    public boolean contains(E e) {
        if (isEmpty()) {
            return false;
        }
        for (Object areEqual : this) {
            if (Intrinsics.areEqual(areEqual, (Object) e)) {
                return true;
            }
        }
        return false;
    }
}
