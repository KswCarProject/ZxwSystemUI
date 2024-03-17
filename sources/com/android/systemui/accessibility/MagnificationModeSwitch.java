package com.android.systemui.accessibility;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.accessibility.MagnificationGestureDetector;
import java.util.Collections;

public class MagnificationModeSwitch implements MagnificationGestureDetector.OnGestureListener, ComponentCallbacks {
    @VisibleForTesting
    public static final int DEFAULT_FADE_OUT_ANIMATION_DELAY_MS = 5000;
    @VisibleForTesting
    public static final long FADING_ANIMATION_DURATION_MS = 300;
    public final AccessibilityManager mAccessibilityManager;
    public final Configuration mConfiguration;
    public final Context mContext;
    @VisibleForTesting
    public final Rect mDraggableWindowBounds;
    public final Runnable mFadeInAnimationTask;
    public final Runnable mFadeOutAnimationTask;
    public final MagnificationGestureDetector mGestureDetector;
    public final ImageView mImageView;
    @VisibleForTesting
    public boolean mIsFadeOutAnimating;
    public boolean mIsVisible;
    public int mMagnificationMode;
    public final WindowManager.LayoutParams mParams;
    public final SfVsyncFrameCallbackProvider mSfVsyncFrameProvider;
    public boolean mSingleTapDetected;
    public final SwitchListener mSwitchListener;
    public boolean mToLeftScreenEdge;
    public int mUiTimeout;
    public final Runnable mWindowInsetChangeRunnable;
    public final WindowManager mWindowManager;

    public interface SwitchListener {
        void onSwitch(int i, int i2);
    }

    public void onLowMemory() {
    }

    public MagnificationModeSwitch(Context context, SwitchListener switchListener) {
        this(context, createView(context), new SfVsyncFrameCallbackProvider(), switchListener);
    }

