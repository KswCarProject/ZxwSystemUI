package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;

public class QRCodeScannerTile extends QSTileImpl<QSTile.State> {
    public final QRCodeScannerController.Callback mCallback;
    public final CharSequence mLabel = this.mContext.getString(R$string.qr_code_scanner_title);
    public final QRCodeScannerController mQRCodeScannerController;

    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    public QRCodeScannerTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, QRCodeScannerController qRCodeScannerController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        AnonymousClass1 r1 = new QRCodeScannerController.Callback() {
            public void onQRCodeScannerActivityChanged() {
                QRCodeScannerTile.this.refreshState();
            }
        };
        this.mCallback = r1;
        this.mQRCodeScannerController = qRCodeScannerController;
        qRCodeScannerController.observe(getLifecycle(), r1);
    }

    public void handleInitialize() {
        this.mQRCodeScannerController.registerQRCodeScannerChangeObservers(0);
    }

    public void handleDestroy() {
        super.handleDestroy();
        this.mQRCodeScannerController.unregisterQRCodeScannerChangeObservers(0);
    }

    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    public void handleClick(View view) {
        ActivityLaunchAnimator.Controller controller;
        Intent intent = this.mQRCodeScannerController.getIntent();
        if (intent == null) {
            Log.e("QRCodeScanner", "Expected a non-null intent");
            return;
        }
        if (view == null) {
            controller = null;
        } else {
            controller = ActivityLaunchAnimator.Controller.fromView(view, 32);
        }
        this.mActivityStarter.startActivity(intent, true, controller, true);
    }

    public void handleUpdateState(QSTile.State state, Object obj) {
        String string = this.mContext.getString(R$string.qr_code_scanner_title);
        state.label = string;
        state.contentDescription = string;
        state.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qr_code_scanner);
        state.state = this.mQRCodeScannerController.isEnabledForQuickSettings() ? 2 : 0;
    }

    public boolean isAvailable() {
        return this.mQRCodeScannerController.isCameraAvailable();
    }

    public CharSequence getTileLabel() {
        return this.mLabel;
    }
}
