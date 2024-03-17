package com.android.systemui.unfold.util;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ScopedUnfoldTransitionProgressProvider.kt */
public class ScopedUnfoldTransitionProgressProvider implements UnfoldTransitionProgressProvider, UnfoldTransitionProgressProvider.TransitionProgressListener {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public boolean isReadyToHandleTransition;
    public boolean isTransitionRunning;
    public float lastTransitionProgress;
    @NotNull
    public final List<UnfoldTransitionProgressProvider.TransitionProgressListener> listeners;
    @Nullable
    public UnfoldTransitionProgressProvider source;

    public ScopedUnfoldTransitionProgressProvider() {
        this((UnfoldTransitionProgressProvider) null, 1, (DefaultConstructorMarker) null);
    }

    public ScopedUnfoldTransitionProgressProvider(@Nullable UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        this.listeners = new ArrayList();
        this.lastTransitionProgress = -1.0f;
        setSourceProvider(unfoldTransitionProgressProvider);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ScopedUnfoldTransitionProgressProvider(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : unfoldTransitionProgressProvider);
    }

    public final void setSourceProvider(@Nullable UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        UnfoldTransitionProgressProvider unfoldTransitionProgressProvider2 = this.source;
        if (unfoldTransitionProgressProvider2 != null) {
            unfoldTransitionProgressProvider2.removeCallback(this);
        }
        if (unfoldTransitionProgressProvider != null) {
            this.source = unfoldTransitionProgressProvider;
            unfoldTransitionProgressProvider.addCallback(this);
            return;
        }
        this.source = null;
    }

    public final void setReadyToHandleTransition(boolean z) {
        if (this.isTransitionRunning) {
            boolean z2 = false;
            if (z) {
                for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionStarted : this.listeners) {
                    onTransitionStarted.onTransitionStarted();
                }
                if (this.lastTransitionProgress == -1.0f) {
                    z2 = true;
                }
                if (!z2) {
                    for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionProgress : this.listeners) {
                        onTransitionProgress.onTransitionProgress(this.lastTransitionProgress);
                    }
                }
            } else {
                this.isTransitionRunning = false;
                for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionFinished : this.listeners) {
                    onTransitionFinished.onTransitionFinished();
                }
            }
        }
        this.isReadyToHandleTransition = z;
    }

    public void addCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.add(transitionProgressListener);
    }

    public void removeCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.remove(transitionProgressListener);
    }

    public void onTransitionStarted() {
        this.isTransitionRunning = true;
        if (this.isReadyToHandleTransition) {
            for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionStarted : this.listeners) {
                onTransitionStarted.onTransitionStarted();
            }
        }
    }

    public void onTransitionProgress(float f) {
        if (this.isReadyToHandleTransition) {
            for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionProgress : this.listeners) {
                onTransitionProgress.onTransitionProgress(f);
            }
        }
        this.lastTransitionProgress = f;
    }

    public void onTransitionFinished() {
        if (this.isReadyToHandleTransition) {
            for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionFinished : this.listeners) {
                onTransitionFinished.onTransitionFinished();
            }
        }
        this.isTransitionRunning = false;
        this.lastTransitionProgress = -1.0f;
    }

    /* compiled from: ScopedUnfoldTransitionProgressProvider.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
