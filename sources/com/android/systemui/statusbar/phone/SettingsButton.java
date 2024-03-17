package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.AlphaOptimizedImageView;

public class SettingsButton extends AlphaOptimizedImageView {
    public ObjectAnimator mAnimator;
    public final Runnable mLongPressCallback = new Runnable() {
        public void run() {
            SettingsButton.this.startAccelSpin();
        }
    };
    public float mSlop = ((float) ViewConfiguration.get(getContext()).getScaledTouchSlop());
    public boolean mUpToSpeed;

    public SettingsButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                float f = this.mSlop;
                if (x < (-f) || y < (-f) || x > ((float) getWidth()) + this.mSlop || y > ((float) getHeight()) + this.mSlop) {
                    cancelLongClick();
                }
            } else if (actionMasked == 3) {
                cancelLongClick();
            }
        } else if (this.mUpToSpeed) {
            startExitAnimation();
        } else {
            cancelLongClick();
        }
        return super.onTouchEvent(motionEvent);
    }

    public final void cancelLongClick() {
        cancelAnimation();
        this.mUpToSpeed = false;
        removeCallbacks(this.mLongPressCallback);
    }

    public final void cancelAnimation() {
        ObjectAnimator objectAnimator = this.mAnimator;
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
    }

    public final void startExitAnimation() {
        animate().translationX(((float) ((View) getParent().getParent()).getWidth()) - getX()).alpha(0.0f).setDuration(350).setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17563650)).setListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                SettingsButton.this.setAlpha(1.0f);
                SettingsButton.this.setTranslationX(0.0f);
                SettingsButton.this.cancelLongClick();
                SettingsButton.this.animate().setListener((Animator.AnimatorListener) null);
            }
        }).start();
    }

    public void startAccelSpin() {
        cancelAnimation();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ROTATION, new float[]{0.0f, 360.0f});
        this.mAnimator = ofFloat;
        ofFloat.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17563648));
        this.mAnimator.setDuration(750);
        this.mAnimator.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                SettingsButton.this.startContinuousSpin();
            }
        });
        this.mAnimator.start();
    }

    public void startContinuousSpin() {
        cancelAnimation();
        performHapticFeedback(0);
        this.mUpToSpeed = true;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ROTATION, new float[]{0.0f, 360.0f});
        this.mAnimator = ofFloat;
        ofFloat.setInterpolator(Interpolators.LINEAR);
        this.mAnimator.setDuration(375);
        this.mAnimator.setRepeatCount(-1);
        this.mAnimator.start();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(Button.class.getName());
    }
}
