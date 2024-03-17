package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.util.ListenerSet;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifUiAdjustmentProvider.kt */
public final class NotifUiAdjustmentProvider {
    @NotNull
    public final ListenerSet<Runnable> dirtyListeners = new ListenerSet<>();
    @NotNull
    public final NotificationLockscreenUserManager lockscreenUserManager;
    @NotNull
    public final NotificationLockscreenUserManager.NotificationStateChangedListener notifStateChangedListener = new NotifUiAdjustmentProvider$notifStateChangedListener$1(this);
    @NotNull
    public final SectionClassifier sectionClassifier;

    public NotifUiAdjustmentProvider(@NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull SectionClassifier sectionClassifier2) {
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.sectionClassifier = sectionClassifier2;
    }

    public final void addDirtyListener(@NotNull Runnable runnable) {
        if (this.dirtyListeners.isEmpty()) {
            this.lockscreenUserManager.addNotificationStateChangedListener(this.notifStateChangedListener);
        }
        this.dirtyListeners.addIfAbsent(runnable);
    }

    public final boolean isEntryMinimized(NotificationEntry notificationEntry) {
        NotifSection section = notificationEntry.getSection();
        if (section != null) {
            GroupEntry parent = notificationEntry.getParent();
            if (parent != null) {
                return this.sectionClassifier.isMinimizedSection(section) && (Intrinsics.areEqual((Object) parent, (Object) GroupEntry.ROOT_ENTRY) || Intrinsics.areEqual((Object) parent.getSummary(), (Object) notificationEntry));
            }
            throw new IllegalStateException("Entry must have a parent to determine if minimized".toString());
        }
        throw new IllegalStateException("Entry must have a section to determine if minimized".toString());
    }

    @NotNull
    public final NotifUiAdjustment calculateAdjustment(@NotNull NotificationEntry notificationEntry) {
        return new NotifUiAdjustment(notificationEntry.getKey(), notificationEntry.getRanking().getSmartActions(), notificationEntry.getRanking().getSmartReplies(), notificationEntry.getRanking().isConversation(), isEntryMinimized(notificationEntry), this.lockscreenUserManager.needsRedaction(notificationEntry));
    }
}
