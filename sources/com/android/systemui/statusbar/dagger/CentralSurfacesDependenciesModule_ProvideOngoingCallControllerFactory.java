package com.android.systemui.statusbar.dagger;

import android.app.IActivityManager;
import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class CentralSurfacesDependenciesModule_ProvideOngoingCallControllerFactory implements Factory<OngoingCallController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<IActivityManager> iActivityManagerProvider;
    public final Provider<OngoingCallLogger> loggerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<OngoingCallFlags> ongoingCallFlagsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;
    public final Provider<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandlerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public CentralSurfacesDependenciesModule_ProvideOngoingCallControllerFactory(Provider<Context> provider, Provider<CommonNotifCollection> provider2, Provider<SystemClock> provider3, Provider<ActivityStarter> provider4, Provider<Executor> provider5, Provider<IActivityManager> provider6, Provider<OngoingCallLogger> provider7, Provider<DumpManager> provider8, Provider<StatusBarWindowController> provider9, Provider<SwipeStatusBarAwayGestureHandler> provider10, Provider<StatusBarStateController> provider11, Provider<OngoingCallFlags> provider12) {
        this.contextProvider = provider;
        this.notifCollectionProvider = provider2;
        this.systemClockProvider = provider3;
        this.activityStarterProvider = provider4;
        this.mainExecutorProvider = provider5;
        this.iActivityManagerProvider = provider6;
        this.loggerProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.statusBarWindowControllerProvider = provider9;
        this.swipeStatusBarAwayGestureHandlerProvider = provider10;
        this.statusBarStateControllerProvider = provider11;
        this.ongoingCallFlagsProvider = provider12;
    }

    public OngoingCallController get() {
        return provideOngoingCallController(this.contextProvider.get(), this.notifCollectionProvider.get(), this.systemClockProvider.get(), this.activityStarterProvider.get(), this.mainExecutorProvider.get(), this.iActivityManagerProvider.get(), this.loggerProvider.get(), this.dumpManagerProvider.get(), this.statusBarWindowControllerProvider.get(), this.swipeStatusBarAwayGestureHandlerProvider.get(), this.statusBarStateControllerProvider.get(), this.ongoingCallFlagsProvider.get());
    }

    public static CentralSurfacesDependenciesModule_ProvideOngoingCallControllerFactory create(Provider<Context> provider, Provider<CommonNotifCollection> provider2, Provider<SystemClock> provider3, Provider<ActivityStarter> provider4, Provider<Executor> provider5, Provider<IActivityManager> provider6, Provider<OngoingCallLogger> provider7, Provider<DumpManager> provider8, Provider<StatusBarWindowController> provider9, Provider<SwipeStatusBarAwayGestureHandler> provider10, Provider<StatusBarStateController> provider11, Provider<OngoingCallFlags> provider12) {
        return new CentralSurfacesDependenciesModule_ProvideOngoingCallControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static OngoingCallController provideOngoingCallController(Context context, CommonNotifCollection commonNotifCollection, SystemClock systemClock, ActivityStarter activityStarter, Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger, DumpManager dumpManager, StatusBarWindowController statusBarWindowController, SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler, StatusBarStateController statusBarStateController, OngoingCallFlags ongoingCallFlags) {
        return (OngoingCallController) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideOngoingCallController(context, commonNotifCollection, systemClock, activityStarter, executor, iActivityManager, ongoingCallLogger, dumpManager, statusBarWindowController, swipeStatusBarAwayGestureHandler, statusBarStateController, ongoingCallFlags));
    }
}
