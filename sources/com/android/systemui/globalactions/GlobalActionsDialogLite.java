package com.android.systemui.globalactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.sysprop.TelephonyProperties;
import android.telecom.TelecomManager;
import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import android.util.ArraySet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.EmergencyAffordanceManager;
import com.android.internal.util.ScreenshotHelper;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.MultiListLayout;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.plugins.GlobalActionsPanelPlugin;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class GlobalActionsDialogLite implements DialogInterface.OnDismissListener, DialogInterface.OnShowListener, ConfigurationController.ConfigurationListener, GlobalActionsPanelPlugin.Callbacks, LifecycleOwner {
    @VisibleForTesting
    public static final String GLOBAL_ACTION_KEY_POWER = "power";
    public MyAdapter mAdapter;
    public final ContentObserver mAirplaneModeObserver;
    public ToggleAction mAirplaneModeOn;
    public ToggleState mAirplaneState = ToggleState.Off;
    public final AudioManager mAudioManager;
    public final Executor mBackgroundExecutor;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) || "android.intent.action.SCREEN_OFF".equals(action)) {
                String stringExtra = intent.getStringExtra("reason");
                if (!"globalactions".equals(stringExtra)) {
                    GlobalActionsDialogLite.this.mHandler.sendMessage(GlobalActionsDialogLite.this.mHandler.obtainMessage(0, stringExtra));
                }
            } else if ("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED".equals(action) && !intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false) && GlobalActionsDialogLite.this.mIsWaitingForEcmExit) {
                GlobalActionsDialogLite.this.mIsWaitingForEcmExit = false;
                GlobalActionsDialogLite.this.changeAirplaneModeSystemSetting(true);
            }
        }
    };
    public final Optional<CentralSurfaces> mCentralSurfacesOptional;
    public final ConfigurationController mConfigurationController;
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public boolean mDeviceProvisioned = false;
    @VisibleForTesting
    public ActionsDialogLite mDialog;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public int mDialogPressDelay = 850;
    public final IDreamManager mDreamManager;
    public final EmergencyAffordanceManager mEmergencyAffordanceManager;
    public final GlobalSettings mGlobalSettings;
    public Handler mHandler;
    public boolean mHasTelephony;
    public boolean mHasVibrator;
    public final IActivityManager mIActivityManager;
    public final IWindowManager mIWindowManager;
    public boolean mIsWaitingForEcmExit = false;
    @VisibleForTesting
    public final ArrayList<Action> mItems = new ArrayList<>();
    public boolean mKeyguardShowing = false;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    public final LockPatternUtils mLockPatternUtils;
    public Handler mMainHandler;
    public final MetricsLogger mMetricsLogger;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public MyOverflowAdapter mOverflowAdapter;
    @VisibleForTesting
    public final ArrayList<Action> mOverflowItems = new ArrayList<>();
    public final TelephonyCallback.ServiceStateListener mPhoneStateListener;
    public MyPowerOptionsAdapter mPowerAdapter;
    @VisibleForTesting
    public final ArrayList<Action> mPowerItems = new ArrayList<>();
    public final Resources mResources;
    public final RingerModeTracker mRingerModeTracker;
    public final ScreenshotHelper mScreenshotHelper;
    public final SecureSettings mSecureSettings;
    public final boolean mShowSilentToggle;
    public Action mSilentModeAction;
    public int mSmallestScreenWidthDp;
    public final IStatusBarService mStatusBarService;
    public final SysuiColorExtractor mSysuiColorExtractor;
    public final TelecomManager mTelecomManager;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public final TrustManager mTrustManager;
    public final UiEventLogger mUiEventLogger;
    public final UserManager mUserManager;
    public final GlobalActions.GlobalActionsManager mWindowManagerFuncs;

    public interface Action {
        View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater);

        Drawable getIcon(Context context);

        CharSequence getMessage();

        int getMessageResId();

        boolean isEnabled();

        void onPress();

        boolean shouldBeSeparated() {
            return false;
        }

        boolean shouldShow() {
            return true;
        }

        boolean showBeforeProvisioning();

        boolean showDuringKeyguard();
    }

    public interface LongPressAction extends Action {
        boolean onLongPress();
    }

    @VisibleForTesting
    public enum GlobalActionsEvent implements UiEventLogger.UiEventEnum {
        GA_POWER_MENU_OPEN(337),
        GA_POWER_MENU_CLOSE(471),
        GA_BUGREPORT_PRESS(344),
        GA_BUGREPORT_LONG_PRESS(345),
        GA_EMERGENCY_DIALER_PRESS(346),
        GA_SCREENSHOT_PRESS(347),
        GA_SCREENSHOT_LONG_PRESS(348),
        GA_SHUTDOWN_PRESS(802),
        GA_SHUTDOWN_LONG_PRESS(803),
        GA_REBOOT_PRESS(349),
        GA_REBOOT_LONG_PRESS(804),
        GA_LOCKDOWN_PRESS(354),
        GA_OPEN_QS(805),
        GA_OPEN_POWER_VOLUP(806),
        GA_OPEN_LONG_PRESS_POWER(807),
        GA_CLOSE_LONG_PRESS_POWER(808),
        GA_CLOSE_BACK(809),
        GA_CLOSE_TAP_OUTSIDE(810),
        GA_CLOSE_POWER_VOLUP(811);
        
        private final int mId;

        /* access modifiers changed from: public */
        GlobalActionsEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public GlobalActionsDialogLite(Context context, GlobalActions.GlobalActionsManager globalActionsManager, AudioManager audioManager, IDreamManager iDreamManager, DevicePolicyManager devicePolicyManager, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, TelephonyListenerManager telephonyListenerManager, GlobalSettings globalSettings, SecureSettings secureSettings, VibratorHelper vibratorHelper, Resources resources, ConfigurationController configurationController, KeyguardStateController keyguardStateController, UserManager userManager, TrustManager trustManager, IActivityManager iActivityManager, TelecomManager telecomManager, MetricsLogger metricsLogger, SysuiColorExtractor sysuiColorExtractor, IStatusBarService iStatusBarService, NotificationShadeWindowController notificationShadeWindowController, IWindowManager iWindowManager, Executor executor, UiEventLogger uiEventLogger, RingerModeTracker ringerModeTracker, Handler handler, PackageManager packageManager, Optional<CentralSurfaces> optional, KeyguardUpdateMonitor keyguardUpdateMonitor, DialogLaunchAnimator dialogLaunchAnimator) {
        BroadcastDispatcher broadcastDispatcher2 = broadcastDispatcher;
        TelephonyListenerManager telephonyListenerManager2 = telephonyListenerManager;
        GlobalSettings globalSettings2 = globalSettings;
        Resources resources2 = resources;
        ConfigurationController configurationController2 = configurationController;
        AnonymousClass6 r7 = new TelephonyCallback.ServiceStateListener() {
            public void onServiceStateChanged(ServiceState serviceState) {
                if (GlobalActionsDialogLite.this.mHasTelephony) {
                    if (GlobalActionsDialogLite.this.mAirplaneModeOn == null) {
                        Log.d("GlobalActionsDialogLite", "Service changed before actions created");
                        return;
                    }
                    GlobalActionsDialogLite.this.mAirplaneState = serviceState.getState() == 3 ? ToggleState.On : ToggleState.Off;
                    GlobalActionsDialogLite.this.mAirplaneModeOn.updateState(GlobalActionsDialogLite.this.mAirplaneState);
                    GlobalActionsDialogLite.this.mAdapter.notifyDataSetChanged();
                    GlobalActionsDialogLite.this.mOverflowAdapter.notifyDataSetChanged();
                    GlobalActionsDialogLite.this.mPowerAdapter.notifyDataSetChanged();
                }
            }
        };
        this.mPhoneStateListener = r7;
        AnonymousClass7 r8 = new ContentObserver(this.mMainHandler) {
            public void onChange(boolean z) {
                GlobalActionsDialogLite.this.onAirplaneModeChanged();
            }
        };
        this.mAirplaneModeObserver = r8;
        this.mHandler = new Handler() {
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 0) {
                    if (i == 1) {
                        GlobalActionsDialogLite.this.refreshSilentMode();
                        GlobalActionsDialogLite.this.mAdapter.notifyDataSetChanged();
                    }
                } else if (GlobalActionsDialogLite.this.mDialog != null) {
                    if ("dream".equals(message.obj)) {
                        GlobalActionsDialogLite.this.mDialog.hide();
                        GlobalActionsDialogLite.this.mDialog.dismiss();
                    } else {
                        GlobalActionsDialogLite.this.mDialog.dismiss();
                    }
                    GlobalActionsDialogLite.this.mDialog = null;
                }
            }
        };
        this.mContext = context;
        this.mWindowManagerFuncs = globalActionsManager;
        this.mAudioManager = audioManager;
        this.mDreamManager = iDreamManager;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mLockPatternUtils = lockPatternUtils;
        this.mTelephonyListenerManager = telephonyListenerManager2;
        this.mKeyguardStateController = keyguardStateController;
        this.mBroadcastDispatcher = broadcastDispatcher2;
        this.mGlobalSettings = globalSettings2;
        this.mSecureSettings = secureSettings;
        this.mResources = resources2;
        this.mConfigurationController = configurationController2;
        this.mUserManager = userManager;
        this.mTrustManager = trustManager;
        this.mIActivityManager = iActivityManager;
        this.mTelecomManager = telecomManager;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mSysuiColorExtractor = sysuiColorExtractor;
        this.mStatusBarService = iStatusBarService;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mIWindowManager = iWindowManager;
        this.mBackgroundExecutor = executor;
        this.mRingerModeTracker = ringerModeTracker;
        this.mMainHandler = handler;
        this.mSmallestScreenWidthDp = resources.getConfiguration().smallestScreenWidthDp;
        this.mCentralSurfacesOptional = optional;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        broadcastDispatcher2.registerReceiver(this.mBroadcastReceiver, intentFilter);
        this.mHasTelephony = packageManager.hasSystemFeature("android.hardware.telephony");
        telephonyListenerManager2.addServiceStateListener(r7);
        globalSettings2.registerContentObserver(Settings.Global.getUriFor("airplane_mode_on"), true, r8);
        this.mHasVibrator = vibratorHelper.hasVibrator();
        boolean z = !resources2.getBoolean(17891815);
        this.mShowSilentToggle = z;
        if (z) {
            ringerModeTracker.getRingerMode().observe(this, new GlobalActionsDialogLite$$ExternalSyntheticLambda0(this));
        }
        this.mEmergencyAffordanceManager = new EmergencyAffordanceManager(context);
        this.mScreenshotHelper = new ScreenshotHelper(context);
        configurationController2.addCallback(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Integer num) {
        this.mHandler.sendEmptyMessage(1);
    }

    public void destroy() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
        this.mTelephonyListenerManager.removeServiceStateListener(this.mPhoneStateListener);
        this.mGlobalSettings.unregisterContentObserver(this.mAirplaneModeObserver);
        this.mConfigurationController.removeCallback(this);
    }

    public void showOrHideDialog(boolean z, boolean z2, View view) {
        this.mKeyguardShowing = z;
        this.mDeviceProvisioned = z2;
        ActionsDialogLite actionsDialogLite = this.mDialog;
        if (actionsDialogLite == null || !actionsDialogLite.isShowing()) {
            handleShow(view);
            return;
        }
        this.mWindowManagerFuncs.onGlobalActionsShown();
        this.mDialog.dismiss();
        this.mDialog = null;
    }

    public void dismissDialog() {
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessage(0);
    }

    public void awakenIfNecessary() {
        IDreamManager iDreamManager = this.mDreamManager;
        if (iDreamManager != null) {
            try {
                if (iDreamManager.isDreaming()) {
                    this.mDreamManager.awaken();
                }
            } catch (RemoteException unused) {
            }
        }
    }

    public void handleShow(View view) {
        awakenIfNecessary();
        this.mDialog = createDialog();
        prepareDialog();
        WindowManager.LayoutParams attributes = this.mDialog.getWindow().getAttributes();
        attributes.setTitle("ActionsDialog");
        attributes.layoutInDisplayCutoutMode = 3;
        this.mDialog.getWindow().setAttributes(attributes);
        this.mDialog.getWindow().addFlags(131072);
        if (view != null) {
            this.mDialogLaunchAnimator.showFromView(this.mDialog, view);
        } else {
            this.mDialog.show();
        }
        this.mWindowManagerFuncs.onGlobalActionsShown();
    }

    @VisibleForTesting
    public boolean shouldShowAction(Action action) {
        if (this.mKeyguardShowing && !action.showDuringKeyguard()) {
            return false;
        }
        if (this.mDeviceProvisioned || action.showBeforeProvisioning()) {
            return action.shouldShow();
        }
        return false;
    }

    @VisibleForTesting
    public int getMaxShownPowerItems() {
        return this.mResources.getInteger(R$integer.power_menu_lite_max_columns) * this.mResources.getInteger(R$integer.power_menu_lite_max_rows);
    }

    public final void addActionItem(Action action) {
        if (this.mItems.size() < getMaxShownPowerItems()) {
            this.mItems.add(action);
        } else {
            this.mOverflowItems.add(action);
        }
    }

    @VisibleForTesting
    public String[] getDefaultActions() {
        return this.mResources.getStringArray(17236070);
    }

    public final void addIfShouldShowAction(List<Action> list, Action action) {
        if (shouldShowAction(action)) {
            list.add(action);
        }
    }

    @VisibleForTesting
    public void createActionItems() {
        if (!this.mHasVibrator) {
            this.mSilentModeAction = new SilentModeToggleAction();
        } else {
            this.mSilentModeAction = new SilentModeTriStateAction(this.mAudioManager, this.mHandler);
        }
        this.mAirplaneModeOn = new AirplaneModeAction();
        onAirplaneModeChanged();
        this.mItems.clear();
        this.mOverflowItems.clear();
        this.mPowerItems.clear();
        String[] defaultActions = getDefaultActions();
        ShutDownAction shutDownAction = new ShutDownAction();
        RestartAction restartAction = new RestartAction();
        ArraySet arraySet = new ArraySet();
        ArrayList<Action> arrayList = new ArrayList<>();
        CurrentUserProvider currentUserProvider = new CurrentUserProvider();
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            addIfShouldShowAction(arrayList, new EmergencyAffordanceAction());
            arraySet.add("emergency");
        }
        for (String str : defaultActions) {
            if (!arraySet.contains(str)) {
                if (GLOBAL_ACTION_KEY_POWER.equals(str)) {
                    addIfShouldShowAction(arrayList, shutDownAction);
                } else if ("airplane".equals(str)) {
                    addIfShouldShowAction(arrayList, this.mAirplaneModeOn);
                } else if ("bugreport".equals(str)) {
                    if (shouldDisplayBugReport(currentUserProvider.get())) {
                        addIfShouldShowAction(arrayList, new BugReportAction());
                    }
                } else if ("silent".equals(str)) {
                    if (this.mShowSilentToggle) {
                        addIfShouldShowAction(arrayList, this.mSilentModeAction);
                    }
                } else if ("users".equals(str)) {
                    if (SystemProperties.getBoolean("fw.power_user_switcher", false)) {
                        addUserActions(arrayList, currentUserProvider.get());
                    }
                } else if ("settings".equals(str)) {
                    addIfShouldShowAction(arrayList, getSettingsAction());
                } else if ("lockdown".equals(str)) {
                    if (shouldDisplayLockdown(currentUserProvider.get())) {
                        addIfShouldShowAction(arrayList, new LockDownAction());
                    }
                } else if ("voiceassist".equals(str)) {
                    addIfShouldShowAction(arrayList, getVoiceAssistAction());
                } else if ("assist".equals(str)) {
                    addIfShouldShowAction(arrayList, getAssistAction());
                } else if ("restart".equals(str)) {
                    addIfShouldShowAction(arrayList, restartAction);
                } else if ("screenshot".equals(str)) {
                    addIfShouldShowAction(arrayList, new ScreenshotAction());
                } else if ("logout".equals(str)) {
                    if (!(!this.mDevicePolicyManager.isLogoutEnabled() || currentUserProvider.get() == null || currentUserProvider.get().id == 0)) {
                        addIfShouldShowAction(arrayList, new LogoutAction());
                    }
                } else if (!"emergency".equals(str)) {
                    Log.e("GlobalActionsDialogLite", "Invalid global action key " + str);
                } else if (shouldDisplayEmergency()) {
                    addIfShouldShowAction(arrayList, new EmergencyDialerAction());
                }
                arraySet.add(str);
            }
        }
        if (arrayList.contains(shutDownAction) && arrayList.contains(restartAction) && arrayList.size() > getMaxShownPowerItems()) {
            int min = Math.min(arrayList.indexOf(restartAction), arrayList.indexOf(shutDownAction));
            arrayList.remove(shutDownAction);
            arrayList.remove(restartAction);
            this.mPowerItems.add(shutDownAction);
            this.mPowerItems.add(restartAction);
            arrayList.add(min, new PowerOptionsAction());
        }
        for (Action addActionItem : arrayList) {
            addActionItem(addActionItem);
        }
    }

    public void onRefresh() {
        createActionItems();
    }

    public void initDialogItems() {
        createActionItems();
        this.mAdapter = new MyAdapter();
        this.mOverflowAdapter = new MyOverflowAdapter();
        this.mPowerAdapter = new MyPowerOptionsAdapter();
    }

    public ActionsDialogLite createDialog() {
        initDialogItems();
        ActionsDialogLite actionsDialogLite = r1;
        ActionsDialogLite actionsDialogLite2 = new ActionsDialogLite(this.mContext, R$style.Theme_SystemUI_Dialog_GlobalActionsLite, this.mAdapter, this.mOverflowAdapter, this.mSysuiColorExtractor, this.mStatusBarService, this.mNotificationShadeWindowController, new GlobalActionsDialogLite$$ExternalSyntheticLambda1(this), this.mKeyguardShowing, this.mPowerAdapter, this.mUiEventLogger, this.mCentralSurfacesOptional, this.mKeyguardUpdateMonitor, this.mLockPatternUtils);
        ActionsDialogLite actionsDialogLite3 = actionsDialogLite;
        actionsDialogLite3.setOnDismissListener(this);
        actionsDialogLite3.setOnShowListener(this);
        return actionsDialogLite3;
    }

    @VisibleForTesting
    public boolean shouldDisplayLockdown(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }
        int i = userInfo.id;
        if (!this.mKeyguardStateController.isMethodSecure()) {
            return false;
        }
        int strongAuthForUser = this.mLockPatternUtils.getStrongAuthForUser(i);
        if (strongAuthForUser == 0 || strongAuthForUser == 4) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public boolean shouldDisplayEmergency() {
        return this.mHasTelephony;
    }

    @VisibleForTesting
    public boolean shouldDisplayBugReport(UserInfo userInfo) {
        if (this.mGlobalSettings.getInt("bugreport_in_power_menu", 0) == 0) {
            return false;
        }
        if (userInfo == null || userInfo.isPrimary()) {
            return true;
        }
        return false;
    }

    public void onConfigChanged(Configuration configuration) {
        int i;
        ActionsDialogLite actionsDialogLite = this.mDialog;
        if (actionsDialogLite != null && actionsDialogLite.isShowing() && (i = configuration.smallestScreenWidthDp) != this.mSmallestScreenWidthDp) {
            this.mSmallestScreenWidthDp = i;
            this.mDialog.refreshDialog();
        }
    }

    public void dismissGlobalActionsMenu() {
        dismissDialog();
    }

    @VisibleForTesting
    public final class PowerOptionsAction extends SinglePressAction {
        public boolean showBeforeProvisioning() {
            return true;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public PowerOptionsAction() {
            super(R$drawable.ic_settings_power, 17040406);
        }

        public void onPress() {
            ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
            if (actionsDialogLite != null) {
                actionsDialogLite.showPowerOptionsMenu();
            }
        }
    }

    @VisibleForTesting
    public final class ShutDownAction extends SinglePressAction implements LongPressAction {
        public boolean showBeforeProvisioning() {
            return true;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public ShutDownAction() {
            super(17301552, 17040405);
        }

        public boolean onLongPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SHUTDOWN_LONG_PRESS);
            if (GlobalActionsDialogLite.this.mUserManager.hasUserRestriction("no_safe_boot")) {
                return false;
            }
            GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(true);
            return true;
        }

        public void onPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SHUTDOWN_PRESS);
            GlobalActionsDialogLite.this.mWindowManagerFuncs.shutdown();
        }
    }

    @VisibleForTesting
    public abstract class EmergencyAction extends SinglePressAction {
        public boolean shouldBeSeparated() {
            return false;
        }

        public boolean showBeforeProvisioning() {
            return true;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public EmergencyAction(int i, int i2) {
            super(i, i2);
        }

        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            View create = super.create(context, view, viewGroup, layoutInflater);
            int emergencyTextColor = GlobalActionsDialogLite.this.getEmergencyTextColor(context);
            int emergencyIconColor = GlobalActionsDialogLite.this.getEmergencyIconColor(context);
            int emergencyBackgroundColor = GlobalActionsDialogLite.this.getEmergencyBackgroundColor(context);
            TextView textView = (TextView) create.findViewById(16908299);
            textView.setTextColor(emergencyTextColor);
            textView.setSelected(true);
            ImageView imageView = (ImageView) create.findViewById(16908294);
            imageView.getDrawable().setTint(emergencyIconColor);
            imageView.setBackgroundTintList(ColorStateList.valueOf(emergencyBackgroundColor));
            create.setBackgroundTintList(ColorStateList.valueOf(emergencyBackgroundColor));
            return create;
        }
    }

    public int getEmergencyTextColor(Context context) {
        return context.getResources().getColor(R$color.global_actions_lite_text);
    }

    public int getEmergencyIconColor(Context context) {
        return context.getResources().getColor(R$color.global_actions_lite_emergency_icon);
    }

    public int getEmergencyBackgroundColor(Context context) {
        return context.getResources().getColor(R$color.global_actions_lite_emergency_background);
    }

    public class EmergencyAffordanceAction extends EmergencyAction {
        public EmergencyAffordanceAction() {
            super(17302212, 17040401);
        }

        public void onPress() {
            GlobalActionsDialogLite.this.mEmergencyAffordanceManager.performEmergencyCall();
        }
    }

    @VisibleForTesting
    public class EmergencyDialerAction extends EmergencyAction {
        public EmergencyDialerAction() {
            super(R$drawable.ic_emergency_star, 17040401);
        }

        public void onPress() {
            GlobalActionsDialogLite.this.mMetricsLogger.action(1569);
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_EMERGENCY_DIALER_PRESS);
            if (GlobalActionsDialogLite.this.mTelecomManager != null) {
                GlobalActionsDialogLite.this.mCentralSurfacesOptional.ifPresent(new GlobalActionsDialogLite$BugReportAction$$ExternalSyntheticLambda0());
                Intent createLaunchEmergencyDialerIntent = GlobalActionsDialogLite.this.mTelecomManager.createLaunchEmergencyDialerIntent((String) null);
                createLaunchEmergencyDialerIntent.addFlags(343932928);
                createLaunchEmergencyDialerIntent.putExtra("com.android.phone.EmergencyDialer.extra.ENTRY_TYPE", 2);
                GlobalActionsDialogLite.this.mContext.startActivityAsUser(createLaunchEmergencyDialerIntent, UserHandle.CURRENT);
            }
        }
    }

    @VisibleForTesting
    public EmergencyDialerAction makeEmergencyDialerActionForTesting() {
        return new EmergencyDialerAction();
    }

    @VisibleForTesting
    public final class RestartAction extends SinglePressAction implements LongPressAction {
        public boolean showBeforeProvisioning() {
            return true;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public RestartAction() {
            super(17302836, 17040407);
        }

        public boolean onLongPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_REBOOT_LONG_PRESS);
            if (GlobalActionsDialogLite.this.mUserManager.hasUserRestriction("no_safe_boot")) {
                return false;
            }
            GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(true);
            return true;
        }

        public void onPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_REBOOT_PRESS);
            GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(false);
        }
    }

    @VisibleForTesting
    public class ScreenshotAction extends SinglePressAction {
        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public ScreenshotAction() {
            super(17302838, 17040408);
        }

        public void onPress() {
            GlobalActionsDialogLite.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    GlobalActionsDialogLite.this.mScreenshotHelper.takeScreenshot(1, true, true, 0, GlobalActionsDialogLite.this.mHandler, (Consumer) null);
                    GlobalActionsDialogLite.this.mMetricsLogger.action(1282);
                    GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SCREENSHOT_PRESS);
                }
            }, (long) GlobalActionsDialogLite.this.mDialogPressDelay);
        }

        public boolean shouldShow() {
            return is2ButtonNavigationEnabled();
        }

        public boolean is2ButtonNavigationEnabled() {
            return 1 == GlobalActionsDialogLite.this.mContext.getResources().getInteger(17694881);
        }
    }

    @VisibleForTesting
    public ScreenshotAction makeScreenshotActionForTesting() {
        return new ScreenshotAction();
    }

    @VisibleForTesting
    public class BugReportAction extends SinglePressAction implements LongPressAction {
        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public BugReportAction() {
            super(17302496, 17039826);
        }

        public void onPress() {
            if (!ActivityManager.isUserAMonkey()) {
                GlobalActionsDialogLite.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            GlobalActionsDialogLite.this.mMetricsLogger.action(292);
                            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_BUGREPORT_PRESS);
                            if (!GlobalActionsDialogLite.this.mIActivityManager.launchBugReportHandlerApp()) {
                                Log.w("GlobalActionsDialogLite", "Bugreport handler could not be launched");
                                GlobalActionsDialogLite.this.mIActivityManager.requestInteractiveBugReport();
                            }
                            GlobalActionsDialogLite.this.mCentralSurfacesOptional.ifPresent(new GlobalActionsDialogLite$BugReportAction$$ExternalSyntheticLambda0());
                        } catch (RemoteException unused) {
                        }
                    }
                }, (long) GlobalActionsDialogLite.this.mDialogPressDelay);
            }
        }

        public boolean onLongPress() {
            if (ActivityManager.isUserAMonkey()) {
                return false;
            }
            try {
                GlobalActionsDialogLite.this.mMetricsLogger.action(293);
                GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_BUGREPORT_LONG_PRESS);
                GlobalActionsDialogLite.this.mIActivityManager.requestFullBugReport();
                GlobalActionsDialogLite.this.mCentralSurfacesOptional.ifPresent(new GlobalActionsDialogLite$BugReportAction$$ExternalSyntheticLambda0());
            } catch (RemoteException unused) {
            }
            return false;
        }
    }

    @VisibleForTesting
    public BugReportAction makeBugReportActionForTesting() {
        return new BugReportAction();
    }

    public final class LogoutAction extends SinglePressAction {
        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public LogoutAction() {
            super(17302546, 17040404);
        }

        public void onPress() {
            GlobalActionsDialogLite.this.mHandler.postDelayed(new GlobalActionsDialogLite$LogoutAction$$ExternalSyntheticLambda0(this), (long) GlobalActionsDialogLite.this.mDialogPressDelay);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onPress$0() {
            GlobalActionsDialogLite.this.mDevicePolicyManager.logoutUser();
        }
    }

    public final Action getSettingsAction() {
        return new SinglePressAction(17302845, 17040409) {
            public boolean showBeforeProvisioning() {
                return true;
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public void onPress() {
                Intent intent = new Intent("android.settings.SETTINGS");
                intent.addFlags(335544320);
                GlobalActionsDialogLite.this.mContext.startActivity(intent);
            }
        };
    }

    public final Action getAssistAction() {
        return new SinglePressAction(17302310, 17040399) {
            public boolean showBeforeProvisioning() {
                return true;
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public void onPress() {
                Intent intent = new Intent("android.intent.action.ASSIST");
                intent.addFlags(335544320);
                GlobalActionsDialogLite.this.mContext.startActivity(intent);
            }
        };
    }

    public final Action getVoiceAssistAction() {
        return new SinglePressAction(17302887, 17040413) {
            public boolean showBeforeProvisioning() {
                return true;
            }

            public boolean showDuringKeyguard() {
                return true;
            }

            public void onPress() {
                Intent intent = new Intent("android.intent.action.VOICE_ASSIST");
                intent.addFlags(335544320);
                GlobalActionsDialogLite.this.mContext.startActivity(intent);
            }
        };
    }

    @VisibleForTesting
    public class LockDownAction extends SinglePressAction {
        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public LockDownAction() {
            super(17302499, 17040403);
        }

        public void onPress() {
            GlobalActionsDialogLite.this.mLockPatternUtils.requireStrongAuth(32, -1);
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_LOCKDOWN_PRESS);
            try {
                GlobalActionsDialogLite.this.mIWindowManager.lockNow((Bundle) null);
                GlobalActionsDialogLite.this.mBackgroundExecutor.execute(new GlobalActionsDialogLite$LockDownAction$$ExternalSyntheticLambda0(this));
            } catch (RemoteException e) {
                Log.e("GlobalActionsDialogLite", "Error while trying to lock device.", e);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onPress$0() {
            GlobalActionsDialogLite.this.lockProfiles();
        }
    }

    public final void lockProfiles() {
        int i = getCurrentUser().id;
        for (int i2 : this.mUserManager.getEnabledProfileIds(i)) {
            if (i2 != i) {
                this.mTrustManager.setDeviceLockedForUser(i2, true);
            }
        }
    }

    public UserInfo getCurrentUser() {
        try {
            return this.mIActivityManager.getCurrentUser();
        } catch (RemoteException unused) {
            return null;
        }
    }

    public class CurrentUserProvider {
        public boolean mFetched;
        public UserInfo mUserInfo;

        public CurrentUserProvider() {
            this.mUserInfo = null;
            this.mFetched = false;
        }

        public UserInfo get() {
            if (!this.mFetched) {
                this.mFetched = true;
                this.mUserInfo = GlobalActionsDialogLite.this.getCurrentUser();
            }
            return this.mUserInfo;
        }
    }

    public final void addUserActions(List<Action> list, UserInfo userInfo) {
        if (this.mUserManager.isUserSwitcherEnabled()) {
            for (final UserInfo userInfo2 : this.mUserManager.getUsers()) {
                if (userInfo2.supportsSwitchToByUser()) {
                    boolean z = true;
                    if (userInfo != null ? userInfo.id != userInfo2.id : userInfo2.id != 0) {
                        z = false;
                    }
                    String str = userInfo2.iconPath;
                    Drawable createFromPath = str != null ? Drawable.createFromPath(str) : null;
                    StringBuilder sb = new StringBuilder();
                    String str2 = userInfo2.name;
                    if (str2 == null) {
                        str2 = "Primary";
                    }
                    sb.append(str2);
                    sb.append(z ? " ✔" : "");
                    addIfShouldShowAction(list, new SinglePressAction(17302715, createFromPath, sb.toString()) {
                        public boolean showBeforeProvisioning() {
                            return false;
                        }

                        public boolean showDuringKeyguard() {
                            return true;
                        }

                        public void onPress() {
                            try {
                                GlobalActionsDialogLite.this.mIActivityManager.switchUser(userInfo2.id);
                            } catch (RemoteException e) {
                                Log.e("GlobalActionsDialogLite", "Couldn't switch user " + e);
                            }
                        }
                    });
                }
            }
        }
    }

    public void prepareDialog() {
        refreshSilentMode();
        this.mAirplaneModeOn.updateState(this.mAirplaneState);
        this.mAdapter.notifyDataSetChanged();
        this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
    }

    public final void refreshSilentMode() {
        if (!this.mHasVibrator) {
            Integer value = this.mRingerModeTracker.getRingerMode().getValue();
            ((ToggleAction) this.mSilentModeAction).updateState(value != null && value.intValue() != 2 ? ToggleState.On : ToggleState.Off);
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (this.mDialog == dialogInterface) {
            this.mDialog = null;
        }
        this.mUiEventLogger.log(GlobalActionsEvent.GA_POWER_MENU_CLOSE);
        this.mWindowManagerFuncs.onGlobalActionsHidden();
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public void onShow(DialogInterface dialogInterface) {
        this.mMetricsLogger.visible(1568);
        this.mUiEventLogger.log(GlobalActionsEvent.GA_POWER_MENU_OPEN);
    }

    public class MyAdapter extends MultiListLayout.MultiListAdapter {
        public boolean areAllItemsEnabled() {
            return false;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public MyAdapter() {
        }

        public final int countItems(boolean z) {
            int i = 0;
            for (int i2 = 0; i2 < GlobalActionsDialogLite.this.mItems.size(); i2++) {
                if (GlobalActionsDialogLite.this.mItems.get(i2).shouldBeSeparated() == z) {
                    i++;
                }
            }
            return i;
        }

        public int countSeparatedItems() {
            return countItems(true);
        }

        public int countListItems() {
            return countItems(false);
        }

        public int getCount() {
            return countSeparatedItems() + countListItems();
        }

        public boolean isEnabled(int i) {
            return getItem(i).isEnabled();
        }

        public Action getItem(int i) {
            int i2 = 0;
            for (int i3 = 0; i3 < GlobalActionsDialogLite.this.mItems.size(); i3++) {
                Action action = GlobalActionsDialogLite.this.mItems.get(i3);
                if (GlobalActionsDialogLite.this.shouldShowAction(action)) {
                    if (i2 == i) {
                        return action;
                    }
                    i2++;
                }
            }
            throw new IllegalArgumentException("position " + i + " out of range of showable actions, filtered count=" + getCount() + ", keyguardshowing=" + GlobalActionsDialogLite.this.mKeyguardShowing + ", provisioned=" + GlobalActionsDialogLite.this.mDeviceProvisioned);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            Action item = getItem(i);
            View create = item.create(GlobalActionsDialogLite.this.mContext, view, viewGroup, LayoutInflater.from(GlobalActionsDialogLite.this.mContext));
            create.setOnClickListener(new GlobalActionsDialogLite$MyAdapter$$ExternalSyntheticLambda0(this, i));
            if (item instanceof LongPressAction) {
                create.setOnLongClickListener(new GlobalActionsDialogLite$MyAdapter$$ExternalSyntheticLambda1(this, i));
            }
            return create;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$0(int i, View view) {
            onClickItem(i);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getView$1(int i, View view) {
            return onLongClickItem(i);
        }

        public boolean onLongClickItem(int i) {
            Action item = GlobalActionsDialogLite.this.mAdapter.getItem(i);
            if (!(item instanceof LongPressAction)) {
                return false;
            }
            GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
            if (globalActionsDialogLite.mDialog != null) {
                globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                GlobalActionsDialogLite.this.mDialog.dismiss();
            } else {
                Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
            }
            return ((LongPressAction) item).onLongPress();
        }

        public void onClickItem(int i) {
            Action item = GlobalActionsDialogLite.this.mAdapter.getItem(i);
            if (!(item instanceof SilentModeTriStateAction)) {
                GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                if (globalActionsDialogLite.mDialog == null) {
                    Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                } else if (!(item instanceof PowerOptionsAction)) {
                    globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                    GlobalActionsDialogLite.this.mDialog.dismiss();
                }
                item.onPress();
            }
        }

        public boolean shouldBeSeparated(int i) {
            return getItem(i).shouldBeSeparated();
        }
    }

    public class MyPowerOptionsAdapter extends BaseAdapter {
        public long getItemId(int i) {
            return (long) i;
        }

        public MyPowerOptionsAdapter() {
        }

        public int getCount() {
            return GlobalActionsDialogLite.this.mPowerItems.size();
        }

        public Action getItem(int i) {
            return GlobalActionsDialogLite.this.mPowerItems.get(i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            Action item = getItem(i);
            if (item == null) {
                Log.w("GlobalActionsDialogLite", "No power options action found at position: " + i);
                return null;
            }
            int i2 = R$layout.global_actions_power_item;
            if (view == null) {
                view = LayoutInflater.from(GlobalActionsDialogLite.this.mContext).inflate(i2, viewGroup, false);
            }
            view.setOnClickListener(new GlobalActionsDialogLite$MyPowerOptionsAdapter$$ExternalSyntheticLambda0(this, i));
            if (item instanceof LongPressAction) {
                view.setOnLongClickListener(new GlobalActionsDialogLite$MyPowerOptionsAdapter$$ExternalSyntheticLambda1(this, i));
            }
            ImageView imageView = (ImageView) view.findViewById(16908294);
            TextView textView = (TextView) view.findViewById(16908299);
            textView.setSelected(true);
            imageView.setImageDrawable(item.getIcon(GlobalActionsDialogLite.this.mContext));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (item.getMessage() != null) {
                textView.setText(item.getMessage());
            } else {
                textView.setText(item.getMessageResId());
            }
            return view;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$0(int i, View view) {
            onClickItem(i);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getView$1(int i, View view) {
            return onLongClickItem(i);
        }

        public final boolean onLongClickItem(int i) {
            Action item = getItem(i);
            if (!(item instanceof LongPressAction)) {
                return false;
            }
            GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
            if (globalActionsDialogLite.mDialog != null) {
                globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                GlobalActionsDialogLite.this.mDialog.dismiss();
            } else {
                Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
            }
            return ((LongPressAction) item).onLongPress();
        }

        public final void onClickItem(int i) {
            Action item = getItem(i);
            if (!(item instanceof SilentModeTriStateAction)) {
                GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                if (globalActionsDialogLite.mDialog != null) {
                    globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                    GlobalActionsDialogLite.this.mDialog.dismiss();
                } else {
                    Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                }
                item.onPress();
            }
        }
    }

    public class MyOverflowAdapter extends BaseAdapter {
        public long getItemId(int i) {
            return (long) i;
        }

        public MyOverflowAdapter() {
        }

        public int getCount() {
            return GlobalActionsDialogLite.this.mOverflowItems.size();
        }

        public Action getItem(int i) {
            return GlobalActionsDialogLite.this.mOverflowItems.get(i);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            Action item = getItem(i);
            if (item == null) {
                Log.w("GlobalActionsDialogLite", "No overflow action found at position: " + i);
                return null;
            }
            int i2 = R$layout.controls_more_item;
            if (view == null) {
                view = LayoutInflater.from(GlobalActionsDialogLite.this.mContext).inflate(i2, viewGroup, false);
            }
            TextView textView = (TextView) view;
            if (item.getMessageResId() != 0) {
                textView.setText(item.getMessageResId());
            } else {
                textView.setText(item.getMessage());
            }
            return textView;
        }

        public boolean onLongClickItem(int i) {
            Action item = getItem(i);
            if (!(item instanceof LongPressAction)) {
                return false;
            }
            GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
            if (globalActionsDialogLite.mDialog != null) {
                globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                GlobalActionsDialogLite.this.mDialog.dismiss();
            } else {
                Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
            }
            return ((LongPressAction) item).onLongPress();
        }

        public void onClickItem(int i) {
            Action item = getItem(i);
            if (!(item instanceof SilentModeTriStateAction)) {
                GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                if (globalActionsDialogLite.mDialog != null) {
                    globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                    GlobalActionsDialogLite.this.mDialog.dismiss();
                } else {
                    Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                }
                item.onPress();
            }
        }
    }

    public abstract class SinglePressAction implements Action {
        public final Drawable mIcon;
        public final int mIconResId;
        public final CharSequence mMessage;
        public final int mMessageResId;

        public boolean isEnabled() {
            return true;
        }

        public SinglePressAction(int i, int i2) {
            this.mIconResId = i;
            this.mMessageResId = i2;
            this.mMessage = null;
            this.mIcon = null;
        }

        public SinglePressAction(int i, Drawable drawable, CharSequence charSequence) {
            this.mIconResId = i;
            this.mMessageResId = 0;
            this.mMessage = charSequence;
            this.mIcon = drawable;
        }

        public int getMessageResId() {
            return this.mMessageResId;
        }

        public CharSequence getMessage() {
            return this.mMessage;
        }

        public Drawable getIcon(Context context) {
            Drawable drawable = this.mIcon;
            if (drawable != null) {
                return drawable;
            }
            return context.getDrawable(this.mIconResId);
        }

        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            View inflate = layoutInflater.inflate(GlobalActionsDialogLite.this.getGridItemLayoutResource(), viewGroup, false);
            inflate.setId(View.generateViewId());
            ImageView imageView = (ImageView) inflate.findViewById(16908294);
            TextView textView = (TextView) inflate.findViewById(16908299);
            textView.setSelected(true);
            imageView.setImageDrawable(getIcon(context));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            CharSequence charSequence = this.mMessage;
            if (charSequence != null) {
                textView.setText(charSequence);
            } else {
                textView.setText(this.mMessageResId);
            }
            return inflate;
        }
    }

    public int getGridItemLayoutResource() {
        return R$layout.global_actions_grid_item_lite;
    }

    public enum ToggleState {
        Off(false),
        TurningOn(true),
        TurningOff(true),
        On(false);
        
        private final boolean mInTransition;

        /* access modifiers changed from: public */
        ToggleState(boolean z) {
            this.mInTransition = z;
        }

        public boolean inTransition() {
            return this.mInTransition;
        }
    }

    public abstract class ToggleAction implements Action {
        public int mDisabledIconResid;
        public int mDisabledStatusMessageResId;
        public int mEnabledIconResId;
        public int mEnabledStatusMessageResId;
        public int mMessageResId;
        public ToggleState mState = ToggleState.Off;

        public CharSequence getMessage() {
            return null;
        }

        public abstract void onToggle(boolean z);

        public void willCreate() {
        }

        public ToggleAction(int i, int i2, int i3, int i4, int i5) {
            this.mEnabledIconResId = i;
            this.mDisabledIconResid = i2;
            this.mMessageResId = i3;
            this.mEnabledStatusMessageResId = i4;
            this.mDisabledStatusMessageResId = i5;
        }

        public final boolean isOn() {
            ToggleState toggleState = this.mState;
            return toggleState == ToggleState.On || toggleState == ToggleState.TurningOn;
        }

        public int getMessageResId() {
            return isOn() ? this.mEnabledStatusMessageResId : this.mDisabledStatusMessageResId;
        }

        public final int getIconResId() {
            return isOn() ? this.mEnabledIconResId : this.mDisabledIconResid;
        }

        public Drawable getIcon(Context context) {
            return context.getDrawable(getIconResId());
        }

        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            willCreate();
            View inflate = layoutInflater.inflate(R$layout.global_actions_grid_item_v2, viewGroup, false);
            ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
            layoutParams.width = -2;
            inflate.setLayoutParams(layoutParams);
            ImageView imageView = (ImageView) inflate.findViewById(16908294);
            TextView textView = (TextView) inflate.findViewById(16908299);
            boolean isEnabled = isEnabled();
            if (textView != null) {
                textView.setText(getMessageResId());
                textView.setEnabled(isEnabled);
                textView.setSelected(true);
            }
            if (imageView != null) {
                imageView.setImageDrawable(context.getDrawable(getIconResId()));
                imageView.setEnabled(isEnabled);
            }
            inflate.setEnabled(isEnabled);
            return inflate;
        }

        public final void onPress() {
            if (this.mState.inTransition()) {
                Log.w("GlobalActionsDialogLite", "shouldn't be able to toggle when in transition");
                return;
            }
            boolean z = this.mState != ToggleState.On;
            onToggle(z);
            changeStateFromPress(z);
        }

        public boolean isEnabled() {
            return !this.mState.inTransition();
        }

        public void changeStateFromPress(boolean z) {
            this.mState = z ? ToggleState.On : ToggleState.Off;
        }

        public void updateState(ToggleState toggleState) {
            this.mState = toggleState;
        }
    }

    public class AirplaneModeAction extends ToggleAction {
        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public AirplaneModeAction() {
            super(17302492, 17302494, 17040417, 17040416, 17040415);
        }

        public void onToggle(boolean z) {
            if (!GlobalActionsDialogLite.this.mHasTelephony || !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
                GlobalActionsDialogLite.this.changeAirplaneModeSystemSetting(z);
                return;
            }
            GlobalActionsDialogLite.this.mIsWaitingForEcmExit = true;
            Intent intent = new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (Uri) null);
            intent.addFlags(268435456);
            GlobalActionsDialogLite.this.mContext.startActivity(intent);
        }

        public void changeStateFromPress(boolean z) {
            if (GlobalActionsDialogLite.this.mHasTelephony && !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
                ToggleState toggleState = z ? ToggleState.TurningOn : ToggleState.TurningOff;
                this.mState = toggleState;
                GlobalActionsDialogLite.this.mAirplaneState = toggleState;
            }
        }
    }

    public class SilentModeToggleAction extends ToggleAction {
        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public SilentModeToggleAction() {
            super(17302329, 17302328, 17040412, 17040411, 17040410);
        }

        public void onToggle(boolean z) {
            if (z) {
                GlobalActionsDialogLite.this.mAudioManager.setRingerMode(0);
            } else {
                GlobalActionsDialogLite.this.mAudioManager.setRingerMode(2);
            }
        }
    }

    public static class SilentModeTriStateAction implements Action, View.OnClickListener {
        public static final int[] ITEM_IDS = {16909313, 16909314, 16909315};
        public final AudioManager mAudioManager;
        public final Handler mHandler;

        public Drawable getIcon(Context context) {
            return null;
        }

        public CharSequence getMessage() {
            return null;
        }

        public int getMessageResId() {
            return 0;
        }

        public final int indexToRingerMode(int i) {
            return i;
        }

        public boolean isEnabled() {
            return true;
        }

        public void onPress() {
        }

        public final int ringerModeToIndex(int i) {
            return i;
        }

        public boolean showBeforeProvisioning() {
            return false;
        }

        public boolean showDuringKeyguard() {
            return true;
        }

        public SilentModeTriStateAction(AudioManager audioManager, Handler handler) {
            this.mAudioManager = audioManager;
            this.mHandler = handler;
        }

        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            View inflate = layoutInflater.inflate(17367173, viewGroup, false);
            int ringerModeToIndex = ringerModeToIndex(this.mAudioManager.getRingerMode());
            int i = 0;
            while (i < 3) {
                View findViewById = inflate.findViewById(ITEM_IDS[i]);
                findViewById.setSelected(ringerModeToIndex == i);
                findViewById.setTag(Integer.valueOf(i));
                findViewById.setOnClickListener(this);
                i++;
            }
            return inflate;
        }

        public void onClick(View view) {
            if (view.getTag() instanceof Integer) {
                this.mAudioManager.setRingerMode(indexToRingerMode(((Integer) view.getTag()).intValue()));
                this.mHandler.sendEmptyMessageDelayed(0, 300);
            }
        }
    }

    @VisibleForTesting
    public void setZeroDialogPressDelayForTesting() {
        this.mDialogPressDelay = 0;
    }

    public final void onAirplaneModeChanged() {
        if (!this.mHasTelephony && this.mAirplaneModeOn != null) {
            boolean z = false;
            if (this.mGlobalSettings.getInt("airplane_mode_on", 0) == 1) {
                z = true;
            }
            ToggleState toggleState = z ? ToggleState.On : ToggleState.Off;
            this.mAirplaneState = toggleState;
            this.mAirplaneModeOn.updateState(toggleState);
        }
    }

    public final void changeAirplaneModeSystemSetting(boolean z) {
        this.mGlobalSettings.putInt("airplane_mode_on", z ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.addFlags(536870912);
        intent.putExtra("state", z);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        if (!this.mHasTelephony) {
            this.mAirplaneState = z ? ToggleState.On : ToggleState.Off;
        }
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    @VisibleForTesting
    public static class ActionsDialogLite extends SystemUIDialog implements ColorExtractor.OnColorsChangedListener {
        public final MyAdapter mAdapter;
        public Drawable mBackgroundDrawable;
        public Optional<CentralSurfaces> mCentralSurfacesOptional;
        public final SysuiColorExtractor mColorExtractor;
        public ViewGroup mContainer;
        public final Context mContext;
        public GestureDetector mGestureDetector;
        @VisibleForTesting
        public GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            public boolean onSingleTapUp(MotionEvent motionEvent) {
                ActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_TAP_OUTSIDE);
                ActionsDialogLite.this.cancel();
                return false;
            }

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (f2 >= 0.0f || f2 <= f || motionEvent.getY() > ((float) ((Integer) ActionsDialogLite.this.mCentralSurfacesOptional.map(new GlobalActionsDialogLite$ActionsDialogLite$1$$ExternalSyntheticLambda0()).orElse(0)).intValue())) {
                    return false;
                }
                ActionsDialogLite.this.openShadeAndDismiss();
                return true;
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (f2 <= 0.0f || Math.abs(f2) <= Math.abs(f) || motionEvent.getY() > ((float) ((Integer) ActionsDialogLite.this.mCentralSurfacesOptional.map(new GlobalActionsDialogLite$ActionsDialogLite$1$$ExternalSyntheticLambda0()).orElse(0)).intValue())) {
                    return false;
                }
                ActionsDialogLite.this.openShadeAndDismiss();
                return true;
            }
        };
        public MultiListLayout mGlobalActionsLayout;
        public boolean mKeyguardShowing;
        public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public LockPatternUtils mLockPatternUtils;
        public final NotificationShadeWindowController mNotificationShadeWindowController;
        public final Runnable mOnRefreshCallback;
        public final MyOverflowAdapter mOverflowAdapter;
        public ListPopupWindow mOverflowPopup;
        public final MyPowerOptionsAdapter mPowerOptionsAdapter;
        public Dialog mPowerOptionsDialog;
        public float mScrimAlpha;
        public final IStatusBarService mStatusBarService;
        public final IBinder mToken = new Binder();
        public UiEventLogger mUiEventLogger;
        public float mWindowDimAmount;

        public int getHeight() {
            return -1;
        }

        public int getWidth() {
            return -1;
        }

        public ActionsDialogLite(Context context, int i, MyAdapter myAdapter, MyOverflowAdapter myOverflowAdapter, SysuiColorExtractor sysuiColorExtractor, IStatusBarService iStatusBarService, NotificationShadeWindowController notificationShadeWindowController, Runnable runnable, boolean z, MyPowerOptionsAdapter myPowerOptionsAdapter, UiEventLogger uiEventLogger, Optional<CentralSurfaces> optional, KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils) {
            super(context, i, false);
            this.mContext = context;
            this.mAdapter = myAdapter;
            this.mOverflowAdapter = myOverflowAdapter;
            this.mPowerOptionsAdapter = myPowerOptionsAdapter;
            this.mColorExtractor = sysuiColorExtractor;
            this.mStatusBarService = iStatusBarService;
            this.mNotificationShadeWindowController = notificationShadeWindowController;
            this.mOnRefreshCallback = runnable;
            this.mKeyguardShowing = z;
            this.mUiEventLogger = uiEventLogger;
            this.mCentralSurfacesOptional = optional;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mLockPatternUtils = lockPatternUtils;
            this.mGestureDetector = new GestureDetector(context, this.mGestureListener);
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            initializeLayout();
            this.mWindowDimAmount = getWindow().getAttributes().dimAmount;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mGestureDetector.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
        }

        public final void openShadeAndDismiss() {
            this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_TAP_OUTSIDE);
            if (((Boolean) this.mCentralSurfacesOptional.map(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8()).orElse(Boolean.FALSE)).booleanValue()) {
                this.mCentralSurfacesOptional.ifPresent(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda9());
            } else {
                this.mCentralSurfacesOptional.ifPresent(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda10());
            }
            dismiss();
        }

        public final ListPopupWindow createPowerOverflowPopup() {
            GlobalActionsPopupMenu globalActionsPopupMenu = new GlobalActionsPopupMenu(new ContextThemeWrapper(this.mContext, R$style.Control_ListPopupWindow), false);
            globalActionsPopupMenu.setOnItemClickListener(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6(this));
            globalActionsPopupMenu.setOnItemLongClickListener(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda7(this));
            globalActionsPopupMenu.setAnchorView(findViewById(R$id.global_actions_overflow_button));
            globalActionsPopupMenu.setAdapter(this.mOverflowAdapter);
            return globalActionsPopupMenu;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$createPowerOverflowPopup$2(AdapterView adapterView, View view, int i, long j) {
            this.mOverflowAdapter.onClickItem(i);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ boolean lambda$createPowerOverflowPopup$3(AdapterView adapterView, View view, int i, long j) {
            return this.mOverflowAdapter.onLongClickItem(i);
        }

        public void showPowerOptionsMenu() {
            Dialog create = GlobalActionsPowerDialog.create(this.mContext, this.mPowerOptionsAdapter);
            this.mPowerOptionsDialog = create;
            create.show();
        }

        public void showPowerOverflowMenu() {
            ListPopupWindow createPowerOverflowPopup = createPowerOverflowPopup();
            this.mOverflowPopup = createPowerOverflowPopup;
            createPowerOverflowPopup.show();
        }

        public int getLayoutResource() {
            return R$layout.global_actions_grid_lite;
        }

        public void initializeLayout() {
            setContentView(getLayoutResource());
            fixNavBarClipping();
            MultiListLayout multiListLayout = (MultiListLayout) findViewById(R$id.global_actions_view);
            this.mGlobalActionsLayout = multiListLayout;
            multiListLayout.setListViewAccessibilityDelegate(new View.AccessibilityDelegate() {
                public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityEvent.getText().add(ActionsDialogLite.this.mContext.getString(17040414));
                    return true;
                }
            });
            this.mGlobalActionsLayout.setRotationListener(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda0(this));
            this.mGlobalActionsLayout.setAdapter(this.mAdapter);
            ViewGroup viewGroup = (ViewGroup) findViewById(R$id.global_actions_container);
            this.mContainer = viewGroup;
            viewGroup.setOnTouchListener(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda1(this));
            View findViewById = findViewById(R$id.global_actions_overflow_button);
            if (findViewById != null) {
                if (this.mOverflowAdapter.getCount() > 0) {
                    findViewById.setOnClickListener(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda2(this));
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mGlobalActionsLayout.getLayoutParams();
                    layoutParams.setMarginEnd(0);
                    this.mGlobalActionsLayout.setLayoutParams(layoutParams);
                } else {
                    findViewById.setVisibility(8);
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mGlobalActionsLayout.getLayoutParams();
                    layoutParams2.setMarginEnd(this.mContext.getResources().getDimensionPixelSize(R$dimen.global_actions_side_margin));
                    this.mGlobalActionsLayout.setLayoutParams(layoutParams2);
                }
            }
            if (this.mBackgroundDrawable == null) {
                this.mBackgroundDrawable = new ScrimDrawable();
                this.mScrimAlpha = 1.0f;
            }
            boolean userHasTrust = this.mKeyguardUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser());
            if (this.mKeyguardShowing && userHasTrust) {
                this.mLockPatternUtils.requireCredentialEntry(KeyguardUpdateMonitor.getCurrentUser());
                showSmartLockDisabledMessage();
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ boolean lambda$initializeLayout$4(View view, MotionEvent motionEvent) {
            this.mGestureDetector.onTouchEvent(motionEvent);
            return view.onTouchEvent(motionEvent);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$initializeLayout$5(View view) {
            showPowerOverflowMenu();
        }

        public void fixNavBarClipping() {
            ViewGroup viewGroup = (ViewGroup) findViewById(16908290);
            viewGroup.setClipChildren(false);
            viewGroup.setClipToPadding(false);
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
            viewGroup2.setClipChildren(false);
            viewGroup2.setClipToPadding(false);
        }

        public final void showSmartLockDisabledMessage() {
            final View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.global_actions_toast, this.mContainer, false);
            final int recommendedTimeoutMillis = ((AccessibilityManager) getContext().getSystemService("accessibility")).getRecommendedTimeoutMillis(3500, 2);
            inflate.setVisibility(0);
            inflate.setAlpha(0.0f);
            this.mContainer.addView(inflate);
            inflate.animate().alpha(1.0f).setDuration(333).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    inflate.animate().alpha(0.0f).setDuration(333).setStartDelay((long) recommendedTimeoutMillis).setListener((Animator.AnimatorListener) null);
                }
            });
        }

        public void onStart() {
            super.onStart();
            this.mGlobalActionsLayout.updateList();
            if (this.mBackgroundDrawable instanceof ScrimDrawable) {
                this.mColorExtractor.addOnColorsChangedListener(this);
                updateColors(this.mColorExtractor.getNeutralColors(), false);
            }
        }

        public final void updateColors(ColorExtractor.GradientColors gradientColors, boolean z) {
            Drawable drawable = this.mBackgroundDrawable;
            if (drawable instanceof ScrimDrawable) {
                ((ScrimDrawable) drawable).setColor(-16777216, z);
                View decorView = getWindow().getDecorView();
                if (gradientColors.supportsDarkText()) {
                    decorView.setSystemUiVisibility(8208);
                } else {
                    decorView.setSystemUiVisibility(0);
                }
            }
        }

        public void onStop() {
            super.onStop();
            this.mColorExtractor.removeOnColorsChangedListener(this);
        }

        public void onBackPressed() {
            super.onBackPressed();
            this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_BACK);
        }

        public void show() {
            super.show();
            this.mNotificationShadeWindowController.setRequestTopUi(true, "GlobalActionsDialogLite");
            if (getWindow().getAttributes().windowAnimations == 0) {
                startAnimation(true, (Runnable) null);
                setDismissOverride(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda3(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$show$7() {
            startAnimation(false, new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$show$6() {
            setDismissOverride((Runnable) null);
            hide();
            dismiss();
        }

        public final void startAnimation(boolean z, final Runnable runnable) {
            float f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            Resources resources = getContext().getResources();
            if (z) {
                f = resources.getDimension(17105456);
                ofFloat.setInterpolator(Interpolators.STANDARD);
                ofFloat.setDuration((long) resources.getInteger(17694730));
            } else {
                f = resources.getDimension(17105457);
                ofFloat.setInterpolator(Interpolators.STANDARD_ACCELERATE);
                ofFloat.setDuration((long) resources.getInteger(17694731));
            }
            Window window = getWindow();
            ofFloat.addUpdateListener(new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda4(this, z, window, f, window.getWindowManager().getDefaultDisplay().getRotation()));
            ofFloat.addListener(new AnimatorListenerAdapter() {
                public int mPreviousLayerType;

                public void onAnimationStart(Animator animator, boolean z) {
                    this.mPreviousLayerType = ActionsDialogLite.this.mGlobalActionsLayout.getLayerType();
                    ActionsDialogLite.this.mGlobalActionsLayout.setLayerType(2, (Paint) null);
                }

                public void onAnimationEnd(Animator animator) {
                    ActionsDialogLite.this.mGlobalActionsLayout.setLayerType(this.mPreviousLayerType, (Paint) null);
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            ofFloat.start();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$startAnimation$8(boolean z, Window window, float f, int i, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            float f2 = z ? floatValue : 1.0f - floatValue;
            this.mGlobalActionsLayout.setAlpha(f2);
            window.setDimAmount(this.mWindowDimAmount * f2);
            float f3 = z ? f * (1.0f - floatValue) : f * floatValue;
            if (i == 0) {
                this.mGlobalActionsLayout.setTranslationX(f3);
            } else if (i == 1) {
                this.mGlobalActionsLayout.setTranslationY(-f3);
            } else if (i == 2) {
                this.mGlobalActionsLayout.setTranslationX(-f3);
            } else if (i == 3) {
                this.mGlobalActionsLayout.setTranslationY(f3);
            }
        }

        public void dismiss() {
            dismissOverflow();
            dismissPowerOptions();
            this.mNotificationShadeWindowController.setRequestTopUi(false, "GlobalActionsDialogLite");
            super.dismiss();
        }

        public final void dismissOverflow() {
            ListPopupWindow listPopupWindow = this.mOverflowPopup;
            if (listPopupWindow != null) {
                listPopupWindow.dismiss();
            }
        }

        public final void dismissPowerOptions() {
            Dialog dialog = this.mPowerOptionsDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        public void onColorsChanged(ColorExtractor colorExtractor, int i) {
            if (this.mKeyguardShowing) {
                if ((i & 2) != 0) {
                    updateColors(colorExtractor.getColors(2), true);
                }
            } else if ((i & 1) != 0) {
                updateColors(colorExtractor.getColors(1), true);
            }
        }

        public void refreshDialog() {
            this.mOnRefreshCallback.run();
            dismissOverflow();
            dismissPowerOptions();
            this.mGlobalActionsLayout.updateList();
        }

        public void onRotate(int i, int i2) {
            refreshDialog();
        }
    }
}
