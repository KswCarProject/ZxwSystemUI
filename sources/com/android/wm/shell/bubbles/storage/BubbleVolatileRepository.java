package com.android.wm.shell.bubbles.storage;

import android.content.pm.LauncherApps;
import android.os.UserHandle;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.bubbles.ShortcutKey;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: BubbleVolatileRepository.kt */
public final class BubbleVolatileRepository {
    public int capacity = 16;
    @NotNull
    public SparseArray<List<BubbleEntity>> entitiesByUser = new SparseArray<>();
    @NotNull
    public final LauncherApps launcherApps;

    @VisibleForTesting
    public static /* synthetic */ void getCapacity$annotations() {
    }

    public BubbleVolatileRepository(@NotNull LauncherApps launcherApps2) {
        this.launcherApps = launcherApps2;
    }

    @NotNull
    public final synchronized SparseArray<List<BubbleEntity>> getBubbles() {
        SparseArray<List<BubbleEntity>> sparseArray;
        sparseArray = new SparseArray<>();
        int i = 0;
        int size = this.entitiesByUser.size();
        while (i < size) {
            int i2 = i + 1;
            sparseArray.put(this.entitiesByUser.keyAt(i), CollectionsKt___CollectionsKt.toList(this.entitiesByUser.valueAt(i)));
            i = i2;
        }
        return sparseArray;
    }

    @NotNull
    public final synchronized List<BubbleEntity> getEntities(int i) {
        List<BubbleEntity> list;
        list = this.entitiesByUser.get(i);
        if (list == null) {
            list = new ArrayList<>();
            this.entitiesByUser.put(i, list);
        }
        return list;
    }

    public final synchronized void addBubbles(int i, @NotNull List<BubbleEntity> list) {
        if (!list.isEmpty()) {
            List<BubbleEntity> entities = getEntities(i);
            List<T> takeLast = CollectionsKt___CollectionsKt.takeLast(list, this.capacity);
            ArrayList arrayList = new ArrayList();
            for (Object next : takeLast) {
                if (!entities.removeIf(new BubbleVolatileRepository$addBubbles$uniqueBubbles$1$1((BubbleEntity) next))) {
                    arrayList.add(next);
                }
            }
            int size = (entities.size() + takeLast.size()) - this.capacity;
            if (size > 0) {
                uncache(CollectionsKt___CollectionsKt.take(entities, size));
                entities = CollectionsKt___CollectionsKt.toMutableList(CollectionsKt___CollectionsKt.drop(entities, size));
            }
            entities.addAll(takeLast);
            this.entitiesByUser.put(i, entities);
            cache(arrayList);
        }
    }

    public final synchronized void removeBubbles(int i, @NotNull List<BubbleEntity> list) {
        ArrayList arrayList = new ArrayList();
        for (Object next : list) {
            if (getEntities(i).removeIf(new BubbleVolatileRepository$removeBubbles$1$1((BubbleEntity) next))) {
                arrayList.add(next);
            }
        }
        uncache(arrayList);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001d, code lost:
        return r3 != null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized boolean removeBubblesForUser(int r2, int r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            r0 = -1
            if (r3 == r0) goto L_0x000a
            boolean r2 = r1.removeBubblesForUserWithParent(r2, r3)     // Catch:{ all -> 0x001e }
            monitor-exit(r1)
            return r2
        L_0x000a:
            android.util.SparseArray<java.util.List<com.android.wm.shell.bubbles.storage.BubbleEntity>> r3 = r1.entitiesByUser     // Catch:{ all -> 0x001e }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ all -> 0x001e }
            java.util.List r3 = (java.util.List) r3     // Catch:{ all -> 0x001e }
            android.util.SparseArray<java.util.List<com.android.wm.shell.bubbles.storage.BubbleEntity>> r0 = r1.entitiesByUser     // Catch:{ all -> 0x001e }
            r0.remove(r2)     // Catch:{ all -> 0x001e }
            if (r3 == 0) goto L_0x001b
            r2 = 1
            goto L_0x001c
        L_0x001b:
            r2 = 0
        L_0x001c:
            monitor-exit(r1)
            return r2
        L_0x001e:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository.removeBubblesForUser(int, int):boolean");
    }

    public final synchronized boolean removeBubblesForUserWithParent(int i, int i2) {
        if (this.entitiesByUser.get(i2) == null) {
            return false;
        }
        return this.entitiesByUser.get(i2).removeIf(new BubbleVolatileRepository$removeBubblesForUserWithParent$1(i));
    }

    public final synchronized boolean sanitizeBubbles(@NotNull List<Integer> list) {
        int size = this.entitiesByUser.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            int keyAt = this.entitiesByUser.keyAt(i);
            if (!list.contains(Integer.valueOf(keyAt))) {
                this.entitiesByUser.remove(keyAt);
                return true;
            } else if (this.entitiesByUser.get(keyAt) != null) {
                return this.entitiesByUser.get(keyAt).removeIf(new BubbleVolatileRepository$sanitizeBubbles$1(list));
            } else {
                i = i2;
            }
        }
        return false;
    }

    public final void cache(List<BubbleEntity> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object next : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) next;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj = linkedHashMap.get(shortcutKey);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(shortcutKey, obj);
            }
            ((List) obj).add(next);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            LauncherApps launcherApps2 = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            Iterable<BubbleEntity> iterable = (List) entry.getValue();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (BubbleEntity shortcutId : iterable) {
                arrayList.add(shortcutId.getShortcutId());
            }
            launcherApps2.cacheShortcuts(pkg, arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }

    public final void uncache(List<BubbleEntity> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object next : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) next;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj = linkedHashMap.get(shortcutKey);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(shortcutKey, obj);
            }
            ((List) obj).add(next);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            LauncherApps launcherApps2 = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            Iterable<BubbleEntity> iterable = (List) entry.getValue();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (BubbleEntity shortcutId : iterable) {
                arrayList.add(shortcutId.getShortcutId());
            }
            launcherApps2.uncacheShortcuts(pkg, arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }
}
