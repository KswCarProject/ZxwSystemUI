package com.android.systemui.qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$safetyCenterReceiver$1 extends BroadcastReceiver {
    public final /* synthetic */ HeaderPrivacyIconsController this$0;

    public HeaderPrivacyIconsController$safetyCenterReceiver$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        HeaderPrivacyIconsController headerPrivacyIconsController = this.this$0;
        headerPrivacyIconsController.safetyCenterEnabled = headerPrivacyIconsController.safetyCenterManager.isSafetyCenterEnabled();
    }
}
