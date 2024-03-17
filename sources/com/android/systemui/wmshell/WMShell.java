package com.android.systemui.wmshell;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.CoreStartable;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.nano.WmShellTraceProto;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedEventCallback;
import com.android.wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreen;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;

public final class WMShell extends CoreStartable implements CommandQueue.Callbacks, ProtoTraceable<SystemUiTraceProto> {
    public final CommandQueue mCommandQueue;
    public KeyguardStateController.Callback mCompatUIKeyguardCallback;
    public final Optional<CompatUI> mCompatUIOptional;
    public final ConfigurationController mConfigurationController;
    public final Optional<DragAndDrop> mDragAndDropOptional;
    public final Optional<HideDisplayCutout> mHideDisplayCutoutOptional;
    public boolean mIsSysUiStateValid;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final NavigationModeController mNavigationModeController;
    public KeyguardUpdateMonitorCallback mOneHandedKeyguardCallback;
    public final Optional<OneHanded> mOneHandedOptional;
    public KeyguardUpdateMonitorCallback mPipKeyguardCallback;
    public final Optional<Pip> mPipOptional;
    public final ProtoTracer mProtoTracer;
    public final ScreenLifecycle mScreenLifecycle;
    public final Optional<ShellCommandHandler> mShellCommandHandler;
    public KeyguardUpdateMonitorCallback mSplitScreenKeyguardCallback;
    public final Optional<SplitScreen> mSplitScreenOptional;
    public final Executor mSysUiMainExecutor;
    public final SysUiState mSysUiState;
    public final UserInfoController mUserInfoController;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public WakefulnessLifecycle.Observer mWakefulnessObserver;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public WMShell(Context context, Optional<Pip> optional, Optional<SplitScreen> optional2, Optional<OneHanded> optional3, Optional<HideDisplayCutout> optional4, Optional<ShellCommandHandler> optional5, Optional<CompatUI> optional6, Optional<DragAndDrop> optional7, CommandQueue commandQueue, ConfigurationController configurationController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, NavigationModeController navigationModeController, ScreenLifecycle screenLifecycle, SysUiState sysUiState, ProtoTracer protoTracer, WakefulnessLifecycle wakefulnessLifecycle, UserInfoController userInfoController, Executor executor) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mConfigurationController = configurationController;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mNavigationModeController = navigationModeController;
        this.mScreenLifecycle = screenLifecycle;
        this.mSysUiState = sysUiState;
        this.mPipOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mOneHandedOptional = optional3;
        this.mHideDisplayCutoutOptional = optional4;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mProtoTracer = protoTracer;
        this.mShellCommandHandler = optional5;
        this.mCompatUIOptional = optional6;
        this.mDragAndDropOptional = optional7;
        this.mUserInfoController = userInfoController;
        this.mSysUiMainExecutor = executor;
    }

    public void start() {
        this.mProtoTracer.add(this);
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mPipOptional.ifPresent(new WMShell$$ExternalSyntheticLambda3(this));
        this.mSplitScreenOptional.ifPresent(new WMShell$$ExternalSyntheticLambda4(this));
        this.mOneHandedOptional.ifPresent(new WMShell$$ExternalSyntheticLambda5(this));
        this.mHideDisplayCutoutOptional.ifPresent(new WMShell$$ExternalSyntheticLambda6(this));
        this.mCompatUIOptional.ifPresent(new WMShell$$ExternalSyntheticLambda7(this));
        this.mDragAndDropOptional.ifPresent(new WMShell$$ExternalSyntheticLambda8(this));
    }

    @VisibleForTesting
    public void initPip(final Pip pip) {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() {
            public void showPictureInPictureMenu() {
                pip.showPictureInPictureMenu();
            }
        });
        AnonymousClass2 r0 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                pip.onKeyguardVisibilityChanged(z, WMShell.this.mKeyguardStateController.isAnimatingBetweenKeyguardAndSurfaceBehind());
            }

            public void onKeyguardDismissAnimationFinished() {
                pip.onKeyguardDismissAnimationFinished();
            }
        };
        this.mPipKeyguardCallback = r0;
        this.mKeyguardUpdateMonitor.registerCallback(r0);
        this.mSysUiState.addCallback(new WMShell$$ExternalSyntheticLambda0(this, pip));
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                pip.onConfigurationChanged(configuration);
            }

            public void onDensityOrFontScaleChanged() {
                pip.onDensityOrFontScaleChanged();
            }

            public void onThemeChanged() {
                pip.onOverlayChanged();
            }
        });
        this.mUserInfoController.addCallback(new WMShell$$ExternalSyntheticLambda1(pip));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initPip$0(Pip pip, int i) {
        boolean z = (8440396 & i) == 0;
        this.mIsSysUiStateValid = z;
        pip.onSystemUiStateChanged(z, i);
    }

    @VisibleForTesting
    public void initSplitScreen(final SplitScreen splitScreen) {
        AnonymousClass4 r0 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                splitScreen.onKeyguardVisibilityChanged(z);
            }
        };
        this.mSplitScreenKeyguardCallback = r0;
        this.mKeyguardUpdateMonitor.registerCallback(r0);
        this.mWakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                splitScreen.onFinishedWakingUp();
            }
        });
    }

    @VisibleForTesting
    public void initOneHanded(final OneHanded oneHanded) {
        oneHanded.registerTransitionCallback(new OneHandedTransitionCallback() {
            public void onStartTransition(boolean z) {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$6$$ExternalSyntheticLambda2(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onStartTransition$0() {
                WMShell.this.mSysUiState.setFlag(65536, true).commitUpdate(0);
            }

            public void onStartFinished(Rect rect) {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$6$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onStartFinished$1() {
                WMShell.this.mSysUiState.setFlag(65536, true).commitUpdate(0);
            }

            public void onStopFinished(Rect rect) {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$6$$ExternalSyntheticLambda1(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onStopFinished$2() {
                WMShell.this.mSysUiState.setFlag(65536, false).commitUpdate(0);
            }
        });
        oneHanded.registerEventCallback(new OneHandedEventCallback() {
            public void notifyExpandNotification() {
                WMShell.this.mSysUiMainExecutor.execute(new WMShell$7$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$notifyExpandNotification$0() {
                WMShell.this.mCommandQueue.handleSystemKey(281);
            }
        });
        AnonymousClass8 r0 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                oneHanded.onKeyguardVisibilityChanged(z);
                oneHanded.stopOneHanded();
            }

            public void onUserSwitchComplete(int i) {
                oneHanded.onUserSwitch(i);
            }
        };
        this.mOneHandedKeyguardCallback = r0;
        this.mKeyguardUpdateMonitor.registerCallback(r0);
        AnonymousClass9 r02 = new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                oneHanded.setLockedDisabled(false, false);
            }

            public void onStartedGoingToSleep() {
                oneHanded.stopOneHanded();
                oneHanded.setLockedDisabled(true, false);
            }
        };
        this.mWakefulnessObserver = r02;
        this.mWakefulnessLifecycle.addObserver(r02);
        this.mScreenLifecycle.addObserver(new ScreenLifecycle.Observer() {
            public void onScreenTurningOff() {
                oneHanded.stopOneHanded(7);
            }
        });
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() {
            public void onCameraLaunchGestureDetected(int i) {
                oneHanded.stopOneHanded();
            }

            public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
                if (i == 0 && (i2 & 2) != 0) {
                    oneHanded.stopOneHanded(3);
                }
            }
        });
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                oneHanded.onConfigChanged(configuration);
            }
        });
    }

    @VisibleForTesting
    public void initHideDisplayCutout(final HideDisplayCutout hideDisplayCutout) {
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                hideDisplayCutout.onConfigurationChanged(configuration);
            }
        });
    }

    @VisibleForTesting
    public void initCompatUi(final CompatUI compatUI) {
        AnonymousClass14 r0 = new KeyguardStateController.Callback() {
            public void onKeyguardShowingChanged() {
                compatUI.onKeyguardShowingChanged(WMShell.this.mKeyguardStateController.isShowing());
            }
        };
        this.mCompatUIKeyguardCallback = r0;
        this.mKeyguardStateController.addCallback(r0);
    }

    public void initDragAndDrop(final DragAndDrop dragAndDrop) {
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                dragAndDrop.onConfigChanged(configuration);
            }

            public void onThemeChanged() {
                dragAndDrop.onThemeChanged();
            }
        });
    }

    public void writeToProto(SystemUiTraceProto systemUiTraceProto) {
        if (systemUiTraceProto.wmShell == null) {
            systemUiTraceProto.wmShell = new WmShellTraceProto();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        if ((!this.mShellCommandHandler.isPresent() || !this.mShellCommandHandler.get().handleCommand(strArr, printWriter)) && !handleLoggingCommand(strArr, printWriter)) {
            this.mShellCommandHandler.ifPresent(new WMShell$$ExternalSyntheticLambda2(printWriter));
        }
    }

    public void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        PrintWriter printWriter = new PrintWriter(new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor));
        handleLoggingCommand(strArr, printWriter);
        printWriter.flush();
        printWriter.close();
    }

    public final boolean handleLoggingCommand(String[] strArr, PrintWriter printWriter) {
        ShellProtoLogImpl singleInstance = ShellProtoLogImpl.getSingleInstance();
        int i = 0;
        while (i < strArr.length) {
            String str = strArr[i];
            str.hashCode();
            if (str.equals("enable-text")) {
                String[] strArr2 = (String[]) Arrays.copyOfRange(strArr, i + 1, strArr.length);
                if (singleInstance.startTextLogging(strArr2, printWriter) == 0) {
                    printWriter.println("Starting logging on groups: " + Arrays.toString(strArr2));
                }
                return true;
            } else if (!str.equals("disable-text")) {
                i++;
            } else {
                String[] strArr3 = (String[]) Arrays.copyOfRange(strArr, i + 1, strArr.length);
                if (singleInstance.stopTextLogging(strArr3, printWriter) == 0) {
                    printWriter.println("Stopping logging on groups: " + Arrays.toString(strArr3));
                }
                return true;
            }
        }
        return false;
    }
}
