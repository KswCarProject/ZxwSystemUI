package androidx.appcompat.widget;

import android.widget.TextView;
import androidx.core.util.Preconditions;

public final class AppCompatTextClassifierHelper {
    public TextView mTextView;

    public AppCompatTextClassifierHelper(TextView textView) {
        this.mTextView = (TextView) Preconditions.checkNotNull(textView);
    }
}
