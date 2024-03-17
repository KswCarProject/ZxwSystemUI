package com.android.wm.shell.common;

import android.os.RemoteException;
import android.util.Slog;
import android.view.IDisplayWindowRotationCallback;
import android.view.IDisplayWindowRotationController;
import android.view.IWindowManager;
import android.window.WindowContainerTransaction;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayChangeController {
    public static final String TAG = "DisplayChangeController";
    public final IDisplayWindowRotationController mControllerImpl;
    public final ShellExecutor mMainExecutor;
    public final CopyOnWriteArrayList<OnDisplayChangingListener> mRotationListener = new CopyOnWriteArrayList<>();
    public final IWindowManager mWmService;

    public interface OnDisplayChangingListener {
        void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction);
    }

    public DisplayChangeController(IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
        this.mWmService = iWindowManager;
        DisplayWindowRotationControllerImpl displayWindowRotationControllerImpl = new DisplayWindowRotationControllerImpl();
        this.mControllerImpl = displayWindowRotationControllerImpl;
        try {
            iWindowManager.setDisplayWindowRotationController(displayWindowRotationControllerImpl);
        } catch (RemoteException unused) {
            throw new RuntimeException("Unable to register rotation controller");
        }
    }

    public void addRotationListener(OnDisplayChangingListener onDisplayChangingListener) {
        this.mRotationListener.add(onDisplayChangingListener);
    }

    public void dispatchOnRotateDisplay(WindowContainerTransaction windowContainerTransaction, int i, int i2, int i3) {
        Iterator<OnDisplayChangingListener> it = this.mRotationListener.iterator();
        while (it.hasNext()) {
            it.next().onRotateDisplay(i, i2, i3, windowContainerTransaction);
        }
    }

    public final void onRotateDisplay(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        dispatchOnRotateDisplay(windowContainerTransaction, i, i2, i3);
        try {
            iDisplayWindowRotationCallback.continueRotateDisplay(i3, windowContainerTransaction);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to continue rotation", e);
        }
    }

    public class DisplayWindowRotationControllerImpl extends IDisplayWindowRotationController.Stub {
        public DisplayWindowRotationControllerImpl() {
        }

        public void onRotateDisplay(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
            DisplayChangeController.this.mMainExecutor.execute(new DisplayChangeController$DisplayWindowRotationControllerImpl$$ExternalSyntheticLambda0(this, i, i2, i3, iDisplayWindowRotationCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRotateDisplay$0(int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
            DisplayChangeController.this.onRotateDisplay(i, i2, i3, iDisplayWindowRotationCallback);
        }
    }
}
