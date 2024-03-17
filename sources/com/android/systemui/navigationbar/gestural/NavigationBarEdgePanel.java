package com.android.systemui.navigationbar.gestural;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.MathUtils;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import androidx.core.graphics.ColorUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.internal.util.LatencyTracker;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.NavigationEdgeBackPlugin;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.wm.shell.back.BackAnimation;
import java.io.PrintWriter;
import java.util.concurrent.Executor;

public class NavigationBarEdgePanel extends View implements NavigationEdgeBackPlugin {
    public static final FloatPropertyCompat<NavigationBarEdgePanel> CURRENT_ANGLE = new FloatPropertyCompat<NavigationBarEdgePanel>("currentAngle") {
        public void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            navigationBarEdgePanel.setCurrentAngle(f);
        }

        public float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            return navigationBarEdgePanel.getCurrentAngle();
        }
    };
    public static final FloatPropertyCompat<NavigationBarEdgePanel> CURRENT_TRANSLATION = new FloatPropertyCompat<NavigationBarEdgePanel>("currentTranslation") {
        public void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            navigationBarEdgePanel.setCurrentTranslation(f);
        }

        public float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            return navigationBarEdgePanel.getCurrentTranslation();
        }
    };
    public static final FloatPropertyCompat<NavigationBarEdgePanel> CURRENT_VERTICAL_TRANSLATION = new FloatPropertyCompat<NavigationBarEdgePanel>("verticalTranslation") {
        public void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            navigationBarEdgePanel.setVerticalTranslation(f);
        }

        public float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            return navigationBarEdgePanel.getVerticalTranslation();
        }
    };
    public static final Interpolator RUBBER_BAND_INTERPOLATOR = new PathInterpolator(0.2f, 1.0f, 1.0f, 1.0f);
    public static final Interpolator RUBBER_BAND_INTERPOLATOR_APPEAR = new PathInterpolator(0.25f, 1.0f, 1.0f, 1.0f);
    public final SpringAnimation mAngleAnimation;
    public final SpringForce mAngleAppearForce;
    public final SpringForce mAngleDisappearForce;
    public float mAngleOffset;
    public int mArrowColor;
    public final ValueAnimator mArrowColorAnimator;
    public int mArrowColorDark;
    public int mArrowColorLight;
    public final ValueAnimator mArrowDisappearAnimation;
    public final float mArrowLength;
    public int mArrowPaddingEnd;
    public final Path mArrowPath = new Path();
    public int mArrowStartColor;
    public final float mArrowThickness;
    public boolean mArrowsPointLeft;
    public BackAnimation mBackAnimation;
    public NavigationEdgeBackPlugin.BackCallback mBackCallback;
    public final float mBaseTranslation;
    public float mCurrentAngle;
    public int mCurrentArrowColor;
    public float mCurrentTranslation;
    public final float mDensity;
    public float mDesiredAngle;
    public float mDesiredTranslation;
    public float mDesiredVerticalTranslation;
    public float mDisappearAmount;
    public final Point mDisplaySize = new Point();
    public boolean mDragSlopPassed;
    public final Runnable mFailsafeRunnable;
    public int mFingerOffset;
    public final Handler mHandler;
    public boolean mIsDark;
    public boolean mIsLeftPanel;
    public final LatencyTracker mLatencyTracker;
    public WindowManager.LayoutParams mLayoutParams;
    public int mLeftInset;
    public float mMaxTranslation;
    public int mMinArrowPosition;
    public final float mMinDeltaForSwitch;
    public final Paint mPaint;
    public float mPreviousTouchTranslation;
    public int mProtectionColor;
    public int mProtectionColorDark;
    public int mProtectionColorLight;
    public final Paint mProtectionPaint;
    public RegionSamplingHelper mRegionSamplingHelper;
    public final SpringForce mRegularTranslationSpring;
    public int mRightInset;
    public final Rect mSamplingRect;
    public int mScreenSize;
    public DynamicAnimation.OnAnimationEndListener mSetGoneEndListener;
    public boolean mShowProtection;
    public float mStartX;
    public float mStartY;
    public final float mSwipeProgressThreshold;
    public final float mSwipeTriggerThreshold;
    public float mTotalTouchDelta;
    public boolean mTrackingBackArrowLatency;
    public final SpringAnimation mTranslationAnimation;
    public boolean mTriggerBack;
    public final SpringForce mTriggerBackSpring;
    public VelocityTracker mVelocityTracker;
    public float mVerticalTranslation;
    public final SpringAnimation mVerticalTranslationAnimation;
    public long mVibrationTime;
    public final VibratorHelper mVibratorHelper;
    public final WindowManager mWindowManager;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public NavigationBarEdgePanel(Context context, BackAnimation backAnimation, LatencyTracker latencyTracker) {
        super(context);
        Paint paint = new Paint();
        this.mPaint = paint;
        final boolean z = false;
        this.mIsDark = false;
        this.mShowProtection = false;
        this.mSamplingRect = new Rect();
        this.mTrackingBackArrowLatency = false;
        this.mHandler = new Handler();
        this.mFailsafeRunnable = new NavigationBarEdgePanel$$ExternalSyntheticLambda0(this);
        this.mSetGoneEndListener = new DynamicAnimation.OnAnimationEndListener() {
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                dynamicAnimation.removeEndListener(this);
                if (!z) {
                    NavigationBarEdgePanel.this.setVisibility(8);
                }
            }
        };
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mBackAnimation = backAnimation;
        this.mVibratorHelper = (VibratorHelper) Dependency.get(VibratorHelper.class);
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mBaseTranslation = dp(32.0f);
        this.mArrowLength = dp(18.0f);
        float dp = dp(2.5f);
        this.mArrowThickness = dp;
        this.mMinDeltaForSwitch = dp(32.0f);
        paint.setStrokeWidth(dp);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mArrowColorAnimator = ofFloat;
        ofFloat.setDuration(120);
        ofFloat.addUpdateListener(new NavigationBarEdgePanel$$ExternalSyntheticLambda1(this));
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mArrowDisappearAnimation = ofFloat2;
        ofFloat2.setDuration(100);
        ofFloat2.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat2.addUpdateListener(new NavigationBarEdgePanel$$ExternalSyntheticLambda2(this));
        SpringAnimation springAnimation = new SpringAnimation(this, CURRENT_ANGLE);
        this.mAngleAnimation = springAnimation;
        SpringForce dampingRatio = new SpringForce().setStiffness(500.0f).setDampingRatio(0.5f);
        this.mAngleAppearForce = dampingRatio;
        this.mAngleDisappearForce = new SpringForce().setStiffness(1500.0f).setDampingRatio(0.5f).setFinalPosition(90.0f);
        springAnimation.setSpring(dampingRatio).setMaxValue(90.0f);
        SpringAnimation springAnimation2 = new SpringAnimation(this, CURRENT_TRANSLATION);
        this.mTranslationAnimation = springAnimation2;
        SpringForce dampingRatio2 = new SpringForce().setStiffness(1500.0f).setDampingRatio(0.75f);
        this.mRegularTranslationSpring = dampingRatio2;
        this.mTriggerBackSpring = new SpringForce().setStiffness(450.0f).setDampingRatio(0.75f);
        springAnimation2.setSpring(dampingRatio2);
        SpringAnimation springAnimation3 = new SpringAnimation(this, CURRENT_VERTICAL_TRANSLATION);
        this.mVerticalTranslationAnimation = springAnimation3;
        springAnimation3.setSpring(new SpringForce().setStiffness(1500.0f).setDampingRatio(0.75f));
        Paint paint2 = new Paint(paint);
        this.mProtectionPaint = paint2;
        paint2.setStrokeWidth(dp + 2.0f);
        loadDimens();
        loadColors(context);
        updateArrowDirection();
        this.mSwipeTriggerThreshold = context.getResources().getDimension(R$dimen.navigation_edge_action_drag_threshold);
        this.mSwipeProgressThreshold = context.getResources().getDimension(R$dimen.navigation_edge_action_progress_threshold);
        initializeBackAnimation();
        setVisibility(8);
        Executor executor = (Executor) Dependency.get(Dependency.BACKGROUND_EXECUTOR);
        z = this.mContext.getDisplayId() == 0 ? true : z;
        RegionSamplingHelper regionSamplingHelper = new RegionSamplingHelper(this, new RegionSamplingHelper.SamplingCallback() {
            public void onRegionDarknessChanged(boolean z) {
                NavigationBarEdgePanel.this.setIsDark(!z, true);
            }

            public Rect getSampledRegion(View view) {
                return NavigationBarEdgePanel.this.mSamplingRect;
            }

            public boolean isSamplingEnabled() {
                return z;
            }
        }, executor);
        this.mRegionSamplingHelper = regionSamplingHelper;
        regionSamplingHelper.setWindowVisible(true);
        this.mShowProtection = !z;
        this.mLatencyTracker = latencyTracker;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        setCurrentArrowColor(ColorUtils.blendARGB(this.mArrowStartColor, this.mArrowColor, valueAnimator.getAnimatedFraction()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        this.mDisappearAmount = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void setBackAnimation(BackAnimation backAnimation) {
        this.mBackAnimation = backAnimation;
        initializeBackAnimation();
    }

    public final void initializeBackAnimation() {
        BackAnimation backAnimation = this.mBackAnimation;
        if (backAnimation != null) {
            backAnimation.setSwipeThresholds(this.mSwipeTriggerThreshold, this.mSwipeProgressThreshold);
        }
    }

    public void onDestroy() {
        cancelFailsafe();
        this.mWindowManager.removeView(this);
        this.mRegionSamplingHelper.stop();
        this.mRegionSamplingHelper = null;
    }

    public final void setIsDark(boolean z, boolean z2) {
        this.mIsDark = z;
        updateIsDark(z2);
    }

    public void setIsLeftPanel(boolean z) {
        this.mIsLeftPanel = z;
        this.mLayoutParams.gravity = z ? 51 : 53;
    }

    public void setInsets(int i, int i2) {
        this.mLeftInset = i;
        this.mRightInset = i2;
    }

    public void setDisplaySize(Point point) {
        this.mDisplaySize.set(point.x, point.y);
        Point point2 = this.mDisplaySize;
        this.mScreenSize = Math.min(point2.x, point2.y);
    }

    public void setBackCallback(NavigationEdgeBackPlugin.BackCallback backCallback) {
        this.mBackCallback = backCallback;
    }

    public void setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mLayoutParams = layoutParams;
        this.mWindowManager.addView(this, layoutParams);
    }

    public final void adjustSamplingRectToBoundingBox() {
        float f = this.mDesiredTranslation;
        if (!this.mTriggerBack) {
            f = this.mBaseTranslation;
            boolean z = this.mIsLeftPanel;
            if ((z && this.mArrowsPointLeft) || (!z && !this.mArrowsPointLeft)) {
                f -= getStaticArrowWidth();
            }
        }
        float f2 = f - (this.mArrowThickness / 2.0f);
        if (!this.mIsLeftPanel) {
            f2 = ((float) this.mSamplingRect.width()) - f2;
        }
        float staticArrowWidth = getStaticArrowWidth();
        float polarToCartY = polarToCartY(56.0f) * this.mArrowLength * 2.0f;
        if (!this.mArrowsPointLeft) {
            f2 -= staticArrowWidth;
        }
        this.mSamplingRect.offset((int) f2, (int) (((((float) getHeight()) * 0.5f) + this.mDesiredVerticalTranslation) - (polarToCartY / 2.0f)));
        Rect rect = this.mSamplingRect;
        int i = rect.left;
        int i2 = rect.top;
        rect.set(i, i2, (int) (((float) i) + staticArrowWidth), (int) (((float) i2) + polarToCartY));
        this.mRegionSamplingHelper.updateSamplingRect();
    }

    public void onMotionEvent(MotionEvent motionEvent) {
        BackAnimation backAnimation = this.mBackAnimation;
        if (backAnimation != null) {
            backAnimation.onBackMotion(motionEvent.getX(), motionEvent.getY(), motionEvent.getActionMasked(), this.mIsLeftPanel ^ true ? 1 : 0);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mDragSlopPassed = false;
            resetOnDown();
            this.mStartX = motionEvent.getX();
            this.mStartY = motionEvent.getY();
            setVisibility(0);
            updatePosition(motionEvent.getY());
            this.mRegionSamplingHelper.start(this.mSamplingRect);
            this.mWindowManager.updateViewLayout(this, this.mLayoutParams);
            this.mLatencyTracker.onActionStart(15);
            this.mTrackingBackArrowLatency = true;
        } else if (actionMasked == 1) {
            if (this.mTriggerBack) {
                triggerBack();
            } else {
                cancelBack();
            }
            this.mRegionSamplingHelper.stop();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        } else if (actionMasked == 2) {
            handleMoveEvent(motionEvent);
        } else if (actionMasked == 3) {
            cancelBack();
            this.mRegionSamplingHelper.stop();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateArrowDirection();
        loadDimens();
    }

    public void onDraw(Canvas canvas) {
        float f = this.mCurrentTranslation - (this.mArrowThickness / 2.0f);
        canvas.save();
        if (!this.mIsLeftPanel) {
            f = ((float) getWidth()) - f;
        }
        canvas.translate(f, (((float) getHeight()) * 0.5f) + this.mVerticalTranslation);
        Path calculatePath = calculatePath(polarToCartX(this.mCurrentAngle) * this.mArrowLength, polarToCartY(this.mCurrentAngle) * this.mArrowLength);
        if (this.mShowProtection) {
            canvas.drawPath(calculatePath, this.mProtectionPaint);
        }
        canvas.drawPath(calculatePath, this.mPaint);
        canvas.restore();
        if (this.mTrackingBackArrowLatency) {
            this.mLatencyTracker.onActionEnd(15);
            this.mTrackingBackArrowLatency = false;
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mMaxTranslation = (float) (getWidth() - this.mArrowPaddingEnd);
    }

    public final void loadDimens() {
        Resources resources = getResources();
        this.mArrowPaddingEnd = resources.getDimensionPixelSize(R$dimen.navigation_edge_panel_padding);
        this.mMinArrowPosition = resources.getDimensionPixelSize(R$dimen.navigation_edge_arrow_min_y);
        this.mFingerOffset = resources.getDimensionPixelSize(R$dimen.navigation_edge_finger_offset);
    }

    public final void updateArrowDirection() {
        this.mArrowsPointLeft = getLayoutDirection() == 0;
        invalidate();
    }

    public final void loadColors(Context context) {
        int themeAttr = Utils.getThemeAttr(context, R$attr.darkIconTheme);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, R$attr.lightIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, themeAttr);
        int i = R$attr.singleToneColor;
        this.mArrowColorLight = Utils.getColorAttrDefaultColor(contextThemeWrapper, i);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, i);
        this.mArrowColorDark = colorAttrDefaultColor;
        this.mProtectionColorDark = this.mArrowColorLight;
        this.mProtectionColorLight = colorAttrDefaultColor;
        updateIsDark(false);
    }

    public final void updateIsDark(boolean z) {
        int i = this.mIsDark ? this.mProtectionColorDark : this.mProtectionColorLight;
        this.mProtectionColor = i;
        this.mProtectionPaint.setColor(i);
        this.mArrowColor = this.mIsDark ? this.mArrowColorDark : this.mArrowColorLight;
        this.mArrowColorAnimator.cancel();
        if (!z) {
            setCurrentArrowColor(this.mArrowColor);
            return;
        }
        this.mArrowStartColor = this.mCurrentArrowColor;
        this.mArrowColorAnimator.start();
    }

    public final void setCurrentArrowColor(int i) {
        this.mCurrentArrowColor = i;
        this.mPaint.setColor(i);
        invalidate();
    }

    public final float getStaticArrowWidth() {
        return polarToCartX(56.0f) * this.mArrowLength;
    }

    public final float polarToCartX(float f) {
        return (float) Math.cos(Math.toRadians((double) f));
    }

    public final float polarToCartY(float f) {
        return (float) Math.sin(Math.toRadians((double) f));
    }

    public final Path calculatePath(float f, float f2) {
        if (!this.mArrowsPointLeft) {
            f = -f;
        }
        float lerp = MathUtils.lerp(1.0f, 0.75f, this.mDisappearAmount);
        float f3 = f * lerp;
        float f4 = f2 * lerp;
        this.mArrowPath.reset();
        this.mArrowPath.moveTo(f3, f4);
        this.mArrowPath.lineTo(0.0f, 0.0f);
        this.mArrowPath.lineTo(f3, -f4);
        return this.mArrowPath;
    }

    public final float getCurrentAngle() {
        return this.mCurrentAngle;
    }

    public final float getCurrentTranslation() {
        return this.mCurrentTranslation;
    }

    public final void triggerBack() {
        this.mBackCallback.triggerBack();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.computeCurrentVelocity(1000);
        if ((Math.abs(this.mVelocityTracker.getXVelocity()) < 500.0f) || SystemClock.uptimeMillis() - this.mVibrationTime >= 400) {
            this.mVibratorHelper.vibrate(0);
        }
        float f = this.mAngleOffset;
        if (f > -4.0f) {
            this.mAngleOffset = Math.max(-8.0f, f - 8.0f);
            updateAngle(true);
        }
        final NavigationBarEdgePanel$$ExternalSyntheticLambda3 navigationBarEdgePanel$$ExternalSyntheticLambda3 = new NavigationBarEdgePanel$$ExternalSyntheticLambda3(this);
        if (this.mTranslationAnimation.isRunning()) {
            this.mTranslationAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    dynamicAnimation.removeEndListener(this);
                    if (!z) {
                        navigationBarEdgePanel$$ExternalSyntheticLambda3.run();
                    }
                }
            });
            scheduleFailsafe();
            return;
        }
        navigationBarEdgePanel$$ExternalSyntheticLambda3.run();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerBack$3() {
        this.mAngleOffset = Math.max(0.0f, this.mAngleOffset + 8.0f);
        updateAngle(true);
        this.mTranslationAnimation.setSpring(this.mTriggerBackSpring);
        setDesiredTranslation(this.mDesiredTranslation - dp(32.0f), true);
        animate().alpha(0.0f).setDuration(80).withEndAction(new NavigationBarEdgePanel$$ExternalSyntheticLambda4(this));
        this.mArrowDisappearAnimation.start();
        scheduleFailsafe();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerBack$2() {
        setVisibility(8);
    }

    public final void cancelBack() {
        this.mBackCallback.cancelBack();
        if (this.mTranslationAnimation.isRunning()) {
            this.mTranslationAnimation.addEndListener(this.mSetGoneEndListener);
            scheduleFailsafe();
            return;
        }
        setVisibility(8);
    }

    public final void resetOnDown() {
        animate().cancel();
        this.mAngleAnimation.cancel();
        this.mTranslationAnimation.cancel();
        this.mVerticalTranslationAnimation.cancel();
        this.mArrowDisappearAnimation.cancel();
        this.mAngleOffset = 0.0f;
        this.mTranslationAnimation.setSpring(this.mRegularTranslationSpring);
        setTriggerBack(false, false);
        setDesiredTranslation(0.0f, false);
        setCurrentTranslation(0.0f);
        updateAngle(false);
        this.mPreviousTouchTranslation = 0.0f;
        this.mTotalTouchDelta = 0.0f;
        this.mVibrationTime = 0;
        setDesiredVerticalTransition(0.0f, false);
        cancelFailsafe();
    }

    public final void handleMoveEvent(MotionEvent motionEvent) {
        float f;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float abs = MathUtils.abs(x - this.mStartX);
        float f2 = y - this.mStartY;
        float f3 = abs - this.mPreviousTouchTranslation;
        if (Math.abs(f3) > 0.0f) {
            if (Math.signum(f3) == Math.signum(this.mTotalTouchDelta)) {
                this.mTotalTouchDelta += f3;
            } else {
                this.mTotalTouchDelta = f3;
            }
        }
        this.mPreviousTouchTranslation = abs;
        if (!this.mDragSlopPassed && abs > this.mSwipeTriggerThreshold) {
            this.mDragSlopPassed = true;
            this.mVibratorHelper.vibrate(2);
            this.mVibrationTime = SystemClock.uptimeMillis();
            this.mDisappearAmount = 0.0f;
            setAlpha(1.0f);
            setTriggerBack(true, true);
        }
        float f4 = this.mBaseTranslation;
        if (abs > f4) {
            float interpolation = RUBBER_BAND_INTERPOLATOR.getInterpolation(MathUtils.saturate((abs - f4) / (((float) this.mScreenSize) - f4)));
            float f5 = this.mMaxTranslation;
            float f6 = this.mBaseTranslation;
            f = f6 + (interpolation * (f5 - f6));
        } else {
            float interpolation2 = RUBBER_BAND_INTERPOLATOR_APPEAR.getInterpolation(MathUtils.saturate((f4 - abs) / f4));
            float f7 = this.mBaseTranslation;
            f = f7 - (interpolation2 * (f7 / 4.0f));
        }
        boolean z = this.mTriggerBack;
        boolean z2 = false;
        if (Math.abs(this.mTotalTouchDelta) > this.mMinDeltaForSwitch) {
            z = this.mTotalTouchDelta > 0.0f;
        }
        this.mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = this.mVelocityTracker.getXVelocity();
        float min = Math.min((MathUtils.mag(xVelocity, this.mVelocityTracker.getYVelocity()) / 1000.0f) * 4.0f, 4.0f) * Math.signum(xVelocity);
        this.mAngleOffset = min;
        boolean z3 = this.mIsLeftPanel;
        if ((z3 && this.mArrowsPointLeft) || (!z3 && !this.mArrowsPointLeft)) {
            this.mAngleOffset = min * -1.0f;
        }
        if (Math.abs(f2) <= Math.abs(x - this.mStartX) * 2.0f) {
            z2 = z;
        }
        setTriggerBack(z2, true);
        if (!this.mTriggerBack) {
            f = 0.0f;
        } else {
            boolean z4 = this.mIsLeftPanel;
            if ((z4 && this.mArrowsPointLeft) || (!z4 && !this.mArrowsPointLeft)) {
                f -= getStaticArrowWidth();
            }
        }
        setDesiredTranslation(f, true);
        updateAngle(true);
        float height = (((float) getHeight()) / 2.0f) - this.mArrowLength;
        setDesiredVerticalTransition(RUBBER_BAND_INTERPOLATOR.getInterpolation(MathUtils.constrain(Math.abs(f2) / (15.0f * height), 0.0f, 1.0f)) * height * Math.signum(f2), true);
        updateSamplingRect();
    }

    public final void updatePosition(float f) {
        float max = Math.max(f - ((float) this.mFingerOffset), (float) this.mMinArrowPosition);
        WindowManager.LayoutParams layoutParams = this.mLayoutParams;
        layoutParams.y = MathUtils.constrain((int) (max - (((float) layoutParams.height) / 2.0f)), 0, this.mDisplaySize.y);
        updateSamplingRect();
    }

    public final void updateSamplingRect() {
        WindowManager.LayoutParams layoutParams = this.mLayoutParams;
        int i = layoutParams.y;
        int i2 = this.mIsLeftPanel ? this.mLeftInset : (this.mDisplaySize.x - this.mRightInset) - layoutParams.width;
        this.mSamplingRect.set(i2, i, layoutParams.width + i2, layoutParams.height + i);
        adjustSamplingRectToBoundingBox();
    }

    public final void setDesiredVerticalTransition(float f, boolean z) {
        if (this.mDesiredVerticalTranslation != f) {
            this.mDesiredVerticalTranslation = f;
            if (!z) {
                setVerticalTranslation(f);
            } else {
                this.mVerticalTranslationAnimation.animateToFinalPosition(f);
            }
            invalidate();
        }
    }

    public final void setVerticalTranslation(float f) {
        this.mVerticalTranslation = f;
        invalidate();
    }

    public final float getVerticalTranslation() {
        return this.mVerticalTranslation;
    }

    public final void setDesiredTranslation(float f, boolean z) {
        if (this.mDesiredTranslation != f) {
            this.mDesiredTranslation = f;
            if (!z) {
                setCurrentTranslation(f);
            } else {
                this.mTranslationAnimation.animateToFinalPosition(f);
            }
        }
    }

    public final void setCurrentTranslation(float f) {
        this.mCurrentTranslation = f;
        invalidate();
    }

    public final void setTriggerBack(boolean z, boolean z2) {
        if (this.mTriggerBack != z) {
            this.mTriggerBack = z;
            this.mAngleAnimation.cancel();
            updateAngle(z2);
            this.mTranslationAnimation.cancel();
            BackAnimation backAnimation = this.mBackAnimation;
            if (backAnimation != null) {
                backAnimation.setTriggerBack(z);
            }
        }
    }

    public final void updateAngle(boolean z) {
        boolean z2 = this.mTriggerBack;
        float f = z2 ? this.mAngleOffset + 56.0f : 90.0f;
        if (f != this.mDesiredAngle) {
            if (!z) {
                setCurrentAngle(f);
            } else {
                this.mAngleAnimation.setSpring(z2 ? this.mAngleAppearForce : this.mAngleDisappearForce);
                this.mAngleAnimation.animateToFinalPosition(f);
            }
            this.mDesiredAngle = f;
        }
    }

    public final void setCurrentAngle(float f) {
        this.mCurrentAngle = f;
        invalidate();
    }

    public final void scheduleFailsafe() {
        cancelFailsafe();
        this.mHandler.postDelayed(this.mFailsafeRunnable, 200);
    }

    public final void cancelFailsafe() {
        this.mHandler.removeCallbacks(this.mFailsafeRunnable);
    }

    public final void onFailsafe() {
        setVisibility(8);
    }

    public final float dp(float f) {
        return this.mDensity * f;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBarEdgePanel:");
        printWriter.println("  mIsLeftPanel=" + this.mIsLeftPanel);
        printWriter.println("  mTriggerBack=" + this.mTriggerBack);
        printWriter.println("  mDragSlopPassed=" + this.mDragSlopPassed);
        printWriter.println("  mCurrentAngle=" + this.mCurrentAngle);
        printWriter.println("  mDesiredAngle=" + this.mDesiredAngle);
        printWriter.println("  mCurrentTranslation=" + this.mCurrentTranslation);
        printWriter.println("  mDesiredTranslation=" + this.mDesiredTranslation);
        printWriter.println("  mTranslationAnimation running=" + this.mTranslationAnimation.isRunning());
        this.mRegionSamplingHelper.dump(printWriter);
    }
}
