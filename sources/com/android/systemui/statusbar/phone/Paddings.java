package com.android.systemui.statusbar.phone;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationsQSContainerController.kt */
public final class Paddings {
    public final int containerPadding;
    public final int notificationsMargin;
    public final int qsContainerPadding;

    public final int component1() {
        return this.containerPadding;
    }

    public final int component2() {
        return this.notificationsMargin;
    }

    public final int component3() {
        return this.qsContainerPadding;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Paddings)) {
            return false;
        }
        Paddings paddings = (Paddings) obj;
        return this.containerPadding == paddings.containerPadding && this.notificationsMargin == paddings.notificationsMargin && this.qsContainerPadding == paddings.qsContainerPadding;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.containerPadding) * 31) + Integer.hashCode(this.notificationsMargin)) * 31) + Integer.hashCode(this.qsContainerPadding);
    }

    @NotNull
    public String toString() {
        return "Paddings(containerPadding=" + this.containerPadding + ", notificationsMargin=" + this.notificationsMargin + ", qsContainerPadding=" + this.qsContainerPadding + ')';
    }

    public Paddings(int i, int i2, int i3) {
        this.containerPadding = i;
        this.notificationsMargin = i2;
        this.qsContainerPadding = i3;
    }
}
