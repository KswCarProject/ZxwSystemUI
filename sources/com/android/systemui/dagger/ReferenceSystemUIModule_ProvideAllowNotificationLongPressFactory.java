package com.android.systemui.dagger;

import dagger.internal.Factory;

public final class ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {

    public static final class InstanceHolder {
        public static final ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory INSTANCE = new ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory();
    }

    public Boolean get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return ReferenceSystemUIModule.provideAllowNotificationLongPress();
    }
}
