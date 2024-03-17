package com.android.systemui.unfold.config;

import android.content.Context;
import android.os.SystemProperties;
import org.jetbrains.annotations.NotNull;

/* compiled from: ResourceUnfoldTransitionConfig.kt */
public final class ResourceUnfoldTransitionConfig implements UnfoldTransitionConfig {
    @NotNull
    public final Context context;

    public ResourceUnfoldTransitionConfig(@NotNull Context context2) {
        this.context = context2;
    }

    public boolean isEnabled() {
        return readIsEnabledResource() && isPropertyEnabled();
    }

    public boolean isHingeAngleEnabled() {
        return readIsHingeAngleEnabled();
    }

    public final boolean isPropertyEnabled() {
        return SystemProperties.getInt("persist.unfold.transition_enabled", 1) == 1;
    }

    public final boolean readIsEnabledResource() {
        return this.context.getResources().getBoolean(17891806);
    }

    public final boolean readIsHingeAngleEnabled() {
        return this.context.getResources().getBoolean(17891807);
    }
}
