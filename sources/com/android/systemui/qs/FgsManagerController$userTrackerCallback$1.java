package com.android.systemui.qs;

import android.content.Context;
import android.content.pm.UserInfo;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$userTrackerCallback$1 implements UserTracker.Callback {
    public final /* synthetic */ FgsManagerController this$0;

    public void onUserChanged(int i, @NotNull Context context) {
    }

    public FgsManagerController$userTrackerCallback$1(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public void onProfilesChanged(@NotNull List<? extends UserInfo> list) {
        Object access$getLock$p = this.this$0.lock;
        FgsManagerController fgsManagerController = this.this$0;
        synchronized (access$getLock$p) {
            fgsManagerController.currentProfileIds.clear();
            Set access$getCurrentProfileIds$p = fgsManagerController.currentProfileIds;
            Iterable<UserInfo> iterable = list;
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (UserInfo userInfo : iterable) {
                arrayList.add(Integer.valueOf(userInfo.id));
            }
            access$getCurrentProfileIds$p.addAll(arrayList);
            fgsManagerController.lastNumberOfVisiblePackages = 0;
            fgsManagerController.updateNumberOfVisibleRunningPackagesLocked();
            Unit unit = Unit.INSTANCE;
        }
    }
}
