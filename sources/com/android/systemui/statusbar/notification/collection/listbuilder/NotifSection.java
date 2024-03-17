package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifSection.kt */
public final class NotifSection {
    public final int bucket;
    @Nullable
    public final NotifComparator comparator;
    @Nullable
    public final NodeController headerController;
    public final int index;
    @NotNull
    public final NotifSectioner sectioner;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NotifSection)) {
            return false;
        }
        NotifSection notifSection = (NotifSection) obj;
        return Intrinsics.areEqual((Object) this.sectioner, (Object) notifSection.sectioner) && this.index == notifSection.index;
    }

    public int hashCode() {
        return (this.sectioner.hashCode() * 31) + Integer.hashCode(this.index);
    }

    @NotNull
    public String toString() {
        return "NotifSection(sectioner=" + this.sectioner + ", index=" + this.index + ')';
    }

    public NotifSection(@NotNull NotifSectioner notifSectioner, int i) {
        this.sectioner = notifSectioner;
        this.index = i;
        this.headerController = notifSectioner.getHeaderNodeController();
        this.comparator = notifSectioner.getComparator();
        this.bucket = notifSectioner.getBucket();
    }

    @NotNull
    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    public final int getIndex() {
        return this.index;
    }

    @NotNull
    public final String getLabel() {
        return "Section(" + this.index + ", " + this.bucket + ", \"" + this.sectioner.getName() + "\")";
    }

    @Nullable
    public final NodeController getHeaderController() {
        return this.headerController;
    }

    @Nullable
    public final NotifComparator getComparator() {
        return this.comparator;
    }

    public final int getBucket() {
        return this.bucket;
    }
}
