package com.android.systemui.statusbar.notification.icon;

import android.app.Notification;
import android.app.Person;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Pair;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: IconManager.kt */
public final class IconManager implements ConversationIconManager {
    @NotNull
    public final IconManager$entryListener$1 entryListener = new IconManager$entryListener$1(this);
    @NotNull
    public final IconBuilder iconBuilder;
    @NotNull
    public final LauncherApps launcherApps;
    @NotNull
    public final CommonNotifCollection notifCollection;
    @NotNull
    public final NotificationEntry.OnSensitivityChangedListener sensitivityListener = new IconManager$sensitivityListener$1(this);
    @NotNull
    public Set<String> unimportantConversationKeys = SetsKt__SetsKt.emptySet();

    public IconManager(@NotNull CommonNotifCollection commonNotifCollection, @NotNull LauncherApps launcherApps2, @NotNull IconBuilder iconBuilder2) {
        this.notifCollection = commonNotifCollection;
        this.launcherApps = launcherApps2;
        this.iconBuilder = iconBuilder2;
    }

    public final void attach() {
        this.notifCollection.addCollectionListener(this.entryListener);
    }

    public final void recalculateForImportantConversationChange() {
        for (NotificationEntry next : this.notifCollection.getAllNotifs()) {
            boolean isImportantConversation = isImportantConversation(next);
            if (next.getIcons().getAreIconsAvailable() && isImportantConversation != next.getIcons().isImportantConversation()) {
                updateIconsSafe(next);
            }
            next.getIcons().setImportantConversation(isImportantConversation);
        }
    }

    public final void createIcons(@NotNull NotificationEntry notificationEntry) throws InflationException {
        StatusBarIconView statusBarIconView;
        StatusBarIconView createIconView = this.iconBuilder.createIconView(notificationEntry);
        createIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        StatusBarIconView createIconView2 = this.iconBuilder.createIconView(notificationEntry);
        createIconView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView2.setVisibility(4);
        StatusBarIconView createIconView3 = this.iconBuilder.createIconView(notificationEntry);
        createIconView3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView3.setIncreasedSize(true);
        if (notificationEntry.getSbn().getNotification().isMediaNotification()) {
            statusBarIconView = this.iconBuilder.createIconView(notificationEntry);
            statusBarIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            statusBarIconView = null;
        }
        Pair<StatusBarIcon, StatusBarIcon> iconDescriptors = getIconDescriptors(notificationEntry);
        StatusBarIcon component1 = iconDescriptors.component1();
        StatusBarIcon component2 = iconDescriptors.component2();
        try {
            setIcon(notificationEntry, component1, createIconView);
            setIcon(notificationEntry, component2, createIconView2);
            setIcon(notificationEntry, component2, createIconView3);
            if (statusBarIconView != null) {
                setIcon(notificationEntry, component1, statusBarIconView);
            }
            notificationEntry.setIcons(IconPack.buildPack(createIconView, createIconView2, createIconView3, statusBarIconView, notificationEntry.getIcons()));
        } catch (InflationException e) {
            notificationEntry.setIcons(IconPack.buildEmptyPack(notificationEntry.getIcons()));
            throw e;
        }
    }

