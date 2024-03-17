package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Handler;
import android.util.Log;
import android.util.RotationUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import com.airbnb.lottie.LottieAnimationView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.recents.OverviewProxyService;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SidefpsController.kt */
public final class SidefpsController {
    @NotNull
    public final ActivityTaskManager activityTaskManager;
    public final long animationDuration;
    @NotNull
    public final Context context;
    @NotNull
    public final Handler handler;
    @NotNull
    public final LayoutInflater layoutInflater;
    @NotNull
    public final BiometricDisplayListener orientationListener;
    @Nullable
    public ViewPropertyAnimator overlayHideAnimator;
    @NotNull
    public SensorLocationInternal overlayOffsets;
    @Nullable
    public View overlayView;
    @NotNull
    public final WindowManager.LayoutParams overlayViewParams;
    @NotNull
    public final OverviewProxyService.OverviewProxyListener overviewProxyListener;
    @NotNull
    public final FingerprintSensorPropertiesInternal sensorProps;
    @NotNull
    public final WindowManager windowManager;

    @VisibleForTesting
    public static /* synthetic */ void getOrientationListener$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getOverlayOffsets$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getOverviewProxyListener$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSensorProps$annotations() {
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v1, resolved type: android.hardware.fingerprint.FingerprintSensorPropertiesInternal} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: android.hardware.fingerprint.FingerprintSensorPropertiesInternal} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: android.hardware.fingerprint.FingerprintSensorPropertiesInternal} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v9, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v1, resolved type: android.hardware.fingerprint.FingerprintSensorPropertiesInternal} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: android.hardware.fingerprint.FingerprintSensorPropertiesInternal} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SidefpsController(@org.jetbrains.annotations.NotNull android.content.Context r7, @org.jetbrains.annotations.NotNull android.view.LayoutInflater r8, @org.jetbrains.annotations.Nullable android.hardware.fingerprint.FingerprintManager r9, @org.jetbrains.annotations.NotNull android.view.WindowManager r10, @org.jetbrains.annotations.NotNull android.app.ActivityTaskManager r11, @org.jetbrains.annotations.NotNull com.android.systemui.recents.OverviewProxyService r12, @org.jetbrains.annotations.NotNull android.hardware.display.DisplayManager r13, @org.jetbrains.annotations.NotNull final com.android.systemui.util.concurrency.DelayableExecutor r14, @org.jetbrains.annotations.NotNull android.os.Handler r15) {
        /*
            r6 = this;
            r6.<init>()
            r6.context = r7
            r6.layoutInflater = r8
            r6.windowManager = r10
            r6.activityTaskManager = r11
            r6.handler = r15
            r7 = 0
            if (r9 != 0) goto L_0x0011
            goto L_0x0034
        L_0x0011:
            java.util.List r8 = r9.getSensorPropertiesInternal()
            if (r8 != 0) goto L_0x0018
            goto L_0x0034
        L_0x0018:
            java.lang.Iterable r8 = (java.lang.Iterable) r8
            java.util.Iterator r8 = r8.iterator()
        L_0x001e:
            boolean r10 = r8.hasNext()
            if (r10 == 0) goto L_0x0032
            java.lang.Object r10 = r8.next()
            r11 = r10
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r11 = (android.hardware.fingerprint.FingerprintSensorPropertiesInternal) r11
            boolean r11 = r11.isAnySidefpsType()
            if (r11 == 0) goto L_0x001e
            r7 = r10
        L_0x0032:
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r7 = (android.hardware.fingerprint.FingerprintSensorPropertiesInternal) r7
        L_0x0034:
            if (r7 == 0) goto L_0x009d
            r6.sensorProps = r7
            com.android.systemui.biometrics.BiometricDisplayListener r8 = new com.android.systemui.biometrics.BiometricDisplayListener
            android.content.Context r1 = r6.context
            android.os.Handler r3 = r6.handler
            com.android.systemui.biometrics.BiometricDisplayListener$SensorType$SideFingerprint r4 = new com.android.systemui.biometrics.BiometricDisplayListener$SensorType$SideFingerprint
            r4.<init>(r7)
            com.android.systemui.biometrics.SidefpsController$orientationListener$1 r5 = new com.android.systemui.biometrics.SidefpsController$orientationListener$1
            r5.<init>(r6)
            r0 = r8
            r2 = r13
            r0.<init>(r1, r2, r3, r4, r5)
            r6.orientationListener = r8
            com.android.systemui.biometrics.SidefpsController$overviewProxyListener$1 r7 = new com.android.systemui.biometrics.SidefpsController$overviewProxyListener$1
            r7.<init>(r6)
            r6.overviewProxyListener = r7
            android.content.Context r8 = r6.context
            android.content.res.Resources r8 = r8.getResources()
            r10 = 17694721(0x10e0001, float:2.6081284E-38)
            int r8 = r8.getInteger(r10)
            long r10 = (long) r8
            r6.animationDuration = r10
            android.hardware.biometrics.SensorLocationInternal r8 = android.hardware.biometrics.SensorLocationInternal.DEFAULT
            r6.overlayOffsets = r8
            android.view.WindowManager$LayoutParams r8 = new android.view.WindowManager$LayoutParams
            r1 = -2
            r2 = -2
            r3 = 2010(0x7da, float:2.817E-42)
            r4 = 16777512(0x1000128, float:2.3510717E-38)
            r5 = -3
            r0 = r8
            r0.<init>(r1, r2, r3, r4, r5)
            java.lang.String r10 = "SidefpsController"
            r8.setTitle(r10)
            r10 = 0
            r8.setFitInsetsTypes(r10)
            r10 = 51
            r8.gravity = r10
            r10 = 3
            r8.layoutInDisplayCutoutMode = r10
            r10 = 536870912(0x20000000, float:1.0842022E-19)
            r8.privateFlags = r10
            r6.overlayViewParams = r8
            if (r9 != 0) goto L_0x0091
            goto L_0x0099
        L_0x0091:
            com.android.systemui.biometrics.SidefpsController$1 r8 = new com.android.systemui.biometrics.SidefpsController$1
            r8.<init>(r6, r14)
            r9.setSidefpsController(r8)
        L_0x0099:
            r12.addCallback((com.android.systemui.recents.OverviewProxyService.OverviewProxyListener) r7)
            return
        L_0x009d:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "no side fingerprint sensor"
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.SidefpsController.<init>(android.content.Context, android.view.LayoutInflater, android.hardware.fingerprint.FingerprintManager, android.view.WindowManager, android.app.ActivityTaskManager, com.android.systemui.recents.OverviewProxyService, android.hardware.display.DisplayManager, com.android.systemui.util.concurrency.DelayableExecutor, android.os.Handler):void");
    }

    @NotNull
    public final FingerprintSensorPropertiesInternal getSensorProps() {
        return this.sensorProps;
    }

    @NotNull
    public final BiometricDisplayListener getOrientationListener() {
        return this.orientationListener;
    }

    public final void setOverlayView(View view) {
        View view2 = this.overlayView;
        if (view2 != null) {
            this.windowManager.removeView(view2);
            getOrientationListener().disable();
        }
        ViewPropertyAnimator viewPropertyAnimator = this.overlayHideAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        this.overlayHideAnimator = null;
        this.overlayView = view;
        if (view != null) {
            this.windowManager.addView(view, this.overlayViewParams);
            updateOverlayVisibility(view);
            getOrientationListener().enable();
        }
    }

    public final void onOrientationChanged() {
        if (this.overlayView != null) {
            createOverlayForDisplay();
        }
    }

    public final void createOverlayForDisplay() {
        View inflate = this.layoutInflater.inflate(R$layout.sidefps_view, (ViewGroup) null, false);
        setOverlayView(inflate);
        Display display = this.context.getDisplay();
        Intrinsics.checkNotNull(display);
        SensorLocationInternal location = this.sensorProps.getLocation(display.getUniqueId());
        if (location == null) {
            Log.w("SidefpsController", Intrinsics.stringPlus("No location specified for display: ", display.getUniqueId()));
        }
        if (location == null) {
            location = getSensorProps().getLocation();
        }
        this.overlayOffsets = location;
        View findViewById = inflate.findViewById(R$id.sidefps_animation);
        if (findViewById != null) {
            LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById;
            inflate.setRotation(SidefpsControllerKt.asSideFpsAnimationRotation(display, SidefpsControllerKt.isYAligned(location)));
            lottieAnimationView.setAnimation(SidefpsControllerKt.asSideFpsAnimation(display, SidefpsControllerKt.isYAligned(location)));
            lottieAnimationView.addLottieOnCompositionLoadedListener(new SidefpsController$createOverlayForDisplay$1(this, inflate, display));
            SidefpsControllerKt.addOverlayDynamicColor(lottieAnimationView, this.context);
            inflate.setAccessibilityDelegate(new SidefpsController$createOverlayForDisplay$2());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.airbnb.lottie.LottieAnimationView");
    }

    @VisibleForTesting
    public final void updateOverlayParams$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull Display display, @NotNull Rect rect) {
        Rect rect2;
        boolean access$isNaturalOrientation = SidefpsControllerKt.isNaturalOrientation(display);
        Rect bounds = this.windowManager.getMaximumWindowMetrics().getBounds();
        int width = access$isNaturalOrientation ? bounds.width() : bounds.height();
        int height = access$isNaturalOrientation ? bounds.height() : bounds.width();
        int width2 = access$isNaturalOrientation ? rect.width() : rect.height();
        int height2 = access$isNaturalOrientation ? rect.height() : rect.width();
        if (SidefpsControllerKt.isYAligned(this.overlayOffsets)) {
            int i = this.overlayOffsets.sensorLocationY;
            rect2 = new Rect(width - width2, i, width, height2 + i);
        } else {
            int i2 = this.overlayOffsets.sensorLocationX;
            rect2 = new Rect(i2, 0, width2 + i2, height2);
        }
        RotationUtils.rotateBounds(rect2, new Rect(0, 0, width, height), display.getRotation());
        WindowManager.LayoutParams layoutParams = this.overlayViewParams;
        layoutParams.x = rect2.left;
        layoutParams.y = rect2.top;
        this.windowManager.updateViewLayout(this.overlayView, layoutParams);
    }

    public final void updateOverlayVisibility(View view) {
        if (Intrinsics.areEqual((Object) view, (Object) this.overlayView)) {
            Display display = this.context.getDisplay();
            Integer valueOf = display == null ? null : Integer.valueOf(display.getRotation());
            if (!SidefpsControllerKt.hasBigNavigationBar(this.windowManager.getCurrentWindowMetrics().getWindowInsets()) || ((valueOf == null || valueOf.intValue() != 3 || !SidefpsControllerKt.isYAligned(this.overlayOffsets)) && (valueOf == null || valueOf.intValue() != 2 || SidefpsControllerKt.isYAligned(this.overlayOffsets)))) {
                ViewPropertyAnimator viewPropertyAnimator = this.overlayHideAnimator;
                if (viewPropertyAnimator != null) {
                    viewPropertyAnimator.cancel();
                }
                this.overlayHideAnimator = null;
                view.setAlpha(1.0f);
                view.setVisibility(0);
                return;
            }
            this.overlayHideAnimator = view.animate().alpha(0.0f).setStartDelay(3000).setDuration(this.animationDuration).setListener(new SidefpsController$updateOverlayVisibility$1(view, this));
        }
    }
}
