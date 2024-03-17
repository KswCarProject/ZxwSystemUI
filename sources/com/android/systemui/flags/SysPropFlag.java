package com.android.systemui.flags;

import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public interface SysPropFlag<T> extends Flag<T> {
    @NotNull
    String getName();
}
