package com.android.systemui.qs;

import kotlin.collections.CollectionsKt___CollectionsKt;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$updateAppItemsLocked$3 implements Runnable {
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$updateAppItemsLocked$3(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public final void run() {
        this.this$0.appListAdapter.setData(CollectionsKt___CollectionsKt.sortedWith(CollectionsKt___CollectionsKt.toList(this.this$0.runningApps.values()), new FgsManagerController$updateAppItemsLocked$3$run$$inlined$sortedByDescending$1()));
    }
}
