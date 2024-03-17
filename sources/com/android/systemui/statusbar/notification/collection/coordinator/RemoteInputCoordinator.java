package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.InternalNotifUpdater;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender;
import java.io.PrintWriter;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: RemoteInputCoordinator.kt */
public final class RemoteInputCoordinator implements Coordinator, NotificationRemoteInputManager.RemoteInputListener, Dumpable {
    @NotNull
    public final NotifCollectionListener mCollectionListener = new RemoteInputCoordinator$mCollectionListener$1(this);
    @NotNull
    public final Handler mMainHandler;
    public InternalNotifUpdater mNotifUpdater;
    @NotNull
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    @NotNull
    public final RemoteInputNotificationRebuilder mRebuilder;
    @NotNull
    public final RemoteInputActiveExtender mRemoteInputActiveExtender;
    @NotNull
    public final RemoteInputHistoryExtender mRemoteInputHistoryExtender;
    @NotNull
    public final List<SelfTrackingLifetimeExtender> mRemoteInputLifetimeExtenders;
    @NotNull
    public final SmartReplyController mSmartReplyController;
    @NotNull
    public final SmartReplyHistoryExtender mSmartReplyHistoryExtender;

    public static /* synthetic */ void getMRemoteInputActiveExtender$annotations() {
    }

    public static /* synthetic */ void getMRemoteInputHistoryExtender$annotations() {
    }

    public static /* synthetic */ void getMSmartReplyHistoryExtender$annotations() {
    }

    public RemoteInputCoordinator(@NotNull DumpManager dumpManager, @NotNull RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, @NotNull NotificationRemoteInputManager notificationRemoteInputManager, @NotNull Handler handler, @NotNull SmartReplyController smartReplyController) {
        this.mRebuilder = remoteInputNotificationRebuilder;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mMainHandler = handler;
        this.mSmartReplyController = smartReplyController;
        RemoteInputHistoryExtender remoteInputHistoryExtender = new RemoteInputHistoryExtender();
        this.mRemoteInputHistoryExtender = remoteInputHistoryExtender;
        SmartReplyHistoryExtender smartReplyHistoryExtender = new SmartReplyHistoryExtender();
        this.mSmartReplyHistoryExtender = smartReplyHistoryExtender;
        RemoteInputActiveExtender remoteInputActiveExtender = new RemoteInputActiveExtender();
        this.mRemoteInputActiveExtender = remoteInputActiveExtender;
        this.mRemoteInputLifetimeExtenders = CollectionsKt__CollectionsKt.listOf(remoteInputHistoryExtender, smartReplyHistoryExtender, remoteInputActiveExtender);
        dumpManager.registerDumpable(this);
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        this.mNotificationRemoteInputManager.setRemoteInputListener(this);
        for (SelfTrackingLifetimeExtender addNotificationLifetimeExtender : this.mRemoteInputLifetimeExtenders) {
            notifPipeline.addNotificationLifetimeExtender(addNotificationLifetimeExtender);
        }
        this.mNotifUpdater = notifPipeline.getInternalNotifUpdater("RemoteInputCoordinator");
        notifPipeline.addCollectionListener(this.mCollectionListener);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        for (SelfTrackingLifetimeExtender dump : this.mRemoteInputLifetimeExtenders) {
            dump.dump(printWriter, strArr);
        }
    }

