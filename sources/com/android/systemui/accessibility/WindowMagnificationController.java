package com.android.systemui.accessibility;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Choreographer;
import android.view.Display;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.WindowMetrics;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import androidx.core.math.MathUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.accessibility.MagnificationGestureDetector;
import com.android.systemui.model.SysUiState;
import com.android.systemui.shared.system.WindowManagerWrapper;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;

public class WindowMagnificationController implements View.OnTouchListener, SurfaceHolder.Callback, MagnificationGestureDetector.OnGestureListener, ComponentCallbacks {
    public static final Range<Float> A11Y_ACTION_SCALE_RANGE = new Range<>(Float.valueOf(2.0f), Float.valueOf(8.0f));
    public static final boolean DEBUG = (Log.isLoggable("WindowMagnificationController", 3) || Build.IS_DEBUGGABLE);
    public final WindowMagnificationAnimationController mAnimationController;
    public int mBorderDragSize;
    public View mBottomDrag;
    public float mBounceEffectAnimationScale;
    public final int mBounceEffectDuration;
    public final Configuration mConfiguration;
    public final Context mContext;
    public final int mDisplayId;
    public View mDragView;
    public int mDragViewSize;
    public final MagnificationGestureDetector mGestureDetector;
    public final Handler mHandler;
    public View mLeftDrag;
    public Locale mLocale;
    public final Rect mMagnificationFrame = new Rect();
    public final Rect mMagnificationFrameBoundary = new Rect();
    public int mMagnificationFrameOffsetX = 0;
    public int mMagnificationFrameOffsetY = 0;
    public int mMinWindowSize;
    public SurfaceControl mMirrorSurface;
    public int mMirrorSurfaceMargin;
    public SurfaceView mMirrorSurfaceView;
    public final View.OnLayoutChangeListener mMirrorSurfaceViewLayoutChangeListener;
    public View mMirrorView;
    public final Rect mMirrorViewBounds = new Rect();
    public Choreographer.FrameCallback mMirrorViewGeometryVsyncCallback;
    public final View.OnLayoutChangeListener mMirrorViewLayoutChangeListener;
    public final Runnable mMirrorViewRunnable;
    public int mOuterBorderSize;
    public boolean mOverlapWithGestureInsets;
    public NumberFormat mPercentFormat;
    public final Resources mResources;
    public View mRightDrag;
    @VisibleForTesting
    public int mRotation;
    public float mScale;
    public final SfVsyncFrameCallbackProvider mSfVsyncFrameProvider;
    public final Rect mSourceBounds = new Rect();
    public SysUiState mSysUiState;
    public int mSystemGestureTop = -1;
    public final Rect mTmpRect = new Rect();
    public View mTopDrag;
    public final SurfaceControl.Transaction mTransaction;
    public final Runnable mUpdateStateDescriptionRunnable;
    public Rect mWindowBounds;
    public final Runnable mWindowInsetChangeRunnable;
    public final WindowMagnifierCallback mWindowMagnifierCallback;
    public final WindowManager mWm;

    public boolean onFinish(float f, float f2) {
        return false;
    }

    public void onLowMemory() {
    }

    public boolean onStart(float f, float f2) {
        return true;
    }

