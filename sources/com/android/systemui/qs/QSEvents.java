package com.android.systemui.qs;

import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: QSEvents.kt */
public final class QSEvents {
    @NotNull
    public static final QSEvents INSTANCE = new QSEvents();
    @NotNull
    public static UiEventLogger qsUiEventsLogger = new UiEventLoggerImpl();

    @NotNull
    public final UiEventLogger getQsUiEventsLogger() {
        return qsUiEventsLogger;
    }
}
