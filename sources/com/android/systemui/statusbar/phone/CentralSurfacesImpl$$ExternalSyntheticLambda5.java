package com.android.systemui.statusbar.phone;

import android.content.Intent;
import android.os.UserHandle;
import android.view.RemoteAnimationAdapter;
import kotlin.jvm.functions.Function1;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda5 implements Function1 {
    public final /* synthetic */ CentralSurfacesImpl f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ UserHandle f$2;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda5(CentralSurfacesImpl centralSurfacesImpl, Intent intent, UserHandle userHandle) {
        this.f$0 = centralSurfacesImpl;
        this.f$1 = intent;
        this.f$2 = userHandle;
    }

    public final Object invoke(Object obj) {
        return this.f$0.lambda$startActivity$21(this.f$1, this.f$2, (RemoteAnimationAdapter) obj);
    }
}
