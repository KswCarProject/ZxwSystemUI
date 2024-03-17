package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ListAttachState.kt */
public final class ListAttachState {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public NotifFilter excludingFilter;
    @Nullable
    public String groupPruneReason;
    @Nullable
    public GroupEntry parent;
    @Nullable
    public NotifPromoter promoter;
    @Nullable
    public NotifSection section;
    public int stableIndex;
    @NotNull
    public SuppressedAttachState suppressedChanges;

    public /* synthetic */ ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, String str, SuppressedAttachState suppressedAttachState, DefaultConstructorMarker defaultConstructorMarker) {
        this(groupEntry, notifSection, notifFilter, notifPromoter, str, suppressedAttachState);
    }

    @NotNull
    public static final ListAttachState create() {
        return Companion.create();
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ListAttachState)) {
            return false;
        }
        ListAttachState listAttachState = (ListAttachState) obj;
        return Intrinsics.areEqual((Object) this.parent, (Object) listAttachState.parent) && Intrinsics.areEqual((Object) this.section, (Object) listAttachState.section) && Intrinsics.areEqual((Object) this.excludingFilter, (Object) listAttachState.excludingFilter) && Intrinsics.areEqual((Object) this.promoter, (Object) listAttachState.promoter) && Intrinsics.areEqual((Object) this.groupPruneReason, (Object) listAttachState.groupPruneReason) && Intrinsics.areEqual((Object) this.suppressedChanges, (Object) listAttachState.suppressedChanges);
    }

    public int hashCode() {
        GroupEntry groupEntry = this.parent;
        int i = 0;
        int hashCode = (groupEntry == null ? 0 : groupEntry.hashCode()) * 31;
        NotifSection notifSection = this.section;
        int hashCode2 = (hashCode + (notifSection == null ? 0 : notifSection.hashCode())) * 31;
        NotifFilter notifFilter = this.excludingFilter;
        int hashCode3 = (hashCode2 + (notifFilter == null ? 0 : notifFilter.hashCode())) * 31;
        NotifPromoter notifPromoter = this.promoter;
        int hashCode4 = (hashCode3 + (notifPromoter == null ? 0 : notifPromoter.hashCode())) * 31;
        String str = this.groupPruneReason;
        if (str != null) {
            i = str.hashCode();
        }
        return ((hashCode4 + i) * 31) + this.suppressedChanges.hashCode();
    }

    @NotNull
    public String toString() {
        return "ListAttachState(parent=" + this.parent + ", section=" + this.section + ", excludingFilter=" + this.excludingFilter + ", promoter=" + this.promoter + ", groupPruneReason=" + this.groupPruneReason + ", suppressedChanges=" + this.suppressedChanges + ')';
    }

    public ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, String str, SuppressedAttachState suppressedAttachState) {
        this.parent = groupEntry;
        this.section = notifSection;
        this.excludingFilter = notifFilter;
        this.promoter = notifPromoter;
        this.groupPruneReason = str;
        this.suppressedChanges = suppressedAttachState;
        this.stableIndex = -1;
    }

    @Nullable
    public final GroupEntry getParent() {
        return this.parent;
    }

    public final void setParent(@Nullable GroupEntry groupEntry) {
        this.parent = groupEntry;
    }

    @Nullable
    public final NotifSection getSection() {
        return this.section;
    }

    public final void setSection(@Nullable NotifSection notifSection) {
        this.section = notifSection;
    }

    @Nullable
    public final NotifFilter getExcludingFilter() {
        return this.excludingFilter;
    }

    public final void setExcludingFilter(@Nullable NotifFilter notifFilter) {
        this.excludingFilter = notifFilter;
    }

    @Nullable
    public final NotifPromoter getPromoter() {
        return this.promoter;
    }

    public final void setPromoter(@Nullable NotifPromoter notifPromoter) {
        this.promoter = notifPromoter;
    }

    @Nullable
    public final String getGroupPruneReason() {
        return this.groupPruneReason;
    }

    public final void setGroupPruneReason(@Nullable String str) {
        this.groupPruneReason = str;
    }

    @NotNull
    public final SuppressedAttachState getSuppressedChanges() {
        return this.suppressedChanges;
    }

    public final int getStableIndex() {
        return this.stableIndex;
    }

    public final void setStableIndex(int i) {
        this.stableIndex = i;
    }

    public final void clone(@NotNull ListAttachState listAttachState) {
        this.parent = listAttachState.parent;
        this.section = listAttachState.section;
        this.excludingFilter = listAttachState.excludingFilter;
        this.promoter = listAttachState.promoter;
        this.groupPruneReason = listAttachState.groupPruneReason;
        this.suppressedChanges.clone(listAttachState.suppressedChanges);
        this.stableIndex = listAttachState.stableIndex;
    }

    public final void reset() {
        this.parent = null;
        this.section = null;
        this.excludingFilter = null;
        this.promoter = null;
        this.groupPruneReason = null;
        this.suppressedChanges.reset();
        this.stableIndex = -1;
    }

    /* compiled from: ListAttachState.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final ListAttachState create() {
            return new ListAttachState((GroupEntry) null, (NotifSection) null, (NotifFilter) null, (NotifPromoter) null, (String) null, SuppressedAttachState.Companion.create(), (DefaultConstructorMarker) null);
        }
    }
}
