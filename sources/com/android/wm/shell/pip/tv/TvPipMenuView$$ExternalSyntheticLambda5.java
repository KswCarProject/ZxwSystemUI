package com.android.wm.shell.pip.tv;

import android.graphics.Rect;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvPipMenuView$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ TvPipMenuView f$0;
    public final /* synthetic */ Rect f$1;

    public /* synthetic */ TvPipMenuView$$ExternalSyntheticLambda5(TvPipMenuView tvPipMenuView, Rect rect) {
        this.f$0 = tvPipMenuView;
        this.f$1 = rect;
    }

    public final void run() {
        this.f$0.lambda$onPipTransitionStarted$3(this.f$1);
    }
}