    public void onRemoteInputSent(@NotNull NotificationEntry notificationEntry) {
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "onRemoteInputSent(entry=" + notificationEntry.getKey() + ')');
        }
        this.mRemoteInputHistoryExtender.endLifetimeExtension(notificationEntry.getKey());
        this.mSmartReplyHistoryExtender.endLifetimeExtension(notificationEntry.getKey());
        this.mRemoteInputActiveExtender.endLifetimeExtensionAfterDelay(notificationEntry.getKey(), 500);
    }

    public final void onSmartReplySent(NotificationEntry notificationEntry, CharSequence charSequence) {
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "onSmartReplySent(entry=" + notificationEntry.getKey() + ')');
        }
        StatusBarNotification rebuildForSendingSmartReply = this.mRebuilder.rebuildForSendingSmartReply(notificationEntry, charSequence);
        InternalNotifUpdater internalNotifUpdater = this.mNotifUpdater;
        if (internalNotifUpdater == null) {
            internalNotifUpdater = null;
        }
        internalNotifUpdater.onInternalNotificationUpdate(rebuildForSendingSmartReply, "Adding smart reply spinner for sent");
        this.mRemoteInputActiveExtender.endLifetimeExtensionAfterDelay(notificationEntry.getKey(), 500);
    }

    public void onPanelCollapsed() {
        this.mRemoteInputActiveExtender.endAllLifetimeExtensions();
    }

    public boolean isNotificationKeptForRemoteInputHistory(@NotNull String str) {
        return this.mRemoteInputHistoryExtender.isExtending(str) || this.mSmartReplyHistoryExtender.isExtending(str);
    }

    public void releaseNotificationIfKeptForRemoteInputHistory(@NotNull NotificationEntry notificationEntry) {
        if (RemoteInputCoordinatorKt.getDEBUG()) {
            Log.d("RemoteInputCoordinator", "releaseNotificationIfKeptForRemoteInputHistory(entry=" + notificationEntry.getKey() + ')');
        }
        this.mRemoteInputHistoryExtender.endLifetimeExtensionAfterDelay(notificationEntry.getKey(), 200);
        this.mSmartReplyHistoryExtender.endLifetimeExtensionAfterDelay(notificationEntry.getKey(), 200);
        this.mRemoteInputActiveExtender.endLifetimeExtensionAfterDelay(notificationEntry.getKey(), 200);
    }

    public void setRemoteInputController(@NotNull RemoteInputController remoteInputController) {
        this.mSmartReplyController.setCallback(new RemoteInputCoordinator$setRemoteInputController$1(this));
    }

    /* compiled from: RemoteInputCoordinator.kt */
    public final class RemoteInputHistoryExtender extends SelfTrackingLifetimeExtender {
        public RemoteInputHistoryExtender() {
            super("RemoteInputCoordinator", "RemoteInputHistory", RemoteInputCoordinatorKt.getDEBUG(), RemoteInputCoordinator.this.mMainHandler);
        }

        public boolean queryShouldExtendLifetime(@NotNull NotificationEntry notificationEntry) {
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.shouldKeepForRemoteInputHistory(notificationEntry);
        }

        public void onStartedLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
            StatusBarNotification rebuildForRemoteInputReply = RemoteInputCoordinator.this.mRebuilder.rebuildForRemoteInputReply(notificationEntry);
            notificationEntry.onRemoteInputInserted();
            InternalNotifUpdater access$getMNotifUpdater$p = RemoteInputCoordinator.this.mNotifUpdater;
            if (access$getMNotifUpdater$p == null) {
                access$getMNotifUpdater$p = null;
            }
            access$getMNotifUpdater$p.onInternalNotificationUpdate(rebuildForRemoteInputReply, "Extending lifetime of notification with remote input");
        }
    }

    /* compiled from: RemoteInputCoordinator.kt */
    public final class SmartReplyHistoryExtender extends SelfTrackingLifetimeExtender {
        public SmartReplyHistoryExtender() {
            super("RemoteInputCoordinator", "SmartReplyHistory", RemoteInputCoordinatorKt.getDEBUG(), RemoteInputCoordinator.this.mMainHandler);
        }

        public boolean queryShouldExtendLifetime(@NotNull NotificationEntry notificationEntry) {
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.shouldKeepForSmartReplyHistory(notificationEntry);
        }

        public void onStartedLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
            StatusBarNotification rebuildForCanceledSmartReplies = RemoteInputCoordinator.this.mRebuilder.rebuildForCanceledSmartReplies(notificationEntry);
            RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
            InternalNotifUpdater access$getMNotifUpdater$p = RemoteInputCoordinator.this.mNotifUpdater;
            if (access$getMNotifUpdater$p == null) {
                access$getMNotifUpdater$p = null;
            }
            access$getMNotifUpdater$p.onInternalNotificationUpdate(rebuildForCanceledSmartReplies, "Extending lifetime of notification with smart reply");
        }

        public void onCanceledLifetimeExtension(@NotNull NotificationEntry notificationEntry) {
            RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
        }
    }

    /* compiled from: RemoteInputCoordinator.kt */
    public final class RemoteInputActiveExtender extends SelfTrackingLifetimeExtender {
        public RemoteInputActiveExtender() {
            super("RemoteInputCoordinator", "RemoteInputActive", RemoteInputCoordinatorKt.getDEBUG(), RemoteInputCoordinator.this.mMainHandler);
        }

        public boolean queryShouldExtendLifetime(@NotNull NotificationEntry notificationEntry) {
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.isRemoteInputActive(notificationEntry);
        }
    }
}
