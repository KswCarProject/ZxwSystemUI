package com.android.systemui.util.wakelock;

import android.content.Context;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DelayedWakeLock_Builder_Factory implements Factory<DelayedWakeLock.Builder> {
    public final Provider<Context> contextProvider;

    public DelayedWakeLock_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public DelayedWakeLock.Builder get() {
        return newInstance(this.contextProvider.get());
    }

    public static DelayedWakeLock_Builder_Factory create(Provider<Context> provider) {
        return new DelayedWakeLock_Builder_Factory(provider);
    }

    public static DelayedWakeLock.Builder newInstance(Context context) {
        return new DelayedWakeLock.Builder(context);
    }
}
