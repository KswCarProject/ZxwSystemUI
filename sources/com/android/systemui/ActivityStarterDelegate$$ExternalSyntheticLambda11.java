package com.android.systemui;

import android.app.PendingIntent;
import android.view.View;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda11 implements Consumer {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ View f$2;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda11(PendingIntent pendingIntent, Runnable runnable, View view) {
        this.f$0 = pendingIntent;
        this.f$1 = runnable;
        this.f$2 = view;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).startPendingIntentDismissingKeyguard(this.f$0, this.f$1, this.f$2);
    }
}
