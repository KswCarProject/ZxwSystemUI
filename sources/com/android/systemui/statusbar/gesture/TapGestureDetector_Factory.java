package com.android.systemui.statusbar.gesture;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TapGestureDetector_Factory implements Factory<TapGestureDetector> {
    public final Provider<Context> contextProvider;

    public TapGestureDetector_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public TapGestureDetector get() {
        return newInstance(this.contextProvider.get());
    }

    public static TapGestureDetector_Factory create(Provider<Context> provider) {
        return new TapGestureDetector_Factory(provider);
    }

    public static TapGestureDetector newInstance(Context context) {
        return new TapGestureDetector(context);
    }
}
