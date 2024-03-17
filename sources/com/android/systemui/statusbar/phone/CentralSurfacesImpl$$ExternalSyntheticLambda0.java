package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ CentralSurfacesImpl f$0;
    public final /* synthetic */ PendingIntent f$1;
    public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda0(CentralSurfacesImpl centralSurfacesImpl, PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller) {
        this.f$0 = centralSurfacesImpl;
        this.f$1 = pendingIntent;
        this.f$2 = controller;
    }

    public final void run() {
        this.f$0.lambda$postStartActivityDismissingKeyguard$32(this.f$1, this.f$2);
    }
}
