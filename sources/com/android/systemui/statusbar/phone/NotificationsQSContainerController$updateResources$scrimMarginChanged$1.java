package com.android.systemui.statusbar.phone;

import kotlin.jvm.internal.MutablePropertyReference0Impl;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationsQSContainerController.kt */
public /* synthetic */ class NotificationsQSContainerController$updateResources$scrimMarginChanged$1 extends MutablePropertyReference0Impl {
    public NotificationsQSContainerController$updateResources$scrimMarginChanged$1(Object obj) {
        super(obj, NotificationsQSContainerController.class, "scrimShadeBottomMargin", "getScrimShadeBottomMargin()I", 0);
    }

    @Nullable
    public Object get() {
        return Integer.valueOf(((NotificationsQSContainerController) this.receiver).scrimShadeBottomMargin);
    }

    public void set(@Nullable Object obj) {
        ((NotificationsQSContainerController) this.receiver).scrimShadeBottomMargin = ((Number) obj).intValue();
    }
}
