package com.android.systemui.statusbar.phone;

import org.jetbrains.annotations.NotNull;

/* compiled from: NotifPanelEvents.kt */
public interface NotifPanelEvents {

    /* compiled from: NotifPanelEvents.kt */
    public interface Listener {
        void onLaunchingActivityChanged(boolean z);

        void onPanelCollapsingChanged(boolean z);
    }

    void registerListener(@NotNull Listener listener);
}
