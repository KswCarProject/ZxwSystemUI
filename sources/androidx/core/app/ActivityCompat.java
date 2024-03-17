package androidx.core.app;

import android.app.Activity;
import androidx.core.content.ContextCompat;

public class ActivityCompat extends ContextCompat {
    public static void finishAffinity(Activity activity) {
        Api16Impl.finishAffinity(activity);
    }

    public static void recreate(Activity activity) {
        activity.recreate();
    }

    public static class Api16Impl {
        public static void finishAffinity(Activity activity) {
            activity.finishAffinity();
        }
    }
}
