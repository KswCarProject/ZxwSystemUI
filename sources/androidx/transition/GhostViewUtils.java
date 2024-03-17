package androidx.transition;

import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;

public class GhostViewUtils {
    public static GhostView addGhost(View view, ViewGroup viewGroup, Matrix matrix) {
        return GhostViewPort.addGhost(view, viewGroup, matrix);
    }

    public static void removeGhost(View view) {
        GhostViewPort.removeGhost(view);
    }
}
