package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.phone.NotifPanelEvents;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VisualStabilityCoordinator implements Coordinator, Dumpable, NotifPanelEvents.Listener {
    public static final long ALLOW_SECTION_CHANGE_TIMEOUT = 500;
    public final DelayableExecutor mDelayableExecutor;
    public Map<String, Runnable> mEntriesThatCanChangeSection = new HashMap();
    public final Set<String> mEntriesWithSuppressedSectionChange = new HashSet();
    public final HeadsUpManager mHeadsUpManager;
    public boolean mIsSuppressingEntryReorder = false;
    public boolean mIsSuppressingGroupChange = false;
    public boolean mIsSuppressingPipelineRun = false;
    public boolean mNotifPanelCollapsing;
    public final NotifPanelEvents mNotifPanelEvents;
    public boolean mNotifPanelLaunchingActivity;
    public final NotifStabilityManager mNotifStabilityManager = new NotifStabilityManager("VisualStabilityCoordinator") {
        public void onBeginRun() {
            VisualStabilityCoordinator.this.mIsSuppressingPipelineRun = false;
            VisualStabilityCoordinator.this.mIsSuppressingGroupChange = false;
            VisualStabilityCoordinator.this.mEntriesWithSuppressedSectionChange.clear();
            VisualStabilityCoordinator.this.mIsSuppressingEntryReorder = false;
        }

        public boolean isPipelineRunAllowed() {
            VisualStabilityCoordinator visualStabilityCoordinator = VisualStabilityCoordinator.this;
            visualStabilityCoordinator.mIsSuppressingPipelineRun = visualStabilityCoordinator.mIsSuppressingPipelineRun | (!VisualStabilityCoordinator.this.mPipelineRunAllowed);
            return VisualStabilityCoordinator.this.mPipelineRunAllowed;
        }

        public boolean isGroupChangeAllowed(NotificationEntry notificationEntry) {
            boolean z = VisualStabilityCoordinator.this.mReorderingAllowed || VisualStabilityCoordinator.this.mHeadsUpManager.isAlerting(notificationEntry.getKey());
            VisualStabilityCoordinator visualStabilityCoordinator = VisualStabilityCoordinator.this;
            visualStabilityCoordinator.mIsSuppressingGroupChange = visualStabilityCoordinator.mIsSuppressingGroupChange | (!z);
            return z;
        }

        public boolean isGroupPruneAllowed(GroupEntry groupEntry) {
            boolean r3 = VisualStabilityCoordinator.this.mReorderingAllowed;
            VisualStabilityCoordinator visualStabilityCoordinator = VisualStabilityCoordinator.this;
            visualStabilityCoordinator.mIsSuppressingGroupChange = visualStabilityCoordinator.mIsSuppressingGroupChange | (!r3);
            return r3;
        }

        public boolean isSectionChangeAllowed(NotificationEntry notificationEntry) {
            boolean z = VisualStabilityCoordinator.this.mReorderingAllowed || VisualStabilityCoordinator.this.mHeadsUpManager.isAlerting(notificationEntry.getKey()) || VisualStabilityCoordinator.this.mEntriesThatCanChangeSection.containsKey(notificationEntry.getKey());
            if (!z) {
                VisualStabilityCoordinator.this.mEntriesWithSuppressedSectionChange.add(notificationEntry.getKey());
            }
            return z;
        }

        public boolean isEntryReorderingAllowed(ListEntry listEntry) {
            return VisualStabilityCoordinator.this.mReorderingAllowed;
        }

        public boolean isEveryChangeAllowed() {
            return VisualStabilityCoordinator.this.mReorderingAllowed;
        }

        public void onEntryReorderSuppressed() {
            VisualStabilityCoordinator.this.mIsSuppressingEntryReorder = true;
        }
    };
    public boolean mPanelExpanded;
    public boolean mPipelineRunAllowed;
    public boolean mPulsing;
    public boolean mReorderingAllowed;
    public boolean mScreenOn;
    public final StatusBarStateController mStatusBarStateController;
    public final StatusBarStateController.StateListener mStatusBarStateControllerListener = new StatusBarStateController.StateListener() {
        public void onPulsingChanged(boolean z) {
            VisualStabilityCoordinator.this.mPulsing = z;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }

        public void onExpandedChanged(boolean z) {
            VisualStabilityCoordinator.this.mPanelExpanded = z;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }
    };
    public final VisualStabilityProvider mVisualStabilityProvider;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WakefulnessLifecycle.Observer mWakefulnessObserver = new WakefulnessLifecycle.Observer() {
        public void onFinishedGoingToSleep() {
            VisualStabilityCoordinator.this.mScreenOn = false;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }

        public void onStartedWakingUp() {
            VisualStabilityCoordinator.this.mScreenOn = true;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }
    };

    public VisualStabilityCoordinator(DelayableExecutor delayableExecutor, DumpManager dumpManager, HeadsUpManager headsUpManager, NotifPanelEvents notifPanelEvents, StatusBarStateController statusBarStateController, VisualStabilityProvider visualStabilityProvider, WakefulnessLifecycle wakefulnessLifecycle) {
        this.mHeadsUpManager = headsUpManager;
        this.mVisualStabilityProvider = visualStabilityProvider;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mStatusBarStateController = statusBarStateController;
        this.mDelayableExecutor = delayableExecutor;
        this.mNotifPanelEvents = notifPanelEvents;
        dumpManager.registerDumpable(this);
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
        boolean z = true;
        if (!(this.mWakefulnessLifecycle.getWakefulness() == 2 || this.mWakefulnessLifecycle.getWakefulness() == 1)) {
            z = false;
        }
        this.mScreenOn = z;
        this.mStatusBarStateController.addCallback(this.mStatusBarStateControllerListener);
        this.mPulsing = this.mStatusBarStateController.isPulsing();
        this.mNotifPanelEvents.registerListener(this);
        notifPipeline.setVisualStabilityManager(this.mNotifStabilityManager);
    }

    public final void updateAllowedStates() {
        this.mPipelineRunAllowed = !isPanelCollapsingOrLaunchingActivity();
        boolean isReorderingAllowed = isReorderingAllowed();
        this.mReorderingAllowed = isReorderingAllowed;
        if ((this.mPipelineRunAllowed && this.mIsSuppressingPipelineRun) || (isReorderingAllowed && (this.mIsSuppressingGroupChange || isSuppressingSectionChange() || this.mIsSuppressingEntryReorder))) {
            this.mNotifStabilityManager.invalidateList();
        }
        this.mVisualStabilityProvider.setReorderingAllowed(this.mReorderingAllowed);
    }

    public final boolean isSuppressingSectionChange() {
        return !this.mEntriesWithSuppressedSectionChange.isEmpty();
    }

    public final boolean isPanelCollapsingOrLaunchingActivity() {
        return this.mNotifPanelCollapsing || this.mNotifPanelLaunchingActivity;
    }

    public final boolean isReorderingAllowed() {
        return (!this.mScreenOn || !this.mPanelExpanded) && !this.mPulsing;
    }

    public void temporarilyAllowSectionChanges(NotificationEntry notificationEntry, long j) {
        String key = notificationEntry.getKey();
        boolean isSectionChangeAllowed = this.mNotifStabilityManager.isSectionChangeAllowed(notificationEntry);
        if (this.mEntriesThatCanChangeSection.containsKey(key)) {
            this.mEntriesThatCanChangeSection.get(key).run();
        }
        this.mEntriesThatCanChangeSection.put(key, this.mDelayableExecutor.executeAtTime(new VisualStabilityCoordinator$$ExternalSyntheticLambda0(this, key), j + 500));
        if (!isSectionChangeAllowed) {
            this.mNotifStabilityManager.invalidateList();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$temporarilyAllowSectionChanges$0(String str) {
        this.mEntriesThatCanChangeSection.remove(str);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("pipelineRunAllowed: " + this.mPipelineRunAllowed);
        printWriter.println("  notifPanelCollapsing: " + this.mNotifPanelCollapsing);
        printWriter.println("  launchingNotifActivity: " + this.mNotifPanelLaunchingActivity);
        printWriter.println("reorderingAllowed: " + this.mReorderingAllowed);
        printWriter.println("  screenOn: " + this.mScreenOn);
        printWriter.println("  panelExpanded: " + this.mPanelExpanded);
        printWriter.println("  pulsing: " + this.mPulsing);
        printWriter.println("isSuppressingPipelineRun: " + this.mIsSuppressingPipelineRun);
        printWriter.println("isSuppressingGroupChange: " + this.mIsSuppressingGroupChange);
        printWriter.println("isSuppressingEntryReorder: " + this.mIsSuppressingEntryReorder);
        printWriter.println("entriesWithSuppressedSectionChange: " + this.mEntriesWithSuppressedSectionChange.size());
        for (String str : this.mEntriesWithSuppressedSectionChange) {
            printWriter.println("  " + str);
        }
        printWriter.println("entriesThatCanChangeSection: " + this.mEntriesThatCanChangeSection.size());
        for (String str2 : this.mEntriesThatCanChangeSection.keySet()) {
            printWriter.println("  " + str2);
        }
    }

    public void onPanelCollapsingChanged(boolean z) {
        this.mNotifPanelCollapsing = z;
        updateAllowedStates();
    }

    public void onLaunchingActivityChanged(boolean z) {
        this.mNotifPanelLaunchingActivity = z;
        updateAllowedStates();
    }
}
