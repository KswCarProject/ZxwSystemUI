package com.android.systemui.media;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaUiEventLogger_Factory implements Factory<MediaUiEventLogger> {
    public final Provider<UiEventLogger> loggerProvider;

    public MediaUiEventLogger_Factory(Provider<UiEventLogger> provider) {
        this.loggerProvider = provider;
    }

    public MediaUiEventLogger get() {
        return newInstance(this.loggerProvider.get());
    }

    public static MediaUiEventLogger_Factory create(Provider<UiEventLogger> provider) {
        return new MediaUiEventLogger_Factory(provider);
    }

    public static MediaUiEventLogger newInstance(UiEventLogger uiEventLogger) {
        return new MediaUiEventLogger(uiEventLogger);
    }
}
