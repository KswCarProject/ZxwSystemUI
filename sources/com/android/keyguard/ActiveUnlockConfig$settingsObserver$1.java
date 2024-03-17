package com.android.keyguard;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.SetsKt__SetsJVMKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ActiveUnlockConfig.kt */
public final class ActiveUnlockConfig$settingsObserver$1 extends ContentObserver {
    public final Uri bioFailUri;
    public final Uri faceAcquireInfoUri;
    public final Uri faceErrorsUri;
    public final /* synthetic */ ActiveUnlockConfig this$0;
    public final Uri unlockIntentUri;
    public final Uri unlockIntentWhenBiometricEnrolledUri;
    public final Uri wakeUri;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ActiveUnlockConfig$settingsObserver$1(ActiveUnlockConfig activeUnlockConfig, Handler handler) {
        super(handler);
        this.this$0 = activeUnlockConfig;
        this.wakeUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_wake");
        this.unlockIntentUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_unlock_intent");
        this.bioFailUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_biometric_fail");
        this.faceErrorsUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_face_errors");
        this.faceAcquireInfoUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_face_acquire_info");
        this.unlockIntentWhenBiometricEnrolledUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_unlock_intent_when_biometric_enrolled");
    }

    public final void register() {
        registerUri(CollectionsKt__CollectionsKt.listOf(this.wakeUri, this.unlockIntentUri, this.bioFailUri, this.faceErrorsUri, this.faceAcquireInfoUri, this.unlockIntentWhenBiometricEnrolledUri));
        onChange(true, new ArrayList(), 0, KeyguardUpdateMonitor.getCurrentUser());
    }

    public final void registerUri(Collection<? extends Uri> collection) {
        for (Uri registerContentObserver : collection) {
            this.this$0.contentResolver.registerContentObserver(registerContentObserver, false, this, -1);
        }
    }

    public void onChange(boolean z, @NotNull Collection<? extends Uri> collection, int i, int i2) {
        if (KeyguardUpdateMonitor.getCurrentUser() == i2) {
            boolean z2 = true;
            if (z || collection.contains(this.wakeUri)) {
                ActiveUnlockConfig activeUnlockConfig = this.this$0;
                activeUnlockConfig.requestActiveUnlockOnWakeup = activeUnlockConfig.secureSettings.getIntForUser("active_unlock_on_wake", 0, KeyguardUpdateMonitor.getCurrentUser()) == 1;
            }
            if (z || collection.contains(this.unlockIntentUri)) {
                ActiveUnlockConfig activeUnlockConfig2 = this.this$0;
                activeUnlockConfig2.requestActiveUnlockOnUnlockIntent = activeUnlockConfig2.secureSettings.getIntForUser("active_unlock_on_unlock_intent", 0, KeyguardUpdateMonitor.getCurrentUser()) == 1;
            }
            if (z || collection.contains(this.bioFailUri)) {
                ActiveUnlockConfig activeUnlockConfig3 = this.this$0;
                if (activeUnlockConfig3.secureSettings.getIntForUser("active_unlock_on_biometric_fail", 0, KeyguardUpdateMonitor.getCurrentUser()) != 1) {
                    z2 = false;
                }
                activeUnlockConfig3.requestActiveUnlockOnBioFail = z2;
            }
            if (z || collection.contains(this.faceErrorsUri)) {
                processStringArray(this.this$0.secureSettings.getStringForUser("active_unlock_on_face_errors", KeyguardUpdateMonitor.getCurrentUser()), this.this$0.faceErrorsToTriggerBiometricFailOn, SetsKt__SetsJVMKt.setOf(3));
            }
            if (z || collection.contains(this.faceAcquireInfoUri)) {
                processStringArray(this.this$0.secureSettings.getStringForUser("active_unlock_on_face_acquire_info", KeyguardUpdateMonitor.getCurrentUser()), this.this$0.faceAcquireInfoToTriggerBiometricFailOn, SetsKt__SetsKt.emptySet());
            }
            if (z || collection.contains(this.unlockIntentWhenBiometricEnrolledUri)) {
                processStringArray(this.this$0.secureSettings.getStringForUser("active_unlock_on_unlock_intent_when_biometric_enrolled", KeyguardUpdateMonitor.getCurrentUser()), this.this$0.onUnlockIntentWhenBiometricEnrolled, SetsKt__SetsJVMKt.setOf(0));
            }
        }
    }

    public final void processStringArray(String str, Set<Integer> set, Set<Integer> set2) {
        Unit unit;
        set.clear();
        if (str == null) {
            unit = null;
        } else {
            for (String str2 : StringsKt__StringsKt.split$default(str, new String[]{"|"}, false, 0, 6, (Object) null)) {
                try {
                    set.add(Integer.valueOf(Integer.parseInt(str2)));
                } catch (NumberFormatException unused) {
                    Log.e("ActiveUnlockConfig", Intrinsics.stringPlus("Passed an invalid setting=", str2));
                }
            }
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            set.addAll(set2);
        }
    }
}
