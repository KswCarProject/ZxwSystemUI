package com.android.systemui.qs.tiles;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.app.MediaRouteDialogPresenter;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CastTile extends QSTileImpl<QSTile.BooleanState> {
    public static final Intent CAST_SETTINGS = new Intent("android.settings.CAST_SETTINGS");
    public final Callback mCallback;
    public final CastController mController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final HotspotController.Callback mHotspotCallback;
    public boolean mHotspotConnected;
    public final KeyguardStateController mKeyguard;
    public final NetworkController mNetworkController;
    public final SignalCallback mSignalCallback;
    public boolean mWifiConnected;

    public int getMetricsCategory() {
        return 114;
    }

    public CastTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, CastController castController, KeyguardStateController keyguardStateController, NetworkController networkController, HotspotController hotspotController, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Callback callback = new Callback();
        this.mCallback = callback;
        AnonymousClass1 r2 = new SignalCallback() {
            public void setWifiIndicators(WifiIndicators wifiIndicators) {
                boolean z = false;
                if (!SystemProperties.getBoolean("persist.debug.wfd.enable", false)) {
                    if (wifiIndicators.enabled && wifiIndicators.qsIcon.visible) {
                        z = true;
                    }
                    if (z != CastTile.this.mWifiConnected) {
                        CastTile.this.mWifiConnected = z;
                        if (!CastTile.this.mHotspotConnected) {
                            CastTile.this.refreshState();
                        }
                    }
                } else if (wifiIndicators.enabled != CastTile.this.mWifiConnected) {
                    CastTile.this.mWifiConnected = wifiIndicators.enabled;
                    CastTile.this.refreshState();
                }
            }
        };
        this.mSignalCallback = r2;
        AnonymousClass2 r3 = new HotspotController.Callback() {
            public void onHotspotChanged(boolean z, int i) {
                boolean z2 = z && i > 0;
                if (z2 != CastTile.this.mHotspotConnected) {
                    CastTile.this.mHotspotConnected = z2;
                    if (!CastTile.this.mWifiConnected) {
                        CastTile.this.refreshState();
                    }
                }
            }
        };
        this.mHotspotCallback = r3;
        this.mController = castController;
        this.mKeyguard = keyguardStateController;
        this.mNetworkController = networkController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        castController.observe((LifecycleOwner) this, callback);
        keyguardStateController.observe((LifecycleOwner) this, callback);
        networkController.observe((LifecycleOwner) this, r2);
        hotspotController.observe((LifecycleOwner) this, r3);
    }

    public QSTile.BooleanState newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.handlesLongClick = false;
        return booleanState;
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (QSTileImpl.DEBUG) {
            String str = this.TAG;
            Log.d(str, "handleSetListening " + z);
        }
        if (!z) {
            this.mController.setDiscovering(false);
        }
    }

    public void handleUserSwitch(int i) {
        super.handleUserSwitch(i);
        this.mController.setCurrentUserId(i);
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.CAST_SETTINGS");
    }

    public void handleLongClick(View view) {
        handleClick(view);
    }

    public void handleClick(View view) {
        if (((QSTile.BooleanState) getState()).state != 0) {
            List<CastController.CastDevice> activeDevices = getActiveDevices();
            if (!willPopDialog()) {
                this.mController.stopCasting(activeDevices.get(0));
            } else if (!this.mKeyguard.isShowing()) {
                showDialog(view);
            } else {
                this.mActivityStarter.postQSRunnableDismissingKeyguard(new CastTile$$ExternalSyntheticLambda0(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0() {
        showDialog((View) null);
    }

    public final boolean willPopDialog() {
        List<CastController.CastDevice> activeDevices = getActiveDevices();
        return activeDevices.isEmpty() || (activeDevices.get(0).tag instanceof MediaRouter.RouteInfo);
    }

    public final List<CastController.CastDevice> getActiveDevices() {
        ArrayList arrayList = new ArrayList();
        for (CastController.CastDevice next : this.mController.getCastDevices()) {
            int i = next.state;
            if (i == 2 || i == 1) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static class DialogHolder {
        public Dialog mDialog;

        public DialogHolder() {
        }

        public final void init(Dialog dialog) {
            this.mDialog = dialog;
        }
    }

    public final void showDialog(View view) {
        this.mUiHandler.post(new CastTile$$ExternalSyntheticLambda1(this, view));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showDialog$3(View view) {
        DialogHolder dialogHolder = new DialogHolder();
        Dialog createDialog = MediaRouteDialogPresenter.createDialog(this.mContext, 4, new CastTile$$ExternalSyntheticLambda2(this, dialogHolder), R$style.Theme_SystemUI_Dialog_Cast, false);
        dialogHolder.init(createDialog);
        SystemUIDialog.setShowForAllUsers(createDialog, true);
        SystemUIDialog.registerDismissListener(createDialog);
        SystemUIDialog.setWindowOnTop(createDialog, this.mKeyguard.isShowing());
        SystemUIDialog.setDialogSize(createDialog);
        this.mUiHandler.post(new CastTile$$ExternalSyntheticLambda3(this, view, createDialog));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showDialog$1(DialogHolder dialogHolder, View view) {
        ActivityLaunchAnimator.Controller createActivityLaunchController = this.mDialogLaunchAnimator.createActivityLaunchController(view);
        if (createActivityLaunchController == null) {
            dialogHolder.mDialog.dismiss();
        }
        this.mActivityStarter.postStartActivityDismissingKeyguard(getLongClickIntent(), 0, createActivityLaunchController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showDialog$2(View view, Dialog dialog) {
        if (view != null) {
            this.mDialogLaunchAnimator.showFromView(dialog, view);
        } else {
            dialog.show();
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_cast_title);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        int i2;
        String string = this.mContext.getString(R$string.quick_settings_cast_title);
        booleanState.label = string;
        booleanState.contentDescription = string;
        booleanState.stateDescription = "";
        booleanState.value = false;
        Iterator<CastController.CastDevice> it = this.mController.getCastDevices().iterator();
        boolean z = false;
        while (true) {
            i = 2;
            if (!it.hasNext()) {
                break;
            }
            CastController.CastDevice next = it.next();
            int i3 = next.state;
            if (i3 == 2) {
                booleanState.value = true;
                booleanState.secondaryLabel = getDeviceName(next);
                booleanState.stateDescription += "," + this.mContext.getString(R$string.accessibility_cast_name, new Object[]{booleanState.label});
                z = false;
                break;
            } else if (i3 == 1) {
                z = true;
            }
        }
        if (z && !booleanState.value) {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_connecting);
        }
        if (booleanState.value) {
            i2 = R$drawable.ic_cast_connected;
        } else {
            i2 = R$drawable.ic_cast;
        }
        booleanState.icon = QSTileImpl.ResourceIcon.get(i2);
        if (canCastToWifi() || booleanState.value) {
            boolean z2 = booleanState.value;
            if (!z2) {
                i = 1;
            }
            booleanState.state = i;
            if (!z2) {
                booleanState.secondaryLabel = "";
            }
            booleanState.expandedAccessibilityClassName = Button.class.getName();
            booleanState.forceExpandIcon = willPopDialog();
        } else {
            booleanState.state = 0;
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_cast_no_wifi);
            booleanState.forceExpandIcon = false;
        }
        booleanState.stateDescription += ", " + booleanState.secondaryLabel;
    }

    public final String getDeviceName(CastController.CastDevice castDevice) {
        String str = castDevice.name;
        return str != null ? str : this.mContext.getString(R$string.quick_settings_cast_device_default_name);
    }

    public final boolean canCastToWifi() {
        return this.mWifiConnected || this.mHotspotConnected;
    }

    public final class Callback implements CastController.Callback, KeyguardStateController.Callback {
        public Callback() {
        }

        public void onCastDevicesChanged() {
            CastTile.this.refreshState();
        }

        public void onKeyguardShowingChanged() {
            CastTile.this.refreshState();
        }
    }
}
