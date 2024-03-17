package com.android.systemui.statusbar.events;

import android.provider.DeviceConfig;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: SystemEventCoordinator.kt */
public final class SystemEventCoordinator$privacyStateListener$1 implements PrivacyItemController.Callback {
    @NotNull
    public List<PrivacyItem> currentPrivacyItems = CollectionsKt__CollectionsKt.emptyList();
    @NotNull
    public List<PrivacyItem> previousPrivacyItems = CollectionsKt__CollectionsKt.emptyList();
    public final /* synthetic */ SystemEventCoordinator this$0;
    public long timeLastEmpty;

    public SystemEventCoordinator$privacyStateListener$1(SystemEventCoordinator systemEventCoordinator) {
        this.this$0 = systemEventCoordinator;
        this.timeLastEmpty = systemEventCoordinator.systemClock.elapsedRealtime();
    }

    @NotNull
    public final List<PrivacyItem> getCurrentPrivacyItems() {
        return this.currentPrivacyItems;
    }

    public void onPrivacyItemsChanged(@NotNull List<PrivacyItem> list) {
        if (!uniqueItemsMatch(list, this.currentPrivacyItems)) {
            if (list.isEmpty()) {
                this.previousPrivacyItems = this.currentPrivacyItems;
                this.timeLastEmpty = this.this$0.systemClock.elapsedRealtime();
            }
            this.currentPrivacyItems = list;
            notifyListeners();
        }
    }

    public final void notifyListeners() {
        if (this.currentPrivacyItems.isEmpty()) {
            this.this$0.notifyPrivacyItemsEmpty();
        } else {
            this.this$0.notifyPrivacyItemsChanged(isChipAnimationEnabled() && (!uniqueItemsMatch(this.currentPrivacyItems, this.previousPrivacyItems) || this.this$0.systemClock.elapsedRealtime() - this.timeLastEmpty >= 3000));
        }
    }

    public final boolean uniqueItemsMatch(List<PrivacyItem> list, List<PrivacyItem> list2) {
        Iterable<PrivacyItem> iterable = list;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (PrivacyItem privacyItem : iterable) {
            arrayList.add(TuplesKt.to(Integer.valueOf(privacyItem.getApplication().getUid()), privacyItem.getPrivacyType().getPermGroupName()));
        }
        Set set = CollectionsKt___CollectionsKt.toSet(arrayList);
        Iterable<PrivacyItem> iterable2 = list2;
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable2, 10));
        for (PrivacyItem privacyItem2 : iterable2) {
            arrayList2.add(TuplesKt.to(Integer.valueOf(privacyItem2.getApplication().getUid()), privacyItem2.getPrivacyType().getPermGroupName()));
        }
        return Intrinsics.areEqual((Object) set, (Object) CollectionsKt___CollectionsKt.toSet(arrayList2));
    }

    public final boolean isChipAnimationEnabled() {
        return DeviceConfig.getBoolean("privacy", "privacy_chip_animation_enabled", true);
    }
}
