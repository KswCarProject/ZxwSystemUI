package com.android.systemui.statusbar.phone;

import com.android.systemui.tuner.TunerService;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationShadeWindowViewController$$ExternalSyntheticLambda2 implements TunerService.Tunable {
    public final /* synthetic */ NotificationShadeWindowViewController f$0;

    public /* synthetic */ NotificationShadeWindowViewController$$ExternalSyntheticLambda2(NotificationShadeWindowViewController notificationShadeWindowViewController) {
        this.f$0 = notificationShadeWindowViewController;
    }

    public final void onTuningChanged(String str, String str2) {
        this.f$0.lambda$setupExpandedStatusBar$0(str, str2);
    }
}
