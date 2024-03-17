package com.android.wm.shell.bubbles.storage;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/* compiled from: BubbleVolatileRepository.kt */
public final class BubbleVolatileRepository$sanitizeBubbles$1<T> implements Predicate {
    public final /* synthetic */ List<Integer> $activeUsers;

    public BubbleVolatileRepository$sanitizeBubbles$1(List<Integer> list) {
        this.$activeUsers = list;
    }

    public final boolean test(@NotNull BubbleEntity bubbleEntity) {
        return !this.$activeUsers.contains(Integer.valueOf(bubbleEntity.getUserId()));
    }
}
