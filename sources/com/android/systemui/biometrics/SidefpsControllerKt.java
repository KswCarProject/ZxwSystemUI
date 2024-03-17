package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.hardware.biometrics.SensorLocationInternal;
import android.view.Display;
import android.view.WindowInsets;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.android.systemui.R$color;
import com.android.systemui.R$raw;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SidefpsController.kt */
public final class SidefpsControllerKt {
    public static final boolean isReasonToShow(int i, ActivityTaskManager activityTaskManager) {
        if (i == 4 || (i == 6 && Intrinsics.areEqual((Object) topClass(activityTaskManager), (Object) "com.android.settings.biometrics.fingerprint.FingerprintSettings"))) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0015, code lost:
        r1 = (r1 = r1.topActivity).getClassName();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.String topClass(android.app.ActivityTaskManager r1) {
        /*
            r0 = 1
            java.util.List r1 = r1.getTasks(r0)
            java.lang.Object r1 = kotlin.collections.CollectionsKt___CollectionsKt.firstOrNull(r1)
            android.app.ActivityManager$RunningTaskInfo r1 = (android.app.ActivityManager.RunningTaskInfo) r1
            java.lang.String r0 = ""
            if (r1 != 0) goto L_0x0010
            goto L_0x001d
        L_0x0010:
            android.content.ComponentName r1 = r1.topActivity
            if (r1 != 0) goto L_0x0015
            goto L_0x001d
        L_0x0015:
            java.lang.String r1 = r1.getClassName()
            if (r1 != 0) goto L_0x001c
            goto L_0x001d
        L_0x001c:
            r0 = r1
        L_0x001d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.SidefpsControllerKt.topClass(android.app.ActivityTaskManager):java.lang.String");
    }

    public static final int asSideFpsAnimation(Display display, boolean z) {
        int rotation = display.getRotation();
        return rotation != 0 ? rotation != 2 ? z ? R$raw.sfps_pulse_landscape : R$raw.sfps_pulse : z ? R$raw.sfps_pulse : R$raw.sfps_pulse_landscape : z ? R$raw.sfps_pulse : R$raw.sfps_pulse_landscape;
    }

    public static final float asSideFpsAnimationRotation(Display display, boolean z) {
        int rotation = display.getRotation();
        if (rotation != 1) {
            if (rotation != 2 && (rotation != 3 || !z)) {
                return 0.0f;
            }
        } else if (z) {
            return 0.0f;
        }
        return 180.0f;
    }

    public static final boolean isYAligned(SensorLocationInternal sensorLocationInternal) {
        return sensorLocationInternal.sensorLocationY != 0;
    }

    public static final boolean isNaturalOrientation(Display display) {
        return display.getRotation() == 0 || display.getRotation() == 2;
    }

    public static final boolean hasBigNavigationBar(WindowInsets windowInsets) {
        return windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom >= 70;
    }

    public static final void addOverlayDynamicColor$update(Context context, LottieAnimationView lottieAnimationView) {
        int color = context.getColor(R$color.biometric_dialog_accent);
        for (String str : CollectionsKt__CollectionsKt.listOf(".blue600", ".blue400")) {
            lottieAnimationView.addValueCallback(new KeyPath(str, "**"), LottieProperty.COLOR_FILTER, new SidefpsControllerKt$addOverlayDynamicColor$update$1(color));
        }
    }

    public static final void addOverlayDynamicColor(LottieAnimationView lottieAnimationView, Context context) {
        if (lottieAnimationView.getComposition() != null) {
            addOverlayDynamicColor$update(context, lottieAnimationView);
        } else {
            lottieAnimationView.addLottieOnCompositionLoadedListener(new SidefpsControllerKt$addOverlayDynamicColor$1(context, lottieAnimationView));
        }
    }
}
