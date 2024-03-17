package com.android.systemui.controls.management;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$loadControls$1$2<T> implements Consumer {
    public final /* synthetic */ ControlsFavoritingActivity this$0;

    public ControlsFavoritingActivity$loadControls$1$2(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public final void accept(@NotNull Runnable runnable) {
        this.this$0.cancelLoadRunnable = runnable;
    }
}
