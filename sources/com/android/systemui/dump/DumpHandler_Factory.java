package com.android.systemui.dump;

import android.content.Context;
import com.android.systemui.CoreStartable;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager;
import dagger.internal.Factory;
import java.util.Map;
import javax.inject.Provider;

public final class DumpHandler_Factory implements Factory<DumpHandler> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<LogBufferEulogizer> logBufferEulogizerProvider;
    public final Provider<Map<Class<?>, Provider<CoreStartable>>> startablesProvider;
    public final Provider<UncaughtExceptionPreHandlerManager> uncaughtExceptionPreHandlerManagerProvider;

    public DumpHandler_Factory(Provider<Context> provider, Provider<DumpManager> provider2, Provider<LogBufferEulogizer> provider3, Provider<Map<Class<?>, Provider<CoreStartable>>> provider4, Provider<UncaughtExceptionPreHandlerManager> provider5) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.logBufferEulogizerProvider = provider3;
        this.startablesProvider = provider4;
        this.uncaughtExceptionPreHandlerManagerProvider = provider5;
    }

    public DumpHandler get() {
        return newInstance(this.contextProvider.get(), this.dumpManagerProvider.get(), this.logBufferEulogizerProvider.get(), this.startablesProvider.get(), this.uncaughtExceptionPreHandlerManagerProvider.get());
    }

    public static DumpHandler_Factory create(Provider<Context> provider, Provider<DumpManager> provider2, Provider<LogBufferEulogizer> provider3, Provider<Map<Class<?>, Provider<CoreStartable>>> provider4, Provider<UncaughtExceptionPreHandlerManager> provider5) {
        return new DumpHandler_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static DumpHandler newInstance(Context context, DumpManager dumpManager, LogBufferEulogizer logBufferEulogizer, Map<Class<?>, Provider<CoreStartable>> map, UncaughtExceptionPreHandlerManager uncaughtExceptionPreHandlerManager) {
        return new DumpHandler(context, dumpManager, logBufferEulogizer, map, uncaughtExceptionPreHandlerManager);
    }
}
