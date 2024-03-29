package com.android.wm.shell.pip.phone;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.provider.DeviceConfig;
import android.util.Size;
import android.view.DisplayCutout;
import android.view.SurfaceControl;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;
import java.util.Objects;

public class PipTouchHandler {
    public final AccessibilityManager mAccessibilityManager;
    public int mBottomOffsetBufferPx;
    public final PipAccessibilityInteractionConnection mConnection;
    public final Context mContext;
    public int mDeferResizeToNormalBoundsUntilRotation = -1;
    public int mDisplayRotation;
    public boolean mEnableResize;
    public boolean mEnableStash = true;
    public int mExpandedShortestEdgeSize;
    public final FloatingContentCoordinator mFloatingContentCoordinator;
    public PipTouchGesture mGesture;
    public int mImeHeight;
    public int mImeOffset;
    public final Rect mInsetBounds = new Rect();
    public boolean mIsImeShowing;
    public boolean mIsShelfShowing;
    public final ShellExecutor mMainExecutor;
    public final PhonePipMenuController mMenuController;
    public int mMenuState = 0;
    public float mMinimumSizePercent;
    public PipMotionHelper mMotionHelper;
    public int mMovementBoundsExtraOffsets;
    public boolean mMovementWithinDismiss;
    public final PipBoundsAlgorithm mPipBoundsAlgorithm;
    public final PipBoundsState mPipBoundsState;
    public final PipDismissTargetHandler mPipDismissTargetHandler;
    public PipResizeGestureHandler mPipResizeGestureHandler;
    public final PipTaskOrganizer mPipTaskOrganizer;
    public final PipUiEventLogger mPipUiEventLogger;
    public float mSavedSnapFraction = -1.0f;
    public boolean mSendingHoverAccessibilityEvents;
    public int mShelfHeight;
    public float mStashVelocityThreshold;
    public final Rect mTmpBounds = new Rect();
    public final PipTouchState mTouchState;

    public final boolean shouldShowResizeHandle() {
        return false;
    }

    public class PipMenuListener implements PhonePipMenuController.Listener {
        public PipMenuListener() {
        }

        public void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable) {
            PipTouchHandler.this.onPipMenuStateChangeStart(i, z, runnable);
        }

        public void onPipMenuStateChangeFinish(int i) {
            PipTouchHandler.this.setMenuState(i);
        }

        public void onPipExpand() {
            PipTouchHandler.this.mMotionHelper.expandLeavePip(false);
        }

        public void onEnterSplit() {
            PipTouchHandler.this.mMotionHelper.expandIntoSplit();
        }

        public void onPipDismiss() {
            PipTouchHandler.this.mTouchState.removeDoubleTapTimeoutCallback();
            PipTouchHandler.this.mMotionHelper.dismissPip();
        }

