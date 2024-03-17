package com.android.systemui.hdmi;

import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class HdmiCecSetMenuLanguageHelper_Factory implements Factory<HdmiCecSetMenuLanguageHelper> {
    public final Provider<Executor> executorProvider;
    public final Provider<SecureSettings> secureSettingsProvider;

    public HdmiCecSetMenuLanguageHelper_Factory(Provider<Executor> provider, Provider<SecureSettings> provider2) {
        this.executorProvider = provider;
        this.secureSettingsProvider = provider2;
    }

    public HdmiCecSetMenuLanguageHelper get() {
        return newInstance(this.executorProvider.get(), this.secureSettingsProvider.get());
    }

    public static HdmiCecSetMenuLanguageHelper_Factory create(Provider<Executor> provider, Provider<SecureSettings> provider2) {
        return new HdmiCecSetMenuLanguageHelper_Factory(provider, provider2);
    }

    public static HdmiCecSetMenuLanguageHelper newInstance(Executor executor, SecureSettings secureSettings) {
        return new HdmiCecSetMenuLanguageHelper(executor, secureSettings);
    }
}
