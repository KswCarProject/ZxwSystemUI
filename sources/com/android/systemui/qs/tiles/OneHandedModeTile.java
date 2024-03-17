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
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.onehanded.OneHanded;

public class OneHandedModeTile extends QSTileImpl<QSTile.BooleanState> {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302833);
    public final SettingObserver mSetting;

    public int getMetricsCategory() {
        return 0;
    }

    public OneHandedModeTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, UserTracker userTracker, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mSetting = new SettingObserver(secureSettings, this.mHandler, "one_handed_mode_enabled", userTracker.getUserId()) {
            public void handleValueChanged(int i, boolean z) {
                OneHandedModeTile.this.handleRefreshState(Integer.valueOf(i));
            }
        };
    }

    public boolean isAvailable() {
        return isSupportOneHandedMode();
    }

    public void handleDestroy() {
        super.handleDestroy();
        this.mSetting.setListening(false);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mSetting.setListening(z);
    }

    public void handleUserSwitch(int i) {
        this.mSetting.setUserId(i);
        handleRefreshState(Integer.valueOf(this.mSetting.getValue()));
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.action.ONE_HANDED_SETTINGS");
    }

    public void handleClick(View view) {
        this.mSetting.setValue(((QSTile.BooleanState) this.mState).value ^ true ? 1 : 0);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_onehanded_label);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i = 1;
        booleanState.value = (obj instanceof Integer ? ((Integer) obj).intValue() : this.mSetting.getValue()) != 0;
        booleanState.label = this.mContext.getString(R$string.quick_settings_onehanded_label);
        booleanState.icon = this.mIcon;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        QSTile.SlashState slashState = booleanState.slash;
        boolean z = booleanState.value;
        slashState.isSlashed = !z;
        if (z) {
            i = 2;
        }
        booleanState.state = i;
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    @VisibleForTesting
    public boolean isSupportOneHandedMode() {
        return OneHanded.sIsSupportOneHandedMode;
    }
}
