package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.graphics.Matrix;
import android.hardware.biometrics.BiometricSourceType;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardConstants;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.systemui.CoreStartable;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.DeviceConfigProxy;
import com.szchoiceway.eventcenter.EventUtils;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;

public class KeyguardViewMediator extends CoreStartable implements StatusBarStateController.StateListener {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public static final Intent USER_PRESENT_INTENT = new Intent("android.intent.action.USER_PRESENT").addFlags(606076928);
    public static SparseIntArray mUnlockTrackSimStates = new SparseIntArray();
    public Lazy<ActivityLaunchAnimator> mActivityLaunchAnimator;
    public AlarmManager mAlarmManager;
    public boolean mAnimatingScreenOff;
    public boolean mAodShowing;
    public AudioManager mAudioManager;
    public boolean mBootCompleted;
    public boolean mBootSendUserPresent;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final BroadcastReceiver mBroadcastReceiver;
    public CentralSurfaces mCentralSurfaces;
    public CharSequence mCustomMessage;
    public final BroadcastReceiver mDelayedLockBroadcastReceiver;
    public int mDelayedProfileShowingSequence;
    public int mDelayedShowingSequence;
    public DeviceConfigProxy mDeviceConfig;
    public boolean mDeviceInteractive;
    public final DismissCallbackRegistry mDismissCallbackRegistry;
    public DozeParameters mDozeParameters;
    public boolean mDozing;
    public boolean mDreamOverlayShowing;
    public final DreamOverlayStateController.Callback mDreamOverlayStateCallback;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public IKeyguardExitCallback mExitSecureCallback;
    public boolean mExternallyEnabled = true;
    public final FalsingCollector mFalsingCollector;
    public boolean mGoingToSleep;
    public Handler mHandler;
    public Animation mHideAnimation;
    public final Runnable mHideAnimationFinishedRunnable;
    public boolean mHideAnimationRun = false;
    public boolean mHideAnimationRunning = false;
    public boolean mHiding;
    public boolean mInGestureNavigationMode;
    public boolean mInputRestricted;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public final KeyguardDisplayManager mKeyguardDisplayManager;
    public boolean mKeyguardDonePending = false;
    public IRemoteAnimationRunner mKeyguardExitAnimationRunner;
    public final Runnable mKeyguardGoingAwayRunnable;
    public final ArrayList<IKeyguardStateCallback> mKeyguardStateCallbacks = new ArrayList<>();
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardStateController.Callback mKeyguardStateControllerCallback;
    public final Lazy<KeyguardUnlockAnimationController> mKeyguardUnlockAnimationControllerLazy;
    public final Lazy<KeyguardViewController> mKeyguardViewControllerLazy;
    public final SparseIntArray mLastSimStates = new SparseIntArray();
    public boolean mLockLater;
    public final LockPatternUtils mLockPatternUtils;
    public int mLockSoundId;
    public int mLockSoundStreamId;
    public float mLockSoundVolume;
    public SoundPool mLockSounds;
    public boolean mNeedToReshowWhenReenabled = false;
    public final Lazy<NotificationShadeDepthController> mNotificationShadeDepthController;
    public final Lazy<NotificationShadeWindowController> mNotificationShadeWindowControllerLazy;
    public final ActivityLaunchAnimator.Controller mOccludeAnimationController;
    public IRemoteAnimationRunner mOccludeAnimationRunner;
    public boolean mOccluded = false;
    public final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;
    public final PowerManager mPM;
    public boolean mPendingLock;
    public boolean mPendingReset;
    public String mPhoneState = TelephonyManager.EXTRA_STATE_IDLE;
    public final float mPowerButtonY;
    public boolean mPowerGestureIntercepted = false;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public ScreenOnCoordinator mScreenOnCoordinator;
    public boolean mShowHomeOverLockscreen;
    public PowerManager.WakeLock mShowKeyguardWakeLock;
    public boolean mShowing;
    public boolean mShuttingDown;
    public final SparseBooleanArray mSimWasLocked = new SparseBooleanArray();
    public StatusBarManager mStatusBarManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public IRemoteAnimationFinishedCallback mSurfaceBehindRemoteAnimationFinishedCallback;
    public boolean mSurfaceBehindRemoteAnimationRequested = false;
    public boolean mSurfaceBehindRemoteAnimationRunning;
    public boolean mSystemReady;
    public final TrustManager mTrustManager;
    public int mTrustedSoundId;
    public final Executor mUiBgExecutor;
    public int mUiSoundsStreamType;
    public int mUnlockSoundId;
    public final IRemoteAnimationRunner mUnoccludeAnimationRunner;
    public KeyguardUpdateMonitorCallback mUpdateCallback;
    public final KeyguardUpdateMonitor mUpdateMonitor;
    public final UserSwitcherController mUserSwitcherController;
    public ViewMediatorCallback mViewMediatorCallback;
    public boolean mWaitingUntilKeyguardVisible = false;
    public boolean mWakeAndUnlocking = false;
    public boolean mWallpaperSupportsAmbientMode;
    public final float mWindowCornerRadius;
    public WorkLockActivityController mWorkLockController;

    public void dismissKeyguardToLaunch(Intent intent) {
    }

    public void onShortPowerPressedGoHome() {
    }

    public void onSystemKeyPressed(int i) {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardViewMediator(Context context, FalsingCollector falsingCollector, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, Lazy<KeyguardViewController> lazy, DismissCallbackRegistry dismissCallbackRegistry, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, Executor executor, PowerManager powerManager, TrustManager trustManager, UserSwitcherController userSwitcherController, DeviceConfigProxy deviceConfigProxy, NavigationModeController navigationModeController, KeyguardDisplayManager keyguardDisplayManager, DozeParameters dozeParameters, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, Lazy<KeyguardUnlockAnimationController> lazy2, ScreenOffAnimationController screenOffAnimationController, Lazy<NotificationShadeDepthController> lazy3, ScreenOnCoordinator screenOnCoordinator, InteractionJankMonitor interactionJankMonitor, DreamOverlayStateController dreamOverlayStateController, Lazy<NotificationShadeWindowController> lazy4, Lazy<ActivityLaunchAnimator> lazy5) {
        super(context);
        DeviceConfigProxy deviceConfigProxy2 = deviceConfigProxy;
        SysuiStatusBarStateController sysuiStatusBarStateController2 = sysuiStatusBarStateController;
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        AnonymousClass1 r5 = new DeviceConfig.OnPropertiesChangedListener() {
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("nav_bar_handle_show_over_lockscreen")) {
                    KeyguardViewMediator.this.mShowHomeOverLockscreen = properties.getBoolean("nav_bar_handle_show_over_lockscreen", true);
                }
            }
        };
        this.mOnPropertiesChangedListener = r5;
        this.mDreamOverlayStateCallback = new DreamOverlayStateController.Callback() {
            public void onStateChanged() {
                KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                keyguardViewMediator.mDreamOverlayShowing = keyguardViewMediator.mDreamOverlayStateController.isOverlayActive();
            }
        };
        this.mUpdateCallback = new KeyguardUpdateMonitorCallback() {
            public void onUserInfoChanged(int i) {
            }

            public void onUserSwitching(int i) {
                if (KeyguardViewMediator.DEBUG) {
                    Log.d("KeyguardViewMediator", String.format("onUserSwitching %d", new Object[]{Integer.valueOf(i)}));
                }
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator.this.resetKeyguardDonePendingLocked();
                    if (KeyguardViewMediator.this.mLockPatternUtils.isLockScreenDisabled(i)) {
                        KeyguardViewMediator.this.dismiss((IKeyguardDismissCallback) null, (CharSequence) null);
                    } else {
                        KeyguardViewMediator.this.resetStateLocked();
                    }
                    KeyguardViewMediator.this.adjustStatusBarLocked();
                }
            }

            public void onUserSwitchComplete(int i) {
                UserInfo userInfo;
                if (KeyguardViewMediator.DEBUG) {
                    Log.d("KeyguardViewMediator", String.format("onUserSwitchComplete %d", new Object[]{Integer.valueOf(i)}));
                }
                if (i != 0 && (userInfo = UserManager.get(KeyguardViewMediator.this.mContext).getUserInfo(i)) != null && !KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                    if (userInfo.isGuest() || userInfo.isDemo()) {
                        KeyguardViewMediator.this.dismiss((IKeyguardDismissCallback) null, (CharSequence) null);
                    }
                }
            }

            public void onClockVisibilityChanged() {
                KeyguardViewMediator.this.adjustStatusBarLocked();
            }

            public void onDeviceProvisioned() {
                KeyguardViewMediator.this.sendUserPresentBroadcast();
                synchronized (KeyguardViewMediator.this) {
                    if (KeyguardViewMediator.this.mustNotUnlockCurrentUser()) {
                        KeyguardViewMediator.this.doKeyguardLocked((Bundle) null);
                    }
                }
            }

