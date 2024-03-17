package com.android.wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Property;
import android.view.GestureDetector;
import android.view.InsetsController;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.MotionEvent;
import android.view.SurfaceControlViewHost;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;

public class DividerView extends FrameLayout implements View.OnTouchListener {
    public static final Property<DividerView, Integer> DIVIDER_HEIGHT_PROPERTY = new Property<DividerView, Integer>(Integer.class, "height") {
        public Integer get(DividerView dividerView) {
            return Integer.valueOf(dividerView.mDividerBar.getLayoutParams().height);
        }

        public void set(DividerView dividerView, Integer num) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) dividerView.mDividerBar.getLayoutParams();
            marginLayoutParams.height = num.intValue();
            dividerView.mDividerBar.setLayoutParams(marginLayoutParams);
        }
    };
    public AnimatorListenerAdapter mAnimatorListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animator) {
            DividerView.this.mSetTouchRegion = true;
        }

        public void onAnimationCancel(Animator animator) {
            DividerView.this.mSetTouchRegion = true;
        }
    };
    public View mBackground;
    public FrameLayout mDividerBar;
    public final Rect mDividerBounds = new Rect();
    public GestureDetector mDoubleTapDetector;
    public float mExpandedTaskBarHeight;
    public DividerHandleView mHandle;
    public final View.AccessibilityDelegate mHandleDelegate = new View.AccessibilityDelegate() {
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            DividerSnapAlgorithm dividerSnapAlgorithm = DividerView.this.mSplitLayout.mDividerSnapAlgorithm;
            if (DividerView.this.isLandscape()) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_full, DividerView.this.mContext.getString(R.string.accessibility_action_divider_left_full)));
                if (dividerSnapAlgorithm.isFirstSplitTargetAvailable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_70, DividerView.this.mContext.getString(R.string.accessibility_action_divider_left_70)));
                }
                if (dividerSnapAlgorithm.showMiddleSplitTargetForAccessibility()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_50, DividerView.this.mContext.getString(R.string.accessibility_action_divider_left_50)));
                }
                if (dividerSnapAlgorithm.isLastSplitTargetAvailable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_30, DividerView.this.mContext.getString(R.string.accessibility_action_divider_left_30)));
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_rb_full, DividerView.this.mContext.getString(R.string.accessibility_action_divider_right_full)));
                return;
            }
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_full, DividerView.this.mContext.getString(R.string.accessibility_action_divider_top_full)));
            if (dividerSnapAlgorithm.isFirstSplitTargetAvailable()) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_70, DividerView.this.mContext.getString(R.string.accessibility_action_divider_top_70)));
            }
            if (dividerSnapAlgorithm.showMiddleSplitTargetForAccessibility()) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_50, DividerView.this.mContext.getString(R.string.accessibility_action_divider_top_50)));
            }
            if (dividerSnapAlgorithm.isLastSplitTargetAvailable()) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_30, DividerView.this.mContext.getString(R.string.accessibility_action_divider_top_30)));
            }
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_rb_full, DividerView.this.mContext.getString(R.string.accessibility_action_divider_bottom_full)));
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            DividerSnapAlgorithm.SnapTarget snapTarget;
            DividerSnapAlgorithm dividerSnapAlgorithm = DividerView.this.mSplitLayout.mDividerSnapAlgorithm;
            if (i == R.id.action_move_tl_full) {
                snapTarget = dividerSnapAlgorithm.getDismissEndTarget();
            } else if (i == R.id.action_move_tl_70) {
                snapTarget = dividerSnapAlgorithm.getLastSplitTarget();
            } else if (i == R.id.action_move_tl_50) {
                snapTarget = dividerSnapAlgorithm.getMiddleTarget();
            } else if (i == R.id.action_move_tl_30) {
                snapTarget = dividerSnapAlgorithm.getFirstSplitTarget();
            } else {
                snapTarget = i == R.id.action_move_rb_full ? dividerSnapAlgorithm.getDismissStartTarget() : null;
            }
            if (snapTarget == null) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            DividerView.this.mSplitLayout.snapToTarget(DividerView.this.mSplitLayout.getDividePosition(), snapTarget);
            return true;
        }
    };
    public boolean mInteractive;
    public boolean mMoving;
    public boolean mSetTouchRegion = true;
    public SplitLayout mSplitLayout;
    public SplitWindowManager mSplitWindowManager;
    public int mStartPos;
    public final Rect mTempRect = new Rect();
    public int mTouchElevation;
    public final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    public VelocityTracker mVelocityTracker;
    public SurfaceControlViewHost mViewHost;

    public DividerView(Context context) {
        super(context);
    }

    public DividerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setup(SplitLayout splitLayout, SplitWindowManager splitWindowManager, SurfaceControlViewHost surfaceControlViewHost, InsetsState insetsState) {
        this.mSplitLayout = splitLayout;
        this.mSplitWindowManager = splitWindowManager;
        this.mViewHost = surfaceControlViewHost;
        this.mDividerBounds.set(splitLayout.getDividerBounds());
        onInsetsChanged(insetsState, false);
    }

    public void onInsetsChanged(InsetsState insetsState, boolean z) {
        this.mTempRect.set(this.mSplitLayout.getDividerBounds());
        InsetsSource source = insetsState.getSource(21);
        if (((float) source.getFrame().height()) >= this.mExpandedTaskBarHeight) {
            Rect rect = this.mTempRect;
            rect.inset(source.calculateVisibleInsets(rect));
        }
        if (!this.mTempRect.equals(this.mDividerBounds)) {
            if (z) {
                ObjectAnimator ofInt = ObjectAnimator.ofInt(this, DIVIDER_HEIGHT_PROPERTY, new int[]{this.mDividerBounds.height(), this.mTempRect.height()});
                ofInt.setInterpolator(InsetsController.RESIZE_INTERPOLATOR);
                ofInt.setDuration(300);
                ofInt.addListener(this.mAnimatorListener);
                ofInt.start();
            } else {
                DIVIDER_HEIGHT_PROPERTY.set(this, Integer.valueOf(this.mTempRect.height()));
                this.mSetTouchRegion = true;
            }
            this.mDividerBounds.set(this.mTempRect);
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDividerBar = (FrameLayout) findViewById(R.id.divider_bar);
        this.mHandle = (DividerHandleView) findViewById(R.id.docked_divider_handle);
        this.mBackground = findViewById(R.id.docked_divider_background);
        this.mExpandedTaskBarHeight = (float) getResources().getDimensionPixelSize(17105564);
        this.mTouchElevation = getResources().getDimensionPixelSize(R.dimen.docked_stack_divider_lift_elevation);
        this.mDoubleTapDetector = new GestureDetector(getContext(), new DoubleTapListener());
        this.mInteractive = true;
        setOnTouchListener(this);
        this.mHandle.setAccessibilityDelegate(this.mHandleDelegate);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mSetTouchRegion) {
            this.mTempRect.set(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom());
            this.mSplitWindowManager.setTouchRegion(this.mTempRect);
            this.mSetTouchRegion = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003e, code lost:
        if (r6 != 3) goto L_0x00ba;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(android.view.View r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            com.android.wm.shell.common.split.SplitLayout r6 = r5.mSplitLayout
            r0 = 0
            if (r6 == 0) goto L_0x00bb
            boolean r6 = r5.mInteractive
            if (r6 != 0) goto L_0x000b
            goto L_0x00bb
        L_0x000b:
            android.view.GestureDetector r6 = r5.mDoubleTapDetector
            boolean r6 = r6.onTouchEvent(r7)
            r1 = 1
            if (r6 == 0) goto L_0x0015
            return r1
        L_0x0015:
            float r6 = r7.getRawX()
            float r2 = r7.getRawY()
            r7.setLocation(r6, r2)
            int r6 = r7.getAction()
            r6 = r6 & 255(0xff, float:3.57E-43)
            boolean r2 = r5.isLandscape()
            if (r2 == 0) goto L_0x0031
            float r3 = r7.getX()
            goto L_0x0035
        L_0x0031:
            float r3 = r7.getY()
        L_0x0035:
            int r3 = (int) r3
            if (r6 == 0) goto L_0x00aa
            if (r6 == r1) goto L_0x006f
            r4 = 2
            if (r6 == r4) goto L_0x0042
            r4 = 3
            if (r6 == r4) goto L_0x006f
            goto L_0x00ba
        L_0x0042:
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            r6.addMovement(r7)
            boolean r6 = r5.mMoving
            if (r6 != 0) goto L_0x005b
            int r6 = r5.mStartPos
            int r6 = r3 - r6
            int r6 = java.lang.Math.abs(r6)
            int r7 = r5.mTouchSlop
            if (r6 <= r7) goto L_0x005b
            r5.mStartPos = r3
            r5.mMoving = r1
        L_0x005b:
            boolean r6 = r5.mMoving
            if (r6 == 0) goto L_0x00ba
            com.android.wm.shell.common.split.SplitLayout r6 = r5.mSplitLayout
            int r6 = r6.getDividePosition()
            int r6 = r6 + r3
            int r7 = r5.mStartPos
            int r6 = r6 - r7
            com.android.wm.shell.common.split.SplitLayout r5 = r5.mSplitLayout
            r5.updateDivideBounds(r6)
            goto L_0x00ba
        L_0x006f:
            r5.releaseTouching()
            boolean r6 = r5.mMoving
            if (r6 != 0) goto L_0x0077
            goto L_0x00ba
        L_0x0077:
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            r6.addMovement(r7)
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            r7 = 1000(0x3e8, float:1.401E-42)
            r6.computeCurrentVelocity(r7)
            if (r2 == 0) goto L_0x008c
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            float r6 = r6.getXVelocity()
            goto L_0x0092
        L_0x008c:
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            float r6 = r6.getYVelocity()
        L_0x0092:
            com.android.wm.shell.common.split.SplitLayout r7 = r5.mSplitLayout
            int r7 = r7.getDividePosition()
            int r7 = r7 + r3
            int r2 = r5.mStartPos
            int r7 = r7 - r2
            com.android.wm.shell.common.split.SplitLayout r2 = r5.mSplitLayout
            com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r6 = r2.findSnapTarget(r7, r6, r0)
            com.android.wm.shell.common.split.SplitLayout r2 = r5.mSplitLayout
            r2.snapToTarget(r7, r6)
            r5.mMoving = r0
            goto L_0x00ba
        L_0x00aa:
            android.view.VelocityTracker r6 = android.view.VelocityTracker.obtain()
            r5.mVelocityTracker = r6
            r6.addMovement(r7)
            r5.setTouching()
            r5.mStartPos = r3
            r5.mMoving = r0
        L_0x00ba:
            return r1
        L_0x00bb:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.split.DividerView.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public final void setTouching() {
        setSlippery(false);
        this.mHandle.setTouching(true, true);
        this.mHandle.animate().setInterpolator(Interpolators.TOUCH_RESPONSE).setDuration(150).translationZ((float) this.mTouchElevation).start();
    }

    public final void releaseTouching() {
        setSlippery(true);
        this.mHandle.setTouching(false, true);
        this.mHandle.animate().setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setDuration(200).translationZ(0.0f).start();
    }

    public final void setSlippery(boolean z) {
        if (this.mViewHost != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
            int i = layoutParams.flags;
            if (((i & 536870912) != 0) != z) {
                if (z) {
                    layoutParams.flags = i | 536870912;
                } else {
                    layoutParams.flags = -536870913 & i;
                }
                this.mViewHost.relayout(layoutParams);
            }
        }
    }

    public void setInteractive(boolean z) {
        if (z != this.mInteractive) {
            this.mInteractive = z;
            releaseTouching();
            this.mHandle.setVisibility(this.mInteractive ? 0 : 4);
        }
    }

    public final boolean isLandscape() {
        return getResources().getConfiguration().orientation == 2;
    }

    public class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        public DoubleTapListener() {
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (DividerView.this.mSplitLayout == null) {
                return true;
            }
            DividerView.this.mSplitLayout.onDoubleTappedDivider();
            return true;
        }
    }
}
