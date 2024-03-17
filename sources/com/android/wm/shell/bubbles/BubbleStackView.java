package com.android.wm.shell.bubbles;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.wm.shell.R;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BadgedImageView;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.bubbles.animation.AnimatableScaleMatrix;
import com.android.wm.shell.bubbles.animation.ExpandedAnimationController;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.bubbles.animation.StackAnimationController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BubbleStackView extends FrameLayout implements ViewTreeObserver.OnComputeInternalInsetsListener {
    public static final SurfaceSynchronizer DEFAULT_SURFACE_SYNCHRONIZER = new SurfaceSynchronizer() {
        public void syncSurfaceAndRun(final Runnable runnable) {
            Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
                public int mFrameWait = 2;

                public void doFrame(long j) {
                    int i = this.mFrameWait - 1;
                    this.mFrameWait = i;
                    if (i > 0) {
                        Choreographer.getInstance().postFrameCallback(this);
                    } else {
                        runnable.run();
                    }
                }
            });
        }
    };
    @VisibleForTesting
    public static final int FLYOUT_HIDE_AFTER = 5000;
    public static final PhysicsAnimator.SpringConfig FLYOUT_IME_ANIMATION_SPRING_CONFIG = new PhysicsAnimator.SpringConfig(200.0f, 0.9f);
    public Runnable mAfterFlyoutHidden;
    public final DynamicAnimation.OnAnimationEndListener mAfterFlyoutTransitionSpring;
    public Runnable mAnimateInFlyout;
    public final Runnable mAnimateTemporarilyInvisibleImmediate;
    public SurfaceControl.ScreenshotHardwareBuffer mAnimatingOutBubbleBuffer;
    public final ValueAnimator mAnimatingOutSurfaceAlphaAnimator;
    public FrameLayout mAnimatingOutSurfaceContainer;
    public boolean mAnimatingOutSurfaceReady;
    public SurfaceView mAnimatingOutSurfaceView;
    public View.OnClickListener mBubbleClickListener;
    public PhysicsAnimationLayout mBubbleContainer;
    public final BubbleController mBubbleController;
    public final BubbleData mBubbleData;
    public int mBubbleElevation;
    public BubbleOverflow mBubbleOverflow;
    public int mBubbleSize;
    public BubbleViewProvider mBubbleToExpandAfterFlyoutCollapse;
    public RelativeTouchListener mBubbleTouchListener;
    public int mBubbleTouchPadding;
    public int mCornerRadius;
    public Runnable mDelayedAnimation;
    public final ShellExecutor mDelayedAnimationExecutor;
    public final ValueAnimator mDismissBubbleAnimator;
    public DismissView mDismissView;
    public Bubbles.BubbleExpandListener mExpandListener;
    public ExpandedAnimationController mExpandedAnimationController;
    public BubbleViewProvider mExpandedBubble;
    public final ValueAnimator mExpandedViewAlphaAnimator;
    public FrameLayout mExpandedViewContainer;
    public final AnimatableScaleMatrix mExpandedViewContainerMatrix = new AnimatableScaleMatrix();
    public int mExpandedViewPadding;
    public boolean mExpandedViewTemporarilyHidden;
    public BubbleFlyoutView mFlyout;
    public View.OnClickListener mFlyoutClickListener;
    public final FloatPropertyCompat mFlyoutCollapseProperty;
    public float mFlyoutDragDeltaX;
    public RelativeTouchListener mFlyoutTouchListener;
    public final SpringAnimation mFlyoutTransitionSpring;
    public Runnable mHideFlyout;
    public final MagnetizedObject.MagnetListener mIndividualBubbleMagnetListener;
    public boolean mIsBubbleSwitchAnimating;
    public boolean mIsDraggingStack;
    public boolean mIsExpanded;
    public boolean mIsExpansionAnimating;
    public boolean mIsGestureInProgress;
    public MagnetizedObject.MagneticTarget mMagneticTarget;
    public MagnetizedObject<?> mMagnetizedObject;
    public ManageEducationView mManageEduView;
    public ViewGroup mManageMenu;
    public View mManageMenuScrim;
    public ImageView mManageSettingsIcon;
    public TextView mManageSettingsText;
    public PhysicsAnimator.SpringConfig mManageSpringConfig;
    public View.OnLayoutChangeListener mOrientationChangedListener;
    public int mPointerIndexDown;
    public BubblePositioner mPositioner;
    public RelativeStackPosition mRelativeStackPositionBeforeRotation;
    public final PhysicsAnimator.SpringConfig mScaleInSpringConfig = new PhysicsAnimator.SpringConfig(300.0f, 0.9f);
    public final PhysicsAnimator.SpringConfig mScaleOutSpringConfig = new PhysicsAnimator.SpringConfig(900.0f, 1.0f);
    public View mScrim;
    public boolean mShowedUserEducationInTouchListenerActive;
    public boolean mShowingManage;
    public StackAnimationController mStackAnimationController;
    public StackEducationView mStackEduView;
    public final MagnetizedObject.MagnetListener mStackMagnetListener;
    public boolean mStackOnLeftOrWillBe;
    public StackViewState mStackViewState = new StackViewState();
    public final SurfaceSynchronizer mSurfaceSynchronizer;
    public ViewTreeObserver.OnDrawListener mSystemGestureExcludeUpdater;
    public final List<Rect> mSystemGestureExclusionRects;
    public Rect mTempRect;
    public boolean mTemporarilyInvisible;
    public final PhysicsAnimator.SpringConfig mTranslateSpringConfig = new PhysicsAnimator.SpringConfig(50.0f, 1.0f);
    public Consumer<String> mUnbubbleConversationCallback;
    public View mViewBeingDismissed;
    public boolean mViewUpdatedRequested;
    public ViewTreeObserver.OnPreDrawListener mViewUpdater;

    public static class StackViewState {
        public int numberOfBubbles;
        public boolean onLeft;
        public int selectedIndex;
    }

    public interface SurfaceSynchronizer {
        void syncSurfaceAndRun(Runnable runnable);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        animateFlyoutCollapsed(true, 0.0f);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("Stack view state:");
        String formatBubblesString = BubbleDebugConfig.formatBubblesString(getBubblesOnScreen(), getExpandedBubble());
        printWriter.print("  bubbles on screen:       ");
        printWriter.println(formatBubblesString);
        printWriter.print("  gestureInProgress:       ");
        printWriter.println(this.mIsGestureInProgress);
        printWriter.print("  showingDismiss:          ");
        printWriter.println(this.mDismissView.isShowing());
        printWriter.print("  isExpansionAnimating:    ");
        printWriter.println(this.mIsExpansionAnimating);
        printWriter.print("  expandedContainerVis:    ");
        printWriter.println(this.mExpandedViewContainer.getVisibility());
        printWriter.print("  expandedContainerAlpha:  ");
        printWriter.println(this.mExpandedViewContainer.getAlpha());
        printWriter.print("  expandedContainerMatrix: ");
        printWriter.println(this.mExpandedViewContainer.getAnimationMatrix());
        this.mStackAnimationController.dump(printWriter, strArr);
        this.mExpandedAnimationController.dump(printWriter, strArr);
        if (this.mExpandedBubble != null) {
            printWriter.println("Expanded bubble state:");
            printWriter.println("  expandedBubbleKey: " + this.mExpandedBubble.getKey());
            BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
            if (expandedView != null) {
                printWriter.println("  expandedViewVis:    " + expandedView.getVisibility());
                printWriter.println("  expandedViewAlpha:  " + expandedView.getAlpha());
                printWriter.println("  expandedViewTaskId: " + expandedView.getTaskId());
                TaskView taskView = expandedView.getTaskView();
                if (taskView != null) {
                    printWriter.println("  activityViewVis:    " + taskView.getVisibility());
                    printWriter.println("  activityViewAlpha:  " + taskView.getAlpha());
                    return;
                }
                printWriter.println("  activityView is null");
                return;
            }
            printWriter.println("Expanded bubble view state: expanded bubble view is null");
            return;
        }
        printWriter.println("Expanded bubble state: expanded bubble is null");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        if (this.mFlyoutDragDeltaX == 0.0f) {
            this.mFlyout.postDelayed(this.mHideFlyout, 5000);
        } else {
            this.mFlyout.hideFlyout();
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @SuppressLint({"ClickableViewAccessibility"})
    public BubbleStackView(Context context, BubbleController bubbleController, BubbleData bubbleData, SurfaceSynchronizer surfaceSynchronizer, FloatingContentCoordinator floatingContentCoordinator, ShellExecutor shellExecutor) {
        super(context);
        SurfaceSynchronizer surfaceSynchronizer2;
        Context context2 = context;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimatingOutSurfaceAlphaAnimator = ofFloat;
        this.mHideFlyout = new BubbleStackView$$ExternalSyntheticLambda7(this);
        this.mBubbleToExpandAfterFlyoutCollapse = null;
        this.mStackOnLeftOrWillBe = true;
        this.mIsGestureInProgress = false;
        this.mTemporarilyInvisible = false;
        this.mIsDraggingStack = false;
        this.mExpandedViewTemporarilyHidden = false;
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mExpandedViewAlphaAnimator = ofFloat2;
        this.mPointerIndexDown = -1;
        this.mViewUpdatedRequested = false;
        this.mIsExpansionAnimating = false;
        this.mIsBubbleSwitchAnimating = false;
        this.mTempRect = new Rect();
        this.mSystemGestureExclusionRects = Collections.singletonList(new Rect());
        this.mViewUpdater = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                BubbleStackView.this.getViewTreeObserver().removeOnPreDrawListener(BubbleStackView.this.mViewUpdater);
                BubbleStackView.this.updateExpandedView();
                BubbleStackView.this.mViewUpdatedRequested = false;
                return true;
            }
        };
        this.mSystemGestureExcludeUpdater = new BubbleStackView$$ExternalSyntheticLambda11(this);
        AnonymousClass3 r8 = new FloatPropertyCompat("FlyoutCollapseSpring") {
            public float getValue(Object obj) {
                return BubbleStackView.this.mFlyoutDragDeltaX;
            }

            public void setValue(Object obj, float f) {
                BubbleStackView.this.setFlyoutStateForDragLength(f);
            }
        };
        this.mFlyoutCollapseProperty = r8;
        SpringAnimation springAnimation = new SpringAnimation(this, r8);
        this.mFlyoutTransitionSpring = springAnimation;
        this.mFlyoutDragDeltaX = 0.0f;
        BubbleStackView$$ExternalSyntheticLambda12 bubbleStackView$$ExternalSyntheticLambda12 = new BubbleStackView$$ExternalSyntheticLambda12(this);
        this.mAfterFlyoutTransitionSpring = bubbleStackView$$ExternalSyntheticLambda12;
        this.mIndividualBubbleMagnetListener = new MagnetizedObject.MagnetListener() {
            public void onStuckToTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                if (BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble() != null) {
                    BubbleStackView bubbleStackView = BubbleStackView.this;
                    bubbleStackView.animateDismissBubble(bubbleStackView.mExpandedAnimationController.getDraggedOutBubble(), true);
                }
            }

            public void onUnstuckFromTarget(MagnetizedObject.MagneticTarget magneticTarget, float f, float f2, boolean z) {
                if (BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble() != null) {
                    BubbleStackView bubbleStackView = BubbleStackView.this;
                    bubbleStackView.animateDismissBubble(bubbleStackView.mExpandedAnimationController.getDraggedOutBubble(), false);
                    if (z) {
                        BubbleStackView.this.mExpandedAnimationController.snapBubbleBack(BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble(), f, f2);
                        BubbleStackView.this.mDismissView.hide();
                        return;
                    }
                    BubbleStackView.this.mExpandedAnimationController.onUnstuckFromTarget();
                }
            }

            public void onReleasedInTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                if (BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble() != null) {
                    BubbleStackView.this.mExpandedAnimationController.dismissDraggedOutBubble(BubbleStackView.this.mExpandedAnimationController.getDraggedOutBubble(), (float) BubbleStackView.this.mDismissView.getHeight(), new BubbleStackView$4$$ExternalSyntheticLambda0(BubbleStackView.this));
                    BubbleStackView.this.mDismissView.hide();
                }
            }
        };
        this.mStackMagnetListener = new MagnetizedObject.MagnetListener() {
            public void onStuckToTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                BubbleStackView bubbleStackView = BubbleStackView.this;
                bubbleStackView.animateDismissBubble(bubbleStackView.mBubbleContainer, true);
            }

            public void onUnstuckFromTarget(MagnetizedObject.MagneticTarget magneticTarget, float f, float f2, boolean z) {
                BubbleStackView bubbleStackView = BubbleStackView.this;
                bubbleStackView.animateDismissBubble(bubbleStackView.mBubbleContainer, false);
                if (z) {
                    BubbleStackView.this.mStackAnimationController.flingStackThenSpringToEdge(BubbleStackView.this.mStackAnimationController.getStackPosition().x, f, f2);
                    BubbleStackView.this.mDismissView.hide();
                    return;
                }
                BubbleStackView.this.mStackAnimationController.onUnstuckFromTarget();
            }

            public void onReleasedInTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                BubbleStackView.this.mStackAnimationController.animateStackDismissal((float) BubbleStackView.this.mDismissView.getHeight(), new BubbleStackView$5$$ExternalSyntheticLambda0(this));
                BubbleStackView.this.mDismissView.hide();
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReleasedInTarget$0() {
                BubbleStackView.this.resetDismissAnimator();
                BubbleStackView.this.dismissMagnetizedObject();
            }
        };
        this.mBubbleClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                Bubble bubbleWithView;
                BubbleStackView.this.mIsDraggingStack = false;
                if (!BubbleStackView.this.mIsExpansionAnimating && !BubbleStackView.this.mIsBubbleSwitchAnimating && (bubbleWithView = BubbleStackView.this.mBubbleData.getBubbleWithView(view)) != null) {
                    boolean equals = bubbleWithView.getKey().equals(BubbleStackView.this.mExpandedBubble.getKey());
                    if (BubbleStackView.this.isExpanded()) {
                        BubbleStackView.this.mExpandedAnimationController.onGestureFinished();
                    }
                    if (!BubbleStackView.this.isExpanded() || equals) {
                        if (!BubbleStackView.this.maybeShowStackEdu() && !BubbleStackView.this.mShowedUserEducationInTouchListenerActive) {
                            BubbleStackView.this.mBubbleData.setExpanded(!BubbleStackView.this.mBubbleData.isExpanded());
                        }
                        BubbleStackView.this.mShowedUserEducationInTouchListenerActive = false;
                    } else if (bubbleWithView != BubbleStackView.this.mBubbleData.getSelectedBubble()) {
                        BubbleStackView.this.mBubbleData.setSelectedBubble(bubbleWithView);
                    } else {
                        BubbleStackView.this.setSelectedBubble(bubbleWithView);
                    }
                }
            }
        };
        this.mBubbleTouchListener = new RelativeTouchListener() {
            public boolean onDown(View view, MotionEvent motionEvent) {
                if (BubbleStackView.this.mIsExpansionAnimating) {
                    return true;
                }
                BubbleStackView.this.mShowedUserEducationInTouchListenerActive = false;
                if (BubbleStackView.this.maybeShowStackEdu()) {
                    BubbleStackView.this.mShowedUserEducationInTouchListenerActive = true;
                    return true;
                }
                if (BubbleStackView.this.isStackEduShowing()) {
                    BubbleStackView.this.mStackEduView.hide(false);
                }
                if (BubbleStackView.this.mShowingManage) {
                    BubbleStackView.this.showManageMenu(false);
                }
                if (BubbleStackView.this.mBubbleData.isExpanded()) {
                    if (BubbleStackView.this.mManageEduView != null) {
                        BubbleStackView.this.mManageEduView.hide();
                    }
                    BubbleStackView.this.mExpandedAnimationController.prepareForBubbleDrag(view, BubbleStackView.this.mMagneticTarget, BubbleStackView.this.mIndividualBubbleMagnetListener);
                    BubbleStackView.this.hideCurrentInputMethod();
                    BubbleStackView bubbleStackView = BubbleStackView.this;
                    bubbleStackView.mMagnetizedObject = bubbleStackView.mExpandedAnimationController.getMagnetizedBubbleDraggingOut();
                } else {
                    BubbleStackView.this.mStackAnimationController.cancelStackPositionAnimations();
                    BubbleStackView.this.mBubbleContainer.setActiveController(BubbleStackView.this.mStackAnimationController);
                    BubbleStackView.this.hideFlyoutImmediate();
                    if (BubbleStackView.this.mPositioner.showingInTaskbar()) {
                        BubbleStackView.this.mMagnetizedObject = null;
                    } else {
                        BubbleStackView bubbleStackView2 = BubbleStackView.this;
                        bubbleStackView2.mMagnetizedObject = bubbleStackView2.mStackAnimationController.getMagnetizedStack();
                        BubbleStackView.this.mMagnetizedObject.clearAllTargets();
                        BubbleStackView.this.mMagnetizedObject.addTarget(BubbleStackView.this.mMagneticTarget);
                        BubbleStackView.this.mMagnetizedObject.setMagnetListener(BubbleStackView.this.mStackMagnetListener);
                    }
                    BubbleStackView.this.mIsDraggingStack = true;
                    BubbleStackView.this.updateTemporarilyInvisibleAnimation(false);
                }
                boolean unused = BubbleStackView.this.passEventToMagnetizedObject(motionEvent);
                return true;
            }

            public void onMove(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4) {
                if (BubbleStackView.this.mIsExpansionAnimating) {
                    return;
                }
                if ((!BubbleStackView.this.mPositioner.showingInTaskbar() || BubbleStackView.this.mIsExpanded) && !BubbleStackView.this.mShowedUserEducationInTouchListenerActive) {
                    BubbleStackView.this.mDismissView.show();
                    if (BubbleStackView.this.mIsExpanded && BubbleStackView.this.mExpandedBubble != null && view.equals(BubbleStackView.this.mExpandedBubble.getIconView())) {
                        BubbleStackView.this.hideExpandedViewIfNeeded();
                    }
                    if (!BubbleStackView.this.passEventToMagnetizedObject(motionEvent)) {
                        BubbleStackView.this.updateBubbleShadows(true);
                        if (BubbleStackView.this.mBubbleData.isExpanded() || BubbleStackView.this.mPositioner.showingInTaskbar()) {
                            BubbleStackView.this.mExpandedAnimationController.dragBubbleOut(view, f + f3, f2 + f4);
                            return;
                        }
                        if (BubbleStackView.this.isStackEduShowing()) {
                            BubbleStackView.this.mStackEduView.hide(false);
                        }
                        BubbleStackView.this.mStackAnimationController.moveStackFromTouch(f + f3, f2 + f4);
                    }
                }
            }

            public void onUp(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4, float f5, float f6) {
                if (BubbleStackView.this.mIsExpansionAnimating) {
                    return;
                }
                if (BubbleStackView.this.mPositioner.showingInTaskbar() && !BubbleStackView.this.mIsExpanded) {
                    return;
                }
                if (BubbleStackView.this.mShowedUserEducationInTouchListenerActive) {
                    BubbleStackView.this.mShowedUserEducationInTouchListenerActive = false;
                    return;
                }
                if (!BubbleStackView.this.passEventToMagnetizedObject(motionEvent)) {
                    if (BubbleStackView.this.mBubbleData.isExpanded()) {
                        BubbleStackView.this.mExpandedAnimationController.snapBubbleBack(view, f5, f6);
                        BubbleStackView.this.showExpandedViewIfNeeded();
                    } else {
                        boolean r1 = BubbleStackView.this.mStackOnLeftOrWillBe;
                        BubbleStackView bubbleStackView = BubbleStackView.this;
                        boolean z = true;
                        bubbleStackView.mStackOnLeftOrWillBe = bubbleStackView.mStackAnimationController.flingStackThenSpringToEdge(f + f3, f5, f6) <= 0.0f;
                        if (r1 == BubbleStackView.this.mStackOnLeftOrWillBe) {
                            z = false;
                        }
                        BubbleStackView.this.updateBadges(z);
                        BubbleStackView.this.logBubbleEvent((BubbleViewProvider) null, 7);
                    }
                    BubbleStackView.this.mDismissView.hide();
                }
                BubbleStackView.this.mIsDraggingStack = false;
                BubbleStackView.this.updateTemporarilyInvisibleAnimation(false);
            }
        };
        this.mFlyoutClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (BubbleStackView.this.maybeShowStackEdu()) {
                    BubbleStackView.this.mBubbleToExpandAfterFlyoutCollapse = null;
                } else {
                    BubbleStackView bubbleStackView = BubbleStackView.this;
                    bubbleStackView.mBubbleToExpandAfterFlyoutCollapse = bubbleStackView.mBubbleData.getSelectedBubble();
                }
                BubbleStackView.this.mFlyout.removeCallbacks(BubbleStackView.this.mHideFlyout);
                BubbleStackView.this.mHideFlyout.run();
            }
        };
        this.mFlyoutTouchListener = new RelativeTouchListener() {
            public boolean onDown(View view, MotionEvent motionEvent) {
                BubbleStackView.this.mFlyout.removeCallbacks(BubbleStackView.this.mHideFlyout);
                return true;
            }

            public void onMove(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4) {
                BubbleStackView.this.setFlyoutStateForDragLength(f3);
            }

            public void onUp(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4, float f5, float f6) {
                boolean isStackOnLeftSide = BubbleStackView.this.mStackAnimationController.isStackOnLeftSide();
                boolean z = true;
                boolean z2 = !isStackOnLeftSide ? f5 > 2000.0f : f5 < -2000.0f;
                boolean z3 = !isStackOnLeftSide ? f3 > ((float) BubbleStackView.this.mFlyout.getWidth()) * 0.25f : f3 < ((float) (-BubbleStackView.this.mFlyout.getWidth())) * 0.25f;
                boolean z4 = !isStackOnLeftSide ? f5 < 0.0f : f5 > 0.0f;
                if (!z2 && (!z3 || z4)) {
                    z = false;
                }
                BubbleStackView.this.mFlyout.removeCallbacks(BubbleStackView.this.mHideFlyout);
                BubbleStackView.this.animateFlyoutCollapsed(z, f5);
                boolean unused = BubbleStackView.this.maybeShowStackEdu();
            }
        };
        this.mShowingManage = false;
        this.mShowedUserEducationInTouchListenerActive = false;
        this.mManageSpringConfig = new PhysicsAnimator.SpringConfig(1500.0f, 0.75f);
        this.mAnimateTemporarilyInvisibleImmediate = new BubbleStackView$$ExternalSyntheticLambda13(this);
        this.mDelayedAnimationExecutor = shellExecutor;
        this.mBubbleController = bubbleController;
        this.mBubbleData = bubbleData;
        Resources resources = getResources();
        this.mBubbleSize = resources.getDimensionPixelSize(R.dimen.bubble_size);
        int i = R.dimen.bubble_elevation;
        this.mBubbleElevation = resources.getDimensionPixelSize(i);
        this.mBubbleTouchPadding = resources.getDimensionPixelSize(R.dimen.bubble_touch_padding);
        this.mExpandedViewPadding = resources.getDimensionPixelSize(R.dimen.bubble_expanded_view_padding);
        int dimensionPixelSize = resources.getDimensionPixelSize(i);
        this.mPositioner = bubbleController.getPositioner();
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16844145});
        this.mCornerRadius = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        obtainStyledAttributes.recycle();
        BubbleStackView$$ExternalSyntheticLambda14 bubbleStackView$$ExternalSyntheticLambda14 = new BubbleStackView$$ExternalSyntheticLambda14(this);
        this.mStackAnimationController = new StackAnimationController(floatingContentCoordinator, new BubbleStackView$$ExternalSyntheticLambda15(this), bubbleStackView$$ExternalSyntheticLambda14, new BubbleStackView$$ExternalSyntheticLambda16(this), this.mPositioner);
        this.mExpandedAnimationController = new ExpandedAnimationController(this.mPositioner, bubbleStackView$$ExternalSyntheticLambda14, this);
        if (surfaceSynchronizer != null) {
            surfaceSynchronizer2 = surfaceSynchronizer;
        } else {
            surfaceSynchronizer2 = DEFAULT_SURFACE_SYNCHRONIZER;
        }
        this.mSurfaceSynchronizer = surfaceSynchronizer2;
        setLayoutDirection(0);
        PhysicsAnimationLayout physicsAnimationLayout = new PhysicsAnimationLayout(context2);
        this.mBubbleContainer = physicsAnimationLayout;
        physicsAnimationLayout.setActiveController(this.mStackAnimationController);
        float f = (float) dimensionPixelSize;
        this.mBubbleContainer.setElevation(f);
        this.mBubbleContainer.setClipChildren(false);
        addView(this.mBubbleContainer, new FrameLayout.LayoutParams(-1, -1));
        FrameLayout frameLayout = new FrameLayout(context2);
        this.mExpandedViewContainer = frameLayout;
        frameLayout.setElevation(f);
        this.mExpandedViewContainer.setClipChildren(false);
        addView(this.mExpandedViewContainer);
        FrameLayout frameLayout2 = new FrameLayout(getContext());
        this.mAnimatingOutSurfaceContainer = frameLayout2;
        frameLayout2.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        addView(this.mAnimatingOutSurfaceContainer);
        SurfaceView surfaceView = new SurfaceView(getContext());
        this.mAnimatingOutSurfaceView = surfaceView;
        surfaceView.setUseAlpha();
        this.mAnimatingOutSurfaceView.setZOrderOnTop(true);
        this.mAnimatingOutSurfaceView.setCornerRadius(ScreenDecorationsUtils.supportsRoundedCornersOnWindows(this.mContext.getResources()) ? (float) this.mCornerRadius : 0.0f);
        this.mAnimatingOutSurfaceView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        this.mAnimatingOutSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            }

            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                BubbleStackView.this.mAnimatingOutSurfaceReady = true;
            }

            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                BubbleStackView.this.mAnimatingOutSurfaceReady = false;
            }
        });
        this.mAnimatingOutSurfaceContainer.addView(this.mAnimatingOutSurfaceView);
        this.mAnimatingOutSurfaceContainer.setPadding(this.mExpandedViewContainer.getPaddingLeft(), this.mExpandedViewContainer.getPaddingTop(), this.mExpandedViewContainer.getPaddingRight(), this.mExpandedViewContainer.getPaddingBottom());
        setUpManageMenu();
        setUpFlyout();
        springAnimation.setSpring(new SpringForce().setStiffness(200.0f).setDampingRatio(0.75f));
        springAnimation.addEndListener(bubbleStackView$$ExternalSyntheticLambda12);
        setUpDismissView();
        setClipChildren(false);
        setFocusable(true);
        this.mBubbleContainer.bringToFront();
        BubbleOverflow overflow = bubbleData.getOverflow();
        this.mBubbleOverflow = overflow;
        this.mBubbleContainer.addView(overflow.getIconView(), this.mBubbleContainer.getChildCount(), new FrameLayout.LayoutParams(this.mPositioner.getBubbleSize(), this.mPositioner.getBubbleSize()));
        updateOverflow();
        this.mBubbleOverflow.getIconView().setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda17(this));
        View view = new View(getContext());
        this.mScrim = view;
        view.setImportantForAccessibility(2);
        this.mScrim.setBackgroundDrawable(new ColorDrawable(getResources().getColor(17170473)));
        addView(this.mScrim);
        this.mScrim.setAlpha(0.0f);
        View view2 = new View(getContext());
        this.mManageMenuScrim = view2;
        view2.setImportantForAccessibility(2);
        this.mManageMenuScrim.setBackgroundDrawable(new ColorDrawable(getResources().getColor(17170473)));
        addView(this.mManageMenuScrim, new FrameLayout.LayoutParams(-1, -1));
        this.mManageMenuScrim.setAlpha(0.0f);
        this.mManageMenuScrim.setVisibility(4);
        this.mOrientationChangedListener = new BubbleStackView$$ExternalSyntheticLambda18(this);
        float dimensionPixelSize2 = ((float) getResources().getDimensionPixelSize(R.dimen.dismiss_circle_small)) / ((float) getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size));
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        this.mDismissBubbleAnimator = ofFloat3;
        ofFloat3.addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda19(this, dimensionPixelSize2));
        setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda8(this));
        ViewPropertyAnimator animate = animate();
        Interpolator interpolator = Interpolators.PANEL_CLOSE_ACCELERATED;
        animate.setInterpolator(interpolator).setDuration(320);
        ofFloat2.setDuration(150);
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                if (BubbleStackView.this.mExpandedBubble != null && BubbleStackView.this.mExpandedBubble.getExpandedView() != null) {
                    BubbleStackView.this.mExpandedBubble.getExpandedView().setSurfaceZOrderedOnTop(true);
                    BubbleStackView.this.mExpandedBubble.getExpandedView().setAlphaAnimating(true);
                }
            }

            public void onAnimationEnd(Animator animator) {
                if (BubbleStackView.this.mExpandedBubble != null && BubbleStackView.this.mExpandedBubble.getExpandedView() != null && !BubbleStackView.this.mExpandedViewTemporarilyHidden) {
                    BubbleStackView.this.mExpandedBubble.getExpandedView().setSurfaceZOrderedOnTop(false);
                    BubbleStackView.this.mExpandedBubble.getExpandedView().setAlphaAnimating(false);
                }
            }
        });
        ofFloat2.addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda9(this));
        ofFloat.setDuration(150);
        ofFloat.setInterpolator(interpolator);
        ofFloat.addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda10(this));
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                BubbleStackView.this.releaseAnimatingOutBubbleBuffer();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (getBubbleCount() == 0) {
            this.mBubbleController.onAllBubblesAnimatedOut();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        this.mBubbleData.setShowingOverflow(true);
        this.mBubbleData.setSelectedBubble(this.mBubbleOverflow);
        this.mBubbleData.setExpanded(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        RelativeStackPosition relativeStackPosition;
        this.mPositioner.update();
        onDisplaySizeChanged();
        this.mExpandedAnimationController.updateResources();
        this.mStackAnimationController.updateResources();
        this.mBubbleOverflow.updateResources();
        if (!isStackEduShowing() && (relativeStackPosition = this.mRelativeStackPositionBeforeRotation) != null) {
            this.mStackAnimationController.setStackPosition(relativeStackPosition);
            this.mRelativeStackPositionBeforeRotation = null;
        }
        if (this.mIsExpanded) {
            beforeExpandedViewAnimation();
            updateOverflowVisibility();
            updatePointerPosition(false);
            this.mExpandedAnimationController.expandFromStack(new BubbleStackView$$ExternalSyntheticLambda22(this));
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(getBubbleIndex(this.mExpandedBubble), getState());
            BubblePositioner bubblePositioner = this.mPositioner;
            float expandedViewY = bubblePositioner.getExpandedViewY(this.mExpandedBubble, bubblePositioner.showBubblesVertically() ? expandedBubbleXY.y : expandedBubbleXY.x);
            this.mExpandedViewContainer.setTranslationX(0.0f);
            this.mExpandedViewContainer.setTranslationY(expandedViewY);
            this.mExpandedViewContainer.setAlpha(1.0f);
        }
        removeOnLayoutChangeListener(this.mOrientationChangedListener);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        afterExpandedViewAnimation();
        showManageMenu(this.mShowingManage);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        DismissView dismissView = this.mDismissView;
        if (dismissView != null) {
            dismissView.setPivotX(((float) (dismissView.getRight() - this.mDismissView.getLeft())) / 2.0f);
            DismissView dismissView2 = this.mDismissView;
            dismissView2.setPivotY(((float) (dismissView2.getBottom() - this.mDismissView.getTop())) / 2.0f);
            float max = Math.max(floatValue, f);
            this.mDismissView.getCircle().setScaleX(max);
            this.mDismissView.getCircle().setScaleY(max);
        }
        View view = this.mViewBeingDismissed;
        if (view != null) {
            view.setAlpha(Math.max(floatValue, 0.7f));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(View view) {
        if (this.mShowingManage) {
            showManageMenu(false);
            return;
        }
        ManageEducationView manageEducationView = this.mManageEduView;
        if (manageEducationView != null && manageEducationView.getVisibility() == 0) {
            this.mManageEduView.hide();
        } else if (isStackEduShowing()) {
            this.mStackEduView.hide(false);
        } else if (this.mBubbleData.isExpanded()) {
            this.mBubbleData.setExpanded(false);
        } else {
            maybeShowStackEdu();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(ValueAnimator valueAnimator) {
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider != null && bubbleViewProvider.getExpandedView() != null) {
            this.mExpandedBubble.getExpandedView().setTaskViewAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(ValueAnimator valueAnimator) {
        if (!this.mExpandedViewTemporarilyHidden) {
            this.mAnimatingOutSurfaceView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    public void setTemporarilyInvisible(boolean z) {
        this.mTemporarilyInvisible = z;
        updateTemporarilyInvisibleAnimation(z);
    }

    public final void updateTemporarilyInvisibleAnimation(boolean z) {
        removeCallbacks(this.mAnimateTemporarilyInvisibleImmediate);
        if (!this.mIsDraggingStack) {
            postDelayed(this.mAnimateTemporarilyInvisibleImmediate, (!(this.mTemporarilyInvisible && this.mFlyout.getVisibility() != 0) || z) ? 0 : 1000);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10() {
        if (!this.mTemporarilyInvisible || this.mFlyout.getVisibility() == 0) {
            animate().translationX(0.0f).start();
        } else if (this.mStackAnimationController.isStackOnLeftSide()) {
            animate().translationX((float) (-(this.mBubbleSize + (this.mPositioner.getAvailableRect().left - this.mPositioner.getScreenRect().left)))).start();
        } else {
            animate().translationX((float) (this.mBubbleSize - (this.mPositioner.getAvailableRect().right - this.mPositioner.getScreenRect().right))).start();
        }
    }

    public final void setUpDismissView() {
        DismissView dismissView = this.mDismissView;
        if (dismissView != null) {
            removeView(dismissView);
        }
        this.mDismissView = new DismissView(getContext());
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.bubble_elevation);
        addView(this.mDismissView);
        this.mDismissView.setElevation((float) dimensionPixelSize);
        this.mMagneticTarget = new MagnetizedObject.MagneticTarget(this.mDismissView.getCircle(), Settings.Secure.getInt(getContext().getContentResolver(), "bubble_dismiss_radius", this.mBubbleSize * 2));
        this.mBubbleContainer.bringToFront();
    }

    public final void setUpManageMenu() {
        ViewGroup viewGroup = this.mManageMenu;
        if (viewGroup != null) {
            removeView(viewGroup);
        }
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.bubble_manage_menu, this, false);
        this.mManageMenu = viewGroup2;
        viewGroup2.setVisibility(4);
        PhysicsAnimator.getInstance(this.mManageMenu).setDefaultSpringConfig(this.mManageSpringConfig);
        this.mManageMenu.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) BubbleStackView.this.mCornerRadius);
            }
        });
        this.mManageMenu.setClipToOutline(true);
        this.mManageMenu.findViewById(R.id.bubble_manage_menu_dismiss_container).setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda24(this));
        this.mManageMenu.findViewById(R.id.bubble_manage_menu_dont_bubble_container).setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda25(this));
        this.mManageMenu.findViewById(R.id.bubble_manage_menu_settings_container).setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda26(this));
        this.mManageSettingsIcon = (ImageView) this.mManageMenu.findViewById(R.id.bubble_manage_menu_settings_icon);
        this.mManageSettingsText = (TextView) this.mManageMenu.findViewById(R.id.bubble_manage_menu_settings_name);
        this.mManageMenu.setLayoutDirection(3);
        addView(this.mManageMenu);
        lambda$updateExpandedBubble$40();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setUpManageMenu$11(View view) {
        showManageMenu(false);
        dismissBubbleIfExists(this.mBubbleData.getSelectedBubble());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setUpManageMenu$12(View view) {
        showManageMenu(false);
        this.mUnbubbleConversationCallback.accept(this.mBubbleData.getSelectedBubble().getKey());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setUpManageMenu$13(View view) {
        showManageMenu(false);
        BubbleViewProvider selectedBubble = this.mBubbleData.getSelectedBubble();
        if (selectedBubble != null && this.mBubbleData.hasBubbleInStackWithKey(selectedBubble.getKey())) {
            Bubble bubble = (Bubble) selectedBubble;
            Intent settingsIntent = bubble.getSettingsIntent(this.mContext);
            this.mBubbleData.setExpanded(false);
            this.mContext.startActivityAsUser(settingsIntent, bubble.getUser());
            logBubbleEvent(selectedBubble, 9);
        }
    }

    public final boolean shouldShowManageEdu() {
        if (ActivityManager.isRunningInTestHarness()) {
            return false;
        }
        if ((!getPrefBoolean("HasSeenBubblesManageOnboarding") || BubbleDebugConfig.forceShowUserEducation(this.mContext)) && this.mExpandedBubble != null) {
            return true;
        }
        return false;
    }

    public final void maybeShowManageEdu() {
        if (shouldShowManageEdu()) {
            if (this.mManageEduView == null) {
                ManageEducationView manageEducationView = new ManageEducationView(this.mContext, this.mPositioner);
                this.mManageEduView = manageEducationView;
                addView(manageEducationView);
            }
            this.mManageEduView.show(this.mExpandedBubble.getExpandedView());
        }
    }

    public final boolean shouldShowStackEdu() {
        if (ActivityManager.isRunningInTestHarness()) {
            return false;
        }
        if (!getPrefBoolean("HasSeenBubblesOnboarding") || BubbleDebugConfig.forceShowUserEducation(this.mContext)) {
            return true;
        }
        return false;
    }

    public final boolean getPrefBoolean(String str) {
        Context context = this.mContext;
        return context.getSharedPreferences(context.getPackageName(), 0).getBoolean(str, false);
    }

    public final boolean maybeShowStackEdu() {
        if (!shouldShowStackEdu() || isExpanded()) {
            return false;
        }
        if (this.mStackEduView == null) {
            StackEducationView stackEducationView = new StackEducationView(this.mContext, this.mPositioner, this.mBubbleController);
            this.mStackEduView = stackEducationView;
            addView(stackEducationView);
        }
        this.mBubbleContainer.bringToFront();
        this.mStackAnimationController.setStackPosition(this.mPositioner.getDefaultStartPosition());
        return this.mStackEduView.show(this.mPositioner.getDefaultStartPosition());
    }

    public final boolean isStackEduShowing() {
        StackEducationView stackEducationView = this.mStackEduView;
        return stackEducationView != null && stackEducationView.getVisibility() == 0;
    }

    public final void updateUserEdu() {
        if (isStackEduShowing()) {
            removeView(this.mStackEduView);
            StackEducationView stackEducationView = new StackEducationView(this.mContext, this.mPositioner, this.mBubbleController);
            this.mStackEduView = stackEducationView;
            addView(stackEducationView);
            this.mBubbleContainer.bringToFront();
            this.mStackAnimationController.setStackPosition(this.mPositioner.getDefaultStartPosition());
            this.mStackEduView.show(this.mPositioner.getDefaultStartPosition());
        }
        ManageEducationView manageEducationView = this.mManageEduView;
        if (manageEducationView != null && manageEducationView.getVisibility() == 0) {
            removeView(this.mManageEduView);
            ManageEducationView manageEducationView2 = new ManageEducationView(this.mContext, this.mPositioner);
            this.mManageEduView = manageEducationView2;
            addView(manageEducationView2);
            this.mManageEduView.show(this.mExpandedBubble.getExpandedView());
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final void setUpFlyout() {
        BubbleFlyoutView bubbleFlyoutView = this.mFlyout;
        if (bubbleFlyoutView != null) {
            removeView(bubbleFlyoutView);
        }
        BubbleFlyoutView bubbleFlyoutView2 = new BubbleFlyoutView(getContext(), this.mPositioner);
        this.mFlyout = bubbleFlyoutView2;
        bubbleFlyoutView2.setVisibility(8);
        this.mFlyout.setOnClickListener(this.mFlyoutClickListener);
        this.mFlyout.setOnTouchListener(this.mFlyoutTouchListener);
        addView(this.mFlyout, new FrameLayout.LayoutParams(-2, -2));
    }

    public void updateFontScale() {
        setUpManageMenu();
        this.mFlyout.updateFontSize();
        for (Bubble next : this.mBubbleData.getBubbles()) {
            if (next.getExpandedView() != null) {
                next.getExpandedView().updateFontSize();
            }
        }
        BubbleOverflow bubbleOverflow = this.mBubbleOverflow;
        if (bubbleOverflow != null && bubbleOverflow.getExpandedView() != null) {
            this.mBubbleOverflow.getExpandedView().updateFontSize();
        }
    }

    public final void updateOverflow() {
        this.mBubbleOverflow.update();
        this.mBubbleContainer.reorderView(this.mBubbleOverflow.getIconView(), this.mBubbleContainer.getChildCount() - 1);
        updateOverflowVisibility();
    }

    public void updateOverflowButtonDot() {
        for (Bubble showDot : this.mBubbleData.getOverflowBubbles()) {
            if (showDot.showDot()) {
                this.mBubbleOverflow.setShowDot(true);
                return;
            }
        }
        this.mBubbleOverflow.setShowDot(false);
    }

    public void onThemeChanged() {
        setUpFlyout();
        setUpManageMenu();
        setUpDismissView();
        updateOverflow();
        updateUserEdu();
        updateExpandedViewTheme();
        this.mScrim.setBackgroundDrawable(new ColorDrawable(getResources().getColor(17170473)));
        this.mManageMenuScrim.setBackgroundDrawable(new ColorDrawable(getResources().getColor(17170473)));
    }

    public void onOrientationChanged() {
        this.mRelativeStackPositionBeforeRotation = new RelativeStackPosition(this.mPositioner.getRestingPosition(), this.mPositioner.getAllowableStackPositionRegion(getBubbleCount()));
        addOnLayoutChangeListener(this.mOrientationChangedListener);
        hideFlyoutImmediate();
    }

    public void onLayoutDirectionChanged(int i) {
        this.mManageMenu.setLayoutDirection(i);
        this.mFlyout.setLayoutDirection(i);
        StackEducationView stackEducationView = this.mStackEduView;
        if (stackEducationView != null) {
            stackEducationView.setLayoutDirection(i);
        }
        ManageEducationView manageEducationView = this.mManageEduView;
        if (manageEducationView != null) {
            manageEducationView.setLayoutDirection(i);
        }
        updateExpandedViewDirection(i);
    }

    public void onDisplaySizeChanged() {
        updateOverflow();
        setUpFlyout();
        setUpDismissView();
        updateUserEdu();
        this.mBubbleSize = this.mPositioner.getBubbleSize();
        for (Bubble next : this.mBubbleData.getBubbles()) {
            if (next.getIconView() == null) {
                Log.d("Bubbles", "Display size changed. Icon null: " + next);
            } else {
                BadgedImageView iconView = next.getIconView();
                int i = this.mBubbleSize;
                iconView.setLayoutParams(new FrameLayout.LayoutParams(i, i));
                if (next.getExpandedView() != null) {
                    next.getExpandedView().updateDimensions();
                }
            }
        }
        BadgedImageView iconView2 = this.mBubbleOverflow.getIconView();
        int i2 = this.mBubbleSize;
        iconView2.setLayoutParams(new FrameLayout.LayoutParams(i2, i2));
        this.mExpandedAnimationController.updateResources();
        this.mStackAnimationController.updateResources();
        this.mDismissView.updateResources();
        this.mMagneticTarget.setMagneticFieldRadiusPx(this.mBubbleSize * 2);
        if (!isStackEduShowing()) {
            this.mStackAnimationController.setStackPosition(new RelativeStackPosition(this.mPositioner.getRestingPosition(), this.mPositioner.getAllowableStackPositionRegion(getBubbleCount())));
        }
        if (this.mIsExpanded) {
            updateExpandedView();
        }
        setUpManageMenu();
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        this.mTempRect.setEmpty();
        getTouchableRegion(this.mTempRect);
        internalInsetsInfo.touchableRegion.set(this.mTempRect);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mPositioner.update();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        getViewTreeObserver().addOnDrawListener(this.mSystemGestureExcludeUpdater);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this.mViewUpdater);
        getViewTreeObserver().removeOnDrawListener(this.mSystemGestureExcludeUpdater);
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
        BubbleOverflow bubbleOverflow = this.mBubbleOverflow;
        if (bubbleOverflow != null) {
            bubbleOverflow.cleanUpExpandedState();
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        setupLocalMenu(accessibilityNodeInfo);
    }

    public void updateExpandedViewTheme() {
        List<Bubble> bubbles = this.mBubbleData.getBubbles();
        if (!bubbles.isEmpty()) {
            bubbles.forEach(new BubbleStackView$$ExternalSyntheticLambda43());
        }
    }

    public static /* synthetic */ void lambda$updateExpandedViewTheme$14(Bubble bubble) {
        if (bubble.getExpandedView() != null) {
            bubble.getExpandedView().applyThemeAttrs();
        }
    }

    public void updateExpandedViewDirection(int i) {
        List<Bubble> bubbles = this.mBubbleData.getBubbles();
        if (!bubbles.isEmpty()) {
            bubbles.forEach(new BubbleStackView$$ExternalSyntheticLambda27(i));
        }
    }

    public static /* synthetic */ void lambda$updateExpandedViewDirection$15(int i, Bubble bubble) {
        if (bubble.getExpandedView() != null) {
            bubble.getExpandedView().setLayoutDirection(i);
        }
    }

    public void setupLocalMenu(AccessibilityNodeInfo accessibilityNodeInfo) {
        Resources resources = this.mContext.getResources();
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_top_left, resources.getString(R.string.bubble_accessibility_action_move_top_left)));
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_top_right, resources.getString(R.string.bubble_accessibility_action_move_top_right)));
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_bottom_left, resources.getString(R.string.bubble_accessibility_action_move_bottom_left)));
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_bottom_right, resources.getString(R.string.bubble_accessibility_action_move_bottom_right)));
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        if (this.mIsExpanded) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
        } else {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
        }
    }

    public boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        if (super.performAccessibilityActionInternal(i, bundle)) {
            return true;
        }
        RectF allowableStackPositionRegion = this.mPositioner.getAllowableStackPositionRegion(getBubbleCount());
        if (i == 1048576) {
            this.mBubbleData.dismissAll(6);
            announceForAccessibility(getResources().getString(R.string.accessibility_bubble_dismissed));
            return true;
        } else if (i == 524288) {
            this.mBubbleData.setExpanded(false);
            return true;
        } else if (i == 262144) {
            this.mBubbleData.setExpanded(true);
            return true;
        } else if (i == R.id.action_move_top_left) {
            this.mStackAnimationController.springStackAfterFling(allowableStackPositionRegion.left, allowableStackPositionRegion.top);
            return true;
        } else if (i == R.id.action_move_top_right) {
            this.mStackAnimationController.springStackAfterFling(allowableStackPositionRegion.right, allowableStackPositionRegion.top);
            return true;
        } else if (i == R.id.action_move_bottom_left) {
            this.mStackAnimationController.springStackAfterFling(allowableStackPositionRegion.left, allowableStackPositionRegion.bottom);
            return true;
        } else if (i != R.id.action_move_bottom_right) {
            return false;
        } else {
            this.mStackAnimationController.springStackAfterFling(allowableStackPositionRegion.right, allowableStackPositionRegion.bottom);
            return true;
        }
    }

    public void updateContentDescription() {
        if (!this.mBubbleData.getBubbles().isEmpty()) {
            for (int i = 0; i < this.mBubbleData.getBubbles().size(); i++) {
                Bubble bubble = this.mBubbleData.getBubbles().get(i);
                String appName = bubble.getAppName();
                String title = bubble.getTitle();
                if (title == null) {
                    title = getResources().getString(R.string.notification_bubble_title);
                }
                if (bubble.getIconView() != null) {
                    if (this.mIsExpanded || i > 0) {
                        bubble.getIconView().setContentDescription(getResources().getString(R.string.bubble_content_description_single, new Object[]{title, appName}));
                    } else {
                        bubble.getIconView().setContentDescription(getResources().getString(R.string.bubble_content_description_stack, new Object[]{title, appName, Integer.valueOf(this.mBubbleContainer.getChildCount() - 1)}));
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0070, code lost:
        r0 = r6.mBubbleData.getBubbles().get(r6.mBubbleData.getBubbles().size() - 1).getIconView();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateBubblesAcessibillityStates() {
        /*
            r6 = this;
            r0 = 0
        L_0x0001:
            com.android.wm.shell.bubbles.BubbleData r1 = r6.mBubbleData
            java.util.List r1 = r1.getBubbles()
            int r1 = r1.size()
            r2 = 0
            r3 = 1
            if (r0 >= r1) goto L_0x0056
            if (r0 <= 0) goto L_0x0020
            com.android.wm.shell.bubbles.BubbleData r1 = r6.mBubbleData
            java.util.List r1 = r1.getBubbles()
            int r4 = r0 + -1
            java.lang.Object r1 = r1.get(r4)
            com.android.wm.shell.bubbles.Bubble r1 = (com.android.wm.shell.bubbles.Bubble) r1
            goto L_0x0021
        L_0x0020:
            r1 = r2
        L_0x0021:
            com.android.wm.shell.bubbles.BubbleData r4 = r6.mBubbleData
            java.util.List r4 = r4.getBubbles()
            java.lang.Object r4 = r4.get(r0)
            com.android.wm.shell.bubbles.Bubble r4 = (com.android.wm.shell.bubbles.Bubble) r4
            com.android.wm.shell.bubbles.BadgedImageView r4 = r4.getIconView()
            if (r4 != 0) goto L_0x0034
            goto L_0x0053
        L_0x0034:
            boolean r5 = r6.mIsExpanded
            if (r5 == 0) goto L_0x004c
            r4.setImportantForAccessibility(r3)
            if (r1 == 0) goto L_0x0041
            com.android.wm.shell.bubbles.BadgedImageView r2 = r1.getIconView()
        L_0x0041:
            if (r2 == 0) goto L_0x0053
            com.android.wm.shell.bubbles.BubbleStackView$14 r1 = new com.android.wm.shell.bubbles.BubbleStackView$14
            r1.<init>(r2)
            r4.setAccessibilityDelegate(r1)
            goto L_0x0053
        L_0x004c:
            if (r0 != 0) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r3 = 2
        L_0x0050:
            r4.setImportantForAccessibility(r3)
        L_0x0053:
            int r0 = r0 + 1
            goto L_0x0001
        L_0x0056:
            boolean r0 = r6.mIsExpanded
            if (r0 == 0) goto L_0x0095
            com.android.wm.shell.bubbles.BubbleOverflow r0 = r6.mBubbleOverflow
            if (r0 == 0) goto L_0x0062
            com.android.wm.shell.bubbles.BadgedImageView r2 = r0.getIconView()
        L_0x0062:
            if (r2 == 0) goto L_0x0095
            com.android.wm.shell.bubbles.BubbleData r0 = r6.mBubbleData
            java.util.List r0 = r0.getBubbles()
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0095
            com.android.wm.shell.bubbles.BubbleData r0 = r6.mBubbleData
            java.util.List r0 = r0.getBubbles()
            com.android.wm.shell.bubbles.BubbleData r1 = r6.mBubbleData
            java.util.List r1 = r1.getBubbles()
            int r1 = r1.size()
            int r1 = r1 - r3
            java.lang.Object r0 = r0.get(r1)
            com.android.wm.shell.bubbles.Bubble r0 = (com.android.wm.shell.bubbles.Bubble) r0
            com.android.wm.shell.bubbles.BadgedImageView r0 = r0.getIconView()
            if (r0 == 0) goto L_0x0095
            com.android.wm.shell.bubbles.BubbleStackView$15 r1 = new com.android.wm.shell.bubbles.BubbleStackView$15
            r1.<init>(r0)
            r2.setAccessibilityDelegate(r1)
        L_0x0095:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleStackView.updateBubblesAcessibillityStates():void");
    }

    public final void updateSystemGestureExcludeRects() {
        Rect rect = this.mSystemGestureExclusionRects.get(0);
        if (getBubbleCount() > 0) {
            View childAt = this.mBubbleContainer.getChildAt(0);
            rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
            rect.offset((int) (childAt.getTranslationX() + 0.5f), (int) (childAt.getTranslationY() + 0.5f));
            this.mBubbleContainer.setSystemGestureExclusionRects(this.mSystemGestureExclusionRects);
            return;
        }
        rect.setEmpty();
        this.mBubbleContainer.setSystemGestureExclusionRects(Collections.emptyList());
    }

    public void setExpandListener(Bubbles.BubbleExpandListener bubbleExpandListener) {
        this.mExpandListener = bubbleExpandListener;
    }

    public void setUnbubbleConversationCallback(Consumer<String> consumer) {
        this.mUnbubbleConversationCallback = consumer;
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public boolean isExpansionAnimating() {
        return this.mIsExpansionAnimating;
    }

    public boolean isSwitchAnimating() {
        return this.mIsBubbleSwitchAnimating;
    }

    @VisibleForTesting
    public BubbleViewProvider getExpandedBubble() {
        return this.mExpandedBubble;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void addBubble(Bubble bubble) {
        boolean z = getBubbleCount() == 0;
        if (z && shouldShowStackEdu()) {
            this.mStackAnimationController.setStackPosition(this.mPositioner.getDefaultStartPosition());
        }
        if (bubble.getIconView() != null) {
            this.mBubbleContainer.addView(bubble.getIconView(), 0, new FrameLayout.LayoutParams(this.mPositioner.getBubbleSize(), this.mPositioner.getBubbleSize()));
            if (z) {
                this.mStackOnLeftOrWillBe = this.mStackAnimationController.isStackOnLeftSide();
            }
            bubble.getIconView().setDotBadgeOnLeft(!this.mStackOnLeftOrWillBe);
            bubble.getIconView().setOnClickListener(this.mBubbleClickListener);
            bubble.getIconView().setOnTouchListener(this.mBubbleTouchListener);
            updateBubbleShadows(false);
            animateInFlyoutForBubble(bubble);
            requestUpdate();
            logBubbleEvent(bubble, 1);
        }
    }

    public void removeBubble(Bubble bubble) {
        int i = 0;
        while (i < getBubbleCount()) {
            View childAt = this.mBubbleContainer.getChildAt(i);
            if (!(childAt instanceof BadgedImageView) || !((BadgedImageView) childAt).getKey().equals(bubble.getKey())) {
                i++;
            } else {
                this.mBubbleContainer.removeViewAt(i);
                if (this.mBubbleData.hasOverflowBubbleWithKey(bubble.getKey())) {
                    bubble.cleanupExpandedView();
                } else {
                    bubble.cleanupViews();
                }
                updateExpandedView();
                if (getBubbleCount() == 0 && !isExpanded()) {
                    updateStackPosition();
                }
                logBubbleEvent(bubble, 5);
                return;
            }
        }
        if (bubble.isSuppressed()) {
            bubble.cleanupViews();
            return;
        }
        Log.d("Bubbles", "was asked to remove Bubble, but didn't find the view! " + bubble);
    }

    public final void updateOverflowVisibility() {
        this.mBubbleOverflow.setVisible((this.mIsExpanded || this.mBubbleData.isShowingOverflow()) ? 0 : 8);
    }

    public void updateBubble(Bubble bubble) {
        animateInFlyoutForBubble(bubble);
        requestUpdate();
        logBubbleEvent(bubble, 2);
    }

    public void updateBubbleOrder(List<Bubble> list) {
        BubbleStackView$$ExternalSyntheticLambda20 bubbleStackView$$ExternalSyntheticLambda20 = new BubbleStackView$$ExternalSyntheticLambda20(this, list);
        if (this.mIsExpanded || isExpansionAnimating()) {
            bubbleStackView$$ExternalSyntheticLambda20.run();
            updateBadges(false);
            updateZOrder();
        } else if (!isExpansionAnimating()) {
            this.mStackAnimationController.animateReorder((List) list.stream().map(new BubbleStackView$$ExternalSyntheticLambda21()).collect(Collectors.toList()), bubbleStackView$$ExternalSyntheticLambda20);
        }
        updatePointerPosition(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBubbleOrder$16(List list) {
        for (int i = 0; i < list.size(); i++) {
            this.mBubbleContainer.reorderView(((Bubble) list.get(i)).getIconView(), i);
        }
    }

    public void setSelectedBubble(BubbleViewProvider bubbleViewProvider) {
        BubbleViewProvider bubbleViewProvider2;
        if (bubbleViewProvider == null) {
            this.mBubbleData.setShowingOverflow(false);
        } else if (this.mExpandedBubble != bubbleViewProvider) {
            if (bubbleViewProvider.getKey().equals("Overflow")) {
                this.mBubbleData.setShowingOverflow(true);
            } else {
                this.mBubbleData.setShowingOverflow(false);
            }
            if (this.mIsExpanded && this.mIsExpansionAnimating) {
                cancelAllExpandCollapseSwitchAnimations();
            }
            showManageMenu(false);
            if (!this.mIsExpanded || (bubbleViewProvider2 = this.mExpandedBubble) == null || bubbleViewProvider2.getExpandedView() == null || this.mExpandedViewTemporarilyHidden) {
                showNewlySelectedBubble(bubbleViewProvider);
                return;
            }
            BubbleViewProvider bubbleViewProvider3 = this.mExpandedBubble;
            if (!(bubbleViewProvider3 == null || bubbleViewProvider3.getExpandedView() == null)) {
                this.mExpandedBubble.getExpandedView().setSurfaceZOrderedOnTop(true);
            }
            try {
                screenshotAnimatingOutBubbleIntoSurface(new BubbleStackView$$ExternalSyntheticLambda23(this, bubbleViewProvider));
            } catch (Exception e) {
                showNewlySelectedBubble(bubbleViewProvider);
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSelectedBubble$18(BubbleViewProvider bubbleViewProvider, Boolean bool) {
        this.mAnimatingOutSurfaceContainer.setVisibility(bool.booleanValue() ? 0 : 4);
        showNewlySelectedBubble(bubbleViewProvider);
    }

    public final void showNewlySelectedBubble(BubbleViewProvider bubbleViewProvider) {
        BubbleViewProvider bubbleViewProvider2 = this.mExpandedBubble;
        this.mExpandedBubble = bubbleViewProvider;
        if (this.mIsExpanded) {
            hideCurrentInputMethod();
            this.mExpandedViewContainer.setAlpha(0.0f);
            this.mSurfaceSynchronizer.syncSurfaceAndRun(new BubbleStackView$$ExternalSyntheticLambda36(this, bubbleViewProvider2, bubbleViewProvider));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showNewlySelectedBubble$19(BubbleViewProvider bubbleViewProvider, BubbleViewProvider bubbleViewProvider2) {
        if (bubbleViewProvider != null) {
            bubbleViewProvider.setTaskViewVisibility(false);
        }
        updateExpandedBubble();
        requestUpdate();
        logBubbleEvent(bubbleViewProvider, 4);
        logBubbleEvent(bubbleViewProvider2, 3);
        notifyExpansionChanged(bubbleViewProvider, false);
        notifyExpansionChanged(bubbleViewProvider2, true);
    }

    public void setExpanded(boolean z) {
        if (!z) {
            releaseAnimatingOutBubbleBuffer();
        }
        if (z != this.mIsExpanded) {
            hideCurrentInputMethod();
            this.mBubbleController.getSysuiProxy().onStackExpandChanged(z);
            if (this.mIsExpanded) {
                animateCollapse();
                logBubbleEvent(this.mExpandedBubble, 4);
            } else {
                animateExpansion();
                logBubbleEvent(this.mExpandedBubble, 3);
                logBubbleEvent(this.mExpandedBubble, 15);
            }
            notifyExpansionChanged(this.mExpandedBubble, this.mIsExpanded);
        }
    }

    public void onBackPressed() {
        if (!this.mIsExpanded) {
            return;
        }
        if (this.mShowingManage) {
            showManageMenu(false);
            return;
        }
        ManageEducationView manageEducationView = this.mManageEduView;
        if (manageEducationView == null || manageEducationView.getVisibility() != 0) {
            this.mBubbleData.setExpanded(false);
        } else {
            this.mManageEduView.hide();
        }
    }

    public void setBubbleSuppressed(Bubble bubble, boolean z) {
        if (z) {
            this.mBubbleContainer.removeViewAt(getBubbleIndex(bubble));
            updateExpandedView();
        } else if (bubble.getIconView() != null) {
            if (bubble.getIconView().getParent() != null) {
                Log.e("Bubbles", "Bubble is already added to parent. Can't unsuppress: " + bubble);
                return;
            }
            this.mBubbleContainer.addView(bubble.getIconView(), this.mBubbleData.getBubbles().indexOf(bubble), new FrameLayout.LayoutParams(this.mPositioner.getBubbleSize(), this.mPositioner.getBubbleSize()));
            updateBubbleShadows(false);
            requestUpdate();
        }
    }

    public void hideCurrentInputMethod() {
        this.mPositioner.setImeVisible(false, 0);
        this.mBubbleController.hideCurrentInputMethod();
    }

    public void updateStackPosition() {
        this.mStackAnimationController.setStackPosition(this.mPositioner.getRestingPosition());
        this.mDismissView.hide();
    }

    public final void beforeExpandedViewAnimation() {
        this.mIsExpansionAnimating = true;
        hideFlyoutImmediate();
        updateExpandedBubble();
        updateExpandedView();
    }

    public final void afterExpandedViewAnimation() {
        this.mIsExpansionAnimating = false;
        updateExpandedView();
        requestUpdate();
    }

    public final void hideExpandedViewIfNeeded() {
        BubbleViewProvider bubbleViewProvider;
        if (!this.mExpandedViewTemporarilyHidden && (bubbleViewProvider = this.mExpandedBubble) != null && bubbleViewProvider.getExpandedView() != null) {
            this.mExpandedViewTemporarilyHidden = true;
            PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).spring(AnimatableScaleMatrix.SCALE_X, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(0.9f), this.mScaleOutSpringConfig).spring(AnimatableScaleMatrix.SCALE_Y, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(0.9f), this.mScaleOutSpringConfig).addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda28(this)).start();
            this.mExpandedViewAlphaAnimator.reverse();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hideExpandedViewIfNeeded$20(AnimatableScaleMatrix animatableScaleMatrix, ArrayMap arrayMap) {
        this.mExpandedViewContainer.setAnimationMatrix(this.mExpandedViewContainerMatrix);
    }

    public final void showExpandedViewIfNeeded() {
        if (this.mExpandedViewTemporarilyHidden) {
            this.mExpandedViewTemporarilyHidden = false;
            PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).spring(AnimatableScaleMatrix.SCALE_X, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(1.0f), this.mScaleOutSpringConfig).spring(AnimatableScaleMatrix.SCALE_Y, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(1.0f), this.mScaleOutSpringConfig).addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda33(this)).start();
            this.mExpandedViewAlphaAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showExpandedViewIfNeeded$21(AnimatableScaleMatrix animatableScaleMatrix, ArrayMap arrayMap) {
        this.mExpandedViewContainer.setAnimationMatrix(this.mExpandedViewContainerMatrix);
    }

    public final void showScrim(boolean z) {
        if (z) {
            this.mScrim.animate().setInterpolator(Interpolators.ALPHA_IN).alpha(0.6f).start();
        } else {
            this.mScrim.animate().alpha(0.0f).setInterpolator(Interpolators.ALPHA_OUT).start();
        }
    }

    public final void animateExpansion() {
        int i;
        float f;
        float f2;
        float f3;
        cancelDelayedExpandCollapseSwitchAnimations();
        boolean showBubblesVertically = this.mPositioner.showBubblesVertically();
        this.mIsExpanded = true;
        if (isStackEduShowing()) {
            this.mStackEduView.hide(true);
        }
        beforeExpandedViewAnimation();
        showScrim(true);
        updateZOrder();
        updateBadges(false);
        this.mBubbleContainer.setActiveController(this.mExpandedAnimationController);
        updateOverflowVisibility();
        updatePointerPosition(false);
        this.mExpandedAnimationController.expandFromStack(new BubbleStackView$$ExternalSyntheticLambda34(this));
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider == null || !"Overflow".equals(bubbleViewProvider.getKey())) {
            i = getBubbleIndex(this.mExpandedBubble);
        } else {
            i = this.mBubbleData.getBubbles().size();
        }
        PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(i, getState());
        BubblePositioner bubblePositioner = this.mPositioner;
        float expandedViewY = bubblePositioner.getExpandedViewY(this.mExpandedBubble, bubblePositioner.showBubblesVertically() ? expandedBubbleXY.y : expandedBubbleXY.x);
        this.mExpandedViewContainer.setTranslationX(0.0f);
        this.mExpandedViewContainer.setTranslationY(expandedViewY);
        this.mExpandedViewContainer.setAlpha(1.0f);
        if (showBubblesVertically) {
            f = this.mStackAnimationController.getStackPosition().y;
        } else {
            f = this.mStackAnimationController.getStackPosition().x;
        }
        if (showBubblesVertically) {
            f2 = expandedBubbleXY.y;
        } else {
            f2 = expandedBubbleXY.x;
        }
        float abs = Math.abs(f2 - f);
        long j = 0;
        if (getWidth() > 0) {
            j = (long) (((abs / ((float) getWidth())) * 30.0f) + 210.00002f);
        }
        if (showBubblesVertically) {
            if (this.mStackOnLeftOrWillBe) {
                f3 = expandedBubbleXY.x + ((float) this.mBubbleSize) + ((float) this.mExpandedViewPadding);
            } else {
                f3 = expandedBubbleXY.x - ((float) this.mExpandedViewPadding);
            }
            this.mExpandedViewContainerMatrix.setScale(0.9f, 0.9f, f3, expandedBubbleXY.y + (((float) this.mBubbleSize) / 2.0f));
        } else {
            AnimatableScaleMatrix animatableScaleMatrix = this.mExpandedViewContainerMatrix;
            float f4 = expandedBubbleXY.x;
            int i2 = this.mBubbleSize;
            animatableScaleMatrix.setScale(0.9f, 0.9f, f4 + (((float) i2) / 2.0f), expandedBubbleXY.y + ((float) i2) + ((float) this.mExpandedViewPadding));
        }
        this.mExpandedViewContainer.setAnimationMatrix(this.mExpandedViewContainerMatrix);
        if (this.mExpandedBubble.getExpandedView() != null) {
            this.mExpandedBubble.getExpandedView().setTaskViewAlpha(0.0f);
            this.mExpandedBubble.getExpandedView().setAlphaAnimating(true);
        }
        BubbleStackView$$ExternalSyntheticLambda35 bubbleStackView$$ExternalSyntheticLambda35 = new BubbleStackView$$ExternalSyntheticLambda35(this, showBubblesVertically, f2);
        this.mDelayedAnimation = bubbleStackView$$ExternalSyntheticLambda35;
        this.mDelayedAnimationExecutor.executeDelayed(bubbleStackView$$ExternalSyntheticLambda35, j);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateExpansion$22() {
        if (this.mIsExpanded && this.mExpandedBubble.getExpandedView() != null) {
            maybeShowManageEdu();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateExpansion$25(boolean z, float f) {
        this.mExpandedViewAlphaAnimator.start();
        PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).cancel();
        PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).spring(AnimatableScaleMatrix.SCALE_X, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(1.0f), this.mScaleInSpringConfig).spring(AnimatableScaleMatrix.SCALE_Y, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(1.0f), this.mScaleInSpringConfig).addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda41(this, z, f)).withEndActions(new BubbleStackView$$ExternalSyntheticLambda42(this)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateExpansion$23(boolean z, float f, AnimatableScaleMatrix animatableScaleMatrix, ArrayMap arrayMap) {
        float f2;
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider != null && bubbleViewProvider.getIconView() != null) {
            if (z) {
                f2 = this.mExpandedBubble.getIconView().getTranslationY();
            } else {
                f2 = this.mExpandedBubble.getIconView().getTranslationX();
            }
            this.mExpandedViewContainerMatrix.postTranslate(f2 - f, 0.0f);
            this.mExpandedViewContainer.setAnimationMatrix(this.mExpandedViewContainerMatrix);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateExpansion$24() {
        this.mExpandedViewContainer.setAnimationMatrix((Matrix) null);
        afterExpandedViewAnimation();
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider != null && bubbleViewProvider.getExpandedView() != null) {
            this.mExpandedBubble.getExpandedView().setSurfaceZOrderedOnTop(false);
        }
    }

    public final void animateCollapse() {
        int i;
        int i2;
        cancelDelayedExpandCollapseSwitchAnimations();
        ManageEducationView manageEducationView = this.mManageEduView;
        if (manageEducationView != null && manageEducationView.getVisibility() == 0) {
            this.mManageEduView.hide();
        }
        showManageMenu(false);
        this.mIsExpanded = false;
        this.mIsExpansionAnimating = true;
        showScrim(false);
        this.mBubbleContainer.cancelAllAnimations();
        PhysicsAnimator.getInstance(this.mAnimatingOutSurfaceContainer).cancel();
        this.mAnimatingOutSurfaceContainer.setScaleX(0.0f);
        this.mAnimatingOutSurfaceContainer.setScaleY(0.0f);
        this.mExpandedAnimationController.notifyPreparingToCollapse();
        this.mExpandedAnimationController.collapseBackToStack(this.mStackAnimationController.getStackPositionAlongNearestHorizontalEdge(), new BubbleStackView$$ExternalSyntheticLambda29(this));
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider == null || !"Overflow".equals(bubbleViewProvider.getKey())) {
            i = this.mBubbleData.getBubbles().indexOf(this.mExpandedBubble);
        } else {
            i = this.mBubbleData.getBubbles().size();
        }
        PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(i, getState());
        if (this.mPositioner.showBubblesVertically()) {
            float f = expandedBubbleXY.y + (((float) this.mBubbleSize) / 2.0f);
            if (this.mStackOnLeftOrWillBe) {
                i2 = this.mPositioner.getAvailableRect().left + this.mBubbleSize + this.mExpandedViewPadding;
            } else {
                i2 = (this.mPositioner.getAvailableRect().right - this.mBubbleSize) - this.mExpandedViewPadding;
            }
            this.mExpandedViewContainerMatrix.setScale(1.0f, 1.0f, (float) i2, f);
        } else {
            AnimatableScaleMatrix animatableScaleMatrix = this.mExpandedViewContainerMatrix;
            float f2 = expandedBubbleXY.x;
            int i3 = this.mBubbleSize;
            animatableScaleMatrix.setScale(1.0f, 1.0f, f2 + (((float) i3) / 2.0f), expandedBubbleXY.y + ((float) i3) + ((float) this.mExpandedViewPadding));
        }
        this.mExpandedViewAlphaAnimator.reverse();
        if (this.mExpandedBubble.getExpandedView() != null) {
            this.mExpandedBubble.getExpandedView().setContentVisibility(false);
        }
        PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).cancel();
        PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).spring(AnimatableScaleMatrix.SCALE_X, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(0.9f), this.mScaleOutSpringConfig).spring(AnimatableScaleMatrix.SCALE_Y, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(0.9f), this.mScaleOutSpringConfig).addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda30(this)).withEndActions(new BubbleStackView$$ExternalSyntheticLambda31(this)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateCollapse$26() {
        this.mBubbleContainer.setActiveController(this.mStackAnimationController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateCollapse$27(AnimatableScaleMatrix animatableScaleMatrix, ArrayMap arrayMap) {
        this.mExpandedViewContainer.setAnimationMatrix(this.mExpandedViewContainerMatrix);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateCollapse$28() {
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        beforeExpandedViewAnimation();
        ManageEducationView manageEducationView = this.mManageEduView;
        if (manageEducationView != null) {
            manageEducationView.hide();
        }
        updateOverflowVisibility();
        updateZOrder();
        updateBadges(true);
        afterExpandedViewAnimation();
        if (bubbleViewProvider != null) {
            bubbleViewProvider.setTaskViewVisibility(false);
        }
    }

    public final void animateSwitchBubbles() {
        int i;
        float f;
        float f2;
        if (!this.mIsExpanded) {
            this.mIsBubbleSwitchAnimating = false;
            return;
        }
        PhysicsAnimator.getInstance(this.mAnimatingOutSurfaceContainer).cancel();
        this.mAnimatingOutSurfaceAlphaAnimator.reverse();
        this.mExpandedViewAlphaAnimator.start();
        if (this.mPositioner.showBubblesVertically()) {
            if (this.mStackAnimationController.isStackOnLeftSide()) {
                f2 = this.mAnimatingOutSurfaceContainer.getTranslationX() + ((float) (this.mBubbleSize * 2));
            } else {
                f2 = this.mAnimatingOutSurfaceContainer.getTranslationX();
            }
            PhysicsAnimator.getInstance(this.mAnimatingOutSurfaceContainer).spring(DynamicAnimation.TRANSLATION_X, f2, this.mTranslateSpringConfig).start();
        } else {
            PhysicsAnimator.getInstance(this.mAnimatingOutSurfaceContainer).spring(DynamicAnimation.TRANSLATION_Y, this.mAnimatingOutSurfaceContainer.getTranslationY() - ((float) this.mBubbleSize), this.mTranslateSpringConfig).start();
        }
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        boolean z = bubbleViewProvider != null && bubbleViewProvider.getKey().equals("Overflow");
        BubblePositioner bubblePositioner = this.mPositioner;
        if (z) {
            i = this.mBubbleContainer.getChildCount() - 1;
        } else {
            i = this.mBubbleData.getBubbles().indexOf(this.mExpandedBubble);
        }
        PointF expandedBubbleXY = bubblePositioner.getExpandedBubbleXY(i, getState());
        this.mExpandedViewContainer.setAlpha(1.0f);
        this.mExpandedViewContainer.setVisibility(0);
        if (this.mPositioner.showBubblesVertically()) {
            float f3 = expandedBubbleXY.y;
            int i2 = this.mBubbleSize;
            float f4 = f3 + (((float) i2) / 2.0f);
            if (this.mStackOnLeftOrWillBe) {
                f = expandedBubbleXY.x + ((float) i2) + ((float) this.mExpandedViewPadding);
            } else {
                f = expandedBubbleXY.x - ((float) this.mExpandedViewPadding);
            }
            this.mExpandedViewContainerMatrix.setScale(0.9f, 0.9f, f, f4);
        } else {
            AnimatableScaleMatrix animatableScaleMatrix = this.mExpandedViewContainerMatrix;
            float f5 = expandedBubbleXY.x;
            int i3 = this.mBubbleSize;
            animatableScaleMatrix.setScale(0.9f, 0.9f, f5 + (((float) i3) / 2.0f), expandedBubbleXY.y + ((float) i3) + ((float) this.mExpandedViewPadding));
        }
        this.mExpandedViewContainer.setAnimationMatrix(this.mExpandedViewContainerMatrix);
        this.mDelayedAnimationExecutor.executeDelayed(new BubbleStackView$$ExternalSyntheticLambda47(this), 25);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateSwitchBubbles$31() {
        if (!this.mIsExpanded) {
            this.mIsBubbleSwitchAnimating = false;
            return;
        }
        PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).cancel();
        PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).spring(AnimatableScaleMatrix.SCALE_X, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(1.0f), this.mScaleInSpringConfig).spring(AnimatableScaleMatrix.SCALE_Y, AnimatableScaleMatrix.getAnimatableValueForScaleFactor(1.0f), this.mScaleInSpringConfig).addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda48(this)).withEndActions(new BubbleStackView$$ExternalSyntheticLambda49(this)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateSwitchBubbles$29(AnimatableScaleMatrix animatableScaleMatrix, ArrayMap arrayMap) {
        this.mExpandedViewContainer.setAnimationMatrix(this.mExpandedViewContainerMatrix);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateSwitchBubbles$30() {
        this.mExpandedViewTemporarilyHidden = false;
        this.mIsBubbleSwitchAnimating = false;
        this.mExpandedViewContainer.setAnimationMatrix((Matrix) null);
    }

    public final void cancelDelayedExpandCollapseSwitchAnimations() {
        this.mDelayedAnimationExecutor.removeCallbacks(this.mDelayedAnimation);
        this.mIsExpansionAnimating = false;
        this.mIsBubbleSwitchAnimating = false;
    }

    public final void cancelAllExpandCollapseSwitchAnimations() {
        cancelDelayedExpandCollapseSwitchAnimations();
        PhysicsAnimator.getInstance(this.mAnimatingOutSurfaceView).cancel();
        PhysicsAnimator.getInstance(this.mExpandedViewContainerMatrix).cancel();
        this.mExpandedViewContainer.setAnimationMatrix((Matrix) null);
    }

    public final void notifyExpansionChanged(BubbleViewProvider bubbleViewProvider, boolean z) {
        Bubbles.BubbleExpandListener bubbleExpandListener = this.mExpandListener;
        if (bubbleExpandListener != null && bubbleViewProvider != null) {
            bubbleExpandListener.onBubbleExpandChanged(z, bubbleViewProvider.getKey());
        }
    }

    public void animateForIme(boolean z) {
        BubbleViewProvider bubbleViewProvider;
        if ((this.mIsExpansionAnimating || this.mIsBubbleSwitchAnimating) && this.mIsExpanded) {
            this.mExpandedAnimationController.expandFromStack(new BubbleStackView$$ExternalSyntheticLambda44(this));
        } else if (!this.mIsExpanded && getBubbleCount() > 0) {
            float animateForImeVisibility = this.mStackAnimationController.animateForImeVisibility(z) - this.mStackAnimationController.getStackPosition().y;
            if (this.mFlyout.getVisibility() == 0) {
                PhysicsAnimator.getInstance(this.mFlyout).spring(DynamicAnimation.TRANSLATION_Y, this.mFlyout.getTranslationY() + animateForImeVisibility, FLYOUT_IME_ANIMATION_SPRING_CONFIG).start();
            }
        } else if (this.mPositioner.showBubblesVertically() && this.mIsExpanded && (bubbleViewProvider = this.mExpandedBubble) != null && bubbleViewProvider.getExpandedView() != null) {
            float expandedViewY = this.mPositioner.getExpandedViewY(this.mExpandedBubble, this.mPositioner.getExpandedBubbleXY(getState().selectedIndex, getState()).y);
            this.mExpandedBubble.getExpandedView().setImeVisible(z);
            if (!this.mExpandedBubble.getExpandedView().isUsingMaxHeight()) {
                this.mExpandedViewContainer.animate().translationY(expandedViewY);
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.mBubbleContainer.getChildCount(); i++) {
                arrayList.add(ObjectAnimator.ofFloat(this.mBubbleContainer.getChildAt(i), FrameLayout.TRANSLATION_Y, new float[]{this.mPositioner.getExpandedBubbleXY(i, getState()).y}));
            }
            updatePointerPosition(true);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(arrayList);
            animatorSet.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateForIme$32() {
        updatePointerPosition(false);
        afterExpandedViewAnimation();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (motionEvent.getAction() != 0 && motionEvent.getActionIndex() != this.mPointerIndexDown) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            this.mPointerIndexDown = motionEvent.getActionIndex();
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.mPointerIndexDown = -1;
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (!dispatchTouchEvent && !this.mIsExpanded && this.mIsGestureInProgress) {
            dispatchTouchEvent = this.mBubbleTouchListener.onTouch(this, motionEvent);
        }
        if (!(motionEvent.getAction() == 1 || motionEvent.getAction() == 3)) {
            z = true;
        }
        this.mIsGestureInProgress = z;
        return dispatchTouchEvent;
    }

    public void setFlyoutStateForDragLength(float f) {
        if (this.mFlyout.getWidth() > 0) {
            boolean isStackOnLeftSide = this.mStackAnimationController.isStackOnLeftSide();
            this.mFlyoutDragDeltaX = f;
            if (isStackOnLeftSide) {
                f = -f;
            }
            float width = f / ((float) this.mFlyout.getWidth());
            float f2 = 0.0f;
            this.mFlyout.setCollapsePercent(Math.min(1.0f, Math.max(0.0f, width)));
            int i = (width > 0.0f ? 1 : (width == 0.0f ? 0 : -1));
            if (i < 0 || width > 1.0f) {
                int i2 = (width > 1.0f ? 1 : (width == 1.0f ? 0 : -1));
                boolean z = false;
                int i3 = 1;
                boolean z2 = i2 > 0;
                if ((isStackOnLeftSide && i2 > 0) || (!isStackOnLeftSide && i < 0)) {
                    z = true;
                }
                float f3 = (z2 ? width - 1.0f : width * -1.0f) * ((float) (z ? -1 : 1));
                float width2 = (float) this.mFlyout.getWidth();
                if (z2) {
                    i3 = 2;
                }
                f2 = f3 * (width2 / (8.0f / ((float) i3)));
            }
            BubbleFlyoutView bubbleFlyoutView = this.mFlyout;
            bubbleFlyoutView.setTranslationX(bubbleFlyoutView.getRestingTranslationX() + f2);
        }
    }

    public final boolean passEventToMagnetizedObject(MotionEvent motionEvent) {
        MagnetizedObject<?> magnetizedObject = this.mMagnetizedObject;
        return magnetizedObject != null && magnetizedObject.maybeConsumeMotionEvent(motionEvent);
    }

    public final void dismissMagnetizedObject() {
        if (this.mIsExpanded) {
            dismissBubbleIfExists(this.mBubbleData.getBubbleWithView((View) this.mMagnetizedObject.getUnderlyingObject()));
            return;
        }
        this.mBubbleData.dismissAll(1);
    }

    public final void dismissBubbleIfExists(BubbleViewProvider bubbleViewProvider) {
        if (bubbleViewProvider != null && this.mBubbleData.hasBubbleInStackWithKey(bubbleViewProvider.getKey())) {
            if (this.mIsExpanded && this.mBubbleData.getBubbles().size() > 1) {
                this.mIsBubbleSwitchAnimating = true;
            }
            this.mBubbleData.dismissBubbleWithKey(bubbleViewProvider.getKey(), 1);
        }
    }

    public final void animateDismissBubble(View view, boolean z) {
        this.mViewBeingDismissed = view;
        if (view != null) {
            if (z) {
                this.mDismissBubbleAnimator.removeAllListeners();
                this.mDismissBubbleAnimator.start();
                return;
            }
            this.mDismissBubbleAnimator.removeAllListeners();
            this.mDismissBubbleAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    BubbleStackView.this.resetDismissAnimator();
                }

                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    BubbleStackView.this.resetDismissAnimator();
                }
            });
            this.mDismissBubbleAnimator.reverse();
        }
    }

    public final void resetDismissAnimator() {
        this.mDismissBubbleAnimator.removeAllListeners();
        this.mDismissBubbleAnimator.cancel();
        View view = this.mViewBeingDismissed;
        if (view != null) {
            view.setAlpha(1.0f);
            this.mViewBeingDismissed = null;
        }
        DismissView dismissView = this.mDismissView;
        if (dismissView != null) {
            dismissView.getCircle().setScaleX(1.0f);
            this.mDismissView.getCircle().setScaleY(1.0f);
        }
    }

    public final void animateFlyoutCollapsed(boolean z, float f) {
        float f2;
        boolean isStackOnLeftSide = this.mStackAnimationController.isStackOnLeftSide();
        this.mFlyoutTransitionSpring.getSpring().setStiffness(this.mBubbleToExpandAfterFlyoutCollapse != null ? 1500.0f : 200.0f);
        SpringAnimation springAnimation = (SpringAnimation) ((SpringAnimation) this.mFlyoutTransitionSpring.setStartValue(this.mFlyoutDragDeltaX)).setStartVelocity(f);
        if (z) {
            int width = this.mFlyout.getWidth();
            if (isStackOnLeftSide) {
                width = -width;
            }
            f2 = (float) width;
        } else {
            f2 = 0.0f;
        }
        springAnimation.animateToFinalPosition(f2);
    }

    public final boolean shouldShowFlyout(Bubble bubble) {
        Bubble.FlyoutMessage flyoutMessage = bubble.getFlyoutMessage();
        BadgedImageView iconView = bubble.getIconView();
        if (flyoutMessage != null && flyoutMessage.message != null && bubble.showFlyout() && !isStackEduShowing() && !isExpanded() && !this.mIsExpansionAnimating && !this.mIsGestureInProgress && this.mBubbleToExpandAfterFlyoutCollapse == null && iconView != null) {
            return true;
        }
        if (iconView == null || this.mFlyout.getVisibility() == 0) {
            return false;
        }
        iconView.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        return false;
    }

    @VisibleForTesting
    public void animateInFlyoutForBubble(Bubble bubble) {
        if (shouldShowFlyout(bubble)) {
            this.mFlyoutDragDeltaX = 0.0f;
            clearFlyoutOnHide();
            this.mAfterFlyoutHidden = new BubbleStackView$$ExternalSyntheticLambda0(this, bubble);
            bubble.getIconView().addDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
            post(new BubbleStackView$$ExternalSyntheticLambda1(this, bubble));
            this.mFlyout.removeCallbacks(this.mHideFlyout);
            this.mFlyout.postDelayed(this.mHideFlyout, 5000);
            logBubbleEvent(bubble, 16);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateInFlyoutForBubble$33(Bubble bubble) {
        this.mAfterFlyoutHidden = null;
        BubbleViewProvider bubbleViewProvider = this.mBubbleToExpandAfterFlyoutCollapse;
        if (bubbleViewProvider != null) {
            this.mBubbleData.setSelectedBubble(bubbleViewProvider);
            this.mBubbleData.setExpanded(true);
            this.mBubbleToExpandAfterFlyoutCollapse = null;
        }
        if (bubble.getIconView() != null) {
            bubble.getIconView().removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        }
        updateTemporarilyInvisibleAnimation(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateInFlyoutForBubble$36(Bubble bubble) {
        if (!isExpanded() && bubble.getIconView() != null) {
            BubbleStackView$$ExternalSyntheticLambda5 bubbleStackView$$ExternalSyntheticLambda5 = new BubbleStackView$$ExternalSyntheticLambda5(this);
            if (this.mFlyout.getVisibility() == 0) {
                this.mFlyout.animateUpdate(bubble.getFlyoutMessage(), this.mStackAnimationController.getStackPosition(), !bubble.showDot(), bubble.getIconView().getDotCenter(), this.mAfterFlyoutHidden);
            } else {
                this.mFlyout.setVisibility(4);
                this.mFlyout.setupFlyoutStartingAsDot(bubble.getFlyoutMessage(), this.mStackAnimationController.getStackPosition(), this.mStackAnimationController.isStackOnLeftSide(), bubble.getIconView().getDotColor(), bubbleStackView$$ExternalSyntheticLambda5, this.mAfterFlyoutHidden, bubble.getIconView().getDotCenter(), !bubble.showDot());
            }
            this.mFlyout.bringToFront();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateInFlyoutForBubble$35() {
        BubbleStackView$$ExternalSyntheticLambda6 bubbleStackView$$ExternalSyntheticLambda6 = new BubbleStackView$$ExternalSyntheticLambda6(this);
        this.mAnimateInFlyout = bubbleStackView$$ExternalSyntheticLambda6;
        this.mFlyout.postDelayed(bubbleStackView$$ExternalSyntheticLambda6, 200);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateInFlyoutForBubble$34() {
        int i;
        this.mFlyout.setVisibility(0);
        updateTemporarilyInvisibleAnimation(false);
        if (this.mStackAnimationController.isStackOnLeftSide()) {
            i = -this.mFlyout.getWidth();
        } else {
            i = this.mFlyout.getWidth();
        }
        this.mFlyoutDragDeltaX = (float) i;
        animateFlyoutCollapsed(false, 0.0f);
        this.mFlyout.postDelayed(this.mHideFlyout, 5000);
    }

    public final void hideFlyoutImmediate() {
        clearFlyoutOnHide();
        this.mFlyout.removeCallbacks(this.mAnimateInFlyout);
        this.mFlyout.removeCallbacks(this.mHideFlyout);
        this.mFlyout.hideFlyout();
    }

    public final void clearFlyoutOnHide() {
        this.mFlyout.removeCallbacks(this.mAnimateInFlyout);
        Runnable runnable = this.mAfterFlyoutHidden;
        if (runnable != null) {
            runnable.run();
            this.mAfterFlyoutHidden = null;
        }
    }

    public void getTouchableRegion(Rect rect) {
        if (isStackEduShowing()) {
            rect.set(0, 0, getWidth(), getHeight());
            return;
        }
        if (this.mIsExpanded) {
            this.mBubbleContainer.getBoundsOnScreen(rect);
            rect.bottom -= this.mPositioner.getImeHeight();
        } else if (getBubbleCount() > 0 || this.mBubbleData.isShowingOverflow()) {
            this.mBubbleContainer.getChildAt(0).getBoundsOnScreen(rect);
            int i = rect.top;
            int i2 = this.mBubbleTouchPadding;
            rect.top = i - i2;
            rect.left -= i2;
            rect.right += i2;
            rect.bottom += i2;
        }
        if (this.mFlyout.getVisibility() == 0) {
            Rect rect2 = new Rect();
            this.mFlyout.getBoundsOnScreen(rect2);
            rect.union(rect2);
        }
    }

    public final void requestUpdate() {
        if (!this.mViewUpdatedRequested && !this.mIsExpansionAnimating) {
            this.mViewUpdatedRequested = true;
            getViewTreeObserver().addOnPreDrawListener(this.mViewUpdater);
            invalidate();
        }
    }

    @VisibleForTesting
    public void showManageMenu(boolean z) {
        float f;
        Bubble bubbleInStackWithKey;
        this.mShowingManage = z;
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider == null || bubbleViewProvider.getExpandedView() == null) {
            this.mManageMenu.setVisibility(4);
            this.mManageMenuScrim.setVisibility(4);
            this.mBubbleController.getSysuiProxy().onManageMenuExpandChanged(false);
            return;
        }
        if (z) {
            this.mManageMenuScrim.setVisibility(0);
            this.mManageMenuScrim.setTranslationZ(this.mManageMenu.getElevation() - 1.0f);
        }
        BubbleStackView$$ExternalSyntheticLambda2 bubbleStackView$$ExternalSyntheticLambda2 = new BubbleStackView$$ExternalSyntheticLambda2(this, z);
        this.mBubbleController.getSysuiProxy().onManageMenuExpandChanged(z);
        this.mManageMenuScrim.animate().setInterpolator(z ? Interpolators.ALPHA_IN : Interpolators.ALPHA_OUT).alpha(z ? 0.6f : 0.0f).withEndAction(bubbleStackView$$ExternalSyntheticLambda2).start();
        if (z && (bubbleInStackWithKey = this.mBubbleData.getBubbleInStackWithKey(this.mExpandedBubble.getKey())) != null) {
            this.mManageSettingsIcon.setImageBitmap(bubbleInStackWithKey.getRawAppBadge());
            this.mManageSettingsText.setText(getResources().getString(R.string.bubbles_app_settings, new Object[]{bubbleInStackWithKey.getAppName()}));
        }
        if (this.mExpandedBubble.getExpandedView().getTaskView() != null) {
            this.mExpandedBubble.getExpandedView().getTaskView().setObscuredTouchRect(this.mShowingManage ? new Rect(0, 0, getWidth(), getHeight()) : null);
        }
        boolean z2 = getResources().getConfiguration().getLayoutDirection() == 0;
        this.mExpandedBubble.getExpandedView().getManageButtonBoundsOnScreen(this.mTempRect);
        float manageButtonMargin = (float) this.mExpandedBubble.getExpandedView().getManageButtonMargin();
        if (z2) {
            f = (float) this.mTempRect.left;
        } else {
            f = ((float) this.mTempRect.right) + manageButtonMargin;
            manageButtonMargin = (float) this.mManageMenu.getWidth();
        }
        float f2 = f - manageButtonMargin;
        float height = (float) (this.mTempRect.bottom - this.mManageMenu.getHeight());
        float width = ((float) ((z2 ? 1 : -1) * this.mManageMenu.getWidth())) / 4.0f;
        if (z) {
            this.mManageMenu.setScaleX(0.5f);
            this.mManageMenu.setScaleY(0.5f);
            this.mManageMenu.setTranslationX(f2 - width);
            ViewGroup viewGroup = this.mManageMenu;
            viewGroup.setTranslationY((((float) viewGroup.getHeight()) / 4.0f) + height);
            this.mManageMenu.setAlpha(0.0f);
            PhysicsAnimator.getInstance(this.mManageMenu).spring(DynamicAnimation.ALPHA, 1.0f).spring(DynamicAnimation.SCALE_X, 1.0f).spring(DynamicAnimation.SCALE_Y, 1.0f).spring(DynamicAnimation.TRANSLATION_X, f2).spring(DynamicAnimation.TRANSLATION_Y, height).withEndActions(new BubbleStackView$$ExternalSyntheticLambda3(this)).start();
            this.mManageMenu.setVisibility(0);
            return;
        }
        PhysicsAnimator.getInstance(this.mManageMenu).spring(DynamicAnimation.ALPHA, 0.0f).spring(DynamicAnimation.SCALE_X, 0.5f).spring(DynamicAnimation.SCALE_Y, 0.5f).spring(DynamicAnimation.TRANSLATION_X, f2 - width).spring(DynamicAnimation.TRANSLATION_Y, height + (((float) this.mManageMenu.getHeight()) / 4.0f)).withEndActions(new BubbleStackView$$ExternalSyntheticLambda4(this)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showManageMenu$37(boolean z) {
        if (!z) {
            this.mManageMenuScrim.setVisibility(4);
            this.mManageMenuScrim.setTranslationZ(0.0f);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showManageMenu$38() {
        this.mManageMenu.getChildAt(0).requestAccessibilityFocus();
        this.mExpandedBubble.getExpandedView().updateObscuredTouchableRegion();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showManageMenu$39() {
        this.mManageMenu.setVisibility(4);
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider != null && bubbleViewProvider.getExpandedView() != null) {
            this.mExpandedBubble.getExpandedView().updateObscuredTouchableRegion();
        }
    }

    public final void updateExpandedBubble() {
        BubbleViewProvider bubbleViewProvider;
        this.mExpandedViewContainer.removeAllViews();
        if (this.mIsExpanded && (bubbleViewProvider = this.mExpandedBubble) != null && bubbleViewProvider.getExpandedView() != null) {
            BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
            expandedView.setContentVisibility(false);
            expandedView.setAlphaAnimating(!this.mIsExpansionAnimating);
            this.mExpandedViewContainerMatrix.setScaleX(0.0f);
            this.mExpandedViewContainerMatrix.setScaleY(0.0f);
            this.mExpandedViewContainerMatrix.setTranslate(0.0f, 0.0f);
            this.mExpandedViewContainer.setVisibility(4);
            this.mExpandedViewContainer.setAlpha(0.0f);
            this.mExpandedViewContainer.addView(expandedView);
            postDelayed(new BubbleStackView$$ExternalSyntheticLambda39(this), 0);
            if (!this.mIsExpansionAnimating) {
                this.mIsBubbleSwitchAnimating = true;
                this.mSurfaceSynchronizer.syncSurfaceAndRun(new BubbleStackView$$ExternalSyntheticLambda40(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateExpandedBubble$41() {
        post(new BubbleStackView$$ExternalSyntheticLambda46(this));
    }

    /* renamed from: updateManageButtonListener */
    public final void lambda$updateExpandedBubble$40() {
        BubbleViewProvider bubbleViewProvider;
        if (this.mIsExpanded && (bubbleViewProvider = this.mExpandedBubble) != null && bubbleViewProvider.getExpandedView() != null) {
            this.mExpandedBubble.getExpandedView().setManageClickListener(new BubbleStackView$$ExternalSyntheticLambda37(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateManageButtonListener$42(View view) {
        showManageMenu(true);
    }

    public final void screenshotAnimatingOutBubbleIntoSurface(Consumer<Boolean> consumer) {
        BubbleViewProvider bubbleViewProvider;
        int i;
        if (!this.mIsExpanded || (bubbleViewProvider = this.mExpandedBubble) == null || bubbleViewProvider.getExpandedView() == null) {
            consumer.accept(Boolean.FALSE);
            return;
        }
        BubbleExpandedView expandedView = this.mExpandedBubble.getExpandedView();
        if (this.mAnimatingOutBubbleBuffer != null) {
            releaseAnimatingOutBubbleBuffer();
        }
        try {
            this.mAnimatingOutBubbleBuffer = expandedView.snapshotActivitySurface();
        } catch (Exception e) {
            Log.wtf("Bubbles", e);
            consumer.accept(Boolean.FALSE);
        }
        SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer = this.mAnimatingOutBubbleBuffer;
        if (screenshotHardwareBuffer == null || screenshotHardwareBuffer.getHardwareBuffer() == null) {
            consumer.accept(Boolean.FALSE);
            return;
        }
        PhysicsAnimator.getInstance(this.mAnimatingOutSurfaceContainer).cancel();
        this.mAnimatingOutSurfaceContainer.setScaleX(1.0f);
        this.mAnimatingOutSurfaceContainer.setScaleY(1.0f);
        if (!this.mPositioner.showBubblesVertically() || !this.mStackOnLeftOrWillBe) {
            i = this.mExpandedViewContainer.getPaddingLeft();
        } else {
            i = this.mExpandedViewContainer.getPaddingLeft() + this.mPositioner.getPointerSize();
        }
        this.mAnimatingOutSurfaceContainer.setTranslationX((float) i);
        this.mAnimatingOutSurfaceContainer.setTranslationY(0.0f);
        this.mAnimatingOutSurfaceContainer.setTranslationY((float) (this.mExpandedBubble.getExpandedView().getTaskViewLocationOnScreen()[1] - this.mAnimatingOutSurfaceView.getLocationOnScreen()[1]));
        this.mAnimatingOutSurfaceView.getLayoutParams().width = this.mAnimatingOutBubbleBuffer.getHardwareBuffer().getWidth();
        this.mAnimatingOutSurfaceView.getLayoutParams().height = this.mAnimatingOutBubbleBuffer.getHardwareBuffer().getHeight();
        this.mAnimatingOutSurfaceView.requestLayout();
        post(new BubbleStackView$$ExternalSyntheticLambda32(this, consumer));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$screenshotAnimatingOutBubbleIntoSurface$45(Consumer consumer) {
        SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer = this.mAnimatingOutBubbleBuffer;
        if (screenshotHardwareBuffer == null || screenshotHardwareBuffer.getHardwareBuffer() == null || this.mAnimatingOutBubbleBuffer.getHardwareBuffer().isClosed()) {
            consumer.accept(Boolean.FALSE);
        } else if (!this.mIsExpanded || !this.mAnimatingOutSurfaceReady) {
            consumer.accept(Boolean.FALSE);
        } else {
            this.mAnimatingOutSurfaceView.getHolder().getSurface().attachAndQueueBufferWithColorSpace(this.mAnimatingOutBubbleBuffer.getHardwareBuffer(), this.mAnimatingOutBubbleBuffer.getColorSpace());
            this.mAnimatingOutSurfaceView.setAlpha(1.0f);
            this.mExpandedViewContainer.setVisibility(8);
            this.mSurfaceSynchronizer.syncSurfaceAndRun(new BubbleStackView$$ExternalSyntheticLambda38(this, consumer));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$screenshotAnimatingOutBubbleIntoSurface$44(Consumer consumer) {
        post(new BubbleStackView$$ExternalSyntheticLambda45(consumer));
    }

    public final void releaseAnimatingOutBubbleBuffer() {
        SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer = this.mAnimatingOutBubbleBuffer;
        if (screenshotHardwareBuffer != null && !screenshotHardwareBuffer.getHardwareBuffer().isClosed()) {
            this.mAnimatingOutBubbleBuffer.getHardwareBuffer().close();
        }
    }

    public final void updateExpandedView() {
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        int[] expandedViewContainerPadding = this.mPositioner.getExpandedViewContainerPadding(this.mStackAnimationController.isStackOnLeftSide(), bubbleViewProvider != null && "Overflow".equals(bubbleViewProvider.getKey()));
        this.mExpandedViewContainer.setPadding(expandedViewContainerPadding[0], expandedViewContainerPadding[1], expandedViewContainerPadding[2], expandedViewContainerPadding[3]);
        if (this.mIsExpansionAnimating) {
            this.mExpandedViewContainer.setVisibility(this.mIsExpanded ? 0 : 8);
        }
        BubbleViewProvider bubbleViewProvider2 = this.mExpandedBubble;
        if (!(bubbleViewProvider2 == null || bubbleViewProvider2.getExpandedView() == null)) {
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(getBubbleIndex(this.mExpandedBubble), getState());
            FrameLayout frameLayout = this.mExpandedViewContainer;
            BubblePositioner bubblePositioner = this.mPositioner;
            frameLayout.setTranslationY(bubblePositioner.getExpandedViewY(this.mExpandedBubble, bubblePositioner.showBubblesVertically() ? expandedBubbleXY.y : expandedBubbleXY.x));
            this.mExpandedViewContainer.setTranslationX(0.0f);
            this.mExpandedBubble.getExpandedView().updateView(this.mExpandedViewContainer.getLocationOnScreen());
            updatePointerPosition(false);
        }
        this.mStackOnLeftOrWillBe = this.mStackAnimationController.isStackOnLeftSide();
    }

    public final void updateBubbleShadows(boolean z) {
        int bubbleCount = getBubbleCount();
        for (int i = 0; i < bubbleCount; i++) {
            float maxBubbles = (float) ((this.mPositioner.getMaxBubbles() * this.mBubbleElevation) - i);
            BadgedImageView badgedImageView = (BadgedImageView) this.mBubbleContainer.getChildAt(i);
            MagnetizedObject<?> magnetizedObject = this.mMagnetizedObject;
            boolean z2 = magnetizedObject != null && magnetizedObject.getUnderlyingObject().equals(badgedImageView);
            if (z || z2) {
                badgedImageView.setZ(maxBubbles);
            } else {
                if (i >= 2) {
                    maxBubbles = 0.0f;
                }
                badgedImageView.setZ(maxBubbles);
            }
        }
    }

    public final void animateShadows() {
        int bubbleCount = getBubbleCount();
        int i = 0;
        while (i < bubbleCount) {
            BadgedImageView badgedImageView = (BadgedImageView) this.mBubbleContainer.getChildAt(i);
            if (!(i < 2)) {
                badgedImageView.animate().translationZ(0.0f).start();
            }
            i++;
        }
    }

    public final void updateZOrder() {
        int bubbleCount = getBubbleCount();
        int i = 0;
        while (i < bubbleCount) {
            ((BadgedImageView) this.mBubbleContainer.getChildAt(i)).setZ(i < 2 ? (float) ((this.mPositioner.getMaxBubbles() * this.mBubbleElevation) - i) : 0.0f);
            i++;
        }
    }

    public final void updateBadges(boolean z) {
        int bubbleCount = getBubbleCount();
        for (int i = 0; i < bubbleCount; i++) {
            BadgedImageView badgedImageView = (BadgedImageView) this.mBubbleContainer.getChildAt(i);
            boolean z2 = true;
            if (this.mIsExpanded) {
                if (!this.mPositioner.showBubblesVertically() || this.mStackOnLeftOrWillBe) {
                    z2 = false;
                }
                badgedImageView.showDotAndBadge(z2);
            } else if (z) {
                if (i == 0) {
                    badgedImageView.showDotAndBadge(!this.mStackOnLeftOrWillBe);
                } else {
                    badgedImageView.hideDotAndBadge(!this.mStackOnLeftOrWillBe);
                }
            }
        }
    }

    public final void updatePointerPosition(boolean z) {
        int bubbleIndex;
        float f;
        BubbleViewProvider bubbleViewProvider = this.mExpandedBubble;
        if (bubbleViewProvider != null && bubbleViewProvider.getExpandedView() != null && (bubbleIndex = getBubbleIndex(this.mExpandedBubble)) != -1) {
            PointF expandedBubbleXY = this.mPositioner.getExpandedBubbleXY(bubbleIndex, getState());
            if (this.mPositioner.showBubblesVertically()) {
                f = expandedBubbleXY.y;
            } else {
                f = expandedBubbleXY.x;
            }
            this.mExpandedBubble.getExpandedView().setPointerPosition(f, this.mStackOnLeftOrWillBe, z);
        }
    }

    public int getBubbleCount() {
        return this.mBubbleContainer.getChildCount() - 1;
    }

    public int getBubbleIndex(BubbleViewProvider bubbleViewProvider) {
        if (bubbleViewProvider == null) {
            return 0;
        }
        return this.mBubbleContainer.indexOfChild(bubbleViewProvider.getIconView());
    }

    public float getNormalizedXPosition() {
        BigDecimal bigDecimal = new BigDecimal((double) (getStackPosition().x / ((float) this.mPositioner.getAvailableRect().width())));
        RoundingMode roundingMode = RoundingMode.CEILING;
        return bigDecimal.setScale(4, RoundingMode.HALF_UP).floatValue();
    }

    public float getNormalizedYPosition() {
        BigDecimal bigDecimal = new BigDecimal((double) (getStackPosition().y / ((float) this.mPositioner.getAvailableRect().height())));
        RoundingMode roundingMode = RoundingMode.CEILING;
        return bigDecimal.setScale(4, RoundingMode.HALF_UP).floatValue();
    }

    public PointF getStackPosition() {
        return this.mStackAnimationController.getStackPosition();
    }

    public final void logBubbleEvent(BubbleViewProvider bubbleViewProvider, int i) {
        this.mBubbleData.logBubbleEvent(bubbleViewProvider, i, (bubbleViewProvider == null || !(bubbleViewProvider instanceof Bubble)) ? "null" : ((Bubble) bubbleViewProvider).getPackageName(), getBubbleCount(), getBubbleIndex(bubbleViewProvider), getNormalizedXPosition(), getNormalizedYPosition());
    }

    public List<Bubble> getBubblesOnScreen() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < getBubbleCount(); i++) {
            View childAt = this.mBubbleContainer.getChildAt(i);
            if (childAt instanceof BadgedImageView) {
                arrayList.add(this.mBubbleData.getBubbleInStackWithKey(((BadgedImageView) childAt).getKey()));
            }
        }
        return arrayList;
    }

    public StackViewState getState() {
        this.mStackViewState.numberOfBubbles = this.mBubbleContainer.getChildCount();
        this.mStackViewState.selectedIndex = getBubbleIndex(this.mExpandedBubble);
        StackViewState stackViewState = this.mStackViewState;
        stackViewState.onLeft = this.mStackOnLeftOrWillBe;
        return stackViewState;
    }

    public void onVerticalOffsetChanged(int i) {
        this.mDismissView.setPadding(0, 0, 0, i);
    }

    public static class RelativeStackPosition {
        public boolean mOnLeft;
        public float mVerticalOffsetPercent;

        public RelativeStackPosition(boolean z, float f) {
            this.mOnLeft = z;
            this.mVerticalOffsetPercent = clampVerticalOffsetPercent(f);
        }

        public RelativeStackPosition(PointF pointF, RectF rectF) {
            this.mOnLeft = pointF.x < rectF.width() / 2.0f;
            this.mVerticalOffsetPercent = clampVerticalOffsetPercent((pointF.y - rectF.top) / rectF.height());
        }

        public final float clampVerticalOffsetPercent(float f) {
            return Math.max(0.0f, Math.min(1.0f, f));
        }

        public PointF getAbsolutePositionInRegion(RectF rectF) {
            return new PointF(this.mOnLeft ? rectF.left : rectF.right, rectF.top + (this.mVerticalOffsetPercent * rectF.height()));
        }
    }
}
