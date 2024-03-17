package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ComplicationModule_ProvidesVisibilityControllerFactory implements Factory<Complication.VisibilityController> {
    public static Complication.VisibilityController providesVisibilityController(ComplicationLayoutEngine complicationLayoutEngine) {
        return (Complication.VisibilityController) Preconditions.checkNotNullFromProvides(ComplicationModule.providesVisibilityController(complicationLayoutEngine));
    }
}
