package com.android.systemui.statusbar.policy;

import android.view.View;

/* compiled from: RemoteInputViewController.kt */
public final class RemoteInputViewControllerImpl$onFocusChangeListener$1 implements View.OnFocusChangeListener {
    public final /* synthetic */ RemoteInputViewControllerImpl this$0;

    public RemoteInputViewControllerImpl$onFocusChangeListener$1(RemoteInputViewControllerImpl remoteInputViewControllerImpl) {
        this.this$0 = remoteInputViewControllerImpl;
    }

    public final void onFocusChange(View view, boolean z) {
        this.this$0.remoteInputQuickSettingsDisabler.setRemoteInputActive(z);
    }
}
