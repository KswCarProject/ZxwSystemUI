package com.android.systemui.statusbar.notification.stack;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Trace;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.ExpandHelper;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$bool;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.LaunchAnimationParameters;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.collection.render.NotifStats;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.NotificationSnooze;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.HeadsUpTouchHelper;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.tuner.TunerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import kotlin.Unit;

public class NotificationStackScrollLayoutController {
    public static final boolean DEBUG = Log.isLoggable("StackScrollerController", 3);
    public final boolean mAllowLongPress;
    public int mBarState;
    public final CentralSurfaces mCentralSurfaces;
    public final ConfigurationController mConfigurationController;
    @VisibleForTesting
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onDensityOrFontScaleChanged() {
            NotificationStackScrollLayoutController.this.updateShowEmptyShadeView();
            NotificationStackScrollLayoutController.this.mView.reinflateViews();
        }

        public void onUiModeChanged() {
            NotificationStackScrollLayoutController.this.mView.updateBgColor();
            NotificationStackScrollLayoutController.this.mView.updateDecorViews();
        }

        public void onThemeChanged() {
            NotificationStackScrollLayoutController.this.mView.updateCornerRadius();
            NotificationStackScrollLayoutController.this.mView.updateBgColor();
            NotificationStackScrollLayoutController.this.mView.updateDecorViews();
            NotificationStackScrollLayoutController.this.mView.reinflateViews();
            NotificationStackScrollLayoutController.this.updateShowEmptyShadeView();
            NotificationStackScrollLayoutController.this.updateFooter();
        }

