package com.android.systemui.recents;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.transition.ShellTransitions;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class OverviewProxyService_Factory implements Factory<OverviewProxyService> {
    public final Provider<AssistUtils> assistUtilsProvider;
    public final Provider<Optional<BackAnimation>> backAnimationProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Optional<LegacySplitScreenController>> legacySplitScreenCtlOptionalProvider;
    public final Provider<NavigationBarController> navBarControllerLazyProvider;
    public final Provider<NavigationModeController> navModeControllerProvider;
    public final Provider<Optional<OneHanded>> oneHandedOptionalProvider;
    public final Provider<Optional<Pip>> pipOptionalProvider;
    public final Provider<Optional<RecentTasks>> recentTasksProvider;
    public final Provider<ScreenLifecycle> screenLifecycleProvider;
    public final Provider<ShellTransitions> shellTransitionsProvider;
    public final Provider<Optional<SplitScreen>> splitScreenOptionalProvider;
    public final Provider<Optional<SplitScreenController>> spliteScreenCtlOptionalProvider;
    public final Provider<Optional<StartingSurface>> startingSurfaceProvider;
    public final Provider<NotificationShadeWindowController> statusBarWinControllerProvider;
    public final Provider<SysUiState> sysUiStateProvider;
    public final Provider<KeyguardUnlockAnimationController> sysuiUnlockAnimationControllerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public OverviewProxyService_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<NavigationBarController> provider3, Provider<Optional<CentralSurfaces>> provider4, Provider<NavigationModeController> provider5, Provider<NotificationShadeWindowController> provider6, Provider<SysUiState> provider7, Provider<Optional<Pip>> provider8, Provider<Optional<LegacySplitScreenController>> provider9, Provider<Optional<SplitScreen>> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<Optional<OneHanded>> provider12, Provider<Optional<RecentTasks>> provider13, Provider<Optional<BackAnimation>> provider14, Provider<Optional<StartingSurface>> provider15, Provider<BroadcastDispatcher> provider16, Provider<ShellTransitions> provider17, Provider<ScreenLifecycle> provider18, Provider<UiEventLogger> provider19, Provider<KeyguardUnlockAnimationController> provider20, Provider<AssistUtils> provider21, Provider<DumpManager> provider22) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.navBarControllerLazyProvider = provider3;
        this.centralSurfacesOptionalLazyProvider = provider4;
        this.navModeControllerProvider = provider5;
        this.statusBarWinControllerProvider = provider6;
        this.sysUiStateProvider = provider7;
        this.pipOptionalProvider = provider8;
        this.legacySplitScreenCtlOptionalProvider = provider9;
        this.splitScreenOptionalProvider = provider10;
        this.spliteScreenCtlOptionalProvider = provider11;
        this.oneHandedOptionalProvider = provider12;
        this.recentTasksProvider = provider13;
        this.backAnimationProvider = provider14;
        this.startingSurfaceProvider = provider15;
        this.broadcastDispatcherProvider = provider16;
        this.shellTransitionsProvider = provider17;
        this.screenLifecycleProvider = provider18;
        this.uiEventLoggerProvider = provider19;
        this.sysuiUnlockAnimationControllerProvider = provider20;
        this.assistUtilsProvider = provider21;
        this.dumpManagerProvider = provider22;
    }

    public OverviewProxyService get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), DoubleCheck.lazy(this.navBarControllerLazyProvider), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.navModeControllerProvider.get(), this.statusBarWinControllerProvider.get(), this.sysUiStateProvider.get(), this.pipOptionalProvider.get(), this.legacySplitScreenCtlOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.spliteScreenCtlOptionalProvider.get(), this.oneHandedOptionalProvider.get(), this.recentTasksProvider.get(), this.backAnimationProvider.get(), this.startingSurfaceProvider.get(), this.broadcastDispatcherProvider.get(), this.shellTransitionsProvider.get(), this.screenLifecycleProvider.get(), this.uiEventLoggerProvider.get(), this.sysuiUnlockAnimationControllerProvider.get(), this.assistUtilsProvider.get(), this.dumpManagerProvider.get());
    }

    public static OverviewProxyService_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<NavigationBarController> provider3, Provider<Optional<CentralSurfaces>> provider4, Provider<NavigationModeController> provider5, Provider<NotificationShadeWindowController> provider6, Provider<SysUiState> provider7, Provider<Optional<Pip>> provider8, Provider<Optional<LegacySplitScreenController>> provider9, Provider<Optional<SplitScreen>> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<Optional<OneHanded>> provider12, Provider<Optional<RecentTasks>> provider13, Provider<Optional<BackAnimation>> provider14, Provider<Optional<StartingSurface>> provider15, Provider<BroadcastDispatcher> provider16, Provider<ShellTransitions> provider17, Provider<ScreenLifecycle> provider18, Provider<UiEventLogger> provider19, Provider<KeyguardUnlockAnimationController> provider20, Provider<AssistUtils> provider21, Provider<DumpManager> provider22) {
        return new OverviewProxyService_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22);
    }

    public static OverviewProxyService newInstance(Context context, CommandQueue commandQueue, Lazy<NavigationBarController> lazy, Lazy<Optional<CentralSurfaces>> lazy2, NavigationModeController navigationModeController, NotificationShadeWindowController notificationShadeWindowController, SysUiState sysUiState, Optional<Pip> optional, Optional<LegacySplitScreenController> optional2, Optional<SplitScreen> optional3, Optional<SplitScreenController> optional4, Optional<OneHanded> optional5, Optional<RecentTasks> optional6, Optional<BackAnimation> optional7, Optional<StartingSurface> optional8, BroadcastDispatcher broadcastDispatcher, ShellTransitions shellTransitions, ScreenLifecycle screenLifecycle, UiEventLogger uiEventLogger, KeyguardUnlockAnimationController keyguardUnlockAnimationController, AssistUtils assistUtils, DumpManager dumpManager) {
        return new OverviewProxyService(context, commandQueue, lazy, lazy2, navigationModeController, notificationShadeWindowController, sysUiState, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, broadcastDispatcher, shellTransitions, screenLifecycle, uiEventLogger, keyguardUnlockAnimationController, assistUtils, dumpManager);
    }
}
