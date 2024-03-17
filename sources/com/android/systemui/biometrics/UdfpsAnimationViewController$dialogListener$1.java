package com.android.systemui.biometrics;

import com.android.systemui.statusbar.phone.SystemUIDialogManager;

/* compiled from: UdfpsAnimationViewController.kt */
public final class UdfpsAnimationViewController$dialogListener$1 implements SystemUIDialogManager.Listener {
    public final /* synthetic */ UdfpsAnimationViewController<T> this$0;

    public UdfpsAnimationViewController$dialogListener$1(UdfpsAnimationViewController<T> udfpsAnimationViewController) {
        this.this$0 = udfpsAnimationViewController;
    }

    public final void shouldHideAffordances(boolean z) {
        this.this$0.runDialogAlphaAnimator();
    }
}
