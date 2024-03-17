package com.android.systemui.util.leak;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.MessageRouter;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GarbageMonitor_Factory implements Factory<GarbageMonitor> {
    public final Provider<Context> contextProvider;
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<LeakDetector> leakDetectorProvider;
    public final Provider<LeakReporter> leakReporterProvider;
    public final Provider<MessageRouter> messageRouterProvider;

    public GarbageMonitor_Factory(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<MessageRouter> provider3, Provider<LeakDetector> provider4, Provider<LeakReporter> provider5, Provider<DumpManager> provider6) {
        this.contextProvider = provider;
        this.delayableExecutorProvider = provider2;
        this.messageRouterProvider = provider3;
        this.leakDetectorProvider = provider4;
        this.leakReporterProvider = provider5;
        this.dumpManagerProvider = provider6;
    }

    public GarbageMonitor get() {
        return newInstance(this.contextProvider.get(), this.delayableExecutorProvider.get(), this.messageRouterProvider.get(), this.leakDetectorProvider.get(), this.leakReporterProvider.get(), this.dumpManagerProvider.get());
    }

    public static GarbageMonitor_Factory create(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<MessageRouter> provider3, Provider<LeakDetector> provider4, Provider<LeakReporter> provider5, Provider<DumpManager> provider6) {
        return new GarbageMonitor_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static GarbageMonitor newInstance(Context context, DelayableExecutor delayableExecutor, MessageRouter messageRouter, LeakDetector leakDetector, LeakReporter leakReporter, DumpManager dumpManager) {
        return new GarbageMonitor(context, delayableExecutor, messageRouter, leakDetector, leakReporter, dumpManager);
    }
}
