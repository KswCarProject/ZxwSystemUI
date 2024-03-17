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

public class RebootTile extends QSTileImpl<QSTile.BooleanState> {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_reboot_zxw);
    public boolean mListening;

    public int getMetricsCategory() {
        return 2147;
    }

    public RebootTile(QSHost qSHost, Looper looper, Handler handler, MetricsLogger metricsLogger, FalsingManager falsingManager, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        MetricsLogger.action(this.mContext, getMetricsCategory(), !((QSTile.BooleanState) this.mState).value);
        this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("ZXW_ACTION_REBOOT_SYS_REBOOT"), 0);
        this.mActivityStarter.postQSRunnableDismissingKeyguard(new RebootTile$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0() {
        this.mContext.sendBroadcast(new Intent("ZXW_ACTION_REBOOT_SYS_REBOOT"));
    }

    public Intent getLongClickIntent() {
        this.mContext.sendBroadcast(new Intent("ZXW_ACTION_REBOOT_SYS_REBOOT"));
        return new Intent("ZXW_ACTION_REBOOT_SYS_REBOOT");
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.lbl_reboot);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        if (obj instanceof Integer) {
            ((Integer) obj).intValue();
        }
        booleanState.value = false;
        booleanState.label = this.mContext.getString(R$string.lbl_reboot);
        booleanState.icon = this.mIcon;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = true;
        booleanState.state = 1;
        booleanState.secondaryLabel = this.mContext.getString(R$string.stream_system);
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public void handleSetListening(boolean z) {
        if (this.mListening != z) {
            this.mListening = z;
        }
    }
}
