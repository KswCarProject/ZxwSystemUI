package com.android.systemui.media.muteawait;

import android.content.Context;
import com.android.settingslib.media.DeviceIconUtil;
import com.android.settingslib.media.LocalMediaManager;
import com.android.systemui.media.MediaFlags;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaMuteAwaitConnectionManagerFactory.kt */
public final class MediaMuteAwaitConnectionManagerFactory {
    @NotNull
    public final Context context;
    @NotNull
    public final DeviceIconUtil deviceIconUtil = new DeviceIconUtil();
    @NotNull
    public final MediaMuteAwaitLogger logger;
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final MediaFlags mediaFlags;

    public MediaMuteAwaitConnectionManagerFactory(@NotNull MediaFlags mediaFlags2, @NotNull Context context2, @NotNull MediaMuteAwaitLogger mediaMuteAwaitLogger, @NotNull Executor executor) {
        this.mediaFlags = mediaFlags2;
        this.context = context2;
        this.logger = mediaMuteAwaitLogger;
        this.mainExecutor = executor;
    }

    @Nullable
    public final MediaMuteAwaitConnectionManager create(@NotNull LocalMediaManager localMediaManager) {
        if (!this.mediaFlags.areMuteAwaitConnectionsEnabled()) {
            return null;
        }
        return new MediaMuteAwaitConnectionManager(this.mainExecutor, localMediaManager, this.context, this.deviceIconUtil, this.logger);
    }
}
