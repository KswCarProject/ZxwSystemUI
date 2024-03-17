package com.android.systemui;

import android.content.Intent;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda8 implements Consumer {
    public final /* synthetic */ Intent f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ ActivityStarter.Callback f$2;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda8(Intent intent, boolean z, ActivityStarter.Callback callback) {
        this.f$0 = intent;
        this.f$1 = z;
        this.f$2 = callback;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).startActivity(this.f$0, this.f$1, this.f$2);
    }
}
