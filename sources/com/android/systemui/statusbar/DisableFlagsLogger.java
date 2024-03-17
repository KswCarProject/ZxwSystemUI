package com.android.systemui.statusbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DisableFlagsLogger.kt */
public final class DisableFlagsLogger {
    @NotNull
    public final List<DisableFlag> disable1FlagsList;
    @NotNull
    public final List<DisableFlag> disable2FlagsList;

    public DisableFlagsLogger(@NotNull List<DisableFlag> list, @NotNull List<DisableFlag> list2) {
        this.disable1FlagsList = list;
        this.disable2FlagsList = list2;
        if (flagsListHasDuplicateSymbols(list)) {
            throw new IllegalArgumentException("disable1 flags must have unique symbols");
        } else if (flagsListHasDuplicateSymbols(list2)) {
            throw new IllegalArgumentException("disable2 flags must have unique symbols");
        }
    }

    public DisableFlagsLogger() {
        this(DisableFlagsLoggerKt.defaultDisable1FlagsList, DisableFlagsLoggerKt.defaultDisable2FlagsList);
    }

    public final boolean flagsListHasDuplicateSymbols(List<DisableFlag> list) {
        Iterable<DisableFlag> iterable = list;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (DisableFlag flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core : iterable) {
            arrayList.add(Character.valueOf(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0)));
        }
        int size = CollectionsKt___CollectionsKt.distinct(arrayList).size();
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (DisableFlag flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2 : iterable) {
            arrayList2.add(Character.valueOf(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(Integer.MAX_VALUE)));
        }
        int size2 = CollectionsKt___CollectionsKt.distinct(arrayList2).size();
        Collection collection = list;
        if (size < collection.size() || size2 < collection.size()) {
            return true;
        }
        return false;
    }

    @NotNull
    public final String getDisableFlagsString(@Nullable DisableState disableState, @NotNull DisableState disableState2, @Nullable DisableState disableState3) {
        StringBuilder sb = new StringBuilder("Received new disable state: ");
        if (disableState != null && !Intrinsics.areEqual((Object) disableState, (Object) disableState2)) {
            sb.append("Old: ");
            sb.append(getFlagsString(disableState));
            sb.append(" | ");
            sb.append("New: ");
            sb.append(getFlagsString(disableState2));
            sb.append(" ");
            sb.append(getDiffString(disableState, disableState2));
        } else if (disableState == null || !Intrinsics.areEqual((Object) disableState, (Object) disableState2)) {
            sb.append(getFlagsString(disableState2));
        } else {
            sb.append(getFlagsString(disableState2));
            sb.append(" ");
            sb.append(getDiffString(disableState, disableState2));
        }
        if (disableState3 != null && !Intrinsics.areEqual((Object) disableState2, (Object) disableState3)) {
            sb.append(" | New after local modification: ");
            sb.append(getFlagsString(disableState3));
            sb.append(" ");
            sb.append(getDiffString(disableState2, disableState3));
        }
        return sb.toString();
    }

    public final String getDiffString(DisableState disableState, DisableState disableState2) {
        if (Intrinsics.areEqual((Object) disableState, (Object) disableState2)) {
            return "(unchanged)";
        }
        StringBuilder sb = new StringBuilder("(");
        sb.append("changed: ");
        for (DisableFlag disableFlag : this.disable1FlagsList) {
            char flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core = disableFlag.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState2.getDisable1());
            if (disableFlag.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.getDisable1()) != flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                sb.append(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core);
            }
        }
        sb.append(".");
        for (DisableFlag disableFlag2 : this.disable2FlagsList) {
            char flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2 = disableFlag2.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState2.getDisable2());
            if (disableFlag2.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.getDisable2()) != flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2) {
                sb.append(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public final String getFlagsString(DisableState disableState) {
        StringBuilder sb = new StringBuilder("");
        for (DisableFlag flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core : this.disable1FlagsList) {
            sb.append(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.getDisable1()));
        }
        sb.append(".");
        for (DisableFlag flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2 : this.disable2FlagsList) {
            sb.append(flagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core2.getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(disableState.getDisable2()));
        }
        return sb.toString();
    }

    /* compiled from: DisableFlagsLogger.kt */
    public static final class DisableFlag {
        public final int bitMask;
        public final char flagIsSetSymbol;
        public final char flagNotSetSymbol;

        public DisableFlag(int i, char c, char c2) {
            this.bitMask = i;
            this.flagIsSetSymbol = c;
            this.flagNotSetSymbol = c2;
        }

        public final char getFlagStatus$frameworks__base__packages__SystemUI__android_common__SystemUI_core(int i) {
            if ((i & this.bitMask) != 0) {
                return this.flagIsSetSymbol;
            }
            return this.flagNotSetSymbol;
        }
    }

    /* compiled from: DisableFlagsLogger.kt */
    public static final class DisableState {
        public final int disable1;
        public final int disable2;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DisableState)) {
                return false;
            }
            DisableState disableState = (DisableState) obj;
            return this.disable1 == disableState.disable1 && this.disable2 == disableState.disable2;
        }

        public int hashCode() {
            return (Integer.hashCode(this.disable1) * 31) + Integer.hashCode(this.disable2);
        }

        @NotNull
        public String toString() {
            return "DisableState(disable1=" + this.disable1 + ", disable2=" + this.disable2 + ')';
        }

        public DisableState(int i, int i2) {
            this.disable1 = i;
            this.disable2 = i2;
        }

        public final int getDisable1() {
            return this.disable1;
        }

        public final int getDisable2() {
            return this.disable2;
        }
    }
}
