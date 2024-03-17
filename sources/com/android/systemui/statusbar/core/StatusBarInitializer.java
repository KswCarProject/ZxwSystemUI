package com.android.systemui.statusbar.core;

import com.android.systemui.R$id;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusBarInitializer.kt */
public final class StatusBarInitializer {
    @Nullable
    public OnStatusBarViewUpdatedListener statusBarViewUpdatedListener;
    @NotNull
    public final StatusBarWindowController windowController;

    /* compiled from: StatusBarInitializer.kt */
    public interface OnStatusBarViewUpdatedListener {
        void onStatusBarViewUpdated(@NotNull PhoneStatusBarView phoneStatusBarView, @NotNull PhoneStatusBarViewController phoneStatusBarViewController, @NotNull PhoneStatusBarTransitions phoneStatusBarTransitions);
    }

    public StatusBarInitializer(@NotNull StatusBarWindowController statusBarWindowController) {
        this.windowController = statusBarWindowController;
    }

    @Nullable
    public final OnStatusBarViewUpdatedListener getStatusBarViewUpdatedListener() {
        return this.statusBarViewUpdatedListener;
    }

    public final void setStatusBarViewUpdatedListener(@Nullable OnStatusBarViewUpdatedListener onStatusBarViewUpdatedListener) {
        this.statusBarViewUpdatedListener = onStatusBarViewUpdatedListener;
    }

    public final void initializeStatusBar(@NotNull CentralSurfacesComponent centralSurfacesComponent) {
        this.windowController.getFragmentHostManager().addTagListener("CollapsedStatusBarFragment", new StatusBarInitializer$initializeStatusBar$1(this)).getFragmentManager().beginTransaction().replace(R$id.status_bar_container, centralSurfacesComponent.createCollapsedStatusBarFragment(), "CollapsedStatusBarFragment").commit();
    }
}
