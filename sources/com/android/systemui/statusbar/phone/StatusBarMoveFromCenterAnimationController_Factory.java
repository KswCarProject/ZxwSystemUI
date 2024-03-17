package com.android.systemui.statusbar.phone;

import android.view.WindowManager;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarMoveFromCenterAnimationController_Factory implements Factory<StatusBarMoveFromCenterAnimationController> {
    public final Provider<ScopedUnfoldTransitionProgressProvider> progressProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public StatusBarMoveFromCenterAnimationController_Factory(Provider<ScopedUnfoldTransitionProgressProvider> provider, Provider<WindowManager> provider2) {
        this.progressProvider = provider;
        this.windowManagerProvider = provider2;
    }

    public StatusBarMoveFromCenterAnimationController get() {
        return newInstance(this.progressProvider.get(), this.windowManagerProvider.get());
    }

    public static StatusBarMoveFromCenterAnimationController_Factory create(Provider<ScopedUnfoldTransitionProgressProvider> provider, Provider<WindowManager> provider2) {
        return new StatusBarMoveFromCenterAnimationController_Factory(provider, provider2);
    }

    public static StatusBarMoveFromCenterAnimationController newInstance(ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, WindowManager windowManager) {
        return new StatusBarMoveFromCenterAnimationController(scopedUnfoldTransitionProgressProvider, windowManager);
    }
}
