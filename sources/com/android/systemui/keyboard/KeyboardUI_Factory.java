package com.android.systemui.keyboard;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyboardUI_Factory implements Factory<KeyboardUI> {
    public final Provider<Context> contextProvider;

    public KeyboardUI_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public KeyboardUI get() {
        return newInstance(this.contextProvider.get());
    }

    public static KeyboardUI_Factory create(Provider<Context> provider) {
        return new KeyboardUI_Factory(provider);
    }

    public static KeyboardUI newInstance(Context context) {
        return new KeyboardUI(context);
    }
}
