package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class ExpandableViewController_Factory implements Factory<ExpandableViewController> {
    public final Provider<ExpandableView> viewProvider;

    public ExpandableViewController_Factory(Provider<ExpandableView> provider) {
        this.viewProvider = provider;
    }

    public ExpandableViewController get() {
        return newInstance(this.viewProvider.get());
    }

    public static ExpandableViewController_Factory create(Provider<ExpandableView> provider) {
        return new ExpandableViewController_Factory(provider);
    }

    public static ExpandableViewController newInstance(ExpandableView expandableView) {
        return new ExpandableViewController(expandableView);
    }
}