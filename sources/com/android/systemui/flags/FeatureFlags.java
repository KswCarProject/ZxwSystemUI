package com.android.systemui.flags;

import org.jetbrains.annotations.NotNull;

/* compiled from: FeatureFlags.kt */
public interface FeatureFlags extends FlagListenable {
    boolean isEnabled(@NotNull BooleanFlag booleanFlag);

    boolean isEnabled(@NotNull ResourceBooleanFlag resourceBooleanFlag);
}
