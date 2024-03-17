package com.android.keyguard;

import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.CoreStartable;
import com.android.systemui.log.SessionTracker;
import java.io.PrintWriter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: KeyguardBiometricLockoutLogger.kt */
public final class KeyguardBiometricLockoutLogger extends CoreStartable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public boolean encryptedOrLockdown;
    public boolean faceLockedOut;
    public boolean fingerprintLockedOut;
    @NotNull
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1(this);
    @NotNull
    public final SessionTracker sessionTracker;
    public boolean timeout;
    @NotNull
    public final UiEventLogger uiEventLogger;
    public boolean unattendedUpdate;

    public KeyguardBiometricLockoutLogger(@Nullable Context context, @NotNull UiEventLogger uiEventLogger2, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor2, @NotNull SessionTracker sessionTracker2) {
        super(context);
        this.uiEventLogger = uiEventLogger2;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.sessionTracker = sessionTracker2;
    }

    public void start() {
        this.mKeyguardUpdateMonitorCallback.onStrongAuthStateChanged(KeyguardUpdateMonitor.getCurrentUser());
        this.keyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    public final boolean isUnattendedUpdate(int i) {
        return Companion.containsFlag(i, 64);
    }

    public final boolean isStrongAuthTimeout(int i) {
        Companion companion = Companion;
        return companion.containsFlag(i, 16) || companion.containsFlag(i, 128);
    }

    public final void log(PrimaryAuthRequiredEvent primaryAuthRequiredEvent) {
        this.uiEventLogger.log(primaryAuthRequiredEvent, this.sessionTracker.getSessionId(1));
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("  mFingerprintLockedOut=", Boolean.valueOf(this.fingerprintLockedOut)));
        printWriter.println(Intrinsics.stringPlus("  mFaceLockedOut=", Boolean.valueOf(this.faceLockedOut)));
        printWriter.println(Intrinsics.stringPlus("  mIsEncryptedOrLockdown=", Boolean.valueOf(this.encryptedOrLockdown)));
        printWriter.println(Intrinsics.stringPlus("  mIsUnattendedUpdate=", Boolean.valueOf(this.unattendedUpdate)));
        printWriter.println(Intrinsics.stringPlus("  mIsTimeout=", Boolean.valueOf(this.timeout)));
    }

    @VisibleForTesting
    /* compiled from: KeyguardBiometricLockoutLogger.kt */
    public enum PrimaryAuthRequiredEvent implements UiEventLogger.UiEventEnum {
        PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT(924),
        PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT_RESET(925),
        PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT(926),
        PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT_RESET(927),
        PRIMARY_AUTH_REQUIRED_ENCRYPTED_OR_LOCKDOWN(928),
        PRIMARY_AUTH_REQUIRED_TIMEOUT(929),
        PRIMARY_AUTH_REQUIRED_UNATTENDED_UPDATE(931);
        
        private final int mId;

        /* access modifiers changed from: public */
        PrimaryAuthRequiredEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* compiled from: KeyguardBiometricLockoutLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean containsFlag(int i, int i2) {
            return (i & i2) != 0;
        }

        public Companion() {
        }
    }
}
