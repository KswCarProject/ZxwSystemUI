package androidx.transition;

import android.view.ViewGroup;

public class ViewGroupUtils {
    public static ViewGroupOverlayImpl getOverlay(ViewGroup viewGroup) {
        return new ViewGroupOverlayApi18(viewGroup);
    }

    public static void suppressLayout(ViewGroup viewGroup, boolean z) {
        viewGroup.suppressLayout(z);
    }

    public static int getChildDrawingOrder(ViewGroup viewGroup, int i) {
        return viewGroup.getChildDrawingOrder(i);
    }
}
