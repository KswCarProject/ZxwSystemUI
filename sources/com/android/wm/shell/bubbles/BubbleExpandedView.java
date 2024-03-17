package com.android.wm.shell.bubbles;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.CornerPathEffect;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.wm.shell.R;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.common.AlphaOptimizedButton;
import com.android.wm.shell.common.TriangleShape;
import java.io.PrintWriter;

public class BubbleExpandedView extends LinearLayout {
    public int mBackgroundColorFloating;
    public Bubble mBubble;
    public BubbleController mController;
    public float mCornerRadius;
    public ShapeDrawable mCurrentPointer;
    public final FrameLayout mExpandedViewContainer;
    public int[] mExpandedViewContainerLocation;
    public boolean mImeVisible;
    public boolean mIsAlphaAnimating;
    public boolean mIsContentVisible;
    public boolean mIsOverflow;
    public ShapeDrawable mLeftPointer;
    public AlphaOptimizedButton mManageButton;
    public boolean mNeedsNewHeight;
    public BubbleOverflowContainerView mOverflowView;
    public PendingIntent mPendingIntent;
    public CornerPathEffect mPointerEffect;
    public int mPointerHeight;
    public float mPointerOverlap;
    public float mPointerRadius;
    public View mPointerView;
    public int mPointerWidth;
    public BubblePositioner mPositioner;
    public ShapeDrawable mRightPointer;
    public BubbleStackView mStackView;
    public int mTaskId;
    public TaskView mTaskView;
    public final TaskView.Listener mTaskViewListener;
    public ShapeDrawable mTopPointer;
    public boolean mUsingMaxHeight;

