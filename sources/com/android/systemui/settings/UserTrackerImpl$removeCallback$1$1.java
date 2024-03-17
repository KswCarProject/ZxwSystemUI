package com.android.systemui.settings;

import com.android.systemui.settings.UserTracker;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserTrackerImpl.kt */
public final class UserTrackerImpl$removeCallback$1$1<T> implements Predicate {
    public final /* synthetic */ UserTracker.Callback $callback;

    public UserTrackerImpl$removeCallback$1$1(UserTracker.Callback callback) {
        this.$callback = callback;
    }

    public final boolean test(@NotNull DataItem dataItem) {
        return dataItem.sameOrEmpty(this.$callback);
    }
}
