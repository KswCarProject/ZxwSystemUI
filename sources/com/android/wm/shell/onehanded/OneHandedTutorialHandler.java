package com.android.wm.shell.onehanded;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import com.android.wm.shell.onehanded.OneHandedState;
import java.io.PrintWriter;

public class OneHandedTutorialHandler implements OneHandedTransitionCallback, OneHandedState.OnStateChangedListener, OneHandedAnimationCallback {
    public int mAlphaAnimationDurationMs;
    public ValueAnimator mAlphaAnimator;
    public float mAlphaTransitionStart;
    public final BackgroundWindowManager mBackgroundWindowManager;
    public Context mContext;
    public int mCurrentState;
    public Rect mDisplayBounds;
    public ViewGroup mTargetViewContainer;
    public int mTutorialAreaHeight;
    public final float mTutorialHeightRatio;
    public View mTutorialView;
    public final WindowManager mWindowManager;

    public OneHandedTutorialHandler(Context context, OneHandedSettingsUtil oneHandedSettingsUtil, WindowManager windowManager, BackgroundWindowManager backgroundWindowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mBackgroundWindowManager = backgroundWindowManager;
        this.mTutorialHeightRatio = oneHandedSettingsUtil.getTranslationFraction(context);
        this.mAlphaAnimationDurationMs = oneHandedSettingsUtil.getTransitionDuration(context);
    }

    public void onOneHandedAnimationCancel(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
        ValueAnimator valueAnimator = this.mAlphaAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void onAnimationUpdate(SurfaceControl.Transaction transaction, float f, float f2) {
        if (isAttached()) {
            if (f2 < this.mAlphaTransitionStart) {
                checkTransitionEnd();
                return;
            }
            ValueAnimator valueAnimator = this.mAlphaAnimator;
            if (valueAnimator != null && !valueAnimator.isStarted() && !this.mAlphaAnimator.isRunning()) {
                this.mAlphaAnimator.start();
            }
        }
    }

    public void onStartFinished(Rect rect) {
        fillBackgroundColor();
    }

    public void onStopFinished(Rect rect) {
        removeBackgroundSurface();
    }

    public void onStateChanged(int i) {
        this.mCurrentState = i;
        this.mBackgroundWindowManager.onStateChanged(i);
        if (i != 0) {
            if (i == 1) {
                createViewAndAttachToWindow(this.mContext);
                updateThemeColor();
                setupAlphaTransition(true);
                return;
            } else if (i == 2) {
                checkTransitionEnd();
                setupAlphaTransition(false);
                return;
            } else if (i != 3) {
                return;
            }
        }
        checkTransitionEnd();
        removeTutorialFromWindowManager();
    }

    public void onDisplayChanged(DisplayLayout displayLayout) {
        Rect rect = new Rect(0, 0, displayLayout.width(), displayLayout.height());
        this.mDisplayBounds = rect;
        int round = Math.round(((float) rect.height()) * this.mTutorialHeightRatio);
        this.mTutorialAreaHeight = round;
        this.mAlphaTransitionStart = ((float) round) * 0.6f;
        this.mBackgroundWindowManager.onDisplayChanged(displayLayout);
    }

    @VisibleForTesting
    public void createViewAndAttachToWindow(Context context) {
        if (!isAttached()) {
            this.mTutorialView = LayoutInflater.from(context).inflate(R.layout.one_handed_tutorial, (ViewGroup) null);
            FrameLayout frameLayout = new FrameLayout(context);
            this.mTargetViewContainer = frameLayout;
            frameLayout.setClipChildren(false);
            this.mTargetViewContainer.setAlpha(this.mCurrentState == 2 ? 1.0f : 0.0f);
            this.mTargetViewContainer.addView(this.mTutorialView);
            this.mTargetViewContainer.setLayerType(2, (Paint) null);
            attachTargetToWindow();
        }
    }

    public final void attachTargetToWindow() {
        try {
            this.mWindowManager.addView(this.mTargetViewContainer, getTutorialTargetLayoutParams());
            this.mBackgroundWindowManager.showBackgroundLayer();
        } catch (IllegalStateException unused) {
            this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getTutorialTargetLayoutParams());
        }
    }

    @VisibleForTesting
    public void removeTutorialFromWindowManager() {
        if (isAttached()) {
            this.mTargetViewContainer.setLayerType(0, (Paint) null);
            this.mWindowManager.removeViewImmediate(this.mTargetViewContainer);
            this.mTargetViewContainer = null;
        }
    }

