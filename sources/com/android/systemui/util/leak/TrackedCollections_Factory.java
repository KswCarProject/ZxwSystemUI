package com.android.systemui.util.leak;

import dagger.internal.Factory;

public final class TrackedCollections_Factory implements Factory<TrackedCollections> {

    public static final class InstanceHolder {
        public static final TrackedCollections_Factory INSTANCE = new TrackedCollections_Factory();
    }

    public TrackedCollections get() {
        return newInstance();
    }

    public static TrackedCollections_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TrackedCollections newInstance() {
        return new TrackedCollections();
    }
}
