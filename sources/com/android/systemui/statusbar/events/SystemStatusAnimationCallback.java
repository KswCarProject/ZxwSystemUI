package com.android.systemui.statusbar.events;

import android.animation.Animator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SystemStatusAnimationScheduler.kt */
public interface SystemStatusAnimationCallback {

    /* compiled from: SystemStatusAnimationScheduler.kt */
    public static final class DefaultImpls {
        @Nullable
        public static Animator onSystemEventAnimationBegin(@NotNull SystemStatusAnimationCallback systemStatusAnimationCallback) {
            return null;
        }

        @Nullable
        public static Animator onSystemEventAnimationFinish(@NotNull SystemStatusAnimationCallback systemStatusAnimationCallback, boolean z) {
            return null;
        }
    }

    @Nullable
    Animator onHidePersistentDot() {
        return null;
    }

    @Nullable
    Animator onSystemEventAnimationBegin();

    @Nullable
    Animator onSystemEventAnimationFinish(boolean z);

    @Nullable
    Animator onSystemStatusAnimationTransitionToPersistentDot() {
        return null;
    }
}
