package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class PipMenuIconsAlgorithm {
    public View mDismissButton;
    public View mDragHandle;
    public View mEnterSplitButton;
    public View mSettingsButton;
    public ViewGroup mTopEndContainer;
    public ViewGroup mViewRoot;

    public void onBoundsChanged(Rect rect) {
    }

    public PipMenuIconsAlgorithm(Context context) {
    }

    public void bindViews(ViewGroup viewGroup, ViewGroup viewGroup2, View view, View view2, View view3, View view4) {
        this.mViewRoot = viewGroup;
        this.mTopEndContainer = viewGroup2;
        this.mDragHandle = view;
        this.mEnterSplitButton = view2;
        this.mSettingsButton = view3;
        this.mDismissButton = view4;
    }
}
