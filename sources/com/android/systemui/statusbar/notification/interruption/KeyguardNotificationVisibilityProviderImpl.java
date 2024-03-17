package com.android.systemui.statusbar.notification.interruption;

import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.util.IndentingPrintWriter;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.CoreStartable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardNotificationVisibilityProvider.kt */
final class KeyguardNotificationVisibilityProviderImpl extends CoreStartable implements KeyguardNotificationVisibilityProvider {
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final GlobalSettings globalSettings;
    @NotNull
    public final Handler handler;
    public boolean hideSilentNotificationsOnLockscreen;
    @NotNull
    public final HighPriorityProvider highPriorityProvider;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    public final NotificationLockscreenUserManager lockscreenUserManager;
    @NotNull
    public final ListenerSet<Consumer<String>> onStateChangedListeners = new ListenerSet<>();
    @NotNull
    public final SecureSettings secureSettings;
    public final Uri showSilentNotifsUri;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;

    public KeyguardNotificationVisibilityProviderImpl(@NotNull Context context, @NotNull Handler handler2, @NotNull KeyguardStateController keyguardStateController2, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor2, @NotNull HighPriorityProvider highPriorityProvider2, @NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull SecureSettings secureSettings2, @NotNull GlobalSettings globalSettings2) {
        super(context);
        this.handler = handler2;
        this.keyguardStateController = keyguardStateController2;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.highPriorityProvider = highPriorityProvider2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.secureSettings = secureSettings2;
        this.globalSettings = globalSettings2;
        this.showSilentNotifsUri = secureSettings2.getUriFor("lock_screen_show_silent_notifications");
    }

    public void start() {
        readShowSilentNotificationSetting();
        this.keyguardStateController.addCallback(new KeyguardNotificationVisibilityProviderImpl$start$1(this));
        this.keyguardUpdateMonitor.registerCallback(new KeyguardNotificationVisibilityProviderImpl$start$2(this));
        KeyguardNotificationVisibilityProviderImpl$start$settingsObserver$1 keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1 = new KeyguardNotificationVisibilityProviderImpl$start$settingsObserver$1(this, this.handler);
        this.secureSettings.registerContentObserverForUser("lock_screen_show_notifications", (ContentObserver) keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1, -1);
        this.secureSettings.registerContentObserverForUser("lock_screen_allow_private_notifications", true, (ContentObserver) keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1, -1);
        this.globalSettings.registerContentObserver("zen_mode", (ContentObserver) keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1);
        this.secureSettings.registerContentObserverForUser("lock_screen_show_silent_notifications", (ContentObserver) keyguardNotificationVisibilityProviderImpl$start$settingsObserver$1, -1);
        this.statusBarStateController.addCallback(new KeyguardNotificationVisibilityProviderImpl$start$3(this));
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, new KeyguardNotificationVisibilityProviderImpl$start$4(this), new IntentFilter("android.intent.action.USER_SWITCHED"), (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }

    public void addOnStateChangedListener(@NotNull Consumer<String> consumer) {
        this.onStateChangedListeners.addIfAbsent(consumer);
    }

    public final void notifyStateChanged(String str) {
        for (Consumer accept : this.onStateChangedListeners) {
            accept.accept(str);
        }
    }

    public boolean shouldHideNotification(@NotNull NotificationEntry notificationEntry) {
        if (!isLockedOrLocking()) {
            return false;
        }
        if (this.lockscreenUserManager.shouldShowLockscreenNotifications() && !userSettingsDisallowNotification(notificationEntry) && !shouldHideIfEntrySilent(notificationEntry)) {
            return false;
        }
        return true;
    }

    public final boolean shouldHideIfEntrySilent(ListEntry listEntry) {
        if (!this.highPriorityProvider.isHighPriority(listEntry)) {
            NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
            if ((representativeEntry != null && representativeEntry.isAmbient()) || this.hideSilentNotificationsOnLockscreen) {
                return true;
            }
            GroupEntry parent = listEntry.getParent();
            if (parent != null) {
                shouldHideIfEntrySilent(parent);
            }
        }
        return false;
    }

    public static final boolean userSettingsDisallowNotification$disallowForUser(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl, NotificationEntry notificationEntry, int i) {
        if (!keyguardNotificationVisibilityProviderImpl.keyguardUpdateMonitor.isUserInLockdown(i)) {
            if (!keyguardNotificationVisibilityProviderImpl.lockscreenUserManager.isLockscreenPublicMode(i)) {
                return false;
            }
            if (notificationEntry.getRanking().getLockscreenVisibilityOverride() != -1 && keyguardNotificationVisibilityProviderImpl.lockscreenUserManager.userAllowsNotificationsInPublic(i)) {
                return false;
            }
        }
        return true;
    }

    public final boolean userSettingsDisallowNotification(NotificationEntry notificationEntry) {
        int currentUserId = this.lockscreenUserManager.getCurrentUserId();
        int identifier = notificationEntry.getSbn().getUser().getIdentifier();
        if (userSettingsDisallowNotification$disallowForUser(this, notificationEntry, currentUserId)) {
            return true;
        }
        if (identifier == -1 || identifier == currentUserId) {
            return false;
        }
        return userSettingsDisallowNotification$disallowForUser(this, notificationEntry, identifier);
    }

    /* JADX INFO: finally extract failed */
    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println(Intrinsics.stringPlus("isLockedOrLocking=", Boolean.valueOf(isLockedOrLocking())));
        asIndenting.increaseIndent();
        try {
            asIndenting.println(Intrinsics.stringPlus("keyguardStateController.isShowing=", Boolean.valueOf(this.keyguardStateController.isShowing())));
            asIndenting.println(Intrinsics.stringPlus("statusBarStateController.currentOrUpcomingState=", Integer.valueOf(this.statusBarStateController.getCurrentOrUpcomingState())));
            asIndenting.decreaseIndent();
            asIndenting.println(Intrinsics.stringPlus("hideSilentNotificationsOnLockscreen=", Boolean.valueOf(this.hideSilentNotificationsOnLockscreen)));
        } catch (Throwable th) {
            asIndenting.decreaseIndent();
            throw th;
        }
    }

    public final boolean isLockedOrLocking() {
        if (this.keyguardStateController.isShowing() || this.statusBarStateController.getCurrentOrUpcomingState() == 1) {
            return true;
        }
        return false;
    }

    public final void readShowSilentNotificationSetting() {
        this.hideSilentNotificationsOnLockscreen = !this.secureSettings.getBoolForUser("lock_screen_show_silent_notifications", true, -2);
    }
}
