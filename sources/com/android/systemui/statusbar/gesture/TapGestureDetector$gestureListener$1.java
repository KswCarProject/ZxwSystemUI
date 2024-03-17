package com.android.systemui.statusbar.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;
import org.jetbrains.annotations.NotNull;

/* compiled from: TapGestureDetector.kt */
public final class TapGestureDetector$gestureListener$1 extends GestureDetector.SimpleOnGestureListener {
    public final /* synthetic */ TapGestureDetector this$0;

    public TapGestureDetector$gestureListener$1(TapGestureDetector tapGestureDetector) {
        this.this$0 = tapGestureDetector;
    }

    public boolean onSingleTapUp(@NotNull MotionEvent motionEvent) {
        this.this$0.onGestureDetected$frameworks__base__packages__SystemUI__android_common__SystemUI_core(motionEvent);
        return true;
    }
}
