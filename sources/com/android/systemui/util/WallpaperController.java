package com.android.systemui.util;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.util.Log;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WallpaperController.kt */
public final class WallpaperController {
    public float notificationShadeZoomOut;
    @Nullable
    public View rootView;
    public float unfoldTransitionZoomOut;
    @Nullable
    public WallpaperInfo wallpaperInfo;
    @NotNull
    public final WallpaperManager wallpaperManager;

    public WallpaperController(@NotNull WallpaperManager wallpaperManager2) {
        this.wallpaperManager = wallpaperManager2;
    }

    public final void setRootView(@Nullable View view) {
        this.rootView = view;
    }

    public final void onWallpaperInfoUpdated(@Nullable WallpaperInfo wallpaperInfo2) {
        this.wallpaperInfo = wallpaperInfo2;
    }

    public final boolean getShouldUseDefaultUnfoldTransition() {
        WallpaperInfo wallpaperInfo2 = this.wallpaperInfo;
        if (wallpaperInfo2 == null) {
            return true;
        }
        return wallpaperInfo2.shouldUseDefaultUnfoldTransition();
    }

    public final void setNotificationShadeZoom(float f) {
        this.notificationShadeZoomOut = f;
        updateZoom();
    }

    public final void setUnfoldTransitionZoom(float f) {
        if (getShouldUseDefaultUnfoldTransition()) {
            this.unfoldTransitionZoomOut = f;
            updateZoom();
        }
    }

    public final void updateZoom() {
        setWallpaperZoom(Math.max(this.notificationShadeZoomOut, this.unfoldTransitionZoomOut));
    }

    public final void setWallpaperZoom(float f) {
        try {
            View view = this.rootView;
            if (view != null) {
                if (!view.isAttachedToWindow() || view.getWindowToken() == null) {
                    Log.i("WallpaperController", Intrinsics.stringPlus("Won't set zoom. Window not attached ", view));
                } else {
                    this.wallpaperManager.setWallpaperZoomOut(view.getWindowToken(), f);
                }
            }
        } catch (IllegalArgumentException e) {
            View view2 = this.rootView;
            Log.w("WallpaperController", Intrinsics.stringPlus("Can't set zoom. Window is gone: ", view2 == null ? null : view2.getWindowToken()), e);
        }
    }
}
