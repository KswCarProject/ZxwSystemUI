package com.android.wm.shell.bubbles.storage;

import java.util.function.Predicate;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: BubbleVolatileRepository.kt */
public final class BubbleVolatileRepository$removeBubbles$1$1<T> implements Predicate {
    public final /* synthetic */ BubbleEntity $b;

    public BubbleVolatileRepository$removeBubbles$1$1(BubbleEntity bubbleEntity) {
        this.$b = bubbleEntity;
    }

    public final boolean test(@NotNull BubbleEntity bubbleEntity) {
        return Intrinsics.areEqual((Object) this.$b.getKey(), (Object) bubbleEntity.getKey());
    }
}
