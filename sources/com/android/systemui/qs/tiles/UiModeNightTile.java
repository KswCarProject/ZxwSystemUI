package com.android.systemui.qs.tiles;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.LocationController;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UiModeNightTile extends QSTileImpl<QSTile.BooleanState> implements ConfigurationController.ConfigurationListener, BatteryController.BatteryStateChangeCallback {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
    public final BatteryController mBatteryController;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302834);
    public final LocationController mLocationController;
    public UiModeManager mUiModeManager;

    public int getMetricsCategory() {
        return 1706;
    }

    public UiModeNightTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ConfigurationController configurationController, BatteryController batteryController, LocationController locationController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBatteryController = batteryController;
        this.mUiModeManager = (UiModeManager) qSHost.getUserContext().getSystemService(UiModeManager.class);
        this.mLocationController = locationController;
        configurationController.observe(getLifecycle(), this);
        batteryController.observe(getLifecycle(), this);
    }

    public void onUiModeChanged() {
        refreshState();
    }

    public void onPowerSaveChanged(boolean z) {
        refreshState();
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        if (((QSTile.BooleanState) getState()).state != 0) {
            boolean z = !((QSTile.BooleanState) this.mState).value;
            this.mUiModeManager.setNightModeActivated(z);
            refreshState(Boolean.valueOf(z));
        }
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        CharSequence charSequence;
        int i;
        LocalTime localTime;
        int i2;
        int i3;
        int nightMode = this.mUiModeManager.getNightMode();
        boolean isPowerSave = this.mBatteryController.isPowerSave();
        int i4 = 1;
        boolean z = (this.mContext.getResources().getConfiguration().uiMode & 48) == 32;
        if (isPowerSave) {
            booleanState.secondaryLabel = this.mContext.getResources().getString(R$string.quick_settings_dark_mode_secondary_label_battery_saver);
        } else if (nightMode == 0 && this.mLocationController.isLocationEnabled()) {
            Resources resources = this.mContext.getResources();
            if (z) {
                i3 = R$string.quick_settings_dark_mode_secondary_label_until_sunrise;
            } else {
                i3 = R$string.quick_settings_dark_mode_secondary_label_on_at_sunset;
            }
            booleanState.secondaryLabel = resources.getString(i3);
        } else if (nightMode == 3) {
            int nightModeCustomType = this.mUiModeManager.getNightModeCustomType();
            if (nightModeCustomType == 0) {
                boolean is24HourFormat = DateFormat.is24HourFormat(this.mContext);
                if (z) {
                    localTime = this.mUiModeManager.getCustomNightModeEnd();
                } else {
                    localTime = this.mUiModeManager.getCustomNightModeStart();
                }
                Resources resources2 = this.mContext.getResources();
                if (z) {
                    i2 = R$string.quick_settings_dark_mode_secondary_label_until;
                } else {
                    i2 = R$string.quick_settings_dark_mode_secondary_label_on_at;
                }
                Object[] objArr = new Object[1];
                objArr[0] = is24HourFormat ? localTime.toString() : formatter.format(localTime);
                booleanState.secondaryLabel = resources2.getString(i2, objArr);
            } else if (nightModeCustomType == 1) {
                Resources resources3 = this.mContext.getResources();
                if (z) {
                    i = R$string.quick_settings_dark_mode_secondary_label_until_bedtime_ends;
                } else {
                    i = R$string.quick_settings_dark_mode_secondary_label_on_at_bedtime;
                }
                booleanState.secondaryLabel = resources3.getString(i);
            } else {
                booleanState.secondaryLabel = null;
            }
        } else {
            booleanState.secondaryLabel = null;
        }
        booleanState.value = z;
        booleanState.label = this.mContext.getString(R$string.quick_settings_ui_mode_night_label);
        booleanState.icon = this.mIcon;
        if (TextUtils.isEmpty(booleanState.secondaryLabel)) {
            charSequence = booleanState.label;
        } else {
            charSequence = TextUtils.concat(new CharSequence[]{booleanState.label, ", ", booleanState.secondaryLabel});
        }
        booleanState.contentDescription = charSequence;
        if (isPowerSave) {
            booleanState.state = 0;
        } else {
            if (booleanState.value) {
                i4 = 2;
            }
            booleanState.state = i4;
        }
        booleanState.showRippleEffect = false;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.DARK_THEME_SETTINGS");
    }

    public CharSequence getTileLabel() {
        return ((QSTile.BooleanState) getState()).label;
    }
}
