package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$drawable;
import com.android.systemui.animation.Interpolators;

public class NotificationGuts extends FrameLayout {
    public int mActualHeight;
    public Drawable mBackground;
    public int mClipBottomAmount;
    public int mClipTopAmount;
    public OnGutsClosedListener mClosedListener;
    public boolean mExposed;
    public Runnable mFalsingCheck;
    public GutsContent mGutsContent;
    public View.AccessibilityDelegate mGutsContentAccessibilityDelegate;
    public Handler mHandler;
    public OnHeightChangedListener mHeightListener;
    public boolean mNeedsFalsingProtection;

    public interface GutsContent {
        int getActualHeight();

        View getContentView();

        boolean handleCloseControls(boolean z, boolean z2);

        boolean isLeavebehind() {
            return false;
        }

        boolean needsFalsingProtection();

        void onFinishedClosing() {
        }

        void setAccessibilityDelegate(View.AccessibilityDelegate accessibilityDelegate);

        void setGutsParent(NotificationGuts notificationGuts);

        boolean shouldBeSaved();

        boolean willBeRemoved();
    }

    public interface OnGutsClosedListener {
        void onGutsClosed(NotificationGuts notificationGuts);
    }

    public interface OnHeightChangedListener {
        void onHeightChanged(NotificationGuts notificationGuts);
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public NotificationGuts(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mGutsContentAccessibilityDelegate = new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK);
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (super.performAccessibilityAction(view, i, bundle)) {
                    return true;
                }
                if (i != 32) {
                    return false;
                }
                NotificationGuts.this.closeControls(view, false);
                return true;
            }
        };
        setWillNotDraw(false);
        this.mHandler = new Handler();
        this.mFalsingCheck = new Runnable() {
            public void run() {
                if (NotificationGuts.this.mNeedsFalsingProtection && NotificationGuts.this.mExposed) {
                    NotificationGuts.this.closeControls(-1, -1, false, false);
                }
            }
        };
    }

    public NotificationGuts(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setGutsContent(GutsContent gutsContent) {
        gutsContent.setGutsParent(this);
        gutsContent.setAccessibilityDelegate(this.mGutsContentAccessibilityDelegate);
        this.mGutsContent = gutsContent;
        removeAllViews();
        addView(this.mGutsContent.getContentView());
    }

    public GutsContent getGutsContent() {
        return this.mGutsContent;
    }

    public void resetFalsingCheck() {
        this.mHandler.removeCallbacks(this.mFalsingCheck);
        if (this.mNeedsFalsingProtection && this.mExposed) {
            this.mHandler.postDelayed(this.mFalsingCheck, 8000);
        }
    }

    public void onDraw(Canvas canvas) {
        draw(canvas, this.mBackground);
    }

    public final void draw(Canvas canvas, Drawable drawable) {
        int i = this.mClipTopAmount;
        int i2 = this.mActualHeight - this.mClipBottomAmount;
        if (drawable != null && i < i2) {
            drawable.setBounds(0, i, getWidth(), i2);
            drawable.draw(canvas);
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        Drawable drawable = this.mContext.getDrawable(R$drawable.notification_guts_bg);
        this.mBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mBackground;
    }

    public void drawableStateChanged() {
        drawableStateChanged(this.mBackground);
    }

    public final void drawableStateChanged(Drawable drawable) {
        if (drawable != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
    }

    public void drawableHotspotChanged(float f, float f2) {
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setHotspot(f, f2);
        }
    }

    public void openControls(boolean z, int i, int i2, boolean z2, Runnable runnable) {
        animateOpen(z, i, i2, runnable);
        setExposed(true, z2);
    }

    public void closeControls(boolean z, boolean z2, int i, int i2, boolean z3) {
        GutsContent gutsContent = this.mGutsContent;
        if (gutsContent == null) {
            return;
        }
        if ((gutsContent.isLeavebehind() && z) || (!this.mGutsContent.isLeavebehind() && z2)) {
            closeControls(i, i2, this.mGutsContent.shouldBeSaved(), z3);
        }
    }

    public void closeControls(View view, boolean z) {
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        getLocationOnScreen(iArr);
        view.getLocationOnScreen(iArr2);
        closeControls((iArr2[0] - iArr[0]) + (view.getWidth() / 2), (iArr2[1] - iArr[1]) + (view.getHeight() / 2), z, false);
    }

    public final void closeControls(int i, int i2, boolean z, boolean z2) {
        if (getWindowToken() == null) {
            OnGutsClosedListener onGutsClosedListener = this.mClosedListener;
            if (onGutsClosedListener != null) {
                onGutsClosedListener.onGutsClosed(this);
                return;
            }
            return;
        }
        GutsContent gutsContent = this.mGutsContent;
        if (gutsContent == null || !gutsContent.handleCloseControls(z, z2)) {
            animateClose(i, i2, true);
            setExposed(false, this.mNeedsFalsingProtection);
            OnGutsClosedListener onGutsClosedListener2 = this.mClosedListener;
            if (onGutsClosedListener2 != null) {
                onGutsClosedListener2.onGutsClosed(this);
            }
        }
    }

    public final void animateOpen(boolean z, int i, int i2, Runnable runnable) {
        if (!isAttachedToWindow()) {
            Log.w("NotificationGuts", "Failed to animate guts open");
        } else if (z) {
            setAlpha(1.0f);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, i, i2, 0.0f, (float) Math.hypot((double) Math.max(getWidth() - i, i), (double) Math.max(getHeight() - i2, i2)));
            createCircularReveal.setDuration(360);
            createCircularReveal.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            createCircularReveal.addListener(new AnimateOpenListener(runnable));
            createCircularReveal.start();
        } else {
            setAlpha(0.0f);
            animate().alpha(1.0f).setDuration(240).setInterpolator(Interpolators.ALPHA_IN).setListener(new AnimateOpenListener(runnable)).start();
        }
    }

    @VisibleForTesting
    public void animateClose(int i, int i2, boolean z) {
        if (!isAttachedToWindow()) {
            Log.w("NotificationGuts", "Failed to animate guts close");
            this.mGutsContent.onFinishedClosing();
        } else if (z) {
            if (i == -1 || i2 == -1) {
                i = (getLeft() + getRight()) / 2;
                i2 = getTop() + (getHeight() / 2);
            }
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, i, i2, (float) Math.hypot((double) Math.max(getWidth() - i, i), (double) Math.max(getHeight() - i2, i2)), 0.0f);
            createCircularReveal.setDuration(360);
            createCircularReveal.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
            createCircularReveal.addListener(new AnimateCloseListener(this, this.mGutsContent));
            createCircularReveal.start();
        } else {
            animate().alpha(0.0f).setDuration(240).setInterpolator(Interpolators.ALPHA_OUT).setListener(new AnimateCloseListener(this, this.mGutsContent)).start();
        }
    }

    public void setActualHeight(int i) {
        this.mActualHeight = i;
        invalidate();
    }

    public int getIntrinsicHeight() {
        GutsContent gutsContent = this.mGutsContent;
        return (gutsContent == null || !this.mExposed) ? getHeight() : gutsContent.getActualHeight();
    }

    public void setClipTopAmount(int i) {
        this.mClipTopAmount = i;
        invalidate();
    }

    public void setClipBottomAmount(int i) {
        this.mClipBottomAmount = i;
        invalidate();
    }

    public void setClosedListener(OnGutsClosedListener onGutsClosedListener) {
        this.mClosedListener = onGutsClosedListener;
    }

    public void setHeightChangedListener(OnHeightChangedListener onHeightChangedListener) {
        this.mHeightListener = onHeightChangedListener;
    }

    public void onHeightChanged() {
        OnHeightChangedListener onHeightChangedListener = this.mHeightListener;
        if (onHeightChangedListener != null) {
            onHeightChangedListener.onHeightChanged(this);
        }
    }

    @VisibleForTesting
    public void setExposed(boolean z, boolean z2) {
        GutsContent gutsContent;
        boolean z3 = this.mExposed;
        this.mExposed = z;
        this.mNeedsFalsingProtection = z2;
        if (!z || !z2) {
            this.mHandler.removeCallbacks(this.mFalsingCheck);
        } else {
            resetFalsingCheck();
        }
        if (z3 != this.mExposed && (gutsContent = this.mGutsContent) != null) {
            View contentView = gutsContent.getContentView();
            contentView.sendAccessibilityEvent(32);
            if (this.mExposed) {
                contentView.requestAccessibilityFocus();
            }
        }
    }

    public boolean willBeRemoved() {
        GutsContent gutsContent = this.mGutsContent;
        if (gutsContent != null) {
            return gutsContent.willBeRemoved();
        }
        return false;
    }

    public boolean isExposed() {
        return this.mExposed;
    }

    public boolean isLeavebehind() {
        GutsContent gutsContent = this.mGutsContent;
        return gutsContent != null && gutsContent.isLeavebehind();
    }

    public static class AnimateOpenListener extends AnimatorListenerAdapter {
        public final Runnable mOnAnimationEnd;

        public AnimateOpenListener(Runnable runnable) {
            this.mOnAnimationEnd = runnable;
        }

        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            Runnable runnable = this.mOnAnimationEnd;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public class AnimateCloseListener extends AnimatorListenerAdapter {
        public final GutsContent mGutsContent;
        public final View mView;

        public AnimateCloseListener(View view, GutsContent gutsContent) {
            this.mView = view;
            this.mGutsContent = gutsContent;
        }

        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (!NotificationGuts.this.isExposed()) {
                this.mView.setVisibility(8);
                this.mGutsContent.onFinishedClosing();
            }
        }
    }
}