package com.android.systemui.dreams;

import android.content.res.Resources;
import android.os.Handler;
import android.view.ViewGroup;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DreamOverlayContainerViewController_Factory implements Factory<DreamOverlayContainerViewController> {
    public final Provider<BlurUtils> blurUtilsProvider;
    public final Provider<Long> burnInProtectionUpdateIntervalProvider;
    public final Provider<ComplicationHostViewController> complicationHostViewControllerProvider;
    public final Provider<DreamOverlayContainerView> containerViewProvider;
    public final Provider<ViewGroup> contentViewProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<Integer> maxBurnInOffsetProvider;
    public final Provider<Long> millisUntilFullJitterProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<DreamOverlayStatusBarViewController> statusBarViewControllerProvider;

    public DreamOverlayContainerViewController_Factory(Provider<DreamOverlayContainerView> provider, Provider<ComplicationHostViewController> provider2, Provider<ViewGroup> provider3, Provider<DreamOverlayStatusBarViewController> provider4, Provider<StatusBarKeyguardViewManager> provider5, Provider<BlurUtils> provider6, Provider<Handler> provider7, Provider<Resources> provider8, Provider<Integer> provider9, Provider<Long> provider10, Provider<Long> provider11) {
        this.containerViewProvider = provider;
        this.complicationHostViewControllerProvider = provider2;
        this.contentViewProvider = provider3;
        this.statusBarViewControllerProvider = provider4;
        this.statusBarKeyguardViewManagerProvider = provider5;
        this.blurUtilsProvider = provider6;
        this.handlerProvider = provider7;
        this.resourcesProvider = provider8;
        this.maxBurnInOffsetProvider = provider9;
        this.burnInProtectionUpdateIntervalProvider = provider10;
        this.millisUntilFullJitterProvider = provider11;
    }

    public DreamOverlayContainerViewController get() {
        return newInstance(this.containerViewProvider.get(), this.complicationHostViewControllerProvider.get(), this.contentViewProvider.get(), this.statusBarViewControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.blurUtilsProvider.get(), this.handlerProvider.get(), this.resourcesProvider.get(), this.maxBurnInOffsetProvider.get().intValue(), this.burnInProtectionUpdateIntervalProvider.get().longValue(), this.millisUntilFullJitterProvider.get().longValue());
    }

    public static DreamOverlayContainerViewController_Factory create(Provider<DreamOverlayContainerView> provider, Provider<ComplicationHostViewController> provider2, Provider<ViewGroup> provider3, Provider<DreamOverlayStatusBarViewController> provider4, Provider<StatusBarKeyguardViewManager> provider5, Provider<BlurUtils> provider6, Provider<Handler> provider7, Provider<Resources> provider8, Provider<Integer> provider9, Provider<Long> provider10, Provider<Long> provider11) {
        return new DreamOverlayContainerViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static DreamOverlayContainerViewController newInstance(DreamOverlayContainerView dreamOverlayContainerView, ComplicationHostViewController complicationHostViewController, ViewGroup viewGroup, DreamOverlayStatusBarViewController dreamOverlayStatusBarViewController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, BlurUtils blurUtils, Handler handler, Resources resources, int i, long j, long j2) {
        return new DreamOverlayContainerViewController(dreamOverlayContainerView, complicationHostViewController, viewGroup, dreamOverlayStatusBarViewController, statusBarKeyguardViewManager, blurUtils, handler, resources, i, j, j2);
    }
}
