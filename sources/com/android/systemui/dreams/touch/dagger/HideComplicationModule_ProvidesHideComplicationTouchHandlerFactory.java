package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class HideComplicationModule_ProvidesHideComplicationTouchHandlerFactory implements Factory<DreamTouchHandler> {
    public static DreamTouchHandler providesHideComplicationTouchHandler(HideComplicationTouchHandler hideComplicationTouchHandler) {
        return (DreamTouchHandler) Preconditions.checkNotNullFromProvides(HideComplicationModule.providesHideComplicationTouchHandler(hideComplicationTouchHandler));
    }
}
