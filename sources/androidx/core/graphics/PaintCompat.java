package androidx.core.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.util.Pair;

public final class PaintCompat {
    public static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal<>();

    public static boolean hasGlyph(Paint paint, String str) {
        return Api23Impl.hasGlyph(paint, str);
    }

    public static class Api23Impl {
        public static boolean hasGlyph(Paint paint, String str) {
            return paint.hasGlyph(str);
        }
    }
}
