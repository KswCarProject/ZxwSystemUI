package androidx.core.os;

import android.annotation.SuppressLint;

public class BuildCompat {
    @SuppressLint({"RestrictedApi"})
    @Deprecated
    public static boolean isAtLeastS() {
        return true;
    }

    public static boolean isAtLeastT() {
        return true;
    }
}
