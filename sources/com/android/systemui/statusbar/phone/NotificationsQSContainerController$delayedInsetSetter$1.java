package com.android.systemui.statusbar.phone;

import android.view.DisplayCutout;
import android.view.WindowInsets;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController$delayedInsetSetter$1 implements Runnable, Consumer<WindowInsets> {
    @Nullable
    public Runnable canceller;
    public int cutoutInsets;
    public int stableInsets;
    public final /* synthetic */ NotificationsQSContainerController this$0;

    public NotificationsQSContainerController$delayedInsetSetter$1(NotificationsQSContainerController notificationsQSContainerController) {
        this.this$0 = notificationsQSContainerController;
    }

    public void accept(@NotNull WindowInsets windowInsets) {
        this.stableInsets = windowInsets.getStableInsetBottom();
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        this.cutoutInsets = displayCutout == null ? 0 : displayCutout.getSafeInsetBottom();
        Runnable runnable = this.canceller;
        if (runnable != null) {
            runnable.run();
        }
        this.canceller = this.this$0.delayableExecutor.executeDelayed(this, 500);
    }

    public void run() {
        this.this$0.bottomStableInsets = this.stableInsets;
        this.this$0.bottomCutoutInsets = this.cutoutInsets;
        this.this$0.updateBottomSpacing();
    }
}
