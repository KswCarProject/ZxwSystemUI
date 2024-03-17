package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import kotlin.Function;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConversationNotifications.kt */
public /* synthetic */ class AnimatedImageNotificationManager$bind$3 implements BindEventManager.Listener, FunctionAdapter {
    public final /* synthetic */ AnimatedImageNotificationManager $tmp0;

    public AnimatedImageNotificationManager$bind$3(AnimatedImageNotificationManager animatedImageNotificationManager) {
        this.$tmp0 = animatedImageNotificationManager;
    }

    public final boolean equals(@Nullable Object obj) {
        if (!(obj instanceof BindEventManager.Listener) || !(obj instanceof FunctionAdapter)) {
            return false;
        }
        return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
    }

    @NotNull
    public final Function<?> getFunctionDelegate() {
        return new AdaptedFunctionReference(1, this.$tmp0, AnimatedImageNotificationManager.class, "updateAnimatedImageDrawables", "updateAnimatedImageDrawables(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Lkotlin/Unit;", 8);
    }

    public final int hashCode() {
        return getFunctionDelegate().hashCode();
    }

    public final void onViewBound(@NotNull NotificationEntry notificationEntry) {
        this.$tmp0.updateAnimatedImageDrawables(notificationEntry);
    }
}
