package com.android.systemui.media;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ResumeMediaBrowserFactory_Factory implements Factory<ResumeMediaBrowserFactory> {
    public final Provider<MediaBrowserFactory> browserFactoryProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ResumeMediaBrowserLogger> loggerProvider;

    public ResumeMediaBrowserFactory_Factory(Provider<Context> provider, Provider<MediaBrowserFactory> provider2, Provider<ResumeMediaBrowserLogger> provider3) {
        this.contextProvider = provider;
        this.browserFactoryProvider = provider2;
        this.loggerProvider = provider3;
    }

    public ResumeMediaBrowserFactory get() {
        return newInstance(this.contextProvider.get(), this.browserFactoryProvider.get(), this.loggerProvider.get());
    }

    public static ResumeMediaBrowserFactory_Factory create(Provider<Context> provider, Provider<MediaBrowserFactory> provider2, Provider<ResumeMediaBrowserLogger> provider3) {
        return new ResumeMediaBrowserFactory_Factory(provider, provider2, provider3);
    }

    public static ResumeMediaBrowserFactory newInstance(Context context, MediaBrowserFactory mediaBrowserFactory, ResumeMediaBrowserLogger resumeMediaBrowserLogger) {
        return new ResumeMediaBrowserFactory(context, mediaBrowserFactory, resumeMediaBrowserLogger);
    }
}
