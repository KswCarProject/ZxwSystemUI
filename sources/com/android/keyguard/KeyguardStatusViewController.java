package com.android.keyguard;

import android.graphics.Rect;
import android.util.Slog;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.util.TimeZone;

public class KeyguardStatusViewController extends ViewController<KeyguardStatusView> {
    public static final AnimationProperties CLOCK_ANIMATION_PROPERTIES = new AnimationProperties().setDuration(360);
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final Rect mClipBounds = new Rect();
    public final ConfigurationController mConfigurationController;
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onLocaleListChanged() {
            KeyguardStatusViewController.this.refreshTime();
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.onLocaleListChanged();
        }

        public void onDensityOrFontScaleChanged() {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.onDensityOrFontScaleChanged();
        }
    };
    public KeyguardUpdateMonitorCallback mInfoCallback = new KeyguardUpdateMonitorCallback() {
        public void onTimeChanged() {
            KeyguardStatusViewController.this.refreshTime();
        }

        public void onTimeFormatChanged(String str) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.refreshFormat();
        }

        public void onTimeZoneChanged(TimeZone timeZone) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.updateTimeZone(timeZone);
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            if (z) {
                if (KeyguardStatusViewController.DEBUG) {
                    Slog.v("KeyguardStatusViewController", "refresh statusview showing:" + z);
                }
                KeyguardStatusViewController.this.refreshTime();
            }
        }

        public void onUserSwitchComplete(int i) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.refreshFormat();
        }
    };
    public final KeyguardClockSwitchController mKeyguardClockSwitchController;
    public final KeyguardSliceViewController mKeyguardSliceViewController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardVisibilityHelper mKeyguardVisibilityHelper;

    public KeyguardStatusViewController(KeyguardStatusView keyguardStatusView, KeyguardSliceViewController keyguardSliceViewController, KeyguardClockSwitchController keyguardClockSwitchController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController) {
        super(keyguardStatusView);
        this.mKeyguardSliceViewController = keyguardSliceViewController;
        this.mKeyguardClockSwitchController = keyguardClockSwitchController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mConfigurationController = configurationController;
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(this.mView, keyguardStateController, dozeParameters, screenOffAnimationController, true);
    }

    public void onInit() {
        this.mKeyguardClockSwitchController.init();
    }

    public void onViewAttached() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    public void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public void dozeTimeTick() {
        refreshTime();
        this.mKeyguardSliceViewController.refresh();
    }

    public void setDarkAmount(float f) {
        ((KeyguardStatusView) this.mView).setDarkAmount(f);
    }

    public void displayClock(int i, boolean z) {
        this.mKeyguardClockSwitchController.displayClock(i, z);
    }

    public void animateFoldToAod() {
        this.mKeyguardClockSwitchController.animateFoldToAod();
    }

    public boolean hasCustomClock() {
        return this.mKeyguardClockSwitchController.hasCustomClock();
    }

    public void setTranslationYExcludingMedia(float f) {
        ((KeyguardStatusView) this.mView).setChildrenTranslationYExcludingMediaView(f);
    }

    public void setAlpha(float f) {
        if (!this.mKeyguardVisibilityHelper.isVisibilityAnimating()) {
            ((KeyguardStatusView) this.mView).setAlpha(f);
        }
    }

    public void setPivotX(float f) {
        ((KeyguardStatusView) this.mView).setPivotX(f);
    }

    public void setPivotY(float f) {
        ((KeyguardStatusView) this.mView).setPivotY(f);
    }

    public float getClockTextSize() {
        return this.mKeyguardClockSwitchController.getClockTextSize();
    }

    public int getLockscreenHeight() {
        return ((KeyguardStatusView) this.mView).getHeight() - this.mKeyguardClockSwitchController.getNotificationIconAreaHeight();
    }

    public int getClockBottom(int i) {
        return this.mKeyguardClockSwitchController.getClockBottom(i);
    }

    public boolean isClockTopAligned() {
        return this.mKeyguardClockSwitchController.isClockTopAligned();
    }

    public void setStatusAccessibilityImportance(int i) {
        ((KeyguardStatusView) this.mView).setImportantForAccessibility(i);
    }

    public void updatePosition(int i, int i2, float f, boolean z) {
        AnimationProperties animationProperties = CLOCK_ANIMATION_PROPERTIES;
        PropertyAnimator.setProperty((KeyguardStatusView) this.mView, AnimatableProperty.Y, (float) i2, animationProperties, z);
        this.mKeyguardClockSwitchController.updatePosition(i, f, animationProperties, z);
    }

    public void setKeyguardStatusViewVisibility(int i, boolean z, boolean z2, int i2) {
        this.mKeyguardVisibilityHelper.setViewVisibility(i, z, z2, i2);
    }

    public final void refreshTime() {
        this.mKeyguardClockSwitchController.refresh();
    }

    public void setClipBounds(Rect rect) {
        if (rect != null) {
            this.mClipBounds.set(rect.left, (int) (((float) rect.top) - ((KeyguardStatusView) this.mView).getY()), rect.right, (int) (((float) rect.bottom) - ((KeyguardStatusView) this.mView).getY()));
            ((KeyguardStatusView) this.mView).setClipBounds(this.mClipBounds);
            return;
        }
        ((KeyguardStatusView) this.mView).setClipBounds((Rect) null);
    }
}
