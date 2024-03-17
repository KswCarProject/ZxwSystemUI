package com.android.wm.shell.pip.tv;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Insets;
import android.util.Size;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;

public class TvPipBoundsState extends PipBoundsState {
    public float mDesiredTvExpandedAspectRatio;
    public final boolean mIsTvExpandedPipSupported;
    public boolean mIsTvPipExpanded;
    public Insets mPipMenuPermanentDecorInsets;
    public Insets mPipMenuTemporaryDecorInsets;
    public Size mTvExpandedSize;
    public int mTvFixedPipOrientation;
    public int mTvPipGravity;
    public boolean mTvPipManuallyCollapsed;

    public TvPipBoundsState(Context context) {
        super(context);
        Insets insets = Insets.NONE;
        this.mPipMenuPermanentDecorInsets = insets;
        this.mPipMenuTemporaryDecorInsets = insets;
        this.mIsTvExpandedPipSupported = context.getPackageManager().hasSystemFeature("android.software.expanded_picture_in_picture");
    }

    public void setBoundsStateForEntry(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, PipBoundsAlgorithm pipBoundsAlgorithm) {
        super.setBoundsStateForEntry(componentName, activityInfo, pictureInPictureParams, pipBoundsAlgorithm);
        if (pictureInPictureParams != null) {
            setDesiredTvExpandedAspectRatio(pictureInPictureParams.getExpandedAspectRatioFloat(), true);
        }
    }

    public void resetTvPipState() {
        this.mTvFixedPipOrientation = 0;
        this.mTvPipGravity = 85;
    }

    public void setTvExpandedSize(Size size) {
        this.mTvExpandedSize = size;
    }

    public Size getTvExpandedSize() {
        return this.mTvExpandedSize;
    }

    public void setDesiredTvExpandedAspectRatio(float f, boolean z) {
        int i;
        if (z || (i = this.mTvFixedPipOrientation) == 0) {
            this.mDesiredTvExpandedAspectRatio = f;
            resetTvPipState();
        } else if ((f > 1.0f && i == 2) || ((f <= 1.0f && i == 1) || f == 0.0f)) {
            this.mDesiredTvExpandedAspectRatio = f;
        }
    }

    public float getDesiredTvExpandedAspectRatio() {
        return this.mDesiredTvExpandedAspectRatio;
    }

    public void setTvFixedPipOrientation(int i) {
        this.mTvFixedPipOrientation = i;
    }

    public int getTvFixedPipOrientation() {
        return this.mTvFixedPipOrientation;
    }

    public void setTvPipGravity(int i) {
        this.mTvPipGravity = i;
    }

    public int getTvPipGravity() {
        return this.mTvPipGravity;
    }

    public void setTvPipExpanded(boolean z) {
        this.mIsTvPipExpanded = z;
    }

    public boolean isTvPipExpanded() {
        return this.mIsTvPipExpanded;
    }

    public void setTvPipManuallyCollapsed(boolean z) {
        this.mTvPipManuallyCollapsed = z;
    }

    public boolean isTvPipManuallyCollapsed() {
        return this.mTvPipManuallyCollapsed;
    }

    public boolean isTvExpandedPipSupported() {
        return this.mIsTvExpandedPipSupported;
    }

    public void setPipMenuPermanentDecorInsets(Insets insets) {
        this.mPipMenuPermanentDecorInsets = insets;
    }

    public Insets getPipMenuPermanentDecorInsets() {
        return this.mPipMenuPermanentDecorInsets;
    }

    public void setPipMenuTemporaryDecorInsets(Insets insets) {
        this.mPipMenuTemporaryDecorInsets = insets;
    }

    public Insets getPipMenuTemporaryDecorInsets() {
        return this.mPipMenuTemporaryDecorInsets;
    }
}
