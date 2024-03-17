package com.android.systemui.statusbar.gesture;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SwipeStatusBarAwayGestureLogger.kt */
public final class SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2 INSTANCE = new SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2();

    public SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Input listening stopped ";
    }
}
