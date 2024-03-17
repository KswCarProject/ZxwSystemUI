package com.android.systemui.flags;

import android.os.SystemProperties;
import org.jetbrains.annotations.NotNull;

/* compiled from: SystemPropertiesHelper.kt */
public class SystemPropertiesHelper {
    public final boolean getBoolean(@NotNull String str, boolean z) {
        return SystemProperties.getBoolean(str, z);
    }

    public final void setBoolean(@NotNull String str, boolean z) {
        SystemProperties.set(str, z ? "1" : "0");
    }

    public final void set(@NotNull String str, @NotNull String str2) {
        SystemProperties.set(str, str2);
    }

    public final void erase(@NotNull String str) {
        set(str, "");
    }
}
