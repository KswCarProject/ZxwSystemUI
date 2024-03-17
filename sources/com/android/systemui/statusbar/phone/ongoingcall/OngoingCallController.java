package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.IActivityManager;
import android.app.IUidObserver;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.R$id;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController implements CallbackController<OngoingCallListener>, Dumpable {
    @NotNull
    public final ActivityStarter activityStarter;
    @Nullable
    public CallNotificationInfo callNotificationInfo;
    @Nullable
    public View chipView;
    @NotNull
    public final Context context;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final IActivityManager iActivityManager;
    public boolean isFullscreen;
    @NotNull
    public final OngoingCallLogger logger;
    @NotNull
    public final List<OngoingCallListener> mListeners = new ArrayList();
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final CommonNotifCollection notifCollection;
    @NotNull
    public final OngoingCallController$notifListener$1 notifListener = new OngoingCallController$notifListener$1(this);
    @NotNull
    public final OngoingCallFlags ongoingCallFlags;
    @NotNull
    public final StatusBarStateController statusBarStateController;
    @NotNull
    public final OngoingCallController$statusBarStateListener$1 statusBarStateListener = new OngoingCallController$statusBarStateListener$1(this);
    @NotNull
    public final Optional<StatusBarWindowController> statusBarWindowController;
    @NotNull
    public final Optional<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandler;
    @NotNull
    public final SystemClock systemClock;
    @NotNull
    public final CallAppUidObserver uidObserver = new CallAppUidObserver();

    public final boolean isProcessVisibleToUser(int i) {
        return i <= 2;
    }

    public OngoingCallController(@NotNull Context context2, @NotNull CommonNotifCollection commonNotifCollection, @NotNull OngoingCallFlags ongoingCallFlags2, @NotNull SystemClock systemClock2, @NotNull ActivityStarter activityStarter2, @NotNull Executor executor, @NotNull IActivityManager iActivityManager2, @NotNull OngoingCallLogger ongoingCallLogger, @NotNull DumpManager dumpManager2, @NotNull Optional<StatusBarWindowController> optional, @NotNull Optional<SwipeStatusBarAwayGestureHandler> optional2, @NotNull StatusBarStateController statusBarStateController2) {
        this.context = context2;
        this.notifCollection = commonNotifCollection;
        this.ongoingCallFlags = ongoingCallFlags2;
        this.systemClock = systemClock2;
        this.activityStarter = activityStarter2;
        this.mainExecutor = executor;
        this.iActivityManager = iActivityManager2;
        this.logger = ongoingCallLogger;
        this.dumpManager = dumpManager2;
        this.statusBarWindowController = optional;
        this.swipeStatusBarAwayGestureHandler = optional2;
        this.statusBarStateController = statusBarStateController2;
    }

    public final void init() {
        this.dumpManager.registerDumpable(this);
        if (this.ongoingCallFlags.isStatusBarChipEnabled()) {
            this.notifCollection.addCollectionListener(this.notifListener);
            this.statusBarStateController.addCallback(this.statusBarStateListener);
        }
    }

    public final void setChipView(@NotNull View view) {
        tearDownChipView();
        this.chipView = view;
        if (hasOngoingCall()) {
            updateChip();
        }
    }

    public final void notifyChipVisibilityChanged(boolean z) {
        this.logger.logChipVisibilityChanged(z);
    }

    public final boolean hasOngoingCall() {
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        if (!(callNotificationInfo2 != null && callNotificationInfo2.isOngoing()) || this.uidObserver.isCallAppVisible()) {
            return false;
        }
        return true;
    }

    public void addCallback(@NotNull OngoingCallListener ongoingCallListener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(ongoingCallListener)) {
                this.mListeners.add(ongoingCallListener);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public void removeCallback(@NotNull OngoingCallListener ongoingCallListener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(ongoingCallListener);
        }
    }

    public final void updateChip() {
        OngoingCallChronometer ongoingCallChronometer;
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        if (callNotificationInfo2 != null) {
            View view = this.chipView;
            if (view == null) {
                ongoingCallChronometer = null;
            } else {
                ongoingCallChronometer = getTimeView(view);
            }
            if (view == null || ongoingCallChronometer == null) {
                this.callNotificationInfo = null;
                if (OngoingCallControllerKt.DEBUG) {
                    Log.w("OngoingCallController", "Ongoing call chip view could not be found; Not displaying chip in status bar");
                    return;
                }
                return;
            }
            if (callNotificationInfo2.hasValidStartTime()) {
                ongoingCallChronometer.setShouldHideText(false);
                ongoingCallChronometer.setBase((callNotificationInfo2.getCallStartTime() - this.systemClock.currentTimeMillis()) + this.systemClock.elapsedRealtime());
                ongoingCallChronometer.start();
            } else {
                ongoingCallChronometer.setShouldHideText(true);
                ongoingCallChronometer.stop();
            }
            updateChipClickListener();
            this.uidObserver.registerWithUid(callNotificationInfo2.getUid());
            if (!callNotificationInfo2.getStatusBarSwipedAway()) {
                this.statusBarWindowController.ifPresent(OngoingCallController$updateChip$1.INSTANCE);
            }
            updateGestureListening();
            for (OngoingCallListener onOngoingCallStateChanged : this.mListeners) {
                onOngoingCallStateChanged.onOngoingCallStateChanged(true);
            }
        }
    }

    public final void updateChipClickListener() {
        View view;
        if (this.callNotificationInfo != null) {
            PendingIntent pendingIntent = null;
            if (!this.isFullscreen || this.ongoingCallFlags.isInImmersiveChipTapEnabled()) {
                View view2 = this.chipView;
                if (view2 == null) {
                    view = null;
                } else {
                    view = view2.findViewById(R$id.ongoing_call_chip_background);
                }
                CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
                if (callNotificationInfo2 != null) {
                    pendingIntent = callNotificationInfo2.getIntent();
                }
                if (view2 != null && view != null && pendingIntent != null) {
                    view2.setOnClickListener(new OngoingCallController$updateChipClickListener$1(this, pendingIntent, view));
                    return;
                }
                return;
            }
            View view3 = this.chipView;
            if (view3 != null) {
                view3.setOnClickListener((View.OnClickListener) null);
            }
        }
    }

    public final void updateGestureListening() {
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        if (callNotificationInfo2 != null) {
            boolean z = false;
            if (callNotificationInfo2 != null && callNotificationInfo2.getStatusBarSwipedAway()) {
                z = true;
            }
            if (!z && this.isFullscreen) {
                this.swipeStatusBarAwayGestureHandler.ifPresent(new OngoingCallController$updateGestureListening$2(this));
                return;
            }
        }
        this.swipeStatusBarAwayGestureHandler.ifPresent(OngoingCallController$updateGestureListening$1.INSTANCE);
    }

    public final void removeChip() {
        this.callNotificationInfo = null;
        tearDownChipView();
        this.statusBarWindowController.ifPresent(OngoingCallController$removeChip$1.INSTANCE);
        this.swipeStatusBarAwayGestureHandler.ifPresent(OngoingCallController$removeChip$2.INSTANCE);
        for (OngoingCallListener onOngoingCallStateChanged : this.mListeners) {
            onOngoingCallStateChanged.onOngoingCallStateChanged(true);
        }
        this.uidObserver.unregister();
    }

    @Nullable
    public final Unit tearDownChipView() {
        OngoingCallChronometer timeView;
        View view = this.chipView;
        if (view == null || (timeView = getTimeView(view)) == null) {
            return null;
        }
        timeView.stop();
        return Unit.INSTANCE;
    }

    public final OngoingCallChronometer getTimeView(View view) {
        return (OngoingCallChronometer) view.findViewById(R$id.ongoing_call_chip_time);
    }

    public final void onSwipeAwayGestureDetected() {
        if (OngoingCallControllerKt.DEBUG) {
            Log.d("OngoingCallController", "Swipe away gesture detected");
        }
        CallNotificationInfo callNotificationInfo2 = this.callNotificationInfo;
        this.callNotificationInfo = callNotificationInfo2 == null ? null : CallNotificationInfo.copy$default(callNotificationInfo2, (String) null, 0, (PendingIntent) null, 0, false, true, 31, (Object) null);
        this.statusBarWindowController.ifPresent(OngoingCallController$onSwipeAwayGestureDetected$1.INSTANCE);
        this.swipeStatusBarAwayGestureHandler.ifPresent(OngoingCallController$onSwipeAwayGestureDetected$2.INSTANCE);
    }

    /* compiled from: OngoingCallController.kt */
    public static final class CallNotificationInfo {
        public final long callStartTime;
        @Nullable
        public final PendingIntent intent;
        public final boolean isOngoing;
        @NotNull
        public final String key;
        public final boolean statusBarSwipedAway;
        public final int uid;

        public static /* synthetic */ CallNotificationInfo copy$default(CallNotificationInfo callNotificationInfo, String str, long j, PendingIntent pendingIntent, int i, boolean z, boolean z2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = callNotificationInfo.key;
            }
            if ((i2 & 2) != 0) {
                j = callNotificationInfo.callStartTime;
            }
            long j2 = j;
            if ((i2 & 4) != 0) {
                pendingIntent = callNotificationInfo.intent;
            }
            PendingIntent pendingIntent2 = pendingIntent;
            if ((i2 & 8) != 0) {
                i = callNotificationInfo.uid;
            }
            int i3 = i;
            if ((i2 & 16) != 0) {
                z = callNotificationInfo.isOngoing;
            }
            boolean z3 = z;
            if ((i2 & 32) != 0) {
                z2 = callNotificationInfo.statusBarSwipedAway;
            }
            return callNotificationInfo.copy(str, j2, pendingIntent2, i3, z3, z2);
        }

        @NotNull
        public final CallNotificationInfo copy(@NotNull String str, long j, @Nullable PendingIntent pendingIntent, int i, boolean z, boolean z2) {
            return new CallNotificationInfo(str, j, pendingIntent, i, z, z2);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CallNotificationInfo)) {
                return false;
            }
            CallNotificationInfo callNotificationInfo = (CallNotificationInfo) obj;
            return Intrinsics.areEqual((Object) this.key, (Object) callNotificationInfo.key) && this.callStartTime == callNotificationInfo.callStartTime && Intrinsics.areEqual((Object) this.intent, (Object) callNotificationInfo.intent) && this.uid == callNotificationInfo.uid && this.isOngoing == callNotificationInfo.isOngoing && this.statusBarSwipedAway == callNotificationInfo.statusBarSwipedAway;
        }

        public int hashCode() {
            int hashCode = ((this.key.hashCode() * 31) + Long.hashCode(this.callStartTime)) * 31;
            PendingIntent pendingIntent = this.intent;
            int hashCode2 = (((hashCode + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31) + Integer.hashCode(this.uid)) * 31;
            boolean z = this.isOngoing;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i = (hashCode2 + (z ? 1 : 0)) * 31;
            boolean z3 = this.statusBarSwipedAway;
            if (!z3) {
                z2 = z3;
            }
            return i + (z2 ? 1 : 0);
        }

        @NotNull
        public String toString() {
            return "CallNotificationInfo(key=" + this.key + ", callStartTime=" + this.callStartTime + ", intent=" + this.intent + ", uid=" + this.uid + ", isOngoing=" + this.isOngoing + ", statusBarSwipedAway=" + this.statusBarSwipedAway + ')';
        }

        public CallNotificationInfo(@NotNull String str, long j, @Nullable PendingIntent pendingIntent, int i, boolean z, boolean z2) {
            this.key = str;
            this.callStartTime = j;
            this.intent = pendingIntent;
            this.uid = i;
            this.isOngoing = z;
            this.statusBarSwipedAway = z2;
        }

        @NotNull
        public final String getKey() {
            return this.key;
        }

        public final long getCallStartTime() {
            return this.callStartTime;
        }

        @Nullable
        public final PendingIntent getIntent() {
            return this.intent;
        }

        public final int getUid() {
            return this.uid;
        }

        public final boolean isOngoing() {
            return this.isOngoing;
        }

        public final boolean getStatusBarSwipedAway() {
            return this.statusBarSwipedAway;
        }

        public final boolean hasValidStartTime() {
            return this.callStartTime > 0;
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Active call notification: ", this.callNotificationInfo));
        printWriter.println(Intrinsics.stringPlus("Call app visible: ", Boolean.valueOf(this.uidObserver.isCallAppVisible())));
    }

    /* compiled from: OngoingCallController.kt */
    public final class CallAppUidObserver extends IUidObserver.Stub {
        @Nullable
        public Integer callAppUid;
        public boolean isCallAppVisible;
        public boolean isRegistered;

        public void onUidActive(int i) {
        }

        public void onUidCachedChanged(int i, boolean z) {
        }

        public void onUidGone(int i, boolean z) {
        }

        public void onUidIdle(int i, boolean z) {
        }

        public void onUidProcAdjChanged(int i) {
        }

        public CallAppUidObserver() {
        }

        public final boolean isCallAppVisible() {
            return this.isCallAppVisible;
        }

        public final void registerWithUid(int i) {
            Integer num = this.callAppUid;
            if (num == null || num.intValue() != i) {
                this.callAppUid = Integer.valueOf(i);
                try {
                    OngoingCallController ongoingCallController = OngoingCallController.this;
                    this.isCallAppVisible = ongoingCallController.isProcessVisibleToUser(ongoingCallController.iActivityManager.getUidProcessState(i, OngoingCallController.this.context.getOpPackageName()));
                    if (!this.isRegistered) {
                        OngoingCallController.this.iActivityManager.registerUidObserver(OngoingCallController.this.uidObserver, 1, -1, OngoingCallController.this.context.getOpPackageName());
                        this.isRegistered = true;
                    }
                } catch (SecurityException e) {
                    Log.e("OngoingCallController", Intrinsics.stringPlus("Security exception when trying to set up uid observer: ", e));
                }
            }
        }

        public final void unregister() {
            this.callAppUid = null;
            this.isRegistered = false;
            OngoingCallController.this.iActivityManager.unregisterUidObserver(OngoingCallController.this.uidObserver);
        }

        public void onUidStateChanged(int i, int i2, long j, int i3) {
            Integer num = this.callAppUid;
            if (num != null && i == num.intValue()) {
                boolean z = this.isCallAppVisible;
                boolean access$isProcessVisibleToUser = OngoingCallController.this.isProcessVisibleToUser(i2);
                this.isCallAppVisible = access$isProcessVisibleToUser;
                if (z != access$isProcessVisibleToUser) {
                    OngoingCallController.this.mainExecutor.execute(new OngoingCallController$CallAppUidObserver$onUidStateChanged$1(OngoingCallController.this));
                }
            }
        }
    }
}
