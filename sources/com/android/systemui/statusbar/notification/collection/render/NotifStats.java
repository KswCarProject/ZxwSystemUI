package com.android.systemui.statusbar.notification.collection.render;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifStackController.kt */
public final class NotifStats {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final NotifStats empty = new NotifStats(0, false, false, false, false);
    public final boolean hasClearableAlertingNotifs;
    public final boolean hasClearableSilentNotifs;
    public final boolean hasNonClearableAlertingNotifs;
    public final boolean hasNonClearableSilentNotifs;
    public final int numActiveNotifs;

    @NotNull
    public static final NotifStats getEmpty() {
        return Companion.getEmpty();
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NotifStats)) {
            return false;
        }
        NotifStats notifStats = (NotifStats) obj;
        return this.numActiveNotifs == notifStats.numActiveNotifs && this.hasNonClearableAlertingNotifs == notifStats.hasNonClearableAlertingNotifs && this.hasClearableAlertingNotifs == notifStats.hasClearableAlertingNotifs && this.hasNonClearableSilentNotifs == notifStats.hasNonClearableSilentNotifs && this.hasClearableSilentNotifs == notifStats.hasClearableSilentNotifs;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.numActiveNotifs) * 31;
        boolean z = this.hasNonClearableAlertingNotifs;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (hashCode + (z ? 1 : 0)) * 31;
        boolean z3 = this.hasClearableAlertingNotifs;
        if (z3) {
            z3 = true;
        }
        int i2 = (i + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.hasNonClearableSilentNotifs;
        if (z4) {
            z4 = true;
        }
        int i3 = (i2 + (z4 ? 1 : 0)) * 31;
        boolean z5 = this.hasClearableSilentNotifs;
        if (!z5) {
            z2 = z5;
        }
        return i3 + (z2 ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "NotifStats(numActiveNotifs=" + this.numActiveNotifs + ", hasNonClearableAlertingNotifs=" + this.hasNonClearableAlertingNotifs + ", hasClearableAlertingNotifs=" + this.hasClearableAlertingNotifs + ", hasNonClearableSilentNotifs=" + this.hasNonClearableSilentNotifs + ", hasClearableSilentNotifs=" + this.hasClearableSilentNotifs + ')';
    }

    public NotifStats(int i, boolean z, boolean z2, boolean z3, boolean z4) {
        this.numActiveNotifs = i;
        this.hasNonClearableAlertingNotifs = z;
        this.hasClearableAlertingNotifs = z2;
        this.hasNonClearableSilentNotifs = z3;
        this.hasClearableSilentNotifs = z4;
    }

    public final int getNumActiveNotifs() {
        return this.numActiveNotifs;
    }

    public final boolean getHasNonClearableAlertingNotifs() {
        return this.hasNonClearableAlertingNotifs;
    }

    public final boolean getHasClearableAlertingNotifs() {
        return this.hasClearableAlertingNotifs;
    }

    public final boolean getHasNonClearableSilentNotifs() {
        return this.hasNonClearableSilentNotifs;
    }

    public final boolean getHasClearableSilentNotifs() {
        return this.hasClearableSilentNotifs;
    }

    /* compiled from: NotifStackController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final NotifStats getEmpty() {
            return NotifStats.empty;
        }
    }
}
