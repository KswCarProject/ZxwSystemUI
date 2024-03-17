package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.Parcelable;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: SmartspaceDedupingCoordinator.kt */
public /* synthetic */ class SmartspaceDedupingCoordinator$attach$1 implements BcSmartspaceDataPlugin.SmartspaceTargetListener {
    public final /* synthetic */ SmartspaceDedupingCoordinator $tmp0;

    public SmartspaceDedupingCoordinator$attach$1(SmartspaceDedupingCoordinator smartspaceDedupingCoordinator) {
        this.$tmp0 = smartspaceDedupingCoordinator;
    }

    public final void onSmartspaceTargetsUpdated(@NotNull List<? extends Parcelable> list) {
        this.$tmp0.onNewSmartspaceTargets(list);
    }
}
