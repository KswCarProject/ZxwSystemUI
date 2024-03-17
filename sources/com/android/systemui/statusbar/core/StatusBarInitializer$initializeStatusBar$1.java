package com.android.systemui.statusbar.core;

import android.app.Fragment;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusBarInitializer.kt */
public final class StatusBarInitializer$initializeStatusBar$1 implements FragmentHostManager.FragmentListener {
    public final /* synthetic */ StatusBarInitializer this$0;

    public void onFragmentViewDestroyed(@Nullable String str, @Nullable Fragment fragment) {
    }

    public StatusBarInitializer$initializeStatusBar$1(StatusBarInitializer statusBarInitializer) {
        this.this$0 = statusBarInitializer;
    }

    public void onFragmentViewCreated(@NotNull String str, @NotNull Fragment fragment) {
        StatusBarFragmentComponent statusBarFragmentComponent = ((CollapsedStatusBarFragment) fragment).getStatusBarFragmentComponent();
        if (statusBarFragmentComponent != null) {
            StatusBarInitializer.OnStatusBarViewUpdatedListener statusBarViewUpdatedListener = this.this$0.getStatusBarViewUpdatedListener();
            if (statusBarViewUpdatedListener != null) {
                statusBarViewUpdatedListener.onStatusBarViewUpdated(statusBarFragmentComponent.getPhoneStatusBarView(), statusBarFragmentComponent.getPhoneStatusBarViewController(), statusBarFragmentComponent.getPhoneStatusBarTransitions());
                return;
            }
            return;
        }
        throw new IllegalStateException();
    }
}
