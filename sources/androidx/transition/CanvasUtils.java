package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class CanvasUtils {
    @SuppressLint({"SoonBlockedPrivateApi"})
    public static void enableZ(Canvas canvas, boolean z) {
        if (z) {
            canvas.enableZ();
        } else {
            canvas.disableZ();
        }
    }
}
