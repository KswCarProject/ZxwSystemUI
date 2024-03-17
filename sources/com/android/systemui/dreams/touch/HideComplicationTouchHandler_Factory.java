package com.android.systemui.dreams.touch;

import android.os.Handler;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.touch.TouchInsetManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;

public final class HideComplicationTouchHandler_Factory implements Factory<HideComplicationTouchHandler> {
    public static HideComplicationTouchHandler newInstance(Complication.VisibilityController visibilityController, int i, TouchInsetManager touchInsetManager, Executor executor, Handler handler) {
        return new HideComplicationTouchHandler(visibilityController, i, touchInsetManager, executor, handler);
    }
}
