package com.android.systemui.biometrics;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.TaskStackListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.BiometricStateListener;
import android.hardware.biometrics.IBiometricContextListener;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.DisplayUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DisplayInfo;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.CoreStartable;
import com.android.systemui.R$array;
import com.android.systemui.R$dimen;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.biometrics.BiometricDisplayListener;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
import kotlin.Unit;

public class AuthController extends CoreStartable implements CommandQueue.Callbacks, AuthDialogCallback, DozeReceiver {
    public final ActivityTaskManager mActivityTaskManager;
    public boolean mAllFingerprintAuthenticatorsRegistered;
    public final DelayableExecutor mBackgroundExecutor;
    public IBiometricContextListener mBiometricContextListener;
    public final BiometricStateListener mBiometricStateListener = new BiometricStateListener() {
        public void onEnrollmentsChanged(int i, int i2, boolean z) {
            AuthController.this.mHandler.post(new AuthController$3$$ExternalSyntheticLambda0(this, i, i2, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentsChanged$0(int i, int i2, boolean z) {
            AuthController.this.handleEnrollmentsChanged(i, i2, z);
        }
    };
    @VisibleForTesting
    public final BroadcastReceiver mBroadcastReceiver;
    public final Set<Callback> mCallbacks = new HashSet();
    public final CommandQueue mCommandQueue;
    @VisibleForTesting
    public AuthDialog mCurrentDialog;
    public SomeArgs mCurrentDialogArgs;
    public final DisplayManager mDisplayManager;
    public final Execution mExecution;
    public final PointF mFaceAuthSensorLocation;
    public final FaceManager mFaceManager;
    public final List<FaceSensorPropertiesInternal> mFaceProps;
    public final IFingerprintAuthenticatorsRegisteredCallback mFingerprintAuthenticatorsRegisteredCallback = new IFingerprintAuthenticatorsRegisteredCallback.Stub() {
        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAllAuthenticatorsRegistered$0(List list) {
            AuthController.this.handleAllFingerprintAuthenticatorsRegistered(list);
        }

        public void onAllAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
            AuthController.this.mHandler.post(new AuthController$2$$ExternalSyntheticLambda0(this, list));
        }
    };
    public PointF mFingerprintLocation;
    public final FingerprintManager mFingerprintManager;
    public List<FingerprintSensorPropertiesInternal> mFpProps;
    public final Handler mHandler;
    public final LockPatternUtils mLockPatternUtils;
    @VisibleForTesting
    public final BiometricDisplayListener mOrientationListener;
    @VisibleForTesting
    public IBiometricSysuiReceiver mReceiver;
    public final SensorPrivacyManager mSensorPrivacyManager;
    public SidefpsController mSidefpsController;
    public final Provider<SidefpsController> mSidefpsControllerFactory;
    public List<FingerprintSensorPropertiesInternal> mSidefpsProps;
    public Point mStableDisplaySize = new Point();
    public final StatusBarStateController mStatusBarStateController;
    @VisibleForTesting
    public final TaskStackListener mTaskStackListener = new TaskStackListener() {
        public void onTaskStackChanged() {
            AuthController.this.mHandler.post(new AuthController$1$$ExternalSyntheticLambda0(AuthController.this));
        }
    };
    public Rect mUdfpsBounds;
    public UdfpsController mUdfpsController;
    public final Provider<UdfpsController> mUdfpsControllerFactory;
    public final SparseBooleanArray mUdfpsEnrolledForUser;
    public IUdfpsHbmListener mUdfpsHbmListener;
    public List<FingerprintSensorPropertiesInternal> mUdfpsProps;
    public final UserManager mUserManager;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WindowManager mWindowManager;

    public interface Callback {
        void onAllAuthenticatorsRegistered() {
        }

        void onBiometricPromptDismissed() {
        }

        void onBiometricPromptShown() {
        }

        void onEnrollmentsChanged() {
        }

        void onUdfpsLocationChanged() {
        }
    }

    public final void cancelIfOwnerIsNotInForeground() {
        this.mExecution.assertIsMainThread();
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            try {
                String opPackageName = authDialog.getOpPackageName();
                Log.w("AuthController", "Task stack changed, current client: " + opPackageName);
                List tasks = this.mActivityTaskManager.getTasks(1);
                if (!tasks.isEmpty()) {
                    String packageName = ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity.getPackageName();
                    if (!packageName.contentEquals(opPackageName) && !Utils.isSystem(this.mContext, opPackageName)) {
                        Log.e("AuthController", "Evicting client due to: " + packageName);
                        this.mCurrentDialog.dismissWithoutCallback(true);
                        this.mCurrentDialog = null;
                        this.mOrientationListener.disable();
                        for (Callback onBiometricPromptDismissed : this.mCallbacks) {
                            onBiometricPromptDismissed.onBiometricPromptDismissed();
                        }
                        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
                        if (iBiometricSysuiReceiver != null) {
                            iBiometricSysuiReceiver.onDialogDismissed(3, (byte[]) null);
                            this.mReceiver = null;
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e("AuthController", "Remote exception", e);
            }
        }
    }

    public boolean areAllFingerprintAuthenticatorsRegistered() {
        return this.mAllFingerprintAuthenticatorsRegistered;
    }

    public final void handleAllFingerprintAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
        this.mExecution.assertIsMainThread();
        Log.d("AuthController", "handleAllAuthenticatorsRegistered | sensors: " + Arrays.toString(list.toArray()));
        this.mAllFingerprintAuthenticatorsRegistered = true;
        this.mFpProps = list;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (FingerprintSensorPropertiesInternal next : this.mFpProps) {
            if (next.isAnyUdfpsType()) {
                arrayList.add(next);
            }
            if (next.isAnySidefpsType()) {
                arrayList2.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            arrayList = null;
        }
        this.mUdfpsProps = arrayList;
        if (arrayList != null) {
            UdfpsController udfpsController = this.mUdfpsControllerFactory.get();
            this.mUdfpsController = udfpsController;
            udfpsController.addCallback(new UdfpsController.Callback() {
                public void onFingerUp() {
                }

                public void onFingerDown() {
                    AuthDialog authDialog = AuthController.this.mCurrentDialog;
                    if (authDialog != null) {
                        authDialog.onPointerDown();
                    }
                }
            });
            this.mUdfpsController.setAuthControllerUpdateUdfpsLocation(new AuthController$$ExternalSyntheticLambda3(this));
            this.mUdfpsController.setHalControlsIllumination(this.mUdfpsProps.get(0).halControlsIllumination);
            this.mUdfpsBounds = this.mUdfpsProps.get(0).getLocation().getRect();
            updateUdfpsLocation();
        }
        if (arrayList2.isEmpty()) {
            arrayList2 = null;
        }
        this.mSidefpsProps = arrayList2;
        if (arrayList2 != null) {
            this.mSidefpsController = this.mSidefpsControllerFactory.get();
        }
        this.mFingerprintManager.registerBiometricStateListener(this.mBiometricStateListener);
        updateFingerprintLocation();
        for (Callback onAllAuthenticatorsRegistered : this.mCallbacks) {
            onAllAuthenticatorsRegistered.onAllAuthenticatorsRegistered();
        }
    }

    public final void handleEnrollmentsChanged(int i, int i2, boolean z) {
        this.mExecution.assertIsMainThread();
        Log.d("AuthController", "handleEnrollmentsChanged, userId: " + i + ", sensorId: " + i2 + ", hasEnrollments: " + z);
        List<FingerprintSensorPropertiesInternal> list = this.mUdfpsProps;
        if (list == null) {
            Log.d("AuthController", "handleEnrollmentsChanged, mUdfpsProps is null");
        } else {
            for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : list) {
                if (fingerprintSensorPropertiesInternal.sensorId == i2) {
                    this.mUdfpsEnrolledForUser.put(i, z);
                }
            }
        }
        for (Callback onEnrollmentsChanged : this.mCallbacks) {
            onEnrollmentsChanged.onEnrollmentsChanged();
        }
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public void dozeTimeTick() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.dozeTimeTick();
        }
    }

    public void onTryAgainPressed() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onTryAgainPressed: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onTryAgainPressed();
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when handling try again", e);
        }
    }

