package com.android.systemui.animation;

import android.os.IBinder;
import android.view.IRemoteAnimationRunner;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionInfo;
import org.jetbrains.annotations.NotNull;

/* compiled from: RemoteTransitionAdapter.kt */
public final class RemoteTransitionAdapter$Companion$adaptRemoteRunner$1 extends IRemoteTransition.Stub {
    public final /* synthetic */ IRemoteAnimationRunner $runner;

    public void mergeAnimation(@NotNull IBinder iBinder, @NotNull TransitionInfo transitionInfo, @NotNull SurfaceControl.Transaction transaction, @NotNull IBinder iBinder2, @NotNull IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
    }

    public RemoteTransitionAdapter$Companion$adaptRemoteRunner$1(IRemoteAnimationRunner iRemoteAnimationRunner) {
        this.$runner = iRemoteAnimationRunner;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x00b4 A[LOOP:0: B:3:0x0030->B:26:0x00b4, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00ab A[EDGE_INSN: B:74:0x00ab->B:25:0x00ab ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startAnimation(@org.jetbrains.annotations.NotNull android.os.IBinder r22, @org.jetbrains.annotations.NotNull android.window.TransitionInfo r23, @org.jetbrains.annotations.NotNull android.view.SurfaceControl.Transaction r24, @org.jetbrains.annotations.NotNull android.window.IRemoteTransitionFinishedCallback r25) {
        /*
            r21 = this;
            r3 = r23
            r0 = r24
            android.util.ArrayMap r10 = new android.util.ArrayMap
            r10.<init>()
            com.android.systemui.animation.RemoteTransitionAdapter$Companion r1 = com.android.systemui.animation.RemoteTransitionAdapter.Companion
            r2 = 0
            android.view.RemoteAnimationTarget[] r11 = r1.wrapTargets(r3, r2, r0, r10)
            r4 = 1
            android.view.RemoteAnimationTarget[] r12 = r1.wrapTargets(r3, r4, r0, r10)
            android.view.RemoteAnimationTarget[] r13 = new android.view.RemoteAnimationTarget[r2]
            java.util.List r1 = r23.getChanges()
            int r1 = r1.size()
            r14 = -1
            int r1 = r1 + r14
            r5 = 0
            r6 = 0
            r15 = 3
            r9 = 2
            if (r1 < 0) goto L_0x00ba
            r16 = r2
            r17 = r16
            r18 = r17
            r7 = r6
            r8 = r7
            r6 = r5
        L_0x0030:
            int r19 = r1 + -1
            java.util.List r2 = r23.getChanges()
            java.lang.Object r2 = r2.get(r1)
            android.window.TransitionInfo$Change r2 = (android.window.TransitionInfo.Change) r2
            android.app.ActivityManager$RunningTaskInfo r20 = r2.getTaskInfo()
            if (r20 == 0) goto L_0x006d
            android.app.ActivityManager$RunningTaskInfo r20 = r2.getTaskInfo()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r20)
            int r14 = r20.getActivityType()
            if (r14 != r9) goto L_0x006d
            int r7 = r2.getMode()
            if (r7 == r4) goto L_0x005f
            int r7 = r2.getMode()
            if (r7 != r15) goto L_0x005c
            goto L_0x005f
        L_0x005c:
            r16 = 0
            goto L_0x0061
        L_0x005f:
            r16 = r4
        L_0x0061:
            java.util.List r7 = r23.getChanges()
            int r7 = r7.size()
            int r17 = r7 - r1
            r7 = r2
            goto L_0x0075
        L_0x006d:
            int r1 = r2.getFlags()
            r1 = r1 & r9
            if (r1 == 0) goto L_0x0075
            r8 = r2
        L_0x0075:
            android.window.WindowContainerToken r1 = r2.getParent()
            if (r1 != 0) goto L_0x00a9
            int r1 = r2.getEndRotation()
            if (r1 < 0) goto L_0x00a9
            int r1 = r2.getEndRotation()
            int r14 = r2.getStartRotation()
            if (r1 == r14) goto L_0x00a9
            int r1 = r2.getEndRotation()
            int r5 = r2.getStartRotation()
            int r18 = r1 - r5
            android.graphics.Rect r1 = r2.getEndAbsBounds()
            int r1 = r1.width()
            float r1 = (float) r1
            android.graphics.Rect r2 = r2.getEndAbsBounds()
            int r2 = r2.height()
            float r2 = (float) r2
            r5 = r1
            r6 = r2
        L_0x00a9:
            if (r19 >= 0) goto L_0x00b4
            r19 = r6
            r1 = r7
            r14 = r8
            r2 = r17
            r17 = r5
            goto L_0x00c5
        L_0x00b4:
            r1 = r19
            r2 = 0
            r14 = -1
            goto L_0x0030
        L_0x00ba:
            r17 = r5
            r19 = r17
            r1 = r6
            r14 = r1
            r2 = 0
            r16 = 0
            r18 = 0
        L_0x00c5:
            com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator r8 = new com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator
            r8.<init>()
            com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator r7 = new com.android.systemui.animation.RemoteTransitionAdapter$CounterRotator
            r7.<init>()
            if (r1 == 0) goto L_0x010c
            if (r18 == 0) goto L_0x010c
            android.window.WindowContainerToken r4 = r1.getParent()
            if (r4 == 0) goto L_0x010c
            android.window.WindowContainerToken r4 = r1.getParent()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            android.window.TransitionInfo$Change r4 = r3.getChange(r4)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            android.view.SurfaceControl r6 = r4.getLeash()
            r4 = r8
            r5 = r24
            r22 = r7
            r7 = r18
            r20 = r8
            r8 = r17
            r9 = r19
            r4.setup(r5, r6, r7, r8, r9)
            android.view.SurfaceControl r4 = r20.getSurface()
            if (r4 == 0) goto L_0x0110
            android.view.SurfaceControl r4 = r20.getSurface()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            r0.setLayer(r4, r2)
            goto L_0x0110
        L_0x010c:
            r22 = r7
            r20 = r8
        L_0x0110:
            if (r16 == 0) goto L_0x01a8
            android.view.SurfaceControl r1 = r20.getSurface()
            if (r1 == 0) goto L_0x012b
            android.view.SurfaceControl r1 = r20.getSurface()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            java.util.List r2 = r23.getChanges()
            int r2 = r2.size()
            int r2 = r2 * r15
            r0.setLayer(r1, r2)
        L_0x012b:
            java.util.List r1 = r23.getChanges()
            int r1 = r1.size()
            r2 = -1
            int r1 = r1 + r2
            if (r1 < 0) goto L_0x018a
        L_0x0137:
            int r2 = r1 + -1
            java.util.List r4 = r23.getChanges()
            java.lang.Object r4 = r4.get(r1)
            android.window.TransitionInfo$Change r4 = (android.window.TransitionInfo.Change) r4
            android.view.SurfaceControl r5 = r4.getLeash()
            java.lang.Object r5 = r10.get(r5)
            android.view.SurfaceControl r5 = (android.view.SurfaceControl) r5
            java.util.List r6 = r23.getChanges()
            java.lang.Object r6 = r6.get(r1)
            android.window.TransitionInfo$Change r6 = (android.window.TransitionInfo.Change) r6
            int r6 = r6.getMode()
            boolean r4 = android.window.TransitionInfo.isIndependent(r4, r3)
            if (r4 != 0) goto L_0x0165
            r9 = r20
            r4 = 2
            goto L_0x0183
        L_0x0165:
            r4 = 2
            if (r6 == r4) goto L_0x016e
            r7 = 4
            if (r6 == r7) goto L_0x016e
            r9 = r20
            goto L_0x0183
        L_0x016e:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r5)
            java.util.List r6 = r23.getChanges()
            int r6 = r6.size()
            int r6 = r6 * r15
            int r6 = r6 - r1
            r0.setLayer(r5, r6)
            r9 = r20
            r9.addChild(r0, r5)
        L_0x0183:
            if (r2 >= 0) goto L_0x0186
            goto L_0x018c
        L_0x0186:
            r1 = r2
            r20 = r9
            goto L_0x0137
        L_0x018a:
            r9 = r20
        L_0x018c:
            int r1 = r12.length
            r2 = -1
            int r1 = r1 + r2
            if (r1 < 0) goto L_0x0207
        L_0x0191:
            int r2 = r1 + -1
            r4 = r12[r1]
            android.view.SurfaceControl r4 = r4.leash
            r0.show(r4)
            r1 = r12[r1]
            android.view.SurfaceControl r1 = r1.leash
            r4 = 1065353216(0x3f800000, float:1.0)
            r0.setAlpha(r1, r4)
            if (r2 >= 0) goto L_0x01a6
            goto L_0x0207
        L_0x01a6:
            r1 = r2
            goto L_0x0191
        L_0x01a8:
            r9 = r20
            if (r1 == 0) goto L_0x01b9
            android.view.SurfaceControl r1 = r1.getLeash()
            java.lang.Object r1 = r10.get(r1)
            android.view.SurfaceControl r1 = (android.view.SurfaceControl) r1
            r9.addChild(r0, r1)
        L_0x01b9:
            if (r14 == 0) goto L_0x0207
            if (r18 == 0) goto L_0x0207
            android.window.WindowContainerToken r1 = r14.getParent()
            if (r1 == 0) goto L_0x0207
            android.window.WindowContainerToken r1 = r14.getParent()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            android.window.TransitionInfo$Change r1 = r3.getChange(r1)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            android.view.SurfaceControl r6 = r1.getLeash()
            r4 = r22
            r5 = r24
            r7 = r18
            r8 = r17
            r1 = r9
            r9 = r19
            r4.setup(r5, r6, r7, r8, r9)
            android.view.SurfaceControl r2 = r22.getSurface()
            if (r2 == 0) goto L_0x0204
            android.view.SurfaceControl r2 = r22.getSurface()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            r4 = -1
            r0.setLayer(r2, r4)
            android.view.SurfaceControl r2 = r14.getLeash()
            java.lang.Object r2 = r10.get(r2)
            android.view.SurfaceControl r2 = (android.view.SurfaceControl) r2
            r4 = r22
            r4.addChild(r0, r2)
            goto L_0x020a
        L_0x0204:
            r4 = r22
            goto L_0x020a
        L_0x0207:
            r4 = r22
            r1 = r9
        L_0x020a:
            r24.apply()
            com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1$startAnimation$animationFinishedCallback$1 r8 = new com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1$startAnimation$animationFinishedCallback$1
            r0 = r8
            r2 = r4
            r3 = r23
            r4 = r10
            r5 = r25
            r0.<init>(r1, r2, r3, r4, r5)
            r0 = r21
            android.view.IRemoteAnimationRunner r3 = r0.$runner
            r4 = 0
            r5 = r11
            r6 = r12
            r7 = r13
            r3.onAnimationStart(r4, r5, r6, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.window.IRemoteTransitionFinishedCallback):void");
    }
}
