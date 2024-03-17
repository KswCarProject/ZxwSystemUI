package com.android.systemui;

import android.app.ActivityThread;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.dagger.DaggerGlobalRootComponent;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import com.android.wm.shell.dagger.WMShellConcurrencyModule;
import com.android.wm.shell.transition.ShellTransitions;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public class SystemUIFactory {
    public static SystemUIFactory mFactory;
    public boolean mInitializeComponents;
    public GlobalRootComponent mRootComponent;
    public SysUIComponent mSysUIComponent;
    public WMComponent mWMComponent;

    public SysUIComponent.Builder prepareSysUIComponentBuilder(SysUIComponent.Builder builder, WMComponent wMComponent) {
        return builder;
    }

    public static <T extends SystemUIFactory> T getInstance() {
        return mFactory;
    }

    public static void createFromConfig(Context context) {
        createFromConfig(context, false);
    }

    @VisibleForTesting
    public static void createFromConfig(Context context, boolean z) {
        if (mFactory == null) {
            String string = context.getString(R$string.config_systemUIFactoryComponent);
            if (string == null || string.length() == 0) {
                throw new RuntimeException("No SystemUIFactory component configured");
            }
            try {
                SystemUIFactory systemUIFactory = (SystemUIFactory) context.getClassLoader().loadClass(string).newInstance();
                mFactory = systemUIFactory;
                systemUIFactory.init(context, z);
            } catch (Throwable th) {
                Log.w("SystemUIFactory", "Error creating SystemUIFactory component: " + string, th);
                throw new RuntimeException(th);
            }
        }
    }

    @VisibleForTesting
    public static void cleanup() {
        mFactory = null;
    }

    @VisibleForTesting
    public void init(Context context, boolean z) throws ExecutionException, InterruptedException {
        SysUIComponent.Builder builder;
        this.mInitializeComponents = !z && Process.myUserHandle().isSystem() && ActivityThread.currentProcessName().equals(ActivityThread.currentPackageName());
        this.mRootComponent = buildGlobalRootComponent(context);
        setupWmComponent(context);
        if (this.mInitializeComponents) {
            this.mWMComponent.init();
        }
        SysUIComponent.Builder sysUIComponent = this.mRootComponent.getSysUIComponent();
        if (this.mInitializeComponents) {
            builder = prepareSysUIComponentBuilder(sysUIComponent, this.mWMComponent).setPip(this.mWMComponent.getPip()).setLegacySplitScreen(this.mWMComponent.getLegacySplitScreen()).setLegacySplitScreenController(this.mWMComponent.getLegacySplitScreenController()).setSplitScreen(this.mWMComponent.getSplitScreen()).setSplitScreenController(this.mWMComponent.getSplitScreenController()).setOneHanded(this.mWMComponent.getOneHanded()).setBubbles(this.mWMComponent.getBubbles()).setHideDisplayCutout(this.mWMComponent.getHideDisplayCutout()).setShellCommandHandler(this.mWMComponent.getShellCommandHandler()).setAppPairs(this.mWMComponent.getAppPairs()).setTaskViewFactory(this.mWMComponent.getTaskViewFactory()).setTransitions(this.mWMComponent.getTransitions()).setStartingSurface(this.mWMComponent.getStartingSurface()).setDisplayAreaHelper(this.mWMComponent.getDisplayAreaHelper()).setTaskSurfaceHelper(this.mWMComponent.getTaskSurfaceHelper()).setRecentTasks(this.mWMComponent.getRecentTasks()).setCompatUI(this.mWMComponent.getCompatUI()).setDragAndDrop(this.mWMComponent.getDragAndDrop()).setBackAnimation(this.mWMComponent.getBackAnimation());
        } else {
            builder = prepareSysUIComponentBuilder(sysUIComponent, this.mWMComponent).setPip(Optional.ofNullable((Object) null)).setLegacySplitScreen(Optional.ofNullable((Object) null)).setLegacySplitScreenController(Optional.ofNullable((Object) null)).setSplitScreen(Optional.ofNullable((Object) null)).setSplitScreenController(Optional.ofNullable((Object) null)).setOneHanded(Optional.ofNullable((Object) null)).setBubbles(Optional.ofNullable((Object) null)).setHideDisplayCutout(Optional.ofNullable((Object) null)).setShellCommandHandler(Optional.ofNullable((Object) null)).setAppPairs(Optional.ofNullable((Object) null)).setTaskViewFactory(Optional.ofNullable((Object) null)).setTransitions(new ShellTransitions() {
            }).setDisplayAreaHelper(Optional.ofNullable((Object) null)).setStartingSurface(Optional.ofNullable((Object) null)).setTaskSurfaceHelper(Optional.ofNullable((Object) null)).setRecentTasks(Optional.ofNullable((Object) null)).setCompatUI(Optional.ofNullable((Object) null)).setDragAndDrop(Optional.ofNullable((Object) null)).setBackAnimation(Optional.ofNullable((Object) null));
        }
        SysUIComponent build = builder.build();
        this.mSysUIComponent = build;
        if (this.mInitializeComponents) {
            build.init();
        }
        this.mSysUIComponent.createDependency().start();
    }

    public final void setupWmComponent(Context context) {
        WMComponent.Builder wMComponentBuilder = this.mRootComponent.getWMComponentBuilder();
        if (!this.mInitializeComponents || !WMShellConcurrencyModule.enableShellMainThread(context)) {
            this.mWMComponent = wMComponentBuilder.build();
            return;
        }
        HandlerThread createShellMainThread = WMShellConcurrencyModule.createShellMainThread();
        createShellMainThread.start();
        if (!Handler.createAsync(createShellMainThread.getLooper()).runWithScissors(new SystemUIFactory$$ExternalSyntheticLambda0(this, wMComponentBuilder, createShellMainThread), 5000)) {
            Log.w("SystemUIFactory", "Failed to initialize WMComponent");
            throw new RuntimeException();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupWmComponent$0(WMComponent.Builder builder, HandlerThread handlerThread) {
        builder.setShellMainThread(handlerThread);
        this.mWMComponent = builder.build();
    }

    public GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerGlobalRootComponent.builder().context(context).build();
    }

    public GlobalRootComponent getRootComponent() {
        return this.mRootComponent;
    }

    public SysUIComponent getSysUIComponent() {
        return this.mSysUIComponent;
    }

    public Map<Class<?>, Provider<CoreStartable>> getStartableComponents() {
        return this.mSysUIComponent.getStartables();
    }

    public String getVendorComponent(Resources resources) {
        return resources.getString(R$string.config_systemUIVendorServiceComponent);
    }

    public Map<Class<?>, Provider<CoreStartable>> getStartableComponentsPerUser() {
        return this.mSysUIComponent.getPerUserStartables();
    }

    public ScreenshotNotificationSmartActionsProvider createScreenshotNotificationSmartActionsProvider(Context context, Executor executor, Handler handler) {
        return new ScreenshotNotificationSmartActionsProvider();
    }

    public BackGestureTfClassifierProvider createBackGestureTfClassifierProvider(AssetManager assetManager, String str) {
        return new BackGestureTfClassifierProvider();
    }
}