        public void onConfigChanged(Configuration configuration) {
            NotificationStackScrollLayoutController.this.updateResources();
        }
    };
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() {
        public void onDeviceProvisionedChanged() {
            updateCurrentUserIsSetup();
        }

        public void onUserSwitched() {
            updateCurrentUserIsSetup();
        }

        public void onUserSetupChanged() {
            updateCurrentUserIsSetup();
        }

        public final void updateCurrentUserIsSetup() {
            NotificationStackScrollLayoutController.this.mView.setCurrentUserSetup(NotificationStackScrollLayoutController.this.mDeviceProvisionedController.isCurrentUserSetup());
        }
    };
    public final DynamicPrivacyController mDynamicPrivacyController;
    public final DynamicPrivacyController.Listener mDynamicPrivacyControllerListener = new NotificationStackScrollLayoutController$$ExternalSyntheticLambda15(this);
    public boolean mFadeNotificationsOnDismiss;
    public final FalsingCollector mFalsingCollector;
    public final FalsingManager mFalsingManager;
    public final GroupExpansionManager mGroupExpansionManager;
    public HeadsUpAppearanceController mHeadsUpAppearanceController;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public Boolean mHistoryEnabled;
    public final IStatusBarService mIStatusBarService;
    public final InteractionJankMonitor mJankMonitor;
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardMediaController mKeyguardMediaController;
    public final LayoutInflater mLayoutInflater;
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    public final NotificationLockscreenUserManager.UserChangedListener mLockscreenUserChangeListener = new NotificationLockscreenUserManager.UserChangedListener() {
        public void onUserChanged(int i) {
            NotificationStackScrollLayoutController.this.mView.updateSensitiveness(false, NotificationStackScrollLayoutController.this.mLockscreenUserManager.isAnyProfilePublicMode());
            NotificationStackScrollLayoutController.this.mHistoryEnabled = null;
            NotificationStackScrollLayoutController.this.updateFooter();
        }
    };
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final NotificationStackScrollLogger mLogger;
    public View mLongPressedView;
    public final NotificationMenuRowPlugin.OnMenuEventListener mMenuEventListener = new NotificationMenuRowPlugin.OnMenuEventListener() {
        public void onMenuClicked(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
            if (NotificationStackScrollLayoutController.this.mAllowLongPress) {
                if (view instanceof ExpandableNotificationRow) {
                    NotificationStackScrollLayoutController.this.mMetricsLogger.write(((ExpandableNotificationRow) view).getEntry().getSbn().getLogMaker().setCategory(333).setType(4));
                }
                NotificationStackScrollLayoutController.this.mNotificationGutsManager.openGuts(view, i, i2, menuItem);
            }
        }

        public void onMenuReset(View view) {
            View translatingParentView = NotificationStackScrollLayoutController.this.mSwipeHelper.getTranslatingParentView();
            if (translatingParentView != null && view == translatingParentView) {
                NotificationStackScrollLayoutController.this.mSwipeHelper.clearExposedMenuView();
                NotificationStackScrollLayoutController.this.mSwipeHelper.clearTranslatingParentView();
                if (view instanceof ExpandableNotificationRow) {
                    NotificationStackScrollLayoutController.this.mHeadsUpManager.setMenuShown(((ExpandableNotificationRow) view).getEntry(), false);
                }
            }
        }

        public void onMenuShown(View view) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                NotificationStackScrollLayoutController.this.mMetricsLogger.write(expandableNotificationRow.getEntry().getSbn().getLogMaker().setCategory(332).setType(4));
                NotificationStackScrollLayoutController.this.mHeadsUpManager.setMenuShown(expandableNotificationRow.getEntry(), true);
                NotificationStackScrollLayoutController.this.mSwipeHelper.onMenuShown(view);
                NotificationStackScrollLayoutController.this.mNotificationGutsManager.closeAndSaveGuts(true, false, false, -1, -1, false);
                NotificationMenuRowPlugin provider = expandableNotificationRow.getProvider();
                if (provider.shouldShowGutsOnSnapOpen()) {
                    NotificationMenuRowPlugin.MenuItem menuItemToExposeOnSnap = provider.menuItemToExposeOnSnap();
                    if (menuItemToExposeOnSnap != null) {
                        Point revealAnimationOrigin = provider.getRevealAnimationOrigin();
                        NotificationStackScrollLayoutController.this.mNotificationGutsManager.openGuts(view, revealAnimationOrigin.x, revealAnimationOrigin.y, menuItemToExposeOnSnap);
                    } else {
                        Log.e("StackScrollerController", "Provider has shouldShowGutsOnSnapOpen, but provided no menu item in menuItemtoExposeOnSnap. Skipping.");
                    }
                    NotificationStackScrollLayoutController.this.mSwipeHelper.resetExposedMenuView(false, true);
                }
            }
        }
    };
    public final MetricsLogger mMetricsLogger;
    public final NotifCollection mNotifCollection;
    public final NotifPipeline mNotifPipeline;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final NotifStackController mNotifStackController = new NotifStackControllerImpl();
    public NotifStats mNotifStats = NotifStats.getEmpty();
    public NotificationActivityStarter mNotificationActivityStarter;
    public final NotificationSwipeHelper.NotificationCallback mNotificationCallback = new NotificationSwipeHelper.NotificationCallback() {
        public void onDismiss() {
            NotificationStackScrollLayoutController.this.mNotificationGutsManager.closeAndSaveGuts(true, false, false, -1, -1, false);
        }

        public float getTotalTranslationLength(View view) {
            return NotificationStackScrollLayoutController.this.mView.getTotalTranslationLength(view);
        }

        public void onSnooze(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
            NotificationStackScrollLayoutController.this.mCentralSurfaces.setNotificationSnoozed(statusBarNotification, snoozeOption);
        }

        public boolean shouldDismissQuickly() {
            return NotificationStackScrollLayoutController.this.mView.isExpanded() && NotificationStackScrollLayoutController.this.mView.isFullyAwake();
        }

        public void onDragCancelled(View view) {
            NotificationStackScrollLayoutController.this.mFalsingCollector.onNotificationStopDismissing();
        }

        public void onChildDismissed(View view) {
            if (view instanceof ActivatableNotificationView) {
                ActivatableNotificationView activatableNotificationView = (ActivatableNotificationView) view;
                if (!activatableNotificationView.isDismissed()) {
                    handleChildViewDismissed(view);
                }
                activatableNotificationView.removeFromTransientContainer();
            }
        }

        public void handleChildViewDismissed(View view) {
            if (!NotificationStackScrollLayoutController.this.mView.getClearAllInProgress()) {
                NotificationStackScrollLayoutController.this.mView.onSwipeEnd();
                if (view instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                    if (expandableNotificationRow.isHeadsUp()) {
                        NotificationStackScrollLayoutController.this.mHeadsUpManager.addSwipedOutNotification(expandableNotificationRow.getEntry().getSbn().getKey());
                    }
                    expandableNotificationRow.performDismiss(false);
                }
                NotificationStackScrollLayoutController.this.mView.addSwipedOutView(view);
                NotificationStackScrollLayoutController.this.mFalsingCollector.onNotificationDismissed();
                if (NotificationStackScrollLayoutController.this.mFalsingCollector.shouldEnforceBouncer()) {
                    NotificationStackScrollLayoutController.this.mCentralSurfaces.executeRunnableDismissingKeyguard((Runnable) null, (Runnable) null, false, true, false);
                }
            }
        }

        public boolean isAntiFalsingNeeded() {
            return NotificationStackScrollLayoutController.this.mView.onKeyguard();
        }

        public View getChildAtPosition(MotionEvent motionEvent) {
            ExpandableNotificationRow notificationParent;
            ExpandableView childAtPosition = NotificationStackScrollLayoutController.this.mView.getChildAtPosition(motionEvent.getX(), motionEvent.getY(), true, false);
            if (!(childAtPosition instanceof ExpandableNotificationRow) || (notificationParent = ((ExpandableNotificationRow) childAtPosition).getNotificationParent()) == null || !notificationParent.areChildrenExpanded()) {
                return childAtPosition;
            }
            return (notificationParent.areGutsExposed() || NotificationStackScrollLayoutController.this.mSwipeHelper.getExposedMenuView() == notificationParent || (notificationParent.getAttachedChildren().size() == 1 && notificationParent.getEntry().isDismissable())) ? notificationParent : childAtPosition;
        }

        public void onLongPressSent(View view) {
            NotificationStackScrollLayoutController.this.mLongPressedView = view;
        }

        public void onBeginDrag(View view) {
            NotificationStackScrollLayoutController.this.mFalsingCollector.onNotificationStartDismissing();
            NotificationStackScrollLayoutController.this.mView.onSwipeBegin(view);
        }

        public void onChildSnappedBack(View view, float f) {
            NotificationStackScrollLayoutController.this.mView.onSwipeEnd();
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (expandableNotificationRow.isPinned() && !canChildBeDismissed(expandableNotificationRow) && expandableNotificationRow.getEntry().getSbn().getNotification().fullScreenIntent == null) {
                    NotificationStackScrollLayoutController.this.mHeadsUpManager.removeNotification(expandableNotificationRow.getEntry().getSbn().getKey(), true);
                }
            }
        }

        public boolean updateSwipeProgress(View view, boolean z, float f) {
            return !NotificationStackScrollLayoutController.this.mFadeNotificationsOnDismiss;
        }

        public float getFalsingThresholdFactor() {
            return NotificationStackScrollLayoutController.this.mCentralSurfaces.isWakeUpComingFromTouch() ? 1.5f : 1.0f;
        }

        public int getConstrainSwipeStartPosition() {
            NotificationMenuRowPlugin currentMenuRow = NotificationStackScrollLayoutController.this.mSwipeHelper.getCurrentMenuRow();
            if (currentMenuRow != null) {
                return Math.abs(currentMenuRow.getMenuSnapTarget());
            }
            return 0;
        }

        public boolean canChildBeDismissed(View view) {
            return NotificationStackScrollLayout.canChildBeDismissed(view);
        }

        public boolean canChildBeDismissedInDirection(View view, boolean z) {
            return canChildBeDismissed(view);
        }
    };
    public final NotificationEntryManager mNotificationEntryManager;
    public final NotificationGutsManager mNotificationGutsManager;
    public final NotificationListContainerImpl mNotificationListContainer = new NotificationListContainerImpl();
    public final NotificationRoundnessManager mNotificationRoundnessManager;
    public final NotificationStackSizeCalculator mNotificationStackSizeCalculator;
    public final NotificationSwipeHelper.Builder mNotificationSwipeHelperBuilder;
    @VisibleForTesting
    public final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View view) {
            NotificationStackScrollLayoutController.this.mConfigurationController.addCallback(NotificationStackScrollLayoutController.this.mConfigurationListener);
            NotificationStackScrollLayoutController.this.mZenModeController.addCallback(NotificationStackScrollLayoutController.this.mZenModeControllerCallback);
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = NotificationStackScrollLayoutController.this;
            notificationStackScrollLayoutController.mBarState = notificationStackScrollLayoutController.mStatusBarStateController.getState();
            NotificationStackScrollLayoutController.this.mStatusBarStateController.addCallback(NotificationStackScrollLayoutController.this.mStateListener, 2);
        }

        public void onViewDetachedFromWindow(View view) {
            NotificationStackScrollLayoutController.this.mConfigurationController.removeCallback(NotificationStackScrollLayoutController.this.mConfigurationListener);
            NotificationStackScrollLayoutController.this.mZenModeController.removeCallback(NotificationStackScrollLayoutController.this.mZenModeControllerCallback);
            NotificationStackScrollLayoutController.this.mStatusBarStateController.removeCallback(NotificationStackScrollLayoutController.this.mStateListener);
        }
    };
    public final OnHeadsUpChangedListener mOnHeadsUpChangedListener = new OnHeadsUpChangedListener() {
        public void onHeadsUpPinnedModeChanged(boolean z) {
            NotificationStackScrollLayoutController.this.mView.setInHeadsUpPinnedMode(z);
        }

        public void onHeadsUpPinned(NotificationEntry notificationEntry) {
            NotificationStackScrollLayoutController.this.mNotificationRoundnessManager.updateView(notificationEntry.getRow(), false);
        }

        public void onHeadsUpUnPinned(NotificationEntry notificationEntry) {
            ExpandableNotificationRow row = notificationEntry.getRow();
            row.post(new NotificationStackScrollLayoutController$8$$ExternalSyntheticLambda0(this, row));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onHeadsUpUnPinned$0(ExpandableNotificationRow expandableNotificationRow) {
            NotificationStackScrollLayoutController.this.mNotificationRoundnessManager.updateView(expandableNotificationRow, true);
        }

        public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
            long count = NotificationStackScrollLayoutController.this.mHeadsUpManager.getAllEntries().count();
            NotificationEntry topEntry = NotificationStackScrollLayoutController.this.mHeadsUpManager.getTopEntry();
            NotificationStackScrollLayoutController.this.mView.setNumHeadsUp(count);
            NotificationStackScrollLayoutController.this.mView.setTopHeadsUpEntry(topEntry);
            NotificationStackScrollLayoutController.this.generateHeadsUpAnimation(notificationEntry, z);
            NotificationStackScrollLayoutController.this.mNotificationRoundnessManager.updateView(notificationEntry.getRow(), true);
        }
    };
    public final NotificationRemoteInputManager mRemoteInputManager;
    public final Resources mResources;
    public final ScrimController mScrimController;
    public final ShadeController mShadeController;
    public final ShadeTransitionController mShadeTransitionController;
    public boolean mShowEmptyShadeView;
    public final SectionHeaderController mSilentHeaderController;
    public final StackStateLogger mStackStateLogger;
    public final StatusBarStateController.StateListener mStateListener = new StatusBarStateController.StateListener() {
        public void onStatePreChange(int i, int i2) {
            if (i == 2 && i2 == 1) {
                NotificationStackScrollLayoutController.this.mView.requestAnimateEverything();
            }
        }

        public void onStateChanged(int i) {
            NotificationStackScrollLayoutController.this.mBarState = i;
            NotificationStackScrollLayoutController.this.mView.setStatusBarState(NotificationStackScrollLayoutController.this.mBarState);
            if (i == 1) {
                NotificationStackScrollLayoutController.this.mGroupExpansionManager.collapseGroups();
            }
        }

        public void onUpcomingStateChanged(int i) {
            NotificationStackScrollLayoutController.this.mView.setUpcomingStatusBarState(i);
        }

        public void onStatePostChange() {
            NotificationStackScrollLayoutController.this.mView.updateSensitiveness(NotificationStackScrollLayoutController.this.mStatusBarStateController.goingToFullShade(), NotificationStackScrollLayoutController.this.mLockscreenUserManager.isAnyProfilePublicMode());
            NotificationStackScrollLayoutController.this.mView.onStatePostChange(NotificationStackScrollLayoutController.this.mStatusBarStateController.fromShadeLocked());
            NotificationStackScrollLayoutController.this.mNotificationEntryManager.updateNotifications("CentralSurfaces state changed");
        }
    };
    public final SysuiStatusBarStateController mStatusBarStateController;
    public NotificationSwipeHelper mSwipeHelper;
    public final TunerService mTunerService;
    public final UiEventLogger mUiEventLogger;
    public NotificationStackScrollLayout mView;
    public final NotificationVisibilityProvider mVisibilityProvider;
    public final VisualStabilityManager mVisualStabilityManager;
    public final ZenModeController mZenModeController;
    public final ZenModeController.Callback mZenModeControllerCallback = new ZenModeController.Callback() {
        public void onZenChanged(int i) {
            NotificationStackScrollLayoutController.this.updateShowEmptyShadeView();
        }
    };

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        if (this.mView.isExpanded()) {
            this.mView.setAnimateBottomOnLayout(true);
        }
        this.mView.post(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda16(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateFooter();
        updateSectionBoundaries("dynamic privacy changed");
    }

    public final void updateResources() {
        this.mNotificationStackSizeCalculator.updateResources();
    }

    public void setOverExpansion(float f) {
        this.mView.setOverExpansion(f);
    }

    public NotificationStackScrollLayoutController(boolean z, NotificationGutsManager notificationGutsManager, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManagerPhone headsUpManagerPhone, NotificationRoundnessManager notificationRoundnessManager, TunerService tunerService, DeviceProvisionedController deviceProvisionedController, DynamicPrivacyController dynamicPrivacyController, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardMediaController keyguardMediaController, KeyguardBypassController keyguardBypassController, ZenModeController zenModeController, SysuiColorExtractor sysuiColorExtractor, NotificationLockscreenUserManager notificationLockscreenUserManager, MetricsLogger metricsLogger, FalsingCollector falsingCollector, FalsingManager falsingManager, Resources resources, NotificationSwipeHelper.Builder builder, CentralSurfaces centralSurfaces, ScrimController scrimController, NotificationGroupManagerLegacy notificationGroupManagerLegacy, GroupExpansionManager groupExpansionManager, SectionHeaderController sectionHeaderController, NotifPipelineFlags notifPipelineFlags, NotifPipeline notifPipeline, NotifCollection notifCollection, NotificationEntryManager notificationEntryManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ShadeTransitionController shadeTransitionController, IStatusBarService iStatusBarService, UiEventLogger uiEventLogger, LayoutInflater layoutInflater, NotificationRemoteInputManager notificationRemoteInputManager, VisualStabilityManager visualStabilityManager, ShadeController shadeController, InteractionJankMonitor interactionJankMonitor, StackStateLogger stackStateLogger, NotificationStackScrollLogger notificationStackScrollLogger, NotificationStackSizeCalculator notificationStackSizeCalculator) {
        this.mStackStateLogger = stackStateLogger;
        this.mLogger = notificationStackScrollLogger;
        this.mAllowLongPress = z;
        this.mNotificationGutsManager = notificationGutsManager;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mNotificationRoundnessManager = notificationRoundnessManager;
        this.mTunerService = tunerService;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mDynamicPrivacyController = dynamicPrivacyController;
        this.mConfigurationController = configurationController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mKeyguardMediaController = keyguardMediaController;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mZenModeController = zenModeController;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mMetricsLogger = metricsLogger;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mShadeTransitionController = shadeTransitionController;
        this.mFalsingCollector = falsingCollector;
        this.mFalsingManager = falsingManager;
        this.mResources = resources;
        this.mNotificationSwipeHelperBuilder = builder;
        this.mCentralSurfaces = centralSurfaces;
        this.mScrimController = scrimController;
        this.mJankMonitor = interactionJankMonitor;
        this.mNotificationStackSizeCalculator = notificationStackSizeCalculator;
        this.mGroupExpansionManager = groupExpansionManager;
        notificationGroupManagerLegacy.registerGroupChangeListener(new NotificationGroupManagerLegacy.OnGroupChangeListener() {
            public void onGroupsChanged() {
                NotificationStackScrollLayoutController.this.mCentralSurfaces.requestNotificationUpdate("onGroupsChanged");
            }
        });
        this.mNotifPipelineFlags = notifPipelineFlags;
        this.mSilentHeaderController = sectionHeaderController;
        this.mNotifPipeline = notifPipeline;
        this.mNotifCollection = notifCollection;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mIStatusBarService = iStatusBarService;
        this.mUiEventLogger = uiEventLogger;
        this.mLayoutInflater = layoutInflater;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mShadeController = shadeController;
        updateResources();
    }

    public void attach(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.mView = notificationStackScrollLayout;
        notificationStackScrollLayout.setLogger(this.mStackStateLogger);
        this.mView.setController(this);
        this.mView.setLogger(this.mLogger);
        this.mView.setTouchHandler(new TouchHandler());
        this.mView.setCentralSurfaces(this.mCentralSurfaces);
        this.mView.setClearAllAnimationListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda0(this));
        this.mView.setClearAllListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda6(this));
        this.mView.setFooterClearAllListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda7(this));
        this.mView.setIsRemoteInputActive(this.mRemoteInputManager.isRemoteInputActive());
        this.mRemoteInputManager.addControllerCallback(new RemoteInputController.Callback() {
            public void onRemoteInputActive(boolean z) {
                NotificationStackScrollLayoutController.this.mView.setIsRemoteInputActive(z);
            }
        });
        this.mView.setShadeController(this.mShadeController);
        this.mKeyguardBypassController.registerOnBypassStateChangedListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda8(this));
        this.mNotificationRoundnessManager.setShouldRoundPulsingViews(!this.mKeyguardBypassController.getBypassEnabled());
        this.mSwipeHelper = this.mNotificationSwipeHelperBuilder.setSwipeDirection(0).setNotificationCallback(this.mNotificationCallback).setOnMenuEventListener(this.mMenuEventListener).build();
        if (this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() {
                public void onEntryUpdated(NotificationEntry notificationEntry) {
                    NotificationStackScrollLayoutController.this.mView.onEntryUpdated(notificationEntry);
                }
            });
        } else {
            this.mNotificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
                public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                    NotificationStackScrollLayoutController.this.mView.onEntryUpdated(notificationEntry);
                }
            });
        }
        NotificationStackScrollLayout notificationStackScrollLayout2 = this.mView;
        notificationStackScrollLayout2.initView(notificationStackScrollLayout2.getContext(), this.mSwipeHelper, this.mNotificationStackSizeCalculator);
        this.mView.setKeyguardBypassEnabled(this.mKeyguardBypassController.getBypassEnabled());
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        NotificationStackScrollLayout notificationStackScrollLayout3 = this.mView;
        Objects.requireNonNull(notificationStackScrollLayout3);
        keyguardBypassController.registerOnBypassStateChangedListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda9(notificationStackScrollLayout3));
        this.mView.setManageButtonClickListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda10(this));
        this.mHeadsUpManager.addListener(this.mOnHeadsUpChangedListener);
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        NotificationStackScrollLayout notificationStackScrollLayout4 = this.mView;
        Objects.requireNonNull(notificationStackScrollLayout4);
        headsUpManagerPhone.setAnimationStateHandler(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda11(notificationStackScrollLayout4));
        this.mDynamicPrivacyController.addListener(this.mDynamicPrivacyControllerListener);
        ScrimController scrimController = this.mScrimController;
        NotificationStackScrollLayout notificationStackScrollLayout5 = this.mView;
        Objects.requireNonNull(notificationStackScrollLayout5);
        scrimController.setScrimBehindChangeRunnable(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda12(notificationStackScrollLayout5));
        this.mLockscreenShadeTransitionController.setStackScroller(this);
        this.mShadeTransitionController.setNotificationStackScrollLayoutController(this);
        this.mLockscreenUserManager.addUserChangedListener(this.mLockscreenUserChangeListener);
        this.mFadeNotificationsOnDismiss = this.mResources.getBoolean(R$bool.config_fadeNotificationsOnDismiss);
        NotificationRoundnessManager notificationRoundnessManager = this.mNotificationRoundnessManager;
        NotificationStackScrollLayout notificationStackScrollLayout6 = this.mView;
        Objects.requireNonNull(notificationStackScrollLayout6);
        notificationRoundnessManager.setOnRoundingChangedCallback(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda13(notificationStackScrollLayout6));
        NotificationStackScrollLayout notificationStackScrollLayout7 = this.mView;
        NotificationRoundnessManager notificationRoundnessManager2 = this.mNotificationRoundnessManager;
        Objects.requireNonNull(notificationRoundnessManager2);
        notificationStackScrollLayout7.addOnExpandedHeightChangedListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda14(notificationRoundnessManager2));
        this.mVisualStabilityManager.setVisibilityLocationProvider(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda1(this));
        this.mTunerService.addTunable(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda2(this), "high_priority", "notification_history_enabled");
        this.mKeyguardMediaController.setVisibilityChangedListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda3(this));
        this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
        this.mDeviceProvisionedListener.onDeviceProvisionedChanged();
        if (this.mView.isAttachedToWindow()) {
            this.mOnAttachStateChangeListener.onViewAttachedToWindow(this.mView);
        }
        this.mView.addOnAttachStateChangeListener(this.mOnAttachStateChangeListener);
        this.mSilentHeaderController.setOnClearSectionClickListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda4(this));
        this.mGroupExpansionManager.registerGroupExpansionChangeListener(new NotificationStackScrollLayoutController$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$2(int i) {
        this.mUiEventLogger.log(NotificationPanelEvent.fromSelection(i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$3() {
        this.mMetricsLogger.action(148);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$4(boolean z) {
        this.mNotificationRoundnessManager.setShouldRoundPulsingViews(!z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$5(View view) {
        NotificationActivityStarter notificationActivityStarter = this.mNotificationActivityStarter;
        if (notificationActivityStarter != null) {
            notificationActivityStarter.startHistoryIntent(view, this.mView.isHistoryShown());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$6(String str, String str2) {
        str.hashCode();
        if (str.equals("high_priority")) {
            this.mView.setHighPriorityBeforeSpeedBump("1".equals(str2));
        } else if (str.equals("notification_history_enabled")) {
            this.mHistoryEnabled = null;
            updateFooter();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$attach$7(Boolean bool) {
        if (bool.booleanValue()) {
            this.mView.generateAddAnimation(this.mKeyguardMediaController.getSinglePaneContainer(), false);
        } else {
            this.mView.generateRemoveAnimation(this.mKeyguardMediaController.getSinglePaneContainer());
        }
        this.mView.requestChildrenUpdate();
        return Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$8(View view) {
        clearSilentNotifications();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$9(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        this.mView.onGroupExpandChanged(expandableNotificationRow, z);
    }

    public final boolean isInVisibleLocation(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        ExpandableViewState viewState = row.getViewState();
        if (viewState == null || (viewState.location & 5) == 0 || row.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    public boolean isViewAffectedBySwipe(ExpandableView expandableView) {
        return this.mNotificationRoundnessManager.isViewAffectedBySwipe(expandableView);
    }

    public void addOnExpandedHeightChangedListener(BiConsumer<Float, Float> biConsumer) {
        this.mView.addOnExpandedHeightChangedListener(biConsumer);
    }

    public void removeOnExpandedHeightChangedListener(BiConsumer<Float, Float> biConsumer) {
        this.mView.removeOnExpandedHeightChangedListener(biConsumer);
    }

    public void setHeadsUpAppearanceController(HeadsUpAppearanceController headsUpAppearanceController) {
        this.mHeadsUpAppearanceController = headsUpAppearanceController;
        this.mView.setHeadsUpAppearanceController(headsUpAppearanceController);
    }

    public float getAppearFraction() {
        return this.mView.getAppearFraction();
    }

    public float getExpandedHeight() {
        return this.mView.getExpandedHeight();
    }

    public void requestLayout() {
        this.mView.requestLayout();
    }

    public int getRight() {
        return this.mView.getRight();
    }

    public int getLeft() {
        return this.mView.getLeft();
    }

    public int getTop() {
        return this.mView.getTop();
    }

    public int getBottom() {
        return this.mView.getBottom();
    }

    public int indexOfChild(View view) {
        return this.mView.indexOfChild(view);
    }

    public void setOnHeightChangedListener(ExpandableView.OnHeightChangedListener onHeightChangedListener) {
        this.mView.setOnHeightChangedListener(onHeightChangedListener);
    }

    public void setOverscrollTopChangedListener(NotificationStackScrollLayout.OnOverscrollTopChangedListener onOverscrollTopChangedListener) {
        this.mView.setOverscrollTopChangedListener(onOverscrollTopChangedListener);
    }

    public void setOnEmptySpaceClickListener(NotificationStackScrollLayout.OnEmptySpaceClickListener onEmptySpaceClickListener) {
        this.mView.setOnEmptySpaceClickListener(onEmptySpaceClickListener);
    }

    public void setTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        this.mView.setTrackingHeadsUp(expandableNotificationRow);
        this.mNotificationRoundnessManager.setTrackingHeadsUp(expandableNotificationRow);
    }

    public void wakeUpFromPulse() {
        this.mView.wakeUpFromPulse();
    }

    public boolean isPulseExpanding() {
        return this.mView.isPulseExpanding();
    }

    public void setOnPulseHeightChangedListener(Runnable runnable) {
        this.mView.setOnPulseHeightChangedListener(runnable);
    }

    public void setDozeAmount(float f) {
        this.mView.setDozeAmount(f);
    }

    public int getSpeedBumpIndex() {
        return this.mView.getSpeedBumpIndex();
    }

    public void setHideAmount(float f, float f2) {
        this.mView.setHideAmount(f, f2);
    }

    public void notifyHideAnimationStart(boolean z) {
        this.mView.notifyHideAnimationStart(z);
    }

    public float setPulseHeight(float f) {
        return this.mView.setPulseHeight(f);
    }

    public void getLocationOnScreen(int[] iArr) {
        this.mView.getLocationOnScreen(iArr);
    }

    public ExpandableView getChildAtRawPosition(float f, float f2) {
        return this.mView.getChildAtRawPosition(f, f2);
    }

    public void setIsFullWidth(boolean z) {
        this.mView.setIsFullWidth(z);
    }

    public boolean isAddOrRemoveAnimationPending() {
        return this.mView.isAddOrRemoveAnimationPending();
    }

    public int getVisibleNotificationCount() {
        return this.mNotifStats.getNumActiveNotifs();
    }

    public boolean isHistoryEnabled() {
        Boolean bool = this.mHistoryEnabled;
        if (bool == null) {
            NotificationStackScrollLayout notificationStackScrollLayout = this.mView;
            boolean z = false;
            if (notificationStackScrollLayout == null || notificationStackScrollLayout.getContext() == null) {
                Log.wtf("StackScrollerController", "isHistoryEnabled failed to initialize its value");
                return false;
            }
            if (Settings.Secure.getIntForUser(this.mView.getContext().getContentResolver(), "notification_history_enabled", 0, -2) == 1) {
                z = true;
            }
            bool = Boolean.valueOf(z);
            this.mHistoryEnabled = bool;
        }
        return bool.booleanValue();
    }

    public int getIntrinsicContentHeight() {
        return this.mView.getIntrinsicContentHeight();
    }

    public void setIntrinsicPadding(int i) {
        this.mView.setIntrinsicPadding(i);
    }

    public int getHeight() {
        return this.mView.getHeight();
    }

    public int getChildCount() {
        return this.mView.getChildCount();
    }

    public ExpandableView getChildAt(int i) {
        return (ExpandableView) this.mView.getChildAt(i);
    }

    public void goToFullShade(long j) {
        this.mView.goToFullShade(j);
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2, boolean z3) {
        this.mView.setOverScrollAmount(f, z, z2, z3);
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2) {
        this.mView.setOverScrollAmount(f, z, z2);
    }

    public void resetScrollPosition() {
        this.mView.resetScrollPosition();
    }

    public void setShouldShowShelfOnly(boolean z) {
        this.mView.setShouldShowShelfOnly(z);
    }

    public void cancelLongPress() {
        this.mView.cancelLongPress();
    }

    public float getX() {
        return this.mView.getX();
    }

    public boolean isBelowLastNotification(float f, float f2) {
        return this.mView.isBelowLastNotification(f, f2);
    }

    public float getWidth() {
        return (float) this.mView.getWidth();
    }

    public float getOpeningHeight() {
        return this.mView.getOpeningHeight();
    }

    public float getBottomMostNotificationBottom() {
        return this.mView.getBottomMostNotificationBottom();
    }

    public void checkSnoozeLeavebehind() {
        if (this.mView.getCheckSnoozeLeaveBehind()) {
            this.mNotificationGutsManager.closeAndSaveGuts(true, false, false, -1, -1, false);
            this.mView.setCheckForLeaveBehind(false);
        }
    }

    public void setQsFullScreen(boolean z) {
        this.mView.setQsFullScreen(z);
        updateShowEmptyShadeView();
    }

    public void setScrollingEnabled(boolean z) {
        this.mView.setScrollingEnabled(z);
    }

    public void setQsExpansionFraction(float f) {
        this.mView.setQsExpansionFraction(f);
    }

    public void setOnStackYChanged(Consumer<Boolean> consumer) {
        this.mView.setOnStackYChanged(consumer);
    }

    public float getNotificationSquishinessFraction() {
        return this.mView.getNotificationSquishinessFraction();
    }

    public float calculateAppearFractionBypass() {
        return this.mView.calculateAppearFractionBypass();
    }

    public void updateTopPadding(float f, boolean z) {
        this.mView.updateTopPadding(f, z);
    }

    public boolean isScrolledToBottom() {
        return this.mView.isScrolledToBottom();
    }

    public int getNotGoneChildCount() {
        return this.mView.getNotGoneChildCount();
    }

    public float getIntrinsicPadding() {
        return (float) this.mView.getIntrinsicPadding();
    }

    public float getLayoutMinHeight() {
        return (float) this.mView.getLayoutMinHeight();
    }

    public int getEmptyBottomMargin() {
        return this.mView.getEmptyBottomMargin();
    }

    public float getTopPaddingOverflow() {
        return this.mView.getTopPaddingOverflow();
    }

    public int getTopPadding() {
        return this.mView.getTopPadding();
    }

    public float getEmptyShadeViewHeight() {
        return (float) this.mView.getEmptyShadeViewHeight();
    }

    public void setAlpha(float f) {
        this.mView.setAlpha(f);
    }

    public float calculateAppearFraction(float f) {
        return this.mView.calculateAppearFraction(f);
    }

    public void onExpansionStarted() {
        this.mView.onExpansionStarted();
        checkSnoozeLeavebehind();
    }

    public void onExpansionStopped() {
        this.mView.setCheckForLeaveBehind(false);
        this.mView.onExpansionStopped();
    }

    public void onPanelTrackingStarted() {
        this.mView.onPanelTrackingStarted();
    }

    public void onPanelTrackingStopped() {
        this.mView.onPanelTrackingStopped();
    }

    public void setHeadsUpBoundaries(int i, int i2) {
        this.mView.setHeadsUpBoundaries(i, i2);
    }

    public void setUnlockHintRunning(boolean z) {
        this.mView.setUnlockHintRunning(z);
    }

    public void setPanelFlinging(boolean z) {
        this.mView.setPanelFlinging(z);
    }

    public void updateShowEmptyShadeView() {
        Trace.beginSection("NSSLC.updateShowEmptyShadeView");
        boolean z = true;
        if (this.mStatusBarStateController.getCurrentOrUpcomingState() == 1 || this.mView.isQsFullScreen() || getVisibleNotificationCount() != 0) {
            z = false;
        }
        this.mShowEmptyShadeView = z;
        this.mView.updateEmptyShadeView(z, this.mZenModeController.areNotificationsHiddenInShade());
        Trace.endSection();
    }

    public boolean isShowingEmptyShadeView() {
        return this.mShowEmptyShadeView;
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        this.mView.setHeadsUpAnimatingAway(z);
    }

    public HeadsUpTouchHelper.Callback getHeadsUpCallback() {
        return this.mView.getHeadsUpCallback();
    }

    public void forceNoOverlappingRendering(boolean z) {
        this.mView.forceNoOverlappingRendering(z);
    }

    public void setExpandingVelocity(float f) {
        this.mView.setExpandingVelocity(f);
    }

    public void setExpandedHeight(float f) {
        this.mView.setExpandedHeight(f);
    }

    public void setQsHeader(ViewGroup viewGroup) {
        this.mView.setQsHeader(viewGroup);
    }

    public void setAnimationsEnabled(boolean z) {
        this.mView.setAnimationsEnabled(z);
    }

    public void setDozing(boolean z, boolean z2, PointF pointF) {
        this.mView.setDozing(z, z2, pointF);
    }

    public void setPulsing(boolean z, boolean z2) {
        this.mView.setPulsing(z, z2);
    }

    public boolean hasActiveClearableNotifications(int i) {
        return hasNotifications(i, true);
    }

    public boolean hasNotifications(int i, boolean z) {
        boolean z2;
        boolean z3;
        if (z) {
            z2 = this.mNotifStats.getHasClearableAlertingNotifs();
        } else {
            z2 = this.mNotifStats.getHasNonClearableAlertingNotifs();
        }
        if (z) {
            z3 = this.mNotifStats.getHasClearableSilentNotifs();
        } else {
            z3 = this.mNotifStats.getHasNonClearableSilentNotifs();
        }
        if (i != 0) {
            if (i == 1) {
                return z2;
            }
            if (i == 2) {
                return z3;
            }
            throw new IllegalStateException("Bad selection: " + i);
        } else if (z3 || z2) {
            return true;
        } else {
            return false;
        }
    }

    public void setMaxDisplayedNotifications(int i) {
        this.mNotificationListContainer.setMaxDisplayedNotifications(i);
    }

    public void setKeyguardBottomPaddingForDebug(float f) {
        this.mView.setKeyguardBottomPadding(f);
    }

    public RemoteInputController.Delegate createDelegate() {
        return new RemoteInputController.Delegate() {
            public void setRemoteInputActive(NotificationEntry notificationEntry, boolean z) {
                NotificationStackScrollLayoutController.this.mHeadsUpManager.setRemoteInputActive(notificationEntry, z);
                notificationEntry.notifyHeightChanged(true);
                NotificationStackScrollLayoutController.this.updateFooter();
            }

            public void lockScrollTo(NotificationEntry notificationEntry) {
                NotificationStackScrollLayoutController.this.mView.lockScrollTo(notificationEntry.getRow());
            }

            public void requestDisallowLongPressAndDismiss() {
                NotificationStackScrollLayoutController.this.mView.requestDisallowLongPress();
                NotificationStackScrollLayoutController.this.mView.requestDisallowDismiss();
            }
        };
    }

    public void updateSectionBoundaries(String str) {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            Trace.beginSection("NSSLC.updateSectionBoundaries");
            this.mView.updateSectionBoundaries(str);
            Trace.endSection();
        }
    }

    public void updateFooter() {
        Trace.beginSection("NSSLC.updateFooter");
        this.mView.updateFooter();
        Trace.endSection();
    }

    public void onUpdateRowStates() {
        this.mView.onUpdateRowStates();
    }

    public ActivatableNotificationView getActivatedChild() {
        return this.mView.getActivatedChild();
    }

    public void setActivatedChild(ActivatableNotificationView activatableNotificationView) {
        this.mView.setActivatedChild(activatableNotificationView);
    }

    public void runAfterAnimationFinished(Runnable runnable) {
        this.mView.runAfterAnimationFinished(runnable);
    }

    public void setShelfController(NotificationShelfController notificationShelfController) {
        this.mView.setShelfController(notificationShelfController);
    }

    public ExpandableView getFirstChildNotGone() {
        return this.mView.getFirstChildNotGone();
    }

    public final void generateHeadsUpAnimation(NotificationEntry notificationEntry, boolean z) {
        this.mView.generateHeadsUpAnimation(notificationEntry, z);
    }

    public void generateHeadsUpAnimation(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        this.mView.generateHeadsUpAnimation(expandableNotificationRow, z);
    }

    public void setMaxTopPadding(int i) {
        this.mView.setMaxTopPadding(i);
    }

    public int getTransientViewCount() {
        return this.mView.getTransientViewCount();
    }

    public View getTransientView(int i) {
        return this.mView.getTransientView(i);
    }

    public NotificationStackScrollLayout getView() {
        return this.mView;
    }

    public NotificationRoundnessManager getNoticationRoundessManager() {
        return this.mNotificationRoundnessManager;
    }

    public NotificationListContainer getNotificationListContainer() {
        return this.mNotificationListContainer;
    }

    public NotifStackController getNotifStackController() {
        return this.mNotifStackController;
    }

    public void resetCheckSnoozeLeavebehind() {
        this.mView.resetCheckSnoozeLeavebehind();
    }

    public final DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry) {
        return new DismissedByUserStats(3, 1, this.mVisibilityProvider.obtain(notificationEntry, true));
    }

    /* JADX WARNING: type inference failed for: r2v1, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void closeControlsIfOutsideTouch(android.view.MotionEvent r8) {
        /*
            r7 = this;
            com.android.systemui.statusbar.notification.row.NotificationGutsManager r0 = r7.mNotificationGutsManager
            com.android.systemui.statusbar.notification.row.NotificationGuts r0 = r0.getExposedGuts()
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper r1 = r7.mSwipeHelper
            com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin r1 = r1.getCurrentMenuRow()
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper r2 = r7.mSwipeHelper
            android.view.View r2 = r2.getTranslatingParentView()
            if (r0 == 0) goto L_0x001f
            com.android.systemui.statusbar.notification.row.NotificationGuts$GutsContent r3 = r0.getGutsContent()
            boolean r3 = r3.isLeavebehind()
            if (r3 != 0) goto L_0x001f
            goto L_0x002c
        L_0x001f:
            if (r1 == 0) goto L_0x002b
            boolean r0 = r1.isMenuVisible()
            if (r0 == 0) goto L_0x002b
            if (r2 == 0) goto L_0x002b
            r0 = r2
            goto L_0x002c
        L_0x002b:
            r0 = 0
        L_0x002c:
            if (r0 == 0) goto L_0x0045
            boolean r8 = com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper.isTouchInView(r8, r0)
            if (r8 != 0) goto L_0x0045
            com.android.systemui.statusbar.notification.row.NotificationGutsManager r0 = r7.mNotificationGutsManager
            r1 = 0
            r2 = 0
            r3 = 1
            r4 = -1
            r5 = -1
            r6 = 0
            r0.closeAndSaveGuts(r1, r2, r3, r4, r5, r6)
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper r7 = r7.mSwipeHelper
            r8 = 1
            r7.resetExposedMenuView(r8, r8)
        L_0x0045:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.closeControlsIfOutsideTouch(android.view.MotionEvent):void");
    }

    public void clearSilentNotifications() {
        this.mView.clearNotifications(2, true ^ hasActiveClearableNotifications(1));
    }

    public final void onAnimationEnd(List<ExpandableNotificationRow> list, int i) {
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            for (ExpandableNotificationRow next : list) {
                if (NotificationStackScrollLayout.canChildBeCleared(next)) {
                    this.mNotificationEntryManager.performRemoveNotification(next.getEntry().getSbn(), getDismissedByUserStats(next.getEntry()), 3);
                } else {
                    next.resetTranslation();
                }
            }
            if (i == 0) {
                try {
                    this.mIStatusBarService.onClearAllNotifications(this.mLockscreenUserManager.getCurrentUserId());
                } catch (Exception unused) {
                }
            }
        } else if (i == 0) {
            this.mNotifCollection.dismissAllNotifications(this.mLockscreenUserManager.getCurrentUserId());
        } else {
            ArrayList arrayList = new ArrayList();
            for (ExpandableNotificationRow entry : list) {
                NotificationEntry entry2 = entry.getEntry();
                arrayList.add(new Pair(entry2, getDismissedByUserStats(entry2)));
            }
            this.mNotifCollection.dismissNotifications(arrayList);
        }
    }

    public ExpandHelper.Callback getExpandHelperCallback() {
        return this.mView.getExpandHelperCallback();
    }

    public boolean isInLockedDownShade() {
        return this.mDynamicPrivacyController.isInLockedDownShade();
    }

    public boolean isLongPressInProgress() {
        return this.mLongPressedView != null;
    }

    public void setDimmed(boolean z, boolean z2) {
        this.mView.setDimmed(z, z2);
    }

    public int getFullShadeTransitionInset() {
        MediaContainerView singlePaneContainer = this.mKeyguardMediaController.getSinglePaneContainer();
        if (singlePaneContainer == null || singlePaneContainer.getHeight() == 0 || this.mStatusBarStateController.getState() != 1) {
            return 0;
        }
        return singlePaneContainer.getHeight() + this.mView.getPaddingAfterMedia();
    }

    public void setTransitionToFullShadeAmount(float f) {
        this.mView.setFractionToShade(f);
    }

    public void setOverScrollAmount(int i) {
        this.mView.setExtraTopInsetForFullShadeTransition((float) i);
    }

    public void setWillExpand(boolean z) {
        this.mView.setWillExpand(z);
    }

    public void setOnScrollListener(Consumer<Integer> consumer) {
        this.mView.setOnScrollListener(consumer);
    }

    public void setRoundedClippingBounds(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mView.setRoundedClippingBounds(i, i2, i3, i4, i5, i6);
    }

    public void animateNextTopPaddingChange() {
        this.mView.animateNextTopPaddingChange();
    }

    public void setNotificationActivityStarter(NotificationActivityStarter notificationActivityStarter) {
        this.mNotificationActivityStarter = notificationActivityStarter;
    }

    public enum NotificationPanelEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        DISMISS_ALL_NOTIFICATIONS_PANEL(312),
        DISMISS_SILENT_NOTIFICATIONS_PANEL(314);
        
        private final int mId;

        /* access modifiers changed from: public */
        NotificationPanelEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static UiEventLogger.UiEventEnum fromSelection(int i) {
            if (i == 0) {
                return DISMISS_ALL_NOTIFICATIONS_PANEL;
            }
            if (i == 2) {
                return DISMISS_SILENT_NOTIFICATIONS_PANEL;
            }
            if (!NotificationStackScrollLayoutController.DEBUG) {
                return INVALID;
            }
            throw new IllegalArgumentException("Unexpected selection" + i);
        }
    }

    public class NotificationListContainerImpl implements NotificationListContainer {
        public NotificationListContainerImpl() {
        }

        public void setChildTransferInProgress(boolean z) {
            NotificationStackScrollLayoutController.this.mView.setChildTransferInProgress(z);
        }

        public void changeViewPosition(ExpandableView expandableView, int i) {
            NotificationStackScrollLayoutController.this.mView.changeViewPosition(expandableView, i);
        }

        public void notifyGroupChildAdded(ExpandableView expandableView) {
            NotificationStackScrollLayoutController.this.mView.notifyGroupChildAdded(expandableView);
        }

        public void notifyGroupChildRemoved(ExpandableView expandableView, ViewGroup viewGroup) {
            NotificationStackScrollLayoutController.this.mView.notifyGroupChildRemoved(expandableView, viewGroup);
        }

        public void generateAddAnimation(ExpandableView expandableView, boolean z) {
            NotificationStackScrollLayoutController.this.mView.generateAddAnimation(expandableView, z);
        }

        public void generateChildOrderChangedEvent() {
            NotificationStackScrollLayoutController.this.mView.generateChildOrderChangedEvent();
        }

        public int getContainerChildCount() {
            return NotificationStackScrollLayoutController.this.mView.getContainerChildCount();
        }

        public int getTopClippingStartLocation() {
            return NotificationStackScrollLayoutController.this.mView.getTopClippingStartLocation();
        }

        public View getContainerChildAt(int i) {
            return NotificationStackScrollLayoutController.this.mView.getContainerChildAt(i);
        }

        public void removeContainerView(View view) {
            NotificationStackScrollLayoutController.this.mView.removeContainerView(view);
        }

        public void addContainerView(View view) {
            NotificationStackScrollLayoutController.this.mView.addContainerView(view);
        }

        public void addContainerViewAt(View view, int i) {
            NotificationStackScrollLayoutController.this.mView.addContainerViewAt(view, i);
        }

        public void setMaxDisplayedNotifications(int i) {
            NotificationStackScrollLayoutController.this.mView.setMaxDisplayedNotifications(i);
        }

        public ViewGroup getViewParentForNotification(NotificationEntry notificationEntry) {
            return NotificationStackScrollLayoutController.this.mView.getViewParentForNotification(notificationEntry);
        }

        public void resetExposedMenuView(boolean z, boolean z2) {
            NotificationStackScrollLayoutController.this.mSwipeHelper.resetExposedMenuView(z, z2);
        }

        public NotificationSwipeActionHelper getSwipeActionHelper() {
            return NotificationStackScrollLayoutController.this.mSwipeHelper;
        }

        public void cleanUpViewStateForEntry(NotificationEntry notificationEntry) {
            NotificationStackScrollLayoutController.this.mView.cleanUpViewStateForEntry(notificationEntry);
        }

        public void setChildLocationsChangedListener(NotificationLogger.OnChildLocationsChangedListener onChildLocationsChangedListener) {
            NotificationStackScrollLayoutController.this.mView.setChildLocationsChangedListener(onChildLocationsChangedListener);
        }

        public boolean hasPulsingNotifications() {
            return NotificationStackScrollLayoutController.this.mView.hasPulsingNotifications();
        }

        public boolean isInVisibleLocation(NotificationEntry notificationEntry) {
            return NotificationStackScrollLayoutController.this.isInVisibleLocation(notificationEntry);
        }

        public void onHeightChanged(ExpandableView expandableView, boolean z) {
            NotificationStackScrollLayoutController.this.mView.onChildHeightChanged(expandableView, z);
        }

        public void onReset(ExpandableView expandableView) {
            NotificationStackScrollLayoutController.this.mView.onChildHeightReset(expandableView);
        }

        public void bindRow(ExpandableNotificationRow expandableNotificationRow) {
            expandableNotificationRow.setHeadsUpAnimatingAwayListener(new NotificationStackScrollLayoutController$NotificationListContainerImpl$$ExternalSyntheticLambda0(this, expandableNotificationRow));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$bindRow$0(ExpandableNotificationRow expandableNotificationRow, Boolean bool) {
            NotificationStackScrollLayoutController.this.mNotificationRoundnessManager.updateView(expandableNotificationRow, false);
            NotificationStackScrollLayoutController.this.mHeadsUpAppearanceController.lambda$updateHeadsUpHeaders$4(expandableNotificationRow.getEntry());
        }

        public void applyLaunchAnimationParams(LaunchAnimationParameters launchAnimationParameters) {
            NotificationStackScrollLayoutController.this.mView.applyLaunchAnimationParams(launchAnimationParameters);
        }

        public void setExpandingNotification(ExpandableNotificationRow expandableNotificationRow) {
            NotificationStackScrollLayoutController.this.mView.setExpandingNotification(expandableNotificationRow);
        }

        public boolean containsView(View view) {
            return NotificationStackScrollLayoutController.this.mView.containsView(view);
        }
    }

    public class TouchHandler implements Gefingerpoken {
        public TouchHandler() {
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            MotionEvent motionEvent2 = motionEvent;
            NotificationStackScrollLayoutController.this.mView.initDownStates(motionEvent2);
            NotificationStackScrollLayoutController.this.mView.handleEmptySpaceClick(motionEvent2);
            NotificationGuts exposedGuts = NotificationStackScrollLayoutController.this.mNotificationGutsManager.getExposedGuts();
            boolean onInterceptTouchEvent = NotificationStackScrollLayoutController.this.mLongPressedView != null ? NotificationStackScrollLayoutController.this.mSwipeHelper.onInterceptTouchEvent(motionEvent2) : false;
            boolean onInterceptTouchEvent2 = (NotificationStackScrollLayoutController.this.mLongPressedView != null || NotificationStackScrollLayoutController.this.mSwipeHelper.isSwiping() || NotificationStackScrollLayoutController.this.mView.getOnlyScrollingInThisMotion() || exposedGuts != null) ? false : NotificationStackScrollLayoutController.this.mView.getExpandHelper().onInterceptTouchEvent(motionEvent2);
            boolean onInterceptTouchEventScroll = (NotificationStackScrollLayoutController.this.mLongPressedView != null || NotificationStackScrollLayoutController.this.mSwipeHelper.isSwiping() || NotificationStackScrollLayoutController.this.mView.isExpandingNotification()) ? false : NotificationStackScrollLayoutController.this.mView.onInterceptTouchEventScroll(motionEvent2);
            boolean onInterceptTouchEvent3 = (NotificationStackScrollLayoutController.this.mLongPressedView != null || NotificationStackScrollLayoutController.this.mView.isBeingDragged() || NotificationStackScrollLayoutController.this.mView.isExpandingNotification() || NotificationStackScrollLayoutController.this.mView.getExpandedInThisMotion() || NotificationStackScrollLayoutController.this.mView.getOnlyScrollingInThisMotion() || NotificationStackScrollLayoutController.this.mView.getDisallowDismissInThisMotion()) ? false : NotificationStackScrollLayoutController.this.mSwipeHelper.onInterceptTouchEvent(motionEvent2);
            boolean z = motionEvent.getActionMasked() == 1;
            if (!NotificationSwipeHelper.isTouchInView(motionEvent2, exposedGuts) && z && !onInterceptTouchEvent3 && !onInterceptTouchEvent2 && !onInterceptTouchEventScroll) {
                NotificationStackScrollLayoutController.this.mView.setCheckForLeaveBehind(false);
                NotificationStackScrollLayoutController.this.mNotificationGutsManager.closeAndSaveGuts(true, false, false, -1, -1, false);
            }
            if (motionEvent.getActionMasked() == 1) {
                NotificationStackScrollLayoutController.this.mView.setCheckForLeaveBehind(true);
            }
            if (!(NotificationStackScrollLayoutController.this.mJankMonitor == null || !onInterceptTouchEventScroll || motionEvent.getActionMasked() == 0)) {
                NotificationStackScrollLayoutController.this.mJankMonitor.begin(NotificationStackScrollLayoutController.this.mView, 2);
            }
            if (onInterceptTouchEvent3 || onInterceptTouchEventScroll || onInterceptTouchEvent2 || onInterceptTouchEvent) {
                return true;
            }
            return false;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z;
            NotificationGuts exposedGuts = NotificationStackScrollLayoutController.this.mNotificationGutsManager.getExposedGuts();
            boolean z2 = motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1;
            NotificationStackScrollLayoutController.this.mView.handleEmptySpaceClick(motionEvent);
            boolean onTouchEvent = (exposedGuts == null || NotificationStackScrollLayoutController.this.mLongPressedView == null) ? false : NotificationStackScrollLayoutController.this.mSwipeHelper.onTouchEvent(motionEvent);
            boolean onlyScrollingInThisMotion = NotificationStackScrollLayoutController.this.mView.getOnlyScrollingInThisMotion();
            boolean isExpandingNotification = NotificationStackScrollLayoutController.this.mView.isExpandingNotification();
            if (NotificationStackScrollLayoutController.this.mLongPressedView != null || !NotificationStackScrollLayoutController.this.mView.getIsExpanded() || NotificationStackScrollLayoutController.this.mSwipeHelper.isSwiping() || onlyScrollingInThisMotion || exposedGuts != null) {
                z = false;
            } else {
                ExpandHelper expandHelper = NotificationStackScrollLayoutController.this.mView.getExpandHelper();
                if (z2) {
                    expandHelper.onlyObserveMovements(false);
                }
                z = expandHelper.onTouchEvent(motionEvent);
                boolean isExpandingNotification2 = NotificationStackScrollLayoutController.this.mView.isExpandingNotification();
                if (NotificationStackScrollLayoutController.this.mView.getExpandedInThisMotion() && !isExpandingNotification2 && isExpandingNotification && !NotificationStackScrollLayoutController.this.mView.getDisallowScrollingInThisMotion()) {
                    NotificationStackScrollLayoutController.this.mView.dispatchDownEventToScroller(motionEvent);
                }
                isExpandingNotification = isExpandingNotification2;
            }
            boolean onScrollTouch = (NotificationStackScrollLayoutController.this.mLongPressedView != null || !NotificationStackScrollLayoutController.this.mView.isExpanded() || NotificationStackScrollLayoutController.this.mSwipeHelper.isSwiping() || isExpandingNotification || NotificationStackScrollLayoutController.this.mView.getDisallowScrollingInThisMotion()) ? false : NotificationStackScrollLayoutController.this.mView.onScrollTouch(motionEvent);
            boolean onTouchEvent2 = (NotificationStackScrollLayoutController.this.mLongPressedView != null || NotificationStackScrollLayoutController.this.mView.isBeingDragged() || isExpandingNotification || NotificationStackScrollLayoutController.this.mView.getExpandedInThisMotion() || onlyScrollingInThisMotion || NotificationStackScrollLayoutController.this.mView.getDisallowDismissInThisMotion()) ? false : NotificationStackScrollLayoutController.this.mSwipeHelper.onTouchEvent(motionEvent);
            if (exposedGuts != null && !NotificationSwipeHelper.isTouchInView(motionEvent, exposedGuts) && (exposedGuts.getGutsContent() instanceof NotificationSnooze) && ((((NotificationSnooze) exposedGuts.getGutsContent()).isExpanded() && z2) || (!onTouchEvent2 && onScrollTouch))) {
                NotificationStackScrollLayoutController.this.checkSnoozeLeavebehind();
            }
            if (motionEvent.getActionMasked() == 1) {
                NotificationStackScrollLayoutController.this.mFalsingManager.isFalseTouch(11);
                NotificationStackScrollLayoutController.this.mView.setCheckForLeaveBehind(true);
            }
            traceJankOnTouchEvent(motionEvent.getActionMasked(), onScrollTouch);
            if (onTouchEvent2 || onScrollTouch || z || onTouchEvent) {
                return true;
            }
            return false;
        }

        public final void traceJankOnTouchEvent(int i, boolean z) {
            if (NotificationStackScrollLayoutController.this.mJankMonitor == null) {
                Log.w("StackScrollerController", "traceJankOnTouchEvent, mJankMonitor is null");
            } else if (i != 0) {
                if (i != 1) {
                    if (i == 3 && z) {
                        NotificationStackScrollLayoutController.this.mJankMonitor.cancel(2);
                    }
                } else if (z && !NotificationStackScrollLayoutController.this.mView.isFlingAfterUpEvent()) {
                    NotificationStackScrollLayoutController.this.mJankMonitor.end(2);
                }
            } else if (z) {
                NotificationStackScrollLayoutController.this.mJankMonitor.begin(NotificationStackScrollLayoutController.this.mView, 2);
            }
        }
    }

    public class NotifStackControllerImpl implements NotifStackController {
        public NotifStackControllerImpl() {
        }

        public void setNotifStats(NotifStats notifStats) {
            NotificationStackScrollLayoutController.this.mNotifStats = notifStats;
            NotificationStackScrollLayoutController.this.updateFooter();
            NotificationStackScrollLayoutController.this.updateShowEmptyShadeView();
        }
    }
}
