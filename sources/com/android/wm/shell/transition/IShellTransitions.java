package com.android.wm.shell.transition;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.window.RemoteTransition;
import android.window.TransitionFilter;

public interface IShellTransitions extends IInterface {
    void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) throws RemoteException;

    void unregisterRemote(RemoteTransition remoteTransition) throws RemoteException;

    public static abstract class Stub extends Binder implements IShellTransitions {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.transition.IShellTransitions");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.transition.IShellTransitions");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    parcel.enforceNoDataAvail();
                    registerRemote((TransitionFilter) parcel.readTypedObject(TransitionFilter.CREATOR), (RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR));
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceNoDataAvail();
                    unregisterRemote((RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR));
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.transition.IShellTransitions");
            return true;
        }
    }
}
