package com.android.systemui.unfold;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import java.util.function.Function;

/* compiled from: UnfoldTransitionModule.kt */
public final class UnfoldTransitionModule$provideNaturalRotationProgressProvider$1<T, R> implements Function {
    public final /* synthetic */ Context $context;
    public final /* synthetic */ IWindowManager $windowManager;

    public UnfoldTransitionModule$provideNaturalRotationProgressProvider$1(Context context, IWindowManager iWindowManager) {
        this.$context = context;
        this.$windowManager = iWindowManager;
    }

    public final NaturalRotationUnfoldProgressProvider apply(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        return new NaturalRotationUnfoldProgressProvider(this.$context, this.$windowManager, unfoldTransitionProgressProvider);
    }
}