        public void onPipShowMenu() {
            PipTouchHandler.this.mMenuController.showMenu(1, PipTouchHandler.this.mPipBoundsState.getBounds(), true, PipTouchHandler.this.willResizeMenu(), PipTouchHandler.this.shouldShowResizeHandle());
        }
    }

    @SuppressLint({"InflateParams"})
    public PipTouchHandler(Context context, PhonePipMenuController phonePipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PipMotionHelper pipMotionHelper, FloatingContentCoordinator floatingContentCoordinator, PipUiEventLogger pipUiEventLogger, ShellExecutor shellExecutor) {
        Context context2 = context;
        PhonePipMenuController phonePipMenuController2 = phonePipMenuController;
        PipMotionHelper pipMotionHelper2 = pipMotionHelper;
        PipUiEventLogger pipUiEventLogger2 = pipUiEventLogger;
        ShellExecutor shellExecutor2 = shellExecutor;
        this.mContext = context2;
        this.mMainExecutor = shellExecutor2;
        this.mAccessibilityManager = (AccessibilityManager) context2.getSystemService(AccessibilityManager.class);
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipBoundsState = pipBoundsState;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mMenuController = phonePipMenuController2;
        this.mPipUiEventLogger = pipUiEventLogger2;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        phonePipMenuController2.addListener(new PipMenuListener());
        this.mGesture = new DefaultPipTouchGesture();
        this.mMotionHelper = pipMotionHelper2;
        PipDismissTargetHandler pipDismissTargetHandler = new PipDismissTargetHandler(context2, pipUiEventLogger2, pipMotionHelper2, shellExecutor2);
        this.mPipDismissTargetHandler = pipDismissTargetHandler;
        PipMotionHelper pipMotionHelper3 = this.mMotionHelper;
        PipTouchHandler$$ExternalSyntheticLambda0 pipTouchHandler$$ExternalSyntheticLambda0 = new PipTouchHandler$$ExternalSyntheticLambda0(this);
        PipTouchHandler$$ExternalSyntheticLambda0 pipTouchHandler$$ExternalSyntheticLambda02 = pipTouchHandler$$ExternalSyntheticLambda0;
        PipResizeGestureHandler pipResizeGestureHandler = r1;
        PipResizeGestureHandler pipResizeGestureHandler2 = new PipResizeGestureHandler(context, pipBoundsAlgorithm, pipBoundsState, pipMotionHelper3, pipTaskOrganizer, pipDismissTargetHandler, pipTouchHandler$$ExternalSyntheticLambda02, new PipTouchHandler$$ExternalSyntheticLambda1(this), pipUiEventLogger2, phonePipMenuController, shellExecutor);
        this.mPipResizeGestureHandler = pipResizeGestureHandler;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        PipTouchHandler$$ExternalSyntheticLambda2 pipTouchHandler$$ExternalSyntheticLambda2 = new PipTouchHandler$$ExternalSyntheticLambda2(this);
        Objects.requireNonNull(phonePipMenuController);
        this.mTouchState = new PipTouchState(viewConfiguration, pipTouchHandler$$ExternalSyntheticLambda2, new PipTouchHandler$$ExternalSyntheticLambda3(phonePipMenuController2), shellExecutor2);
        this.mConnection = new PipAccessibilityInteractionConnection(context, pipBoundsState, this.mMotionHelper, pipTaskOrganizer, pipBoundsAlgorithm.getSnapAlgorithm(), new PipTouchHandler$$ExternalSyntheticLambda4(this), new PipTouchHandler$$ExternalSyntheticLambda1(this), new PipTouchHandler$$ExternalSyntheticLambda5(this), shellExecutor);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mPipBoundsState.isStashed()) {
            animateToUnStashedState();
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_UNSTASHED);
            this.mPipBoundsState.setStashed(0);
            return;
        }
        this.mMenuController.showMenuWithPossibleDelay(1, this.mPipBoundsState.getBounds(), true, willResizeMenu(), shouldShowResizeHandle());
    }

    public void init() {
        this.mEnableResize = this.mContext.getResources().getBoolean(R.bool.config_pipEnableResizeForMenu);
        reloadResources();
        this.mMotionHelper.init();
        this.mPipResizeGestureHandler.init();
        this.mPipDismissTargetHandler.init();
        this.mEnableStash = DeviceConfig.getBoolean("systemui", "pip_stashing", true);
        DeviceConfig.addOnPropertiesChangedListener("systemui", this.mMainExecutor, new PipTouchHandler$$ExternalSyntheticLambda6(this));
        this.mStashVelocityThreshold = DeviceConfig.getFloat("systemui", "pip_velocity_threshold", 18000.0f);
        DeviceConfig.addOnPropertiesChangedListener("systemui", this.mMainExecutor, new PipTouchHandler$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains("pip_stashing")) {
            this.mEnableStash = properties.getBoolean("pip_stashing", true);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2(DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains("pip_velocity_threshold")) {
            this.mStashVelocityThreshold = properties.getFloat("pip_velocity_threshold", 18000.0f);
        }
    }

    public final void reloadResources() {
        Resources resources = this.mContext.getResources();
        this.mBottomOffsetBufferPx = resources.getDimensionPixelSize(R.dimen.pip_bottom_offset_buffer);
        this.mExpandedShortestEdgeSize = resources.getDimensionPixelSize(R.dimen.pip_expanded_shortest_edge_size);
        this.mImeOffset = resources.getDimensionPixelSize(R.dimen.pip_ime_offset);
        this.mMinimumSizePercent = resources.getFraction(R.fraction.config_pipShortestEdgePercent, 1, 1);
        this.mPipDismissTargetHandler.updateMagneticTargetSize();
    }

    public void onOverlayChanged() {
        this.mPipDismissTargetHandler.init();
    }

    public void setTouchEnabled(boolean z) {
        this.mTouchState.setAllowTouches(z);
    }

    public void showPictureInPictureMenu() {
        if (!this.mTouchState.isUserInteracting()) {
            this.mMenuController.showMenu(1, this.mPipBoundsState.getBounds(), false, willResizeMenu(), shouldShowResizeHandle());
        }
    }

    public void onActivityPinned() {
        this.mPipDismissTargetHandler.createOrUpdateDismissTarget();
        this.mPipResizeGestureHandler.onActivityPinned();
        this.mFloatingContentCoordinator.onContentAdded(this.mMotionHelper);
    }

    public void onActivityUnpinned(ComponentName componentName) {
        if (componentName == null) {
            this.mPipDismissTargetHandler.cleanUpDismissTarget();
            this.mFloatingContentCoordinator.onContentRemoved(this.mMotionHelper);
        }
        this.mPipResizeGestureHandler.onActivityUnpinned();
    }

    public void onPinnedStackAnimationEnded(int i) {
        this.mMotionHelper.synchronizePinnedStackBounds();
        updateMovementBounds();
        if (i == 2) {
            this.mPipResizeGestureHandler.setUserResizeBounds(this.mPipBoundsState.getBounds());
        }
    }

    public void onConfigurationChanged() {
        this.mPipResizeGestureHandler.onConfigurationChanged();
        this.mMotionHelper.synchronizePinnedStackBounds();
        reloadResources();
        this.mPipDismissTargetHandler.createOrUpdateDismissTarget();
    }

    public void onImeVisibilityChanged(boolean z, int i) {
        this.mIsImeShowing = z;
        this.mImeHeight = i;
    }

    public void onShelfVisibilityChanged(boolean z, int i) {
        this.mIsShelfShowing = z;
        this.mShelfHeight = i;
    }

    public void onSystemUiStateChanged(boolean z) {
        this.mPipResizeGestureHandler.onSystemUiStateChanged(z);
    }

    public void adjustBoundsForRotation(Rect rect, Rect rect2, Rect rect3) {
        Rect rect4 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect, rect3, rect4, 0);
        if ((this.mPipBoundsState.getMovementBounds().bottom - this.mMovementBoundsExtraOffsets) - this.mBottomOffsetBufferPx <= rect2.top) {
            rect.offsetTo(rect.left, rect4.bottom);
        }
    }

    public void onAspectRatioChanged() {
        this.mPipResizeGestureHandler.invalidateUserResizeBounds();
    }

    public void onMovementBoundsChanged(Rect rect, Rect rect2, Rect rect3, boolean z, boolean z2, int i) {
        int i2;
        if (this.mPipResizeGestureHandler.getUserResizeBounds().isEmpty()) {
            this.mPipResizeGestureHandler.setUserResizeBounds(rect2);
        }
        int i3 = 0;
        int i4 = this.mIsImeShowing ? this.mImeHeight : 0;
        boolean z3 = true;
        if (this.mDisplayRotation != i) {
            this.mTouchState.reset();
        }
        Rect rect4 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect2, rect, rect4, i4);
        if (this.mPipBoundsState.getMovementBounds().isEmpty()) {
            this.mPipBoundsAlgorithm.getMovementBounds(rect3, rect, this.mPipBoundsState.getMovementBounds(), 0);
        }
        float width = ((float) rect2.width()) / ((float) rect2.height());
        Point point = new Point();
        this.mContext.getDisplay().getRealSize(point);
        Size sizeForAspectRatio = this.mPipBoundsAlgorithm.getSizeForAspectRatio(width, (float) this.mExpandedShortestEdgeSize, point.x, point.y);
        this.mPipBoundsState.setExpandedBounds(new Rect(0, 0, sizeForAspectRatio.getWidth(), sizeForAspectRatio.getHeight()));
        Rect rect5 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(this.mPipBoundsState.getExpandedBounds(), rect, rect5, i4);
        if (this.mPipResizeGestureHandler.isUsingPinchToZoom()) {
            updatePinchResizeSizeConstraints(rect, rect2, width);
        } else {
            this.mPipResizeGestureHandler.updateMinSize(rect2.width(), rect2.height());
            this.mPipResizeGestureHandler.updateMaxSize(this.mPipBoundsState.getExpandedBounds().width(), this.mPipBoundsState.getExpandedBounds().height());
        }
        boolean z4 = this.mIsImeShowing;
        int max = Math.max(z4 ? this.mImeOffset : 0, (z4 || !this.mIsShelfShowing) ? 0 : this.mShelfHeight);
        if ((z || z2) && !this.mTouchState.isUserInteracting()) {
            if (this.mMenuState != 1 || !willResizeMenu()) {
                z3 = false;
            }
            Rect rect6 = new Rect();
            PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
            if (this.mIsImeShowing) {
                i3 = this.mImeHeight;
            }
            pipBoundsAlgorithm.getMovementBounds(rect3, rect, rect6, i3);
            int i5 = this.mPipBoundsState.getMovementBounds().bottom - this.mMovementBoundsExtraOffsets;
            int i6 = rect6.bottom;
            if (i6 >= rect6.top) {
                i6 -= max;
            }
            if (z3) {
                rect3.set(this.mPipBoundsState.getExpandedBounds());
                this.mPipBoundsAlgorithm.getSnapAlgorithm().applySnapFraction(rect3, rect6, this.mSavedSnapFraction);
            }
            if (i5 < i6) {
                int i7 = rect3.top;
                if (i7 > i5 - this.mBottomOffsetBufferPx) {
                    this.mMotionHelper.animateToOffset(rect3, i6 - i7);
                }
            } else if (i5 > i6 && (i2 = rect3.top) > i6 - this.mBottomOffsetBufferPx) {
                this.mMotionHelper.animateToOffset(rect3, i6 - i2);
            }
        }
        this.mPipBoundsState.setNormalMovementBounds(rect4);
        this.mPipBoundsState.setExpandedMovementBounds(rect5);
        this.mDisplayRotation = i;
        this.mInsetBounds.set(rect);
        updateMovementBounds();
        this.mMovementBoundsExtraOffsets = max;
        this.mConnection.onMovementBoundsChanged(rect2, this.mPipBoundsState.getExpandedBounds(), this.mPipBoundsState.getNormalMovementBounds(), this.mPipBoundsState.getExpandedMovementBounds());
        if (this.mDeferResizeToNormalBoundsUntilRotation == i) {
            this.mMotionHelper.animateToUnexpandedState(rect2, this.mSavedSnapFraction, this.mPipBoundsState.getNormalMovementBounds(), this.mPipBoundsState.getMovementBounds(), true);
            this.mSavedSnapFraction = -1.0f;
            this.mDeferResizeToNormalBoundsUntilRotation = -1;
        }
    }

    public final void updatePinchResizeSizeConstraints(Rect rect, Rect rect2, float f) {
        int i;
        int i2;
        int i3;
        int i4;
        int min = Math.min(this.mPipBoundsState.getDisplayBounds().width(), this.mPipBoundsState.getDisplayBounds().height());
        int width = rect.left + (this.mPipBoundsState.getDisplayBounds().width() - rect.right);
        int height = rect.top + (this.mPipBoundsState.getDisplayBounds().height() - rect.bottom);
        if (f > 1.0f) {
            i3 = (int) Math.min((float) rect2.width(), ((float) min) * this.mMinimumSizePercent);
            i4 = (int) (((float) i3) / f);
            i2 = Math.max(rect2.width(), min - width);
            i = (int) (((float) i2) / f);
        } else {
            int min2 = (int) Math.min((float) rect2.height(), ((float) min) * this.mMinimumSizePercent);
            int max = Math.max(rect2.height(), min - height);
            i4 = min2;
            i3 = (int) (((float) min2) * f);
            int i5 = (int) (((float) max) * f);
            i = max;
            i2 = i5;
        }
        this.mPipResizeGestureHandler.updateMinSize(i3, i4);
        this.mPipResizeGestureHandler.updateMaxSize(i2, i);
        this.mPipBoundsState.setMaxSize(i2, i);
        this.mPipBoundsState.setMinSize(i3, i4);
    }

    public void onRegistrationChanged(boolean z) {
        if (z) {
            this.mConnection.register(this.mAccessibilityManager);
        } else {
            this.mAccessibilityManager.setPictureInPictureActionReplacingConnection((IAccessibilityInteractionConnection) null);
        }
        if (!z && this.mTouchState.isUserInteracting()) {
            this.mPipDismissTargetHandler.cleanUpDismissTarget();
        }
    }

    public final void onAccessibilityShowMenu() {
        this.mMenuController.showMenu(1, this.mPipBoundsState.getBounds(), true, willResizeMenu(), shouldShowResizeHandle());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00e7, code lost:
        if (r11.mGesture.onUp(r11.mTouchState) != false) goto L_0x0109;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handleTouchEvent(android.view.InputEvent r12) {
        /*
            r11 = this;
            boolean r0 = r12 instanceof android.view.MotionEvent
            r1 = 1
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            android.view.MotionEvent r12 = (android.view.MotionEvent) r12
            com.android.wm.shell.pip.PipBoundsState r0 = r11.mPipBoundsState
            boolean r0 = r0.isStashed()
            if (r0 != 0) goto L_0x0023
            com.android.wm.shell.pip.phone.PipResizeGestureHandler r0 = r11.mPipResizeGestureHandler
            boolean r0 = r0.willStartResizeGesture(r12)
            if (r0 == 0) goto L_0x0023
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            r0.onTouchEvent(r12)
            com.android.wm.shell.pip.phone.PipTouchState r11 = r11.mTouchState
            r11.reset()
            return r1
        L_0x0023:
            com.android.wm.shell.pip.phone.PipResizeGestureHandler r0 = r11.mPipResizeGestureHandler
            boolean r0 = r0.hasOngoingGesture()
            if (r0 == 0) goto L_0x0031
            com.android.wm.shell.pip.phone.PipDismissTargetHandler r11 = r11.mPipDismissTargetHandler
            r11.hideDismissTargetMaybe()
            return r1
        L_0x0031:
            int r0 = r12.getAction()
            if (r0 == 0) goto L_0x003f
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            boolean r0 = r0.isUserInteracting()
            if (r0 == 0) goto L_0x0058
        L_0x003f:
            com.android.wm.shell.pip.phone.PipDismissTargetHandler r0 = r11.mPipDismissTargetHandler
            boolean r0 = r0.maybeConsumeMotionEvent(r12)
            if (r0 == 0) goto L_0x0058
            int r0 = r12.getAction()
            if (r0 != 0) goto L_0x0052
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            r0.onTouchEvent(r12)
        L_0x0052:
            com.android.wm.shell.pip.phone.PipTouchState r11 = r11.mTouchState
            r11.addMovementToVelocityTracker(r12)
            return r1
        L_0x0058:
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            r0.onTouchEvent(r12)
            int r0 = r11.mMenuState
            r2 = 0
            if (r0 == 0) goto L_0x0064
            r0 = r1
            goto L_0x0065
        L_0x0064:
            r0 = r2
        L_0x0065:
            int r3 = r12.getAction()
            r4 = 3
            if (r3 == 0) goto L_0x0102
            if (r3 == r1) goto L_0x00dc
            r5 = 2
            if (r3 == r5) goto L_0x00c9
            if (r3 == r4) goto L_0x00ea
            r5 = 7
            if (r3 == r5) goto L_0x00bb
            r5 = 9
            if (r3 == r5) goto L_0x009c
            r5 = 10
            if (r3 == r5) goto L_0x0080
            goto L_0x0109
        L_0x0080:
            android.view.accessibility.AccessibilityManager r3 = r11.mAccessibilityManager
            boolean r3 = r3.isTouchExplorationEnabled()
            if (r3 != 0) goto L_0x008d
            com.android.wm.shell.pip.phone.PipTouchState r3 = r11.mTouchState
            r3.scheduleHoverExitTimeoutCallback()
        L_0x008d:
            if (r0 != 0) goto L_0x0109
            boolean r3 = r11.mSendingHoverAccessibilityEvents
            if (r3 == 0) goto L_0x0109
            r3 = 256(0x100, float:3.59E-43)
            r11.sendAccessibilityHoverEvent(r3)
            r11.mSendingHoverAccessibilityEvents = r2
            goto L_0x0109
        L_0x009c:
            android.view.accessibility.AccessibilityManager r2 = r11.mAccessibilityManager
            boolean r2 = r2.isTouchExplorationEnabled()
            if (r2 != 0) goto L_0x00bb
            com.android.wm.shell.pip.phone.PipTouchState r2 = r11.mTouchState
            r2.removeHoverExitTimeoutCallback()
            com.android.wm.shell.pip.phone.PhonePipMenuController r5 = r11.mMenuController
            r6 = 1
            com.android.wm.shell.pip.PipBoundsState r2 = r11.mPipBoundsState
            android.graphics.Rect r7 = r2.getBounds()
            r8 = 0
            r9 = 0
            boolean r10 = r11.shouldShowResizeHandle()
            r5.showMenu(r6, r7, r8, r9, r10)
        L_0x00bb:
            if (r0 != 0) goto L_0x0109
            boolean r2 = r11.mSendingHoverAccessibilityEvents
            if (r2 != 0) goto L_0x0109
            r2 = 128(0x80, float:1.794E-43)
            r11.sendAccessibilityHoverEvent(r2)
            r11.mSendingHoverAccessibilityEvents = r1
            goto L_0x0109
        L_0x00c9:
            com.android.wm.shell.pip.phone.PipTouchGesture r2 = r11.mGesture
            com.android.wm.shell.pip.phone.PipTouchState r3 = r11.mTouchState
            boolean r2 = r2.onMove(r3)
            if (r2 == 0) goto L_0x00d4
            goto L_0x0109
        L_0x00d4:
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            boolean r0 = r0.isDragging()
            r0 = r0 ^ r1
            goto L_0x0109
        L_0x00dc:
            r11.updateMovementBounds()
            com.android.wm.shell.pip.phone.PipTouchGesture r3 = r11.mGesture
            com.android.wm.shell.pip.phone.PipTouchState r5 = r11.mTouchState
            boolean r3 = r3.onUp(r5)
            if (r3 == 0) goto L_0x00ea
            goto L_0x0109
        L_0x00ea:
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            boolean r0 = r0.startedDragging()
            if (r0 != 0) goto L_0x00fb
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            boolean r0 = r0.isDragging()
            if (r0 != 0) goto L_0x00fb
            r2 = r1
        L_0x00fb:
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            r0.reset()
            r0 = r2
            goto L_0x0109
        L_0x0102:
            com.android.wm.shell.pip.phone.PipTouchGesture r2 = r11.mGesture
            com.android.wm.shell.pip.phone.PipTouchState r3 = r11.mTouchState
            r2.onDown(r3)
        L_0x0109:
            com.android.wm.shell.pip.PipBoundsState r2 = r11.mPipBoundsState
            boolean r2 = r2.isStashed()
            r2 = r2 ^ r1
            r0 = r0 & r2
            if (r0 == 0) goto L_0x012f
            android.view.MotionEvent r12 = android.view.MotionEvent.obtain(r12)
            com.android.wm.shell.pip.phone.PipTouchState r0 = r11.mTouchState
            boolean r0 = r0.startedDragging()
            if (r0 == 0) goto L_0x0127
            r12.setAction(r4)
            com.android.wm.shell.pip.phone.PhonePipMenuController r0 = r11.mMenuController
            r0.pokeMenu()
        L_0x0127:
            com.android.wm.shell.pip.phone.PhonePipMenuController r11 = r11.mMenuController
            r11.handlePointerEvent(r12)
            r12.recycle()
        L_0x012f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PipTouchHandler.handleTouchEvent(android.view.InputEvent):boolean");
    }

    public final void sendAccessibilityHoverEvent(int i) {
        if (this.mAccessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
            obtain.setImportantForAccessibility(true);
            obtain.setSourceNodeId(AccessibilityNodeInfo.ROOT_NODE_ID);
            obtain.setWindowId(-3);
            this.mAccessibilityManager.sendAccessibilityEvent(obtain);
        }
    }

    public final void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable) {
        int rotation;
        int i2 = this.mMenuState;
        if (i2 == i && !z) {
            return;
        }
        if (i != 1 || i2 == 1) {
            if (i != 0 || i2 != 1) {
                return;
            }
            if (!z || this.mPipResizeGestureHandler.isResizing()) {
                this.mSavedSnapFraction = -1.0f;
                return;
            }
            if (this.mDeferResizeToNormalBoundsUntilRotation == -1 && this.mDisplayRotation != (rotation = this.mContext.getDisplay().getRotation())) {
                this.mDeferResizeToNormalBoundsUntilRotation = rotation;
            }
            if (this.mDeferResizeToNormalBoundsUntilRotation == -1) {
                animateToUnexpandedState(getUserResizeBounds());
            }
        } else if (z) {
            animateToNormalSize(runnable);
        }
    }

    public final void setMenuState(int i) {
        this.mMenuState = i;
        updateMovementBounds();
        onRegistrationChanged(i == 0);
        if (i == 0) {
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_HIDE_MENU);
        } else if (i == 1) {
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_SHOW_MENU);
        }
    }

    public final void animateToMaximizedState(Runnable runnable) {
        Rect rect = new Rect();
        int i = 0;
        Rect rect2 = new Rect(0, 0, this.mPipBoundsState.getMaxSize().x, this.mPipBoundsState.getMaxSize().y);
        PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
        Rect rect3 = this.mInsetBounds;
        if (this.mIsImeShowing) {
            i = this.mImeHeight;
        }
        pipBoundsAlgorithm.getMovementBounds(rect2, rect3, rect, i);
        this.mSavedSnapFraction = this.mMotionHelper.animateToExpandedState(rect2, this.mPipBoundsState.getMovementBounds(), rect, runnable);
    }

    public final void animateToNormalSize(Runnable runnable) {
        this.mPipResizeGestureHandler.setUserResizeBounds(this.mPipBoundsState.getBounds());
        Rect adjustNormalBoundsToFitMenu = this.mPipBoundsAlgorithm.adjustNormalBoundsToFitMenu(this.mPipBoundsState.getNormalBounds(), this.mMenuController.getEstimatedMinMenuSize());
        Rect rect = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(adjustNormalBoundsToFitMenu, this.mInsetBounds, rect, this.mIsImeShowing ? this.mImeHeight : 0);
        this.mSavedSnapFraction = this.mMotionHelper.animateToExpandedState(adjustNormalBoundsToFitMenu, this.mPipBoundsState.getMovementBounds(), rect, runnable);
    }

    public final void animateToUnexpandedState(Rect rect) {
        Rect rect2 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect, this.mInsetBounds, rect2, this.mIsImeShowing ? this.mImeHeight : 0);
        this.mMotionHelper.animateToUnexpandedState(rect, this.mSavedSnapFraction, rect2, this.mPipBoundsState.getMovementBounds(), false);
        this.mSavedSnapFraction = -1.0f;
    }

    public final void animateToUnStashedState() {
        int i;
        int i2;
        Rect bounds = this.mPipBoundsState.getBounds();
        boolean z = bounds.left < this.mPipBoundsState.getDisplayBounds().left;
        Rect rect = new Rect(0, bounds.top, 0, bounds.bottom);
        if (z) {
            i = this.mInsetBounds.left;
        } else {
            i = this.mInsetBounds.right - bounds.width();
        }
        rect.left = i;
        if (z) {
            i2 = this.mInsetBounds.left + bounds.width();
        } else {
            i2 = this.mInsetBounds.right;
        }
        rect.right = i2;
        this.mMotionHelper.animateToUnStashedBounds(rect);
    }

    public PipMotionHelper getMotionHelper() {
        return this.mMotionHelper;
    }

    @VisibleForTesting
    public PipResizeGestureHandler getPipResizeGestureHandler() {
        return this.mPipResizeGestureHandler;
    }

    @VisibleForTesting
    public void setPipResizeGestureHandler(PipResizeGestureHandler pipResizeGestureHandler) {
        this.mPipResizeGestureHandler = pipResizeGestureHandler;
    }

    @VisibleForTesting
    public void setPipMotionHelper(PipMotionHelper pipMotionHelper) {
        this.mMotionHelper = pipMotionHelper;
    }

    public Rect getUserResizeBounds() {
        return this.mPipResizeGestureHandler.getUserResizeBounds();
    }

    public class DefaultPipTouchGesture extends PipTouchGesture {
        public final PointF mDelta;
        public boolean mShouldHideMenuAfterFling;
        public final Point mStartPosition;

        public DefaultPipTouchGesture() {
            this.mStartPosition = new Point();
            this.mDelta = new PointF();
        }

        public void onDown(PipTouchState pipTouchState) {
            if (pipTouchState.isUserInteracting()) {
                Rect possiblyMotionBounds = PipTouchHandler.this.getPossiblyMotionBounds();
                this.mDelta.set(0.0f, 0.0f);
                this.mStartPosition.set(possiblyMotionBounds.left, possiblyMotionBounds.top);
                PipTouchHandler.this.mMovementWithinDismiss = pipTouchState.getDownTouchPosition().y >= ((float) PipTouchHandler.this.mPipBoundsState.getMovementBounds().bottom);
                PipTouchHandler.this.mMotionHelper.setSpringingToTouch(false);
                PipTouchHandler.this.mPipDismissTargetHandler.setTaskLeash(PipTouchHandler.this.mPipTaskOrganizer.getSurfaceControl());
                if (PipTouchHandler.this.mMenuState != 0 && !PipTouchHandler.this.mPipBoundsState.isStashed()) {
                    PipTouchHandler.this.mMenuController.pokeMenu();
                }
            }
        }

        public boolean onMove(PipTouchState pipTouchState) {
            boolean z = false;
            if (!pipTouchState.isUserInteracting()) {
                return false;
            }
            if (pipTouchState.startedDragging()) {
                PipTouchHandler.this.mSavedSnapFraction = -1.0f;
                PipTouchHandler.this.mPipDismissTargetHandler.showDismissTargetMaybe();
            }
            if (!pipTouchState.isDragging()) {
                return false;
            }
            PointF lastTouchDelta = pipTouchState.getLastTouchDelta();
            Point point = this.mStartPosition;
            PointF pointF = this.mDelta;
            float f = pointF.x;
            float f2 = ((float) point.x) + f;
            float f3 = pointF.y;
            float f4 = ((float) point.y) + f3;
            float f5 = lastTouchDelta.x + f2;
            float f6 = lastTouchDelta.y + f4;
            pointF.x = f + (f5 - f2);
            pointF.y = f3 + (f6 - f4);
            PipTouchHandler.this.mTmpBounds.set(PipTouchHandler.this.getPossiblyMotionBounds());
            PipTouchHandler.this.mTmpBounds.offsetTo((int) f5, (int) f6);
            PipTouchHandler.this.mMotionHelper.movePip(PipTouchHandler.this.mTmpBounds, true);
            PointF lastTouchPosition = pipTouchState.getLastTouchPosition();
            if (PipTouchHandler.this.mMovementWithinDismiss) {
                PipTouchHandler pipTouchHandler = PipTouchHandler.this;
                if (lastTouchPosition.y >= ((float) pipTouchHandler.mPipBoundsState.getMovementBounds().bottom)) {
                    z = true;
                }
                pipTouchHandler.mMovementWithinDismiss = z;
            }
            return true;
        }

        public boolean onUp(PipTouchState pipTouchState) {
            PipTouchHandler.this.mPipDismissTargetHandler.hideDismissTargetMaybe();
            PipTouchHandler.this.mPipDismissTargetHandler.setTaskLeash((SurfaceControl) null);
            if (!pipTouchState.isUserInteracting()) {
                return false;
            }
            PointF velocity = pipTouchState.getVelocity();
            if (pipTouchState.isDragging()) {
                if (PipTouchHandler.this.mMenuState != 0) {
                    PipTouchHandler.this.mMenuController.showMenu(PipTouchHandler.this.mMenuState, PipTouchHandler.this.mPipBoundsState.getBounds(), true, PipTouchHandler.this.willResizeMenu(), PipTouchHandler.this.shouldShowResizeHandle());
                }
                this.mShouldHideMenuAfterFling = PipTouchHandler.this.mMenuState == 0;
                PipTouchHandler.this.mTouchState.reset();
                if (!PipTouchHandler.this.mEnableStash || !shouldStash(velocity, PipTouchHandler.this.getPossiblyMotionBounds())) {
                    if (PipTouchHandler.this.mPipBoundsState.isStashed()) {
                        PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_UNSTASHED);
                        PipTouchHandler.this.mPipBoundsState.setStashed(0);
                    }
                    PipTouchHandler.this.mMotionHelper.flingToSnapTarget(velocity.x, velocity.y, new PipTouchHandler$DefaultPipTouchGesture$$ExternalSyntheticLambda1(this));
                } else {
                    PipTouchHandler.this.mMotionHelper.stashToEdge(velocity.x, velocity.y, new PipTouchHandler$DefaultPipTouchGesture$$ExternalSyntheticLambda0(this));
                }
            } else if (!PipTouchHandler.this.mTouchState.isDoubleTap() || PipTouchHandler.this.mPipBoundsState.isStashed() || PipTouchHandler.this.mMenuState == 1) {
                if (PipTouchHandler.this.mMenuState != 1) {
                    if (PipTouchHandler.this.mPipBoundsState.isStashed()) {
                        PipTouchHandler.this.animateToUnStashedState();
                        PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_UNSTASHED);
                        PipTouchHandler.this.mPipBoundsState.setStashed(0);
                        PipTouchHandler.this.mTouchState.removeDoubleTapTimeoutCallback();
                    } else if (!PipTouchHandler.this.mTouchState.isWaitingForDoubleTap()) {
                        PipTouchHandler.this.mMenuController.showMenu(1, PipTouchHandler.this.mPipBoundsState.getBounds(), true, PipTouchHandler.this.willResizeMenu(), PipTouchHandler.this.shouldShowResizeHandle());
                    } else {
                        PipTouchHandler.this.mTouchState.scheduleDoubleTapTimeoutCallback();
                    }
                }
            } else if (PipTouchHandler.this.mPipResizeGestureHandler.isUsingPinchToZoom()) {
                boolean z = PipTouchHandler.this.mPipBoundsState.getBounds().width() < PipTouchHandler.this.mPipBoundsState.getMaxSize().x && PipTouchHandler.this.mPipBoundsState.getBounds().height() < PipTouchHandler.this.mPipBoundsState.getMaxSize().y;
                if (PipTouchHandler.this.mMenuController.isMenuVisible()) {
                    PipTouchHandler.this.mMenuController.hideMenu(0, false);
                }
                if (z) {
                    PipTouchHandler.this.mPipResizeGestureHandler.setUserResizeBounds(PipTouchHandler.this.mPipBoundsState.getBounds());
                    PipTouchHandler.this.animateToMaximizedState((Runnable) null);
                } else {
                    PipTouchHandler pipTouchHandler = PipTouchHandler.this;
                    pipTouchHandler.animateToUnexpandedState(pipTouchHandler.getUserResizeBounds());
                }
            } else {
                PipTouchHandler.this.setTouchEnabled(false);
                PipTouchHandler.this.mMotionHelper.expandLeavePip(false);
            }
            return true;
        }

        public final void stashEndAction() {
            if (PipTouchHandler.this.mPipBoundsState.getBounds().left < 0 && PipTouchHandler.this.mPipBoundsState.getStashedState() != 1) {
                PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_LEFT);
                PipTouchHandler.this.mPipBoundsState.setStashed(1);
            } else if (PipTouchHandler.this.mPipBoundsState.getBounds().left >= 0 && PipTouchHandler.this.mPipBoundsState.getStashedState() != 2) {
                PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_RIGHT);
                PipTouchHandler.this.mPipBoundsState.setStashed(2);
            }
            PipTouchHandler.this.mMenuController.hideMenu();
        }

        public final void flingEndAction() {
            if (this.mShouldHideMenuAfterFling) {
                PipTouchHandler.this.mMenuController.hideMenu();
            }
        }

        public final boolean shouldStash(PointF pointF, Rect rect) {
            boolean z = pointF.x < (-PipTouchHandler.this.mStashVelocityThreshold);
            boolean z2 = pointF.x > PipTouchHandler.this.mStashVelocityThreshold;
            int width = rect.width() / 2;
            boolean z3 = rect.left < PipTouchHandler.this.mPipBoundsState.getDisplayBounds().left - width;
            boolean z4 = rect.right > PipTouchHandler.this.mPipBoundsState.getDisplayBounds().right + width;
            DisplayCutout displayCutout = PipTouchHandler.this.mPipBoundsState.getDisplayLayout().getDisplayCutout();
            if (displayCutout != null) {
                if ((z || z3) && !displayCutout.getBoundingRectLeft().isEmpty()) {
                    return false;
                }
                if ((z2 || z4) && !displayCutout.getBoundingRectRight().isEmpty()) {
                    return false;
                }
            }
            boolean z5 = (z && PipTouchHandler.this.mPipBoundsState.getStashedState() != 2) || (z2 && PipTouchHandler.this.mPipBoundsState.getStashedState() != 1);
            boolean z6 = z3 || z4;
            if (z5 || z6) {
                return true;
            }
            return false;
        }
    }

    public final void updateMovementBounds() {
        int i;
        boolean z = false;
        this.mPipBoundsAlgorithm.getMovementBounds(this.mPipBoundsState.getBounds(), this.mInsetBounds, this.mPipBoundsState.getMovementBounds(), this.mIsImeShowing ? this.mImeHeight : 0);
        this.mMotionHelper.onMovementBoundsChanged();
        if (this.mMenuState == 1) {
            z = true;
        }
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        if (!z || !willResizeMenu()) {
            i = this.mPipBoundsAlgorithm.getDefaultMinSize();
        } else {
            i = this.mExpandedShortestEdgeSize;
        }
        pipBoundsState.setMinEdgeSize(i);
    }

    public final Rect getMovementBounds(Rect rect) {
        Rect rect2 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect, this.mInsetBounds, rect2, this.mIsImeShowing ? this.mImeHeight : 0);
        return rect2;
    }

    public final boolean willResizeMenu() {
        if (!this.mEnableResize) {
            return false;
        }
        Size estimatedMinMenuSize = this.mMenuController.getEstimatedMinMenuSize();
        if (estimatedMinMenuSize == null) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.wtf(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -520008159, 0, (String) null, "PipTouchHandler");
            }
            return false;
        }
        Rect bounds = this.mPipBoundsState.getBounds();
        if (bounds.width() < estimatedMinMenuSize.getWidth() || bounds.height() < estimatedMinMenuSize.getHeight()) {
            return true;
        }
        return false;
    }

    public Rect getPossiblyMotionBounds() {
        if (this.mPipBoundsState.getMotionBoundsState().isInMotion()) {
            return this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion();
        }
        return this.mPipBoundsState.getBounds();
    }

    public void setOhmOffset(int i) {
        this.mPipResizeGestureHandler.setOhmOffset(i);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + "PipTouchHandler");
        printWriter.println(str2 + "mMenuState=" + this.mMenuState);
        printWriter.println(str2 + "mIsImeShowing=" + this.mIsImeShowing);
        printWriter.println(str2 + "mImeHeight=" + this.mImeHeight);
        printWriter.println(str2 + "mIsShelfShowing=" + this.mIsShelfShowing);
        printWriter.println(str2 + "mShelfHeight=" + this.mShelfHeight);
        printWriter.println(str2 + "mSavedSnapFraction=" + this.mSavedSnapFraction);
        printWriter.println(str2 + "mMovementBoundsExtraOffsets=" + this.mMovementBoundsExtraOffsets);
        this.mPipBoundsAlgorithm.dump(printWriter, str2);
        this.mTouchState.dump(printWriter, str2);
        PipResizeGestureHandler pipResizeGestureHandler = this.mPipResizeGestureHandler;
        if (pipResizeGestureHandler != null) {
            pipResizeGestureHandler.dump(printWriter, str2);
        }
    }
}
