package com.android.systemui.screenshot;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ActionProxyReceiver_Factory implements Factory<ActionProxyReceiver> {
    public final Provider<ActivityManagerWrapper> activityManagerWrapperProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalProvider;
    public final Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;

    public ActionProxyReceiver_Factory(Provider<Optional<CentralSurfaces>> provider, Provider<ActivityManagerWrapper> provider2, Provider<ScreenshotSmartActions> provider3) {
        this.centralSurfacesOptionalProvider = provider;
        this.activityManagerWrapperProvider = provider2;
        this.screenshotSmartActionsProvider = provider3;
    }

    public ActionProxyReceiver get() {
        return newInstance(this.centralSurfacesOptionalProvider.get(), this.activityManagerWrapperProvider.get(), this.screenshotSmartActionsProvider.get());
    }

    public static ActionProxyReceiver_Factory create(Provider<Optional<CentralSurfaces>> provider, Provider<ActivityManagerWrapper> provider2, Provider<ScreenshotSmartActions> provider3) {
        return new ActionProxyReceiver_Factory(provider, provider2, provider3);
    }

    public static ActionProxyReceiver newInstance(Optional<CentralSurfaces> optional, ActivityManagerWrapper activityManagerWrapper, ScreenshotSmartActions screenshotSmartActions) {
        return new ActionProxyReceiver(optional, activityManagerWrapper, screenshotSmartActions);
    }
}
