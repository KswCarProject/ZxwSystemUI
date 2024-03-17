package com.android.systemui.media.dagger;

import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory implements Factory<Optional<MediaMuteAwaitConnectionCli>> {
    public final Provider<MediaFlags> mediaFlagsProvider;
    public final Provider<MediaMuteAwaitConnectionCli> muteAwaitConnectionCliLazyProvider;

    public MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory(Provider<MediaFlags> provider, Provider<MediaMuteAwaitConnectionCli> provider2) {
        this.mediaFlagsProvider = provider;
        this.muteAwaitConnectionCliLazyProvider = provider2;
    }

    public Optional<MediaMuteAwaitConnectionCli> get() {
        return providesMediaMuteAwaitConnectionCli(this.mediaFlagsProvider.get(), DoubleCheck.lazy(this.muteAwaitConnectionCliLazyProvider));
    }

    public static MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory create(Provider<MediaFlags> provider, Provider<MediaMuteAwaitConnectionCli> provider2) {
        return new MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [dagger.Lazy<com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli>, dagger.Lazy] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli> providesMediaMuteAwaitConnectionCli(com.android.systemui.media.MediaFlags r0, dagger.Lazy<com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli> r1) {
        /*
            java.util.Optional r0 = com.android.systemui.media.dagger.MediaModule.providesMediaMuteAwaitConnectionCli(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dagger.MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory.providesMediaMuteAwaitConnectionCli(com.android.systemui.media.MediaFlags, dagger.Lazy):java.util.Optional");
    }
}
