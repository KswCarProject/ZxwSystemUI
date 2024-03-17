package com.android.systemui.statusbar.policy.dagger;

import android.content.Context;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory implements Factory<DeviceStateRotationLockSettingsManager> {
    public final Provider<Context> contextProvider;

    public StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public DeviceStateRotationLockSettingsManager get() {
        return provideAutoRotateSettingsManager(this.contextProvider.get());
    }

    public static StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory create(Provider<Context> provider) {
        return new StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory(provider);
    }

    public static DeviceStateRotationLockSettingsManager provideAutoRotateSettingsManager(Context context) {
        return (DeviceStateRotationLockSettingsManager) Preconditions.checkNotNullFromProvides(StatusBarPolicyModule.provideAutoRotateSettingsManager(context));
    }
}
