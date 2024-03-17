package androidx.core.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityRecord;

public class AccessibilityRecordCompat {
    public static void setSource(AccessibilityRecord accessibilityRecord, View view, int i) {
        Api16Impl.setSource(accessibilityRecord, view, i);
    }

    public static void setMaxScrollX(AccessibilityRecord accessibilityRecord, int i) {
        Api15Impl.setMaxScrollX(accessibilityRecord, i);
    }

    public static void setMaxScrollY(AccessibilityRecord accessibilityRecord, int i) {
        Api15Impl.setMaxScrollY(accessibilityRecord, i);
    }

    public static class Api16Impl {
        public static void setSource(AccessibilityRecord accessibilityRecord, View view, int i) {
            accessibilityRecord.setSource(view, i);
        }
    }

    public static class Api15Impl {
        public static void setMaxScrollX(AccessibilityRecord accessibilityRecord, int i) {
            accessibilityRecord.setMaxScrollX(i);
        }

        public static void setMaxScrollY(AccessibilityRecord accessibilityRecord, int i) {
            accessibilityRecord.setMaxScrollY(i);
        }
    }
}
