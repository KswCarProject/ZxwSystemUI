package com.android.systemui.util.concurrency;

import dagger.internal.Factory;

public final class ExecutionImpl_Factory implements Factory<ExecutionImpl> {

    public static final class InstanceHolder {
        public static final ExecutionImpl_Factory INSTANCE = new ExecutionImpl_Factory();
    }

    public ExecutionImpl get() {
        return newInstance();
    }

    public static ExecutionImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ExecutionImpl newInstance() {
        return new ExecutionImpl();
    }
}
