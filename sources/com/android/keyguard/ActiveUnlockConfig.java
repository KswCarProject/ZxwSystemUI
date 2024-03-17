package com.android.keyguard;

import android.content.ContentResolver;
import android.os.Handler;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ActiveUnlockConfig.kt */
public final class ActiveUnlockConfig implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final ContentResolver contentResolver;
    @NotNull
    public Set<Integer> faceAcquireInfoToTriggerBiometricFailOn = new LinkedHashSet();
    @NotNull
    public Set<Integer> faceErrorsToTriggerBiometricFailOn = SetsKt__SetsKt.mutableSetOf(3);
    @NotNull
    public final Handler handler;
    @Nullable
    public KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    public Set<Integer> onUnlockIntentWhenBiometricEnrolled = SetsKt__SetsKt.mutableSetOf(0);
    public boolean requestActiveUnlockOnBioFail;
    public boolean requestActiveUnlockOnUnlockIntent;
    public boolean requestActiveUnlockOnWakeup;
    @NotNull
    public final SecureSettings secureSettings;
    @NotNull
    public final ActiveUnlockConfig$settingsObserver$1 settingsObserver;

    /* compiled from: ActiveUnlockConfig.kt */
    public enum ACTIVE_UNLOCK_REQUEST_ORIGIN {
        WAKE,
        UNLOCK_INTENT,
        BIOMETRIC_FAIL,
        ASSISTANT
    }

    /* compiled from: ActiveUnlockConfig.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[ACTIVE_UNLOCK_REQUEST_ORIGIN.values().length];
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.WAKE.ordinal()] = 1;
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.UNLOCK_INTENT.ordinal()] = 2;
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.BIOMETRIC_FAIL.ordinal()] = 3;
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.ASSISTANT.ordinal()] = 4;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public ActiveUnlockConfig(@NotNull Handler handler2, @NotNull SecureSettings secureSettings2, @NotNull ContentResolver contentResolver2, @NotNull DumpManager dumpManager) {
        this.handler = handler2;
        this.secureSettings = secureSettings2;
        this.contentResolver = contentResolver2;
        ActiveUnlockConfig$settingsObserver$1 activeUnlockConfig$settingsObserver$1 = new ActiveUnlockConfig$settingsObserver$1(this, handler2);
        this.settingsObserver = activeUnlockConfig$settingsObserver$1;
        activeUnlockConfig$settingsObserver$1.register();
        dumpManager.registerDumpable(this);
    }

    /* compiled from: ActiveUnlockConfig.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void setKeyguardUpdateMonitor(@Nullable KeyguardUpdateMonitor keyguardUpdateMonitor2) {
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
    }

    public final boolean isActiveUnlockEnabled() {
        return this.requestActiveUnlockOnWakeup || this.requestActiveUnlockOnUnlockIntent || this.requestActiveUnlockOnBioFail;
    }

    public final boolean shouldRequestActiveUnlockOnFaceError(int i) {
        return this.faceErrorsToTriggerBiometricFailOn.contains(Integer.valueOf(i));
    }

    public final boolean shouldRequestActiveUnlockOnFaceAcquireInfo(int i) {
        return this.faceAcquireInfoToTriggerBiometricFailOn.contains(Integer.valueOf(i));
    }

    public final boolean shouldAllowActiveUnlockFromOrigin(@NotNull ACTIVE_UNLOCK_REQUEST_ORIGIN active_unlock_request_origin) {
        int i = WhenMappings.$EnumSwitchMapping$0[active_unlock_request_origin.ordinal()];
        if (i == 1) {
            return this.requestActiveUnlockOnWakeup;
        }
        if (i != 2) {
            if (i != 3) {
                if (i == 4) {
                    return isActiveUnlockEnabled();
                }
                throw new NoWhenBranchMatchedException();
            } else if (!this.requestActiveUnlockOnBioFail && !this.requestActiveUnlockOnUnlockIntent && !this.requestActiveUnlockOnWakeup) {
                return false;
            }
        } else if (!this.requestActiveUnlockOnUnlockIntent && !this.requestActiveUnlockOnWakeup && !shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment()) {
            return false;
        }
        return true;
    }

    public final boolean shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment() {
        KeyguardUpdateMonitor keyguardUpdateMonitor2;
        if (this.requestActiveUnlockOnBioFail && (keyguardUpdateMonitor2 = this.keyguardUpdateMonitor) != null) {
            boolean isFaceEnrolled = keyguardUpdateMonitor2.isFaceEnrolled();
            boolean cachedIsUnlockWithFingerprintPossible = keyguardUpdateMonitor2.getCachedIsUnlockWithFingerprintPossible(KeyguardUpdateMonitor.getCurrentUser());
            boolean isUdfpsEnrolled = keyguardUpdateMonitor2.isUdfpsEnrolled();
            if (!isFaceEnrolled && !cachedIsUnlockWithFingerprintPossible) {
                return this.onUnlockIntentWhenBiometricEnrolled.contains(0);
            }
            if (isFaceEnrolled || !cachedIsUnlockWithFingerprintPossible) {
                if (!cachedIsUnlockWithFingerprintPossible && isFaceEnrolled) {
                    return this.onUnlockIntentWhenBiometricEnrolled.contains(1);
                }
            } else if (this.onUnlockIntentWhenBiometricEnrolled.contains(2) || (isUdfpsEnrolled && this.onUnlockIntentWhenBiometricEnrolled.contains(3))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        Unit unit;
        printWriter.println("Settings:");
        printWriter.println(Intrinsics.stringPlus("   requestActiveUnlockOnWakeup=", Boolean.valueOf(this.requestActiveUnlockOnWakeup)));
        printWriter.println(Intrinsics.stringPlus("   requestActiveUnlockOnUnlockIntent=", Boolean.valueOf(this.requestActiveUnlockOnUnlockIntent)));
        printWriter.println(Intrinsics.stringPlus("   requestActiveUnlockOnBioFail=", Boolean.valueOf(this.requestActiveUnlockOnBioFail)));
        printWriter.println(Intrinsics.stringPlus("   requestActiveUnlockOnUnlockIntentWhenBiometricEnrolled=", this.onUnlockIntentWhenBiometricEnrolled));
        printWriter.println(Intrinsics.stringPlus("   requestActiveUnlockOnFaceError=", this.faceErrorsToTriggerBiometricFailOn));
        printWriter.println(Intrinsics.stringPlus("   requestActiveUnlockOnFaceAcquireInfo=", this.faceAcquireInfoToTriggerBiometricFailOn));
        printWriter.println("Current state:");
        KeyguardUpdateMonitor keyguardUpdateMonitor2 = this.keyguardUpdateMonitor;
        if (keyguardUpdateMonitor2 == null) {
            unit = null;
        } else {
            printWriter.println(Intrinsics.stringPlus("   shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment=", Boolean.valueOf(shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment())));
            printWriter.println(Intrinsics.stringPlus("   faceEnrolled=", Boolean.valueOf(keyguardUpdateMonitor2.isFaceEnrolled())));
            printWriter.println(Intrinsics.stringPlus("   fpEnrolled=", Boolean.valueOf(keyguardUpdateMonitor2.getCachedIsUnlockWithFingerprintPossible(KeyguardUpdateMonitor.getCurrentUser()))));
            printWriter.println(Intrinsics.stringPlus("   udfpsEnrolled=", Boolean.valueOf(keyguardUpdateMonitor2.isUdfpsEnrolled())));
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            printWriter.println("   keyguardUpdateMonitor is uninitialized");
        }
    }
}
