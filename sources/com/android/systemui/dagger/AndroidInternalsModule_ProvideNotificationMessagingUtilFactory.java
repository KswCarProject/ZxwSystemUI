package com.android.systemui.dagger;

import android.content.Context;
import com.android.internal.util.NotificationMessagingUtil;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AndroidInternalsModule_ProvideNotificationMessagingUtilFactory implements Factory<NotificationMessagingUtil> {
    public final Provider<Context> contextProvider;
    public final AndroidInternalsModule module;

    public AndroidInternalsModule_ProvideNotificationMessagingUtilFactory(AndroidInternalsModule androidInternalsModule, Provider<Context> provider) {
        this.module = androidInternalsModule;
        this.contextProvider = provider;
    }

    public NotificationMessagingUtil get() {
        return provideNotificationMessagingUtil(this.module, this.contextProvider.get());
    }

    public static AndroidInternalsModule_ProvideNotificationMessagingUtilFactory create(AndroidInternalsModule androidInternalsModule, Provider<Context> provider) {
        return new AndroidInternalsModule_ProvideNotificationMessagingUtilFactory(androidInternalsModule, provider);
    }

    public static NotificationMessagingUtil provideNotificationMessagingUtil(AndroidInternalsModule androidInternalsModule, Context context) {
        return (NotificationMessagingUtil) Preconditions.checkNotNullFromProvides(androidInternalsModule.provideNotificationMessagingUtil(context));
    }
}
