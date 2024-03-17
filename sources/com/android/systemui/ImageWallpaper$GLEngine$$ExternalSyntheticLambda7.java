package com.android.systemui;

import android.view.SurfaceHolder;
import com.android.systemui.ImageWallpaper;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ ImageWallpaper.GLEngine f$0;
    public final /* synthetic */ SurfaceHolder f$1;

    public /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda7(ImageWallpaper.GLEngine gLEngine, SurfaceHolder surfaceHolder) {
        this.f$0 = gLEngine;
        this.f$1 = surfaceHolder;
    }

    public final void run() {
        this.f$0.lambda$onSurfaceCreated$5(this.f$1);
    }
}
