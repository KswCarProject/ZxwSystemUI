package com.android.systemui.media.taptotransfer.receiver;

import com.android.internal.logging.UiEventLogger;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttReceiverUiEventLogger.kt */
public final class MediaTttReceiverUiEventLogger {
    @NotNull
    public final UiEventLogger logger;

    public MediaTttReceiverUiEventLogger(@NotNull UiEventLogger uiEventLogger) {
        this.logger = uiEventLogger;
    }

    public final void logReceiverStateChange(@NotNull ChipStateReceiver chipStateReceiver) {
        this.logger.log(chipStateReceiver.getUiEvent());
    }
}
