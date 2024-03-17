package com.android.systemui.battery;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
import java.util.Objects;

public class BatteryMeterViewController extends ViewController<BatteryMeterView> {
    public final BatteryController mBatteryController;
    public final BatteryController.BatteryStateChangeCallback mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() {
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).onBatteryLevelChanged(i, z);
        }

        public void onPowerSaveChanged(boolean z) {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).onPowerSaveChanged(z);
        }

        public void onBatteryUnknownStateChanged(boolean z) {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).onBatteryUnknownStateChanged(z);
        }
    };
    public final ConfigurationController mConfigurationController;
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onDensityOrFontScaleChanged() {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).scaleBatteryMeterViews();
        }
    };
    public final ContentResolver mContentResolver;
    public final CurrentUserTracker mCurrentUserTracker;
    public boolean mIgnoreTunerUpdates;
    public boolean mIsSubscribedForTunerUpdates;
    public final SettingObserver mSettingObserver;
    public final String mSlotBattery;
    public final TunerService.Tunable mTunable = new TunerService.Tunable() {
        public void onTuningChanged(String str, String str2) {
            if ("icon_blacklist".equals(str)) {
                ((BatteryMeterView) BatteryMeterViewController.this.mView).setVisibility(StatusBarIconController.getIconHideList(BatteryMeterViewController.this.getContext(), str2).contains(BatteryMeterViewController.this.mSlotBattery) ? 8 : 0);
            }
        }
    };
    public final TunerService mTunerService;

    public BatteryMeterViewController(BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, Handler handler, final ContentResolver contentResolver, BatteryController batteryController) {
        super(batteryMeterView);
        this.mConfigurationController = configurationController;
        this.mTunerService = tunerService;
        this.mContentResolver = contentResolver;
        this.mBatteryController = batteryController;
        Objects.requireNonNull(batteryController);
        ((BatteryMeterView) this.mView).setBatteryEstimateFetcher(new BatteryMeterViewController$$ExternalSyntheticLambda0(batteryController));
        this.mSlotBattery = getResources().getString(17041557);
        this.mSettingObserver = new SettingObserver(handler);
        this.mCurrentUserTracker = new CurrentUserTracker(broadcastDispatcher) {
            public void onUserSwitched(int i) {
                contentResolver.unregisterContentObserver(BatteryMeterViewController.this.mSettingObserver);
                BatteryMeterViewController.this.registerShowBatteryPercentObserver(i);
                ((BatteryMeterView) BatteryMeterViewController.this.mView).updateShowPercent();
            }
        };
    }

    public void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        subscribeForTunerUpdates();
        this.mBatteryController.addCallback(this.mBatteryStateChangeCallback);
        registerShowBatteryPercentObserver(ActivityManager.getCurrentUser());
        registerGlobalBatteryUpdateObserver();
        this.mCurrentUserTracker.startTracking();
        ((BatteryMeterView) this.mView).updateShowPercent();
    }

    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        unsubscribeFromTunerUpdates();
        this.mBatteryController.removeCallback(this.mBatteryStateChangeCallback);
        this.mCurrentUserTracker.stopTracking();
        this.mContentResolver.unregisterContentObserver(this.mSettingObserver);
    }

    public void ignoreTunerUpdates() {
        this.mIgnoreTunerUpdates = true;
        unsubscribeFromTunerUpdates();
    }

    public final void subscribeForTunerUpdates() {
        if (!this.mIsSubscribedForTunerUpdates && !this.mIgnoreTunerUpdates) {
            this.mTunerService.addTunable(this.mTunable, "icon_blacklist");
            this.mIsSubscribedForTunerUpdates = true;
        }
    }

    public final void unsubscribeFromTunerUpdates() {
        if (this.mIsSubscribedForTunerUpdates) {
            this.mTunerService.removeTunable(this.mTunable);
            this.mIsSubscribedForTunerUpdates = false;
        }
    }

    public final void registerShowBatteryPercentObserver(int i) {
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, this.mSettingObserver, i);
    }

    public final void registerGlobalBatteryUpdateObserver() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("battery_estimates_last_update_time"), false, this.mSettingObserver);
    }

    public final class SettingObserver extends ContentObserver {
        public SettingObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            ((BatteryMeterView) BatteryMeterViewController.this.mView).updateShowPercent();
            if (TextUtils.equals(uri.getLastPathSegment(), "battery_estimates_last_update_time")) {
                ((BatteryMeterView) BatteryMeterViewController.this.mView).updatePercentText();
            }
        }
    }
}
