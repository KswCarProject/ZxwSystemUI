package com.android.systemui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraManager;
import android.util.PathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.math.MathKt__MathJVMKt;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: CameraAvailabilityListener.kt */
public final class CameraAvailabilityListener {
    @NotNull
    public static final Factory Factory = new Factory((DefaultConstructorMarker) null);
    @NotNull
    public final CameraManager.AvailabilityCallback availabilityCallback = new CameraAvailabilityListener$availabilityCallback$1(this);
    @NotNull
    public final CameraManager cameraManager;
    @NotNull
    public Rect cutoutBounds = new Rect();
    @NotNull
    public final Path cutoutProtectionPath;
    @NotNull
    public final Set<String> excludedPackageIds;
    @NotNull
    public final Executor executor;
    @NotNull
    public final List<CameraTransitionCallback> listeners = new ArrayList();
    @NotNull
    public final String targetCameraId;

    /* compiled from: CameraAvailabilityListener.kt */
    public interface CameraTransitionCallback {
        void onApplyCameraProtection(@NotNull Path path, @NotNull Rect rect);

        void onHideCameraProtection();
    }

    public CameraAvailabilityListener(@NotNull CameraManager cameraManager2, @NotNull Path path, @NotNull String str, @NotNull String str2, @NotNull Executor executor2) {
        this.cameraManager = cameraManager2;
        this.cutoutProtectionPath = path;
        this.targetCameraId = str;
        this.executor = executor2;
        RectF rectF = new RectF();
        path.computeBounds(rectF, false);
        this.cutoutBounds.set(MathKt__MathJVMKt.roundToInt(rectF.left), MathKt__MathJVMKt.roundToInt(rectF.top), MathKt__MathJVMKt.roundToInt(rectF.right), MathKt__MathJVMKt.roundToInt(rectF.bottom));
        this.excludedPackageIds = CollectionsKt___CollectionsKt.toSet(StringsKt__StringsKt.split$default(str2, new String[]{","}, false, 0, 6, (Object) null));
    }

    public final void startListening() {
        registerCameraListener();
    }

    public final void addTransitionCallback(@NotNull CameraTransitionCallback cameraTransitionCallback) {
        this.listeners.add(cameraTransitionCallback);
    }

    public final boolean isExcluded(String str) {
        return this.excludedPackageIds.contains(str);
    }

    public final void registerCameraListener() {
        this.cameraManager.registerAvailabilityCallback(this.executor, this.availabilityCallback);
    }

    public final void notifyCameraActive() {
        for (CameraTransitionCallback onApplyCameraProtection : this.listeners) {
            onApplyCameraProtection.onApplyCameraProtection(this.cutoutProtectionPath, this.cutoutBounds);
        }
    }

    public final void notifyCameraInactive() {
        for (CameraTransitionCallback onHideCameraProtection : this.listeners) {
            onHideCameraProtection.onHideCameraProtection();
        }
    }

    /* compiled from: CameraAvailabilityListener.kt */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Factory() {
        }

        @NotNull
        public final CameraAvailabilityListener build(@NotNull Context context, @NotNull Executor executor) {
            Object systemService = context.getSystemService("camera");
            if (systemService != null) {
                Resources resources = context.getResources();
                String string = resources.getString(R$string.config_frontBuiltInDisplayCutoutProtection);
                return new CameraAvailabilityListener((CameraManager) systemService, pathFromString(string), resources.getString(R$string.config_protectedCameraId), resources.getString(R$string.config_cameraProtectionExcludedPackages), executor);
            }
            throw new NullPointerException("null cannot be cast to non-null type android.hardware.camera2.CameraManager");
        }

        public final Path pathFromString(String str) {
            try {
                return PathParser.createPathFromPathData(StringsKt__StringsKt.trim(str).toString());
            } catch (Throwable th) {
                throw new IllegalArgumentException("Invalid protection path", th);
            }
        }
    }
}
