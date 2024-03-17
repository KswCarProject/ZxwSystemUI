package com.android.systemui.qs.tiles.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;
import com.android.systemui.Prefs;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class InternetDialog extends SystemUIDialog implements InternetDialogController.InternetDialogCallback {
    public static final boolean DEBUG = Log.isLoggable("InternetDialog", 3);
    public InternetAdapter mAdapter;
    public Button mAirplaneModeButton;
    public TextView mAirplaneModeSummaryText;
    public AlertDialog mAlertDialog;
    public final Executor mBackgroundExecutor;
    public Drawable mBackgroundOff = null;
    public Drawable mBackgroundOn;
    public boolean mCanChangeWifiState;
    public boolean mCanConfigMobileData;
    public boolean mCanConfigWifi;
    public LinearLayout mConnectedWifListLayout;
    public WifiEntry mConnectedWifiEntry;
    public ImageView mConnectedWifiIcon;
    public TextView mConnectedWifiSummaryText;
    public TextView mConnectedWifiTitleText;
    public Context mContext;
    public int mDefaultDataSubId = -1;
    public View mDialogView;
    public View mDivider;
    public Button mDoneButton;
    public LinearLayout mEthernetLayout;
    public final Handler mHandler;
    public boolean mHasMoreWifiEntries;
    public final Runnable mHideProgressBarRunnable = new InternetDialog$$ExternalSyntheticLambda0(this);
    public Runnable mHideSearchingRunnable = new InternetDialog$$ExternalSyntheticLambda1(this);
    public InternetDialogController mInternetDialogController;
    public InternetDialogFactory mInternetDialogFactory;
    public LinearLayout mInternetDialogLayout;
    public TextView mInternetDialogSubTitle;
    public TextView mInternetDialogTitle;
    public boolean mIsProgressBarVisible;
    public boolean mIsSearchingHidden;
    public KeyguardStateController mKeyguard;
    public Switch mMobileDataToggle;
    public LinearLayout mMobileNetworkLayout;
    public TextView mMobileSummaryText;
    public TextView mMobileTitleText;
    public View mMobileToggleDivider;
    public ProgressBar mProgressBar;
    public LinearLayout mSeeAllLayout;
    public ImageView mSignalIcon;
    public SubscriptionManager mSubscriptionManager;
    public TelephonyManager mTelephonyManager;
    public LinearLayout mTurnWifiOnLayout;
    public UiEventLogger mUiEventLogger;
    public Switch mWiFiToggle;
    public int mWifiEntriesCount;
    public int mWifiNetworkHeight;
    public RecyclerView mWifiRecyclerView;
    public LinearLayout mWifiScanNotifyLayout;
    public TextView mWifiScanNotifyText;
    public ImageView mWifiSettingsIcon;
    public TextView mWifiToggleTitleText;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        setProgressBarVisible(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mIsSearchingHidden = true;
        this.mInternetDialogSubTitle.setText(getSubtitleText());
    }

    public InternetDialog(Context context, InternetDialogFactory internetDialogFactory, InternetDialogController internetDialogController, boolean z, boolean z2, boolean z3, UiEventLogger uiEventLogger, Handler handler, Executor executor, KeyguardStateController keyguardStateController) {
        super(context);
        if (DEBUG) {
            Log.d("InternetDialog", "Init InternetDialog");
        }
        this.mContext = getContext();
        this.mHandler = handler;
        this.mBackgroundExecutor = executor;
        this.mInternetDialogFactory = internetDialogFactory;
        this.mInternetDialogController = internetDialogController;
        this.mSubscriptionManager = internetDialogController.getSubscriptionManager();
        this.mDefaultDataSubId = this.mInternetDialogController.getDefaultDataSubscriptionId();
        this.mTelephonyManager = this.mInternetDialogController.getTelephonyManager();
        this.mCanConfigMobileData = z;
        this.mCanConfigWifi = z2;
        this.mCanChangeWifiState = WifiEnterpriseRestrictionUtils.isChangeWifiStateAllowed(context);
        this.mKeyguard = keyguardStateController;
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new InternetAdapter(this.mInternetDialogController);
        if (!z3) {
            getWindow().setType(2038);
        }
    }

    /* JADX INFO: finally extract failed */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (DEBUG) {
            Log.d("InternetDialog", "onCreate");
        }
        this.mUiEventLogger.log(InternetDialogEvent.INTERNET_DIALOG_SHOW);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(R$layout.internet_connectivity_dialog, (ViewGroup) null);
        Window window = getWindow();
        window.setContentView(this.mDialogView);
        window.setWindowAnimations(R$style.Animation_InternetDialog);
        this.mWifiNetworkHeight = this.mContext.getResources().getDimensionPixelSize(R$dimen.internet_dialog_wifi_network_height);
        this.mInternetDialogLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.internet_connectivity_dialog);
        this.mInternetDialogTitle = (TextView) this.mDialogView.requireViewById(R$id.internet_dialog_title);
        this.mInternetDialogSubTitle = (TextView) this.mDialogView.requireViewById(R$id.internet_dialog_subtitle);
        this.mDivider = this.mDialogView.requireViewById(R$id.divider);
        this.mProgressBar = (ProgressBar) this.mDialogView.requireViewById(R$id.wifi_searching_progress);
        this.mEthernetLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.ethernet_layout);
        this.mMobileNetworkLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.mobile_network_layout);
        this.mTurnWifiOnLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.turn_on_wifi_layout);
        this.mWifiToggleTitleText = (TextView) this.mDialogView.requireViewById(R$id.wifi_toggle_title);
        this.mWifiScanNotifyLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.wifi_scan_notify_layout);
        this.mWifiScanNotifyText = (TextView) this.mDialogView.requireViewById(R$id.wifi_scan_notify_text);
        this.mConnectedWifListLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.wifi_connected_layout);
        this.mConnectedWifiIcon = (ImageView) this.mDialogView.requireViewById(R$id.wifi_connected_icon);
        this.mConnectedWifiTitleText = (TextView) this.mDialogView.requireViewById(R$id.wifi_connected_title);
        this.mConnectedWifiSummaryText = (TextView) this.mDialogView.requireViewById(R$id.wifi_connected_summary);
        this.mWifiSettingsIcon = (ImageView) this.mDialogView.requireViewById(R$id.wifi_settings_icon);
        this.mWifiRecyclerView = (RecyclerView) this.mDialogView.requireViewById(R$id.wifi_list_layout);
        this.mSeeAllLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.see_all_layout);
        this.mDoneButton = (Button) this.mDialogView.requireViewById(R$id.done_button);
        this.mAirplaneModeButton = (Button) this.mDialogView.requireViewById(R$id.apm_button);
        this.mSignalIcon = (ImageView) this.mDialogView.requireViewById(R$id.signal_icon);
        this.mMobileTitleText = (TextView) this.mDialogView.requireViewById(R$id.mobile_title);
        this.mMobileSummaryText = (TextView) this.mDialogView.requireViewById(R$id.mobile_summary);
        this.mAirplaneModeSummaryText = (TextView) this.mDialogView.requireViewById(R$id.airplane_mode_summary);
        this.mMobileToggleDivider = this.mDialogView.requireViewById(R$id.mobile_toggle_divider);
        this.mMobileDataToggle = (Switch) this.mDialogView.requireViewById(R$id.mobile_toggle);
        this.mWiFiToggle = (Switch) this.mDialogView.requireViewById(R$id.wifi_toggle);
        this.mBackgroundOn = this.mContext.getDrawable(R$drawable.settingslib_switch_bar_bg_on);
        this.mInternetDialogTitle.setText(getDialogTitleText());
        this.mInternetDialogTitle.setGravity(8388627);
        int i = 0;
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16843534});
        try {
            this.mBackgroundOff = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            setOnClickListener();
            this.mTurnWifiOnLayout.setBackground((Drawable) null);
            Button button = this.mAirplaneModeButton;
            if (!this.mInternetDialogController.isAirplaneModeEnabled()) {
                i = 8;
            }
            button.setVisibility(i);
            this.mWifiRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
            this.mWifiRecyclerView.setAdapter(this.mAdapter);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public void onStart() {
        super.onStart();
        if (DEBUG) {
            Log.d("InternetDialog", "onStart");
        }
        this.mInternetDialogController.onStart(this, this.mCanConfigWifi);
        if (!this.mCanConfigWifi) {
            hideWifiViews();
        }
    }

    public void hideWifiViews() {
        setProgressBarVisible(false);
        this.mTurnWifiOnLayout.setVisibility(8);
        this.mConnectedWifListLayout.setVisibility(8);
        this.mWifiRecyclerView.setVisibility(8);
        this.mSeeAllLayout.setVisibility(8);
    }

    public void onStop() {
        super.onStop();
        if (DEBUG) {
            Log.d("InternetDialog", "onStop");
        }
        this.mHandler.removeCallbacks(this.mHideProgressBarRunnable);
        this.mHandler.removeCallbacks(this.mHideSearchingRunnable);
        this.mMobileNetworkLayout.setOnClickListener((View.OnClickListener) null);
        this.mMobileDataToggle.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        this.mConnectedWifListLayout.setOnClickListener((View.OnClickListener) null);
        this.mSeeAllLayout.setOnClickListener((View.OnClickListener) null);
        this.mWiFiToggle.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        this.mDoneButton.setOnClickListener((View.OnClickListener) null);
        this.mAirplaneModeButton.setOnClickListener((View.OnClickListener) null);
        this.mInternetDialogController.onStop();
        this.mInternetDialogFactory.destroyDialog();
    }

    public void dismissDialog() {
        if (DEBUG) {
            Log.d("InternetDialog", "dismissDialog");
        }
        this.mInternetDialogFactory.destroyDialog();
        dismiss();
    }

    public void updateDialog(boolean z) {
        if (DEBUG) {
            Log.d("InternetDialog", "updateDialog");
        }
        this.mInternetDialogTitle.setText(getDialogTitleText());
        this.mInternetDialogSubTitle.setText(getSubtitleText());
        this.mAirplaneModeButton.setVisibility(this.mInternetDialogController.isAirplaneModeEnabled() ? 0 : 8);
        updateEthernet();
        if (z) {
            setMobileDataLayout(this.mInternetDialogController.activeNetworkIsCellular(), this.mInternetDialogController.isCarrierNetworkActive());
        }
        if (this.mCanConfigWifi) {
            showProgressBar();
            boolean isDeviceLocked = this.mInternetDialogController.isDeviceLocked();
            boolean isWifiEnabled = this.mInternetDialogController.isWifiEnabled();
            boolean isWifiScanEnabled = this.mInternetDialogController.isWifiScanEnabled();
            updateWifiToggle(isWifiEnabled, isDeviceLocked);
            updateConnectedWifi(isWifiEnabled, isDeviceLocked);
            updateWifiListAndSeeAll(isWifiEnabled, isDeviceLocked);
            updateWifiScanNotify(isWifiEnabled, isWifiScanEnabled, isDeviceLocked);
        }
    }

    public final void setOnClickListener() {
        this.mMobileNetworkLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda3(this));
        this.mMobileDataToggle.setOnCheckedChangeListener(new InternetDialog$$ExternalSyntheticLambda4(this));
        this.mConnectedWifListLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda5(this));
        this.mSeeAllLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda6(this));
        this.mWiFiToggle.setOnCheckedChangeListener(new InternetDialog$$ExternalSyntheticLambda7(this));
        this.mDoneButton.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda8(this));
        this.mAirplaneModeButton.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda9(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$2(View view) {
        if (this.mInternetDialogController.isMobileDataEnabled() && !this.mInternetDialogController.isDeviceLocked() && !this.mInternetDialogController.activeNetworkIsCellular()) {
            this.mInternetDialogController.connectCarrierNetwork();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$3(CompoundButton compoundButton, boolean z) {
        if (!z && shouldShowMobileDialog()) {
            showTurnOffMobileDialog();
        } else if (!shouldShowMobileDialog()) {
            this.mInternetDialogController.setMobileDataEnabled(this.mContext, this.mDefaultDataSubId, z, false);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$4(CompoundButton compoundButton, boolean z) {
        if (this.mInternetDialogController.isWifiEnabled() != z) {
            this.mInternetDialogController.setWifiEnabled(z);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$5(View view) {
        dismiss();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnClickListener$6(View view) {
        this.mInternetDialogController.setAirplaneModeDisabled();
    }

    public final void updateEthernet() {
        this.mEthernetLayout.setVisibility(this.mInternetDialogController.hasEthernet() ? 0 : 8);
    }

    public final boolean shouldDisallowUserToDisableMobileData() {
        return this.mInternetDialogController.isMobileDataEnabled() && !this.mInternetDialogController.isNonDdsCallStateIdle();
    }

    public final void setMobileDataLayout(boolean z, boolean z2) {
        int i;
        int i2;
        int i3 = 0;
        boolean z3 = z || z2;
        if (DEBUG) {
            Log.d("InternetDialog", "setMobileDataLayout, isCarrierNetworkActive = " + z2);
        }
        boolean isWifiEnabled = this.mInternetDialogController.isWifiEnabled();
        if (this.mInternetDialogController.hasActiveSubId() || (isWifiEnabled && z2)) {
            if (shouldDisallowUserToDisableMobileData()) {
                Log.d("InternetDialog", "Do not allow mobile data switch to be turned off");
                this.mMobileDataToggle.setEnabled(false);
            } else {
                this.mMobileDataToggle.setEnabled(true);
            }
            this.mMobileNetworkLayout.setVisibility(0);
            this.mMobileDataToggle.setChecked(this.mInternetDialogController.isMobileDataEnabled());
            this.mMobileTitleText.setText(getMobileNetworkTitle());
            String mobileNetworkSummary = getMobileNetworkSummary();
            if (!TextUtils.isEmpty(mobileNetworkSummary)) {
                this.mMobileSummaryText.setText(Html.fromHtml(mobileNetworkSummary, 0));
                this.mMobileSummaryText.setVisibility(0);
            } else {
                this.mMobileSummaryText.setVisibility(8);
            }
            this.mBackgroundExecutor.execute(new InternetDialog$$ExternalSyntheticLambda25(this));
            TextView textView = this.mMobileTitleText;
            if (z3) {
                i = R$style.TextAppearance_InternetDialog_Active;
            } else {
                i = R$style.TextAppearance_InternetDialog;
            }
            textView.setTextAppearance(i);
            if (z3) {
                i2 = R$style.TextAppearance_InternetDialog_Secondary_Active;
            } else {
                i2 = R$style.TextAppearance_InternetDialog_Secondary;
            }
            this.mMobileSummaryText.setTextAppearance(i2);
            if (this.mInternetDialogController.isAirplaneModeEnabled()) {
                this.mAirplaneModeSummaryText.setVisibility(0);
                this.mAirplaneModeSummaryText.setText(this.mContext.getText(R$string.airplane_mode));
                this.mAirplaneModeSummaryText.setTextAppearance(i2);
            } else {
                this.mAirplaneModeSummaryText.setVisibility(8);
            }
            this.mMobileNetworkLayout.setBackground(z3 ? this.mBackgroundOn : this.mBackgroundOff);
            TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R$style.InternetDialog_Divider_Active, new int[]{16842964});
            int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16842808);
            View view = this.mMobileToggleDivider;
            if (z3) {
                colorAttrDefaultColor = obtainStyledAttributes.getColor(0, colorAttrDefaultColor);
            }
            view.setBackgroundColor(colorAttrDefaultColor);
            obtainStyledAttributes.recycle();
            this.mMobileDataToggle.setVisibility(this.mCanConfigMobileData ? 0 : 4);
            View view2 = this.mMobileToggleDivider;
            if (!this.mCanConfigMobileData) {
                i3 = 4;
            }
            view2.setVisibility(i3);
            return;
        }
        this.mMobileNetworkLayout.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setMobileDataLayout$8() {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda26(this, getSignalStrengthDrawable()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setMobileDataLayout$7(Drawable drawable) {
        this.mSignalIcon.setImageDrawable(drawable);
    }

    public final void updateWifiToggle(boolean z, boolean z2) {
        int i;
        if (this.mWiFiToggle.isChecked() != z) {
            this.mWiFiToggle.setChecked(z);
        }
        if (z2) {
            TextView textView = this.mWifiToggleTitleText;
            if (this.mConnectedWifiEntry != null) {
                i = R$style.TextAppearance_InternetDialog_Active;
            } else {
                i = R$style.TextAppearance_InternetDialog;
            }
            textView.setTextAppearance(i);
        }
        this.mTurnWifiOnLayout.setBackground((!z2 || this.mConnectedWifiEntry == null) ? null : this.mBackgroundOn);
        if (!this.mCanChangeWifiState && this.mWiFiToggle.isEnabled()) {
            this.mWiFiToggle.setEnabled(false);
            this.mWifiToggleTitleText.setEnabled(false);
            TextView textView2 = (TextView) this.mDialogView.requireViewById(R$id.wifi_toggle_summary);
            textView2.setEnabled(false);
            textView2.setVisibility(0);
        }
    }

    public final void updateConnectedWifi(boolean z, boolean z2) {
        if (!z || this.mConnectedWifiEntry == null || z2) {
            this.mConnectedWifListLayout.setVisibility(8);
            return;
        }
        this.mConnectedWifListLayout.setVisibility(0);
        this.mConnectedWifiTitleText.setText(this.mConnectedWifiEntry.getTitle());
        this.mConnectedWifiSummaryText.setText(this.mConnectedWifiEntry.getSummary(false));
        this.mConnectedWifiIcon.setImageDrawable(this.mInternetDialogController.getInternetWifiDrawable(this.mConnectedWifiEntry));
        this.mWifiSettingsIcon.setColorFilter(this.mContext.getColor(R$color.connected_network_primary_color));
    }

    public final void updateWifiListAndSeeAll(boolean z, boolean z2) {
        if (!z || z2) {
            this.mWifiRecyclerView.setVisibility(8);
            this.mSeeAllLayout.setVisibility(8);
            return;
        }
        int wifiListMaxCount = getWifiListMaxCount();
        if (this.mAdapter.getItemCount() > wifiListMaxCount) {
            this.mHasMoreWifiEntries = true;
        }
        this.mAdapter.setMaxEntriesCount(wifiListMaxCount);
        int i = this.mWifiNetworkHeight * wifiListMaxCount;
        if (this.mWifiRecyclerView.getMinimumHeight() != i) {
            this.mWifiRecyclerView.setMinimumHeight(i);
        }
        int i2 = 0;
        this.mWifiRecyclerView.setVisibility(0);
        LinearLayout linearLayout = this.mSeeAllLayout;
        if (!this.mHasMoreWifiEntries) {
            i2 = 4;
        }
        linearLayout.setVisibility(i2);
    }

    public int getWifiListMaxCount() {
        int i = 3;
        int i2 = this.mEthernetLayout.getVisibility() == 0 ? 3 : 4;
        if (this.mMobileNetworkLayout.getVisibility() == 0) {
            i2--;
        }
        if (i2 <= 3) {
            i = i2;
        }
        return this.mConnectedWifListLayout.getVisibility() == 0 ? i - 1 : i;
    }

    public final void updateWifiScanNotify(boolean z, boolean z2, boolean z3) {
        if (z || !z2 || z3) {
            this.mWifiScanNotifyLayout.setVisibility(8);
            return;
        }
        if (TextUtils.isEmpty(this.mWifiScanNotifyText.getText())) {
            InternetDialogController internetDialogController = this.mInternetDialogController;
            Objects.requireNonNull(internetDialogController);
            AnnotationLinkSpan.LinkInfo linkInfo = new AnnotationLinkSpan.LinkInfo("link", new InternetDialog$$ExternalSyntheticLambda24(internetDialogController));
            this.mWifiScanNotifyText.setText(AnnotationLinkSpan.linkify(getContext().getText(R$string.wifi_scan_notify_message), linkInfo));
            this.mWifiScanNotifyText.setMovementMethod(LinkMovementMethod.getInstance());
        }
        this.mWifiScanNotifyLayout.setVisibility(0);
    }

    public void onClickConnectedWifi(View view) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            this.mInternetDialogController.launchWifiDetailsSetting(wifiEntry.getKey(), view);
        }
    }

    public void onClickSeeMoreButton(View view) {
        this.mInternetDialogController.launchNetworkSetting(view);
    }

    public CharSequence getDialogTitleText() {
        return this.mInternetDialogController.getDialogTitleText();
    }

    public CharSequence getSubtitleText() {
        return this.mInternetDialogController.getSubtitleText(this.mIsProgressBarVisible && !this.mIsSearchingHidden);
    }

    public final Drawable getSignalStrengthDrawable() {
        return this.mInternetDialogController.getSignalStrengthDrawable();
    }

    public CharSequence getMobileNetworkTitle() {
        return this.mInternetDialogController.getMobileNetworkTitle();
    }

    public String getMobileNetworkSummary() {
        if (shouldDisallowUserToDisableMobileData()) {
            return this.mContext.getString(R$string.mobile_data_summary_not_allowed_to_disable_data);
        }
        return this.mInternetDialogController.getMobileNetworkSummary();
    }

    public void showProgressBar() {
        if (!this.mInternetDialogController.isWifiEnabled() || this.mInternetDialogController.isDeviceLocked()) {
            setProgressBarVisible(false);
            return;
        }
        setProgressBarVisible(true);
        if (this.mConnectedWifiEntry != null || this.mWifiEntriesCount > 0) {
            this.mHandler.postDelayed(this.mHideProgressBarRunnable, 1500);
        } else if (!this.mIsSearchingHidden) {
            this.mHandler.postDelayed(this.mHideSearchingRunnable, 1500);
        }
    }

    public final void setProgressBarVisible(boolean z) {
        if (this.mIsProgressBarVisible != z) {
            this.mIsProgressBarVisible = z;
            int i = 0;
            this.mProgressBar.setVisibility(z ? 0 : 8);
            this.mProgressBar.setIndeterminate(z);
            View view = this.mDivider;
            if (z) {
                i = 8;
            }
            view.setVisibility(i);
            this.mInternetDialogSubTitle.setText(getSubtitleText());
        }
    }

    public final boolean shouldShowMobileDialog() {
        boolean z = Prefs.getBoolean(this.mContext, "QsHasTurnedOffMobileData", false);
        if (!this.mInternetDialogController.isMobileDataEnabled() || z) {
            return false;
        }
        return true;
    }

    public final void showTurnOffMobileDialog() {
        CharSequence mobileNetworkTitle = getMobileNetworkTitle();
        boolean isVoiceStateInService = this.mInternetDialogController.isVoiceStateInService();
        if (TextUtils.isEmpty(mobileNetworkTitle) || !isVoiceStateInService) {
            mobileNetworkTitle = this.mContext.getString(R$string.mobile_data_disable_message_default_carrier);
        }
        AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(R$string.mobile_data_disable_title).setMessage(this.mContext.getString(R$string.mobile_data_disable_message, new Object[]{mobileNetworkTitle})).setNegativeButton(17039360, new InternetDialog$$ExternalSyntheticLambda21(this)).setPositiveButton(17039659, new InternetDialog$$ExternalSyntheticLambda22(this)).create();
        this.mAlertDialog = create;
        create.setOnCancelListener(new InternetDialog$$ExternalSyntheticLambda23(this));
        this.mAlertDialog.getWindow().setType(2009);
        SystemUIDialog.setShowForAllUsers(this.mAlertDialog, true);
        SystemUIDialog.registerDismissListener(this.mAlertDialog);
        SystemUIDialog.setWindowOnTop(this.mAlertDialog, this.mKeyguard.isShowing());
        this.mAlertDialog.show();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showTurnOffMobileDialog$9(DialogInterface dialogInterface, int i) {
        this.mMobileDataToggle.setChecked(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showTurnOffMobileDialog$10(DialogInterface dialogInterface, int i) {
        this.mInternetDialogController.setMobileDataEnabled(this.mContext, this.mDefaultDataSubId, false, false);
        this.mMobileDataToggle.setChecked(false);
        Prefs.putBoolean(this.mContext, "QsHasTurnedOffMobileData", true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showTurnOffMobileDialog$11(DialogInterface dialogInterface) {
        this.mMobileDataToggle.setChecked(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onRefreshCarrierInfo$12() {
        updateDialog(true);
    }

    public void onRefreshCarrierInfo() {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSimStateChanged$13() {
        updateDialog(true);
    }

    public void onSimStateChanged() {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda11(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCapabilitiesChanged$14() {
        updateDialog(true);
    }

    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda12(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onLost$15() {
        updateDialog(true);
    }

    public void onLost(Network network) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda10(this));
    }

    public void onSubscriptionsChanged(int i) {
        this.mDefaultDataSubId = i;
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda15(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSubscriptionsChanged$16() {
        updateDialog(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserMobileDataStateChanged$17() {
        updateDialog(true);
    }

    public void onUserMobileDataStateChanged(boolean z) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda20(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onServiceStateChanged$18() {
        updateDialog(true);
    }

    public void onServiceStateChanged(ServiceState serviceState) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda16(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDataConnectionStateChanged$19() {
        updateDialog(true);
    }

    public void onDataConnectionStateChanged(int i, int i2) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda19(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSignalStrengthsChanged$20() {
        updateDialog(true);
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda14(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayInfoChanged$21() {
        updateDialog(true);
    }

    public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda17(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNonDdsCallStateChanged$22() {
        updateDialog(true);
    }

    public void onNonDdsCallStateChanged(int i) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda13(this));
    }

    public void onAccessPointsChanged(List<WifiEntry> list, WifiEntry wifiEntry, boolean z) {
        this.mHandler.post(new InternetDialog$$ExternalSyntheticLambda18(this, wifiEntry, list, z, this.mMobileNetworkLayout.getVisibility() == 0 && this.mInternetDialogController.isAirplaneModeEnabled()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAccessPointsChanged$23(WifiEntry wifiEntry, List list, boolean z, boolean z2) {
        int i;
        this.mConnectedWifiEntry = wifiEntry;
        if (list == null) {
            i = 0;
        } else {
            i = list.size();
        }
        this.mWifiEntriesCount = i;
        this.mHasMoreWifiEntries = z;
        updateDialog(z2);
        this.mAdapter.setWifiEntries(list, this.mWifiEntriesCount);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null && !alertDialog.isShowing() && !z && isShowing()) {
            dismiss();
        }
    }

    public enum InternetDialogEvent implements UiEventLogger.UiEventEnum {
        INTERNET_DIALOG_SHOW(843);
        
        private final int mId;

        /* access modifiers changed from: public */
        InternetDialogEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
