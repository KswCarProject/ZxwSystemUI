package com.android.wm.shell.startingsurface;

import android.os.IBinder;
import android.widget.FrameLayout;
import com.android.wm.shell.startingsurface.StartingSurfaceDrawer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StartingSurfaceDrawer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StartingSurfaceDrawer f$0;
    public final /* synthetic */ StartingSurfaceDrawer.SplashScreenViewSupplier f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ IBinder f$3;
    public final /* synthetic */ FrameLayout f$4;

    public /* synthetic */ StartingSurfaceDrawer$$ExternalSyntheticLambda0(StartingSurfaceDrawer startingSurfaceDrawer, StartingSurfaceDrawer.SplashScreenViewSupplier splashScreenViewSupplier, int i, IBinder iBinder, FrameLayout frameLayout) {
        this.f$0 = startingSurfaceDrawer;
        this.f$1 = splashScreenViewSupplier;
        this.f$2 = i;
        this.f$3 = iBinder;
        this.f$4 = frameLayout;
    }

    public final void run() {
        this.f$0.lambda$addSplashScreenStartingWindow$1(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
