package com.android.wm.shell.startingsurface;

import android.graphics.Rect;
import android.view.SurfaceControl;
import android.window.SplashScreenView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplashscreenContentDrawer$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SplashscreenContentDrawer f$0;
    public final /* synthetic */ SplashScreenView f$1;
    public final /* synthetic */ SurfaceControl f$2;
    public final /* synthetic */ Rect f$3;
    public final /* synthetic */ Runnable f$4;

    public /* synthetic */ SplashscreenContentDrawer$$ExternalSyntheticLambda1(SplashscreenContentDrawer splashscreenContentDrawer, SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, Runnable runnable) {
        this.f$0 = splashscreenContentDrawer;
        this.f$1 = splashScreenView;
        this.f$2 = surfaceControl;
        this.f$3 = rect;
        this.f$4 = runnable;
    }

    public final void run() {
        this.f$0.lambda$applyExitAnimation$8(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
