package com.android.systemui.util.wakelock;

import android.content.Context;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class WakeLock_Builder_Factory implements Factory<WakeLock.Builder> {
    public final Provider<Context> contextProvider;

    public WakeLock_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public WakeLock.Builder get() {
        return newInstance(this.contextProvider.get());
    }

    public static WakeLock_Builder_Factory create(Provider<Context> provider) {
        return new WakeLock_Builder_Factory(provider);
    }

    public static WakeLock.Builder newInstance(Context context) {
        return new WakeLock.Builder(context);
    }
}
