package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardSliceView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class KeyguardStatusViewModule_GetKeyguardSliceViewFactory implements Factory<KeyguardSliceView> {
    public final Provider<KeyguardClockSwitch> keyguardClockSwitchProvider;

    public KeyguardStatusViewModule_GetKeyguardSliceViewFactory(Provider<KeyguardClockSwitch> provider) {
        this.keyguardClockSwitchProvider = provider;
    }

    public KeyguardSliceView get() {
        return getKeyguardSliceView(this.keyguardClockSwitchProvider.get());
    }

    public static KeyguardStatusViewModule_GetKeyguardSliceViewFactory create(Provider<KeyguardClockSwitch> provider) {
        return new KeyguardStatusViewModule_GetKeyguardSliceViewFactory(provider);
    }

    public static KeyguardSliceView getKeyguardSliceView(KeyguardClockSwitch keyguardClockSwitch) {
        return (KeyguardSliceView) Preconditions.checkNotNullFromProvides(KeyguardStatusViewModule.getKeyguardSliceView(keyguardClockSwitch));
    }
}
