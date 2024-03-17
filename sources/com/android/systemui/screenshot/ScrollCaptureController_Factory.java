package com.android.systemui.screenshot;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ScrollCaptureController_Factory implements Factory<ScrollCaptureController> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<ScrollCaptureClient> clientProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ImageTileSet> imageTileSetProvider;
    public final Provider<UiEventLogger> loggerProvider;

    public ScrollCaptureController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<ScrollCaptureClient> provider3, Provider<ImageTileSet> provider4, Provider<UiEventLogger> provider5) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
        this.clientProvider = provider3;
        this.imageTileSetProvider = provider4;
        this.loggerProvider = provider5;
    }

    public ScrollCaptureController get() {
        return newInstance(this.contextProvider.get(), this.bgExecutorProvider.get(), this.clientProvider.get(), this.imageTileSetProvider.get(), this.loggerProvider.get());
    }

    public static ScrollCaptureController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<ScrollCaptureClient> provider3, Provider<ImageTileSet> provider4, Provider<UiEventLogger> provider5) {
        return new ScrollCaptureController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ScrollCaptureController newInstance(Context context, Executor executor, ScrollCaptureClient scrollCaptureClient, Object obj, UiEventLogger uiEventLogger) {
        return new ScrollCaptureController(context, executor, scrollCaptureClient, (ImageTileSet) obj, uiEventLogger);
    }
}
