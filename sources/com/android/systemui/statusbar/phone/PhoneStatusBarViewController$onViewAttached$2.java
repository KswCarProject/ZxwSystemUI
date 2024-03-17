package com.android.systemui.statusbar.phone;

import android.view.View;

/* compiled from: PhoneStatusBarViewController.kt */
public final class PhoneStatusBarViewController$onViewAttached$2 implements View.OnLayoutChangeListener {
    public final /* synthetic */ PhoneStatusBarViewController this$0;

    public PhoneStatusBarViewController$onViewAttached$2(PhoneStatusBarViewController phoneStatusBarViewController) {
        this.this$0 = phoneStatusBarViewController;
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i3 - i != i7 - i5) {
            this.this$0.moveFromCenterAnimationController.onStatusBarWidthChanged();
        }
    }
}
