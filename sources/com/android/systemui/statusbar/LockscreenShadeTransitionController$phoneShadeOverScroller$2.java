package com.android.systemui.statusbar;

import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$phoneShadeOverScroller$2 extends Lambda implements Function0<SingleShadeLockScreenOverScroller> {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$phoneShadeOverScroller$2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    @NotNull
    public final SingleShadeLockScreenOverScroller invoke() {
        SingleShadeLockScreenOverScroller.Factory access$getSingleShadeOverScrollerFactory$p = this.this$0.singleShadeOverScrollerFactory;
        NotificationStackScrollLayoutController access$getNsslController$p = this.this$0.nsslController;
        if (access$getNsslController$p == null) {
            access$getNsslController$p = null;
        }
        return access$getSingleShadeOverScrollerFactory$p.create(access$getNsslController$p);
    }
}
