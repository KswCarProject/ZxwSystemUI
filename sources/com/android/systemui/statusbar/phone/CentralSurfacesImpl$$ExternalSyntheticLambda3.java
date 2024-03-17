package com.android.systemui.statusbar.phone;

import android.content.Intent;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ CentralSurfacesImpl f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda3(CentralSurfacesImpl centralSurfacesImpl, Intent intent, ActivityLaunchAnimator.Controller controller) {
        this.f$0 = centralSurfacesImpl;
        this.f$1 = intent;
        this.f$2 = controller;
    }

    public final void run() {
        this.f$0.lambda$postStartActivityDismissingKeyguard$33(this.f$1, this.f$2);
    }
}
