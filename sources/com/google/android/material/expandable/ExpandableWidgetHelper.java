package com.google.android.material.expandable;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public final class ExpandableWidgetHelper {
    public boolean expanded = false;
    public int expandedComponentIdHint = 0;
    public final View widget;

    public ExpandableWidgetHelper(ExpandableWidget expandableWidget) {
        this.widget = (View) expandableWidget;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("expanded", this.expanded);
        bundle.putInt("expandedComponentIdHint", this.expandedComponentIdHint);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle bundle) {
        this.expanded = bundle.getBoolean("expanded", false);
        this.expandedComponentIdHint = bundle.getInt("expandedComponentIdHint", 0);
        if (this.expanded) {
            dispatchExpandedStateChanged();
        }
    }

    public int getExpandedComponentIdHint() {
        return this.expandedComponentIdHint;
    }

    public final void dispatchExpandedStateChanged() {
        ViewParent parent = this.widget.getParent();
        if (parent instanceof CoordinatorLayout) {
            ((CoordinatorLayout) parent).dispatchDependentViewsChanged(this.widget);
        }
    }
}
