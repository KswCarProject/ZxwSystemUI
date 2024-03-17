package com.android.systemui.unfold;

import android.content.res.Configuration;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.view.WindowlessWindowManager;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
public final class UnfoldLightRevealOverlayAnimation$init$1<T> implements Consumer {
    public final /* synthetic */ UnfoldLightRevealOverlayAnimation this$0;

    public UnfoldLightRevealOverlayAnimation$init$1(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation) {
        this.this$0 = unfoldLightRevealOverlayAnimation;
    }

    public final void accept(final SurfaceControl.Builder builder) {
        Executor access$getExecutor$p = this.this$0.executor;
        final UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = this.this$0;
        access$getExecutor$p.execute(new Runnable() {
            public final void run() {
                unfoldLightRevealOverlayAnimation.overlayContainer = builder.build();
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                SurfaceControl access$getOverlayContainer$p = unfoldLightRevealOverlayAnimation.overlayContainer;
                if (access$getOverlayContainer$p == null) {
                    access$getOverlayContainer$p = null;
                }
                SurfaceControl.Transaction layer = transaction.setLayer(access$getOverlayContainer$p, Integer.MAX_VALUE);
                SurfaceControl access$getOverlayContainer$p2 = unfoldLightRevealOverlayAnimation.overlayContainer;
                if (access$getOverlayContainer$p2 == null) {
                    access$getOverlayContainer$p2 = null;
                }
                layer.show(access$getOverlayContainer$p2).apply();
                UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = unfoldLightRevealOverlayAnimation;
                Configuration configuration = unfoldLightRevealOverlayAnimation.context.getResources().getConfiguration();
                SurfaceControl access$getOverlayContainer$p3 = unfoldLightRevealOverlayAnimation.overlayContainer;
                if (access$getOverlayContainer$p3 == null) {
                    access$getOverlayContainer$p3 = null;
                }
                unfoldLightRevealOverlayAnimation.wwm = new WindowlessWindowManager(configuration, access$getOverlayContainer$p3, (IBinder) null);
            }
        });
    }
}
