package com.android.wm.shell.recents;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.wm.shell.recents.IRecentTasksListener;
import com.android.wm.shell.util.GroupedRecentTaskInfo;

public interface IRecentTasks extends IInterface {
    GroupedRecentTaskInfo[] getRecentTasks(int i, int i2, int i3) throws RemoteException;

    void registerRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException;

    void unregisterRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException;

    public static abstract class Stub extends Binder implements IRecentTasks {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.recents.IRecentTasks");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.recents.IRecentTasks");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    IRecentTasksListener asInterface = IRecentTasksListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    registerRecentTasksListener(asInterface);
                } else if (i == 3) {
                    IRecentTasksListener asInterface2 = IRecentTasksListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    unregisterRecentTasksListener(asInterface2);
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    int readInt = parcel.readInt();
                    int readInt2 = parcel.readInt();
                    int readInt3 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    GroupedRecentTaskInfo[] recentTasks = getRecentTasks(readInt, readInt2, readInt3);
                    parcel2.writeNoException();
                    parcel2.writeTypedArray(recentTasks, 1);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.recents.IRecentTasks");
            return true;
        }
    }
}
