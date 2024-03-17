package com.android.systemui.statusbar.phone;

import android.hardware.display.AmbientDisplayConfiguration;
import android.media.session.MediaSessionLegacyHelper;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.LockIconViewController;
import com.android.systemui.R$id;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.statusbar.DragDownHelper;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.tuner.TunerService;
import java.io.PrintWriter;
import java.util.Optional;

public class NotificationShadeWindowViewController {
    public final AmbientState mAmbientState;
    public View mBrightnessMirror;
    public final NotificationShadeDepthController mDepthController;
    public final DockManager mDockManager;
    public boolean mDoubleTapEnabled;
    public DragDownHelper mDragDownHelper;
    public boolean mExpandAnimationRunning;
    public boolean mExpandingBelowNotch;
    public final FalsingCollector mFalsingCollector;
    public GestureDetector mGestureDetector;
    public boolean mIsTrackingBarGesture = false;
    public final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    public final LockIconViewController mLockIconViewController;
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    public final Optional<LowLightClockController> mLowLightClockController;
    public final NotificationPanelViewController mNotificationPanelViewController;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public final NotificationStackScrollLayoutController mNotificationStackScrollLayoutController;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public final CentralSurfaces mService;
    public boolean mSingleTapEnabled;
    public NotificationStackScrollLayout mStackScrollLayout;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public PhoneStatusBarViewController mStatusBarViewController;
    public final StatusBarWindowStateController mStatusBarWindowStateController;
    public boolean mTouchActive;
    public boolean mTouchCancelled;
    public final TunerService mTunerService;
    public final NotificationShadeWindowView mView;

    public NotificationShadeWindowViewController(LockscreenShadeTransitionController lockscreenShadeTransitionController, FalsingCollector falsingCollector, TunerService tunerService, SysuiStatusBarStateController sysuiStatusBarStateController, DockManager dockManager, NotificationShadeDepthController notificationShadeDepthController, NotificationShadeWindowView notificationShadeWindowView, NotificationPanelViewController notificationPanelViewController, PanelExpansionStateManager panelExpansionStateManager, NotificationStackScrollLayoutController notificationStackScrollLayoutController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, StatusBarWindowStateController statusBarWindowStateController, LockIconViewController lockIconViewController, Optional<LowLightClockController> optional, CentralSurfaces centralSurfaces, NotificationShadeWindowController notificationShadeWindowController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, AmbientState ambientState) {
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mFalsingCollector = falsingCollector;
        this.mTunerService = tunerService;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mView = notificationShadeWindowView;
        this.mDockManager = dockManager;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mDepthController = notificationShadeDepthController;
        this.mNotificationStackScrollLayoutController = notificationStackScrollLayoutController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mStatusBarWindowStateController = statusBarWindowStateController;
        this.mLockIconViewController = lockIconViewController;
        this.mLowLightClockController = optional;
        this.mService = centralSurfaces;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mAmbientState = ambientState;
        this.mBrightnessMirror = notificationShadeWindowView.findViewById(R$id.brightness_mirror_container);
    }

    public ViewGroup getBouncerContainer() {
        return (ViewGroup) this.mView.findViewById(R$id.keyguard_bouncer_container);
    }

