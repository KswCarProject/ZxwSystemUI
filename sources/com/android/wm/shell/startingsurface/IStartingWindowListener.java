package com.android.wm.shell.startingsurface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IStartingWindowListener extends IInterface {
    void onTaskLaunching(int i, int i2, int i3) throws RemoteException;

    public static abstract class Stub extends Binder implements IStartingWindowListener {
        public static IStartingWindowListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.wm.shell.startingsurface.IStartingWindowListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStartingWindowListener)) {
                return new Proxy(iBinder);
            }
            return (IStartingWindowListener) queryLocalInterface;
        }

        public static class Proxy implements IStartingWindowListener {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onTaskLaunching(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.startingsurface.IStartingWindowListener");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
