package com.android.systemui;

import android.app.PendingIntent;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda9(PendingIntent pendingIntent, Runnable runnable) {
        this.f$0 = pendingIntent;
        this.f$1 = runnable;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).startPendingIntentDismissingKeyguard(this.f$0, this.f$1);
    }
}
