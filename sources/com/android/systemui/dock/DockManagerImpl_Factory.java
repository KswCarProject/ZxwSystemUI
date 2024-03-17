package com.android.systemui.dock;

import dagger.internal.Factory;

public final class DockManagerImpl_Factory implements Factory<DockManagerImpl> {

    public static final class InstanceHolder {
        public static final DockManagerImpl_Factory INSTANCE = new DockManagerImpl_Factory();
    }

    public DockManagerImpl get() {
        return newInstance();
    }

    public static DockManagerImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DockManagerImpl newInstance() {
        return new DockManagerImpl();
    }
}
