package com.android.systemui.util.leak;

import java.util.Collection;
import java.util.WeakHashMap;

public class TrackedObjects {
    public final WeakHashMap<Class<?>, TrackedClass<?>> mTrackedClasses = new WeakHashMap<>();
    public final TrackedCollections mTrackedCollections;

    public TrackedObjects(TrackedCollections trackedCollections) {
        this.mTrackedCollections = trackedCollections;
    }

    public synchronized <T> void track(T t) {
        Class<?> cls = t.getClass();
        TrackedClass trackedClass = this.mTrackedClasses.get(cls);
        if (trackedClass == null) {
            trackedClass = new TrackedClass();
            this.mTrackedClasses.put(cls, trackedClass);
        }
        trackedClass.track(t);
        this.mTrackedCollections.track(trackedClass, cls.getName());
    }

    public static boolean isTrackedObject(Collection<?> collection) {
        return collection instanceof TrackedClass;
    }

    public static class TrackedClass<T> extends AbstractCollection<T> {
        public final WeakIdentityHashMap<T, Void> instances;

        public TrackedClass() {
            this.instances = new WeakIdentityHashMap<>();
        }

        public void track(T t) {
            this.instances.put(t, null);
        }

        public int size() {
            return this.instances.size();
        }

        public boolean isEmpty() {
            return this.instances.isEmpty();
        }
    }
}
