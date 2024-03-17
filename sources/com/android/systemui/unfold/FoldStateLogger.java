package com.android.systemui.unfold;

import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import org.jetbrains.annotations.NotNull;

/* compiled from: FoldStateLogger.kt */
public final class FoldStateLogger implements FoldStateLoggingProvider.FoldStateLoggingListener {
    @NotNull
    public final FoldStateLoggingProvider foldStateLoggingProvider;

    public FoldStateLogger(@NotNull FoldStateLoggingProvider foldStateLoggingProvider2) {
        this.foldStateLoggingProvider = foldStateLoggingProvider2;
    }

    public final void init() {
        this.foldStateLoggingProvider.addCallback(this);
    }

    public void onFoldUpdate(@NotNull FoldStateChange foldStateChange) {
        FrameworkStatsLog.write(414, foldStateChange.getPrevious(), foldStateChange.getCurrent(), foldStateChange.getDtMillis());
    }
}
