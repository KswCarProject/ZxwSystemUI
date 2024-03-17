package com.android.systemui.accessibility;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.CoreStartable;
import com.android.systemui.recents.Recents;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.Assert;
import dagger.Lazy;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class SystemActions extends CoreStartable {
    public final AccessibilityManager mA11yManager = ((AccessibilityManager) this.mContext.getSystemService("accessibility"));
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public boolean mDismissNotificationShadeActionRegistered;
    public Locale mLocale = this.mContext.getResources().getConfiguration().getLocales().get(0);
    public final StatusBarWindowCallback mNotificationShadeCallback;
    public final NotificationShadeWindowController mNotificationShadeController;
    public final SystemActionsBroadcastReceiver mReceiver = new SystemActionsBroadcastReceiver();
    public final Optional<Recents> mRecentsOptional;

    public SystemActions(Context context, NotificationShadeWindowController notificationShadeWindowController, Lazy<Optional<CentralSurfaces>> lazy, Optional<Recents> optional) {
        super(context);
        this.mRecentsOptional = optional;
        this.mNotificationShadeController = notificationShadeWindowController;
        this.mNotificationShadeCallback = new SystemActions$$ExternalSyntheticLambda0(this);
        this.mCentralSurfacesOptionalLazy = lazy;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(boolean z, boolean z2, boolean z3, boolean z4) {
        registerOrUnregisterDismissNotificationShadeAction();
    }

    public void start() {
        this.mNotificationShadeController.registerCallback(this.mNotificationShadeCallback);
        Context context = this.mContext;
        SystemActionsBroadcastReceiver systemActionsBroadcastReceiver = this.mReceiver;
        context.registerReceiverForAllUsers(systemActionsBroadcastReceiver, systemActionsBroadcastReceiver.createIntentFilter(), "com.android.systemui.permission.SELF", (Handler) null, 2);
        registerActions();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            registerActions();
        }
    }

    public final void registerActions() {
        RemoteAction createRemoteAction = createRemoteAction(17039604, "SYSTEM_ACTION_BACK");
        RemoteAction createRemoteAction2 = createRemoteAction(17039613, "SYSTEM_ACTION_HOME");
        RemoteAction createRemoteAction3 = createRemoteAction(17039620, "SYSTEM_ACTION_RECENTS");
        RemoteAction createRemoteAction4 = createRemoteAction(17039615, "SYSTEM_ACTION_NOTIFICATIONS");
        RemoteAction createRemoteAction5 = createRemoteAction(17039619, "SYSTEM_ACTION_QUICK_SETTINGS");
        RemoteAction createRemoteAction6 = createRemoteAction(17039618, "SYSTEM_ACTION_POWER_DIALOG");
        RemoteAction createRemoteAction7 = createRemoteAction(17039614, "SYSTEM_ACTION_LOCK_SCREEN");
        RemoteAction createRemoteAction8 = createRemoteAction(17039621, "SYSTEM_ACTION_TAKE_SCREENSHOT");
        RemoteAction createRemoteAction9 = createRemoteAction(17039612, "SYSTEM_ACTION_HEADSET_HOOK");
        RemoteAction createRemoteAction10 = createRemoteAction(17039611, "SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT");
        RemoteAction createRemoteAction11 = createRemoteAction(17039610, "SYSTEM_ACTION_DPAD_UP");
        RemoteAction createRemoteAction12 = createRemoteAction(17039607, "SYSTEM_ACTION_DPAD_DOWN");
        RemoteAction createRemoteAction13 = createRemoteAction(17039608, "SYSTEM_ACTION_DPAD_LEFT");
        RemoteAction createRemoteAction14 = createRemoteAction(17039609, "SYSTEM_ACTION_DPAD_RIGHT");
        RemoteAction createRemoteAction15 = createRemoteAction(17039606, "SYSTEM_ACTION_DPAD_CENTER");
        this.mA11yManager.registerSystemAction(createRemoteAction, 1);
        this.mA11yManager.registerSystemAction(createRemoteAction2, 2);
        this.mA11yManager.registerSystemAction(createRemoteAction3, 3);
        this.mA11yManager.registerSystemAction(createRemoteAction4, 4);
        this.mA11yManager.registerSystemAction(createRemoteAction5, 5);
        this.mA11yManager.registerSystemAction(createRemoteAction6, 6);
        this.mA11yManager.registerSystemAction(createRemoteAction7, 8);
        this.mA11yManager.registerSystemAction(createRemoteAction8, 9);
        this.mA11yManager.registerSystemAction(createRemoteAction9, 10);
        this.mA11yManager.registerSystemAction(createRemoteAction10, 13);
        this.mA11yManager.registerSystemAction(createRemoteAction11, 16);
        this.mA11yManager.registerSystemAction(createRemoteAction12, 17);
        this.mA11yManager.registerSystemAction(createRemoteAction13, 18);
        this.mA11yManager.registerSystemAction(createRemoteAction14, 19);
        this.mA11yManager.registerSystemAction(createRemoteAction15, 20);
        registerOrUnregisterDismissNotificationShadeAction();
    }

    public final void registerOrUnregisterDismissNotificationShadeAction() {
        Assert.isMainThread();
        Optional optional = this.mCentralSurfacesOptionalLazy.get();
        if (!((Boolean) optional.map(new SystemActions$$ExternalSyntheticLambda1()).orElse(Boolean.FALSE)).booleanValue() || ((CentralSurfaces) optional.get()).isKeyguardShowing()) {
            if (this.mDismissNotificationShadeActionRegistered) {
                this.mA11yManager.unregisterSystemAction(15);
                this.mDismissNotificationShadeActionRegistered = false;
            }
        } else if (!this.mDismissNotificationShadeActionRegistered) {
            this.mA11yManager.registerSystemAction(createRemoteAction(17039605, "SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE"), 15);
            this.mDismissNotificationShadeActionRegistered = true;
        }
    }

    public void register(int i) {
        String str;
        int i2;
        switch (i) {
            case 1:
                i2 = 17039604;
                str = "SYSTEM_ACTION_BACK";
                break;
            case 2:
                i2 = 17039613;
                str = "SYSTEM_ACTION_HOME";
                break;
            case 3:
                i2 = 17039620;
                str = "SYSTEM_ACTION_RECENTS";
                break;
            case 4:
                i2 = 17039615;
                str = "SYSTEM_ACTION_NOTIFICATIONS";
                break;
            case 5:
                i2 = 17039619;
                str = "SYSTEM_ACTION_QUICK_SETTINGS";
                break;
            case 6:
                i2 = 17039618;
                str = "SYSTEM_ACTION_POWER_DIALOG";
                break;
            case 8:
                i2 = 17039614;
                str = "SYSTEM_ACTION_LOCK_SCREEN";
                break;
            case 9:
                i2 = 17039621;
                str = "SYSTEM_ACTION_TAKE_SCREENSHOT";
                break;
            case 10:
                i2 = 17039612;
                str = "SYSTEM_ACTION_HEADSET_HOOK";
                break;
            case 11:
                i2 = 17039617;
                str = "SYSTEM_ACTION_ACCESSIBILITY_BUTTON";
                break;
            case 12:
                i2 = 17039616;
                str = "SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU";
                break;
            case 13:
                i2 = 17039611;
                str = "SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT";
                break;
            case 15:
                i2 = 17039605;
                str = "SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE";
                break;
            case 16:
                i2 = 17039610;
                str = "SYSTEM_ACTION_DPAD_UP";
                break;
            case 17:
                i2 = 17039607;
                str = "SYSTEM_ACTION_DPAD_DOWN";
                break;
            case 18:
                i2 = 17039608;
                str = "SYSTEM_ACTION_DPAD_LEFT";
                break;
            case 19:
                i2 = 17039609;
                str = "SYSTEM_ACTION_DPAD_RIGHT";
                break;
            case 20:
                i2 = 17039606;
                str = "SYSTEM_ACTION_DPAD_CENTER";
                break;
            default:
                return;
        }
        this.mA11yManager.registerSystemAction(createRemoteAction(i2, str), i);
    }

    public final RemoteAction createRemoteAction(int i, String str) {
        return new RemoteAction(Icon.createWithResource(this.mContext, 17301684), this.mContext.getString(i), this.mContext.getString(i), this.mReceiver.createPendingIntent(this.mContext, str));
    }

    public void unregister(int i) {
        this.mA11yManager.unregisterSystemAction(i);
    }

    public final void handleBack() {
        sendDownAndUpKeyEvents(4);
    }

    public final void handleHome() {
        sendDownAndUpKeyEvents(3);
    }

    public final void sendDownAndUpKeyEvents(int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        int i2 = i;
        long j = uptimeMillis;
        sendKeyEventIdentityCleared(i2, 0, j, uptimeMillis);
        sendKeyEventIdentityCleared(i2, 1, j, SystemClock.uptimeMillis());
    }

    public final void sendKeyEventIdentityCleared(int i, int i2, long j, long j2) {
        KeyEvent obtain = KeyEvent.obtain(j, j2, i2, i, 0, 0, -1, 0, 8, 257, (String) null);
        InputManager.getInstance().injectInputEvent(obtain, 0);
        obtain.recycle();
    }

    public final void handleRecents() {
        this.mRecentsOptional.ifPresent(new SystemActions$$ExternalSyntheticLambda6());
    }

    public final void handleSplitScreen() {
        this.mRecentsOptional.ifPresent(new SystemActions$$ExternalSyntheticLambda4());
    }

    public final void handleNotifications() {
        this.mCentralSurfacesOptionalLazy.get().ifPresent(new SystemActions$$ExternalSyntheticLambda5());
    }

    public final void handleQuickSettings() {
        this.mCentralSurfacesOptionalLazy.get().ifPresent(new SystemActions$$ExternalSyntheticLambda3());
    }

    public final void handlePowerDialog() {
        try {
            WindowManagerGlobal.getWindowManagerService().showGlobalActions();
        } catch (RemoteException unused) {
            Log.e("SystemActions", "failed to display power dialog.");
        }
    }

    public final void handleLockScreen() {
        IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
        ((PowerManager) this.mContext.getSystemService(PowerManager.class)).goToSleep(SystemClock.uptimeMillis(), 7, 0);
        try {
            windowManagerService.lockNow((Bundle) null);
        } catch (RemoteException unused) {
            Log.e("SystemActions", "failed to lock screen.");
        }
    }

    public final void handleTakeScreenshot() {
        new ScreenshotHelper(this.mContext).takeScreenshot(1, true, true, 4, new Handler(Looper.getMainLooper()), (Consumer) null);
    }

    public final void handleHeadsetHook() {
        sendDownAndUpKeyEvents(79);
    }

    public final void handleAccessibilityButton() {
        AccessibilityManager.getInstance(this.mContext).notifyAccessibilityButtonClicked(0);
    }

    public final void handleAccessibilityButtonChooser() {
        Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
        intent.addFlags(268468224);
        intent.setClassName(ThemeOverlayApplier.ANDROID_PACKAGE, AccessibilityButtonChooserActivity.class.getName());
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
    }

    public final void handleAccessibilityShortcut() {
        this.mA11yManager.performAccessibilityShortcut();
    }

    public final void handleAccessibilityDismissNotificationShade() {
        this.mCentralSurfacesOptionalLazy.get().ifPresent(new SystemActions$$ExternalSyntheticLambda2());
    }

    public final void handleDpadUp() {
        sendDownAndUpKeyEvents(19);
    }

    public final void handleDpadDown() {
        sendDownAndUpKeyEvents(20);
    }

    public final void handleDpadLeft() {
        sendDownAndUpKeyEvents(21);
    }

    public final void handleDpadRight() {
        sendDownAndUpKeyEvents(22);
    }

    public final void handleDpadCenter() {
        sendDownAndUpKeyEvents(23);
    }

    public class SystemActionsBroadcastReceiver extends BroadcastReceiver {
        public SystemActionsBroadcastReceiver() {
        }

        public final PendingIntent createPendingIntent(Context context, String str) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1103811776:
                    if (str.equals("SYSTEM_ACTION_BACK")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1103619272:
                    if (str.equals("SYSTEM_ACTION_HOME")) {
                        c = 1;
                        break;
                    }
                    break;
                case -720484549:
                    if (str.equals("SYSTEM_ACTION_POWER_DIALOG")) {
                        c = 2;
                        break;
                    }
                    break;
                case -535129457:
                    if (str.equals("SYSTEM_ACTION_NOTIFICATIONS")) {
                        c = 3;
                        break;
                    }
                    break;
                case -181386672:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT")) {
                        c = 4;
                        break;
                    }
                    break;
                case -153384569:
                    if (str.equals("SYSTEM_ACTION_LOCK_SCREEN")) {
                        c = 5;
                        break;
                    }
                    break;
                case 42571871:
                    if (str.equals("SYSTEM_ACTION_RECENTS")) {
                        c = 6;
                        break;
                    }
                    break;
                case 526987266:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU")) {
                        c = 7;
                        break;
                    }
                    break;
                case 689657964:
                    if (str.equals("SYSTEM_ACTION_DPAD_CENTER")) {
                        c = 8;
                        break;
                    }
                    break;
                case 815482418:
                    if (str.equals("SYSTEM_ACTION_DPAD_UP")) {
                        c = 9;
                        break;
                    }
                    break;
                case 1245940668:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON")) {
                        c = 10;
                        break;
                    }
                    break;
                case 1493428793:
                    if (str.equals("SYSTEM_ACTION_HEADSET_HOOK")) {
                        c = 11;
                        break;
                    }
                    break;
                case 1579999269:
                    if (str.equals("SYSTEM_ACTION_TAKE_SCREENSHOT")) {
                        c = 12;
                        break;
                    }
                    break;
                case 1668921710:
                    if (str.equals("SYSTEM_ACTION_QUICK_SETTINGS")) {
                        c = 13;
                        break;
                    }
                    break;
                case 1698779909:
                    if (str.equals("SYSTEM_ACTION_DPAD_RIGHT")) {
                        c = 14;
                        break;
                    }
                    break;
                case 1894867256:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE")) {
                        c = 15;
                        break;
                    }
                    break;
                case 1994051193:
                    if (str.equals("SYSTEM_ACTION_DPAD_DOWN")) {
                        c = 16;
                        break;
                    }
                    break;
                case 1994279390:
                    if (str.equals("SYSTEM_ACTION_DPAD_LEFT")) {
                        c = 17;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                    Intent intent = new Intent(str);
                    intent.setPackage(context.getPackageName());
                    return PendingIntent.getBroadcast(context, 0, intent, 67108864);
                default:
                    return null;
            }
        }

        public final IntentFilter createIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("SYSTEM_ACTION_BACK");
            intentFilter.addAction("SYSTEM_ACTION_HOME");
            intentFilter.addAction("SYSTEM_ACTION_RECENTS");
            intentFilter.addAction("SYSTEM_ACTION_SLITSCREEN");
            intentFilter.addAction("SYSTEM_ACTION_NOTIFICATIONS");
            intentFilter.addAction("SYSTEM_ACTION_QUICK_SETTINGS");
            intentFilter.addAction("SYSTEM_ACTION_POWER_DIALOG");
            intentFilter.addAction("SYSTEM_ACTION_LOCK_SCREEN");
            intentFilter.addAction("SYSTEM_ACTION_TAKE_SCREENSHOT");
            intentFilter.addAction("SYSTEM_ACTION_HEADSET_HOOK");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_BUTTON");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE");
            intentFilter.addAction("SYSTEM_ACTION_DPAD_UP");
            intentFilter.addAction("SYSTEM_ACTION_DPAD_DOWN");
            intentFilter.addAction("SYSTEM_ACTION_DPAD_LEFT");
            intentFilter.addAction("SYSTEM_ACTION_DPAD_RIGHT");
            intentFilter.addAction("SYSTEM_ACTION_DPAD_CENTER");
            return intentFilter;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1103811776:
                    if (action.equals("SYSTEM_ACTION_BACK")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1103619272:
                    if (action.equals("SYSTEM_ACTION_HOME")) {
                        c = 1;
                        break;
                    }
                    break;
                case -720484549:
                    if (action.equals("SYSTEM_ACTION_POWER_DIALOG")) {
                        c = 2;
                        break;
                    }
                    break;
                case -593438135:
                    if (action.equals("SYSTEM_ACTION_SLITSCREEN")) {
                        c = 3;
                        break;
                    }
                    break;
                case -535129457:
                    if (action.equals("SYSTEM_ACTION_NOTIFICATIONS")) {
                        c = 4;
                        break;
                    }
                    break;
                case -181386672:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT")) {
                        c = 5;
                        break;
                    }
                    break;
                case -153384569:
                    if (action.equals("SYSTEM_ACTION_LOCK_SCREEN")) {
                        c = 6;
                        break;
                    }
                    break;
                case 42571871:
                    if (action.equals("SYSTEM_ACTION_RECENTS")) {
                        c = 7;
                        break;
                    }
                    break;
                case 526987266:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU")) {
                        c = 8;
                        break;
                    }
                    break;
                case 689657964:
                    if (action.equals("SYSTEM_ACTION_DPAD_CENTER")) {
                        c = 9;
                        break;
                    }
                    break;
                case 815482418:
                    if (action.equals("SYSTEM_ACTION_DPAD_UP")) {
                        c = 10;
                        break;
                    }
                    break;
                case 1245940668:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON")) {
                        c = 11;
                        break;
                    }
                    break;
                case 1493428793:
                    if (action.equals("SYSTEM_ACTION_HEADSET_HOOK")) {
                        c = 12;
                        break;
                    }
                    break;
                case 1579999269:
                    if (action.equals("SYSTEM_ACTION_TAKE_SCREENSHOT")) {
                        c = 13;
                        break;
                    }
                    break;
                case 1668921710:
                    if (action.equals("SYSTEM_ACTION_QUICK_SETTINGS")) {
                        c = 14;
                        break;
                    }
                    break;
                case 1698779909:
                    if (action.equals("SYSTEM_ACTION_DPAD_RIGHT")) {
                        c = 15;
                        break;
                    }
                    break;
                case 1894867256:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE")) {
                        c = 16;
                        break;
                    }
                    break;
                case 1994051193:
                    if (action.equals("SYSTEM_ACTION_DPAD_DOWN")) {
                        c = 17;
                        break;
                    }
                    break;
                case 1994279390:
                    if (action.equals("SYSTEM_ACTION_DPAD_LEFT")) {
                        c = 18;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    SystemActions.this.handleBack();
                    return;
                case 1:
                    SystemActions.this.handleHome();
                    return;
                case 2:
                    SystemActions.this.handlePowerDialog();
                    return;
                case 3:
                    SystemActions.this.handleSplitScreen();
                    return;
                case 4:
                    SystemActions.this.handleNotifications();
                    return;
                case 5:
                    SystemActions.this.handleAccessibilityShortcut();
                    return;
                case 6:
                    SystemActions.this.handleLockScreen();
                    return;
                case 7:
                    SystemActions.this.handleRecents();
                    return;
                case 8:
                    SystemActions.this.handleAccessibilityButtonChooser();
                    return;
                case 9:
                    SystemActions.this.handleDpadCenter();
                    return;
                case 10:
                    SystemActions.this.handleDpadUp();
                    return;
                case 11:
                    SystemActions.this.handleAccessibilityButton();
                    return;
                case 12:
                    SystemActions.this.handleHeadsetHook();
                    return;
                case 13:
                    SystemActions.this.handleTakeScreenshot();
                    return;
                case 14:
                    SystemActions.this.handleQuickSettings();
                    return;
                case 15:
                    SystemActions.this.handleDpadRight();
                    return;
                case 16:
                    SystemActions.this.handleAccessibilityDismissNotificationShade();
                    return;
                case 17:
                    SystemActions.this.handleDpadDown();
                    return;
                case 18:
                    SystemActions.this.handleDpadLeft();
                    return;
                default:
                    return;
            }
        }
    }
}
