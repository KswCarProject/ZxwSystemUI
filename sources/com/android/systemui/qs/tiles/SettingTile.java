package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;

public class SettingTile extends QSTileImpl<QSTile.BooleanState> {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_settings);
    public boolean mListening;

    public int getMetricsCategory() {
        return 2145;
    }

    public SettingTile(QSHost qSHost, Looper looper, Handler handler, MetricsLogger metricsLogger, FalsingManager falsingManager, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        MetricsLogger.action(this.mContext, getMetricsCategory(), !((QSTile.BooleanState) this.mState).value);
        this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.SETTINGS"), 0);
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.SETTINGS");
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.accessibility_settings_button);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        if (obj instanceof Integer) {
            ((Integer) obj).intValue();
        }
        booleanState.value = false;
        booleanState.label = this.mContext.getString(R$string.accessibility_settings_button);
        booleanState.icon = this.mIcon;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = true;
        booleanState.state = 1;
        booleanState.secondaryLabel = this.mContext.getString(R$string.accessibility_menu);
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public void handleSetListening(boolean z) {
        if (this.mListening != z) {
            this.mListening = z;
        }
    }
}
