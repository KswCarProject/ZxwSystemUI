package com.android.systemui.statusbar.notification.row.dagger;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory implements Factory<String> {
    public final Provider<StatusBarNotification> statusBarNotificationProvider;

    public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory(Provider<StatusBarNotification> provider) {
        this.statusBarNotificationProvider = provider;
    }

    public String get() {
        return provideNotificationKey(this.statusBarNotificationProvider.get());
    }

    public static ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory create(Provider<StatusBarNotification> provider) {
        return new ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory(provider);
    }

    public static String provideNotificationKey(StatusBarNotification statusBarNotification) {
        return (String) Preconditions.checkNotNullFromProvides(ExpandableNotificationRowComponent.ExpandableNotificationRowModule.provideNotificationKey(statusBarNotification));
    }
}
