package com.android.systemui.statusbar.notification.stack;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackSizeCalculator.kt */
public final class NotificationStackSizeCalculator$childrenSequence$1 extends Lambda implements Function1<View, ExpandableView> {
    public static final NotificationStackSizeCalculator$childrenSequence$1 INSTANCE = new NotificationStackSizeCalculator$childrenSequence$1();

    public NotificationStackSizeCalculator$childrenSequence$1() {
        super(1);
    }

    @NotNull
    public final ExpandableView invoke(View view) {
        if (view != null) {
            return (ExpandableView) view;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.notification.row.ExpandableView");
    }
}
