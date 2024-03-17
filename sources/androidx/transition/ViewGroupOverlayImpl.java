package androidx.transition;

import android.view.View;

public interface ViewGroupOverlayImpl extends ViewOverlayImpl {
    void add(View view);

    void remove(View view);
}
