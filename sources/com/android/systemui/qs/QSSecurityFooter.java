package com.android.systemui.qs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.util.ViewController;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class QSSecurityFooter extends ViewController<View> implements View.OnClickListener, DialogInterface.OnClickListener {
    public static final boolean DEBUG = Log.isLoggable("QSSecurityFooter", 3);
    public final ActivityStarter mActivityStarter;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Callback mCallback = new Callback();
    public Context mContext;
    public AlertDialog mDialog;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final DevicePolicyManager mDpm;
    public int mFooterIconId = R$drawable.ic_info_outline;
    public final TextView mFooterText = ((TextView) this.mView.findViewById(R$id.footer_text));
    public CharSequence mFooterTextContent = null;
    public H mHandler;
    public boolean mIsVisible;
    public final Handler mMainHandler;
    public Supplier<String> mManagementDialogCaCertStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda8(this);
    public Supplier<String> mManagementDialogNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda10(this);
    public Supplier<String> mManagementDialogStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda7(this);
    public Supplier<String> mManagementMessageSupplier = new QSSecurityFooter$$ExternalSyntheticLambda12(this);
    public Supplier<String> mManagementMonitoringStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda13(this);
    public Supplier<String> mManagementMultipleVpnStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda14(this);
    public Supplier<String> mManagementTitleSupplier = new QSSecurityFooter$$ExternalSyntheticLambda6(this);
    public Supplier<String> mMonitoringSubtitleCaCertStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda17(this);
    public Supplier<String> mMonitoringSubtitleNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda18(this);
    public Supplier<String> mMonitoringSubtitleVpnStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda19(this);
    public final ImageView mPrimaryFooterIcon = ((ImageView) this.mView.findViewById(R$id.primary_footer_icon));
    public Drawable mPrimaryFooterIconDrawable;
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.app.action.SHOW_DEVICE_MONITORING_DIALOG")) {
                QSSecurityFooter.this.showDeviceMonitoringDialog();
            }
        }
    };
    public final SecurityController mSecurityController;
    public final AtomicBoolean mShouldUseSettingsButton = new AtomicBoolean(false);
    public final Runnable mUpdateDisplayState = new Runnable() {
        public void run() {
            if (QSSecurityFooter.this.mFooterTextContent != null) {
                QSSecurityFooter.this.mFooterText.setText(QSSecurityFooter.this.mFooterTextContent);
            }
            QSSecurityFooter.this.mView.setVisibility(!QSSecurityFooter.this.mIsVisible ? 8 : 0);
            if (QSSecurityFooter.this.mVisibilityChangedListener != null) {
                QSSecurityFooter.this.mVisibilityChangedListener.onVisibilityChanged(QSSecurityFooter.this.mView.getVisibility());
            }
        }
    };
    public final Runnable mUpdatePrimaryIcon = new Runnable() {
        public void run() {
            if (QSSecurityFooter.this.mPrimaryFooterIconDrawable != null) {
                QSSecurityFooter.this.mPrimaryFooterIcon.setImageDrawable(QSSecurityFooter.this.mPrimaryFooterIconDrawable);
            } else {
                QSSecurityFooter.this.mPrimaryFooterIcon.setImageResource(QSSecurityFooter.this.mFooterIconId);
            }
        }
    };
    public final UserTracker mUserTracker;
    public Supplier<String> mViewPoliciesButtonStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda20(this);
    public VisibilityChangedDispatcher$OnVisibilityChangedListener mVisibilityChangedListener;
    public Supplier<String> mWorkProfileDialogCaCertStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda9(this);
    public Supplier<String> mWorkProfileDialogNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda11(this);
    public Supplier<String> mWorkProfileMonitoringStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda15(this);
    public Supplier<String> mWorkProfileNetworkStringSupplier = new QSSecurityFooter$$ExternalSyntheticLambda16(this);

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$0() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_title_device_owned);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$1() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.quick_settings_disclosure_management);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$2() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.quick_settings_disclosure_management_monitoring);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$3() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.quick_settings_disclosure_management_vpns);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$4() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.quick_settings_disclosure_managed_profile_monitoring);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$5() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.quick_settings_disclosure_managed_profile_network_activity);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$6() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_subtitle_ca_certificate);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$7() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_subtitle_network_logging);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$8() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_subtitle_vpn);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$9() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_button_view_policies);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$10() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_description_management);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$11() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_description_management_ca_certificate);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$12() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_description_managed_profile_ca_certificate);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$13() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_description_management_network_logging);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$new$14() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return context.getString(R$string.monitoring_description_managed_profile_network_logging);
    }

    public QSSecurityFooter(View view, UserTracker userTracker, Handler handler, ActivityStarter activityStarter, SecurityController securityController, DialogLaunchAnimator dialogLaunchAnimator, Looper looper, BroadcastDispatcher broadcastDispatcher) {
        super(view);
        this.mContext = view.getContext();
        this.mDpm = (DevicePolicyManager) view.getContext().getSystemService(DevicePolicyManager.class);
        this.mMainHandler = handler;
        this.mActivityStarter = activityStarter;
        this.mSecurityController = securityController;
        this.mHandler = new H(looper);
        this.mUserTracker = userTracker;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    public void onViewAttached() {
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mReceiver, new IntentFilter("android.app.action.SHOW_DEVICE_MONITORING_DIALOG"), this.mHandler, UserHandle.ALL);
        this.mView.setOnClickListener(this);
    }

    public void onViewDetached() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
        this.mView.setOnClickListener((View.OnClickListener) null);
    }

    public void setListening(boolean z) {
        if (z) {
            this.mSecurityController.addCallback(this.mCallback);
            refreshState();
            return;
        }
        this.mSecurityController.removeCallback(this.mCallback);
    }

    public void setOnVisibilityChangedListener(VisibilityChangedDispatcher$OnVisibilityChangedListener visibilityChangedDispatcher$OnVisibilityChangedListener) {
        this.mVisibilityChangedListener = visibilityChangedDispatcher$OnVisibilityChangedListener;
    }

    public View getView() {
        return this.mView;
    }

    public boolean hasFooter() {
        return this.mView.getVisibility() != 8;
    }

    public void onClick(View view) {
        if (hasFooter()) {
            this.mHandler.sendEmptyMessage(0);
        }
    }

    public final void handleClick() {
        showDeviceMonitoringDialog();
        DevicePolicyEventLogger.createEvent(57).write();
    }

    public void showDeviceMonitoringDialog() {
        createDialog();
    }

    public void refreshState() {
        this.mHandler.sendEmptyMessage(1);
    }

    public final void handleRefreshState() {
        boolean isDeviceManaged = this.mSecurityController.isDeviceManaged();
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        boolean z = UserManager.isDeviceInDemoMode(this.mContext) && userInfo != null && userInfo.isDemo();
        boolean hasWorkProfile = this.mSecurityController.hasWorkProfile();
        boolean hasCACertInCurrentUser = this.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = this.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = this.mSecurityController.isNetworkLoggingEnabled();
        String primaryVpnName = this.mSecurityController.getPrimaryVpnName();
        String workProfileVpnName = this.mSecurityController.getWorkProfileVpnName();
        CharSequence deviceOwnerOrganizationName = this.mSecurityController.getDeviceOwnerOrganizationName();
        CharSequence workProfileOrganizationName = this.mSecurityController.getWorkProfileOrganizationName();
        boolean isProfileOwnerOfOrganizationOwnedDevice = this.mSecurityController.isProfileOwnerOfOrganizationOwnedDevice();
        boolean isParentalControlsEnabled = this.mSecurityController.isParentalControlsEnabled();
        boolean isWorkProfileOn = this.mSecurityController.isWorkProfileOn();
        boolean z2 = hasCACertInWorkProfile || workProfileVpnName != null || (hasWorkProfile && isNetworkLoggingEnabled);
        boolean z3 = (isDeviceManaged && !z) || hasCACertInCurrentUser || primaryVpnName != null || isProfileOwnerOfOrganizationOwnedDevice || isParentalControlsEnabled || (z2 && isWorkProfileOn);
        this.mIsVisible = z3;
        if (!z3 || !isProfileOwnerOfOrganizationOwnedDevice || (z2 && isWorkProfileOn)) {
            this.mView.setClickable(true);
            this.mView.findViewById(R$id.footer_icon).setVisibility(0);
        } else {
            this.mView.setClickable(false);
            this.mView.findViewById(R$id.footer_icon).setVisibility(8);
        }
        this.mFooterTextContent = getFooterText(isDeviceManaged, hasWorkProfile, hasCACertInCurrentUser, hasCACertInWorkProfile, isNetworkLoggingEnabled, primaryVpnName, workProfileVpnName, deviceOwnerOrganizationName, workProfileOrganizationName, isProfileOwnerOfOrganizationOwnedDevice, isParentalControlsEnabled, isWorkProfileOn);
        int i = R$drawable.ic_info_outline;
        if (!(primaryVpnName == null && workProfileVpnName == null)) {
            if (this.mSecurityController.isVpnBranded()) {
                i = R$drawable.stat_sys_branded_vpn;
            } else {
                i = R$drawable.stat_sys_vpn_ic;
            }
        }
        if (this.mFooterIconId != i) {
            this.mFooterIconId = i;
        }
        if (!isParentalControlsEnabled) {
            this.mPrimaryFooterIconDrawable = null;
        } else if (this.mPrimaryFooterIconDrawable == null) {
            this.mPrimaryFooterIconDrawable = this.mSecurityController.getIcon(this.mSecurityController.getDeviceAdminInfo());
        }
        this.mMainHandler.post(this.mUpdatePrimaryIcon);
        this.mMainHandler.post(this.mUpdateDisplayState);
    }

    public CharSequence getFooterText(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String str, String str2, CharSequence charSequence, CharSequence charSequence2, boolean z6, boolean z7, boolean z8) {
        if (z7) {
            return this.mContext.getString(R$string.quick_settings_disclosure_parental_controls);
        }
        if (!z) {
            return getManagedAndPersonalProfileFooterText(z2, z3, z4, z5, str, str2, charSequence2, z6, z8);
        }
        return getManagedDeviceFooterText(z3, z4, z5, str, str2, charSequence);
    }

    public final String getManagedDeviceFooterText(boolean z, boolean z2, boolean z3, String str, String str2, CharSequence charSequence) {
        if (z || z2 || z3) {
            return getManagedDeviceMonitoringText(charSequence);
        }
        if (str == null && str2 == null) {
            return getMangedDeviceGeneralText(charSequence);
        }
        return getManagedDeviceVpnText(str, str2, charSequence);
    }

    public final String getManagedDeviceMonitoringText(CharSequence charSequence) {
        if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT_MONITORING", this.mManagementMonitoringStringSupplier);
        }
        return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_MONITORING", new QSSecurityFooter$$ExternalSyntheticLambda26(this, charSequence), new Object[]{charSequence});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getManagedDeviceMonitoringText$15(CharSequence charSequence) {
        return this.mContext.getString(R$string.quick_settings_disclosure_named_management_monitoring, new Object[]{charSequence});
    }

    public final String getManagedDeviceVpnText(String str, String str2, CharSequence charSequence) {
        if (str == null || str2 == null) {
            if (str == null) {
                str = str2;
            }
            if (charSequence == null) {
                return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda24(this, str), new Object[]{str});
            }
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda25(this, charSequence, str), new Object[]{charSequence, str});
        } else if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT_MULTIPLE_VPNS", this.mManagementMultipleVpnStringSupplier);
        } else {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT_MULTIPLE_VPNS", new QSSecurityFooter$$ExternalSyntheticLambda23(this, charSequence), new Object[]{charSequence});
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getManagedDeviceVpnText$16(CharSequence charSequence) {
        return this.mContext.getString(R$string.quick_settings_disclosure_named_management_vpns, new Object[]{charSequence});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getManagedDeviceVpnText$17(String str) {
        return this.mContext.getString(R$string.quick_settings_disclosure_management_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getManagedDeviceVpnText$18(CharSequence charSequence, String str) {
        return this.mContext.getString(R$string.quick_settings_disclosure_named_management_named_vpn, new Object[]{charSequence, str});
    }

    public final String getMangedDeviceGeneralText(CharSequence charSequence) {
        if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_MANAGEMENT", this.mManagementMessageSupplier);
        }
        if (isFinancedDevice()) {
            return this.mContext.getString(R$string.quick_settings_financed_disclosure_named_management, new Object[]{charSequence});
        }
        return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_MANAGEMENT", new QSSecurityFooter$$ExternalSyntheticLambda27(this, charSequence), new Object[]{charSequence});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getMangedDeviceGeneralText$19(CharSequence charSequence) {
        return this.mContext.getString(R$string.quick_settings_disclosure_named_management, new Object[]{charSequence});
    }

    public final String getManagedAndPersonalProfileFooterText(boolean z, boolean z2, boolean z3, boolean z4, String str, String str2, CharSequence charSequence, boolean z5, boolean z6) {
        if (z2 || (z3 && z6)) {
            return getMonitoringText(z2, z3, charSequence, z6);
        }
        if (str != null || (str2 != null && z6)) {
            return getVpnText(z, str, str2, z6);
        }
        if (z && z4 && z6) {
            return getManagedProfileNetworkActivityText();
        }
        if (z5) {
            return getMangedDeviceGeneralText(charSequence);
        }
        return null;
    }

    public final String getMonitoringText(boolean z, boolean z2, CharSequence charSequence, boolean z3) {
        if (!z2 || !z3) {
            if (z) {
                return this.mContext.getString(R$string.quick_settings_disclosure_monitoring);
            }
            return null;
        } else if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_WORK_PROFILE_MONITORING", this.mWorkProfileMonitoringStringSupplier);
        } else {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_NAMED_WORK_PROFILE_MONITORING", new QSSecurityFooter$$ExternalSyntheticLambda28(this, charSequence), new Object[]{charSequence});
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getMonitoringText$20(CharSequence charSequence) {
        return this.mContext.getString(R$string.quick_settings_disclosure_named_managed_profile_monitoring, new Object[]{charSequence});
    }

    public final String getVpnText(boolean z, String str, String str2, boolean z2) {
        if (str != null && str2 != null) {
            return this.mContext.getString(R$string.quick_settings_disclosure_vpns);
        }
        if (str2 != null && z2) {
            return this.mDpm.getResources().getString("SystemUi.QS_MSG_WORK_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda29(this, str2), new Object[]{str2});
        } else if (str == null) {
            return null;
        } else {
            if (z) {
                return this.mDpm.getResources().getString("SystemUi.QS_MSG_PERSONAL_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda30(this, str), new Object[]{str});
            }
            return this.mContext.getString(R$string.quick_settings_disclosure_named_vpn, new Object[]{str});
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getVpnText$21(String str) {
        return this.mContext.getString(R$string.quick_settings_disclosure_managed_profile_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getVpnText$22(String str) {
        return this.mContext.getString(R$string.quick_settings_disclosure_personal_profile_named_vpn, new Object[]{str});
    }

    public final String getManagedProfileNetworkActivityText() {
        return this.mDpm.getResources().getString("SystemUi.QS_MSG_WORK_PROFILE_NETWORK", this.mWorkProfileNetworkStringSupplier);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            Intent intent = new Intent("android.settings.ENTERPRISE_PRIVACY_SETTINGS");
            dialogInterface.dismiss();
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }
    }

    public final void createDialog() {
        this.mShouldUseSettingsButton.set(false);
        this.mHandler.post(new QSSecurityFooter$$ExternalSyntheticLambda21(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createDialog$24() {
        this.mMainHandler.post(new QSSecurityFooter$$ExternalSyntheticLambda22(this, getSettingsButton(), createDialogView()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createDialog$23(String str, View view) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext, 0);
        this.mDialog = systemUIDialog;
        systemUIDialog.requestWindowFeature(1);
        this.mDialog.setButton(-1, getPositiveButton(), this);
        AlertDialog alertDialog = this.mDialog;
        if (!this.mShouldUseSettingsButton.get()) {
            str = getNegativeButton();
        }
        alertDialog.setButton(-2, str, this);
        this.mDialog.setView(view);
        if (this.mView.isAggregatedVisible()) {
            this.mDialogLaunchAnimator.showFromView(this.mDialog, this.mView);
        } else {
            this.mDialog.show();
        }
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public View createDialogView() {
        if (this.mSecurityController.isParentalControlsEnabled()) {
            return createParentalControlsDialogView();
        }
        return createOrganizationDialogView();
    }

    public final View createOrganizationDialogView() {
        boolean isDeviceManaged = this.mSecurityController.isDeviceManaged();
        boolean hasWorkProfile = this.mSecurityController.hasWorkProfile();
        CharSequence deviceOwnerOrganizationName = this.mSecurityController.getDeviceOwnerOrganizationName();
        boolean hasCACertInCurrentUser = this.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = this.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = this.mSecurityController.isNetworkLoggingEnabled();
        String primaryVpnName = this.mSecurityController.getPrimaryVpnName();
        String workProfileVpnName = this.mSecurityController.getWorkProfileVpnName();
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.quick_settings_footer_dialog, (ViewGroup) null, false);
        ((TextView) inflate.findViewById(R$id.device_management_subtitle)).setText(getManagementTitle(deviceOwnerOrganizationName));
        CharSequence managementMessage = getManagementMessage(isDeviceManaged, deviceOwnerOrganizationName);
        boolean z = true;
        if (managementMessage == null) {
            inflate.findViewById(R$id.device_management_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.device_management_disclosures).setVisibility(0);
            ((TextView) inflate.findViewById(R$id.device_management_warning)).setText(managementMessage);
            this.mShouldUseSettingsButton.set(true);
        }
        CharSequence caCertsMessage = getCaCertsMessage(isDeviceManaged, hasCACertInCurrentUser, hasCACertInWorkProfile);
        if (caCertsMessage == null) {
            inflate.findViewById(R$id.ca_certs_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.ca_certs_disclosures).setVisibility(0);
            TextView textView = (TextView) inflate.findViewById(R$id.ca_certs_warning);
            textView.setText(caCertsMessage);
            textView.setMovementMethod(new LinkMovementMethod());
            ((TextView) inflate.findViewById(R$id.ca_certs_subtitle)).setText(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MONITORING_CA_CERT_SUBTITLE", this.mMonitoringSubtitleCaCertStringSupplier));
        }
        CharSequence networkLoggingMessage = getNetworkLoggingMessage(isDeviceManaged, isNetworkLoggingEnabled);
        if (networkLoggingMessage == null) {
            inflate.findViewById(R$id.network_logging_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.network_logging_disclosures).setVisibility(0);
            ((TextView) inflate.findViewById(R$id.network_logging_warning)).setText(networkLoggingMessage);
            ((TextView) inflate.findViewById(R$id.network_logging_subtitle)).setText(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MONITORING_NETWORK_SUBTITLE", this.mMonitoringSubtitleNetworkStringSupplier));
        }
        CharSequence vpnMessage = getVpnMessage(isDeviceManaged, hasWorkProfile, primaryVpnName, workProfileVpnName);
        if (vpnMessage == null) {
            inflate.findViewById(R$id.vpn_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.vpn_disclosures).setVisibility(0);
            TextView textView2 = (TextView) inflate.findViewById(R$id.vpn_warning);
            textView2.setText(vpnMessage);
            textView2.setMovementMethod(new LinkMovementMethod());
            ((TextView) inflate.findViewById(R$id.vpn_subtitle)).setText(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MONITORING_VPN_SUBTITLE", this.mMonitoringSubtitleVpnStringSupplier));
        }
        boolean z2 = managementMessage != null;
        boolean z3 = caCertsMessage != null;
        boolean z4 = networkLoggingMessage != null;
        if (vpnMessage == null) {
            z = false;
        }
        configSubtitleVisibility(z2, z3, z4, z, inflate);
        return inflate;
    }

    public final View createParentalControlsDialogView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.quick_settings_footer_dialog_parental_controls, (ViewGroup) null, false);
        DeviceAdminInfo deviceAdminInfo = this.mSecurityController.getDeviceAdminInfo();
        Drawable icon = this.mSecurityController.getIcon(deviceAdminInfo);
        if (icon != null) {
            ((ImageView) inflate.findViewById(R$id.parental_controls_icon)).setImageDrawable(icon);
        }
        ((TextView) inflate.findViewById(R$id.parental_controls_title)).setText(this.mSecurityController.getLabel(deviceAdminInfo));
        return inflate;
    }

    public void configSubtitleVisibility(boolean z, boolean z2, boolean z3, boolean z4, View view) {
        if (!z) {
            int i = z3 ? (z2 ? 1 : 0) + true : z2;
            if (z4) {
                i++;
            }
            if (i == 1) {
                if (z2) {
                    view.findViewById(R$id.ca_certs_subtitle).setVisibility(8);
                }
                if (z3) {
                    view.findViewById(R$id.network_logging_subtitle).setVisibility(8);
                }
                if (z4) {
                    view.findViewById(R$id.vpn_subtitle).setVisibility(8);
                }
            }
        }
    }

    public String getSettingsButton() {
        return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_VIEW_POLICIES", this.mViewPoliciesButtonStringSupplier);
    }

    public final String getPositiveButton() {
        return this.mContext.getString(R$string.ok);
    }

    public final String getNegativeButton() {
        if (this.mSecurityController.isParentalControlsEnabled()) {
            return this.mContext.getString(R$string.monitoring_button_view_controls);
        }
        return null;
    }

    public CharSequence getManagementMessage(boolean z, CharSequence charSequence) {
        if (!z) {
            return null;
        }
        if (charSequence == null) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT", this.mManagementDialogStringSupplier);
        }
        if (isFinancedDevice()) {
            return this.mContext.getString(R$string.monitoring_financed_description_named_management, new Object[]{charSequence, charSequence});
        }
        return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_NAMED_MANAGEMENT", new QSSecurityFooter$$ExternalSyntheticLambda0(this, charSequence), new Object[]{charSequence});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getManagementMessage$25(CharSequence charSequence) {
        return this.mContext.getString(R$string.monitoring_description_named_management, new Object[]{charSequence});
    }

    public CharSequence getCaCertsMessage(boolean z, boolean z2, boolean z3) {
        if (!z2 && !z3) {
            return null;
        }
        if (z) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_CA_CERT", this.mManagementDialogCaCertStringSupplier);
        }
        if (z3) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_WORK_PROFILE_CA_CERT", this.mWorkProfileDialogCaCertStringSupplier);
        }
        return this.mContext.getString(R$string.monitoring_description_ca_certificate);
    }

    public CharSequence getNetworkLoggingMessage(boolean z, boolean z2) {
        if (!z2) {
            return null;
        }
        if (z) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_NETWORK", this.mManagementDialogNetworkStringSupplier);
        }
        return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_WORK_PROFILE_NETWORK", this.mWorkProfileDialogNetworkStringSupplier);
    }

    public CharSequence getVpnMessage(boolean z, boolean z2, String str, String str2) {
        if (str == null && str2 == null) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (z) {
            if (str == null || str2 == null) {
                if (str == null) {
                    str = str2;
                }
                spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda2(this, str), new Object[]{str}));
            } else {
                spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_TWO_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda1(this, str, str2), new Object[]{str, str2}));
            }
        } else if (str != null && str2 != null) {
            spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_TWO_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda3(this, str, str2), new Object[]{str, str2}));
        } else if (str2 != null) {
            spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_WORK_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda4(this, str2), new Object[]{str2}));
        } else if (z2) {
            spannableStringBuilder.append(this.mDpm.getResources().getString("SystemUi.QS_DIALOG_PERSONAL_PROFILE_NAMED_VPN", new QSSecurityFooter$$ExternalSyntheticLambda5(this, str), new Object[]{str}));
        } else {
            spannableStringBuilder.append(this.mContext.getString(R$string.monitoring_description_named_vpn, new Object[]{str}));
        }
        spannableStringBuilder.append(this.mContext.getString(R$string.monitoring_description_vpn_settings_separator));
        spannableStringBuilder.append(this.mContext.getString(R$string.monitoring_description_vpn_settings), new VpnSpan(), 0);
        return spannableStringBuilder;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getVpnMessage$26(String str, String str2) {
        return this.mContext.getString(R$string.monitoring_description_two_named_vpns, new Object[]{str, str2});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getVpnMessage$27(String str) {
        return this.mContext.getString(R$string.monitoring_description_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getVpnMessage$28(String str, String str2) {
        return this.mContext.getString(R$string.monitoring_description_two_named_vpns, new Object[]{str, str2});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getVpnMessage$29(String str) {
        return this.mContext.getString(R$string.monitoring_description_managed_profile_named_vpn, new Object[]{str});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getVpnMessage$30(String str) {
        return this.mContext.getString(R$string.monitoring_description_personal_profile_named_vpn, new Object[]{str});
    }

    public CharSequence getManagementTitle(CharSequence charSequence) {
        if (charSequence == null || !isFinancedDevice()) {
            return this.mDpm.getResources().getString("SystemUi.QS_DIALOG_MANAGEMENT_TITLE", this.mManagementTitleSupplier);
        }
        return this.mContext.getString(R$string.monitoring_title_financed_device, new Object[]{charSequence});
    }

    public final boolean isFinancedDevice() {
        if (this.mSecurityController.isDeviceManaged()) {
            SecurityController securityController = this.mSecurityController;
            if (securityController.getDeviceOwnerType(securityController.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return true;
            }
        }
        return false;
    }

    public class Callback implements SecurityController.SecurityControllerCallback {
        public Callback() {
        }

        public void onStateChanged() {
            QSSecurityFooter.this.refreshState();
        }
    }

    public class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            try {
                int i = message.what;
                if (i == 1) {
                    QSSecurityFooter.this.handleRefreshState();
                } else if (i == 0) {
                    QSSecurityFooter.this.handleClick();
                }
            } catch (Throwable th) {
                Log.w("QSSecurityFooter", "Error in " + null, th);
            }
        }
    }

    public class VpnSpan extends ClickableSpan {
        public int hashCode() {
            return 314159257;
        }

        public VpnSpan() {
        }

        public void onClick(View view) {
            Intent intent = new Intent("android.settings.VPN_SETTINGS");
            QSSecurityFooter.this.mDialog.dismiss();
            QSSecurityFooter.this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }

        public boolean equals(Object obj) {
            return obj instanceof VpnSpan;
        }
    }
}
