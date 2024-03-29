package com.android.systemui.util.leak;

import android.content.Context;
import com.android.systemui.util.leak.GarbageMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GarbageMonitor_Service_Factory implements Factory<GarbageMonitor.Service> {
    public final Provider<Context> contextProvider;
    public final Provider<GarbageMonitor> garbageMonitorProvider;

    public GarbageMonitor_Service_Factory(Provider<Context> provider, Provider<GarbageMonitor> provider2) {
        this.contextProvider = provider;
        this.garbageMonitorProvider = provider2;
    }

    public GarbageMonitor.Service get() {
        return newInstance(this.contextProvider.get(), this.garbageMonitorProvider.get());
    }

    public static GarbageMonitor_Service_Factory create(Provider<Context> provider, Provider<GarbageMonitor> provider2) {
        return new GarbageMonitor_Service_Factory(provider, provider2);
    }

    public static GarbageMonitor.Service newInstance(Context context, GarbageMonitor garbageMonitor) {
        return new GarbageMonitor.Service(context, garbageMonitor);
    }
}
