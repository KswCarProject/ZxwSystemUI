package com.android.systemui.statusbar.policy;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.text.format.DateFormat;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.settings.GlobalSettings;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class ZenModeControllerImpl extends CurrentUserTracker implements ZenModeController, Dumpable {
    public static final boolean DEBUG = Log.isLoggable("ZenModeController", 3);
    public final AlarmManager mAlarmManager;
    public final ArrayList<ZenModeController.Callback> mCallbacks = new ArrayList<>();
    public final Object mCallbacksLock = new Object();
    public ZenModeConfig mConfig;
    public final SettingObserver mConfigSetting;
    public NotificationManager.Policy mConsolidatedNotificationPolicy;
    public final Context mContext;
    public final SettingObserver mModeSetting;
    public final NotificationManager mNoMan;
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.app.action.NEXT_ALARM_CLOCK_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl.this.fireNextAlarmChanged();
            }
            if ("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl.this.fireEffectsSuppressorChanged();
            }
        }
    };
    public boolean mRegistered;
    public final SetupObserver mSetupObserver;
    public int mUserId;
    public final UserManager mUserManager;
    public int mZenMode;
    public long mZenUpdateTime;

    public ZenModeControllerImpl(Context context, Handler handler, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, GlobalSettings globalSettings) {
        super(broadcastDispatcher);
        this.mContext = context;
        AnonymousClass1 r5 = new SettingObserver(globalSettings, handler, "zen_mode") {
            public void handleValueChanged(int i, boolean z) {
                ZenModeControllerImpl.this.updateZenMode(i);
                ZenModeControllerImpl.this.fireZenChanged(i);
            }
        };
        this.mModeSetting = r5;
        AnonymousClass2 r0 = new SettingObserver(globalSettings, handler, "zen_mode_config_etag") {
            public void handleValueChanged(int i, boolean z) {
                ZenModeControllerImpl.this.updateZenModeConfig();
            }
        };
        this.mConfigSetting = r0;
        this.mNoMan = (NotificationManager) context.getSystemService("notification");
        r5.setListening(true);
        updateZenMode(r5.getValue());
        r0.setListening(true);
        updateZenModeConfig();
        updateConsolidatedNotificationPolicy();
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        SetupObserver setupObserver = new SetupObserver(handler);
        this.mSetupObserver = setupObserver;
        setupObserver.register();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        startTracking();
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public boolean areNotificationsHiddenInShade() {
        if (this.mZenMode == 0 || (this.mConsolidatedNotificationPolicy.suppressedVisualEffects & 256) == 0) {
            return false;
        }
        return true;
    }

    public void addCallback(ZenModeController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.add(callback);
        }
    }

    public void removeCallback(ZenModeController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.remove(callback);
        }
    }

    public int getZen() {
        return this.mZenMode;
    }

    public void setZen(int i, Uri uri, String str) {
        this.mNoMan.setZenMode(i, uri, str);
    }

    public boolean isZenAvailable() {
        return this.mSetupObserver.isDeviceProvisioned() && this.mSetupObserver.isUserSetup();
    }

    public ZenModeConfig getConfig() {
        return this.mConfig;
    }

    public NotificationManager.Policy getConsolidatedPolicy() {
        return this.mConsolidatedNotificationPolicy;
    }

    public void onUserSwitched(int i) {
        this.mUserId = i;
        if (this.mRegistered) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
        IntentFilter intentFilter = new IntentFilter("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter.addAction("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED");
        this.mContext.registerReceiverAsUser(this.mReceiver, new UserHandle(this.mUserId), intentFilter, (String) null, (Handler) null);
        this.mRegistered = true;
        this.mSetupObserver.register();
    }

    public final void fireNextAlarmChanged() {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda2());
        }
    }

    public final void fireEffectsSuppressorChanged() {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda3());
        }
    }

    public final void fireZenChanged(int i) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda6(i));
        }
    }

    public final void fireZenAvailableChanged(boolean z) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda5(z));
        }
    }

    public final void fireManualRuleChanged(ZenModeConfig.ZenRule zenRule) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda1(zenRule));
        }
    }

    public final void fireConsolidatedPolicyChanged(NotificationManager.Policy policy) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda4(policy));
        }
    }

    @VisibleForTesting
    public void fireConfigChanged(ZenModeConfig zenModeConfig) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda0(zenModeConfig));
        }
    }

    @VisibleForTesting
    public void updateZenMode(int i) {
        this.mZenMode = i;
        this.mZenUpdateTime = System.currentTimeMillis();
    }

    @VisibleForTesting
    public void updateConsolidatedNotificationPolicy() {
        NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
        if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
            this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
            fireConsolidatedPolicyChanged(consolidatedNotificationPolicy);
        }
    }

    @VisibleForTesting
    public void updateZenModeConfig() {
        ZenModeConfig zenModeConfig = this.mNoMan.getZenModeConfig();
        if (!Objects.equals(zenModeConfig, this.mConfig)) {
            ZenModeConfig zenModeConfig2 = this.mConfig;
            ZenModeConfig.ZenRule zenRule = null;
            ZenModeConfig.ZenRule zenRule2 = zenModeConfig2 != null ? zenModeConfig2.manualRule : null;
            this.mConfig = zenModeConfig;
            this.mZenUpdateTime = System.currentTimeMillis();
            fireConfigChanged(zenModeConfig);
            if (zenModeConfig != null) {
                zenRule = zenModeConfig.manualRule;
            }
            if (!Objects.equals(zenRule2, zenRule)) {
                fireManualRuleChanged(zenRule);
            }
            NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
            if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
                this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
                fireConsolidatedPolicyChanged(consolidatedNotificationPolicy);
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("ZenModeControllerImpl:");
        printWriter.println("  mZenMode=" + this.mZenMode);
        printWriter.println("  mConfig=" + this.mConfig);
        printWriter.println("  mConsolidatedNotificationPolicy=" + this.mConsolidatedNotificationPolicy);
        printWriter.println("  mZenUpdateTime=" + DateFormat.format("MM-dd HH:mm:ss", this.mZenUpdateTime));
    }

    public final class SetupObserver extends ContentObserver {
        public boolean mRegistered;
        public final ContentResolver mResolver;

        public SetupObserver(Handler handler) {
            super(handler);
            this.mResolver = ZenModeControllerImpl.this.mContext.getContentResolver();
        }

        public boolean isUserSetup() {
            return Settings.Secure.getIntForUser(this.mResolver, "user_setup_complete", 0, ZenModeControllerImpl.this.mUserId) != 0;
        }

        public boolean isDeviceProvisioned() {
            return Settings.Global.getInt(this.mResolver, "device_provisioned", 0) != 0;
        }

        public void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            this.mResolver.registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this);
            this.mResolver.registerContentObserver(Settings.Secure.getUriFor("user_setup_complete"), false, this, ZenModeControllerImpl.this.mUserId);
            this.mRegistered = true;
            ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
            zenModeControllerImpl.fireZenAvailableChanged(zenModeControllerImpl.isZenAvailable());
        }

        public void onChange(boolean z, Uri uri) {
            if (Settings.Global.getUriFor("device_provisioned").equals(uri) || Settings.Secure.getUriFor("user_setup_complete").equals(uri)) {
                ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
                zenModeControllerImpl.fireZenAvailableChanged(zenModeControllerImpl.isZenAvailable());
            }
        }
    }
}
