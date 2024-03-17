package com.android.systemui.statusbar.lockscreen;

import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;

/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$statusBarStateListener$1 implements StatusBarStateController.StateListener {
    public final /* synthetic */ LockscreenSmartspaceController this$0;

    public LockscreenSmartspaceController$statusBarStateListener$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void onDozeAmountChanged(float f, float f2) {
        this.this$0.execution.assertIsMainThread();
        for (BcSmartspaceDataPlugin.SmartspaceView dozeAmount : this.this$0.smartspaceViews) {
            dozeAmount.setDozeAmount(f2);
        }
    }
}
