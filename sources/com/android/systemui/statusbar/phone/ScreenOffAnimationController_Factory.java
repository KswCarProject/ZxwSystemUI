package com.android.systemui.statusbar.phone;

import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ScreenOffAnimationController_Factory implements Factory<ScreenOffAnimationController> {
    public final Provider<Optional<SysUIUnfoldComponent>> sysUiUnfoldComponentProvider;
    public final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public ScreenOffAnimationController_Factory(Provider<Optional<SysUIUnfoldComponent>> provider, Provider<UnlockedScreenOffAnimationController> provider2, Provider<WakefulnessLifecycle> provider3) {
        this.sysUiUnfoldComponentProvider = provider;
        this.unlockedScreenOffAnimationProvider = provider2;
        this.wakefulnessLifecycleProvider = provider3;
    }

    public ScreenOffAnimationController get() {
        return newInstance(this.sysUiUnfoldComponentProvider.get(), this.unlockedScreenOffAnimationProvider.get(), this.wakefulnessLifecycleProvider.get());
    }

    public static ScreenOffAnimationController_Factory create(Provider<Optional<SysUIUnfoldComponent>> provider, Provider<UnlockedScreenOffAnimationController> provider2, Provider<WakefulnessLifecycle> provider3) {
        return new ScreenOffAnimationController_Factory(provider, provider2, provider3);
    }

    public static ScreenOffAnimationController newInstance(Optional<SysUIUnfoldComponent> optional, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, WakefulnessLifecycle wakefulnessLifecycle) {
        return new ScreenOffAnimationController(optional, unlockedScreenOffAnimationController, wakefulnessLifecycle);
    }
}
