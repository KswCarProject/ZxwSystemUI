package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AbstractList.kt */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    public void add(int i, E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public abstract E get(int i);

    public E remove(int i) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public E set(int i, E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @NotNull
    public Iterator<E> iterator() {
        return new IteratorImpl();
    }

    @NotNull
    public ListIterator<E> listIterator() {
        return new ListIteratorImpl(0);
    }

    @NotNull
    public ListIterator<E> listIterator(int i) {
        return new ListIteratorImpl(i);
    }

    @NotNull
    public List<E> subList(int i, int i2) {
        return new SubList(this, i, i2);
    }

    /* compiled from: AbstractList.kt */
    public static final class SubList<E> extends AbstractList<E> implements RandomAccess {
        public int _size;
        public final int fromIndex;
        @NotNull
        public final AbstractList<E> list;

        public SubList(@NotNull AbstractList<? extends E> abstractList, int i, int i2) {
            this.list = abstractList;
            this.fromIndex = i;
            AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(i, i2, abstractList.size());
            this._size = i2 - i;
        }

        public E get(int i) {
            AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, this._size);
            return this.list.get(this.fromIndex + i);
        }

        public int getSize() {
            return this._size;
        }
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        return Companion.orderedEquals$kotlin_stdlib(this, (Collection) obj);
    }

    public int hashCode() {
        return Companion.orderedHashCode$kotlin_stdlib(this);
    }

    /* compiled from: AbstractList.kt */
    public class IteratorImpl implements Iterator<E>, KMappedMarker {
        public int index;

        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public IteratorImpl() {
        }

        public final int getIndex() {
            return this.index;
        }

        public final void setIndex(int i) {
            this.index = i;
        }

        public boolean hasNext() {
            return this.index < AbstractList.this.size();
        }

        public E next() {
            if (hasNext()) {
                AbstractList<E> abstractList = AbstractList.this;
                int i = this.index;
                this.index = i + 1;
                return abstractList.get(i);
            }
            throw new NoSuchElementException();
        }
    }

    /* compiled from: AbstractList.kt */
    public class ListIteratorImpl extends AbstractList<E>.IteratorImpl implements ListIterator<E> {
        public void add(E e) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public void set(E e) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public ListIteratorImpl(int i) {
            super();
            AbstractList.Companion.checkPositionIndex$kotlin_stdlib(i, AbstractList.this.size());
            setIndex(i);
        }

        public boolean hasPrevious() {
            return getIndex() > 0;
        }

        public int nextIndex() {
            return getIndex();
        }

        public E previous() {
            if (hasPrevious()) {
                AbstractList<E> abstractList = AbstractList.this;
                setIndex(getIndex() - 1);
                return abstractList.get(getIndex());
            }
            throw new NoSuchElementException();
        }

        public int previousIndex() {
            return getIndex() - 1;
        }
    }

    /* compiled from: AbstractList.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final void checkElementIndex$kotlin_stdlib(int i, int i2) {
            if (i < 0 || i >= i2) {
                throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
            }
        }

        public final void checkPositionIndex$kotlin_stdlib(int i, int i2) {
            if (i < 0 || i > i2) {
                throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
            }
        }

        public final void checkRangeIndexes$kotlin_stdlib(int i, int i2, int i3) {
            if (i < 0 || i2 > i3) {
                throw new IndexOutOfBoundsException("fromIndex: " + i + ", toIndex: " + i2 + ", size: " + i3);
            } else if (i > i2) {
                throw new IllegalArgumentException("fromIndex: " + i + " > toIndex: " + i2);
            }
        }

        public final int orderedHashCode$kotlin_stdlib(@NotNull Collection<?> collection) {
            int i;
            int i2 = 1;
            for (Object next : collection) {
                int i3 = i2 * 31;
                if (next == null) {
                    i = 0;
                } else {
                    i = next.hashCode();
                }
                i2 = i3 + i;
            }
            return i2;
        }

        public final boolean orderedEquals$kotlin_stdlib(@NotNull Collection<?> collection, @NotNull Collection<?> collection2) {
            if (collection.size() != collection2.size()) {
                return false;
            }
            Iterator<?> it = collection2.iterator();
            for (Object areEqual : collection) {
                if (!Intrinsics.areEqual((Object) areEqual, (Object) it.next())) {
                    return false;
                }
            }
            return true;
        }
    }

    public int indexOf(E e) {
        int i = 0;
        for (Object areEqual : this) {
            if (Intrinsics.areEqual(areEqual, (Object) e)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int lastIndexOf(E e) {
        ListIterator listIterator = listIterator(size());
        while (listIterator.hasPrevious()) {
            if (Intrinsics.areEqual(listIterator.previous(), (Object) e)) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }
}
