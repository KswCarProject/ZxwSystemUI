package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaViewController_Factory implements Factory<MediaViewController> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<MediaViewLogger> loggerProvider;
    public final Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;

    public MediaViewController_Factory(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<MediaHostStatesManager> provider3, Provider<MediaViewLogger> provider4) {
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.mediaHostStatesManagerProvider = provider3;
        this.loggerProvider = provider4;
    }

    public MediaViewController get() {
        return newInstance(this.contextProvider.get(), this.configurationControllerProvider.get(), this.mediaHostStatesManagerProvider.get(), this.loggerProvider.get());
    }

    public static MediaViewController_Factory create(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<MediaHostStatesManager> provider3, Provider<MediaViewLogger> provider4) {
        return new MediaViewController_Factory(provider, provider2, provider3, provider4);
    }

    public static MediaViewController newInstance(Context context, ConfigurationController configurationController, MediaHostStatesManager mediaHostStatesManager, MediaViewLogger mediaViewLogger) {
        return new MediaViewController(context, configurationController, mediaHostStatesManager, mediaViewLogger);
    }
}
