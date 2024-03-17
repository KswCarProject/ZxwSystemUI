package com.android.systemui.statusbar.notification;

import android.view.View;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationNotifications.kt */
public final class AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 extends Lambda implements Function1<NotificationContentView, Sequence<? extends View>> {
    public static final AnimatedImageNotificationManager$updateAnimatedImageDrawables$2 INSTANCE = new AnimatedImageNotificationManager$updateAnimatedImageDrawables$2();

    public AnimatedImageNotificationManager$updateAnimatedImageDrawables$2() {
        super(1);
    }

    @NotNull
    public final Sequence<View> invoke(NotificationContentView notificationContentView) {
        return ArraysKt___ArraysKt.asSequence(notificationContentView.getAllViews());
    }
}
