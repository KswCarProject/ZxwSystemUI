package com.android.systemui.statusbar.notification.collection.coordinator;

import dagger.internal.Factory;

public final class HideLocallyDismissedNotifsCoordinator_Factory implements Factory<HideLocallyDismissedNotifsCoordinator> {

    public static final class InstanceHolder {
        public static final HideLocallyDismissedNotifsCoordinator_Factory INSTANCE = new HideLocallyDismissedNotifsCoordinator_Factory();
    }

    public HideLocallyDismissedNotifsCoordinator get() {
        return newInstance();
    }

    public static HideLocallyDismissedNotifsCoordinator_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static HideLocallyDismissedNotifsCoordinator newInstance() {
        return new HideLocallyDismissedNotifsCoordinator();
    }
}
