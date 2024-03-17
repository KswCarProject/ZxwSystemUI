package com.android.systemui.dreams.touch;

import android.view.InputEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class InputSession$$ExternalSyntheticLambda0 implements InputChannelCompat$InputEventListener {
    public final /* synthetic */ InputSession f$0;
    public final /* synthetic */ InputChannelCompat$InputEventListener f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ InputSession$$ExternalSyntheticLambda0(InputSession inputSession, InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener, boolean z) {
        this.f$0 = inputSession;
        this.f$1 = inputChannelCompat$InputEventListener;
        this.f$2 = z;
    }

    public final void onInputEvent(InputEvent inputEvent) {
        this.f$0.lambda$new$0(this.f$1, this.f$2, inputEvent);
    }
}
