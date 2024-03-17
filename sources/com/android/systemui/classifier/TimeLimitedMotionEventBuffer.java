package com.android.systemui.classifier;

import android.view.MotionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TimeLimitedMotionEventBuffer implements List<MotionEvent> {
    public final long mMaxAgeMs;
    public final LinkedList<MotionEvent> mMotionEvents = new LinkedList<>();

    public TimeLimitedMotionEventBuffer(long j) {
        this.mMaxAgeMs = j;
    }

    public final void ejectOldEvents() {
        if (!this.mMotionEvents.isEmpty()) {
            ListIterator<MotionEvent> listIterator = listIterator();
            long eventTime = this.mMotionEvents.getLast().getEventTime();
            while (listIterator.hasNext()) {
                MotionEvent next = listIterator.next();
                if (eventTime - next.getEventTime() > this.mMaxAgeMs) {
                    listIterator.remove();
                    next.recycle();
                }
            }
        }
    }

    public void add(int i, MotionEvent motionEvent) {
        throw new UnsupportedOperationException();
    }

    public MotionEvent remove(int i) {
        return this.mMotionEvents.remove(i);
    }

    public int indexOf(Object obj) {
        return this.mMotionEvents.indexOf(obj);
    }

    public int lastIndexOf(Object obj) {
        return this.mMotionEvents.lastIndexOf(obj);
    }

    public int size() {
        return this.mMotionEvents.size();
    }

    public boolean isEmpty() {
        return this.mMotionEvents.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.mMotionEvents.contains(obj);
    }

    public Iterator<MotionEvent> iterator() {
        return this.mMotionEvents.iterator();
    }

    public Object[] toArray() {
        return this.mMotionEvents.toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return this.mMotionEvents.toArray(tArr);
    }

    public boolean add(MotionEvent motionEvent) {
        boolean add = this.mMotionEvents.add(motionEvent);
        ejectOldEvents();
        return add;
    }

    public boolean remove(Object obj) {
        return this.mMotionEvents.remove(obj);
    }

    public boolean containsAll(Collection<?> collection) {
        return this.mMotionEvents.containsAll(collection);
    }

    public boolean addAll(Collection<? extends MotionEvent> collection) {
        boolean addAll = this.mMotionEvents.addAll(collection);
        ejectOldEvents();
        return addAll;
    }

    public boolean addAll(int i, Collection<? extends MotionEvent> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection) {
        return this.mMotionEvents.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.mMotionEvents.retainAll(collection);
    }

    public void clear() {
        this.mMotionEvents.clear();
    }

    public boolean equals(Object obj) {
        return this.mMotionEvents.equals(obj);
    }

    public int hashCode() {
        return this.mMotionEvents.hashCode();
    }

    public MotionEvent get(int i) {
        return this.mMotionEvents.get(i);
    }

    public MotionEvent set(int i, MotionEvent motionEvent) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<MotionEvent> listIterator() {
        return new Iter(0);
    }

    public ListIterator<MotionEvent> listIterator(int i) {
        return new Iter(i);
    }

    public List<MotionEvent> subList(int i, int i2) {
        throw new UnsupportedOperationException();
    }

    public class Iter implements ListIterator<MotionEvent> {
        public final ListIterator<MotionEvent> mIterator;

        public Iter(int i) {
            this.mIterator = TimeLimitedMotionEventBuffer.this.mMotionEvents.listIterator(i);
        }

        public boolean hasNext() {
            return this.mIterator.hasNext();
        }

        public MotionEvent next() {
            return this.mIterator.next();
        }

        public boolean hasPrevious() {
            return this.mIterator.hasPrevious();
        }

        public MotionEvent previous() {
            return this.mIterator.previous();
        }

        public int nextIndex() {
            return this.mIterator.nextIndex();
        }

        public int previousIndex() {
            return this.mIterator.previousIndex();
        }

        public void remove() {
            this.mIterator.remove();
        }

        public void set(MotionEvent motionEvent) {
            throw new UnsupportedOperationException();
        }

        public void add(MotionEvent motionEvent) {
            throw new UnsupportedOperationException();
        }
    }
}
