package com.android.systemui.media;

import android.app.StatusBarManager;
import android.os.UserHandle;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaFlags.kt */
public final class MediaFlags {
    @NotNull
    public final FeatureFlags featureFlags;

    public MediaFlags(@NotNull FeatureFlags featureFlags2) {
        this.featureFlags = featureFlags2;
    }

    public final boolean areMediaSessionActionsEnabled(@NotNull String str, @NotNull UserHandle userHandle) {
        return StatusBarManager.useMediaSessionActionsForApp(str, userHandle) || this.featureFlags.isEnabled(Flags.MEDIA_SESSION_ACTIONS);
    }

    public final boolean areMuteAwaitConnectionsEnabled() {
        return this.featureFlags.isEnabled(Flags.MEDIA_MUTE_AWAIT);
    }

    public final boolean areNearbyMediaDevicesEnabled() {
        return this.featureFlags.isEnabled(Flags.MEDIA_NEARBY_DEVICES);
    }
}