    public final void updateIcons(@NotNull NotificationEntry notificationEntry) throws InflationException {
        if (notificationEntry.getIcons().getAreIconsAvailable()) {
            notificationEntry.getIcons().setSmallIconDescriptor((StatusBarIcon) null);
            notificationEntry.getIcons().setPeopleAvatarDescriptor((StatusBarIcon) null);
            Pair<StatusBarIcon, StatusBarIcon> iconDescriptors = getIconDescriptors(notificationEntry);
            StatusBarIcon component1 = iconDescriptors.component1();
            StatusBarIcon component2 = iconDescriptors.component2();
            StatusBarIconView statusBarIcon = notificationEntry.getIcons().getStatusBarIcon();
            if (statusBarIcon != null) {
                statusBarIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component1, statusBarIcon);
            }
            StatusBarIconView shelfIcon = notificationEntry.getIcons().getShelfIcon();
            if (shelfIcon != null) {
                shelfIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component1, shelfIcon);
            }
            StatusBarIconView aodIcon = notificationEntry.getIcons().getAodIcon();
            if (aodIcon != null) {
                aodIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component2, aodIcon);
            }
            StatusBarIconView centeredIcon = notificationEntry.getIcons().getCenteredIcon();
            if (centeredIcon != null) {
                centeredIcon.setNotification(notificationEntry.getSbn());
                setIcon(notificationEntry, component2, centeredIcon);
            }
        }
    }

    public final void updateIconsSafe(NotificationEntry notificationEntry) {
        try {
            updateIcons(notificationEntry);
        } catch (InflationException e) {
            Log.e("IconManager", "Unable to update icon", e);
        }
    }

    public final Pair<StatusBarIcon, StatusBarIcon> getIconDescriptors(NotificationEntry notificationEntry) throws InflationException {
        StatusBarIcon iconDescriptor = getIconDescriptor(notificationEntry, false);
        return new Pair<>(iconDescriptor, notificationEntry.isSensitive() ? getIconDescriptor(notificationEntry, true) : iconDescriptor);
    }

    public final StatusBarIcon getIconDescriptor(NotificationEntry notificationEntry, boolean z) throws InflationException {
        Icon icon;
        Notification notification = notificationEntry.getSbn().getNotification();
        boolean z2 = isImportantConversation(notificationEntry) && !z;
        StatusBarIcon peopleAvatarDescriptor = notificationEntry.getIcons().getPeopleAvatarDescriptor();
        StatusBarIcon smallIconDescriptor = notificationEntry.getIcons().getSmallIconDescriptor();
        if (z2 && peopleAvatarDescriptor != null) {
            return peopleAvatarDescriptor;
        }
        if (!z2 && smallIconDescriptor != null) {
            return smallIconDescriptor;
        }
        if (z2) {
            icon = createPeopleAvatar(notificationEntry);
        } else {
            icon = notification.getSmallIcon();
        }
        Icon icon2 = icon;
        if (icon2 != null) {
            StatusBarIcon statusBarIcon = new StatusBarIcon(notificationEntry.getSbn().getUser(), notificationEntry.getSbn().getPackageName(), icon2, notification.iconLevel, notification.number, this.iconBuilder.getIconContentDescription(notification));
            if (isImportantConversation(notificationEntry)) {
                if (z2) {
                    notificationEntry.getIcons().setPeopleAvatarDescriptor(statusBarIcon);
                } else {
                    notificationEntry.getIcons().setSmallIconDescriptor(statusBarIcon);
                }
            }
            return statusBarIcon;
        }
        throw new InflationException(Intrinsics.stringPlus("No icon in notification from ", notificationEntry.getSbn().getPackageName()));
    }

    public final void setIcon(NotificationEntry notificationEntry, StatusBarIcon statusBarIcon, StatusBarIconView statusBarIconView) throws InflationException {
        statusBarIconView.setShowsConversation(showsConversation(notificationEntry, statusBarIconView, statusBarIcon));
        statusBarIconView.setTag(R$id.icon_is_pre_L, Boolean.valueOf(notificationEntry.targetSdk < 21));
        if (!statusBarIconView.set(statusBarIcon)) {
            throw new InflationException(Intrinsics.stringPlus("Couldn't create icon ", statusBarIcon));
        }
    }

    public final Icon createPeopleAvatar(NotificationEntry notificationEntry) throws InflationException {
        ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
        Icon shortcutIcon = conversationShortcutInfo != null ? this.launcherApps.getShortcutIcon(conversationShortcutInfo) : null;
        if (shortcutIcon == null) {
            Bundle bundle = notificationEntry.getSbn().getNotification().extras;
            List messagesFromBundleArray = Notification.MessagingStyle.Message.getMessagesFromBundleArray(bundle.getParcelableArray("android.messages"));
            Person person = (Person) bundle.getParcelable("android.messagingUser");
            int size = messagesFromBundleArray.size() - 1;
            if (size >= 0) {
                while (true) {
                    int i = size - 1;
                    Notification.MessagingStyle.Message message = (Notification.MessagingStyle.Message) messagesFromBundleArray.get(size);
                    Person senderPerson = message.getSenderPerson();
                    if (senderPerson != null && senderPerson != person) {
                        Person senderPerson2 = message.getSenderPerson();
                        Intrinsics.checkNotNull(senderPerson2);
                        shortcutIcon = senderPerson2.getIcon();
                        break;
                    } else if (i < 0) {
                        break;
                    } else {
                        size = i;
                    }
                }
            }
        }
        if (shortcutIcon == null) {
            shortcutIcon = notificationEntry.getSbn().getNotification().getLargeIcon();
        }
        if (shortcutIcon == null) {
            shortcutIcon = notificationEntry.getSbn().getNotification().getSmallIcon();
        }
        if (shortcutIcon != null) {
            return shortcutIcon;
        }
        throw new InflationException(Intrinsics.stringPlus("No icon in notification from ", notificationEntry.getSbn().getPackageName()));
    }

    public final boolean showsConversation(NotificationEntry notificationEntry, StatusBarIconView statusBarIconView, StatusBarIcon statusBarIcon) {
        boolean z = statusBarIconView == notificationEntry.getIcons().getShelfIcon() || statusBarIconView == notificationEntry.getIcons().getAodIcon();
        boolean equals = statusBarIcon.icon.equals(notificationEntry.getSbn().getNotification().getSmallIcon());
        if (!isImportantConversation(notificationEntry) || equals) {
            return false;
        }
        if (!z || !notificationEntry.isSensitive()) {
            return true;
        }
        return false;
    }

    public final boolean isImportantConversation(NotificationEntry notificationEntry) {
        return notificationEntry.getRanking().getChannel() != null && notificationEntry.getRanking().getChannel().isImportantConversation() && !this.unimportantConversationKeys.contains(notificationEntry.getKey());
    }

    public void setUnimportantConversations(@NotNull Collection<String> collection) {
        Set<String> set = CollectionsKt___CollectionsKt.toSet(collection);
        boolean z = !Intrinsics.areEqual((Object) this.unimportantConversationKeys, (Object) set);
        this.unimportantConversationKeys = set;
        if (z) {
            recalculateForImportantConversationChange();
        }
    }
}
