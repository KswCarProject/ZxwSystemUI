package com.android.systemui.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;

/* compiled from: RingBuffer.kt */
public final class RingBuffer$iterator$1 implements Iterator<T>, KMappedMarker {
    public int position;
    public final /* synthetic */ RingBuffer<T> this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public RingBuffer$iterator$1(RingBuffer<T> ringBuffer) {
        this.this$0 = ringBuffer;
    }

    public T next() {
        if (this.position < this.this$0.getSize()) {
            T t = this.this$0.get(this.position);
            this.position++;
            return t;
        }
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        return this.position < this.this$0.getSize();
    }
}
