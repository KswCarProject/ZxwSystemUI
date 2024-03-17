package com.android.systemui;

import android.content.Intent;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda10 implements Consumer {
    public final /* synthetic */ Intent f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda10(Intent intent, int i) {
        this.f$0 = intent;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).postStartActivityDismissingKeyguard(this.f$0, this.f$1);
    }
}
