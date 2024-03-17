package com.android.wm.shell.bubbles;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import org.jetbrains.annotations.NotNull;

/* compiled from: RelativeTouchListener.kt */
public abstract class RelativeTouchListener implements View.OnTouchListener {
    public boolean movedEnough;
    public boolean performedLongClick;
    @NotNull
    public final PointF touchDown = new PointF();
    public int touchSlop = -1;
    public final VelocityTracker velocityTracker = VelocityTracker.obtain();
    @NotNull
    public final PointF viewPositionOnTouchDown = new PointF();

    public abstract boolean onDown(@NotNull View view, @NotNull MotionEvent motionEvent);

    public abstract void onMove(@NotNull View view, @NotNull MotionEvent motionEvent, float f, float f2, float f3, float f4);

    public abstract void onUp(@NotNull View view, @NotNull MotionEvent motionEvent, float f, float f2, float f3, float f4, float f5, float f6);

    public boolean onTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
        addMovement(motionEvent);
        float rawX = motionEvent.getRawX() - this.touchDown.x;
        float rawY = motionEvent.getRawY() - this.touchDown.y;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                if (this.movedEnough) {
                    this.velocityTracker.computeCurrentVelocity(1000);
                    PointF pointF = this.viewPositionOnTouchDown;
                    onUp(view, motionEvent, pointF.x, pointF.y, rawX, rawY, this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                } else if (!this.performedLongClick) {
                    view.performClick();
                } else {
                    view.getHandler().removeCallbacksAndMessages((Object) null);
                }
                this.velocityTracker.clear();
                this.movedEnough = false;
            } else if (action == 2) {
                if (!this.movedEnough && ((float) Math.hypot((double) rawX, (double) rawY)) > ((float) this.touchSlop) && !this.performedLongClick) {
                    this.movedEnough = true;
                    view.getHandler().removeCallbacksAndMessages((Object) null);
                }
                if (this.movedEnough) {
                    PointF pointF2 = this.viewPositionOnTouchDown;
                    onMove(view, motionEvent, pointF2.x, pointF2.y, rawX, rawY);
                }
            }
        } else if (!onDown(view, motionEvent)) {
            return false;
        } else {
            this.touchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
            this.touchDown.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.viewPositionOnTouchDown.set(view.getTranslationX(), view.getTranslationY());
            this.performedLongClick = false;
            view.getHandler().postDelayed(new RelativeTouchListener$onTouch$1(view, this), (long) ViewConfiguration.getLongPressTimeout());
        }
        return true;
    }

    public final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }
}
