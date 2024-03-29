package com.android.systemui.media;

import android.os.Trace;
import com.android.systemui.util.animation.MeasurementOutput;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaHostStatesManager.kt */
public final class MediaHostStatesManager {
    @NotNull
    public final Set<Callback> callbacks = new LinkedHashSet();
    @NotNull
    public final Map<Integer, MeasurementOutput> carouselSizes = new LinkedHashMap();
    @NotNull
    public final Set<MediaViewController> controllers = new LinkedHashSet();
    @NotNull
    public final Map<Integer, MediaHostState> mediaHostStates = new LinkedHashMap();

    /* compiled from: MediaHostStatesManager.kt */
    public interface Callback {
        void onHostStateChanged(int i, @NotNull MediaHostState mediaHostState);
    }

    @NotNull
    public final MeasurementOutput updateCarouselDimensions(int i, @NotNull MediaHostState mediaHostState) {
        Trace.beginSection("MediaHostStatesManager#updateCarouselDimensions");
        try {
            MeasurementOutput measurementOutput = new MeasurementOutput(0, 0);
            for (MediaViewController measurementsForState : this.controllers) {
                MeasurementOutput measurementsForState2 = measurementsForState.getMeasurementsForState(mediaHostState);
                if (measurementsForState2 != null) {
                    if (measurementsForState2.getMeasuredHeight() > measurementOutput.getMeasuredHeight()) {
                        measurementOutput.setMeasuredHeight(measurementsForState2.getMeasuredHeight());
                    }
                    if (measurementsForState2.getMeasuredWidth() > measurementOutput.getMeasuredWidth()) {
                        measurementOutput.setMeasuredWidth(measurementsForState2.getMeasuredWidth());
                    }
                }
            }
            getCarouselSizes().put(Integer.valueOf(i), measurementOutput);
            return measurementOutput;
        } finally {
            Trace.endSection();
        }
    }

    public final void updateHostState(int i, @NotNull MediaHostState mediaHostState) {
        Trace.beginSection("MediaHostStatesManager#updateHostState");
        try {
            if (!mediaHostState.equals(getMediaHostStates().get(Integer.valueOf(i)))) {
                MediaHostState copy = mediaHostState.copy();
                getMediaHostStates().put(Integer.valueOf(i), copy);
                updateCarouselDimensions(i, mediaHostState);
                for (MediaViewController stateCallback : this.controllers) {
                    stateCallback.getStateCallback().onHostStateChanged(i, copy);
                }
                for (Callback onHostStateChanged : this.callbacks) {
                    onHostStateChanged.onHostStateChanged(i, copy);
                }
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    @NotNull
    public final Map<Integer, MeasurementOutput> getCarouselSizes() {
        return this.carouselSizes;
    }

    @NotNull
    public final Map<Integer, MediaHostState> getMediaHostStates() {
        return this.mediaHostStates;
    }

    public final void addCallback(@NotNull Callback callback) {
        this.callbacks.add(callback);
    }

    public final void addController(@NotNull MediaViewController mediaViewController) {
        this.controllers.add(mediaViewController);
    }

    public final void removeController(@NotNull MediaViewController mediaViewController) {
        this.controllers.remove(mediaViewController);
    }
}
