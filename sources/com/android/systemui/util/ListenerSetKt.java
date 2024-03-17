package com.android.systemui.util;

import org.jetbrains.annotations.NotNull;

/* compiled from: ListenerSet.kt */
public final class ListenerSetKt {
    public static final <T> boolean isNotEmpty(@NotNull ListenerSet<T> listenerSet) {
        return !listenerSet.isEmpty();
    }
}
