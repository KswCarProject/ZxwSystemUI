package com.android.wm.shell.pip;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.SurfaceControl;
import com.android.wm.shell.pip.IPipAnimationListener;

public interface IPip extends IInterface {
    void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) throws RemoteException;

    void setShelfHeight(boolean z, int i) throws RemoteException;

    Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) throws RemoteException;

    void stopSwipePipToHome(int i, ComponentName componentName, Rect rect, SurfaceControl surfaceControl) throws RemoteException;

    public static abstract class Stub extends Binder implements IPip {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.pip.IPip");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.pip.IPip");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    int readInt = parcel.readInt();
                    int readInt2 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    Rect startSwipePipToHome = startSwipePipToHome((ComponentName) parcel.readTypedObject(ComponentName.CREATOR), (ActivityInfo) parcel.readTypedObject(ActivityInfo.CREATOR), (PictureInPictureParams) parcel.readTypedObject(PictureInPictureParams.CREATOR), readInt, readInt2);
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(startSwipePipToHome, 1);
                } else if (i == 3) {
                    parcel.enforceNoDataAvail();
                    stopSwipePipToHome(parcel.readInt(), (ComponentName) parcel.readTypedObject(ComponentName.CREATOR), (Rect) parcel.readTypedObject(Rect.CREATOR), (SurfaceControl) parcel.readTypedObject(SurfaceControl.CREATOR));
                } else if (i == 4) {
                    IPipAnimationListener asInterface = IPipAnimationListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    setPinnedStackAnimationListener(asInterface);
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    boolean readBoolean = parcel.readBoolean();
                    int readInt3 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    setShelfHeight(readBoolean, readInt3);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.pip.IPip");
            return true;
        }
    }
}
