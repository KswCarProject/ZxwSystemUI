package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import java.util.Collection;
import java.util.Set;
import kotlin.collections.CollectionsKt___CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: SectionClassifier.kt */
public final class SectionClassifier {
    public Set<? extends NotifSectioner> lowPrioritySections;

    public final void setMinimizedSections(@NotNull Collection<? extends NotifSectioner> collection) {
        this.lowPrioritySections = CollectionsKt___CollectionsKt.toSet(collection);
    }

    public final boolean isMinimizedSection(@NotNull NotifSection notifSection) {
        Set<? extends NotifSectioner> set = this.lowPrioritySections;
        if (set == null) {
            set = null;
        }
        return set.contains(notifSection.getSectioner());
    }
}
