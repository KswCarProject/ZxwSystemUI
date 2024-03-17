package androidx.appcompat.widget;

import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

public final class AppCompatReceiveContentHelper {
    public static boolean maybeHandleDragEventViaPerformReceiveContent(View view, DragEvent dragEvent) {
        return false;
    }

    public static boolean maybeHandleMenuActionViaPerformReceiveContent(TextView textView, int i) {
        return false;
    }
}
