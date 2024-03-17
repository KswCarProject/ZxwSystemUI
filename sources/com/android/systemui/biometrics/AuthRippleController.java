package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.DisplayMetrics;
import android.view.Display;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$integer;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LightRevealEffect;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.leak.RotationUtils;
import java.io.PrintWriter;
import java.util.List;
import javax.inject.Provider;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringNumberConversionsJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController extends ViewController<AuthRippleView> implements KeyguardStateController.Callback, WakefulnessLifecycle.Observer {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final AuthController authController;
    @NotNull
    public final AuthRippleController$authControllerCallback$1 authControllerCallback = new AuthRippleController$authControllerCallback$1(this);
    @NotNull
    public final BiometricUnlockController biometricUnlockController;
    @NotNull
    public final KeyguardBypassController bypassController;
    @NotNull
    public final CentralSurfaces centralSurfaces;
    @Nullable
    public LightRevealEffect circleReveal;
    @NotNull
    public final CommandRegistry commandRegistry;
    @NotNull
    public final AuthRippleController$configurationChangedListener$1 configurationChangedListener = new AuthRippleController$configurationChangedListener$1(this);
    @NotNull
    public final ConfigurationController configurationController;
    @Nullable
    public PointF faceSensorLocation;
    @Nullable
    public PointF fingerprintSensorLocation;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    public final AuthRippleController$keyguardUpdateMonitorCallback$1 keyguardUpdateMonitorCallback = new AuthRippleController$keyguardUpdateMonitorCallback$1(this);
    @Nullable
    public ValueAnimator lightRevealScrimAnimator;
    @NotNull
    public final NotificationShadeWindowController notificationShadeWindowController;
    public boolean startLightRevealScrimOnKeyguardFadingAway;
    @NotNull
    public final StatusBarStateController statusBarStateController;
    @NotNull
    public final Context sysuiContext;
    @Nullable
    public UdfpsController udfpsController;
    @NotNull
    public final AuthRippleController$udfpsControllerCallback$1 udfpsControllerCallback = new AuthRippleController$udfpsControllerCallback$1(this);
    @NotNull
    public final Provider<UdfpsController> udfpsControllerProvider;
    public float udfpsRadius = -1.0f;
    @NotNull
    public final WakefulnessLifecycle wakefulnessLifecycle;

    public static /* synthetic */ void getStartLightRevealScrimOnKeyguardFadingAway$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public AuthRippleController(@NotNull CentralSurfaces centralSurfaces2, @NotNull Context context, @NotNull AuthController authController2, @NotNull ConfigurationController configurationController2, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor2, @NotNull KeyguardStateController keyguardStateController2, @NotNull WakefulnessLifecycle wakefulnessLifecycle2, @NotNull CommandRegistry commandRegistry2, @NotNull NotificationShadeWindowController notificationShadeWindowController2, @NotNull KeyguardBypassController keyguardBypassController, @NotNull BiometricUnlockController biometricUnlockController2, @NotNull Provider<UdfpsController> provider, @NotNull StatusBarStateController statusBarStateController2, @Nullable AuthRippleView authRippleView) {
        super(authRippleView);
        this.centralSurfaces = centralSurfaces2;
        this.sysuiContext = context;
        this.authController = authController2;
        this.configurationController = configurationController2;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.keyguardStateController = keyguardStateController2;
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        this.commandRegistry = commandRegistry2;
        this.notificationShadeWindowController = notificationShadeWindowController2;
        this.bypassController = keyguardBypassController;
        this.biometricUnlockController = biometricUnlockController2;
        this.udfpsControllerProvider = provider;
        this.statusBarStateController = statusBarStateController2;
    }

    public final void setStartLightRevealScrimOnKeyguardFadingAway$frameworks__base__packages__SystemUI__android_common__SystemUI_core(boolean z) {
        this.startLightRevealScrimOnKeyguardFadingAway = z;
    }

    public final void setLightRevealScrimAnimator(@Nullable ValueAnimator valueAnimator) {
        this.lightRevealScrimAnimator = valueAnimator;
    }

    @Nullable
    public final PointF getFingerprintSensorLocation() {
        return this.fingerprintSensorLocation;
    }

    public final void setFingerprintSensorLocation(@Nullable PointF pointF) {
        this.fingerprintSensorLocation = pointF;
    }

    public void onInit() {
        ((AuthRippleView) this.mView).setAlphaInDuration((long) this.sysuiContext.getResources().getInteger(R$integer.auth_ripple_alpha_in_duration));
    }

    public void onViewAttached() {
        this.authController.addCallback(this.authControllerCallback);
        updateRippleColor();
        updateSensorLocation();
        updateUdfpsDependentParams();
        UdfpsController udfpsController2 = this.udfpsController;
        if (udfpsController2 != null) {
            udfpsController2.addCallback(this.udfpsControllerCallback);
        }
        this.configurationController.addCallback(this.configurationChangedListener);
        this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateMonitorCallback);
        this.keyguardStateController.addCallback(this);
        this.wakefulnessLifecycle.addObserver(this);
        this.commandRegistry.registerCommand("auth-ripple", new AuthRippleController$onViewAttached$1(this));
    }

    public void onViewDetached() {
        UdfpsController udfpsController2 = this.udfpsController;
        if (udfpsController2 != null) {
            udfpsController2.removeCallback(this.udfpsControllerCallback);
        }
        this.authController.removeCallback(this.authControllerCallback);
        this.keyguardUpdateMonitor.removeCallback(this.keyguardUpdateMonitorCallback);
        this.configurationController.removeCallback(this.configurationChangedListener);
        this.keyguardStateController.removeCallback(this);
        this.wakefulnessLifecycle.removeObserver(this);
        this.commandRegistry.unregisterCommand("auth-ripple");
        this.notificationShadeWindowController.setForcePluginOpen(false, this);
    }

    public final void showUnlockRipple(@Nullable BiometricSourceType biometricSourceType) {
        PointF pointF;
        if ((this.keyguardUpdateMonitor.isKeyguardVisible() || this.keyguardUpdateMonitor.isDreaming()) && !this.keyguardUpdateMonitor.userNeedsStrongAuth()) {
            updateSensorLocation();
            if (biometricSourceType == BiometricSourceType.FINGERPRINT && (pointF = this.fingerprintSensorLocation) != null) {
                Intrinsics.checkNotNull(pointF);
                ((AuthRippleView) this.mView).setFingerprintSensorLocation(pointF, this.udfpsRadius);
                showUnlockedRipple();
            } else if (biometricSourceType == BiometricSourceType.FACE && this.faceSensorLocation != null && this.bypassController.canBypass()) {
                PointF pointF2 = this.faceSensorLocation;
                Intrinsics.checkNotNull(pointF2);
                ((AuthRippleView) this.mView).setSensorLocation(pointF2);
                showUnlockedRipple();
            }
        }
    }

    public final void showUnlockedRipple() {
        LightRevealEffect lightRevealEffect;
        this.notificationShadeWindowController.setForcePluginOpen(true, this);
        LightRevealScrim lightRevealScrim = this.centralSurfaces.getLightRevealScrim();
        if ((this.statusBarStateController.isDozing() || this.biometricUnlockController.isWakeAndUnlock()) && (lightRevealEffect = this.circleReveal) != null) {
            if (lightRevealScrim != null) {
                lightRevealScrim.setRevealAmount(0.0f);
            }
            if (lightRevealScrim != null) {
                lightRevealScrim.setRevealEffect(lightRevealEffect);
            }
            setStartLightRevealScrimOnKeyguardFadingAway$frameworks__base__packages__SystemUI__android_common__SystemUI_core(true);
        }
        ((AuthRippleView) this.mView).startUnlockedRipple(new AuthRippleController$showUnlockedRipple$2(this));
    }

    public void onKeyguardFadingAwayChanged() {
        if (this.keyguardStateController.isKeyguardFadingAway()) {
            LightRevealScrim lightRevealScrim = this.centralSurfaces.getLightRevealScrim();
            if (this.startLightRevealScrimOnKeyguardFadingAway && lightRevealScrim != null) {
                ValueAnimator valueAnimator = this.lightRevealScrimAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.1f, 1.0f});
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat.setDuration(1533);
                ofFloat.setStartDelay(this.keyguardStateController.getKeyguardFadingAwayDelay());
                ofFloat.addUpdateListener(new AuthRippleController$onKeyguardFadingAwayChanged$1$1(lightRevealScrim, this, ofFloat));
                ofFloat.addListener(new AuthRippleController$onKeyguardFadingAwayChanged$1$2(lightRevealScrim, this));
                ofFloat.start();
                this.lightRevealScrimAnimator = ofFloat;
                this.startLightRevealScrimOnKeyguardFadingAway = false;
            }
        }
    }

    public final boolean isAnimatingLightRevealScrim() {
        ValueAnimator valueAnimator = this.lightRevealScrimAnimator;
        if (valueAnimator == null) {
            return false;
        }
        return valueAnimator.isRunning();
    }

    public void onStartedGoingToSleep() {
        this.startLightRevealScrimOnKeyguardFadingAway = false;
    }

    public final void updateSensorLocation() {
        updateFingerprintLocation();
        this.faceSensorLocation = this.authController.getFaceAuthSensorLocation();
        PointF pointF = this.fingerprintSensorLocation;
        if (pointF != null) {
            float f = pointF.x;
            this.circleReveal = new CircleReveal(f, pointF.y, 0.0f, Math.max(Math.max(f, this.centralSurfaces.getDisplayWidth() - pointF.x), Math.max(pointF.y, this.centralSurfaces.getDisplayHeight() - pointF.y)));
        }
    }

    public final void updateFingerprintLocation() {
        PointF pointF;
        PointF pointF2;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = this.sysuiContext.getDisplay();
        if (display != null) {
            display.getRealMetrics(displayMetrics);
        }
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        PointF fingerprintSensorLocation2 = this.authController.getFingerprintSensorLocation();
        if (fingerprintSensorLocation2 != null) {
            int rotation = RotationUtils.getRotation(this.sysuiContext);
            if (rotation != 1) {
                if (rotation == 2) {
                    pointF = new PointF(((float) i) - fingerprintSensorLocation2.x, ((float) i2) - fingerprintSensorLocation2.y);
                } else if (rotation != 3) {
                    pointF = new PointF(fingerprintSensorLocation2.x, fingerprintSensorLocation2.y);
                } else {
                    float f = (float) i;
                    float f2 = (float) i2;
                    pointF2 = new PointF(f * (((float) 1) - (fingerprintSensorLocation2.y / f)), f2 * (fingerprintSensorLocation2.x / f2));
                }
                setFingerprintSensorLocation(pointF);
            }
            float f3 = (float) i;
            float f4 = (float) i2;
            pointF2 = new PointF(f3 * (fingerprintSensorLocation2.y / f3), f4 * (((float) 1) - (fingerprintSensorLocation2.x / f4)));
            pointF = pointF2;
            setFingerprintSensorLocation(pointF);
        }
    }

    public final void updateRippleColor() {
        ((AuthRippleView) this.mView).setLockScreenColor(Utils.getColorAttrDefaultColor(this.sysuiContext, R$attr.wallpaperTextColorAccent));
    }

    public final void showDwellRipple() {
        ((AuthRippleView) this.mView).startDwellRipple(this.statusBarStateController.isDozing());
    }

    public final void updateUdfpsDependentParams() {
        UdfpsController udfpsController2;
        List<FingerprintSensorPropertiesInternal> udfpsProps = this.authController.getUdfpsProps();
        if (udfpsProps != null && udfpsProps.size() > 0) {
            this.udfpsController = this.udfpsControllerProvider.get();
            this.udfpsRadius = this.authController.getUdfpsRadius();
            if (((AuthRippleView) this.mView).isAttachedToWindow() && (udfpsController2 = this.udfpsController) != null) {
                udfpsController2.addCallback(this.udfpsControllerCallback);
            }
        }
    }

    /* compiled from: AuthRippleController.kt */
    public final class AuthRippleCommand implements Command {
        public AuthRippleCommand() {
        }

        public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
            if (list.isEmpty()) {
                invalidCommand(printWriter);
                return;
            }
            String str = list.get(0);
            switch (str.hashCode()) {
                case -1375934236:
                    if (str.equals("fingerprint")) {
                        AuthRippleController.this.updateSensorLocation();
                        printWriter.println(Intrinsics.stringPlus("fingerprint ripple sensorLocation=", AuthRippleController.this.getFingerprintSensorLocation()));
                        AuthRippleController.this.showUnlockRipple(BiometricSourceType.FINGERPRINT);
                        return;
                    }
                    break;
                case -1349088399:
                    if (str.equals("custom")) {
                        if (list.size() != 3 || StringsKt__StringNumberConversionsJVMKt.toFloatOrNull(list.get(1)) == null || StringsKt__StringNumberConversionsJVMKt.toFloatOrNull(list.get(2)) == null) {
                            invalidCommand(printWriter);
                            return;
                        }
                        printWriter.println("custom ripple sensorLocation=" + Float.parseFloat(list.get(1)) + ", " + Float.parseFloat(list.get(2)));
                        ((AuthRippleView) AuthRippleController.this.mView).setSensorLocation(new PointF(Float.parseFloat(list.get(1)), Float.parseFloat(list.get(2))));
                        AuthRippleController.this.showUnlockedRipple();
                        return;
                    }
                    break;
                case 3135069:
                    if (str.equals("face")) {
                        AuthRippleController.this.updateSensorLocation();
                        printWriter.println(Intrinsics.stringPlus("face ripple sensorLocation=", AuthRippleController.this.faceSensorLocation));
                        AuthRippleController.this.showUnlockRipple(BiometricSourceType.FACE);
                        return;
                    }
                    break;
                case 95997746:
                    if (str.equals("dwell")) {
                        AuthRippleController.this.showDwellRipple();
                        printWriter.println("lock screen dwell ripple: \n\tsensorLocation=" + AuthRippleController.this.getFingerprintSensorLocation() + "\n\tudfpsRadius=" + AuthRippleController.this.udfpsRadius);
                        return;
                    }
                    break;
            }
            invalidCommand(printWriter);
        }

        public void help(@NotNull PrintWriter printWriter) {
            printWriter.println("Usage: adb shell cmd statusbar auth-ripple <command>");
            printWriter.println("Available commands:");
            printWriter.println("  dwell");
            printWriter.println("  fingerprint");
            printWriter.println("  face");
            printWriter.println("  custom <x-location: int> <y-location: int>");
        }

        public final void invalidCommand(@NotNull PrintWriter printWriter) {
            printWriter.println("invalid command");
            help(printWriter);
        }
    }

    /* compiled from: AuthRippleController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
