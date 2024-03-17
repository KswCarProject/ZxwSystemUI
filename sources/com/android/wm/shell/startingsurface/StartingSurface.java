package com.android.wm.shell.startingsurface;

import android.app.TaskInfo;

public interface StartingSurface {

    public interface SysuiProxy {
        void requestTopUi(boolean z, String str);
    }

    IStartingWindow createExternalInterface() {
        return null;
    }

    int getBackgroundColor(TaskInfo taskInfo) {
        return -16777216;
    }

    void setSysuiProxy(SysuiProxy sysuiProxy);
}
