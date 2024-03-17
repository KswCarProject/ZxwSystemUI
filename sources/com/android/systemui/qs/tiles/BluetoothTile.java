package com.android.systemui.qs.tiles;

import android.bluetooth.BluetoothClass;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.R$drawable;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BluetoothController;
import java.util.List;

public class BluetoothTile extends QSTileImpl<QSTile.BooleanState> {
    public static final Intent BLUETOOTH_SETTINGS = new Intent("android.settings.BLUETOOTH_SETTINGS");
    public final BluetoothController.Callback mCallback;
    public final BluetoothController mController;

    public int getMetricsCategory() {
        return 113;
    }

    public BluetoothTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BluetoothController bluetoothController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        AnonymousClass1 r1 = new BluetoothController.Callback() {
            public void onBluetoothStateChange(boolean z) {
                BluetoothTile.this.refreshState();
            }

            public void onBluetoothDevicesChanged() {
                BluetoothTile.this.refreshState();
            }
        };
        this.mCallback = r1;
        this.mController = bluetoothController;
        bluetoothController.observe(getLifecycle(), r1);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        Object obj;
        boolean z = ((QSTile.BooleanState) this.mState).value;
        if (z) {
            obj = null;
        } else {
            obj = QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
        }
        refreshState(obj);
        this.mController.setBluetoothEnabled(!z);
    }

    public Intent getLongClickIntent() {
        Intent intent = new Intent();
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName("com.szchoiceway.btsuite", "com.szchoiceway.btsuite.BTMainActivity"));
        intent.setFlags(270532608);
        return intent;
    }

    public void handleSecondaryClick(View view) {
        if (!this.mController.canConfigBluetooth()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.BLUETOOTH_SETTINGS"), 0);
        } else if (!((QSTile.BooleanState) this.mState).value) {
            this.mController.setBluetoothEnabled(true);
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_bluetooth_label);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_bluetooth");
        boolean z = obj == QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
        boolean z2 = z || this.mController.isBluetoothEnabled();
        boolean isBluetoothConnected = this.mController.isBluetoothConnected();
        boolean isBluetoothConnecting = this.mController.isBluetoothConnecting();
        booleanState.isTransient = z || isBluetoothConnecting || this.mController.getBluetoothState() == 11;
        booleanState.dualTarget = true;
        booleanState.value = z2;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = !z2;
        booleanState.label = this.mContext.getString(R$string.quick_settings_bluetooth_label);
        booleanState.secondaryLabel = TextUtils.emptyIfNull(getSecondaryLabel(z2, isBluetoothConnecting, isBluetoothConnected, booleanState.isTransient));
        booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_bluetooth);
        booleanState.stateDescription = "";
        if (z2) {
            if (isBluetoothConnected) {
                booleanState.icon = new BluetoothConnectedTileIcon();
                if (!TextUtils.isEmpty(this.mController.getConnectedDeviceName())) {
                    booleanState.label = this.mController.getConnectedDeviceName();
                }
                booleanState.stateDescription = this.mContext.getString(R$string.accessibility_bluetooth_name, new Object[]{booleanState.label}) + ", " + booleanState.secondaryLabel;
            } else if (booleanState.isTransient) {
                booleanState.icon = QSTileImpl.ResourceIcon.get(17302333);
                booleanState.stateDescription = booleanState.secondaryLabel;
            } else {
                booleanState.icon = QSTileImpl.ResourceIcon.get(17302829);
                booleanState.stateDescription = this.mContext.getString(R$string.accessibility_not_connected);
            }
            booleanState.state = 2;
        } else {
            booleanState.icon = QSTileImpl.ResourceIcon.get(17302829);
            booleanState.state = 1;
        }
        booleanState.dualLabelContentDescription = this.mContext.getResources().getString(R$string.accessibility_quick_settings_open_settings, new Object[]{getTileLabel()});
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public final String getSecondaryLabel(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z2) {
            return this.mContext.getString(R$string.quick_settings_connecting);
        }
        if (z4) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_transient);
        }
        List<CachedBluetoothDevice> connectedDevices = this.mController.getConnectedDevices();
        if (!z || !z3 || connectedDevices.isEmpty()) {
            return null;
        }
        if (connectedDevices.size() > 1) {
            return this.mContext.getResources().getQuantityString(R$plurals.quick_settings_hotspot_secondary_label_num_devices, connectedDevices.size(), new Object[]{Integer.valueOf(connectedDevices.size())});
        }
        CachedBluetoothDevice cachedBluetoothDevice = connectedDevices.get(0);
        int batteryLevel = cachedBluetoothDevice.getBatteryLevel();
        if (batteryLevel > -1) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_battery_level, new Object[]{Utils.formatPercentage(batteryLevel)});
        }
        BluetoothClass btClass = cachedBluetoothDevice.getBtClass();
        if (btClass == null) {
            return null;
        }
        if (cachedBluetoothDevice.isHearingAidDevice()) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_hearing_aids);
        }
        if (btClass.doesClassMatch(1)) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_audio);
        }
        if (btClass.doesClassMatch(0)) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_headset);
        }
        if (btClass.doesClassMatch(3)) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_input);
        }
        return null;
    }

    public boolean isAvailable() {
        return this.mController.isBluetoothSupported();
    }

    public class BluetoothConnectedTileIcon extends QSTile.Icon {
        public BluetoothConnectedTileIcon() {
        }

        public Drawable getDrawable(Context context) {
            return context.getDrawable(R$drawable.ic_bluetooth_connected);
        }
    }
}
