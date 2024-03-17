package com.android.systemui.statusbar.gesture;

import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import org.jetbrains.annotations.NotNull;

/* compiled from: SwipeStatusBarAwayGestureHandler.kt */
public class SwipeStatusBarAwayGestureHandler extends GenericGestureDetector {
    @NotNull
    public final SwipeStatusBarAwayGestureLogger logger;
    public boolean monitoringCurrentTouch;
    public long startTime;
    public float startY;
    @NotNull
    public final StatusBarWindowController statusBarWindowController;
    public int swipeDistanceThreshold;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SwipeStatusBarAwayGestureHandler(@org.jetbrains.annotations.NotNull android.content.Context r2, @org.jetbrains.annotations.NotNull com.android.systemui.statusbar.window.StatusBarWindowController r3, @org.jetbrains.annotations.NotNull com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger r4) {
        /*
            r1 = this;
            java.lang.Class<com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler> r0 = com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler.class
            kotlin.reflect.KClass r0 = kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(r0)
            java.lang.String r0 = r0.getSimpleName()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1.<init>(r0)
            r1.statusBarWindowController = r3
            r1.logger = r4
            android.content.res.Resources r2 = r2.getResources()
            r3 = 17105562(0x105029a, float:2.4430109E-38)
            int r2 = r2.getDimensionPixelSize(r3)
            r1.swipeDistanceThreshold = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler.<init>(android.content.Context, com.android.systemui.statusbar.window.StatusBarWindowController, com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger):void");
    }

    public void onInputEvent(@NotNull InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            return;
                        }
                    } else if (this.monitoringCurrentTouch) {
                        float y = motionEvent.getY();
                        float f = this.startY;
                        if (y < f && f - motionEvent.getY() >= ((float) this.swipeDistanceThreshold) && motionEvent.getEventTime() - this.startTime < 500) {
                            this.monitoringCurrentTouch = false;
                            this.logger.logGestureDetected((int) motionEvent.getY());
                            onGestureDetected$frameworks__base__packages__SystemUI__android_common__SystemUI_core(motionEvent);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                }
                if (this.monitoringCurrentTouch) {
                    this.logger.logGestureDetectionEndedWithoutTriggering((int) motionEvent.getY());
                }
                this.monitoringCurrentTouch = false;
            } else if (motionEvent.getY() < ((float) this.statusBarWindowController.getStatusBarHeight()) || motionEvent.getY() > ((float) (this.statusBarWindowController.getStatusBarHeight() * 3))) {
                this.monitoringCurrentTouch = false;
            } else {
                this.logger.logGestureDetectionStarted((int) motionEvent.getY());
                this.startY = motionEvent.getY();
                this.startTime = motionEvent.getEventTime();
                this.monitoringCurrentTouch = true;
            }
        }
    }

    public void startGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        super.startGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        this.logger.logInputListeningStarted();
    }

    public void stopGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        super.stopGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        this.logger.logInputListeningStopped();
    }
}
