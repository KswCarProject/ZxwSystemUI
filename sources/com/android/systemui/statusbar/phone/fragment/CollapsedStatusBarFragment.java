package com.android.systemui.statusbar.phone.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.res.ColorStateList;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.android.systemui.Dependency;
import com.android.systemui.R$array;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.OperatorNameView;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.EncryptionHelper;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.SecureSettings;
import com.szchoiceway.view.StatusBarView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

@SuppressLint({"ValidFragment"})
public class CollapsedStatusBarFragment extends Fragment implements CommandQueue.Callbacks, StatusBarStateController.StateListener, SystemStatusAnimationCallback {
    public final SystemStatusAnimationScheduler mAnimationScheduler;
    public List<String> mBlockedIcons = new ArrayList();
    public final CarrierConfigTracker.CarrierConfigChangedListener mCarrierConfigCallback = new CarrierConfigTracker.CarrierConfigChangedListener() {
        public void onCarrierConfigChanged() {
            if (CollapsedStatusBarFragment.this.mOperatorNameViewController == null) {
                CollapsedStatusBarFragment.this.initOperatorName();
            }
        }
    };
    public final CarrierConfigTracker mCarrierConfigTracker;
    public View mClockView;
    public final CollapsedStatusBarFragmentLogger mCollapsedStatusBarFragmentLogger;
    public final CommandQueue mCommandQueue;
    public StatusBarIconController.DarkIconManager mDarkIconManager;
    public DarkIconDispatcher.DarkReceiver mDarkReceiver;
    public final CarrierConfigTracker.DefaultDataSubscriptionChangedListener mDefaultDataListener = new CarrierConfigTracker.DefaultDataSubscriptionChangedListener() {
        public void onDefaultSubscriptionChanged(int i) {
            if (CollapsedStatusBarFragment.this.mOperatorNameViewController == null) {
                CollapsedStatusBarFragment.this.initOperatorName();
            }
        }
    };
    public int mDisabled1;
    public int mDisabled2;
    public final FeatureFlags mFeatureFlags;
    public final KeyguardStateController mKeyguardStateController;
    public final StatusBarLocationPublisher mLocationPublisher;
    public final Executor mMainExecutor;
    public final NetworkController mNetworkController;
    public final NotificationIconAreaController mNotificationIconAreaController;
    public View mNotificationIconAreaInner;
    public final NotificationPanelViewController mNotificationPanelViewController;
    public View mOngoingCallChip;
    public final OngoingCallController mOngoingCallController;
    public final OngoingCallListener mOngoingCallListener = new OngoingCallListener() {
        public void onOngoingCallStateChanged(boolean z) {
            CollapsedStatusBarFragment collapsedStatusBarFragment = CollapsedStatusBarFragment.this;
            collapsedStatusBarFragment.disable(collapsedStatusBarFragment.getContext().getDisplayId(), CollapsedStatusBarFragment.this.mDisabled1, CollapsedStatusBarFragment.this.mDisabled2, z);
        }
    };
    public OperatorNameViewController mOperatorNameViewController;
    public final OperatorNameViewController.Factory mOperatorNameViewControllerFactory;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public final SecureSettings mSecureSettings;
    public SignalCallback mSignalCallback = new SignalCallback() {
        public void setIsAirplaneMode(IconState iconState) {
            CollapsedStatusBarFragment.this.mCommandQueue.recomputeDisableFlags(CollapsedStatusBarFragment.this.getContext().getDisplayId(), true);
        }
    };
    public PhoneStatusBarView mStatusBar;
    public StatusBarFragmentComponent mStatusBarFragmentComponent;
    public final StatusBarFragmentComponent.Factory mStatusBarFragmentComponentFactory;
    public final StatusBarHideIconsForBouncerManager mStatusBarHideIconsForBouncerManager;
    public final StatusBarIconController mStatusBarIconController;
    public View.OnLayoutChangeListener mStatusBarLayoutListener = new CollapsedStatusBarFragment$$ExternalSyntheticLambda6(this);
    public final StatusBarStateController mStatusBarStateController;
    public StatusBarView mStatusCustomer;
    public StatusBarSystemEventAnimator mSystemEventAnimator;
    public LinearLayout mSystemIconArea;
    public boolean mSystemIconAreaPendingToShow;
    public final ContentObserver mVolumeSettingObserver = new ContentObserver((Handler) null) {
        public void onChange(boolean z) {
            CollapsedStatusBarFragment.this.updateBlockedIcons();
        }
    };
    public boolean mbCustomerUI = false;
    public ImageButton mbtnExit;
    public ImageButton mbtnHome;
    public ImageButton mbtnTask;

