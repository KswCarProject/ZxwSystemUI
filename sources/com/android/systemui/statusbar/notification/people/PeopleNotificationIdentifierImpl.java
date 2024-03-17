package com.android.systemui.statusbar.notification.people;

import android.app.NotificationChannel;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: PeopleNotificationIdentifier.kt */
public final class PeopleNotificationIdentifierImpl implements PeopleNotificationIdentifier {
    @NotNull
    public final GroupMembershipManager groupManager;
    @NotNull
    public final NotificationPersonExtractor personExtractor;

    public PeopleNotificationIdentifierImpl(@NotNull NotificationPersonExtractor notificationPersonExtractor, @NotNull GroupMembershipManager groupMembershipManager) {
        this.personExtractor = notificationPersonExtractor;
        this.groupManager = groupMembershipManager;
    }

    public int getPeopleNotificationType(@NotNull NotificationEntry notificationEntry) {
        int upperBound;
        int personTypeInfo = getPersonTypeInfo(notificationEntry.getRanking());
        if (personTypeInfo == 3 || (upperBound = upperBound(personTypeInfo, extractPersonTypeInfo(notificationEntry.getSbn()))) == 3) {
            return 3;
        }
        return upperBound(upperBound, getPeopleTypeOfSummary(notificationEntry));
    }

    public int compareTo(int i, int i2) {
        return Intrinsics.compare(i2, i);
    }

    public final int upperBound(int i, int i2) {
        return Math.max(i, i2);
    }

    public final int getPersonTypeInfo(NotificationListenerService.Ranking ranking) {
        boolean z = true;
        if (!ranking.isConversation()) {
            return 0;
        }
        if (ranking.getConversationShortcutInfo() == null) {
            return 1;
        }
        NotificationChannel channel = ranking.getChannel();
        if (channel == null || !channel.isImportantConversation()) {
            z = false;
        }
        return z ? 3 : 2;
    }

    public final int extractPersonTypeInfo(StatusBarNotification statusBarNotification) {
        return this.personExtractor.isPersonNotification(statusBarNotification) ? 1 : 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002d A[LOOP:0: B:11:0x002d->B:14:0x0042, LOOP_START, PHI: r1 
      PHI: (r1v1 int) = (r1v0 int), (r1v3 int) binds: [B:10:0x0029, B:14:0x0042] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int getPeopleTypeOfSummary(com.android.systemui.statusbar.notification.collection.NotificationEntry r3) {
        /*
            r2 = this;
            com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r0 = r2.groupManager
            boolean r0 = r0.isGroupSummary(r3)
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r0 = r2.groupManager
            java.util.List r3 = r0.getChildren(r3)
            r0 = 0
            if (r3 != 0) goto L_0x0014
            goto L_0x0026
        L_0x0014:
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            kotlin.sequences.Sequence r3 = kotlin.collections.CollectionsKt___CollectionsKt.asSequence(r3)
            if (r3 != 0) goto L_0x001d
            goto L_0x0026
        L_0x001d:
            com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1 r0 = new com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1
            r0.<init>(r2)
            kotlin.sequences.Sequence r0 = kotlin.sequences.SequencesKt___SequencesKt.map(r3, r0)
        L_0x0026:
            if (r0 != 0) goto L_0x0029
            return r1
        L_0x0029:
            java.util.Iterator r3 = r0.iterator()
        L_0x002d:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x0044
            java.lang.Object r0 = r3.next()
            java.lang.Number r0 = (java.lang.Number) r0
            int r0 = r0.intValue()
            int r1 = r2.upperBound(r1, r0)
            r0 = 3
            if (r1 != r0) goto L_0x002d
        L_0x0044:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl.getPeopleTypeOfSummary(com.android.systemui.statusbar.notification.collection.NotificationEntry):int");
    }
}
