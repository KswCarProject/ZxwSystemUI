package com.android.systemui.statusbar;

import android.os.SystemClock;
import android.view.View;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$bindController$1 implements View.OnClickListener {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    public LockscreenShadeTransitionController$bindController$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final void onClick(View view) {
        if (this.this$0.statusBarStateController.getState() == 1) {
            this.this$0.getCentralSurfaces().wakeUpIfDozing(SystemClock.uptimeMillis(), view, "SHADE_CLICK");
            LockscreenShadeTransitionController.goToLockedShade$default(this.this$0, view, false, 2, (Object) null);
        }
    }
}
