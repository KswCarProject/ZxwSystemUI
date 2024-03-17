package com.android.systemui.controls.management;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$setUpList$1$1 extends GridLayoutManager {
    public final /* synthetic */ int $spanCount;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlsEditingActivity$setUpList$1$1(int i, Context context) {
        super(context, i);
        this.$spanCount = i;
    }

    public int getRowCountForAccessibility(@NotNull RecyclerView.Recycler recycler, @NotNull RecyclerView.State state) {
        int rowCountForAccessibility = super.getRowCountForAccessibility(recycler, state);
        return rowCountForAccessibility > 0 ? rowCountForAccessibility - 1 : rowCountForAccessibility;
    }
}
