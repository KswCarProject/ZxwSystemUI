package com.android.systemui.touch;

import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TouchInsetManager$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ TouchInsetManager f$0;
    public final /* synthetic */ CallbackToFutureAdapter.Completer f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ TouchInsetManager$$ExternalSyntheticLambda3(TouchInsetManager touchInsetManager, CallbackToFutureAdapter.Completer completer, int i, int i2) {
        this.f$0 = touchInsetManager;
        this.f$1 = completer;
        this.f$2 = i;
        this.f$3 = i2;
    }

    public final void run() {
        this.f$0.lambda$checkWithinTouchRegion$1(this.f$1, this.f$2, this.f$3);
    }
}
