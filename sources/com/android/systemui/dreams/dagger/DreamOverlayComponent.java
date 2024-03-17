package com.android.systemui.dreams.dagger;

import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;

public interface DreamOverlayComponent {

    public interface Factory {
        DreamOverlayComponent create(ViewModelStore viewModelStore, Complication.Host host);
    }

    DreamOverlayContainerViewController getDreamOverlayContainerViewController();

    DreamOverlayTouchMonitor getDreamOverlayTouchMonitor();

    LifecycleRegistry getLifecycleRegistry();
}
