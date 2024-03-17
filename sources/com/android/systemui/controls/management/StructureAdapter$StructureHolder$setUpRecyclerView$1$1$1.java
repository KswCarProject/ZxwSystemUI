package com.android.systemui.controls.management;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: StructureAdapter.kt */
public final class StructureAdapter$StructureHolder$setUpRecyclerView$1$1$1 extends GridLayoutManager.SpanSizeLookup {
    public final /* synthetic */ int $spanCount;
    public final /* synthetic */ RecyclerView $this_apply;

    public StructureAdapter$StructureHolder$setUpRecyclerView$1$1$1(RecyclerView recyclerView, int i) {
        this.$this_apply = recyclerView;
        this.$spanCount = i;
    }

    public int getSpanSize(int i) {
        RecyclerView.Adapter adapter = this.$this_apply.getAdapter();
        boolean z = false;
        if (adapter != null && adapter.getItemViewType(i) == 1) {
            z = true;
        }
        if (!z) {
            return this.$spanCount;
        }
        return 1;
    }
}
