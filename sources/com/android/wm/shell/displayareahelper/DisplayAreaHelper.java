package com.android.wm.shell.displayareahelper;

import android.view.SurfaceControl;
import java.util.function.Consumer;

public interface DisplayAreaHelper {
    void attachToRootDisplayArea(int i, SurfaceControl.Builder builder, Consumer<SurfaceControl.Builder> consumer) {
    }
}
