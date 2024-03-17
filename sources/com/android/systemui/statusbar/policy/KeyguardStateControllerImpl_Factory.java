package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardStateControllerImpl_Factory implements Factory<KeyguardStateControllerImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;

    public KeyguardStateControllerImpl_Factory(Provider<Context> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<LockPatternUtils> provider3, Provider<KeyguardUnlockAnimationController> provider4, Provider<DumpManager> provider5) {
        this.contextProvider = provider;
        this.keyguardUpdateMonitorProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.keyguardUnlockAnimationControllerProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    public KeyguardStateControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.lockPatternUtilsProvider.get(), DoubleCheck.lazy(this.keyguardUnlockAnimationControllerProvider), this.dumpManagerProvider.get());
    }

    public static KeyguardStateControllerImpl_Factory create(Provider<Context> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<LockPatternUtils> provider3, Provider<KeyguardUnlockAnimationController> provider4, Provider<DumpManager> provider5) {
        return new KeyguardStateControllerImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static KeyguardStateControllerImpl newInstance(Context context, KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, Lazy<KeyguardUnlockAnimationController> lazy, DumpManager dumpManager) {
        return new KeyguardStateControllerImpl(context, keyguardUpdateMonitor, lockPatternUtils, lazy, dumpManager);
    }
}
