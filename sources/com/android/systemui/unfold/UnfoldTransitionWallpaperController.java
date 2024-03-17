package com.android.systemui.unfold;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.util.WallpaperController;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldTransitionWallpaperController.kt */
public final class UnfoldTransitionWallpaperController {
    @NotNull
    public final UnfoldTransitionProgressProvider unfoldTransitionProgressProvider;
    @NotNull
    public final WallpaperController wallpaperController;

    public UnfoldTransitionWallpaperController(@NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider2, @NotNull WallpaperController wallpaperController2) {
        this.unfoldTransitionProgressProvider = unfoldTransitionProgressProvider2;
        this.wallpaperController = wallpaperController2;
    }

    public final void init() {
        this.unfoldTransitionProgressProvider.addCallback(new TransitionListener());
    }

    /* compiled from: UnfoldTransitionWallpaperController.kt */
    public final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        public TransitionListener() {
        }

        public void onTransitionStarted() {
            UnfoldTransitionProgressProvider.TransitionProgressListener.DefaultImpls.onTransitionStarted(this);
        }

        public void onTransitionProgress(float f) {
            UnfoldTransitionWallpaperController.this.wallpaperController.setUnfoldTransitionZoom(((float) 1) - f);
        }

        public void onTransitionFinished() {
            UnfoldTransitionWallpaperController.this.wallpaperController.setUnfoldTransitionZoom(0.0f);
        }
    }
}
