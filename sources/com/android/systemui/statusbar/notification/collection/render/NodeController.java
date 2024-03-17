package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NodeController.kt */
public interface NodeController {
    void addChildAt(@NotNull NodeController nodeController, int i);

    @Nullable
    View getChildAt(int i);

    int getChildCount();

    @NotNull
    String getNodeLabel();

    @NotNull
    View getView();

    void moveChildTo(@NotNull NodeController nodeController, int i);

    void onViewAdded();

    void onViewMoved();

    void onViewRemoved();

    void removeChild(@NotNull NodeController nodeController, boolean z);

    /* compiled from: NodeController.kt */
    public static final class DefaultImpls {
        public static int getChildCount(@NotNull NodeController nodeController) {
            return 0;
        }

        public static void onViewAdded(@NotNull NodeController nodeController) {
        }

        public static void onViewMoved(@NotNull NodeController nodeController) {
        }

        public static void onViewRemoved(@NotNull NodeController nodeController) {
        }

        @Nullable
        public static View getChildAt(@NotNull NodeController nodeController, int i) {
            throw new RuntimeException("Not supported");
        }

        public static void addChildAt(@NotNull NodeController nodeController, @NotNull NodeController nodeController2, int i) {
            throw new RuntimeException("Not supported");
        }

        public static void moveChildTo(@NotNull NodeController nodeController, @NotNull NodeController nodeController2, int i) {
            throw new RuntimeException("Not supported");
        }

        public static void removeChild(@NotNull NodeController nodeController, @NotNull NodeController nodeController2, boolean z) {
            throw new RuntimeException("Not supported");
        }
    }
}
