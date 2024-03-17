package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AccessibilityFloatingMenuController_Factory implements Factory<AccessibilityFloatingMenuController> {
    public final Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
    public final Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
    public final Provider<Context> contextProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public AccessibilityFloatingMenuController_Factory(Provider<Context> provider, Provider<AccessibilityButtonTargetsObserver> provider2, Provider<AccessibilityButtonModeObserver> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        this.contextProvider = provider;
        this.accessibilityButtonTargetsObserverProvider = provider2;
        this.accessibilityButtonModeObserverProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
    }

    public AccessibilityFloatingMenuController get() {
        return newInstance(this.contextProvider.get(), this.accessibilityButtonTargetsObserverProvider.get(), this.accessibilityButtonModeObserverProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static AccessibilityFloatingMenuController_Factory create(Provider<Context> provider, Provider<AccessibilityButtonTargetsObserver> provider2, Provider<AccessibilityButtonModeObserver> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        return new AccessibilityFloatingMenuController_Factory(provider, provider2, provider3, provider4);
    }

    public static AccessibilityFloatingMenuController newInstance(Context context, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, AccessibilityButtonModeObserver accessibilityButtonModeObserver, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new AccessibilityFloatingMenuController(context, accessibilityButtonTargetsObserver, accessibilityButtonModeObserver, keyguardUpdateMonitor);
    }
}
