package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import androidx.lifecycle.LifecycleOwner;
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
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.util.settings.SecureSettings;

public class RotationLockTile extends QSTileImpl<QSTile.BooleanState> implements BatteryController.BatteryStateChangeCallback {
    public final BatteryController mBatteryController;
    public final RotationLockController.RotationLockControllerCallback mCallback;
    public final RotationLockController mController;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302827);
    public final SensorPrivacyManager mPrivacyManager;
    public final SensorPrivacyManager.OnSensorPrivacyChangedListener mSensorPrivacyChangedListener;
    public final SettingObserver mSetting;

    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowFixedWidthMinor;
    }

    public RotationLockTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RotationLockController rotationLockController, SensorPrivacyManager sensorPrivacyManager, BatteryController batteryController, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        AnonymousClass2 r2 = new RotationLockController.RotationLockControllerCallback() {
            public void onRotationLockStateChanged(boolean z, boolean z2) {
                RotationLockTile.this.refreshState(Boolean.valueOf(z));
            }
        };
        this.mCallback = r2;
        this.mSensorPrivacyChangedListener = new RotationLockTile$$ExternalSyntheticLambda0(this);
        this.mController = rotationLockController;
        rotationLockController.observe((LifecycleOwner) this, r2);
        this.mPrivacyManager = sensorPrivacyManager;
        this.mBatteryController = batteryController;
        SecureSettings secureSettings2 = secureSettings;
        this.mSetting = new SettingObserver(secureSettings2, this.mHandler, "camera_autorotate", qSHost.getUserContext().getUserId()) {
            public void handleValueChanged(int i, boolean z) {
                RotationLockTile.this.handleRefreshState((Object) null);
            }
        };
        batteryController.observe(getLifecycle(), this);
    }

    public void handleInitialize() {
        this.mPrivacyManager.addSensorPrivacyListener(2, this.mSensorPrivacyChangedListener);
    }

    public void onPowerSaveChanged(boolean z) {
        refreshState();
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.AUTO_ROTATE_SETTINGS");
    }

    public void handleClick(View view) {
        boolean z = !((QSTile.BooleanState) this.mState).value;
        this.mController.setRotationLocked(!z);
        refreshState(Boolean.valueOf(z));
    }

    public CharSequence getTileLabel() {
        return ((QSTile.BooleanState) getState()).label;
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean isRotationLocked = this.mController.isRotationLocked();
        int i = 2;
        boolean z = !this.mBatteryController.isPowerSave() && !this.mPrivacyManager.isSensorPrivacyEnabled(2) && RotationLockControllerImpl.hasSufficientPermission(this.mContext) && this.mController.isCameraRotationEnabled();
        booleanState.value = !isRotationLocked;
        booleanState.label = this.mContext.getString(R$string.quick_settings_rotation_unlocked_label);
        booleanState.icon = this.mIcon;
        booleanState.contentDescription = getAccessibilityString(isRotationLocked);
        if (isRotationLocked || !z) {
            booleanState.secondaryLabel = "";
        } else {
            booleanState.secondaryLabel = this.mContext.getResources().getString(R$string.rotation_lock_camera_rotation_on);
        }
        booleanState.stateDescription = booleanState.secondaryLabel;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        if (!booleanState.value) {
            i = 1;
        }
        booleanState.state = i;
    }

    public void handleDestroy() {
        super.handleDestroy();
        this.mSetting.setListening(false);
        this.mPrivacyManager.removeSensorPrivacyListener(2, this.mSensorPrivacyChangedListener);
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mSetting.setListening(z);
    }

    public void handleUserSwitch(int i) {
        this.mSetting.setUserId(i);
        handleRefreshState((Object) null);
    }

    public static boolean isCurrentOrientationLockPortrait(RotationLockController rotationLockController, Resources resources) {
        int rotationLockOrientation = rotationLockController.getRotationLockOrientation();
        if (rotationLockOrientation == 0) {
            if (resources.getConfiguration().orientation != 2) {
                return true;
            }
            return false;
        } else if (rotationLockOrientation != 2) {
            return true;
        } else {
            return false;
        }
    }

    public final String getAccessibilityString(boolean z) {
        return this.mContext.getString(R$string.accessibility_quick_settings_rotation);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, boolean z) {
        refreshState();
    }
}
