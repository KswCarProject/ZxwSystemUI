package com.android.systemui.media.taptotransfer.receiver;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaTttReceiverUiEventLogger_Factory implements Factory<MediaTttReceiverUiEventLogger> {
    public final Provider<UiEventLogger> loggerProvider;

    public MediaTttReceiverUiEventLogger_Factory(Provider<UiEventLogger> provider) {
        this.loggerProvider = provider;
    }

    public MediaTttReceiverUiEventLogger get() {
        return newInstance(this.loggerProvider.get());
    }

    public static MediaTttReceiverUiEventLogger_Factory create(Provider<UiEventLogger> provider) {
        return new MediaTttReceiverUiEventLogger_Factory(provider);
    }

    public static MediaTttReceiverUiEventLogger newInstance(UiEventLogger uiEventLogger) {
        return new MediaTttReceiverUiEventLogger(uiEventLogger);
    }
}
