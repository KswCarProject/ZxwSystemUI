package com.android.systemui.shared.system.smartspace;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController;

public interface ISysuiUnlockAnimationController extends IInterface {
    void onLauncherSmartspaceStateUpdated(SmartspaceState smartspaceState) throws RemoteException;

    void setLauncherUnlockController(ILauncherUnlockAnimationController iLauncherUnlockAnimationController) throws RemoteException;

    public static abstract class Stub extends Binder implements ISysuiUnlockAnimationController {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController");
            }
            if (i != 1598968902) {
                if (i == 1) {
                    ILauncherUnlockAnimationController asInterface = ILauncherUnlockAnimationController.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    setLauncherUnlockController(asInterface);
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceNoDataAvail();
                    onLauncherSmartspaceStateUpdated((SmartspaceState) parcel.readTypedObject(SmartspaceState.CREATOR));
                }
                return true;
            }
            parcel2.writeString("com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController");
            return true;
        }
    }
}