    public void onStateChanged(int i) {
    }

    @SuppressLint({"ValidFragment"})
    public CollapsedStatusBarFragment(StatusBarFragmentComponent.Factory factory, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, PanelExpansionStateManager panelExpansionStateManager, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, KeyguardStateController keyguardStateController, NotificationPanelViewController notificationPanelViewController, NetworkController networkController, StatusBarStateController statusBarStateController, CommandQueue commandQueue, CarrierConfigTracker carrierConfigTracker, CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger, OperatorNameViewController.Factory factory2, SecureSettings secureSettings, Executor executor) {
        this.mStatusBarFragmentComponentFactory = factory;
        this.mOngoingCallController = ongoingCallController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mLocationPublisher = statusBarLocationPublisher;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mFeatureFlags = featureFlags;
        this.mStatusBarIconController = statusBarIconController;
        this.mStatusBarHideIconsForBouncerManager = statusBarHideIconsForBouncerManager;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mNetworkController = networkController;
        this.mStatusBarStateController = statusBarStateController;
        this.mCommandQueue = commandQueue;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mCollapsedStatusBarFragmentLogger = collapsedStatusBarFragmentLogger;
        this.mOperatorNameViewControllerFactory = factory2;
        this.mSecureSettings = secureSettings;
        this.mMainExecutor = executor;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R$layout.status_bar, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        Class cls = DarkIconDispatcher.class;
        super.onViewCreated(view, bundle);
        StatusBarFragmentComponent create = this.mStatusBarFragmentComponentFactory.create(this);
        this.mStatusBarFragmentComponent = create;
        create.init();
        PhoneStatusBarView phoneStatusBarView = (PhoneStatusBarView) view;
        this.mStatusBar = phoneStatusBarView;
        View findViewById = phoneStatusBarView.findViewById(R$id.status_bar_contents);
        findViewById.addOnLayoutChangeListener(this.mStatusBarLayoutListener);
        updateStatusBarLocation(findViewById.getLeft(), findViewById.getRight());
        if (bundle != null && bundle.containsKey("panel_state")) {
            this.mStatusBar.restoreHierarchyState(bundle.getSparseParcelableArray("panel_state"));
        }
        StatusBarIconController.DarkIconManager darkIconManager = new StatusBarIconController.DarkIconManager((LinearLayout) view.findViewById(R$id.statusIcons), this.mFeatureFlags);
        this.mDarkIconManager = darkIconManager;
        darkIconManager.setShouldLog(true);
        updateBlockedIcons();
        this.mStatusBarIconController.addIconGroup(this.mDarkIconManager);
        this.mSystemIconArea = (LinearLayout) this.mStatusBar.findViewById(R$id.system_icon_area);
        this.mClockView = this.mStatusBar.findViewById(R$id.clock);
        this.mOngoingCallChip = this.mStatusBar.findViewById(R$id.ongoing_call_chip);
        showSystemIconArea(false);
        showClock(false);
        initEmergencyCryptkeeperText();
        initOperatorName();
        initNotificationIconArea();
        this.mSystemEventAnimator = new StatusBarSystemEventAnimator(this.mSystemIconArea, getResources());
        this.mCarrierConfigTracker.addCallback(this.mCarrierConfigCallback);
        this.mCarrierConfigTracker.addDefaultDataSubscriptionChangedListener(this.mDefaultDataListener);
        StatusBarView statusBarView = (StatusBarView) this.mStatusBar.findViewById(R$id.statusCustomer);
        this.mStatusCustomer = statusBarView;
        if (statusBarView != null && statusBarView.isCustomerRes()) {
            this.mbCustomerUI = true;
            View findViewById2 = this.mStatusBar.findViewById(R$id.menu_contents);
            if (findViewById2 != null) {
                findViewById2.setVisibility(8);
            }
        }
        if (!this.mbCustomerUI || this.mStatusCustomer.getHomeView() == null) {
            this.mbtnHome = (ImageButton) this.mStatusBar.findViewById(R$id.btnHome);
        } else {
            this.mbtnHome = (ImageButton) this.mStatusCustomer.getHomeView();
        }
        ImageButton imageButton = this.mbtnHome;
        if (imageButton != null) {
            imageButton.setOnClickListener(new CollapsedStatusBarFragment$$ExternalSyntheticLambda1());
        }
        if (!this.mbCustomerUI || this.mStatusCustomer.getReturnView() == null) {
            this.mbtnExit = (ImageButton) this.mStatusBar.findViewById(R$id.btnExit);
        } else {
            this.mbtnExit = (ImageButton) this.mStatusCustomer.getReturnView();
        }
        ImageButton imageButton2 = this.mbtnExit;
        if (imageButton2 != null) {
            imageButton2.setOnClickListener(new CollapsedStatusBarFragment$$ExternalSyntheticLambda2());
        }
        if (!this.mbCustomerUI || this.mStatusCustomer.getRecentView() == null) {
            this.mbtnTask = (ImageButton) this.mStatusBar.findViewById(R$id.btnTask);
        } else {
            this.mbtnTask = (ImageButton) this.mStatusCustomer.getRecentView();
        }
        ImageButton imageButton3 = this.mbtnTask;
        if (imageButton3 != null) {
            imageButton3.setOnClickListener(new CollapsedStatusBarFragment$$ExternalSyntheticLambda3());
            this.mbtnTask.setLongClickable(true);
            this.mbtnTask.setOnLongClickListener(new CollapsedStatusBarFragment$$ExternalSyntheticLambda4());
        }
        this.mDarkReceiver = new CollapsedStatusBarFragment$$ExternalSyntheticLambda5(this);
        ((DarkIconDispatcher) Dependency.get(cls)).addDarkReceiver(this.mDarkReceiver);
        ((DarkIconDispatcher) Dependency.get(cls)).applyDark(this.mDarkReceiver);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$4(ArrayList arrayList, float f, int i) {
        ImageButton imageButton = this.mbtnHome;
        if (imageButton != null) {
            this.mbtnHome.setImageTintList(ColorStateList.valueOf(DarkIconDispatcher.getTint(arrayList, imageButton, i)));
        }
        ImageButton imageButton2 = this.mbtnExit;
        if (imageButton2 != null) {
            this.mbtnExit.setImageTintList(ColorStateList.valueOf(DarkIconDispatcher.getTint(arrayList, imageButton2, i)));
        }
        ImageButton imageButton3 = this.mbtnTask;
        if (imageButton3 != null) {
            this.mbtnTask.setImageTintList(ColorStateList.valueOf(DarkIconDispatcher.getTint(arrayList, imageButton3, i)));
        }
    }

    public void updateBlockedIcons() {
        this.mBlockedIcons.clear();
        List asList = Arrays.asList(getResources().getStringArray(R$array.config_collapsed_statusbar_icon_blocklist));
        String string = getString(17041587);
        boolean z = this.mSecureSettings.getIntForUser("status_bar_show_vibrate_icon", 0, -2) == 0;
        for (int i = 0; i < asList.size(); i++) {
            if (!((String) asList.get(i)).equals(string)) {
                this.mBlockedIcons.add((String) asList.get(i));
            } else if (z) {
                this.mBlockedIcons.add((String) asList.get(i));
            }
        }
        this.mMainExecutor.execute(new CollapsedStatusBarFragment$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBlockedIcons$5() {
        this.mDarkIconManager.setBlockList(this.mBlockedIcons);
    }

    public List<String> getBlockedIcons() {
        return this.mBlockedIcons;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        SparseArray sparseArray = new SparseArray();
        this.mStatusBar.saveHierarchyState(sparseArray);
        bundle.putSparseParcelableArray("panel_state", sparseArray);
    }

    public void onResume() {
        super.onResume();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.addCallback(this);
        initOngoingCallChip();
        this.mAnimationScheduler.addCallback((SystemStatusAnimationCallback) this);
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("status_bar_show_vibrate_icon"), false, this.mVolumeSettingObserver, -1);
    }

    public void onPause() {
        super.onPause();
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.removeCallback(this);
        this.mOngoingCallController.removeCallback(this.mOngoingCallListener);
        this.mAnimationScheduler.removeCallback((SystemStatusAnimationCallback) this);
        this.mSecureSettings.unregisterContentObserver(this.mVolumeSettingObserver);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mStatusBarIconController.removeIconGroup(this.mDarkIconManager);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            this.mNetworkController.removeCallback(this.mSignalCallback);
        }
        this.mCarrierConfigTracker.removeCallback(this.mCarrierConfigCallback);
        this.mCarrierConfigTracker.removeDataSubscriptionChangedListener(this.mDefaultDataListener);
    }

    public void initNotificationIconArea() {
        ViewGroup viewGroup = (ViewGroup) this.mStatusBar.findViewById(R$id.notification_icon_area);
        View notificationInnerAreaView = this.mNotificationIconAreaController.getNotificationInnerAreaView();
        this.mNotificationIconAreaInner = notificationInnerAreaView;
        if (notificationInnerAreaView.getParent() != null) {
            ((ViewGroup) this.mNotificationIconAreaInner.getParent()).removeView(this.mNotificationIconAreaInner);
        }
        viewGroup.addView(this.mNotificationIconAreaInner);
        updateNotificationIconAreaAndCallChip(this.mDisabled1, false);
    }

    public StatusBarFragmentComponent getStatusBarFragmentComponent() {
        return this.mStatusBarFragmentComponent;
    }

    public void disable(int i, int i2, int i3, boolean z) {
        if (i == getContext().getDisplayId()) {
            int adjustDisableFlags = adjustDisableFlags(i2);
            this.mCollapsedStatusBarFragmentLogger.logDisableFlagChange(new DisableFlagsLogger.DisableState(i2, i3), new DisableFlagsLogger.DisableState(adjustDisableFlags, i3));
            int i4 = this.mDisabled1 ^ adjustDisableFlags;
            int i5 = this.mDisabled2 ^ i3;
            this.mDisabled1 = adjustDisableFlags;
            this.mDisabled2 = i3;
            if (!((i4 & 1048576) == 0 && (i5 & 2) == 0)) {
                if ((adjustDisableFlags & 1048576) == 0 && (i3 & 2) == 0) {
                    showSystemIconArea(z);
                    showOperatorName(z);
                } else {
                    hideSystemIconArea(z);
                    hideOperatorName(z);
                }
            }
            if (!((67108864 & i4) == 0 && (131072 & i4) == 0)) {
                updateNotificationIconAreaAndCallChip(adjustDisableFlags, z);
            }
            if ((i4 & 8388608) != 0 || this.mClockView.getVisibility() != clockHiddenMode()) {
                if ((adjustDisableFlags & 8388608) != 0) {
                    hideClock(z);
                } else {
                    showClock(z);
                }
            }
        }
    }

    public int adjustDisableFlags(int i) {
        boolean shouldBeVisible = this.mStatusBarFragmentComponent.getHeadsUpAppearanceController().shouldBeVisible();
        if (shouldBeVisible) {
            i |= 8388608;
        }
        if (!this.mKeyguardStateController.isLaunchTransitionFadingAway() && !this.mKeyguardStateController.isKeyguardFadingAway() && shouldHideNotificationIcons() && (this.mStatusBarStateController.getState() != 1 || !shouldBeVisible)) {
            i = i | 131072 | 1048576 | 8388608;
        }
        NetworkController networkController = this.mNetworkController;
        if (networkController != null && EncryptionHelper.IS_DATA_ENCRYPTED) {
            if (networkController.hasEmergencyCryptKeeperText()) {
                i |= 131072;
            }
            if (!this.mNetworkController.isRadioOn()) {
                i |= 1048576;
            }
        }
        if (this.mStatusBarStateController.isDozing() && this.mNotificationPanelViewController.hasCustomClock()) {
            i |= 9437184;
        }
        return this.mOngoingCallController.hasOngoingCall() ? -67108865 & i : 67108864 | i;
    }

    public final void updateNotificationIconAreaAndCallChip(int i, boolean z) {
        boolean z2 = true;
        boolean z3 = (131072 & i) != 0;
        boolean z4 = (i & 67108864) == 0;
        if (z3 || z4) {
            hideNotificationIconArea(z);
        } else {
            showNotificationIconArea(z);
        }
        if (!z4 || z3) {
            z2 = false;
        }
        if (z2) {
            showOngoingCallChip(z);
        } else {
            hideOngoingCallChip(z);
        }
        this.mOngoingCallController.notifyChipVisibilityChanged(z2);
    }

    public final boolean shouldHideNotificationIcons() {
        if (this.mPanelExpansionStateManager.isClosed() || !this.mNotificationPanelViewController.hideStatusBarIconsWhenExpanded()) {
            return this.mStatusBarHideIconsForBouncerManager.getShouldHideStatusBarIconsForBouncer();
        }
        return true;
    }

    public final void hideSystemIconArea(boolean z) {
        this.mSystemIconAreaPendingToShow = false;
        animateHide(this.mSystemIconArea, z);
    }

    public final void showSystemIconArea(boolean z) {
        int animationState = this.mAnimationScheduler.getAnimationState();
        if (animationState == 0 || animationState == 5) {
            animateShow(this.mSystemIconArea, z);
        } else {
            this.mSystemIconAreaPendingToShow = true;
        }
    }

    public final void hideClock(boolean z) {
        animateHiddenState(this.mClockView, clockHiddenMode(), z);
    }

    public final void showClock(boolean z) {
        animateShow(this.mClockView, z);
    }

    public void hideOngoingCallChip(boolean z) {
        animateHiddenState(this.mOngoingCallChip, 8, z);
    }

    public void showOngoingCallChip(boolean z) {
        animateShow(this.mOngoingCallChip, z);
    }

    public final int clockHiddenMode() {
        return (this.mPanelExpansionStateManager.isClosed() || this.mKeyguardStateController.isShowing() || this.mStatusBarStateController.isDozing()) ? 8 : 4;
    }

    public void hideNotificationIconArea(boolean z) {
        animateHide(this.mNotificationIconAreaInner, z);
    }

    public void showNotificationIconArea(boolean z) {
        animateShow(this.mNotificationIconAreaInner, z);
    }

    public void hideOperatorName(boolean z) {
        OperatorNameViewController operatorNameViewController = this.mOperatorNameViewController;
        if (operatorNameViewController != null) {
            animateHide(operatorNameViewController.getView(), z);
        }
    }

    public void showOperatorName(boolean z) {
        OperatorNameViewController operatorNameViewController = this.mOperatorNameViewController;
        if (operatorNameViewController != null) {
            animateShow(operatorNameViewController.getView(), z);
        }
    }

    public final void animateHiddenState(View view, int i, boolean z) {
        view.animate().cancel();
        if (!z) {
            view.setAlpha(0.0f);
            view.setVisibility(i);
            return;
        }
        view.animate().alpha(0.0f).setDuration(160).setStartDelay(0).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(new CollapsedStatusBarFragment$$ExternalSyntheticLambda7(view, i));
    }

    public final void animateHide(View view, boolean z) {
        animateHiddenState(view, 4, z);
    }

    public final void animateShow(View view, boolean z) {
        view.animate().cancel();
        view.setVisibility(0);
        if (!z) {
            view.setAlpha(1.0f);
            return;
        }
        view.animate().alpha(1.0f).setDuration(320).setInterpolator(Interpolators.ALPHA_IN).setStartDelay(50).withEndAction((Runnable) null);
        if (this.mKeyguardStateController.isKeyguardFadingAway()) {
            view.animate().setDuration(this.mKeyguardStateController.getKeyguardFadingAwayDuration()).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).start();
        }
    }

