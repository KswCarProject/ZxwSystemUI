package com.android.systemui.statusbar.gesture;

import android.os.Looper;
import android.view.Choreographer;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.shared.system.InputMonitorCompat;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: GenericGestureDetector.kt */
public abstract class GenericGestureDetector {
    @NotNull
    public final Map<String, Function1<MotionEvent, Unit>> callbacks = new LinkedHashMap();
    @Nullable
    public InputMonitorCompat inputMonitor;
    @Nullable
    public InputChannelCompat$InputEventReceiver inputReceiver;
    @NotNull
    public final String tag;

    public abstract void onInputEvent(@NotNull InputEvent inputEvent);

    public GenericGestureDetector(@NotNull String str) {
        this.tag = str;
    }

    public final void addOnGestureDetectedCallback(@NotNull String str, @NotNull Function1<? super MotionEvent, Unit> function1) {
        boolean isEmpty = this.callbacks.isEmpty();
        this.callbacks.put(str, function1);
        if (isEmpty) {
            startGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        }
    }

    public final void removeOnGestureDetectedCallback(@NotNull String str) {
        this.callbacks.remove(str);
        if (this.callbacks.isEmpty()) {
            stopGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        }
    }

    public final void onGestureDetected$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull MotionEvent motionEvent) {
        for (Function1 invoke : this.callbacks.values()) {
            invoke.invoke(motionEvent);
        }
    }

    public void startGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        stopGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        InputMonitorCompat inputMonitorCompat = new InputMonitorCompat(this.tag, 0);
        this.inputReceiver = inputMonitorCompat.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new GenericGestureDetector$startGestureListening$1$1(this));
        this.inputMonitor = inputMonitorCompat;
    }

    public void stopGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        InputMonitorCompat inputMonitorCompat = this.inputMonitor;
        if (inputMonitorCompat != null) {
            this.inputMonitor = null;
            inputMonitorCompat.dispose();
        }
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.inputReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            this.inputReceiver = null;
            inputChannelCompat$InputEventReceiver.dispose();
        }
    }
}
