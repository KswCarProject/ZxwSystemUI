package com.android.systemui.controls.management;

import androidx.recyclerview.widget.GridLayoutManager;

/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$setUpList$1$2$1 extends GridLayoutManager.SpanSizeLookup {
    public final /* synthetic */ ControlAdapter $adapter;
    public final /* synthetic */ int $spanCount;

    public ControlsEditingActivity$setUpList$1$2$1(ControlAdapter controlAdapter, int i) {
        this.$adapter = controlAdapter;
        this.$spanCount = i;
    }

    public int getSpanSize(int i) {
        ControlAdapter controlAdapter = this.$adapter;
        boolean z = false;
        if (controlAdapter != null && controlAdapter.getItemViewType(i) == 1) {
            z = true;
        }
        if (!z) {
            return this.$spanCount;
        }
        return 1;
    }
}
