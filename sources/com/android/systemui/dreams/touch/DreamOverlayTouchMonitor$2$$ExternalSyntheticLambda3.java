package com.android.systemui.dreams.touch;

import android.view.InputEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ InputEvent f$0;

    public /* synthetic */ DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda3(InputEvent inputEvent) {
        this.f$0 = inputEvent;
    }

    public final void accept(Object obj) {
        ((InputChannelCompat$InputEventListener) obj).onInputEvent(this.f$0);
    }
}
