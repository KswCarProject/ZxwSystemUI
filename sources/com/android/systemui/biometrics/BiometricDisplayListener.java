package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Handler;
import android.view.Display;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BiometricDisplayListener.kt */
public final class BiometricDisplayListener implements DisplayManager.DisplayListener {
    @NotNull
    public final Context context;
    @NotNull
    public final DisplayManager displayManager;
    @NotNull
    public final Handler handler;
    public int lastRotation;
    @NotNull
    public final Function0<Unit> onChanged;
    @NotNull
    public final SensorType sensorType;

    public void onDisplayAdded(int i) {
    }

    public void onDisplayRemoved(int i) {
    }

    public BiometricDisplayListener(@NotNull Context context2, @NotNull DisplayManager displayManager2, @NotNull Handler handler2, @NotNull SensorType sensorType2, @NotNull Function0<Unit> function0) {
        this.context = context2;
        this.displayManager = displayManager2;
        this.handler = handler2;
        this.sensorType = sensorType2;
        this.onChanged = function0;
    }

    public void onDisplayChanged(int i) {
        boolean didRotationChange = didRotationChange();
        if (this.sensorType instanceof SensorType.SideFingerprint) {
            this.onChanged.invoke();
        } else if (didRotationChange) {
            this.onChanged.invoke();
        }
    }

    public final boolean didRotationChange() {
        Display display = this.context.getDisplay();
        Integer valueOf = display == null ? null : Integer.valueOf(display.getRotation());
        if (valueOf == null) {
            return false;
        }
        int intValue = valueOf.intValue();
        int i = this.lastRotation;
        this.lastRotation = intValue;
        if (i != intValue) {
            return true;
        }
        return false;
    }

    public final void enable() {
        Display display = this.context.getDisplay();
        this.lastRotation = display == null ? 0 : display.getRotation();
        this.displayManager.registerDisplayListener(this, this.handler, 4);
    }

    public final void disable() {
        this.displayManager.unregisterDisplayListener(this);
    }

    /* compiled from: BiometricDisplayListener.kt */
    public static abstract class SensorType {
        public /* synthetic */ SensorType(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* compiled from: BiometricDisplayListener.kt */
        public static final class Generic extends SensorType {
            @NotNull
            public static final Generic INSTANCE = new Generic();

            public Generic() {
                super((DefaultConstructorMarker) null);
            }
        }

        public SensorType() {
        }

        /* compiled from: BiometricDisplayListener.kt */
        public static final class UnderDisplayFingerprint extends SensorType {
            @NotNull
            public static final UnderDisplayFingerprint INSTANCE = new UnderDisplayFingerprint();

            public UnderDisplayFingerprint() {
                super((DefaultConstructorMarker) null);
            }
        }

        /* compiled from: BiometricDisplayListener.kt */
        public static final class SideFingerprint extends SensorType {
            @NotNull
            public final FingerprintSensorPropertiesInternal properties;

            public boolean equals(@Nullable Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof SideFingerprint) && Intrinsics.areEqual((Object) this.properties, (Object) ((SideFingerprint) obj).properties);
            }

            public int hashCode() {
                return this.properties.hashCode();
            }

            @NotNull
            public String toString() {
                return "SideFingerprint(properties=" + this.properties + ')';
            }

            public SideFingerprint(@NotNull FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
                super((DefaultConstructorMarker) null);
                this.properties = fingerprintSensorPropertiesInternal;
            }
        }
    }
}
