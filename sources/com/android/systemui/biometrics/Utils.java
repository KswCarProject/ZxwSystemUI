package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.hardware.biometrics.SensorPropertiesInternal;
import android.os.UserManager;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Utils.kt */
public final class Utils {
    @NotNull
    public static final Utils INSTANCE = new Utils();

    public static final float dpToPixels(@NotNull Context context, float f) {
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / ((float) 160));
    }

    public static final void notifyAccessibilityContentChanged(@NotNull AccessibilityManager accessibilityManager, @NotNull ViewGroup viewGroup) {
        if (accessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            obtain.setContentChangeTypes(1);
            viewGroup.sendAccessibilityEventUnchecked(obtain);
            viewGroup.notifySubtreeAccessibilityStateChanged(viewGroup, viewGroup, 1);
        }
    }

    public static final boolean isDeviceCredentialAllowed(@NotNull PromptInfo promptInfo) {
        return (promptInfo.getAuthenticators() & 32768) != 0;
    }

    public static final boolean isBiometricAllowed(@NotNull PromptInfo promptInfo) {
        return (promptInfo.getAuthenticators() & 255) != 0;
    }

    public static final int getCredentialType(@NotNull LockPatternUtils lockPatternUtils, int i) {
        int keyguardStoredPasswordQuality = lockPatternUtils.getKeyguardStoredPasswordQuality(i);
        if (keyguardStoredPasswordQuality == 65536) {
            return 2;
        }
        if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) {
            return 1;
        }
        return (keyguardStoredPasswordQuality == 262144 || keyguardStoredPasswordQuality == 327680 || keyguardStoredPasswordQuality == 393216) ? 3 : 3;
    }

    public static final boolean isManagedProfile(@NotNull Context context, int i) {
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        if (userManager == null) {
            return false;
        }
        return userManager.isManagedProfile(i);
    }

    @Nullable
    public static final <T extends SensorPropertiesInternal> T findFirstSensorProperties(@Nullable List<? extends T> list, @NotNull int[] iArr) {
        T t = null;
        if (list == null) {
            return null;
        }
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            T next = it.next();
            if (ArraysKt___ArraysKt.contains(iArr, ((SensorPropertiesInternal) next).sensorId)) {
                t = next;
                break;
            }
        }
        return (SensorPropertiesInternal) t;
    }

    public static final boolean isSystem(@NotNull Context context, @Nullable String str) {
        if (!(context.checkCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL") == 0) || !Intrinsics.areEqual((Object) ThemeOverlayApplier.ANDROID_PACKAGE, (Object) str)) {
            return false;
        }
        return true;
    }
}
