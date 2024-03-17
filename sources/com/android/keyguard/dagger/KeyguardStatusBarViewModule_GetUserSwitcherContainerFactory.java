package com.android.keyguard.dagger;

import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory implements Factory<StatusBarUserSwitcherContainer> {
    public final Provider<KeyguardStatusBarView> viewProvider;

    public KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory(Provider<KeyguardStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public StatusBarUserSwitcherContainer get() {
        return getUserSwitcherContainer(this.viewProvider.get());
    }

    public static KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory create(Provider<KeyguardStatusBarView> provider) {
        return new KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory(provider);
    }

    public static StatusBarUserSwitcherContainer getUserSwitcherContainer(KeyguardStatusBarView keyguardStatusBarView) {
        return (StatusBarUserSwitcherContainer) Preconditions.checkNotNullFromProvides(KeyguardStatusBarViewModule.getUserSwitcherContainer(keyguardStatusBarView));
    }
}
