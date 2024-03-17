package com.android.systemui.wmshell;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.ZenModeConfig;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationChannelHelper;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleEntry;
import com.android.wm.shell.bubbles.Bubbles;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class BubblesManager implements Dumpable {
    public final IStatusBarService mBarService;
    public final Bubbles mBubbles;
    public final List<NotifCallback> mCallbacks = new ArrayList();
    public final CommonNotifCollection mCommonNotifCollection;
    public final Context mContext;
    public final NotifPipeline mNotifPipeline;
    public final NotificationLockscreenUserManager mNotifUserManager;
    public final NotificationEntryManager mNotificationEntryManager;
    public final NotificationGroupManagerLegacy mNotificationGroupManager;
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    public final INotificationManager mNotificationManager;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public final ShadeController mShadeController;
    public final Executor mSysuiMainExecutor;
    public final Bubbles.SysuiProxy mSysuiProxy;
    public final NotificationVisibilityProvider mVisibilityProvider;

    public interface NotifCallback {
        void invalidateNotifications(String str);

        void maybeCancelSummary(NotificationEntry notificationEntry);

        void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i);
    }

    public static BubblesManager create(Context context, Optional<Bubbles> optional, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationVisibilityProvider notificationVisibilityProvider, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, CommonNotifCollection commonNotifCollection, NotifPipeline notifPipeline, SysUiState sysUiState, NotifPipelineFlags notifPipelineFlags, DumpManager dumpManager, Executor executor) {
        if (!optional.isPresent()) {
            return null;
        }
        return new BubblesManager(context, optional.get(), notificationShadeWindowController, keyguardStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationVisibilityProvider, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, commonNotifCollection, notifPipeline, sysUiState, notifPipelineFlags, dumpManager, executor);
    }

    @VisibleForTesting
    public BubblesManager(Context context, final Bubbles bubbles, NotificationShadeWindowController notificationShadeWindowController, final KeyguardStateController keyguardStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationVisibilityProvider notificationVisibilityProvider, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, CommonNotifCollection commonNotifCollection, NotifPipeline notifPipeline, SysUiState sysUiState, NotifPipelineFlags notifPipelineFlags, DumpManager dumpManager, Executor executor) {
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        NotificationLockscreenUserManager notificationLockscreenUserManager2 = notificationLockscreenUserManager;
        final Executor executor2 = executor;
        this.mContext = context;
        this.mBubbles = bubbles;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mShadeController = shadeController;
        this.mNotificationManager = iNotificationManager;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mNotifUserManager = notificationLockscreenUserManager2;
        this.mNotificationGroupManager = notificationGroupManagerLegacy;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mCommonNotifCollection = commonNotifCollection;
        this.mNotifPipeline = notifPipeline;
        this.mSysuiMainExecutor = executor2;
        this.mBarService = iStatusBarService == null ? IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar")) : iStatusBarService;
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            setupNotifPipeline();
        } else {
            setupNEM();
        }
        dumpManager.registerDumpable("Bubbles", this);
        keyguardStateController.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardShowingChanged() {
                bubbles.onStatusBarStateChanged(!keyguardStateController.isShowing() && !keyguardStateController.isOccluded());
            }
        });
        ConfigurationController configurationController2 = configurationController;
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                BubblesManager.this.mBubbles.onConfigChanged(configuration);
            }

            public void onUiModeChanged() {
                BubblesManager.this.mBubbles.updateForThemeChanges();
            }

            public void onThemeChanged() {
                BubblesManager.this.mBubbles.updateForThemeChanges();
            }
        });
        zenModeController.addCallback(new ZenModeController.Callback() {
            public void onZenChanged(int i) {
                BubblesManager.this.mBubbles.onZenStateChanged();
            }

            public void onConfigChanged(ZenModeConfig zenModeConfig) {
                BubblesManager.this.mBubbles.onZenStateChanged();
            }
        });
        notificationLockscreenUserManager2.addUserChangedListener(new NotificationLockscreenUserManager.UserChangedListener() {
            public void onUserChanged(int i) {
                BubblesManager.this.mBubbles.onUserChanged(i);
            }

            public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
                BubblesManager.this.mBubbles.onCurrentProfilesChanged(sparseArray);
            }

            public void onUserRemoved(int i) {
                BubblesManager.this.mBubbles.onUserRemoved(i);
            }
        });
        final SysUiState sysUiState2 = sysUiState;
        AnonymousClass5 r2 = new Bubbles.SysuiProxy() {
            public void getPendingOrActiveEntry(String str, Consumer<BubbleEntry> consumer) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda4(this, str, consumer));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$getPendingOrActiveEntry$1(String str, Consumer consumer) {
                BubbleEntry bubbleEntry;
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry == null) {
                    bubbleEntry = null;
                } else {
                    bubbleEntry = BubblesManager.notifToBubbleEntry(entry);
                }
                consumer.accept(bubbleEntry);
            }

            public void getShouldRestoredEntries(ArraySet<String> arraySet, Consumer<List<BubbleEntry>> consumer) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda2(this, arraySet, consumer));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$getShouldRestoredEntries$2(ArraySet arraySet, Consumer consumer) {
                ArrayList arrayList = new ArrayList();
                for (NotificationEntry next : BubblesManager.this.mCommonNotifCollection.getAllNotifs()) {
                    if (BubblesManager.this.mNotifUserManager.isCurrentProfile(next.getSbn().getUserId()) && arraySet.contains(next.getKey()) && BubblesManager.this.mNotificationInterruptStateProvider.shouldBubbleUp(next) && next.isBubble()) {
                        arrayList.add(BubblesManager.notifToBubbleEntry(next));
                    }
                }
                consumer.accept(arrayList);
            }

            public void setNotificationInterruption(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda0(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$setNotificationInterruption$3(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null && entry.getImportance() >= 4) {
                    entry.setInterruption();
                }
            }

            public void requestNotificationShadeTopUi(boolean z, String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda8(this, z, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$requestNotificationShadeTopUi$4(boolean z, String str) {
                BubblesManager.this.mNotificationShadeWindowController.setRequestTopUi(z, str);
            }

            public void notifyRemoveNotification(String str, int i) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda7(this, str, i));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$notifyRemoveNotification$5(String str, int i) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    for (NotifCallback removeNotification : BubblesManager.this.mCallbacks) {
                        removeNotification.removeNotification(entry, BubblesManager.this.getDismissedByUserStats(entry, true), i);
                    }
                }
            }

            public void notifyInvalidateNotifications(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda5(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$notifyInvalidateNotifications$6(String str) {
                for (NotifCallback invalidateNotifications : BubblesManager.this.mCallbacks) {
                    invalidateNotifications.invalidateNotifications(str);
                }
            }

            public void notifyMaybeCancelSummary(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda11(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$notifyMaybeCancelSummary$7(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    for (NotifCallback maybeCancelSummary : BubblesManager.this.mCallbacks) {
                        maybeCancelSummary.maybeCancelSummary(entry);
                    }
                }
            }

            public void removeNotificationEntry(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda12(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$removeNotificationEntry$8(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    BubblesManager.this.mNotificationGroupManager.onEntryRemoved(entry);
                }
            }

            public void updateNotificationBubbleButton(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda6(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$updateNotificationBubbleButton$9(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null && entry.getRow() != null) {
                    entry.getRow().updateBubbleButton();
                }
            }

            public void updateNotificationSuppression(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda9(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$updateNotificationSuppression$10(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    BubblesManager.this.mNotificationGroupManager.updateSuppression(entry);
                }
            }

            public void onStackExpandChanged(boolean z) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda10(this, sysUiState2, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onStackExpandChanged$11(SysUiState sysUiState, boolean z) {
                sysUiState.setFlag(16384, z).commitUpdate(BubblesManager.this.mContext.getDisplayId());
                if (!z) {
                    sysUiState.setFlag(8388608, false).commitUpdate(BubblesManager.this.mContext.getDisplayId());
                }
            }

            public void onManageMenuExpandChanged(boolean z) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda1(this, sysUiState2, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onManageMenuExpandChanged$12(SysUiState sysUiState, boolean z) {
                sysUiState.setFlag(8388608, z).commitUpdate(BubblesManager.this.mContext.getDisplayId());
            }

            public void onUnbubbleConversation(String str) {
                executor2.execute(new BubblesManager$5$$ExternalSyntheticLambda3(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onUnbubbleConversation$13(String str) {
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry(str);
                if (entry != null) {
                    BubblesManager.this.onUserChangedBubble(entry, false);
                }
            }
        };
        this.mSysuiProxy = r2;
        bubbles.setSysuiProxy(r2);
    }

    public final void setupNEM() {
        this.mNotificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
            public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryAdded(notificationEntry);
            }

            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryUpdated(notificationEntry);
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                BubblesManager.this.onEntryRemoved(notificationEntry);
            }

            public void onNotificationRankingUpdated(NotificationListenerService.RankingMap rankingMap) {
                BubblesManager.this.onRankingUpdate(rankingMap);
            }

            public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                BubblesManager.this.onNotificationChannelModified(str, userHandle, notificationChannel, i);
            }
        });
        this.mNotificationEntryManager.addNotificationRemoveInterceptor(new BubblesManager$$ExternalSyntheticLambda0(this));
        this.mNotificationGroupManager.registerGroupChangeListener(new NotificationGroupManagerLegacy.OnGroupChangeListener() {
            public void onGroupSuppressionChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, boolean z) {
                NotificationEntry notificationEntry = notificationGroup.summary;
                String groupKey = notificationEntry != null ? notificationEntry.getSbn().getGroupKey() : null;
                if (!z && groupKey != null) {
                    BubblesManager.this.mBubbles.removeSuppressedSummaryIfNecessary(groupKey, (Consumer<String>) null, (Executor) null);
                }
            }
        });
        addNotifCallback(new NotifCallback() {
            public void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i) {
                BubblesManager.this.mNotificationEntryManager.performRemoveNotification(notificationEntry.getSbn(), dismissedByUserStats, i);
            }

            public void invalidateNotifications(String str) {
                BubblesManager.this.mNotificationEntryManager.updateNotifications(str);
            }

            public void maybeCancelSummary(NotificationEntry notificationEntry) {
                BubblesManager.this.mBubbles.removeSuppressedSummaryIfNecessary(notificationEntry.getSbn().getGroupKey(), new BubblesManager$8$$ExternalSyntheticLambda0(this), BubblesManager.this.mSysuiMainExecutor);
                NotificationEntry logicalGroupSummary = BubblesManager.this.mNotificationGroupManager.getLogicalGroupSummary(notificationEntry);
                if (logicalGroupSummary != null) {
                    ArrayList<NotificationEntry> logicalChildren = BubblesManager.this.mNotificationGroupManager.getLogicalChildren(logicalGroupSummary.getSbn());
                    if (logicalGroupSummary.getKey().equals(notificationEntry.getKey())) {
                        return;
                    }
                    if (logicalChildren == null || logicalChildren.isEmpty()) {
                        BubblesManager.this.mNotificationEntryManager.performRemoveNotification(logicalGroupSummary.getSbn(), BubblesManager.this.getDismissedByUserStats(logicalGroupSummary, false), 0);
                    }
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$maybeCancelSummary$0(String str) {
                NotificationEntry activeNotificationUnfiltered = BubblesManager.this.mNotificationEntryManager.getActiveNotificationUnfiltered(str);
                if (activeNotificationUnfiltered != null) {
                    BubblesManager.this.mNotificationEntryManager.performRemoveNotification(activeNotificationUnfiltered.getSbn(), BubblesManager.this.getDismissedByUserStats(activeNotificationUnfiltered, false), 0);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupNEM$0(String str, NotificationEntry notificationEntry, int i) {
        boolean z = true;
        boolean z2 = i == 3;
        boolean z3 = i == 2 || i == 1;
        boolean z4 = i == 8 || i == 9;
        boolean z5 = i == 12;
        if ((notificationEntry == null || !notificationEntry.isRowDismissed() || z4) && !z2 && !z3 && !z5) {
            z = false;
        }
        if (z) {
            return handleDismissalInterception(notificationEntry);
        }
        return false;
    }

    public final void setupNotifPipeline() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() {
            public void onEntryAdded(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryAdded(notificationEntry);
            }

            public void onEntryUpdated(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryUpdated(notificationEntry);
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                if (i == 8 || i == 9) {
                    BubblesManager.this.onEntryRemoved(notificationEntry);
                }
            }

            public void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
                BubblesManager.this.onRankingUpdate(rankingMap);
            }

            public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                BubblesManager.this.onNotificationChannelModified(str, userHandle, notificationChannel, i);
            }
        });
    }

    public void onEntryAdded(NotificationEntry notificationEntry) {
        if (this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry) && notificationEntry.isBubble()) {
            this.mBubbles.onEntryAdded(notifToBubbleEntry(notificationEntry));
        }
    }

    public void onEntryUpdated(NotificationEntry notificationEntry) {
        this.mBubbles.onEntryUpdated(notifToBubbleEntry(notificationEntry), this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry));
    }

    public void onEntryRemoved(NotificationEntry notificationEntry) {
        this.mBubbles.onEntryRemoved(notifToBubbleEntry(notificationEntry));
    }

    public void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        String[] orderedKeys = rankingMap.getOrderedKeys();
        HashMap hashMap = new HashMap();
        for (String str : orderedKeys) {
            NotificationEntry entry = this.mCommonNotifCollection.getEntry(str);
            hashMap.put(str, new Pair(entry != null ? notifToBubbleEntry(entry) : null, Boolean.valueOf(entry != null ? this.mNotificationInterruptStateProvider.shouldBubbleUp(entry) : false)));
        }
        this.mBubbles.onRankingUpdated(rankingMap, hashMap);
    }

    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        this.mBubbles.onNotificationChannelModified(str, userHandle, notificationChannel, i);
    }

    public final DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry, boolean z) {
        return new DismissedByUserStats(3, 1, this.mVisibilityProvider.obtain(notificationEntry, z));
    }

    public boolean handleDismissalInterception(NotificationEntry notificationEntry) {
        if (notificationEntry == null) {
            return false;
        }
        List<NotificationEntry> attachedNotifChildren = notificationEntry.getAttachedNotifChildren();
        ArrayList arrayList = null;
        if (attachedNotifChildren != null) {
            arrayList = new ArrayList();
            for (int i = 0; i < attachedNotifChildren.size(); i++) {
                arrayList.add(notifToBubbleEntry(attachedNotifChildren.get(i)));
            }
        }
        return this.mBubbles.handleDismissalInterception(notifToBubbleEntry(notificationEntry), arrayList, new BubblesManager$$ExternalSyntheticLambda1(this, attachedNotifChildren, notificationEntry), this.mSysuiMainExecutor);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleDismissalInterception$1(List list, NotificationEntry notificationEntry, int i) {
        if (i >= 0) {
            for (NotifCallback removeNotification : this.mCallbacks) {
                removeNotification.removeNotification((NotificationEntry) list.get(i), getDismissedByUserStats((NotificationEntry) list.get(i), true), 12);
            }
            return;
        }
        this.mNotificationGroupManager.onEntryRemoved(notificationEntry);
    }

    public void expandStackAndSelectBubble(NotificationEntry notificationEntry) {
        this.mBubbles.expandStackAndSelectBubble(notifToBubbleEntry(notificationEntry));
    }

    public void expandStackAndSelectBubble(Bubble bubble) {
        this.mBubbles.expandStackAndSelectBubble(bubble);
    }

    public Bubble getBubbleWithShortcutId(String str) {
        return this.mBubbles.getBubbleWithShortcutId(str);
    }

    public void addNotifCallback(NotifCallback notifCallback) {
        this.mCallbacks.add(notifCallback);
    }

    public void onUserSetImportantConversation(NotificationEntry notificationEntry) {
        if (notificationEntry.getBubbleMetadata() != null) {
            try {
                this.mBarService.onNotificationBubbleChanged(notificationEntry.getKey(), true, 2);
            } catch (RemoteException e) {
                Log.e("Bubbles", e.getMessage());
            }
            this.mShadeController.collapsePanel(true);
            if (notificationEntry.getRow() != null) {
                notificationEntry.getRow().updateBubbleButton();
            }
        }
    }

    public void onUserChangedBubble(NotificationEntry notificationEntry, boolean z) {
        NotificationChannel channel = notificationEntry.getChannel();
        String packageName = notificationEntry.getSbn().getPackageName();
        int uid = notificationEntry.getSbn().getUid();
        if (channel != null && packageName != null) {
            notificationEntry.setFlagBubble(z);
            try {
                this.mBarService.onNotificationBubbleChanged(notificationEntry.getKey(), z, 3);
            } catch (RemoteException unused) {
            }
            NotificationChannel createConversationChannelIfNeeded = NotificationChannelHelper.createConversationChannelIfNeeded(this.mContext, this.mNotificationManager, notificationEntry, channel);
            createConversationChannelIfNeeded.setAllowBubbles(z);
            try {
                int bubblePreferenceForPackage = this.mNotificationManager.getBubblePreferenceForPackage(packageName, uid);
                if (z && bubblePreferenceForPackage == 0) {
                    this.mNotificationManager.setBubblesAllowed(packageName, uid, 2);
                }
                this.mNotificationManager.updateNotificationChannelForPackage(packageName, uid, createConversationChannelIfNeeded);
            } catch (RemoteException e) {
                Log.e("Bubbles", e.getMessage());
            }
            if (z) {
                this.mShadeController.collapsePanel(true);
                if (notificationEntry.getRow() != null) {
                    notificationEntry.getRow().updateBubbleButton();
                }
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        this.mBubbles.dump(printWriter, strArr);
    }

    public static boolean areBubblesEnabled(Context context, UserHandle userHandle) {
        if (userHandle.getIdentifier() < 0) {
            if (Settings.Secure.getInt(context.getContentResolver(), "notification_bubbles", 0) == 1) {
                return true;
            }
            return false;
        } else if (Settings.Secure.getIntForUser(context.getContentResolver(), "notification_bubbles", 0, userHandle.getIdentifier()) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static BubbleEntry notifToBubbleEntry(NotificationEntry notificationEntry) {
        return new BubbleEntry(notificationEntry.getSbn(), notificationEntry.getRanking(), notificationEntry.isDismissable(), notificationEntry.shouldSuppressNotificationDot(), notificationEntry.shouldSuppressNotificationList(), notificationEntry.shouldSuppressPeek());
    }
}
