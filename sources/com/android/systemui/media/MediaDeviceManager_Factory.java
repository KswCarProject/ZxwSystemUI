package com.android.systemui.media;

import android.media.MediaRouter2Manager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaDeviceManager_Factory implements Factory<MediaDeviceManager> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<MediaControllerFactory> controllerFactoryProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Executor> fgExecutorProvider;
    public final Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
    public final Provider<MediaRouter2Manager> mr2managerProvider;
    public final Provider<MediaMuteAwaitConnectionManagerFactory> muteAwaitConnectionManagerFactoryProvider;

    public MediaDeviceManager_Factory(Provider<MediaControllerFactory> provider, Provider<LocalMediaManagerFactory> provider2, Provider<MediaRouter2Manager> provider3, Provider<MediaMuteAwaitConnectionManagerFactory> provider4, Provider<ConfigurationController> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<DumpManager> provider8) {
        this.controllerFactoryProvider = provider;
        this.localMediaManagerFactoryProvider = provider2;
        this.mr2managerProvider = provider3;
        this.muteAwaitConnectionManagerFactoryProvider = provider4;
        this.configurationControllerProvider = provider5;
        this.fgExecutorProvider = provider6;
        this.bgExecutorProvider = provider7;
        this.dumpManagerProvider = provider8;
    }

    public MediaDeviceManager get() {
        return newInstance(this.controllerFactoryProvider.get(), this.localMediaManagerFactoryProvider.get(), this.mr2managerProvider.get(), this.muteAwaitConnectionManagerFactoryProvider.get(), this.configurationControllerProvider.get(), this.fgExecutorProvider.get(), this.bgExecutorProvider.get(), this.dumpManagerProvider.get());
    }

    public static MediaDeviceManager_Factory create(Provider<MediaControllerFactory> provider, Provider<LocalMediaManagerFactory> provider2, Provider<MediaRouter2Manager> provider3, Provider<MediaMuteAwaitConnectionManagerFactory> provider4, Provider<ConfigurationController> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<DumpManager> provider8) {
        return new MediaDeviceManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static MediaDeviceManager newInstance(MediaControllerFactory mediaControllerFactory, LocalMediaManagerFactory localMediaManagerFactory, MediaRouter2Manager mediaRouter2Manager, MediaMuteAwaitConnectionManagerFactory mediaMuteAwaitConnectionManagerFactory, ConfigurationController configurationController, Executor executor, Executor executor2, DumpManager dumpManager) {
        return new MediaDeviceManager(mediaControllerFactory, localMediaManagerFactory, mediaRouter2Manager, mediaMuteAwaitConnectionManagerFactory, configurationController, executor, executor2, dumpManager);
    }
}
