package com.android.systemui.controls.management;

import android.service.controls.Control;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.management.ControlsModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AllModel.kt */
public final class AllModel implements ControlsModel {
    @NotNull
    public final List<ControlStatus> controls;
    @NotNull
    public final ControlsModel.ControlsModelCallback controlsModelCallback;
    @NotNull
    public final List<ElementWrapper> elements;
    @NotNull
    public final CharSequence emptyZoneString;
    @NotNull
    public final List<String> favoriteIds;
    public boolean modified;
    @Nullable
    public final Void moveHelper;

    public AllModel(@NotNull List<ControlStatus> list, @NotNull List<String> list2, @NotNull CharSequence charSequence, @NotNull ControlsModel.ControlsModelCallback controlsModelCallback2) {
        this.controls = list;
        this.emptyZoneString = charSequence;
        this.controlsModelCallback = controlsModelCallback2;
        HashSet hashSet = new HashSet();
        for (ControlStatus control : list) {
            hashSet.add(control.getControl().getControlId());
        }
        ArrayList arrayList = new ArrayList();
        for (Object next : list2) {
            if (hashSet.contains((String) next)) {
                arrayList.add(next);
            }
        }
        this.favoriteIds = CollectionsKt___CollectionsKt.toMutableList(arrayList);
        this.elements = createWrappers(this.controls);
    }

    @Nullable
    public Void getMoveHelper() {
        return this.moveHelper;
    }

    @NotNull
    public List<ControlInfo> getFavorites() {
        ControlInfo controlInfo;
        Object obj;
        ArrayList arrayList = new ArrayList();
        for (String str : this.favoriteIds) {
            Iterator it = this.controls.iterator();
            while (true) {
                controlInfo = null;
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (Intrinsics.areEqual((Object) ((ControlStatus) obj).getControl().getControlId(), (Object) str)) {
                    break;
                }
            }
            ControlStatus controlStatus = (ControlStatus) obj;
            Control control = controlStatus == null ? null : controlStatus.getControl();
            if (control != null) {
                controlInfo = ControlInfo.Companion.fromControl(control);
            }
            if (controlInfo != null) {
                arrayList.add(controlInfo);
            }
        }
        return arrayList;
    }

    @NotNull
    public List<ElementWrapper> getElements() {
        return this.elements;
    }

    public void changeFavoriteStatus(@NotNull String str, boolean z) {
        boolean z2;
        Object obj;
        boolean z3;
        ControlStatus controlStatus;
        boolean z4;
        Iterator it = getElements().iterator();
        while (true) {
            z2 = false;
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            ElementWrapper elementWrapper = (ElementWrapper) obj;
            if (!(elementWrapper instanceof ControlStatusWrapper) || !Intrinsics.areEqual((Object) ((ControlStatusWrapper) elementWrapper).getControlStatus().getControl().getControlId(), (Object) str)) {
                z4 = false;
                continue;
            } else {
                z4 = true;
                continue;
            }
            if (z4) {
                break;
            }
        }
        ControlStatusWrapper controlStatusWrapper = (ControlStatusWrapper) obj;
        if (!(controlStatusWrapper == null || (controlStatus = controlStatusWrapper.getControlStatus()) == null || z != controlStatus.getFavorite())) {
            z2 = true;
        }
        if (!z2) {
            if (z) {
                z3 = this.favoriteIds.add(str);
            } else {
                z3 = this.favoriteIds.remove(str);
            }
            if (z3 && !this.modified) {
                this.modified = true;
                this.controlsModelCallback.onFirstChange();
            }
            if (controlStatusWrapper != null) {
                controlStatusWrapper.getControlStatus().setFavorite(z);
            }
        }
    }

    public final List<ElementWrapper> createWrappers(List<ControlStatus> list) {
        OrderedMap orderedMap = new OrderedMap(new ArrayMap());
        for (Object next : list) {
            Object zone = ((ControlStatus) next).getControl().getZone();
            if (zone == null) {
                zone = "";
            }
            Object obj = orderedMap.get(zone);
            if (obj == null) {
                obj = new ArrayList();
                orderedMap.put(zone, obj);
            }
            ((List) obj).add(next);
        }
        ArrayList arrayList = new ArrayList();
        Sequence sequence = null;
        for (CharSequence charSequence : orderedMap.getOrderedKeys()) {
            Sequence map = SequencesKt___SequencesKt.map(CollectionsKt___CollectionsKt.asSequence((Iterable) MapsKt__MapsKt.getValue(orderedMap, charSequence)), AllModel$createWrappers$values$1.INSTANCE);
            if (TextUtils.isEmpty(charSequence)) {
                sequence = map;
            } else {
                arrayList.add(new ZoneNameWrapper(charSequence));
                CollectionsKt__MutableCollectionsKt.addAll(arrayList, map);
            }
        }
        if (sequence != null) {
            if (orderedMap.size() != 1) {
                arrayList.add(new ZoneNameWrapper(this.emptyZoneString));
            }
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, sequence);
        }
        return arrayList;
    }

    /* compiled from: AllModel.kt */
    public static final class OrderedMap<K, V> implements Map<K, V>, KMappedMarker {
        @NotNull
        public final Map<K, V> map;
        @NotNull
        public final List<K> orderedKeys = new ArrayList();

        public boolean containsKey(Object obj) {
            return this.map.containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return this.map.containsValue(obj);
        }

        @Nullable
        public V get(Object obj) {
            return this.map.get(obj);
        }

        @NotNull
        public Set<Map.Entry<K, V>> getEntries() {
            return this.map.entrySet();
        }

        @NotNull
        public Set<K> getKeys() {
            return this.map.keySet();
        }

        public int getSize() {
            return this.map.size();
        }

        @NotNull
        public Collection<V> getValues() {
            return this.map.values();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public void putAll(@NotNull Map<? extends K, ? extends V> map2) {
            this.map.putAll(map2);
        }

        public OrderedMap(@NotNull Map<K, V> map2) {
            this.map = map2;
        }

        public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
            return getEntries();
        }

        public final /* bridge */ Set<K> keySet() {
            return getKeys();
        }

        public final /* bridge */ int size() {
            return getSize();
        }

        public final /* bridge */ Collection<V> values() {
            return getValues();
        }

        @NotNull
        public final List<K> getOrderedKeys() {
            return this.orderedKeys;
        }

        @Nullable
        public V put(K k, V v) {
            if (!this.map.containsKey(k)) {
                this.orderedKeys.add(k);
            }
            return this.map.put(k, v);
        }

        public void clear() {
            this.orderedKeys.clear();
            this.map.clear();
        }

        @Nullable
        public V remove(Object obj) {
            V remove = this.map.remove(obj);
            if (remove != null) {
                this.orderedKeys.remove(obj);
            }
            return remove;
        }
    }
}
