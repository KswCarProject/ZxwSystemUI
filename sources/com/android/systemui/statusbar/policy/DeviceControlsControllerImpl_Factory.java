package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DeviceControlsControllerImpl_Factory implements Factory<DeviceControlsControllerImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<ControlsComponent> controlsComponentProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<UserContextProvider> userContextProvider;

    public DeviceControlsControllerImpl_Factory(Provider<Context> provider, Provider<ControlsComponent> provider2, Provider<UserContextProvider> provider3, Provider<SecureSettings> provider4) {
        this.contextProvider = provider;
        this.controlsComponentProvider = provider2;
        this.userContextProvider = provider3;
        this.secureSettingsProvider = provider4;
    }

    public DeviceControlsControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.controlsComponentProvider.get(), this.userContextProvider.get(), this.secureSettingsProvider.get());
    }

    public static DeviceControlsControllerImpl_Factory create(Provider<Context> provider, Provider<ControlsComponent> provider2, Provider<UserContextProvider> provider3, Provider<SecureSettings> provider4) {
        return new DeviceControlsControllerImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static DeviceControlsControllerImpl newInstance(Context context, ControlsComponent controlsComponent, UserContextProvider userContextProvider2, SecureSettings secureSettings) {
        return new DeviceControlsControllerImpl(context, controlsComponent, userContextProvider2, secureSettings);
    }
}
