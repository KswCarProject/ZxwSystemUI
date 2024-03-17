package com.android.systemui.media.dagger;

import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class MediaModule_ProvidesMediaTttCommandLineHelperFactory implements Factory<Optional<MediaTttCommandLineHelper>> {
    public final Provider<MediaTttCommandLineHelper> helperLazyProvider;
    public final Provider<MediaTttFlags> mediaTttFlagsProvider;

    public MediaModule_ProvidesMediaTttCommandLineHelperFactory(Provider<MediaTttFlags> provider, Provider<MediaTttCommandLineHelper> provider2) {
        this.mediaTttFlagsProvider = provider;
        this.helperLazyProvider = provider2;
    }

    public Optional<MediaTttCommandLineHelper> get() {
        return providesMediaTttCommandLineHelper(this.mediaTttFlagsProvider.get(), DoubleCheck.lazy(this.helperLazyProvider));
    }

    public static MediaModule_ProvidesMediaTttCommandLineHelperFactory create(Provider<MediaTttFlags> provider, Provider<MediaTttCommandLineHelper> provider2) {
        return new MediaModule_ProvidesMediaTttCommandLineHelperFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [dagger.Lazy<com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper>, dagger.Lazy] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper> providesMediaTttCommandLineHelper(com.android.systemui.media.taptotransfer.MediaTttFlags r0, dagger.Lazy<com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper> r1) {
        /*
            java.util.Optional r0 = com.android.systemui.media.dagger.MediaModule.providesMediaTttCommandLineHelper(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttCommandLineHelperFactory.providesMediaTttCommandLineHelper(com.android.systemui.media.taptotransfer.MediaTttFlags, dagger.Lazy):java.util.Optional");
    }
}
