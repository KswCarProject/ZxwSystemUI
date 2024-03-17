package androidx.transition;

import android.graphics.drawable.Drawable;

public interface ViewOverlayImpl {
    void add(Drawable drawable);

    void remove(Drawable drawable);
}
