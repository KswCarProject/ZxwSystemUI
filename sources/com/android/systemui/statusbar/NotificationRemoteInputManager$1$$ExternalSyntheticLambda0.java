package com.android.systemui.statusbar;

import android.os.SystemClock;
import android.view.View;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationRemoteInputManager$1$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ View f$0;

    public /* synthetic */ NotificationRemoteInputManager$1$$ExternalSyntheticLambda0(View view) {
        this.f$0 = view;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).wakeUpIfDozing(SystemClock.uptimeMillis(), this.f$0, "NOTIFICATION_CLICK");
    }
}
