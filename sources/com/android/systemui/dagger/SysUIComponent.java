package com.android.systemui.dagger;

import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.InitController;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.FoldStateLogger;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.UnfoldLatencyTracker;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.transition.ShellTransitions;
import java.util.Map;
import java.util.Optional;
import javax.inject.Provider;

public interface SysUIComponent {

    public interface Builder {
        SysUIComponent build();

        Builder setAppPairs(Optional<AppPairs> optional);

        Builder setBackAnimation(Optional<BackAnimation> optional);

        Builder setBubbles(Optional<Bubbles> optional);

        Builder setCompatUI(Optional<CompatUI> optional);

        Builder setDisplayAreaHelper(Optional<DisplayAreaHelper> optional);

        Builder setDragAndDrop(Optional<DragAndDrop> optional);

        Builder setHideDisplayCutout(Optional<HideDisplayCutout> optional);

        Builder setLegacySplitScreen(Optional<LegacySplitScreen> optional);

        Builder setLegacySplitScreenController(Optional<LegacySplitScreenController> optional);

        Builder setOneHanded(Optional<OneHanded> optional);

        Builder setPip(Optional<Pip> optional);

        Builder setRecentTasks(Optional<RecentTasks> optional);

        Builder setShellCommandHandler(Optional<ShellCommandHandler> optional);

        Builder setSplitScreen(Optional<SplitScreen> optional);

        Builder setSplitScreenController(Optional<SplitScreenController> optional);

        Builder setStartingSurface(Optional<StartingSurface> optional);

        Builder setTaskSurfaceHelper(Optional<TaskSurfaceHelper> optional);

        Builder setTaskViewFactory(Optional<TaskViewFactory> optional);

        Builder setTransitions(ShellTransitions shellTransitions);
    }

    Dependency createDependency();

    DumpManager createDumpManager();

    ConfigurationController getConfigurationController();

    ContextComponentHelper getContextComponentHelper();

    Optional<FoldStateLogger> getFoldStateLogger();

    Optional<FoldStateLoggingProvider> getFoldStateLoggingProvider();

    InitController getInitController();

    Optional<MediaMuteAwaitConnectionCli> getMediaMuteAwaitConnectionCli();

    Optional<MediaTttChipControllerReceiver> getMediaTttChipControllerReceiver();

    Optional<MediaTttChipControllerSender> getMediaTttChipControllerSender();

    Optional<MediaTttCommandLineHelper> getMediaTttCommandLineHelper();

    Optional<NaturalRotationUnfoldProgressProvider> getNaturalRotationUnfoldProgressProvider();

    Optional<NearbyMediaDevicesManager> getNearbyMediaDevicesManager();

    Map<Class<?>, Provider<CoreStartable>> getPerUserStartables();

    Map<Class<?>, Provider<CoreStartable>> getStartables();

    Optional<SysUIUnfoldComponent> getSysUIUnfoldComponent();

    UnfoldLatencyTracker getUnfoldLatencyTracker();

    void inject(SystemUIAppComponentFactory systemUIAppComponentFactory);

    BootCompleteCacheImpl provideBootCacheImpl();

    void init() {
        getSysUIUnfoldComponent().ifPresent(new SysUIComponent$$ExternalSyntheticLambda0());
        getNaturalRotationUnfoldProgressProvider().ifPresent(new SysUIComponent$$ExternalSyntheticLambda1());
        getMediaTttChipControllerSender();
        getMediaTttChipControllerReceiver();
        getMediaTttCommandLineHelper();
        getMediaMuteAwaitConnectionCli();
        getNearbyMediaDevicesManager();
        getUnfoldLatencyTracker().init();
        getFoldStateLoggingProvider().ifPresent(new SysUIComponent$$ExternalSyntheticLambda2());
        getFoldStateLogger().ifPresent(new SysUIComponent$$ExternalSyntheticLambda3());
    }

    static /* synthetic */ void lambda$init$0(SysUIUnfoldComponent sysUIUnfoldComponent) {
        sysUIUnfoldComponent.getUnfoldLightRevealOverlayAnimation().init();
        sysUIUnfoldComponent.getUnfoldTransitionWallpaperController().init();
    }
}
