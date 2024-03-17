package com.android.wm.shell.unfold;

import java.util.concurrent.Executor;

public interface ShellUnfoldProgressProvider {
    public static final ShellUnfoldProgressProvider NO_PROVIDER = new ShellUnfoldProgressProvider() {
    };

    public interface UnfoldListener {
        void onStateChangeFinished() {
        }

        void onStateChangeProgress(float f) {
        }

        void onStateChangeStarted() {
        }
    }

    void addListener(Executor executor, UnfoldListener unfoldListener) {
    }
}
