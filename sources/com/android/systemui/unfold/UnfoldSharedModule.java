package com.android.systemui.unfold;

import android.hardware.SensorManager;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.progress.FixedTimingTransitionProgressProvider;
import com.android.systemui.unfold.progress.PhysicsBasedUnfoldTransitionProgressProvider;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.hinge.EmptyHingeAngleProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.hinge.HingeSensorAngleProvider;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import java.util.Optional;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldSharedModule.kt */
public final class UnfoldSharedModule {
    @NotNull
    public final FoldStateProvider provideFoldStateProvider(@NotNull DeviceFoldStateProvider deviceFoldStateProvider) {
        return deviceFoldStateProvider;
    }

    @NotNull
    public final Optional<UnfoldTransitionProgressProvider> unfoldTransitionProgressProvider(@NotNull UnfoldTransitionConfig unfoldTransitionConfig, @NotNull ScaleAwareTransitionProgressProvider.Factory factory, @NotNull ATraceLoggerTransitionProgressListener aTraceLoggerTransitionProgressListener, @NotNull FoldStateProvider foldStateProvider) {
        UnfoldTransitionProgressProvider unfoldTransitionProgressProvider;
        if (!unfoldTransitionConfig.isEnabled()) {
            return Optional.empty();
        }
        if (unfoldTransitionConfig.isHingeAngleEnabled()) {
            unfoldTransitionProgressProvider = new PhysicsBasedUnfoldTransitionProgressProvider(foldStateProvider);
        } else {
            unfoldTransitionProgressProvider = new FixedTimingTransitionProgressProvider(foldStateProvider);
        }
        ScaleAwareTransitionProgressProvider wrap = factory.wrap(unfoldTransitionProgressProvider);
        wrap.addCallback((UnfoldTransitionProgressProvider.TransitionProgressListener) aTraceLoggerTransitionProgressListener);
        return Optional.of(wrap);
    }

    @NotNull
    public final HingeAngleProvider hingeAngleProvider(@NotNull UnfoldTransitionConfig unfoldTransitionConfig, @NotNull SensorManager sensorManager, @NotNull Executor executor) {
        if (unfoldTransitionConfig.isHingeAngleEnabled()) {
            return new HingeSensorAngleProvider(sensorManager, executor);
        }
        return EmptyHingeAngleProvider.INSTANCE;
    }
}
