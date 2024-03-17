package com.android.systemui.unfold.updates;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Handler;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DeviceFoldStateProvider.kt */
public final class DeviceFoldStateProvider implements FoldStateProvider {
    @NotNull
    public final ActivityManager activityManager;
    @NotNull
    public final DeviceStateManager deviceStateManager;
    @NotNull
    public final FoldStateListener foldStateListener;
    public final int halfOpenedTimeoutMillis;
    @NotNull
    public final Handler handler;
    @NotNull
    public final HingeAngleListener hingeAngleListener = new HingeAngleListener();
    @NotNull
    public final HingeAngleProvider hingeAngleProvider;
    public boolean isFolded;
    public boolean isUnfoldHandled;
    @Nullable
    public Integer lastFoldUpdate;
    public float lastHingeAngle;
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final List<FoldStateProvider.FoldUpdatesListener> outputListeners = new ArrayList();
    @NotNull
    public final ScreenStatusListener screenListener = new ScreenStatusListener();
    @NotNull
    public final ScreenStatusProvider screenStatusProvider;
    @NotNull
    public final TimeoutRunnable timeoutRunnable;

    public DeviceFoldStateProvider(@NotNull Context context, @NotNull HingeAngleProvider hingeAngleProvider2, @NotNull ScreenStatusProvider screenStatusProvider2, @NotNull DeviceStateManager deviceStateManager2, @NotNull ActivityManager activityManager2, @NotNull Executor executor, @NotNull Handler handler2) {
        this.hingeAngleProvider = hingeAngleProvider2;
        this.screenStatusProvider = screenStatusProvider2;
        this.deviceStateManager = deviceStateManager2;
        this.activityManager = activityManager2;
        this.mainExecutor = executor;
        this.handler = handler2;
        this.foldStateListener = new FoldStateListener(context);
        this.timeoutRunnable = new TimeoutRunnable();
        this.halfOpenedTimeoutMillis = context.getResources().getInteger(17694958);
        this.isUnfoldHandled = true;
    }

    public void start() {
        this.deviceStateManager.registerCallback(this.mainExecutor, this.foldStateListener);
        this.screenStatusProvider.addCallback(this.screenListener);
        this.hingeAngleProvider.addCallback(this.hingeAngleListener);
    }

    public void addCallback(@NotNull FoldStateProvider.FoldUpdatesListener foldUpdatesListener) {
        this.outputListeners.add(foldUpdatesListener);
    }

