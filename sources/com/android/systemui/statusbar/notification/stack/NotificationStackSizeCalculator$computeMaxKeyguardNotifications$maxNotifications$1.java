package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackSizeCalculator.kt */
public final class NotificationStackSizeCalculator$computeMaxKeyguardNotifications$maxNotifications$1 extends Lambda implements Function1<NotificationStackSizeCalculator.StackHeight, Boolean> {
    public final /* synthetic */ float $spaceForNotifications;
    public final /* synthetic */ float $spaceForShelf;
    public final /* synthetic */ NotificationStackSizeCalculator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationStackSizeCalculator$computeMaxKeyguardNotifications$maxNotifications$1(NotificationStackSizeCalculator notificationStackSizeCalculator, float f, float f2) {
        super(1);
        this.this$0 = notificationStackSizeCalculator;
        this.$spaceForNotifications = f;
        this.$spaceForShelf = f2;
    }

    @NotNull
    public final Boolean invoke(@NotNull NotificationStackSizeCalculator.StackHeight stackHeight) {
        return Boolean.valueOf(this.this$0.canStackFitInSpace(stackHeight, this.$spaceForNotifications, this.$spaceForShelf));
    }
}
