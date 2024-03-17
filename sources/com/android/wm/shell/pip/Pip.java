package com.android.wm.shell.pip;

import android.content.res.Configuration;
import android.graphics.Rect;
import java.io.PrintWriter;
import java.util.function.Consumer;

public interface Pip {
    void addPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
    }

    IPip createExternalInterface() {
        return null;
    }

    void dump(PrintWriter printWriter) {
    }

    void onConfigurationChanged(Configuration configuration) {
    }

    void onDensityOrFontScaleChanged() {
    }

    void onKeyguardDismissAnimationFinished() {
    }

    void onKeyguardVisibilityChanged(boolean z, boolean z2) {
    }

    void onOverlayChanged() {
    }

    void onSystemUiStateChanged(boolean z, int i) {
    }

    void registerSessionListenerForCurrentUser() {
    }

    void removePipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
    }

    void setPinnedStackAnimationType(int i) {
    }

    void showPictureInPictureMenu() {
    }
}