    @VisibleForTesting
    public MagnificationModeSwitch(Context context, ImageView imageView, SfVsyncFrameCallbackProvider sfVsyncFrameCallbackProvider, SwitchListener switchListener) {
        this.mIsFadeOutAnimating = false;
        this.mMagnificationMode = 0;
        this.mDraggableWindowBounds = new Rect();
        this.mIsVisible = false;
        this.mSingleTapDetected = false;
        this.mToLeftScreenEdge = false;
        this.mContext = context;
        this.mConfiguration = new Configuration(context.getResources().getConfiguration());
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mSfVsyncFrameProvider = sfVsyncFrameCallbackProvider;
        this.mSwitchListener = switchListener;
        this.mParams = createLayoutParams(context);
        this.mImageView = imageView;
        imageView.setOnTouchListener(new MagnificationModeSwitch$$ExternalSyntheticLambda1(this));
        imageView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setStateDescription(MagnificationModeSwitch.this.formatStateDescription());
                accessibilityNodeInfo.setContentDescription(MagnificationModeSwitch.this.mContext.getResources().getString(R$string.magnification_mode_switch_description));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId(), MagnificationModeSwitch.this.mContext.getResources().getString(R$string.magnification_mode_switch_click_label)));
                accessibilityNodeInfo.setClickable(true);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_up, MagnificationModeSwitch.this.mContext.getString(R$string.accessibility_control_move_up)));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_down, MagnificationModeSwitch.this.mContext.getString(R$string.accessibility_control_move_down)));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_left, MagnificationModeSwitch.this.mContext.getString(R$string.accessibility_control_move_left)));
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_right, MagnificationModeSwitch.this.mContext.getString(R$string.accessibility_control_move_right)));
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (performA11yAction(i)) {
                    return true;
                }
                return super.performAccessibilityAction(view, i, bundle);
            }

            public final boolean performA11yAction(int i) {
                Rect bounds = MagnificationModeSwitch.this.mWindowManager.getCurrentWindowMetrics().getBounds();
                if (i == AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId()) {
                    MagnificationModeSwitch.this.handleSingleTap();
                    return true;
                } else if (i == R$id.accessibility_action_move_up) {
                    MagnificationModeSwitch.this.moveButton(0.0f, (float) (-bounds.height()));
                    return true;
                } else if (i == R$id.accessibility_action_move_down) {
                    MagnificationModeSwitch.this.moveButton(0.0f, (float) bounds.height());
                    return true;
                } else if (i == R$id.accessibility_action_move_left) {
                    MagnificationModeSwitch.this.moveButton((float) (-bounds.width()), 0.0f);
                    return true;
                } else if (i != R$id.accessibility_action_move_right) {
                    return false;
                } else {
                    MagnificationModeSwitch.this.moveButton((float) bounds.width(), 0.0f);
                    return true;
                }
            }
        });
        this.mWindowInsetChangeRunnable = new MagnificationModeSwitch$$ExternalSyntheticLambda2(this);
        imageView.setOnApplyWindowInsetsListener(new MagnificationModeSwitch$$ExternalSyntheticLambda3(this));
        this.mFadeInAnimationTask = new MagnificationModeSwitch$$ExternalSyntheticLambda4(this);
        this.mFadeOutAnimationTask = new MagnificationModeSwitch$$ExternalSyntheticLambda5(this);
        this.mGestureDetector = new MagnificationGestureDetector(context, context.getMainThreadHandler(), this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$new$0(View view, WindowInsets windowInsets) {
        if (!this.mImageView.getHandler().hasCallbacks(this.mWindowInsetChangeRunnable)) {
            this.mImageView.getHandler().post(this.mWindowInsetChangeRunnable);
        }
        return view.onApplyWindowInsets(windowInsets);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mImageView.animate().alpha(1.0f).setDuration(300).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        this.mImageView.animate().alpha(0.0f).setDuration(300).withEndAction(new MagnificationModeSwitch$$ExternalSyntheticLambda6(this)).start();
        this.mIsFadeOutAnimating = true;
    }

    public final CharSequence formatStateDescription() {
        int i;
        if (this.mMagnificationMode == 2) {
            i = R$string.magnification_mode_switch_state_window;
        } else {
            i = R$string.magnification_mode_switch_state_full_screen;
        }
        return this.mContext.getResources().getString(i);
    }

    public final void applyResourcesValuesWithDensityChanged() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.magnification_switch_button_size);
        WindowManager.LayoutParams layoutParams = this.mParams;
        layoutParams.height = dimensionPixelSize;
        layoutParams.width = dimensionPixelSize;
        if (this.mIsVisible) {
            stickToScreenEdge(this.mToLeftScreenEdge);
            lambda$new$2();
            showButton(this.mMagnificationMode, false);
        }
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.mIsVisible) {
            return false;
        }
        return this.mGestureDetector.onTouch(motionEvent);
    }

    public boolean onSingleTap() {
        this.mSingleTapDetected = true;
        handleSingleTap();
        return true;
    }

    public boolean onDrag(float f, float f2) {
        moveButton(f, f2);
        return true;
    }

    public boolean onStart(float f, float f2) {
        stopFadeOutAnimation();
        return true;
    }

    public boolean onFinish(float f, float f2) {
        if (this.mIsVisible) {
            boolean z = this.mParams.x < this.mWindowManager.getCurrentWindowMetrics().getBounds().width() / 2;
            this.mToLeftScreenEdge = z;
            stickToScreenEdge(z);
        }
        if (!this.mSingleTapDetected) {
            showButton(this.mMagnificationMode);
        }
        this.mSingleTapDetected = false;
        return true;
    }

    public final void stickToScreenEdge(boolean z) {
        this.mParams.x = z ? this.mDraggableWindowBounds.left : this.mDraggableWindowBounds.right;
        updateButtonViewLayoutIfNeeded();
    }

    public final void moveButton(float f, float f2) {
        this.mSfVsyncFrameProvider.postFrameCallback(new MagnificationModeSwitch$$ExternalSyntheticLambda0(this, f, f2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$moveButton$4(float f, float f2, long j) {
        WindowManager.LayoutParams layoutParams = this.mParams;
        layoutParams.x = (int) (((float) layoutParams.x) + f);
        layoutParams.y = (int) (((float) layoutParams.y) + f2);
        updateButtonViewLayoutIfNeeded();
    }

    /* renamed from: removeButton */
    public void lambda$new$2() {
        if (this.mIsVisible) {
            this.mImageView.removeCallbacks(this.mFadeInAnimationTask);
            this.mImageView.removeCallbacks(this.mFadeOutAnimationTask);
            this.mImageView.animate().cancel();
            this.mIsFadeOutAnimating = false;
            this.mImageView.setAlpha(0.0f);
            this.mWindowManager.removeView(this.mImageView);
            this.mContext.unregisterComponentCallbacks(this);
            this.mIsVisible = false;
        }
    }

    public void showButton(int i) {
        showButton(i, true);
    }

    public final void showButton(int i, boolean z) {
        if (this.mMagnificationMode != i) {
            this.mMagnificationMode = i;
            this.mImageView.setImageResource(getIconResId(i));
        }
        if (!this.mIsVisible) {
            onConfigurationChanged(this.mContext.getResources().getConfiguration());
            this.mContext.registerComponentCallbacks(this);
            if (z) {
                this.mDraggableWindowBounds.set(getDraggableWindowBounds());
                WindowManager.LayoutParams layoutParams = this.mParams;
                Rect rect = this.mDraggableWindowBounds;
                layoutParams.x = rect.right;
                layoutParams.y = rect.bottom;
                this.mToLeftScreenEdge = false;
            }
            this.mWindowManager.addView(this.mImageView, this.mParams);
            setSystemGestureExclusion();
            this.mIsVisible = true;
            this.mImageView.postOnAnimation(this.mFadeInAnimationTask);
            this.mUiTimeout = this.mAccessibilityManager.getRecommendedTimeoutMillis(5000, 5);
        }
        stopFadeOutAnimation();
        this.mImageView.postOnAnimationDelayed(this.mFadeOutAnimationTask, (long) this.mUiTimeout);
    }

    public final void stopFadeOutAnimation() {
        this.mImageView.removeCallbacks(this.mFadeOutAnimationTask);
        if (this.mIsFadeOutAnimating) {
            this.mImageView.animate().cancel();
            this.mImageView.setAlpha(1.0f);
            this.mIsFadeOutAnimating = false;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        int diff = configuration.diff(this.mConfiguration);
        this.mConfiguration.setTo(configuration);
        onConfigurationChanged(diff);
    }

    public void onConfigurationChanged(int i) {
        if (i != 0) {
            if ((i & 1152) != 0) {
                Rect rect = new Rect(this.mDraggableWindowBounds);
                this.mDraggableWindowBounds.set(getDraggableWindowBounds());
                float height = ((float) (this.mParams.y - rect.top)) / ((float) rect.height());
                this.mParams.y = ((int) (height * ((float) this.mDraggableWindowBounds.height()))) + this.mDraggableWindowBounds.top;
                stickToScreenEdge(this.mToLeftScreenEdge);
            } else if ((i & 4096) != 0) {
                applyResourcesValuesWithDensityChanged();
            } else if ((i & 4) != 0) {
                updateAccessibilityWindowTitle();
            }
        }
    }

    public final void onWindowInsetChanged() {
        Rect draggableWindowBounds = getDraggableWindowBounds();
        if (!this.mDraggableWindowBounds.equals(draggableWindowBounds)) {
            this.mDraggableWindowBounds.set(draggableWindowBounds);
            stickToScreenEdge(this.mToLeftScreenEdge);
        }
    }

    public final void updateButtonViewLayoutIfNeeded() {
        if (this.mIsVisible) {
            WindowManager.LayoutParams layoutParams = this.mParams;
            int i = layoutParams.x;
            Rect rect = this.mDraggableWindowBounds;
            layoutParams.x = MathUtils.constrain(i, rect.left, rect.right);
            WindowManager.LayoutParams layoutParams2 = this.mParams;
            int i2 = layoutParams2.y;
            Rect rect2 = this.mDraggableWindowBounds;
            layoutParams2.y = MathUtils.constrain(i2, rect2.top, rect2.bottom);
            this.mWindowManager.updateViewLayout(this.mImageView, this.mParams);
        }
    }

    public final void updateAccessibilityWindowTitle() {
        this.mParams.accessibilityTitle = getAccessibilityWindowTitle(this.mContext);
        if (this.mIsVisible) {
            this.mWindowManager.updateViewLayout(this.mImageView, this.mParams);
        }
    }

    public final void toggleMagnificationMode() {
        int i = this.mMagnificationMode ^ 3;
        this.mMagnificationMode = i;
        this.mImageView.setImageResource(getIconResId(i));
        this.mSwitchListener.onSwitch(this.mContext.getDisplayId(), i);
    }

    public final void handleSingleTap() {
        lambda$new$2();
        toggleMagnificationMode();
    }

    public static ImageView createView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setAlpha(0.0f);
        return imageView;
    }

    @VisibleForTesting
    public static int getIconResId(int i) {
        if (i == 1) {
            return R$drawable.ic_open_in_new_window;
        }
        return R$drawable.ic_open_in_new_fullscreen;
    }

    public static WindowManager.LayoutParams createLayoutParams(Context context) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.magnification_switch_button_size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(dimensionPixelSize, dimensionPixelSize, 2039, 8, -2);
        layoutParams.gravity = 51;
        layoutParams.accessibilityTitle = getAccessibilityWindowTitle(context);
        layoutParams.layoutInDisplayCutoutMode = 3;
        return layoutParams;
    }

    public final Rect getDraggableWindowBounds() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.magnification_switch_button_margin);
        WindowMetrics currentWindowMetrics = this.mWindowManager.getCurrentWindowMetrics();
        Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        Rect rect = new Rect(currentWindowMetrics.getBounds());
        rect.offsetTo(0, 0);
        WindowManager.LayoutParams layoutParams = this.mParams;
        rect.inset(0, 0, layoutParams.width, layoutParams.height);
        rect.inset(insetsIgnoringVisibility);
        rect.inset(dimensionPixelSize, dimensionPixelSize);
        return rect;
    }

    public static String getAccessibilityWindowTitle(Context context) {
        return context.getString(17039666);
    }

    public final void setSystemGestureExclusion() {
        this.mImageView.post(new MagnificationModeSwitch$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSystemGestureExclusion$5() {
        this.mImageView.setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, this.mImageView.getWidth(), this.mImageView.getHeight())));
    }
}
