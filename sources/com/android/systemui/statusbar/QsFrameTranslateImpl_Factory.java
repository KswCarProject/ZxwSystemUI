package com.android.systemui.statusbar;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class QsFrameTranslateImpl_Factory implements Factory<QsFrameTranslateImpl> {
    public final Provider<CentralSurfaces> centralSurfacesProvider;

    public QsFrameTranslateImpl_Factory(Provider<CentralSurfaces> provider) {
        this.centralSurfacesProvider = provider;
    }

    public QsFrameTranslateImpl get() {
        return newInstance(this.centralSurfacesProvider.get());
    }

    public static QsFrameTranslateImpl_Factory create(Provider<CentralSurfaces> provider) {
        return new QsFrameTranslateImpl_Factory(provider);
    }

    public static QsFrameTranslateImpl newInstance(CentralSurfaces centralSurfaces) {
        return new QsFrameTranslateImpl(centralSurfaces);
    }
}
