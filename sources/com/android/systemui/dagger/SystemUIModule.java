package com.android.systemui.dagger;

import android.app.INotificationManager;
import android.content.Context;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.model.SysUiState;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.Optional;
import java.util.concurrent.Executor;

public abstract class SystemUIModule {
    public static SysUiState provideSysUiState(DumpManager dumpManager) {
        SysUiState sysUiState = new SysUiState();
        dumpManager.registerDumpable(sysUiState);
        return sysUiState;
    }

    public static Optional<BubblesManager> provideBubblesManager(Context context, Optional<Bubbles> optional, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationVisibilityProvider notificationVisibilityProvider, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, CommonNotifCollection commonNotifCollection, NotifPipeline notifPipeline, SysUiState sysUiState, NotifPipelineFlags notifPipelineFlags, DumpManager dumpManager, Executor executor) {
        return Optional.ofNullable(BubblesManager.create(context, optional, notificationShadeWindowController, keyguardStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationVisibilityProvider, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, commonNotifCollection, notifPipeline, sysUiState, notifPipelineFlags, dumpManager, executor));
    }

    public static Optional<LowLightClockController> provideLowLightClockController(Optional<LowLightClockController> optional) {
        if (!optional.isPresent() || !optional.get().isLowLightClockEnabled()) {
            return Optional.empty();
        }
        return optional;
    }
}
