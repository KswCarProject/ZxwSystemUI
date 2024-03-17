package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackSizeCalculator.kt */
public final class NotificationStackSizeCalculator$computeHeight$2 extends Lambda implements Function1<Integer, NotificationStackSizeCalculator.StackHeight> {
    public final /* synthetic */ Sequence<NotificationStackSizeCalculator.StackHeight> $heightPerMaxNotifications;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationStackSizeCalculator$computeHeight$2(Sequence<NotificationStackSizeCalculator.StackHeight> sequence) {
        super(1);
        this.$heightPerMaxNotifications = sequence;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    @NotNull
    public final NotificationStackSizeCalculator.StackHeight invoke(int i) {
        return (NotificationStackSizeCalculator.StackHeight) SequencesKt___SequencesKt.last(this.$heightPerMaxNotifications);
    }
}
