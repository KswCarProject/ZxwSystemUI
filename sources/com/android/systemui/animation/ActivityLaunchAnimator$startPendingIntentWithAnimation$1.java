package com.android.systemui.animation;

import android.view.RemoteAnimationAdapter;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ActivityLaunchAnimator.kt */
public final class ActivityLaunchAnimator$startPendingIntentWithAnimation$1 extends Lambda implements Function1<RemoteAnimationAdapter, Integer> {
    public final /* synthetic */ ActivityLaunchAnimator.PendingIntentStarter $intentStarter;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ActivityLaunchAnimator$startPendingIntentWithAnimation$1(ActivityLaunchAnimator.PendingIntentStarter pendingIntentStarter) {
        super(1);
        this.$intentStarter = pendingIntentStarter;
    }

    @NotNull
    public final Integer invoke(@Nullable RemoteAnimationAdapter remoteAnimationAdapter) {
        return Integer.valueOf(this.$intentStarter.startPendingIntent(remoteAnimationAdapter));
    }
}
