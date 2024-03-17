package com.android.systemui.util.wrapper;

import android.content.Context;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RotationPolicyWrapperImpl_Factory implements Factory<RotationPolicyWrapperImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<SecureSettings> secureSettingsProvider;

    public RotationPolicyWrapperImpl_Factory(Provider<Context> provider, Provider<SecureSettings> provider2) {
        this.contextProvider = provider;
        this.secureSettingsProvider = provider2;
    }

    public RotationPolicyWrapperImpl get() {
        return newInstance(this.contextProvider.get(), this.secureSettingsProvider.get());
    }

    public static RotationPolicyWrapperImpl_Factory create(Provider<Context> provider, Provider<SecureSettings> provider2) {
        return new RotationPolicyWrapperImpl_Factory(provider, provider2);
    }

    public static RotationPolicyWrapperImpl newInstance(Context context, SecureSettings secureSettings) {
        return new RotationPolicyWrapperImpl(context, secureSettings);
    }
}
