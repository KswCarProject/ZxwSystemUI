package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.wm.shell.animation.FlingAnimationUtils;

public class KeyguardAffordanceHelper {
    public Runnable mAnimationEndRunnable = new Runnable() {
        public void run() {
            KeyguardAffordanceHelper.this.mCallback.onAnimationToSideEnded();
        }
    };
    public final Callback mCallback;
    public final Context mContext;
    public final FalsingManager mFalsingManager;
    public FlingAnimationUtils mFlingAnimationUtils;
    public AnimatorListenerAdapter mFlingEndListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animator) {
            KeyguardAffordanceHelper.this.mSwipeAnimator = null;
            KeyguardAffordanceHelper.this.mSwipingInProgress = false;
            KeyguardAffordanceHelper.this.mTargetedView = null;
        }
    };
    public int mHintGrowAmount;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public KeyguardAffordanceView mLeftIcon;
    public int mMinBackgroundRadius;
    public int mMinFlingVelocity;
    public int mMinTranslationAmount;
    public boolean mMotionCancelled;
    public KeyguardAffordanceView mRightIcon;
    public Animator mSwipeAnimator;
    public boolean mSwipingInProgress;
    public View mTargetedView;
    public int mTouchSlop;
    public boolean mTouchSlopExeeded;
    public int mTouchTargetSize;
    public float mTranslation;
    public float mTranslationOnDown;
    public VelocityTracker mVelocityTracker;

    public interface Callback {
        float getAffordanceFalsingFactor();

        KeyguardAffordanceView getLeftIcon();

        View getLeftPreview();

        float getMaxTranslationDistance();

        KeyguardAffordanceView getRightIcon();

        View getRightPreview();

        boolean needsAntiFalsing();

        void onAnimationToSideEnded();

        void onAnimationToSideStarted(boolean z, float f, float f2);

        void onIconClicked(boolean z);

        void onSwipingAborted();

        void onSwipingStarted(boolean z);
    }

    public KeyguardAffordanceHelper(Callback callback, Context context, FalsingManager falsingManager) {
        this.mContext = context;
        this.mCallback = callback;
        initIcons();
        KeyguardAffordanceView keyguardAffordanceView = this.mLeftIcon;
        updateIcon(keyguardAffordanceView, 0.0f, keyguardAffordanceView.getRestingAlpha(), false, false, true, false);
        KeyguardAffordanceView keyguardAffordanceView2 = this.mRightIcon;
        updateIcon(keyguardAffordanceView2, 0.0f, keyguardAffordanceView2.getRestingAlpha(), false, false, true, false);
        this.mFalsingManager = falsingManager;
        initDimens();
    }

    public final void initDimens() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMinTranslationAmount = this.mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_min_swipe_amount);
        this.mMinBackgroundRadius = this.mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_affordance_min_background_radius);
        this.mTouchTargetSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_affordance_touch_target_size);
        this.mHintGrowAmount = this.mContext.getResources().getDimensionPixelSize(R$dimen.hint_grow_amount_sideways);
        this.mFlingAnimationUtils = new FlingAnimationUtils(this.mContext.getResources().getDisplayMetrics(), 0.4f);
    }

    public final void initIcons() {
        this.mLeftIcon = this.mCallback.getLeftIcon();
        this.mRightIcon = this.mCallback.getRightIcon();
        updatePreviews();
    }

    public void updatePreviews() {
        this.mLeftIcon.setPreviewView(this.mCallback.getLeftPreview());
        this.mRightIcon.setPreviewView(this.mCallback.getRightPreview());
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        View view;
        boolean z;
        float f;
        int actionMasked = motionEvent.getActionMasked();
        boolean z2 = false;
        if (this.mMotionCancelled && actionMasked != 0) {
            return false;
        }
        float y = motionEvent.getY();
        float x = motionEvent.getX();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                z = true;
            } else if (actionMasked == 2) {
                trackMovement(motionEvent);
                float hypot = (float) Math.hypot((double) (x - this.mInitialTouchX), (double) (y - this.mInitialTouchY));
                if (!this.mTouchSlopExeeded && hypot > ((float) this.mTouchSlop)) {
                    this.mTouchSlopExeeded = true;
                }
                if (this.mSwipingInProgress) {
                    if (this.mTargetedView == this.mRightIcon) {
                        f = Math.min(0.0f, this.mTranslationOnDown - hypot);
                    } else {
                        f = Math.max(0.0f, this.mTranslationOnDown + hypot);
                    }
                    setTranslation(f, false, false);
                }
            } else if (actionMasked == 3) {
                z = false;
            } else if (actionMasked == 5) {
                this.mMotionCancelled = true;
                endMotion(true, x, y);
            }
            if (this.mTargetedView == this.mRightIcon) {
                z2 = true;
            }
            trackMovement(motionEvent);
            endMotion(!z, x, y);
            if (!this.mTouchSlopExeeded && z) {
                this.mCallback.onIconClicked(z2);
            }
        } else {
            View iconAtPosition = getIconAtPosition(x, y);
            if (iconAtPosition == null || !((view = this.mTargetedView) == null || view == iconAtPosition)) {
                this.mMotionCancelled = true;
                return false;
            }
            if (view != null) {
                cancelAnimation();
            } else {
                this.mTouchSlopExeeded = false;
            }
            startSwiping(iconAtPosition);
            this.mInitialTouchX = x;
            this.mInitialTouchY = y;
            this.mTranslationOnDown = this.mTranslation;
            initVelocityTracker();
            trackMovement(motionEvent);
            this.mMotionCancelled = false;
        }
        return true;
    }

    public final void startSwiping(View view) {
        this.mCallback.onSwipingStarted(view == this.mRightIcon);
        this.mSwipingInProgress = true;
        this.mTargetedView = view;
    }

    public final View getIconAtPosition(float f, float f2) {
        if (leftSwipePossible() && isOnIcon(this.mLeftIcon, f, f2)) {
            return this.mLeftIcon;
        }
        if (!rightSwipePossible() || !isOnIcon(this.mRightIcon, f, f2)) {
            return null;
        }
        return this.mRightIcon;
    }

    public boolean isOnAffordanceIcon(float f, float f2) {
        return isOnIcon(this.mLeftIcon, f, f2) || isOnIcon(this.mRightIcon, f, f2);
    }

    public final boolean isOnIcon(View view, float f, float f2) {
        return Math.hypot((double) (f - (view.getX() + (((float) view.getWidth()) / 2.0f))), (double) (f2 - (view.getY() + (((float) view.getHeight()) / 2.0f)))) <= ((double) (this.mTouchTargetSize / 2));
    }

    public final void endMotion(boolean z, float f, float f2) {
        if (this.mSwipingInProgress) {
            flingWithCurrentVelocity(z, f, f2);
        } else {
            this.mTargetedView = null;
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public final boolean rightSwipePossible() {
        return this.mRightIcon.getVisibility() == 0;
    }

    public final boolean leftSwipePossible() {
        return this.mLeftIcon.getVisibility() == 0;
    }

    public void startHintAnimation(boolean z, Runnable runnable) {
        cancelAnimation();
        startHintAnimationPhase1(z, runnable);
    }

    public final void startHintAnimationPhase1(final boolean z, final Runnable runnable) {
        KeyguardAffordanceView keyguardAffordanceView = z ? this.mRightIcon : this.mLeftIcon;
        ValueAnimator animatorToRadius = getAnimatorToRadius(z, this.mHintGrowAmount);
        animatorToRadius.addListener(new AnimatorListenerAdapter() {
            public boolean mCancelled;

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (this.mCancelled) {
                    KeyguardAffordanceHelper.this.mSwipeAnimator = null;
                    KeyguardAffordanceHelper.this.mTargetedView = null;
                    runnable.run();
                    return;
                }
                KeyguardAffordanceHelper.this.startUnlockHintAnimationPhase2(z, runnable);
            }
        });
        animatorToRadius.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        animatorToRadius.setDuration(200);
        animatorToRadius.start();
        this.mSwipeAnimator = animatorToRadius;
        this.mTargetedView = keyguardAffordanceView;
    }

    public final void startUnlockHintAnimationPhase2(boolean z, final Runnable runnable) {
        ValueAnimator animatorToRadius = getAnimatorToRadius(z, 0);
        animatorToRadius.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                KeyguardAffordanceHelper.this.mSwipeAnimator = null;
                KeyguardAffordanceHelper.this.mTargetedView = null;
                runnable.run();
            }
        });
        animatorToRadius.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        animatorToRadius.setDuration(350);
        animatorToRadius.setStartDelay(500);
        animatorToRadius.start();
        this.mSwipeAnimator = animatorToRadius;
    }

    public final ValueAnimator getAnimatorToRadius(final boolean z, int i) {
        final KeyguardAffordanceView keyguardAffordanceView = z ? this.mRightIcon : this.mLeftIcon;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{keyguardAffordanceView.getCircleRadius(), (float) i});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                keyguardAffordanceView.setCircleRadiusWithoutAnimation(floatValue);
                float r3 = KeyguardAffordanceHelper.this.getTranslationFromRadius(floatValue);
                KeyguardAffordanceHelper keyguardAffordanceHelper = KeyguardAffordanceHelper.this;
                if (z) {
                    r3 = -r3;
                }
                keyguardAffordanceHelper.mTranslation = r3;
                KeyguardAffordanceHelper.this.updateIconsFromTranslation(keyguardAffordanceView);
            }
        });
        return ofFloat;
    }

    public final void cancelAnimation() {
        Animator animator = this.mSwipeAnimator;
        if (animator != null) {
            animator.cancel();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x005d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void flingWithCurrentVelocity(boolean r7, float r8, float r9) {
        /*
            r6 = this;
            float r8 = r6.getCurrentVelocity(r8, r9)
            com.android.systemui.statusbar.phone.KeyguardAffordanceHelper$Callback r9 = r6.mCallback
            boolean r9 = r9.needsAntiFalsing()
            r0 = 1
            r1 = 0
            if (r9 == 0) goto L_0x0021
            com.android.systemui.plugins.FalsingManager r9 = r6.mFalsingManager
            android.view.View r2 = r6.mTargetedView
            com.android.systemui.statusbar.KeyguardAffordanceView r3 = r6.mRightIcon
            if (r2 != r3) goto L_0x0018
            r2 = 6
            goto L_0x0019
        L_0x0018:
            r2 = 5
        L_0x0019:
            boolean r9 = r9.isFalseTouch(r2)
            if (r9 == 0) goto L_0x0021
            r9 = r0
            goto L_0x0022
        L_0x0021:
            r9 = r1
        L_0x0022:
            if (r9 != 0) goto L_0x002d
            boolean r9 = r6.isBelowFalsingThreshold()
            if (r9 == 0) goto L_0x002b
            goto L_0x002d
        L_0x002b:
            r9 = r1
            goto L_0x002e
        L_0x002d:
            r9 = r0
        L_0x002e:
            float r2 = r6.mTranslation
            float r2 = r2 * r8
            r3 = 0
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 >= 0) goto L_0x0038
            r2 = r0
            goto L_0x0039
        L_0x0038:
            r2 = r1
        L_0x0039:
            float r4 = java.lang.Math.abs(r8)
            int r5 = r6.mMinFlingVelocity
            float r5 = (float) r5
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L_0x0048
            if (r2 == 0) goto L_0x0048
            r4 = r0
            goto L_0x0049
        L_0x0048:
            r4 = r1
        L_0x0049:
            r9 = r9 | r4
            r2 = r2 ^ r9
            if (r2 == 0) goto L_0x004e
            r8 = r3
        L_0x004e:
            if (r9 != 0) goto L_0x0055
            if (r7 == 0) goto L_0x0053
            goto L_0x0055
        L_0x0053:
            r7 = r1
            goto L_0x0056
        L_0x0055:
            r7 = r0
        L_0x0056:
            float r9 = r6.mTranslation
            int r9 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            if (r9 >= 0) goto L_0x005d
            goto L_0x005e
        L_0x005d:
            r0 = r1
        L_0x005e:
            r6.fling(r8, r7, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.flingWithCurrentVelocity(boolean, float, float):void");
    }

    public final boolean isBelowFalsingThreshold() {
        return Math.abs(this.mTranslation) < Math.abs(this.mTranslationOnDown) + ((float) getMinTranslationAmount());
    }

    public final int getMinTranslationAmount() {
        return (int) (((float) this.mMinTranslationAmount) * this.mCallback.getAffordanceFalsingFactor());
    }

    public final void fling(float f, boolean z, boolean z2) {
        float f2;
        if (z2) {
            f2 = -this.mCallback.getMaxTranslationDistance();
        } else {
            f2 = this.mCallback.getMaxTranslationDistance();
        }
        if (z) {
            f2 = 0.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mTranslation, f2});
        this.mFlingAnimationUtils.apply(ofFloat, this.mTranslation, f2, f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                KeyguardAffordanceHelper.this.mTranslation = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        ofFloat.addListener(this.mFlingEndListener);
        if (!z) {
            startFinishingCircleAnimation(0.375f * f, this.mAnimationEndRunnable, z2);
            this.mCallback.onAnimationToSideStarted(z2, this.mTranslation, f);
        } else {
            reset(true);
        }
        ofFloat.start();
        this.mSwipeAnimator = ofFloat;
        if (z) {
            this.mCallback.onSwipingAborted();
        }
    }

    public final void startFinishingCircleAnimation(float f, Runnable runnable, boolean z) {
        (z ? this.mRightIcon : this.mLeftIcon).finishAnimation(f, runnable);
    }

    public final void setTranslation(float f, boolean z, boolean z2) {
        float max = rightSwipePossible() ? f : Math.max(0.0f, f);
        if (!leftSwipePossible()) {
            max = Math.min(0.0f, max);
        }
        float f2 = max;
        float abs = Math.abs(f2);
        if (f2 != this.mTranslation || z) {
            int i = (f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1));
            KeyguardAffordanceView keyguardAffordanceView = i > 0 ? this.mLeftIcon : this.mRightIcon;
            KeyguardAffordanceView keyguardAffordanceView2 = i > 0 ? this.mRightIcon : this.mLeftIcon;
            float minTranslationAmount = abs / ((float) getMinTranslationAmount());
            float max2 = Math.max(1.0f - minTranslationAmount, 0.0f);
            boolean z3 = z && z2;
            boolean z4 = z && !z2;
            float radiusFromTranslation = getRadiusFromTranslation(abs);
            boolean z5 = z && isBelowFalsingThreshold();
            if (!z) {
                updateIcon(keyguardAffordanceView, radiusFromTranslation, minTranslationAmount + (keyguardAffordanceView.getRestingAlpha() * max2), false, false, false, false);
            } else {
                updateIcon(keyguardAffordanceView, 0.0f, max2 * keyguardAffordanceView.getRestingAlpha(), z3, z5, true, z4);
            }
            updateIcon(keyguardAffordanceView2, 0.0f, max2 * keyguardAffordanceView2.getRestingAlpha(), z3, z5, z, z4);
            this.mTranslation = f2;
        }
    }

    public final void updateIconsFromTranslation(KeyguardAffordanceView keyguardAffordanceView) {
        float abs = Math.abs(this.mTranslation) / ((float) getMinTranslationAmount());
        float max = Math.max(0.0f, 1.0f - abs);
        KeyguardAffordanceView keyguardAffordanceView2 = this.mRightIcon;
        if (keyguardAffordanceView == keyguardAffordanceView2) {
            keyguardAffordanceView2 = this.mLeftIcon;
        }
        updateIconAlpha(keyguardAffordanceView, abs + (keyguardAffordanceView.getRestingAlpha() * max), false);
        updateIconAlpha(keyguardAffordanceView2, max * keyguardAffordanceView2.getRestingAlpha(), false);
    }

    public final float getTranslationFromRadius(float f) {
        float f2 = (f - ((float) this.mMinBackgroundRadius)) / 0.25f;
        if (f2 > 0.0f) {
            return f2 + ((float) this.mTouchSlop);
        }
        return 0.0f;
    }

    public final float getRadiusFromTranslation(float f) {
        int i = this.mTouchSlop;
        if (f <= ((float) i)) {
            return 0.0f;
        }
        return ((f - ((float) i)) * 0.25f) + ((float) this.mMinBackgroundRadius);
    }

    public void animateHideLeftRightIcon() {
        cancelAnimation();
        updateIcon(this.mRightIcon, 0.0f, 0.0f, true, false, false, false);
        updateIcon(this.mLeftIcon, 0.0f, 0.0f, true, false, false, false);
    }

    public final void updateIcon(KeyguardAffordanceView keyguardAffordanceView, float f, float f2, boolean z, boolean z2, boolean z3, boolean z4) {
        if (keyguardAffordanceView.getVisibility() == 0 || z3) {
            if (z4) {
                keyguardAffordanceView.setCircleRadiusWithoutAnimation(f);
            } else {
                keyguardAffordanceView.setCircleRadius(f, z2);
            }
            updateIconAlpha(keyguardAffordanceView, f2, z);
        }
    }

    public final void updateIconAlpha(KeyguardAffordanceView keyguardAffordanceView, float f, boolean z) {
        float scale = getScale(f, keyguardAffordanceView);
        keyguardAffordanceView.setImageAlpha(Math.min(1.0f, f), z);
        keyguardAffordanceView.setImageScale(scale, z);
    }

    public final float getScale(float f, KeyguardAffordanceView keyguardAffordanceView) {
        return Math.min(((f / keyguardAffordanceView.getRestingAlpha()) * 0.2f) + 0.8f, 1.5f);
    }

    public final void trackMovement(MotionEvent motionEvent) {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
        }
    }

    public final void initVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    public final float getCurrentVelocity(float f, float f2) {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000);
        float xVelocity = this.mVelocityTracker.getXVelocity();
        float yVelocity = this.mVelocityTracker.getYVelocity();
        float f3 = f - this.mInitialTouchX;
        float f4 = f2 - this.mInitialTouchY;
        float hypot = ((xVelocity * f3) + (yVelocity * f4)) / ((float) Math.hypot((double) f3, (double) f4));
        return this.mTargetedView == this.mRightIcon ? -hypot : hypot;
    }

    public void onConfigurationChanged() {
        initDimens();
        initIcons();
    }

    public void onRtlPropertiesChanged() {
        initIcons();
    }

    public void reset(boolean z) {
        cancelAnimation();
        setTranslation(0.0f, true, z);
        this.mMotionCancelled = true;
        if (this.mSwipingInProgress) {
            this.mCallback.onSwipingAborted();
            this.mSwipingInProgress = false;
        }
    }

    public boolean isSwipingInProgress() {
        return this.mSwipingInProgress;
    }

    public void launchAffordance(boolean z, boolean z2) {
        float f;
        if (!this.mSwipingInProgress) {
            KeyguardAffordanceView keyguardAffordanceView = z2 ? this.mLeftIcon : this.mRightIcon;
            KeyguardAffordanceView keyguardAffordanceView2 = z2 ? this.mRightIcon : this.mLeftIcon;
            startSwiping(keyguardAffordanceView);
            if (keyguardAffordanceView.getVisibility() != 0) {
                z = false;
            }
            if (z) {
                fling(0.0f, false, !z2);
                updateIcon(keyguardAffordanceView2, 0.0f, 0.0f, true, false, true, false);
                return;
            }
            this.mCallback.onAnimationToSideStarted(!z2, this.mTranslation, 0.0f);
            if (z2) {
                f = this.mCallback.getMaxTranslationDistance();
            } else {
                f = this.mCallback.getMaxTranslationDistance();
            }
            this.mTranslation = f;
            updateIcon(keyguardAffordanceView2, 0.0f, 0.0f, false, false, true, false);
            keyguardAffordanceView.instantFinishAnimation();
            this.mFlingEndListener.onAnimationEnd((Animator) null);
            this.mAnimationEndRunnable.run();
        }
    }
}
