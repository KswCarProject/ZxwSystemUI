package com.android.systemui.statusbar.events;

import android.content.Context;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusEvent.kt */
public interface StatusEvent {

    /* compiled from: StatusEvent.kt */
    public static final class DefaultImpls {
        public static boolean shouldUpdateFromEvent(@NotNull StatusEvent statusEvent, @Nullable StatusEvent statusEvent2) {
            return false;
        }

        public static void updateFromEvent(@NotNull StatusEvent statusEvent, @Nullable StatusEvent statusEvent2) {
        }
    }

    boolean getForceVisible();

    int getPriority();

    boolean getShowAnimation();

    @NotNull
    Function1<Context, BackgroundAnimatableView> getViewCreator();

    boolean shouldUpdateFromEvent(@Nullable StatusEvent statusEvent);

    void updateFromEvent(@Nullable StatusEvent statusEvent);
}
