package com.android.systemui.decor;

import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: DecorProvider.kt */
public abstract class CornerDecorProvider extends DecorProvider {
    @NotNull
    public final Lazy alignedBounds$delegate = LazyKt__LazyJVMKt.lazy(new CornerDecorProvider$alignedBounds$2(this));

    public abstract int getAlignedBound1();

    public abstract int getAlignedBound2();

    @NotNull
    public List<Integer> getAlignedBounds() {
        return (List) this.alignedBounds$delegate.getValue();
    }
}
