package com.android.systemui.statusbar.policy;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AccessibilityController_Factory implements Factory<AccessibilityController> {
    public final Provider<Context> contextProvider;

    public AccessibilityController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public AccessibilityController get() {
        return newInstance(this.contextProvider.get());
    }

    public static AccessibilityController_Factory create(Provider<Context> provider) {
        return new AccessibilityController_Factory(provider);
    }

    public static AccessibilityController newInstance(Context context) {
        return new AccessibilityController(context);
    }
}
