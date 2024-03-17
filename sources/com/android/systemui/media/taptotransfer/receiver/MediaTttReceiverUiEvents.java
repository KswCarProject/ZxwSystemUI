package com.android.systemui.media.taptotransfer.receiver;

import com.android.internal.logging.UiEventLogger;

/* compiled from: MediaTttReceiverUiEventLogger.kt */
public enum MediaTttReceiverUiEvents implements UiEventLogger.UiEventEnum {
    MEDIA_TTT_RECEIVER_CLOSE_TO_SENDER(982),
    MEDIA_TTT_RECEIVER_FAR_FROM_SENDER(983);
    
    private final int metricId;

    /* access modifiers changed from: public */
    MediaTttReceiverUiEvents(int i) {
        this.metricId = i;
    }

    public int getId() {
        return this.metricId;
    }
}
