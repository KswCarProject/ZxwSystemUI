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

public class BlackScreenTile extends QSTileImpl<QSTile.BooleanState> {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_blackscreen);
    public boolean mListening;

    public int getMetricsCategory() {
        return 2144;
    }

    public BlackScreenTile(QSHost qSHost, Looper looper, Handler handler, MetricsLogger metricsLogger, FalsingManager falsingManager, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        MetricsLogger.action(this.mContext, getMetricsCategory(), !((QSTile.BooleanState) this.mState).value);
        this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.intent.action.BLACKSCREEN"), 0);
        this.mActivityStarter.postQSRunnableDismissingKeyguard(new BlackScreenTile$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0() {
        Intent intent = new Intent("com.szchoiceway.ACTION_LAUNCHER_KEY_CTRL");
        intent.putExtra("LauncherKeyWord", "BlackScreen");
        this.mContext.sendBroadcast(intent);
    }

    public Intent getLongClickIntent() {
        return new Intent("android.intent.action.BLACKSCREEN");
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.lbl_blackscreen);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        if (obj instanceof Integer) {
            ((Integer) obj).intValue();
        }
        booleanState.value = false;
        booleanState.label = this.mContext.getString(R$string.lbl_blackscreen);
        booleanState.icon = this.mIcon;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = true;
        booleanState.state = 1;
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public void handleSetListening(boolean z) {
        if (this.mListening != z) {
            this.mListening = z;
        }
    }
}
