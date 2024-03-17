package com.android.systemui.qs;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.SecurityController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class QSSecurityFooter_Factory implements Factory<QSSecurityFooter> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> bgLooperProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<View> rootViewProvider;
    public final Provider<SecurityController> securityControllerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public QSSecurityFooter_Factory(Provider<View> provider, Provider<UserTracker> provider2, Provider<Handler> provider3, Provider<ActivityStarter> provider4, Provider<SecurityController> provider5, Provider<DialogLaunchAnimator> provider6, Provider<Looper> provider7, Provider<BroadcastDispatcher> provider8) {
        this.rootViewProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.activityStarterProvider = provider4;
        this.securityControllerProvider = provider5;
        this.dialogLaunchAnimatorProvider = provider6;
        this.bgLooperProvider = provider7;
        this.broadcastDispatcherProvider = provider8;
    }

    public QSSecurityFooter get() {
        return newInstance(this.rootViewProvider.get(), this.userTrackerProvider.get(), this.mainHandlerProvider.get(), this.activityStarterProvider.get(), this.securityControllerProvider.get(), this.dialogLaunchAnimatorProvider.get(), this.bgLooperProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static QSSecurityFooter_Factory create(Provider<View> provider, Provider<UserTracker> provider2, Provider<Handler> provider3, Provider<ActivityStarter> provider4, Provider<SecurityController> provider5, Provider<DialogLaunchAnimator> provider6, Provider<Looper> provider7, Provider<BroadcastDispatcher> provider8) {
        return new QSSecurityFooter_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static QSSecurityFooter newInstance(View view, UserTracker userTracker, Handler handler, ActivityStarter activityStarter, SecurityController securityController, DialogLaunchAnimator dialogLaunchAnimator, Looper looper, BroadcastDispatcher broadcastDispatcher) {
        return new QSSecurityFooter(view, userTracker, handler, activityStarter, securityController, dialogLaunchAnimator, looper, broadcastDispatcher);
    }
}
