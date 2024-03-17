package com.android.systemui.util.view;

import dagger.internal.Factory;

public final class ViewUtil_Factory implements Factory<ViewUtil> {

    public static final class InstanceHolder {
        public static final ViewUtil_Factory INSTANCE = new ViewUtil_Factory();
    }

    public ViewUtil get() {
        return newInstance();
    }

    public static ViewUtil_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ViewUtil newInstance() {
        return new ViewUtil();
    }
}
