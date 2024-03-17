package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.os.UserManager;
import android.provider.Settings;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.fragment.StatusBarIconBlocklistKt;
import com.android.systemui.statusbar.phone.fragment.StatusBarSystemEventAnimator;
import com.android.systemui.statusbar.phone.userswitcher.OnUserSwitcherPreferenceChangeListener;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class KeyguardStatusBarViewController extends ViewController<KeyguardStatusBarView> {
    public static final AnimationProperties KEYGUARD_HUN_PROPERTIES = new AnimationProperties().setDuration(360);
    public final SystemStatusAnimationCallback mAnimationCallback = new SystemStatusAnimationCallback() {
        public Animator onSystemEventAnimationFinish(boolean z) {
            return KeyguardStatusBarViewController.this.mSystemEventAnimator.onSystemEventAnimationFinish(z);
        }

        public Animator onSystemEventAnimationBegin() {
            return KeyguardStatusBarViewController.this.mSystemEventAnimator.onSystemEventAnimationBegin();
        }
    };
    public final SystemStatusAnimationScheduler mAnimationScheduler;
    public final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new KeyguardStatusBarViewController$$ExternalSyntheticLambda5(this);
    public final BatteryController mBatteryController;
    public boolean mBatteryListening;
    public final BatteryMeterViewController mBatteryMeterViewController;
    public final BatteryController.BatteryStateChangeCallback mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() {
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).onBatteryLevelChanged(z2);
        }
    };
    public final BiometricUnlockController mBiometricUnlockController;
    public final List<String> mBlockedIcons = new ArrayList();
    public final CarrierTextController mCarrierTextController;
    public final ConfigurationController mConfigurationController;
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onDensityOrFontScaleChanged() {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).loadDimens();
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            keyguardStatusBarViewController.mSystemEventAnimator = new StatusBarSystemEventAnimator(keyguardStatusBarViewController.mView, KeyguardStatusBarViewController.this.getResources());
        }

        public void onThemeChanged() {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).onOverlayChanged();
            KeyguardStatusBarViewController.this.onThemeChanged();
        }

        public void onConfigChanged(Configuration configuration) {
            KeyguardStatusBarViewController.this.updateUserSwitcher();
        }
    };
    public boolean mDelayShowingKeyguardStatusBar;
    public boolean mDozing;
    public float mExplicitAlpha = -1.0f;
    public final StatusBarUserSwitcherFeatureController mFeatureController;
    public boolean mFirstBypassAttempt;
    public final AnimatableProperty mHeadsUpShowingAmountAnimation = AnimatableProperty.from("KEYGUARD_HEADS_UP_SHOWING_AMOUNT", new KeyguardStatusBarViewController$$ExternalSyntheticLambda2(this), new KeyguardStatusBarViewController$$ExternalSyntheticLambda3(this), R$id.keyguard_hun_animator_tag, R$id.keyguard_hun_animator_end_tag, R$id.keyguard_hun_animator_start_tag);
    public final StatusBarContentInsetsProvider mInsetsProvider;
    public final KeyguardBypassController mKeyguardBypassController;
    public float mKeyguardHeadsUpShowingAmount = 0.0f;
    public final KeyguardStateController mKeyguardStateController;
    public float mKeyguardStatusBarAnimateAlpha = 1.0f;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            if (KeyguardStatusBarViewController.this.mFirstBypassAttempt && KeyguardStatusBarViewController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(z)) {
                KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = true;
            }
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            if (z) {
                KeyguardStatusBarViewController.this.updateUserSwitcher();
            }
        }

        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            boolean z2 = true;
            if (!(KeyguardStatusBarViewController.this.mStatusBarState == 1 || KeyguardStatusBarViewController.this.mStatusBarState == 2)) {
                z2 = false;
            }
            if (!z && KeyguardStatusBarViewController.this.mFirstBypassAttempt && z2 && !KeyguardStatusBarViewController.this.mDozing && !KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar && !KeyguardStatusBarViewController.this.mBiometricUnlockController.isBiometricUnlock()) {
                KeyguardStatusBarViewController.this.mFirstBypassAttempt = false;
                KeyguardStatusBarViewController.this.animateKeyguardStatusBarIn();
            }
        }

        public void onFinishedGoingToSleep(int i) {
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            keyguardStatusBarViewController.mFirstBypassAttempt = keyguardStatusBarViewController.mKeyguardBypassController.getBypassEnabled();
            KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = false;
        }
    };
    public final Object mLock = new Object();
    public final Executor mMainExecutor;
    public final NotificationPanelViewController.NotificationPanelViewStateProvider mNotificationPanelViewStateProvider;
    public final int mNotificationsHeaderCollideDistance;
    public final UserInfoController.OnUserInfoChangedListener mOnUserInfoChangedListener = new KeyguardStatusBarViewController$$ExternalSyntheticLambda4(this);
    public final SecureSettings mSecureSettings;
    public boolean mShowingKeyguardHeadsUp;
    public final StatusBarIconController mStatusBarIconController;
    public int mStatusBarState;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onStateChanged(int i) {
            KeyguardStatusBarViewController.this.mStatusBarState = i;
        }
    };
    public final StatusBarUserInfoTracker mStatusBarUserInfoTracker;
    public StatusBarSystemEventAnimator mSystemEventAnimator;
    public StatusBarIconController.TintedIconManager mTintedIconManager;
    public final StatusBarIconController.TintedIconManager.Factory mTintedIconManagerFactory;
    public final UserInfoController mUserInfoController;
    public final UserManager mUserManager;
    public final StatusBarUserSwitcherController mUserSwitcherController;
    public final ContentObserver mVolumeSettingObserver = new ContentObserver((Handler) null) {
        public void onChange(boolean z) {
            KeyguardStatusBarViewController.this.updateBlockedIcons();
        }
    };

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, Float f) {
        this.mKeyguardHeadsUpShowingAmount = f.floatValue();
        updateViewState();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Float lambda$new$1(View view) {
        return Float.valueOf(this.mKeyguardHeadsUpShowingAmount);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(String str, Drawable drawable, String str2) {
        ((KeyguardStatusBarView) this.mView).onUserInfoChanged(drawable);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(ValueAnimator valueAnimator) {
        this.mKeyguardStatusBarAnimateAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateViewState();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardStatusBarViewController(KeyguardStatusBarView keyguardStatusBarView, CarrierTextController carrierTextController, ConfigurationController configurationController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, BatteryController batteryController, UserInfoController userInfoController, StatusBarIconController statusBarIconController, StatusBarIconController.TintedIconManager.Factory factory, BatteryMeterViewController batteryMeterViewController, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, KeyguardUpdateMonitor keyguardUpdateMonitor, BiometricUnlockController biometricUnlockController, SysuiStatusBarStateController sysuiStatusBarStateController, StatusBarContentInsetsProvider statusBarContentInsetsProvider, UserManager userManager, StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, StatusBarUserSwitcherController statusBarUserSwitcherController, StatusBarUserInfoTracker statusBarUserInfoTracker, SecureSettings secureSettings, Executor executor) {
        super(keyguardStatusBarView);
        KeyguardStateController keyguardStateController2 = keyguardStateController;
        StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController2 = statusBarUserSwitcherFeatureController;
        this.mCarrierTextController = carrierTextController;
        this.mConfigurationController = configurationController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mBatteryController = batteryController;
        this.mUserInfoController = userInfoController;
        this.mStatusBarIconController = statusBarIconController;
        this.mTintedIconManagerFactory = factory;
        this.mBatteryMeterViewController = batteryMeterViewController;
        this.mNotificationPanelViewStateProvider = notificationPanelViewStateProvider;
        this.mKeyguardStateController = keyguardStateController2;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        this.mUserManager = userManager;
        this.mFeatureController = statusBarUserSwitcherFeatureController2;
        this.mUserSwitcherController = statusBarUserSwitcherController;
        this.mStatusBarUserInfoTracker = statusBarUserInfoTracker;
        this.mSecureSettings = secureSettings;
        this.mMainExecutor = executor;
        this.mFirstBypassAttempt = keyguardBypassController.getBypassEnabled();
        keyguardStateController2.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardFadingAwayChanged() {
                if (!KeyguardStatusBarViewController.this.mKeyguardStateController.isKeyguardFadingAway()) {
                    KeyguardStatusBarViewController.this.mFirstBypassAttempt = false;
                    KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = false;
                }
            }
        });
        Resources resources = getResources();
        updateBlockedIcons();
        this.mNotificationsHeaderCollideDistance = resources.getDimensionPixelSize(R$dimen.header_notifications_collide_distance);
        ((KeyguardStatusBarView) this.mView).setKeyguardUserAvatarEnabled(!statusBarUserSwitcherFeatureController.isStatusBarUserSwitcherFeatureEnabled());
        statusBarUserSwitcherFeatureController2.addCallback((OnUserSwitcherPreferenceChangeListener) new KeyguardStatusBarViewController$$ExternalSyntheticLambda6(this));
        this.mSystemEventAnimator = new StatusBarSystemEventAnimator(this.mView, resources);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(boolean z) {
        ((KeyguardStatusBarView) this.mView).setKeyguardUserAvatarEnabled(!z);
    }

    public void onInit() {
        super.onInit();
        this.mCarrierTextController.init();
        this.mBatteryMeterViewController.init();
        this.mUserSwitcherController.init();
    }

    public void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mAnimationScheduler.addCallback(this.mAnimationCallback);
        this.mUserInfoController.addCallback(this.mOnUserInfoChangedListener);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        if (this.mTintedIconManager == null) {
            StatusBarIconController.TintedIconManager create = this.mTintedIconManagerFactory.create((ViewGroup) ((KeyguardStatusBarView) this.mView).findViewById(R$id.statusIcons));
            this.mTintedIconManager = create;
            create.setBlockList(getBlockedIcons());
            this.mStatusBarIconController.addIconGroup(this.mTintedIconManager);
        }
        ((KeyguardStatusBarView) this.mView).setOnApplyWindowInsetsListener(new KeyguardStatusBarViewController$$ExternalSyntheticLambda1(this));
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("status_bar_show_vibrate_icon"), false, this.mVolumeSettingObserver, -1);
        updateUserSwitcher();
        onThemeChanged();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$onViewAttached$5(View view, WindowInsets windowInsets) {
        return ((KeyguardStatusBarView) this.mView).updateWindowInsets(windowInsets, this.mInsetsProvider);
    }

    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mAnimationScheduler.removeCallback(this.mAnimationCallback);
        this.mUserInfoController.removeCallback(this.mOnUserInfoChangedListener);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mSecureSettings.unregisterContentObserver(this.mVolumeSettingObserver);
        StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
        if (tintedIconManager != null) {
            this.mStatusBarIconController.removeIconGroup(tintedIconManager);
        }
    }

    public void onThemeChanged() {
        ((KeyguardStatusBarView) this.mView).onThemeChanged(this.mTintedIconManager);
    }

    public void setKeyguardUserSwitcherEnabled(boolean z) {
        ((KeyguardStatusBarView) this.mView).setKeyguardUserSwitcherEnabled(z);
        this.mStatusBarUserInfoTracker.checkEnabled();
    }

    public void setBatteryListening(boolean z) {
        if (z != this.mBatteryListening) {
            this.mBatteryListening = z;
            if (z) {
                this.mBatteryController.addCallback(this.mBatteryStateChangeCallback);
            } else {
                this.mBatteryController.removeCallback(this.mBatteryStateChangeCallback);
            }
        }
    }

    public void setNoTopClipping() {
        ((KeyguardStatusBarView) this.mView).setTopClipping(0);
    }

    public void updateTopClipping(int i) {
        T t = this.mView;
        ((KeyguardStatusBarView) t).setTopClipping(i - ((KeyguardStatusBarView) t).getTop());
    }

    public void setDozing(boolean z) {
        this.mDozing = z;
    }

    public void animateKeyguardStatusBarIn() {
        ((KeyguardStatusBarView) this.mView).setVisibility(0);
        ((KeyguardStatusBarView) this.mView).setAlpha(0.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.addUpdateListener(this.mAnimatorUpdateListener);
        ofFloat.setDuration(360);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.start();
    }

    public void animateKeyguardStatusBarOut(long j, long j2) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{((KeyguardStatusBarView) this.mView).getAlpha(), 0.0f});
        ofFloat.addUpdateListener(this.mAnimatorUpdateListener);
        ofFloat.setStartDelay(j);
        ofFloat.setDuration(j2);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).setVisibility(4);
                ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).setAlpha(1.0f);
                KeyguardStatusBarViewController.this.mKeyguardStatusBarAnimateAlpha = 1.0f;
            }
        });
        ofFloat.start();
    }

    public void updateViewState() {
        if (isKeyguardShowing()) {
            float min = 1.0f - Math.min(1.0f, this.mNotificationPanelViewStateProvider.getLockscreenShadeDragProgress() * 2.0f);
            float f = this.mExplicitAlpha;
            if (f == -1.0f) {
                f = Math.min(getKeyguardContentsAlpha(), min) * this.mKeyguardStatusBarAnimateAlpha * (1.0f - this.mKeyguardHeadsUpShowingAmount);
            }
            int i = 0;
            boolean z = (this.mFirstBypassAttempt && this.mKeyguardUpdateMonitor.shouldListenForFace()) || this.mDelayShowingKeyguardStatusBar;
            if (f == 0.0f || this.mDozing || z) {
                i = 4;
            }
            updateViewState(f, i);
        }
    }

    public void updateViewState(float f, int i) {
        ((KeyguardStatusBarView) this.mView).setAlpha(f);
        ((KeyguardStatusBarView) this.mView).setVisibility(i);
    }

    public final float getKeyguardContentsAlpha() {
        float f;
        float f2;
        if (isKeyguardShowing()) {
            f2 = this.mNotificationPanelViewStateProvider.getPanelViewExpandedHeight();
            f = (float) (((KeyguardStatusBarView) this.mView).getHeight() + this.mNotificationsHeaderCollideDistance);
        } else {
            f2 = this.mNotificationPanelViewStateProvider.getPanelViewExpandedHeight();
            f = (float) ((KeyguardStatusBarView) this.mView).getHeight();
        }
        return (float) Math.pow((double) MathUtils.saturate(f2 / f), 0.75d);
    }

    public final void updateUserSwitcher() {
        ((KeyguardStatusBarView) this.mView).setUserSwitcherEnabled(this.mUserManager.isUserSwitcherEnabled(getResources().getBoolean(R$bool.qs_show_user_switcher_for_single_user)));
    }

    public void updateBlockedIcons() {
        List<String> statusBarIconBlocklist = StatusBarIconBlocklistKt.getStatusBarIconBlocklist(getResources(), this.mSecureSettings);
        synchronized (this.mLock) {
            this.mBlockedIcons.clear();
            this.mBlockedIcons.addAll(statusBarIconBlocklist);
        }
        this.mMainExecutor.execute(new KeyguardStatusBarViewController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBlockedIcons$6() {
        StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
        if (tintedIconManager != null) {
            tintedIconManager.setBlockList(getBlockedIcons());
        }
    }

    public List<String> getBlockedIcons() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mBlockedIcons);
        }
        return arrayList;
    }

    public void updateForHeadsUp() {
        updateForHeadsUp(true);
    }

    public void updateForHeadsUp(boolean z) {
        boolean z2 = isKeyguardShowing() && this.mNotificationPanelViewStateProvider.shouldHeadsUpBeVisible();
        if (this.mShowingKeyguardHeadsUp != z2) {
            this.mShowingKeyguardHeadsUp = z2;
            float f = 0.0f;
            if (isKeyguardShowing()) {
                KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) this.mView;
                AnimatableProperty animatableProperty = this.mHeadsUpShowingAmountAnimation;
                if (z2) {
                    f = 1.0f;
                }
                PropertyAnimator.setProperty(keyguardStatusBarView, animatableProperty, f, KEYGUARD_HUN_PROPERTIES, z);
                return;
            }
            PropertyAnimator.applyImmediately((KeyguardStatusBarView) this.mView, this.mHeadsUpShowingAmountAnimation, 0.0f);
        }
    }

    public final boolean isKeyguardShowing() {
        return this.mStatusBarState == 1;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardStatusBarView:");
        printWriter.println("  mBatteryListening: " + this.mBatteryListening);
        printWriter.println("  mExplicitAlpha: " + this.mExplicitAlpha);
        ((KeyguardStatusBarView) this.mView).dump(printWriter, strArr);
    }

    public void setAlpha(float f) {
        this.mExplicitAlpha = f;
        updateViewState();
    }
}
