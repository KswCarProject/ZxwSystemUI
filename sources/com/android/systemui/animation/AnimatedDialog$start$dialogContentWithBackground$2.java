package com.android.systemui.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$start$dialogContentWithBackground$2 implements View.OnLayoutChangeListener {
    public final /* synthetic */ FrameLayout $dialogContentWithBackground;
    public final /* synthetic */ Window $window;

    public AnimatedDialog$start$dialogContentWithBackground$2(Window window, FrameLayout frameLayout) {
        this.$window = window;
        this.$dialogContentWithBackground = frameLayout;
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.$window.getAttributes().width != -1 || this.$window.getAttributes().height != -1) {
            ViewGroup.LayoutParams layoutParams = this.$dialogContentWithBackground.getLayoutParams();
            layoutParams.width = this.$window.getAttributes().width;
            layoutParams.height = this.$window.getAttributes().height;
            this.$dialogContentWithBackground.setLayoutParams(layoutParams);
            this.$window.setLayout(-1, -1);
        }
    }
}
