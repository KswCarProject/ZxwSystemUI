package com.android.systemui.statusbar.notification;

import android.app.NotificationChannel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationRemoveInterceptor;
import com.android.systemui.statusbar.NotificationUiAdjustment;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRankerStub;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.util.Assert;
import com.android.systemui.util.leak.LeakDetector;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationEntryManager implements CommonNotifCollection, Dumpable, VisualStabilityManager.Callback {
    public static final boolean DEBUG = Log.isLoggable("NotificationEntryMgr", 3);
    public final ArrayMap<String, NotificationEntry> mActiveNotifications = new ArrayMap<>();
    public final Set<NotificationEntry> mAllNotifications;
    public final DumpManager mDumpManager;
    public final NotificationGroupManagerLegacy mGroupManager;
    public final NotificationRowContentBinder.InflationCallback mInflationCallback;
    public NotificationListenerService.RankingMap mLatestRankingMap;
    public final LeakDetector mLeakDetector;
    public final NotificationEntryManagerLogger mLogger;
    public final List<NotifCollectionListener> mNotifCollectionListeners;
    public final NotificationListener.NotificationHandler mNotifListener;
    public final NotifLiveDataStoreImpl mNotifLiveDataStore;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final List<NotificationEntryListener> mNotificationEntryListeners;
    @VisibleForTesting
    public final ArrayList<NotificationLifetimeExtender> mNotificationLifetimeExtenders;
    public final Lazy<NotificationRowBinder> mNotificationRowBinderLazy;
    @VisibleForTesting
    public final HashMap<String, NotificationEntry> mPendingNotifications = new HashMap<>();
    public NotificationPresenter mPresenter;
    public LegacyNotificationRanker mRanker;
    public final Set<NotificationEntry> mReadOnlyAllNotifications;
    public final List<NotificationEntry> mReadOnlyNotifications;
    public final Lazy<NotificationRemoteInputManager> mRemoteInputManagerLazy;
    public final List<NotificationRemoveInterceptor> mRemoveInterceptors;
    public final Map<NotificationEntry, NotificationLifetimeExtender> mRetainedNotifications;
    @VisibleForTesting
    public final ArrayList<NotificationEntry> mSortedAndFiltered;
    public final IStatusBarService mStatusBarService;

    public interface KeyguardEnvironment {
        boolean isDeviceProvisioned();

        boolean isNotificationForCurrentProfiles(StatusBarNotification statusBarNotification);
    }

    public NotificationEntryManager(NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotifPipelineFlags notifPipelineFlags, Lazy<NotificationRowBinder> lazy, Lazy<NotificationRemoteInputManager> lazy2, LeakDetector leakDetector, IStatusBarService iStatusBarService, NotifLiveDataStoreImpl notifLiveDataStoreImpl, DumpManager dumpManager) {
        ArraySet arraySet = new ArraySet();
        this.mAllNotifications = arraySet;
        this.mReadOnlyAllNotifications = Collections.unmodifiableSet(arraySet);
        ArrayList<NotificationEntry> arrayList = new ArrayList<>();
        this.mSortedAndFiltered = arrayList;
        this.mReadOnlyNotifications = Collections.unmodifiableList(arrayList);
        this.mRetainedNotifications = new ArrayMap();
        this.mNotifCollectionListeners = new ArrayList();
        this.mRanker = new LegacyNotificationRankerStub();
        this.mNotificationLifetimeExtenders = new ArrayList<>();
        this.mNotificationEntryListeners = new ArrayList();
        this.mRemoveInterceptors = new ArrayList();
        this.mInflationCallback = new NotificationRowContentBinder.InflationCallback() {
            public void handleInflationException(NotificationEntry notificationEntry, Exception exc) {
                Trace.beginSection("NotificationEntryManager.handleInflationException");
                NotificationEntryManager.this.handleInflationException(notificationEntry.getSbn(), exc);
                Trace.endSection();
            }

            public void onAsyncInflationFinished(NotificationEntry notificationEntry) {
                Trace.beginSection("NotificationEntryManager.onAsyncInflationFinished");
                NotificationEntryManager.this.mPendingNotifications.remove(notificationEntry.getKey());
                if (!notificationEntry.isRowRemoved()) {
                    boolean z = NotificationEntryManager.this.getActiveNotificationUnfiltered(notificationEntry.getKey()) == null;
                    NotificationEntryManager.this.mLogger.logNotifInflated(notificationEntry.getKey(), z);
                    if (z) {
                        for (NotificationEntryListener onEntryInflated : NotificationEntryManager.this.mNotificationEntryListeners) {
                            onEntryInflated.onEntryInflated(notificationEntry);
                        }
                        NotificationEntryManager.this.addActiveNotification(notificationEntry);
                        NotificationEntryManager.this.updateNotifications("onAsyncInflationFinished");
                        for (NotificationEntryListener onNotificationAdded : NotificationEntryManager.this.mNotificationEntryListeners) {
                            onNotificationAdded.onNotificationAdded(notificationEntry);
                        }
                    } else {
                        for (NotificationEntryListener onEntryReinflated : NotificationEntryManager.this.mNotificationEntryListeners) {
                            onEntryReinflated.onEntryReinflated(notificationEntry);
                        }
                    }
                }
                Trace.endSection();
            }
        };
        this.mNotifListener = new NotificationListener.NotificationHandler() {
            public void onNotificationsInitialized() {
            }

            public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                if (NotificationEntryManager.this.mActiveNotifications.containsKey(statusBarNotification.getKey())) {
                    NotificationEntryManager.this.updateNotification(statusBarNotification, rankingMap);
                } else {
                    NotificationEntryManager.this.addNotification(statusBarNotification, rankingMap);
                }
            }

            public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
                NotificationEntryManager.this.removeNotification(statusBarNotification.getKey(), rankingMap, i);
            }

            public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
                NotificationEntryManager.this.updateNotificationRanking(rankingMap);
            }

            public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                NotificationEntryManager.this.notifyChannelModified(str, userHandle, notificationChannel, i);
            }
        };
        this.mLogger = notificationEntryManagerLogger;
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mNotificationRowBinderLazy = lazy;
        this.mRemoteInputManagerLazy = lazy2;
        this.mLeakDetector = leakDetector;
        this.mStatusBarService = iStatusBarService;
        this.mNotifLiveDataStore = notifLiveDataStoreImpl;
        this.mDumpManager = dumpManager;
    }

    public void initialize(NotificationListener notificationListener, LegacyNotificationRanker legacyNotificationRanker) {
        this.mRanker = legacyNotificationRanker;
        notificationListener.addNotificationHandler(this.mNotifListener);
        this.mDumpManager.registerDumpable(this);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationEntryManager state:");
        printWriter.println("  mAllNotifications=");
        if (this.mAllNotifications.size() == 0) {
            printWriter.println("null");
        } else {
            int i = 0;
            for (NotificationEntry dumpEntry : this.mAllNotifications) {
                dumpEntry(printWriter, "  ", i, dumpEntry);
                i++;
            }
        }
        printWriter.print("  mPendingNotifications=");
        if (this.mPendingNotifications.size() == 0) {
            printWriter.println("null");
        } else {
            for (NotificationEntry sbn : this.mPendingNotifications.values()) {
                printWriter.println(sbn.getSbn());
            }
        }
        printWriter.println("  Remove interceptors registered:");
        for (NotificationRemoveInterceptor notificationRemoveInterceptor : this.mRemoveInterceptors) {
            printWriter.println("    " + notificationRemoveInterceptor.getClass().getSimpleName());
        }
        printWriter.println("  Lifetime extenders registered:");
        Iterator<NotificationLifetimeExtender> it = this.mNotificationLifetimeExtenders.iterator();
        while (it.hasNext()) {
            printWriter.println("    " + it.next().getClass().getSimpleName());
        }
        printWriter.println("  Lifetime-extended notifications:");
        if (this.mRetainedNotifications.isEmpty()) {
            printWriter.println("    None");
            return;
        }
        for (Map.Entry next : this.mRetainedNotifications.entrySet()) {
            printWriter.println("    " + ((NotificationEntry) next.getKey()).getSbn() + " retained by " + ((NotificationLifetimeExtender) next.getValue()).getClass().getName());
        }
    }

    public void addNotificationEntryListener(NotificationEntryListener notificationEntryListener) {
        this.mNotificationEntryListeners.add(notificationEntryListener);
    }

    public void addNotificationRemoveInterceptor(NotificationRemoveInterceptor notificationRemoveInterceptor) {
        this.mRemoveInterceptors.add(notificationRemoveInterceptor);
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
    }

    public void addNotificationLifetimeExtenders(List<NotificationLifetimeExtender> list) {
        for (NotificationLifetimeExtender addNotificationLifetimeExtender : list) {
            addNotificationLifetimeExtender(addNotificationLifetimeExtender);
        }
    }

    public void addNotificationLifetimeExtender(NotificationLifetimeExtender notificationLifetimeExtender) {
        this.mNotificationLifetimeExtenders.add(notificationLifetimeExtender);
        notificationLifetimeExtender.setCallback(new NotificationEntryManager$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addNotificationLifetimeExtender$0(String str) {
        removeNotification(str, this.mLatestRankingMap, 0);
    }

    public void onChangeAllowed() {
        updateNotifications("reordering is now allowed");
    }

    public void performRemoveNotification(StatusBarNotification statusBarNotification, DismissedByUserStats dismissedByUserStats, int i) {
        removeNotificationInternal(statusBarNotification.getKey(), (NotificationListenerService.RankingMap) null, dismissedByUserStats.notificationVisibility, false, dismissedByUserStats, i);
    }

    public final NotificationVisibility obtainVisibility(String str) {
        NotificationEntry notificationEntry = this.mActiveNotifications.get(str);
        return NotificationVisibility.obtain(str, notificationEntry != null ? notificationEntry.getRanking().getRank() : 0, this.mActiveNotifications.size(), true, NotificationLogger.getNotificationLocation(getActiveNotificationUnfiltered(str)));
    }

    public final void abortExistingInflation(String str, String str2) {
        if (this.mPendingNotifications.containsKey(str)) {
            this.mPendingNotifications.get(str).abortTask();
            this.mPendingNotifications.remove(str);
            this.mLogger.logInflationAborted(str, "pending", str2);
        }
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        if (activeNotificationUnfiltered != null) {
            activeNotificationUnfiltered.abortTask();
            this.mLogger.logInflationAborted(str, "active", str2);
        }
    }

    public final void handleInflationException(StatusBarNotification statusBarNotification, Exception exc) {
        removeNotificationInternal(statusBarNotification.getKey(), (NotificationListenerService.RankingMap) null, (NotificationVisibility) null, true, (DismissedByUserStats) null, 4);
        for (NotificationEntryListener onInflationError : this.mNotificationEntryListeners) {
            onInflationError.onInflationError(statusBarNotification, exc);
        }
    }

    public final void addActiveNotification(NotificationEntry notificationEntry) {
        Assert.isMainThread();
        this.mActiveNotifications.put(notificationEntry.getKey(), notificationEntry);
        this.mGroupManager.onEntryAdded(notificationEntry);
        updateRankingAndSort(this.mRanker.getRankingMap(), "addEntryInternalInternal");
    }

    @VisibleForTesting
    public void addActiveNotificationForTest(NotificationEntry notificationEntry) {
        this.mActiveNotifications.put(notificationEntry.getKey(), notificationEntry);
        this.mGroupManager.onEntryAdded(notificationEntry);
        reapplyFilterAndSort("addVisibleNotification");
    }

    @VisibleForTesting
    public void removeNotification(String str, NotificationListenerService.RankingMap rankingMap, int i) {
        removeNotificationInternal(str, rankingMap, obtainVisibility(str), false, (DismissedByUserStats) null, i);
    }

    public final void removeNotificationInternal(String str, NotificationListenerService.RankingMap rankingMap, NotificationVisibility notificationVisibility, boolean z, DismissedByUserStats dismissedByUserStats, int i) {
        boolean z2;
        Trace.beginSection("NotificationEntryManager.removeNotificationInternal");
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        for (NotificationRemoveInterceptor onNotificationRemoveRequested : this.mRemoveInterceptors) {
            if (onNotificationRemoveRequested.onNotificationRemoveRequested(str, activeNotificationUnfiltered, i)) {
                this.mLogger.logRemovalIntercepted(str);
                Trace.endSection();
                return;
            }
        }
        boolean z3 = true;
        if (activeNotificationUnfiltered == null) {
            NotificationEntry notificationEntry = this.mPendingNotifications.get(str);
            if (notificationEntry != null) {
                Iterator<NotificationLifetimeExtender> it = this.mNotificationLifetimeExtenders.iterator();
                boolean z4 = false;
                while (it.hasNext()) {
                    NotificationLifetimeExtender next = it.next();
                    if (next.shouldExtendLifetimeForPendingNotification(notificationEntry)) {
                        extendLifetime(notificationEntry, next);
                        this.mLogger.logLifetimeExtended(str, next.getClass().getName(), "pending");
                        z4 = true;
                    }
                }
                if (!z4) {
                    abortExistingInflation(str, "removeNotification");
                    for (NotifCollectionListener onEntryRemoved : this.mNotifCollectionListeners) {
                        onEntryRemoved.onEntryRemoved(notificationEntry, 0);
                    }
                    for (NotifCollectionListener onEntryCleanUp : this.mNotifCollectionListeners) {
                        onEntryCleanUp.onEntryCleanUp(notificationEntry);
                    }
                    this.mAllNotifications.remove(notificationEntry);
                    this.mLeakDetector.trackGarbage(notificationEntry);
                }
            }
        } else {
            boolean isRowDismissed = activeNotificationUnfiltered.isRowDismissed();
            if (!z && !isRowDismissed) {
                Iterator<NotificationLifetimeExtender> it2 = this.mNotificationLifetimeExtenders.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    NotificationLifetimeExtender next2 = it2.next();
                    if (next2.shouldExtendLifetime(activeNotificationUnfiltered)) {
                        this.mLatestRankingMap = rankingMap;
                        extendLifetime(activeNotificationUnfiltered, next2);
                        this.mLogger.logLifetimeExtended(str, next2.getClass().getName(), "active");
                        z2 = true;
                        break;
                    }
                }
            }
            z2 = false;
            if (!z2) {
                abortExistingInflation(str, "removeNotification");
                this.mAllNotifications.remove(activeNotificationUnfiltered);
                cancelLifetimeExtension(activeNotificationUnfiltered);
                if (activeNotificationUnfiltered.rowExists()) {
                    activeNotificationUnfiltered.removeRow();
                }
                handleGroupSummaryRemoved(str);
                removeVisibleNotification(str);
                updateNotifications("removeNotificationInternal");
                if (dismissedByUserStats == null) {
                    z3 = false;
                }
                this.mLogger.logNotifRemoved(activeNotificationUnfiltered.getKey(), z3);
                if (z3 && notificationVisibility != null) {
                    sendNotificationRemovalToServer(activeNotificationUnfiltered.getSbn(), dismissedByUserStats);
                }
                for (NotificationEntryListener onEntryRemoved2 : this.mNotificationEntryListeners) {
                    onEntryRemoved2.onEntryRemoved(activeNotificationUnfiltered, notificationVisibility, z3, i);
                }
                for (NotifCollectionListener onEntryRemoved3 : this.mNotifCollectionListeners) {
                    onEntryRemoved3.onEntryRemoved(activeNotificationUnfiltered, 0);
                }
                for (NotifCollectionListener onEntryCleanUp2 : this.mNotifCollectionListeners) {
                    onEntryCleanUp2.onEntryCleanUp(activeNotificationUnfiltered);
                }
                this.mLeakDetector.trackGarbage(activeNotificationUnfiltered);
            }
        }
        Trace.endSection();
    }

    public final void sendNotificationRemovalToServer(StatusBarNotification statusBarNotification, DismissedByUserStats dismissedByUserStats) {
        try {
            this.mStatusBarService.onNotificationClear(statusBarNotification.getPackageName(), statusBarNotification.getUser().getIdentifier(), statusBarNotification.getKey(), dismissedByUserStats.dismissalSurface, dismissedByUserStats.dismissalSentiment, dismissedByUserStats.notificationVisibility);
        } catch (RemoteException unused) {
        }
    }

    public final void handleGroupSummaryRemoved(String str) {
        List<NotificationEntry> attachedNotifChildren;
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        if (activeNotificationUnfiltered != null && activeNotificationUnfiltered.rowExists() && activeNotificationUnfiltered.isSummaryWithChildren()) {
            if ((activeNotificationUnfiltered.getSbn().getOverrideGroupKey() == null || activeNotificationUnfiltered.isRowDismissed()) && (attachedNotifChildren = activeNotificationUnfiltered.getAttachedNotifChildren()) != null) {
                for (int i = 0; i < attachedNotifChildren.size(); i++) {
                    NotificationEntry notificationEntry = attachedNotifChildren.get(i);
                    boolean z = (activeNotificationUnfiltered.getSbn().getNotification().flags & 64) != 0;
                    boolean z2 = this.mRemoteInputManagerLazy.get().shouldKeepForRemoteInputHistory(notificationEntry) || this.mRemoteInputManagerLazy.get().shouldKeepForSmartReplyHistory(notificationEntry);
                    if (!z && !z2) {
                        notificationEntry.setKeepInParent(true);
                        notificationEntry.removeRow();
                    }
                }
            }
        }
    }

    public final void addNotificationInternal(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) throws InflationException {
        Trace.beginSection("NotificationEntryManager.addNotificationInternal");
        String key = statusBarNotification.getKey();
        if (DEBUG) {
            Log.d("NotificationEntryMgr", "addNotification key=" + key);
        }
        updateRankingAndSort(rankingMap, "addNotificationInternal");
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        rankingMap.getRanking(key, ranking);
        NotificationEntry notificationEntry = this.mPendingNotifications.get(key);
        if (notificationEntry != null) {
            notificationEntry.setSbn(statusBarNotification);
            notificationEntry.setRanking(ranking);
        } else {
            notificationEntry = new NotificationEntry(statusBarNotification, ranking, SystemClock.uptimeMillis());
            this.mAllNotifications.add(notificationEntry);
            this.mLeakDetector.trackInstance(notificationEntry);
            for (NotifCollectionListener onEntryInit : this.mNotifCollectionListeners) {
                onEntryInit.onEntryInit(notificationEntry);
            }
        }
        for (NotifCollectionListener onEntryBind : this.mNotifCollectionListeners) {
            onEntryBind.onEntryBind(notificationEntry, statusBarNotification);
        }
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mNotificationRowBinderLazy.get().inflateViews(notificationEntry, (NotifInflater.Params) null, this.mInflationCallback);
        }
        this.mPendingNotifications.put(key, notificationEntry);
        this.mLogger.logNotifAdded(notificationEntry.getKey());
        for (NotificationEntryListener onPendingEntryAdded : this.mNotificationEntryListeners) {
            onPendingEntryAdded.onPendingEntryAdded(notificationEntry);
        }
        for (NotifCollectionListener onEntryAdded : this.mNotifCollectionListeners) {
            onEntryAdded.onEntryAdded(notificationEntry);
        }
        for (NotifCollectionListener onRankingApplied : this.mNotifCollectionListeners) {
            onRankingApplied.onRankingApplied();
        }
        Trace.endSection();
    }

    public void addNotification(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        try {
            addNotificationInternal(statusBarNotification, rankingMap);
        } catch (InflationException e) {
            handleInflationException(statusBarNotification, e);
        }
    }

    public final void updateNotificationInternal(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) throws InflationException {
        Trace.beginSection("NotificationEntryManager.updateNotificationInternal");
        if (DEBUG) {
            Log.d("NotificationEntryMgr", "updateNotification(" + statusBarNotification + ")");
        }
        String key = statusBarNotification.getKey();
        abortExistingInflation(key, "updateNotification");
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(key);
        if (activeNotificationUnfiltered == null) {
            Trace.endSection();
            return;
        }
        cancelLifetimeExtension(activeNotificationUnfiltered);
        updateRankingAndSort(rankingMap, "updateNotificationInternal");
        StatusBarNotification sbn = activeNotificationUnfiltered.getSbn();
        activeNotificationUnfiltered.setSbn(statusBarNotification);
        for (NotifCollectionListener onEntryBind : this.mNotifCollectionListeners) {
            onEntryBind.onEntryBind(activeNotificationUnfiltered, statusBarNotification);
        }
        this.mGroupManager.onEntryUpdated(activeNotificationUnfiltered, sbn);
        this.mLogger.logNotifUpdated(activeNotificationUnfiltered.getKey());
        for (NotificationEntryListener onPreEntryUpdated : this.mNotificationEntryListeners) {
            onPreEntryUpdated.onPreEntryUpdated(activeNotificationUnfiltered);
        }
        boolean z = rankingMap != null;
        for (NotifCollectionListener onEntryUpdated : this.mNotifCollectionListeners) {
            onEntryUpdated.onEntryUpdated(activeNotificationUnfiltered, z);
        }
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mNotificationRowBinderLazy.get().inflateViews(activeNotificationUnfiltered, (NotifInflater.Params) null, this.mInflationCallback);
        }
        updateNotifications("updateNotificationInternal");
        for (NotificationEntryListener onPostEntryUpdated : this.mNotificationEntryListeners) {
            onPostEntryUpdated.onPostEntryUpdated(activeNotificationUnfiltered);
        }
        for (NotifCollectionListener onRankingApplied : this.mNotifCollectionListeners) {
            onRankingApplied.onRankingApplied();
        }
        Trace.endSection();
    }

    public void updateNotification(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        try {
            updateNotificationInternal(statusBarNotification, rankingMap);
        } catch (InflationException e) {
            handleInflationException(statusBarNotification, e);
        }
    }

    public void updateNotifications(String str) {
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mLogger.logUseWhileNewPipelineActive("updateNotifications", str);
            return;
        }
        Trace.beginSection("NotificationEntryManager.updateNotifications");
        reapplyFilterAndSort(str);
        NotificationPresenter notificationPresenter = this.mPresenter;
        if (notificationPresenter != null) {
            notificationPresenter.updateNotificationViews(str);
        }
        this.mNotifLiveDataStore.setActiveNotifList(getVisibleNotifications());
        Trace.endSection();
    }

    public void updateNotificationRanking(NotificationListenerService.RankingMap rankingMap) {
        Trace.beginSection("NotificationEntryManager.updateNotificationRanking");
        ArrayList<NotificationEntry> arrayList = new ArrayList<>();
        arrayList.addAll(getVisibleNotifications());
        arrayList.addAll(this.mPendingNotifications.values());
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        for (NotificationEntry notificationEntry : arrayList) {
            arrayMap.put(notificationEntry.getKey(), NotificationUiAdjustment.extractFromNotificationEntry(notificationEntry));
            arrayMap2.put(notificationEntry.getKey(), Integer.valueOf(notificationEntry.getImportance()));
        }
        updateRankingAndSort(rankingMap, "updateNotificationRanking");
        updateRankingOfPendingNotifications(rankingMap);
        for (NotificationEntry notificationEntry2 : arrayList) {
            this.mNotificationRowBinderLazy.get().onNotificationRankingUpdated(notificationEntry2, (Integer) arrayMap2.get(notificationEntry2.getKey()), (NotificationUiAdjustment) arrayMap.get(notificationEntry2.getKey()), NotificationUiAdjustment.extractFromNotificationEntry(notificationEntry2), this.mInflationCallback);
        }
        updateNotifications("updateNotificationRanking");
        for (NotificationEntryListener onNotificationRankingUpdated : this.mNotificationEntryListeners) {
            onNotificationRankingUpdated.onNotificationRankingUpdated(rankingMap);
        }
        for (NotifCollectionListener onRankingUpdate : this.mNotifCollectionListeners) {
            onRankingUpdate.onRankingUpdate(rankingMap);
        }
        for (NotifCollectionListener onRankingApplied : this.mNotifCollectionListeners) {
            onRankingApplied.onRankingApplied();
        }
        Trace.endSection();
    }

    public void notifyChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        for (NotifCollectionListener onNotificationChannelModified : this.mNotifCollectionListeners) {
            onNotificationChannelModified.onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }
        for (NotificationEntryListener onNotificationChannelModified2 : this.mNotificationEntryListeners) {
            onNotificationChannelModified2.onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }
    }

    public final void updateRankingOfPendingNotifications(NotificationListenerService.RankingMap rankingMap) {
        if (rankingMap != null) {
            for (NotificationEntry next : this.mPendingNotifications.values()) {
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                if (rankingMap.getRanking(next.getKey(), ranking)) {
                    next.setRanking(ranking);
                }
            }
        }
    }

    public Iterable<NotificationEntry> getPendingNotificationsIterator() {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return this.mPendingNotifications.values();
    }

    public NotificationEntry getActiveNotificationUnfiltered(String str) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return this.mActiveNotifications.get(str);
    }

    public NotificationEntry getPendingOrActiveNotif(String str) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        NotificationEntry notificationEntry = this.mPendingNotifications.get(str);
        if (notificationEntry != null) {
            return notificationEntry;
        }
        return this.mActiveNotifications.get(str);
    }

    public final void extendLifetime(NotificationEntry notificationEntry, NotificationLifetimeExtender notificationLifetimeExtender) {
        NotificationLifetimeExtender notificationLifetimeExtender2 = this.mRetainedNotifications.get(notificationEntry);
        if (!(notificationLifetimeExtender2 == null || notificationLifetimeExtender2 == notificationLifetimeExtender)) {
            notificationLifetimeExtender2.setShouldManageLifetime(notificationEntry, false);
        }
        this.mRetainedNotifications.put(notificationEntry, notificationLifetimeExtender);
        notificationLifetimeExtender.setShouldManageLifetime(notificationEntry, true);
    }

    public final void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        NotificationLifetimeExtender remove = this.mRetainedNotifications.remove(notificationEntry);
        if (remove != null) {
            remove.setShouldManageLifetime(notificationEntry, false);
        }
    }

    public final void removeVisibleNotification(String str) {
        Assert.isMainThread();
        NotificationEntry remove = this.mActiveNotifications.remove(str);
        if (remove != null) {
            this.mGroupManager.onEntryRemoved(remove);
        }
    }

    public List<NotificationEntry> getActiveNotificationsForCurrentUser() {
        Trace.beginSection("NotificationEntryManager.getActiveNotificationsForCurrentUser");
        Assert.isMainThread();
        ArrayList arrayList = new ArrayList();
        int size = this.mActiveNotifications.size();
        for (int i = 0; i < size; i++) {
            NotificationEntry valueAt = this.mActiveNotifications.valueAt(i);
            if (this.mRanker.isNotificationForCurrentProfiles(valueAt)) {
                arrayList.add(valueAt);
            }
        }
        Trace.endSection();
        return arrayList;
    }

    public void reapplyFilterAndSort(String str) {
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mLogger.logUseWhileNewPipelineActive("reapplyFilterAndSort", str);
            return;
        }
        Trace.beginSection("NotificationEntryManager.reapplyFilterAndSort");
        updateRankingAndSort(this.mRanker.getRankingMap(), str);
        Trace.endSection();
    }

    public final void updateRankingAndSort(NotificationListenerService.RankingMap rankingMap, String str) {
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mLogger.logUseWhileNewPipelineActive("updateRankingAndSort", str);
            return;
        }
        Trace.beginSection("NotificationEntryManager.updateRankingAndSort");
        this.mSortedAndFiltered.clear();
        this.mSortedAndFiltered.addAll(this.mRanker.updateRanking(rankingMap, this.mActiveNotifications.values(), str));
        Trace.endSection();
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println("NotificationEntryManager (Legacy)");
        int size = this.mSortedAndFiltered.size();
        printWriter.print(str);
        printWriter.println("active notifications: " + size);
        int i = 0;
        while (i < size) {
            dumpEntry(printWriter, str, i, this.mSortedAndFiltered.get(i));
            i++;
        }
        synchronized (this.mActiveNotifications) {
            int size2 = this.mActiveNotifications.size();
            printWriter.print(str);
            printWriter.println("inactive notifications: " + (size2 - i));
            int i2 = 0;
            for (int i3 = 0; i3 < size2; i3++) {
                NotificationEntry valueAt = this.mActiveNotifications.valueAt(i3);
                if (!this.mSortedAndFiltered.contains(valueAt)) {
                    dumpEntry(printWriter, str, i2, valueAt);
                    i2++;
                }
            }
        }
    }

    public final void dumpEntry(PrintWriter printWriter, String str, int i, NotificationEntry notificationEntry) {
        printWriter.print(str);
        printWriter.println("  [" + i + "] key=" + notificationEntry.getKey() + " icon=" + notificationEntry.getIcons().getStatusBarIcon());
        StatusBarNotification sbn = notificationEntry.getSbn();
        printWriter.print(str);
        printWriter.println("      pkg=" + sbn.getPackageName() + " id=" + sbn.getId() + " importance=" + notificationEntry.getRanking().getImportance());
        printWriter.print(str);
        StringBuilder sb = new StringBuilder();
        sb.append("      notification=");
        sb.append(sbn.getNotification());
        printWriter.println(sb.toString());
    }

    public List<NotificationEntry> getVisibleNotifications() {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return this.mReadOnlyNotifications;
    }

    public Collection<NotificationEntry> getAllNotifs() {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return this.mReadOnlyAllNotifications;
    }

    public NotificationEntry getEntry(String str) {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return getPendingOrActiveNotif(str);
    }

    public boolean hasActiveNotifications() {
        this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        return this.mReadOnlyNotifications.size() != 0;
    }

    public void addCollectionListener(NotifCollectionListener notifCollectionListener) {
        this.mNotifCollectionListeners.add(notifCollectionListener);
    }
}
