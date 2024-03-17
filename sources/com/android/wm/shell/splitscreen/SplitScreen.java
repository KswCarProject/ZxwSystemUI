package com.android.wm.shell.splitscreen;

public interface SplitScreen {

    public interface SplitScreenListener {
        void onSplitVisibilityChanged(boolean z) {
        }

        void onStagePositionChanged(int i, int i2) {
        }

        void onTaskStageChanged(int i, int i2, boolean z) {
        }
    }

    ISplitScreen createExternalInterface() {
        return null;
    }

    void onFinishedWakingUp();

    void onKeyguardVisibilityChanged(boolean z);

    static String stageTypeToString(int i) {
        if (i == -1) {
            return "UNDEFINED";
        }
        if (i == 0) {
            return "MAIN";
        }
        if (i == 1) {
            return "SIDE";
        }
        return "UNKNOWN(" + i + ")";
    }
}
