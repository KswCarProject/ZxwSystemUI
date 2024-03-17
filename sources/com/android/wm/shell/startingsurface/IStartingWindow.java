package com.android.wm.shell.startingsurface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.wm.shell.startingsurface.IStartingWindowListener;

public interface IStartingWindow extends IInterface {
    void setStartingWindowListener(IStartingWindowListener iStartingWindowListener) throws RemoteException;

    public static abstract class Stub extends Binder implements IStartingWindow {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.startingsurface.IStartingWindow");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.startingsurface.IStartingWindow");
            }
            if (i == 1598968902) {
                parcel2.writeString("com.android.wm.shell.startingsurface.IStartingWindow");
                return true;
            } else if (i != 44) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                IStartingWindowListener asInterface = IStartingWindowListener.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                setStartingWindowListener(asInterface);
                return true;
            }
        }
    }
}
