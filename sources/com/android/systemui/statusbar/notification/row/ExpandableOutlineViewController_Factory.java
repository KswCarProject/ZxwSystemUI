package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class ExpandableOutlineViewController_Factory implements Factory<ExpandableOutlineViewController> {
    public final Provider<ExpandableViewController> expandableViewControllerProvider;
    public final Provider<ExpandableOutlineView> viewProvider;

    public ExpandableOutlineViewController_Factory(Provider<ExpandableOutlineView> provider, Provider<ExpandableViewController> provider2) {
        this.viewProvider = provider;
        this.expandableViewControllerProvider = provider2;
    }

    public ExpandableOutlineViewController get() {
        return newInstance(this.viewProvider.get(), this.expandableViewControllerProvider.get());
    }

    public static ExpandableOutlineViewController_Factory create(Provider<ExpandableOutlineView> provider, Provider<ExpandableViewController> provider2) {
        return new ExpandableOutlineViewController_Factory(provider, provider2);
    }

    public static ExpandableOutlineViewController newInstance(ExpandableOutlineView expandableOutlineView, ExpandableViewController expandableViewController) {
        return new ExpandableOutlineViewController(expandableOutlineView, expandableViewController);
    }
}
