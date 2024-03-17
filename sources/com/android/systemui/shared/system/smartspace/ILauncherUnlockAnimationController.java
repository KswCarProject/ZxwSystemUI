package com.android.systemui.shared.system.smartspace;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ILauncherUnlockAnimationController extends IInterface {
    void playUnlockAnimation(boolean z, long j, long j2) throws RemoteException;

    void prepareForUnlock(boolean z, Rect rect, int i) throws RemoteException;

    void setUnlockAmount(float f, boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements ILauncherUnlockAnimationController {
        public static ILauncherUnlockAnimationController asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ILauncherUnlockAnimationController)) {
                return new Proxy(iBinder);
            }
            return (ILauncherUnlockAnimationController) queryLocalInterface;
        }

        public static class Proxy implements ILauncherUnlockAnimationController {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void prepareForUnlock(boolean z, Rect rect, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(rect, 0);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setUnlockAmount(float f, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                    obtain.writeFloat(f);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void playUnlockAnimation(boolean z, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                    obtain.writeBoolean(z);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
