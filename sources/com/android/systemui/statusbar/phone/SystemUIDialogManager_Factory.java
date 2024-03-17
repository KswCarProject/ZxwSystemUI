package com.android.systemui.statusbar.phone;

import com.android.keyguard.KeyguardViewController;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SystemUIDialogManager_Factory implements Factory<SystemUIDialogManager> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardViewController> keyguardViewControllerProvider;

    public SystemUIDialogManager_Factory(Provider<DumpManager> provider, Provider<KeyguardViewController> provider2) {
        this.dumpManagerProvider = provider;
        this.keyguardViewControllerProvider = provider2;
    }

    public SystemUIDialogManager get() {
        return newInstance(this.dumpManagerProvider.get(), this.keyguardViewControllerProvider.get());
    }

    public static SystemUIDialogManager_Factory create(Provider<DumpManager> provider, Provider<KeyguardViewController> provider2) {
        return new SystemUIDialogManager_Factory(provider, provider2);
    }

    public static SystemUIDialogManager newInstance(DumpManager dumpManager, KeyguardViewController keyguardViewController) {
        return new SystemUIDialogManager(dumpManager, keyguardViewController);
    }
}
