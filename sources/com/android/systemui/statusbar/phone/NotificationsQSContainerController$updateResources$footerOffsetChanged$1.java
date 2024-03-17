package com.android.systemui.statusbar.phone;

import kotlin.jvm.internal.MutablePropertyReference0Impl;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationsQSContainerController.kt */
public /* synthetic */ class NotificationsQSContainerController$updateResources$footerOffsetChanged$1 extends MutablePropertyReference0Impl {
    public NotificationsQSContainerController$updateResources$footerOffsetChanged$1(Object obj) {
        super(obj, NotificationsQSContainerController.class, "footerActionsOffset", "getFooterActionsOffset()I", 0);
    }

    @Nullable
    public Object get() {
        return Integer.valueOf(((NotificationsQSContainerController) this.receiver).footerActionsOffset);
    }

    public void set(@Nullable Object obj) {
        ((NotificationsQSContainerController) this.receiver).footerActionsOffset = ((Number) obj).intValue();
    }
}