    public void removeCallback(@NotNull FoldStateProvider.FoldUpdatesListener foldUpdatesListener) {
        this.outputListeners.remove(foldUpdatesListener);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.lastFoldUpdate;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0010, code lost:
        r2 = r2.lastFoldUpdate;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isFinishedOpening() {
        /*
            r2 = this;
            boolean r0 = r2.isFolded
            if (r0 != 0) goto L_0x001e
            java.lang.Integer r0 = r2.lastFoldUpdate
            r1 = 4
            if (r0 != 0) goto L_0x000a
            goto L_0x0010
        L_0x000a:
            int r0 = r0.intValue()
            if (r0 == r1) goto L_0x001c
        L_0x0010:
            java.lang.Integer r2 = r2.lastFoldUpdate
            r0 = 3
            if (r2 != 0) goto L_0x0016
            goto L_0x001e
        L_0x0016:
            int r2 = r2.intValue()
            if (r2 != r0) goto L_0x001e
        L_0x001c:
            r2 = 1
            goto L_0x001f
        L_0x001e:
            r2 = 0
        L_0x001f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.updates.DeviceFoldStateProvider.isFinishedOpening():boolean");
    }

    public final boolean isTransitionInProgress() {
        Integer num = this.lastFoldUpdate;
        if (num != null && num.intValue() == 0) {
            return true;
        }
        Integer num2 = this.lastFoldUpdate;
        if (num2 != null && num2.intValue() == 1) {
            return true;
        }
        return false;
    }

    public final void onHingeAngle(float f) {
        boolean z = false;
        boolean z2 = f < this.lastHingeAngle;
        Integer closingThreshold = getClosingThreshold();
        boolean z3 = closingThreshold == null || f < ((float) closingThreshold.intValue());
        boolean z4 = 180.0f - f < 15.0f;
        Integer num = this.lastFoldUpdate;
        if (num != null && num.intValue() == 1) {
            z = true;
        }
        if (z2 && z3 && !z && !z4) {
            notifyFoldUpdate(1);
        }
        if (isTransitionInProgress()) {
            if (z4) {
                notifyFoldUpdate(4);
                cancelTimeout();
            } else {
                rescheduleAbortAnimationTimeout();
            }
        }
        this.lastHingeAngle = f;
        for (FoldStateProvider.FoldUpdatesListener onHingeAngleUpdate : this.outputListeners) {
            onHingeAngleUpdate.onHingeAngleUpdate(f);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000c, code lost:
        r2 = (android.app.ActivityManager.RunningTaskInfo) kotlin.collections.CollectionsKt___CollectionsKt.getOrNull(r2, 0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Integer getClosingThreshold() {
        /*
            r2 = this;
            android.app.ActivityManager r2 = r2.activityManager
            r0 = 1
            java.util.List r2 = r2.getRunningTasks(r0)
            r0 = 0
            if (r2 != 0) goto L_0x000c
        L_0x000a:
            r2 = r0
            goto L_0x001c
        L_0x000c:
            r1 = 0
            java.lang.Object r2 = kotlin.collections.CollectionsKt___CollectionsKt.getOrNull(r2, r1)
            android.app.ActivityManager$RunningTaskInfo r2 = (android.app.ActivityManager.RunningTaskInfo) r2
            if (r2 != 0) goto L_0x0016
            goto L_0x000a
        L_0x0016:
            int r2 = r2.topActivityType
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
        L_0x001c:
            if (r2 != 0) goto L_0x001f
            return r0
        L_0x001f:
            int r2 = r2.intValue()
            r1 = 2
            if (r2 != r1) goto L_0x0027
            goto L_0x002d
        L_0x0027:
            r2 = 60
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)
        L_0x002d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.updates.DeviceFoldStateProvider.getClosingThreshold():java.lang.Integer");
    }

    /* compiled from: DeviceFoldStateProvider.kt */
    public final class FoldStateListener extends DeviceStateManager.FoldStateListener {
        public FoldStateListener(@NotNull Context context) {
            super(context, new Consumer(DeviceFoldStateProvider.this) {
                public /* bridge */ /* synthetic */ void accept(Object obj) {
                    accept(((Boolean) obj).booleanValue());
                }

                public final void accept(boolean z) {
                    r2.isFolded = z;
                    r2.lastHingeAngle = 0.0f;
                    if (z) {
                        r2.hingeAngleProvider.stop();
                        r2.notifyFoldUpdate(5);
                        r2.cancelTimeout();
                        r2.isUnfoldHandled = false;
                        return;
                    }
                    r2.notifyFoldUpdate(0);
                    r2.rescheduleAbortAnimationTimeout();
                    r2.hingeAngleProvider.start();
                }
            });
        }
    }

    public final void notifyFoldUpdate(int i) {
        for (FoldStateProvider.FoldUpdatesListener onFoldUpdate : this.outputListeners) {
            onFoldUpdate.onFoldUpdate(i);
        }
        this.lastFoldUpdate = Integer.valueOf(i);
    }

    public final void rescheduleAbortAnimationTimeout() {
        if (isTransitionInProgress()) {
            cancelTimeout();
        }
        this.handler.postDelayed(this.timeoutRunnable, (long) this.halfOpenedTimeoutMillis);
    }

    public final void cancelTimeout() {
        this.handler.removeCallbacks(this.timeoutRunnable);
    }

    /* compiled from: DeviceFoldStateProvider.kt */
    public final class ScreenStatusListener implements ScreenStatusProvider.ScreenListener {
        public ScreenStatusListener() {
        }

        public void onScreenTurnedOn() {
            if (!DeviceFoldStateProvider.this.isFolded && !DeviceFoldStateProvider.this.isUnfoldHandled) {
                for (FoldStateProvider.FoldUpdatesListener onFoldUpdate : DeviceFoldStateProvider.this.outputListeners) {
                    onFoldUpdate.onFoldUpdate(2);
                }
                DeviceFoldStateProvider.this.isUnfoldHandled = true;
            }
        }
    }

    /* compiled from: DeviceFoldStateProvider.kt */
    public final class HingeAngleListener implements androidx.core.util.Consumer<Float> {
        public HingeAngleListener() {
        }

        public /* bridge */ /* synthetic */ void accept(Object obj) {
            accept(((Number) obj).floatValue());
        }

        public void accept(float f) {
            DeviceFoldStateProvider.this.onHingeAngle(f);
        }
    }

    /* compiled from: DeviceFoldStateProvider.kt */
    public final class TimeoutRunnable implements Runnable {
        public TimeoutRunnable() {
        }

        public void run() {
            DeviceFoldStateProvider.this.notifyFoldUpdate(3);
        }
    }
}
