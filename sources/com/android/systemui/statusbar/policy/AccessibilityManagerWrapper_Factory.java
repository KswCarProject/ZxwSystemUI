package com.android.systemui.statusbar.policy;

import android.view.accessibility.AccessibilityManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AccessibilityManagerWrapper_Factory implements Factory<AccessibilityManagerWrapper> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;

    public AccessibilityManagerWrapper_Factory(Provider<AccessibilityManager> provider) {
        this.accessibilityManagerProvider = provider;
    }

    public AccessibilityManagerWrapper get() {
        return newInstance(this.accessibilityManagerProvider.get());
    }

    public static AccessibilityManagerWrapper_Factory create(Provider<AccessibilityManager> provider) {
        return new AccessibilityManagerWrapper_Factory(provider);
    }

    public static AccessibilityManagerWrapper newInstance(AccessibilityManager accessibilityManager) {
        return new AccessibilityManagerWrapper(accessibilityManager);
    }
}
