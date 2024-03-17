package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import java.util.Collections;
import java.util.List;

public class RankingCoordinator implements Coordinator {
    public final NodeController mAlertingHeaderController;
    public final NotifSectioner mAlertingNotifSectioner = new NotifSectioner("Alerting", 5) {
        public NodeController getHeaderNodeController() {
            return null;
        }

        public boolean isInSection(ListEntry listEntry) {
            return RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry);
        }
    };
    public final NotifFilter mDndVisualEffectsFilter = new NotifFilter("DndSuppressingVisualEffects") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (RankingCoordinator.this.mStatusBarStateController.isDozing() && notificationEntry.shouldSuppressAmbient()) {
                return true;
            }
            if (RankingCoordinator.this.mStatusBarStateController.isDozing() || !notificationEntry.shouldSuppressNotificationList()) {
                return false;
            }
            return true;
        }
    };
    public boolean mHasMinimizedEntries;
    public boolean mHasSilentEntries;
    public final HighPriorityProvider mHighPriorityProvider;
    public final NotifSectioner mMinimizedNotifSectioner = new NotifSectioner("Minimized", 6) {
        public boolean isInSection(ListEntry listEntry) {
            return !RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry) && listEntry.getRepresentativeEntry().isAmbient();
        }

        public NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mSilentNodeController;
        }

        public void onEntriesUpdated(List<ListEntry> list) {
            int i = 0;
            RankingCoordinator.this.mHasMinimizedEntries = false;
            while (true) {
                if (i >= list.size()) {
                    break;
                } else if (list.get(i).getRepresentativeEntry().getSbn().isClearable()) {
                    RankingCoordinator.this.mHasMinimizedEntries = true;
                    break;
                } else {
                    i++;
                }
            }
            RankingCoordinator.this.mSilentHeaderController.setClearSectionEnabled(RankingCoordinator.this.mHasMinimizedEntries | RankingCoordinator.this.mHasSilentEntries);
        }
    };
    public final SectionClassifier mSectionClassifier;
    public final SectionHeaderController mSilentHeaderController;
    public final NodeController mSilentNodeController;
    public final NotifSectioner mSilentNotifSectioner = new NotifSectioner("Silent", 6) {
        public boolean isInSection(ListEntry listEntry) {
            return !RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry) && !listEntry.getRepresentativeEntry().isAmbient();
        }

        public NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mSilentNodeController;
        }

        public void onEntriesUpdated(List<ListEntry> list) {
            int i = 0;
            RankingCoordinator.this.mHasSilentEntries = false;
            while (true) {
                if (i >= list.size()) {
                    break;
                } else if (list.get(i).getRepresentativeEntry().getSbn().isClearable()) {
                    RankingCoordinator.this.mHasSilentEntries = true;
                    break;
                } else {
                    i++;
                }
            }
            RankingCoordinator.this.mSilentHeaderController.setClearSectionEnabled(RankingCoordinator.this.mHasMinimizedEntries | RankingCoordinator.this.mHasSilentEntries);
        }
    };
    public final StatusBarStateController.StateListener mStatusBarStateCallback = new StatusBarStateController.StateListener() {
        public void onDozingChanged(boolean z) {
            RankingCoordinator.this.mDndVisualEffectsFilter.invalidateList();
        }
    };
    public final StatusBarStateController mStatusBarStateController;
    public final NotifFilter mSuspendedFilter = new NotifFilter("IsSuspendedFilter") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return notificationEntry.getRanking().isSuspended();
        }
    };

    public RankingCoordinator(StatusBarStateController statusBarStateController, HighPriorityProvider highPriorityProvider, SectionClassifier sectionClassifier, NodeController nodeController, SectionHeaderController sectionHeaderController, NodeController nodeController2) {
        this.mStatusBarStateController = statusBarStateController;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mSectionClassifier = sectionClassifier;
        this.mAlertingHeaderController = nodeController;
        this.mSilentNodeController = nodeController2;
        this.mSilentHeaderController = sectionHeaderController;
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mStatusBarStateController.addCallback(this.mStatusBarStateCallback);
        this.mSectionClassifier.setMinimizedSections(Collections.singleton(this.mMinimizedNotifSectioner));
        notifPipeline.addPreGroupFilter(this.mSuspendedFilter);
        notifPipeline.addPreGroupFilter(this.mDndVisualEffectsFilter);
    }

    public NotifSectioner getAlertingSectioner() {
        return this.mAlertingNotifSectioner;
    }

    public NotifSectioner getSilentSectioner() {
        return this.mSilentNotifSectioner;
    }

    public NotifSectioner getMinimizedSectioner() {
        return this.mMinimizedNotifSectioner;
    }
}
