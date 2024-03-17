package androidx.slice.widget;

import android.content.Context;
import android.net.Uri;

public class SliceMetrics {
    public void logHidden() {
        throw null;
    }

    public void logVisible() {
        throw null;
    }

    public static SliceMetrics getInstance(Context context, Uri uri) {
        return new SliceMetricsWrapper(context, uri);
    }
}
