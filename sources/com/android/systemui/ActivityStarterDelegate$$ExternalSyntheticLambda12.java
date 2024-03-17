package com.android.systemui;

import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda12 implements Consumer {
    public final /* synthetic */ ActivityStarter.OnDismissAction f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda12(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        this.f$0 = onDismissAction;
        this.f$1 = runnable;
        this.f$2 = z;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).dismissKeyguardThenExecute(this.f$0, this.f$1, this.f$2);
    }
}
