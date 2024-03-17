package com.android.systemui.media.dagger;

import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class MediaModule_ProvidesMediaTttChipControllerReceiverFactory implements Factory<Optional<MediaTttChipControllerReceiver>> {
    public final Provider<MediaTttChipControllerReceiver> controllerReceiverLazyProvider;
    public final Provider<MediaTttFlags> mediaTttFlagsProvider;

    public MediaModule_ProvidesMediaTttChipControllerReceiverFactory(Provider<MediaTttFlags> provider, Provider<MediaTttChipControllerReceiver> provider2) {
        this.mediaTttFlagsProvider = provider;
        this.controllerReceiverLazyProvider = provider2;
    }

    public Optional<MediaTttChipControllerReceiver> get() {
        return providesMediaTttChipControllerReceiver(this.mediaTttFlagsProvider.get(), DoubleCheck.lazy(this.controllerReceiverLazyProvider));
    }

    public static MediaModule_ProvidesMediaTttChipControllerReceiverFactory create(Provider<MediaTttFlags> provider, Provider<MediaTttChipControllerReceiver> provider2) {
        return new MediaModule_ProvidesMediaTttChipControllerReceiverFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [dagger.Lazy<com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver>, dagger.Lazy] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver> providesMediaTttChipControllerReceiver(com.android.systemui.media.taptotransfer.MediaTttFlags r0, dagger.Lazy<com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver> r1) {
        /*
            java.util.Optional r0 = com.android.systemui.media.dagger.MediaModule.providesMediaTttChipControllerReceiver(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttChipControllerReceiverFactory.providesMediaTttChipControllerReceiver(com.android.systemui.media.taptotransfer.MediaTttFlags, dagger.Lazy):java.util.Optional");
    }
}
