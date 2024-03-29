package com.android.systemui.statusbar.phone;

import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.systemui.util.Assert;
import org.jetbrains.annotations.Nullable;

/* compiled from: KeyguardLiftController.kt */
public final class KeyguardLiftController$listener$1 extends TriggerEventListener {
    public final /* synthetic */ KeyguardLiftController this$0;

    public KeyguardLiftController$listener$1(KeyguardLiftController keyguardLiftController) {
        this.this$0 = keyguardLiftController;
    }

    public void onTrigger(@Nullable TriggerEvent triggerEvent) {
        Assert.isMainThread();
        this.this$0.isListening = false;
        this.this$0.updateListeningState();
        this.this$0.keyguardUpdateMonitor.requestFaceAuth(true);
        this.this$0.keyguardUpdateMonitor.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.WAKE, "KeyguardLiftController");
    }
}
