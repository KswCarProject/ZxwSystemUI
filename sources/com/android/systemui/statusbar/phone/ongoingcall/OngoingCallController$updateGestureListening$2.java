package com.android.systemui.statusbar.phone.ongoingcall;

import android.view.MotionEvent;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$updateGestureListening$2<T> implements Consumer {
    public final /* synthetic */ OngoingCallController this$0;

    public OngoingCallController$updateGestureListening$2(OngoingCallController ongoingCallController) {
        this.this$0 = ongoingCallController;
    }

    public final void accept(@NotNull SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler) {
        final OngoingCallController ongoingCallController = this.this$0;
        swipeStatusBarAwayGestureHandler.addOnGestureDetectedCallback("OngoingCallController", new Function1<MotionEvent, Unit>() {
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MotionEvent) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(@NotNull MotionEvent motionEvent) {
                ongoingCallController.onSwipeAwayGestureDetected();
            }
        });
    }
}
