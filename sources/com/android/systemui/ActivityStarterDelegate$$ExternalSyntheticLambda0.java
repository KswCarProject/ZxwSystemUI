package com.android.systemui;

import android.app.PendingIntent;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda0(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller) {
        this.f$0 = pendingIntent;
        this.f$1 = runnable;
        this.f$2 = controller;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).startPendingIntentDismissingKeyguard(this.f$0, this.f$1, this.f$2);
    }
}
