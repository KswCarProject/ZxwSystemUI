package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;

public class KeyguardMessageAreaController extends ViewController<KeyguardMessageArea> {
    public final ConfigurationController mConfigurationController;
    public ConfigurationController.ConfigurationListener mConfigurationListener;
    public KeyguardUpdateMonitorCallback mInfoCallback;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    public KeyguardMessageAreaController(KeyguardMessageArea keyguardMessageArea, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController) {
        super(keyguardMessageArea);
        this.mInfoCallback = new KeyguardUpdateMonitorCallback() {
            public void onFinishedGoingToSleep(int i) {
                ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).setSelected(false);
            }

            public void onStartedWakingUp() {
                ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).setSelected(true);
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).onConfigChanged();
            }

            public void onThemeChanged() {
                ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).onThemeChanged();
            }

            public void onDensityOrFontScaleChanged() {
                ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).onDensityOrFontScaleChanged();
            }
        };
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mConfigurationController = configurationController;
    }

    public void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        ((KeyguardMessageArea) this.mView).setSelected(this.mKeyguardUpdateMonitor.isDeviceInteractive());
        ((KeyguardMessageArea) this.mView).onThemeChanged();
    }

    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
    }

    public void setAltBouncerShowing(boolean z) {
        ((KeyguardMessageArea) this.mView).setAltBouncerShowing(z);
    }

    public void setBouncerShowing(boolean z) {
        ((KeyguardMessageArea) this.mView).setBouncerShowing(z);
    }

    public void setMessage(CharSequence charSequence) {
        ((KeyguardMessageArea) this.mView).setMessage(charSequence);
    }

    public void setMessage(int i) {
        ((KeyguardMessageArea) this.mView).setMessage(i);
    }

    public void setNextMessageColor(ColorStateList colorStateList) {
        ((KeyguardMessageArea) this.mView).setNextMessageColor(colorStateList);
    }

    public void reloadColors() {
        ((KeyguardMessageArea) this.mView).reloadColor();
    }

    public static class Factory {
        public final ConfigurationController mConfigurationController;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

        public Factory(KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController) {
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mConfigurationController = configurationController;
        }

        public KeyguardMessageAreaController create(KeyguardMessageArea keyguardMessageArea) {
            return new KeyguardMessageAreaController(keyguardMessageArea, this.mKeyguardUpdateMonitor, this.mConfigurationController);
        }
    }
}
