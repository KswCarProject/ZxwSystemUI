package com.android.systemui.media.dagger;

import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class MediaModule_ProvidesNearbyMediaDevicesManagerFactory implements Factory<Optional<NearbyMediaDevicesManager>> {
    public final Provider<MediaFlags> mediaFlagsProvider;
    public final Provider<NearbyMediaDevicesManager> nearbyMediaDevicesManagerLazyProvider;

    public MediaModule_ProvidesNearbyMediaDevicesManagerFactory(Provider<MediaFlags> provider, Provider<NearbyMediaDevicesManager> provider2) {
        this.mediaFlagsProvider = provider;
        this.nearbyMediaDevicesManagerLazyProvider = provider2;
    }

    public Optional<NearbyMediaDevicesManager> get() {
        return providesNearbyMediaDevicesManager(this.mediaFlagsProvider.get(), DoubleCheck.lazy(this.nearbyMediaDevicesManagerLazyProvider));
    }

    public static MediaModule_ProvidesNearbyMediaDevicesManagerFactory create(Provider<MediaFlags> provider, Provider<NearbyMediaDevicesManager> provider2) {
        return new MediaModule_ProvidesNearbyMediaDevicesManagerFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [dagger.Lazy<com.android.systemui.media.nearby.NearbyMediaDevicesManager>, dagger.Lazy] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.media.nearby.NearbyMediaDevicesManager> providesNearbyMediaDevicesManager(com.android.systemui.media.MediaFlags r0, dagger.Lazy<com.android.systemui.media.nearby.NearbyMediaDevicesManager> r1) {
        /*
            java.util.Optional r0 = com.android.systemui.media.dagger.MediaModule.providesNearbyMediaDevicesManager(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dagger.MediaModule_ProvidesNearbyMediaDevicesManagerFactory.providesNearbyMediaDevicesManager(com.android.systemui.media.MediaFlags, dagger.Lazy):java.util.Optional");
    }
}
