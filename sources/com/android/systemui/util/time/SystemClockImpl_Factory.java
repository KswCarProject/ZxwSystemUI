package com.android.systemui.util.time;

import dagger.internal.Factory;

public final class SystemClockImpl_Factory implements Factory<SystemClockImpl> {

    public static final class InstanceHolder {
        public static final SystemClockImpl_Factory INSTANCE = new SystemClockImpl_Factory();
    }

    public SystemClockImpl get() {
        return newInstance();
    }

    public static SystemClockImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SystemClockImpl newInstance() {
        return new SystemClockImpl();
    }
}
