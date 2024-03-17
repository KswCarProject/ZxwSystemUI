package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackSizeCalculator.kt */
public final class NotificationStackSizeCalculator$showableChildren$1 extends Lambda implements Function1<ExpandableView, Boolean> {
    public final /* synthetic */ NotificationStackSizeCalculator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationStackSizeCalculator$showableChildren$1(NotificationStackSizeCalculator notificationStackSizeCalculator) {
        super(1);
        this.this$0 = notificationStackSizeCalculator;
    }

    @NotNull
    public final Boolean invoke(@NotNull ExpandableView expandableView) {
        NotificationStackSizeCalculator notificationStackSizeCalculator = this.this$0;
        return Boolean.valueOf(notificationStackSizeCalculator.isShowable(expandableView, notificationStackSizeCalculator.onLockscreen()));
    }
}
