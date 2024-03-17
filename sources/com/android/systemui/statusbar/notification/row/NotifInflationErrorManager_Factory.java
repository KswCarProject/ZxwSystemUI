package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;

public final class NotifInflationErrorManager_Factory implements Factory<NotifInflationErrorManager> {

    public static final class InstanceHolder {
        public static final NotifInflationErrorManager_Factory INSTANCE = new NotifInflationErrorManager_Factory();
    }

    public NotifInflationErrorManager get() {
        return newInstance();
    }

    public static NotifInflationErrorManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotifInflationErrorManager newInstance() {
        return new NotifInflationErrorManager();
    }
}
