package com.android.systemui.qs.user;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.UserDetailView;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UserSwitchDialogController_Factory implements Factory<UserSwitchDialogController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;

    public UserSwitchDialogController_Factory(Provider<UserDetailView.Adapter> provider, Provider<ActivityStarter> provider2, Provider<FalsingManager> provider3, Provider<DialogLaunchAnimator> provider4, Provider<UiEventLogger> provider5) {
        this.userDetailViewAdapterProvider = provider;
        this.activityStarterProvider = provider2;
        this.falsingManagerProvider = provider3;
        this.dialogLaunchAnimatorProvider = provider4;
        this.uiEventLoggerProvider = provider5;
    }

    public UserSwitchDialogController get() {
        return newInstance(this.userDetailViewAdapterProvider, this.activityStarterProvider.get(), this.falsingManagerProvider.get(), this.dialogLaunchAnimatorProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static UserSwitchDialogController_Factory create(Provider<UserDetailView.Adapter> provider, Provider<ActivityStarter> provider2, Provider<FalsingManager> provider3, Provider<DialogLaunchAnimator> provider4, Provider<UiEventLogger> provider5) {
        return new UserSwitchDialogController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static UserSwitchDialogController newInstance(Provider<UserDetailView.Adapter> provider, ActivityStarter activityStarter, FalsingManager falsingManager, DialogLaunchAnimator dialogLaunchAnimator, UiEventLogger uiEventLogger) {
        return new UserSwitchDialogController(provider, activityStarter, falsingManager, dialogLaunchAnimator, uiEventLogger);
    }
}
