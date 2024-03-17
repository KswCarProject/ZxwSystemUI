package com.android.systemui.unfold;

import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FoldStateLoggingProviderImpl.kt */
public final class FoldStateLoggingProviderImpl implements FoldStateLoggingProvider, FoldStateProvider.FoldUpdatesListener {
    @Nullable
    public Long actionStartMillis;
    @NotNull
    public final SystemClock clock;
    @NotNull
    public final FoldStateProvider foldStateProvider;
    @Nullable
    public Integer lastState;
    @NotNull
    public final List<FoldStateLoggingProvider.FoldStateLoggingListener> outputListeners = new ArrayList();

    public void onHingeAngleUpdate(float f) {
    }

    public FoldStateLoggingProviderImpl(@NotNull FoldStateProvider foldStateProvider2, @NotNull SystemClock systemClock) {
        this.foldStateProvider = foldStateProvider2;
        this.clock = systemClock;
    }

    public void init() {
        this.foldStateProvider.addCallback(this);
        this.foldStateProvider.start();
    }

    public void onFoldUpdate(int i) {
        long elapsedRealtime = this.clock.elapsedRealtime();
        if (i == 0) {
            this.lastState = 2;
            this.actionStartMillis = Long.valueOf(elapsedRealtime);
        } else if (i == 1) {
            this.actionStartMillis = Long.valueOf(elapsedRealtime);
        } else if (i == 3) {
            dispatchState(3);
        } else if (i == 4) {
            dispatchState(1);
        } else if (i == 5) {
            dispatchState(2);
        }
    }

    public final void dispatchState(int i) {
        long elapsedRealtime = this.clock.elapsedRealtime();
        Integer num = this.lastState;
        Long l = this.actionStartMillis;
        if (!(num == null || num.intValue() == i || l == null)) {
            FoldStateChange foldStateChange = new FoldStateChange(num.intValue(), i, elapsedRealtime - l.longValue());
            for (FoldStateLoggingProvider.FoldStateLoggingListener onFoldUpdate : this.outputListeners) {
                onFoldUpdate.onFoldUpdate(foldStateChange);
            }
        }
        this.actionStartMillis = null;
        this.lastState = Integer.valueOf(i);
    }

    public void addCallback(@NotNull FoldStateLoggingProvider.FoldStateLoggingListener foldStateLoggingListener) {
        this.outputListeners.add(foldStateLoggingListener);
    }

    public void removeCallback(@NotNull FoldStateLoggingProvider.FoldStateLoggingListener foldStateLoggingListener) {
        this.outputListeners.remove(foldStateLoggingListener);
    }
}
