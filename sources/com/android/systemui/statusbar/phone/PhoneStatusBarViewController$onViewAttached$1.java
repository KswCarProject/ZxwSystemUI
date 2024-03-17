package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.ViewTreeObserver;

/* compiled from: PhoneStatusBarViewController.kt */
public final class PhoneStatusBarViewController$onViewAttached$1 implements ViewTreeObserver.OnPreDrawListener {
    public final /* synthetic */ View[] $viewsToAnimate;
    public final /* synthetic */ PhoneStatusBarViewController this$0;

    public PhoneStatusBarViewController$onViewAttached$1(PhoneStatusBarViewController phoneStatusBarViewController, View[] viewArr) {
        this.this$0 = phoneStatusBarViewController;
        this.$viewsToAnimate = viewArr;
    }

    public boolean onPreDraw() {
        this.this$0.moveFromCenterAnimationController.onViewsReady(this.$viewsToAnimate);
        ((PhoneStatusBarView) this.this$0.mView).getViewTreeObserver().removeOnPreDrawListener(this);
        return true;
    }
}
