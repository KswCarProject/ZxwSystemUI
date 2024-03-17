package com.android.systemui.log;

import android.app.StatusBarManager;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.CoreStartable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class SessionTracker extends CoreStartable {
    public final AuthController mAuthController;
    public AuthController.Callback mAuthControllerCallback = new AuthController.Callback() {
        public void onBiometricPromptShown() {
            SessionTracker.this.startSession(2);
        }

        public void onBiometricPromptDismissed() {
            SessionTracker.this.endSession(2);
        }
    };
    public final InstanceIdSequence mInstanceIdGenerator = new InstanceIdSequence(1048576);
    public boolean mKeyguardSessionStarted;
    public KeyguardStateController.Callback mKeyguardStateCallback = new KeyguardStateController.Callback() {
        public void onKeyguardShowingChanged() {
            boolean r0 = SessionTracker.this.mKeyguardSessionStarted;
            boolean isShowing = SessionTracker.this.mKeyguardStateController.isShowing();
            if (isShowing && !r0) {
                SessionTracker.this.mKeyguardSessionStarted = true;
                SessionTracker.this.startSession(1);
            } else if (!isShowing && r0) {
                SessionTracker.this.mKeyguardSessionStarted = false;
                SessionTracker.this.endSession(1);
            }
        }
    };
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onStartedGoingToSleep(int i) {
            if (!SessionTracker.this.mKeyguardSessionStarted) {
                SessionTracker.this.mKeyguardSessionStarted = true;
                SessionTracker.this.startSession(1);
            }
        }
    };
    public final Map<Integer, InstanceId> mSessionToInstanceId = new HashMap();
    public final IStatusBarService mStatusBarManagerService;

    public SessionTracker(Context context, IStatusBarService iStatusBarService, AuthController authController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController) {
        super(context);
        this.mStatusBarManagerService = iStatusBarService;
        this.mAuthController = authController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
    }

    public void start() {
        this.mAuthController.addCallback(this.mAuthControllerCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        boolean isShowing = this.mKeyguardStateController.isShowing();
        this.mKeyguardSessionStarted = isShowing;
        if (isShowing) {
            startSession(1);
        }
    }

    public InstanceId getSessionId(int i) {
        return this.mSessionToInstanceId.getOrDefault(Integer.valueOf(i), (Object) null);
    }

    public final void startSession(int i) {
        if (this.mSessionToInstanceId.getOrDefault(Integer.valueOf(i), (Object) null) != null) {
            Log.e("SessionTracker", "session [" + getString(i) + "] was already started");
            return;
        }
        InstanceId newInstanceId = this.mInstanceIdGenerator.newInstanceId();
        this.mSessionToInstanceId.put(Integer.valueOf(i), newInstanceId);
        try {
            this.mStatusBarManagerService.onSessionStarted(i, newInstanceId);
        } catch (RemoteException e) {
            Log.e("SessionTracker", "Unable to send onSessionStarted for session=[" + getString(i) + "]", e);
        }
    }

    public final void endSession(int i) {
        if (this.mSessionToInstanceId.getOrDefault(Integer.valueOf(i), (Object) null) == null) {
            Log.e("SessionTracker", "session [" + getString(i) + "] was not started");
            return;
        }
        InstanceId instanceId = this.mSessionToInstanceId.get(Integer.valueOf(i));
        this.mSessionToInstanceId.put(Integer.valueOf(i), (Object) null);
        try {
            this.mStatusBarManagerService.onSessionEnded(i, instanceId);
        } catch (RemoteException e) {
            Log.e("SessionTracker", "Unable to send onSessionEnded for session=[" + getString(i) + "]", e);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        for (Integer intValue : StatusBarManager.ALL_SESSIONS) {
            int intValue2 = intValue.intValue();
            printWriter.println("  " + getString(intValue2) + " instanceId=" + this.mSessionToInstanceId.get(Integer.valueOf(intValue2)));
        }
    }

    public static String getString(int i) {
        if (i == 1) {
            return "KEYGUARD";
        }
        if (i == 2) {
            return "BIOMETRIC_PROMPT";
        }
        return "unknownType=" + i;
    }
}
