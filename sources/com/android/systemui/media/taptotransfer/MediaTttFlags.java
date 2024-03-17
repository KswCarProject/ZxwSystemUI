package com.android.systemui.media.taptotransfer;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttFlags.kt */
public final class MediaTttFlags {
    @NotNull
    public final FeatureFlags featureFlags;

    public MediaTttFlags(@NotNull FeatureFlags featureFlags2) {
        this.featureFlags = featureFlags2;
    }

    public final boolean isMediaTttEnabled() {
        return this.featureFlags.isEnabled(Flags.MEDIA_TAP_TO_TRANSFER);
    }
}
