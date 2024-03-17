package com.android.systemui.qs;

import android.content.Intent;
import com.android.systemui.animation.ActivityLaunchAnimator;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/* compiled from: HeaderPrivacyIconsController.kt */
public final class HeaderPrivacyIconsController$showSafetyCenter$1 implements Runnable {
    public final /* synthetic */ HeaderPrivacyIconsController this$0;

    public HeaderPrivacyIconsController$showSafetyCenter$1(HeaderPrivacyIconsController headerPrivacyIconsController) {
        this.this$0 = headerPrivacyIconsController;
    }

    public final void run() {
        ArrayList arrayList = new ArrayList(this.this$0.permGroupUsage());
        this.this$0.privacyLogger.logUnfilteredPermGroupUsage(arrayList);
        final Intent intent = new Intent("android.intent.action.VIEW_SAFETY_CENTER_QS");
        intent.putParcelableArrayListExtra("android.permission.extra.PERMISSION_USAGES", arrayList);
        intent.setFlags(268435456);
        Executor access$getUiExecutor$p = this.this$0.uiExecutor;
        final HeaderPrivacyIconsController headerPrivacyIconsController = this.this$0;
        access$getUiExecutor$p.execute(new Runnable() {
            public final void run() {
                headerPrivacyIconsController.activityStarter.startActivity(intent, true, ActivityLaunchAnimator.Controller.Companion.fromView$default(ActivityLaunchAnimator.Controller.Companion, headerPrivacyIconsController.privacyChip, (Integer) null, 2, (Object) null));
            }
        });
    }
}