    public final void showControls() {
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public WindowMagnificationController(Context context, Handler handler, WindowMagnificationAnimationController windowMagnificationAnimationController, SfVsyncFrameCallbackProvider sfVsyncFrameCallbackProvider, MirrorWindowControl mirrorWindowControl, SurfaceControl.Transaction transaction, WindowMagnifierCallback windowMagnifierCallback, SysUiState sysUiState) {
        this.mContext = context;
        this.mHandler = handler;
        this.mAnimationController = windowMagnificationAnimationController;
        windowMagnificationAnimationController.setWindowMagnificationController(this);
        this.mSfVsyncFrameProvider = sfVsyncFrameCallbackProvider;
        this.mWindowMagnifierCallback = windowMagnifierCallback;
        this.mSysUiState = sysUiState;
        this.mConfiguration = new Configuration(context.getResources().getConfiguration());
        Display display = context.getDisplay();
        this.mDisplayId = context.getDisplayId();
        this.mRotation = display.getRotation();
        WindowManager windowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mWm = windowManager;
        this.mWindowBounds = new Rect(windowManager.getCurrentWindowMetrics().getBounds());
        Resources resources = context.getResources();
        this.mResources = resources;
        this.mScale = (float) resources.getInteger(R$integer.magnification_default_scale);
        this.mBounceEffectDuration = resources.getInteger(17694720);
        updateDimensions();
        Size defaultWindowSizeWithWindowBounds = getDefaultWindowSizeWithWindowBounds(this.mWindowBounds);
        setMagnificationFrame(defaultWindowSizeWithWindowBounds.getWidth(), defaultWindowSizeWithWindowBounds.getHeight(), this.mWindowBounds.width() / 2, this.mWindowBounds.height() / 2);
        computeBounceAnimationScale();
        this.mTransaction = transaction;
        this.mGestureDetector = new MagnificationGestureDetector(context, handler, this);
        this.mMirrorViewRunnable = new WindowMagnificationController$$ExternalSyntheticLambda1(this);
        this.mMirrorViewLayoutChangeListener = new WindowMagnificationController$$ExternalSyntheticLambda2(this);
        this.mMirrorSurfaceViewLayoutChangeListener = new WindowMagnificationController$$ExternalSyntheticLambda3(this);
        this.mMirrorViewGeometryVsyncCallback = new WindowMagnificationController$$ExternalSyntheticLambda4(this);
        this.mUpdateStateDescriptionRunnable = new WindowMagnificationController$$ExternalSyntheticLambda5(this);
        this.mWindowInsetChangeRunnable = new WindowMagnificationController$$ExternalSyntheticLambda6(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mMirrorView != null) {
            Rect rect = new Rect(this.mMirrorViewBounds);
            this.mMirrorView.getBoundsOnScreen(this.mMirrorViewBounds);
            if (!(rect.width() == this.mMirrorViewBounds.width() && rect.height() == this.mMirrorViewBounds.height())) {
                this.mMirrorView.setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, this.mMirrorViewBounds.width(), this.mMirrorViewBounds.height())));
            }
            updateSystemUIStateIfNeeded();
            this.mWindowMagnifierCallback.onWindowMagnifierBoundsChanged(this.mDisplayId, this.mMirrorViewBounds);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (!this.mHandler.hasCallbacks(this.mMirrorViewRunnable)) {
            this.mHandler.post(this.mMirrorViewRunnable);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        applyTapExcludeRegion();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(long j) {
        if (isWindowVisible() && this.mMirrorSurface != null && calculateSourceBounds(this.mMagnificationFrame, this.mScale)) {
            this.mTmpRect.set(0, 0, this.mMagnificationFrame.width(), this.mMagnificationFrame.height());
            this.mTransaction.setGeometry(this.mMirrorSurface, this.mSourceBounds, this.mTmpRect, 0).apply();
            if (!this.mAnimationController.isAnimating()) {
                this.mWindowMagnifierCallback.onSourceBoundsChanged(this.mDisplayId, this.mSourceBounds);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        if (isWindowVisible()) {
            this.mMirrorView.setStateDescription(formatStateDescription(this.mScale));
        }
    }

    public final void updateDimensions() {
        this.mMirrorSurfaceMargin = this.mResources.getDimensionPixelSize(R$dimen.magnification_mirror_surface_margin);
        this.mBorderDragSize = this.mResources.getDimensionPixelSize(R$dimen.magnification_border_drag_size);
        this.mDragViewSize = this.mResources.getDimensionPixelSize(R$dimen.magnification_drag_view_size);
        this.mOuterBorderSize = this.mResources.getDimensionPixelSize(R$dimen.magnification_outer_border_margin);
        this.mMinWindowSize = this.mResources.getDimensionPixelSize(17104911);
    }

    public final void computeBounceAnimationScale() {
        float width = (float) (this.mMagnificationFrame.width() + (this.mMirrorSurfaceMargin * 2));
        this.mBounceEffectAnimationScale = Math.min(width / (width - ((float) (this.mOuterBorderSize * 2))), 1.05f);
    }

    public final boolean updateSystemGestureInsetsTop() {
        WindowMetrics currentWindowMetrics = this.mWm.getCurrentWindowMetrics();
        Insets insets = currentWindowMetrics.getWindowInsets().getInsets(WindowInsets.Type.systemGestures());
        int i = insets.bottom != 0 ? currentWindowMetrics.getBounds().bottom - insets.bottom : -1;
        if (i == this.mSystemGestureTop) {
            return false;
        }
        this.mSystemGestureTop = i;
        return true;
    }

    public void deleteWindowMagnification(IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mAnimationController.deleteWindowMagnification(iRemoteMagnificationAnimationCallback);
    }

    public void deleteWindowMagnification() {
        if (isWindowVisible()) {
            SurfaceControl surfaceControl = this.mMirrorSurface;
            if (surfaceControl != null) {
                this.mTransaction.remove(surfaceControl).apply();
                this.mMirrorSurface = null;
            }
            SurfaceView surfaceView = this.mMirrorSurfaceView;
            if (surfaceView != null) {
                surfaceView.removeOnLayoutChangeListener(this.mMirrorSurfaceViewLayoutChangeListener);
            }
            if (this.mMirrorView != null) {
                this.mHandler.removeCallbacks(this.mMirrorViewRunnable);
                this.mMirrorView.removeOnLayoutChangeListener(this.mMirrorViewLayoutChangeListener);
                this.mWm.removeView(this.mMirrorView);
                this.mMirrorView = null;
            }
            this.mMirrorViewBounds.setEmpty();
            this.mSourceBounds.setEmpty();
            updateSystemUIStateIfNeeded();
            this.mContext.unregisterComponentCallbacks(this);
            this.mWindowMagnifierCallback.onSourceBoundsChanged(this.mDisplayId, new Rect());
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        int diff = configuration.diff(this.mConfiguration);
        this.mConfiguration.setTo(configuration);
        onConfigurationChanged(diff);
    }

    public void onConfigurationChanged(int i) {
        if (DEBUG) {
            Log.d("WindowMagnificationController", "onConfigurationChanged = " + Configuration.configurationDiffToString(i));
        }
        if (i != 0) {
            if ((i & 128) != 0) {
                onRotate();
            }
            if ((i & 4) != 0) {
                updateAccessibilityWindowTitleIfNeeded();
            }
            boolean z = false;
            if ((i & 4096) != 0) {
                updateDimensions();
                computeBounceAnimationScale();
                z = true;
            }
            if ((i & 1024) != 0) {
                z |= handleScreenSizeChanged();
            }
            if (isWindowVisible() && z) {
                deleteWindowMagnification();
                enableWindowMagnificationInternal(Float.NaN, Float.NaN, Float.NaN);
            }
        }
    }

    public final boolean handleScreenSizeChanged() {
        Rect rect = new Rect(this.mWindowBounds);
        Rect bounds = this.mWm.getCurrentWindowMetrics().getBounds();
        if (!bounds.equals(rect)) {
            this.mWindowBounds.set(bounds);
            Size defaultWindowSizeWithWindowBounds = getDefaultWindowSizeWithWindowBounds(this.mWindowBounds);
            setMagnificationFrame(defaultWindowSizeWithWindowBounds.getWidth(), defaultWindowSizeWithWindowBounds.getHeight(), (int) ((getCenterX() * ((float) this.mWindowBounds.width())) / ((float) rect.width())), (int) ((getCenterY() * ((float) this.mWindowBounds.height())) / ((float) rect.height())));
            calculateMagnificationFrameBoundary();
            return true;
        } else if (!DEBUG) {
            return false;
        } else {
            Log.d("WindowMagnificationController", "handleScreenSizeChanged -- window bounds is not changed");
            return false;
        }
    }

    public final void updateSystemUIStateIfNeeded() {
        updateSysUIState(false);
    }

    public final void updateAccessibilityWindowTitleIfNeeded() {
        if (isWindowVisible()) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mMirrorView.getLayoutParams();
            layoutParams.accessibilityTitle = getAccessibilityWindowTitle();
            this.mWm.updateViewLayout(this.mMirrorView, layoutParams);
        }
    }

    public final void onRotate() {
        Display display = this.mContext.getDisplay();
        int i = this.mRotation;
        int rotation = display.getRotation();
        this.mRotation = rotation;
        int degreeFromRotation = getDegreeFromRotation(rotation, i);
        if (degreeFromRotation == 0 || degreeFromRotation == 180) {
            Log.w("WindowMagnificationController", "onRotate -- rotate with the device. skip it");
            return;
        }
        Rect rect = new Rect(this.mWm.getCurrentWindowMetrics().getBounds());
        if (rect.width() == this.mWindowBounds.height() && rect.height() == this.mWindowBounds.width()) {
            this.mWindowBounds.set(rect);
            Matrix matrix = new Matrix();
            matrix.setRotate((float) degreeFromRotation);
            if (degreeFromRotation == 90) {
                matrix.postTranslate((float) this.mWindowBounds.width(), 0.0f);
            } else if (degreeFromRotation == 270) {
                matrix.postTranslate(0.0f, (float) this.mWindowBounds.height());
            }
            RectF rectF = new RectF(this.mMagnificationFrame);
            int i2 = this.mMirrorSurfaceMargin;
            rectF.inset((float) (-i2), (float) (-i2));
            matrix.mapRect(rectF);
            setWindowSizeAndCenter((int) rectF.width(), (int) rectF.height(), (float) ((int) rectF.centerX()), (float) ((int) rectF.centerY()));
            return;
        }
        Log.w("WindowMagnificationController", "onRotate -- unexpected window height/width");
    }

    public final int getDegreeFromRotation(int i, int i2) {
        return (((i2 - i) + 4) % 4) * 90;
    }

    public final void createMirrorWindow() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(this.mMagnificationFrame.width() + (this.mMirrorSurfaceMargin * 2), this.mMagnificationFrame.height() + (this.mMirrorSurfaceMargin * 2), 2039, 40, -2);
        layoutParams.gravity = 51;
        Rect rect = this.mMagnificationFrame;
        int i = rect.left;
        int i2 = this.mMirrorSurfaceMargin;
        layoutParams.x = i - i2;
        layoutParams.y = rect.top - i2;
        layoutParams.layoutInDisplayCutoutMode = 1;
        layoutParams.receiveInsetsIgnoringZOrder = true;
        layoutParams.setTitle(this.mContext.getString(R$string.magnification_window_title));
        layoutParams.accessibilityTitle = getAccessibilityWindowTitle();
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.window_magnifier_view, (ViewGroup) null);
        this.mMirrorView = inflate;
        SurfaceView surfaceView = (SurfaceView) inflate.findViewById(R$id.surface_view);
        this.mMirrorSurfaceView = surfaceView;
        surfaceView.addOnLayoutChangeListener(this.mMirrorSurfaceViewLayoutChangeListener);
        this.mMirrorView.setSystemUiVisibility(5894);
        this.mMirrorView.addOnLayoutChangeListener(this.mMirrorViewLayoutChangeListener);
        this.mMirrorView.setAccessibilityDelegate(new MirrorWindowA11yDelegate());
        this.mMirrorView.setOnApplyWindowInsetsListener(new WindowMagnificationController$$ExternalSyntheticLambda0(this));
        this.mWm.addView(this.mMirrorView, layoutParams);
        SurfaceHolder holder = this.mMirrorSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setFormat(1);
        addDragTouchListeners();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$createMirrorWindow$5(View view, WindowInsets windowInsets) {
        if (!this.mHandler.hasCallbacks(this.mWindowInsetChangeRunnable)) {
            this.mHandler.post(this.mWindowInsetChangeRunnable);
        }
        return view.onApplyWindowInsets(windowInsets);
    }

    public final void onWindowInsetChanged() {
        if (updateSystemGestureInsetsTop()) {
            updateSystemUIStateIfNeeded();
        }
    }

    public final void applyTapExcludeRegion() {
        Region calculateTapExclude = calculateTapExclude();
        try {
            WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(IWindow.Stub.asInterface(this.mMirrorView.getWindowToken()), calculateTapExclude);
        } catch (RemoteException unused) {
        }
    }

    public final Region calculateTapExclude() {
        int i = this.mBorderDragSize;
        Region region = new Region(i, i, this.mMirrorView.getWidth() - this.mBorderDragSize, this.mMirrorView.getHeight() - this.mBorderDragSize);
        region.op(new Rect((this.mMirrorView.getWidth() - this.mDragViewSize) - this.mBorderDragSize, (this.mMirrorView.getHeight() - this.mDragViewSize) - this.mBorderDragSize, this.mMirrorView.getWidth(), this.mMirrorView.getHeight()), Region.Op.DIFFERENCE);
        return region;
    }

    public final String getAccessibilityWindowTitle() {
        return this.mResources.getString(17039666);
    }

    public void setWindowSizeAndCenter(int i, int i2, float f, float f2) {
        int clamp = MathUtils.clamp(i, this.mMinWindowSize, this.mWindowBounds.width());
        int clamp2 = MathUtils.clamp(i2, this.mMinWindowSize, this.mWindowBounds.height());
        if (Float.isNaN(f)) {
            f = (float) this.mMagnificationFrame.centerX();
        }
        if (Float.isNaN(f)) {
            f2 = (float) this.mMagnificationFrame.centerY();
        }
        int i3 = this.mMirrorSurfaceMargin;
        setMagnificationFrame(clamp - (i3 * 2), clamp2 - (i3 * 2), (int) f, (int) f2);
        calculateMagnificationFrameBoundary();
        updateMagnificationFramePosition(0, 0);
        modifyWindowMagnification(true);
    }

    public final void setMagnificationFrame(int i, int i2, int i3, int i4) {
        int i5 = i3 - (i / 2);
        int i6 = i4 - (i2 / 2);
        this.mMagnificationFrame.set(i5, i6, i + i5, i2 + i6);
    }

    public final Size getDefaultWindowSizeWithWindowBounds(Rect rect) {
        int min = Math.min(this.mResources.getDimensionPixelSize(R$dimen.magnification_max_frame_size), Math.min(rect.width(), rect.height()) / 2) + (this.mMirrorSurfaceMargin * 2);
        return new Size(min, min);
    }

    public final void createMirror() {
        SurfaceControl mirrorDisplay = WindowManagerWrapper.getInstance().mirrorDisplay(this.mDisplayId);
        this.mMirrorSurface = mirrorDisplay;
        if (mirrorDisplay.isValid()) {
            this.mTransaction.show(this.mMirrorSurface).reparent(this.mMirrorSurface, this.mMirrorSurfaceView.getSurfaceControl());
            modifyWindowMagnification(false);
        }
    }

    public final void addDragTouchListeners() {
        this.mDragView = this.mMirrorView.findViewById(R$id.drag_handle);
        this.mLeftDrag = this.mMirrorView.findViewById(R$id.left_handle);
        this.mTopDrag = this.mMirrorView.findViewById(R$id.top_handle);
        this.mRightDrag = this.mMirrorView.findViewById(R$id.right_handle);
        this.mBottomDrag = this.mMirrorView.findViewById(R$id.bottom_handle);
        this.mDragView.setOnTouchListener(this);
        this.mLeftDrag.setOnTouchListener(this);
        this.mTopDrag.setOnTouchListener(this);
        this.mRightDrag.setOnTouchListener(this);
        this.mBottomDrag.setOnTouchListener(this);
    }

    public final void modifyWindowMagnification(boolean z) {
        this.mSfVsyncFrameProvider.postFrameCallback(this.mMirrorViewGeometryVsyncCallback);
        updateMirrorViewLayout(z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0077  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateMirrorViewLayout(boolean r7) {
        /*
            r6 = this;
            boolean r0 = r6.isWindowVisible()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            android.graphics.Rect r0 = r6.mWindowBounds
            int r0 = r0.width()
            android.view.View r1 = r6.mMirrorView
            int r1 = r1.getWidth()
            int r0 = r0 - r1
            android.graphics.Rect r1 = r6.mWindowBounds
            int r1 = r1.height()
            android.view.View r2 = r6.mMirrorView
            int r2 = r2.getHeight()
            int r1 = r1 - r2
            android.view.View r2 = r6.mMirrorView
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            android.view.WindowManager$LayoutParams r2 = (android.view.WindowManager.LayoutParams) r2
            android.graphics.Rect r3 = r6.mMagnificationFrame
            int r4 = r3.left
            int r5 = r6.mMirrorSurfaceMargin
            int r4 = r4 - r5
            r2.x = r4
            int r4 = r3.top
            int r4 = r4 - r5
            r2.y = r4
            if (r7 == 0) goto L_0x0051
            int r7 = r3.width()
            int r3 = r6.mMirrorSurfaceMargin
            int r3 = r3 * 2
            int r7 = r7 + r3
            r2.width = r7
            android.graphics.Rect r7 = r6.mMagnificationFrame
            int r7 = r7.height()
            int r3 = r6.mMirrorSurfaceMargin
            int r3 = r3 * 2
            int r7 = r7 + r3
            r2.height = r7
        L_0x0051:
            int r7 = r2.x
            r3 = 0
            if (r7 >= 0) goto L_0x005f
            int r0 = r6.mOuterBorderSize
            int r0 = -r0
            int r7 = java.lang.Math.max(r7, r0)
        L_0x005d:
            float r7 = (float) r7
            goto L_0x006a
        L_0x005f:
            if (r7 <= r0) goto L_0x0069
            int r7 = r7 - r0
            int r0 = r6.mOuterBorderSize
            int r7 = java.lang.Math.min(r7, r0)
            goto L_0x005d
        L_0x0069:
            r7 = r3
        L_0x006a:
            int r0 = r2.y
            if (r0 >= 0) goto L_0x0077
            int r1 = r6.mOuterBorderSize
            int r1 = -r1
            int r0 = java.lang.Math.max(r0, r1)
        L_0x0075:
            float r3 = (float) r0
            goto L_0x0081
        L_0x0077:
            if (r0 <= r1) goto L_0x0081
            int r0 = r0 - r1
            int r1 = r6.mOuterBorderSize
            int r0 = java.lang.Math.min(r0, r1)
            goto L_0x0075
        L_0x0081:
            android.view.View r0 = r6.mMirrorView
            r0.setTranslationX(r7)
            android.view.View r7 = r6.mMirrorView
            r7.setTranslationY(r3)
            android.view.WindowManager r7 = r6.mWm
            android.view.View r6 = r6.mMirrorView
            r7.updateViewLayout(r6, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.WindowMagnificationController.updateMirrorViewLayout(boolean):void");
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == this.mDragView || view == this.mLeftDrag || view == this.mTopDrag || view == this.mRightDrag || view == this.mBottomDrag) {
            return this.mGestureDetector.onTouch(motionEvent);
        }
        return false;
    }

    public void updateSysUIStateFlag() {
        updateSysUIState(true);
    }

    public final boolean calculateSourceBounds(Rect rect, float f) {
        Rect rect2 = this.mTmpRect;
        rect2.set(this.mSourceBounds);
        int width = rect.width() / 2;
        int height = rect.height() / 2;
        int i = width - ((int) (((float) width) / f));
        int i2 = rect.right - i;
        int i3 = height - ((int) (((float) height) / f));
        this.mSourceBounds.set(rect.left + i, rect.top + i3, i2, rect.bottom - i3);
        this.mSourceBounds.offset(-this.mMagnificationFrameOffsetX, -this.mMagnificationFrameOffsetY);
        Rect rect3 = this.mSourceBounds;
        if (rect3.left < 0) {
            rect3.offsetTo(0, rect3.top);
        } else if (rect3.right > this.mWindowBounds.width()) {
            this.mSourceBounds.offsetTo(this.mWindowBounds.width() - this.mSourceBounds.width(), this.mSourceBounds.top);
        }
        Rect rect4 = this.mSourceBounds;
        if (rect4.top < 0) {
            rect4.offsetTo(rect4.left, 0);
        } else if (rect4.bottom > this.mWindowBounds.height()) {
            Rect rect5 = this.mSourceBounds;
            rect5.offsetTo(rect5.left, this.mWindowBounds.height() - this.mSourceBounds.height());
        }
        return !this.mSourceBounds.equals(rect2);
    }

    public final void calculateMagnificationFrameBoundary() {
        int width = this.mMagnificationFrame.width() / 2;
        int height = this.mMagnificationFrame.height() / 2;
        float f = this.mScale;
        int i = width - ((int) (((float) width) / f));
        int max = Math.max(i - this.mMagnificationFrameOffsetX, 0);
        int max2 = Math.max(i + this.mMagnificationFrameOffsetX, 0);
        int i2 = height - ((int) (((float) height) / f));
        this.mMagnificationFrameBoundary.set(-max, -Math.max(i2 - this.mMagnificationFrameOffsetY, 0), this.mWindowBounds.width() + max2, this.mWindowBounds.height() + Math.max(i2 + this.mMagnificationFrameOffsetY, 0));
    }

    public final boolean updateMagnificationFramePosition(int i, int i2) {
        this.mTmpRect.set(this.mMagnificationFrame);
        this.mTmpRect.offset(i, i2);
        Rect rect = this.mTmpRect;
        int i3 = rect.left;
        Rect rect2 = this.mMagnificationFrameBoundary;
        int i4 = rect2.left;
        if (i3 < i4) {
            rect.offsetTo(i4, rect.top);
        } else {
            int i5 = rect.right;
            int i6 = rect2.right;
            if (i5 > i6) {
                int width = i6 - this.mMagnificationFrame.width();
                Rect rect3 = this.mTmpRect;
                rect3.offsetTo(width, rect3.top);
            }
        }
        Rect rect4 = this.mTmpRect;
        int i7 = rect4.top;
        Rect rect5 = this.mMagnificationFrameBoundary;
        int i8 = rect5.top;
        if (i7 < i8) {
            rect4.offsetTo(rect4.left, i8);
        } else {
            int i9 = rect4.bottom;
            int i10 = rect5.bottom;
            if (i9 > i10) {
                int height = i10 - this.mMagnificationFrame.height();
                Rect rect6 = this.mTmpRect;
                rect6.offsetTo(rect6.left, height);
            }
        }
        if (this.mTmpRect.equals(this.mMagnificationFrame)) {
            return false;
        }
        this.mMagnificationFrame.set(this.mTmpRect);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = r2.mSystemGestureTop;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateSysUIState(boolean r3) {
        /*
            r2 = this;
            boolean r0 = r2.isWindowVisible()
            if (r0 == 0) goto L_0x0012
            int r0 = r2.mSystemGestureTop
            if (r0 <= 0) goto L_0x0012
            android.graphics.Rect r1 = r2.mMirrorViewBounds
            int r1 = r1.bottom
            if (r1 <= r0) goto L_0x0012
            r0 = 1
            goto L_0x0013
        L_0x0012:
            r0 = 0
        L_0x0013:
            if (r3 != 0) goto L_0x0019
            boolean r3 = r2.mOverlapWithGestureInsets
            if (r0 == r3) goto L_0x0028
        L_0x0019:
            r2.mOverlapWithGestureInsets = r0
            com.android.systemui.model.SysUiState r3 = r2.mSysUiState
            r1 = 524288(0x80000, float:7.34684E-40)
            com.android.systemui.model.SysUiState r3 = r3.setFlag(r1, r0)
            int r2 = r2.mDisplayId
            r3.commitUpdate(r2)
        L_0x0028:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.WindowMagnificationController.updateSysUIState(boolean):void");
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        createMirror();
    }

    public void move(int i, int i2) {
        moveWindowMagnifier((float) i, (float) i2);
        this.mWindowMagnifierCallback.onMove(this.mDisplayId);
    }

    public void enableWindowMagnification(float f, float f2, float f3, float f4, float f5, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mAnimationController.enableWindowMagnification(f, f2, f3, f4, f5, iRemoteMagnificationAnimationCallback);
    }

    public void enableWindowMagnificationInternal(float f, float f2, float f3) {
        enableWindowMagnificationInternal(f, f2, f3, Float.NaN, Float.NaN);
    }

    public void enableWindowMagnificationInternal(float f, float f2, float f3, float f4, float f5) {
        int i;
        int i2;
        float f6;
        if (Float.compare(f, 1.0f) <= 0) {
            deleteWindowMagnification();
            return;
        }
        if (!isWindowVisible()) {
            onConfigurationChanged(this.mResources.getConfiguration());
            this.mContext.registerComponentCallbacks(this);
        }
        if (Float.isNaN(f4)) {
            i = this.mMagnificationFrameOffsetX;
        } else {
            i = (int) (((float) (this.mMagnificationFrame.width() / 2)) * f4);
        }
        this.mMagnificationFrameOffsetX = i;
        if (Float.isNaN(f5)) {
            i2 = this.mMagnificationFrameOffsetY;
        } else {
            i2 = (int) (((float) (this.mMagnificationFrame.height() / 2)) * f5);
        }
        this.mMagnificationFrameOffsetY = i2;
        float f7 = ((float) this.mMagnificationFrameOffsetX) + f2;
        float f8 = ((float) i2) + f3;
        float f9 = 0.0f;
        if (Float.isNaN(f2)) {
            f6 = 0.0f;
        } else {
            f6 = f7 - this.mMagnificationFrame.exactCenterX();
        }
        if (!Float.isNaN(f3)) {
            f9 = f8 - this.mMagnificationFrame.exactCenterY();
        }
        if (Float.isNaN(f)) {
            f = this.mScale;
        }
        this.mScale = f;
        calculateMagnificationFrameBoundary();
        updateMagnificationFramePosition((int) f6, (int) f9);
        if (!isWindowVisible()) {
            createMirrorWindow();
            showControls();
            return;
        }
        modifyWindowMagnification(false);
    }

    public void setScale(float f) {
        if (!this.mAnimationController.isAnimating() && isWindowVisible() && this.mScale != f) {
            enableWindowMagnificationInternal(f, Float.NaN, Float.NaN);
            this.mHandler.removeCallbacks(this.mUpdateStateDescriptionRunnable);
            this.mHandler.postDelayed(this.mUpdateStateDescriptionRunnable, 100);
        }
    }

    public void moveWindowMagnifier(float f, float f2) {
        if (!this.mAnimationController.isAnimating() && this.mMirrorSurfaceView != null && updateMagnificationFramePosition((int) f, (int) f2)) {
            modifyWindowMagnification(false);
        }
    }

    public void moveWindowMagnifierToPosition(float f, float f2, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        if (this.mMirrorSurfaceView != null) {
            this.mAnimationController.moveWindowMagnifierToPosition(f, f2, iRemoteMagnificationAnimationCallback);
        }
    }

    public float getScale() {
        if (isWindowVisible()) {
            return this.mScale;
        }
        return Float.NaN;
    }

    public float getCenterX() {
        if (isWindowVisible()) {
            return this.mMagnificationFrame.exactCenterX();
        }
        return Float.NaN;
    }

    public float getCenterY() {
        if (isWindowVisible()) {
            return this.mMagnificationFrame.exactCenterY();
        }
        return Float.NaN;
    }

    public final boolean isWindowVisible() {
        return this.mMirrorView != null;
    }

    public final CharSequence formatStateDescription(float f) {
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mPercentFormat = NumberFormat.getPercentInstance(locale);
        }
        return this.mPercentFormat.format((double) f);
    }

    public boolean onSingleTap() {
        animateBounceEffect();
        return true;
    }

    public boolean onDrag(float f, float f2) {
        move((int) f, (int) f2);
        return true;
    }

    public final void animateBounceEffect() {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mMirrorView, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{1.0f, this.mBounceEffectAnimationScale, 1.0f}), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{1.0f, this.mBounceEffectAnimationScale, 1.0f})});
        ofPropertyValuesHolder.setDuration((long) this.mBounceEffectDuration);
        ofPropertyValuesHolder.start();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("WindowMagnificationController (displayId=" + this.mDisplayId + "):");
        StringBuilder sb = new StringBuilder();
        sb.append("      mOverlapWithGestureInsets:");
        sb.append(this.mOverlapWithGestureInsets);
        printWriter.println(sb.toString());
        printWriter.println("      mScale:" + this.mScale);
        printWriter.println("      mWindowBounds:" + this.mWindowBounds);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("      mMirrorViewBounds:");
        Object obj = "empty";
        sb2.append(isWindowVisible() ? this.mMirrorViewBounds : obj);
        printWriter.println(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("      mMagnificationFrameBoundary:");
        sb3.append(isWindowVisible() ? this.mMagnificationFrameBoundary : obj);
        printWriter.println(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("      mMagnificationFrame:");
        sb4.append(isWindowVisible() ? this.mMagnificationFrame : obj);
        printWriter.println(sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append("      mSourceBounds:");
        if (!this.mSourceBounds.isEmpty()) {
            obj = this.mSourceBounds;
        }
        sb5.append(obj);
        printWriter.println(sb5.toString());
        printWriter.println("      mSystemGestureTop:" + this.mSystemGestureTop);
        printWriter.println("      mMagnificationFrameOffsetX:" + this.mMagnificationFrameOffsetX);
        printWriter.println("      mMagnificationFrameOffsetY:" + this.mMagnificationFrameOffsetY);
    }

    public class MirrorWindowA11yDelegate extends View.AccessibilityDelegate {
        public MirrorWindowA11yDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_zoom_in, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_zoom_in)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_zoom_out, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_zoom_out)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_up, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_up)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_down, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_down)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_left, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_left)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_right, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_right)));
            accessibilityNodeInfo.setContentDescription(WindowMagnificationController.this.mContext.getString(R$string.magnification_window_title));
            WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
            accessibilityNodeInfo.setStateDescription(windowMagnificationController.formatStateDescription(windowMagnificationController.getScale()));
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (performA11yAction(i)) {
                return true;
            }
            return super.performAccessibilityAction(view, i, bundle);
        }

        public final boolean performA11yAction(int i) {
            if (i == R$id.accessibility_action_zoom_in) {
                WindowMagnificationController.this.mWindowMagnifierCallback.onPerformScaleAction(WindowMagnificationController.this.mDisplayId, ((Float) WindowMagnificationController.A11Y_ACTION_SCALE_RANGE.clamp(Float.valueOf(WindowMagnificationController.this.mScale + 1.0f))).floatValue());
            } else if (i == R$id.accessibility_action_zoom_out) {
                WindowMagnificationController.this.mWindowMagnifierCallback.onPerformScaleAction(WindowMagnificationController.this.mDisplayId, ((Float) WindowMagnificationController.A11Y_ACTION_SCALE_RANGE.clamp(Float.valueOf(WindowMagnificationController.this.mScale - 1.0f))).floatValue());
            } else if (i == R$id.accessibility_action_move_up) {
                WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
                windowMagnificationController.move(0, -windowMagnificationController.mSourceBounds.height());
            } else if (i == R$id.accessibility_action_move_down) {
                WindowMagnificationController windowMagnificationController2 = WindowMagnificationController.this;
                windowMagnificationController2.move(0, windowMagnificationController2.mSourceBounds.height());
            } else if (i == R$id.accessibility_action_move_left) {
                WindowMagnificationController windowMagnificationController3 = WindowMagnificationController.this;
                windowMagnificationController3.move(-windowMagnificationController3.mSourceBounds.width(), 0);
            } else if (i != R$id.accessibility_action_move_right) {
                return false;
            } else {
                WindowMagnificationController windowMagnificationController4 = WindowMagnificationController.this;
                windowMagnificationController4.move(windowMagnificationController4.mSourceBounds.width(), 0);
            }
            WindowMagnificationController.this.mWindowMagnifierCallback.onAccessibilityActionPerformed(WindowMagnificationController.this.mDisplayId);
            return true;
        }
    }
}
