package com.android.systemui.qs.tiles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.notification.EnableZenModeDialog;
import com.android.systemui.Prefs;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.dialog.QSZenModeDialogMetricsLogger;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.settings.SecureSettings;

public class DndTile extends QSTileImpl<QSTile.BooleanState> {
    public static final Intent ZEN_PRIORITY_SETTINGS = new Intent("android.settings.ZEN_MODE_PRIORITY_SETTINGS");
    public static final Intent ZEN_SETTINGS = new Intent("android.settings.ZEN_MODE_SETTINGS");
    public final ZenModeController mController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public boolean mListening;
    public final SharedPreferences.OnSharedPreferenceChangeListener mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if ("DndTileCombinedIcon".equals(str) || "DndTileVisible".equals(str)) {
                DndTile.this.refreshState();
            }
        }
    };
    public final QSZenModeDialogMetricsLogger mQSZenDialogMetricsLogger;
    public final SettingObserver mSettingZenDuration;
    public final SharedPreferences mSharedPreferences;
    public final ZenModeController.Callback mZenCallback;

    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowActionBarOverlay;
    }

    public DndTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ZenModeController zenModeController, SharedPreferences sharedPreferences, SecureSettings secureSettings, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        AnonymousClass3 r1 = new ZenModeController.Callback() {
            public void onZenChanged(int i) {
                DndTile.this.refreshState(Integer.valueOf(i));
            }
        };
        this.mZenCallback = r1;
        this.mController = zenModeController;
        this.mSharedPreferences = sharedPreferences;
        zenModeController.observe(getLifecycle(), r1);
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mSettingZenDuration = new SettingObserver(secureSettings, this.mUiHandler, "zen_duration", getHost().getUserId()) {
            public void handleValueChanged(int i, boolean z) {
                DndTile.this.refreshState();
            }
        };
        this.mQSZenDialogMetricsLogger = new QSZenModeDialogMetricsLogger(this.mContext);
    }

    public static void setVisible(Context context, boolean z) {
        Prefs.putBoolean(context, "DndTileVisible", z);
    }

    public static boolean isVisible(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("DndTileVisible", false);
    }

    public static void setCombinedIcon(Context context, boolean z) {
        Prefs.putBoolean(context, "DndTileCombinedIcon", z);
    }

    public static boolean isCombinedIcon(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("DndTileCombinedIcon", false);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        return ZEN_SETTINGS;
    }

    public void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).value) {
            this.mController.setZen(0, (Uri) null, this.TAG);
        } else {
            enableZenMode(view);
        }
    }

    public void handleUserSwitch(int i) {
        super.handleUserSwitch(i);
        this.mSettingZenDuration.setUserId(i);
    }

    public final void enableZenMode(View view) {
        int value = this.mSettingZenDuration.getValue();
        if ((Settings.Secure.getInt(this.mContext.getContentResolver(), "show_zen_upgrade_notification", 0) == 0 || Settings.Secure.getInt(this.mContext.getContentResolver(), "zen_settings_updated", 0) == 1) ? false : true) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "show_zen_upgrade_notification", 0);
            this.mController.setZen(1, (Uri) null, this.TAG);
            Intent intent = new Intent("android.settings.ZEN_MODE_ONBOARDING");
            intent.addFlags(268468224);
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        } else if (value == -1) {
            this.mUiHandler.post(new DndTile$$ExternalSyntheticLambda0(this, view));
        } else if (value != 0) {
            this.mController.setZen(1, ZenModeConfig.toTimeCondition(this.mContext, value, this.mHost.getUserId(), true).id, this.TAG);
        } else {
            this.mController.setZen(1, (Uri) null, this.TAG);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$enableZenMode$0(View view) {
        Dialog makeZenModeDialog = makeZenModeDialog();
        if (view != null) {
            this.mDialogLaunchAnimator.showFromView(makeZenModeDialog, view, false);
        } else {
            makeZenModeDialog.show();
        }
    }

    public final Dialog makeZenModeDialog() {
        AlertDialog createDialog = new EnableZenModeDialog(this.mContext, R$style.Theme_SystemUI_Dialog, true, this.mQSZenDialogMetricsLogger).createDialog();
        SystemUIDialog.applyFlags(createDialog);
        SystemUIDialog.setShowForAllUsers(createDialog, true);
        SystemUIDialog.registerDismissListener(createDialog);
        SystemUIDialog.setDialogSize(createDialog);
        return createDialog;
    }

    public void handleSecondaryClick(View view) {
        handleLongClick(view);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_dnd_label);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        ZenModeController zenModeController = this.mController;
        if (zenModeController != null) {
            int intValue = obj instanceof Integer ? ((Integer) obj).intValue() : zenModeController.getZen();
            boolean z = false;
            boolean z2 = intValue != 0;
            boolean z3 = booleanState.value;
            if (booleanState.slash == null) {
                booleanState.slash = new QSTile.SlashState();
            }
            booleanState.dualTarget = true;
            booleanState.value = z2;
            booleanState.state = z2 ? 2 : 1;
            booleanState.slash.isSlashed = !z2;
            booleanState.label = getTileLabel();
            booleanState.secondaryLabel = TextUtils.emptyIfNull(ZenModeConfig.getDescription(this.mContext, intValue != 0, this.mController.getConfig(), false));
            booleanState.icon = QSTileImpl.ResourceIcon.get(17302830);
            checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_adjust_volume");
            if (intValue == 1) {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd) + ", " + booleanState.secondaryLabel;
            } else if (intValue == 2) {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd) + ", " + this.mContext.getString(R$string.accessibility_quick_settings_dnd_none_on) + ", " + booleanState.secondaryLabel;
            } else if (intValue != 3) {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd);
            } else {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd) + ", " + this.mContext.getString(R$string.accessibility_quick_settings_dnd_alarms_on) + ", " + booleanState.secondaryLabel;
            }
            booleanState.dualLabelContentDescription = this.mContext.getResources().getString(R$string.accessibility_quick_settings_open_settings, new Object[]{getTileLabel()});
            booleanState.expandedAccessibilityClassName = Switch.class.getName();
            if (this.mSettingZenDuration.getValue() == -1) {
                z = true;
            }
            booleanState.forceExpandIcon = z;
        }
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                Prefs.registerListener(this.mContext, this.mPrefListener);
            } else {
                Prefs.unregisterListener(this.mContext, this.mPrefListener);
            }
            this.mSettingZenDuration.setListening(z);
        }
    }

    public void handleDestroy() {
        super.handleDestroy();
        this.mSettingZenDuration.setListening(false);
    }

    public boolean isAvailable() {
        return isVisible(this.mSharedPreferences);
    }
}
