package com.android.wm.shell.common;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Slog;
import com.android.wm.shell.common.RemoteCallable;
import java.util.function.Consumer;

public class SingleInstanceRemoteListener<C extends RemoteCallable, L extends IInterface> {
    public static final String TAG = "SingleInstanceRemoteListener";
    public final C mCallableController;
    public L mListener;
    public final IBinder.DeathRecipient mListenerDeathRecipient = new IBinder.DeathRecipient() {
        public void binderDied() {
            SingleInstanceRemoteListener.this.mCallableController.getRemoteCallExecutor().execute(new SingleInstanceRemoteListener$1$$ExternalSyntheticLambda0(this, SingleInstanceRemoteListener.this.mCallableController));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$binderDied$0(RemoteCallable remoteCallable) {
            SingleInstanceRemoteListener singleInstanceRemoteListener = SingleInstanceRemoteListener.this;
            singleInstanceRemoteListener.mListener = null;
            singleInstanceRemoteListener.mOnUnregisterCallback.accept(remoteCallable);
        }
    };
    public final Consumer<C> mOnRegisterCallback;
    public final Consumer<C> mOnUnregisterCallback;

    public interface RemoteCall<L> {
        void accept(L l) throws RemoteException;
    }

    public SingleInstanceRemoteListener(C c, Consumer<C> consumer, Consumer<C> consumer2) {
        this.mCallableController = c;
        this.mOnRegisterCallback = consumer;
        this.mOnUnregisterCallback = consumer2;
    }

    public void register(L l) {
        L l2 = this.mListener;
        if (l2 != null) {
            l2.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
        }
        if (l != null) {
            try {
                l.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
            } catch (RemoteException unused) {
                Slog.e(TAG, "Failed to link to death");
                return;
            }
        }
        this.mListener = l;
        this.mOnRegisterCallback.accept(this.mCallableController);
    }

    public void unregister() {
        L l = this.mListener;
        if (l != null) {
            l.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
        }
        this.mListener = null;
        this.mOnUnregisterCallback.accept(this.mCallableController);
    }

    public void call(RemoteCall<L> remoteCall) {
        L l = this.mListener;
        if (l == null) {
            Slog.e(TAG, "Failed remote call on null listener");
            return;
        }
        try {
            remoteCall.accept(l);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed remote call", e);
        }
    }
}
