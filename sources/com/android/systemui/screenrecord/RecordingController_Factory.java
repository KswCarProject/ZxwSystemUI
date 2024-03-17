package com.android.systemui.screenrecord;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserContextProvider;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RecordingController_Factory implements Factory<RecordingController> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<UserContextProvider> userContextProvider;

    public RecordingController_Factory(Provider<BroadcastDispatcher> provider, Provider<UserContextProvider> provider2) {
        this.broadcastDispatcherProvider = provider;
        this.userContextProvider = provider2;
    }

    public RecordingController get() {
        return newInstance(this.broadcastDispatcherProvider.get(), this.userContextProvider.get());
    }

    public static RecordingController_Factory create(Provider<BroadcastDispatcher> provider, Provider<UserContextProvider> provider2) {
        return new RecordingController_Factory(provider, provider2);
    }

    public static RecordingController newInstance(BroadcastDispatcher broadcastDispatcher, UserContextProvider userContextProvider2) {
        return new RecordingController(broadcastDispatcher, userContextProvider2);
    }
}
