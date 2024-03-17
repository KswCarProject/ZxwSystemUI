package com.android.keyguard;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: KeyguardListenModel.kt */
public final class KeyguardActiveUnlockModel extends KeyguardListenModel {
    public final boolean authInterruptActive;
    public final boolean awakeKeyguard;
    public final boolean encryptedOrTimedOut;
    public final boolean fpLockout;
    public final boolean listening;
    public final boolean lockDown;
    public final boolean switchingUser;
    public final long timeMillis;
    public final boolean triggerActiveUnlockForAssistant;
    public final boolean userCanDismissLockScreen;
    public final int userId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyguardActiveUnlockModel)) {
            return false;
        }
        KeyguardActiveUnlockModel keyguardActiveUnlockModel = (KeyguardActiveUnlockModel) obj;
        return getTimeMillis() == keyguardActiveUnlockModel.getTimeMillis() && getUserId() == keyguardActiveUnlockModel.getUserId() && getListening() == keyguardActiveUnlockModel.getListening() && this.awakeKeyguard == keyguardActiveUnlockModel.awakeKeyguard && this.authInterruptActive == keyguardActiveUnlockModel.authInterruptActive && this.encryptedOrTimedOut == keyguardActiveUnlockModel.encryptedOrTimedOut && this.fpLockout == keyguardActiveUnlockModel.fpLockout && this.lockDown == keyguardActiveUnlockModel.lockDown && this.switchingUser == keyguardActiveUnlockModel.switchingUser && this.triggerActiveUnlockForAssistant == keyguardActiveUnlockModel.triggerActiveUnlockForAssistant && this.userCanDismissLockScreen == keyguardActiveUnlockModel.userCanDismissLockScreen;
    }

    public int hashCode() {
        int hashCode = ((Long.hashCode(getTimeMillis()) * 31) + Integer.hashCode(getUserId())) * 31;
        boolean listening2 = getListening();
        boolean z = true;
        if (listening2) {
            listening2 = true;
        }
        int i = (hashCode + (listening2 ? 1 : 0)) * 31;
        boolean z2 = this.awakeKeyguard;
        if (z2) {
            z2 = true;
        }
        int i2 = (i + (z2 ? 1 : 0)) * 31;
        boolean z3 = this.authInterruptActive;
        if (z3) {
            z3 = true;
        }
        int i3 = (i2 + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.encryptedOrTimedOut;
        if (z4) {
            z4 = true;
        }
        int i4 = (i3 + (z4 ? 1 : 0)) * 31;
        boolean z5 = this.fpLockout;
        if (z5) {
            z5 = true;
        }
        int i5 = (i4 + (z5 ? 1 : 0)) * 31;
        boolean z6 = this.lockDown;
        if (z6) {
            z6 = true;
        }
        int i6 = (i5 + (z6 ? 1 : 0)) * 31;
        boolean z7 = this.switchingUser;
        if (z7) {
            z7 = true;
        }
        int i7 = (i6 + (z7 ? 1 : 0)) * 31;
        boolean z8 = this.triggerActiveUnlockForAssistant;
        if (z8) {
            z8 = true;
        }
        int i8 = (i7 + (z8 ? 1 : 0)) * 31;
        boolean z9 = this.userCanDismissLockScreen;
        if (!z9) {
            z = z9;
        }
        return i8 + (z ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "KeyguardActiveUnlockModel(timeMillis=" + getTimeMillis() + ", userId=" + getUserId() + ", listening=" + getListening() + ", awakeKeyguard=" + this.awakeKeyguard + ", authInterruptActive=" + this.authInterruptActive + ", encryptedOrTimedOut=" + this.encryptedOrTimedOut + ", fpLockout=" + this.fpLockout + ", lockDown=" + this.lockDown + ", switchingUser=" + this.switchingUser + ", triggerActiveUnlockForAssistant=" + this.triggerActiveUnlockForAssistant + ", userCanDismissLockScreen=" + this.userCanDismissLockScreen + ')';
    }

    public long getTimeMillis() {
        return this.timeMillis;
    }

    public int getUserId() {
        return this.userId;
    }

    public boolean getListening() {
        return this.listening;
    }

    public KeyguardActiveUnlockModel(long j, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9) {
        super((DefaultConstructorMarker) null);
        this.timeMillis = j;
        this.userId = i;
        this.listening = z;
        this.awakeKeyguard = z2;
        this.authInterruptActive = z3;
        this.encryptedOrTimedOut = z4;
        this.fpLockout = z5;
        this.lockDown = z6;
        this.switchingUser = z7;
        this.triggerActiveUnlockForAssistant = z8;
        this.userCanDismissLockScreen = z9;
    }
}