    public BubbleExpandedView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BubbleExpandedView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BubbleExpandedView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BubbleExpandedView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTaskId = -1;
        this.mIsContentVisible = false;
        this.mIsAlphaAnimating = false;
        this.mCornerRadius = 0.0f;
        this.mExpandedViewContainer = new FrameLayout(getContext());
        this.mTaskViewListener = new TaskView.Listener() {
            public boolean mDestroyed = false;
            public boolean mInitialized = false;

            public void onInitialized() {
                if (!this.mDestroyed && !this.mInitialized) {
                    ActivityOptions makeCustomAnimation = ActivityOptions.makeCustomAnimation(BubbleExpandedView.this.getContext(), 0, 0);
                    Rect rect = new Rect();
                    BubbleExpandedView.this.mTaskView.getBoundsOnScreen(rect);
                    BubbleExpandedView.this.post(new BubbleExpandedView$1$$ExternalSyntheticLambda0(this, makeCustomAnimation, rect));
                    this.mInitialized = true;
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onInitialized$0(ActivityOptions activityOptions, Rect rect) {
                try {
                    activityOptions.setTaskAlwaysOnTop(true);
                    activityOptions.setLaunchedFromBubble(true);
                    if (BubbleExpandedView.this.mIsOverflow || !BubbleExpandedView.this.mBubble.hasMetadataShortcutId()) {
                        Intent intent = new Intent();
                        intent.addFlags(524288);
                        intent.addFlags(134217728);
                        if (BubbleExpandedView.this.mBubble != null) {
                            BubbleExpandedView.this.mBubble.setIntentActive();
                        }
                        BubbleExpandedView.this.mTaskView.startActivity(BubbleExpandedView.this.mPendingIntent, intent, activityOptions, rect);
                        return;
                    }
                    activityOptions.setApplyActivityFlagsForBubbles(true);
                    BubbleExpandedView.this.mTaskView.startShortcutActivity(BubbleExpandedView.this.mBubble.getShortcutInfo(), activityOptions, rect);
                } catch (RuntimeException e) {
                    Log.w("Bubbles", "Exception while displaying bubble: " + BubbleExpandedView.this.getBubbleKey() + ", " + e.getMessage() + "; removing bubble");
                    BubbleExpandedView.this.mController.removeBubble(BubbleExpandedView.this.getBubbleKey(), 10);
                }
            }

            public void onReleased() {
                this.mDestroyed = true;
            }

            public void onTaskCreated(int i, ComponentName componentName) {
                BubbleExpandedView.this.mTaskId = i;
                BubbleExpandedView.this.setContentVisibility(true);
            }

            public void onTaskVisibilityChanged(int i, boolean z) {
                BubbleExpandedView.this.setContentVisibility(z);
            }

            public void onTaskRemovalStarted(int i) {
                if (BubbleExpandedView.this.mBubble != null) {
                    BubbleExpandedView.this.post(new BubbleExpandedView$1$$ExternalSyntheticLambda1(this));
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onTaskRemovalStarted$1() {
                BubbleExpandedView.this.mController.removeBubble(BubbleExpandedView.this.mBubble.getKey(), 3);
            }

            public void onBackPressedOnTaskRoot(int i) {
                if (BubbleExpandedView.this.mTaskId == i && BubbleExpandedView.this.mStackView.isExpanded()) {
                    BubbleExpandedView.this.mStackView.onBackPressed();
                }
            }
        };
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mManageButton = (AlphaOptimizedButton) LayoutInflater.from(getContext()).inflate(R.layout.bubble_manage_button, this, false);
        updateDimensions();
        View findViewById = findViewById(R.id.pointer_view);
        this.mPointerView = findViewById;
        this.mCurrentPointer = this.mTopPointer;
        findViewById.setVisibility(4);
        setContentVisibility(false);
        this.mExpandedViewContainer.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), BubbleExpandedView.this.mCornerRadius);
            }
        });
        this.mExpandedViewContainer.setClipToOutline(true);
        this.mExpandedViewContainer.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        addView(this.mExpandedViewContainer);
        bringChildToFront(this.mManageButton);
        applyThemeAttrs();
        setClipToPadding(false);
        setOnTouchListener(new BubbleExpandedView$$ExternalSyntheticLambda0(this));
        setLayoutDirection(3);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$0(View view, MotionEvent motionEvent) {
        if (this.mTaskView == null) {
            return false;
        }
        Rect rect = new Rect();
        this.mTaskView.getBoundsOnScreen(rect);
        if (motionEvent.getRawY() < ((float) rect.top) || motionEvent.getRawY() > ((float) rect.bottom) || (motionEvent.getRawX() >= ((float) rect.left) && motionEvent.getRawX() <= ((float) rect.right))) {
            return false;
        }
        return true;
    }

    public void initialize(BubbleController bubbleController, BubbleStackView bubbleStackView, boolean z) {
        this.mController = bubbleController;
        this.mStackView = bubbleStackView;
        this.mIsOverflow = z;
        this.mPositioner = bubbleController.getPositioner();
        if (this.mIsOverflow) {
            BubbleOverflowContainerView bubbleOverflowContainerView = (BubbleOverflowContainerView) LayoutInflater.from(getContext()).inflate(R.layout.bubble_overflow_container, (ViewGroup) null);
            this.mOverflowView = bubbleOverflowContainerView;
            bubbleOverflowContainerView.setBubbleController(this.mController);
            this.mExpandedViewContainer.addView(this.mOverflowView, new FrameLayout.LayoutParams(-1, -1));
            this.mExpandedViewContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            bringChildToFront(this.mOverflowView);
            this.mManageButton.setVisibility(8);
            return;
        }
        TaskView taskView = new TaskView(this.mContext, this.mController.getTaskOrganizer(), this.mController.getTaskViewTransitions(), this.mController.getSyncTransactionQueue());
        this.mTaskView = taskView;
        taskView.setListener(this.mController.getMainExecutor(), this.mTaskViewListener);
        this.mExpandedViewContainer.addView(this.mTaskView);
        bringChildToFront(this.mTaskView);
    }

    public void updateDimensions() {
        Resources resources = getResources();
        updateFontSize();
        this.mPointerWidth = resources.getDimensionPixelSize(R.dimen.bubble_pointer_width);
        this.mPointerHeight = resources.getDimensionPixelSize(R.dimen.bubble_pointer_height);
        this.mPointerRadius = (float) getResources().getDimensionPixelSize(R.dimen.bubble_pointer_radius);
        this.mPointerEffect = new CornerPathEffect(this.mPointerRadius);
        this.mPointerOverlap = (float) getResources().getDimensionPixelSize(R.dimen.bubble_pointer_overlap);
        this.mTopPointer = new ShapeDrawable(TriangleShape.create((float) this.mPointerWidth, (float) this.mPointerHeight, true));
        this.mLeftPointer = new ShapeDrawable(TriangleShape.createHorizontal((float) this.mPointerWidth, (float) this.mPointerHeight, true));
        this.mRightPointer = new ShapeDrawable(TriangleShape.createHorizontal((float) this.mPointerWidth, (float) this.mPointerHeight, false));
        if (this.mPointerView != null) {
            updatePointerView();
        }
        AlphaOptimizedButton alphaOptimizedButton = this.mManageButton;
        if (alphaOptimizedButton != null) {
            int visibility = alphaOptimizedButton.getVisibility();
            removeView(this.mManageButton);
            AlphaOptimizedButton alphaOptimizedButton2 = (AlphaOptimizedButton) LayoutInflater.from(getContext()).inflate(R.layout.bubble_manage_button, this, false);
            this.mManageButton = alphaOptimizedButton2;
            addView(alphaOptimizedButton2);
            this.mManageButton.setVisibility(visibility);
        }
    }

    public void updateFontSize() {
        float dimensionPixelSize = (float) this.mContext.getResources().getDimensionPixelSize(17105570);
        AlphaOptimizedButton alphaOptimizedButton = this.mManageButton;
        if (alphaOptimizedButton != null) {
            alphaOptimizedButton.setTextSize(0, dimensionPixelSize);
        }
        BubbleOverflowContainerView bubbleOverflowContainerView = this.mOverflowView;
        if (bubbleOverflowContainerView != null) {
            bubbleOverflowContainerView.updateFontSize();
        }
    }

    public void applyThemeAttrs() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16844145, 16844002});
        this.mCornerRadius = ScreenDecorationsUtils.supportsRoundedCornersOnWindows(this.mContext.getResources()) ? (float) obtainStyledAttributes.getDimensionPixelSize(0, 0) : 0.0f;
        int color = obtainStyledAttributes.getColor(1, -1);
        this.mBackgroundColorFloating = color;
        this.mExpandedViewContainer.setBackgroundColor(color);
        obtainStyledAttributes.recycle();
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.setCornerRadius(this.mCornerRadius);
        }
        updatePointerView();
    }

    public final void updatePointerView() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mPointerView.getLayoutParams();
        ShapeDrawable shapeDrawable = this.mCurrentPointer;
        if (shapeDrawable == this.mLeftPointer || shapeDrawable == this.mRightPointer) {
            layoutParams.width = this.mPointerHeight;
            layoutParams.height = this.mPointerWidth;
        } else {
            layoutParams.width = this.mPointerWidth;
            layoutParams.height = this.mPointerHeight;
        }
        shapeDrawable.setTint(this.mBackgroundColorFloating);
        Paint paint = this.mCurrentPointer.getPaint();
        paint.setColor(this.mBackgroundColorFloating);
        paint.setPathEffect(this.mPointerEffect);
        this.mPointerView.setLayoutParams(layoutParams);
        this.mPointerView.setBackground(this.mCurrentPointer);
    }

    @VisibleForTesting
    public String getBubbleKey() {
        Bubble bubble = this.mBubble;
        if (bubble != null) {
            return bubble.getKey();
        }
        if (this.mIsOverflow) {
            return "Overflow";
        }
        return null;
    }

    public void setSurfaceZOrderedOnTop(boolean z) {
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.setZOrderedOnTop(z, true);
        }
    }

    public void setImeVisible(boolean z) {
        this.mImeVisible = z;
        if (!z && this.mNeedsNewHeight) {
            updateHeight();
        }
    }

    public SurfaceControl.ScreenshotHardwareBuffer snapshotActivitySurface() {
        if (this.mIsOverflow) {
            Picture picture = new Picture();
            BubbleOverflowContainerView bubbleOverflowContainerView = this.mOverflowView;
            bubbleOverflowContainerView.draw(picture.beginRecording(bubbleOverflowContainerView.getWidth(), this.mOverflowView.getHeight()));
            picture.endRecording();
            Bitmap createBitmap = Bitmap.createBitmap(picture);
            return new SurfaceControl.ScreenshotHardwareBuffer(createBitmap.getHardwareBuffer(), createBitmap.getColorSpace(), false, false);
        }
        TaskView taskView = this.mTaskView;
        if (taskView == null || taskView.getSurfaceControl() == null) {
            return null;
        }
        return SurfaceControl.captureLayers(this.mTaskView.getSurfaceControl(), new Rect(0, 0, this.mTaskView.getWidth(), this.mTaskView.getHeight()), 1.0f);
    }

    public int[] getTaskViewLocationOnScreen() {
        if (this.mIsOverflow) {
            return this.mOverflowView.getLocationOnScreen();
        }
        TaskView taskView = this.mTaskView;
        return taskView != null ? taskView.getLocationOnScreen() : new int[]{0, 0};
    }

    public void setManageClickListener(View.OnClickListener onClickListener) {
        this.mManageButton.setOnClickListener(onClickListener);
    }

    public void updateObscuredTouchableRegion() {
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.onLocationChanged();
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mImeVisible = false;
        this.mNeedsNewHeight = false;
    }

    public void setAlphaAnimating(boolean z) {
        this.mIsAlphaAnimating = z;
        if (!z) {
            setContentVisibility(this.mIsContentVisible);
        }
    }

    public void setTaskViewAlpha(float f) {
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.setAlpha(f);
        }
        this.mPointerView.setAlpha(f);
        setAlpha(f);
    }

    public void setContentVisibility(boolean z) {
        this.mIsContentVisible = z;
        TaskView taskView = this.mTaskView;
        if (taskView != null && !this.mIsAlphaAnimating) {
            float f = 1.0f;
            taskView.setAlpha(z ? 1.0f : 0.0f);
            View view = this.mPointerView;
            if (!z) {
                f = 0.0f;
            }
            view.setAlpha(f);
        }
    }

    public TaskView getTaskView() {
        return this.mTaskView;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public void update(Bubble bubble) {
        if (this.mStackView == null) {
            Log.w("Bubbles", "Stack is null for bubble: " + bubble);
            return;
        }
        boolean z = this.mBubble == null || didBackingContentChange(bubble);
        if (z || (bubble != null && bubble.getKey().equals(this.mBubble.getKey()))) {
            this.mBubble = bubble;
            this.mManageButton.setContentDescription(getResources().getString(R.string.bubbles_settings_button_description, new Object[]{bubble.getAppName()}));
            this.mManageButton.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    BubbleExpandedView.this.mStackView.setupLocalMenu(accessibilityNodeInfo);
                }
            });
            if (z) {
                PendingIntent bubbleIntent = this.mBubble.getBubbleIntent();
                this.mPendingIntent = bubbleIntent;
                if ((bubbleIntent != null || this.mBubble.hasMetadataShortcutId()) && this.mTaskView != null) {
                    setContentVisibility(false);
                    this.mTaskView.setVisibility(0);
                }
            }
            applyThemeAttrs();
            return;
        }
        Log.w("Bubbles", "Trying to update entry with different key, new bubble: " + bubble.getKey() + " old bubble: " + bubble.getKey());
    }

    public final boolean didBackingContentChange(Bubble bubble) {
        if (((this.mBubble == null || this.mPendingIntent == null) ? false : true) != (bubble.getBubbleIntent() != null)) {
            return true;
        }
        return false;
    }

    public boolean isUsingMaxHeight() {
        return this.mUsingMaxHeight;
    }

    public void updateHeight() {
        float f;
        FrameLayout.LayoutParams layoutParams;
        if (this.mExpandedViewContainerLocation != null) {
            Bubble bubble = this.mBubble;
            if ((bubble != null && this.mTaskView != null) || this.mIsOverflow) {
                float expandedViewHeight = this.mPositioner.getExpandedViewHeight(bubble);
                int maxExpandedViewHeight = this.mPositioner.getMaxExpandedViewHeight(this.mIsOverflow);
                if (expandedViewHeight == -1.0f) {
                    f = (float) maxExpandedViewHeight;
                } else {
                    f = Math.min(expandedViewHeight, (float) maxExpandedViewHeight);
                }
                boolean z = true;
                this.mUsingMaxHeight = f == ((float) maxExpandedViewHeight);
                if (this.mIsOverflow) {
                    layoutParams = (FrameLayout.LayoutParams) this.mOverflowView.getLayoutParams();
                } else {
                    layoutParams = (FrameLayout.LayoutParams) this.mTaskView.getLayoutParams();
                }
                if (((float) layoutParams.height) == f) {
                    z = false;
                }
                this.mNeedsNewHeight = z;
                if (!this.mImeVisible) {
                    layoutParams.height = (int) f;
                    if (this.mIsOverflow) {
                        this.mOverflowView.setLayoutParams(layoutParams);
                    } else {
                        this.mTaskView.setLayoutParams(layoutParams);
                    }
                    this.mNeedsNewHeight = false;
                }
            }
        }
    }

    public void updateView(int[] iArr) {
        this.mExpandedViewContainerLocation = iArr;
        updateHeight();
        TaskView taskView = this.mTaskView;
        if (taskView != null && taskView.getVisibility() == 0 && this.mTaskView.isAttachedToWindow()) {
            this.mTaskView.onLocationChanged();
        }
        if (this.mIsOverflow) {
            this.mOverflowView.show();
        }
    }

    public void setPointerPosition(float f, boolean z, boolean z2) {
        boolean z3 = this.mContext.getResources().getConfiguration().getLayoutDirection() == 1;
        boolean showBubblesVertically = this.mPositioner.showBubblesVertically();
        float f2 = 0.0f;
        float f3 = (!showBubblesVertically || !z) ? 0.0f : ((float) this.mPointerHeight) - this.mPointerOverlap;
        float f4 = (!showBubblesVertically || z) ? 0.0f : ((float) this.mPointerHeight) - this.mPointerOverlap;
        if (!showBubblesVertically) {
            f2 = ((float) this.mPointerHeight) - this.mPointerOverlap;
        }
        setPadding((int) f3, (int) f2, (int) f4, 0);
        float pointerPosition = this.mPositioner.getPointerPosition(f);
        if (this.mPositioner.showBubblesVertically()) {
            pointerPosition -= this.mPositioner.getExpandedViewY(this.mBubble, f);
        }
        post(new BubbleExpandedView$$ExternalSyntheticLambda1(this, showBubblesVertically, z, pointerPosition, z3, z2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setPointerPosition$1(boolean z, boolean z2, float f, boolean z3, boolean z4) {
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        this.mCurrentPointer = z ? z2 ? this.mLeftPointer : this.mRightPointer : this.mTopPointer;
        updatePointerView();
        if (z) {
            f2 = f - (((float) this.mPointerWidth) / 2.0f);
            if (!z3) {
                if (z2) {
                    f3 = ((float) (-this.mPointerHeight)) + this.mPointerOverlap;
                } else {
                    f5 = (float) (getWidth() - this.mPaddingRight);
                    f6 = this.mPointerOverlap;
                }
            } else if (z2) {
                f3 = -(((float) (getWidth() - this.mPaddingLeft)) - this.mPointerOverlap);
            } else {
                f5 = (float) this.mPointerHeight;
                f6 = this.mPointerOverlap;
            }
            f3 = f5 - f6;
        } else {
            float f7 = this.mPointerOverlap;
            if (!z3) {
                f4 = f - (((float) this.mPointerWidth) / 2.0f);
            } else {
                f4 = (-(((float) (getWidth() - this.mPaddingLeft)) - f)) + (((float) this.mPointerWidth) / 2.0f);
            }
            f2 = f7;
            f3 = f4;
        }
        if (z4) {
            this.mPointerView.animate().translationX(f3).translationY(f2).start();
            return;
        }
        this.mPointerView.setTranslationY(f2);
        this.mPointerView.setTranslationX(f3);
        this.mPointerView.setVisibility(0);
    }

    public void getManageButtonBoundsOnScreen(Rect rect) {
        this.mManageButton.getBoundsOnScreen(rect);
    }

    public int getManageButtonMargin() {
        return ((LinearLayout.LayoutParams) this.mManageButton.getLayoutParams()).getMarginStart();
    }

    public void cleanUpExpandedState() {
        if (getTaskId() != -1) {
            try {
                ActivityTaskManager.getService().removeTask(getTaskId());
            } catch (RemoteException e) {
                Log.w("Bubbles", e.getMessage());
            }
        }
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.release();
            removeView(this.mTaskView);
            this.mTaskView = null;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("BubbleExpandedView");
        printWriter.print("  taskId:               ");
        printWriter.println(this.mTaskId);
        printWriter.print("  stackView:            ");
        printWriter.println(this.mStackView);
    }
}
