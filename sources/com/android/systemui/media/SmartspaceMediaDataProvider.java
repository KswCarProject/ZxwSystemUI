package com.android.systemui.media;

import android.app.smartspace.SmartspaceTarget;
import android.util.Log;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SmartspaceMediaDataProvider.kt */
public final class SmartspaceMediaDataProvider implements BcSmartspaceDataPlugin {
    @NotNull
    public final List<BcSmartspaceDataPlugin.SmartspaceTargetListener> smartspaceMediaTargetListeners = new ArrayList();
    @NotNull
    public List<SmartspaceTarget> smartspaceMediaTargets = CollectionsKt__CollectionsKt.emptyList();

    public void registerListener(@NotNull BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.smartspaceMediaTargetListeners.add(smartspaceTargetListener);
    }

    public void unregisterListener(@Nullable BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.smartspaceMediaTargetListeners.remove(smartspaceTargetListener);
    }

    public void onTargetsAvailable(@NotNull List<SmartspaceTarget> list) {
        ArrayList arrayList = new ArrayList();
        for (SmartspaceTarget next : list) {
            if (next.getFeatureType() == 15) {
                arrayList.add(next);
            }
        }
        if (!arrayList.isEmpty()) {
            Log.d("SsMediaDataProvider", Intrinsics.stringPlus("Forwarding Smartspace media updates ", arrayList));
        }
        this.smartspaceMediaTargets = arrayList;
        for (BcSmartspaceDataPlugin.SmartspaceTargetListener onSmartspaceTargetsUpdated : this.smartspaceMediaTargetListeners) {
            onSmartspaceTargetsUpdated.onSmartspaceTargetsUpdated(this.smartspaceMediaTargets);
        }
    }
}
