package com.android.systemui.util.io;

import dagger.internal.Factory;

public final class Files_Factory implements Factory<Files> {

    public static final class InstanceHolder {
        public static final Files_Factory INSTANCE = new Files_Factory();
    }

    public Files get() {
        return newInstance();
    }

    public static Files_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Files newInstance() {
        return new Files();
    }
}
