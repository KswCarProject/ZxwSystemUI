package com.android.wm.shell.transition;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import java.util.ArrayList;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DefaultTransitionHandler$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ ValueAnimator f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;
    public final /* synthetic */ ShellExecutor f$10;
    public final /* synthetic */ ArrayList f$11;
    public final /* synthetic */ Runnable f$12;
    public final /* synthetic */ SurfaceControl f$2;
    public final /* synthetic */ Animation f$3;
    public final /* synthetic */ Transformation f$4;
    public final /* synthetic */ float[] f$5;
    public final /* synthetic */ Point f$6;
    public final /* synthetic */ float f$7;
    public final /* synthetic */ Rect f$8;
    public final /* synthetic */ TransactionPool f$9;

    public /* synthetic */ DefaultTransitionHandler$$ExternalSyntheticLambda5(ValueAnimator valueAnimator, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, Point point, float f, Rect rect, TransactionPool transactionPool, ShellExecutor shellExecutor, ArrayList arrayList, Runnable runnable) {
        this.f$0 = valueAnimator;
        this.f$1 = transaction;
        this.f$2 = surfaceControl;
        this.f$3 = animation;
        this.f$4 = transformation;
        this.f$5 = fArr;
        this.f$6 = point;
        this.f$7 = f;
        this.f$8 = rect;
        this.f$9 = transactionPool;
        this.f$10 = shellExecutor;
        this.f$11 = arrayList;
        this.f$12 = runnable;
    }

    public final void run() {
        DefaultTransitionHandler.lambda$startSurfaceAnimation$6(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12);
    }
}
