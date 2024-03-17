package com.android.systemui;

import dagger.internal.Factory;

public final class InitController_Factory implements Factory<InitController> {

    public static final class InstanceHolder {
        public static final InitController_Factory INSTANCE = new InitController_Factory();
    }

    public InitController get() {
        return newInstance();
    }

    public static InitController_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static InitController newInstance() {
        return new InitController();
    }
}
