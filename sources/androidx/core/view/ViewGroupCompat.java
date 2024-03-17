package androidx.core.view;

import android.view.ViewGroup;

public final class ViewGroupCompat {
    public static boolean isTransitionGroup(ViewGroup viewGroup) {
        return Api21Impl.isTransitionGroup(viewGroup);
    }

    public static class Api21Impl {
        public static boolean isTransitionGroup(ViewGroup viewGroup) {
            return viewGroup.isTransitionGroup();
        }
    }
}
