package androidx.slice.widget;

import android.app.slice.SliceMetrics;
import android.content.Context;
import android.net.Uri;

public class SliceMetricsWrapper extends SliceMetrics {
    public final SliceMetrics mSliceMetrics;

    public SliceMetricsWrapper(Context context, Uri uri) {
        this.mSliceMetrics = new SliceMetrics(context, uri);
    }

    public void logVisible() {
        this.mSliceMetrics.logVisible();
    }

    public void logHidden() {
        this.mSliceMetrics.logHidden();
    }
}
