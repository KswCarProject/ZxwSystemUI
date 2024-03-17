package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.EventLog;
import android.view.RemoteAnimationAdapter;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;

public class StatusBarNotificationActivityStarter implements NotificationActivityStarter {
    public final ActivityIntentHelper mActivityIntentHelper;
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    public final ActivityStarter mActivityStarter;
    public final Lazy<AssistManager> mAssistManagerLazy;
    public final Optional<BubblesManager> mBubblesManagerOptional;
    public final CentralSurfaces mCentralSurfaces;
    public final NotificationClickNotifier mClickNotifier;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public final IDreamManager mDreamManager;
    public final NotificationEntryManager mEntryManager;
    public final GroupMembershipManager mGroupMembershipManager;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public boolean mIsCollapsingToShowActivityOverLockscreen;
    public final KeyguardManager mKeyguardManager;
    public final KeyguardStateController mKeyguardStateController;
    public final LockPatternUtils mLockPatternUtils;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final StatusBarNotificationActivityStarterLogger mLogger;
    public final Handler mMainThreadHandler;
    public final MetricsLogger mMetricsLogger;
    public final NotifPipeline mNotifPipeline;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final NotificationLaunchAnimatorControllerProvider mNotificationAnimationProvider;
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    public final NotificationPanelViewController mNotificationPanel;
    public final OnUserInteractionCallback mOnUserInteractionCallback;
    public final NotificationPresenter mPresenter;
    public final NotificationRemoteInputManager mRemoteInputManager;
    public final ShadeController mShadeController;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final StatusBarRemoteInputCallback mStatusBarRemoteInputCallback;
    public final StatusBarStateController mStatusBarStateController;
    public final Executor mUiBgExecutor;
    public final NotificationVisibilityProvider mVisibilityProvider;

