package com.android.systemui.unfold;

import com.android.systemui.statusbar.policy.CallbackController;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldTransitionProgressProvider.kt */
public interface UnfoldTransitionProgressProvider extends CallbackController<TransitionProgressListener> {

    /* compiled from: UnfoldTransitionProgressProvider.kt */
    public interface TransitionProgressListener {

        /* compiled from: UnfoldTransitionProgressProvider.kt */
        public static final class DefaultImpls {
            public static void onTransitionFinished(@NotNull TransitionProgressListener transitionProgressListener) {
            }

            public static void onTransitionProgress(@NotNull TransitionProgressListener transitionProgressListener, float f) {
            }

            public static void onTransitionStarted(@NotNull TransitionProgressListener transitionProgressListener) {
            }
        }

        void onTransitionFinished();

        void onTransitionProgress(float f);

        void onTransitionStarted();
    }
}
