package com.android.systemui.qs;

import com.android.systemui.qs.FgsManagerController;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$updateNumberOfVisibleRunningPackagesLocked$1$1 implements Runnable {
    public final /* synthetic */ FgsManagerController.OnNumberOfPackagesChangedListener $it;
    public final /* synthetic */ int $num;

    public FgsManagerController$updateNumberOfVisibleRunningPackagesLocked$1$1(FgsManagerController.OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener, int i) {
        this.$it = onNumberOfPackagesChangedListener;
        this.$num = i;
    }

    public final void run() {
        this.$it.onNumberOfPackagesChanged(this.$num);
    }
}
