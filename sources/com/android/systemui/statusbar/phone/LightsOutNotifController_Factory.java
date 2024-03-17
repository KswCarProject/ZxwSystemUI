package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.WindowManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LightsOutNotifController_Factory implements Factory<LightsOutNotifController> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<View> lightsOutNotifViewProvider;
    public final Provider<NotifLiveDataStore> notifDataStoreProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public LightsOutNotifController_Factory(Provider<View> provider, Provider<WindowManager> provider2, Provider<NotifLiveDataStore> provider3, Provider<CommandQueue> provider4) {
        this.lightsOutNotifViewProvider = provider;
        this.windowManagerProvider = provider2;
        this.notifDataStoreProvider = provider3;
        this.commandQueueProvider = provider4;
    }

    public LightsOutNotifController get() {
        return newInstance(this.lightsOutNotifViewProvider.get(), this.windowManagerProvider.get(), this.notifDataStoreProvider.get(), this.commandQueueProvider.get());
    }

    public static LightsOutNotifController_Factory create(Provider<View> provider, Provider<WindowManager> provider2, Provider<NotifLiveDataStore> provider3, Provider<CommandQueue> provider4) {
        return new LightsOutNotifController_Factory(provider, provider2, provider3, provider4);
    }

    public static LightsOutNotifController newInstance(View view, WindowManager windowManager, NotifLiveDataStore notifLiveDataStore, CommandQueue commandQueue) {
        return new LightsOutNotifController(view, windowManager, notifLiveDataStore, commandQueue);
    }
}
