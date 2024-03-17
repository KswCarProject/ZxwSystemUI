package com.android.systemui.statusbar.window;

import com.android.systemui.statusbar.CommandQueue;

/* compiled from: StatusBarWindowStateController.kt */
public final class StatusBarWindowStateController$commandQueueCallback$1 implements CommandQueue.Callbacks {
    public final /* synthetic */ StatusBarWindowStateController this$0;

    public StatusBarWindowStateController$commandQueueCallback$1(StatusBarWindowStateController statusBarWindowStateController) {
        this.this$0 = statusBarWindowStateController;
    }

    public void setWindowState(int i, int i2, int i3) {
        this.this$0.setWindowState(i, i2, i3);
    }
}
