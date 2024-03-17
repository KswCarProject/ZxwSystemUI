package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.media.MediaDataManagerKt;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.icon.IconManager;

public class MediaCoordinator implements Coordinator {
    public final NotifCollectionListener mCollectionListener = new NotifCollectionListener() {
        public void onEntryInit(NotificationEntry notificationEntry) {
            MediaCoordinator.this.mIconsState.put(notificationEntry, 0);
        }

        public void onEntryUpdated(NotificationEntry notificationEntry) {
            if (((Integer) MediaCoordinator.this.mIconsState.getOrDefault(notificationEntry, 0)).intValue() == 2) {
                MediaCoordinator.this.mIconsState.put(notificationEntry, 0);
            }
        }

        public void onEntryCleanUp(NotificationEntry notificationEntry) {
            MediaCoordinator.this.mIconsState.remove(notificationEntry);
        }
    };
    public final IconManager mIconManager;
    public final ArrayMap<NotificationEntry, Integer> mIconsState = new ArrayMap<>();
    public final Boolean mIsMediaFeatureEnabled;
    public final NotifFilter mMediaFilter = new NotifFilter("MediaCoordinator") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (!MediaCoordinator.this.mIsMediaFeatureEnabled.booleanValue() || !MediaDataManagerKt.isMediaNotification(notificationEntry.getSbn())) {
                return false;
            }
            int intValue = ((Integer) MediaCoordinator.this.mIconsState.getOrDefault(notificationEntry, 0)).intValue();
            if (intValue == 0) {
                try {
                    MediaCoordinator.this.mIconManager.createIcons(notificationEntry);
                    MediaCoordinator.this.mIconsState.put(notificationEntry, 1);
                } catch (InflationException e) {
                    MediaCoordinator.this.reportInflationError(notificationEntry, e);
                    MediaCoordinator.this.mIconsState.put(notificationEntry, 2);
                }
            } else if (intValue == 1) {
                try {
                    MediaCoordinator.this.mIconManager.updateIcons(notificationEntry);
                } catch (InflationException e2) {
                    MediaCoordinator.this.reportInflationError(notificationEntry, e2);
                    MediaCoordinator.this.mIconsState.put(notificationEntry, 2);
                }
            }
            return true;
        }
    };
    public final IStatusBarService mStatusBarService;

    public final void reportInflationError(NotificationEntry notificationEntry, Exception exc) {
        try {
            StatusBarNotification sbn = notificationEntry.getSbn();
            this.mStatusBarService.onNotificationError(sbn.getPackageName(), sbn.getTag(), sbn.getId(), sbn.getUid(), sbn.getInitialPid(), exc.getMessage(), sbn.getUser().getIdentifier());
        } catch (RemoteException unused) {
        }
    }

    public MediaCoordinator(MediaFeatureFlag mediaFeatureFlag, IStatusBarService iStatusBarService, IconManager iconManager) {
        this.mIsMediaFeatureEnabled = Boolean.valueOf(mediaFeatureFlag.getEnabled());
        this.mStatusBarService = iStatusBarService;
        this.mIconManager = iconManager;
    }

    public void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.mMediaFilter);
        notifPipeline.addCollectionListener(this.mCollectionListener);
    }
}
