package com.android.systemui.statusbar.policy;

import android.view.accessibility.AccessibilityManager;

public class AccessibilityManagerWrapper implements CallbackController<AccessibilityManager.AccessibilityServicesStateChangeListener> {
    public final AccessibilityManager mAccessibilityManager;

    public AccessibilityManagerWrapper(AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }

    public void addCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.addAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener);
    }

    public void removeCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.removeAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener);
    }

    public int getRecommendedTimeoutMillis(int i, int i2) {
        return this.mAccessibilityManager.getRecommendedTimeoutMillis(i, i2);
    }
}
