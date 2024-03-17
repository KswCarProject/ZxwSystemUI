package com.android.systemui.qs.tiles;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Prefs;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.DataSaverController;

public class DataSaverTile extends QSTileImpl<QSTile.BooleanState> implements DataSaverController.Listener {
    public final DataSaverController mDataSaverController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;

    public int getMetricsCategory() {
        return 284;
    }

    public DataSaverTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, DataSaverController dataSaverController, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mDataSaverController = dataSaverController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        dataSaverController.observe(getLifecycle(), this);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.DATA_SAVER_SETTINGS");
    }

    public void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).value || Prefs.getBoolean(this.mContext, "QsDataSaverDialogShown", false)) {
            toggleDataSaver();
        } else {
            this.mUiHandler.post(new DataSaverTile$$ExternalSyntheticLambda0(this, view));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$1(View view) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
        systemUIDialog.setTitle(17040098);
        systemUIDialog.setMessage(17040096);
        systemUIDialog.setPositiveButton(17040097, new DataSaverTile$$ExternalSyntheticLambda1(this));
        systemUIDialog.setNeutralButton(17039360, (DialogInterface.OnClickListener) null);
        systemUIDialog.setShowForAllUsers(true);
        if (view != null) {
            this.mDialogLaunchAnimator.showFromView(systemUIDialog, view);
        } else {
            systemUIDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0(DialogInterface dialogInterface, int i) {
        toggleDataSaver();
        Prefs.putBoolean(this.mContext, "QsDataSaverDialogShown", true);
    }

    public final void toggleDataSaver() {
        ((QSTile.BooleanState) this.mState).value = !this.mDataSaverController.isDataSaverEnabled();
        this.mDataSaverController.setDataSaverEnabled(((QSTile.BooleanState) this.mState).value);
        refreshState(Boolean.valueOf(((QSTile.BooleanState) this.mState).value));
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.data_saver);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        int i;
        if (obj instanceof Boolean) {
            z = ((Boolean) obj).booleanValue();
        } else {
            z = this.mDataSaverController.isDataSaverEnabled();
        }
        booleanState.value = z;
        booleanState.state = z ? 2 : 1;
        String string = this.mContext.getString(R$string.data_saver);
        booleanState.label = string;
        booleanState.contentDescription = string;
        if (booleanState.value) {
            i = R$drawable.ic_data_saver;
        } else {
            i = R$drawable.ic_data_saver_off;
        }
        booleanState.icon = QSTileImpl.ResourceIcon.get(i);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public void onDataSaverChanged(boolean z) {
        refreshState(Boolean.valueOf(z));
    }
}
