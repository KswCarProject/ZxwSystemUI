package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AmbientState_Factory implements Factory<AmbientState> {
    public final Provider<StackScrollAlgorithm.BypassController> bypassControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<StackScrollAlgorithm.SectionProvider> sectionProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;

    public AmbientState_Factory(Provider<Context> provider, Provider<DumpManager> provider2, Provider<StackScrollAlgorithm.SectionProvider> provider3, Provider<StackScrollAlgorithm.BypassController> provider4, Provider<StatusBarKeyguardViewManager> provider5) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.sectionProvider = provider3;
        this.bypassControllerProvider = provider4;
        this.statusBarKeyguardViewManagerProvider = provider5;
    }

    public AmbientState get() {
        return newInstance(this.contextProvider.get(), this.dumpManagerProvider.get(), this.sectionProvider.get(), this.bypassControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get());
    }

    public static AmbientState_Factory create(Provider<Context> provider, Provider<DumpManager> provider2, Provider<StackScrollAlgorithm.SectionProvider> provider3, Provider<StackScrollAlgorithm.BypassController> provider4, Provider<StatusBarKeyguardViewManager> provider5) {
        return new AmbientState_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static AmbientState newInstance(Context context, DumpManager dumpManager, StackScrollAlgorithm.SectionProvider sectionProvider2, StackScrollAlgorithm.BypassController bypassController, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        return new AmbientState(context, dumpManager, sectionProvider2, bypassController, statusBarKeyguardViewManager);
    }
}
