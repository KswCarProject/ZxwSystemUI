package com.android.systemui.flags;

import dagger.internal.Factory;

public final class SystemPropertiesHelper_Factory implements Factory<SystemPropertiesHelper> {

    public static final class InstanceHolder {
        public static final SystemPropertiesHelper_Factory INSTANCE = new SystemPropertiesHelper_Factory();
    }

    public SystemPropertiesHelper get() {
        return newInstance();
    }

    public static SystemPropertiesHelper_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SystemPropertiesHelper newInstance() {
        return new SystemPropertiesHelper();
    }
}
