package com.android.systemui.statusbar.phone.dagger;

import com.android.keyguard.LockIconView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_GetLockIconViewFactory implements Factory<LockIconView> {
    public final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;

    public StatusBarViewModule_GetLockIconViewFactory(Provider<NotificationShadeWindowView> provider) {
        this.notificationShadeWindowViewProvider = provider;
    }

    public LockIconView get() {
        return getLockIconView(this.notificationShadeWindowViewProvider.get());
    }

    public static StatusBarViewModule_GetLockIconViewFactory create(Provider<NotificationShadeWindowView> provider) {
        return new StatusBarViewModule_GetLockIconViewFactory(provider);
    }

    public static LockIconView getLockIconView(NotificationShadeWindowView notificationShadeWindowView) {
        return (LockIconView) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getLockIconView(notificationShadeWindowView));
    }
}
