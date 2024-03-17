package com.android.systemui.statusbar.phone.shade.transition;

import android.content.Context;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ShadeTransitionController_Factory implements Factory<ShadeTransitionController> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<NoOpOverScroller> noOpOverScrollerProvider;
    public final Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
    public final Provider<SplitShadeOverScroller.Factory> splitShadeOverScrollerFactoryProvider;

    public ShadeTransitionController_Factory(Provider<ConfigurationController> provider, Provider<PanelExpansionStateManager> provider2, Provider<Context> provider3, Provider<SplitShadeOverScroller.Factory> provider4, Provider<NoOpOverScroller> provider5) {
        this.configurationControllerProvider = provider;
        this.panelExpansionStateManagerProvider = provider2;
        this.contextProvider = provider3;
        this.splitShadeOverScrollerFactoryProvider = provider4;
        this.noOpOverScrollerProvider = provider5;
    }

    public ShadeTransitionController get() {
        return newInstance(this.configurationControllerProvider.get(), this.panelExpansionStateManagerProvider.get(), this.contextProvider.get(), this.splitShadeOverScrollerFactoryProvider.get(), this.noOpOverScrollerProvider.get());
    }

    public static ShadeTransitionController_Factory create(Provider<ConfigurationController> provider, Provider<PanelExpansionStateManager> provider2, Provider<Context> provider3, Provider<SplitShadeOverScroller.Factory> provider4, Provider<NoOpOverScroller> provider5) {
        return new ShadeTransitionController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ShadeTransitionController newInstance(ConfigurationController configurationController, PanelExpansionStateManager panelExpansionStateManager, Context context, SplitShadeOverScroller.Factory factory, NoOpOverScroller noOpOverScroller) {
        return new ShadeTransitionController(configurationController, panelExpansionStateManager, context, factory, noOpOverScroller);
    }
}
