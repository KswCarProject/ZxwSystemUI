package com.android.systemui.statusbar.window;

import android.content.Context;
import android.content.res.Resources;
import android.view.IWindowManager;
import android.view.WindowManager;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class StatusBarWindowController_Factory implements Factory<StatusBarWindowController> {
    public final Provider<StatusBarContentInsetsProvider> contentInsetsProvider;
    public final Provider<Context> contextProvider;
    public final Provider<IWindowManager> iWindowManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<StatusBarWindowView> statusBarWindowViewProvider;
    public final Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public StatusBarWindowController_Factory(Provider<Context> provider, Provider<StatusBarWindowView> provider2, Provider<WindowManager> provider3, Provider<IWindowManager> provider4, Provider<StatusBarContentInsetsProvider> provider5, Provider<Resources> provider6, Provider<Optional<UnfoldTransitionProgressProvider>> provider7) {
        this.contextProvider = provider;
        this.statusBarWindowViewProvider = provider2;
        this.windowManagerProvider = provider3;
        this.iWindowManagerProvider = provider4;
        this.contentInsetsProvider = provider5;
        this.resourcesProvider = provider6;
        this.unfoldTransitionProgressProvider = provider7;
    }

    public StatusBarWindowController get() {
        return newInstance(this.contextProvider.get(), this.statusBarWindowViewProvider.get(), this.windowManagerProvider.get(), this.iWindowManagerProvider.get(), this.contentInsetsProvider.get(), this.resourcesProvider.get(), this.unfoldTransitionProgressProvider.get());
    }

    public static StatusBarWindowController_Factory create(Provider<Context> provider, Provider<StatusBarWindowView> provider2, Provider<WindowManager> provider3, Provider<IWindowManager> provider4, Provider<StatusBarContentInsetsProvider> provider5, Provider<Resources> provider6, Provider<Optional<UnfoldTransitionProgressProvider>> provider7) {
        return new StatusBarWindowController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static StatusBarWindowController newInstance(Context context, StatusBarWindowView statusBarWindowView, WindowManager windowManager, IWindowManager iWindowManager, StatusBarContentInsetsProvider statusBarContentInsetsProvider, Resources resources, Optional<UnfoldTransitionProgressProvider> optional) {
        return new StatusBarWindowController(context, statusBarWindowView, windowManager, iWindowManager, statusBarContentInsetsProvider, resources, optional);
    }
}
