package com.android.systemui.accessibility;

import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import android.view.accessibility.IWindowMagnificationConnection;
import android.view.accessibility.IWindowMagnificationConnectionCallback;

public class WindowMagnificationConnectionImpl extends IWindowMagnificationConnection.Stub {
    public IWindowMagnificationConnectionCallback mConnectionCallback;
    public final Handler mHandler;
    public final ModeSwitchesController mModeSwitchesController;
    public final WindowMagnification mWindowMagnification;

    public WindowMagnificationConnectionImpl(WindowMagnification windowMagnification, Handler handler, ModeSwitchesController modeSwitchesController) {
        this.mWindowMagnification = windowMagnification;
        this.mHandler = handler;
        this.mModeSwitchesController = modeSwitchesController;
    }

    public void enableWindowMagnification(int i, float f, float f2, float f3, float f4, float f5, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new WindowMagnificationConnectionImpl$$ExternalSyntheticLambda1(this, i, f, f2, f3, f4, f5, iRemoteMagnificationAnimationCallback));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$enableWindowMagnification$0(int i, float f, float f2, float f3, float f4, float f5, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mWindowMagnification.enableWindowMagnification(i, f, f2, f3, f4, f5, iRemoteMagnificationAnimationCallback);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setScale$1(int i, float f) {
        this.mWindowMagnification.setScale(i, f);
    }

    public void setScale(int i, float f) {
        this.mHandler.post(new WindowMagnificationConnectionImpl$$ExternalSyntheticLambda3(this, i, f));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$disableWindowMagnification$2(int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mWindowMagnification.disableWindowMagnification(i, iRemoteMagnificationAnimationCallback);
    }

    public void disableWindowMagnification(int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new WindowMagnificationConnectionImpl$$ExternalSyntheticLambda6(this, i, iRemoteMagnificationAnimationCallback));
    }

    public void moveWindowMagnifier(int i, float f, float f2) {
        this.mHandler.post(new WindowMagnificationConnectionImpl$$ExternalSyntheticLambda4(this, i, f, f2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$moveWindowMagnifier$3(int i, float f, float f2) {
        this.mWindowMagnification.moveWindowMagnifier(i, f, f2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$moveWindowMagnifierToPosition$4(int i, float f, float f2, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mWindowMagnification.moveWindowMagnifierToPositionInternal(i, f, f2, iRemoteMagnificationAnimationCallback);
    }

    public void moveWindowMagnifierToPosition(int i, float f, float f2, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new WindowMagnificationConnectionImpl$$ExternalSyntheticLambda0(this, i, f, f2, iRemoteMagnificationAnimationCallback));
    }

    public void showMagnificationButton(int i, int i2) {
        this.mHandler.post(new WindowMagnificationConnectionImpl$$ExternalSyntheticLambda2(this, i, i2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showMagnificationButton$5(int i, int i2) {
        this.mModeSwitchesController.showButton(i, i2);
    }

    public void removeMagnificationButton(int i) {
        this.mHandler.post(new WindowMagnificationConnectionImpl$$ExternalSyntheticLambda5(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeMagnificationButton$6(int i) {
        this.mModeSwitchesController.removeButton(i);
    }

    public void setConnectionCallback(IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback) {
        this.mConnectionCallback = iWindowMagnificationConnectionCallback;
    }

    public void onWindowMagnifierBoundsChanged(int i, Rect rect) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onWindowMagnifierBoundsChanged(i, rect);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform bounds changed", e);
            }
        }
    }

    public void onSourceBoundsChanged(int i, Rect rect) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onSourceBoundsChanged(i, rect);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform source bounds changed", e);
            }
        }
    }

    public void onPerformScaleAction(int i, float f) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onPerformScaleAction(i, f);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform performing scale action", e);
            }
        }
    }

    public void onAccessibilityActionPerformed(int i) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onAccessibilityActionPerformed(i);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform an accessibility action is already performed", e);
            }
        }
    }

    public void onChangeMagnificationMode(int i, int i2) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onChangeMagnificationMode(i, i2);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform changing magnification mode", e);
            }
        }
    }

    public void onMove(int i) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = this.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onMove(i);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform taking control by a user", e);
            }
        }
    }
}
