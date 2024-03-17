package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;

public abstract class SensorPrivacyToggleTile extends QSTileImpl<QSTile.BooleanState> implements IndividualSensorPrivacyController.Callback {
    public final KeyguardStateController mKeyguard;
    public IndividualSensorPrivacyController mSensorPrivacyController;

    public abstract int getIconRes(boolean z);

    public int getMetricsCategory() {
        return 0;
    }

    public abstract String getRestriction();

    public abstract int getSensorId();

    public SensorPrivacyToggleTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mSensorPrivacyController = individualSensorPrivacyController;
        this.mKeyguard = keyguardStateController;
        individualSensorPrivacyController.observe(getLifecycle(), this);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        boolean isSensorBlocked = this.mSensorPrivacyController.isSensorBlocked(getSensorId());
        if (!this.mSensorPrivacyController.requiresAuthentication() || !this.mKeyguard.isMethodSecure() || !this.mKeyguard.isShowing()) {
            this.mSensorPrivacyController.setSensorBlocked(1, getSensorId(), !isSensorBlocked);
        } else {
            this.mActivityStarter.postQSRunnableDismissingKeyguard(new SensorPrivacyToggleTile$$ExternalSyntheticLambda0(this, isSensorBlocked));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0(boolean z) {
        this.mSensorPrivacyController.setSensorBlocked(1, getSensorId(), !z);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        if (obj == null) {
            z = this.mSensorPrivacyController.isSensorBlocked(getSensorId());
        } else {
            z = ((Boolean) obj).booleanValue();
        }
        checkIfRestrictionEnforcedByAdminOnly(booleanState, getRestriction());
        booleanState.icon = QSTileImpl.ResourceIcon.get(getIconRes(z));
        booleanState.state = z ? 1 : 2;
        booleanState.value = !z;
        booleanState.label = getTileLabel();
        if (z) {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_camera_mic_blocked);
        } else {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_camera_mic_available);
        }
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.PRIVACY_SETTINGS");
    }

    public void onSensorBlockedChanged(int i, boolean z) {
        if (i == getSensorId()) {
            refreshState(Boolean.valueOf(z));
        }
    }
}
