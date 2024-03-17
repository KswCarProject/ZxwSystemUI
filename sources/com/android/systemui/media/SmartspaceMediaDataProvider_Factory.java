package com.android.systemui.media;

import dagger.internal.Factory;

public final class SmartspaceMediaDataProvider_Factory implements Factory<SmartspaceMediaDataProvider> {

    public static final class InstanceHolder {
        public static final SmartspaceMediaDataProvider_Factory INSTANCE = new SmartspaceMediaDataProvider_Factory();
    }

    public SmartspaceMediaDataProvider get() {
        return newInstance();
    }

    public static SmartspaceMediaDataProvider_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SmartspaceMediaDataProvider newInstance() {
        return new SmartspaceMediaDataProvider();
    }
}
