package com.android.keyguard;

import android.content.ContentResolver;
import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ActiveUnlockConfig_Factory implements Factory<ActiveUnlockConfig> {
    public final Provider<ContentResolver> contentResolverProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;

    public ActiveUnlockConfig_Factory(Provider<Handler> provider, Provider<SecureSettings> provider2, Provider<ContentResolver> provider3, Provider<DumpManager> provider4) {
        this.handlerProvider = provider;
        this.secureSettingsProvider = provider2;
        this.contentResolverProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    public ActiveUnlockConfig get() {
        return newInstance(this.handlerProvider.get(), this.secureSettingsProvider.get(), this.contentResolverProvider.get(), this.dumpManagerProvider.get());
    }

    public static ActiveUnlockConfig_Factory create(Provider<Handler> provider, Provider<SecureSettings> provider2, Provider<ContentResolver> provider3, Provider<DumpManager> provider4) {
        return new ActiveUnlockConfig_Factory(provider, provider2, provider3, provider4);
    }

    public static ActiveUnlockConfig newInstance(Handler handler, SecureSettings secureSettings, ContentResolver contentResolver, DumpManager dumpManager) {
        return new ActiveUnlockConfig(handler, secureSettings, contentResolver, dumpManager);
    }
}
