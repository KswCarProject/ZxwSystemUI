package com.android.wm.shell.bubbles.storage;

import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/* compiled from: BubbleVolatileRepository.kt */
public final class BubbleVolatileRepository$removeBubblesForUserWithParent$1<T> implements Predicate {
    public final /* synthetic */ int $userId;

    public BubbleVolatileRepository$removeBubblesForUserWithParent$1(int i) {
        this.$userId = i;
    }

    public final boolean test(@NotNull BubbleEntity bubbleEntity) {
        return bubbleEntity.getUserId() == this.$userId;
    }
}
