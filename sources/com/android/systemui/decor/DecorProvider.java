package com.android.systemui.decor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DecorProvider.kt */
public abstract class DecorProvider {
    @NotNull
    public abstract List<Integer> getAlignedBounds();

    public abstract int getViewId();

    @NotNull
    public abstract View inflateView(@NotNull Context context, @NotNull ViewGroup viewGroup, int i);

    public abstract void onReloadResAndMeasure(@NotNull View view, int i, int i2, @Nullable String str);
}
