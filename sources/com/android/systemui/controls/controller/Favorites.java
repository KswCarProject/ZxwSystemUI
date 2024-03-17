package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsControllerImpl.kt */
public final class Favorites {
    @NotNull
    public static final Favorites INSTANCE = new Favorites();
    @NotNull
    public static Map<ComponentName, ? extends List<StructureInfo>> favMap = MapsKt__MapsKt.emptyMap();

    @NotNull
    public final List<StructureInfo> getAllStructures() {
        Map<ComponentName, ? extends List<StructureInfo>> map = favMap;
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<ComponentName, ? extends List<StructureInfo>> value : map.entrySet()) {
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, (List) value.getValue());
        }
        return arrayList;
    }

    @NotNull
    public final List<StructureInfo> getStructuresForComponent(@NotNull ComponentName componentName) {
        List<StructureInfo> list = (List) favMap.get(componentName);
        return list == null ? CollectionsKt__CollectionsKt.emptyList() : list;
    }

    @NotNull
    public final List<ControlInfo> getControlsForStructure(@NotNull StructureInfo structureInfo) {
        List<ControlInfo> list;
        Object obj;
        Iterator it = getStructuresForComponent(structureInfo.getComponentName()).iterator();
        while (true) {
            list = null;
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual((Object) ((StructureInfo) obj).getStructure(), (Object) structureInfo.getStructure())) {
                break;
            }
        }
        StructureInfo structureInfo2 = (StructureInfo) obj;
        if (structureInfo2 != null) {
            list = structureInfo2.getControls();
        }
        return list == null ? CollectionsKt__CollectionsKt.emptyList() : list;
    }

    @NotNull
    public final List<ControlInfo> getControlsForComponent(@NotNull ComponentName componentName) {
        ArrayList arrayList = new ArrayList();
        for (StructureInfo controls : getStructuresForComponent(componentName)) {
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, controls.getControls());
        }
        return arrayList;
    }

    public final void load(@NotNull List<StructureInfo> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object next : list) {
            ComponentName componentName = ((StructureInfo) next).getComponentName();
            Object obj = linkedHashMap.get(componentName);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(componentName, obj);
            }
            ((List) obj).add(next);
        }
        favMap = linkedHashMap;
    }

    public final boolean updateControls(@NotNull ComponentName componentName, @NotNull List<Control> list) {
        Pair pair;
        boolean z;
        ControlInfo controlInfo;
        ComponentName componentName2 = componentName;
        Iterable iterable = list;
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10)), 16));
        for (Object next : iterable) {
            linkedHashMap.put(((Control) next).getControlId(), next);
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        boolean z2 = false;
        for (StructureInfo structureInfo : getStructuresForComponent(componentName)) {
            for (ControlInfo controlInfo2 : structureInfo.getControls()) {
                Control control = (Control) linkedHashMap.get(controlInfo2.getControlId());
                if (control == null) {
                    pair = null;
                } else {
                    if (!Intrinsics.areEqual((Object) control.getTitle(), (Object) controlInfo2.getControlTitle()) || !Intrinsics.areEqual((Object) control.getSubtitle(), (Object) controlInfo2.getControlSubtitle()) || control.getDeviceType() != controlInfo2.getDeviceType()) {
                        controlInfo = ControlInfo.copy$default(controlInfo2, (String) null, control.getTitle(), control.getSubtitle(), control.getDeviceType(), 1, (Object) null);
                        z = true;
                    } else {
                        z = z2;
                        controlInfo = controlInfo2;
                    }
                    Object structure = control.getStructure();
                    if (structure == null) {
                        structure = "";
                    }
                    if (!Intrinsics.areEqual((Object) structureInfo.getStructure(), structure)) {
                        z = true;
                    }
                    Pair pair2 = new Pair(structure, controlInfo);
                    z2 = z;
                    pair = pair2;
                }
                if (pair == null) {
                    pair = new Pair(structureInfo.getStructure(), controlInfo2);
                }
                CharSequence charSequence = (CharSequence) pair.component1();
                ControlInfo controlInfo3 = (ControlInfo) pair.component2();
                Object obj = linkedHashMap2.get(charSequence);
                if (obj == null) {
                    obj = new ArrayList();
                    linkedHashMap2.put(charSequence, obj);
                }
                ((List) obj).add(controlInfo3);
            }
        }
        if (!z2) {
            return false;
        }
        ArrayList arrayList = new ArrayList(linkedHashMap2.size());
        for (Map.Entry entry : linkedHashMap2.entrySet()) {
            arrayList.add(new StructureInfo(componentName2, (CharSequence) entry.getKey(), (List) entry.getValue()));
        }
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap = MapsKt__MapsKt.toMutableMap(favMap);
        mutableMap.put(componentName2, arrayList);
        favMap = mutableMap;
        return true;
    }

    public final void removeStructures(@NotNull ComponentName componentName) {
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap = MapsKt__MapsKt.toMutableMap(favMap);
        mutableMap.remove(componentName);
        favMap = mutableMap;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean addFavorite(@org.jetbrains.annotations.NotNull android.content.ComponentName r10, @org.jetbrains.annotations.NotNull java.lang.CharSequence r11, @org.jetbrains.annotations.NotNull com.android.systemui.controls.controller.ControlInfo r12) {
        /*
            r9 = this;
            java.util.List r0 = r9.getControlsForComponent(r10)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            boolean r1 = r0 instanceof java.util.Collection
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0017
            r1 = r0
            java.util.Collection r1 = (java.util.Collection) r1
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L_0x0017
        L_0x0015:
            r0 = r3
            goto L_0x0036
        L_0x0017:
            java.util.Iterator r0 = r0.iterator()
        L_0x001b:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0015
            java.lang.Object r1 = r0.next()
            com.android.systemui.controls.controller.ControlInfo r1 = (com.android.systemui.controls.controller.ControlInfo) r1
            java.lang.String r1 = r1.getControlId()
            java.lang.String r4 = r12.getControlId()
            boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r4)
            if (r1 == 0) goto L_0x001b
            r0 = r2
        L_0x0036:
            if (r0 == 0) goto L_0x0039
            return r3
        L_0x0039:
            java.util.Map<android.content.ComponentName, ? extends java.util.List<com.android.systemui.controls.controller.StructureInfo>> r0 = favMap
            java.lang.Object r0 = r0.get(r10)
            java.util.List r0 = (java.util.List) r0
            r1 = 0
            if (r0 != 0) goto L_0x0045
            goto L_0x0065
        L_0x0045:
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r0 = r0.iterator()
        L_0x004b:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x0063
            java.lang.Object r3 = r0.next()
            r4 = r3
            com.android.systemui.controls.controller.StructureInfo r4 = (com.android.systemui.controls.controller.StructureInfo) r4
            java.lang.CharSequence r4 = r4.getStructure()
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r4, (java.lang.Object) r11)
            if (r4 == 0) goto L_0x004b
            r1 = r3
        L_0x0063:
            com.android.systemui.controls.controller.StructureInfo r1 = (com.android.systemui.controls.controller.StructureInfo) r1
        L_0x0065:
            if (r1 != 0) goto L_0x0070
            com.android.systemui.controls.controller.StructureInfo r1 = new com.android.systemui.controls.controller.StructureInfo
            java.util.List r0 = kotlin.collections.CollectionsKt__CollectionsKt.emptyList()
            r1.<init>(r10, r11, r0)
        L_0x0070:
            r3 = r1
            r4 = 0
            r5 = 0
            java.util.List r10 = r3.getControls()
            java.util.Collection r10 = (java.util.Collection) r10
            java.util.List r6 = kotlin.collections.CollectionsKt___CollectionsKt.plus(r10, r12)
            r7 = 3
            r8 = 0
            com.android.systemui.controls.controller.StructureInfo r10 = com.android.systemui.controls.controller.StructureInfo.copy$default(r3, r4, r5, r6, r7, r8)
            r9.replaceControls(r10)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.Favorites.addFavorite(android.content.ComponentName, java.lang.CharSequence, com.android.systemui.controls.controller.ControlInfo):boolean");
    }

    public final void replaceControls(@NotNull StructureInfo structureInfo) {
        Map<ComponentName, ? extends List<StructureInfo>> mutableMap = MapsKt__MapsKt.toMutableMap(favMap);
        ArrayList arrayList = new ArrayList();
        ComponentName componentName = structureInfo.getComponentName();
        boolean z = false;
        for (StructureInfo structureInfo2 : getStructuresForComponent(componentName)) {
            if (Intrinsics.areEqual((Object) structureInfo2.getStructure(), (Object) structureInfo.getStructure())) {
                z = true;
                structureInfo2 = structureInfo;
            }
            if (!structureInfo2.getControls().isEmpty()) {
                arrayList.add(structureInfo2);
            }
        }
        if (!z && !structureInfo.getControls().isEmpty()) {
            arrayList.add(structureInfo);
        }
        mutableMap.put(componentName, arrayList);
        favMap = mutableMap;
    }

    public final void clear() {
        favMap = MapsKt__MapsKt.emptyMap();
    }
}
