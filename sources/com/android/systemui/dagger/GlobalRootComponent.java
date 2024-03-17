package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;

public interface GlobalRootComponent {

    public interface Builder {
        GlobalRootComponent build();

        Builder context(Context context);
    }

    SysUIComponent.Builder getSysUIComponent();

    WMComponent.Builder getWMComponentBuilder();
}
