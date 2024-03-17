package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ReferenceSystemUIModule_ProvideHeadsUpManagerPhoneFactory implements Factory<HeadsUpManagerPhone> {
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<GroupMembershipManager> groupManagerProvider;
    public final Provider<HeadsUpManagerLogger> headsUpManagerLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VisualStabilityProvider> visualStabilityProvider;

    public ReferenceSystemUIModule_ProvideHeadsUpManagerPhoneFactory(Provider<Context> provider, Provider<HeadsUpManagerLogger> provider2, Provider<StatusBarStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<GroupMembershipManager> provider5, Provider<VisualStabilityProvider> provider6, Provider<ConfigurationController> provider7) {
        this.contextProvider = provider;
        this.headsUpManagerLoggerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.bypassControllerProvider = provider4;
        this.groupManagerProvider = provider5;
        this.visualStabilityProvider = provider6;
        this.configurationControllerProvider = provider7;
    }

    public HeadsUpManagerPhone get() {
        return provideHeadsUpManagerPhone(this.contextProvider.get(), this.headsUpManagerLoggerProvider.get(), this.statusBarStateControllerProvider.get(), this.bypassControllerProvider.get(), this.groupManagerProvider.get(), this.visualStabilityProvider.get(), this.configurationControllerProvider.get());
    }

    public static ReferenceSystemUIModule_ProvideHeadsUpManagerPhoneFactory create(Provider<Context> provider, Provider<HeadsUpManagerLogger> provider2, Provider<StatusBarStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<GroupMembershipManager> provider5, Provider<VisualStabilityProvider> provider6, Provider<ConfigurationController> provider7) {
        return new ReferenceSystemUIModule_ProvideHeadsUpManagerPhoneFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static HeadsUpManagerPhone provideHeadsUpManagerPhone(Context context, HeadsUpManagerLogger headsUpManagerLogger, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, VisualStabilityProvider visualStabilityProvider2, ConfigurationController configurationController) {
        return (HeadsUpManagerPhone) Preconditions.checkNotNullFromProvides(ReferenceSystemUIModule.provideHeadsUpManagerPhone(context, headsUpManagerLogger, statusBarStateController, keyguardBypassController, groupMembershipManager, visualStabilityProvider2, configurationController));
    }
}
