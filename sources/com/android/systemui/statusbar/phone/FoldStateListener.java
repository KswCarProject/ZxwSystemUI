package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FoldStateListener.kt */
public final class FoldStateListener implements DeviceStateManager.DeviceStateCallback {
    @NotNull
    public final int[] foldedDeviceStates;
    @NotNull
    public final int[] goToSleepDeviceStates;
    @NotNull
    public final OnFoldStateChangeListener listener;
    @Nullable
    public Boolean wasFolded;

    /* compiled from: FoldStateListener.kt */
    public interface OnFoldStateChangeListener {
        void onFoldStateChanged(boolean z, boolean z2);
    }

    public FoldStateListener(@NotNull Context context, @NotNull OnFoldStateChangeListener onFoldStateChangeListener) {
        this.listener = onFoldStateChangeListener;
        this.foldedDeviceStates = context.getResources().getIntArray(17236068);
        this.goToSleepDeviceStates = context.getResources().getIntArray(17236027);
    }

    public void onStateChanged(int i) {
        boolean contains = ArraysKt___ArraysKt.contains(this.foldedDeviceStates, i);
        if (!Intrinsics.areEqual((Object) this.wasFolded, (Object) Boolean.valueOf(contains))) {
            this.wasFolded = Boolean.valueOf(contains);
            this.listener.onFoldStateChanged(contains, ArraysKt___ArraysKt.contains(this.goToSleepDeviceStates, i));
        }
    }
}
