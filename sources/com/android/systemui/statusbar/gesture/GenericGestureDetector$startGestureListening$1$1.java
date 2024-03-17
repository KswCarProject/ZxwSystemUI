package com.android.systemui.statusbar.gesture;

import android.view.InputEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: GenericGestureDetector.kt */
public /* synthetic */ class GenericGestureDetector$startGestureListening$1$1 implements InputChannelCompat$InputEventListener {
    public final /* synthetic */ GenericGestureDetector $tmp0;

    public GenericGestureDetector$startGestureListening$1$1(GenericGestureDetector genericGestureDetector) {
        this.$tmp0 = genericGestureDetector;
    }

    public final void onInputEvent(@NotNull InputEvent inputEvent) {
        this.$tmp0.onInputEvent(inputEvent);
    }
}
