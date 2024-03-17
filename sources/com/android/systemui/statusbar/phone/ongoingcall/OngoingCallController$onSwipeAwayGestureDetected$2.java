package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$onSwipeAwayGestureDetected$2<T> implements Consumer {
    public static final OngoingCallController$onSwipeAwayGestureDetected$2<T> INSTANCE = new OngoingCallController$onSwipeAwayGestureDetected$2<>();

    public final void accept(@NotNull SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler) {
        swipeStatusBarAwayGestureHandler.removeOnGestureDetectedCallback("OngoingCallController");
    }
}
