package com.android.systemui.unfold;

import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/* compiled from: SysUIUnfoldModule.kt */
public final class SysUIUnfoldModule {
    @NotNull
    public final Optional<SysUIUnfoldComponent> provideSysUIUnfoldComponent(@NotNull Optional<UnfoldTransitionProgressProvider> optional, @NotNull Optional<NaturalRotationUnfoldProgressProvider> optional2, @NotNull Optional<ScopedUnfoldTransitionProgressProvider> optional3, @NotNull SysUIUnfoldComponent.Factory factory) {
        UnfoldTransitionProgressProvider orElse = optional.orElse((Object) null);
        NaturalRotationUnfoldProgressProvider orElse2 = optional2.orElse((Object) null);
        ScopedUnfoldTransitionProgressProvider orElse3 = optional3.orElse((Object) null);
        if (orElse == null || orElse2 == null || orElse3 == null) {
            return Optional.empty();
        }
        return Optional.of(factory.create(orElse, orElse2, orElse3));
    }
}
