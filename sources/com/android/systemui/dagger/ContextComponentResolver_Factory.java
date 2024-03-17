package com.android.systemui.dagger;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import com.android.systemui.recents.RecentsImplementation;
import dagger.internal.Factory;
import java.util.Map;
import javax.inject.Provider;

public final class ContextComponentResolver_Factory implements Factory<ContextComponentResolver> {
    public final Provider<Map<Class<?>, Provider<Activity>>> activityCreatorsProvider;
    public final Provider<Map<Class<?>, Provider<BroadcastReceiver>>> broadcastReceiverCreatorsProvider;
    public final Provider<Map<Class<?>, Provider<RecentsImplementation>>> recentsCreatorsProvider;
    public final Provider<Map<Class<?>, Provider<Service>>> serviceCreatorsProvider;

    public ContextComponentResolver_Factory(Provider<Map<Class<?>, Provider<Activity>>> provider, Provider<Map<Class<?>, Provider<Service>>> provider2, Provider<Map<Class<?>, Provider<RecentsImplementation>>> provider3, Provider<Map<Class<?>, Provider<BroadcastReceiver>>> provider4) {
        this.activityCreatorsProvider = provider;
        this.serviceCreatorsProvider = provider2;
        this.recentsCreatorsProvider = provider3;
        this.broadcastReceiverCreatorsProvider = provider4;
    }

    public ContextComponentResolver get() {
        return newInstance(this.activityCreatorsProvider.get(), this.serviceCreatorsProvider.get(), this.recentsCreatorsProvider.get(), this.broadcastReceiverCreatorsProvider.get());
    }

    public static ContextComponentResolver_Factory create(Provider<Map<Class<?>, Provider<Activity>>> provider, Provider<Map<Class<?>, Provider<Service>>> provider2, Provider<Map<Class<?>, Provider<RecentsImplementation>>> provider3, Provider<Map<Class<?>, Provider<BroadcastReceiver>>> provider4) {
        return new ContextComponentResolver_Factory(provider, provider2, provider3, provider4);
    }

    public static ContextComponentResolver newInstance(Map<Class<?>, Provider<Activity>> map, Map<Class<?>, Provider<Service>> map2, Map<Class<?>, Provider<RecentsImplementation>> map3, Map<Class<?>, Provider<BroadcastReceiver>> map4) {
        return new ContextComponentResolver(map, map2, map3, map4);
    }
}
