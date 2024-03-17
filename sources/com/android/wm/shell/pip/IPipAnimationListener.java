package com.android.wm.shell.pip;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPipAnimationListener extends IInterface {
    void onExpandPip() throws RemoteException;

    void onPipAnimationStarted() throws RemoteException;

    void onPipResourceDimensionsChanged(int i, int i2) throws RemoteException;

    public static abstract class Stub extends Binder implements IPipAnimationListener {
        public static IPipAnimationListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.wm.shell.pip.IPipAnimationListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPipAnimationListener)) {
                return new Proxy(iBinder);
            }
            return (IPipAnimationListener) queryLocalInterface;
        }

        public static class Proxy implements IPipAnimationListener {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onPipAnimationStarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.pip.IPipAnimationListener");
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onPipResourceDimensionsChanged(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.pip.IPipAnimationListener");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onExpandPip() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.pip.IPipAnimationListener");
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
