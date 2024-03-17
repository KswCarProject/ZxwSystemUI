package com.android.systemui.keyguard;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Log;
import com.android.internal.policy.IKeyguardDrawnCallback;

public class KeyguardLifecyclesDispatcher {
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            final Object obj = message.obj;
            switch (message.what) {
                case 0:
                    Trace.beginSection("KeyguardLifecyclesDispatcher#SCREEN_TURNING_ON");
                    final int identityHashCode = System.identityHashCode(message);
                    Trace.beginAsyncSection("Waiting for KeyguardDrawnCallback#onDrawn", identityHashCode);
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurningOn(new Runnable() {
                        public boolean mInvoked;

                        public void run() {
                            if (obj != null) {
                                if (!this.mInvoked) {
                                    this.mInvoked = true;
                                    try {
                                        Trace.endAsyncSection("Waiting for KeyguardDrawnCallback#onDrawn", identityHashCode);
                                        ((IKeyguardDrawnCallback) obj).onDrawn();
                                    } catch (RemoteException e) {
                                        Log.w("KeyguardLifecyclesDispatcher", "Exception calling onDrawn():", e);
                                    }
                                } else {
                                    Log.w("KeyguardLifecyclesDispatcher", "KeyguardDrawnCallback#onDrawn() invoked > 1 times");
                                }
                            }
                        }
                    });
                    Trace.endSection();
                    return;
                case 1:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurnedOn();
                    return;
                case 2:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurningOff();
                    return;
                case 3:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurnedOff();
                    return;
                case 4:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchStartedWakingUp(message.arg1);
                    return;
                case 5:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchFinishedWakingUp();
                    return;
                case 6:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchStartedGoingToSleep(message.arg1);
                    return;
                case 7:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchFinishedGoingToSleep();
                    return;
                default:
                    throw new IllegalArgumentException("Unknown message: " + message);
            }
        }
    };
    public final ScreenLifecycle mScreenLifecycle;
    public final WakefulnessLifecycle mWakefulnessLifecycle;

    public KeyguardLifecyclesDispatcher(ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle) {
        this.mScreenLifecycle = screenLifecycle;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
    }

    public void dispatch(int i) {
        this.mHandler.obtainMessage(i).sendToTarget();
    }

    public void dispatch(int i, int i2) {
        Message obtainMessage = this.mHandler.obtainMessage(i);
        obtainMessage.arg1 = i2;
        obtainMessage.sendToTarget();
    }

    public void dispatch(int i, Object obj) {
        this.mHandler.obtainMessage(i, obj).sendToTarget();
    }
}
