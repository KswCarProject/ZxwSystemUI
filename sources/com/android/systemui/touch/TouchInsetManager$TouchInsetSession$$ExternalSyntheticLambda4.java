package com.android.systemui.touch;

import android.view.View;
import com.android.systemui.touch.TouchInsetManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ TouchInsetManager.TouchInsetSession f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda4(TouchInsetManager.TouchInsetSession touchInsetSession, View view) {
        this.f$0 = touchInsetSession;
        this.f$1 = view;
    }

    public final void run() {
        this.f$0.lambda$removeViewFromTracking$2(this.f$1);
    }
}
