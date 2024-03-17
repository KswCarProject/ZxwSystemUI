package com.android.systemui.controls.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.controls.ui.ToggleRangeBehavior;
import org.jetbrains.annotations.NotNull;

/* compiled from: ToggleRangeBehavior.kt */
public final class ToggleRangeBehavior$initialize$1 implements View.OnTouchListener {
    public final /* synthetic */ GestureDetector $gestureDetector;
    public final /* synthetic */ ToggleRangeBehavior.ToggleRangeGestureListener $gestureListener;
    public final /* synthetic */ ToggleRangeBehavior this$0;

    public ToggleRangeBehavior$initialize$1(GestureDetector gestureDetector, ToggleRangeBehavior.ToggleRangeGestureListener toggleRangeGestureListener, ToggleRangeBehavior toggleRangeBehavior) {
        this.$gestureDetector = gestureDetector;
        this.$gestureListener = toggleRangeGestureListener;
        this.this$0 = toggleRangeBehavior;
    }

    public final boolean onTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
        if (!this.$gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1 && this.$gestureListener.isDragging()) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
            this.$gestureListener.setDragging(false);
            this.this$0.endUpdateRange();
        }
        return false;
    }
}
