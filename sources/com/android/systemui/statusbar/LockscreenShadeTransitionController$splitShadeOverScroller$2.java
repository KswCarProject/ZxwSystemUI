package com.android.systemui.statusbar;

import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$splitShadeOverScroller$2 extends Lambda implements Function0<SplitShadeLockScreenOverScroller> {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$splitShadeOverScroller$2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    @NotNull
    public final SplitShadeLockScreenOverScroller invoke() {
        SplitShadeLockScreenOverScroller.Factory access$getSplitShadeOverScrollerFactory$p = this.this$0.splitShadeOverScrollerFactory;
        QS qs = this.this$0.getQS();
        NotificationStackScrollLayoutController access$getNsslController$p = this.this$0.nsslController;
        if (access$getNsslController$p == null) {
            access$getNsslController$p = null;
        }
        return access$getSplitShadeOverScrollerFactory$p.create(qs, access$getNsslController$p);
    }
}
