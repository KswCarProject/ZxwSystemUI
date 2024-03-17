package com.android.systemui.touch;

import android.graphics.Region;
import com.android.systemui.touch.TouchInsetManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TouchInsetManager$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ TouchInsetManager f$0;
    public final /* synthetic */ TouchInsetManager.TouchInsetSession f$1;
    public final /* synthetic */ Region f$2;

    public /* synthetic */ TouchInsetManager$$ExternalSyntheticLambda2(TouchInsetManager touchInsetManager, TouchInsetManager.TouchInsetSession touchInsetSession, Region region) {
        this.f$0 = touchInsetManager;
        this.f$1 = touchInsetSession;
        this.f$2 = region;
    }

    public final void run() {
        this.f$0.lambda$setTouchRegion$3(this.f$1, this.f$2);
    }
}
