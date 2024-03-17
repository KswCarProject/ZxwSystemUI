package com.android.systemui.dreams.touch;

import android.os.Looper;
import android.view.Choreographer;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.shared.system.InputMonitorCompat;

public class InputSession {
    public final GestureDetector mGestureDetector;
    public final InputChannelCompat$InputEventReceiver mInputEventReceiver;
    public final InputMonitorCompat mInputMonitor;

    public InputSession(String str, InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener, GestureDetector.OnGestureListener onGestureListener, boolean z) {
        InputMonitorCompat inputMonitorCompat = new InputMonitorCompat(str, 0);
        this.mInputMonitor = inputMonitorCompat;
        this.mGestureDetector = new GestureDetector(onGestureListener);
        this.mInputEventReceiver = inputMonitorCompat.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new InputSession$$ExternalSyntheticLambda0(this, inputChannelCompat$InputEventListener, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener, boolean z, InputEvent inputEvent) {
        inputChannelCompat$InputEventListener.onInputEvent(inputEvent);
        if ((inputEvent instanceof MotionEvent) && this.mGestureDetector.onTouchEvent((MotionEvent) inputEvent) && z) {
            this.mInputMonitor.pilferPointers();
        }
    }

    public void dispose() {
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.mInputEventReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            inputChannelCompat$InputEventReceiver.dispose();
        }
        InputMonitorCompat inputMonitorCompat = this.mInputMonitor;
        if (inputMonitorCompat != null) {
            inputMonitorCompat.dispose();
        }
    }
}
