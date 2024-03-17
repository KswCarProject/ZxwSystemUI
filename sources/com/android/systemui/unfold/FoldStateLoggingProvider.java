package com.android.systemui.unfold;

import com.android.systemui.statusbar.policy.CallbackController;
import org.jetbrains.annotations.NotNull;

/* compiled from: FoldStateLoggingProvider.kt */
public interface FoldStateLoggingProvider extends CallbackController<FoldStateLoggingListener> {

    /* compiled from: FoldStateLoggingProvider.kt */
    public interface FoldStateLoggingListener {
        void onFoldUpdate(@NotNull FoldStateChange foldStateChange);
    }

    void init();
}
