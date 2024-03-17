package com.android.systemui.controls.ui;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.VibrationEffect;
import android.service.controls.Control;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.TaskViewFactory;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl implements ControlActionCoordinator {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public Set<String> actionsInProgress = new LinkedHashSet();
    public Context activityContext;
    @NotNull
    public final ActivityStarter activityStarter;
    @NotNull
    public final DelayableExecutor bgExecutor;
    @NotNull
    public final BroadcastSender broadcastSender;
    @NotNull
    public final Context context;
    @NotNull
    public final ControlsMetricsLogger controlsMetricsLogger;
    @Nullable
    public Dialog dialog;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    public boolean mAllowTrivialControls;
    public boolean mShowDeviceControlsInLockscreen;
    @Nullable
    public Action pendingAction;
    @NotNull
    public final SecureSettings secureSettings;
    @NotNull
    public final Optional<TaskViewFactory> taskViewFactory;
    @NotNull
    public final DelayableExecutor uiExecutor;
    @NotNull
    public final UserContextProvider userContextProvider;
    @NotNull
    public final VibratorHelper vibrator;

    public ControlActionCoordinatorImpl(@NotNull Context context2, @NotNull DelayableExecutor delayableExecutor, @NotNull DelayableExecutor delayableExecutor2, @NotNull ActivityStarter activityStarter2, @NotNull BroadcastSender broadcastSender2, @NotNull KeyguardStateController keyguardStateController2, @NotNull Optional<TaskViewFactory> optional, @NotNull ControlsMetricsLogger controlsMetricsLogger2, @NotNull VibratorHelper vibratorHelper, @NotNull SecureSettings secureSettings2, @NotNull UserContextProvider userContextProvider2, @NotNull Handler handler) {
        this.context = context2;
        this.bgExecutor = delayableExecutor;
        this.uiExecutor = delayableExecutor2;
        this.activityStarter = activityStarter2;
        this.broadcastSender = broadcastSender2;
        this.keyguardStateController = keyguardStateController2;
        this.taskViewFactory = optional;
        this.controlsMetricsLogger = controlsMetricsLogger2;
        this.vibrator = vibratorHelper;
        this.secureSettings = secureSettings2;
        this.userContextProvider = userContextProvider2;
        boolean z = true;
        this.mAllowTrivialControls = secureSettings2.getIntForUser("lockscreen_allow_trivial_controls", 0, -2) != 0;
        this.mShowDeviceControlsInLockscreen = secureSettings2.getIntForUser("lockscreen_show_controls", 0, -2) == 0 ? false : z;
        Uri uriFor = secureSettings2.getUriFor("lockscreen_allow_trivial_controls");
        Uri uriFor2 = secureSettings2.getUriFor("lockscreen_show_controls");
        ControlActionCoordinatorImpl$controlsContentObserver$1 controlActionCoordinatorImpl$controlsContentObserver$1 = new ControlActionCoordinatorImpl$controlsContentObserver$1(handler, uriFor, this, uriFor2);
        secureSettings2.registerContentObserverForUser(uriFor, false, (ContentObserver) controlActionCoordinatorImpl$controlsContentObserver$1, -1);
        secureSettings2.registerContentObserverForUser(uriFor2, false, (ContentObserver) controlActionCoordinatorImpl$controlsContentObserver$1, -1);
    }

    public final boolean isLocked() {
        return !this.keyguardStateController.isUnlocked();
    }

    @NotNull
    public Context getActivityContext() {
        Context context2 = this.activityContext;
        if (context2 != null) {
            return context2;
        }
        return null;
    }

    public void setActivityContext(@NotNull Context context2) {
        this.activityContext = context2;
    }

    /* compiled from: ControlActionCoordinatorImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void closeDialogs() {
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
        this.dialog = null;
    }

    public void toggle(@NotNull ControlViewHolder controlViewHolder, @NotNull String str, boolean z) {
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        ControlActionCoordinatorImpl$toggle$1 controlActionCoordinatorImpl$toggle$1 = new ControlActionCoordinatorImpl$toggle$1(controlViewHolder, str, z);
        Control control = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$toggle$1, true, control == null ? true : control.isAuthRequired()));
    }

    public void touch(@NotNull ControlViewHolder controlViewHolder, @NotNull String str, @NotNull Control control) {
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        boolean usePanel = controlViewHolder.usePanel();
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        ControlActionCoordinatorImpl$touch$1 controlActionCoordinatorImpl$touch$1 = new ControlActionCoordinatorImpl$touch$1(controlViewHolder, this, control, str);
        Control control2 = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$touch$1, usePanel, control2 == null ? true : control2.isAuthRequired()));
    }

    public void drag(boolean z) {
        if (z) {
            vibrate(Vibrations.INSTANCE.getRangeEdgeEffect());
        } else {
            vibrate(Vibrations.INSTANCE.getRangeMiddleEffect());
        }
    }

    public void setValue(@NotNull ControlViewHolder controlViewHolder, @NotNull String str, float f) {
        this.controlsMetricsLogger.drag(controlViewHolder, isLocked());
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        ControlActionCoordinatorImpl$setValue$1 controlActionCoordinatorImpl$setValue$1 = new ControlActionCoordinatorImpl$setValue$1(controlViewHolder, str, f);
        Control control = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$setValue$1, false, control == null ? true : control.isAuthRequired()));
    }

    public void longPress(@NotNull ControlViewHolder controlViewHolder) {
        this.controlsMetricsLogger.longPress(controlViewHolder, isLocked());
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        ControlActionCoordinatorImpl$longPress$1 controlActionCoordinatorImpl$longPress$1 = new ControlActionCoordinatorImpl$longPress$1(controlViewHolder, this);
        Control control = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$longPress$1, false, control == null ? true : control.isAuthRequired()));
    }

    public void runPendingAction(@NotNull String str) {
        if (!isLocked()) {
            Action action = this.pendingAction;
            if (Intrinsics.areEqual((Object) action == null ? null : action.getControlId(), (Object) str)) {
                Action action2 = this.pendingAction;
                Intrinsics.checkNotNull(action2);
                showSettingsDialogIfNeeded(action2);
                Action action3 = this.pendingAction;
                if (action3 != null) {
                    action3.invoke();
                }
                this.pendingAction = null;
            }
        }
    }

    public void enableActionOnTouch(@NotNull String str) {
        this.actionsInProgress.remove(str);
    }

    public final boolean shouldRunAction(String str) {
        if (!this.actionsInProgress.add(str)) {
            return false;
        }
        this.uiExecutor.executeDelayed(new ControlActionCoordinatorImpl$shouldRunAction$1(this, str), 3000);
        return true;
    }

    @VisibleForTesting
    public final void bouncerOrRun(@NotNull Action action) {
        boolean z = action.getAuthIsRequired() || !this.mAllowTrivialControls;
        if (!this.keyguardStateController.isShowing() || !z) {
            showSettingsDialogIfNeeded(action);
            action.invoke();
            return;
        }
        if (isLocked()) {
            this.broadcastSender.closeSystemDialogs();
            this.pendingAction = action;
        }
        this.activityStarter.dismissKeyguardThenExecute(new ControlActionCoordinatorImpl$bouncerOrRun$1(action), new ControlActionCoordinatorImpl$bouncerOrRun$2(this), true);
    }

    public final void vibrate(VibrationEffect vibrationEffect) {
        this.vibrator.vibrate(vibrationEffect);
    }

    public final void showDetail(ControlViewHolder controlViewHolder, PendingIntent pendingIntent) {
        this.bgExecutor.execute(new ControlActionCoordinatorImpl$showDetail$1(this, pendingIntent, controlViewHolder));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        r5 = r4.userContextProvider.getUserContext().getSharedPreferences("controls_prefs", 0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void showSettingsDialogIfNeeded(com.android.systemui.controls.ui.ControlActionCoordinatorImpl.Action r5) {
        /*
            r4 = this;
            boolean r5 = r5.getAuthIsRequired()
            if (r5 == 0) goto L_0x0007
            return
        L_0x0007:
            com.android.systemui.settings.UserContextProvider r5 = r4.userContextProvider
            android.content.Context r5 = r5.getUserContext()
            java.lang.String r0 = "controls_prefs"
            r1 = 0
            android.content.SharedPreferences r5 = r5.getSharedPreferences(r0, r1)
            java.lang.String r0 = "show_settings_attempts"
            int r0 = r5.getInt(r0, r1)
            r1 = 2
            if (r0 >= r1) goto L_0x00a6
            boolean r1 = r4.mShowDeviceControlsInLockscreen
            if (r1 == 0) goto L_0x0028
            boolean r1 = r4.mAllowTrivialControls
            if (r1 == 0) goto L_0x0028
            goto L_0x00a6
        L_0x0028:
            android.app.AlertDialog$Builder r1 = new android.app.AlertDialog$Builder
            android.content.Context r2 = r4.getActivityContext()
            int r3 = com.android.systemui.R$style.Theme_SystemUI_Dialog
            r1.<init>(r2, r3)
            int r2 = com.android.systemui.R$drawable.ic_warning
            android.app.AlertDialog$Builder r1 = r1.setIcon(r2)
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$1 r2 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$1
            r2.<init>(r0, r5)
            android.app.AlertDialog$Builder r1 = r1.setOnCancelListener(r2)
            int r2 = com.android.systemui.R$string.controls_settings_dialog_neutral_button
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$2 r3 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$2
            r3.<init>(r0, r5)
            android.app.AlertDialog$Builder r1 = r1.setNeutralButton(r2, r3)
            boolean r2 = r4.mShowDeviceControlsInLockscreen
            if (r2 == 0) goto L_0x006f
            int r2 = com.android.systemui.R$string.controls_settings_trivial_controls_dialog_title
            android.app.AlertDialog$Builder r1 = r1.setTitle(r2)
            int r2 = com.android.systemui.R$string.controls_settings_trivial_controls_dialog_message
            android.app.AlertDialog$Builder r1 = r1.setMessage(r2)
            int r2 = com.android.systemui.R$string.controls_settings_dialog_positive_button
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$1 r3 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$1
            r3.<init>(r0, r5, r4)
            android.app.AlertDialog$Builder r5 = r1.setPositiveButton(r2, r3)
            android.app.AlertDialog r5 = r5.create()
            r4.dialog = r5
            goto L_0x008c
        L_0x006f:
            int r2 = com.android.systemui.R$string.controls_settings_show_controls_dialog_title
            android.app.AlertDialog$Builder r1 = r1.setTitle(r2)
            int r2 = com.android.systemui.R$string.controls_settings_show_controls_dialog_message
            android.app.AlertDialog$Builder r1 = r1.setMessage(r2)
            int r2 = com.android.systemui.R$string.controls_settings_dialog_positive_button
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$2 r3 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$2
            r3.<init>(r0, r5, r4)
            android.app.AlertDialog$Builder r5 = r1.setPositiveButton(r2, r3)
            android.app.AlertDialog r5 = r5.create()
            r4.dialog = r5
        L_0x008c:
            android.app.Dialog r5 = r4.dialog
            com.android.systemui.statusbar.phone.SystemUIDialog.registerDismissListener(r5)
            android.app.Dialog r5 = r4.dialog
            com.android.systemui.statusbar.phone.SystemUIDialog.setDialogSize(r5)
            android.app.Dialog r5 = r4.dialog
            if (r5 != 0) goto L_0x009b
            goto L_0x009e
        L_0x009b:
            r5.create()
        L_0x009e:
            android.app.Dialog r4 = r4.dialog
            if (r4 != 0) goto L_0x00a3
            goto L_0x00a6
        L_0x00a3:
            r4.show()
        L_0x00a6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.ui.ControlActionCoordinatorImpl.showSettingsDialogIfNeeded(com.android.systemui.controls.ui.ControlActionCoordinatorImpl$Action):void");
    }

    @NotNull
    @VisibleForTesting
    public final Action createAction(@NotNull String str, @NotNull Function0<Unit> function0, boolean z, boolean z2) {
        return new Action(str, function0, z, z2);
    }

    /* compiled from: ControlActionCoordinatorImpl.kt */
    public final class Action {
        public final boolean authIsRequired;
        public final boolean blockable;
        @NotNull
        public final String controlId;
        @NotNull
        public final Function0<Unit> f;

        public Action(@NotNull String str, @NotNull Function0<Unit> function0, boolean z, boolean z2) {
            this.controlId = str;
            this.f = function0;
            this.blockable = z;
            this.authIsRequired = z2;
        }

        @NotNull
        public final String getControlId() {
            return this.controlId;
        }

        public final boolean getAuthIsRequired() {
            return this.authIsRequired;
        }

        public final void invoke() {
            if (!this.blockable || ControlActionCoordinatorImpl.this.shouldRunAction(this.controlId)) {
                this.f.invoke();
            }
        }
    }
}
