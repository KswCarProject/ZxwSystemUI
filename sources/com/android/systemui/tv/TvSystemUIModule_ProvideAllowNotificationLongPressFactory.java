package com.android.systemui.tv;

import dagger.internal.Factory;

public final class TvSystemUIModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {

    public static final class InstanceHolder {
        public static final TvSystemUIModule_ProvideAllowNotificationLongPressFactory INSTANCE = new TvSystemUIModule_ProvideAllowNotificationLongPressFactory();
    }

    public Boolean get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static TvSystemUIModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return TvSystemUIModule.provideAllowNotificationLongPress();
    }
}
