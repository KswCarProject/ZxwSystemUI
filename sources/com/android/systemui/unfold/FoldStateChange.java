package com.android.systemui.unfold;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FoldStateLoggingProvider.kt */
public final class FoldStateChange {
    public final int current;
    public final long dtMillis;
    public final int previous;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FoldStateChange)) {
            return false;
        }
        FoldStateChange foldStateChange = (FoldStateChange) obj;
        return this.previous == foldStateChange.previous && this.current == foldStateChange.current && this.dtMillis == foldStateChange.dtMillis;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.previous) * 31) + Integer.hashCode(this.current)) * 31) + Long.hashCode(this.dtMillis);
    }

    @NotNull
    public String toString() {
        return "FoldStateChange(previous=" + this.previous + ", current=" + this.current + ", dtMillis=" + this.dtMillis + ')';
    }

    public FoldStateChange(int i, int i2, long j) {
        this.previous = i;
        this.current = i2;
        this.dtMillis = j;
    }

    public final int getPrevious() {
        return this.previous;
    }

    public final int getCurrent() {
        return this.current;
    }

    public final long getDtMillis() {
        return this.dtMillis;
    }
}
