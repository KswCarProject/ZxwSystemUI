package com.android.wm.shell.pip.phone;

import android.view.InputEvent;
import com.android.wm.shell.pip.phone.PipInputConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$$ExternalSyntheticLambda5 implements PipInputConsumer.InputListener {
    public final /* synthetic */ PipTouchHandler f$0;

    public /* synthetic */ PipController$$ExternalSyntheticLambda5(PipTouchHandler pipTouchHandler) {
        this.f$0 = pipTouchHandler;
    }

    public final boolean onInputEvent(InputEvent inputEvent) {
        return this.f$0.handleTouchEvent(inputEvent);
    }
}
