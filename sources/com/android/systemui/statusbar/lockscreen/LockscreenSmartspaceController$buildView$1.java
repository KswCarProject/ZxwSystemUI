package com.android.systemui.statusbar.lockscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$buildView$1 implements BcSmartspaceDataPlugin.IntentStarter {
    public final /* synthetic */ LockscreenSmartspaceController this$0;

    public LockscreenSmartspaceController$buildView$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void startIntent(@NotNull View view, @NotNull Intent intent, boolean z) {
        this.this$0.activityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) null, z);
    }

    public void startPendingIntent(@NotNull PendingIntent pendingIntent, boolean z) {
        if (z) {
            pendingIntent.send();
        } else {
            this.this$0.activityStarter.startPendingIntentDismissingKeyguard(pendingIntent);
        }
    }
}
