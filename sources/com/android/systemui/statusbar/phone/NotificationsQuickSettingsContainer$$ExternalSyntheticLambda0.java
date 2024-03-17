package com.android.systemui.statusbar.phone;

import android.view.View;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationsQuickSettingsContainer$$ExternalSyntheticLambda0 implements ToIntFunction {
    public final /* synthetic */ NotificationsQuickSettingsContainer f$0;

    public /* synthetic */ NotificationsQuickSettingsContainer$$ExternalSyntheticLambda0(NotificationsQuickSettingsContainer notificationsQuickSettingsContainer) {
        this.f$0 = notificationsQuickSettingsContainer;
    }

    public final int applyAsInt(Object obj) {
        return this.f$0.indexOfChild((View) obj);
    }
}
