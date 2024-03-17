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

public class SoundTile extends QSTileImpl<QSTile.BooleanState> {
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_soundsetting);
    public boolean mListening;

    public int getMetricsCategory() {
        return 2500;
    }

    public SoundTile(QSHost qSHost, Looper looper, Handler handler, MetricsLogger metricsLogger, FalsingManager falsingManager, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        MetricsLogger.action(this.mContext, getMetricsCategory(), !((QSTile.BooleanState) this.mState).value);
        this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("ZXW_ACTION_NOTIFICATION_CLICK_MUTE"), 0);
        this.mActivityStarter.postQSRunnableDismissingKeyguard(new SoundTile$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$1() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mHandler.postDelayed(new SoundTile$$ExternalSyntheticLambda2(this), 500);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0() {
        this.mContext.sendBroadcast(new Intent("ZXW_ACTION_NOTIFICATION_CLICK_MUTE"));
    }

    public Intent getLongClickIntent() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        Intent intent = new Intent("ZXW_ACTION_NOTIFICATION_CLICK_MUTE");
        this.mHandler.postDelayed(new SoundTile$$ExternalSyntheticLambda1(this, intent), 500);
        return intent;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getLongClickIntent$2(Intent intent) {
        this.mContext.sendBroadcast(intent);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.accessibility_volume_settings);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        if (obj instanceof Integer) {
            ((Integer) obj).intValue();
        }
        booleanState.value = false;
        booleanState.label = this.mContext.getString(R$string.accessibility_volume_settings);
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
