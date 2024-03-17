package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifPipelineFlags.kt */
public final class NotifPipelineFlags {
    @NotNull
    public final Context context;
    @NotNull
    public final FeatureFlags featureFlags;

    public NotifPipelineFlags(@NotNull Context context2, @NotNull FeatureFlags featureFlags2) {
        this.context = context2;
        this.featureFlags = featureFlags2;
    }

    public final boolean checkLegacyPipelineEnabled() {
        if (!isNewPipelineEnabled()) {
            return true;
        }
        Toast.makeText(this.context, "Old pipeline code running!", 0).show();
        if (!this.featureFlags.isEnabled(Flags.NEW_PIPELINE_CRASH_ON_CALL_TO_OLD_PIPELINE)) {
            Log.d("NotifPipeline", "Old pipeline code running with new pipeline enabled", new Exception());
            return false;
        }
        throw new RuntimeException("Old pipeline code running with new pipeline enabled");
    }

    public final void assertLegacyPipelineEnabled() {
        if (!(!isNewPipelineEnabled())) {
            throw new IllegalStateException("Old pipeline code running w/ new pipeline enabled".toString());
        }
    }

    public final boolean isNewPipelineEnabled() {
        return this.featureFlags.isEnabled(Flags.NEW_NOTIFICATION_PIPELINE_RENDERING);
    }

    public final boolean isDevLoggingEnabled() {
        return this.featureFlags.isEnabled(Flags.NOTIFICATION_PIPELINE_DEVELOPER_LOGGING);
    }

    public final boolean isSmartspaceDedupingEnabled() {
        return this.featureFlags.isEnabled(Flags.SMARTSPACE) && this.featureFlags.isEnabled(Flags.SMARTSPACE_DEDUPING);
    }
}
