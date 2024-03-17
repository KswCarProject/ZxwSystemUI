package com.android.wm.shell.transition;

import android.animation.ValueAnimator;
import java.util.ArrayList;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DefaultTransitionHandler$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ ArrayList f$0;
    public final /* synthetic */ ValueAnimator f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ DefaultTransitionHandler$$ExternalSyntheticLambda8(ArrayList arrayList, ValueAnimator valueAnimator, Runnable runnable) {
        this.f$0 = arrayList;
        this.f$1 = valueAnimator;
        this.f$2 = runnable;
    }

    public final void run() {
        DefaultTransitionHandler.lambda$startSurfaceAnimation$5(this.f$0, this.f$1, this.f$2);
    }
}
