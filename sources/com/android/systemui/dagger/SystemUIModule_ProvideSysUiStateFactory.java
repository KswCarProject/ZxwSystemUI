package com.android.systemui.dagger;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SystemUIModule_ProvideSysUiStateFactory implements Factory<SysUiState> {
    public final Provider<DumpManager> dumpManagerProvider;

    public SystemUIModule_ProvideSysUiStateFactory(Provider<DumpManager> provider) {
        this.dumpManagerProvider = provider;
    }

    public SysUiState get() {
        return provideSysUiState(this.dumpManagerProvider.get());
    }

    public static SystemUIModule_ProvideSysUiStateFactory create(Provider<DumpManager> provider) {
        return new SystemUIModule_ProvideSysUiStateFactory(provider);
    }

    public static SysUiState provideSysUiState(DumpManager dumpManager) {
        return (SysUiState) Preconditions.checkNotNullFromProvides(SystemUIModule.provideSysUiState(dumpManager));
    }
}
