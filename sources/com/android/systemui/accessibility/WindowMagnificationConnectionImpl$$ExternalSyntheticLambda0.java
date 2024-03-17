package com.android.systemui.accessibility;

import android.view.accessibility.IRemoteMagnificationAnimationCallback;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WindowMagnificationConnectionImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WindowMagnificationConnectionImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ IRemoteMagnificationAnimationCallback f$4;

    public /* synthetic */ WindowMagnificationConnectionImpl$$ExternalSyntheticLambda0(WindowMagnificationConnectionImpl windowMagnificationConnectionImpl, int i, float f, float f2, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.f$0 = windowMagnificationConnectionImpl;
        this.f$1 = i;
        this.f$2 = f;
        this.f$3 = f2;
        this.f$4 = iRemoteMagnificationAnimationCallback;
    }

    public final void run() {
        this.f$0.lambda$moveWindowMagnifierToPosition$4(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
