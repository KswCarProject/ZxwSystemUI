package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import org.jetbrains.annotations.NotNull;

/* compiled from: OngoingCallFlags.kt */
public final class OngoingCallFlags {
    @NotNull
    public final FeatureFlags featureFlags;

    public OngoingCallFlags(@NotNull FeatureFlags featureFlags2) {
        this.featureFlags = featureFlags2;
    }

    public final boolean isStatusBarChipEnabled() {
        return this.featureFlags.isEnabled(Flags.ONGOING_CALL_STATUS_BAR_CHIP);
    }

    public final boolean isInImmersiveEnabled() {
        return isStatusBarChipEnabled() && this.featureFlags.isEnabled(Flags.ONGOING_CALL_IN_IMMERSIVE);
    }

    public final boolean isInImmersiveChipTapEnabled() {
        return isInImmersiveEnabled() && this.featureFlags.isEnabled(Flags.ONGOING_CALL_IN_IMMERSIVE_CHIP_TAP);
    }
}
