package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler;

public class HideComplicationModule {
    public static DreamTouchHandler providesHideComplicationTouchHandler(HideComplicationTouchHandler hideComplicationTouchHandler) {
        return hideComplicationTouchHandler;
    }
}
