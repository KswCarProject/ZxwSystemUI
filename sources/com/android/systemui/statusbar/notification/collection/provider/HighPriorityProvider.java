package com.android.systemui.statusbar.notification.collection.provider;

import android.app.Notification;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import java.util.List;

public class HighPriorityProvider {
    public final GroupMembershipManager mGroupMembershipManager;
    public final PeopleNotificationIdentifier mPeopleNotificationIdentifier;

    public HighPriorityProvider(PeopleNotificationIdentifier peopleNotificationIdentifier, GroupMembershipManager groupMembershipManager) {
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
        this.mGroupMembershipManager = groupMembershipManager;
    }

    public boolean isHighPriority(ListEntry listEntry) {
        NotificationEntry representativeEntry;
        if (listEntry == null || (representativeEntry = listEntry.getRepresentativeEntry()) == null) {
            return false;
        }
        if (representativeEntry.getRanking().getImportance() >= 3 || hasHighPriorityCharacteristics(representativeEntry) || hasHighPriorityChild(listEntry)) {
            return true;
        }
        return false;
    }

    public final boolean hasHighPriorityChild(ListEntry listEntry) {
        List<NotificationEntry> children;
        if ((!(listEntry instanceof NotificationEntry) || this.mGroupMembershipManager.isGroupSummary((NotificationEntry) listEntry)) && (children = this.mGroupMembershipManager.getChildren(listEntry)) != null) {
            for (NotificationEntry next : children) {
                if (next != listEntry && isHighPriority(next)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean hasHighPriorityCharacteristics(NotificationEntry notificationEntry) {
        return !hasUserSetImportance(notificationEntry) && (notificationEntry.getSbn().getNotification().isMediaNotification() || isPeopleNotification(notificationEntry) || isMessagingStyle(notificationEntry));
    }

    public final boolean isMessagingStyle(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().isStyle(Notification.MessagingStyle.class);
    }

    public final boolean isPeopleNotification(NotificationEntry notificationEntry) {
        return this.mPeopleNotificationIdentifier.getPeopleNotificationType(notificationEntry) != 0;
    }

    public final boolean hasUserSetImportance(NotificationEntry notificationEntry) {
        return notificationEntry.getRanking().getChannel() != null && notificationEntry.getRanking().getChannel().hasUserSetImportance();
    }
}
