package com.android.wm.shell.back;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.window.IOnBackInvokedCallback;

public interface IBackAnimation extends IInterface {
    void clearBackToLauncherCallback() throws RemoteException;

    void onBackToLauncherAnimationFinished() throws RemoteException;

    void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IBackAnimation {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.back.IBackAnimation");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.back.IBackAnimation");
            }
            if (i != 1598968902) {
                if (i == 1) {
                    IOnBackInvokedCallback asInterface = IOnBackInvokedCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    setBackToLauncherCallback(asInterface);
                    parcel2.writeNoException();
                } else if (i == 2) {
                    clearBackToLauncherCallback();
                    parcel2.writeNoException();
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onBackToLauncherAnimationFinished();
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.back.IBackAnimation");
            return true;
        }
    }
}
