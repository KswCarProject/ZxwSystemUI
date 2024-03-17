package com.android.systemui.screenshot;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TimeoutHandler_Factory implements Factory<TimeoutHandler> {
    public final Provider<Context> contextProvider;

    public TimeoutHandler_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public TimeoutHandler get() {
        return newInstance(this.contextProvider.get());
    }

    public static TimeoutHandler_Factory create(Provider<Context> provider) {
        return new TimeoutHandler_Factory(provider);
    }

    public static TimeoutHandler newInstance(Context context) {
        return new TimeoutHandler(context);
    }
}
