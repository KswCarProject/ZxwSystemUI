package com.android.systemui.statusbar;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationLockscreenUserManagerImpl_Factory implements Factory<NotificationLockscreenUserManagerImpl> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<NotificationClickNotifier> clickNotifierProvider;
    public final Provider<CommonNotifCollection> commonNotifCollectionLazyProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardManager> keyguardManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProviderLazyProvider;

    public NotificationLockscreenUserManagerImpl_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<DevicePolicyManager> provider3, Provider<UserManager> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<CommonNotifCollection> provider6, Provider<NotificationClickNotifier> provider7, Provider<KeyguardManager> provider8, Provider<StatusBarStateController> provider9, Provider<Handler> provider10, Provider<DeviceProvisionedController> provider11, Provider<KeyguardStateController> provider12, Provider<SecureSettings> provider13, Provider<DumpManager> provider14) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.devicePolicyManagerProvider = provider3;
        this.userManagerProvider = provider4;
        this.visibilityProviderLazyProvider = provider5;
        this.commonNotifCollectionLazyProvider = provider6;
        this.clickNotifierProvider = provider7;
        this.keyguardManagerProvider = provider8;
        this.statusBarStateControllerProvider = provider9;
        this.mainHandlerProvider = provider10;
        this.deviceProvisionedControllerProvider = provider11;
        this.keyguardStateControllerProvider = provider12;
        this.secureSettingsProvider = provider13;
        this.dumpManagerProvider = provider14;
    }

    public NotificationLockscreenUserManagerImpl get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.devicePolicyManagerProvider.get(), this.userManagerProvider.get(), DoubleCheck.lazy(this.visibilityProviderLazyProvider), DoubleCheck.lazy(this.commonNotifCollectionLazyProvider), this.clickNotifierProvider.get(), this.keyguardManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.mainHandlerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.secureSettingsProvider.get(), this.dumpManagerProvider.get());
    }

    public static NotificationLockscreenUserManagerImpl_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<DevicePolicyManager> provider3, Provider<UserManager> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<CommonNotifCollection> provider6, Provider<NotificationClickNotifier> provider7, Provider<KeyguardManager> provider8, Provider<StatusBarStateController> provider9, Provider<Handler> provider10, Provider<DeviceProvisionedController> provider11, Provider<KeyguardStateController> provider12, Provider<SecureSettings> provider13, Provider<DumpManager> provider14) {
        return new NotificationLockscreenUserManagerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static NotificationLockscreenUserManagerImpl newInstance(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, Lazy<NotificationVisibilityProvider> lazy, Lazy<CommonNotifCollection> lazy2, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, SecureSettings secureSettings, DumpManager dumpManager) {
        return new NotificationLockscreenUserManagerImpl(context, broadcastDispatcher, devicePolicyManager, userManager, lazy, lazy2, notificationClickNotifier, keyguardManager, statusBarStateController, handler, deviceProvisionedController, keyguardStateController, secureSettings, dumpManager);
    }
}
