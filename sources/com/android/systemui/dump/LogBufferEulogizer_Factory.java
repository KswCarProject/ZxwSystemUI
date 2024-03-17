package com.android.systemui.dump;

import android.content.Context;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LogBufferEulogizer_Factory implements Factory<LogBufferEulogizer> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Files> filesProvider;
    public final Provider<SystemClock> systemClockProvider;

    public LogBufferEulogizer_Factory(Provider<Context> provider, Provider<DumpManager> provider2, Provider<SystemClock> provider3, Provider<Files> provider4) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.systemClockProvider = provider3;
        this.filesProvider = provider4;
    }

    public LogBufferEulogizer get() {
        return newInstance(this.contextProvider.get(), this.dumpManagerProvider.get(), this.systemClockProvider.get(), this.filesProvider.get());
    }

    public static LogBufferEulogizer_Factory create(Provider<Context> provider, Provider<DumpManager> provider2, Provider<SystemClock> provider3, Provider<Files> provider4) {
        return new LogBufferEulogizer_Factory(provider, provider2, provider3, provider4);
    }

    public static LogBufferEulogizer newInstance(Context context, DumpManager dumpManager, SystemClock systemClock, Files files) {
        return new LogBufferEulogizer(context, dumpManager, systemClock, files);
    }
}
