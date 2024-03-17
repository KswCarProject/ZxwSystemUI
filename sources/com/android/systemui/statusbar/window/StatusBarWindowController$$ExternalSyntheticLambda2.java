package com.android.systemui.statusbar.window;

import com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarWindowController$$ExternalSyntheticLambda2 implements StatusBarContentInsetsChangedListener {
    public final /* synthetic */ StatusBarWindowController f$0;

    public /* synthetic */ StatusBarWindowController$$ExternalSyntheticLambda2(StatusBarWindowController statusBarWindowController) {
        this.f$0 = statusBarWindowController;
    }

    public final void onStatusBarContentInsetsChanged() {
        this.f$0.calculateStatusBarLocationsForAllRotations();
    }
}
