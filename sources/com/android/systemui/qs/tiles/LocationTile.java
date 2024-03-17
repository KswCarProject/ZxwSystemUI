package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import androidx.lifecycle.LifecycleOwner;
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
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;

public class LocationTile extends QSTileImpl<QSTile.BooleanState> {
    public final Callback mCallback;
    public final LocationController mController;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_location);
    public final KeyguardStateController mKeyguard;

    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowFixedWidthMajor;
    }

    public LocationTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, LocationController locationController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Callback callback = new Callback();
        this.mCallback = callback;
        this.mController = locationController;
        this.mKeyguard = keyguardStateController;
        locationController.observe((LifecycleOwner) this, callback);
        keyguardStateController.observe((LifecycleOwner) this, callback);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
    }

    public void handleClick(View view) {
        if (!this.mKeyguard.isMethodSecure() || !this.mKeyguard.isShowing()) {
            this.mController.setLocationEnabled(!((QSTile.BooleanState) this.mState).value);
            return;
        }
        this.mActivityStarter.postQSRunnableDismissingKeyguard(new LocationTile$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0() {
        boolean z = ((QSTile.BooleanState) this.mState).value;
        this.mHost.openPanels();
        this.mController.setLocationEnabled(!z);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_location_label);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.value = this.mController.isLocationEnabled();
        checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_share_location");
        if (!booleanState.disabledByPolicy) {
            checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_config_location");
        }
        booleanState.icon = this.mIcon;
        int i = 1;
        booleanState.slash.isSlashed = !booleanState.value;
        String string = this.mContext.getString(R$string.quick_settings_location_label);
        booleanState.label = string;
        booleanState.contentDescription = string;
        if (booleanState.value) {
            i = 2;
        }
        booleanState.state = i;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public final class Callback implements LocationController.LocationChangeCallback, KeyguardStateController.Callback {
        public Callback() {
        }

        public void onLocationSettingsChanged(boolean z) {
            LocationTile.this.refreshState();
        }

        public void onKeyguardShowingChanged() {
            LocationTile.this.refreshState();
        }
    }
}
