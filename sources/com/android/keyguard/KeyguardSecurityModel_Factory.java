package com.android.keyguard;

import android.content.res.Resources;
import com.android.internal.widget.LockPatternUtils;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardSecurityModel_Factory implements Factory<KeyguardSecurityModel> {
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<Resources> resourcesProvider;

    public KeyguardSecurityModel_Factory(Provider<Resources> provider, Provider<LockPatternUtils> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        this.resourcesProvider = provider;
        this.lockPatternUtilsProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    public KeyguardSecurityModel get() {
        return newInstance(this.resourcesProvider.get(), this.lockPatternUtilsProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static KeyguardSecurityModel_Factory create(Provider<Resources> provider, Provider<LockPatternUtils> provider2, Provider<KeyguardUpdateMonitor> provider3) {
        return new KeyguardSecurityModel_Factory(provider, provider2, provider3);
    }

    public static KeyguardSecurityModel newInstance(Resources resources, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new KeyguardSecurityModel(resources, lockPatternUtils, keyguardUpdateMonitor);
    }
}
