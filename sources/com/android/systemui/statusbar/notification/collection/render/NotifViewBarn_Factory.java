package com.android.systemui.statusbar.notification.collection.render;

import dagger.internal.Factory;

public final class NotifViewBarn_Factory implements Factory<NotifViewBarn> {

    public static final class InstanceHolder {
        public static final NotifViewBarn_Factory INSTANCE = new NotifViewBarn_Factory();
    }

    public NotifViewBarn get() {
        return newInstance();
    }

    public static NotifViewBarn_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotifViewBarn newInstance() {
        return new NotifViewBarn();
    }
}
