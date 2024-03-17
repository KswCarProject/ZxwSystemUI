package com.android.wm.shell.recents;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRecentTasksListener extends IInterface {
    void onRecentTasksChanged() throws RemoteException;

    public static abstract class Stub extends Binder implements IRecentTasksListener {
        public static IRecentTasksListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.wm.shell.recents.IRecentTasksListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IRecentTasksListener)) {
                return new Proxy(iBinder);
            }
            return (IRecentTasksListener) queryLocalInterface;
        }

        public static class Proxy implements IRecentTasksListener {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onRecentTasksChanged() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wm.shell.recents.IRecentTasksListener");
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
