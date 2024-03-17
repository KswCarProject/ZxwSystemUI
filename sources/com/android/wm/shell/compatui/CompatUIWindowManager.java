package com.android.wm.shell.compatui;

import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIController;

public class CompatUIWindowManager extends CompatUIWindowManagerAbstract {
    public final CompatUIController.CompatUICallback mCallback;
    @VisibleForTesting
    public int mCameraCompatControlState = 0;
    @VisibleForTesting
    public CompatUIHintsState mCompatUIHintsState;
    @VisibleForTesting
    public boolean mHasSizeCompat;
    @VisibleForTesting
    public CompatUILayout mLayout;

    public static class CompatUIHintsState {
        @VisibleForTesting
        public boolean mHasShownCameraCompatHint;
        @VisibleForTesting
        public boolean mHasShownSizeCompatHint;
    }

    public int getZOrder() {
        return 2147483646;
    }

    public CompatUIWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, CompatUIController.CompatUICallback compatUICallback, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, CompatUIHintsState compatUIHintsState) {
        super(context, taskInfo, syncTransactionQueue, taskListener, displayLayout);
        this.mCallback = compatUICallback;
        this.mHasSizeCompat = taskInfo.topActivityInSizeCompat;
        this.mCameraCompatControlState = taskInfo.cameraCompatControlState;
        this.mCompatUIHintsState = compatUIHintsState;
    }

    public View getLayout() {
        return this.mLayout;
    }

    public void removeLayout() {
        this.mLayout = null;
    }

    public boolean eligibleToShowLayout() {
        return this.mHasSizeCompat || shouldShowCameraControl();
    }

    public View createLayout() {
        CompatUILayout inflateLayout = inflateLayout();
        this.mLayout = inflateLayout;
        inflateLayout.inject(this);
        updateVisibilityOfViews();
        if (this.mHasSizeCompat) {
            this.mCallback.onSizeCompatRestartButtonAppeared(this.mTaskId);
        }
        return this.mLayout;
    }

    @VisibleForTesting
    public CompatUILayout inflateLayout() {
        return (CompatUILayout) LayoutInflater.from(this.mContext).inflate(R.layout.compat_ui_layout, (ViewGroup) null);
    }

    public boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        boolean z2 = this.mHasSizeCompat;
        int i = this.mCameraCompatControlState;
        this.mHasSizeCompat = taskInfo.topActivityInSizeCompat;
        this.mCameraCompatControlState = taskInfo.cameraCompatControlState;
        if (!super.updateCompatInfo(taskInfo, taskListener, z)) {
            return false;
        }
        if (z2 == this.mHasSizeCompat && i == this.mCameraCompatControlState) {
            return true;
        }
        updateVisibilityOfViews();
        return true;
    }

    public void onRestartButtonClicked() {
        this.mCallback.onSizeCompatRestartButtonClicked(this.mTaskId);
    }

    public void onCameraTreatmentButtonClicked() {
        if (!shouldShowCameraControl()) {
            Log.w(getTag(), "Camera compat shouldn't receive clicks in the hidden state.");
            return;
        }
        int i = 1;
        if (this.mCameraCompatControlState == 1) {
            i = 2;
        }
        this.mCameraCompatControlState = i;
        this.mCallback.onCameraControlStateUpdated(this.mTaskId, i);
        this.mLayout.updateCameraTreatmentButton(this.mCameraCompatControlState);
    }

    public void onCameraDismissButtonClicked() {
        if (!shouldShowCameraControl()) {
            Log.w(getTag(), "Camera compat shouldn't receive clicks in the hidden state.");
            return;
        }
        this.mCameraCompatControlState = 3;
        this.mCallback.onCameraControlStateUpdated(this.mTaskId, 3);
        this.mLayout.setCameraControlVisibility(false);
    }

    public void onRestartButtonLongClicked() {
        CompatUILayout compatUILayout = this.mLayout;
        if (compatUILayout != null) {
            compatUILayout.setSizeCompatHintVisibility(true);
        }
    }

    public void onCameraButtonLongClicked() {
        CompatUILayout compatUILayout = this.mLayout;
        if (compatUILayout != null) {
            compatUILayout.setCameraCompatHintVisibility(true);
        }
    }

    @VisibleForTesting
    public void updateSurfacePosition() {
        int i;
        int i2;
        if (this.mLayout != null) {
            Rect taskBounds = getTaskBounds();
            Rect taskStableBounds = getTaskStableBounds();
            if (getLayoutDirection() == 1) {
                i2 = taskStableBounds.left;
                i = taskBounds.left;
            } else {
                i2 = taskStableBounds.right - taskBounds.left;
                i = this.mLayout.getMeasuredWidth();
            }
            updateSurfacePosition(i2 - i, (taskStableBounds.bottom - taskBounds.top) - this.mLayout.getMeasuredHeight());
        }
    }

    public final void updateVisibilityOfViews() {
        CompatUILayout compatUILayout = this.mLayout;
        if (compatUILayout != null) {
            compatUILayout.setRestartButtonVisibility(this.mHasSizeCompat);
            if (this.mHasSizeCompat && !this.mCompatUIHintsState.mHasShownSizeCompatHint) {
                this.mLayout.setSizeCompatHintVisibility(true);
                this.mCompatUIHintsState.mHasShownSizeCompatHint = true;
            }
            this.mLayout.setCameraControlVisibility(shouldShowCameraControl());
            if (shouldShowCameraControl() && !this.mCompatUIHintsState.mHasShownCameraCompatHint) {
                this.mLayout.setCameraCompatHintVisibility(true);
                this.mCompatUIHintsState.mHasShownCameraCompatHint = true;
            }
            if (shouldShowCameraControl()) {
                this.mLayout.updateCameraTreatmentButton(this.mCameraCompatControlState);
            }
        }
    }

    public final boolean shouldShowCameraControl() {
        int i = this.mCameraCompatControlState;
        return (i == 0 || i == 3) ? false : true;
    }
}