    public void onDeviceCredentialPressed() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onDeviceCredentialPressed: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDeviceCredentialPressed();
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when handling credential button", e);
        }
    }

    public void onSystemEvent(int i) {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onSystemEvent(" + i + "): Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onSystemEvent(i);
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when sending system event", e);
        }
    }

    public void onDialogAnimatedIn() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onDialogAnimatedIn: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDialogAnimatedIn();
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when sending onDialogAnimatedIn", e);
        }
    }

    public void onDismissed(int i, byte[] bArr) {
        switch (i) {
            case 1:
                sendResultAndCleanUp(3, bArr);
                return;
            case 2:
                sendResultAndCleanUp(2, bArr);
                return;
            case 3:
                sendResultAndCleanUp(1, bArr);
                return;
            case 4:
                sendResultAndCleanUp(4, bArr);
                return;
            case 5:
                sendResultAndCleanUp(5, bArr);
                return;
            case 6:
                sendResultAndCleanUp(6, bArr);
                return;
            case 7:
                sendResultAndCleanUp(7, bArr);
                return;
            default:
                Log.e("AuthController", "Unhandled reason: " + i);
                return;
        }
    }

    public PointF getUdfpsLocation() {
        if (this.mUdfpsController == null || this.mUdfpsBounds == null) {
            return null;
        }
        return new PointF((float) this.mUdfpsBounds.centerX(), (float) this.mUdfpsBounds.centerY());
    }

    public float getUdfpsRadius() {
        Rect rect;
        if (this.mUdfpsController == null || (rect = this.mUdfpsBounds) == null) {
            return -1.0f;
        }
        return ((float) rect.height()) / 2.0f;
    }

    public float getScaleFactor() {
        UdfpsOverlayParams udfpsOverlayParams;
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null || (udfpsOverlayParams = udfpsController.mOverlayParams) == null) {
            return 1.0f;
        }
        return udfpsOverlayParams.getScaleFactor();
    }

    public PointF getFingerprintSensorLocation() {
        if (getUdfpsLocation() != null) {
            return getUdfpsLocation();
        }
        return this.mFingerprintLocation;
    }

    public PointF getFaceAuthSensorLocation() {
        if (this.mFaceProps == null || this.mFaceAuthSensorLocation == null) {
            return null;
        }
        DisplayInfo displayInfo = new DisplayInfo();
        this.mContext.getDisplay().getDisplayInfo(displayInfo);
        Point point = this.mStableDisplaySize;
        float physicalPixelDisplaySizeRatio = DisplayUtils.getPhysicalPixelDisplaySizeRatio(point.x, point.y, displayInfo.getNaturalWidth(), displayInfo.getNaturalHeight());
        PointF pointF = this.mFaceAuthSensorLocation;
        return new PointF(pointF.x * physicalPixelDisplaySizeRatio, pointF.y * physicalPixelDisplaySizeRatio);
    }

    public void onAodInterrupt(int i, int i2, float f, float f2) {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.onAodInterrupt(i, i2, f, f2);
        }
    }

    public void onCancelUdfps() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.onCancelUdfps();
        }
    }

    public final void sendResultAndCleanUp(int i, byte[] bArr) {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "sendResultAndCleanUp: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDialogDismissed(i, bArr);
        } catch (RemoteException e) {
            Log.w("AuthController", "Remote exception", e);
        }
        onDialogDismissed(i);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AuthController(Context context, Execution execution, CommandQueue commandQueue, ActivityTaskManager activityTaskManager, WindowManager windowManager, FingerprintManager fingerprintManager, FaceManager faceManager, Provider<UdfpsController> provider, Provider<SidefpsController> provider2, DisplayManager displayManager, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils, StatusBarStateController statusBarStateController, Handler handler, DelayableExecutor delayableExecutor) {
        super(context);
        Context context2 = context;
        FaceManager faceManager2 = faceManager;
        StatusBarStateController statusBarStateController2 = statusBarStateController;
        AnonymousClass4 r10 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (AuthController.this.mCurrentDialog != null && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    Log.w("AuthController", "ACTION_CLOSE_SYSTEM_DIALOGS received");
                    AuthController.this.mCurrentDialog.dismissWithoutCallback(true);
                    AuthController authController = AuthController.this;
                    authController.mCurrentDialog = null;
                    authController.mOrientationListener.disable();
                    for (Callback onBiometricPromptDismissed : AuthController.this.mCallbacks) {
                        onBiometricPromptDismissed.onBiometricPromptDismissed();
                    }
                    try {
                        IBiometricSysuiReceiver iBiometricSysuiReceiver = AuthController.this.mReceiver;
                        if (iBiometricSysuiReceiver != null) {
                            iBiometricSysuiReceiver.onDialogDismissed(3, (byte[]) null);
                            AuthController.this.mReceiver = null;
                        }
                    } catch (RemoteException e) {
                        Log.e("AuthController", "Remote exception", e);
                    }
                }
            }
        };
        this.mBroadcastReceiver = r10;
        this.mExecution = execution;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mUserManager = userManager;
        this.mLockPatternUtils = lockPatternUtils;
        Handler handler2 = handler;
        this.mHandler = handler2;
        this.mBackgroundExecutor = delayableExecutor;
        this.mCommandQueue = commandQueue;
        this.mActivityTaskManager = activityTaskManager;
        this.mFingerprintManager = fingerprintManager;
        this.mFaceManager = faceManager2;
        this.mUdfpsControllerFactory = provider;
        this.mSidefpsControllerFactory = provider2;
        DisplayManager displayManager2 = displayManager;
        this.mDisplayManager = displayManager2;
        this.mWindowManager = windowManager;
        this.mUdfpsEnrolledForUser = new SparseBooleanArray();
        this.mOrientationListener = new BiometricDisplayListener(context, displayManager2, handler2, BiometricDisplayListener.SensorType.Generic.INSTANCE, new AuthController$$ExternalSyntheticLambda0(this));
        this.mStatusBarStateController = statusBarStateController2;
        statusBarStateController2.addCallback(new StatusBarStateController.StateListener() {
            public void onDozingChanged(boolean z) {
                AuthController.this.notifyDozeChanged(z);
            }
        });
        this.mFaceProps = faceManager2 != null ? faceManager.getSensorPropertiesInternal() : null;
        int[] intArray = context.getResources().getIntArray(R$array.config_face_auth_props);
        if (intArray == null || intArray.length < 2) {
            this.mFaceAuthSensorLocation = null;
        } else {
            this.mFaceAuthSensorLocation = new PointF((float) intArray[0], (float) intArray[1]);
        }
        updateFingerprintLocation();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(r10, intentFilter, 2);
        this.mSensorPrivacyManager = (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$0() {
        onOrientationChanged();
        return Unit.INSTANCE;
    }

    public final int getDisplayWidth() {
        DisplayInfo displayInfo = new DisplayInfo();
        this.mContext.getDisplay().getDisplayInfo(displayInfo);
        return displayInfo.getNaturalWidth();
    }

    public final void updateFingerprintLocation() {
        int displayWidth = getDisplayWidth() / 2;
        try {
            displayWidth = this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_fingerprint_sensor_center_screen_location_x);
        } catch (Resources.NotFoundException unused) {
        }
        this.mFingerprintLocation = new PointF((float) displayWidth, (float) this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_fingerprint_sensor_center_screen_location_y));
    }

    public final void updateUdfpsLocation() {
        if (this.mUdfpsController != null) {
            DisplayInfo displayInfo = new DisplayInfo();
            this.mContext.getDisplay().getDisplayInfo(displayInfo);
            Point point = this.mStableDisplaySize;
            float physicalPixelDisplaySizeRatio = DisplayUtils.getPhysicalPixelDisplaySizeRatio(point.x, point.y, displayInfo.getNaturalWidth(), displayInfo.getNaturalHeight());
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mUdfpsProps.get(0);
            Rect rect = this.mUdfpsBounds;
            Rect rect2 = fingerprintSensorPropertiesInternal.getLocation().getRect();
            this.mUdfpsBounds = rect2;
            rect2.scale(physicalPixelDisplaySizeRatio);
            this.mUdfpsController.updateOverlayParams(fingerprintSensorPropertiesInternal.sensorId, new UdfpsOverlayParams(this.mUdfpsBounds, displayInfo.getNaturalWidth(), displayInfo.getNaturalHeight(), physicalPixelDisplaySizeRatio, displayInfo.rotation));
            if (!Objects.equals(rect, this.mUdfpsBounds)) {
                for (Callback onUdfpsLocationChanged : this.mCallbacks) {
                    onUdfpsLocationChanged.onUdfpsLocationChanged();
                }
            }
        }
    }

    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager != null) {
            fingerprintManager.addAuthenticatorsRegisteredCallback(this.mFingerprintAuthenticatorsRegisteredCallback);
        }
        this.mStableDisplaySize = this.mDisplayManager.getStableDisplaySize();
        this.mActivityTaskManager.registerTaskStackListener(this.mTaskStackListener);
    }

    public void setBiometicContextListener(IBiometricContextListener iBiometricContextListener) {
        this.mBiometricContextListener = iBiometricContextListener;
        notifyDozeChanged(this.mStatusBarStateController.isDozing());
    }

    public final void notifyDozeChanged(boolean z) {
        IBiometricContextListener iBiometricContextListener = this.mBiometricContextListener;
        if (iBiometricContextListener != null) {
            try {
                iBiometricContextListener.onDozeChanged(z);
            } catch (RemoteException unused) {
                Log.w("AuthController", "failed to notify initial doze state");
            }
        }
    }

    public void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        this.mUdfpsHbmListener = iUdfpsHbmListener;
    }

    public void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        int[] iArr2 = iArr;
        long j3 = j;
        long j4 = j2;
        int i3 = i2;
        int authenticators = promptInfo.getAuthenticators();
        StringBuilder sb = new StringBuilder();
        boolean z3 = false;
        for (int append : iArr2) {
            sb.append(append);
            sb.append(" ");
        }
        Log.d("AuthController", "showAuthenticationDialog, authenticators: " + authenticators + ", sensorIds: " + sb.toString() + ", credentialAllowed: " + z + ", requireConfirmation: " + z2 + ", operationId: " + j3 + ", requestId: " + j4 + ", multiSensorConfig: " + i3);
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = promptInfo;
        obtain.arg2 = iBiometricSysuiReceiver;
        obtain.arg3 = iArr2;
        obtain.arg4 = Boolean.valueOf(z);
        obtain.arg5 = Boolean.valueOf(z2);
        obtain.argi1 = i;
        obtain.arg6 = str;
        obtain.argl1 = j3;
        obtain.argl2 = j4;
        obtain.argi2 = i3;
        if (this.mCurrentDialog != null) {
            Log.w("AuthController", "mCurrentDialog: " + this.mCurrentDialog);
            z3 = true;
        }
        showDialog(obtain, z3, (Bundle) null);
    }

    public void onBiometricAuthenticated(int i) {
        Log.d("AuthController", "onBiometricAuthenticated: ");
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onAuthenticationSucceeded(i);
        } else {
            Log.w("AuthController", "onBiometricAuthenticated callback but dialog gone");
        }
    }

    public void onBiometricHelp(int i, String str) {
        Log.d("AuthController", "onBiometricHelp: " + str);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onHelp(i, str);
        } else {
            Log.w("AuthController", "onBiometricHelp callback but dialog gone");
        }
    }

    public List<FingerprintSensorPropertiesInternal> getUdfpsProps() {
        return this.mUdfpsProps;
    }

    public final String getErrorString(int i, int i2, int i3) {
        if (i != 2) {
            return i != 8 ? "" : FaceManager.getErrorString(this.mContext, i2, i3);
        }
        return FingerprintManager.getErrorString(this.mContext, i2, i3);
    }

    public void onBiometricError(int i, int i2, int i3) {
        String str;
        boolean z = false;
        Log.d("AuthController", String.format("onBiometricError(%d, %d, %d)", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)}));
        boolean z2 = i2 == 7 || i2 == 9;
        boolean z3 = i2 == 1 && this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, 2);
        if (i2 == 100 || i2 == 3 || z3) {
            z = true;
        }
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog == null) {
            Log.w("AuthController", "onBiometricError callback but dialog is gone");
        } else if (authDialog.isAllowDeviceCredentials() && z2) {
            Log.d("AuthController", "onBiometricError, lockout");
            this.mCurrentDialog.animateToCredentialUI();
        } else if (z) {
            if (i2 == 100) {
                str = this.mContext.getString(17039811);
            } else {
                str = getErrorString(i, i2, i3);
            }
            Log.d("AuthController", "onBiometricError, soft error: " + str);
            if (z3) {
                this.mHandler.postDelayed(new AuthController$$ExternalSyntheticLambda2(this, i), 500);
            } else {
                this.mCurrentDialog.onAuthenticationFailed(i, str);
            }
        } else {
            String errorString = getErrorString(i, i2, i3);
            Log.d("AuthController", "onBiometricError, hard error: " + errorString);
            this.mCurrentDialog.onError(i, errorString);
        }
        onCancelUdfps();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBiometricError$1(int i) {
        this.mCurrentDialog.onAuthenticationFailed(i, this.mContext.getString(17040309));
    }

    public void hideAuthenticationDialog(long j) {
        Log.d("AuthController", "hideAuthenticationDialog: " + this.mCurrentDialog);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog == null) {
            Log.d("AuthController", "dialog already gone");
        } else if (j != authDialog.getRequestId()) {
            Log.w("AuthController", "ignore - ids do not match: " + j + " current: " + this.mCurrentDialog.getRequestId());
        } else {
            this.mCurrentDialog.dismissFromSystemServer();
            this.mCurrentDialog = null;
            this.mOrientationListener.disable();
        }
    }

    public boolean isUdfpsFingerDown() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null) {
            return false;
        }
        return udfpsController.isFingerDown();
    }

    public boolean isUdfpsEnrolled(int i) {
        if (this.mUdfpsController == null) {
            return false;
        }
        return this.mUdfpsEnrolledForUser.get(i);
    }

    public final void showDialog(SomeArgs someArgs, boolean z, Bundle bundle) {
        SomeArgs someArgs2 = someArgs;
        Bundle bundle2 = bundle;
        this.mCurrentDialogArgs = someArgs2;
        PromptInfo promptInfo = (PromptInfo) someArgs2.arg1;
        ((Boolean) someArgs2.arg4).booleanValue();
        boolean booleanValue = ((Boolean) someArgs2.arg5).booleanValue();
        int i = someArgs2.argi1;
        long j = someArgs2.argl1;
        long j2 = someArgs2.argl2;
        int i2 = someArgs2.argi2;
        DelayableExecutor delayableExecutor = this.mBackgroundExecutor;
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        UserManager userManager = this.mUserManager;
        UserManager userManager2 = userManager;
        PromptInfo promptInfo2 = promptInfo;
        WakefulnessLifecycle wakefulnessLifecycle2 = wakefulnessLifecycle;
        int i3 = i;
        int i4 = i2;
        int i5 = i;
        AuthDialog buildDialog = buildDialog(delayableExecutor, promptInfo2, booleanValue, i3, (int[]) someArgs2.arg3, (String) someArgs2.arg6, z, j, j2, i4, wakefulnessLifecycle2, userManager2, this.mLockPatternUtils);
        if (buildDialog == null) {
            Log.e("AuthController", "Unsupported type configuration");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("userId: ");
        sb.append(i5);
        sb.append(" savedState: ");
        Bundle bundle3 = bundle;
        sb.append(bundle3);
        sb.append(" mCurrentDialog: ");
        sb.append(this.mCurrentDialog);
        sb.append(" newDialog: ");
        sb.append(buildDialog);
        Log.d("AuthController", sb.toString());
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.dismissWithoutCallback(false);
        }
        this.mReceiver = (IBiometricSysuiReceiver) someArgs.arg2;
        for (Callback onBiometricPromptShown : this.mCallbacks) {
            onBiometricPromptShown.onBiometricPromptShown();
        }
        this.mCurrentDialog = buildDialog;
        buildDialog.show(this.mWindowManager, bundle3);
        this.mOrientationListener.enable();
        if (!promptInfo.isAllowBackgroundAuthentication()) {
            this.mHandler.post(new AuthController$$ExternalSyntheticLambda1(this));
        }
    }

    public final void onDialogDismissed(int i) {
        Log.d("AuthController", "onDialogDismissed: " + i);
        if (this.mCurrentDialog == null) {
            Log.w("AuthController", "Dialog already dismissed");
        }
        for (Callback onBiometricPromptDismissed : this.mCallbacks) {
            onBiometricPromptDismissed.onBiometricPromptDismissed();
        }
        this.mReceiver = null;
        this.mCurrentDialog = null;
        this.mOrientationListener.disable();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateFingerprintLocation();
        updateUdfpsLocation();
        if (this.mCurrentDialog != null) {
            Bundle bundle = new Bundle();
            this.mCurrentDialog.onSaveState(bundle);
            this.mCurrentDialog.dismissWithoutCallback(false);
            this.mCurrentDialog = null;
            this.mOrientationListener.disable();
            if (!bundle.getBoolean("container_going_away", false)) {
                if (bundle.getBoolean("credential_showing")) {
                    ((PromptInfo) this.mCurrentDialogArgs.arg1).setAuthenticators(32768);
                }
                showDialog(this.mCurrentDialogArgs, true, bundle);
            }
        }
    }

    public final void onOrientationChanged() {
        updateFingerprintLocation();
        updateUdfpsLocation();
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onOrientationChanged();
        }
    }

    public AuthDialog buildDialog(DelayableExecutor delayableExecutor, PromptInfo promptInfo, boolean z, int i, int[] iArr, String str, boolean z2, long j, long j2, int i2, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils) {
        PromptInfo promptInfo2 = promptInfo;
        boolean z3 = z;
        int i3 = i;
        return new AuthContainerView.Builder(this.mContext).setCallback(this).setPromptInfo(promptInfo).setRequireConfirmation(z).setUserId(i).setOpPackageName(str).setSkipIntro(z2).setOperationId(j).setRequestId(j2).setMultiSensorConfig(i2).build(delayableExecutor, iArr, this.mFpProps, this.mFaceProps, wakefulnessLifecycle, userManager, lockPatternUtils);
    }
}
