package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.keyguard.ScreenLifecycle;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UnfoldLatencyTracker.kt */
public final class UnfoldLatencyTracker implements ScreenLifecycle.Observer {
    @NotNull
    public final Context context;
    @NotNull
    public final DeviceStateManager deviceStateManager;
    @NotNull
    public final FoldStateListener foldStateListener;
    @Nullable
    public Boolean folded;
    @NotNull
    public final LatencyTracker latencyTracker;
    @NotNull
    public final ScreenLifecycle screenLifecycle;
    @NotNull
    public final Executor uiBgExecutor;

    public UnfoldLatencyTracker(@NotNull LatencyTracker latencyTracker2, @NotNull DeviceStateManager deviceStateManager2, @NotNull Executor executor, @NotNull Context context2, @NotNull ScreenLifecycle screenLifecycle2) {
        this.latencyTracker = latencyTracker2;
        this.deviceStateManager = deviceStateManager2;
        this.uiBgExecutor = executor;
        this.context = context2;
        this.screenLifecycle = screenLifecycle2;
        this.foldStateListener = new FoldStateListener(context2);
    }

    public final boolean isFoldable() {
        return !(this.context.getResources().getIntArray(17236068).length == 0);
    }

    public final void init() {
        if (isFoldable()) {
            this.deviceStateManager.registerCallback(this.uiBgExecutor, this.foldStateListener);
            this.screenLifecycle.addObserver(this);
        }
    }

    public void onScreenTurnedOn() {
        if (Intrinsics.areEqual((Object) this.folded, (Object) Boolean.FALSE)) {
            this.latencyTracker.onActionEnd(13);
        }
    }

    public final void onFoldEvent(boolean z) {
        if (!Intrinsics.areEqual((Object) this.folded, (Object) Boolean.valueOf(z))) {
            this.folded = Boolean.valueOf(z);
            if (!z) {
                this.latencyTracker.onActionStart(13);
            }
        }
    }

    /* compiled from: UnfoldLatencyTracker.kt */
    public final class FoldStateListener extends DeviceStateManager.FoldStateListener {
        public FoldStateListener(@NotNull Context context) {
            super(context, new Consumer(UnfoldLatencyTracker.this) {
                public final void accept(Boolean bool) {
                    r2.onFoldEvent(bool.booleanValue());
                }
            });
        }
    }
}
