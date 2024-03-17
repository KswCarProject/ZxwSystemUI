package com.android.systemui.statusbar.lockscreen;

import android.view.View;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$stateChangeListener$1 implements View.OnAttachStateChangeListener {
    public final /* synthetic */ LockscreenSmartspaceController this$0;

    public LockscreenSmartspaceController$stateChangeListener$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void onViewAttachedToWindow(@NotNull View view) {
        this.this$0.smartspaceViews.add((BcSmartspaceDataPlugin.SmartspaceView) view);
        this.this$0.connectSession();
        this.this$0.updateTextColorFromWallpaper();
        this.this$0.statusBarStateListener.onDozeAmountChanged(0.0f, this.this$0.statusBarStateController.getDozeAmount());
    }

    public void onViewDetachedFromWindow(@NotNull View view) {
        this.this$0.smartspaceViews.remove((BcSmartspaceDataPlugin.SmartspaceView) view);
        if (this.this$0.smartspaceViews.isEmpty()) {
            this.this$0.disconnect();
        }
    }
}
