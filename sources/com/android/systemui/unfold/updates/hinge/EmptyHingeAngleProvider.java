package com.android.systemui.unfold.updates.hinge;

import androidx.core.util.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: EmptyHingeAngleProvider.kt */
public final class EmptyHingeAngleProvider implements HingeAngleProvider {
    @NotNull
    public static final EmptyHingeAngleProvider INSTANCE = new EmptyHingeAngleProvider();

    public void addCallback(@NotNull Consumer<Float> consumer) {
    }

    public void removeCallback(@NotNull Consumer<Float> consumer) {
    }

    public void start() {
    }

    public void stop() {
    }
}
