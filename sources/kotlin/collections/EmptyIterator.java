package kotlin.collections;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Collections.kt */
public final class EmptyIterator implements ListIterator, KMappedMarker {
    @NotNull
    public static final EmptyIterator INSTANCE = new EmptyIterator();

    public void add(Void voidR) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean hasNext() {
        return false;
    }

    public boolean hasPrevious() {
        return false;
    }

    public int nextIndex() {
        return 0;
    }

    public int previousIndex() {
        return -1;
    }

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void set(Void voidR) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @NotNull
    public Void next() {
        throw new NoSuchElementException();
    }

    @NotNull
    public Void previous() {
        throw new NoSuchElementException();
    }
}
