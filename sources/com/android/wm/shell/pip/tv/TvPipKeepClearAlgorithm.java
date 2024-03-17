package com.android.wm.shell.pip.tv;

import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Size;
import android.view.Gravity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt__MathJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TvPipKeepClearAlgorithm.kt */
public final class TvPipKeepClearAlgorithm {
    @NotNull
    public Set<Rect> lastAreasOverlappingUnstashPosition = SetsKt__SetsKt.emptySet();
    public double maxRestrictedDistanceFraction = 0.15d;
    @NotNull
    public Rect movementBounds = new Rect();
    public int pipAreaPadding = 48;
    public int pipGravity = 85;
    public Insets pipPermanentDecorInsets = Insets.NONE;
    @NotNull
    public Size screenSize = new Size(0, 0);
    public int stashOffset = 48;
    @NotNull
    public Rect transformedMovementBounds = new Rect();
    @NotNull
    public Rect transformedScreenBounds = new Rect();

    /* compiled from: TvPipKeepClearAlgorithm.kt */
    public static final class Placement {
        @NotNull
        public final Rect anchorBounds;
        @NotNull
        public final Rect bounds;
        public final int stashType;
        public final boolean triggerStash;
        @Nullable
        public final Rect unstashDestinationBounds;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Placement)) {
                return false;
            }
            Placement placement = (Placement) obj;
            return Intrinsics.areEqual((Object) this.bounds, (Object) placement.bounds) && Intrinsics.areEqual((Object) this.anchorBounds, (Object) placement.anchorBounds) && this.stashType == placement.stashType && Intrinsics.areEqual((Object) this.unstashDestinationBounds, (Object) placement.unstashDestinationBounds) && this.triggerStash == placement.triggerStash;
        }

        public int hashCode() {
            int hashCode = ((((this.bounds.hashCode() * 31) + this.anchorBounds.hashCode()) * 31) + Integer.hashCode(this.stashType)) * 31;
            Rect rect = this.unstashDestinationBounds;
            int hashCode2 = (hashCode + (rect == null ? 0 : rect.hashCode())) * 31;
            boolean z = this.triggerStash;
            if (z) {
                z = true;
            }
            return hashCode2 + (z ? 1 : 0);
        }

        @NotNull
        public String toString() {
            return "Placement(bounds=" + this.bounds + ", anchorBounds=" + this.anchorBounds + ", stashType=" + this.stashType + ", unstashDestinationBounds=" + this.unstashDestinationBounds + ", triggerStash=" + this.triggerStash + ')';
        }

        public Placement(@NotNull Rect rect, @NotNull Rect rect2, int i, @Nullable Rect rect3, boolean z) {
            this.bounds = rect;
            this.anchorBounds = rect2;
            this.stashType = i;
            this.unstashDestinationBounds = rect3;
            this.triggerStash = z;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ Placement(Rect rect, Rect rect2, int i, Rect rect3, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(rect, rect2, (i2 & 4) != 0 ? 0 : i, (i2 & 8) != 0 ? null : rect3, (i2 & 16) != 0 ? false : z);
        }

        @NotNull
        public final Rect getBounds() {
            return this.bounds;
        }

        @NotNull
        public final Rect getAnchorBounds() {
            return this.anchorBounds;
        }

        public final int getStashType() {
            return this.stashType;
        }

        @Nullable
        public final Rect getUnstashDestinationBounds() {
            return this.unstashDestinationBounds;
        }

        public final boolean getTriggerStash() {
            return this.triggerStash;
        }

        @NotNull
        public final Rect getUnstashedBounds() {
            Rect rect = this.unstashDestinationBounds;
            return rect == null ? this.bounds : rect;
        }
    }

    public final int getPipAreaPadding() {
        return this.pipAreaPadding;
    }

    public final void setPipAreaPadding(int i) {
        this.pipAreaPadding = i;
    }

    public final void setStashOffset(int i) {
        this.stashOffset = i;
    }

    public final void setMaxRestrictedDistanceFraction(double d) {
        this.maxRestrictedDistanceFraction = d;
    }

    @NotNull
    public final Placement calculatePipPosition(@NotNull Size size, @NotNull Set<Rect> set, @NotNull Set<Rect> set2) {
        Rect rect;
        Placement calculatePipPositionTransformed = calculatePipPositionTransformed(getNormalPipAnchorBounds(addDecors(size), this.transformedMovementBounds), transformAndFilterAreas(set), transformAndFilterAreas(set2));
        Rect removePermanentDecors = removePermanentDecors(fromTransformedSpace(calculatePipPositionTransformed.getBounds()));
        Rect removePermanentDecors2 = removePermanentDecors(fromTransformedSpace(calculatePipPositionTransformed.getAnchorBounds()));
        Rect unstashDestinationBounds = calculatePipPositionTransformed.getUnstashDestinationBounds();
        if (unstashDestinationBounds == null) {
            rect = null;
        } else {
            rect = removePermanentDecors(fromTransformedSpace(unstashDestinationBounds));
        }
        Rect rect2 = rect;
        return new Placement(removePermanentDecors, removePermanentDecors2, getStashType(removePermanentDecors, rect2), rect2, calculatePipPositionTransformed.getTriggerStash());
    }

    public final Set<Rect> transformAndFilterAreas(Set<Rect> set) {
        Rect rect;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Rect rect2 : set) {
            if (rect2.contains(this.movementBounds)) {
                rect = null;
            } else {
                rect = toTransformedSpace(rect2);
            }
            if (rect != null) {
                linkedHashSet.add(rect);
            }
        }
        return linkedHashSet;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm.Placement calculatePipPositionTransformed(android.graphics.Rect r11, java.util.Set<android.graphics.Rect> r12, java.util.Set<android.graphics.Rect> r13) {
        /*
            r10 = this;
            r3 = r13
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.util.Set r3 = kotlin.collections.SetsKt___SetsKt.plus(r12, r3)
            r5 = r3
            java.lang.Iterable r5 = (java.lang.Iterable) r5
            boolean r6 = r5 instanceof java.util.Collection
            r7 = 1
            if (r6 == 0) goto L_0x001a
            r6 = r5
            java.util.Collection r6 = (java.util.Collection) r6
            boolean r6 = r6.isEmpty()
            if (r6 == 0) goto L_0x001a
        L_0x0018:
            r6 = r7
            goto L_0x0031
        L_0x001a:
            java.util.Iterator r6 = r5.iterator()
        L_0x001e:
            boolean r8 = r6.hasNext()
            if (r8 == 0) goto L_0x0018
            java.lang.Object r8 = r6.next()
            android.graphics.Rect r8 = (android.graphics.Rect) r8
            boolean r8 = r10.intersects(r8, r11)
            if (r8 == 0) goto L_0x001e
            r6 = 0
        L_0x0031:
            if (r6 == 0) goto L_0x0048
            java.util.Set r1 = kotlin.collections.SetsKt__SetsKt.emptySet()
            r10.lastAreasOverlappingUnstashPosition = r1
            com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement r8 = new com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 28
            r7 = 0
            r0 = r8
            r1 = r11
            r2 = r11
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            return r8
        L_0x0048:
            android.graphics.Rect r6 = r10.findFreeMovePosition(r11, r12, r13)
            if (r6 == 0) goto L_0x0065
            java.util.Set r1 = kotlin.collections.SetsKt__SetsKt.emptySet()
            r10.lastAreasOverlappingUnstashPosition = r1
            com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement r8 = new com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement
            r3 = 0
            r4 = 0
            r5 = 0
            r7 = 28
            r9 = 0
            r0 = r8
            r1 = r6
            r2 = r11
            r6 = r7
            r7 = r9
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            return r8
        L_0x0065:
            android.graphics.Rect r4 = r10.findRelaxedMovePosition(r11, r12, r13)
            if (r4 != 0) goto L_0x0078
            java.util.Set r4 = kotlin.collections.SetsKt__SetsKt.emptySet()
            android.graphics.Rect r1 = r10.findFreeMovePosition(r11, r4, r13)
            if (r1 != 0) goto L_0x0077
            r4 = r11
            goto L_0x0078
        L_0x0077:
            r4 = r1
        L_0x0078:
            java.util.LinkedHashSet r1 = new java.util.LinkedHashSet
            r1.<init>()
            java.util.Iterator r5 = r5.iterator()
        L_0x0081:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x0098
            java.lang.Object r6 = r5.next()
            r8 = r6
            android.graphics.Rect r8 = (android.graphics.Rect) r8
            boolean r8 = r10.intersects(r8, r4)
            if (r8 == 0) goto L_0x0081
            r1.add(r6)
            goto L_0x0081
        L_0x0098:
            java.util.Set<android.graphics.Rect> r5 = r10.lastAreasOverlappingUnstashPosition
            boolean r5 = r5.containsAll(r1)
            r5 = r5 ^ r7
            r10.lastAreasOverlappingUnstashPosition = r1
            android.graphics.Rect r1 = r10.getNearbyStashedPosition(r4, r3)
            com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement r6 = new com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement
            int r3 = r10.getStashType(r1, r4)
            r0 = r6
            r2 = r11
            r0.<init>(r1, r2, r3, r4, r5)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm.calculatePipPositionTransformed(android.graphics.Rect, java.util.Set, java.util.Set):com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm$Placement");
    }

    public final int getStashType(Rect rect, Rect rect2) {
        if (rect2 == null) {
            return 0;
        }
        if (rect.left < rect2.left) {
            return 1;
        }
        if (rect.right > rect2.right) {
            return 2;
        }
        if (rect.top < rect2.top) {
            return 4;
        }
        if (rect.bottom > rect2.bottom) {
            return 3;
        }
        return 0;
    }

    public final Rect findRelaxedMovePosition(Rect rect, Set<Rect> set, Set<Rect> set2) {
        return findRelaxedMovePosition(1, rect, CollectionsKt___CollectionsKt.toMutableSet(set), set2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v2, resolved type: android.graphics.Rect} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.graphics.Rect findRelaxedMovePosition(int r5, android.graphics.Rect r6, java.util.Set<android.graphics.Rect> r7, java.util.Set<android.graphics.Rect> r8) {
        /*
            r4 = this;
            if (r5 != 0) goto L_0x0007
            android.graphics.Rect r4 = r4.findFreeMovePosition(r6, r7, r8)
            return r4
        L_0x0007:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = r7
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.List r1 = kotlin.collections.CollectionsKt___CollectionsKt.toList(r1)
            java.util.Iterator r1 = r1.iterator()
        L_0x0017:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0035
            java.lang.Object r2 = r1.next()
            android.graphics.Rect r2 = (android.graphics.Rect) r2
            r7.remove(r2)
            int r3 = r5 + -1
            android.graphics.Rect r3 = r4.findRelaxedMovePosition(r3, r6, r7, r8)
            r7.add(r2)
            if (r3 == 0) goto L_0x0017
            r0.add(r3)
            goto L_0x0017
        L_0x0035:
            java.util.Iterator r5 = r0.iterator()
            boolean r7 = r5.hasNext()
            if (r7 != 0) goto L_0x0041
            r4 = 0
            goto L_0x006a
        L_0x0041:
            java.lang.Object r7 = r5.next()
            boolean r8 = r5.hasNext()
            if (r8 != 0) goto L_0x004d
        L_0x004b:
            r4 = r7
            goto L_0x006a
        L_0x004d:
            r8 = r7
            android.graphics.Rect r8 = (android.graphics.Rect) r8
            int r8 = r4.candidateCost(r8, r6)
        L_0x0054:
            java.lang.Object r0 = r5.next()
            r1 = r0
            android.graphics.Rect r1 = (android.graphics.Rect) r1
            int r1 = r4.candidateCost(r1, r6)
            if (r8 <= r1) goto L_0x0063
            r7 = r0
            r8 = r1
        L_0x0063:
            boolean r0 = r5.hasNext()
            if (r0 != 0) goto L_0x0054
            goto L_0x004b
        L_0x006a:
            android.graphics.Rect r4 = (android.graphics.Rect) r4
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm.findRelaxedMovePosition(int, android.graphics.Rect, java.util.Set, java.util.Set):android.graphics.Rect");
    }

    public final int candidateCost(Rect rect, Rect rect2) {
        int i = rect.left - rect2.left;
        int i2 = rect.top - rect2.top;
        return (i * i) + (i2 * i2);
    }

    public final Rect findFreeMovePosition(Rect rect, Set<Rect> set, Set<Rect> set2) {
        boolean z;
        Rect rect2 = rect;
        Set<Rect> set3 = set;
        Set<Rect> set4 = set2;
        Rect rect3 = this.transformedMovementBounds;
        ArrayList arrayList = new ArrayList();
        double width = ((double) rect2.right) - (((double) this.screenSize.getWidth()) * this.maxRestrictedDistanceFraction);
        int i = 0;
        arrayList.add(offsetCopy(rect3, rect3.width() + this.pipAreaPadding, 0));
        arrayList.addAll(set4);
        ArrayList arrayList2 = new ArrayList();
        Iterator it = set3.iterator();
        while (true) {
            z = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            if (((double) ((Rect) next).left) < width) {
                z = false;
            }
            if (z) {
                arrayList2.add(next);
            }
        }
        arrayList.addAll(arrayList2);
        CollectionsKt__MutableCollectionsKt.retainAll(arrayList, new TvPipKeepClearAlgorithm$findFreeMovePosition$2(this, rect3.left + rect.width()));
        if (arrayList.size() > 1) {
            CollectionsKt__MutableCollectionsJVMKt.sortWith(arrayList, new TvPipKeepClearAlgorithm$findFreeMovePosition$$inlined$sortBy$1());
        }
        int roundToInt = MathKt__MathJVMKt.roundToInt(((double) this.screenSize.getHeight()) * this.maxRestrictedDistanceFraction);
        ArrayList arrayList3 = new ArrayList();
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            Rect rect4 = (Rect) it2.next();
            int width2 = ((rect4.left - this.pipAreaPadding) - rect.width()) - rect2.left;
            Rect offsetCopy = offsetCopy(rect2, width2, i);
            boolean isPipAnchoredToCorner = isPipAnchoredToCorner() ^ z;
            SweepLineEvent findMinMoveUp = findMinMoveUp(offsetCopy, set3, set4);
            int pos = (findMinMoveUp.getPos() - rect2.bottom) - (findMinMoveUp.getStart() ? i : this.pipAreaPadding);
            int height = findMinMoveUp.getUnrestricted() ? rect3.height() : roundToInt;
            Rect offsetCopy2 = offsetCopy(rect2, width2, pos);
            Iterator it3 = it2;
            boolean z2 = offsetCopy2.top > rect3.top;
            boolean z3 = !intersectsY(offsetCopy2, rect4);
            if (z2 && Math.abs(pos) <= height && !z3) {
                arrayList3.add(offsetCopy2);
            }
            if (isPipAnchoredToCorner) {
                SweepLineEvent findMinMoveDown = findMinMoveDown(offsetCopy, set3, set4);
                int pos2 = (findMinMoveDown.getPos() - rect2.top) + (findMinMoveDown.getStart() ? 0 : this.pipAreaPadding);
                int height2 = findMinMoveDown.getUnrestricted() ? rect3.height() : roundToInt;
                Rect offsetCopy3 = offsetCopy(rect2, width2, pos2);
                boolean z4 = offsetCopy3.bottom < rect3.bottom;
                boolean z5 = !intersectsY(offsetCopy3, rect4);
                if (z4 && Math.abs(pos2) <= height2 && !z5) {
                    arrayList3.add(offsetCopy3);
                }
            }
            it2 = it3;
            i = 0;
            z = true;
        }
        if (arrayList3.size() > 1) {
            CollectionsKt__MutableCollectionsJVMKt.sortWith(arrayList3, new TvPipKeepClearAlgorithm$findFreeMovePosition$$inlined$sortBy$2(this, rect2));
        }
        return (Rect) CollectionsKt___CollectionsKt.firstOrNull(arrayList3);
    }

    public final Rect getNearbyStashedPosition(Rect rect, Set<Rect> set) {
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4;
        Rect rect2 = this.transformedScreenBounds;
        ArrayList arrayList = new ArrayList();
        Iterable iterable = set;
        ArrayList arrayList2 = new ArrayList();
        for (Object next : iterable) {
            if (intersectsX((Rect) next, rect)) {
                arrayList2.add(next);
            }
        }
        ArrayList arrayList3 = new ArrayList();
        for (Object next2 : iterable) {
            if (intersectsY((Rect) next2, rect)) {
                arrayList3.add(next2);
            }
        }
        Object obj5 = null;
        if (!arrayList2.isEmpty()) {
            int i = rect2.bottom;
            if (i - rect.bottom <= rect.top - rect2.top) {
                int i2 = i - this.stashOffset;
                Iterator it = arrayList2.iterator();
                if (!it.hasNext()) {
                    obj4 = null;
                } else {
                    obj4 = it.next();
                    if (it.hasNext()) {
                        int i3 = ((Rect) obj4).bottom;
                        do {
                            Object next3 = it.next();
                            int i4 = ((Rect) next3).bottom;
                            if (i3 < i4) {
                                obj4 = next3;
                                i3 = i4;
                            }
                        } while (it.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj4);
                int min = Math.min(i2, ((Rect) obj4).bottom + this.pipAreaPadding);
                if (min > rect.top) {
                    Rect rect3 = new Rect(rect);
                    rect3.offsetTo(rect.left, min);
                    arrayList.add(rect3);
                }
            }
            int i5 = rect2.bottom - rect.bottom;
            int i6 = rect.top;
            int i7 = rect2.top;
            if (i5 >= i6 - i7) {
                int height = (i7 - rect.height()) + this.stashOffset;
                Iterator it2 = arrayList2.iterator();
                if (!it2.hasNext()) {
                    obj3 = null;
                } else {
                    obj3 = it2.next();
                    if (it2.hasNext()) {
                        int i8 = ((Rect) obj3).top;
                        do {
                            Object next4 = it2.next();
                            int i9 = ((Rect) next4).top;
                            if (i8 > i9) {
                                obj3 = next4;
                                i8 = i9;
                            }
                        } while (it2.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj3);
                int max = Math.max(height, (((Rect) obj3).top - rect.height()) - this.pipAreaPadding);
                if (max < rect.top) {
                    Rect rect4 = new Rect(rect);
                    rect4.offsetTo(rect.left, max);
                    arrayList.add(rect4);
                }
            }
        }
        if (!arrayList3.isEmpty()) {
            int i10 = rect2.right;
            if (i10 - rect.right <= rect.left - rect2.left) {
                int i11 = i10 - this.stashOffset;
                Iterator it3 = arrayList3.iterator();
                if (!it3.hasNext()) {
                    obj2 = null;
                } else {
                    obj2 = it3.next();
                    if (it3.hasNext()) {
                        int i12 = ((Rect) obj2).right;
                        do {
                            Object next5 = it3.next();
                            int i13 = ((Rect) next5).right;
                            if (i12 < i13) {
                                obj2 = next5;
                                i12 = i13;
                            }
                        } while (it3.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj2);
                int min2 = Math.min(i11, ((Rect) obj2).right + this.pipAreaPadding);
                if (min2 > rect.left) {
                    Rect rect5 = new Rect(rect);
                    rect5.offsetTo(min2, rect.top);
                    arrayList.add(rect5);
                }
            }
            int i14 = rect2.right - rect.right;
            int i15 = rect.left;
            int i16 = rect2.left;
            if (i14 >= i15 - i16) {
                int width = (i16 - rect.width()) + this.stashOffset;
                Iterator it4 = arrayList3.iterator();
                if (!it4.hasNext()) {
                    obj = null;
                } else {
                    obj = it4.next();
                    if (it4.hasNext()) {
                        int i17 = ((Rect) obj).left;
                        do {
                            Object next6 = it4.next();
                            int i18 = ((Rect) next6).left;
                            if (i17 > i18) {
                                obj = next6;
                                i17 = i18;
                            }
                        } while (it4.hasNext());
                    }
                }
                Intrinsics.checkNotNull(obj);
                int max2 = Math.max(width, (((Rect) obj).left - rect.width()) - this.pipAreaPadding);
                if (max2 < rect.left) {
                    Rect rect6 = new Rect(rect);
                    rect6.offsetTo(max2, rect.top);
                    arrayList.add(rect6);
                }
            }
        }
        Iterator it5 = arrayList.iterator();
        if (it5.hasNext()) {
            obj5 = it5.next();
            if (it5.hasNext()) {
                Rect rect7 = (Rect) obj5;
                int abs = Math.abs(rect7.left - rect.left) + Math.abs(rect7.top - rect.top);
                do {
                    Object next7 = it5.next();
                    Rect rect8 = (Rect) next7;
                    int abs2 = Math.abs(rect8.left - rect.left) + Math.abs(rect8.top - rect.top);
                    if (abs > abs2) {
                        obj5 = next7;
                        abs = abs2;
                    }
                } while (it5.hasNext());
            }
        }
        Rect rect9 = (Rect) obj5;
        return rect9 == null ? rect : rect9;
    }

    public final void setScreenSize(@NotNull Size size) {
        if (!Intrinsics.areEqual((Object) this.screenSize, (Object) size)) {
            this.screenSize = size;
            this.transformedScreenBounds = toTransformedSpace(new Rect(0, 0, this.screenSize.getWidth(), this.screenSize.getHeight()));
            this.transformedMovementBounds = toTransformedSpace(this.transformedMovementBounds);
        }
    }

    public final void setMovementBounds(@NotNull Rect rect) {
        if (!Intrinsics.areEqual((Object) this.movementBounds, (Object) rect)) {
            this.movementBounds.set(rect);
            this.transformedMovementBounds = toTransformedSpace(this.movementBounds);
        }
    }

    public final void setGravity(int i) {
        if (this.pipGravity != i) {
            this.pipGravity = i;
            this.transformedScreenBounds = toTransformedSpace(new Rect(0, 0, this.screenSize.getWidth(), this.screenSize.getHeight()));
            this.transformedMovementBounds = toTransformedSpace(this.movementBounds);
        }
    }

    public final void setPipPermanentDecorInsets(@NotNull Insets insets) {
        this.pipPermanentDecorInsets = insets;
    }

    /* compiled from: TvPipKeepClearAlgorithm.kt */
    public static final class SweepLineEvent {
        public final boolean open;
        public final int pos;
        public final boolean start;
        public final boolean unrestricted;

        public static /* synthetic */ SweepLineEvent copy$default(SweepLineEvent sweepLineEvent, boolean z, int i, boolean z2, boolean z3, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = sweepLineEvent.open;
            }
            if ((i2 & 2) != 0) {
                i = sweepLineEvent.pos;
            }
            if ((i2 & 4) != 0) {
                z2 = sweepLineEvent.unrestricted;
            }
            if ((i2 & 8) != 0) {
                z3 = sweepLineEvent.start;
            }
            return sweepLineEvent.copy(z, i, z2, z3);
        }

        @NotNull
        public final SweepLineEvent copy(boolean z, int i, boolean z2, boolean z3) {
            return new SweepLineEvent(z, i, z2, z3);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SweepLineEvent)) {
                return false;
            }
            SweepLineEvent sweepLineEvent = (SweepLineEvent) obj;
            return this.open == sweepLineEvent.open && this.pos == sweepLineEvent.pos && this.unrestricted == sweepLineEvent.unrestricted && this.start == sweepLineEvent.start;
        }

        public int hashCode() {
            boolean z = this.open;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int hashCode = (((z ? 1 : 0) * true) + Integer.hashCode(this.pos)) * 31;
            boolean z3 = this.unrestricted;
            if (z3) {
                z3 = true;
            }
            int i = (hashCode + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.start;
            if (!z4) {
                z2 = z4;
            }
            return i + (z2 ? 1 : 0);
        }

        @NotNull
        public String toString() {
            return "SweepLineEvent(open=" + this.open + ", pos=" + this.pos + ", unrestricted=" + this.unrestricted + ", start=" + this.start + ')';
        }

        public SweepLineEvent(boolean z, int i, boolean z2, boolean z3) {
            this.open = z;
            this.pos = i;
            this.unrestricted = z2;
            this.start = z3;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ SweepLineEvent(boolean z, int i, boolean z2, boolean z3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(z, i, z2, (i2 & 8) != 0 ? false : z3);
        }

        public final boolean getOpen() {
            return this.open;
        }

        public final int getPos() {
            return this.pos;
        }

        public final boolean getUnrestricted() {
            return this.unrestricted;
        }

        public final boolean getStart() {
            return this.start;
        }
    }

    public final SweepLineEvent findMinMoveUp(Rect rect, Set<Rect> set, Set<Rect> set2) {
        ArrayList arrayList = new ArrayList();
        TvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1 tvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1 = new TvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1(this, rect, arrayList);
        Function1 function1 = (Function1) tvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1.invoke(Boolean.FALSE);
        for (Object invoke : set) {
            function1.invoke(invoke);
        }
        Function1 function12 = (Function1) tvPipKeepClearAlgorithm$findMinMoveUp$generateEvents$1.invoke(Boolean.TRUE);
        for (Object invoke2 : set2) {
            function12.invoke(invoke2);
        }
        return sweepLineFindEarliestGap(arrayList, rect.height() + this.pipAreaPadding, rect.bottom, rect.height());
    }

    public final SweepLineEvent findMinMoveDown(Rect rect, Set<Rect> set, Set<Rect> set2) {
        ArrayList arrayList = new ArrayList();
        TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1 tvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1 = new TvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1(this, rect, arrayList);
        Function1 function1 = (Function1) tvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1.invoke(Boolean.FALSE);
        for (Object invoke : set) {
            function1.invoke(invoke);
        }
        Function1 function12 = (Function1) tvPipKeepClearAlgorithm$findMinMoveDown$generateEvents$1.invoke(Boolean.TRUE);
        for (Object invoke2 : set2) {
            function12.invoke(invoke2);
        }
        SweepLineEvent sweepLineFindEarliestGap = sweepLineFindEarliestGap(arrayList, rect.height() + this.pipAreaPadding, -rect.top, rect.height());
        return SweepLineEvent.copy$default(sweepLineFindEarliestGap, false, -sweepLineFindEarliestGap.getPos(), false, false, 13, (Object) null);
    }

    public final SweepLineEvent sweepLineFindEarliestGap(List<SweepLineEvent> list, int i, int i2, int i3) {
        int pos;
        list.add(new SweepLineEvent(false, i2, true, true));
        if (list.size() > 1) {
            CollectionsKt__MutableCollectionsJVMKt.sortWith(list, new TvPipKeepClearAlgorithm$sweepLineFindEarliestGap$$inlined$sortBy$1());
        }
        int i4 = 0;
        for (int i5 = 0; i5 < list.size(); i5++) {
            SweepLineEvent sweepLineEvent = list.get(i5);
            if (!sweepLineEvent.getStart()) {
                i4 = sweepLineEvent.getOpen() ? i4 + 1 : i4 - 1;
            }
            if (i4 == 0 && (pos = sweepLineEvent.getPos()) <= i2) {
                int i6 = sweepLineEvent.getStart() ? i3 : i;
                SweepLineEvent sweepLineEvent2 = (SweepLineEvent) CollectionsKt___CollectionsKt.getOrNull(list, i5 + 1);
                if (sweepLineEvent2 == null || sweepLineEvent2.getPos() < pos - i6) {
                    return sweepLineEvent;
                }
            }
        }
        return (SweepLineEvent) CollectionsKt___CollectionsKt.last(list);
    }

    public final boolean shouldTransformFlipX() {
        int i = this.pipGravity;
        return i == 3 || i == 19 || i == 51 || i == 83 || i == 48 || i == 49;
    }

    public final boolean shouldTransformFlipY() {
        int i = this.pipGravity;
        return i == 51 || i == 53;
    }

    public final boolean shouldTransformRotate() {
        int i = this.pipGravity;
        int i2 = i & 7;
        if (i2 == 3 || i2 == 5) {
            return false;
        }
        int i3 = i & 112;
        return i3 == 48 || i3 == 80;
    }

    public final Rect toTransformedSpace(Rect rect) {
        int width = this.screenSize.getWidth();
        int height = this.screenSize.getHeight();
        int i = 1;
        Point[] pointArr = {new Point(rect.left, rect.top), new Point(rect.right, rect.top), new Point(rect.right, rect.bottom), new Point(rect.left, rect.bottom)};
        if (shouldTransformRotate()) {
            int i2 = 0;
            while (i2 < 4) {
                Point point = pointArr[i2];
                i2++;
                int i3 = point.x;
                point.x = point.y;
                point.y = (-i3) + width;
            }
            width = this.screenSize.getHeight();
            height = this.screenSize.getWidth();
        }
        int i4 = 0;
        while (i4 < 4) {
            Point point2 = pointArr[i4];
            i4++;
            if (shouldTransformFlipX()) {
                point2.x = width - point2.x;
            }
            if (shouldTransformFlipY()) {
                point2.y = height - point2.y;
            }
        }
        Point point3 = pointArr[0];
        int lastIndex = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex != 0) {
            int i5 = point3.y;
            if (1 <= lastIndex) {
                int i6 = 1;
                while (true) {
                    int i7 = i6 + 1;
                    Point point4 = pointArr[i6];
                    int i8 = point4.y;
                    if (i5 > i8) {
                        point3 = point4;
                        i5 = i8;
                    }
                    if (i6 == lastIndex) {
                        break;
                    }
                    i6 = i7;
                }
            }
        }
        Intrinsics.checkNotNull(point3);
        int i9 = point3.y;
        Point point5 = pointArr[0];
        int lastIndex2 = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex2 != 0) {
            int i10 = point5.x;
            if (1 <= lastIndex2) {
                int i11 = 1;
                while (true) {
                    int i12 = i11 + 1;
                    Point point6 = pointArr[i11];
                    int i13 = point6.x;
                    if (i10 < i13) {
                        point5 = point6;
                        i10 = i13;
                    }
                    if (i11 == lastIndex2) {
                        break;
                    }
                    i11 = i12;
                }
            }
        }
        Intrinsics.checkNotNull(point5);
        int i14 = point5.x;
        Point point7 = pointArr[0];
        int lastIndex3 = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex3 != 0) {
            int i15 = point7.y;
            if (1 <= lastIndex3) {
                int i16 = 1;
                while (true) {
                    int i17 = i16 + 1;
                    Point point8 = pointArr[i16];
                    int i18 = point8.y;
                    if (i15 < i18) {
                        point7 = point8;
                        i15 = i18;
                    }
                    if (i16 == lastIndex3) {
                        break;
                    }
                    i16 = i17;
                }
            }
        }
        Intrinsics.checkNotNull(point7);
        int i19 = point7.y;
        Point point9 = pointArr[0];
        int lastIndex4 = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex4 != 0) {
            int i20 = point9.x;
            if (1 <= lastIndex4) {
                while (true) {
                    int i21 = i + 1;
                    Point point10 = pointArr[i];
                    int i22 = point10.x;
                    if (i20 > i22) {
                        point9 = point10;
                        i20 = i22;
                    }
                    if (i == lastIndex4) {
                        break;
                    }
                    i = i21;
                }
            }
        }
        Intrinsics.checkNotNull(point9);
        return new Rect(point9.x, i9, i14, i19);
    }

    public final Rect fromTransformedSpace(Rect rect) {
        boolean shouldTransformRotate = shouldTransformRotate();
        Size size = this.screenSize;
        int height = shouldTransformRotate ? size.getHeight() : size.getWidth();
        Size size2 = this.screenSize;
        int width = shouldTransformRotate ? size2.getWidth() : size2.getHeight();
        int i = 1;
        Point[] pointArr = {new Point(rect.left, rect.top), new Point(rect.right, rect.top), new Point(rect.right, rect.bottom), new Point(rect.left, rect.bottom)};
        int i2 = 0;
        while (i2 < 4) {
            Point point = pointArr[i2];
            i2++;
            if (shouldTransformFlipX()) {
                point.x = height - point.x;
            }
            if (shouldTransformFlipY()) {
                point.y = width - point.y;
            }
        }
        if (shouldTransformRotate) {
            int i3 = 0;
            while (i3 < 4) {
                Point point2 = pointArr[i3];
                i3++;
                int width2 = point2.y - this.screenSize.getWidth();
                int i4 = point2.x;
                point2.x = -width2;
                point2.y = i4;
            }
        }
        Point point3 = pointArr[0];
        int lastIndex = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex != 0) {
            int i5 = point3.y;
            if (1 <= lastIndex) {
                int i6 = 1;
                while (true) {
                    int i7 = i6 + 1;
                    Point point4 = pointArr[i6];
                    int i8 = point4.y;
                    if (i5 > i8) {
                        point3 = point4;
                        i5 = i8;
                    }
                    if (i6 == lastIndex) {
                        break;
                    }
                    i6 = i7;
                }
            }
        }
        Intrinsics.checkNotNull(point3);
        int i9 = point3.y;
        Point point5 = pointArr[0];
        int lastIndex2 = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex2 != 0) {
            int i10 = point5.x;
            if (1 <= lastIndex2) {
                int i11 = 1;
                while (true) {
                    int i12 = i11 + 1;
                    Point point6 = pointArr[i11];
                    int i13 = point6.x;
                    if (i10 < i13) {
                        point5 = point6;
                        i10 = i13;
                    }
                    if (i11 == lastIndex2) {
                        break;
                    }
                    i11 = i12;
                }
            }
        }
        Intrinsics.checkNotNull(point5);
        int i14 = point5.x;
        Point point7 = pointArr[0];
        int lastIndex3 = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex3 != 0) {
            int i15 = point7.y;
            if (1 <= lastIndex3) {
                int i16 = 1;
                while (true) {
                    int i17 = i16 + 1;
                    Point point8 = pointArr[i16];
                    int i18 = point8.y;
                    if (i15 < i18) {
                        point7 = point8;
                        i15 = i18;
                    }
                    if (i16 == lastIndex3) {
                        break;
                    }
                    i16 = i17;
                }
            }
        }
        Intrinsics.checkNotNull(point7);
        int i19 = point7.y;
        Point point9 = pointArr[0];
        int lastIndex4 = ArraysKt___ArraysKt.getLastIndex(pointArr);
        if (lastIndex4 != 0) {
            int i20 = point9.x;
            if (1 <= lastIndex4) {
                while (true) {
                    int i21 = i + 1;
                    Point point10 = pointArr[i];
                    int i22 = point10.x;
                    if (i20 > i22) {
                        point9 = point10;
                        i20 = i22;
                    }
                    if (i == lastIndex4) {
                        break;
                    }
                    i = i21;
                }
            }
        }
        Intrinsics.checkNotNull(point9);
        return new Rect(point9.x, i9, i14, i19);
    }

    public final Rect getNormalPipAnchorBounds(Size size, Rect rect) {
        if (shouldTransformRotate()) {
            size = new Size(size.getHeight(), size.getWidth());
        }
        Rect rect2 = new Rect();
        if (isPipAnchoredToCorner()) {
            Gravity.apply(85, size.getWidth(), size.getHeight(), rect, rect2);
            return rect2;
        }
        Gravity.apply(5, size.getWidth(), size.getHeight(), rect, rect2);
        return rect2;
    }

    public final boolean isPipAnchoredToCorner() {
        int i = this.pipGravity;
        return (((i & 7) == 3) || ((i & 7) == 5)) && (((i & 112) == 48) || ((i & 112) == 80));
    }

    public final Size addDecors(Size size) {
        Rect rect = new Rect(0, 0, size.getWidth(), size.getHeight());
        rect.inset(this.pipPermanentDecorInsets);
        return new Size(rect.width(), rect.height());
    }

    public final Rect removePermanentDecors(Rect rect) {
        rect.inset(Insets.subtract(Insets.NONE, this.pipPermanentDecorInsets));
        return rect;
    }

    public final Rect offsetCopy(Rect rect, int i, int i2) {
        Rect rect2 = new Rect(rect);
        rect2.offset(i, i2);
        return rect2;
    }

    public final boolean intersectsX(Rect rect, Rect rect2) {
        return rect.right >= rect2.left && rect.left <= rect2.right;
    }

    public final boolean intersectsY(Rect rect, Rect rect2) {
        return rect.bottom >= rect2.top && rect.top <= rect2.bottom;
    }

    public final boolean intersects(Rect rect, Rect rect2) {
        return intersectsX(rect, rect2) && intersectsY(rect, rect2);
    }
}
