package com.android.wm.shell.pip;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.WindowManager;

public interface PipMenuController {
    void attach(SurfaceControl surfaceControl);

    void detach();

    boolean isMenuVisible();

    void movePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
    }

    void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    void resizePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
    }

    void updateMenuBounds(Rect rect) {
    }

    WindowManager.LayoutParams getPipMenuLayoutParams(String str, int i, int i2) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2038, 545521680, -3);
        layoutParams.privateFlags |= 536870912;
        layoutParams.setTitle(str);
        return layoutParams;
    }
}
