package androidx.transition;

import android.graphics.Matrix;
import android.widget.ImageView;

public class ImageViewUtils {
    public static void animateTransform(ImageView imageView, Matrix matrix) {
        imageView.animateTransform(matrix);
    }
}
