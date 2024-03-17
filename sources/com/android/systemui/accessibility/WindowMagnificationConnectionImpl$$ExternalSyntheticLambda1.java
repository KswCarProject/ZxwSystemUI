package com.android.systemui.accessibility;

import android.view.accessibility.IRemoteMagnificationAnimationCallback;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WindowMagnificationConnectionImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WindowMagnificationConnectionImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ float f$4;
    public final /* synthetic */ float f$5;
    public final /* synthetic */ float f$6;
    public final /* synthetic */ IRemoteMagnificationAnimationCallback f$7;

    public /* synthetic */ WindowMagnificationConnectionImpl$$ExternalSyntheticLambda1(WindowMagnificationConnectionImpl windowMagnificationConnectionImpl, int i, float f, float f2, float f3, float f4, float f5, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.f$0 = windowMagnificationConnectionImpl;
        this.f$1 = i;
        this.f$2 = f;
        this.f$3 = f2;
        this.f$4 = f3;
        this.f$5 = f4;
        this.f$6 = f5;
        this.f$7 = iRemoteMagnificationAnimationCallback;
    }

    public final void run() {
        this.f$0.lambda$enableWindowMagnification$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
