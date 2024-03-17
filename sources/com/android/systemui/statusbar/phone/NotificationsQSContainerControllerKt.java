package com.android.systemui.statusbar.phone;

import kotlin.reflect.KMutableProperty0;

/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerControllerKt {
    public static /* synthetic */ void getINSET_DEBOUNCE_MILLIS$annotations() {
    }

    public static final boolean setAndReportChange(KMutableProperty0<Integer> kMutableProperty0, int i) {
        int intValue = kMutableProperty0.get().intValue();
        kMutableProperty0.set(Integer.valueOf(i));
        return intValue != i;
    }
}
