package com.android.systemui.statusbar.policy;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: SmartReplyStateInflater.kt */
public final class DelayedOnClickListener implements View.OnClickListener {
    @NotNull
    public final View.OnClickListener mActualListener;
    public final long mInitDelayMs;
    public final long mInitTimeMs = SystemClock.elapsedRealtime();

    public DelayedOnClickListener(@NotNull View.OnClickListener onClickListener, long j) {
        this.mActualListener = onClickListener;
        this.mInitDelayMs = j;
    }

    public void onClick(@NotNull View view) {
        if (hasFinishedInitialization()) {
            this.mActualListener.onClick(view);
        } else {
            Log.i("SmartReplyViewInflater", Intrinsics.stringPlus("Accidental Smart Suggestion click registered, delay: ", Long.valueOf(this.mInitDelayMs)));
        }
    }

    public final boolean hasFinishedInitialization() {
        return SystemClock.elapsedRealtime() >= this.mInitTimeMs + this.mInitDelayMs;
    }
}
