package com.android.keyguard;

import android.content.Context;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardUnfoldTransition_Factory implements Factory<KeyguardUnfoldTransition> {
    public final Provider<Context> contextProvider;
    public final Provider<NaturalRotationUnfoldProgressProvider> unfoldProgressProvider;

    public KeyguardUnfoldTransition_Factory(Provider<Context> provider, Provider<NaturalRotationUnfoldProgressProvider> provider2) {
        this.contextProvider = provider;
        this.unfoldProgressProvider = provider2;
    }

    public KeyguardUnfoldTransition get() {
        return newInstance(this.contextProvider.get(), this.unfoldProgressProvider.get());
    }

    public static KeyguardUnfoldTransition_Factory create(Provider<Context> provider, Provider<NaturalRotationUnfoldProgressProvider> provider2) {
        return new KeyguardUnfoldTransition_Factory(provider, provider2);
    }

    public static KeyguardUnfoldTransition newInstance(Context context, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        return new KeyguardUnfoldTransition(context, naturalRotationUnfoldProgressProvider);
    }
}
