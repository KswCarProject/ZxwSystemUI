package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.AlphaControlledSignalTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import java.io.PrintWriter;

public class InternetTile extends QSTileImpl<QSTile.SignalState> {
    public static final Intent WIFI_SETTINGS = new Intent("android.settings.WIFI_SETTINGS");
    public final AccessPointController mAccessPointController;
    public final NetworkController mController;
    public final DataUsageController mDataController;
    public final Handler mHandler;
    public final InternetDialogFactory mInternetDialogFactory;
    public int mLastTileState = -1;
    public final InternetSignalCallback mSignalCallback;

    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowNoTitle;
    }

    public InternetTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, AccessPointController accessPointController, InternetDialogFactory internetDialogFactory) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        InternetSignalCallback internetSignalCallback = new InternetSignalCallback();
        this.mSignalCallback = internetSignalCallback;
        this.mInternetDialogFactory = internetDialogFactory;
        this.mHandler = handler;
        this.mController = networkController;
        this.mAccessPointController = accessPointController;
        this.mDataController = networkController.getMobileDataController();
        networkController.observe(getLifecycle(), internetSignalCallback);
    }

    public QSTile.SignalState newTileState() {
        QSTile.SignalState signalState = new QSTile.SignalState();
        signalState.forceExpandIcon = true;
        return signalState;
    }

    public QSIconView createTileView(Context context) {
        return new AlphaControlledSignalTileView(context);
    }

    public Intent getLongClickIntent() {
        return WIFI_SETTINGS;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0(View view) {
        this.mInternetDialogFactory.create(true, this.mAccessPointController.canConfigMobileData(), this.mAccessPointController.canConfigWifi(), view);
    }

    public void handleClick(View view) {
        this.mHandler.post(new InternetTile$$ExternalSyntheticLambda0(this, view));
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_internet_label);
    }

    public boolean isAvailable() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.wifi") || (this.mController.hasMobileDataFeature() && this.mHost.getUserContext().getUserId() == 0);
    }

    public final CharSequence getSecondaryLabel(boolean z, String str) {
        return z ? this.mContext.getString(R$string.quick_settings_wifi_secondary_label_transient) : str;
    }

    public static String removeDoubleQuotes(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length <= 1 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        return str.charAt(i) == '\"' ? str.substring(1, i) : str;
    }

    public static final class EthernetCallbackInfo {
        public boolean mConnected;
        public String mEthernetContentDescription;
        public int mEthernetSignalIconId;

        public EthernetCallbackInfo() {
        }

        public String toString() {
            return "EthernetCallbackInfo[" + "mConnected=" + this.mConnected + ",mEthernetSignalIconId=" + this.mEthernetSignalIconId + ",mEthernetContentDescription=" + this.mEthernetContentDescription + ']';
        }
    }

    public static final class WifiCallbackInfo {
        public boolean mActivityIn;
        public boolean mActivityOut;
        public boolean mAirplaneModeEnabled;
        public boolean mConnected;
        public boolean mEnabled;
        public boolean mIsTransient;
        public boolean mNoDefaultNetwork;
        public boolean mNoNetworksAvailable;
        public boolean mNoValidatedNetwork;
        public String mSsid;
        public String mStatusLabel;
        public String mWifiSignalContentDescription;
        public int mWifiSignalIconId;

        public WifiCallbackInfo() {
        }

        public String toString() {
            return "WifiCallbackInfo[" + "mAirplaneModeEnabled=" + this.mAirplaneModeEnabled + ",mEnabled=" + this.mEnabled + ",mConnected=" + this.mConnected + ",mWifiSignalIconId=" + this.mWifiSignalIconId + ",mSsid=" + this.mSsid + ",mActivityIn=" + this.mActivityIn + ",mActivityOut=" + this.mActivityOut + ",mWifiSignalContentDescription=" + this.mWifiSignalContentDescription + ",mIsTransient=" + this.mIsTransient + ",mNoDefaultNetwork=" + this.mNoDefaultNetwork + ",mNoValidatedNetwork=" + this.mNoValidatedNetwork + ",mNoNetworksAvailable=" + this.mNoNetworksAvailable + ']';
        }
    }

    public static final class CellularCallbackInfo {
        public boolean mActivityIn;
        public boolean mActivityOut;
        public boolean mAirplaneModeEnabled;
        public CharSequence mDataContentDescription;
        public CharSequence mDataSubscriptionName;
        public int mMobileSignalIconId;
        public boolean mMultipleSubs;
        public boolean mNoDefaultNetwork;
        public boolean mNoNetworksAvailable;
        public boolean mNoSim;
        public boolean mNoValidatedNetwork;
        public int mQsTypeIcon;
        public boolean mRoaming;

        public CellularCallbackInfo() {
        }

        public String toString() {
            return "CellularCallbackInfo[" + "mAirplaneModeEnabled=" + this.mAirplaneModeEnabled + ",mDataSubscriptionName=" + this.mDataSubscriptionName + ",mDataContentDescription=" + this.mDataContentDescription + ",mMobileSignalIconId=" + this.mMobileSignalIconId + ",mQsTypeIcon=" + this.mQsTypeIcon + ",mActivityIn=" + this.mActivityIn + ",mActivityOut=" + this.mActivityOut + ",mNoSim=" + this.mNoSim + ",mRoaming=" + this.mRoaming + ",mMultipleSubs=" + this.mMultipleSubs + ",mNoDefaultNetwork=" + this.mNoDefaultNetwork + ",mNoValidatedNetwork=" + this.mNoValidatedNetwork + ",mNoNetworksAvailable=" + this.mNoNetworksAvailable + ']';
        }
    }

    public final class InternetSignalCallback implements SignalCallback {
        public final CellularCallbackInfo mCellularInfo = new CellularCallbackInfo();
        public final EthernetCallbackInfo mEthernetInfo = new EthernetCallbackInfo();
        public final WifiCallbackInfo mWifiInfo = new WifiCallbackInfo();

        public InternetSignalCallback() {
        }

        public void setWifiIndicators(WifiIndicators wifiIndicators) {
            if (QSTileImpl.DEBUG) {
                String access$100 = InternetTile.this.TAG;
                Log.d(access$100, "setWifiIndicators: " + wifiIndicators);
            }
            WifiCallbackInfo wifiCallbackInfo = this.mWifiInfo;
            boolean z = wifiIndicators.enabled;
            wifiCallbackInfo.mEnabled = z;
            IconState iconState = wifiIndicators.qsIcon;
            if (iconState != null) {
                wifiCallbackInfo.mConnected = iconState.visible;
                wifiCallbackInfo.mWifiSignalIconId = iconState.icon;
                wifiCallbackInfo.mWifiSignalContentDescription = iconState.contentDescription;
                wifiCallbackInfo.mEnabled = z;
                wifiCallbackInfo.mSsid = wifiIndicators.description;
                wifiCallbackInfo.mActivityIn = wifiIndicators.activityIn;
                wifiCallbackInfo.mActivityOut = wifiIndicators.activityOut;
                wifiCallbackInfo.mIsTransient = wifiIndicators.isTransient;
                wifiCallbackInfo.mStatusLabel = wifiIndicators.statusLabel;
                InternetTile.this.refreshState(wifiCallbackInfo);
            }
        }

        public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
            if (QSTileImpl.DEBUG) {
                String access$400 = InternetTile.this.TAG;
                Log.d(access$400, "setMobileDataIndicators: " + mobileDataIndicators);
            }
            if (mobileDataIndicators.qsIcon != null) {
                CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
                CharSequence charSequence = mobileDataIndicators.qsDescription;
                if (charSequence == null) {
                    charSequence = InternetTile.this.mController.getMobileDataNetworkName();
                }
                cellularCallbackInfo.mDataSubscriptionName = charSequence;
                CellularCallbackInfo cellularCallbackInfo2 = this.mCellularInfo;
                cellularCallbackInfo2.mDataContentDescription = mobileDataIndicators.qsDescription != null ? mobileDataIndicators.typeContentDescriptionHtml : null;
                cellularCallbackInfo2.mMobileSignalIconId = mobileDataIndicators.qsIcon.icon;
                cellularCallbackInfo2.mQsTypeIcon = mobileDataIndicators.qsType;
                cellularCallbackInfo2.mActivityIn = mobileDataIndicators.activityIn;
                cellularCallbackInfo2.mActivityOut = mobileDataIndicators.activityOut;
                cellularCallbackInfo2.mRoaming = mobileDataIndicators.roaming;
                boolean z = true;
                if (InternetTile.this.mController.getNumberSubscriptions() <= 1) {
                    z = false;
                }
                cellularCallbackInfo2.mMultipleSubs = z;
                InternetTile.this.refreshState(this.mCellularInfo);
            }
        }

        public void setEthernetIndicators(IconState iconState) {
            String str;
            if (QSTileImpl.DEBUG) {
                String access$700 = InternetTile.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("setEthernetIndicators: icon = ");
                if (iconState == null) {
                    str = "";
                } else {
                    str = iconState.toString();
                }
                sb.append(str);
                Log.d(access$700, sb.toString());
            }
            EthernetCallbackInfo ethernetCallbackInfo = this.mEthernetInfo;
            boolean z = iconState.visible;
            ethernetCallbackInfo.mConnected = z;
            ethernetCallbackInfo.mEthernetSignalIconId = iconState.icon;
            ethernetCallbackInfo.mEthernetContentDescription = iconState.contentDescription;
            if (z) {
                InternetTile.this.refreshState(ethernetCallbackInfo);
            }
        }

        public void setNoSims(boolean z, boolean z2) {
            if (QSTileImpl.DEBUG) {
                String access$1000 = InternetTile.this.TAG;
                Log.d(access$1000, "setNoSims: show = " + z + ",simDetected = " + z2);
            }
            CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
            cellularCallbackInfo.mNoSim = z;
            if (z) {
                cellularCallbackInfo.mMobileSignalIconId = 0;
                cellularCallbackInfo.mQsTypeIcon = 0;
            }
        }

        public void setIsAirplaneMode(IconState iconState) {
            String str;
            if (QSTileImpl.DEBUG) {
                String access$1200 = InternetTile.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("setIsAirplaneMode: icon = ");
                if (iconState == null) {
                    str = "";
                } else {
                    str = iconState.toString();
                }
                sb.append(str);
                Log.d(access$1200, sb.toString());
            }
            CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
            boolean z = cellularCallbackInfo.mAirplaneModeEnabled;
            boolean z2 = iconState.visible;
            if (z != z2) {
                cellularCallbackInfo.mAirplaneModeEnabled = z2;
                WifiCallbackInfo wifiCallbackInfo = this.mWifiInfo;
                wifiCallbackInfo.mAirplaneModeEnabled = z2;
                InternetTile internetTile = InternetTile.this;
                if (internetTile.mSignalCallback.mEthernetInfo.mConnected) {
                    return;
                }
                if (!wifiCallbackInfo.mEnabled || wifiCallbackInfo.mWifiSignalIconId <= 0 || wifiCallbackInfo.mSsid == null) {
                    internetTile.refreshState(cellularCallbackInfo);
                } else {
                    internetTile.refreshState(wifiCallbackInfo);
                }
            }
        }

        public void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
            if (QSTileImpl.DEBUG) {
                String access$1600 = InternetTile.this.TAG;
                Log.d(access$1600, "setConnectivityStatus: noDefaultNetwork = " + z + ",noValidatedNetwork = " + z2 + ",noNetworksAvailable = " + z3);
            }
            CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
            cellularCallbackInfo.mNoDefaultNetwork = z;
            cellularCallbackInfo.mNoValidatedNetwork = z2;
            cellularCallbackInfo.mNoNetworksAvailable = z3;
            WifiCallbackInfo wifiCallbackInfo = this.mWifiInfo;
            wifiCallbackInfo.mNoDefaultNetwork = z;
            wifiCallbackInfo.mNoValidatedNetwork = z2;
            wifiCallbackInfo.mNoNetworksAvailable = z3;
            InternetTile.this.refreshState(wifiCallbackInfo);
        }

        public String toString() {
            return "InternetSignalCallback[" + "mWifiInfo=" + this.mWifiInfo + ",mCellularInfo=" + this.mCellularInfo + ",mEthernetInfo=" + this.mEthernetInfo + ']';
        }
    }

    public void handleUpdateState(QSTile.SignalState signalState, Object obj) {
        if (obj instanceof CellularCallbackInfo) {
            this.mLastTileState = 0;
            handleUpdateCellularState(signalState, obj);
        } else if (obj instanceof WifiCallbackInfo) {
            this.mLastTileState = 1;
            handleUpdateWifiState(signalState, obj);
        } else if (obj instanceof EthernetCallbackInfo) {
            this.mLastTileState = 2;
            handleUpdateEthernetState(signalState, obj);
        } else {
            int i = this.mLastTileState;
            if (i == 0) {
                handleUpdateCellularState(signalState, this.mSignalCallback.mCellularInfo);
            } else if (i == 1) {
                handleUpdateWifiState(signalState, this.mSignalCallback.mWifiInfo);
            } else if (i == 2) {
                handleUpdateEthernetState(signalState, this.mSignalCallback.mEthernetInfo);
            }
        }
    }

    public final void handleUpdateWifiState(QSTile.SignalState signalState, Object obj) {
        WifiCallbackInfo wifiCallbackInfo = (WifiCallbackInfo) obj;
        boolean z = QSTileImpl.DEBUG;
        if (z) {
            String str = this.TAG;
            Log.d(str, "handleUpdateWifiState: WifiCallbackInfo = " + wifiCallbackInfo.toString());
        }
        boolean z2 = wifiCallbackInfo.mEnabled && wifiCallbackInfo.mWifiSignalIconId > 0 && wifiCallbackInfo.mSsid != null;
        boolean z3 = wifiCallbackInfo.mWifiSignalIconId > 0 && wifiCallbackInfo.mSsid == null;
        if (signalState.slash == null) {
            QSTile.SlashState slashState = new QSTile.SlashState();
            signalState.slash = slashState;
            slashState.rotation = 6.0f;
        }
        signalState.slash.isSlashed = false;
        signalState.secondaryLabel = getSecondaryLabel(wifiCallbackInfo.mIsTransient, removeDoubleQuotes(wifiCallbackInfo.mSsid));
        signalState.state = 2;
        signalState.dualTarget = true;
        boolean z4 = wifiCallbackInfo.mEnabled;
        signalState.value = z4;
        signalState.activityIn = z4 && wifiCallbackInfo.mActivityIn;
        signalState.activityOut = z4 && wifiCallbackInfo.mActivityOut;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Resources resources = this.mContext.getResources();
        int i = R$string.quick_settings_internet_label;
        signalState.label = resources.getString(i);
        if (wifiCallbackInfo.mAirplaneModeEnabled) {
            if (!signalState.value) {
                signalState.state = 1;
                signalState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_no_internet_unavailable);
                signalState.secondaryLabel = resources.getString(R$string.status_bar_airplane);
            } else if (!z2) {
                signalState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_no_internet_unavailable);
                if (wifiCallbackInfo.mNoNetworksAvailable) {
                    signalState.secondaryLabel = resources.getString(R$string.quick_settings_networks_unavailable);
                } else {
                    signalState.secondaryLabel = resources.getString(R$string.quick_settings_networks_available);
                }
            } else {
                signalState.icon = QSTileImpl.ResourceIcon.get(wifiCallbackInfo.mWifiSignalIconId);
            }
        } else if (wifiCallbackInfo.mNoDefaultNetwork) {
            if (wifiCallbackInfo.mNoNetworksAvailable || !wifiCallbackInfo.mEnabled) {
                signalState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_no_internet_unavailable);
                signalState.secondaryLabel = resources.getString(R$string.quick_settings_networks_unavailable);
            } else {
                signalState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_no_internet_available);
                signalState.secondaryLabel = resources.getString(R$string.quick_settings_networks_available);
            }
        } else if (wifiCallbackInfo.mIsTransient) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302864);
        } else if (!signalState.value) {
            signalState.slash.isSlashed = true;
            signalState.state = 1;
            signalState.icon = QSTileImpl.ResourceIcon.get(17302912);
        } else if (z2) {
            signalState.icon = QSTileImpl.ResourceIcon.get(wifiCallbackInfo.mWifiSignalIconId);
        } else if (z3) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302912);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302912);
        }
        stringBuffer.append(this.mContext.getString(i));
        stringBuffer.append(",");
        if (signalState.value && z2) {
            stringBuffer2.append(wifiCallbackInfo.mWifiSignalContentDescription);
            stringBuffer.append(removeDoubleQuotes(wifiCallbackInfo.mSsid));
        } else if (!TextUtils.isEmpty(signalState.secondaryLabel)) {
            stringBuffer.append(",");
            stringBuffer.append(signalState.secondaryLabel);
        }
        signalState.stateDescription = stringBuffer2.toString();
        signalState.contentDescription = stringBuffer.toString();
        signalState.dualLabelContentDescription = resources.getString(R$string.accessibility_quick_settings_open_settings, new Object[]{getTileLabel()});
        signalState.expandedAccessibilityClassName = Switch.class.getName();
        if (z) {
            String str2 = this.TAG;
            Log.d(str2, "handleUpdateWifiState: SignalState = " + signalState.toString());
        }
    }

    public final void handleUpdateCellularState(QSTile.SignalState signalState, Object obj) {
        CellularCallbackInfo cellularCallbackInfo = (CellularCallbackInfo) obj;
        boolean z = QSTileImpl.DEBUG;
        if (z) {
            Log.d(this.TAG, "handleUpdateCellularState: CellularCallbackInfo = " + cellularCallbackInfo.toString());
        }
        Resources resources = this.mContext.getResources();
        signalState.label = resources.getString(R$string.quick_settings_internet_label);
        signalState.state = 2;
        boolean z2 = false;
        boolean z3 = this.mDataController.isMobileDataSupported() && this.mDataController.isMobileDataEnabled();
        signalState.value = z3;
        signalState.activityIn = z3 && cellularCallbackInfo.mActivityIn;
        if (z3 && cellularCallbackInfo.mActivityOut) {
            z2 = true;
        }
        signalState.activityOut = z2;
        signalState.expandedAccessibilityClassName = Switch.class.getName();
        if (cellularCallbackInfo.mAirplaneModeEnabled && cellularCallbackInfo.mQsTypeIcon != TelephonyIcons.ICON_CWF) {
            signalState.state = 1;
            signalState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_no_internet_unavailable);
            signalState.secondaryLabel = resources.getString(R$string.status_bar_airplane);
        } else if (!cellularCallbackInfo.mNoDefaultNetwork) {
            signalState.icon = new SignalIcon(cellularCallbackInfo.mMobileSignalIconId);
            signalState.secondaryLabel = appendMobileDataType(cellularCallbackInfo.mDataSubscriptionName, getMobileDataContentName(cellularCallbackInfo));
        } else if (cellularCallbackInfo.mNoNetworksAvailable || !this.mSignalCallback.mWifiInfo.mEnabled) {
            signalState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_no_internet_unavailable);
            signalState.secondaryLabel = resources.getString(R$string.quick_settings_networks_unavailable);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_no_internet_available);
            signalState.secondaryLabel = resources.getString(R$string.quick_settings_networks_available);
        }
        signalState.contentDescription = signalState.label;
        if (signalState.state == 1) {
            signalState.stateDescription = "";
        } else {
            signalState.stateDescription = signalState.secondaryLabel;
        }
        if (z) {
            Log.d(this.TAG, "handleUpdateCellularState: SignalState = " + signalState.toString());
        }
    }

    public final void handleUpdateEthernetState(QSTile.SignalState signalState, Object obj) {
        EthernetCallbackInfo ethernetCallbackInfo = (EthernetCallbackInfo) obj;
        boolean z = QSTileImpl.DEBUG;
        if (z) {
            String str = this.TAG;
            Log.d(str, "handleUpdateEthernetState: EthernetCallbackInfo = " + ethernetCallbackInfo.toString());
        }
        signalState.label = this.mContext.getResources().getString(R$string.quick_settings_internet_label);
        signalState.state = 2;
        signalState.icon = QSTileImpl.ResourceIcon.get(ethernetCallbackInfo.mEthernetSignalIconId);
        signalState.secondaryLabel = ethernetCallbackInfo.mEthernetContentDescription;
        if (z) {
            String str2 = this.TAG;
            Log.d(str2, "handleUpdateEthernetState: SignalState = " + signalState.toString());
        }
    }

    public final CharSequence appendMobileDataType(CharSequence charSequence, CharSequence charSequence2) {
        String str = "";
        if (TextUtils.isEmpty(charSequence2)) {
            if (charSequence != null) {
                str = charSequence.toString();
            }
            return Html.fromHtml(str, 0);
        } else if (TextUtils.isEmpty(charSequence)) {
            if (charSequence2 != null) {
                str = charSequence2.toString();
            }
            return Html.fromHtml(str, 0);
        } else {
            return Html.fromHtml(this.mContext.getString(R$string.mobile_carrier_text_format, new Object[]{charSequence, charSequence2}), 0);
        }
    }

    public final CharSequence getMobileDataContentName(CellularCallbackInfo cellularCallbackInfo) {
        String str;
        if (cellularCallbackInfo.mRoaming && !TextUtils.isEmpty(cellularCallbackInfo.mDataContentDescription)) {
            String string = this.mContext.getString(R$string.data_connection_roaming);
            CharSequence charSequence = cellularCallbackInfo.mDataContentDescription;
            if (charSequence == null) {
                str = "";
            } else {
                str = charSequence.toString();
            }
            return this.mContext.getString(R$string.mobile_data_text_format, new Object[]{string, str});
        } else if (cellularCallbackInfo.mRoaming) {
            return this.mContext.getString(R$string.data_connection_roaming);
        } else {
            return cellularCallbackInfo.mDataContentDescription;
        }
    }

    public static class SignalIcon extends QSTile.Icon {
        public final int mState;

        public SignalIcon(int i) {
            this.mState = i;
        }

        public int getState() {
            return this.mState;
        }

        public Drawable getDrawable(Context context) {
            SignalDrawable signalDrawable = new SignalDrawable(context);
            signalDrawable.setLevel(getState());
            return signalDrawable;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.print("    ");
        printWriter.println(((QSTile.SignalState) getState()).toString());
        printWriter.print("    ");
        printWriter.println("mLastTileState=" + this.mLastTileState);
        printWriter.print("    ");
        printWriter.println("mSignalCallback=" + this.mSignalCallback.toString());
    }
}
