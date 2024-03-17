package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.view.ViewUtil;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaTttChipControllerSender_Factory implements Factory<MediaTttChipControllerSender> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<MediaTttLogger> loggerProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<TapGestureDetector> tapGestureDetectorProvider;
    public final Provider<MediaTttSenderUiEventLogger> uiEventLoggerProvider;
    public final Provider<ViewUtil> viewUtilProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public MediaTttChipControllerSender_Factory(Provider<CommandQueue> provider, Provider<Context> provider2, Provider<MediaTttLogger> provider3, Provider<WindowManager> provider4, Provider<ViewUtil> provider5, Provider<DelayableExecutor> provider6, Provider<TapGestureDetector> provider7, Provider<PowerManager> provider8, Provider<MediaTttSenderUiEventLogger> provider9) {
        this.commandQueueProvider = provider;
        this.contextProvider = provider2;
        this.loggerProvider = provider3;
        this.windowManagerProvider = provider4;
        this.viewUtilProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.tapGestureDetectorProvider = provider7;
        this.powerManagerProvider = provider8;
        this.uiEventLoggerProvider = provider9;
    }

    public MediaTttChipControllerSender get() {
        return newInstance(this.commandQueueProvider.get(), this.contextProvider.get(), this.loggerProvider.get(), this.windowManagerProvider.get(), this.viewUtilProvider.get(), this.mainExecutorProvider.get(), this.tapGestureDetectorProvider.get(), this.powerManagerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static MediaTttChipControllerSender_Factory create(Provider<CommandQueue> provider, Provider<Context> provider2, Provider<MediaTttLogger> provider3, Provider<WindowManager> provider4, Provider<ViewUtil> provider5, Provider<DelayableExecutor> provider6, Provider<TapGestureDetector> provider7, Provider<PowerManager> provider8, Provider<MediaTttSenderUiEventLogger> provider9) {
        return new MediaTttChipControllerSender_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static MediaTttChipControllerSender newInstance(CommandQueue commandQueue, Context context, MediaTttLogger mediaTttLogger, WindowManager windowManager, ViewUtil viewUtil, DelayableExecutor delayableExecutor, TapGestureDetector tapGestureDetector, PowerManager powerManager, MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
        return new MediaTttChipControllerSender(commandQueue, context, mediaTttLogger, windowManager, viewUtil, delayableExecutor, tapGestureDetector, powerManager, mediaTttSenderUiEventLogger);
    }
}
