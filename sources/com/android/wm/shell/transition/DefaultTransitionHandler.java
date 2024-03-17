package com.android.wm.shell.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.hardware.HardwareBuffer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.AttributeCache;
import com.android.internal.policy.TransitionAnimation;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda3;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;

public class DefaultTransitionHandler implements Transitions.TransitionHandler {
    public static boolean sDisableCustomTaskAnimationProperty = SystemProperties.getBoolean("persist.wm.disable_custom_task_animation", true);
    public final ShellExecutor mAnimExecutor;
    public final ArrayMap<IBinder, ArrayList<Animator>> mAnimations = new ArrayMap<>();
    public final Context mContext;
    public final int mCurrentUserId;
    public final DevicePolicyManager mDevicePolicyManager;
    public final DisplayController mDisplayController;
    public BroadcastReceiver mEnterpriseResourceUpdatedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("android.app.extra.RESOURCE_TYPE", -1) == 1) {
                DefaultTransitionHandler.this.updateEnterpriseThumbnailDrawable();
            }
        }
    };
    public Drawable mEnterpriseThumbnailDrawable;
    public final Rect mInsets = new Rect(0, 0, 0, 0);
    public final ShellExecutor mMainExecutor;
    public ScreenRotationAnimation mRotationAnimation;
    public final CounterRotatorHelper mRotator = new CounterRotatorHelper();
    public final SurfaceSession mSurfaceSession = new SurfaceSession();
    public final TransactionPool mTransactionPool;
    public final TransitionAnimation mTransitionAnimation;
    public float mTransitionAnimationScaleSetting = 1.0f;

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        return null;
    }

    public DefaultTransitionHandler(DisplayController displayController, TransactionPool transactionPool, Context context, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2) {
        this.mDisplayController = displayController;
        this.mTransactionPool = transactionPool;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAnimExecutor = shellExecutor2;
        this.mTransitionAnimation = new TransitionAnimation(context, false, "ShellTransitions");
        this.mCurrentUserId = UserHandle.myUserId();
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        updateEnterpriseThumbnailDrawable();
        context.registerReceiver(this.mEnterpriseResourceUpdatedReceiver, new IntentFilter("android.app.action.DEVICE_POLICY_RESOURCE_UPDATED"), (String) null, handler);
        AttributeCache.init(context);
    }

    public final void updateEnterpriseThumbnailDrawable() {
        this.mEnterpriseThumbnailDrawable = this.mDevicePolicyManager.getResources().getDrawable("WORK_PROFILE_ICON", "OUTLINE", "PROFILE_SWITCH_ANIMATION", new DefaultTransitionHandler$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Drawable lambda$updateEnterpriseThumbnailDrawable$0() {
        return this.mContext.getDrawable(17302399);
    }

    @VisibleForTesting
    public static boolean isRotationSeamless(TransitionInfo transitionInfo, DisplayController displayController) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 649960056, 0, "Display is changing, check if it should be seamless.", (Object[]) null);
        }
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if (change.getMode() == 6 && change.getEndRotation() != change.getStartRotation()) {
                if ((change.getFlags() & 32) != 0) {
                    if ((change.getFlags() & 128) != 0) {
                        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 42311280, 0, "  display has system alert windows, so not seamless.", (Object[]) null);
                        }
                        return false;
                    }
                    z2 = change.getRotationAnimation() == 3;
                } else if ((change.getFlags() & 2) != 0) {
                    if (change.getRotationAnimation() != 3) {
                        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1015274864, 0, "  wallpaper is participating but isn't seamless.", (Object[]) null);
                        }
                        return false;
                    }
                } else if (change.getTaskInfo() == null) {
                    continue;
                } else if (change.getRotationAnimation() != 3) {
                    if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1915000700, 0, "  task %s isn't requesting seamless, so not seamless.", String.valueOf(change.getTaskInfo().taskId));
                    }
                    return false;
                } else if (!z3) {
                    DisplayLayout displayLayout = displayController.getDisplayLayout(change.getTaskInfo().displayId);
                    int upsideDownRotation = displayLayout.getUpsideDownRotation();
                    if (change.getStartRotation() == upsideDownRotation || change.getEndRotation() == upsideDownRotation) {
                        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1167817788, 0, "  rotation involves upside-down portrait, so not seamless.", (Object[]) null);
                        }
                        return false;
                    } else if (displayLayout.allowSeamlessRotationDespiteNavBarMoving() || (displayLayout.navigationBarCanMove() && change.getStartAbsBounds().width() != change.getStartAbsBounds().height())) {
                        z = true;
                        z3 = true;
                    } else {
                        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1167654715, 0, "  nav bar changes sides, so not seamless.", (Object[]) null);
                        }
                        return false;
                    }
                } else {
                    z = true;
                }
            }
        }
        if (!z && !z2) {
            return false;
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1215677233, 0, "  Rotation IS seamless.", (Object[]) null);
        }
        return true;
    }

    public final int getRotationAnimation(TransitionInfo transitionInfo) {
        int i = 0;
        while (i < transitionInfo.getChanges().size()) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
            if (change.getMode() != 6 || change.getEndRotation() == change.getStartRotation() || change.getTaskInfo() == null) {
                i++;
            } else {
                int rotationAnimation = change.getRotationAnimation();
                if (rotationAnimation == -1 || rotationAnimation == 3) {
                    return 0;
                }
                return rotationAnimation;
            }
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:102:0x0287  */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x029f  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x02c4  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01c5  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x021c  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x025b  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x025f  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x0278  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x027f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startAnimation(android.os.IBinder r30, android.window.TransitionInfo r31, android.view.SurfaceControl.Transaction r32, android.view.SurfaceControl.Transaction r33, com.android.wm.shell.transition.Transitions.TransitionFinishCallback r34) {
        /*
            r29 = this;
            r8 = r29
            r9 = r30
            r10 = r31
            r11 = r32
            r12 = r33
            r0 = r34
            boolean r1 = com.android.wm.shell.protolog.ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled
            r13 = 0
            r14 = 1
            if (r1 == 0) goto L_0x0024
            java.lang.String r1 = java.lang.String.valueOf(r31)
            com.android.wm.shell.protolog.ShellProtoLogGroup r2 = com.android.wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TRANSITIONS
            r3 = -146110597(0xfffffffff74a877b, float:-4.1077806E33)
            java.lang.Object[] r4 = new java.lang.Object[r14]
            r4[r13] = r1
            java.lang.String r1 = "start default transition animation, info = %s"
            com.android.wm.shell.protolog.ShellProtoLogImpl.v(r2, r3, r13, r1, r4)
        L_0x0024:
            int r1 = r31.getType()
            r2 = 11
            if (r1 != r2) goto L_0x003a
            boolean r1 = r31.isKeyguardGoingAway()
            if (r1 != 0) goto L_0x003a
            r32.apply()
            r1 = 0
            r0.onTransitionFinished(r1, r1)
            return r14
        L_0x003a:
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<android.animation.Animator>> r1 = r8.mAnimations
            boolean r1 = r1.containsKey(r9)
            if (r1 != 0) goto L_0x033d
            java.util.ArrayList r15 = new java.util.ArrayList
            r15.<init>()
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<android.animation.Animator>> r1 = r8.mAnimations
            r1.put(r9, r15)
            com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda0 r7 = new com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda0
            r7.<init>(r8, r15, r9, r0)
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            int r5 = getWallpaperTransitType(r31)
            java.util.List r0 = r31.getChanges()
            int r0 = r0.size()
            int r0 = r0 - r14
            r4 = r0
            r3 = r13
        L_0x0065:
            if (r4 < 0) goto L_0x02ec
            java.util.List r0 = r31.getChanges()
            java.lang.Object r0 = r0.get(r4)
            r2 = r0
            android.window.TransitionInfo$Change r2 = (android.window.TransitionInfo.Change) r2
            android.app.ActivityManager$RunningTaskInfo r0 = r2.getTaskInfo()
            if (r0 == 0) goto L_0x007a
            r0 = r14
            goto L_0x007b
        L_0x007a:
            r0 = r13
        L_0x007b:
            int r1 = r2.getMode()
            r13 = 2
            r14 = 6
            if (r1 != r14) goto L_0x00f9
            int r1 = r2.getFlags()
            r1 = r1 & 32
            if (r1 == 0) goto L_0x00f9
            int r1 = r31.getType()
            if (r1 != r14) goto L_0x00e6
            com.android.wm.shell.common.DisplayController r1 = r8.mDisplayController
            boolean r1 = isRotationSeamless(r10, r1)
            int r14 = r8.getRotationAnimation(r10)
            if (r1 != 0) goto L_0x00d8
            if (r14 == r13) goto L_0x00d8
            com.android.wm.shell.transition.ScreenRotationAnimation r13 = new com.android.wm.shell.transition.ScreenRotationAnimation
            android.content.Context r1 = r8.mContext
            android.view.SurfaceSession r0 = r8.mSurfaceSession
            r16 = r3
            com.android.wm.shell.common.TransactionPool r3 = r8.mTransactionPool
            android.view.SurfaceControl r17 = r31.getRootLeash()
            r18 = r0
            r0 = r13
            r25 = r2
            r2 = r18
            r9 = r16
            r26 = r4
            r4 = r32
            r9 = r5
            r5 = r25
            r27 = r6
            r6 = r17
            r28 = r7
            r7 = r14
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            r8.mRotationAnimation = r13
            float r5 = r8.mTransitionAnimationScaleSetting
            com.android.wm.shell.common.ShellExecutor r6 = r8.mMainExecutor
            com.android.wm.shell.common.ShellExecutor r7 = r8.mAnimExecutor
            r2 = r13
            r3 = r15
            r4 = r28
            r2.startAnimation(r3, r4, r5, r6, r7)
            goto L_0x02d5
        L_0x00d8:
            r25 = r2
            r16 = r3
            r26 = r4
            r9 = r5
            r27 = r6
            r28 = r7
            r14 = r25
            goto L_0x0104
        L_0x00e6:
            r25 = r2
            r16 = r3
            r26 = r4
            r9 = r5
            r27 = r6
            r28 = r7
            com.android.wm.shell.transition.CounterRotatorHelper r1 = r8.mRotator
            r14 = r25
            r1.handleClosingChanges(r10, r11, r14)
            goto L_0x0103
        L_0x00f9:
            r14 = r2
            r16 = r3
            r26 = r4
            r9 = r5
            r27 = r6
            r28 = r7
        L_0x0103:
            r1 = 0
        L_0x0104:
            int r2 = r14.getMode()
            r3 = 6
            if (r2 != r3) goto L_0x01b5
            if (r0 == 0) goto L_0x0163
            android.window.WindowContainerToken r2 = r14.getParent()
            if (r2 == 0) goto L_0x0163
            android.window.WindowContainerToken r2 = r14.getParent()
            android.window.TransitionInfo$Change r2 = r10.getChange(r2)
            android.app.ActivityManager$RunningTaskInfo r2 = r2.getTaskInfo()
            if (r2 == 0) goto L_0x0163
            android.app.ActivityManager$RunningTaskInfo r0 = r14.getTaskInfo()
            android.graphics.Point r0 = r0.positionInParent
            android.view.SurfaceControl r1 = r14.getLeash()
            int r2 = r0.x
            float r2 = (float) r2
            int r0 = r0.y
            float r0 = (float) r0
            r11.setPosition(r1, r2, r0)
            android.graphics.Rect r0 = r14.getEndAbsBounds()
            android.window.WindowContainerToken r1 = r14.getParent()
            android.window.TransitionInfo$Change r1 = r10.getChange(r1)
            android.graphics.Rect r1 = r1.getEndAbsBounds()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x02d5
            android.view.SurfaceControl r0 = r14.getLeash()
            android.graphics.Rect r1 = r14.getEndAbsBounds()
            int r1 = r1.width()
            android.graphics.Rect r2 = r14.getEndAbsBounds()
            int r2 = r2.height()
            r11.setWindowCrop(r0, r1, r2)
            goto L_0x02d5
        L_0x0163:
            if (r0 == 0) goto L_0x0175
            android.app.ActivityManager$RunningTaskInfo r2 = r14.getTaskInfo()
            android.content.res.Configuration r2 = r2.configuration
            android.app.WindowConfiguration r2 = r2.windowConfiguration
            int r2 = r2.getWindowingMode()
            if (r2 != r13) goto L_0x0175
            goto L_0x02d5
        L_0x0175:
            android.view.SurfaceControl r2 = r14.getLeash()
            android.graphics.Rect r3 = r14.getEndAbsBounds()
            int r3 = r3.left
            android.graphics.Point r4 = r31.getRootOffset()
            int r4 = r4.x
            int r3 = r3 - r4
            float r3 = (float) r3
            android.graphics.Rect r4 = r14.getEndAbsBounds()
            int r4 = r4.top
            android.graphics.Point r5 = r31.getRootOffset()
            int r5 = r5.y
            int r4 = r4 - r5
            float r4 = (float) r4
            r11.setPosition(r2, r3, r4)
            if (r1 == 0) goto L_0x019c
            goto L_0x02d5
        L_0x019c:
            if (r0 == 0) goto L_0x01b5
            android.view.SurfaceControl r1 = r14.getLeash()
            android.graphics.Rect r2 = r14.getEndAbsBounds()
            int r2 = r2.width()
            android.graphics.Rect r3 = r14.getEndAbsBounds()
            int r3 = r3.height()
            r11.setWindowCrop(r1, r2, r3)
        L_0x01b5:
            boolean r1 = android.window.TransitionInfo.isIndependent(r14, r10)
            if (r1 != 0) goto L_0x01bd
            goto L_0x02d5
        L_0x01bd:
            android.view.animation.Animation r3 = r8.loadAnimation(r10, r14, r9)
            if (r3 == 0) goto L_0x02d5
            if (r0 == 0) goto L_0x01f9
            int r1 = r31.getType()
            r2 = 4
            r4 = 1
            if (r1 == r4) goto L_0x01d7
            if (r1 == r13) goto L_0x01d7
            r4 = 3
            if (r1 == r4) goto L_0x01d7
            if (r1 != r2) goto L_0x01d5
            goto L_0x01d7
        L_0x01d5:
            r4 = 0
            goto L_0x01d8
        L_0x01d7:
            r4 = 1
        L_0x01d8:
            int r1 = r14.getFlags()
            r1 = r1 & r2
            if (r1 == 0) goto L_0x01e1
            r1 = 1
            goto L_0x01e2
        L_0x01e1:
            r1 = 0
        L_0x01e2:
            if (r4 == 0) goto L_0x01f9
            if (r1 != 0) goto L_0x01f9
            if (r9 != 0) goto L_0x01f9
            android.app.ActivityThread r1 = android.app.ActivityThread.currentActivityThread()
            android.app.ContextImpl r1 = r1.getSystemUiContext()
            r2 = 17170985(0x1060229, float:2.4613463E-38)
            int r1 = r1.getColor(r2)
            r16 = r1
        L_0x01f9:
            boolean r1 = r3.hasRoundedCorners()
            r2 = 0
            if (r1 == 0) goto L_0x0215
            if (r0 == 0) goto L_0x0215
            com.android.wm.shell.common.DisplayController r1 = r8.mDisplayController
            android.app.ActivityManager$RunningTaskInfo r4 = r14.getTaskInfo()
            int r4 = r4.displayId
            android.content.Context r1 = r1.getDisplayContext(r4)
            if (r1 != 0) goto L_0x0211
            goto L_0x0215
        L_0x0211:
            float r2 = com.android.internal.policy.ScreenDecorationsUtils.getWindowCornerRadius(r1)
        L_0x0215:
            r13 = r2
            boolean r1 = r3.getShowBackdrop()
            if (r1 == 0) goto L_0x0247
            android.window.TransitionInfo$AnimationOptions r1 = r31.getAnimationOptions()
            int r1 = r1.getBackgroundColor()
            if (r1 == 0) goto L_0x0231
            android.window.TransitionInfo$AnimationOptions r1 = r31.getAnimationOptions()
            int r1 = r1.getBackgroundColor()
        L_0x022e:
            r25 = r1
            goto L_0x0249
        L_0x0231:
            int r1 = r3.getBackdropColor()
            if (r1 == 0) goto L_0x023c
            int r1 = r3.getBackdropColor()
            goto L_0x022e
        L_0x023c:
            int r1 = r14.getBackgroundColor()
            if (r1 == 0) goto L_0x0247
            int r1 = r14.getBackgroundColor()
            goto L_0x022e
        L_0x0247:
            r25 = r16
        L_0x0249:
            if (r0 != 0) goto L_0x026b
            boolean r0 = r3.hasExtension()
            if (r0 == 0) goto L_0x026b
            int r0 = r14.getMode()
            boolean r0 = com.android.wm.shell.transition.Transitions.isOpeningType(r0)
            if (r0 != 0) goto L_0x025f
            r8.lambda$startAnimation$2(r14, r3, r11, r12)
            goto L_0x026b
        L_0x025f:
            com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda1 r0 = new com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda1
            r0.<init>(r8, r14, r3, r12)
            r7 = r27
            r7.add(r0)
            r4 = 1
            goto L_0x026e
        L_0x026b:
            r7 = r27
            r4 = 0
        L_0x026e:
            int r0 = r14.getMode()
            boolean r0 = com.android.wm.shell.transition.Transitions.isClosingType(r0)
            if (r0 == 0) goto L_0x027f
            com.android.wm.shell.transition.CounterRotatorHelper r0 = r8.mRotator
            android.graphics.Rect r0 = r0.getEndBoundsInStartRotation(r14)
            goto L_0x0283
        L_0x027f:
            android.graphics.Rect r0 = r14.getEndAbsBounds()
        L_0x0283:
            r24 = r0
            if (r4 == 0) goto L_0x029f
            com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda2 r6 = new com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda2
            r0 = r6
            r1 = r29
            r2 = r15
            r4 = r14
            r5 = r28
            r27 = r9
            r9 = r6
            r6 = r13
            r10 = r7
            r7 = r24
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            r10.add(r9)
            r6 = r15
            goto L_0x02be
        L_0x029f:
            r10 = r7
            r27 = r9
            android.view.SurfaceControl r17 = r14.getLeash()
            com.android.wm.shell.common.TransactionPool r0 = r8.mTransactionPool
            com.android.wm.shell.common.ShellExecutor r1 = r8.mMainExecutor
            com.android.wm.shell.common.ShellExecutor r2 = r8.mAnimExecutor
            r22 = 0
            r6 = r15
            r16 = r3
            r18 = r28
            r19 = r0
            r20 = r1
            r21 = r2
            r23 = r13
            startSurfaceAnimation(r15, r16, r17, r18, r19, r20, r21, r22, r23, r24)
        L_0x02be:
            android.window.TransitionInfo$AnimationOptions r0 = r31.getAnimationOptions()
            if (r0 == 0) goto L_0x02d2
            android.window.TransitionInfo$AnimationOptions r4 = r31.getAnimationOptions()
            r0 = r29
            r1 = r6
            r2 = r28
            r3 = r14
            r5 = r13
            r0.attachThumbnail(r1, r2, r3, r4, r5)
        L_0x02d2:
            r3 = r25
            goto L_0x02dc
        L_0x02d5:
            r6 = r15
            r10 = r27
            r27 = r9
            r3 = r16
        L_0x02dc:
            int r4 = r26 + -1
            r9 = r30
            r15 = r6
            r6 = r10
            r5 = r27
            r7 = r28
            r13 = 0
            r14 = 1
            r10 = r31
            goto L_0x0065
        L_0x02ec:
            r16 = r3
            r10 = r6
            r28 = r7
            if (r16 == 0) goto L_0x02fc
            android.view.SurfaceControl r0 = r31.getRootLeash()
            r13 = r16
            r8.addBackgroundToTransition(r0, r13, r11, r12)
        L_0x02fc:
            int r0 = r10.size()
            if (r0 <= 0) goto L_0x0304
            r13 = 1
            goto L_0x0305
        L_0x0304:
            r13 = 0
        L_0x0305:
            r11.apply(r13)
            java.util.Iterator r0 = r10.iterator()
        L_0x030c:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x032a
            java.lang.Object r1 = r0.next()
            java.util.function.Consumer r1 = (java.util.function.Consumer) r1
            com.android.wm.shell.common.TransactionPool r2 = r8.mTransactionPool
            android.view.SurfaceControl$Transaction r2 = r2.acquire()
            r1.accept(r2)
            r2.apply()
            com.android.wm.shell.common.TransactionPool r1 = r8.mTransactionPool
            r1.release(r2)
            goto L_0x030c
        L_0x032a:
            com.android.wm.shell.transition.CounterRotatorHelper r0 = r8.mRotator
            r0.cleanUp(r12)
            android.window.TransitionMetrics r0 = android.window.TransitionMetrics.getInstance()
            r1 = r30
            r0.reportAnimationStart(r1)
            r28.run()
            r0 = 1
            return r0
        L_0x033d:
            r1 = r9
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Got a duplicate startAnimation call for "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.transition.DefaultTransitionHandler.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.view.SurfaceControl$Transaction, com.android.wm.shell.transition.Transitions$TransitionFinishCallback):boolean");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$1(ArrayList arrayList, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (arrayList.isEmpty()) {
            ScreenRotationAnimation screenRotationAnimation = this.mRotationAnimation;
            if (screenRotationAnimation != null) {
                screenRotationAnimation.kill();
                this.mRotationAnimation = null;
            }
            this.mAnimations.remove(iBinder);
            transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$3(ArrayList arrayList, Animation animation, TransitionInfo.Change change, Runnable runnable, float f, Rect rect, SurfaceControl.Transaction transaction) {
        startSurfaceAnimation(arrayList, animation, change.getLeash(), runnable, this.mTransactionPool, this.mMainExecutor, this.mAnimExecutor, (Point) null, f, rect);
    }

    /* renamed from: edgeExtendWindow */
    public final void lambda$startAnimation$2(TransitionInfo.Change change, Animation animation, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        Animation animation2 = animation;
        Transformation transformation = new Transformation();
        animation2.getTransformationAt(0.0f, transformation);
        Transformation transformation2 = new Transformation();
        animation2.getTransformationAt(1.0f, transformation2);
        Insets min = Insets.min(transformation.getInsets(), transformation2.getInsets());
        int max = Math.max(change.getStartAbsBounds().height(), change.getEndAbsBounds().height());
        int max2 = Math.max(change.getStartAbsBounds().width(), change.getEndAbsBounds().width());
        if (min.left < 0) {
            createExtensionSurface(change.getLeash(), new Rect(0, 0, 1, max), new Rect(0, 0, -min.left, max), min.left, 0, "Left Edge Extension", transaction, transaction2);
        }
        if (min.top < 0) {
            createExtensionSurface(change.getLeash(), new Rect(0, 0, max2, 1), new Rect(0, 0, max2, -min.top), 0, min.top, "Top Edge Extension", transaction, transaction2);
        }
        if (min.right < 0) {
            createExtensionSurface(change.getLeash(), new Rect(max2 - 1, 0, max2, max), new Rect(0, 0, -min.right, max), max2, 0, "Right Edge Extension", transaction, transaction2);
        }
        if (min.bottom < 0) {
            createExtensionSurface(change.getLeash(), new Rect(0, max - 1, max2, max), new Rect(0, 0, max2, -min.bottom), min.left, max, "Bottom Edge Extension", transaction, transaction2);
        }
    }

    public final SurfaceControl createExtensionSurface(SurfaceControl surfaceControl, Rect rect, Rect rect2, int i, int i2, String str, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        SurfaceControl build = new SurfaceControl.Builder().setName(str).setParent(surfaceControl).setHidden(true).setCallsite("DefaultTransitionHandler#startAnimation").setOpaque(true).setBufferSize(rect2.width(), rect2.height()).build();
        SurfaceControl.ScreenshotHardwareBuffer captureLayers = SurfaceControl.captureLayers(new SurfaceControl.LayerCaptureArgs.Builder(surfaceControl).setSourceCrop(rect).setFrameScale(1.0f).setPixelFormat(1).setChildrenOnly(true).setAllowProtected(true).build());
        if (captureLayers == null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.e(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 457420030, 0, "Failed to capture edge of window.", (Object[]) null);
            }
            return null;
        }
        Bitmap asBitmap = captureLayers.asBitmap();
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(asBitmap, tileMode, tileMode);
        Paint paint = new Paint();
        paint.setShader(bitmapShader);
        Surface surface = new Surface(build);
        Canvas lockHardwareCanvas = surface.lockHardwareCanvas();
        lockHardwareCanvas.drawRect(rect2, paint);
        surface.unlockCanvasAndPost(lockHardwareCanvas);
        surface.release();
        transaction.setLayer(build, Integer.MIN_VALUE);
        transaction.setPosition(build, (float) i, (float) i2);
        transaction.setVisibility(build, true);
        transaction2.remove(build);
        return build;
    }

    public final void addBackgroundToTransition(SurfaceControl surfaceControl, int i, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        Color valueOf = Color.valueOf(i);
        float[] fArr = {valueOf.red(), valueOf.green(), valueOf.blue()};
        SurfaceControl build = new SurfaceControl.Builder().setName("Animation Background").setParent(surfaceControl).setColorLayer().setOpaque(true).build();
        transaction.setLayer(build, Integer.MIN_VALUE).setColor(build, fArr).show(build);
        transaction2.remove(build);
    }

    public void setAnimScaleSetting(float f) {
        this.mTransitionAnimationScaleSetting = f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:121:0x018e  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x01c7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.view.animation.Animation loadAnimation(android.window.TransitionInfo r21, android.window.TransitionInfo.Change r22, int r23) {
        /*
            r20 = this;
            r0 = r20
            r6 = r23
            int r5 = r21.getType()
            int r1 = r21.getFlags()
            int r2 = r22.getMode()
            int r3 = r22.getFlags()
            boolean r4 = com.android.wm.shell.transition.Transitions.isOpeningType(r5)
            boolean r7 = com.android.wm.shell.transition.Transitions.isOpeningType(r2)
            android.app.ActivityManager$RunningTaskInfo r8 = r22.getTaskInfo()
            r10 = 1
            if (r8 == 0) goto L_0x0025
            r8 = r10
            goto L_0x0026
        L_0x0025:
            r8 = 0
        L_0x0026:
            android.window.TransitionInfo$AnimationOptions r11 = r21.getAnimationOptions()
            if (r11 == 0) goto L_0x0031
            int r12 = r11.getType()
            goto L_0x0032
        L_0x0031:
            r12 = 0
        L_0x0032:
            if (r8 == 0) goto L_0x003b
            boolean r13 = sDisableCustomTaskAnimationProperty
            if (r13 != 0) goto L_0x0039
            goto L_0x003b
        L_0x0039:
            r13 = 0
            goto L_0x003c
        L_0x003b:
            r13 = r10
        L_0x003c:
            boolean r14 = com.android.wm.shell.transition.Transitions.isClosingType(r2)
            if (r14 == 0) goto L_0x004b
            com.android.wm.shell.transition.CounterRotatorHelper r14 = r0.mRotator
            r15 = r22
            android.graphics.Rect r14 = r14.getEndBoundsInStartRotation(r15)
            goto L_0x0051
        L_0x004b:
            r15 = r22
            android.graphics.Rect r14 = r22.getEndAbsBounds()
        L_0x0051:
            boolean r15 = r21.isKeyguardGoingAway()
            r16 = 0
            if (r15 == 0) goto L_0x0066
            com.android.internal.policy.TransitionAnimation r2 = r0.mTransitionAnimation
            r3 = r3 & r10
            if (r3 == 0) goto L_0x0060
            r9 = r10
            goto L_0x0061
        L_0x0060:
            r9 = 0
        L_0x0061:
            android.view.animation.Animation r16 = r2.loadKeyguardExitAnimation(r1, r9)
            goto L_0x0070
        L_0x0066:
            r1 = 9
            if (r5 != r1) goto L_0x0074
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.view.animation.Animation r16 = r1.loadKeyguardUnoccludeAnimation()
        L_0x0070:
            r1 = r16
            goto L_0x01c5
        L_0x0074:
            r15 = r3 & 16
            if (r15 == 0) goto L_0x0088
            if (r4 == 0) goto L_0x0081
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.view.animation.Animation r16 = r1.loadVoiceActivityOpenAnimation(r7)
            goto L_0x0070
        L_0x0081:
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.view.animation.Animation r16 = r1.loadVoiceActivityExitAnimation(r7)
            goto L_0x0070
        L_0x0088:
            r15 = 6
            if (r2 != r15) goto L_0x0099
            android.view.animation.AlphaAnimation r1 = new android.view.animation.AlphaAnimation
            r2 = 1065353216(0x3f800000, float:1.0)
            r1.<init>(r2, r2)
            r2 = 336(0x150, double:1.66E-321)
            r1.setDuration(r2)
            goto L_0x01c5
        L_0x0099:
            r2 = 5
            if (r5 != r2) goto L_0x00a5
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.graphics.Rect r2 = r0.mInsets
            android.view.animation.Animation r16 = r1.createRelaunchAnimation(r14, r2, r14)
            goto L_0x0070
        L_0x00a5:
            if (r12 != r10) goto L_0x00c5
            if (r13 != 0) goto L_0x00af
            boolean r17 = r11.getOverrideTaskTransition()
            if (r17 == 0) goto L_0x00c5
        L_0x00af:
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            java.lang.String r2 = r11.getPackageName()
            if (r7 == 0) goto L_0x00bc
            int r3 = r11.getEnterResId()
            goto L_0x00c0
        L_0x00bc:
            int r3 = r11.getExitResId()
        L_0x00c0:
            android.view.animation.Animation r16 = r1.loadAnimationRes(r2, r3)
            goto L_0x0070
        L_0x00c5:
            r1 = 12
            if (r12 != r1) goto L_0x00d2
            if (r7 == 0) goto L_0x00d2
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.view.animation.Animation r16 = r1.loadCrossProfileAppEnterAnimation()
            goto L_0x0070
        L_0x00d2:
            r1 = 11
            if (r12 != r1) goto L_0x00e8
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.graphics.Rect r8 = r11.getTransitionBounds()
            r2 = r5
            r3 = r23
            r4 = r7
            r5 = r14
            r6 = r14
            r7 = r8
            android.view.animation.Animation r16 = r1.createClipRevealAnimationLocked(r2, r3, r4, r5, r6, r7)
            goto L_0x0070
        L_0x00e8:
            r1 = 2
            if (r12 != r1) goto L_0x00fd
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.graphics.Rect r8 = r11.getTransitionBounds()
            r2 = r5
            r3 = r23
            r4 = r7
            r5 = r14
            r6 = r8
            android.view.animation.Animation r16 = r1.createScaleUpAnimationLocked(r2, r3, r4, r5, r6)
            goto L_0x0070
        L_0x00fd:
            r2 = 3
            if (r12 == r2) goto L_0x01aa
            r9 = 4
            if (r12 != r9) goto L_0x0105
            goto L_0x01aa
        L_0x0105:
            r19 = r3 & 8
            if (r19 == 0) goto L_0x010c
            if (r4 == 0) goto L_0x010c
            return r16
        L_0x010c:
            r4 = 14
            if (r6 != r2) goto L_0x011b
            if (r7 == 0) goto L_0x0115
            r1 = 20
            goto L_0x0117
        L_0x0115:
            r1 = 21
        L_0x0117:
            r9 = r1
            r1 = 0
            goto L_0x018c
        L_0x011b:
            if (r6 != r9) goto L_0x0125
            if (r7 == 0) goto L_0x0122
            r1 = 22
            goto L_0x0117
        L_0x0122:
            r1 = 23
            goto L_0x0117
        L_0x0125:
            if (r6 != r10) goto L_0x012f
            if (r7 == 0) goto L_0x012c
            r1 = 16
            goto L_0x0117
        L_0x012c:
            r1 = 17
            goto L_0x0117
        L_0x012f:
            if (r6 != r1) goto L_0x0139
            if (r7 == 0) goto L_0x0136
            r1 = 18
            goto L_0x0117
        L_0x0136:
            r1 = 19
            goto L_0x0117
        L_0x0139:
            if (r5 != r10) goto L_0x015a
            r1 = r3 & 4
            if (r1 == 0) goto L_0x0144
            if (r7 == 0) goto L_0x0144
            r18 = r10
            goto L_0x0146
        L_0x0144:
            r18 = 0
        L_0x0146:
            if (r8 == 0) goto L_0x0153
            if (r18 != 0) goto L_0x0153
            if (r7 == 0) goto L_0x0150
            r1 = 8
            r9 = r1
            goto L_0x0157
        L_0x0150:
            r9 = 9
            goto L_0x0157
        L_0x0153:
            if (r7 == 0) goto L_0x0156
            goto L_0x0157
        L_0x0156:
            r9 = 5
        L_0x0157:
            r1 = r18
            goto L_0x018c
        L_0x015a:
            if (r5 != r2) goto L_0x0164
            if (r7 == 0) goto L_0x0161
            r1 = 12
            goto L_0x0117
        L_0x0161:
            r1 = 13
            goto L_0x0117
        L_0x0164:
            if (r5 != r1) goto L_0x0181
            if (r8 == 0) goto L_0x0170
            if (r7 == 0) goto L_0x016d
            r1 = 10
            goto L_0x0117
        L_0x016d:
            r1 = 11
            goto L_0x0117
        L_0x0170:
            r1 = r3 & 4
            if (r1 == 0) goto L_0x0178
            if (r7 != 0) goto L_0x0178
            r9 = r10
            goto L_0x0179
        L_0x0178:
            r9 = 0
        L_0x0179:
            if (r7 == 0) goto L_0x017c
            goto L_0x017e
        L_0x017c:
            r1 = 7
            r15 = r1
        L_0x017e:
            r1 = r9
            r9 = r15
            goto L_0x018c
        L_0x0181:
            if (r5 != r9) goto L_0x018a
            if (r7 == 0) goto L_0x0187
            r1 = r4
            goto L_0x0117
        L_0x0187:
            r1 = 15
            goto L_0x0117
        L_0x018a:
            r1 = 0
            r9 = 0
        L_0x018c:
            if (r9 == 0) goto L_0x0070
            if (r12 != r4) goto L_0x01a2
            if (r13 == 0) goto L_0x01a2
            com.android.internal.policy.TransitionAnimation r2 = r0.mTransitionAnimation
            java.lang.String r3 = r11.getPackageName()
            int r4 = r11.getAnimations()
            android.view.animation.Animation r16 = r2.loadAnimationAttr(r3, r4, r9, r1)
            goto L_0x0070
        L_0x01a2:
            com.android.internal.policy.TransitionAnimation r2 = r0.mTransitionAnimation
            android.view.animation.Animation r16 = r2.loadDefaultAnimationAttr(r9, r1)
            goto L_0x0070
        L_0x01aa:
            if (r12 != r2) goto L_0x01ae
            r3 = r10
            goto L_0x01af
        L_0x01ae:
            r3 = 0
        L_0x01af:
            com.android.internal.policy.TransitionAnimation r1 = r0.mTransitionAnimation
            android.hardware.HardwareBuffer r8 = r11.getThumbnail()
            android.graphics.Rect r9 = r11.getTransitionBounds()
            r2 = r7
            r4 = r14
            r6 = r23
            r7 = r8
            r8 = r9
            android.view.animation.Animation r16 = r1.createThumbnailEnterExitAnimationLocked(r2, r3, r4, r5, r6, r7, r8)
            goto L_0x0070
        L_0x01c5:
            if (r1 == 0) goto L_0x01e2
            boolean r2 = r1.isInitialized()
            if (r2 != 0) goto L_0x01d8
            int r2 = r14.width()
            int r3 = r14.height()
            r1.initialize(r2, r3, r2, r3)
        L_0x01d8:
            r2 = 3000(0xbb8, double:1.482E-320)
            r1.restrictDuration(r2)
            float r0 = r0.mTransitionAnimationScaleSetting
            r1.scaleCurrentDuration(r0)
        L_0x01e2:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.transition.DefaultTransitionHandler.loadAnimation(android.window.TransitionInfo, android.window.TransitionInfo$Change, int):android.view.animation.Animation");
    }

    public static void startSurfaceAnimation(ArrayList<Animator> arrayList, Animation animation, SurfaceControl surfaceControl, Runnable runnable, TransactionPool transactionPool, ShellExecutor shellExecutor, ShellExecutor shellExecutor2, Point point, float f, Rect rect) {
        SurfaceControl.Transaction acquire = transactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        Transformation transformation = new Transformation();
        ofFloat.overrideDurationScale(1.0f);
        ofFloat.setDuration(animation.computeDurationHint());
        ValueAnimator valueAnimator = ofFloat;
        SurfaceControl.Transaction transaction = acquire;
        SurfaceControl surfaceControl2 = surfaceControl;
        Animation animation2 = animation;
        Transformation transformation2 = transformation;
        float[] fArr = new float[9];
        Point point2 = point;
        float f2 = f;
        Rect rect2 = rect;
        ofFloat.addUpdateListener(new DefaultTransitionHandler$$ExternalSyntheticLambda4(valueAnimator, transaction, surfaceControl2, animation2, transformation2, fArr, point2, f2, rect2));
        final DefaultTransitionHandler$$ExternalSyntheticLambda5 defaultTransitionHandler$$ExternalSyntheticLambda5 = new DefaultTransitionHandler$$ExternalSyntheticLambda5(valueAnimator, transaction, surfaceControl2, animation2, transformation2, fArr, point2, f2, rect2, transactionPool, shellExecutor, arrayList, runnable);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                defaultTransitionHandler$$ExternalSyntheticLambda5.run();
            }

            public void onAnimationCancel(Animator animator) {
                defaultTransitionHandler$$ExternalSyntheticLambda5.run();
            }
        });
        arrayList.add(ofFloat);
        shellExecutor2.execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda3(ofFloat));
    }

    public static /* synthetic */ void lambda$startSurfaceAnimation$6(ValueAnimator valueAnimator, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, Point point, float f, Rect rect, TransactionPool transactionPool, ShellExecutor shellExecutor, ArrayList arrayList, Runnable runnable) {
        applyTransformation(valueAnimator.getDuration(), transaction, surfaceControl, animation, transformation, fArr, point, f, rect);
        SurfaceControl.Transaction transaction2 = transaction;
        transactionPool.release(transaction);
        ValueAnimator valueAnimator2 = valueAnimator;
        shellExecutor.execute(new DefaultTransitionHandler$$ExternalSyntheticLambda8(arrayList, valueAnimator, runnable));
    }

    public static /* synthetic */ void lambda$startSurfaceAnimation$5(ArrayList arrayList, ValueAnimator valueAnimator, Runnable runnable) {
        arrayList.remove(valueAnimator);
        runnable.run();
    }

    public final void attachThumbnail(ArrayList<Animator> arrayList, Runnable runnable, TransitionInfo.Change change, TransitionInfo.AnimationOptions animationOptions, float f) {
        boolean z = change.getTaskInfo() != null;
        boolean isOpeningType = Transitions.isOpeningType(change.getMode());
        boolean isClosingType = Transitions.isClosingType(change.getMode());
        if (isOpeningType) {
            if (animationOptions.getType() == 12 && z) {
                attachCrossProfileThumbnailAnimation(arrayList, runnable, change, f);
            } else if (animationOptions.getType() == 3) {
                attachThumbnailAnimation(arrayList, runnable, change, animationOptions, f);
            }
        } else if (isClosingType && animationOptions.getType() == 4) {
            attachThumbnailAnimation(arrayList, runnable, change, animationOptions, f);
        }
    }

    public final void attachCrossProfileThumbnailAnimation(ArrayList<Animator> arrayList, Runnable runnable, TransitionInfo.Change change, float f) {
        Rect endAbsBounds = change.getEndAbsBounds();
        HardwareBuffer createCrossProfileAppsThumbnail = this.mTransitionAnimation.createCrossProfileAppsThumbnail(change.getTaskInfo().userId == this.mCurrentUserId ? this.mContext.getDrawable(17302309) : this.mEnterpriseThumbnailDrawable, endAbsBounds);
        if (createCrossProfileAppsThumbnail != null) {
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            WindowThumbnail createAndAttach = WindowThumbnail.createAndAttach(this.mSurfaceSession, change.getLeash(), createCrossProfileAppsThumbnail, acquire);
            Animation createCrossProfileAppsThumbnailAnimationLocked = this.mTransitionAnimation.createCrossProfileAppsThumbnailAnimationLocked(endAbsBounds);
            if (createCrossProfileAppsThumbnailAnimationLocked != null) {
                DefaultTransitionHandler$$ExternalSyntheticLambda7 defaultTransitionHandler$$ExternalSyntheticLambda7 = new DefaultTransitionHandler$$ExternalSyntheticLambda7(this, createAndAttach, acquire, runnable);
                createCrossProfileAppsThumbnailAnimationLocked.restrictDuration(3000);
                createCrossProfileAppsThumbnailAnimationLocked.scaleCurrentDuration(this.mTransitionAnimationScaleSetting);
                startSurfaceAnimation(arrayList, createCrossProfileAppsThumbnailAnimationLocked, createAndAttach.getSurface(), defaultTransitionHandler$$ExternalSyntheticLambda7, this.mTransactionPool, this.mMainExecutor, this.mAnimExecutor, new Point(endAbsBounds.left, endAbsBounds.top), f, change.getEndAbsBounds());
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attachCrossProfileThumbnailAnimation$7(WindowThumbnail windowThumbnail, SurfaceControl.Transaction transaction, Runnable runnable) {
        windowThumbnail.destroy(transaction);
        this.mTransactionPool.release(transaction);
        runnable.run();
    }

    public final void attachThumbnailAnimation(ArrayList<Animator> arrayList, Runnable runnable, TransitionInfo.Change change, TransitionInfo.AnimationOptions animationOptions, float f) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        WindowThumbnail createAndAttach = WindowThumbnail.createAndAttach(this.mSurfaceSession, change.getLeash(), animationOptions.getThumbnail(), acquire);
        Animation createThumbnailAspectScaleAnimationLocked = this.mTransitionAnimation.createThumbnailAspectScaleAnimationLocked(change.getEndAbsBounds(), this.mInsets, animationOptions.getThumbnail(), this.mContext.getResources().getConfiguration().orientation, (Rect) null, animationOptions.getTransitionBounds(), animationOptions.getType() == 3);
        DefaultTransitionHandler$$ExternalSyntheticLambda6 defaultTransitionHandler$$ExternalSyntheticLambda6 = new DefaultTransitionHandler$$ExternalSyntheticLambda6(this, createAndAttach, acquire, runnable);
        createThumbnailAspectScaleAnimationLocked.restrictDuration(3000);
        createThumbnailAspectScaleAnimationLocked.scaleCurrentDuration(this.mTransitionAnimationScaleSetting);
        startSurfaceAnimation(arrayList, createThumbnailAspectScaleAnimationLocked, createAndAttach.getSurface(), defaultTransitionHandler$$ExternalSyntheticLambda6, this.mTransactionPool, this.mMainExecutor, this.mAnimExecutor, (Point) null, f, change.getEndAbsBounds());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attachThumbnailAnimation$8(WindowThumbnail windowThumbnail, SurfaceControl.Transaction transaction, Runnable runnable) {
        windowThumbnail.destroy(transaction);
        this.mTransactionPool.release(transaction);
        runnable.run();
    }

    public static int getWallpaperTransitType(TransitionInfo transitionInfo) {
        boolean z = false;
        boolean z2 = false;
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            if ((change.getFlags() & 1) != 0) {
                if (Transitions.isOpeningType(change.getMode())) {
                    z = true;
                } else if (Transitions.isClosingType(change.getMode())) {
                    z2 = true;
                }
            }
        }
        if (z && z2) {
            return Transitions.isOpeningType(transitionInfo.getType()) ? 3 : 4;
        }
        if (z) {
            return 1;
        }
        if (z2) {
            return 2;
        }
        return 0;
    }

    public static void applyTransformation(long j, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, Point point, float f, Rect rect) {
        animation.getTransformation(j, transformation);
        if (point != null) {
            transformation.getMatrix().postTranslate((float) point.x, (float) point.y);
        }
        transaction.setMatrix(surfaceControl, transformation.getMatrix(), fArr);
        transaction.setAlpha(surfaceControl, transformation.getAlpha());
        Insets min = Insets.min(transformation.getInsets(), Insets.NONE);
        if (!min.equals(Insets.NONE) && rect != null && !rect.isEmpty()) {
            rect.inset(min);
            transaction.setCrop(surfaceControl, rect);
        }
        if (animation.hasRoundedCorners() && f > 0.0f && rect != null) {
            transaction.setCrop(surfaceControl, rect);
            transaction.setCornerRadius(surfaceControl, f);
        }
        transaction.setFrameTimelineVsync(Choreographer.getInstance().getVsyncId());
        transaction.apply();
    }
}
