package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;

public class GlobalActionsGridLayout extends GlobalActionsLayout {
    public GlobalActionsGridLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @VisibleForTesting
    public void setupListView() {
        ListGridLayout listView = getListView();
        listView.setExpectedCount(this.mAdapter.countListItems());
        listView.setReverseSublists(shouldReverseSublists());
        listView.setReverseItems(shouldReverseListItems());
        listView.setSwapRowsAndColumns(shouldSwapRowsAndColumns());
    }

    public void onUpdateList() {
        setupListView();
        super.onUpdateList();
        updateSeparatedItemSize();
    }

    @VisibleForTesting
    public void updateSeparatedItemSize() {
        ViewGroup separatedView = getSeparatedView();
        if (separatedView.getChildCount() != 0) {
            ViewGroup.LayoutParams layoutParams = separatedView.getChildAt(0).getLayoutParams();
            if (separatedView.getChildCount() == 1) {
                layoutParams.width = -1;
                layoutParams.height = -1;
                return;
            }
            layoutParams.width = -2;
            layoutParams.height = -2;
        }
    }

    public ListGridLayout getListView() {
        return (ListGridLayout) super.getListView();
    }

    public void removeAllListViews() {
        ListGridLayout listView = getListView();
        if (listView != null) {
            listView.removeAllItems();
        }
    }

    public void addToListView(View view, boolean z) {
        ListGridLayout listView = getListView();
        if (listView != null) {
            listView.addItem(view);
        }
    }

    public void removeAllItems() {
        ViewGroup separatedView = getSeparatedView();
        ListGridLayout listView = getListView();
        if (separatedView != null) {
            separatedView.removeAllViews();
        }
        if (listView != null) {
            listView.removeAllItems();
        }
    }

    @VisibleForTesting
    public boolean shouldReverseSublists() {
        return getCurrentRotation() == 3;
    }

    @VisibleForTesting
    public boolean shouldSwapRowsAndColumns() {
        return getCurrentRotation() != 0;
    }

    public boolean shouldReverseListItems() {
        int currentRotation = getCurrentRotation();
        boolean z = currentRotation == 0 || currentRotation == 3;
        return getCurrentLayoutDirection() == 1 ? !z : z;
    }

    @VisibleForTesting
    public float getAnimationDistance() {
        return (((float) getListView().getRowCount()) * getContext().getResources().getDimension(R$dimen.global_actions_grid_item_height)) / 2.0f;
    }
}
