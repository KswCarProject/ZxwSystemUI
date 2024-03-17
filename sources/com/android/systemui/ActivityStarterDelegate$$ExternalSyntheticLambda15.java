package com.android.systemui;

import android.content.Intent;
import android.os.UserHandle;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda15 implements Consumer {
    public final /* synthetic */ Intent f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ UserHandle f$4;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda15(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, boolean z2, UserHandle userHandle) {
        this.f$0 = intent;
        this.f$1 = z;
        this.f$2 = controller;
        this.f$3 = z2;
        this.f$4 = userHandle;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).startActivity(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
