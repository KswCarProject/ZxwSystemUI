package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory implements Factory<DreamTouchHandler> {
    public static DreamTouchHandler providesBouncerSwipeTouchHandler(BouncerSwipeTouchHandler bouncerSwipeTouchHandler) {
        return (DreamTouchHandler) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesBouncerSwipeTouchHandler(bouncerSwipeTouchHandler));
    }
}
