package com.android.systemui.shared.recents;

import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MotionEvent;
import com.android.systemui.shared.recents.model.Task$TaskKey;

public interface ISystemUiProxy extends IInterface {
    void expandNotificationPanel() throws RemoteException;

    @Deprecated
    Rect getNonMinimizedSplitScreenSecondaryBounds() throws RemoteException;

    @Deprecated
    void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i) throws RemoteException;

    void handleImageBundleAsScreenshot(Bundle bundle, Rect rect, Insets insets, Task$TaskKey task$TaskKey) throws RemoteException;

    void notifyAccessibilityButtonClicked(int i) throws RemoteException;

    void notifyAccessibilityButtonLongClicked() throws RemoteException;

    void notifyPrioritizedRotation(int i) throws RemoteException;

    void notifySwipeToHomeFinished() throws RemoteException;

    void notifySwipeUpGestureStarted() throws RemoteException;

    void notifyTaskbarAutohideSuspend(boolean z) throws RemoteException;

    void notifyTaskbarStatus(boolean z, boolean z2) throws RemoteException;

    void onAssistantGestureCompletion(float f) throws RemoteException;

    void onAssistantProgress(float f) throws RemoteException;

    void onBackPressed() throws RemoteException;

    void onImeSwitcherPressed() throws RemoteException;

    void onOverviewShown(boolean z) throws RemoteException;

    void onStatusBarMotionEvent(MotionEvent motionEvent) throws RemoteException;

    void setHomeRotationEnabled(boolean z) throws RemoteException;

    void setNavBarButtonAlpha(float f, boolean z) throws RemoteException;

    @Deprecated
    void setSplitScreenMinimized(boolean z) throws RemoteException;

    void startAssistant(Bundle bundle) throws RemoteException;

    void startScreenPinning(int i) throws RemoteException;

    void stopScreenPinning() throws RemoteException;

    void toggleNotificationPanel() throws RemoteException;

    public static abstract class Stub extends Binder implements ISystemUiProxy {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.systemui.shared.recents.ISystemUiProxy");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    int readInt = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    startScreenPinning(readInt);
                    parcel2.writeNoException();
                } else if (i == 10) {
                    parcel.enforceNoDataAvail();
                    onStatusBarMotionEvent((MotionEvent) parcel.readTypedObject(MotionEvent.CREATOR));
                    parcel2.writeNoException();
                } else if (i == 26) {
                    int readInt2 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    notifyPrioritizedRotation(readInt2);
                    parcel2.writeNoException();
                } else if (i == 7) {
                    boolean readBoolean = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    onOverviewShown(readBoolean);
                    parcel2.writeNoException();
                } else if (i == 8) {
                    Rect nonMinimizedSplitScreenSecondaryBounds = getNonMinimizedSplitScreenSecondaryBounds();
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(nonMinimizedSplitScreenSecondaryBounds, 1);
                } else if (i == 13) {
                    float readFloat = parcel.readFloat();
                    parcel.enforceNoDataAvail();
                    onAssistantProgress(readFloat);
                    parcel2.writeNoException();
                } else if (i == 14) {
                    parcel.enforceNoDataAvail();
                    startAssistant((Bundle) parcel.readTypedObject(Bundle.CREATOR));
                    parcel2.writeNoException();
                } else if (i == 29) {
                    parcel.enforceNoDataAvail();
                    handleImageBundleAsScreenshot((Bundle) parcel.readTypedObject(Bundle.CREATOR), (Rect) parcel.readTypedObject(Rect.CREATOR), (Insets) parcel.readTypedObject(Insets.CREATOR), (Task$TaskKey) parcel.readTypedObject(Task$TaskKey.CREATOR));
                    parcel2.writeNoException();
                } else if (i != 30) {
                    switch (i) {
                        case 16:
                            int readInt3 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            notifyAccessibilityButtonClicked(readInt3);
                            parcel2.writeNoException();
                            break;
                        case 17:
                            notifyAccessibilityButtonLongClicked();
                            parcel2.writeNoException();
                            break;
                        case 18:
                            stopScreenPinning();
                            parcel2.writeNoException();
                            break;
                        case 19:
                            float readFloat2 = parcel.readFloat();
                            parcel.enforceNoDataAvail();
                            onAssistantGestureCompletion(readFloat2);
                            parcel2.writeNoException();
                            break;
                        case 20:
                            float readFloat3 = parcel.readFloat();
                            boolean readBoolean2 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            setNavBarButtonAlpha(readFloat3, readBoolean2);
                            parcel2.writeNoException();
                            break;
                        default:
                            switch (i) {
                                case 22:
                                    int readInt4 = parcel.readInt();
                                    parcel.enforceNoDataAvail();
                                    handleImageAsScreenshot((Bitmap) parcel.readTypedObject(Bitmap.CREATOR), (Rect) parcel.readTypedObject(Rect.CREATOR), (Insets) parcel.readTypedObject(Insets.CREATOR), readInt4);
                                    parcel2.writeNoException();
                                    break;
                                case 23:
                                    boolean readBoolean3 = parcel.readBoolean();
                                    parcel.enforceNoDataAvail();
                                    setSplitScreenMinimized(readBoolean3);
                                    parcel2.writeNoException();
                                    break;
                                case 24:
                                    notifySwipeToHomeFinished();
                                    parcel2.writeNoException();
                                    break;
                                default:
                                    switch (i) {
                                        case 45:
                                            onBackPressed();
                                            parcel2.writeNoException();
                                            break;
                                        case 46:
                                            boolean readBoolean4 = parcel.readBoolean();
                                            parcel.enforceNoDataAvail();
                                            setHomeRotationEnabled(readBoolean4);
                                            parcel2.writeNoException();
                                            break;
                                        case 47:
                                            notifySwipeUpGestureStarted();
                                            break;
                                        case 48:
                                            boolean readBoolean5 = parcel.readBoolean();
                                            boolean readBoolean6 = parcel.readBoolean();
                                            parcel.enforceNoDataAvail();
                                            notifyTaskbarStatus(readBoolean5, readBoolean6);
                                            break;
                                        case 49:
                                            boolean readBoolean7 = parcel.readBoolean();
                                            parcel.enforceNoDataAvail();
                                            notifyTaskbarAutohideSuspend(readBoolean7);
                                            break;
                                        case 50:
                                            onImeSwitcherPressed();
                                            parcel2.writeNoException();
                                            break;
                                        case 51:
                                            toggleNotificationPanel();
                                            parcel2.writeNoException();
                                            break;
                                        default:
                                            return super.onTransact(i, parcel, parcel2, i2);
                                    }
                            }
                    }
                } else {
                    expandNotificationPanel();
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString("com.android.systemui.shared.recents.ISystemUiProxy");
            return true;
        }
    }
}
