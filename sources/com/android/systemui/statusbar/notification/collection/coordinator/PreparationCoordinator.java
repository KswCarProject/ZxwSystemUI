package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustment;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.collection.render.NotifViewController;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PreparationCoordinator implements Coordinator {
    public final NotifUiAdjustmentProvider mAdjustmentProvider;
    public final BindEventManagerImpl mBindEventManager;
    public final int mChildBindCutoff;
    public final ArraySet<NotificationEntry> mInflatingNotifs;
    public final ArrayMap<NotificationEntry, NotifUiAdjustment> mInflationAdjustments;
    public final NotifInflationErrorManager.NotifInflationErrorListener mInflationErrorListener;
    public final ArrayMap<NotificationEntry, Integer> mInflationStates;
    public final PreparationCoordinatorLogger mLogger;
    public final long mMaxGroupInflationDelay;
    public final NotifCollectionListener mNotifCollectionListener;
    public final NotifInflationErrorManager mNotifErrorManager;
    public final NotifInflater mNotifInflater;
    public final NotifFilter mNotifInflatingFilter;
    public final NotifFilter mNotifInflationErrorFilter;
    public final IStatusBarService mStatusBarService;
    public final NotifViewBarn mViewBarn;

    public PreparationCoordinator(PreparationCoordinatorLogger preparationCoordinatorLogger, NotifInflater notifInflater, NotifInflationErrorManager notifInflationErrorManager, NotifViewBarn notifViewBarn, NotifUiAdjustmentProvider notifUiAdjustmentProvider, IStatusBarService iStatusBarService, BindEventManagerImpl bindEventManagerImpl) {
        this(preparationCoordinatorLogger, notifInflater, notifInflationErrorManager, notifViewBarn, notifUiAdjustmentProvider, iStatusBarService, bindEventManagerImpl, 9, 500);
    }

    @VisibleForTesting
    public PreparationCoordinator(PreparationCoordinatorLogger preparationCoordinatorLogger, NotifInflater notifInflater, NotifInflationErrorManager notifInflationErrorManager, NotifViewBarn notifViewBarn, NotifUiAdjustmentProvider notifUiAdjustmentProvider, IStatusBarService iStatusBarService, BindEventManagerImpl bindEventManagerImpl, int i, long j) {
        this.mInflationStates = new ArrayMap<>();
        this.mInflationAdjustments = new ArrayMap<>();
        this.mInflatingNotifs = new ArraySet<>();
        this.mNotifCollectionListener = new NotifCollectionListener() {
            public void onEntryInit(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.mInflationStates.put(notificationEntry, 0);
            }

            public void onEntryUpdated(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.abortInflation(notificationEntry, "entryUpdated");
                int r0 = PreparationCoordinator.this.getInflationState(notificationEntry);
                if (r0 == 1) {
                    PreparationCoordinator.this.mInflationStates.put(notificationEntry, 2);
                } else if (r0 == -1) {
                    PreparationCoordinator.this.mInflationStates.put(notificationEntry, 0);
                }
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                PreparationCoordinator preparationCoordinator = PreparationCoordinator.this;
                preparationCoordinator.abortInflation(notificationEntry, "entryRemoved reason=" + i);
            }

            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.mInflationStates.remove(notificationEntry);
                PreparationCoordinator.this.mViewBarn.removeViewForEntry(notificationEntry);
                PreparationCoordinator.this.mInflationAdjustments.remove(notificationEntry);
            }
        };
        this.mNotifInflationErrorFilter = new NotifFilter("PreparationCoordinatorInflationError") {
            public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
                return PreparationCoordinator.this.getInflationState(notificationEntry) == -1;
            }
        };
        this.mNotifInflatingFilter = new NotifFilter("PreparationCoordinatorInflating") {
            public final Map<GroupEntry, Boolean> mIsDelayedGroupCache = new ArrayMap();

            public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
                GroupEntry parent = notificationEntry.getParent();
                Objects.requireNonNull(parent);
                Boolean bool = this.mIsDelayedGroupCache.get(parent);
                if (bool == null) {
                    bool = Boolean.valueOf(PreparationCoordinator.this.shouldWaitForGroupToInflate(parent, j));
                    this.mIsDelayedGroupCache.put(parent, bool);
                }
                return !PreparationCoordinator.this.isInflated(notificationEntry) || bool.booleanValue();
            }

            public void onCleanup() {
                this.mIsDelayedGroupCache.clear();
            }
        };
        this.mInflationErrorListener = new NotifInflationErrorManager.NotifInflationErrorListener() {
            public void onNotifInflationError(NotificationEntry notificationEntry, Exception exc) {
                PreparationCoordinator.this.mViewBarn.removeViewForEntry(notificationEntry);
                PreparationCoordinator.this.mInflationStates.put(notificationEntry, -1);
                try {
                    StatusBarNotification sbn = notificationEntry.getSbn();
                    PreparationCoordinator.this.mStatusBarService.onNotificationError(sbn.getPackageName(), sbn.getTag(), sbn.getId(), sbn.getUid(), sbn.getInitialPid(), exc.getMessage(), sbn.getUser().getIdentifier());
                } catch (RemoteException unused) {
                }
                PreparationCoordinator.this.mNotifInflationErrorFilter.invalidateList();
            }

            public void onNotifInflationErrorCleared(NotificationEntry notificationEntry) {
                PreparationCoordinator.this.mNotifInflationErrorFilter.invalidateList();
            }
        };
        this.mLogger = preparationCoordinatorLogger;
        this.mNotifInflater = notifInflater;
        this.mNotifErrorManager = notifInflationErrorManager;
        this.mViewBarn = notifViewBarn;
        this.mAdjustmentProvider = notifUiAdjustmentProvider;
        this.mStatusBarService = iStatusBarService;
        this.mChildBindCutoff = i;
        this.mMaxGroupInflationDelay = j;
        this.mBindEventManager = bindEventManagerImpl;
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mNotifErrorManager.addInflationErrorListener(this.mInflationErrorListener);
        NotifUiAdjustmentProvider notifUiAdjustmentProvider = this.mAdjustmentProvider;
        NotifFilter notifFilter = this.mNotifInflatingFilter;
        Objects.requireNonNull(notifFilter);
        notifUiAdjustmentProvider.addDirtyListener(new PreparationCoordinator$$ExternalSyntheticLambda1(notifFilter));
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeFinalizeFilterListener(new PreparationCoordinator$$ExternalSyntheticLambda2(this));
        notifPipeline.addFinalizeFilter(this.mNotifInflationErrorFilter);
        notifPipeline.addFinalizeFilter(this.mNotifInflatingFilter);
    }

    public final void inflateAllRequiredViews(List<ListEntry> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ListEntry listEntry = list.get(i);
            if (listEntry instanceof GroupEntry) {
                inflateRequiredGroupViews((GroupEntry) listEntry);
            } else {
                inflateRequiredNotifViews((NotificationEntry) listEntry);
            }
        }
    }

    public final void inflateRequiredGroupViews(GroupEntry groupEntry) {
        NotificationEntry summary = groupEntry.getSummary();
        List<NotificationEntry> children = groupEntry.getChildren();
        inflateRequiredNotifViews(summary);
        int i = 0;
        while (i < children.size()) {
            NotificationEntry notificationEntry = children.get(i);
            if (i < this.mChildBindCutoff) {
                inflateRequiredNotifViews(notificationEntry);
            } else {
                if (this.mInflatingNotifs.contains(notificationEntry)) {
                    abortInflation(notificationEntry, "Past last visible group child");
                }
                if (isInflated(notificationEntry)) {
                    freeNotifViews(notificationEntry);
                }
            }
            i++;
        }
    }

    public final void inflateRequiredNotifViews(NotificationEntry notificationEntry) {
        NotifUiAdjustment calculateAdjustment = this.mAdjustmentProvider.calculateAdjustment(notificationEntry);
        if (!this.mInflatingNotifs.contains(notificationEntry)) {
            int intValue = this.mInflationStates.get(notificationEntry).intValue();
            if (intValue != -1) {
                if (intValue == 0) {
                    inflateEntry(notificationEntry, calculateAdjustment, "entryAdded");
                } else if (intValue != 1) {
                    if (intValue == 2) {
                        rebind(notificationEntry, calculateAdjustment, "entryUpdated");
                    }
                } else if (needToReinflate(notificationEntry, calculateAdjustment, "Fully inflated notification has no adjustments")) {
                    rebind(notificationEntry, calculateAdjustment, "adjustment changed after inflated");
                }
            } else if (needToReinflate(notificationEntry, calculateAdjustment, (String) null)) {
                inflateEntry(notificationEntry, calculateAdjustment, "adjustment changed after error");
            }
        } else if (needToReinflate(notificationEntry, calculateAdjustment, "Inflating notification has no adjustments")) {
            inflateEntry(notificationEntry, calculateAdjustment, "adjustment changed while inflating");
        }
    }

    public final boolean needToReinflate(NotificationEntry notificationEntry, NotifUiAdjustment notifUiAdjustment, String str) {
        NotifUiAdjustment notifUiAdjustment2 = this.mInflationAdjustments.get(notificationEntry);
        if (notifUiAdjustment2 != null) {
            return NotifUiAdjustment.needReinflate(notifUiAdjustment2, notifUiAdjustment);
        }
        if (str == null) {
            return true;
        }
        throw new IllegalStateException(str);
    }

    public final void inflateEntry(NotificationEntry notificationEntry, NotifUiAdjustment notifUiAdjustment, String str) {
        abortInflation(notificationEntry, str);
        this.mInflationAdjustments.put(notificationEntry, notifUiAdjustment);
        this.mInflatingNotifs.add(notificationEntry);
        this.mNotifInflater.inflateViews(notificationEntry, getInflaterParams(notifUiAdjustment, str), new PreparationCoordinator$$ExternalSyntheticLambda0(this));
    }

    public final void rebind(NotificationEntry notificationEntry, NotifUiAdjustment notifUiAdjustment, String str) {
        this.mInflationAdjustments.put(notificationEntry, notifUiAdjustment);
        this.mInflatingNotifs.add(notificationEntry);
        this.mNotifInflater.rebindViews(notificationEntry, getInflaterParams(notifUiAdjustment, str), new PreparationCoordinator$$ExternalSyntheticLambda0(this));
    }

    public NotifInflater.Params getInflaterParams(NotifUiAdjustment notifUiAdjustment, String str) {
        return new NotifInflater.Params(notifUiAdjustment.isMinimized(), str);
    }

    public final void abortInflation(NotificationEntry notificationEntry, String str) {
        this.mLogger.logInflationAborted(notificationEntry.getKey(), str);
        this.mNotifInflater.abortInflation(notificationEntry);
        this.mInflatingNotifs.remove(notificationEntry);
    }

    public final void onInflationFinished(NotificationEntry notificationEntry, NotifViewController notifViewController) {
        this.mLogger.logNotifInflated(notificationEntry.getKey());
        this.mInflatingNotifs.remove(notificationEntry);
        this.mViewBarn.registerViewForEntry(notificationEntry, notifViewController);
        this.mInflationStates.put(notificationEntry, 1);
        this.mBindEventManager.notifyViewBound(notificationEntry);
        this.mNotifInflatingFilter.invalidateList();
    }

    public final void freeNotifViews(NotificationEntry notificationEntry) {
        this.mViewBarn.removeViewForEntry(notificationEntry);
        this.mInflationStates.put(notificationEntry, 0);
    }

    public final boolean isInflated(NotificationEntry notificationEntry) {
        int inflationState = getInflationState(notificationEntry);
        return inflationState == 1 || inflationState == 2;
    }

    public final int getInflationState(NotificationEntry notificationEntry) {
        Integer num = this.mInflationStates.get(notificationEntry);
        Objects.requireNonNull(num, "Asking state of a notification preparation coordinator doesn't know about");
        return num.intValue();
    }

    public final boolean shouldWaitForGroupToInflate(GroupEntry groupEntry, long j) {
        if (groupEntry != GroupEntry.ROOT_ENTRY && !groupEntry.wasAttachedInPreviousPass()) {
            if (isBeyondGroupInitializationWindow(groupEntry, j)) {
                this.mLogger.logGroupInflationTookTooLong(groupEntry.getKey());
                return false;
            } else if (this.mInflatingNotifs.contains(groupEntry.getSummary())) {
                this.mLogger.logDelayingGroupRelease(groupEntry.getKey(), groupEntry.getSummary().getKey());
                return true;
            } else {
                for (NotificationEntry next : groupEntry.getChildren()) {
                    if (this.mInflatingNotifs.contains(next) && !next.wasAttachedInPreviousPass()) {
                        this.mLogger.logDelayingGroupRelease(groupEntry.getKey(), next.getKey());
                        return true;
                    }
                }
                this.mLogger.logDoneWaitingForGroupInflation(groupEntry.getKey());
            }
        }
        return false;
    }

    public final boolean isBeyondGroupInitializationWindow(GroupEntry groupEntry, long j) {
        return j - groupEntry.getCreationTime() > this.mMaxGroupInflationDelay;
    }
}
