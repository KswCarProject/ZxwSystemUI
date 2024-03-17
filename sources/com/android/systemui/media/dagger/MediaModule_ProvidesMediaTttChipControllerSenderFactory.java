package com.android.systemui.media.dagger;

import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class MediaModule_ProvidesMediaTttChipControllerSenderFactory implements Factory<Optional<MediaTttChipControllerSender>> {
    public final Provider<MediaTttChipControllerSender> controllerSenderLazyProvider;
    public final Provider<MediaTttFlags> mediaTttFlagsProvider;

    public MediaModule_ProvidesMediaTttChipControllerSenderFactory(Provider<MediaTttFlags> provider, Provider<MediaTttChipControllerSender> provider2) {
        this.mediaTttFlagsProvider = provider;
        this.controllerSenderLazyProvider = provider2;
    }

    public Optional<MediaTttChipControllerSender> get() {
        return providesMediaTttChipControllerSender(this.mediaTttFlagsProvider.get(), DoubleCheck.lazy(this.controllerSenderLazyProvider));
    }

    public static MediaModule_ProvidesMediaTttChipControllerSenderFactory create(Provider<MediaTttFlags> provider, Provider<MediaTttChipControllerSender> provider2) {
        return new MediaModule_ProvidesMediaTttChipControllerSenderFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [dagger.Lazy<com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender>, dagger.Lazy] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender> providesMediaTttChipControllerSender(com.android.systemui.media.taptotransfer.MediaTttFlags r0, dagger.Lazy<com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender> r1) {
        /*
            java.util.Optional r0 = com.android.systemui.media.dagger.MediaModule.providesMediaTttChipControllerSender(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttChipControllerSenderFactory.providesMediaTttChipControllerSender(com.android.systemui.media.taptotransfer.MediaTttFlags, dagger.Lazy):java.util.Optional");
    }
}
