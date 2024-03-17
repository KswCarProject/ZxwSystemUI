package com.android.systemui.dreams.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.assist.PhoneStateMonitor$$ExternalSyntheticLambda1;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.util.Optional;

public class BouncerSwipeTouchHandler implements DreamTouchHandler {
    public boolean mBouncerInitiallyShowing;
    public final float mBouncerZoneScreenPercentage;
    public Boolean mCapture;
    public final Optional<CentralSurfaces> mCentralSurfaces;
    public float mCurrentExpansion;
    public final DisplayMetrics mDisplayMetrics;
    public final FlingAnimationUtils mFlingAnimationUtils;
    public final FlingAnimationUtils mFlingAnimationUtilsClosing;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public final GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (BouncerSwipeTouchHandler.this.mCapture == null) {
                BouncerSwipeTouchHandler.this.mCapture = Boolean.valueOf(Math.abs(f2) > Math.abs(f));
                BouncerSwipeTouchHandler bouncerSwipeTouchHandler = BouncerSwipeTouchHandler.this;
                bouncerSwipeTouchHandler.mBouncerInitiallyShowing = ((Boolean) bouncerSwipeTouchHandler.mCentralSurfaces.map(new PhoneStateMonitor$$ExternalSyntheticLambda1()).orElse(Boolean.FALSE)).booleanValue();
                if (BouncerSwipeTouchHandler.this.mCapture.booleanValue()) {
                    BouncerSwipeTouchHandler.this.mStatusBarKeyguardViewManager.showBouncer(false);
                }
            }
            if (!BouncerSwipeTouchHandler.this.mCapture.booleanValue()) {
                return false;
            }
            if (!BouncerSwipeTouchHandler.this.mBouncerInitiallyShowing && motionEvent.getY() < motionEvent2.getY()) {
                return true;
            }
            if ((BouncerSwipeTouchHandler.this.mBouncerInitiallyShowing && motionEvent.getY() > motionEvent2.getY()) || !BouncerSwipeTouchHandler.this.mCentralSurfaces.isPresent()) {
                return true;
            }
            float y = motionEvent2.getY() - motionEvent.getY();
            float abs = Math.abs(motionEvent.getY() - motionEvent2.getY()) / ((CentralSurfaces) BouncerSwipeTouchHandler.this.mCentralSurfaces.get()).getDisplayHeight();
            BouncerSwipeTouchHandler bouncerSwipeTouchHandler2 = BouncerSwipeTouchHandler.this;
            if (!bouncerSwipeTouchHandler2.mBouncerInitiallyShowing) {
                abs = 1.0f - abs;
            }
            bouncerSwipeTouchHandler2.setPanelExpansion(abs, y);
            return true;
        }
    };
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public DreamTouchHandler.TouchSession mTouchSession;
    public final UiEventLogger mUiEventLogger;
    public ValueAnimatorCreator mValueAnimatorCreator;
    public VelocityTracker mVelocityTracker;
    public VelocityTrackerFactory mVelocityTrackerFactory;

    public interface ValueAnimatorCreator {
        ValueAnimator create(float f, float f2);
    }

    public interface VelocityTrackerFactory {
        VelocityTracker obtain();
    }

    public final void setPanelExpansion(float f, float f2) {
        this.mCurrentExpansion = f;
        this.mStatusBarKeyguardViewManager.onPanelExpansionChanged(new PanelExpansionChangeEvent(f, false, true, f2));
    }

    public enum DreamEvent implements UiEventLogger.UiEventEnum {
        DREAM_SWIPED(988),
        DREAM_BOUNCER_FULLY_VISIBLE(1056);
        
        private final int mId;

        /* access modifiers changed from: public */
        DreamEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public BouncerSwipeTouchHandler(DisplayMetrics displayMetrics, StatusBarKeyguardViewManager statusBarKeyguardViewManager, Optional<CentralSurfaces> optional, NotificationShadeWindowController notificationShadeWindowController, ValueAnimatorCreator valueAnimatorCreator, VelocityTrackerFactory velocityTrackerFactory, FlingAnimationUtils flingAnimationUtils, FlingAnimationUtils flingAnimationUtils2, float f, UiEventLogger uiEventLogger) {
        this.mDisplayMetrics = displayMetrics;
        this.mCentralSurfaces = optional;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mBouncerZoneScreenPercentage = f;
        this.mFlingAnimationUtils = flingAnimationUtils;
        this.mFlingAnimationUtilsClosing = flingAnimationUtils2;
        this.mValueAnimatorCreator = valueAnimatorCreator;
        this.mVelocityTrackerFactory = velocityTrackerFactory;
        this.mUiEventLogger = uiEventLogger;
    }

    public void getTouchInitiationRegion(Region region) {
        if (((Boolean) this.mCentralSurfaces.map(new PhoneStateMonitor$$ExternalSyntheticLambda1()).orElse(Boolean.FALSE)).booleanValue()) {
            DisplayMetrics displayMetrics = this.mDisplayMetrics;
            region.op(new Rect(0, 0, displayMetrics.widthPixels, Math.round(((float) displayMetrics.heightPixels) * this.mBouncerZoneScreenPercentage)), Region.Op.UNION);
            return;
        }
        int round = Math.round(((float) this.mDisplayMetrics.heightPixels) * (1.0f - this.mBouncerZoneScreenPercentage));
        DisplayMetrics displayMetrics2 = this.mDisplayMetrics;
        region.op(new Rect(0, round, displayMetrics2.widthPixels, displayMetrics2.heightPixels), Region.Op.UNION);
    }

    public void onSessionStart(DreamTouchHandler.TouchSession touchSession) {
        VelocityTracker obtain = this.mVelocityTrackerFactory.obtain();
        this.mVelocityTracker = obtain;
        this.mTouchSession = touchSession;
        obtain.clear();
        this.mNotificationShadeWindowController.setForcePluginOpen(true, this);
        touchSession.registerCallback(new BouncerSwipeTouchHandler$$ExternalSyntheticLambda0(this));
        touchSession.registerGestureListener(this.mOnGestureListener);
        touchSession.registerInputListener(new BouncerSwipeTouchHandler$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSessionStart$0() {
        this.mVelocityTracker.recycle();
        this.mCapture = null;
        this.mNotificationShadeWindowController.setForcePluginOpen(false, this);
    }

    /* renamed from: onMotionEvent */
    public final void lambda$onSessionStart$1(InputEvent inputEvent) {
        if (!(inputEvent instanceof MotionEvent)) {
            Log.e("BouncerSwipeTouchHandler", "non MotionEvent received:" + inputEvent);
            return;
        }
        MotionEvent motionEvent = (MotionEvent) inputEvent;
        int action = motionEvent.getAction();
        if (action == 1 || action == 3) {
            this.mTouchSession.pop();
            Boolean bool = this.mCapture;
            if (bool != null && bool.booleanValue()) {
                this.mVelocityTracker.computeCurrentVelocity(1000);
                float yVelocity = this.mVelocityTracker.getYVelocity();
                float f = flingRevealsOverlay(yVelocity, (float) Math.hypot((double) this.mVelocityTracker.getXVelocity(), (double) yVelocity)) ? 1.0f : 0.0f;
                if (!this.mBouncerInitiallyShowing && f == 0.0f) {
                    this.mUiEventLogger.log(DreamEvent.DREAM_SWIPED);
                }
                flingToExpansion(yVelocity, f);
                if (f == 1.0f) {
                    this.mStatusBarKeyguardViewManager.reset(false);
                    return;
                }
                return;
            }
            return;
        }
        this.mVelocityTracker.addMovement(motionEvent);
    }

    public final ValueAnimator createExpansionAnimator(float f, float f2) {
        ValueAnimator create = this.mValueAnimatorCreator.create(this.mCurrentExpansion, f);
        create.addUpdateListener(new BouncerSwipeTouchHandler$$ExternalSyntheticLambda2(this, f2));
        if (!this.mBouncerInitiallyShowing && f == 0.0f) {
            create.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    BouncerSwipeTouchHandler.this.mUiEventLogger.log(DreamEvent.DREAM_BOUNCER_FULLY_VISIBLE);
                }
            });
        }
        return create;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createExpansionAnimator$2(float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        setPanelExpansion(floatValue, f * floatValue);
    }

    public boolean flingRevealsOverlay(float f, float f2) {
        if (Math.abs(f2) < this.mFlingAnimationUtils.getMinVelocityPxPerSecond()) {
            if (this.mCurrentExpansion > 0.5f) {
                return true;
            }
            return false;
        } else if (f > 0.0f) {
            return true;
        } else {
            return false;
        }
    }

    public void flingToExpansion(float f, float f2) {
        if (this.mCentralSurfaces.isPresent()) {
            float displayHeight = this.mCentralSurfaces.get().getDisplayHeight();
            float f3 = displayHeight * this.mCurrentExpansion;
            float f4 = displayHeight * f2;
            ValueAnimator createExpansionAnimator = createExpansionAnimator(f2, f4 - f3);
            if (f2 == 1.0f) {
                this.mFlingAnimationUtilsClosing.apply(createExpansionAnimator, f3, f4, f, displayHeight);
            } else {
                this.mFlingAnimationUtils.apply(createExpansionAnimator, f3, f4, f, displayHeight);
            }
            createExpansionAnimator.start();
        }
    }
}
