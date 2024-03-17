package com.android.systemui.touch;

import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TouchInsetManager$$ExternalSyntheticLambda1 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ TouchInsetManager f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ TouchInsetManager$$ExternalSyntheticLambda1(TouchInsetManager touchInsetManager, int i, int i2) {
        this.f$0 = touchInsetManager;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return this.f$0.lambda$checkWithinTouchRegion$2(this.f$1, this.f$2, completer);
    }
}
