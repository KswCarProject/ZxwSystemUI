package com.android.systemui.keyguard;

import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: LifecycleScreenStatusProvider.kt */
public final class LifecycleScreenStatusProvider implements ScreenStatusProvider, ScreenLifecycle.Observer {
    @NotNull
    public final List<ScreenStatusProvider.ScreenListener> listeners = new ArrayList();

    public LifecycleScreenStatusProvider(@NotNull ScreenLifecycle screenLifecycle) {
        screenLifecycle.addObserver(this);
    }

    public void removeCallback(@NotNull ScreenStatusProvider.ScreenListener screenListener) {
        this.listeners.remove(screenListener);
    }

    public void addCallback(@NotNull ScreenStatusProvider.ScreenListener screenListener) {
        this.listeners.add(screenListener);
    }

    public void onScreenTurnedOn() {
        for (ScreenStatusProvider.ScreenListener onScreenTurnedOn : this.listeners) {
            onScreenTurnedOn.onScreenTurnedOn();
        }
    }
}
