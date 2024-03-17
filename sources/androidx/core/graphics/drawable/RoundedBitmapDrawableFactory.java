package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;

public final class RoundedBitmapDrawableFactory {
    public static RoundedBitmapDrawable create(Resources resources, Bitmap bitmap) {
        return new RoundedBitmapDrawable21(resources, bitmap);
    }
}
