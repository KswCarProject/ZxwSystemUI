package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.notification.ConversationIconFactory;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;

public class NotificationGutsManager implements Dumpable, NotificationLifetimeExtender, NotifGutsViewManager {
    public final AccessibilityManager mAccessibilityManager;
    public final AppWidgetManager mAppWidgetManager;
    public final AssistantFeedbackController mAssistantFeedbackController;
    public final Handler mBgHandler;
    public final Optional<BubblesManager> mBubblesManagerOptional;
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public final ChannelEditorDialogController mChannelEditorDialogController;
    public NotificationInfo.CheckSaveListener mCheckSaveListener;
    public final Context mContext;
    public final UserContextProvider mContextTracker;
    public final DeviceProvisionedController mDeviceProvisionedController = ((DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class));
    public NotifGutsViewListener mGutsListener;
    public NotificationMenuRowPlugin.MenuItem mGutsMenuItem;
    public final HighPriorityProvider mHighPriorityProvider;
    @VisibleForTesting
    public String mKeyToRemoveOnGutsClosed;
    public final LauncherApps mLauncherApps;
    public NotificationListContainer mListContainer;
    public final NotificationLockscreenUserManager mLockscreenUserManager = ((NotificationLockscreenUserManager) Dependency.get(NotificationLockscreenUserManager.class));
    public final Handler mMainHandler;
    public final MetricsLogger mMetricsLogger = ((MetricsLogger) Dependency.get(MetricsLogger.class));
    public NotificationActivityStarter mNotificationActivityStarter;
    public NotificationGuts mNotificationGutsExposed;
    public NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
    public final INotificationManager mNotificationManager;
    public OnSettingsClickListener mOnSettingsClickListener;
    public final OnUserInteractionCallback mOnUserInteractionCallback;
    public Runnable mOpenRunnable;
    public final PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    public NotificationPresenter mPresenter;
    public final ShadeController mShadeController;
    public final ShortcutManager mShortcutManager;
    public final StatusBarStateController mStatusBarStateController = ((StatusBarStateController) Dependency.get(StatusBarStateController.class));
    public final UiEventLogger mUiEventLogger;

    public interface OnSettingsClickListener {
        void onSettingsClick(String str);
    }

    public NotificationGutsManager(Context context, Lazy<Optional<CentralSurfaces>> lazy, Handler handler, Handler handler2, AccessibilityManager accessibilityManager, HighPriorityProvider highPriorityProvider, INotificationManager iNotificationManager, NotificationEntryManager notificationEntryManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, LauncherApps launcherApps, ShortcutManager shortcutManager, ChannelEditorDialogController channelEditorDialogController, UserContextProvider userContextProvider, AssistantFeedbackController assistantFeedbackController, Optional<BubblesManager> optional, UiEventLogger uiEventLogger, OnUserInteractionCallback onUserInteractionCallback, ShadeController shadeController, DumpManager dumpManager) {
        this.mContext = context;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mAccessibilityManager = accessibilityManager;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mNotificationManager = iNotificationManager;
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
        this.mLauncherApps = launcherApps;
        this.mShortcutManager = shortcutManager;
        this.mContextTracker = userContextProvider;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mBubblesManagerOptional = optional;
        this.mUiEventLogger = uiEventLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mShadeController = shadeController;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
        dumpManager.registerDumpable(this);
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, NotificationInfo.CheckSaveListener checkSaveListener, OnSettingsClickListener onSettingsClickListener) {
        this.mPresenter = notificationPresenter;
        this.mListContainer = notificationListContainer;
        this.mCheckSaveListener = checkSaveListener;
        this.mOnSettingsClickListener = onSettingsClickListener;
    }

    public void setNotificationActivityStarter(NotificationActivityStarter notificationActivityStarter) {
        this.mNotificationActivityStarter = notificationActivityStarter;
    }

