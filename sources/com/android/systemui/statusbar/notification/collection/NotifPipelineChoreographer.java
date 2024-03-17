package com.android.systemui.statusbar.notification.collection;

import org.jetbrains.annotations.NotNull;

/* compiled from: NotifPipelineChoreographer.kt */
public interface NotifPipelineChoreographer {
    void addOnEvalListener(@NotNull Runnable runnable);

    void schedule();
}
