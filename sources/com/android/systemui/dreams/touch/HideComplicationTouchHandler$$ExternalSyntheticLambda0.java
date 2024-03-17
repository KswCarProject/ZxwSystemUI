package com.android.systemui.dreams.touch;

import android.view.InputEvent;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HideComplicationTouchHandler$$ExternalSyntheticLambda0 implements InputChannelCompat$InputEventListener {
    public final /* synthetic */ HideComplicationTouchHandler f$0;
    public final /* synthetic */ DreamTouchHandler.TouchSession f$1;

    public /* synthetic */ HideComplicationTouchHandler$$ExternalSyntheticLambda0(HideComplicationTouchHandler hideComplicationTouchHandler, DreamTouchHandler.TouchSession touchSession) {
        this.f$0 = hideComplicationTouchHandler;
        this.f$1 = touchSession;
    }

    public final void onInputEvent(InputEvent inputEvent) {
        this.f$0.lambda$onSessionStart$1(this.f$1, inputEvent);
    }
}
