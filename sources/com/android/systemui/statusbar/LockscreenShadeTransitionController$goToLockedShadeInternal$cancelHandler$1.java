package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1 implements Runnable {
    public final /* synthetic */ Runnable $cancelAction;
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    public LockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1(LockscreenShadeTransitionController lockscreenShadeTransitionController, Runnable runnable) {
        this.this$0 = lockscreenShadeTransitionController;
        this.$cancelAction = runnable;
    }

    public final void run() {
        NotificationEntry access$getDraggedDownEntry$p = this.this$0.draggedDownEntry;
        if (access$getDraggedDownEntry$p != null) {
            LockscreenShadeTransitionController lockscreenShadeTransitionController = this.this$0;
            access$getDraggedDownEntry$p.setUserLocked(false);
            access$getDraggedDownEntry$p.notifyHeightChanged(false);
            lockscreenShadeTransitionController.draggedDownEntry = null;
        }
        Runnable runnable = this.$cancelAction;
        if (runnable != null) {
            runnable.run();
        }
    }
}
