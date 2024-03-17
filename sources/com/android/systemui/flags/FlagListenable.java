package com.android.systemui.flags;

import org.jetbrains.annotations.NotNull;

/* compiled from: FlagListenable.kt */
public interface FlagListenable {

    /* compiled from: FlagListenable.kt */
    public interface FlagEvent {
        void requestNoRestart();
    }

    /* compiled from: FlagListenable.kt */
    public interface Listener {
        void onFlagChanged(@NotNull FlagEvent flagEvent);
    }

    void addListener(@NotNull Flag<?> flag, @NotNull Listener listener);
}
