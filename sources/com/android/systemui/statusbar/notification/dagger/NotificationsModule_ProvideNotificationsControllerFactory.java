package com.android.systemui.statusbar.notification.dagger;

import android.content.Context;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideNotificationsControllerFactory implements Factory<NotificationsController> {
    public final Provider<Context> contextProvider;
    public final Provider<NotificationsControllerImpl> realControllerProvider;
    public final Provider<NotificationsControllerStub> stubControllerProvider;

    public NotificationsModule_ProvideNotificationsControllerFactory(Provider<Context> provider, Provider<NotificationsControllerImpl> provider2, Provider<NotificationsControllerStub> provider3) {
        this.contextProvider = provider;
        this.realControllerProvider = provider2;
        this.stubControllerProvider = provider3;
    }

    public NotificationsController get() {
        return provideNotificationsController(this.contextProvider.get(), this.realControllerProvider, this.stubControllerProvider);
    }

    public static NotificationsModule_ProvideNotificationsControllerFactory create(Provider<Context> provider, Provider<NotificationsControllerImpl> provider2, Provider<NotificationsControllerStub> provider3) {
        return new NotificationsModule_ProvideNotificationsControllerFactory(provider, provider2, provider3);
    }

    public static NotificationsController provideNotificationsController(Context context, Provider<NotificationsControllerImpl> provider, Provider<NotificationsControllerStub> provider2) {
        return (NotificationsController) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotificationsController(context, provider, provider2));
    }
}
