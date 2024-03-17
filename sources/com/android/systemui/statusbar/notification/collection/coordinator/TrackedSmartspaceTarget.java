package com.android.systemui.statusbar.notification.collection.coordinator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SmartspaceDedupingCoordinator.kt */
public final class TrackedSmartspaceTarget {
    public long alertExceptionExpires;
    @Nullable
    public Runnable cancelTimeoutRunnable;
    @NotNull
    public final String key;
    public boolean shouldFilter;

    public TrackedSmartspaceTarget(@NotNull String str) {
        this.key = str;
    }

    @NotNull
    public final String getKey() {
        return this.key;
    }

    @Nullable
    public final Runnable getCancelTimeoutRunnable() {
        return this.cancelTimeoutRunnable;
    }

    public final void setCancelTimeoutRunnable(@Nullable Runnable runnable) {
        this.cancelTimeoutRunnable = runnable;
    }

    public final long getAlertExceptionExpires() {
        return this.alertExceptionExpires;
    }

    public final void setAlertExceptionExpires(long j) {
        this.alertExceptionExpires = j;
    }

    public final boolean getShouldFilter() {
        return this.shouldFilter;
    }

    public final void setShouldFilter(boolean z) {
        this.shouldFilter = z;
    }
}