            /* JADX WARNING: Code restructure failed: missing block: B:114:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:115:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:116:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:117:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:118:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:0x00ab, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x00d0, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:39:0x00d3, code lost:
                if (r11 == 1) goto L_0x019b;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:40:0x00d5, code lost:
                if (r11 == 2) goto L_0x0170;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:41:0x00d7, code lost:
                if (r11 == 3) goto L_0x0170;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:42:0x00d9, code lost:
                if (r11 == 5) goto L_0x0123;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:44:0x00dc, code lost:
                if (r11 == 6) goto L_0x019b;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:46:0x00df, code lost:
                if (r11 == 7) goto L_0x00f9;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:47:0x00e1, code lost:
                android.util.Log.v("KeyguardViewMediator", "Unspecific state: " + r11);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:48:0x00f9, code lost:
                r2 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:49:0x00fb, code lost:
                monitor-enter(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:52:0x0102, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.m1382$$Nest$fgetmShowing(r8.this$0) != false) goto L_0x0111;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:53:0x0104, code lost:
                android.util.Log.d("KeyguardViewMediator", "PERM_DISABLED and keygaurd isn't showing.");
                com.android.systemui.keyguard.KeyguardViewMediator.m1398$$Nest$mdoKeyguardLocked(r8.this$0, (android.os.Bundle) null);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:54:0x0111, code lost:
                android.util.Log.d("KeyguardViewMediator", "PERM_DISABLED, resetStateLocked toshow permanently disabled message in lockscreen.");
                com.android.systemui.keyguard.KeyguardViewMediator.m1418$$Nest$mresetStateLocked(r8.this$0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:55:0x011d, code lost:
                monitor-exit(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:60:0x0123, code lost:
                r11 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:61:0x0125, code lost:
                monitor-enter(r11);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
                android.util.Log.d("KeyguardViewMediator", "READY, reset state? " + com.android.systemui.keyguard.KeyguardViewMediator.m1382$$Nest$fgetmShowing(r8.this$0));
             */
            /* JADX WARNING: Code restructure failed: missing block: B:65:0x0148, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.m1382$$Nest$fgetmShowing(r8.this$0) == false) goto L_0x016b;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:67:0x0154, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.m1383$$Nest$fgetmSimWasLocked(r8.this$0).get(r10, false) == false) goto L_0x016b;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:68:0x0156, code lost:
                android.util.Log.d("KeyguardViewMediator", "SIM moved to READY when the previously was locked. Reset the state.");
                com.android.systemui.keyguard.KeyguardViewMediator.m1383$$Nest$fgetmSimWasLocked(r8.this$0).append(r10, false);
                com.android.systemui.keyguard.KeyguardViewMediator.m1418$$Nest$mresetStateLocked(r8.this$0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:69:0x016b, code lost:
                monitor-exit(r11);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:74:0x0170, code lost:
                r2 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:75:0x0172, code lost:
                monitor-enter(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
                com.android.systemui.keyguard.KeyguardViewMediator.m1383$$Nest$fgetmSimWasLocked(r8.this$0).append(r10, true);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:78:0x0182, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.m1382$$Nest$fgetmShowing(r8.this$0) != false) goto L_0x0191;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:79:0x0184, code lost:
                android.util.Log.d("KeyguardViewMediator", "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing; need to show keyguard so user can enter sim pin");
                com.android.systemui.keyguard.KeyguardViewMediator.m1398$$Nest$mdoKeyguardLocked(r8.this$0, (android.os.Bundle) null);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:80:0x0191, code lost:
                com.android.systemui.keyguard.KeyguardViewMediator.m1418$$Nest$mresetStateLocked(r8.this$0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:81:0x0196, code lost:
                monitor-exit(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:86:0x019b, code lost:
                r2 = r8.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:87:0x019d, code lost:
                monitor-enter(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:90:0x01a4, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.m1420$$Nest$mshouldWaitForProvisioning(r8.this$0) == false) goto L_0x01c0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:92:0x01ac, code lost:
                if (com.android.systemui.keyguard.KeyguardViewMediator.m1382$$Nest$fgetmShowing(r8.this$0) != false) goto L_0x01bb;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:93:0x01ae, code lost:
                android.util.Log.d("KeyguardViewMediator", "ICC_ABSENT isn't showing, we need to show the keyguard since the device isn't provisioned yet.");
                com.android.systemui.keyguard.KeyguardViewMediator.m1398$$Nest$mdoKeyguardLocked(r8.this$0, (android.os.Bundle) null);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:94:0x01bb, code lost:
                com.android.systemui.keyguard.KeyguardViewMediator.m1418$$Nest$mresetStateLocked(r8.this$0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:95:0x01c0, code lost:
                if (r11 != 1) goto L_0x01d9;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:96:0x01c2, code lost:
                if (r0 == false) goto L_0x01d0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:97:0x01c4, code lost:
                android.util.Log.d("KeyguardViewMediator", "SIM moved to ABSENT when the previous state was locked. Reset the state.");
                com.android.systemui.keyguard.KeyguardViewMediator.m1418$$Nest$mresetStateLocked(r8.this$0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:98:0x01d0, code lost:
                com.android.systemui.keyguard.KeyguardViewMediator.m1383$$Nest$fgetmSimWasLocked(r8.this$0).append(r10, false);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:99:0x01d9, code lost:
                monitor-exit(r2);
             */
            /* JADX WARNING: Removed duplicated region for block: B:20:0x0098  */
            /* JADX WARNING: Removed duplicated region for block: B:28:0x00b4  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onSimStateChanged(int r9, int r10, int r11) {
                /*
                    r8 = this;
                    java.lang.String r0 = "KeyguardViewMediator"
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r2 = "onSimStateChanged(subId="
                    r1.append(r2)
                    r1.append(r9)
                    java.lang.String r9 = ", slotId="
                    r1.append(r9)
                    r1.append(r10)
                    java.lang.String r9 = ",state="
                    r1.append(r9)
                    r1.append(r11)
                    java.lang.String r9 = ")"
                    r1.append(r9)
                    java.lang.String r9 = r1.toString()
                    android.util.Log.d(r0, r9)
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    java.util.ArrayList r9 = r9.mKeyguardStateCallbacks
                    int r9 = r9.size()
                    com.android.systemui.keyguard.KeyguardViewMediator r0 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    com.android.keyguard.KeyguardUpdateMonitor r0 = r0.mUpdateMonitor
                    boolean r0 = r0.isSimPinSecure()
                    r1 = 1
                    int r9 = r9 - r1
                L_0x0041:
                    if (r9 < 0) goto L_0x006b
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ RemoteException -> 0x0053 }
                    java.util.ArrayList r2 = r2.mKeyguardStateCallbacks     // Catch:{ RemoteException -> 0x0053 }
                    java.lang.Object r2 = r2.get(r9)     // Catch:{ RemoteException -> 0x0053 }
                    com.android.internal.policy.IKeyguardStateCallback r2 = (com.android.internal.policy.IKeyguardStateCallback) r2     // Catch:{ RemoteException -> 0x0053 }
                    r2.onSimSecureStateChanged(r0)     // Catch:{ RemoteException -> 0x0053 }
                    goto L_0x0068
                L_0x0053:
                    r2 = move-exception
                    java.lang.String r3 = "KeyguardViewMediator"
                    java.lang.String r4 = "Failed to call onSimSecureStateChanged"
                    android.util.Slog.w(r3, r4, r2)
                    boolean r2 = r2 instanceof android.os.DeadObjectException
                    if (r2 == 0) goto L_0x0068
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    java.util.ArrayList r2 = r2.mKeyguardStateCallbacks
                    r2.remove(r9)
                L_0x0068:
                    int r9 = r9 + -1
                    goto L_0x0041
                L_0x006b:
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r9)
                    com.android.systemui.keyguard.KeyguardViewMediator r0 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01de }
                    android.util.SparseIntArray r0 = r0.mLastSimStates     // Catch:{ all -> 0x01de }
                    int r0 = r0.get(r10)     // Catch:{ all -> 0x01de }
                    r2 = 3
                    r3 = 0
                    r4 = 2
                    if (r0 == r4) goto L_0x0082
                    if (r0 != r2) goto L_0x0080
                    goto L_0x0082
                L_0x0080:
                    r0 = r3
                    goto L_0x0083
                L_0x0082:
                    r0 = r1
                L_0x0083:
                    com.android.systemui.keyguard.KeyguardViewMediator r5 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01de }
                    android.util.SparseIntArray r5 = r5.mLastSimStates     // Catch:{ all -> 0x01de }
                    r5.append(r10, r11)     // Catch:{ all -> 0x01de }
                    android.util.SparseIntArray r5 = com.android.systemui.keyguard.KeyguardViewMediator.mUnlockTrackSimStates     // Catch:{ all -> 0x01de }
                    r6 = -1
                    int r5 = r5.get(r10, r6)     // Catch:{ all -> 0x01de }
                    r6 = 5
                    if (r11 != r6) goto L_0x00b4
                    r7 = 10
                    if (r5 != r7) goto L_0x00ac
                    boolean r8 = com.android.systemui.keyguard.KeyguardViewMediator.DEBUG     // Catch:{ all -> 0x01de }
                    if (r8 == 0) goto L_0x00aa
                    java.lang.String r8 = "KeyguardViewMediator"
                    java.lang.String r10 = "skip the redundant SIM_STATE_READY state"
                    android.util.Log.e(r8, r10)     // Catch:{ all -> 0x01de }
                L_0x00aa:
                    monitor-exit(r9)     // Catch:{ all -> 0x01de }
                    return
                L_0x00ac:
                    android.util.SparseIntArray r7 = com.android.systemui.keyguard.KeyguardViewMediator.mUnlockTrackSimStates     // Catch:{ all -> 0x01de }
                    r7.put(r10, r11)     // Catch:{ all -> 0x01de }
                    goto L_0x00bd
                L_0x00b4:
                    if (r11 == r4) goto L_0x00bd
                    android.util.SparseIntArray r7 = com.android.systemui.keyguard.KeyguardViewMediator.mUnlockTrackSimStates     // Catch:{ all -> 0x01de }
                    r7.put(r10, r11)     // Catch:{ all -> 0x01de }
                L_0x00bd:
                    if (r5 != r6) goto L_0x00d1
                    if (r11 != r4) goto L_0x00d1
                    boolean r8 = com.android.systemui.keyguard.KeyguardViewMediator.DEBUG     // Catch:{ all -> 0x01de }
                    if (r8 == 0) goto L_0x00cf
                    java.lang.String r8 = "KeyguardViewMediator"
                    java.lang.String r10 = "skip the unnecessary SIM_STATE_PIN_REQUIRED state"
                    android.util.Log.e(r8, r10)     // Catch:{ all -> 0x01de }
                L_0x00cf:
                    monitor-exit(r9)     // Catch:{ all -> 0x01de }
                    return
                L_0x00d1:
                    monitor-exit(r9)     // Catch:{ all -> 0x01de }
                    r9 = 0
                    if (r11 == r1) goto L_0x019b
                    if (r11 == r4) goto L_0x0170
                    if (r11 == r2) goto L_0x0170
                    if (r11 == r6) goto L_0x0123
                    r2 = 6
                    if (r11 == r2) goto L_0x019b
                    r10 = 7
                    if (r11 == r10) goto L_0x00f9
                    java.lang.String r8 = "KeyguardViewMediator"
                    java.lang.StringBuilder r9 = new java.lang.StringBuilder
                    r9.<init>()
                    java.lang.String r10 = "Unspecific state: "
                    r9.append(r10)
                    r9.append(r11)
                    java.lang.String r9 = r9.toString()
                    android.util.Log.v(r8, r9)
                    goto L_0x01da
                L_0x00f9:
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r2)
                    com.android.systemui.keyguard.KeyguardViewMediator r10 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0120 }
                    boolean r10 = r10.mShowing     // Catch:{ all -> 0x0120 }
                    if (r10 != 0) goto L_0x0111
                    java.lang.String r10 = "KeyguardViewMediator"
                    java.lang.String r11 = "PERM_DISABLED and keygaurd isn't showing."
                    android.util.Log.d(r10, r11)     // Catch:{ all -> 0x0120 }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0120 }
                    r8.doKeyguardLocked(r9)     // Catch:{ all -> 0x0120 }
                    goto L_0x011d
                L_0x0111:
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.String r10 = "PERM_DISABLED, resetStateLocked toshow permanently disabled message in lockscreen."
                    android.util.Log.d(r9, r10)     // Catch:{ all -> 0x0120 }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0120 }
                    r8.resetStateLocked()     // Catch:{ all -> 0x0120 }
                L_0x011d:
                    monitor-exit(r2)     // Catch:{ all -> 0x0120 }
                    goto L_0x01da
                L_0x0120:
                    r8 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x0120 }
                    throw r8
                L_0x0123:
                    com.android.systemui.keyguard.KeyguardViewMediator r11 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r11)
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x016d }
                    r0.<init>()     // Catch:{ all -> 0x016d }
                    java.lang.String r1 = "READY, reset state? "
                    r0.append(r1)     // Catch:{ all -> 0x016d }
                    com.android.systemui.keyguard.KeyguardViewMediator r1 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x016d }
                    boolean r1 = r1.mShowing     // Catch:{ all -> 0x016d }
                    r0.append(r1)     // Catch:{ all -> 0x016d }
                    java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x016d }
                    android.util.Log.d(r9, r0)     // Catch:{ all -> 0x016d }
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x016d }
                    boolean r9 = r9.mShowing     // Catch:{ all -> 0x016d }
                    if (r9 == 0) goto L_0x016b
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x016d }
                    android.util.SparseBooleanArray r9 = r9.mSimWasLocked     // Catch:{ all -> 0x016d }
                    boolean r9 = r9.get(r10, r3)     // Catch:{ all -> 0x016d }
                    if (r9 == 0) goto L_0x016b
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.String r0 = "SIM moved to READY when the previously was locked. Reset the state."
                    android.util.Log.d(r9, r0)     // Catch:{ all -> 0x016d }
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x016d }
                    android.util.SparseBooleanArray r9 = r9.mSimWasLocked     // Catch:{ all -> 0x016d }
                    r9.append(r10, r3)     // Catch:{ all -> 0x016d }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x016d }
                    r8.resetStateLocked()     // Catch:{ all -> 0x016d }
                L_0x016b:
                    monitor-exit(r11)     // Catch:{ all -> 0x016d }
                    goto L_0x01da
                L_0x016d:
                    r8 = move-exception
                    monitor-exit(r11)     // Catch:{ all -> 0x016d }
                    throw r8
                L_0x0170:
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r2)
                    com.android.systemui.keyguard.KeyguardViewMediator r11 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0198 }
                    android.util.SparseBooleanArray r11 = r11.mSimWasLocked     // Catch:{ all -> 0x0198 }
                    r11.append(r10, r1)     // Catch:{ all -> 0x0198 }
                    com.android.systemui.keyguard.KeyguardViewMediator r10 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0198 }
                    boolean r10 = r10.mShowing     // Catch:{ all -> 0x0198 }
                    if (r10 != 0) goto L_0x0191
                    java.lang.String r10 = "KeyguardViewMediator"
                    java.lang.String r11 = "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing; need to show keyguard so user can enter sim pin"
                    android.util.Log.d(r10, r11)     // Catch:{ all -> 0x0198 }
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0198 }
                    r8.doKeyguardLocked(r9)     // Catch:{ all -> 0x0198 }
                    goto L_0x0196
                L_0x0191:
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x0198 }
                    r8.resetStateLocked()     // Catch:{ all -> 0x0198 }
                L_0x0196:
                    monitor-exit(r2)     // Catch:{ all -> 0x0198 }
                    goto L_0x01da
                L_0x0198:
                    r8 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x0198 }
                    throw r8
                L_0x019b:
                    com.android.systemui.keyguard.KeyguardViewMediator r2 = com.android.systemui.keyguard.KeyguardViewMediator.this
                    monitor-enter(r2)
                    com.android.systemui.keyguard.KeyguardViewMediator r4 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01db }
                    boolean r4 = r4.shouldWaitForProvisioning()     // Catch:{ all -> 0x01db }
                    if (r4 == 0) goto L_0x01c0
                    com.android.systemui.keyguard.KeyguardViewMediator r4 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01db }
                    boolean r4 = r4.mShowing     // Catch:{ all -> 0x01db }
                    if (r4 != 0) goto L_0x01bb
                    java.lang.String r4 = "KeyguardViewMediator"
                    java.lang.String r5 = "ICC_ABSENT isn't showing, we need to show the keyguard since the device isn't provisioned yet."
                    android.util.Log.d(r4, r5)     // Catch:{ all -> 0x01db }
                    com.android.systemui.keyguard.KeyguardViewMediator r4 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01db }
                    r4.doKeyguardLocked(r9)     // Catch:{ all -> 0x01db }
                    goto L_0x01c0
                L_0x01bb:
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01db }
                    r9.resetStateLocked()     // Catch:{ all -> 0x01db }
                L_0x01c0:
                    if (r11 != r1) goto L_0x01d9
                    if (r0 == 0) goto L_0x01d0
                    java.lang.String r9 = "KeyguardViewMediator"
                    java.lang.String r11 = "SIM moved to ABSENT when the previous state was locked. Reset the state."
                    android.util.Log.d(r9, r11)     // Catch:{ all -> 0x01db }
                    com.android.systemui.keyguard.KeyguardViewMediator r9 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01db }
                    r9.resetStateLocked()     // Catch:{ all -> 0x01db }
                L_0x01d0:
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = com.android.systemui.keyguard.KeyguardViewMediator.this     // Catch:{ all -> 0x01db }
                    android.util.SparseBooleanArray r8 = r8.mSimWasLocked     // Catch:{ all -> 0x01db }
                    r8.append(r10, r3)     // Catch:{ all -> 0x01db }
                L_0x01d9:
                    monitor-exit(r2)     // Catch:{ all -> 0x01db }
                L_0x01da:
                    return
                L_0x01db:
                    r8 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x01db }
                    throw r8
                L_0x01de:
                    r8 = move-exception
                    monitor-exit(r9)     // Catch:{ all -> 0x01de }
                    throw r8
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.AnonymousClass3.onSimStateChanged(int, int, int):void");
            }

            public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(currentUser)) {
                    KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportFailedBiometricAttempt(currentUser);
                }
            }

            public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
                if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                    KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportSuccessfulBiometricAttempt(i);
                }
            }

            public void onTrustChanged(int i) {
                if (i == KeyguardUpdateMonitor.getCurrentUser()) {
                    synchronized (KeyguardViewMediator.this) {
                        KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                        keyguardViewMediator.notifyTrustedChangedLocked(keyguardViewMediator.mUpdateMonitor.getUserHasTrust(i));
                    }
                }
            }
        };
        this.mViewMediatorCallback = new ViewMediatorCallback() {
            public void userActivity() {
                KeyguardViewMediator.this.userActivity();
            }

            public void keyguardDone(boolean z, int i) {
                if (i == ActivityManager.getCurrentUser()) {
                    if (KeyguardViewMediator.DEBUG) {
                        Log.d("KeyguardViewMediator", "keyguardDone");
                    }
                    KeyguardViewMediator.this.tryKeyguardDone();
                }
            }

            public void keyguardDoneDrawing() {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDoneDrawing");
                KeyguardViewMediator.this.mHandler.sendEmptyMessage(8);
                Trace.endSection();
            }

            public void setNeedsInput(boolean z) {
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setNeedsInput(z);
            }

            public void keyguardDonePending(boolean z, int i) {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDonePending");
                if (KeyguardViewMediator.DEBUG) {
                    Log.d("KeyguardViewMediator", "keyguardDonePending");
                }
                if (i != ActivityManager.getCurrentUser()) {
                    Trace.endSection();
                    return;
                }
                KeyguardViewMediator.this.mKeyguardDonePending = true;
                KeyguardViewMediator.this.mHideAnimationRun = true;
                KeyguardViewMediator.this.mHideAnimationRunning = true;
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).startPreHideAnimation(KeyguardViewMediator.this.mHideAnimationFinishedRunnable);
                KeyguardViewMediator.this.mHandler.sendEmptyMessageDelayed(13, 3000);
                Trace.endSection();
            }

            public void keyguardGone() {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardGone");
                if (KeyguardViewMediator.DEBUG) {
                    Log.d("KeyguardViewMediator", "keyguardGone");
                }
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(false);
                KeyguardViewMediator.this.mKeyguardDisplayManager.hide();
                Trace.endSection();
            }

            public void readyForKeyguardDone() {
                Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#readyForKeyguardDone");
                if (KeyguardViewMediator.this.mKeyguardDonePending) {
                    KeyguardViewMediator.this.mKeyguardDonePending = false;
                    KeyguardViewMediator.this.tryKeyguardDone();
                }
                Trace.endSection();
            }

            public void resetKeyguard() {
                KeyguardViewMediator.this.resetStateLocked();
            }

            public void onCancelClicked() {
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).onCancelClicked();
            }

            public void playTrustedSound() {
                KeyguardViewMediator.this.playTrustedSound();
            }

            public boolean isScreenOn() {
                return KeyguardViewMediator.this.mDeviceInteractive;
            }

            public int getBouncerPromptReason() {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                boolean isTrustUsuallyManaged = KeyguardViewMediator.this.mUpdateMonitor.isTrustUsuallyManaged(currentUser);
                boolean z = isTrustUsuallyManaged || KeyguardViewMediator.this.mUpdateMonitor.isUnlockingWithBiometricsPossible(currentUser);
                KeyguardUpdateMonitor.StrongAuthTracker strongAuthTracker = KeyguardViewMediator.this.mUpdateMonitor.getStrongAuthTracker();
                int strongAuthForUser = strongAuthTracker.getStrongAuthForUser(currentUser);
                if (z && !strongAuthTracker.hasUserAuthenticatedSinceBoot()) {
                    return 1;
                }
                if (z && (strongAuthForUser & 16) != 0) {
                    return 2;
                }
                if ((strongAuthForUser & 2) != 0) {
                    return 3;
                }
                if (isTrustUsuallyManaged && (strongAuthForUser & 4) != 0) {
                    return 4;
                }
                if (z && ((strongAuthForUser & 8) != 0 || KeyguardViewMediator.this.mUpdateMonitor.isFingerprintLockedOut())) {
                    return 5;
                }
                if (z && (strongAuthForUser & 64) != 0) {
                    return 6;
                }
                if (!z || (strongAuthForUser & 128) == 0) {
                    return 0;
                }
                return 7;
            }

            public CharSequence consumeCustomMessage() {
                CharSequence r0 = KeyguardViewMediator.this.mCustomMessage;
                KeyguardViewMediator.this.mCustomMessage = null;
                return r0;
            }
        };
        AnonymousClass5 r6 = new ActivityLaunchAnimator.Controller() {
            public void onLaunchAnimationStart(boolean z) {
            }

            public void onLaunchAnimationCancelled() {
                Log.d("KeyguardViewMediator", "Occlude launch animation cancelled. Occluded state is now: " + KeyguardViewMediator.this.mOccluded);
            }

            public void onLaunchAnimationEnd(boolean z) {
                if (z) {
                    KeyguardViewMediator.this.mCentralSurfaces.instantCollapseNotificationPanel();
                }
            }

            public ViewGroup getLaunchContainer() {
                return (ViewGroup) ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).getViewRootImpl().getView();
            }

            public void setLaunchContainer(ViewGroup viewGroup) {
                Log.wtf("KeyguardViewMediator", "Someone tried to change the launch container for the ActivityLaunchAnimator, which should never happen.");
            }

            public LaunchAnimator.State createAnimatorState() {
                int width = getLaunchContainer().getWidth();
                int height = getLaunchContainer().getHeight();
                float f = ((float) height) / 3.0f;
                float f2 = (float) width;
                float f3 = f2 / 3.0f;
                if (KeyguardViewMediator.this.mUpdateMonitor.isSecureCameraLaunchedOverKeyguard()) {
                    float f4 = f / 2.0f;
                    return new LaunchAnimator.State((int) (KeyguardViewMediator.this.mPowerButtonY - f4), (int) (KeyguardViewMediator.this.mPowerButtonY + f4), (int) (f2 - f3), width, KeyguardViewMediator.this.mWindowCornerRadius, KeyguardViewMediator.this.mWindowCornerRadius);
                }
                int i = height / 2;
                int i2 = width / 2;
                return new LaunchAnimator.State(i, i, i2, i2, KeyguardViewMediator.this.mWindowCornerRadius, KeyguardViewMediator.this.mWindowCornerRadius);
            }
        };
        this.mOccludeAnimationController = r6;
        this.mOccludeAnimationRunner = new OccludeActivityLaunchRemoteAnimationRunner(r6);
        this.mUnoccludeAnimationRunner = new IRemoteAnimationRunner.Stub() {
            public ValueAnimator mUnoccludeAnimator;
            public final Matrix mUnoccludeMatrix = new Matrix();

            public void onAnimationCancelled(boolean z) {
                ValueAnimator valueAnimator = this.mUnoccludeAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                KeyguardViewMediator.this.setOccluded(z, false);
                Log.d("KeyguardViewMediator", "Unocclude animation cancelled. Occluded state is now: " + KeyguardViewMediator.this.mOccluded);
            }

            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
                RemoteAnimationTarget remoteAnimationTarget;
                Log.d("KeyguardViewMediator", "UnoccludeAnimator#onAnimationStart. Set occluded = false.");
                KeyguardViewMediator.this.setOccluded(false, true);
                if (remoteAnimationTargetArr == null || remoteAnimationTargetArr.length == 0 || (remoteAnimationTarget = remoteAnimationTargetArr[0]) == null) {
                    Log.d("KeyguardViewMediator", "No apps provided to unocclude runner; skipping animation and unoccluding.");
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                    return;
                }
                KeyguardViewMediator.this.mContext.getMainExecutor().execute(new KeyguardViewMediator$6$$ExternalSyntheticLambda0(this, remoteAnimationTarget, new SyncRtSurfaceTransactionApplier(((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).getViewRootImpl().getView()), iRemoteAnimationFinishedCallback));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onAnimationStart$1(RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
                ValueAnimator valueAnimator = this.mUnoccludeAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                this.mUnoccludeAnimator = ofFloat;
                ofFloat.setDuration(250);
                this.mUnoccludeAnimator.setInterpolator(Interpolators.TOUCH_RESPONSE);
                this.mUnoccludeAnimator.addUpdateListener(new KeyguardViewMediator$6$$ExternalSyntheticLambda1(this, remoteAnimationTarget, syncRtSurfaceTransactionApplier));
                this.mUnoccludeAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        try {
                            iRemoteAnimationFinishedCallback.onAnimationFinished();
                            AnonymousClass6.this.mUnoccludeAnimator = null;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
                this.mUnoccludeAnimator.start();
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onAnimationStart$0(RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                this.mUnoccludeMatrix.setTranslate(0.0f, (1.0f - floatValue) * ((float) remoteAnimationTarget.screenSpaceBounds.height()) * 0.1f);
                syncRtSurfaceTransactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash).withMatrix(this.mUnoccludeMatrix).withCornerRadius(KeyguardViewMediator.this.mWindowCornerRadius).withAlpha(floatValue).build()});
            }
        };
        AnonymousClass7 r62 = new KeyguardStateController.Callback() {
            public void onBouncerShowingChanged() {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    keyguardViewMediator.adjustStatusBarLocked(keyguardViewMediator.mKeyguardStateController.isBouncerShowing(), false);
                }
            }
        };
        this.mKeyguardStateControllerCallback = r62;
        this.mDelayedLockBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD".equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra("seq", 0);
                    if (KeyguardViewMediator.DEBUG) {
                        Log.d("KeyguardViewMediator", "received DELAYED_KEYGUARD_ACTION with seq = " + intExtra + ", mDelayedShowingSequence = " + KeyguardViewMediator.this.mDelayedShowingSequence);
                    }
                    synchronized (KeyguardViewMediator.this) {
                        if (KeyguardViewMediator.this.mDelayedShowingSequence == intExtra) {
                            KeyguardViewMediator.this.doKeyguardLocked((Bundle) null);
                        }
                    }
                } else if ("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK".equals(intent.getAction())) {
                    int intExtra2 = intent.getIntExtra("seq", 0);
                    int intExtra3 = intent.getIntExtra("android.intent.extra.USER_ID", 0);
                    if (intExtra3 != 0) {
                        synchronized (KeyguardViewMediator.this) {
                            if (KeyguardViewMediator.this.mDelayedProfileShowingSequence == intExtra2) {
                                KeyguardViewMediator.this.lockProfile(intExtra3);
                            }
                        }
                    }
                }
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                    synchronized (KeyguardViewMediator.this) {
                        KeyguardViewMediator.this.mShuttingDown = true;
                    }
                }
            }
        };
        this.mHandler = new Handler(Looper.myLooper(), (Handler.Callback) null, true) {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        KeyguardViewMediator.this.handleShow((Bundle) message.obj);
                        return;
                    case 2:
                        KeyguardViewMediator.this.handleHide();
                        return;
                    case 3:
                        KeyguardViewMediator.this.handleReset();
                        return;
                    case 4:
                        Trace.beginSection("KeyguardViewMediator#handleMessage VERIFY_UNLOCK");
                        KeyguardViewMediator.this.handleVerifyUnlock();
                        Trace.endSection();
                        return;
                    case 5:
                        KeyguardViewMediator.this.handleNotifyFinishedGoingToSleep();
                        return;
                    case 7:
                        Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE");
                        KeyguardViewMediator.this.handleKeyguardDone();
                        Trace.endSection();
                        return;
                    case 8:
                        Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_DRAWING");
                        KeyguardViewMediator.this.handleKeyguardDoneDrawing();
                        Trace.endSection();
                        return;
                    case 9:
                        Trace.beginSection("KeyguardViewMediator#handleMessage SET_OCCLUDED");
                        KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                        boolean z = true;
                        boolean z2 = message.arg1 != 0;
                        if (message.arg2 == 0) {
                            z = false;
                        }
                        keyguardViewMediator.handleSetOccluded(z2, z);
                        Trace.endSection();
                        return;
                    case 10:
                        synchronized (KeyguardViewMediator.this) {
                            KeyguardViewMediator.this.doKeyguardLocked((Bundle) message.obj);
                        }
                        return;
                    case 11:
                        DismissMessage dismissMessage = (DismissMessage) message.obj;
                        KeyguardViewMediator.this.handleDismiss(dismissMessage.getCallback(), dismissMessage.getMessage());
                        return;
                    case 12:
                        Trace.beginSection("KeyguardViewMediator#handleMessage START_KEYGUARD_EXIT_ANIM");
                        ((NotificationShadeWindowController) KeyguardViewMediator.this.mNotificationShadeWindowControllerLazy.get()).batchApplyWindowLayoutParams(new KeyguardViewMediator$10$$ExternalSyntheticLambda0(this, (StartKeyguardExitAnimParams) message.obj));
                        Trace.endSection();
                        return;
                    case 13:
                        Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_PENDING_TIMEOUT");
                        Log.w("KeyguardViewMediator", "Timeout while waiting for activity drawn!");
                        Trace.endSection();
                        return;
                    case 14:
                        Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_STARTED_WAKING_UP");
                        KeyguardViewMediator.this.handleNotifyStartedWakingUp();
                        Trace.endSection();
                        return;
                    case 17:
                        KeyguardViewMediator.this.handleNotifyStartedGoingToSleep();
                        return;
                    case 18:
                        KeyguardViewMediator.this.handleSystemReady();
                        return;
                    case 19:
                        Trace.beginSection("KeyguardViewMediator#handleMessage CANCEL_KEYGUARD_EXIT_ANIM");
                        KeyguardViewMediator.this.handleCancelKeyguardExitAnimation();
                        Trace.endSection();
                        return;
                    default:
                        return;
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$handleMessage$0(StartKeyguardExitAnimParams startKeyguardExitAnimParams) {
                KeyguardViewMediator.this.handleStartKeyguardExitAnimation(startKeyguardExitAnimParams.startTime, startKeyguardExitAnimParams.fadeoutDuration, startKeyguardExitAnimParams.mApps, startKeyguardExitAnimParams.mWallpapers, startKeyguardExitAnimParams.mNonApps, startKeyguardExitAnimParams.mFinishedCallback);
                KeyguardViewMediator.this.mFalsingCollector.onSuccessfulUnlock();
            }
        };
        this.mKeyguardGoingAwayRunnable = new Runnable() {
            public void run() {
                Trace.beginSection("KeyguardViewMediator.mKeyGuardGoingAwayRunnable");
                if (KeyguardViewMediator.DEBUG) {
                    Log.d("KeyguardViewMediator", "keyguardGoingAway");
                }
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).keyguardGoingAway();
                int i = 0;
                if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldDisableWindowAnimationsForUnlock() || (KeyguardViewMediator.this.mWakeAndUnlocking && !KeyguardViewMediator.this.mWallpaperSupportsAmbientMode)) {
                    i = 2;
                }
                if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isGoingToNotificationShade() || (KeyguardViewMediator.this.mWakeAndUnlocking && KeyguardViewMediator.this.mWallpaperSupportsAmbientMode)) {
                    i |= 1;
                }
                if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isUnlockWithWallpaper()) {
                    i |= 4;
                }
                if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldSubtleWindowAnimationsForUnlock()) {
                    i |= 8;
                }
                if (KeyguardViewMediator.this.mWakeAndUnlocking && KeyguardUnlockAnimationController.Companion.isNexusLauncherUnderneath()) {
                    i |= 16;
                }
                KeyguardViewMediator.this.mUpdateMonitor.setKeyguardGoingAway(true);
                ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(true);
                KeyguardViewMediator.this.mUiBgExecutor.execute(new KeyguardViewMediator$11$$ExternalSyntheticLambda0(i));
                Trace.endSection();
            }

            public static /* synthetic */ void lambda$run$0(int i) {
                try {
                    ActivityTaskManager.getService().keyguardGoingAway(i);
                } catch (RemoteException e) {
                    Log.e("KeyguardViewMediator", "Error while calling WindowManager", e);
                }
            }
        };
        this.mHideAnimationFinishedRunnable = new KeyguardViewMediator$$ExternalSyntheticLambda2(this);
        this.mFalsingCollector = falsingCollector;
        this.mLockPatternUtils = lockPatternUtils;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardViewControllerLazy = lazy;
        this.mDismissCallbackRegistry = dismissCallbackRegistry;
        this.mNotificationShadeDepthController = lazy3;
        this.mUiBgExecutor = executor;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mPM = powerManager;
        this.mTrustManager = trustManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mKeyguardDisplayManager = keyguardDisplayManager;
        dumpManager.registerDumpable(getClass().getName(), this);
        this.mDeviceConfig = deviceConfigProxy2;
        this.mScreenOnCoordinator = screenOnCoordinator;
        this.mNotificationShadeWindowControllerLazy = lazy4;
        this.mShowHomeOverLockscreen = deviceConfigProxy2.getBoolean("systemui", "nav_bar_handle_show_over_lockscreen", true);
        DeviceConfigProxy deviceConfigProxy3 = this.mDeviceConfig;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        deviceConfigProxy3.addOnPropertiesChangedListener("systemui", new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), r5);
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(navigationModeController.addListener(new KeyguardViewMediator$$ExternalSyntheticLambda3(this)));
        this.mDozeParameters = dozeParameters;
        this.mStatusBarStateController = sysuiStatusBarStateController2;
        sysuiStatusBarStateController2.addCallback(this);
        this.mKeyguardStateController = keyguardStateController2;
        keyguardStateController2.addCallback(r62);
        this.mKeyguardUnlockAnimationControllerLazy = lazy2;
        this.mScreenOffAnimationController = screenOffAnimationController;
        this.mInteractionJankMonitor = interactionJankMonitor;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mActivityLaunchAnimator = lazy5;
        this.mPowerButtonY = (float) context.getResources().getDimensionPixelSize(R$dimen.physical_power_button_center_screen_location_y);
        this.mWindowCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(context);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(i);
    }

    public void userActivity() {
        this.mPM.userActivity(SystemClock.uptimeMillis(), false);
    }

    public boolean mustNotUnlockCurrentUser() {
        return UserManager.isSplitSystemUser() && KeyguardUpdateMonitor.getCurrentUser() == 0;
    }

    public final void setupLocked() {
        PowerManager.WakeLock newWakeLock = this.mPM.newWakeLock(1, "show keyguard");
        this.mShowKeyguardWakeLock = newWakeLock;
        boolean z = false;
        newWakeLock.setReferenceCounted(false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
        intentFilter2.setPriority(1000);
        this.mContext.registerReceiver(this.mDelayedLockBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", (Handler) null, 2);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        KeyguardUpdateMonitor.setCurrentUser(ActivityManager.getCurrentUser());
        if (isKeyguardServiceEnabled()) {
            if (!shouldWaitForProvisioning() && !this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
                z = true;
            }
            setShowingLocked(z, true);
        } else {
            setShowingLocked(false, true);
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mDeviceInteractive = this.mPM.isInteractive();
        this.mLockSounds = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
        String string = Settings.Global.getString(contentResolver, "lock_sound");
        if (string != null) {
            this.mLockSoundId = this.mLockSounds.load(string, 1);
        }
        if (string == null || this.mLockSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load lock sound from " + string);
        }
        String string2 = Settings.Global.getString(contentResolver, "unlock_sound");
        if (string2 != null) {
            this.mUnlockSoundId = this.mLockSounds.load(string2, 1);
        }
        if (string2 == null || this.mUnlockSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load unlock sound from " + string2);
        }
        String string3 = Settings.Global.getString(contentResolver, "trusted_sound");
        if (string3 != null) {
            this.mTrustedSoundId = this.mLockSounds.load(string3, 1);
        }
        if (string3 == null || this.mTrustedSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load trusted sound from " + string3);
        }
        this.mLockSoundVolume = (float) Math.pow(10.0d, (double) (((float) this.mContext.getResources().getInteger(17694851)) / 20.0f));
        this.mHideAnimation = AnimationUtils.loadAnimation(this.mContext, 17432683);
        this.mWorkLockController = new WorkLockActivityController(this.mContext);
    }

    public void start() {
        synchronized (this) {
            setupLocked();
        }
    }

    public void onSystemReady() {
        this.mHandler.obtainMessage(18).sendToTarget();
    }

    public final void handleSystemReady() {
        synchronized (this) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "onSystemReady");
            }
            this.mSystemReady = true;
            doKeyguardLocked((Bundle) null);
            this.mUpdateMonitor.registerCallback(this.mUpdateCallback);
            this.mDreamOverlayStateController.addCallback(this.mDreamOverlayStateCallback);
        }
        maybeSendUserPresentBroadcast();
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x004e A[Catch:{ RemoteException -> 0x005d }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0070 A[Catch:{ RemoteException -> 0x005d }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a2 A[Catch:{ RemoteException -> 0x005d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStartedGoingToSleep(int r10) {
        /*
            r9 = this;
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x001f
            java.lang.String r1 = "KeyguardViewMediator"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "onStartedGoingToSleep("
            r2.append(r3)
            r2.append(r10)
            java.lang.String r3 = ")"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            android.util.Log.d(r1, r2)
        L_0x001f:
            monitor-enter(r9)
            r1 = 0
            r9.mDeviceInteractive = r1     // Catch:{ all -> 0x00b4 }
            r9.mPowerGestureIntercepted = r1     // Catch:{ all -> 0x00b4 }
            r2 = 1
            r9.mGoingToSleep = r2     // Catch:{ all -> 0x00b4 }
            int r3 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch:{ all -> 0x00b4 }
            com.android.internal.widget.LockPatternUtils r4 = r9.mLockPatternUtils     // Catch:{ all -> 0x00b4 }
            boolean r4 = r4.getPowerButtonInstantlyLocks(r3)     // Catch:{ all -> 0x00b4 }
            if (r4 != 0) goto L_0x003f
            com.android.internal.widget.LockPatternUtils r4 = r9.mLockPatternUtils     // Catch:{ all -> 0x00b4 }
            boolean r4 = r4.isSecure(r3)     // Catch:{ all -> 0x00b4 }
            if (r4 != 0) goto L_0x003d
            goto L_0x003f
        L_0x003d:
            r4 = r1
            goto L_0x0040
        L_0x003f:
            r4 = r2
        L_0x0040:
            int r5 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch:{ all -> 0x00b4 }
            long r5 = r9.getLockTimeout(r5)     // Catch:{ all -> 0x00b4 }
            r9.mLockLater = r1     // Catch:{ all -> 0x00b4 }
            com.android.internal.policy.IKeyguardExitCallback r7 = r9.mExitSecureCallback     // Catch:{ all -> 0x00b4 }
            if (r7 == 0) goto L_0x0070
            if (r0 == 0) goto L_0x0057
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r3 = "pending exit secure callback cancelled"
            android.util.Log.d(r0, r3)     // Catch:{ all -> 0x00b4 }
        L_0x0057:
            com.android.internal.policy.IKeyguardExitCallback r0 = r9.mExitSecureCallback     // Catch:{ RemoteException -> 0x005d }
            r0.onKeyguardExitResult(r1)     // Catch:{ RemoteException -> 0x005d }
            goto L_0x0065
        L_0x005d:
            r0 = move-exception
            java.lang.String r3 = "KeyguardViewMediator"
            java.lang.String r4 = "Failed to call onKeyguardExitResult(false)"
            android.util.Slog.w(r3, r4, r0)     // Catch:{ all -> 0x00b4 }
        L_0x0065:
            r0 = 0
            r9.mExitSecureCallback = r0     // Catch:{ all -> 0x00b4 }
            boolean r0 = r9.mExternallyEnabled     // Catch:{ all -> 0x00b4 }
            if (r0 != 0) goto L_0x009e
            r9.hideLocked()     // Catch:{ all -> 0x00b4 }
            goto L_0x009e
        L_0x0070:
            boolean r0 = r9.mShowing     // Catch:{ all -> 0x00b4 }
            if (r0 == 0) goto L_0x007f
            com.android.systemui.statusbar.policy.KeyguardStateController r0 = r9.mKeyguardStateController     // Catch:{ all -> 0x00b4 }
            boolean r0 = r0.isKeyguardGoingAway()     // Catch:{ all -> 0x00b4 }
            if (r0 != 0) goto L_0x007f
            r9.mPendingReset = r2     // Catch:{ all -> 0x00b4 }
            goto L_0x009e
        L_0x007f:
            r0 = 3
            if (r10 != r0) goto L_0x0088
            r7 = 0
            int r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r0 > 0) goto L_0x008d
        L_0x0088:
            r0 = 2
            if (r10 != r0) goto L_0x0093
            if (r4 != 0) goto L_0x0093
        L_0x008d:
            r9.doKeyguardLaterLocked(r5)     // Catch:{ all -> 0x00b4 }
            r9.mLockLater = r2     // Catch:{ all -> 0x00b4 }
            goto L_0x009e
        L_0x0093:
            com.android.internal.widget.LockPatternUtils r0 = r9.mLockPatternUtils     // Catch:{ all -> 0x00b4 }
            boolean r0 = r0.isLockScreenDisabled(r3)     // Catch:{ all -> 0x00b4 }
            if (r0 != 0) goto L_0x009e
            r9.setPendingLock(r2)     // Catch:{ all -> 0x00b4 }
        L_0x009e:
            boolean r0 = r9.mPendingLock     // Catch:{ all -> 0x00b4 }
            if (r0 == 0) goto L_0x00a5
            r9.playSounds(r2)     // Catch:{ all -> 0x00b4 }
        L_0x00a5:
            monitor-exit(r9)     // Catch:{ all -> 0x00b4 }
            com.android.keyguard.KeyguardUpdateMonitor r0 = r9.mUpdateMonitor
            r0.dispatchStartedGoingToSleep(r10)
            com.android.keyguard.KeyguardUpdateMonitor r10 = r9.mUpdateMonitor
            r10.dispatchKeyguardGoingAway(r1)
            r9.notifyStartedGoingToSleep()
            return
        L_0x00b4:
            r10 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x00b4 }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.onStartedGoingToSleep(int):void");
    }

    public void onFinishedGoingToSleep(int i, boolean z) {
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "onFinishedGoingToSleep(" + i + ")");
        }
        synchronized (this) {
            this.mDeviceInteractive = false;
            this.mGoingToSleep = false;
            this.mWakeAndUnlocking = false;
            this.mAnimatingScreenOff = this.mDozeParameters.shouldAnimateDozingChange();
            resetKeyguardDonePendingLocked();
            this.mHideAnimationRun = false;
            notifyFinishedGoingToSleep();
            if (z) {
                ((PowerManager) this.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 5, "com.android.systemui:CAMERA_GESTURE_PREVENT_LOCK");
                setPendingLock(false);
                this.mPendingReset = false;
            }
            if (this.mPendingReset) {
                resetStateLocked();
                this.mPendingReset = false;
            }
            maybeHandlePendingLock();
            if (!this.mLockLater && !z) {
                doKeyguardForChildProfilesLocked();
            }
        }
        this.mUpdateMonitor.dispatchFinishedGoingToSleep(i);
    }

    public void maybeHandlePendingLock() {
        if (!this.mPendingLock) {
            return;
        }
        if (this.mScreenOffAnimationController.isKeyguardShowDelayed()) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "#maybeHandlePendingLock: not handling because the screen off animation's isKeyguardShowDelayed() returned true. This should be handled soon by #onStartedWakingUp, or by the end actions of the screen off animation.");
            }
        } else if (!this.mKeyguardStateController.isKeyguardGoingAway()) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "#maybeHandlePendingLock: handling pending lock; locking keyguard.");
            }
            doKeyguardLocked((Bundle) null);
            setPendingLock(false);
        } else if (DEBUG) {
            Log.d("KeyguardViewMediator", "#maybeHandlePendingLock: not handling because the keyguard is going away. This should be handled shortly by StatusBar#finishKeyguardFadingAway.");
        }
    }

    public final boolean isKeyguardServiceEnabled() {
        try {
            return this.mContext.getPackageManager().getServiceInfo(new ComponentName(this.mContext, KeyguardService.class), 0).isEnabled();
        } catch (PackageManager.NameNotFoundException unused) {
            return true;
        }
    }

    public final long getLockTimeout(int i) {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        long j = (long) Settings.Secure.getInt(contentResolver, "lock_screen_lock_after_timeout", 5000);
        long maximumTimeToLock = this.mLockPatternUtils.getDevicePolicyManager().getMaximumTimeToLock((ComponentName) null, i);
        return maximumTimeToLock <= 0 ? j : Math.max(Math.min(maximumTimeToLock - Math.max((long) Settings.System.getInt(contentResolver, "screen_off_timeout", 30000), 0), j), 0);
    }

    public final void doKeyguardLaterLocked() {
        long lockTimeout = getLockTimeout(KeyguardUpdateMonitor.getCurrentUser());
        if (lockTimeout == 0) {
            doKeyguardLocked((Bundle) null);
        } else {
            doKeyguardLaterLocked(lockTimeout);
        }
    }

    public final void doKeyguardLaterLocked(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        Intent intent = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intent.setPackage(this.mContext.getPackageName());
        intent.putExtra("seq", this.mDelayedShowingSequence);
        intent.addFlags(268435456);
        this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320));
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "setting alarm to turn off keyguard, seq = " + this.mDelayedShowingSequence);
        }
        doKeyguardLaterForChildProfilesLocked();
    }

    public final void doKeyguardLaterForChildProfilesLocked() {
        for (int i : UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId())) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                long lockTimeout = getLockTimeout(i);
                if (lockTimeout == 0) {
                    doKeyguardForChildProfilesLocked();
                } else {
                    long elapsedRealtime = SystemClock.elapsedRealtime() + lockTimeout;
                    Intent intent = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
                    intent.setPackage(this.mContext.getPackageName());
                    intent.putExtra("seq", this.mDelayedProfileShowingSequence);
                    intent.putExtra("android.intent.extra.USER_ID", i);
                    intent.addFlags(268435456);
                    this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320));
                }
            }
        }
    }

    public final void doKeyguardForChildProfilesLocked() {
        for (int i : UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId())) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                lockProfile(i);
            }
        }
    }

    public final void cancelDoKeyguardLaterLocked() {
        this.mDelayedShowingSequence++;
    }

    public final void cancelDoKeyguardForChildProfilesLocked() {
        this.mDelayedProfileShowingSequence++;
    }

    public void onStartedWakingUp(boolean z) {
        Trace.beginSection("KeyguardViewMediator#onStartedWakingUp");
        synchronized (this) {
            this.mDeviceInteractive = true;
            if (this.mPendingLock && !z) {
                doKeyguardLocked((Bundle) null);
            }
            this.mAnimatingScreenOff = false;
            cancelDoKeyguardLaterLocked();
            cancelDoKeyguardForChildProfilesLocked();
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "onStartedWakingUp, seq = " + this.mDelayedShowingSequence);
            }
            notifyStartedWakingUp();
        }
        this.mUpdateMonitor.dispatchStartedWakingUp();
        maybeSendUserPresentBroadcast();
        Trace.endSection();
    }

    public void onScreenTurnedOff() {
        this.mUpdateMonitor.dispatchScreenTurnedOff();
    }

    public final void maybeSendUserPresentBroadcast() {
        if (this.mSystemReady && this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
            sendUserPresentBroadcast();
        } else if (this.mSystemReady && shouldWaitForProvisioning()) {
            this.mLockPatternUtils.userPresent(KeyguardUpdateMonitor.getCurrentUser());
        }
    }

    public void onDreamingStarted() {
        this.mUpdateMonitor.dispatchDreamingStarted();
        synchronized (this) {
            if (this.mDeviceInteractive && this.mLockPatternUtils.isSecure(KeyguardUpdateMonitor.getCurrentUser())) {
                doKeyguardLaterLocked();
            }
        }
    }

    public void onDreamingStopped() {
        this.mUpdateMonitor.dispatchDreamingStopped();
        synchronized (this) {
            if (this.mDeviceInteractive) {
                cancelDoKeyguardLaterLocked();
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:39|40|41|42|54|51|37) */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0038, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00b6, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x009a, code lost:
        continue;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00a2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setKeyguardEnabled(boolean r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = DEBUG     // Catch:{ all -> 0x00b7 }
            if (r0 == 0) goto L_0x0021
            java.lang.String r1 = "KeyguardViewMediator"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b7 }
            r2.<init>()     // Catch:{ all -> 0x00b7 }
            java.lang.String r3 = "setKeyguardEnabled("
            r2.append(r3)     // Catch:{ all -> 0x00b7 }
            r2.append(r5)     // Catch:{ all -> 0x00b7 }
            java.lang.String r3 = ")"
            r2.append(r3)     // Catch:{ all -> 0x00b7 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00b7 }
            android.util.Log.d(r1, r2)     // Catch:{ all -> 0x00b7 }
        L_0x0021:
            r4.mExternallyEnabled = r5     // Catch:{ all -> 0x00b7 }
            r1 = 1
            if (r5 != 0) goto L_0x004b
            boolean r2 = r4.mShowing     // Catch:{ all -> 0x00b7 }
            if (r2 == 0) goto L_0x004b
            com.android.internal.policy.IKeyguardExitCallback r5 = r4.mExitSecureCallback     // Catch:{ all -> 0x00b7 }
            if (r5 == 0) goto L_0x0039
            if (r0 == 0) goto L_0x0037
            java.lang.String r5 = "KeyguardViewMediator"
            java.lang.String r0 = "in process of verifyUnlock request, ignoring"
            android.util.Log.d(r5, r0)     // Catch:{ all -> 0x00b7 }
        L_0x0037:
            monitor-exit(r4)     // Catch:{ all -> 0x00b7 }
            return
        L_0x0039:
            if (r0 == 0) goto L_0x0042
            java.lang.String r5 = "KeyguardViewMediator"
            java.lang.String r0 = "remembering to reshow, hiding keyguard, disabling status bar expansion"
            android.util.Log.d(r5, r0)     // Catch:{ all -> 0x00b7 }
        L_0x0042:
            r4.mNeedToReshowWhenReenabled = r1     // Catch:{ all -> 0x00b7 }
            r4.updateInputRestrictedLocked()     // Catch:{ all -> 0x00b7 }
            r4.hideLocked()     // Catch:{ all -> 0x00b7 }
            goto L_0x00b5
        L_0x004b:
            if (r5 == 0) goto L_0x00b5
            boolean r5 = r4.mNeedToReshowWhenReenabled     // Catch:{ all -> 0x00b7 }
            if (r5 == 0) goto L_0x00b5
            if (r0 == 0) goto L_0x005a
            java.lang.String r5 = "KeyguardViewMediator"
            java.lang.String r2 = "previously hidden, reshowing, reenabling status bar expansion"
            android.util.Log.d(r5, r2)     // Catch:{ all -> 0x00b7 }
        L_0x005a:
            r5 = 0
            r4.mNeedToReshowWhenReenabled = r5     // Catch:{ all -> 0x00b7 }
            r4.updateInputRestrictedLocked()     // Catch:{ all -> 0x00b7 }
            com.android.internal.policy.IKeyguardExitCallback r2 = r4.mExitSecureCallback     // Catch:{ all -> 0x00b7 }
            r3 = 0
            if (r2 == 0) goto L_0x0082
            if (r0 == 0) goto L_0x006e
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "onKeyguardExitResult(false), resetting"
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x00b7 }
        L_0x006e:
            com.android.internal.policy.IKeyguardExitCallback r0 = r4.mExitSecureCallback     // Catch:{ RemoteException -> 0x0074 }
            r0.onKeyguardExitResult(r5)     // Catch:{ RemoteException -> 0x0074 }
            goto L_0x007c
        L_0x0074:
            r5 = move-exception
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "Failed to call onKeyguardExitResult(false)"
            android.util.Slog.w(r0, r1, r5)     // Catch:{ all -> 0x00b7 }
        L_0x007c:
            r4.mExitSecureCallback = r3     // Catch:{ all -> 0x00b7 }
            r4.resetStateLocked()     // Catch:{ all -> 0x00b7 }
            goto L_0x00b5
        L_0x0082:
            r4.showLocked(r3)     // Catch:{ all -> 0x00b7 }
            r4.mWaitingUntilKeyguardVisible = r1     // Catch:{ all -> 0x00b7 }
            android.os.Handler r5 = r4.mHandler     // Catch:{ all -> 0x00b7 }
            r1 = 8
            r2 = 2000(0x7d0, double:9.88E-321)
            r5.sendEmptyMessageDelayed(r1, r2)     // Catch:{ all -> 0x00b7 }
            if (r0 == 0) goto L_0x009a
            java.lang.String r5 = "KeyguardViewMediator"
            java.lang.String r0 = "waiting until mWaitingUntilKeyguardVisible is false"
            android.util.Log.d(r5, r0)     // Catch:{ all -> 0x00b7 }
        L_0x009a:
            boolean r5 = r4.mWaitingUntilKeyguardVisible     // Catch:{ all -> 0x00b7 }
            if (r5 == 0) goto L_0x00aa
            r4.wait()     // Catch:{ InterruptedException -> 0x00a2 }
            goto L_0x009a
        L_0x00a2:
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x00b7 }
            r5.interrupt()     // Catch:{ all -> 0x00b7 }
            goto L_0x009a
        L_0x00aa:
            boolean r5 = DEBUG     // Catch:{ all -> 0x00b7 }
            if (r5 == 0) goto L_0x00b5
            java.lang.String r5 = "KeyguardViewMediator"
            java.lang.String r0 = "done waiting for mWaitingUntilKeyguardVisible"
            android.util.Log.d(r5, r0)     // Catch:{ all -> 0x00b7 }
        L_0x00b5:
            monitor-exit(r4)     // Catch:{ all -> 0x00b7 }
            return
        L_0x00b7:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x00b7 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.setKeyguardEnabled(boolean):void");
    }

    public void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) {
        Trace.beginSection("KeyguardViewMediator#verifyUnlock");
        synchronized (this) {
            boolean z = DEBUG;
            if (z) {
                Log.d("KeyguardViewMediator", "verifyUnlock");
            }
            if (shouldWaitForProvisioning()) {
                if (z) {
                    Log.d("KeyguardViewMediator", "ignoring because device isn't provisioned");
                }
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e);
                }
            } else if (this.mExternallyEnabled) {
                Log.w("KeyguardViewMediator", "verifyUnlock called when not externally disabled");
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e2) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e2);
                }
            } else if (this.mExitSecureCallback != null) {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e3) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e3);
                }
            } else if (!isSecure()) {
                this.mExternallyEnabled = true;
                this.mNeedToReshowWhenReenabled = false;
                updateInputRestricted();
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(true);
                } catch (RemoteException e4) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e4);
                }
            } else {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e5) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e5);
                }
            }
        }
        Trace.endSection();
    }

    public boolean isShowingAndNotOccluded() {
        return this.mShowing && !this.mOccluded;
    }

    public void setOccluded(boolean z, boolean z2) {
        Log.d("KeyguardViewMediator", "setOccluded(" + z + ")");
        Trace.beginSection("KeyguardViewMediator#setOccluded");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "setOccluded " + z);
        }
        this.mInteractionJankMonitor.cancel(23);
        this.mHandler.removeMessages(9);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9, z ? 1 : 0, z2 ? 1 : 0));
        Trace.endSection();
    }

    public IRemoteAnimationRunner getOccludeAnimationRunner() {
        return this.mOccludeAnimationRunner;
    }

    public IRemoteAnimationRunner getUnoccludeAnimationRunner() {
        return this.mUnoccludeAnimationRunner;
    }

    public static int getUnlockTrackSimState(int i) {
        return mUnlockTrackSimStates.get(i);
    }

    public boolean isHiding() {
        return this.mHiding;
    }

    public final void handleSetOccluded(boolean z, boolean z2) {
        boolean z3;
        Trace.beginSection("KeyguardViewMediator#handleSetOccluded");
        Log.d("KeyguardViewMediator", "handleSetOccluded(" + z + ")");
        synchronized (this) {
            if (this.mHiding && z) {
                startKeyguardExitAnimation(0, 0);
            }
            if (this.mOccluded != z) {
                this.mOccluded = z;
                this.mUpdateMonitor.setKeyguardOccluded(z);
                KeyguardViewController keyguardViewController = this.mKeyguardViewControllerLazy.get();
                if (!((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isSimPinSecure()) {
                    if (z2 && this.mDeviceInteractive) {
                        z3 = true;
                        keyguardViewController.setOccluded(z, z3);
                        adjustStatusBarLocked();
                    }
                }
                z3 = false;
                keyguardViewController.setOccluded(z, z3);
                adjustStatusBarLocked();
            }
        }
        Trace.endSection();
    }

    public void doKeyguardTimeout(Bundle bundle) {
        this.mHandler.removeMessages(10);
        this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(10, bundle));
    }

    public boolean isInputRestricted() {
        return this.mShowing || this.mNeedToReshowWhenReenabled;
    }

    public final void updateInputRestricted() {
        synchronized (this) {
            updateInputRestrictedLocked();
        }
    }

    public final void updateInputRestrictedLocked() {
        boolean isInputRestricted = isInputRestricted();
        if (this.mInputRestricted != isInputRestricted) {
            this.mInputRestricted = isInputRestricted;
            for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
                IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
                try {
                    iKeyguardStateCallback.onInputRestrictedStateChanged(isInputRestricted);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onDeviceProvisioned", e);
                    if (e instanceof DeadObjectException) {
                        this.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                    }
                }
            }
        }
    }

    public final void doKeyguardLocked(Bundle bundle) {
        if (!KeyguardUpdateMonitor.CORE_APPS_ONLY) {
            boolean z = true;
            if (!this.mExternallyEnabled) {
                if (DEBUG) {
                    Log.d("KeyguardViewMediator", "doKeyguard: not showing because externally disabled");
                }
                this.mNeedToReshowWhenReenabled = true;
            } else if (!this.mShowing || !this.mKeyguardViewControllerLazy.get().isShowing()) {
                if (!mustNotUnlockCurrentUser() || !this.mUpdateMonitor.isDeviceProvisioned()) {
                    boolean z2 = this.mUpdateMonitor.isSimPinSecure() || ((SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(1)) || SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(7))) && (SystemProperties.getBoolean("keyguard.no_require_sim", false) ^ true));
                    if (z2 || !shouldWaitForProvisioning()) {
                        if (bundle == null || !bundle.getBoolean("force_show", false)) {
                            z = false;
                        }
                        if (this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser()) && !z2 && !z) {
                            if (DEBUG) {
                                Log.d("KeyguardViewMediator", "doKeyguard: not showing because lockscreen is off");
                                return;
                            }
                            return;
                        }
                    } else if (DEBUG) {
                        Log.d("KeyguardViewMediator", "doKeyguard: not showing because device isn't provisioned and the sim is not locked or missing");
                        return;
                    } else {
                        return;
                    }
                }
                if (DEBUG) {
                    Log.d("KeyguardViewMediator", "doKeyguard: showing the lock screen");
                }
                showLocked(bundle);
            } else {
                if (DEBUG) {
                    Log.d("KeyguardViewMediator", "doKeyguard: not showing because it is already showing");
                }
                resetStateLocked();
            }
        } else if (DEBUG) {
            Log.d("KeyguardViewMediator", "doKeyguard: not showing because booting to cryptkeeper");
        }
    }

    public final void lockProfile(int i) {
        this.mTrustManager.setDeviceLockedForUser(i, true);
    }

    public final boolean shouldWaitForProvisioning() {
        return !this.mUpdateMonitor.isDeviceProvisioned() && !isSecure();
    }

    public final void handleDismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        if (this.mShowing) {
            if (iKeyguardDismissCallback != null) {
                this.mDismissCallbackRegistry.addCallback(iKeyguardDismissCallback);
            }
            this.mCustomMessage = charSequence;
            this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
        } else if (iKeyguardDismissCallback != null) {
            new DismissCallbackWrapper(iKeyguardDismissCallback).notifyDismissError();
        }
    }

    public void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        this.mHandler.obtainMessage(11, new DismissMessage(iKeyguardDismissCallback, charSequence)).sendToTarget();
    }

    public final void resetStateLocked() {
        if (DEBUG) {
            Log.e("KeyguardViewMediator", "resetStateLocked");
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
    }

    public final void notifyStartedGoingToSleep() {
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "notifyStartedGoingToSleep");
        }
        this.mHandler.sendEmptyMessage(17);
    }

    public final void notifyFinishedGoingToSleep() {
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "notifyFinishedGoingToSleep");
        }
        this.mHandler.sendEmptyMessage(5);
    }

    public final void notifyStartedWakingUp() {
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "notifyStartedWakingUp");
        }
        this.mHandler.sendEmptyMessage(14);
    }

    public final void showLocked(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#showLocked acquiring mShowKeyguardWakeLock");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "showLocked");
        }
        this.mShowKeyguardWakeLock.acquire();
        this.mHandler.sendMessageAtFrontOfQueue(this.mHandler.obtainMessage(1, bundle));
        Trace.endSection();
    }

    public final void hideLocked() {
        Trace.beginSection("KeyguardViewMediator#hideLocked");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "hideLocked");
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
        Trace.endSection();
    }

    public void hideWithAnimation(IRemoteAnimationRunner iRemoteAnimationRunner) {
        if (this.mKeyguardDonePending) {
            this.mKeyguardExitAnimationRunner = iRemoteAnimationRunner;
            this.mViewMediatorCallback.readyForKeyguardDone();
        }
    }

    public void setBlursDisabledForAppLaunch(boolean z) {
        this.mNotificationShadeDepthController.get().setBlursDisabledForAppLaunch(z);
    }

    public boolean isSecure() {
        return isSecure(KeyguardUpdateMonitor.getCurrentUser());
    }

    public boolean isSecure(int i) {
        return this.mLockPatternUtils.isSecure(i) || this.mUpdateMonitor.isSimPinSecure();
    }

    public boolean isAnySimPinSecure() {
        for (int i = 0; i < this.mLastSimStates.size(); i++) {
            if (KeyguardUpdateMonitor.isSimPinSecure(this.mLastSimStates.get(this.mLastSimStates.keyAt(i)))) {
                return true;
            }
        }
        return false;
    }

    public void setSwitchingUser(boolean z) {
        this.mUpdateMonitor.setSwitchingUser(z);
    }

    public void setCurrentUser(int i) {
        KeyguardUpdateMonitor.setCurrentUser(i);
        synchronized (this) {
            notifyTrustedChangedLocked(this.mUpdateMonitor.getUserHasTrust(i));
        }
    }

    public final void keyguardDone() {
        Trace.beginSection("KeyguardViewMediator#keyguardDone");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "keyguardDone()");
        }
        userActivity();
        EventLog.writeEvent(70000, 2);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7));
        Trace.endSection();
    }

    public final void tryKeyguardDone() {
        boolean z = DEBUG;
        if (z) {
            Log.d("KeyguardViewMediator", "tryKeyguardDone: pending - " + this.mKeyguardDonePending + ", animRan - " + this.mHideAnimationRun + " animRunning - " + this.mHideAnimationRunning);
        }
        if (!this.mKeyguardDonePending && this.mHideAnimationRun && !this.mHideAnimationRunning) {
            handleKeyguardDone();
        } else if (!this.mHideAnimationRun) {
            if (z) {
                Log.d("KeyguardViewMediator", "tryKeyguardDone: starting pre-hide animation");
            }
            this.mHideAnimationRun = true;
            this.mHideAnimationRunning = true;
            this.mKeyguardViewControllerLazy.get().startPreHideAnimation(this.mHideAnimationFinishedRunnable);
        }
    }

    public final void handleKeyguardDone() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDone");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda8(this, currentUser));
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "handleKeyguardDone");
        }
        synchronized (this) {
            resetKeyguardDonePendingLocked();
        }
        if (this.mGoingToSleep) {
            this.mUpdateMonitor.clearBiometricRecognizedWhenKeyguardDone(currentUser);
            Log.i("KeyguardViewMediator", "Device is going to sleep, aborting keyguardDone");
            return;
        }
        setPendingLock(false);
        IKeyguardExitCallback iKeyguardExitCallback = this.mExitSecureCallback;
        if (iKeyguardExitCallback != null) {
            try {
                iKeyguardExitCallback.onKeyguardExitResult(true);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult()", e);
            }
            this.mExitSecureCallback = null;
            this.mExternallyEnabled = true;
            this.mNeedToReshowWhenReenabled = false;
            updateInputRestricted();
        }
        handleHide();
        this.mUpdateMonitor.clearBiometricRecognizedWhenKeyguardDone(currentUser);
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleKeyguardDone$1(int i) {
        if (this.mLockPatternUtils.isSecure(i)) {
            this.mLockPatternUtils.getDevicePolicyManager().reportKeyguardDismissed(i);
        }
    }

    public final void sendUserPresentBroadcast() {
        synchronized (this) {
            if (this.mBootCompleted) {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda0(this, (UserManager) this.mContext.getSystemService("user"), new UserHandle(currentUser), currentUser));
            } else {
                this.mBootSendUserPresent = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$sendUserPresentBroadcast$2(UserManager userManager, UserHandle userHandle, int i) {
        for (int of : userManager.getProfileIdsWithDisabled(userHandle.getIdentifier())) {
            this.mContext.sendBroadcastAsUser(USER_PRESENT_INTENT, UserHandle.of(of));
        }
        this.mLockPatternUtils.userPresent(i);
    }

    public final void handleKeyguardDoneDrawing() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDoneDrawing");
        synchronized (this) {
            boolean z = DEBUG;
            if (z) {
                Log.d("KeyguardViewMediator", "handleKeyguardDoneDrawing");
            }
            if (this.mWaitingUntilKeyguardVisible) {
                if (z) {
                    Log.d("KeyguardViewMediator", "handleKeyguardDoneDrawing: notifying mWaitingUntilKeyguardVisible");
                }
                this.mWaitingUntilKeyguardVisible = false;
                notifyAll();
                this.mHandler.removeMessages(8);
            }
        }
        Trace.endSection();
    }

    public final void playSounds(boolean z) {
        playSound(z ? this.mLockSoundId : this.mUnlockSoundId);
    }

    public final void playSound(int i) {
        if (i != 0 && Settings.System.getInt(this.mContext.getContentResolver(), "lockscreen_sounds_enabled", 1) == 1) {
            this.mLockSounds.stop(this.mLockSoundStreamId);
            if (this.mAudioManager == null) {
                AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                this.mAudioManager = audioManager;
                if (audioManager != null) {
                    this.mUiSoundsStreamType = audioManager.getUiSoundsStreamType();
                } else {
                    return;
                }
            }
            this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda10(this, i));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$playSound$3(int i) {
        if (!this.mAudioManager.isStreamMute(this.mUiSoundsStreamType)) {
            SoundPool soundPool = this.mLockSounds;
            float f = this.mLockSoundVolume;
            int play = soundPool.play(i, f, f, 1, 0, 1.0f);
            synchronized (this) {
                this.mLockSoundStreamId = play;
            }
        }
    }

    public final void playTrustedSound() {
        playSound(this.mTrustedSoundId);
    }

    public final void updateActivityLockScreenState(boolean z, boolean z2) {
        this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda6(z, z2));
    }

    public static /* synthetic */ void lambda$updateActivityLockScreenState$4(boolean z, boolean z2) {
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "updateActivityLockScreenState(" + z + ", " + z2 + ")");
        }
        try {
            ActivityTaskManager.getService().setLockScreenShown(z, z2);
        } catch (RemoteException unused) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void handleShow(android.os.Bundle r3) {
        /*
            r2 = this;
            java.lang.String r0 = "KeyguardViewMediator#handleShow"
            android.os.Trace.beginSection(r0)
            int r0 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            com.android.internal.widget.LockPatternUtils r1 = r2.mLockPatternUtils
            boolean r1 = r1.isSecure(r0)
            if (r1 == 0) goto L_0x001a
            com.android.internal.widget.LockPatternUtils r1 = r2.mLockPatternUtils
            android.app.admin.DevicePolicyManager r1 = r1.getDevicePolicyManager()
            r1.reportKeyguardSecured(r0)
        L_0x001a:
            monitor-enter(r2)
            boolean r0 = r2.mSystemReady     // Catch:{ all -> 0x0084 }
            if (r0 != 0) goto L_0x002c
            boolean r3 = DEBUG     // Catch:{ all -> 0x0084 }
            if (r3 == 0) goto L_0x002a
            java.lang.String r3 = "KeyguardViewMediator"
            java.lang.String r0 = "ignoring handleShow because system is not ready."
            android.util.Log.d(r3, r0)     // Catch:{ all -> 0x0084 }
        L_0x002a:
            monitor-exit(r2)     // Catch:{ all -> 0x0084 }
            return
        L_0x002c:
            boolean r0 = DEBUG     // Catch:{ all -> 0x0084 }
            if (r0 == 0) goto L_0x0037
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "handleShow"
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x0084 }
        L_0x0037:
            r0 = 0
            r2.mHiding = r0     // Catch:{ all -> 0x0084 }
            r1 = 0
            r2.mKeyguardExitAnimationRunner = r1     // Catch:{ all -> 0x0084 }
            r2.mWakeAndUnlocking = r0     // Catch:{ all -> 0x0084 }
            r2.setPendingLock(r0)     // Catch:{ all -> 0x0084 }
            r1 = 1
            r2.setShowingLocked(r1)     // Catch:{ all -> 0x0084 }
            dagger.Lazy<com.android.keyguard.KeyguardViewController> r1 = r2.mKeyguardViewControllerLazy     // Catch:{ all -> 0x0084 }
            java.lang.Object r1 = r1.get()     // Catch:{ all -> 0x0084 }
            com.android.keyguard.KeyguardViewController r1 = (com.android.keyguard.KeyguardViewController) r1     // Catch:{ all -> 0x0084 }
            r1.show(r3)     // Catch:{ all -> 0x0084 }
            r2.resetKeyguardDonePendingLocked()     // Catch:{ all -> 0x0084 }
            r2.mHideAnimationRun = r0     // Catch:{ all -> 0x0084 }
            r2.adjustStatusBarLocked()     // Catch:{ all -> 0x0084 }
            r2.userActivity()     // Catch:{ all -> 0x0084 }
            com.android.keyguard.KeyguardUpdateMonitor r3 = r2.mUpdateMonitor     // Catch:{ all -> 0x0084 }
            r3.setKeyguardGoingAway(r0)     // Catch:{ all -> 0x0084 }
            dagger.Lazy<com.android.keyguard.KeyguardViewController> r3 = r2.mKeyguardViewControllerLazy     // Catch:{ all -> 0x0084 }
            java.lang.Object r3 = r3.get()     // Catch:{ all -> 0x0084 }
            com.android.keyguard.KeyguardViewController r3 = (com.android.keyguard.KeyguardViewController) r3     // Catch:{ all -> 0x0084 }
            r3.setKeyguardGoingAwayState(r0)     // Catch:{ all -> 0x0084 }
            android.os.PowerManager$WakeLock r3 = r2.mShowKeyguardWakeLock     // Catch:{ all -> 0x0084 }
            r3.release()     // Catch:{ all -> 0x0084 }
            monitor-exit(r2)     // Catch:{ all -> 0x0084 }
            com.android.keyguard.KeyguardDisplayManager r3 = r2.mKeyguardDisplayManager
            r3.show()
            com.android.internal.widget.LockPatternUtils r2 = r2.mLockPatternUtils
            int r3 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            r2.scheduleNonStrongBiometricIdleTimeout(r3)
            android.os.Trace.endSection()
            return
        L_0x0084:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0084 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.handleShow(android.os.Bundle):void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        Log.e("KeyguardViewMediator", "mHideAnimationFinishedRunnable#run");
        this.mHideAnimationRunning = false;
        tryKeyguardDone();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0063, code lost:
        android.os.Trace.endSection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0066, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void handleHide() {
        /*
            r5 = this;
            java.lang.String r0 = "KeyguardViewMediator#handleHide"
            android.os.Trace.beginSection(r0)
            boolean r0 = r5.mAodShowing
            if (r0 != 0) goto L_0x000d
            boolean r0 = r5.mDreamOverlayShowing
            if (r0 == 0) goto L_0x0021
        L_0x000d:
            android.content.Context r0 = r5.mContext
            java.lang.Class<android.os.PowerManager> r1 = android.os.PowerManager.class
            java.lang.Object r0 = r0.getSystemService(r1)
            android.os.PowerManager r0 = (android.os.PowerManager) r0
            long r1 = android.os.SystemClock.uptimeMillis()
            r3 = 4
            java.lang.String r4 = "com.android.systemui:BOUNCER_DOZING"
            r0.wakeUp(r1, r3, r4)
        L_0x0021:
            monitor-enter(r5)
            boolean r0 = DEBUG     // Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x002d
            java.lang.String r1 = "KeyguardViewMediator"
            java.lang.String r2 = "handleHide"
            android.util.Log.d(r1, r2)     // Catch:{ all -> 0x0067 }
        L_0x002d:
            boolean r1 = r5.mustNotUnlockCurrentUser()     // Catch:{ all -> 0x0067 }
            if (r1 == 0) goto L_0x0041
            if (r0 == 0) goto L_0x003c
            java.lang.String r0 = "KeyguardViewMediator"
            java.lang.String r1 = "Split system user, quit unlocking."
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x0067 }
        L_0x003c:
            r0 = 0
            r5.mKeyguardExitAnimationRunner = r0     // Catch:{ all -> 0x0067 }
            monitor-exit(r5)     // Catch:{ all -> 0x0067 }
            return
        L_0x0041:
            r0 = 1
            r5.mHiding = r0     // Catch:{ all -> 0x0067 }
            boolean r0 = r5.mShowing     // Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x0052
            boolean r0 = r5.mOccluded     // Catch:{ all -> 0x0067 }
            if (r0 != 0) goto L_0x0052
            java.lang.Runnable r0 = r5.mKeyguardGoingAwayRunnable     // Catch:{ all -> 0x0067 }
            r0.run()     // Catch:{ all -> 0x0067 }
            goto L_0x0062
        L_0x0052:
            dagger.Lazy<com.android.systemui.statusbar.NotificationShadeWindowController> r0 = r5.mNotificationShadeWindowControllerLazy     // Catch:{ all -> 0x0067 }
            java.lang.Object r0 = r0.get()     // Catch:{ all -> 0x0067 }
            com.android.systemui.statusbar.NotificationShadeWindowController r0 = (com.android.systemui.statusbar.NotificationShadeWindowController) r0     // Catch:{ all -> 0x0067 }
            com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda7 r1 = new com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda7     // Catch:{ all -> 0x0067 }
            r1.<init>(r5)     // Catch:{ all -> 0x0067 }
            r0.batchApplyWindowLayoutParams(r1)     // Catch:{ all -> 0x0067 }
        L_0x0062:
            monitor-exit(r5)     // Catch:{ all -> 0x0067 }
            android.os.Trace.endSection()
            return
        L_0x0067:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0067 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.handleHide():void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleHide$6() {
        handleStartKeyguardExitAnimation(SystemClock.uptimeMillis() + this.mHideAnimation.getStartOffset(), this.mHideAnimation.getDuration(), (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (IRemoteAnimationFinishedCallback) null);
    }

    public final void handleStartKeyguardExitAnimation(long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        long j3 = j;
        long j4 = j2;
        RemoteAnimationTarget[] remoteAnimationTargetArr4 = remoteAnimationTargetArr;
        final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback2 = iRemoteAnimationFinishedCallback;
        Trace.beginSection("KeyguardViewMediator#handleStartKeyguardExitAnimation");
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "handleStartKeyguardExitAnimation startTime=" + j + " fadeoutDuration=" + j4);
        }
        synchronized (this) {
            if (this.mHiding || this.mSurfaceBehindRemoteAnimationRequested || this.mKeyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture()) {
                this.mHiding = false;
                IRemoteAnimationRunner iRemoteAnimationRunner = this.mKeyguardExitAnimationRunner;
                this.mKeyguardExitAnimationRunner = null;
                LatencyTracker.getInstance(this.mContext).onActionEnd(11);
                boolean z = KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation;
                if (z && iRemoteAnimationRunner != null && iRemoteAnimationFinishedCallback2 != null) {
                    AnonymousClass12 r7 = new IRemoteAnimationFinishedCallback() {
                        public void onAnimationFinished() throws RemoteException {
                            try {
                                iRemoteAnimationFinishedCallback2.onAnimationFinished();
                            } catch (RemoteException e) {
                                Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e);
                            }
                            KeyguardViewMediator.this.onKeyguardExitFinished();
                            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).hide(0, 0);
                            KeyguardViewMediator.this.mInteractionJankMonitor.end(29);
                        }

                        public IBinder asBinder() {
                            return iRemoteAnimationFinishedCallback2.asBinder();
                        }
                    };
                    try {
                        this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("RunRemoteAnimation"));
                        iRemoteAnimationRunner.onAnimationStart(7, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, r7);
                    } catch (RemoteException e) {
                        Slog.w("KeyguardViewMediator", "Failed to call onAnimationStart", e);
                    }
                } else if (!z || this.mStatusBarStateController.leaveOpenOnKeyguardHide() || remoteAnimationTargetArr4 == null || remoteAnimationTargetArr4.length <= 0) {
                    this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("RemoteAnimationDisabled"));
                    this.mKeyguardViewControllerLazy.get().hide(j, j4);
                    this.mContext.getMainExecutor().execute(new KeyguardViewMediator$$ExternalSyntheticLambda9(this, iRemoteAnimationFinishedCallback2, remoteAnimationTargetArr4));
                    onKeyguardExitFinished();
                } else {
                    this.mSurfaceBehindRemoteAnimationFinishedCallback = iRemoteAnimationFinishedCallback2;
                    this.mSurfaceBehindRemoteAnimationRunning = true;
                    this.mInteractionJankMonitor.begin(createInteractionJankMonitorConf("DismissPanel"));
                    this.mKeyguardUnlockAnimationControllerLazy.get().notifyStartSurfaceBehindRemoteAnimation(remoteAnimationTargetArr4[0], j, this.mSurfaceBehindRemoteAnimationRequested);
                }
            } else {
                if (iRemoteAnimationFinishedCallback2 != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e2) {
                        Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e2);
                    }
                }
                setShowingLocked(this.mShowing, true);
                return;
            }
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleStartKeyguardExitAnimation$8(final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (iRemoteAnimationFinishedCallback == null) {
            this.mKeyguardUnlockAnimationControllerLazy.get().notifyFinishedKeyguardExitAnimation(false);
            this.mInteractionJankMonitor.end(29);
            return;
        }
        SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView());
        RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[0];
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(400);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new KeyguardViewMediator$$ExternalSyntheticLambda11(remoteAnimationTarget, syncRtSurfaceTransactionApplier));
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException unused) {
                    Slog.e("KeyguardViewMediator", "RemoteException");
                } catch (Throwable th) {
                    KeyguardViewMediator.this.mInteractionJankMonitor.end(29);
                    throw th;
                }
                KeyguardViewMediator.this.mInteractionJankMonitor.end(29);
            }

            public void onAnimationCancel(Animator animator) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException unused) {
                    Slog.e("KeyguardViewMediator", "RemoteException");
                } catch (Throwable th) {
                    KeyguardViewMediator.this.mInteractionJankMonitor.cancel(29);
                    throw th;
                }
                KeyguardViewMediator.this.mInteractionJankMonitor.cancel(29);
            }
        });
        ofFloat.start();
    }

    public final void onKeyguardExitFinished() {
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(this.mPhoneState)) {
            playSounds(false);
        }
        setShowingLocked(false);
        this.mWakeAndUnlocking = false;
        this.mDismissCallbackRegistry.notifyDismissSucceeded();
        resetKeyguardDonePendingLocked();
        this.mHideAnimationRun = false;
        adjustStatusBarLocked();
        sendUserPresentBroadcast();
    }

    public final InteractionJankMonitor.Configuration.Builder createInteractionJankMonitorConf(String str) {
        return InteractionJankMonitor.Configuration.Builder.withView(29, this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView()).setTag(str);
    }

    public boolean isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() {
        return this.mSurfaceBehindRemoteAnimationRunning || this.mKeyguardStateController.isFlingingToDismissKeyguard();
    }

    public final void handleCancelKeyguardExitAnimation() {
        showSurfaceBehindKeyguard();
        onKeyguardExitRemoteAnimationFinished(true);
    }

    public void onKeyguardExitRemoteAnimationFinished(boolean z) {
        if (this.mSurfaceBehindRemoteAnimationRunning || this.mSurfaceBehindRemoteAnimationRequested) {
            this.mKeyguardViewControllerLazy.get().blockPanelExpansionFromCurrentTouch();
            boolean z2 = this.mShowing;
            InteractionJankMonitor.getInstance().end(29);
            DejankUtils.postAfterTraversal(new KeyguardViewMediator$$ExternalSyntheticLambda1(this, z2, z));
            this.mKeyguardUnlockAnimationControllerLazy.get().notifyFinishedKeyguardExitAnimation(z);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onKeyguardExitRemoteAnimationFinished$9(boolean z, boolean z2) {
        onKeyguardExitFinished();
        if (this.mKeyguardStateController.isDismissingFromSwipe() || z) {
            this.mKeyguardUnlockAnimationControllerLazy.get().hideKeyguardViewAfterRemoteAnimation();
        }
        finishSurfaceBehindRemoteAnimation(z2);
        this.mSurfaceBehindRemoteAnimationRequested = false;
        this.mKeyguardStateController.notifyKeyguardGoingAway(false);
        this.mUpdateMonitor.dispatchKeyguardDismissAnimationFinished();
    }

    public void showSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = true;
        int i = 6;
        try {
            if (KeyguardUnlockAnimationController.Companion.isNexusLauncherUnderneath()) {
                i = 22;
            }
            ActivityTaskManager.getService().keyguardGoingAway(i);
            this.mKeyguardStateController.notifyKeyguardGoingAway(true);
        } catch (RemoteException e) {
            this.mSurfaceBehindRemoteAnimationRequested = false;
            e.printStackTrace();
        }
    }

    public void hideSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = false;
        if (this.mShowing) {
            setShowingLocked(true, true);
        }
    }

    public boolean requestedShowSurfaceBehindKeyguard() {
        return this.mSurfaceBehindRemoteAnimationRequested;
    }

    public boolean isAnimatingBetweenKeyguardAndSurfaceBehind() {
        return this.mSurfaceBehindRemoteAnimationRunning;
    }

    public void finishSurfaceBehindRemoteAnimation(boolean z) {
        if (this.mSurfaceBehindRemoteAnimationRunning) {
            this.mSurfaceBehindRemoteAnimationRunning = false;
            IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = this.mSurfaceBehindRemoteAnimationFinishedCallback;
            if (iRemoteAnimationFinishedCallback != null) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                    this.mSurfaceBehindRemoteAnimationFinishedCallback = null;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public final void adjustStatusBarLocked() {
        adjustStatusBarLocked(false, false);
    }

    public final void adjustStatusBarLocked(boolean z, boolean z2) {
        if (this.mStatusBarManager == null) {
            this.mStatusBarManager = (StatusBarManager) this.mContext.getSystemService("statusbar");
        }
        StatusBarManager statusBarManager = this.mStatusBarManager;
        if (statusBarManager == null) {
            Log.w("KeyguardViewMediator", "Could not get status bar manager");
            return;
        }
        int i = 0;
        if (z2) {
            statusBarManager.disable(0);
        }
        if (z || isShowingAndNotOccluded()) {
            if (!this.mShowHomeOverLockscreen || !this.mInGestureNavigationMode) {
                i = 2097152;
            }
            i |= 16777216;
        }
        if (EventUtils.ENABLE_CUSTOMER_PANELBAR) {
            i |= 65536;
        }
        if (DEBUG) {
            Log.d("KeyguardViewMediator", "adjustStatusBarLocked: mShowing=" + this.mShowing + " mOccluded=" + this.mOccluded + " isSecure=" + isSecure() + " force=" + z + " --> flags=0x" + Integer.toHexString(i));
        }
        this.mStatusBarManager.disable(i);
    }

    public final void handleReset() {
        synchronized (this) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleReset");
            }
            this.mKeyguardViewControllerLazy.get().reset(true);
        }
    }

    public final void handleVerifyUnlock() {
        Trace.beginSection("KeyguardViewMediator#handleVerifyUnlock");
        synchronized (this) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleVerifyUnlock");
            }
            setShowingLocked(true);
            this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
        }
        Trace.endSection();
    }

    public final void handleNotifyStartedGoingToSleep() {
        synchronized (this) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleNotifyStartedGoingToSleep");
            }
            this.mKeyguardViewControllerLazy.get().onStartedGoingToSleep();
        }
    }

    public final void handleNotifyFinishedGoingToSleep() {
        synchronized (this) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleNotifyFinishedGoingToSleep");
            }
            this.mKeyguardViewControllerLazy.get().onFinishedGoingToSleep();
        }
    }

    public final void handleNotifyStartedWakingUp() {
        Trace.beginSection("KeyguardViewMediator#handleMotifyStartedWakingUp");
        synchronized (this) {
            if (DEBUG) {
                Log.d("KeyguardViewMediator", "handleNotifyWakingUp");
            }
            this.mKeyguardViewControllerLazy.get().onStartedWakingUp();
        }
        Trace.endSection();
    }

    public final void resetKeyguardDonePendingLocked() {
        this.mKeyguardDonePending = false;
        this.mHandler.removeMessages(13);
    }

    public void onBootCompleted() {
        synchronized (this) {
            if (this.mContext.getResources().getBoolean(17891674)) {
                this.mUserSwitcherController.schedulePostBootGuestCreation();
            }
            this.mBootCompleted = true;
            adjustStatusBarLocked(false, true);
            if (this.mBootSendUserPresent) {
                sendUserPresentBroadcast();
            }
        }
    }

    public void onWakeAndUnlocking() {
        Trace.beginSection("KeyguardViewMediator#onWakeAndUnlocking");
        this.mWakeAndUnlocking = true;
        keyguardDone();
        Trace.endSection();
    }

    public KeyguardViewController registerCentralSurfaces(CentralSurfaces centralSurfaces, NotificationPanelViewController notificationPanelViewController, PanelExpansionStateManager panelExpansionStateManager, BiometricUnlockController biometricUnlockController, View view, KeyguardBypassController keyguardBypassController) {
        this.mCentralSurfaces = centralSurfaces;
        this.mKeyguardViewControllerLazy.get().registerCentralSurfaces(centralSurfaces, notificationPanelViewController, panelExpansionStateManager, biometricUnlockController, view, keyguardBypassController);
        return this.mKeyguardViewControllerLazy.get();
    }

    @Deprecated
    public void startKeyguardExitAnimation(long j, long j2) {
        startKeyguardExitAnimation(0, j, j2, (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (RemoteAnimationTarget[]) null, (IRemoteAnimationFinishedCallback) null);
    }

    public void startKeyguardExitAnimation(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        startKeyguardExitAnimation(i, 0, 0, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
    }

    public final void startKeyguardExitAnimation(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#startKeyguardExitAnimation");
        this.mInteractionJankMonitor.cancel(23);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(12, new StartKeyguardExitAnimParams(i, j, j2, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback)));
        Trace.endSection();
    }

    public void cancelKeyguardExitAnimation() {
        Trace.beginSection("KeyguardViewMediator#cancelKeyguardExitAnimation");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(19));
        Trace.endSection();
    }

    public ViewMediatorCallback getViewMediatorCallback() {
        return this.mViewMediatorCallback;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("  mSystemReady: ");
        printWriter.println(this.mSystemReady);
        printWriter.print("  mBootCompleted: ");
        printWriter.println(this.mBootCompleted);
        printWriter.print("  mBootSendUserPresent: ");
        printWriter.println(this.mBootSendUserPresent);
        printWriter.print("  mExternallyEnabled: ");
        printWriter.println(this.mExternallyEnabled);
        printWriter.print("  mShuttingDown: ");
        printWriter.println(this.mShuttingDown);
        printWriter.print("  mNeedToReshowWhenReenabled: ");
        printWriter.println(this.mNeedToReshowWhenReenabled);
        printWriter.print("  mShowing: ");
        printWriter.println(this.mShowing);
        printWriter.print("  mInputRestricted: ");
        printWriter.println(this.mInputRestricted);
        printWriter.print("  mOccluded: ");
        printWriter.println(this.mOccluded);
        printWriter.print("  mDelayedShowingSequence: ");
        printWriter.println(this.mDelayedShowingSequence);
        printWriter.print("  mExitSecureCallback: ");
        printWriter.println(this.mExitSecureCallback);
        printWriter.print("  mDeviceInteractive: ");
        printWriter.println(this.mDeviceInteractive);
        printWriter.print("  mGoingToSleep: ");
        printWriter.println(this.mGoingToSleep);
        printWriter.print("  mHiding: ");
        printWriter.println(this.mHiding);
        printWriter.print("  mDozing: ");
        printWriter.println(this.mDozing);
        printWriter.print("  mAodShowing: ");
        printWriter.println(this.mAodShowing);
        printWriter.print("  mWaitingUntilKeyguardVisible: ");
        printWriter.println(this.mWaitingUntilKeyguardVisible);
        printWriter.print("  mKeyguardDonePending: ");
        printWriter.println(this.mKeyguardDonePending);
        printWriter.print("  mHideAnimationRun: ");
        printWriter.println(this.mHideAnimationRun);
        printWriter.print("  mPendingReset: ");
        printWriter.println(this.mPendingReset);
        printWriter.print("  mPendingLock: ");
        printWriter.println(this.mPendingLock);
        printWriter.print("  wakeAndUnlocking: ");
        printWriter.println(this.mWakeAndUnlocking);
    }

    public void setDozing(boolean z) {
        if (z != this.mDozing) {
            this.mDozing = z;
            if (!z) {
                this.mAnimatingScreenOff = false;
            }
            if (this.mShowing || !this.mPendingLock || !this.mDozeParameters.canControlUnlockedScreenOff()) {
                setShowingLocked(this.mShowing);
            }
        }
    }

    public void onDozeAmountChanged(float f, float f2) {
        if (this.mAnimatingScreenOff && this.mDozing && f == 1.0f) {
            this.mAnimatingScreenOff = false;
            setShowingLocked(this.mShowing, true);
        }
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
    }

    public static class StartKeyguardExitAnimParams {
        public long fadeoutDuration;
        public RemoteAnimationTarget[] mApps;
        public IRemoteAnimationFinishedCallback mFinishedCallback;
        public RemoteAnimationTarget[] mNonApps;
        public int mTransit;
        public RemoteAnimationTarget[] mWallpapers;
        public long startTime;

        public StartKeyguardExitAnimParams(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            this.mTransit = i;
            this.startTime = j;
            this.fadeoutDuration = j2;
            this.mApps = remoteAnimationTargetArr;
            this.mWallpapers = remoteAnimationTargetArr2;
            this.mNonApps = remoteAnimationTargetArr3;
            this.mFinishedCallback = iRemoteAnimationFinishedCallback;
        }
    }

    public final void setShowingLocked(boolean z) {
        setShowingLocked(z, false);
    }

    public final void setShowingLocked(boolean z, boolean z2) {
        boolean z3 = true;
        boolean z4 = this.mDozing && !this.mWakeAndUnlocking;
        boolean z5 = this.mShowing;
        boolean z6 = z != z5 || z2;
        if (z == z5 && z4 == this.mAodShowing && !z2) {
            z3 = false;
        }
        this.mShowing = z;
        this.mAodShowing = z4;
        if (z6) {
            notifyDefaultDisplayCallbacks(z);
        }
        if (z3) {
            updateActivityLockScreenState(z, z4);
        }
    }

    public final void notifyDefaultDisplayCallbacks(boolean z) {
        DejankUtils.whitelistIpcs((Runnable) new KeyguardViewMediator$$ExternalSyntheticLambda4(this, z));
        updateInputRestrictedLocked();
        this.mUiBgExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDefaultDisplayCallbacks$10(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
            try {
                iKeyguardStateCallback.onShowingStateChanged(z, KeyguardUpdateMonitor.getCurrentUser());
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onShowingStateChanged", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDefaultDisplayCallbacks$11() {
        this.mTrustManager.reportKeyguardShowingChanged();
    }

    public final void notifyTrustedChangedLocked(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            try {
                this.mKeyguardStateCallbacks.get(size).onTrustedChanged(z);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call notifyTrustedChangedLocked", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(size);
                }
            }
        }
    }

    public final void setPendingLock(boolean z) {
        this.mPendingLock = z;
        Trace.traceCounter(4096, "pendingLock", z ? 1 : 0);
    }

    public void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) {
        synchronized (this) {
            this.mKeyguardStateCallbacks.add(iKeyguardStateCallback);
            try {
                iKeyguardStateCallback.onSimSecureStateChanged(this.mUpdateMonitor.isSimPinSecure());
                iKeyguardStateCallback.onShowingStateChanged(this.mShowing, KeyguardUpdateMonitor.getCurrentUser());
                iKeyguardStateCallback.onInputRestrictedStateChanged(this.mInputRestricted);
                iKeyguardStateCallback.onTrustedChanged(this.mUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser()));
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call to IKeyguardStateCallback", e);
            }
        }
    }

    public static class DismissMessage {
        public final IKeyguardDismissCallback mCallback;
        public final CharSequence mMessage;

        public DismissMessage(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
            this.mCallback = iKeyguardDismissCallback;
            this.mMessage = charSequence;
        }

        public IKeyguardDismissCallback getCallback() {
            return this.mCallback;
        }

        public CharSequence getMessage() {
            return this.mMessage;
        }
    }

    public class ActivityLaunchRemoteAnimationRunner extends IRemoteAnimationRunner.Stub {
        public final ActivityLaunchAnimator.Controller mActivityLaunchController;
        public ActivityLaunchAnimator.Runner mRunner;

        public ActivityLaunchRemoteAnimationRunner(ActivityLaunchAnimator.Controller controller) {
            this.mActivityLaunchController = controller;
        }

        public void onAnimationCancelled(boolean z) throws RemoteException {
            ActivityLaunchAnimator.Runner runner = this.mRunner;
            if (runner != null) {
                runner.onAnimationCancelled(z);
            }
        }

        public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
            ActivityLaunchAnimator.Runner createRunner = ((ActivityLaunchAnimator) KeyguardViewMediator.this.mActivityLaunchAnimator.get()).createRunner(this.mActivityLaunchController);
            this.mRunner = createRunner;
            createRunner.onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
        }
    }

    public class OccludeActivityLaunchRemoteAnimationRunner extends ActivityLaunchRemoteAnimationRunner {
        public OccludeActivityLaunchRemoteAnimationRunner(ActivityLaunchAnimator.Controller controller) {
            super(controller);
        }

        public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
            super.onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
            Log.d("KeyguardViewMediator", "OccludeAnimator#onAnimationStart. Set occluded = true.");
            KeyguardViewMediator.this.setOccluded(true, false);
        }

        public void onAnimationCancelled(boolean z) throws RemoteException {
            super.onAnimationCancelled(z);
            Log.d("KeyguardViewMediator", "Occlude animation cancelled by WM. Setting occluded state to: " + z);
            KeyguardViewMediator.this.setOccluded(z, false);
        }
    }
}
