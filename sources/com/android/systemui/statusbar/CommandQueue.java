package com.android.systemui.statusbar;

import android.app.ITransientNotificationCallback;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.hardware.biometrics.IBiometricContextListener;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.media.INearbyMediaDevicesProvider;
import android.media.MediaRoute2Info;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Pair;
import android.util.SparseArray;
import android.view.InsetsVisibilities;
import com.android.internal.os.SomeArgs;
import com.android.internal.statusbar.IAddTileResultCallback;
import com.android.internal.statusbar.IStatusBar;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.util.GcUtils;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.tracing.ProtoTracer;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CommandQueue extends IStatusBar.Stub implements CallbackController<Callbacks>, DisplayManager.DisplayListener {
    public static final String TAG = CommandQueue.class.getSimpleName();
    public ArrayList<Callbacks> mCallbacks = new ArrayList<>();
    public SparseArray<Pair<Integer, Integer>> mDisplayDisabled = new SparseArray<>();
    public Handler mHandler = new H(Looper.getMainLooper());
    public int mLastUpdatedImeDisplayId = -1;
    public final Object mLock = new Object();
    public ProtoTracer mProtoTracer;
    public final CommandRegistry mRegistry;

    public void onDisplayAdded(int i) {
    }

    public void onDisplayChanged(int i) {
    }

    public interface Callbacks {
        void abortTransient(int i, int[] iArr) {
        }

        void addQsTile(ComponentName componentName) {
        }

        void animateCollapsePanels(int i, boolean z) {
        }

        void animateExpandNotificationsPanel() {
        }

        void animateExpandSettingsPanel(String str) {
        }

        void appTransitionCancelled(int i) {
        }

        void appTransitionFinished(int i) {
        }

        void appTransitionPending(int i, boolean z) {
        }

        void appTransitionStarting(int i, long j, long j2, boolean z) {
        }

        void cancelPreloadRecentApps() {
        }

        void cancelRequestAddTile(String str) {
        }

        void clickTile(ComponentName componentName) {
        }

        void disable(int i, int i2, int i3, boolean z) {
        }

        void dismissInattentiveSleepWarning(boolean z) {
        }

        void dismissKeyboardShortcutsMenu() {
        }

        void handleShowGlobalActionsMenu() {
        }

        void handleShowShutdownUi(boolean z, String str) {
        }

        void handleSystemKey(int i) {
        }

        void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        }

        void hideAuthenticationDialog(long j) {
        }

        void hideRecentApps(boolean z, boolean z2) {
        }

        void hideToast(String str, IBinder iBinder) {
        }

        void onBiometricAuthenticated(int i) {
        }

        void onBiometricError(int i, int i2, int i3) {
        }

        void onBiometricHelp(int i, String str) {
        }

        void onCameraLaunchGestureDetected(int i) {
        }

        void onDisplayReady(int i) {
        }

        void onDisplayRemoved(int i) {
        }

        void onEmergencyActionLaunchGestureDetected() {
        }

        void onRecentsAnimationStateChanged(boolean z) {
        }

        void onRotationProposal(int i, boolean z) {
        }

        void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        }

        void onTracingStateChanged(boolean z) {
        }

        void preloadRecentApps() {
        }

        void registerNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        }

        void remQsTile(ComponentName componentName) {
        }

        void removeIcon(String str) {
        }

        void requestAddTile(ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, IAddTileResultCallback iAddTileResultCallback) {
        }

        void requestTileServiceListeningState(ComponentName componentName) {
        }

        void requestWindowMagnificationConnection(boolean z) {
        }

        void setBiometicContextListener(IBiometricContextListener iBiometricContextListener) {
        }

        void setIcon(String str, StatusBarIcon statusBarIcon) {
        }

        void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        }

        void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        }

        void setTopAppHidesStatusBar(boolean z) {
        }

        void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        }

        void setWindowState(int i, int i2, int i3) {
        }

        void showAssistDisclosure() {
        }

        void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        }

        void showInattentiveSleepWarning() {
        }

        void showPictureInPictureMenu() {
        }

        void showPinningEnterExitToast(boolean z) {
        }

        void showPinningEscapeToast() {
        }

        void showRecentApps(boolean z) {
        }

        void showScreenPinningRequest(int i) {
        }

        void showToast(int i, String str, IBinder iBinder, CharSequence charSequence, IBinder iBinder2, int i2, ITransientNotificationCallback iTransientNotificationCallback, int i3) {
        }

        void showTransient(int i, int[] iArr) {
        }

        void showWirelessChargingAnimation(int i) {
        }

        void startAssist(Bundle bundle) {
        }

        void suppressAmbientDisplay(boolean z) {
        }

        void toggleKeyboardShortcutsMenu(int i) {
        }

        void togglePanel() {
        }

        void toggleRecentApps() {
        }

        void toggleSplitScreen() {
        }

        void unregisterNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        }

        void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, CharSequence charSequence) {
        }

        void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        }

        void showTransient(int i, int[] iArr, boolean z) {
            showTransient(i, iArr);
        }
    }

    public CommandQueue(Context context, ProtoTracer protoTracer, CommandRegistry commandRegistry) {
        this.mProtoTracer = protoTracer;
        this.mRegistry = commandRegistry;
        ((DisplayManager) context.getSystemService(DisplayManager.class)).registerDisplayListener(this, this.mHandler);
        setDisabled(0, 0, 0);
    }

    public void onDisplayRemoved(int i) {
        synchronized (this.mLock) {
            this.mDisplayDisabled.remove(i);
        }
        for (int size = this.mCallbacks.size() - 1; size >= 0; size--) {
            this.mCallbacks.get(size).onDisplayRemoved(i);
        }
    }

    public boolean panelsEnabled() {
        int disabled1 = getDisabled1(0);
        int disabled2 = getDisabled2(0);
        if ((disabled1 & 65536) == 0 && (disabled2 & 4) == 0 && !CentralSurfacesImpl.ONLY_CORE_APPS) {
            return true;
        }
        return false;
    }

    public void addCallback(Callbacks callbacks) {
        this.mCallbacks.add(callbacks);
        for (int i = 0; i < this.mDisplayDisabled.size(); i++) {
            int keyAt = this.mDisplayDisabled.keyAt(i);
            callbacks.disable(keyAt, getDisabled1(keyAt), getDisabled2(keyAt), false);
        }
    }

    public void removeCallback(Callbacks callbacks) {
        this.mCallbacks.remove(callbacks);
    }

    public void setIcon(String str, StatusBarIcon statusBarIcon) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(65536, 1, 0, new Pair(str, statusBarIcon)).sendToTarget();
        }
    }

    public void removeIcon(String str) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(65536, 2, 0, str).sendToTarget();
        }
    }

    public void disable(int i, int i2, int i3, boolean z) {
        synchronized (this.mLock) {
            setDisabled(i, i2, i3);
            this.mHandler.removeMessages(131072);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            obtain.argi4 = z ? 1 : 0;
            Message obtainMessage = this.mHandler.obtainMessage(131072, obtain);
            if (Looper.myLooper() == this.mHandler.getLooper()) {
                this.mHandler.handleMessage(obtainMessage);
                obtainMessage.recycle();
            } else {
                obtainMessage.sendToTarget();
            }
        }
    }

    public void disable(int i, int i2, int i3) {
        disable(i, i2, i3, true);
    }

    public void recomputeDisableFlags(int i, boolean z) {
        synchronized (this.mLock) {
            disable(i, getDisabled1(i), getDisabled2(i), z);
        }
    }

    public final void setDisabled(int i, int i2, int i3) {
        this.mDisplayDisabled.put(i, new Pair(Integer.valueOf(i2), Integer.valueOf(i3)));
    }

    public final int getDisabled1(int i) {
        return ((Integer) getDisabled(i).first).intValue();
    }

    public final int getDisabled2(int i) {
        return ((Integer) getDisabled(i).second).intValue();
    }

    public final Pair<Integer, Integer> getDisabled(int i) {
        Pair<Integer, Integer> pair = this.mDisplayDisabled.get(i);
        if (pair != null) {
            return pair;
        }
        Pair<Integer, Integer> pair2 = new Pair<>(0, 0);
        this.mDisplayDisabled.put(i, pair2);
        return pair2;
    }

    public void animateExpandNotificationsPanel() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(196608);
            this.mHandler.sendEmptyMessage(196608);
        }
    }

    public void animateCollapsePanels() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(262144);
            this.mHandler.obtainMessage(262144, 0, 0).sendToTarget();
        }
    }

    public void animateCollapsePanels(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(262144);
            this.mHandler.obtainMessage(262144, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void togglePanel() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2293760);
            this.mHandler.obtainMessage(2293760, 0, 0).sendToTarget();
        }
    }

    public void animateExpandSettingsPanel(String str) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(327680);
            this.mHandler.obtainMessage(327680, str).sendToTarget();
        }
    }

    public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(524288);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            obtain.argi4 = z ? 1 : 0;
            obtain.arg1 = iBinder;
            this.mHandler.obtainMessage(524288, obtain).sendToTarget();
        }
    }

    public void showRecentApps(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(851968);
            this.mHandler.obtainMessage(851968, z ? 1 : 0, 0, (Object) null).sendToTarget();
        }
    }

    public void hideRecentApps(boolean z, boolean z2) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(917504);
            this.mHandler.obtainMessage(917504, z ? 1 : 0, z2 ? 1 : 0, (Object) null).sendToTarget();
        }
    }

    public void toggleSplitScreen() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1966080);
            this.mHandler.obtainMessage(1966080, 0, 0, (Object) null).sendToTarget();
        }
    }

    public void toggleRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(589824);
            Message obtainMessage = this.mHandler.obtainMessage(589824, 0, 0, (Object) null);
            obtainMessage.setAsynchronous(true);
            obtainMessage.sendToTarget();
        }
    }

    public void preloadRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(655360);
            this.mHandler.obtainMessage(655360, 0, 0, (Object) null).sendToTarget();
        }
    }

    public void cancelPreloadRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(720896);
            this.mHandler.obtainMessage(720896, 0, 0, (Object) null).sendToTarget();
        }
    }

    public void dismissKeyboardShortcutsMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2097152);
            this.mHandler.obtainMessage(2097152).sendToTarget();
        }
    }

    public void toggleKeyboardShortcutsMenu(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1638400);
            this.mHandler.obtainMessage(1638400, i, 0).sendToTarget();
        }
    }

    public void showPictureInPictureMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1703936);
            this.mHandler.obtainMessage(1703936).sendToTarget();
        }
    }

    public void setWindowState(int i, int i2, int i3) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(786432, i, i2, Integer.valueOf(i3)).sendToTarget();
        }
    }

    public void showScreenPinningRequest(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1179648, i, 0, (Object) null).sendToTarget();
        }
    }

    public void appTransitionPending(int i) {
        appTransitionPending(i, false);
    }

    public void appTransitionPending(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1245184, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void appTransitionCancelled(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1310720, i, 0).sendToTarget();
        }
    }

    public void appTransitionStarting(int i, long j, long j2) {
        appTransitionStarting(i, j, j2, false);
    }

    public void appTransitionStarting(int i, long j, long j2, boolean z) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = z ? 1 : 0;
            obtain.arg1 = Long.valueOf(j);
            obtain.arg2 = Long.valueOf(j2);
            this.mHandler.obtainMessage(1376256, obtain).sendToTarget();
        }
    }

    public void appTransitionFinished(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2031616, i, 0).sendToTarget();
        }
    }

    public void showAssistDisclosure() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1441792);
            this.mHandler.obtainMessage(1441792).sendToTarget();
        }
    }

    public void startAssist(Bundle bundle) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1507328);
            this.mHandler.obtainMessage(1507328, bundle).sendToTarget();
        }
    }

    public void onCameraLaunchGestureDetected(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1572864);
            this.mHandler.obtainMessage(1572864, i, 0).sendToTarget();
        }
    }

    public void onEmergencyActionLaunchGestureDetected() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(3801088);
            this.mHandler.obtainMessage(3801088).sendToTarget();
        }
    }

    public void addQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1769472, componentName).sendToTarget();
        }
    }

    public void remQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1835008, componentName).sendToTarget();
        }
    }

    public void clickQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1900544, componentName).sendToTarget();
        }
    }

    public void handleSystemKey(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2162688, i, 0).sendToTarget();
        }
    }

    public void showPinningEnterExitToast(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2949120, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void showPinningEscapeToast() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3014656).sendToTarget();
        }
    }

    public void showGlobalActionsMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2228224);
            this.mHandler.obtainMessage(2228224).sendToTarget();
        }
    }

    public void setTopAppHidesStatusBar(boolean z) {
        this.mHandler.removeMessages(2424832);
        this.mHandler.obtainMessage(2424832, z ? 1 : 0, 0).sendToTarget();
    }

    public void showShutdownUi(boolean z, String str) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2359296);
            this.mHandler.obtainMessage(2359296, z ? 1 : 0, 0, str).sendToTarget();
        }
    }

    public void showWirelessChargingAnimation(int i) {
        this.mHandler.removeMessages(2883584);
        this.mHandler.obtainMessage(2883584, i, 0).sendToTarget();
    }

    public void onProposedRotationChanged(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2490368);
            this.mHandler.obtainMessage(2490368, i, z ? 1 : 0, (Object) null).sendToTarget();
        }
    }

    public void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = promptInfo;
            obtain.arg2 = iBiometricSysuiReceiver;
            obtain.arg3 = iArr;
            obtain.arg4 = Boolean.valueOf(z);
            obtain.arg5 = Boolean.valueOf(z2);
            obtain.argi1 = i;
            obtain.arg6 = str;
            obtain.argl1 = j;
            obtain.argl2 = j2;
            obtain.argi2 = i2;
            this.mHandler.obtainMessage(2555904, obtain).sendToTarget();
        }
    }

    public void showToast(int i, String str, IBinder iBinder, CharSequence charSequence, IBinder iBinder2, int i2, ITransientNotificationCallback iTransientNotificationCallback, int i3) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = str;
            obtain.arg2 = iBinder;
            obtain.arg3 = charSequence;
            obtain.arg4 = iBinder2;
            obtain.arg5 = iTransientNotificationCallback;
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            this.mHandler.obtainMessage(3407872, obtain).sendToTarget();
        }
    }

    public void hideToast(String str, IBinder iBinder) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = str;
            obtain.arg2 = iBinder;
            this.mHandler.obtainMessage(3473408, obtain).sendToTarget();
        }
    }

    public void onBiometricAuthenticated(int i) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            this.mHandler.obtainMessage(2621440, obtain).sendToTarget();
        }
    }

    public void onBiometricHelp(int i, String str) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.arg1 = str;
            this.mHandler.obtainMessage(2686976, obtain).sendToTarget();
        }
    }

    public void onBiometricError(int i, int i2, int i3) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            this.mHandler.obtainMessage(2752512, obtain).sendToTarget();
        }
    }

    public void hideAuthenticationDialog(long j) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argl1 = j;
            this.mHandler.obtainMessage(2818048, obtain).sendToTarget();
        }
    }

    public void setBiometicContextListener(IBiometricContextListener iBiometricContextListener) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(4128768, iBiometricContextListener).sendToTarget();
        }
    }

    public void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3932160, iUdfpsHbmListener).sendToTarget();
        }
    }

    public void onDisplayReady(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(458752, i, 0).sendToTarget();
        }
    }

    public void onRecentsAnimationStateChanged(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3080192, z ? 1 : 0, 0).sendToTarget();
        }
    }

    public void showInattentiveSleepWarning() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3276800).sendToTarget();
        }
    }

    public void dismissInattentiveSleepWarning(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3342336, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void requestWindowMagnificationConnection(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3670016, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public final void handleShowImeButton(int i, IBinder iBinder, int i2, int i3, boolean z) {
        if (i != -1) {
            int i4 = this.mLastUpdatedImeDisplayId;
            if (!(i4 == i || i4 == -1)) {
                sendImeInvisibleStatusForPrevNavBar();
            }
            for (int i5 = 0; i5 < this.mCallbacks.size(); i5++) {
                this.mCallbacks.get(i5).setImeWindowStatus(i, iBinder, i2, i3, z);
            }
            this.mLastUpdatedImeDisplayId = i;
        }
    }

    public final void sendImeInvisibleStatusForPrevNavBar() {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).setImeWindowStatus(this.mLastUpdatedImeDisplayId, (IBinder) null, 4, 0, false);
        }
    }

    public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = z ? 1 : 0;
            obtain.arg1 = appearanceRegionArr;
            obtain.argi4 = i3;
            obtain.arg2 = insetsVisibilities;
            obtain.arg3 = str;
            this.mHandler.obtainMessage(393216, obtain).sendToTarget();
        }
    }

    public void showTransient(int i, int[] iArr, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3145728, i, z ? 1 : 0, iArr).sendToTarget();
        }
    }

    public void abortTransient(int i, int[] iArr) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3211264, i, 0, iArr).sendToTarget();
        }
    }

    public void startTracing() {
        synchronized (this.mLock) {
            ProtoTracer protoTracer = this.mProtoTracer;
            if (protoTracer != null) {
                protoTracer.start();
            }
            this.mHandler.obtainMessage(3538944, Boolean.TRUE).sendToTarget();
        }
    }

    public void stopTracing() {
        synchronized (this.mLock) {
            ProtoTracer protoTracer = this.mProtoTracer;
            if (protoTracer != null) {
                protoTracer.stop();
            }
            this.mHandler.obtainMessage(3538944, Boolean.FALSE).sendToTarget();
        }
    }

    public void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = strArr;
            obtain.arg2 = parcelFileDescriptor;
            this.mHandler.obtainMessage(3735552, obtain).sendToTarget();
        }
    }

    public void suppressAmbientDisplay(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3604480, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3866624, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void passThroughShellCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        final PrintWriter printWriter = new PrintWriter(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
        final String[] strArr2 = strArr;
        final ParcelFileDescriptor parcelFileDescriptor2 = parcelFileDescriptor;
        new Thread("Sysui.passThroughShellCommand") {
            public void run() {
                try {
                    if (CommandQueue.this.mRegistry == null) {
                    } else {
                        CommandQueue.this.mRegistry.onShellCommand(printWriter, strArr2);
                        printWriter.flush();
                        try {
                            parcelFileDescriptor2.close();
                        } catch (Exception unused) {
                        }
                    }
                } finally {
                    printWriter.flush();
                    try {
                        parcelFileDescriptor2.close();
                    } catch (Exception unused2) {
                    }
                }
            }
        }.start();
    }

    public void runGcForTest() {
        GcUtils.runGcAndFinalizersSync();
    }

    public void requestTileServiceListeningState(ComponentName componentName) {
        this.mHandler.obtainMessage(4456448, componentName).sendToTarget();
    }

    public void requestAddTile(ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, IAddTileResultCallback iAddTileResultCallback) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = componentName;
        obtain.arg2 = charSequence;
        obtain.arg3 = charSequence2;
        obtain.arg4 = icon;
        obtain.arg5 = iAddTileResultCallback;
        this.mHandler.obtainMessage(3997696, obtain).sendToTarget();
    }

    public void cancelRequestAddTile(String str) throws RemoteException {
        this.mHandler.obtainMessage(4063232, str).sendToTarget();
    }

    public void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) throws RemoteException {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = Integer.valueOf(i);
        obtain.arg2 = mediaRoute2Info;
        obtain.arg3 = iUndoMediaTransferCallback;
        this.mHandler.obtainMessage(4194304, obtain).sendToTarget();
    }

    public void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, CharSequence charSequence) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = Integer.valueOf(i);
        obtain.arg2 = mediaRoute2Info;
        obtain.arg3 = icon;
        obtain.arg4 = charSequence;
        this.mHandler.obtainMessage(4259840, obtain).sendToTarget();
    }

    public void registerNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        this.mHandler.obtainMessage(4325376, iNearbyMediaDevicesProvider).sendToTarget();
    }

    public void unregisterNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        this.mHandler.obtainMessage(4390912, iNearbyMediaDevicesProvider).sendToTarget();
    }

    public final class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:117:0x03c7, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:118:0x03c9, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).showPinningEscapeToast();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:120:0x03e5, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:121:0x03e7, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).showPinningEnterExitToast(((java.lang.Boolean) r1.obj).booleanValue());
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:123:0x040b, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:124:0x040d, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).showWirelessChargingAnimation(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:175:0x05c6, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:176:0x05c8, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).togglePanel();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:178:0x05e4, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:179:0x05e6, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).handleShowGlobalActionsMenu();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:181:0x0602, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:182:0x0604, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).handleSystemKey(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:184:0x0622, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:185:0x0624, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).dismissKeyboardShortcutsMenu();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:187:0x0640, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:188:0x0642, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).appTransitionFinished(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:190:0x0660, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:191:0x0662, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).toggleSplitScreen();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:193:0x067e, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:194:0x0680, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).clickTile((android.content.ComponentName) r1.obj);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:196:0x06a0, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:197:0x06a2, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).remQsTile((android.content.ComponentName) r1.obj);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:199:0x06c2, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:200:0x06c4, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).addQsTile((android.content.ComponentName) r1.obj);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:202:0x06e4, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:203:0x06e6, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).showPictureInPictureMenu();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:205:0x0702, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:206:0x0704, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).toggleKeyboardShortcutsMenu(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:208:0x0722, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:209:0x0724, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).onCameraLaunchGestureDetected(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:211:0x0742, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:212:0x0744, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).startAssist((android.os.Bundle) r1.obj);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:214:0x0764, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:215:0x0766, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).showAssistDisclosure();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:225:0x07bf, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:226:0x07c1, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).appTransitionCancelled(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:236:0x0807, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:237:0x0809, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).showScreenPinningRequest(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:259:0x087a, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x00f2, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:260:0x087c, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).setWindowState(r1.arg1, r1.arg2, ((java.lang.Integer) r1.obj).intValue());
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:262:0x08a4, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:263:0x08a6, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).cancelPreloadRecentApps();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:265:0x08c2, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:266:0x08c4, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).preloadRecentApps();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:268:0x08e0, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:269:0x08e2, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).toggleRecentApps();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x00f4, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).setBiometicContextListener((android.hardware.biometrics.IBiometricContextListener) r1.obj);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:276:0x091b, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:277:0x091d, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).onDisplayReady(r1.arg1);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:288:0x0980, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:289:0x0982, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).animateExpandSettingsPanel((java.lang.String) r1.obj);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:299:0x09ca, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:300:0x09cc, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).animateExpandNotificationsPanel();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x017a, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x017c, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).setUdfpsHbmListener((android.hardware.fingerprint.IUdfpsHbmListener) r1.obj);
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:426:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:428:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:430:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:431:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:433:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:436:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:437:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:441:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:442:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:443:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:447:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:448:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:449:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:450:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:451:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:452:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:453:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:454:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:455:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:456:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:457:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:458:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:459:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:460:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:462:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:464:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:467:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:468:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:469:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:470:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:471:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:472:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:474:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x01c4, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x01c6, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).onEmergencyActionLaunchGestureDetected();
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x022f, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x0231, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).requestWindowMagnificationConnection(((java.lang.Boolean) r1.obj).booleanValue());
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x0277, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:0x0279, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).onTracingStateChanged(((java.lang.Boolean) r1.obj).booleanValue());
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:93:0x030f, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x0311, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).dismissInattentiveSleepWarning(((java.lang.Boolean) r1.obj).booleanValue());
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:96:0x0335, code lost:
            if (r3 >= com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).size()) goto L_0x0a62;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:97:0x0337, code lost:
            ((com.android.systemui.statusbar.CommandQueue.Callbacks) com.android.systemui.statusbar.CommandQueue.m2110$$Nest$fgetmCallbacks(r0.this$0).get(r3)).showInattentiveSleepWarning();
            r3 = r3 + 1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r19) {
            /*
                r18 = this;
                r0 = r18
                r1 = r19
                int r2 = r1.what
                r3 = -65536(0xffffffffffff0000, float:NaN)
                r2 = r2 & r3
                r3 = 0
                r4 = 1
                switch(r2) {
                    case 65536: goto L_0x0a0e;
                    case 131072: goto L_0x09de;
                    case 196608: goto L_0x09c0;
                    case 262144: goto L_0x0998;
                    case 327680: goto L_0x0976;
                    case 393216: goto L_0x0931;
                    case 458752: goto L_0x0911;
                    case 524288: goto L_0x08f4;
                    case 589824: goto L_0x08d6;
                    case 655360: goto L_0x08b8;
                    case 720896: goto L_0x089a;
                    case 786432: goto L_0x0870;
                    case 851968: goto L_0x084a;
                    case 917504: goto L_0x081d;
                    case 1179648: goto L_0x07fd;
                    case 1245184: goto L_0x07d5;
                    case 1310720: goto L_0x07b5;
                    case 1376256: goto L_0x0778;
                    case 1441792: goto L_0x075a;
                    case 1507328: goto L_0x0738;
                    case 1572864: goto L_0x0718;
                    case 1638400: goto L_0x06f8;
                    case 1703936: goto L_0x06da;
                    case 1769472: goto L_0x06b8;
                    case 1835008: goto L_0x0696;
                    case 1900544: goto L_0x0674;
                    case 1966080: goto L_0x0656;
                    case 2031616: goto L_0x0636;
                    case 2097152: goto L_0x0618;
                    case 2162688: goto L_0x05f8;
                    case 2228224: goto L_0x05da;
                    case 2293760: goto L_0x05bc;
                    case 2359296: goto L_0x0592;
                    case 2424832: goto L_0x056c;
                    case 2490368: goto L_0x0544;
                    case 2555904: goto L_0x04cd;
                    case 2621440: goto L_0x04a4;
                    case 2686976: goto L_0x0477;
                    case 2752512: goto L_0x044a;
                    case 2818048: goto L_0x0421;
                    case 2883584: goto L_0x0401;
                    case 2949120: goto L_0x03db;
                    case 3014656: goto L_0x03bd;
                    case 3080192: goto L_0x0397;
                    case 3145728: goto L_0x036d;
                    case 3211264: goto L_0x0349;
                    case 3276800: goto L_0x032b;
                    case 3342336: goto L_0x0305;
                    case 3407872: goto L_0x02b9;
                    case 3473408: goto L_0x0293;
                    case 3538944: goto L_0x026d;
                    case 3604480: goto L_0x024b;
                    case 3670016: goto L_0x0225;
                    case 3735552: goto L_0x01d8;
                    case 3801088: goto L_0x01ba;
                    case 3866624: goto L_0x0192;
                    case 3932160: goto L_0x0170;
                    case 3997696: goto L_0x012c;
                    case 4063232: goto L_0x010a;
                    case 4128768: goto L_0x00e8;
                    case 4194304: goto L_0x00b1;
                    case 4259840: goto L_0x0076;
                    case 4325376: goto L_0x0054;
                    case 4390912: goto L_0x0032;
                    case 4456448: goto L_0x0010;
                    default: goto L_0x000e;
                }
            L_0x000e:
                goto L_0x0a62
            L_0x0010:
                java.lang.Object r1 = r1.obj
                android.content.ComponentName r1 = (android.content.ComponentName) r1
            L_0x0014:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                r2.requestTileServiceListeningState(r1)
                int r3 = r3 + 1
                goto L_0x0014
            L_0x0032:
                java.lang.Object r1 = r1.obj
                android.media.INearbyMediaDevicesProvider r1 = (android.media.INearbyMediaDevicesProvider) r1
            L_0x0036:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                r2.unregisterNearbyMediaDevicesProvider(r1)
                int r3 = r3 + 1
                goto L_0x0036
            L_0x0054:
                java.lang.Object r1 = r1.obj
                android.media.INearbyMediaDevicesProvider r1 = (android.media.INearbyMediaDevicesProvider) r1
            L_0x0058:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                r2.registerNearbyMediaDevicesProvider(r1)
                int r3 = r3 + 1
                goto L_0x0058
            L_0x0076:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                java.lang.Object r2 = r1.arg1
                java.lang.Integer r2 = (java.lang.Integer) r2
                int r2 = r2.intValue()
                java.lang.Object r4 = r1.arg2
                android.media.MediaRoute2Info r4 = (android.media.MediaRoute2Info) r4
                java.lang.Object r5 = r1.arg3
                android.graphics.drawable.Icon r5 = (android.graphics.drawable.Icon) r5
                java.lang.Object r6 = r1.arg4
                java.lang.CharSequence r6 = (java.lang.CharSequence) r6
            L_0x008e:
                com.android.systemui.statusbar.CommandQueue r7 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r7 = r7.mCallbacks
                int r7 = r7.size()
                if (r3 >= r7) goto L_0x00ac
                com.android.systemui.statusbar.CommandQueue r7 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r7 = r7.mCallbacks
                java.lang.Object r7 = r7.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r7 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r7
                r7.updateMediaTapToTransferReceiverDisplay(r2, r4, r5, r6)
                int r3 = r3 + 1
                goto L_0x008e
            L_0x00ac:
                r1.recycle()
                goto L_0x0a62
            L_0x00b1:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                java.lang.Object r2 = r1.arg1
                java.lang.Integer r2 = (java.lang.Integer) r2
                int r2 = r2.intValue()
                java.lang.Object r4 = r1.arg2
                android.media.MediaRoute2Info r4 = (android.media.MediaRoute2Info) r4
                java.lang.Object r5 = r1.arg3
                com.android.internal.statusbar.IUndoMediaTransferCallback r5 = (com.android.internal.statusbar.IUndoMediaTransferCallback) r5
            L_0x00c5:
                com.android.systemui.statusbar.CommandQueue r6 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r6 = r6.mCallbacks
                int r6 = r6.size()
                if (r3 >= r6) goto L_0x00e3
                com.android.systemui.statusbar.CommandQueue r6 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r6 = r6.mCallbacks
                java.lang.Object r6 = r6.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r6 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r6
                r6.updateMediaTapToTransferSenderDisplay(r2, r4, r5)
                int r3 = r3 + 1
                goto L_0x00c5
            L_0x00e3:
                r1.recycle()
                goto L_0x0a62
            L_0x00e8:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                android.hardware.biometrics.IBiometricContextListener r4 = (android.hardware.biometrics.IBiometricContextListener) r4
                r2.setBiometicContextListener(r4)
                int r3 = r3 + 1
                goto L_0x00e8
            L_0x010a:
                java.lang.Object r1 = r1.obj
                java.lang.String r1 = (java.lang.String) r1
            L_0x010e:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                r2.cancelRequestAddTile(r1)
                int r3 = r3 + 1
                goto L_0x010e
            L_0x012c:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                java.lang.Object r2 = r1.arg1
                android.content.ComponentName r2 = (android.content.ComponentName) r2
                java.lang.Object r4 = r1.arg2
                r10 = r4
                java.lang.CharSequence r10 = (java.lang.CharSequence) r10
                java.lang.Object r4 = r1.arg3
                r11 = r4
                java.lang.CharSequence r11 = (java.lang.CharSequence) r11
                java.lang.Object r4 = r1.arg4
                r12 = r4
                android.graphics.drawable.Icon r12 = (android.graphics.drawable.Icon) r12
                java.lang.Object r4 = r1.arg5
                r13 = r4
                com.android.internal.statusbar.IAddTileResultCallback r13 = (com.android.internal.statusbar.IAddTileResultCallback) r13
            L_0x0148:
                com.android.systemui.statusbar.CommandQueue r4 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r4 = r4.mCallbacks
                int r4 = r4.size()
                if (r3 >= r4) goto L_0x016b
                com.android.systemui.statusbar.CommandQueue r4 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r4 = r4.mCallbacks
                java.lang.Object r4 = r4.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r4 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r4
                r5 = r2
                r6 = r10
                r7 = r11
                r8 = r12
                r9 = r13
                r4.requestAddTile(r5, r6, r7, r8, r9)
                int r3 = r3 + 1
                goto L_0x0148
            L_0x016b:
                r1.recycle()
                goto L_0x0a62
            L_0x0170:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                android.hardware.fingerprint.IUdfpsHbmListener r4 = (android.hardware.fingerprint.IUdfpsHbmListener) r4
                r2.setUdfpsHbmListener(r4)
                int r3 = r3 + 1
                goto L_0x0170
            L_0x0192:
                r2 = r3
            L_0x0193:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                int r7 = r1.arg2
                if (r7 == 0) goto L_0x01b3
                r7 = r4
                goto L_0x01b4
            L_0x01b3:
                r7 = r3
            L_0x01b4:
                r5.setNavigationBarLumaSamplingEnabled(r6, r7)
                int r2 = r2 + 1
                goto L_0x0193
            L_0x01ba:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.onEmergencyActionLaunchGestureDetected()
                int r3 = r3 + 1
                goto L_0x01ba
            L_0x01d8:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                java.lang.Object r2 = r1.arg2     // Catch:{ IOException -> 0x0216 }
                android.os.ParcelFileDescriptor r2 = (android.os.ParcelFileDescriptor) r2     // Catch:{ IOException -> 0x0216 }
            L_0x01e0:
                com.android.systemui.statusbar.CommandQueue r4 = com.android.systemui.statusbar.CommandQueue.this     // Catch:{ all -> 0x0208 }
                java.util.ArrayList r4 = r4.mCallbacks     // Catch:{ all -> 0x0208 }
                int r4 = r4.size()     // Catch:{ all -> 0x0208 }
                if (r3 >= r4) goto L_0x0202
                com.android.systemui.statusbar.CommandQueue r4 = com.android.systemui.statusbar.CommandQueue.this     // Catch:{ all -> 0x0208 }
                java.util.ArrayList r4 = r4.mCallbacks     // Catch:{ all -> 0x0208 }
                java.lang.Object r4 = r4.get(r3)     // Catch:{ all -> 0x0208 }
                com.android.systemui.statusbar.CommandQueue$Callbacks r4 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r4     // Catch:{ all -> 0x0208 }
                java.lang.Object r5 = r1.arg1     // Catch:{ all -> 0x0208 }
                java.lang.String[] r5 = (java.lang.String[]) r5     // Catch:{ all -> 0x0208 }
                r4.handleWindowManagerLoggingCommand(r5, r2)     // Catch:{ all -> 0x0208 }
                int r3 = r3 + 1
                goto L_0x01e0
            L_0x0202:
                if (r2 == 0) goto L_0x0220
                r2.close()     // Catch:{ IOException -> 0x0216 }
                goto L_0x0220
            L_0x0208:
                r0 = move-exception
                r3 = r0
                if (r2 == 0) goto L_0x0215
                r2.close()     // Catch:{ all -> 0x0210 }
                goto L_0x0215
            L_0x0210:
                r0 = move-exception
                r2 = r0
                r3.addSuppressed(r2)     // Catch:{ IOException -> 0x0216 }
            L_0x0215:
                throw r3     // Catch:{ IOException -> 0x0216 }
            L_0x0216:
                r0 = move-exception
                java.lang.String r2 = com.android.systemui.statusbar.CommandQueue.TAG
                java.lang.String r3 = "Failed to handle logging command"
                android.util.Log.e(r2, r3, r0)
            L_0x0220:
                r1.recycle()
                goto L_0x0a62
            L_0x0225:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                java.lang.Boolean r4 = (java.lang.Boolean) r4
                boolean r4 = r4.booleanValue()
                r2.requestWindowMagnificationConnection(r4)
                int r3 = r3 + 1
                goto L_0x0225
            L_0x024b:
                com.android.systemui.statusbar.CommandQueue r0 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r0 = r0.mCallbacks
                java.util.Iterator r0 = r0.iterator()
            L_0x0255:
                boolean r2 = r0.hasNext()
                if (r2 == 0) goto L_0x0a62
                java.lang.Object r2 = r0.next()
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r3 = r1.obj
                java.lang.Boolean r3 = (java.lang.Boolean) r3
                boolean r3 = r3.booleanValue()
                r2.suppressAmbientDisplay(r3)
                goto L_0x0255
            L_0x026d:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                java.lang.Boolean r4 = (java.lang.Boolean) r4
                boolean r4 = r4.booleanValue()
                r2.onTracingStateChanged(r4)
                int r3 = r3 + 1
                goto L_0x026d
            L_0x0293:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                java.lang.Object r2 = r1.arg1
                java.lang.String r2 = (java.lang.String) r2
                java.lang.Object r1 = r1.arg2
                android.os.IBinder r1 = (android.os.IBinder) r1
                com.android.systemui.statusbar.CommandQueue r0 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r0 = r0.mCallbacks
                java.util.Iterator r0 = r0.iterator()
            L_0x02a9:
                boolean r3 = r0.hasNext()
                if (r3 == 0) goto L_0x0a62
                java.lang.Object r3 = r0.next()
                com.android.systemui.statusbar.CommandQueue$Callbacks r3 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r3
                r3.hideToast(r2, r1)
                goto L_0x02a9
            L_0x02b9:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                java.lang.Object r2 = r1.arg1
                java.lang.String r2 = (java.lang.String) r2
                java.lang.Object r3 = r1.arg2
                r12 = r3
                android.os.IBinder r12 = (android.os.IBinder) r12
                java.lang.Object r3 = r1.arg3
                r13 = r3
                java.lang.CharSequence r13 = (java.lang.CharSequence) r13
                java.lang.Object r3 = r1.arg4
                r14 = r3
                android.os.IBinder r14 = (android.os.IBinder) r14
                java.lang.Object r3 = r1.arg5
                r15 = r3
                android.app.ITransientNotificationCallback r15 = (android.app.ITransientNotificationCallback) r15
                int r11 = r1.argi1
                int r10 = r1.argi2
                int r1 = r1.argi3
                com.android.systemui.statusbar.CommandQueue r0 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r0 = r0.mCallbacks
                java.util.Iterator r0 = r0.iterator()
            L_0x02e5:
                boolean r3 = r0.hasNext()
                if (r3 == 0) goto L_0x0a62
                java.lang.Object r3 = r0.next()
                com.android.systemui.statusbar.CommandQueue$Callbacks r3 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r3
                r4 = r11
                r5 = r2
                r6 = r12
                r7 = r13
                r8 = r14
                r9 = r10
                r16 = r10
                r10 = r15
                r17 = r11
                r11 = r1
                r3.showToast(r4, r5, r6, r7, r8, r9, r10, r11)
                r10 = r16
                r11 = r17
                goto L_0x02e5
            L_0x0305:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                java.lang.Boolean r4 = (java.lang.Boolean) r4
                boolean r4 = r4.booleanValue()
                r2.dismissInattentiveSleepWarning(r4)
                int r3 = r3 + 1
                goto L_0x0305
            L_0x032b:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.showInattentiveSleepWarning()
                int r3 = r3 + 1
                goto L_0x032b
            L_0x0349:
                int r2 = r1.arg1
                java.lang.Object r1 = r1.obj
                int[] r1 = (int[]) r1
            L_0x034f:
                com.android.systemui.statusbar.CommandQueue r4 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r4 = r4.mCallbacks
                int r4 = r4.size()
                if (r3 >= r4) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r4 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r4 = r4.mCallbacks
                java.lang.Object r4 = r4.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r4 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r4
                r4.abortTransient(r2, r1)
                int r3 = r3 + 1
                goto L_0x034f
            L_0x036d:
                int r2 = r1.arg1
                java.lang.Object r5 = r1.obj
                int[] r5 = (int[]) r5
                int r1 = r1.arg2
                if (r1 == 0) goto L_0x0378
                goto L_0x0379
            L_0x0378:
                r4 = r3
            L_0x0379:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.showTransient(r2, r5, r4)
                int r3 = r3 + 1
                goto L_0x0379
            L_0x0397:
                r2 = r3
            L_0x0398:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                if (r6 <= 0) goto L_0x03b6
                r6 = r4
                goto L_0x03b7
            L_0x03b6:
                r6 = r3
            L_0x03b7:
                r5.onRecentsAnimationStateChanged(r6)
                int r2 = r2 + 1
                goto L_0x0398
            L_0x03bd:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.showPinningEscapeToast()
                int r3 = r3 + 1
                goto L_0x03bd
            L_0x03db:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                java.lang.Boolean r4 = (java.lang.Boolean) r4
                boolean r4 = r4.booleanValue()
                r2.showPinningEnterExitToast(r4)
                int r3 = r3 + 1
                goto L_0x03db
            L_0x0401:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.showWirelessChargingAnimation(r4)
                int r3 = r3 + 1
                goto L_0x0401
            L_0x0421:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
            L_0x0425:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0445
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                long r4 = r1.argl1
                r2.hideAuthenticationDialog(r4)
                int r3 = r3 + 1
                goto L_0x0425
            L_0x0445:
                r1.recycle()
                goto L_0x0a62
            L_0x044a:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
            L_0x044e:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0472
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.argi1
                int r5 = r1.argi2
                int r6 = r1.argi3
                r2.onBiometricError(r4, r5, r6)
                int r3 = r3 + 1
                goto L_0x044e
            L_0x0472:
                r1.recycle()
                goto L_0x0a62
            L_0x0477:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
            L_0x047b:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x049f
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.argi1
                java.lang.Object r5 = r1.arg1
                java.lang.String r5 = (java.lang.String) r5
                r2.onBiometricHelp(r4, r5)
                int r3 = r3 + 1
                goto L_0x047b
            L_0x049f:
                r1.recycle()
                goto L_0x0a62
            L_0x04a4:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
            L_0x04a8:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x04c8
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.argi1
                r2.onBiometricAuthenticated(r4)
                int r3 = r3 + 1
                goto L_0x04a8
            L_0x04c8:
                r1.recycle()
                goto L_0x0a62
            L_0x04cd:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                android.os.Handler r2 = r2.mHandler
                r4 = 2752512(0x2a0000, float:3.857091E-39)
                r2.removeMessages(r4)
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                android.os.Handler r2 = r2.mHandler
                r4 = 2686976(0x290000, float:3.765255E-39)
                r2.removeMessages(r4)
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                android.os.Handler r2 = r2.mHandler
                r4 = 2621440(0x280000, float:3.67342E-39)
                r2.removeMessages(r4)
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
            L_0x04f2:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x053f
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                r4 = r2
                com.android.systemui.statusbar.CommandQueue$Callbacks r4 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r4
                java.lang.Object r2 = r1.arg1
                r5 = r2
                android.hardware.biometrics.PromptInfo r5 = (android.hardware.biometrics.PromptInfo) r5
                java.lang.Object r2 = r1.arg2
                r6 = r2
                android.hardware.biometrics.IBiometricSysuiReceiver r6 = (android.hardware.biometrics.IBiometricSysuiReceiver) r6
                java.lang.Object r2 = r1.arg3
                r7 = r2
                int[] r7 = (int[]) r7
                java.lang.Object r2 = r1.arg4
                java.lang.Boolean r2 = (java.lang.Boolean) r2
                boolean r8 = r2.booleanValue()
                java.lang.Object r2 = r1.arg5
                java.lang.Boolean r2 = (java.lang.Boolean) r2
                boolean r9 = r2.booleanValue()
                int r10 = r1.argi1
                long r11 = r1.argl1
                java.lang.Object r2 = r1.arg6
                r13 = r2
                java.lang.String r13 = (java.lang.String) r13
                long r14 = r1.argl2
                int r2 = r1.argi2
                r16 = r2
                r4.showAuthenticationDialog(r5, r6, r7, r8, r9, r10, r11, r13, r14, r16)
                int r3 = r3 + 1
                goto L_0x04f2
            L_0x053f:
                r1.recycle()
                goto L_0x0a62
            L_0x0544:
                r2 = r3
            L_0x0545:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                int r7 = r1.arg2
                if (r7 == 0) goto L_0x0565
                r7 = r4
                goto L_0x0566
            L_0x0565:
                r7 = r3
            L_0x0566:
                r5.onRotationProposal(r6, r7)
                int r2 = r2 + 1
                goto L_0x0545
            L_0x056c:
                r2 = r3
            L_0x056d:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                if (r6 == 0) goto L_0x058b
                r6 = r4
                goto L_0x058c
            L_0x058b:
                r6 = r3
            L_0x058c:
                r5.setTopAppHidesStatusBar(r6)
                int r2 = r2 + 1
                goto L_0x056d
            L_0x0592:
                r2 = r3
            L_0x0593:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                if (r6 == 0) goto L_0x05b1
                r6 = r4
                goto L_0x05b2
            L_0x05b1:
                r6 = r3
            L_0x05b2:
                java.lang.Object r7 = r1.obj
                java.lang.String r7 = (java.lang.String) r7
                r5.handleShowShutdownUi(r6, r7)
                int r2 = r2 + 1
                goto L_0x0593
            L_0x05bc:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.togglePanel()
                int r3 = r3 + 1
                goto L_0x05bc
            L_0x05da:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.handleShowGlobalActionsMenu()
                int r3 = r3 + 1
                goto L_0x05da
            L_0x05f8:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.handleSystemKey(r4)
                int r3 = r3 + 1
                goto L_0x05f8
            L_0x0618:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.dismissKeyboardShortcutsMenu()
                int r3 = r3 + 1
                goto L_0x0618
            L_0x0636:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.appTransitionFinished(r4)
                int r3 = r3 + 1
                goto L_0x0636
            L_0x0656:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.toggleSplitScreen()
                int r3 = r3 + 1
                goto L_0x0656
            L_0x0674:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                android.content.ComponentName r4 = (android.content.ComponentName) r4
                r2.clickTile(r4)
                int r3 = r3 + 1
                goto L_0x0674
            L_0x0696:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                android.content.ComponentName r4 = (android.content.ComponentName) r4
                r2.remQsTile(r4)
                int r3 = r3 + 1
                goto L_0x0696
            L_0x06b8:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                android.content.ComponentName r4 = (android.content.ComponentName) r4
                r2.addQsTile(r4)
                int r3 = r3 + 1
                goto L_0x06b8
            L_0x06da:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.showPictureInPictureMenu()
                int r3 = r3 + 1
                goto L_0x06da
            L_0x06f8:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.toggleKeyboardShortcutsMenu(r4)
                int r3 = r3 + 1
                goto L_0x06f8
            L_0x0718:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.onCameraLaunchGestureDetected(r4)
                int r3 = r3 + 1
                goto L_0x0718
            L_0x0738:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                android.os.Bundle r4 = (android.os.Bundle) r4
                r2.startAssist(r4)
                int r3 = r3 + 1
                goto L_0x0738
            L_0x075a:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.showAssistDisclosure()
                int r3 = r3 + 1
                goto L_0x075a
            L_0x0778:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                r2 = r3
            L_0x077d:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                r6 = r5
                com.android.systemui.statusbar.CommandQueue$Callbacks r6 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r6
                int r7 = r1.argi1
                java.lang.Object r5 = r1.arg1
                java.lang.Long r5 = (java.lang.Long) r5
                long r8 = r5.longValue()
                java.lang.Object r5 = r1.arg2
                java.lang.Long r5 = (java.lang.Long) r5
                long r10 = r5.longValue()
                int r5 = r1.argi2
                if (r5 == 0) goto L_0x07ae
                r12 = r4
                goto L_0x07af
            L_0x07ae:
                r12 = r3
            L_0x07af:
                r6.appTransitionStarting(r7, r8, r10, r12)
                int r2 = r2 + 1
                goto L_0x077d
            L_0x07b5:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.appTransitionCancelled(r4)
                int r3 = r3 + 1
                goto L_0x07b5
            L_0x07d5:
                r2 = r3
            L_0x07d6:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                int r7 = r1.arg2
                if (r7 == 0) goto L_0x07f6
                r7 = r4
                goto L_0x07f7
            L_0x07f6:
                r7 = r3
            L_0x07f7:
                r5.appTransitionPending(r6, r7)
                int r2 = r2 + 1
                goto L_0x07d6
            L_0x07fd:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.showScreenPinningRequest(r4)
                int r3 = r3 + 1
                goto L_0x07fd
            L_0x081d:
                r2 = r3
            L_0x081e:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                if (r6 == 0) goto L_0x083c
                r6 = r4
                goto L_0x083d
            L_0x083c:
                r6 = r3
            L_0x083d:
                int r7 = r1.arg2
                if (r7 == 0) goto L_0x0843
                r7 = r4
                goto L_0x0844
            L_0x0843:
                r7 = r3
            L_0x0844:
                r5.hideRecentApps(r6, r7)
                int r2 = r2 + 1
                goto L_0x081e
            L_0x084a:
                r2 = r3
            L_0x084b:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                if (r6 == 0) goto L_0x0869
                r6 = r4
                goto L_0x086a
            L_0x0869:
                r6 = r3
            L_0x086a:
                r5.showRecentApps(r6)
                int r2 = r2 + 1
                goto L_0x084b
            L_0x0870:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                int r5 = r1.arg2
                java.lang.Object r6 = r1.obj
                java.lang.Integer r6 = (java.lang.Integer) r6
                int r6 = r6.intValue()
                r2.setWindowState(r4, r5, r6)
                int r3 = r3 + 1
                goto L_0x0870
            L_0x089a:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.cancelPreloadRecentApps()
                int r3 = r3 + 1
                goto L_0x089a
            L_0x08b8:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.preloadRecentApps()
                int r3 = r3 + 1
                goto L_0x08b8
            L_0x08d6:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.toggleRecentApps()
                int r3 = r3 + 1
                goto L_0x08d6
            L_0x08f4:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                int r6 = r1.argi1
                java.lang.Object r0 = r1.arg1
                r7 = r0
                android.os.IBinder r7 = (android.os.IBinder) r7
                int r8 = r1.argi2
                int r9 = r1.argi3
                int r0 = r1.argi4
                if (r0 == 0) goto L_0x090b
                r10 = r4
                goto L_0x090c
            L_0x090b:
                r10 = r3
            L_0x090c:
                r5.handleShowImeButton(r6, r7, r8, r9, r10)
                goto L_0x0a62
            L_0x0911:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                int r4 = r1.arg1
                r2.onDisplayReady(r4)
                int r3 = r3 + 1
                goto L_0x0911
            L_0x0931:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                r2 = r3
            L_0x0936:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0971
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                r6 = r5
                com.android.systemui.statusbar.CommandQueue$Callbacks r6 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r6
                int r7 = r1.argi1
                int r8 = r1.argi2
                java.lang.Object r5 = r1.arg1
                r9 = r5
                com.android.internal.view.AppearanceRegion[] r9 = (com.android.internal.view.AppearanceRegion[]) r9
                int r5 = r1.argi3
                if (r5 != r4) goto L_0x095e
                r10 = r4
                goto L_0x095f
            L_0x095e:
                r10 = r3
            L_0x095f:
                int r11 = r1.argi4
                java.lang.Object r5 = r1.arg2
                r12 = r5
                android.view.InsetsVisibilities r12 = (android.view.InsetsVisibilities) r12
                java.lang.Object r5 = r1.arg3
                r13 = r5
                java.lang.String r13 = (java.lang.String) r13
                r6.onSystemBarAttributesChanged(r7, r8, r9, r10, r11, r12, r13)
                int r2 = r2 + 1
                goto L_0x0936
            L_0x0971:
                r1.recycle()
                goto L_0x0a62
            L_0x0976:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                java.lang.String r4 = (java.lang.String) r4
                r2.animateExpandSettingsPanel(r4)
                int r3 = r3 + 1
                goto L_0x0976
            L_0x0998:
                r2 = r3
            L_0x0999:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.arg1
                int r7 = r1.arg2
                if (r7 == 0) goto L_0x09b9
                r7 = r4
                goto L_0x09ba
            L_0x09b9:
                r7 = r3
            L_0x09ba:
                r5.animateCollapsePanels(r6, r7)
                int r2 = r2 + 1
                goto L_0x0999
            L_0x09c0:
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                int r1 = r1.size()
                if (r3 >= r1) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r1 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r1 = r1.mCallbacks
                java.lang.Object r1 = r1.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r1 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r1
                r1.animateExpandNotificationsPanel()
                int r3 = r3 + 1
                goto L_0x09c0
            L_0x09de:
                java.lang.Object r1 = r1.obj
                com.android.internal.os.SomeArgs r1 = (com.android.internal.os.SomeArgs) r1
                r2 = r3
            L_0x09e3:
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                int r5 = r5.size()
                if (r2 >= r5) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r5 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r5 = r5.mCallbacks
                java.lang.Object r5 = r5.get(r2)
                com.android.systemui.statusbar.CommandQueue$Callbacks r5 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r5
                int r6 = r1.argi1
                int r7 = r1.argi2
                int r8 = r1.argi3
                int r9 = r1.argi4
                if (r9 == 0) goto L_0x0a07
                r9 = r4
                goto L_0x0a08
            L_0x0a07:
                r9 = r3
            L_0x0a08:
                r5.disable(r6, r7, r8, r9)
                int r2 = r2 + 1
                goto L_0x09e3
            L_0x0a0e:
                int r2 = r1.arg1
                if (r2 == r4) goto L_0x0a38
                r4 = 2
                if (r2 == r4) goto L_0x0a16
                goto L_0x0a62
            L_0x0a16:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.obj
                java.lang.String r4 = (java.lang.String) r4
                r2.removeIcon(r4)
                int r3 = r3 + 1
                goto L_0x0a16
            L_0x0a38:
                java.lang.Object r1 = r1.obj
                android.util.Pair r1 = (android.util.Pair) r1
            L_0x0a3c:
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                int r2 = r2.size()
                if (r3 >= r2) goto L_0x0a62
                com.android.systemui.statusbar.CommandQueue r2 = com.android.systemui.statusbar.CommandQueue.this
                java.util.ArrayList r2 = r2.mCallbacks
                java.lang.Object r2 = r2.get(r3)
                com.android.systemui.statusbar.CommandQueue$Callbacks r2 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r2
                java.lang.Object r4 = r1.first
                java.lang.String r4 = (java.lang.String) r4
                java.lang.Object r5 = r1.second
                com.android.internal.statusbar.StatusBarIcon r5 = (com.android.internal.statusbar.StatusBarIcon) r5
                r2.setIcon(r4, r5)
                int r3 = r3 + 1
                goto L_0x0a3c
            L_0x0a62:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.CommandQueue.H.handleMessage(android.os.Message):void");
        }
    }
}
