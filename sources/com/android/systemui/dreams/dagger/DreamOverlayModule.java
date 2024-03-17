package com.android.systemui.dreams.dagger;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.util.Preconditions;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.dreams.DreamOverlayStatusBarView;
import com.android.systemui.touch.TouchInsetManager;
import dagger.Lazy;
import java.util.concurrent.Executor;

public abstract class DreamOverlayModule {
    public static DreamOverlayContainerView providesDreamOverlayContainerView(LayoutInflater layoutInflater) {
        return (DreamOverlayContainerView) Preconditions.checkNotNull((DreamOverlayContainerView) layoutInflater.inflate(R$layout.dream_overlay_container, (ViewGroup) null), "R.layout.dream_layout_container could not be properly inflated");
    }

    public static ViewGroup providesDreamOverlayContentView(DreamOverlayContainerView dreamOverlayContainerView) {
        return (ViewGroup) Preconditions.checkNotNull((ViewGroup) dreamOverlayContainerView.findViewById(R$id.dream_overlay_content), "R.id.dream_overlay_content must not be null");
    }

    public static TouchInsetManager.TouchInsetSession providesTouchInsetSession(TouchInsetManager touchInsetManager) {
        return touchInsetManager.createSession();
    }

    public static TouchInsetManager providesTouchInsetManager(Executor executor, DreamOverlayContainerView dreamOverlayContainerView) {
        return new TouchInsetManager(executor, dreamOverlayContainerView);
    }

    public static DreamOverlayStatusBarView providesDreamOverlayStatusBarView(DreamOverlayContainerView dreamOverlayContainerView) {
        return (DreamOverlayStatusBarView) Preconditions.checkNotNull((DreamOverlayStatusBarView) dreamOverlayContainerView.findViewById(R$id.dream_overlay_status_bar), "R.id.status_bar must not be null");
    }

    public static int providesMaxBurnInOffset(Resources resources) {
        return resources.getDimensionPixelSize(R$dimen.default_burn_in_prevention_offset);
    }

    public static long providesBurnInProtectionUpdateInterval(Resources resources) {
        return (long) resources.getInteger(R$integer.config_dreamOverlayBurnInProtectionUpdateIntervalMillis);
    }

    public static long providesMillisUntilFullJitter(Resources resources) {
        return (long) resources.getInteger(R$integer.config_dreamOverlayMillisUntilFullJitter);
    }

    public static /* synthetic */ Lifecycle lambda$providesLifecycleOwner$0(Lazy lazy) {
        return (Lifecycle) lazy.get();
    }

    public static LifecycleOwner providesLifecycleOwner(Lazy<LifecycleRegistry> lazy) {
        return new DreamOverlayModule$$ExternalSyntheticLambda0(lazy);
    }

    public static LifecycleRegistry providesLifecycleRegistry(LifecycleOwner lifecycleOwner) {
        return new LifecycleRegistry(lifecycleOwner);
    }

    public static Lifecycle providesLifecycle(LifecycleOwner lifecycleOwner) {
        return lifecycleOwner.getLifecycle();
    }
}
