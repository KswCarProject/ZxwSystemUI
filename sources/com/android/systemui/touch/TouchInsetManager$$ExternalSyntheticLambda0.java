package com.android.systemui.touch;

import com.android.systemui.touch.TouchInsetManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TouchInsetManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TouchInsetManager f$0;
    public final /* synthetic */ TouchInsetManager.TouchInsetSession f$1;

    public /* synthetic */ TouchInsetManager$$ExternalSyntheticLambda0(TouchInsetManager touchInsetManager, TouchInsetManager.TouchInsetSession touchInsetSession) {
        this.f$0 = touchInsetManager;
        this.f$1 = touchInsetSession;
    }

    public final void run() {
        this.f$0.lambda$clearRegion$4(this.f$1);
    }
}
