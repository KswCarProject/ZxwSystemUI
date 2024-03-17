package com.android.systemui.decor;

import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DecorProvider.kt */
public final class CornerDecorProvider$alignedBounds$2 extends Lambda implements Function0<List<? extends Integer>> {
    public final /* synthetic */ CornerDecorProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CornerDecorProvider$alignedBounds$2(CornerDecorProvider cornerDecorProvider) {
        super(0);
        this.this$0 = cornerDecorProvider;
    }

    @NotNull
    public final List<Integer> invoke() {
        return CollectionsKt__CollectionsKt.listOf(Integer.valueOf(this.this$0.getAlignedBound1()), Integer.valueOf(this.this$0.getAlignedBound2()));
    }
}
