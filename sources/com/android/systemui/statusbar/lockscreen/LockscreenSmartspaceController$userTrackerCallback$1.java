package com.android.systemui.statusbar.lockscreen;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$userTrackerCallback$1 implements UserTracker.Callback {
    public final /* synthetic */ LockscreenSmartspaceController this$0;

    public LockscreenSmartspaceController$userTrackerCallback$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void onUserChanged(int i, @NotNull Context context) {
        this.this$0.execution.assertIsMainThread();
        this.this$0.reloadSmartspace();
    }
}
