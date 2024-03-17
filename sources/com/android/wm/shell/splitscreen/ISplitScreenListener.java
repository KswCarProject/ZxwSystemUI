package com.android.wm.shell.splitscreen;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISplitScreenListener extends IInterface {
    void onStagePositionChanged(int i, int i2) throws RemoteException;

    void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements ISplitScreenListener {
        public static ISplitScreenListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.wm.shell.splitscreen.ISplitScreenListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISplitScreenListener)) {
                return new Proxy(iBinder);
            }
            return (ISplitScreenListener) queryLocalInterface;
        }

        public static class Proxy implements ISplitScreenListener {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onStagePositionChanged(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.splitscreen.ISplitScreenListener");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.splitscreen.ISplitScreenListener");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
