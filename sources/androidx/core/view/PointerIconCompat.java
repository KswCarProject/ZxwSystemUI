package androidx.core.view;

import android.content.Context;
import android.view.PointerIcon;

public final class PointerIconCompat {
    public final PointerIcon mPointerIcon;

    public PointerIconCompat(PointerIcon pointerIcon) {
        this.mPointerIcon = pointerIcon;
    }

    public Object getPointerIcon() {
        return this.mPointerIcon;
    }

    public static PointerIconCompat getSystemIcon(Context context, int i) {
        return new PointerIconCompat(Api24Impl.getSystemIcon(context, i));
    }

    public static class Api24Impl {
        public static PointerIcon getSystemIcon(Context context, int i) {
            return PointerIcon.getSystemIcon(context, i);
        }
    }
}
