package com.android.wm.shell.splitscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.window.RemoteTransition;
import com.android.wm.shell.splitscreen.ISplitScreenListener;

public interface ISplitScreen extends IInterface {
    void exitSplitScreen(int i) throws RemoteException;

    void exitSplitScreenOnHide(boolean z) throws RemoteException;

    RemoteAnimationTarget[] onGoingToRecentsLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException;

    RemoteAnimationTarget[] onStartingSplitLegacy(RemoteAnimationTarget[] remoteAnimationTargetArr) throws RemoteException;

    void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    void removeFromSideStage(int i) throws RemoteException;

    void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) throws RemoteException;

    void startIntentAndTaskWithLegacyTransition(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle, Bundle bundle2, int i2, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) throws RemoteException;

    void startTask(int i, int i2, Bundle bundle) throws RemoteException;

    void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteTransition remoteTransition) throws RemoteException;

    void startTasksWithLegacyTransition(int i, Bundle bundle, int i2, Bundle bundle2, int i3, float f, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) throws RemoteException;

    public static abstract class Stub extends Binder implements ISplitScreen {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.splitscreen.ISplitScreen");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
            }
            if (i != 1598968902) {
                switch (i) {
                    case 2:
                        ISplitScreenListener asInterface = ISplitScreenListener.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        registerSplitScreenListener(asInterface);
                        break;
                    case 3:
                        ISplitScreenListener asInterface2 = ISplitScreenListener.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        unregisterSplitScreenListener(asInterface2);
                        break;
                    case 5:
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        removeFromSideStage(readInt);
                        break;
                    case 6:
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        exitSplitScreen(readInt2);
                        break;
                    case 7:
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        exitSplitScreenOnHide(readBoolean);
                        break;
                    case 8:
                        parcel.enforceNoDataAvail();
                        startTask(parcel.readInt(), parcel.readInt(), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        break;
                    case 9:
                        String readString = parcel.readString();
                        String readString2 = parcel.readString();
                        int readInt3 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        startShortcut(readString, readString2, readInt3, (Bundle) parcel.readTypedObject(Bundle.CREATOR), (UserHandle) parcel.readTypedObject(UserHandle.CREATOR));
                        break;
                    case 10:
                        parcel.enforceNoDataAvail();
                        startIntent((PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR), (Intent) parcel.readTypedObject(Intent.CREATOR), parcel.readInt(), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        break;
                    case 11:
                        int readInt4 = parcel.readInt();
                        Parcelable.Creator creator = Bundle.CREATOR;
                        int readInt5 = parcel.readInt();
                        int readInt6 = parcel.readInt();
                        float readFloat = parcel.readFloat();
                        parcel.enforceNoDataAvail();
                        startTasks(readInt4, (Bundle) parcel.readTypedObject(creator), readInt5, (Bundle) parcel.readTypedObject(creator), readInt6, readFloat, (RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR));
                        break;
                    case 12:
                        int readInt7 = parcel.readInt();
                        Parcelable.Creator creator2 = Bundle.CREATOR;
                        int readInt8 = parcel.readInt();
                        int readInt9 = parcel.readInt();
                        float readFloat2 = parcel.readFloat();
                        parcel.enforceNoDataAvail();
                        startTasksWithLegacyTransition(readInt7, (Bundle) parcel.readTypedObject(creator2), readInt8, (Bundle) parcel.readTypedObject(creator2), readInt9, readFloat2, (RemoteAnimationAdapter) parcel.readTypedObject(RemoteAnimationAdapter.CREATOR));
                        break;
                    case 13:
                        int readInt10 = parcel.readInt();
                        Parcelable.Creator creator3 = Bundle.CREATOR;
                        int readInt11 = parcel.readInt();
                        float readFloat3 = parcel.readFloat();
                        parcel.enforceNoDataAvail();
                        startIntentAndTaskWithLegacyTransition((PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR), (Intent) parcel.readTypedObject(Intent.CREATOR), readInt10, (Bundle) parcel.readTypedObject(creator3), (Bundle) parcel.readTypedObject(creator3), readInt11, readFloat3, (RemoteAnimationAdapter) parcel.readTypedObject(RemoteAnimationAdapter.CREATOR));
                        break;
                    case 14:
                        parcel.enforceNoDataAvail();
                        RemoteAnimationTarget[] onGoingToRecentsLegacy = onGoingToRecentsLegacy((RemoteAnimationTarget[]) parcel.createTypedArray(RemoteAnimationTarget.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedArray(onGoingToRecentsLegacy, 1);
                        break;
                    case 15:
                        parcel.enforceNoDataAvail();
                        RemoteAnimationTarget[] onStartingSplitLegacy = onStartingSplitLegacy((RemoteAnimationTarget[]) parcel.createTypedArray(RemoteAnimationTarget.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedArray(onStartingSplitLegacy, 1);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.splitscreen.ISplitScreen");
            return true;
        }
    }
}
