package com.android.systemui.decor;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: DecorProviderFactory.kt */
public abstract class DecorProviderFactory {
    public abstract boolean getHasProviders();

    @NotNull
    public abstract List<DecorProvider> getProviders();
}
