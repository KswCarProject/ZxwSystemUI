package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationPanelUnfoldAnimationController_Factory implements Factory<NotificationPanelUnfoldAnimationController> {
    public final Provider<Context> contextProvider;
    public final Provider<NaturalRotationUnfoldProgressProvider> progressProvider;

    public NotificationPanelUnfoldAnimationController_Factory(Provider<Context> provider, Provider<NaturalRotationUnfoldProgressProvider> provider2) {
        this.contextProvider = provider;
        this.progressProvider = provider2;
    }

    public NotificationPanelUnfoldAnimationController get() {
        return newInstance(this.contextProvider.get(), this.progressProvider.get());
    }

    public static NotificationPanelUnfoldAnimationController_Factory create(Provider<Context> provider, Provider<NaturalRotationUnfoldProgressProvider> provider2) {
        return new NotificationPanelUnfoldAnimationController_Factory(provider, provider2);
    }

    public static NotificationPanelUnfoldAnimationController newInstance(Context context, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        return new NotificationPanelUnfoldAnimationController(context, naturalRotationUnfoldProgressProvider);
    }
}
