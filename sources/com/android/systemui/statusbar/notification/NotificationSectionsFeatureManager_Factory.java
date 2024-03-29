package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationSectionsFeatureManager_Factory implements Factory<NotificationSectionsFeatureManager> {
    public final Provider<Context> contextProvider;
    public final Provider<DeviceConfigProxy> proxyProvider;

    public NotificationSectionsFeatureManager_Factory(Provider<DeviceConfigProxy> provider, Provider<Context> provider2) {
        this.proxyProvider = provider;
        this.contextProvider = provider2;
    }

    public NotificationSectionsFeatureManager get() {
        return newInstance(this.proxyProvider.get(), this.contextProvider.get());
    }

    public static NotificationSectionsFeatureManager_Factory create(Provider<DeviceConfigProxy> provider, Provider<Context> provider2) {
        return new NotificationSectionsFeatureManager_Factory(provider, provider2);
    }

    public static NotificationSectionsFeatureManager newInstance(DeviceConfigProxy deviceConfigProxy, Context context) {
        return new NotificationSectionsFeatureManager(deviceConfigProxy, context);
    }
}
