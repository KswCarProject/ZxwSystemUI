package com.android.systemui.media;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;

/* compiled from: SmartspaceMediaData.kt */
public final class SmartspaceMediaDataKt {
    @NotNull
    public static final String TAG;

    static {
        String simpleName = Reflection.getOrCreateKotlinClass(SmartspaceMediaData.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName);
        TAG = simpleName;
    }
}
