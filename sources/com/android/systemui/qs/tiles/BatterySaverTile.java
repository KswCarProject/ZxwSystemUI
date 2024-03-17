package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.settings.SecureSettings;

public class BatterySaverTile extends QSTileImpl<QSTile.BooleanState> implements BatteryController.BatteryStateChangeCallback {
    public final BatteryController mBatteryController;
    public boolean mCharging;
    public QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302828);
    public int mLevel;
    public boolean mPluggedIn;
    public boolean mPowerSave;
    @VisibleForTesting
    public final SettingObserver mSetting;

    public int getMetricsCategory() {
        return 261;
    }

    public BatterySaverTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryController batteryController, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBatteryController = batteryController;
        batteryController.observe(getLifecycle(), this);
        SecureSettings secureSettings2 = secureSettings;
        this.mSetting = new SettingObserver(secureSettings2, this.mHandler, "low_power_warning_acknowledged", qSHost.getUserContext().getUserId()) {
            public void handleValueChanged(int i, boolean z) {
                BatterySaverTile.this.handleRefreshState((Object) null);
            }
        };
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleDestroy() {
        super.handleDestroy();
        this.mSetting.setListening(false);
    }

    public void handleUserSwitch(int i) {
        this.mSetting.setUserId(i);
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mSetting.setListening(z);
        if (!z) {
            this.mBatteryController.clearLastPowerSaverStartView();
        }
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.BATTERY_SAVER_SETTINGS");
    }

    public void handleClick(View view) {
        if (((QSTile.BooleanState) getState()).state != 0) {
            this.mBatteryController.setPowerSaveMode(!this.mPowerSave, view);
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.battery_detail_switch_title);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        boolean z = true;
        if (this.mPluggedIn) {
            i = 0;
        } else {
            i = this.mPowerSave ? 2 : 1;
        }
        booleanState.state = i;
        booleanState.icon = this.mIcon;
        String string = this.mContext.getString(R$string.battery_detail_switch_title);
        booleanState.label = string;
        booleanState.secondaryLabel = "";
        booleanState.contentDescription = string;
        booleanState.value = this.mPowerSave;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        if (this.mSetting.getValue() != 0) {
            z = false;
        }
        booleanState.showRippleEffect = z;
    }

    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        this.mLevel = i;
        this.mPluggedIn = z;
        this.mCharging = z2;
        refreshState(Integer.valueOf(i));
    }

    public void onPowerSaveChanged(boolean z) {
        this.mPowerSave = z;
        refreshState((Object) null);
    }
}
