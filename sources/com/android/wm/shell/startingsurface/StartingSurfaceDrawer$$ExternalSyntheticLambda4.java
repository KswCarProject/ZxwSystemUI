package com.android.wm.shell.startingsurface;

import android.os.Bundle;
import android.os.RemoteCallback;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StartingSurfaceDrawer$$ExternalSyntheticLambda4 implements RemoteCallback.OnResultListener {
    public final /* synthetic */ StartingSurfaceDrawer f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ StartingSurfaceDrawer$$ExternalSyntheticLambda4(StartingSurfaceDrawer startingSurfaceDrawer, int i) {
        this.f$0 = startingSurfaceDrawer;
        this.f$1 = i;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$copySplashScreenView$4(this.f$1, bundle);
    }
}
