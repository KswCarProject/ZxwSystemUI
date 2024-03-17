package com.android.systemui.shared.system;

import dagger.internal.Factory;

public final class UncaughtExceptionPreHandlerManager_Factory implements Factory<UncaughtExceptionPreHandlerManager> {

    public static final class InstanceHolder {
        public static final UncaughtExceptionPreHandlerManager_Factory INSTANCE = new UncaughtExceptionPreHandlerManager_Factory();
    }

    public UncaughtExceptionPreHandlerManager get() {
        return newInstance();
    }

    public static UncaughtExceptionPreHandlerManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UncaughtExceptionPreHandlerManager newInstance() {
        return new UncaughtExceptionPreHandlerManager();
    }
}
