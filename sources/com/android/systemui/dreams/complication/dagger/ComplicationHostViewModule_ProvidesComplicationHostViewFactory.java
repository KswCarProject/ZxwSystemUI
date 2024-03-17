package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import androidx.constraintlayout.widget.ConstraintLayout;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ComplicationHostViewModule_ProvidesComplicationHostViewFactory implements Factory<ConstraintLayout> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    public ComplicationHostViewModule_ProvidesComplicationHostViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public ConstraintLayout get() {
        return providesComplicationHostView(this.layoutInflaterProvider.get());
    }

    public static ComplicationHostViewModule_ProvidesComplicationHostViewFactory create(Provider<LayoutInflater> provider) {
        return new ComplicationHostViewModule_ProvidesComplicationHostViewFactory(provider);
    }

    public static ConstraintLayout providesComplicationHostView(LayoutInflater layoutInflater) {
        return (ConstraintLayout) Preconditions.checkNotNullFromProvides(ComplicationHostViewModule.providesComplicationHostView(layoutInflater));
    }
}
