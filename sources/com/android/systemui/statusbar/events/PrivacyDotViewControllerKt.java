package com.android.systemui.statusbar.events;

/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewControllerKt {
    public static final void dlog(String str) {
    }

    public static final int toGravity(int i) {
        if (i == 0) {
            return 51;
        }
        if (i == 1) {
            return 53;
        }
        if (i == 2) {
            return 85;
        }
        if (i == 3) {
            return 83;
        }
        throw new IllegalArgumentException("Not a corner");
    }

    public static final int innerGravity(int i) {
        if (i != 0) {
            if (i == 1 || i == 2) {
                return 19;
            }
            if (i != 3) {
                throw new IllegalArgumentException("Not a corner");
            }
        }
        return 21;
    }
}
