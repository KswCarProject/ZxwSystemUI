package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotifInflaterImpl_Factory implements Factory<NotifInflaterImpl> {
    public final Provider<NotifInflationErrorManager> errorManagerProvider;

    public NotifInflaterImpl_Factory(Provider<NotifInflationErrorManager> provider) {
        this.errorManagerProvider = provider;
    }

    public NotifInflaterImpl get() {
        return newInstance(this.errorManagerProvider.get());
    }

    public static NotifInflaterImpl_Factory create(Provider<NotifInflationErrorManager> provider) {
        return new NotifInflaterImpl_Factory(provider);
    }

    public static NotifInflaterImpl newInstance(NotifInflationErrorManager notifInflationErrorManager) {
        return new NotifInflaterImpl(notifInflationErrorManager);
    }
}
