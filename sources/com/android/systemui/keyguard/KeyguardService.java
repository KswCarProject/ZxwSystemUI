package com.android.systemui.keyguard;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.Service;
import android.app.WindowConfiguration;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.WindowManagerPolicyConstants;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardService;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.systemui.SystemUIApplication;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;

public class KeyguardService extends Service {
    public static final int sEnableRemoteKeyguardAnimation;
    public static boolean sEnableRemoteKeyguardGoingAwayAnimation;
    public static boolean sEnableRemoteKeyguardOccludeAnimation;
    public final IKeyguardService.Stub mBinder = new IKeyguardService.Stub() {
        public void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.addStateMonitorCallback(iKeyguardStateCallback);
        }

        public void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) {
            Trace.beginSection("KeyguardService.mBinder#verifyUnlock");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.verifyUnlock(iKeyguardExitCallback);
            Trace.endSection();
        }

        public void setOccluded(boolean z, boolean z2) {
            Log.d("KeyguardService", "setOccluded(" + z + ")");
            Trace.beginSection("KeyguardService.mBinder#setOccluded");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setOccluded(z, z2);
            Trace.endSection();
        }

        public void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.dismiss(iKeyguardDismissCallback, charSequence);
        }

        public void onDreamingStarted() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onDreamingStarted();
        }

        public void onDreamingStopped() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onDreamingStopped();
        }

        public void onStartedGoingToSleep(int i) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onStartedGoingToSleep(WindowManagerPolicyConstants.translateSleepReasonToOffReason(i));
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(6, i);
        }

        public void onFinishedGoingToSleep(int i, boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onFinishedGoingToSleep(WindowManagerPolicyConstants.translateSleepReasonToOffReason(i), z);
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(7);
        }

        public void onStartedWakingUp(int i, boolean z) {
            Trace.beginSection("KeyguardService.mBinder#onStartedWakingUp");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onStartedWakingUp(z);
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(4, i);
            Trace.endSection();
        }

        public void onFinishedWakingUp() {
            Trace.beginSection("KeyguardService.mBinder#onFinishedWakingUp");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(5);
            Trace.endSection();
        }

        public void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
            Trace.beginSection("KeyguardService.mBinder#onScreenTurningOn");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(0, (Object) iKeyguardDrawnCallback);
            Trace.endSection();
        }

        public void onScreenTurnedOn() {
            Trace.beginSection("KeyguardService.mBinder#onScreenTurnedOn");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(1);
            Trace.endSection();
        }

        public void onScreenTurningOff() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(2);
        }

        public void onScreenTurnedOff() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onScreenTurnedOff();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(3);
        }

        public void setKeyguardEnabled(boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setKeyguardEnabled(z);
        }

        public void onSystemReady() {
            Trace.beginSection("KeyguardService.mBinder#onSystemReady");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onSystemReady();
            Trace.endSection();
        }

        public void doKeyguardTimeout(Bundle bundle) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.doKeyguardTimeout(bundle);
        }

        public void setSwitchingUser(boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setSwitchingUser(z);
        }

        public void setCurrentUser(int i) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setCurrentUser(i);
        }

        public void onBootCompleted() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onBootCompleted();
        }

        @Deprecated
        public void startKeyguardExitAnimation(long j, long j2) {
            Trace.beginSection("KeyguardService.mBinder#startKeyguardExitAnimation");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.startKeyguardExitAnimation(j, j2);
            Trace.endSection();
        }

        public void onShortPowerPressedGoHome() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onShortPowerPressedGoHome();
        }

        public void dismissKeyguardToLaunch(Intent intent) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.dismissKeyguardToLaunch(intent);
        }

        public void onSystemKeyPressed(int i) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onSystemKeyPressed(i);
        }
    };
    public final IRemoteAnimationRunner.Stub mExitAnimationRunner = new IRemoteAnimationRunner.Stub() {
        public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            Trace.beginSection("mExitAnimationRunner.onAnimationStart#startKeyguardExitAnimation");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.startKeyguardExitAnimation(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
            Trace.endSection();
        }

        public void onAnimationCancelled(boolean z) {
            KeyguardService.this.mKeyguardViewMediator.cancelKeyguardExitAnimation();
        }
    };
    public final KeyguardLifecyclesDispatcher mKeyguardLifecyclesDispatcher;
    public final KeyguardViewMediator mKeyguardViewMediator;
    public final IRemoteTransition mOccludeAnimation = new IRemoteTransition.Stub() {
        public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
        }

        public void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException {
            transaction.apply();
            KeyguardService.this.mBinder.setOccluded(true, true);
            iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null, (SurfaceControl.Transaction) null);
        }
    };
    public final ShellTransitions mShellTransitions;
    public final IRemoteTransition mUnoccludeAnimation = new IRemoteTransition.Stub() {
        public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
        }

        public void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException {
            transaction.apply();
            KeyguardService.this.mBinder.setOccluded(false, true);
            iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null, (SurfaceControl.Transaction) null);
        }
    };

    public static int newModeToLegacyMode(int i) {
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            if (i == 3) {
                return 0;
            }
            if (i != 4) {
                return 2;
            }
        }
        return 1;
    }

    static {
        int i = SystemProperties.getInt("persist.wm.enable_remote_keyguard_animation", 2);
        sEnableRemoteKeyguardAnimation = i;
        boolean z = false;
        sEnableRemoteKeyguardGoingAwayAnimation = i >= 1;
        if (i >= 2) {
            z = true;
        }
        sEnableRemoteKeyguardOccludeAnimation = z;
    }

    public static RemoteAnimationTarget[] wrap(TransitionInfo transitionInfo, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        WindowConfiguration windowConfiguration;
        ArrayList arrayList = new ArrayList();
        boolean z5 = false;
        int i = 0;
        while (i < transitionInfo.getChanges().size()) {
            boolean z6 = true;
            if ((((TransitionInfo.Change) transitionInfo.getChanges().get(i)).getFlags() & 2) != 0) {
                z3 = z;
                z2 = true;
            } else {
                z3 = z;
                z2 = z5;
            }
            if (z3 == z2) {
                TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                ActivityManager.RunningTaskInfo taskInfo = change.getTaskInfo();
                int i2 = taskInfo != null ? change.getTaskInfo().taskId : -1;
                WindowConfiguration windowConfiguration2 = null;
                if (taskInfo != null) {
                    if (taskInfo.getConfiguration() != null) {
                        windowConfiguration2 = change.getTaskInfo().getConfiguration().windowConfiguration;
                    }
                    windowConfiguration = windowConfiguration2;
                    z4 = !change.getTaskInfo().isRunning;
                } else {
                    z4 = true;
                    windowConfiguration = null;
                }
                Rect rect = new Rect(change.getEndAbsBounds());
                rect.offsetTo(change.getEndRelOffset().x, change.getEndRelOffset().y);
                int newModeToLegacyMode = newModeToLegacyMode(change.getMode());
                SurfaceControl leash = change.getLeash();
                if ((change.getFlags() & 4) == 0 && (change.getFlags() & 1) == 0) {
                    z6 = z5;
                }
                Rect rect2 = r6;
                Rect rect3 = new Rect(z5 ? 1 : 0, z5, z5, z5);
                int size = transitionInfo.getChanges().size() - i;
                Point point = r6;
                Point point2 = new Point();
                Rect rect4 = r6;
                Rect rect5 = new Rect(change.getEndAbsBounds());
                RemoteAnimationTarget remoteAnimationTarget = r6;
                RemoteAnimationTarget remoteAnimationTarget2 = new RemoteAnimationTarget(i2, newModeToLegacyMode, leash, z6, (Rect) null, rect2, size, point, rect, rect4, windowConfiguration, z4, (SurfaceControl) null, change.getStartAbsBounds(), taskInfo, false);
                arrayList.add(remoteAnimationTarget);
            }
            i++;
            z5 = false;
        }
        return (RemoteAnimationTarget[]) arrayList.toArray(new RemoteAnimationTarget[arrayList.size()]);
    }

    public static int getTransitionOldType(int i, int i2, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (i == 7 || (i2 & 256) != 0) {
            return remoteAnimationTargetArr.length == 0 ? 21 : 20;
        }
        if (i == 8) {
            return 22;
        }
        if (i == 9) {
            return 23;
        }
        Slog.d("KeyguardService", "Unexpected transit type: " + i);
        return 0;
    }

    public static IRemoteTransition wrap(final IRemoteAnimationRunner iRemoteAnimationRunner) {
        return new IRemoteTransition.Stub() {
            public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
            }

            public void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) throws RemoteException {
                Slog.d("KeyguardService", "Starts IRemoteAnimationRunner: info=" + transitionInfo);
                RemoteAnimationTarget[] r2 = KeyguardService.wrap(transitionInfo, false);
                RemoteAnimationTarget[] r3 = KeyguardService.wrap(transitionInfo, true);
                RemoteAnimationTarget[] remoteAnimationTargetArr = new RemoteAnimationTarget[0];
                for (TransitionInfo.Change leash : transitionInfo.getChanges()) {
                    transaction.setAlpha(leash.getLeash(), 1.0f);
                }
                transaction.apply();
                iRemoteAnimationRunner.onAnimationStart(KeyguardService.getTransitionOldType(transitionInfo.getType(), transitionInfo.getFlags(), r2), r2, r3, remoteAnimationTargetArr, new IRemoteAnimationFinishedCallback.Stub() {
                    public void onAnimationFinished() throws RemoteException {
                        Slog.d("KeyguardService", "Finish IRemoteAnimationRunner.");
                        iRemoteTransitionFinishedCallback.onTransitionFinished((WindowContainerTransaction) null, (SurfaceControl.Transaction) null);
                    }
                });
            }
        };
    }

    public KeyguardService(KeyguardViewMediator keyguardViewMediator, KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher, ShellTransitions shellTransitions) {
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mKeyguardLifecyclesDispatcher = keyguardLifecyclesDispatcher;
        this.mShellTransitions = shellTransitions;
    }

    public void onCreate() {
        ((SystemUIApplication) getApplication()).startServicesIfNeeded();
        if (this.mShellTransitions == null || !Transitions.ENABLE_SHELL_TRANSITIONS) {
            RemoteAnimationDefinition remoteAnimationDefinition = new RemoteAnimationDefinition();
            if (sEnableRemoteKeyguardGoingAwayAnimation) {
                RemoteAnimationAdapter remoteAnimationAdapter = new RemoteAnimationAdapter(this.mExitAnimationRunner, 0, 0);
                remoteAnimationDefinition.addRemoteAnimation(20, remoteAnimationAdapter);
                remoteAnimationDefinition.addRemoteAnimation(21, remoteAnimationAdapter);
            }
            if (sEnableRemoteKeyguardOccludeAnimation) {
                remoteAnimationDefinition.addRemoteAnimation(22, new RemoteAnimationAdapter(this.mKeyguardViewMediator.getOccludeAnimationRunner(), 0, 0));
                remoteAnimationDefinition.addRemoteAnimation(23, new RemoteAnimationAdapter(this.mKeyguardViewMediator.getUnoccludeAnimationRunner(), 0, 0));
            }
            ActivityTaskManager.getInstance().registerRemoteAnimationsForDisplay(0, remoteAnimationDefinition);
            return;
        }
        if (sEnableRemoteKeyguardGoingAwayAnimation) {
            Slog.d("KeyguardService", "KeyguardService registerRemote: TRANSIT_KEYGUARD_GOING_AWAY");
            TransitionFilter transitionFilter = new TransitionFilter();
            transitionFilter.mFlags = 256;
            this.mShellTransitions.registerRemote(transitionFilter, new RemoteTransition(wrap(this.mExitAnimationRunner), getIApplicationThread()));
        }
        if (sEnableRemoteKeyguardOccludeAnimation) {
            Slog.d("KeyguardService", "KeyguardService registerRemote: TRANSIT_KEYGUARD_(UN)OCCLUDE");
            TransitionFilter transitionFilter2 = new TransitionFilter();
            transitionFilter2.mFlags = 64;
            TransitionFilter.Requirement requirement = new TransitionFilter.Requirement();
            TransitionFilter.Requirement[] requirementArr = {new TransitionFilter.Requirement(), requirement};
            transitionFilter2.mRequirements = requirementArr;
            TransitionFilter.Requirement requirement2 = requirementArr[0];
            requirement2.mMustBeIndependent = false;
            requirement2.mFlags = 64;
            requirement2.mModes = new int[]{1, 3};
            requirement.mNot = true;
            requirement.mMustBeIndependent = false;
            requirement.mFlags = 64;
            requirement.mModes = new int[]{2, 4};
            this.mShellTransitions.registerRemote(transitionFilter2, new RemoteTransition(this.mOccludeAnimation, getIApplicationThread()));
            TransitionFilter transitionFilter3 = new TransitionFilter();
            transitionFilter3.mFlags = 64;
            TransitionFilter.Requirement requirement3 = new TransitionFilter.Requirement();
            TransitionFilter.Requirement[] requirementArr2 = {new TransitionFilter.Requirement(), requirement3};
            transitionFilter3.mRequirements = requirementArr2;
            requirement3.mMustBeIndependent = false;
            requirement3.mModes = new int[]{2, 4};
            requirement3.mMustBeTask = true;
            TransitionFilter.Requirement requirement4 = requirementArr2[0];
            requirement4.mNot = true;
            requirement4.mMustBeIndependent = false;
            requirement4.mFlags = 64;
            requirement4.mModes = new int[]{1, 3};
            this.mShellTransitions.registerRemote(transitionFilter3, new RemoteTransition(this.mUnoccludeAnimation, getIApplicationThread()));
        }
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void checkPermission() {
        if (Binder.getCallingUid() != 1000 && getBaseContext().checkCallingOrSelfPermission("android.permission.CONTROL_KEYGUARD") != 0) {
            Log.w("KeyguardService", "Caller needs permission 'android.permission.CONTROL_KEYGUARD' to call " + Debug.getCaller());
            throw new SecurityException("Access denied to process: " + Binder.getCallingPid() + ", must have permission " + "android.permission.CONTROL_KEYGUARD");
        }
    }
}