    public StatusBarNotificationActivityStarter(Context context, CommandQueue commandQueue, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional<BubblesManager> optional, Lazy<AssistManager> lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, NotifPipelineFlags notifPipelineFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback, CentralSurfaces centralSurfaces, NotificationPresenter notificationPresenter, NotificationPanelViewController notificationPanelViewController, ActivityLaunchAnimator activityLaunchAnimator, NotificationLaunchAnimatorControllerProvider notificationLaunchAnimatorControllerProvider) {
        this.mContext = context;
        this.mCommandQueue = commandQueue;
        this.mMainThreadHandler = handler;
        this.mUiBgExecutor = executor;
        this.mEntryManager = notificationEntryManager;
        this.mNotifPipeline = notifPipeline;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mActivityStarter = activityStarter;
        this.mClickNotifier = notificationClickNotifier;
        this.mStatusBarStateController = statusBarStateController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardManager = keyguardManager;
        this.mDreamManager = iDreamManager;
        this.mBubblesManagerOptional = optional;
        this.mAssistManagerLazy = lazy;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mShadeController = shadeController;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mLockPatternUtils = lockPatternUtils;
        this.mStatusBarRemoteInputCallback = statusBarRemoteInputCallback;
        this.mActivityIntentHelper = activityIntentHelper;
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mMetricsLogger = metricsLogger;
        this.mLogger = statusBarNotificationActivityStarterLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mCentralSurfaces = centralSurfaces;
        this.mPresenter = notificationPresenter;
        this.mNotificationPanel = notificationPanelViewController;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        this.mNotificationAnimationProvider = notificationLaunchAnimatorControllerProvider;
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
                public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        } else {
            notifPipeline.addCollectionListener(new NotifCollectionListener() {
                public void onEntryAdded(NotificationEntry notificationEntry) {
                    StatusBarNotificationActivityStarter.this.handleFullScreenIntent(notificationEntry);
                }
            });
        }
    }

    public void onNotificationClicked(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        this.mLogger.logStartingActivityFromClick(notificationEntry);
        if (!this.mRemoteInputManager.isRemoteInputActive(notificationEntry) || TextUtils.isEmpty(expandableNotificationRow.getActiveRemoteInputText())) {
            Notification notification = notificationEntry.getSbn().getNotification();
            PendingIntent pendingIntent = notification.contentIntent;
            if (pendingIntent == null) {
                pendingIntent = notification.fullScreenIntent;
            }
            final PendingIntent pendingIntent2 = pendingIntent;
            boolean isBubble = notificationEntry.isBubble();
            if (pendingIntent2 != null || isBubble) {
                boolean z = false;
                final boolean z2 = pendingIntent2 != null && pendingIntent2.isActivity() && !isBubble;
                boolean z3 = z2 && this.mActivityIntentHelper.wouldLaunchResolverActivity(pendingIntent2.getIntent(), this.mLockscreenUserManager.getCurrentUserId());
                final boolean z4 = !z3 && this.mCentralSurfaces.shouldAnimateLaunch(z2);
                if (this.mKeyguardStateController.isShowing() && pendingIntent2 != null && this.mActivityIntentHelper.wouldShowOverLockscreen(pendingIntent2.getIntent(), this.mLockscreenUserManager.getCurrentUserId())) {
                    z = true;
                }
                final NotificationEntry notificationEntry2 = notificationEntry;
                final ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
                final boolean z5 = z;
                AnonymousClass3 r2 = new ActivityStarter.OnDismissAction() {
                    public boolean onDismiss() {
                        return StatusBarNotificationActivityStarter.this.handleNotificationClickAfterKeyguardDismissed(notificationEntry2, expandableNotificationRow2, pendingIntent2, z2, z4, z5);
                    }

                    public boolean willRunAnimationOnKeyguard() {
                        return z4;
                    }
                };
                if (z) {
                    this.mIsCollapsingToShowActivityOverLockscreen = true;
                    r2.onDismiss();
                    return;
                }
                this.mActivityStarter.dismissKeyguardThenExecute(r2, (Runnable) null, z3);
                return;
            }
            this.mLogger.logNonClickableNotification(notificationEntry);
            return;
        }
        this.mRemoteInputManager.closeRemoteInputs();
    }

    public final boolean handleNotificationClickAfterKeyguardDismissed(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, boolean z, boolean z2, boolean z3) {
        this.mLogger.logHandleClickAfterKeyguardDismissed(notificationEntry);
        StatusBarNotificationActivityStarter$$ExternalSyntheticLambda3 statusBarNotificationActivityStarter$$ExternalSyntheticLambda3 = new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda3(this, notificationEntry, expandableNotificationRow, pendingIntent, z, z2);
        if (z3) {
            this.mShadeController.addPostCollapseAction(statusBarNotificationActivityStarter$$ExternalSyntheticLambda3);
            this.mShadeController.collapsePanel(true);
        } else if (!this.mKeyguardStateController.isShowing() || !this.mCentralSurfaces.isOccluded()) {
            statusBarNotificationActivityStarter$$ExternalSyntheticLambda3.run();
        } else {
            this.mStatusBarKeyguardViewManager.addAfterKeyguardGoneRunnable(statusBarNotificationActivityStarter$$ExternalSyntheticLambda3);
            this.mShadeController.collapsePanel();
        }
        if (z2 || !this.mNotificationPanel.isFullyCollapsed()) {
            return true;
        }
        return false;
    }

    /* renamed from: handleNotificationClickAfterPanelCollapsed */
    public final void lambda$handleNotificationClickAfterKeyguardDismissed$0(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, boolean z, boolean z2) {
        String key = notificationEntry.getKey();
        this.mLogger.logHandleClickAfterPanelCollapsed(notificationEntry);
        try {
            ActivityManager.getService().resumeAppSwitches();
        } catch (RemoteException unused) {
        }
        if (z) {
            int identifier = pendingIntent.getCreatorUserHandle().getIdentifier();
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(identifier) && this.mKeyguardManager.isDeviceLocked(identifier) && this.mStatusBarRemoteInputCallback.startWorkChallengeIfNecessary(identifier, pendingIntent.getIntentSender(), key)) {
                removeHunAfterClick(expandableNotificationRow);
                collapseOnMainThread();
                return;
            }
        }
        Intent intent = null;
        CharSequence charSequence = !TextUtils.isEmpty(notificationEntry.remoteInputText) ? notificationEntry.remoteInputText : null;
        if (!TextUtils.isEmpty(charSequence) && !this.mRemoteInputManager.isSpinning(key)) {
            intent = new Intent().putExtra("android.remoteInputDraft", charSequence.toString());
        }
        Intent intent2 = intent;
        boolean canBubble = notificationEntry.canBubble();
        if (canBubble) {
            this.mLogger.logExpandingBubble(notificationEntry);
            removeHunAfterClick(expandableNotificationRow);
            expandBubbleStackOnMainThread(notificationEntry);
        } else {
            startNotificationIntent(pendingIntent, intent2, notificationEntry, expandableNotificationRow, z2, z);
        }
        if (z || canBubble) {
            this.mAssistManagerLazy.get().hideAssist();
        }
        NotificationVisibility obtain = this.mVisibilityProvider.obtain(notificationEntry, true);
        if (!canBubble && (shouldAutoCancel(notificationEntry.getSbn()) || this.mRemoteInputManager.isNotificationKeptForRemoteInputHistory(key))) {
            this.mMainThreadHandler.post(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda5(this, this.mOnUserInteractionCallback.registerFutureDismissal(notificationEntry, 1)));
        }
        this.mClickNotifier.onNotificationClick(key, obtain);
        this.mIsCollapsingToShowActivityOverLockscreen = false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleNotificationClickAfterPanelCollapsed$1(Runnable runnable) {
        if (this.mPresenter.isCollapsing()) {
            this.mShadeController.addPostCollapseAction(runnable);
        } else {
            runnable.run();
        }
    }

    public void onDragSuccess(NotificationEntry notificationEntry) {
        NotificationVisibility obtain = this.mVisibilityProvider.obtain(notificationEntry, true);
        String key = notificationEntry.getKey();
        if (shouldAutoCancel(notificationEntry.getSbn()) || this.mRemoteInputManager.isNotificationKeptForRemoteInputHistory(key)) {
            this.mMainThreadHandler.post(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda4(this, this.mOnUserInteractionCallback.registerFutureDismissal(notificationEntry, 1)));
        }
        this.mClickNotifier.onNotificationClick(key, obtain);
        this.mIsCollapsingToShowActivityOverLockscreen = false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDragSuccess$2(Runnable runnable) {
        if (this.mPresenter.isCollapsing()) {
            this.mShadeController.addPostCollapseAction(runnable);
        } else {
            runnable.run();
        }
    }

    public final void expandBubbleStackOnMainThread(NotificationEntry notificationEntry) {
        if (this.mBubblesManagerOptional.isPresent()) {
            if (Looper.getMainLooper().isCurrentThread()) {
                lambda$expandBubbleStackOnMainThread$3(notificationEntry);
            } else {
                this.mMainThreadHandler.post(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1(this, notificationEntry));
            }
        }
    }

    /* renamed from: expandBubbleStack */
    public final void lambda$expandBubbleStackOnMainThread$3(NotificationEntry notificationEntry) {
        this.mBubblesManagerOptional.get().expandStackAndSelectBubble(notificationEntry);
        this.mShadeController.collapsePanel();
    }

    public final void startNotificationIntent(PendingIntent pendingIntent, Intent intent, NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, boolean z, boolean z2) {
        this.mLogger.logStartNotificationIntent(notificationEntry);
        try {
            this.mActivityLaunchAnimator.startPendingIntentWithAnimation(new StatusBarLaunchAnimatorController(this.mNotificationAnimationProvider.getAnimatorController(expandableNotificationRow, (Runnable) null), this.mCentralSurfaces, z2), z, pendingIntent.getCreatorPackage(), new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda0(this, expandableNotificationRow, pendingIntent, intent, notificationEntry));
        } catch (PendingIntent.CanceledException e) {
            this.mLogger.logSendingIntentFailed(e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$startNotificationIntent$4(ExpandableNotificationRow expandableNotificationRow, PendingIntent pendingIntent, Intent intent, NotificationEntry notificationEntry, RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException {
        Bundle bundle;
        long andResetLastActionUpTime = expandableNotificationRow.getAndResetLastActionUpTime();
        if (andResetLastActionUpTime > 0) {
            bundle = CentralSurfaces.getActivityOptions(this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter, this.mKeyguardStateController.isShowing(), andResetLastActionUpTime);
        } else {
            bundle = CentralSurfaces.getActivityOptions(this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter);
        }
        PendingIntent pendingIntent2 = pendingIntent;
        int sendAndReturnResult = pendingIntent2.sendAndReturnResult(this.mContext, 0, intent, (PendingIntent.OnFinished) null, (Handler) null, (String) null, bundle);
        this.mLogger.logSendPendingIntent(notificationEntry, pendingIntent, sendAndReturnResult);
        return sendAndReturnResult;
    }

    public void startNotificationGutsIntent(Intent intent, int i, ExpandableNotificationRow expandableNotificationRow) {
        final boolean shouldAnimateLaunch = this.mCentralSurfaces.shouldAnimateLaunch(true);
        final ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
        final Intent intent2 = intent;
        final int i2 = i;
        this.mActivityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() {
            public boolean onDismiss() {
                AsyncTask.execute(new StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda0(this, expandableNotificationRow2, shouldAnimateLaunch, intent2, i2));
                return true;
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onDismiss$1(ExpandableNotificationRow expandableNotificationRow, boolean z, Intent intent, int i) {
                StatusBarNotificationActivityStarter.this.mActivityLaunchAnimator.startIntentWithAnimation(new StatusBarLaunchAnimatorController(StatusBarNotificationActivityStarter.this.mNotificationAnimationProvider.getAnimatorController(expandableNotificationRow), StatusBarNotificationActivityStarter.this.mCentralSurfaces, true), z, intent.getPackage(), new StatusBarNotificationActivityStarter$4$$ExternalSyntheticLambda1(this, intent, i));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ Integer lambda$onDismiss$0(Intent intent, int i, RemoteAnimationAdapter remoteAnimationAdapter) {
                return Integer.valueOf(TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntentWithParentStack(intent).startActivities(CentralSurfaces.getActivityOptions(StatusBarNotificationActivityStarter.this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter), new UserHandle(UserHandle.getUserId(i))));
            }

            public boolean willRunAnimationOnKeyguard() {
                return shouldAnimateLaunch;
            }
        }, (Runnable) null, false);
    }

    public void startHistoryIntent(final View view, final boolean z) {
        final boolean shouldAnimateLaunch = this.mCentralSurfaces.shouldAnimateLaunch(true);
        this.mActivityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() {
            public boolean onDismiss() {
                AsyncTask.execute(new StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda0(this, z, view, shouldAnimateLaunch));
                return true;
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onDismiss$1(boolean z, View view, boolean z2) {
                Intent intent;
                StatusBarLaunchAnimatorController statusBarLaunchAnimatorController;
                if (z) {
                    intent = new Intent("android.settings.NOTIFICATION_HISTORY");
                } else {
                    intent = new Intent("android.settings.NOTIFICATION_SETTINGS");
                }
                TaskStackBuilder addNextIntent = TaskStackBuilder.create(StatusBarNotificationActivityStarter.this.mContext).addNextIntent(new Intent("android.settings.NOTIFICATION_SETTINGS"));
                if (z) {
                    addNextIntent.addNextIntent(intent);
                }
                ActivityLaunchAnimator.Controller fromView = ActivityLaunchAnimator.Controller.fromView(view, 30);
                if (fromView == null) {
                    statusBarLaunchAnimatorController = null;
                } else {
                    statusBarLaunchAnimatorController = new StatusBarLaunchAnimatorController(fromView, StatusBarNotificationActivityStarter.this.mCentralSurfaces, true);
                }
                StatusBarNotificationActivityStarter.this.mActivityLaunchAnimator.startIntentWithAnimation(statusBarLaunchAnimatorController, z2, intent.getPackage(), new StatusBarNotificationActivityStarter$5$$ExternalSyntheticLambda1(this, addNextIntent));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ Integer lambda$onDismiss$0(TaskStackBuilder taskStackBuilder, RemoteAnimationAdapter remoteAnimationAdapter) {
                return Integer.valueOf(taskStackBuilder.startActivities(CentralSurfaces.getActivityOptions(StatusBarNotificationActivityStarter.this.mCentralSurfaces.getDisplayId(), remoteAnimationAdapter), UserHandle.CURRENT));
            }

            public boolean willRunAnimationOnKeyguard() {
                return shouldAnimateLaunch;
            }
        }, (Runnable) null, false);
    }

    public final void removeHunAfterClick(ExpandableNotificationRow expandableNotificationRow) {
        String key = expandableNotificationRow.getEntry().getSbn().getKey();
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        if (headsUpManagerPhone != null && headsUpManagerPhone.isAlerting(key)) {
            if (this.mPresenter.isPresenterFullyCollapsed()) {
                HeadsUpUtil.setNeedsHeadsUpDisappearAnimationAfterClick(expandableNotificationRow, true);
            }
            this.mHeadsUpManager.removeNotification(key, true);
        }
    }

    public void handleFullScreenIntent(NotificationEntry notificationEntry) {
        if (!this.mNotificationInterruptStateProvider.shouldLaunchFullScreenIntentWhenAdded(notificationEntry)) {
            return;
        }
        if (shouldSuppressFullScreenIntent(notificationEntry)) {
            this.mLogger.logFullScreenIntentSuppressedByDnD(notificationEntry);
        } else if (notificationEntry.getImportance() < 4) {
            this.mLogger.logFullScreenIntentNotImportantEnough(notificationEntry);
        } else {
            this.mUiBgExecutor.execute(new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda2(this));
            PendingIntent pendingIntent = notificationEntry.getSbn().getNotification().fullScreenIntent;
            this.mLogger.logSendingFullScreenIntent(notificationEntry, pendingIntent);
            try {
                EventLog.writeEvent(36002, notificationEntry.getKey());
                this.mCentralSurfaces.wakeUpForFullScreenIntent();
                pendingIntent.send();
                notificationEntry.notifyFullScreenIntentLaunched();
                this.mMetricsLogger.count("note_fullscreen", 1);
            } catch (PendingIntent.CanceledException unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleFullScreenIntent$5() {
        try {
            this.mDreamManager.awaken();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isCollapsingToShowActivityOverLockscreen() {
        return this.mIsCollapsingToShowActivityOverLockscreen;
    }

    public static boolean shouldAutoCancel(StatusBarNotification statusBarNotification) {
        int i = statusBarNotification.getNotification().flags;
        return (i & 16) == 16 && (i & 64) == 0;
    }

    public final void collapseOnMainThread() {
        if (Looper.getMainLooper().isCurrentThread()) {
            this.mShadeController.collapsePanel();
            return;
        }
        Handler handler = this.mMainThreadHandler;
        ShadeController shadeController = this.mShadeController;
        Objects.requireNonNull(shadeController);
        handler.post(new CentralSurfacesImpl$$ExternalSyntheticLambda8(shadeController));
    }

    public final boolean shouldSuppressFullScreenIntent(NotificationEntry notificationEntry) {
        if (this.mPresenter.isDeviceInVrMode()) {
            return true;
        }
        return notificationEntry.shouldSuppressFullScreenIntent();
    }
}
