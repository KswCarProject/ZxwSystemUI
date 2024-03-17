package com.android.wm.shell.pip.phone;

import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.IWindowManager;
import android.view.InputChannel;
import android.view.InputEvent;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;

public class PipInputConsumer {
    public static final String TAG = "PipInputConsumer";
    public InputEventReceiver mInputEventReceiver;
    public InputListener mListener;
    public final ShellExecutor mMainExecutor;
    public final String mName;
    public RegistrationListener mRegistrationListener;
    public final IBinder mToken = new Binder();
    public final IWindowManager mWindowManager;

    public interface InputListener {
        boolean onInputEvent(InputEvent inputEvent);
    }

    public interface RegistrationListener {
        void onRegistrationChanged(boolean z);
    }

    public final class InputEventReceiver extends BatchedInputEventReceiver {
        public InputEventReceiver(InputChannel inputChannel, Looper looper, Choreographer choreographer) {
            super(inputChannel, looper, choreographer);
        }

        public void onInputEvent(InputEvent inputEvent) {
            boolean z = true;
            try {
                if (PipInputConsumer.this.mListener != null) {
                    z = PipInputConsumer.this.mListener.onInputEvent(inputEvent);
                }
            } finally {
                finishInputEvent(inputEvent, z);
            }
        }
    }

    public PipInputConsumer(IWindowManager iWindowManager, String str, ShellExecutor shellExecutor) {
        this.mWindowManager = iWindowManager;
        this.mName = str;
        this.mMainExecutor = shellExecutor;
    }

    public void setInputListener(InputListener inputListener) {
        this.mListener = inputListener;
    }

    public void setRegistrationListener(RegistrationListener registrationListener) {
        this.mRegistrationListener = registrationListener;
        this.mMainExecutor.execute(new PipInputConsumer$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setRegistrationListener$0() {
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(this.mInputEventReceiver != null);
        }
    }

    public void registerInputConsumer() {
        if (this.mInputEventReceiver == null) {
            InputChannel inputChannel = new InputChannel();
            try {
                this.mWindowManager.destroyInputConsumer(this.mName, 0);
                this.mWindowManager.createInputConsumer(this.mToken, this.mName, 0, inputChannel);
            } catch (RemoteException e) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    String valueOf = String.valueOf(TAG);
                    String valueOf2 = String.valueOf(e);
                    ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 842293805, 0, (String) null, valueOf, valueOf2);
                }
            }
            this.mMainExecutor.execute(new PipInputConsumer$$ExternalSyntheticLambda0(this, inputChannel));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerInputConsumer$1(InputChannel inputChannel) {
        this.mInputEventReceiver = new InputEventReceiver(inputChannel, Looper.myLooper(), Choreographer.getSfInstance());
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(true);
        }
    }

    public void unregisterInputConsumer() {
        if (this.mInputEventReceiver != null) {
            try {
                this.mWindowManager.destroyInputConsumer(this.mName, 0);
            } catch (RemoteException e) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    String valueOf = String.valueOf(TAG);
                    String valueOf2 = String.valueOf(e);
                    ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -97774237, 0, (String) null, valueOf, valueOf2);
                }
            }
            this.mInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
            this.mMainExecutor.execute(new PipInputConsumer$$ExternalSyntheticLambda2(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterInputConsumer$2() {
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(false);
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("registered=");
        sb.append(this.mInputEventReceiver != null);
        printWriter.println(sb.toString());
    }
}
