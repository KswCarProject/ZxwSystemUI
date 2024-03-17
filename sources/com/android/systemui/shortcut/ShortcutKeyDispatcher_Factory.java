package com.android.systemui.shortcut;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ShortcutKeyDispatcher_Factory implements Factory<ShortcutKeyDispatcher> {
    public final Provider<Context> contextProvider;

    public ShortcutKeyDispatcher_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public ShortcutKeyDispatcher get() {
        return newInstance(this.contextProvider.get());
    }

    public static ShortcutKeyDispatcher_Factory create(Provider<Context> provider) {
        return new ShortcutKeyDispatcher_Factory(provider);
    }

    public static ShortcutKeyDispatcher newInstance(Context context) {
        return new ShortcutKeyDispatcher(context);
    }
}
