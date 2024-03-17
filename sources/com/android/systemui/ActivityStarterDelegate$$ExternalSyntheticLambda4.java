package com.android.systemui;

import android.app.PendingIntent;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ PendingIntent f$0;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda4(PendingIntent pendingIntent) {
        this.f$0 = pendingIntent;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).startPendingIntentDismissingKeyguard(this.f$0);
    }
}
