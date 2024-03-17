package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.DismissView;
import com.android.wm.shell.common.DismissCircleView;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.android.wm.shell.pip.PipUiEventLogger;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class PipDismissTargetHandler implements ViewTreeObserver.OnPreDrawListener {
    public final Context mContext;
    public int mDismissAreaHeight;
    public boolean mEnableDismissDragToEdge;
    public boolean mHasDismissTargetSurface;
    public float mMagneticFieldRadiusPercent = 1.0f;
    public MagnetizedObject.MagneticTarget mMagneticTarget;
    public MagnetizedObject<Rect> mMagnetizedPip;
    public final ShellExecutor mMainExecutor;
    public final PipMotionHelper mMotionHelper;
    public final PipUiEventLogger mPipUiEventLogger;
    public int mTargetSize;
    public DismissCircleView mTargetView;
    public DismissView mTargetViewContainer;
    public SurfaceControl mTaskLeash;
    public WindowInsets mWindowInsets;
    public final WindowManager mWindowManager;

    public PipDismissTargetHandler(Context context, PipUiEventLogger pipUiEventLogger, PipMotionHelper pipMotionHelper, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mMotionHelper = pipMotionHelper;
        this.mMainExecutor = shellExecutor;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    public void init() {
        Resources resources = this.mContext.getResources();
        this.mEnableDismissDragToEdge = resources.getBoolean(R.bool.config_pipEnableDismissDragToEdge);
        this.mDismissAreaHeight = resources.getDimensionPixelSize(R.dimen.floating_dismiss_gradient_height);
        if (this.mTargetViewContainer != null) {
            cleanUpDismissTarget();
        }
        DismissView dismissView = new DismissView(this.mContext);
        this.mTargetViewContainer = dismissView;
        this.mTargetView = dismissView.getCircle();
        this.mTargetViewContainer.setOnApplyWindowInsetsListener(new PipDismissTargetHandler$$ExternalSyntheticLambda0(this));
        MagnetizedObject<Rect> magnetizedPip = this.mMotionHelper.getMagnetizedPip();
        this.mMagnetizedPip = magnetizedPip;
        magnetizedPip.clearAllTargets();
        this.mMagneticTarget = this.mMagnetizedPip.addTarget(this.mTargetView, 0);
        updateMagneticTargetSize();
        this.mMagnetizedPip.setAnimateStuckToTarget(new PipDismissTargetHandler$$ExternalSyntheticLambda1(this));
        this.mMagnetizedPip.setMagnetListener(new MagnetizedObject.MagnetListener() {
            public void onStuckToTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                if (PipDismissTargetHandler.this.mEnableDismissDragToEdge) {
                    PipDismissTargetHandler.this.showDismissTargetMaybe();
                }
            }

            public void onUnstuckFromTarget(MagnetizedObject.MagneticTarget magneticTarget, float f, float f2, boolean z) {
                if (z) {
                    PipDismissTargetHandler.this.mMotionHelper.flingToSnapTarget(f, f2, (Runnable) null);
                    PipDismissTargetHandler.this.hideDismissTargetMaybe();
                    return;
                }
                PipDismissTargetHandler.this.mMotionHelper.setSpringingToTouch(true);
            }

            public void onReleasedInTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                if (PipDismissTargetHandler.this.mEnableDismissDragToEdge) {
                    PipDismissTargetHandler.this.mMainExecutor.executeDelayed(new PipDismissTargetHandler$1$$ExternalSyntheticLambda0(this), 0);
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReleasedInTarget$0() {
                PipDismissTargetHandler.this.mMotionHelper.notifyDismissalPending();
                PipDismissTargetHandler.this.mMotionHelper.animateDismiss();
                PipDismissTargetHandler.this.hideDismissTargetMaybe();
                PipDismissTargetHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_DRAG_TO_REMOVE);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$init$0(View view, WindowInsets windowInsets) {
        if (!windowInsets.equals(this.mWindowInsets)) {
            this.mWindowInsets = windowInsets;
            updateMagneticTargetSize();
        }
        return windowInsets;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$init$1(MagnetizedObject.MagneticTarget magneticTarget, Float f, Float f2, Boolean bool, Function0 function0) {
        if (this.mEnableDismissDragToEdge) {
            this.mMotionHelper.animateIntoDismissTarget(magneticTarget, f.floatValue(), f2.floatValue(), bool.booleanValue(), function0);
        }
        return Unit.INSTANCE;
    }

    public boolean onPreDraw() {
        this.mTargetViewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
        this.mHasDismissTargetSurface = true;
        updateDismissTargetLayer();
        return true;
    }

    public boolean maybeConsumeMotionEvent(MotionEvent motionEvent) {
        return this.mMagnetizedPip.maybeConsumeMotionEvent(motionEvent);
    }

    public void updateMagneticTargetSize() {
        if (this.mTargetView != null) {
            DismissView dismissView = this.mTargetViewContainer;
            if (dismissView != null) {
                dismissView.updateResources();
            }
            Resources resources = this.mContext.getResources();
            this.mTargetSize = resources.getDimensionPixelSize(R.dimen.dismiss_circle_size);
            this.mDismissAreaHeight = resources.getDimensionPixelSize(R.dimen.floating_dismiss_gradient_height);
            setMagneticFieldRadiusPercent(this.mMagneticFieldRadiusPercent);
        }
    }

    public void setMagneticFieldRadiusPercent(float f) {
        this.mMagneticFieldRadiusPercent = f;
        this.mMagneticTarget.setMagneticFieldRadiusPx((int) (f * ((float) this.mTargetSize) * 1.25f));
    }

    public void setTaskLeash(SurfaceControl surfaceControl) {
        this.mTaskLeash = surfaceControl;
    }

    public final void updateDismissTargetLayer() {
        if (this.mHasDismissTargetSurface && this.mTaskLeash != null) {
            SurfaceControl surfaceControl = this.mTargetViewContainer.getViewRootImpl().getSurfaceControl();
            if (surfaceControl.isValid()) {
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                transaction.setRelativeLayer(surfaceControl, this.mTaskLeash, -1);
                transaction.apply();
            }
        }
    }

    public void createOrUpdateDismissTarget() {
        if (!this.mTargetViewContainer.isAttachedToWindow()) {
            this.mTargetViewContainer.cancelAnimators();
            this.mTargetViewContainer.setVisibility(4);
            this.mTargetViewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
            this.mHasDismissTargetSurface = false;
            try {
                this.mWindowManager.addView(this.mTargetViewContainer, getDismissTargetLayoutParams());
            } catch (IllegalStateException unused) {
                this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getDismissTargetLayoutParams());
            }
        } else {
            this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getDismissTargetLayoutParams());
        }
    }

    public final WindowManager.LayoutParams getDismissTargetLayoutParams() {
        Point point = new Point();
        this.mWindowManager.getDefaultDisplay().getRealSize(point);
        int min = Math.min(point.y, this.mDismissAreaHeight);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, min, 0, point.y - min, 2024, 280, -3);
        layoutParams.setTitle("pip-dismiss-overlay");
        layoutParams.privateFlags |= 16;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        return layoutParams;
    }

    public void showDismissTargetMaybe() {
        if (this.mEnableDismissDragToEdge) {
            createOrUpdateDismissTarget();
            if (this.mTargetViewContainer.getVisibility() != 0) {
                this.mTargetViewContainer.getViewTreeObserver().addOnPreDrawListener(this);
                this.mTargetViewContainer.show();
            }
        }
    }

    public void hideDismissTargetMaybe() {
        if (this.mEnableDismissDragToEdge) {
            this.mTargetViewContainer.hide();
        }
    }

    public void cleanUpDismissTarget() {
        if (this.mTargetViewContainer.isAttachedToWindow()) {
            this.mWindowManager.removeViewImmediate(this.mTargetViewContainer);
        }
    }
}
