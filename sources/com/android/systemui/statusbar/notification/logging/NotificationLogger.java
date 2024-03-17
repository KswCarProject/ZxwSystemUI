package com.android.systemui.statusbar.notification.logging;

import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Trace;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class NotificationLogger implements StatusBarStateController.StateListener {
    public static final boolean DEBUG = Log.isLoggable("NotificationLogger", 3);
    public IStatusBarService mBarService;
    public final ArraySet<NotificationVisibility> mCurrentlyVisibleNotifications = new ArraySet<>();
    @GuardedBy({"mDozingLock"})
    public Boolean mDozing = null;
    public final Object mDozingLock = new Object();
    public final NotificationEntryManager mEntryManager;
    public final ExpansionStateLogger mExpansionStateLogger;
    public Handler mHandler = new Handler();
    public long mLastVisibilityReportUptimeMs;
    public NotificationListContainer mListContainer;
    @GuardedBy({"mDozingLock"})
    public Boolean mLockscreen = null;
    public boolean mLogging = false;
    public final NotifLiveDataStore mNotifLiveDataStore;
    public final NotifPipeline mNotifPipeline;
    public final NotificationListenerService mNotificationListener;
    public final OnChildLocationsChangedListener mNotificationLocationsChangedListener = new OnChildLocationsChangedListener() {
        public void onChildLocationsChanged() {
            NotificationLogger notificationLogger = NotificationLogger.this;
            if (!notificationLogger.mHandler.hasCallbacks(notificationLogger.mVisibilityReporter)) {
                NotificationLogger notificationLogger2 = NotificationLogger.this;
                notificationLogger2.mHandler.postAtTime(notificationLogger2.mVisibilityReporter, NotificationLogger.this.mLastVisibilityReportUptimeMs + 500);
            }
        }
    };
    public final NotificationPanelLogger mNotificationPanelLogger;
    public Boolean mPanelExpanded = null;
    public final Executor mUiBgExecutor;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public Runnable mVisibilityReporter = new Runnable() {
        public final ArraySet<NotificationVisibility> mTmpCurrentlyVisibleNotifications = new ArraySet<>();
        public final ArraySet<NotificationVisibility> mTmpNewlyVisibleNotifications = new ArraySet<>();
        public final ArraySet<NotificationVisibility> mTmpNoLongerVisibleNotifications = new ArraySet<>();

        public void run() {
            NotificationLogger.this.mLastVisibilityReportUptimeMs = SystemClock.uptimeMillis();
            List r0 = NotificationLogger.this.getVisibleNotifications();
            int size = r0.size();
            for (int i = 0; i < size; i++) {
                NotificationEntry notificationEntry = (NotificationEntry) r0.get(i);
                String key = notificationEntry.getSbn().getKey();
                boolean isInVisibleLocation = NotificationLogger.this.mListContainer.isInVisibleLocation(notificationEntry);
                NotificationVisibility obtain = NotificationVisibility.obtain(key, i, size, isInVisibleLocation, NotificationLogger.getNotificationLocation(notificationEntry));
                boolean contains = NotificationLogger.this.mCurrentlyVisibleNotifications.contains(obtain);
                if (isInVisibleLocation) {
                    this.mTmpCurrentlyVisibleNotifications.add(obtain);
                    if (!contains) {
                        this.mTmpNewlyVisibleNotifications.add(obtain);
                    }
                } else {
                    obtain.recycle();
                }
            }
            this.mTmpNoLongerVisibleNotifications.addAll(NotificationLogger.this.mCurrentlyVisibleNotifications);
            this.mTmpNoLongerVisibleNotifications.removeAll(this.mTmpCurrentlyVisibleNotifications);
            NotificationLogger.this.logNotificationVisibilityChanges(this.mTmpNewlyVisibleNotifications, this.mTmpNoLongerVisibleNotifications);
            NotificationLogger notificationLogger = NotificationLogger.this;
            notificationLogger.recycleAllVisibilityObjects((ArraySet<NotificationVisibility>) notificationLogger.mCurrentlyVisibleNotifications);
            NotificationLogger.this.mCurrentlyVisibleNotifications.addAll(this.mTmpCurrentlyVisibleNotifications);
            ExpansionStateLogger r02 = NotificationLogger.this.mExpansionStateLogger;
            ArraySet<NotificationVisibility> arraySet = this.mTmpCurrentlyVisibleNotifications;
            r02.onVisibilityChanged(arraySet, arraySet);
            Trace.traceCounter(4096, "Notifications [Active]", size);
            Trace.traceCounter(4096, "Notifications [Visible]", NotificationLogger.this.mCurrentlyVisibleNotifications.size());
            NotificationLogger.this.recycleAllVisibilityObjects(this.mTmpNoLongerVisibleNotifications);
            this.mTmpCurrentlyVisibleNotifications.clear();
            this.mTmpNewlyVisibleNotifications.clear();
            this.mTmpNoLongerVisibleNotifications.clear();
        }
    };

    public interface OnChildLocationsChangedListener {
        void onChildLocationsChanged();
    }

    public final List<NotificationEntry> getVisibleNotifications() {
        return this.mNotifLiveDataStore.getActiveNotifList().getValue();
    }

    public static NotificationVisibility.NotificationLocation getNotificationLocation(NotificationEntry notificationEntry) {
        if (notificationEntry == null || notificationEntry.getRow() == null || notificationEntry.getRow().getViewState() == null) {
            return NotificationVisibility.NotificationLocation.LOCATION_UNKNOWN;
        }
        return convertNotificationLocation(notificationEntry.getRow().getViewState().location);
    }

    public static NotificationVisibility.NotificationLocation convertNotificationLocation(int i) {
        if (i == 1) {
            return NotificationVisibility.NotificationLocation.LOCATION_FIRST_HEADS_UP;
        }
        if (i == 2) {
            return NotificationVisibility.NotificationLocation.LOCATION_HIDDEN_TOP;
        }
        if (i == 4) {
            return NotificationVisibility.NotificationLocation.LOCATION_MAIN_AREA;
        }
        if (i == 8) {
            return NotificationVisibility.NotificationLocation.LOCATION_BOTTOM_STACK_PEEKING;
        }
        if (i == 16) {
            return NotificationVisibility.NotificationLocation.LOCATION_BOTTOM_STACK_HIDDEN;
        }
        if (i != 64) {
            return NotificationVisibility.NotificationLocation.LOCATION_UNKNOWN;
        }
        return NotificationVisibility.NotificationLocation.LOCATION_GONE;
    }

    public NotificationLogger(NotificationListener notificationListener, Executor executor, NotifPipelineFlags notifPipelineFlags, NotifLiveDataStore notifLiveDataStore, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, StatusBarStateController statusBarStateController, ExpansionStateLogger expansionStateLogger, NotificationPanelLogger notificationPanelLogger) {
        this.mNotificationListener = notificationListener;
        this.mUiBgExecutor = executor;
        this.mNotifLiveDataStore = notifLiveDataStore;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mEntryManager = notificationEntryManager;
        this.mNotifPipeline = notifPipeline;
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mExpansionStateLogger = expansionStateLogger;
        this.mNotificationPanelLogger = notificationPanelLogger;
        statusBarStateController.addCallback(this);
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            registerNewPipelineListener();
        } else {
            registerLegacyListener();
        }
    }

    public final void registerLegacyListener() {
        this.mEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                NotificationLogger.this.mExpansionStateLogger.onEntryRemoved(notificationEntry.getKey());
            }

            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                NotificationLogger.this.mExpansionStateLogger.onEntryUpdated(notificationEntry.getKey());
            }

            public void onInflationError(StatusBarNotification statusBarNotification, Exception exc) {
                NotificationLogger.this.logNotificationError(statusBarNotification, exc);
            }
        });
    }

    public final void registerNewPipelineListener() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() {
            public void onEntryUpdated(NotificationEntry notificationEntry, boolean z) {
                NotificationLogger.this.mExpansionStateLogger.onEntryUpdated(notificationEntry.getKey());
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                NotificationLogger.this.mExpansionStateLogger.onEntryRemoved(notificationEntry.getKey());
            }
        });
    }

    public void setUpWithContainer(NotificationListContainer notificationListContainer) {
        this.mListContainer = notificationListContainer;
    }

    public void stopNotificationLogging() {
        if (this.mLogging) {
            this.mLogging = false;
            if (DEBUG) {
                Log.i("NotificationLogger", "stopNotificationLogging: log notifications invisible");
            }
            if (!this.mCurrentlyVisibleNotifications.isEmpty()) {
                logNotificationVisibilityChanges(Collections.emptyList(), this.mCurrentlyVisibleNotifications);
                recycleAllVisibilityObjects(this.mCurrentlyVisibleNotifications);
            }
            this.mHandler.removeCallbacks(this.mVisibilityReporter);
            this.mListContainer.setChildLocationsChangedListener((OnChildLocationsChangedListener) null);
        }
    }

    public void startNotificationLogging() {
        if (!this.mLogging) {
            this.mLogging = true;
            if (DEBUG) {
                Log.i("NotificationLogger", "startNotificationLogging");
            }
            this.mListContainer.setChildLocationsChangedListener(this.mNotificationLocationsChangedListener);
            this.mNotificationLocationsChangedListener.onChildLocationsChanged();
        }
    }

    public final void setDozing(boolean z) {
        synchronized (this.mDozingLock) {
            this.mDozing = Boolean.valueOf(z);
            maybeUpdateLoggingStatus();
        }
    }

    public final void logNotificationError(StatusBarNotification statusBarNotification, Exception exc) {
        try {
            this.mBarService.onNotificationError(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId(), statusBarNotification.getUid(), statusBarNotification.getInitialPid(), exc.getMessage(), statusBarNotification.getUserId());
        } catch (RemoteException unused) {
        }
    }

    public final void logNotificationVisibilityChanges(Collection<NotificationVisibility> collection, Collection<NotificationVisibility> collection2) {
        if (!collection.isEmpty() || !collection2.isEmpty()) {
            this.mUiBgExecutor.execute(new NotificationLogger$$ExternalSyntheticLambda0(this, cloneVisibilitiesAsArr(collection), cloneVisibilitiesAsArr(collection2)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$logNotificationVisibilityChanges$0(NotificationVisibility[] notificationVisibilityArr, NotificationVisibility[] notificationVisibilityArr2) {
        try {
            this.mBarService.onNotificationVisibilityChanged(notificationVisibilityArr, notificationVisibilityArr2);
        } catch (RemoteException unused) {
        }
        int length = notificationVisibilityArr.length;
        if (length > 0) {
            String[] strArr = new String[length];
            for (int i = 0; i < length; i++) {
                strArr[i] = notificationVisibilityArr[i].key;
            }
            try {
                this.mNotificationListener.setNotificationsShown(strArr);
            } catch (RuntimeException e) {
                Log.d("NotificationLogger", "failed setNotificationsShown: ", e);
            }
        }
        recycleAllVisibilityObjects(notificationVisibilityArr);
        recycleAllVisibilityObjects(notificationVisibilityArr2);
    }

    public final void recycleAllVisibilityObjects(ArraySet<NotificationVisibility> arraySet) {
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            arraySet.valueAt(i).recycle();
        }
        arraySet.clear();
    }

    public final void recycleAllVisibilityObjects(NotificationVisibility[] notificationVisibilityArr) {
        for (NotificationVisibility notificationVisibility : notificationVisibilityArr) {
            if (notificationVisibility != null) {
                notificationVisibility.recycle();
            }
        }
    }

    public static NotificationVisibility[] cloneVisibilitiesAsArr(Collection<NotificationVisibility> collection) {
        NotificationVisibility[] notificationVisibilityArr = new NotificationVisibility[collection.size()];
        int i = 0;
        for (NotificationVisibility next : collection) {
            if (next != null) {
                notificationVisibilityArr[i] = next.clone();
            }
            i++;
        }
        return notificationVisibilityArr;
    }

    @VisibleForTesting
    public Runnable getVisibilityReporter() {
        return this.mVisibilityReporter;
    }

    public void onStateChanged(int i) {
        if (DEBUG) {
            Log.i("NotificationLogger", "onStateChanged: new=" + i);
        }
        synchronized (this.mDozingLock) {
            boolean z = true;
            if (!(i == 1 || i == 2)) {
                z = false;
            }
            this.mLockscreen = Boolean.valueOf(z);
        }
    }

    public void onDozingChanged(boolean z) {
        if (DEBUG) {
            Log.i("NotificationLogger", "onDozingChanged: new=" + z);
        }
        setDozing(z);
    }

    @GuardedBy({"mDozingLock"})
    public final void maybeUpdateLoggingStatus() {
        boolean z = false;
        if (this.mPanelExpanded != null && this.mDozing != null) {
            Boolean bool = this.mLockscreen;
            if (bool != null) {
                z = bool.booleanValue();
            }
            if (!this.mPanelExpanded.booleanValue() || this.mDozing.booleanValue()) {
                if (DEBUG) {
                    Log.i("NotificationLogger", "Notification panel hidden, lockscreen=" + z);
                }
                stopNotificationLogging();
                return;
            }
            this.mNotificationPanelLogger.logPanelShown(z, getVisibleNotifications());
            if (DEBUG) {
                Log.i("NotificationLogger", "Notification panel shown, lockscreen=" + z);
            }
            startNotificationLogging();
        } else if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Panel status unclear: panelExpandedKnown=");
            sb.append(this.mPanelExpanded == null);
            sb.append(" dozingKnown=");
            if (this.mDozing == null) {
                z = true;
            }
            sb.append(z);
            Log.i("NotificationLogger", sb.toString());
        }
    }

    public void onPanelExpandedChanged(boolean z) {
        if (DEBUG) {
            Log.i("NotificationLogger", "onPanelExpandedChanged: new=" + z);
        }
        this.mPanelExpanded = Boolean.valueOf(z);
        synchronized (this.mDozingLock) {
            maybeUpdateLoggingStatus();
        }
    }

    public void onExpansionChanged(String str, boolean z, boolean z2) {
        this.mExpansionStateLogger.onExpansionChanged(str, z, z2, this.mVisibilityProvider.getLocation(str));
    }

    @VisibleForTesting
    public void setVisibilityReporter(Runnable runnable) {
        this.mVisibilityReporter = runnable;
    }

    public static class ExpansionStateLogger {
        @VisibleForTesting
        public IStatusBarService mBarService;
        public final Map<String, State> mExpansionStates = new ArrayMap();
        public final Map<String, Boolean> mLoggedExpansionState = new ArrayMap();
        public final Executor mUiBgExecutor;

        public ExpansionStateLogger(Executor executor) {
            this.mUiBgExecutor = executor;
            this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        }

        @VisibleForTesting
        public void onExpansionChanged(String str, boolean z, boolean z2, NotificationVisibility.NotificationLocation notificationLocation) {
            State state = getState(str);
            state.mIsUserAction = Boolean.valueOf(z);
            state.mIsExpanded = Boolean.valueOf(z2);
            state.mLocation = notificationLocation;
            maybeNotifyOnNotificationExpansionChanged(str, state);
        }

        @VisibleForTesting
        public void onVisibilityChanged(Collection<NotificationVisibility> collection, Collection<NotificationVisibility> collection2) {
            NotificationVisibility[] r7 = NotificationLogger.cloneVisibilitiesAsArr(collection);
            NotificationVisibility[] r8 = NotificationLogger.cloneVisibilitiesAsArr(collection2);
            for (NotificationVisibility notificationVisibility : r7) {
                State state = getState(notificationVisibility.key);
                state.mIsVisible = Boolean.TRUE;
                state.mLocation = notificationVisibility.location;
                maybeNotifyOnNotificationExpansionChanged(notificationVisibility.key, state);
            }
            for (NotificationVisibility notificationVisibility2 : r8) {
                getState(notificationVisibility2.key).mIsVisible = Boolean.FALSE;
            }
        }

        @VisibleForTesting
        public void onEntryRemoved(String str) {
            this.mExpansionStates.remove(str);
            this.mLoggedExpansionState.remove(str);
        }

        @VisibleForTesting
        public void onEntryUpdated(String str) {
            this.mLoggedExpansionState.remove(str);
        }

        public final State getState(String str) {
            State state = this.mExpansionStates.get(str);
            if (state != null) {
                return state;
            }
            State state2 = new State();
            this.mExpansionStates.put(str, state2);
            return state2;
        }

        public final void maybeNotifyOnNotificationExpansionChanged(String str, State state) {
            if (state.isFullySet() && state.mIsVisible.booleanValue()) {
                Boolean bool = this.mLoggedExpansionState.get(str);
                if (bool == null && !state.mIsExpanded.booleanValue()) {
                    return;
                }
                if (bool == null || state.mIsExpanded != bool) {
                    this.mLoggedExpansionState.put(str, state.mIsExpanded);
                    this.mUiBgExecutor.execute(new NotificationLogger$ExpansionStateLogger$$ExternalSyntheticLambda0(this, str, new State(state)));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$maybeNotifyOnNotificationExpansionChanged$0(String str, State state) {
            try {
                this.mBarService.onNotificationExpansionChanged(str, state.mIsUserAction.booleanValue(), state.mIsExpanded.booleanValue(), state.mLocation.ordinal());
            } catch (RemoteException e) {
                Log.e("NotificationLogger", "Failed to call onNotificationExpansionChanged: ", e);
            }
        }

        public static class State {
            public Boolean mIsExpanded;
            public Boolean mIsUserAction;
            public Boolean mIsVisible;
            public NotificationVisibility.NotificationLocation mLocation;

            public State() {
            }

            public State(State state) {
                this.mIsUserAction = state.mIsUserAction;
                this.mIsExpanded = state.mIsExpanded;
                this.mIsVisible = state.mIsVisible;
                this.mLocation = state.mLocation;
            }

            public final boolean isFullySet() {
                return (this.mIsUserAction == null || this.mIsExpanded == null || this.mIsVisible == null || this.mLocation == null) ? false : true;
            }
        }
    }
}
