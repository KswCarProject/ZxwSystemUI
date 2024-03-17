package com.android.systemui.dreams.touch;

import android.view.InputEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BouncerSwipeTouchHandler$$ExternalSyntheticLambda1 implements InputChannelCompat$InputEventListener {
    public final /* synthetic */ BouncerSwipeTouchHandler f$0;

    public /* synthetic */ BouncerSwipeTouchHandler$$ExternalSyntheticLambda1(BouncerSwipeTouchHandler bouncerSwipeTouchHandler) {
        this.f$0 = bouncerSwipeTouchHandler;
    }

    public final void onInputEvent(InputEvent inputEvent) {
        this.f$0.lambda$onSessionStart$1(inputEvent);
    }
}
