package com.android.systemui.statusbar.notification.row;

public class ExpandableOutlineViewController {
    public final ExpandableViewController mExpandableViewController;
    public final ExpandableOutlineView mView;

    public ExpandableOutlineViewController(ExpandableOutlineView expandableOutlineView, ExpandableViewController expandableViewController) {
        this.mView = expandableOutlineView;
        this.mExpandableViewController = expandableViewController;
    }

    public void init() {
        this.mExpandableViewController.init();
    }
}
