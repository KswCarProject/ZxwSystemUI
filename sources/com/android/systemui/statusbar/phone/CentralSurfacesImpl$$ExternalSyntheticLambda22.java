package com.android.systemui.statusbar.phone;

import android.content.Intent;
import android.os.UserHandle;
import android.view.RemoteAnimationAdapter;
import kotlin.jvm.functions.Function1;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda22 implements Function1 {
    public final /* synthetic */ CentralSurfacesImpl f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ Intent f$2;
    public final /* synthetic */ int[] f$3;
    public final /* synthetic */ UserHandle f$4;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda22(CentralSurfacesImpl centralSurfacesImpl, boolean z, Intent intent, int[] iArr, UserHandle userHandle) {
        this.f$0 = centralSurfacesImpl;
        this.f$1 = z;
        this.f$2 = intent;
        this.f$3 = iArr;
        this.f$4 = userHandle;
    }

    public final Object invoke(Object obj) {
        return this.f$0.lambda$startActivityDismissingKeyguard$25(this.f$1, this.f$2, this.f$3, this.f$4, (RemoteAnimationAdapter) obj);
    }
}
