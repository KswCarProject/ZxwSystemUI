package com.android.wm.shell.onehanded;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOneHanded extends IInterface {
    void startOneHanded() throws RemoteException;

    void stopOneHanded() throws RemoteException;

    public static abstract class Stub extends Binder implements IOneHanded {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.onehanded.IOneHanded");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.onehanded.IOneHanded");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    startOneHanded();
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    stopOneHanded();
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.onehanded.IOneHanded");
            return true;
        }
    }
}
