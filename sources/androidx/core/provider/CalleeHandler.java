package androidx.core.provider;

import android.os.Handler;
import android.os.Looper;

public class CalleeHandler {
    public static Handler create() {
        if (Looper.myLooper() == null) {
            return new Handler(Looper.getMainLooper());
        }
        return new Handler();
    }
}
