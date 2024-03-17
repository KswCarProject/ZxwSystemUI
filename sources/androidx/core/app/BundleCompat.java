package androidx.core.app;

import android.os.Bundle;
import android.os.IBinder;

public final class BundleCompat {
    public static IBinder getBinder(Bundle bundle, String str) {
        return Api18Impl.getBinder(bundle, str);
    }

    public static class Api18Impl {
        public static IBinder getBinder(Bundle bundle, String str) {
            return bundle.getBinder(str);
        }
    }
}
