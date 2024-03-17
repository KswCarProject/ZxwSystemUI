package com.android.systemui.util.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: RingBuffer.kt */
public final class RingBuffer<T> implements Iterable<T>, KMappedMarker {
    @NotNull
    public final List<T> buffer;
    @NotNull
    public final Function0<T> factory;
    public final int maxSize;
    public long omega;

    public RingBuffer(int i, @NotNull Function0<? extends T> function0) {
        this.maxSize = i;
        this.factory = function0;
        ArrayList arrayList = new ArrayList(i);
        int i2 = 0;
        while (i2 < i) {
            i2++;
            arrayList.add((Object) null);
        }
        this.buffer = arrayList;
    }

    public final int getSize() {
        long j = this.omega;
        int i = this.maxSize;
        return j < ((long) i) ? (int) j : i;
    }

    public final T advance() {
        int indexOf = indexOf(this.omega);
        this.omega++;
        T t = this.buffer.get(indexOf);
        if (t != null) {
            return t;
        }
        T invoke = this.factory.invoke();
        this.buffer.set(indexOf, invoke);
        return invoke;
    }

    public final T get(int i) {
        if (i < 0 || i >= getSize()) {
            throw new IndexOutOfBoundsException("Index " + i + " is out of bounds");
        }
        T t = this.buffer.get(indexOf(Math.max(this.omega, (long) this.maxSize) + ((long) i)));
        Intrinsics.checkNotNull(t);
        return t;
    }

    @NotNull
    public Iterator<T> iterator() {
        return new RingBuffer$iterator$1(this);
    }

    public final int indexOf(long j) {
        return (int) (j % ((long) this.maxSize));
    }
}
