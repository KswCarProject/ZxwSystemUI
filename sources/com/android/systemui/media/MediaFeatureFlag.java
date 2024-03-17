package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.util.Utils;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaFeatureFlag.kt */
public final class MediaFeatureFlag {
    @NotNull
    public final Context context;

    public MediaFeatureFlag(@NotNull Context context2) {
        this.context = context2;
    }

    public final boolean getEnabled() {
        return Utils.useQsMediaPlayer(this.context);
    }
}
