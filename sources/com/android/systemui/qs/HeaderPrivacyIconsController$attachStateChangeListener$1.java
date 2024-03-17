package com.android.systemui.qs;

import android.content.IntentFilter;
import android.os.UserHandle;
import android.view.View;
import com.android.systemui.broadcast.BroadcastDispatcher;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$attachStateChangeListener$1 implements View.OnAttachStateChangeListener {
    public final /* synthetic */ HeaderPrivacyIconsController this$0;

    public HeaderPrivacyIconsController$attachStateChangeListener$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public void onViewAttachedToWindow(@NotNull View view) {
        BroadcastDispatcher.registerReceiver$default(this.this$0.broadcastDispatcher, this.this$0.safetyCenterReceiver, new IntentFilter("android.safetycenter.action.SAFETY_CENTER_ENABLED_CHANGED"), this.this$0.backgroundExecutor, (UserHandle) null, 0, (String) null, 56, (Object) null);
    }

    public void onViewDetachedFromWindow(@NotNull View view) {
        this.this$0.broadcastDispatcher.unregisterReceiver(this.this$0.safetyCenterReceiver);
    }
}
