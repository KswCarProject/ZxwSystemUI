package com.android.systemui.unfold;

import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import java.util.function.Function;

/* compiled from: UnfoldTransitionModule.kt */
public final class UnfoldTransitionModule$provideStatusBarScopedTransitionProvider$1<T, R> implements Function {
    public static final UnfoldTransitionModule$provideStatusBarScopedTransitionProvider$1<T, R> INSTANCE = new UnfoldTransitionModule$provideStatusBarScopedTransitionProvider$1<>();

    public final ScopedUnfoldTransitionProgressProvider apply(NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        return new ScopedUnfoldTransitionProgressProvider(naturalRotationUnfoldProgressProvider);
    }
}
