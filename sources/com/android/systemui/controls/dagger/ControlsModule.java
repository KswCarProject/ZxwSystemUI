package com.android.systemui.controls.dagger;

import android.content.pm.PackageManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsModule.kt */
public abstract class ControlsModule {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    public static final boolean providesControlsFeatureEnabled(@NotNull PackageManager packageManager) {
        return Companion.providesControlsFeatureEnabled(packageManager);
    }

    /* compiled from: ControlsModule.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final boolean providesControlsFeatureEnabled(@NotNull PackageManager packageManager) {
            return packageManager.hasSystemFeature("android.software.controls");
        }
    }
}
