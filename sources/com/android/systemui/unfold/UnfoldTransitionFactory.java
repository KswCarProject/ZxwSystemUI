package com.android.systemui.unfold;

import android.content.Context;
import com.android.systemui.unfold.config.ResourceUnfoldTransitionConfig;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldTransitionFactory.kt */
public final class UnfoldTransitionFactory {
    @NotNull
    public static final UnfoldTransitionConfig createConfig(@NotNull Context context) {
        return new ResourceUnfoldTransitionConfig(context);
    }
}
