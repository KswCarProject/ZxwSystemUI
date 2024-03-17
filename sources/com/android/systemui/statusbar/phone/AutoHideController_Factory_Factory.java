package com.android.systemui.statusbar.phone;

import android.os.Handler;
import android.view.IWindowManager;
import com.android.systemui.statusbar.phone.AutoHideController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AutoHideController_Factory_Factory implements Factory<AutoHideController.Factory> {
    public final Provider<Handler> handlerProvider;
    public final Provider<IWindowManager> iWindowManagerProvider;

    public AutoHideController_Factory_Factory(Provider<Handler> provider, Provider<IWindowManager> provider2) {
        this.handlerProvider = provider;
        this.iWindowManagerProvider = provider2;
    }

    public AutoHideController.Factory get() {
        return newInstance(this.handlerProvider.get(), this.iWindowManagerProvider.get());
    }

    public static AutoHideController_Factory_Factory create(Provider<Handler> provider, Provider<IWindowManager> provider2) {
        return new AutoHideController_Factory_Factory(provider, provider2);
    }

    public static AutoHideController.Factory newInstance(Handler handler, IWindowManager iWindowManager) {
        return new AutoHideController.Factory(handler, iWindowManager);
    }
}
