package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.safetycenter.SafetyCenterManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SafetyController_Factory implements Factory<SafetyController> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<PackageManager> pmProvider;
    public final Provider<SafetyCenterManager> scmProvider;

    public SafetyController_Factory(Provider<Context> provider, Provider<PackageManager> provider2, Provider<SafetyCenterManager> provider3, Provider<Handler> provider4) {
        this.contextProvider = provider;
        this.pmProvider = provider2;
        this.scmProvider = provider3;
        this.bgHandlerProvider = provider4;
    }

    public SafetyController get() {
        return newInstance(this.contextProvider.get(), this.pmProvider.get(), this.scmProvider.get(), this.bgHandlerProvider.get());
    }

    public static SafetyController_Factory create(Provider<Context> provider, Provider<PackageManager> provider2, Provider<SafetyCenterManager> provider3, Provider<Handler> provider4) {
        return new SafetyController_Factory(provider, provider2, provider3, provider4);
    }

    public static SafetyController newInstance(Context context, PackageManager packageManager, SafetyCenterManager safetyCenterManager, Handler handler) {
        return new SafetyController(context, packageManager, safetyCenterManager, handler);
    }
}
