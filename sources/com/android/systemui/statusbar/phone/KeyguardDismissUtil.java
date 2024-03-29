package com.android.systemui.statusbar.phone;

import android.util.Log;
import com.android.systemui.plugins.ActivityStarter;

public class KeyguardDismissUtil implements KeyguardDismissHandler {
    public volatile KeyguardDismissHandler mDismissHandler;

    public void setDismissHandler(KeyguardDismissHandler keyguardDismissHandler) {
        this.mDismissHandler = keyguardDismissHandler;
    }

    public void executeWhenUnlocked(ActivityStarter.OnDismissAction onDismissAction, boolean z, boolean z2) {
        KeyguardDismissHandler keyguardDismissHandler = this.mDismissHandler;
        if (keyguardDismissHandler == null) {
            Log.wtf("KeyguardDismissUtil", "KeyguardDismissHandler not set.");
            onDismissAction.onDismiss();
            return;
        }
        keyguardDismissHandler.executeWhenUnlocked(onDismissAction, z, z2);
    }
}