    @VisibleForTesting
    public void removeBackgroundSurface() {
        this.mBackgroundWindowManager.removeBackgroundLayer();
    }

    public final WindowManager.LayoutParams getTutorialTargetLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(this.mDisplayBounds.width(), this.mTutorialAreaHeight, 0, 0, 2024, 264, -3);
        layoutParams.gravity = 51;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.privateFlags |= 16;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("one-handed-tutorial-overlay");
        return layoutParams;
    }

    @VisibleForTesting
    public boolean isAttached() {
        ViewGroup viewGroup = this.mTargetViewContainer;
        return viewGroup != null && viewGroup.isAttachedToWindow();
    }

    public void onConfigurationChanged() {
        this.mBackgroundWindowManager.onConfigurationChanged();
        removeTutorialFromWindowManager();
        int i = this.mCurrentState;
        if (i == 1 || i == 2) {
            createViewAndAttachToWindow(this.mContext);
            fillBackgroundColor();
            updateThemeColor();
            checkTransitionEnd();
        }
    }

    public final void updateThemeColor() {
        if (this.mTutorialView != null) {
            TypedArray obtainStyledAttributes = new ContextThemeWrapper(this.mTutorialView.getContext(), 16974563).obtainStyledAttributes(new int[]{16842806, 16842808});
            int color = obtainStyledAttributes.getColor(0, 0);
            int color2 = obtainStyledAttributes.getColor(1, 0);
            obtainStyledAttributes.recycle();
            ((ImageView) this.mTutorialView.findViewById(R.id.one_handed_tutorial_image)).setImageTintList(ColorStateList.valueOf(color));
            ((TextView) this.mTutorialView.findViewById(R.id.one_handed_tutorial_title)).setTextColor(color);
            ((TextView) this.mTutorialView.findViewById(R.id.one_handed_tutorial_description)).setTextColor(color2);
        }
    }

    public final void fillBackgroundColor() {
        BackgroundWindowManager backgroundWindowManager;
        ViewGroup viewGroup = this.mTargetViewContainer;
        if (viewGroup != null && (backgroundWindowManager = this.mBackgroundWindowManager) != null) {
            viewGroup.setBackgroundColor(backgroundWindowManager.getThemeColorForBackground());
        }
    }

    public final void setupAlphaTransition(boolean z) {
        float f = 0.0f;
        float f2 = z ? 0.0f : 1.0f;
        if (z) {
            f = 1.0f;
        }
        int round = z ? this.mAlphaAnimationDurationMs : Math.round(((float) this.mAlphaAnimationDurationMs) * (1.0f - this.mTutorialHeightRatio));
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f2, f});
        this.mAlphaAnimator = ofFloat;
        ofFloat.setInterpolator(new LinearInterpolator());
        this.mAlphaAnimator.setDuration((long) round);
        this.mAlphaAnimator.addUpdateListener(new OneHandedTutorialHandler$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupAlphaTransition$0(ValueAnimator valueAnimator) {
        this.mTargetViewContainer.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public final void checkTransitionEnd() {
        ValueAnimator valueAnimator = this.mAlphaAnimator;
        if (valueAnimator == null) {
            return;
        }
        if (valueAnimator.isRunning() || this.mAlphaAnimator.isStarted()) {
            this.mAlphaAnimator.end();
            this.mAlphaAnimator.removeAllUpdateListeners();
            this.mAlphaAnimator = null;
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedTutorialHandler");
        printWriter.print("  isAttached=");
        printWriter.println(isAttached());
        printWriter.print("  mCurrentState=");
        printWriter.println(this.mCurrentState);
        printWriter.print("  mDisplayBounds=");
        printWriter.println(this.mDisplayBounds);
        printWriter.print("  mTutorialAreaHeight=");
        printWriter.println(this.mTutorialAreaHeight);
        printWriter.print("  mAlphaTransitionStart=");
        printWriter.println(this.mAlphaTransitionStart);
        printWriter.print("  mAlphaAnimationDurationMs=");
        printWriter.println(this.mAlphaAnimationDurationMs);
        BackgroundWindowManager backgroundWindowManager = this.mBackgroundWindowManager;
        if (backgroundWindowManager != null) {
            backgroundWindowManager.dump(printWriter);
        }
    }
}
