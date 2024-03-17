package com.android.systemui.media.taptotransfer.sender;

import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttSenderUiEventLogger.kt */
public final class MediaTttSenderUiEventLogger {
    @NotNull
    public final UiEventLogger logger;

    public MediaTttSenderUiEventLogger(@NotNull UiEventLogger uiEventLogger) {
        this.logger = uiEventLogger;
    }

    public final void logSenderStateChange(@NotNull ChipStateSender chipStateSender) {
        this.logger.log(chipStateSender.getUiEvent());
    }

    public final void logUndoClicked(@NotNull UiEventLogger.UiEventEnum uiEventEnum) {
        if (!(uiEventEnum == MediaTttSenderUiEvents.MEDIA_TTT_SENDER_UNDO_TRANSFER_TO_RECEIVER_CLICKED || uiEventEnum == MediaTttSenderUiEvents.MEDIA_TTT_SENDER_UNDO_TRANSFER_TO_THIS_DEVICE_CLICKED)) {
            String simpleName = Reflection.getOrCreateKotlinClass(MediaTttSenderUiEventLogger.class).getSimpleName();
            Intrinsics.checkNotNull(simpleName);
            Log.w(simpleName, "Must pass an undo-specific UiEvent.");
            return;
        }
        this.logger.log(uiEventEnum);
    }
}
