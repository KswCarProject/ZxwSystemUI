package com.android.systemui.biometrics;

/* compiled from: UdfpsControllerOverlay.kt */
public final class UdfpsControllerOverlayKt {
    public static final boolean isEnrollmentReason(int i) {
        return i == 1 || i == 2;
    }

    public static final boolean isImportantForAccessibility(int i) {
        return i == 1 || i == 2 || i == 3;
    }
}