    public void onDensityOrFontScaleChanged(NotificationEntry notificationEntry) {
        setExposedGuts(notificationEntry.getGuts());
        bindGuts(notificationEntry.getRow());
    }

    public final void startAppNotificationSettingsActivity(String str, int i, NotificationChannel notificationChannel, ExpandableNotificationRow expandableNotificationRow) {
        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", str);
        intent.putExtra("app_uid", i);
        if (notificationChannel != null) {
            Bundle bundle = new Bundle();
            intent.putExtra(":settings:fragment_args_key", notificationChannel.getId());
            bundle.putString(":settings:fragment_args_key", notificationChannel.getId());
            intent.putExtra(":settings:show_fragment_args", bundle);
        }
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, i, expandableNotificationRow);
    }

    public final boolean bindGuts(ExpandableNotificationRow expandableNotificationRow) {
        expandableNotificationRow.ensureGutsInflated();
        return bindGuts(expandableNotificationRow, this.mGutsMenuItem);
    }

    @VisibleForTesting
    public boolean bindGuts(ExpandableNotificationRow expandableNotificationRow, NotificationMenuRowPlugin.MenuItem menuItem) {
        NotificationEntry entry = expandableNotificationRow.getEntry();
        expandableNotificationRow.setGutsView(menuItem);
        expandableNotificationRow.setTag(entry.getSbn().getPackageName());
        expandableNotificationRow.getGuts().setClosedListener(new NotificationGutsManager$$ExternalSyntheticLambda4(this, expandableNotificationRow, entry));
        View gutsView = menuItem.getGutsView();
        try {
            if (gutsView instanceof NotificationSnooze) {
                initializeSnoozeView(expandableNotificationRow, (NotificationSnooze) gutsView);
                return true;
            } else if (gutsView instanceof NotificationInfo) {
                initializeNotificationInfo(expandableNotificationRow, (NotificationInfo) gutsView);
                return true;
            } else if (gutsView instanceof NotificationConversationInfo) {
                initializeConversationNotificationInfo(expandableNotificationRow, (NotificationConversationInfo) gutsView);
                return true;
            } else if (gutsView instanceof PartialConversationInfo) {
                initializePartialConversationNotificationInfo(expandableNotificationRow, (PartialConversationInfo) gutsView);
                return true;
            } else if (!(gutsView instanceof FeedbackInfo)) {
                return true;
            } else {
                initializeFeedbackInfo(expandableNotificationRow, (FeedbackInfo) gutsView);
                return true;
            }
        } catch (Exception e) {
            Log.e("NotificationGutsManager", "error binding guts", e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindGuts$0(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry, NotificationGuts notificationGuts) {
        expandableNotificationRow.onGutsClosed();
        if (!notificationGuts.willBeRemoved() && !expandableNotificationRow.isRemoved()) {
            this.mListContainer.onHeightChanged(expandableNotificationRow, !this.mPresenter.isPresenterFullyCollapsed());
        }
        if (this.mNotificationGutsExposed == notificationGuts) {
            this.mNotificationGutsExposed = null;
            this.mGutsMenuItem = null;
        }
        NotifGutsViewListener notifGutsViewListener = this.mGutsListener;
        if (notifGutsViewListener != null) {
            notifGutsViewListener.onGutsClose(notificationEntry);
        }
        String key = notificationEntry.getKey();
        if (key.equals(this.mKeyToRemoveOnGutsClosed)) {
            this.mKeyToRemoveOnGutsClosed = null;
            NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
            if (notificationSafeToRemoveCallback != null) {
                notificationSafeToRemoveCallback.onSafeToRemove(key);
            }
        }
    }

    public final void initializeSnoozeView(ExpandableNotificationRow expandableNotificationRow, NotificationSnooze notificationSnooze) {
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        notificationSnooze.setSnoozeListener(this.mListContainer.getSwipeActionHelper());
        notificationSnooze.setStatusBarNotification(sbn);
        notificationSnooze.setSnoozeOptions(expandableNotificationRow.getEntry().getSnoozeCriteria());
        guts.setHeightChangedListener(new NotificationGutsManager$$ExternalSyntheticLambda7(this, expandableNotificationRow));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeSnoozeView$1(ExpandableNotificationRow expandableNotificationRow, NotificationGuts notificationGuts) {
        this.mListContainer.onHeightChanged(expandableNotificationRow, expandableNotificationRow.isShown());
    }

    public final void initializeFeedbackInfo(ExpandableNotificationRow expandableNotificationRow, FeedbackInfo feedbackInfo) {
        if (this.mAssistantFeedbackController.getFeedbackIcon(expandableNotificationRow.getEntry()) != null) {
            StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
            FeedbackInfo feedbackInfo2 = feedbackInfo;
            ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
            feedbackInfo2.bindGuts(CentralSurfaces.getPackageManagerForUser(this.mContext, sbn.getUser().getIdentifier()), sbn, expandableNotificationRow.getEntry(), expandableNotificationRow2, this.mAssistantFeedbackController);
        }
    }

    @VisibleForTesting
    public void initializeNotificationInfo(ExpandableNotificationRow expandableNotificationRow, NotificationInfo notificationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda6 notificationGutsManager$$ExternalSyntheticLambda6;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = CentralSurfaces.getPackageManagerForUser(this.mContext, user.getIdentifier());
        NotificationGutsManager$$ExternalSyntheticLambda5 notificationGutsManager$$ExternalSyntheticLambda5 = new NotificationGutsManager$$ExternalSyntheticLambda5(this, guts, sbn, expandableNotificationRow);
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda6 = new NotificationGutsManager$$ExternalSyntheticLambda6(this, guts, sbn, packageName, expandableNotificationRow);
        } else {
            notificationGutsManager$$ExternalSyntheticLambda6 = null;
        }
        notificationInfo.bindNotification(packageManagerForUser, this.mNotificationManager, this.mOnUserInteractionCallback, this.mChannelEditorDialogController, packageName, expandableNotificationRow.getEntry().getChannel(), expandableNotificationRow.getUniqueChannels(), expandableNotificationRow.getEntry(), notificationGutsManager$$ExternalSyntheticLambda6, notificationGutsManager$$ExternalSyntheticLambda5, this.mUiEventLogger, this.mDeviceProvisionedController.isDeviceProvisioned(), expandableNotificationRow.getIsNonblockable(), this.mHighPriorityProvider.isHighPriority(expandableNotificationRow.getEntry()), this.mAssistantFeedbackController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeNotificationInfo$2(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, ExpandableNotificationRow expandableNotificationRow, View view, Intent intent) {
        this.mMetricsLogger.action(206);
        notificationGuts.resetFalsingCheck();
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, statusBarNotification.getUid(), expandableNotificationRow);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeNotificationInfo$3(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    @VisibleForTesting
    public void initializePartialConversationNotificationInfo(ExpandableNotificationRow expandableNotificationRow, PartialConversationInfo partialConversationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda3 notificationGutsManager$$ExternalSyntheticLambda3;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = CentralSurfaces.getPackageManagerForUser(this.mContext, user.getIdentifier());
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda3 = new NotificationGutsManager$$ExternalSyntheticLambda3(this, guts, sbn, packageName, expandableNotificationRow);
        } else {
            notificationGutsManager$$ExternalSyntheticLambda3 = null;
        }
        partialConversationInfo.bindNotification(packageManagerForUser, this.mNotificationManager, this.mChannelEditorDialogController, packageName, expandableNotificationRow.getEntry().getChannel(), expandableNotificationRow.getUniqueChannels(), expandableNotificationRow.getEntry(), notificationGutsManager$$ExternalSyntheticLambda3, this.mDeviceProvisionedController.isDeviceProvisioned(), expandableNotificationRow.getIsNonblockable());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializePartialConversationNotificationInfo$4(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    @VisibleForTesting
    public void initializeConversationNotificationInfo(ExpandableNotificationRow expandableNotificationRow, NotificationConversationInfo notificationConversationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda2 notificationGutsManager$$ExternalSyntheticLambda2;
        ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        NotificationEntry entry = expandableNotificationRow.getEntry();
        StatusBarNotification sbn = entry.getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = CentralSurfaces.getPackageManagerForUser(this.mContext, user.getIdentifier());
        new NotificationGutsManager$$ExternalSyntheticLambda0(this, guts, sbn, expandableNotificationRow2);
        NotificationGutsManager$$ExternalSyntheticLambda1 notificationGutsManager$$ExternalSyntheticLambda1 = new NotificationGutsManager$$ExternalSyntheticLambda1(this, sbn, expandableNotificationRow2);
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda2 = new NotificationGutsManager$$ExternalSyntheticLambda2(this, guts, sbn, packageName, expandableNotificationRow);
        } else {
            notificationGutsManager$$ExternalSyntheticLambda2 = null;
        }
        ConversationIconFactory conversationIconFactory = r7;
        Context context = this.mContext;
        PackageManager packageManager = packageManagerForUser;
        ConversationIconFactory conversationIconFactory2 = new ConversationIconFactory(context, this.mLauncherApps, packageManager, IconDrawableFactory.newInstance(context, false), this.mContext.getResources().getDimensionPixelSize(R$dimen.notification_guts_conversation_icon_size));
        notificationConversationInfo.bindNotification(notificationConversationInfo.getSelectedAction(), this.mShortcutManager, packageManager, this.mPeopleSpaceWidgetManager, this.mNotificationManager, this.mOnUserInteractionCallback, packageName, entry.getChannel(), entry, entry.getBubbleMetadata(), notificationGutsManager$$ExternalSyntheticLambda2, conversationIconFactory, this.mContextTracker.getUserContext(), this.mDeviceProvisionedController.isDeviceProvisioned(), this.mMainHandler, this.mBgHandler, notificationGutsManager$$ExternalSyntheticLambda1, this.mBubblesManagerOptional, this.mShadeController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeConversationNotificationInfo$7(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    public void closeAndSaveGuts(boolean z, boolean z2, boolean z3, int i, int i2, boolean z4) {
        NotificationGuts notificationGuts = this.mNotificationGutsExposed;
        if (notificationGuts != null) {
            notificationGuts.removeCallbacks(this.mOpenRunnable);
            this.mNotificationGutsExposed.closeControls(z, z3, i, i2, z2);
        }
        if (z4) {
            this.mListContainer.resetExposedMenuView(false, true);
        }
    }

    public NotificationGuts getExposedGuts() {
        return this.mNotificationGutsExposed;
    }

    public void setExposedGuts(NotificationGuts notificationGuts) {
        this.mNotificationGutsExposed = notificationGuts;
    }

    public boolean openGuts(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        if ((menuItem.getGutsView() instanceof NotificationGuts.GutsContent) && ((NotificationGuts.GutsContent) menuItem.getGutsView()).needsFalsingProtection()) {
            StatusBarStateController statusBarStateController = this.mStatusBarStateController;
            if (statusBarStateController instanceof StatusBarStateControllerImpl) {
                ((StatusBarStateControllerImpl) statusBarStateController).setLeaveOpenOnKeyguardHide(true);
            }
            Optional optional = this.mCentralSurfacesOptionalLazy.get();
            if (optional.isPresent()) {
                ((CentralSurfaces) optional.get()).executeRunnableDismissingKeyguard(new NotificationGutsManager$$ExternalSyntheticLambda8(this, view, i, i2, menuItem), (Runnable) null, false, true, true);
                return true;
            }
        }
        return lambda$openGuts$8(view, i, i2, menuItem);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$openGuts$9(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        this.mMainHandler.post(new NotificationGutsManager$$ExternalSyntheticLambda9(this, view, i, i2, menuItem));
    }

    @VisibleForTesting
    /* renamed from: openGutsInternal */
    public boolean lambda$openGuts$8(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        if (view.getWindowToken() == null) {
            Log.e("NotificationGutsManager", "Trying to show notification guts, but not attached to window");
            return false;
        }
        final ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        view.performHapticFeedback(0);
        if (expandableNotificationRow.areGutsExposed()) {
            closeAndSaveGuts(false, false, true, -1, -1, true);
            return false;
        }
        expandableNotificationRow.ensureGutsInflated();
        NotificationGuts guts = expandableNotificationRow.getGuts();
        this.mNotificationGutsExposed = guts;
        if (!bindGuts(expandableNotificationRow, menuItem) || guts == null) {
            return false;
        }
        guts.setVisibility(4);
        final NotificationGuts notificationGuts = guts;
        final int i3 = i;
        final int i4 = i2;
        final NotificationMenuRowPlugin.MenuItem menuItem2 = menuItem;
        AnonymousClass1 r0 = new Runnable() {
            public void run() {
                if (expandableNotificationRow.getWindowToken() == null) {
                    Log.e("NotificationGutsManager", "Trying to show notification guts in post(), but not attached to window");
                    return;
                }
                notificationGuts.setVisibility(0);
                boolean z = NotificationGutsManager.this.mStatusBarStateController.getState() == 1 && !NotificationGutsManager.this.mAccessibilityManager.isTouchExplorationEnabled();
                NotificationGuts notificationGuts = notificationGuts;
                boolean z2 = !expandableNotificationRow.isBlockingHelperShowing();
                int i = i3;
                int i2 = i4;
                ExpandableNotificationRow expandableNotificationRow = expandableNotificationRow;
                Objects.requireNonNull(expandableNotificationRow);
                notificationGuts.openControls(z2, i, i2, z, new NotificationGutsManager$1$$ExternalSyntheticLambda0(expandableNotificationRow));
                if (NotificationGutsManager.this.mGutsListener != null) {
                    NotificationGutsManager.this.mGutsListener.onGutsOpen(expandableNotificationRow.getEntry(), notificationGuts);
                }
                expandableNotificationRow.closeRemoteInput();
                NotificationGutsManager.this.mListContainer.onHeightChanged(expandableNotificationRow, true);
                NotificationGutsManager.this.mGutsMenuItem = menuItem2;
            }
        };
        this.mOpenRunnable = r0;
        guts.post(r0);
        return true;
    }

    public void setCallback(NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback) {
        this.mNotificationLifetimeFinishedCallback = notificationSafeToRemoveCallback;
    }

    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return (notificationEntry == null || this.mNotificationGutsExposed == null || notificationEntry.getGuts() == null || this.mNotificationGutsExposed != notificationEntry.getGuts() || this.mNotificationGutsExposed.isLeavebehind()) ? false : true;
    }

    public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mKeyToRemoveOnGutsClosed = notificationEntry.getKey();
            if (Log.isLoggable("NotificationGutsManager", 3)) {
                Log.d("NotificationGutsManager", "Keeping notification because it's showing guts. " + notificationEntry.getKey());
                return;
            }
            return;
        }
        String str = this.mKeyToRemoveOnGutsClosed;
        if (str != null && str.equals(notificationEntry.getKey())) {
            this.mKeyToRemoveOnGutsClosed = null;
            if (Log.isLoggable("NotificationGutsManager", 3)) {
                Log.d("NotificationGutsManager", "Notification that was kept for guts was updated. " + notificationEntry.getKey());
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationGutsManager state:");
        printWriter.print("  mKeyToRemoveOnGutsClosed (legacy): ");
        printWriter.println(this.mKeyToRemoveOnGutsClosed);
    }

    public void setGutsListener(NotifGutsViewListener notifGutsViewListener) {
        this.mGutsListener = notifGutsViewListener;
    }
}
