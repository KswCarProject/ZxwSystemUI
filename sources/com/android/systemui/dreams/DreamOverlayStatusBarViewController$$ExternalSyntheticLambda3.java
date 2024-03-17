package com.android.systemui.dreams;

import com.android.systemui.statusbar.window.StatusBarWindowStateListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayStatusBarViewController$$ExternalSyntheticLambda3 implements StatusBarWindowStateListener {
    public final /* synthetic */ DreamOverlayStatusBarViewController f$0;

    public /* synthetic */ DreamOverlayStatusBarViewController$$ExternalSyntheticLambda3(DreamOverlayStatusBarViewController dreamOverlayStatusBarViewController) {
        this.f$0 = dreamOverlayStatusBarViewController;
    }

    public final void onStatusBarWindowStateChanged(int i) {
        this.f$0.onSystemStatusBarStateChanged(i);
    }
}
