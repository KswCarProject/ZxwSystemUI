package com.android.systemui.statusbar.notification;

import com.android.systemui.util.DeviceConfigProxy;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationSectionsFeatureManager.kt */
public final class NotificationSectionsFeatureManagerKt {
    @Nullable
    public static Boolean sUsePeopleFiltering;

    public static final boolean usePeopleFiltering(DeviceConfigProxy deviceConfigProxy) {
        if (sUsePeopleFiltering == null) {
            sUsePeopleFiltering = Boolean.valueOf(deviceConfigProxy.getBoolean("systemui", "notifications_use_people_filtering", true));
        }
        Boolean bool = sUsePeopleFiltering;
        Intrinsics.checkNotNull(bool);
        return bool.booleanValue();
    }
}
