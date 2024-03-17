package com.android.systemui.statusbar.lockscreen;

import android.app.smartspace.SmartspaceManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class LockscreenSmartspaceController_Factory implements Factory<LockscreenSmartspaceController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<ContentResolver> contentResolverProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<Execution> executionProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<Optional<BcSmartspaceDataPlugin>> optionalPluginProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<SmartspaceManager> smartspaceManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<Executor> uiExecutorProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public LockscreenSmartspaceController_Factory(Provider<Context> provider, Provider<FeatureFlags> provider2, Provider<SmartspaceManager> provider3, Provider<ActivityStarter> provider4, Provider<FalsingManager> provider5, Provider<SecureSettings> provider6, Provider<UserTracker> provider7, Provider<ContentResolver> provider8, Provider<ConfigurationController> provider9, Provider<StatusBarStateController> provider10, Provider<DeviceProvisionedController> provider11, Provider<Execution> provider12, Provider<Executor> provider13, Provider<Handler> provider14, Provider<Optional<BcSmartspaceDataPlugin>> provider15) {
        this.contextProvider = provider;
        this.featureFlagsProvider = provider2;
        this.smartspaceManagerProvider = provider3;
        this.activityStarterProvider = provider4;
        this.falsingManagerProvider = provider5;
        this.secureSettingsProvider = provider6;
        this.userTrackerProvider = provider7;
        this.contentResolverProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.statusBarStateControllerProvider = provider10;
        this.deviceProvisionedControllerProvider = provider11;
        this.executionProvider = provider12;
        this.uiExecutorProvider = provider13;
        this.handlerProvider = provider14;
        this.optionalPluginProvider = provider15;
    }

    public LockscreenSmartspaceController get() {
        return newInstance(this.contextProvider.get(), this.featureFlagsProvider.get(), this.smartspaceManagerProvider.get(), this.activityStarterProvider.get(), this.falsingManagerProvider.get(), this.secureSettingsProvider.get(), this.userTrackerProvider.get(), this.contentResolverProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.executionProvider.get(), this.uiExecutorProvider.get(), this.handlerProvider.get(), this.optionalPluginProvider.get());
    }

    public static LockscreenSmartspaceController_Factory create(Provider<Context> provider, Provider<FeatureFlags> provider2, Provider<SmartspaceManager> provider3, Provider<ActivityStarter> provider4, Provider<FalsingManager> provider5, Provider<SecureSettings> provider6, Provider<UserTracker> provider7, Provider<ContentResolver> provider8, Provider<ConfigurationController> provider9, Provider<StatusBarStateController> provider10, Provider<DeviceProvisionedController> provider11, Provider<Execution> provider12, Provider<Executor> provider13, Provider<Handler> provider14, Provider<Optional<BcSmartspaceDataPlugin>> provider15) {
        return new LockscreenSmartspaceController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static LockscreenSmartspaceController newInstance(Context context, FeatureFlags featureFlags, SmartspaceManager smartspaceManager, ActivityStarter activityStarter, FalsingManager falsingManager, SecureSettings secureSettings, UserTracker userTracker, ContentResolver contentResolver, ConfigurationController configurationController, StatusBarStateController statusBarStateController, DeviceProvisionedController deviceProvisionedController, Execution execution, Executor executor, Handler handler, Optional<BcSmartspaceDataPlugin> optional) {
        return new LockscreenSmartspaceController(context, featureFlags, smartspaceManager, activityStarter, falsingManager, secureSettings, userTracker, contentResolver, configurationController, statusBarStateController, deviceProvisionedController, execution, executor, handler, optional);
    }
}
