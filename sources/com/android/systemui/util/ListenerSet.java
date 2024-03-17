package com.android.systemui.util;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: ListenerSet.kt */
public final class ListenerSet<E> implements Iterable<E>, KMappedMarker {
    @NotNull
    public final CopyOnWriteArrayList<E> listeners = new CopyOnWriteArrayList<>();

    public final boolean addIfAbsent(E e) {
        return this.listeners.addIfAbsent(e);
    }

    public final boolean remove(E e) {
        return this.listeners.remove(e);
    }

    public final boolean isEmpty() {
        return this.listeners.isEmpty();
    }

    @NotNull
    public Iterator<E> iterator() {
        return this.listeners.iterator();
    }
}
