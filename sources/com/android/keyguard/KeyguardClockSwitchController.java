package com.android.keyguard;

import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.keyguard.clock.ClockManager;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executor;

public class KeyguardClockSwitchController extends ViewController<KeyguardClockSwitch> implements Dumpable {
    public final BatteryController mBatteryController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public boolean mCanShowDoubleLineClock = true;
    public final ClockManager.ClockChangedListener mClockChangedListener = new KeyguardClockSwitchController$$ExternalSyntheticLambda1(this);
    public FrameLayout mClockFrame;
    public final ClockManager mClockManager;
    public AnimatableClockController mClockViewController;
    public final SysuiColorExtractor mColorExtractor;
    public final ColorExtractor.OnColorsChangedListener mColorsListener = new KeyguardClockSwitchController$$ExternalSyntheticLambda0(this);
    public int mCurrentClockSize = 1;
    public ContentObserver mDoubleLineClockObserver = new ContentObserver((Handler) null) {
        public void onChange(boolean z) {
            KeyguardClockSwitchController.this.updateDoubleLineClock();
        }
    };
    public final DumpManager mDumpManager;
    public int mKeyguardClockTopMargin = 0;
    public final KeyguardSliceViewController mKeyguardSliceViewController;
    public final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    public final KeyguardUnlockAnimationController.KeyguardUnlockAnimationListener mKeyguardUnlockAnimationListener = new KeyguardUnlockAnimationController.KeyguardUnlockAnimationListener() {
        public void onUnlockAnimationFinished() {
            KeyguardClockSwitchController.this.setClipChildrenForUnlock(true);
        }
    };
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public FrameLayout mLargeClockFrame;
    public AnimatableClockController mLargeClockViewController;
    public final NotificationIconAreaController mNotificationIconAreaController;
    public boolean mOnlyClock = false;
    public final Resources mResources;
    public final SecureSettings mSecureSettings;
    public final LockscreenSmartspaceController mSmartspaceController;
    public View mSmartspaceView;
    public ViewGroup mStatusArea;
    public final StatusBarStateController mStatusBarStateController;
    public Executor mUiExecutor;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ColorExtractor colorExtractor, int i) {
        if ((i & 2) != 0) {
            ((KeyguardClockSwitch) this.mView).updateColors(getGradientColors());
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardClockSwitchController(KeyguardClockSwitch keyguardClockSwitch, StatusBarStateController statusBarStateController, SysuiColorExtractor sysuiColorExtractor, ClockManager clockManager, KeyguardSliceViewController keyguardSliceViewController, NotificationIconAreaController notificationIconAreaController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, LockscreenSmartspaceController lockscreenSmartspaceController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SecureSettings secureSettings, Executor executor, Resources resources, DumpManager dumpManager) {
        super(keyguardClockSwitch);
        this.mStatusBarStateController = statusBarStateController;
        this.mColorExtractor = sysuiColorExtractor;
        this.mClockManager = clockManager;
        this.mKeyguardSliceViewController = keyguardSliceViewController;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBatteryController = batteryController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mSmartspaceController = lockscreenSmartspaceController;
        this.mResources = resources;
        this.mSecureSettings = secureSettings;
        this.mUiExecutor = executor;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mDumpManager = dumpManager;
    }

    public void setOnlyClock(boolean z) {
        this.mOnlyClock = z;
    }

    public void onInit() {
        this.mKeyguardSliceViewController.init();
        this.mClockFrame = (FrameLayout) ((KeyguardClockSwitch) this.mView).findViewById(R$id.lockscreen_clock_view);
        this.mLargeClockFrame = (FrameLayout) ((KeyguardClockSwitch) this.mView).findViewById(R$id.lockscreen_clock_view_large);
        AnimatableClockController animatableClockController = new AnimatableClockController((AnimatableClockView) ((KeyguardClockSwitch) this.mView).findViewById(R$id.animatable_clock_view), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor, this.mResources);
        this.mClockViewController = animatableClockController;
        animatableClockController.init();
        AnimatableClockController animatableClockController2 = new AnimatableClockController((AnimatableClockView) ((KeyguardClockSwitch) this.mView).findViewById(R$id.animatable_clock_view_large), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor, this.mResources);
        this.mLargeClockViewController = animatableClockController2;
        animatableClockController2.init();
        this.mDumpManager.unregisterDumpable(getClass().toString());
        this.mDumpManager.registerDumpable(getClass().toString(), this);
    }

    public void onViewAttached() {
        this.mClockManager.addOnClockChangedListener(this.mClockChangedListener);
        this.mColorExtractor.addOnColorsChangedListener(this.mColorsListener);
        ((KeyguardClockSwitch) this.mView).updateColors(getGradientColors());
        this.mKeyguardClockTopMargin = ((KeyguardClockSwitch) this.mView).getResources().getDimensionPixelSize(R$dimen.keyguard_clock_top_margin);
        if (this.mOnlyClock) {
            ((KeyguardClockSwitch) this.mView).findViewById(R$id.keyguard_slice_view).setVisibility(8);
            ((KeyguardClockSwitch) this.mView).findViewById(R$id.left_aligned_notification_icon_container).setVisibility(8);
            return;
        }
        updateAodIcons();
        this.mStatusArea = (ViewGroup) ((KeyguardClockSwitch) this.mView).findViewById(R$id.keyguard_status_area);
        if (this.mSmartspaceController.isEnabled()) {
            View findViewById = ((KeyguardClockSwitch) this.mView).findViewById(R$id.keyguard_slice_view);
            int indexOfChild = this.mStatusArea.indexOfChild(findViewById);
            findViewById.setVisibility(8);
            addSmartspaceView(indexOfChild);
            updateClockLayout();
        }
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("lockscreen_use_double_line_clock"), false, this.mDoubleLineClockObserver, -1);
        updateDoubleLineClock();
        this.mKeyguardUnlockAnimationController.addKeyguardUnlockAnimationListener(this.mKeyguardUnlockAnimationListener);
    }

    public int getNotificationIconAreaHeight() {
        return this.mNotificationIconAreaController.getHeight();
    }

    public void onViewDetached() {
        this.mClockManager.removeOnClockChangedListener(this.mClockChangedListener);
        this.mColorExtractor.removeOnColorsChangedListener(this.mColorsListener);
        ((KeyguardClockSwitch) this.mView).setClockPlugin((ClockPlugin) null, this.mStatusBarStateController.getState());
        this.mSecureSettings.unregisterContentObserver(this.mDoubleLineClockObserver);
        this.mKeyguardUnlockAnimationController.removeKeyguardUnlockAnimationListener(this.mKeyguardUnlockAnimationListener);
    }

    public void onLocaleListChanged() {
        int indexOfChild;
        if (this.mSmartspaceController.isEnabled() && (indexOfChild = this.mStatusArea.indexOfChild(this.mSmartspaceView)) >= 0) {
            this.mStatusArea.removeView(this.mSmartspaceView);
            addSmartspaceView(indexOfChild);
        }
    }

    public final void addSmartspaceView(int i) {
        this.mSmartspaceView = this.mSmartspaceController.buildAndConnectView((ViewGroup) this.mView);
        this.mStatusArea.addView(this.mSmartspaceView, i, new LinearLayout.LayoutParams(-1, -2));
        this.mSmartspaceView.setPaddingRelative(getContext().getResources().getDimensionPixelSize(R$dimen.below_clock_padding_start), 0, getContext().getResources().getDimensionPixelSize(R$dimen.below_clock_padding_end), 0);
        this.mKeyguardUnlockAnimationController.setLockscreenSmartspace(this.mSmartspaceView);
    }

    public void onDensityOrFontScaleChanged() {
        ((KeyguardClockSwitch) this.mView).onDensityOrFontScaleChanged();
        this.mKeyguardClockTopMargin = ((KeyguardClockSwitch) this.mView).getResources().getDimensionPixelSize(R$dimen.keyguard_clock_top_margin);
        updateClockLayout();
    }

    public final void updateClockLayout() {
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.keyguard_large_clock_top_margin) - ((int) this.mLargeClockViewController.getBottom());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.topMargin = dimensionPixelSize;
        this.mLargeClockFrame.setLayoutParams(layoutParams);
    }

    public void displayClock(int i, boolean z) {
        if (this.mCanShowDoubleLineClock || i != 0) {
            this.mCurrentClockSize = i;
            boolean switchToClock = ((KeyguardClockSwitch) this.mView).switchToClock(i, z);
            if (z && switchToClock && i == 0) {
                this.mLargeClockViewController.animateAppear();
            }
        }
    }

    public void animateFoldToAod() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.animateFoldAppear();
            this.mLargeClockViewController.animateFoldAppear();
        }
    }

    public boolean hasCustomClock() {
        return ((KeyguardClockSwitch) this.mView).hasCustomClock();
    }

    public float getClockTextSize() {
        return ((KeyguardClockSwitch) this.mView).getTextSize();
    }

    public void refresh() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.refreshTime();
            this.mLargeClockViewController.refreshTime();
        }
        LockscreenSmartspaceController lockscreenSmartspaceController = this.mSmartspaceController;
        if (lockscreenSmartspaceController != null) {
            lockscreenSmartspaceController.requestSmartspaceUpdate();
        }
        ((KeyguardClockSwitch) this.mView).refresh();
    }

    public void updatePosition(int i, float f, AnimationProperties animationProperties, boolean z) {
        if (getCurrentLayoutDirection() == 1) {
            i = -i;
        }
        FrameLayout frameLayout = this.mClockFrame;
        AnimatableProperty animatableProperty = AnimatableProperty.TRANSLATION_X;
        float f2 = (float) i;
        PropertyAnimator.setProperty(frameLayout, animatableProperty, f2, animationProperties, z);
        PropertyAnimator.setProperty(this.mLargeClockFrame, AnimatableProperty.SCALE_X, f, animationProperties, z);
        PropertyAnimator.setProperty(this.mLargeClockFrame, AnimatableProperty.SCALE_Y, f, animationProperties, z);
        ViewGroup viewGroup = this.mStatusArea;
        if (viewGroup != null) {
            PropertyAnimator.setProperty(viewGroup, animatableProperty, f2, animationProperties, z);
        }
    }

    public void updateTimeZone(TimeZone timeZone) {
        ((KeyguardClockSwitch) this.mView).onTimeZoneChanged(timeZone);
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.onTimeZoneChanged(timeZone);
            this.mLargeClockViewController.onTimeZoneChanged(timeZone);
        }
    }

    public void refreshFormat() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.refreshFormat();
            this.mLargeClockViewController.refreshFormat();
        }
    }

    public int getClockBottom(int i) {
        if (this.mLargeClockFrame.getVisibility() != 0) {
            return this.mClockFrame.findViewById(R$id.animatable_clock_view).getHeight() + i + this.mKeyguardClockTopMargin;
        }
        View findViewById = this.mLargeClockFrame.findViewById(R$id.animatable_clock_view_large);
        return (this.mLargeClockFrame.getHeight() / 2) + (findViewById.getHeight() / 2);
    }

    public boolean isClockTopAligned() {
        return this.mLargeClockFrame.getVisibility() != 0;
    }

    public final void updateAodIcons() {
        this.mNotificationIconAreaController.setupAodIcons((NotificationIconContainer) ((KeyguardClockSwitch) this.mView).findViewById(R$id.left_aligned_notification_icon_container));
    }

    public final void setClockPlugin(ClockPlugin clockPlugin) {
        ((KeyguardClockSwitch) this.mView).setClockPlugin(clockPlugin, this.mStatusBarStateController.getState());
    }

    public final ColorExtractor.GradientColors getGradientColors() {
        return this.mColorExtractor.getColors(2);
    }

    public final int getCurrentLayoutDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
    }

    public final void updateDoubleLineClock() {
        boolean z = true;
        if (this.mSecureSettings.getIntForUser("lockscreen_use_double_line_clock", 1, -2) == 0) {
            z = false;
        }
        this.mCanShowDoubleLineClock = z;
        if (!z) {
            this.mUiExecutor.execute(new KeyguardClockSwitchController$$ExternalSyntheticLambda2(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDoubleLineClock$1() {
        displayClock(1, true);
    }

    public final void setClipChildrenForUnlock(boolean z) {
        ((KeyguardClockSwitch) this.mView).setClipChildren(z);
        ViewGroup viewGroup = this.mStatusArea;
        if (viewGroup != null) {
            viewGroup.setClipChildren(z);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("currentClockSizeLarge=");
        sb.append(this.mCurrentClockSize == 0);
        printWriter.println(sb.toString());
        printWriter.println("mCanShowDoubleLineClock=" + this.mCanShowDoubleLineClock);
        this.mClockViewController.dump(printWriter);
        this.mLargeClockViewController.dump(printWriter);
    }
}
