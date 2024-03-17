package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.systemui.controls.ControlsServiceInfo;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$listingCallback$1$onServicesUpdated$1 implements Runnable {
    public final /* synthetic */ List<ControlsServiceInfo> $serviceInfos;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$listingCallback$1$onServicesUpdated$1(List<ControlsServiceInfo> list, ControlsControllerImpl controlsControllerImpl) {
        this.$serviceInfos = list;
        this.this$0 = controlsControllerImpl;
    }

    public final void run() {
        Iterable<ControlsServiceInfo> iterable = this.$serviceInfos;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (ControlsServiceInfo controlsServiceInfo : iterable) {
            arrayList.add(controlsServiceInfo.componentName);
        }
        Set set = CollectionsKt___CollectionsKt.toSet(arrayList);
        Iterable<StructureInfo> allStructures = Favorites.INSTANCE.getAllStructures();
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(allStructures, 10));
        for (StructureInfo componentName : allStructures) {
            arrayList2.add(componentName.getComponentName());
        }
        Set set2 = CollectionsKt___CollectionsKt.toSet(arrayList2);
        boolean z = false;
        SharedPreferences sharedPreferences = this.this$0.userStructure.getUserContext().getSharedPreferences("controls_prefs", 0);
        Set<String> stringSet = sharedPreferences.getStringSet("SeedingCompleted", new LinkedHashSet());
        Iterable<ComponentName> iterable2 = set;
        ArrayList arrayList3 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable2, 10));
        for (ComponentName packageName : iterable2) {
            arrayList3.add(packageName.getPackageName());
        }
        sharedPreferences.edit().putStringSet("SeedingCompleted", CollectionsKt___CollectionsKt.intersect(stringSet, arrayList3)).apply();
        Iterable iterable3 = set2;
        ControlsControllerImpl controlsControllerImpl = this.this$0;
        for (ComponentName componentName2 : CollectionsKt___CollectionsKt.subtract(iterable3, iterable2)) {
            Favorites.INSTANCE.removeStructures(componentName2);
            controlsControllerImpl.bindingController.onComponentRemoved(componentName2);
            z = true;
        }
        if (!this.this$0.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getFavorites().isEmpty()) {
            ControlsControllerImpl controlsControllerImpl2 = this.this$0;
            for (ComponentName cachedFavoritesAndRemoveFor : CollectionsKt___CollectionsKt.subtract(iterable2, iterable3)) {
                List<StructureInfo> cachedFavoritesAndRemoveFor2 = controlsControllerImpl2.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getCachedFavoritesAndRemoveFor(cachedFavoritesAndRemoveFor);
                if (!cachedFavoritesAndRemoveFor2.isEmpty()) {
                    for (StructureInfo replaceControls : cachedFavoritesAndRemoveFor2) {
                        Favorites.INSTANCE.replaceControls(replaceControls);
                    }
                    z = true;
                }
            }
            ControlsControllerImpl controlsControllerImpl3 = this.this$0;
            for (ComponentName cachedFavoritesAndRemoveFor3 : CollectionsKt___CollectionsKt.intersect(iterable2, iterable3)) {
                controlsControllerImpl3.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getCachedFavoritesAndRemoveFor(cachedFavoritesAndRemoveFor3);
            }
        }
        if (z) {
            Log.d("ControlsControllerImpl", "Detected change in available services, storing updated favorites");
            this.this$0.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
        }
    }
}
