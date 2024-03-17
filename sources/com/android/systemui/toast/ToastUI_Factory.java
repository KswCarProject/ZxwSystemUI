package com.android.systemui.toast;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ToastUI_Factory implements Factory<ToastUI> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ToastFactory> toastFactoryProvider;
    public final Provider<ToastLogger> toastLoggerProvider;

    public ToastUI_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ToastFactory> provider3, Provider<ToastLogger> provider4) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.toastFactoryProvider = provider3;
        this.toastLoggerProvider = provider4;
    }

    public ToastUI get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.toastFactoryProvider.get(), this.toastLoggerProvider.get());
    }

    public static ToastUI_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ToastFactory> provider3, Provider<ToastLogger> provider4) {
        return new ToastUI_Factory(provider, provider2, provider3, provider4);
    }

    public static ToastUI newInstance(Context context, CommandQueue commandQueue, ToastFactory toastFactory, ToastLogger toastLogger) {
        return new ToastUI(context, commandQueue, toastFactory, toastLogger);
    }
}
