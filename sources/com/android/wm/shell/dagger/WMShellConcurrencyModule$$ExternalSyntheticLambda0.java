package com.android.wm.shell.dagger;

import android.animation.AnimationHandler;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WMShellConcurrencyModule$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AnimationHandler f$0;

    public /* synthetic */ WMShellConcurrencyModule$$ExternalSyntheticLambda0(AnimationHandler animationHandler) {
        this.f$0 = animationHandler;
    }

    public final void run() {
        this.f$0.setProvider(new SfVsyncFrameCallbackProvider());
    }
}
