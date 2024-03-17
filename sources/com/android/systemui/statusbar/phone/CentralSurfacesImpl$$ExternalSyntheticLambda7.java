package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import android.view.RemoteAnimationAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda7 implements ActivityLaunchAnimator.PendingIntentStarter {
    public final /* synthetic */ CentralSurfacesImpl f$0;
    public final /* synthetic */ PendingIntent f$1;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda7(CentralSurfacesImpl centralSurfacesImpl, PendingIntent pendingIntent) {
        this.f$0 = centralSurfacesImpl;
        this.f$1 = pendingIntent;
    }

    public final int startPendingIntent(RemoteAnimationAdapter remoteAnimationAdapter) {
        return this.f$0.lambda$startPendingIntentDismissingKeyguard$39(this.f$1, remoteAnimationAdapter);
    }
}
