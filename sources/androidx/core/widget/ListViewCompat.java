package androidx.core.widget;

import android.widget.ListView;

public final class ListViewCompat {
    public static void scrollListBy(ListView listView, int i) {
        Api19Impl.scrollListBy(listView, i);
    }

    public static boolean canScrollList(ListView listView, int i) {
        return Api19Impl.canScrollList(listView, i);
    }

    public static class Api19Impl {
        public static void scrollListBy(ListView listView, int i) {
            listView.scrollListBy(i);
        }

        public static boolean canScrollList(ListView listView, int i) {
            return listView.canScrollList(i);
        }
    }
}
