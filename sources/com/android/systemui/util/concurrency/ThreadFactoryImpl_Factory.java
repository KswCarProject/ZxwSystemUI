package com.android.systemui.util.concurrency;

import dagger.internal.Factory;

public final class ThreadFactoryImpl_Factory implements Factory<ThreadFactoryImpl> {

    public static final class InstanceHolder {
        public static final ThreadFactoryImpl_Factory INSTANCE = new ThreadFactoryImpl_Factory();
    }

    public ThreadFactoryImpl get() {
        return newInstance();
    }

    public static ThreadFactoryImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ThreadFactoryImpl newInstance() {
        return new ThreadFactoryImpl();
    }
}
