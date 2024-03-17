package com.android.wm.shell.onehanded;

import android.graphics.Rect;

public interface OneHandedTransitionCallback {
    void onStartFinished(Rect rect) {
    }

    void onStartTransition(boolean z) {
    }

    void onStopFinished(Rect rect) {
    }
}
