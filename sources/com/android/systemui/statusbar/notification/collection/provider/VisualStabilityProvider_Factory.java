package com.android.systemui.statusbar.notification.collection.provider;

import dagger.internal.Factory;

public final class VisualStabilityProvider_Factory implements Factory<VisualStabilityProvider> {

    public static final class InstanceHolder {
        public static final VisualStabilityProvider_Factory INSTANCE = new VisualStabilityProvider_Factory();
    }

    public VisualStabilityProvider get() {
        return newInstance();
    }

    public static VisualStabilityProvider_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static VisualStabilityProvider newInstance() {
        return new VisualStabilityProvider();
    }
}
