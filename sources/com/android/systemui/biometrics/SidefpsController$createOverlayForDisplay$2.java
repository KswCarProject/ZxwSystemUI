package com.android.systemui.biometrics;

import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import org.jetbrains.annotations.NotNull;

/* compiled from: SidefpsController.kt */
public final class SidefpsController$createOverlayForDisplay$2 extends View.AccessibilityDelegate {
    public boolean dispatchPopulateAccessibilityEvent(@NotNull View view, @NotNull AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 32) {
            return true;
        }
        return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }
}
