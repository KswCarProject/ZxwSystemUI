package com.android.wm.shell.draganddrop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.StatusBarManager;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import com.android.internal.logging.InstanceId;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.draganddrop.DragAndDropPolicy;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;

public class DragLayout extends LinearLayout {
    public DragAndDropPolicy.Target mCurrentTarget = null;
    public int mDisplayMargin;
    public int mDividerSize;
    public DropZoneView mDropZoneView1;
    public DropZoneView mDropZoneView2;
    public boolean mHasDropped;
    public final IconProvider mIconProvider;
    public Insets mInsets = Insets.NONE;
    public boolean mIsShowing;
    public final DragAndDropPolicy mPolicy;
    public final SplitScreenController mSplitScreenController;
    public final StatusBarManager mStatusBarManager;

    @SuppressLint({"WrongConstant"})
    public DragLayout(Context context, SplitScreenController splitScreenController, IconProvider iconProvider) {
        super(context);
        this.mSplitScreenController = splitScreenController;
        this.mIconProvider = iconProvider;
        this.mPolicy = new DragAndDropPolicy(context, splitScreenController);
        this.mStatusBarManager = (StatusBarManager) context.getSystemService(StatusBarManager.class);
        this.mDisplayMargin = context.getResources().getDimensionPixelSize(R.dimen.drop_layout_display_margin);
        this.mDividerSize = context.getResources().getDimensionPixelSize(R.dimen.split_divider_bar_width);
        setLayoutDirection(0);
        this.mDropZoneView1 = new DropZoneView(context);
        this.mDropZoneView2 = new DropZoneView(context);
        addView(this.mDropZoneView1, new LinearLayout.LayoutParams(-1, -1));
        addView(this.mDropZoneView2, new LinearLayout.LayoutParams(-1, -1));
        ((LinearLayout.LayoutParams) this.mDropZoneView1.getLayoutParams()).weight = 1.0f;
        ((LinearLayout.LayoutParams) this.mDropZoneView2.getLayoutParams()).weight = 1.0f;
        updateContainerMargins(getResources().getConfiguration().orientation);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mInsets = windowInsets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        recomputeDropTargets();
        int i = getResources().getConfiguration().orientation;
        if (i == 2) {
            this.mDropZoneView1.setBottomInset((float) this.mInsets.bottom);
            this.mDropZoneView2.setBottomInset((float) this.mInsets.bottom);
        } else if (i == 1) {
            this.mDropZoneView1.setBottomInset(0.0f);
            this.mDropZoneView2.setBottomInset((float) this.mInsets.bottom);
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public void onThemeChange() {
        this.mDropZoneView1.onThemeChange();
        this.mDropZoneView2.onThemeChange();
    }

    public void onConfigChanged(Configuration configuration) {
        if (configuration.orientation == 2 && getOrientation() != 0) {
            setOrientation(0);
            updateContainerMargins(configuration.orientation);
        } else if (configuration.orientation == 1 && getOrientation() != 1) {
            setOrientation(1);
            updateContainerMargins(configuration.orientation);
        }
    }

    public final void updateContainerMarginsForSingleTask() {
        DropZoneView dropZoneView = this.mDropZoneView1;
        int i = this.mDisplayMargin;
        dropZoneView.setContainerMargin((float) i, (float) i, (float) i, (float) i);
        this.mDropZoneView2.setContainerMargin(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public final void updateContainerMargins(int i) {
        int i2 = this.mDisplayMargin;
        float f = ((float) i2) / 2.0f;
        if (i == 2) {
            this.mDropZoneView1.setContainerMargin((float) i2, (float) i2, f, (float) i2);
            DropZoneView dropZoneView = this.mDropZoneView2;
            int i3 = this.mDisplayMargin;
            dropZoneView.setContainerMargin(f, (float) i3, (float) i3, (float) i3);
        } else if (i == 1) {
            this.mDropZoneView1.setContainerMargin((float) i2, (float) i2, (float) i2, f);
            DropZoneView dropZoneView2 = this.mDropZoneView2;
            int i4 = this.mDisplayMargin;
            dropZoneView2.setContainerMargin((float) i4, f, (float) i4, (float) i4);
        }
    }

    public boolean hasDropped() {
        return this.mHasDropped;
    }

    public void prepare(DisplayLayout displayLayout, ClipData clipData, InstanceId instanceId) {
        this.mPolicy.start(displayLayout, clipData, instanceId);
        this.mHasDropped = false;
        this.mCurrentTarget = null;
        SplitScreenController splitScreenController = this.mSplitScreenController;
        if (!(splitScreenController != null && splitScreenController.isSplitScreenVisible())) {
            ActivityManager.RunningTaskInfo latestRunningTask = this.mPolicy.getLatestRunningTask();
            if (latestRunningTask == null) {
                return;
            }
            if (latestRunningTask.getActivityType() == 1) {
                Drawable icon = this.mIconProvider.getIcon(latestRunningTask.topActivityInfo);
                int resizingBackgroundColor = getResizingBackgroundColor(latestRunningTask);
                this.mDropZoneView1.setAppInfo(resizingBackgroundColor, icon);
                this.mDropZoneView2.setAppInfo(resizingBackgroundColor, icon);
                updateDropZoneSizes((Rect) null, (Rect) null);
                return;
            }
            this.mDropZoneView1.setForceIgnoreBottomMargin(true);
            updateDropZoneSizesForSingleTask();
            updateContainerMarginsForSingleTask();
            return;
        }
        ActivityManager.RunningTaskInfo taskInfo = this.mSplitScreenController.getTaskInfo(0);
        ActivityManager.RunningTaskInfo taskInfo2 = this.mSplitScreenController.getTaskInfo(1);
        if (!(taskInfo == null || taskInfo2 == null)) {
            Drawable icon2 = this.mIconProvider.getIcon(taskInfo.topActivityInfo);
            int resizingBackgroundColor2 = getResizingBackgroundColor(taskInfo);
            Drawable icon3 = this.mIconProvider.getIcon(taskInfo2.topActivityInfo);
            int resizingBackgroundColor3 = getResizingBackgroundColor(taskInfo2);
            this.mDropZoneView1.setAppInfo(resizingBackgroundColor2, icon2);
            this.mDropZoneView2.setAppInfo(resizingBackgroundColor3, icon3);
        }
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        this.mSplitScreenController.getStageBounds(rect, rect2);
        updateDropZoneSizes(rect, rect2);
    }

    public final void updateDropZoneSizesForSingleTask() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDropZoneView1.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mDropZoneView2.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams2.width = 0;
        layoutParams2.height = 0;
        layoutParams.weight = 1.0f;
        layoutParams2.weight = 0.0f;
        this.mDropZoneView1.setLayoutParams(layoutParams);
        this.mDropZoneView2.setLayoutParams(layoutParams2);
    }

    public final void updateDropZoneSizes(Rect rect, Rect rect2) {
        boolean z = true;
        if (getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        int i = this.mDividerSize / 2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDropZoneView1.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mDropZoneView2.getLayoutParams();
        int i2 = -1;
        if (z) {
            layoutParams.width = -1;
            layoutParams2.width = -1;
            layoutParams.height = rect != null ? rect.height() + i : -1;
            if (rect2 != null) {
                i2 = rect2.height() + i;
            }
            layoutParams2.height = i2;
        } else {
            layoutParams.width = rect != null ? rect.width() + i : -1;
            layoutParams2.width = rect2 != null ? rect2.width() + i : -1;
            layoutParams.height = -1;
            layoutParams2.height = -1;
        }
        float f = 0.0f;
        layoutParams.weight = rect != null ? 0.0f : 1.0f;
        if (rect2 == null) {
            f = 1.0f;
        }
        layoutParams2.weight = f;
        this.mDropZoneView1.setLayoutParams(layoutParams);
        this.mDropZoneView2.setLayoutParams(layoutParams2);
    }

    public void show() {
        this.mIsShowing = true;
        recomputeDropTargets();
    }

    public final void recomputeDropTargets() {
        if (this.mIsShowing) {
            ArrayList<DragAndDropPolicy.Target> targets = this.mPolicy.getTargets(this.mInsets);
            for (int i = 0; i < targets.size(); i++) {
                DragAndDropPolicy.Target target = targets.get(i);
                if (ShellProtoLogCache.WM_SHELL_DRAG_AND_DROP_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, -710770147, 0, (String) null, String.valueOf(target));
                }
                Rect rect = target.drawRegion;
                int i2 = this.mDisplayMargin;
                rect.inset(i2, i2);
            }
        }
    }

    public void update(DragEvent dragEvent) {
        DragAndDropPolicy.Target targetAtLocation;
        if (!this.mHasDropped && this.mCurrentTarget != (targetAtLocation = this.mPolicy.getTargetAtLocation((int) dragEvent.getX(), (int) dragEvent.getY()))) {
            if (ShellProtoLogCache.WM_SHELL_DRAG_AND_DROP_enabled) {
                String valueOf = String.valueOf(targetAtLocation);
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, 1481772149, 0, (String) null, valueOf);
            }
            if (targetAtLocation == null) {
                animateSplitContainers(false, (Runnable) null);
            } else if (this.mCurrentTarget != null) {
                this.mDropZoneView1.animateSwitch();
                this.mDropZoneView2.animateSwitch();
            } else if (this.mPolicy.getNumTargets() == 1) {
                animateFullscreenContainer(true);
            } else {
                animateSplitContainers(true, (Runnable) null);
                animateHighlight(targetAtLocation);
            }
            this.mCurrentTarget = targetAtLocation;
        }
    }

    public void hide(DragEvent dragEvent, Runnable runnable) {
        this.mIsShowing = false;
        animateSplitContainers(false, runnable);
        this.mDropZoneView1.setForceIgnoreBottomMargin(false);
        this.mDropZoneView2.setForceIgnoreBottomMargin(false);
        updateContainerMargins(getResources().getConfiguration().orientation);
        this.mCurrentTarget = null;
    }

    public boolean drop(DragEvent dragEvent, SurfaceControl surfaceControl, Runnable runnable) {
        DragAndDropPolicy.Target target = this.mCurrentTarget;
        boolean z = target != null;
        this.mHasDropped = true;
        this.mPolicy.handleDrop(target, dragEvent.getClipData());
        hide(dragEvent, runnable);
        hideDragSurface(surfaceControl);
        return z;
    }

    public final void hideDragSurface(final SurfaceControl surfaceControl) {
        final SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(300);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new DragLayout$$ExternalSyntheticLambda0(transaction, surfaceControl));
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public boolean mCanceled = false;

            public void onAnimationCancel(Animator animator) {
                cleanUpSurface();
                this.mCanceled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (!this.mCanceled) {
                    cleanUpSurface();
                }
            }

            public final void cleanUpSurface() {
                transaction.remove(surfaceControl);
                transaction.apply();
            }
        });
        ofFloat.start();
    }

    public static /* synthetic */ void lambda$hideDragSurface$0(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, 1.0f - valueAnimator.getAnimatedFraction());
        transaction.apply();
    }

    public final void animateFullscreenContainer(boolean z) {
        this.mStatusBarManager.disable(z ? 9830400 : 0);
        this.mDropZoneView1.setShowingMargin(z);
        this.mDropZoneView1.setShowingHighlight(z);
    }

    public final void animateSplitContainers(boolean z, final Runnable runnable) {
        this.mStatusBarManager.disable(z ? 9830400 : 0);
        this.mDropZoneView1.setShowingMargin(z);
        this.mDropZoneView2.setShowingMargin(z);
        Animator animator = this.mDropZoneView1.getAnimator();
        if (runnable == null) {
            return;
        }
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

    public final void animateHighlight(DragAndDropPolicy.Target target) {
        int i = target.type;
        if (i == 1 || i == 2) {
            this.mDropZoneView1.setShowingHighlight(true);
            this.mDropZoneView2.setShowingHighlight(false);
        } else if (i == 3 || i == 4) {
            this.mDropZoneView1.setShowingHighlight(false);
            this.mDropZoneView2.setShowingHighlight(true);
        }
    }

    public static int getResizingBackgroundColor(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int backgroundColor = runningTaskInfo.taskDescription.getBackgroundColor();
        if (backgroundColor == -1) {
            backgroundColor = -1;
        }
        return Color.valueOf(backgroundColor).toArgb();
    }
}