    public final void initEmergencyCryptkeeperText() {
        View findViewById = this.mStatusBar.findViewById(R$id.emergency_cryptkeeper_text);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            if (findViewById != null) {
                ((ViewStub) findViewById).inflate();
            }
            this.mNetworkController.addCallback(this.mSignalCallback);
        } else if (findViewById != null) {
            ((ViewGroup) findViewById.getParent()).removeView(findViewById);
        }
    }

    public final void initOperatorName() {
        if (this.mCarrierConfigTracker.getShowOperatorNameInStatusBarConfig(SubscriptionManager.getDefaultDataSubscriptionId())) {
            OperatorNameViewController create = this.mOperatorNameViewControllerFactory.create((OperatorNameView) ((ViewStub) this.mStatusBar.findViewById(R$id.operator_name)).inflate());
            this.mOperatorNameViewController = create;
            create.init();
            if (this.mKeyguardStateController.isShowing()) {
                hideOperatorName(false);
            }
        }
    }

    public final void initOngoingCallChip() {
        this.mOngoingCallController.addCallback(this.mOngoingCallListener);
        this.mOngoingCallController.setChipView(this.mOngoingCallChip);
    }

    public void onDozingChanged(boolean z) {
        disable(getContext().getDisplayId(), this.mDisabled1, this.mDisabled2, false);
    }

    public Animator onSystemEventAnimationBegin() {
        return this.mSystemEventAnimator.onSystemEventAnimationBegin();
    }

    public Animator onSystemEventAnimationFinish(boolean z) {
        if (this.mSystemIconAreaPendingToShow) {
            this.mSystemIconAreaPendingToShow = false;
            animateShow(this.mSystemIconArea, false);
        }
        return this.mSystemEventAnimator.onSystemEventAnimationFinish(z);
    }

    public final void updateStatusBarLocation(int i, int i2) {
        this.mLocationPublisher.updateStatusBarMargin(i - this.mStatusBar.getLeft(), this.mStatusBar.getRight() - i2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i != i5 || i3 != i7) {
            updateStatusBarLocation(i, i3);
        }
    }
}
