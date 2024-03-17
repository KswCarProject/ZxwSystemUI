package com.android.keyguard;

import android.content.res.Resources;
import android.telephony.SubscriptionManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.DejankUtils;

public class KeyguardSecurityModel {
    public final boolean mIsPukScreenAvailable;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LockPatternUtils mLockPatternUtils;

    public enum SecurityMode {
        Invalid,
        None,
        Pattern,
        Password,
        PIN,
        SimPin,
        SimPuk
    }

    public KeyguardSecurityModel(Resources resources, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mIsPukScreenAvailable = resources.getBoolean(17891657);
        this.mLockPatternUtils = lockPatternUtils;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    public SecurityMode getSecurityMode(int i) {
        if (this.mIsPukScreenAvailable && SubscriptionManager.isValidSubscriptionId(this.mKeyguardUpdateMonitor.getNextSubIdForState(3))) {
            return SecurityMode.SimPuk;
        }
        if (SubscriptionManager.isValidSubscriptionId(this.mKeyguardUpdateMonitor.getUnlockedSubIdForState(2))) {
            return SecurityMode.SimPin;
        }
        int intValue = ((Integer) DejankUtils.whitelistIpcs(new KeyguardSecurityModel$$ExternalSyntheticLambda0(this, i))).intValue();
        if (intValue == 0) {
            return SecurityMode.None;
        }
        if (intValue == 65536) {
            return SecurityMode.Pattern;
        }
        if (intValue == 131072 || intValue == 196608) {
            return SecurityMode.PIN;
        }
        if (intValue == 262144 || intValue == 327680 || intValue == 393216 || intValue == 524288) {
            return SecurityMode.Password;
        }
        throw new IllegalStateException("Unknown security quality:" + intValue);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getSecurityMode$0(int i) {
        return Integer.valueOf(this.mLockPatternUtils.getActivePasswordQuality(i));
    }
}
