package com.android.systemui.qs;

import kotlin.Unit;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$showDialog$1$4 implements Runnable {
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$showDialog$1$4(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public final void run() {
        Object access$getLock$p = this.this$0.lock;
        FgsManagerController fgsManagerController = this.this$0;
        synchronized (access$getLock$p) {
            fgsManagerController.updateAppItemsLocked();
            Unit unit = Unit.INSTANCE;
        }
    }
}
