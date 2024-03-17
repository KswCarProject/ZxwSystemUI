package com.android.systemui.media.taptotransfer.sender;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaTttSenderUiEventLogger_Factory implements Factory<MediaTttSenderUiEventLogger> {
    public final Provider<UiEventLogger> loggerProvider;

    public MediaTttSenderUiEventLogger_Factory(Provider<UiEventLogger> provider) {
        this.loggerProvider = provider;
    }

    public MediaTttSenderUiEventLogger get() {
        return newInstance(this.loggerProvider.get());
    }

    public static MediaTttSenderUiEventLogger_Factory create(Provider<UiEventLogger> provider) {
        return new MediaTttSenderUiEventLogger_Factory(provider);
    }

    public static MediaTttSenderUiEventLogger newInstance(UiEventLogger uiEventLogger) {
        return new MediaTttSenderUiEventLogger(uiEventLogger);
    }
}