    public void setupExpandedStatusBar() {
        this.mStackScrollLayout = (NotificationStackScrollLayout) this.mView.findViewById(R$id.notification_stack_scroller);
        this.mTunerService.addTunable(new NotificationShadeWindowViewController$$ExternalSyntheticLambda2(this), "doze_pulse_on_double_tap", "doze_tap_gesture");
        this.mGestureDetector = new GestureDetector(this.mView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                if (!NotificationShadeWindowViewController.this.mSingleTapEnabled || NotificationShadeWindowViewController.this.mDockManager.isDocked()) {
                    return false;
                }
                NotificationShadeWindowViewController.this.mService.wakeUpIfDozing(SystemClock.uptimeMillis(), NotificationShadeWindowViewController.this.mView, "SINGLE_TAP");
                return true;
            }

            public boolean onDoubleTap(MotionEvent motionEvent) {
                if (!NotificationShadeWindowViewController.this.mDoubleTapEnabled && !NotificationShadeWindowViewController.this.mSingleTapEnabled) {
                    return false;
                }
                NotificationShadeWindowViewController.this.mService.wakeUpIfDozing(SystemClock.uptimeMillis(), NotificationShadeWindowViewController.this.mView, "DOUBLE_TAP");
                return true;
            }
        });
        this.mLowLightClockController.ifPresent(new NotificationShadeWindowViewController$$ExternalSyntheticLambda3(this));
        this.mView.setInteractionEventHandler(new NotificationShadeWindowView.InteractionEventHandler() {
            public Boolean handleDispatchTouchEvent(MotionEvent motionEvent) {
                if (NotificationShadeWindowViewController.this.mStatusBarViewController == null) {
                    Log.w("NotifShadeWindowVC", "Ignoring touch while statusBarView not yet set.");
                    return Boolean.FALSE;
                }
                boolean z = motionEvent.getActionMasked() == 0;
                boolean z2 = motionEvent.getActionMasked() == 1;
                boolean z3 = motionEvent.getActionMasked() == 3;
                boolean r6 = NotificationShadeWindowViewController.this.mExpandingBelowNotch;
                if (z2 || z3) {
                    NotificationShadeWindowViewController.this.mExpandingBelowNotch = false;
                }
                if (!z3 && NotificationShadeWindowViewController.this.mService.shouldIgnoreTouch()) {
                    return Boolean.FALSE;
                }
                if (z) {
                    NotificationShadeWindowViewController.this.mTouchActive = true;
                    NotificationShadeWindowViewController.this.mTouchCancelled = false;
                } else if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3) {
                    NotificationShadeWindowViewController.this.mTouchActive = false;
                }
                if (NotificationShadeWindowViewController.this.mTouchCancelled || NotificationShadeWindowViewController.this.mExpandAnimationRunning) {
                    return Boolean.FALSE;
                }
                if (NotificationShadeWindowViewController.this.mKeyguardUnlockAnimationController.isPlayingCannedUnlockAnimation()) {
                    NotificationShadeWindowViewController.this.cancelCurrentTouch();
                    return Boolean.TRUE;
                }
                NotificationShadeWindowViewController.this.mFalsingCollector.onTouchEvent(motionEvent);
                NotificationShadeWindowViewController.this.mGestureDetector.onTouchEvent(motionEvent);
                NotificationShadeWindowViewController.this.mStatusBarKeyguardViewManager.onTouch(motionEvent);
                if (NotificationShadeWindowViewController.this.mBrightnessMirror != null && NotificationShadeWindowViewController.this.mBrightnessMirror.getVisibility() == 0 && motionEvent.getActionMasked() == 5) {
                    return Boolean.FALSE;
                }
                if (z) {
                    NotificationShadeWindowViewController.this.mNotificationStackScrollLayoutController.closeControlsIfOutsideTouch(motionEvent);
                }
                if (NotificationShadeWindowViewController.this.mStatusBarStateController.isDozing()) {
                    NotificationShadeWindowViewController.this.mService.extendDozePulse();
                }
                NotificationShadeWindowViewController.this.mLockIconViewController.onTouchEvent(motionEvent, new NotificationShadeWindowViewController$2$$ExternalSyntheticLambda0(this));
                if (z && motionEvent.getY() >= ((float) NotificationShadeWindowViewController.this.mView.getBottom())) {
                    NotificationShadeWindowViewController.this.mExpandingBelowNotch = true;
                    r6 = true;
                }
                if (r6) {
                    return Boolean.valueOf(NotificationShadeWindowViewController.this.mStatusBarViewController.sendTouchToView(motionEvent));
                }
                if (!NotificationShadeWindowViewController.this.mIsTrackingBarGesture && z && NotificationShadeWindowViewController.this.mNotificationPanelViewController.isFullyCollapsed()) {
                    if (!NotificationShadeWindowViewController.this.mStatusBarViewController.touchIsWithinView(motionEvent.getRawX(), motionEvent.getRawY())) {
                        return null;
                    }
                    if (!NotificationShadeWindowViewController.this.mStatusBarWindowStateController.windowIsShowing()) {
                        return Boolean.TRUE;
                    }
                    NotificationShadeWindowViewController.this.mIsTrackingBarGesture = true;
                    return Boolean.valueOf(NotificationShadeWindowViewController.this.mStatusBarViewController.sendTouchToView(motionEvent));
                } else if (!NotificationShadeWindowViewController.this.mIsTrackingBarGesture) {
                    return null;
                } else {
                    boolean sendTouchToView = NotificationShadeWindowViewController.this.mStatusBarViewController.sendTouchToView(motionEvent);
                    if (z2 || z3) {
                        NotificationShadeWindowViewController.this.mIsTrackingBarGesture = false;
                    }
                    return Boolean.valueOf(sendTouchToView);
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$handleDispatchTouchEvent$0() {
                NotificationShadeWindowViewController.this.mService.wakeUpIfDozing(SystemClock.uptimeMillis(), NotificationShadeWindowViewController.this.mView, "LOCK_ICON_TOUCH");
            }

            public void dispatchTouchEventComplete() {
                NotificationShadeWindowViewController.this.mFalsingCollector.onMotionEventComplete();
            }

            public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
                if ((NotificationShadeWindowViewController.this.mStatusBarStateController.isDozing() && !NotificationShadeWindowViewController.this.mService.isPulsing() && !NotificationShadeWindowViewController.this.mDockManager.isDocked()) || NotificationShadeWindowViewController.this.mStatusBarKeyguardViewManager.isShowingAlternateAuthOrAnimating() || NotificationShadeWindowViewController.this.mLockIconViewController.onInterceptTouchEvent(motionEvent)) {
                    return true;
                }
                if (!NotificationShadeWindowViewController.this.mNotificationPanelViewController.isFullyExpanded() || !NotificationShadeWindowViewController.this.mDragDownHelper.isDragDownEnabled() || NotificationShadeWindowViewController.this.mService.isBouncerShowing() || NotificationShadeWindowViewController.this.mStatusBarStateController.isDozing()) {
                    return false;
                }
                return NotificationShadeWindowViewController.this.mDragDownHelper.onInterceptTouchEvent(motionEvent);
            }

            public void didIntercept(MotionEvent motionEvent) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setAction(3);
                NotificationShadeWindowViewController.this.mStackScrollLayout.onInterceptTouchEvent(obtain);
                NotificationShadeWindowViewController.this.mNotificationPanelViewController.getView().onInterceptTouchEvent(obtain);
                obtain.recycle();
            }

            public boolean handleTouchEvent(MotionEvent motionEvent) {
                boolean z = true;
                boolean z2 = NotificationShadeWindowViewController.this.mStatusBarStateController.isDozing() ? !NotificationShadeWindowViewController.this.mService.isPulsing() : false;
                if (!NotificationShadeWindowViewController.this.mStatusBarKeyguardViewManager.isShowingAlternateAuthOrAnimating()) {
                    z = z2;
                }
                return ((!NotificationShadeWindowViewController.this.mDragDownHelper.isDragDownEnabled() || z) && !NotificationShadeWindowViewController.this.mDragDownHelper.isDraggingDown()) ? z : NotificationShadeWindowViewController.this.mDragDownHelper.onTouchEvent(motionEvent);
            }

            public void didNotHandleTouchEvent(MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 1 || actionMasked == 3) {
                    NotificationShadeWindowViewController.this.mService.setInteracting(1, false);
                }
            }

            public boolean interceptMediaKey(KeyEvent keyEvent) {
                return NotificationShadeWindowViewController.this.mService.interceptMediaKey(keyEvent);
            }

            public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
                return NotificationShadeWindowViewController.this.mService.dispatchKeyEventPreIme(keyEvent);
            }

            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                boolean z = keyEvent.getAction() == 0;
                int keyCode = keyEvent.getKeyCode();
                if (keyCode != 4) {
                    if (keyCode != 62) {
                        if (keyCode != 82) {
                            if ((keyCode == 24 || keyCode == 25) && NotificationShadeWindowViewController.this.mStatusBarStateController.isDozing()) {
                                MediaSessionLegacyHelper.getHelper(NotificationShadeWindowViewController.this.mView.getContext()).sendVolumeKeyEvent(keyEvent, Integer.MIN_VALUE, true);
                                return true;
                            }
                        } else if (!z) {
                            return NotificationShadeWindowViewController.this.mService.onMenuPressed();
                        }
                    } else if (!z) {
                        return NotificationShadeWindowViewController.this.mService.onSpacePressed();
                    }
                    return false;
                }
                if (!z) {
                    NotificationShadeWindowViewController.this.mService.onBackPressed();
                }
                return true;
            }
        });
        this.mView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            public void onChildViewRemoved(View view, View view2) {
            }

            public void onChildViewAdded(View view, View view2) {
                if (view2.getId() == R$id.brightness_mirror_container) {
                    NotificationShadeWindowViewController.this.mBrightnessMirror = view2;
                }
            }
        });
        setDragDownHelper(this.mLockscreenShadeTransitionController.getTouchHelper());
        this.mDepthController.setRoot(this.mView);
        this.mPanelExpansionStateManager.addExpansionListener(this.mDepthController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupExpandedStatusBar$0(String str, String str2) {
        AmbientDisplayConfiguration ambientDisplayConfiguration = new AmbientDisplayConfiguration(this.mView.getContext());
        str.hashCode();
        if (str.equals("doze_tap_gesture")) {
            this.mSingleTapEnabled = ambientDisplayConfiguration.tapGestureEnabled(-2);
        } else if (str.equals("doze_pulse_on_double_tap")) {
            this.mDoubleTapEnabled = ambientDisplayConfiguration.doubleTapGestureEnabled(-2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupExpandedStatusBar$1(LowLightClockController lowLightClockController) {
        lowLightClockController.attachLowLightClockView(this.mView);
    }

    public void cancelCurrentTouch() {
        if (this.mTouchActive) {
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            obtain.setSource(4098);
            this.mView.dispatchTouchEvent(obtain);
            obtain.recycle();
            this.mTouchCancelled = true;
        }
        this.mAmbientState.setSwipingUp(false);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("  mExpandAnimationRunning=");
        printWriter.println(this.mExpandAnimationRunning);
        printWriter.print("  mTouchCancelled=");
        printWriter.println(this.mTouchCancelled);
        printWriter.print("  mTouchActive=");
        printWriter.println(this.mTouchActive);
    }

    public void setExpandAnimationRunning(boolean z) {
        if (this.mExpandAnimationRunning != z) {
            this.mExpandAnimationRunning = z;
            this.mNotificationShadeWindowController.setLaunchingActivity(z);
        }
    }

    public void cancelExpandHelper() {
        NotificationStackScrollLayout notificationStackScrollLayout = this.mStackScrollLayout;
        if (notificationStackScrollLayout != null) {
            notificationStackScrollLayout.cancelExpandHelper();
        }
    }

    public void setStatusBarViewController(PhoneStatusBarViewController phoneStatusBarViewController) {
        this.mStatusBarViewController = phoneStatusBarViewController;
    }

    public void setDozing(boolean z) {
        this.mLowLightClockController.ifPresent(new NotificationShadeWindowViewController$$ExternalSyntheticLambda1(z));
    }

    public void dozeTimeTick() {
        this.mLowLightClockController.ifPresent(new NotificationShadeWindowViewController$$ExternalSyntheticLambda0());
    }

    @VisibleForTesting
    public void setDragDownHelper(DragDownHelper dragDownHelper) {
        this.mDragDownHelper = dragDownHelper;
    }
}
