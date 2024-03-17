package com.android.systemui.shared.rotation;

import android.app.ActivityManager;
import com.android.systemui.shared.rotation.RotationButtonController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ RotationButtonController.TaskStackListenerImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1(RotationButtonController.TaskStackListenerImpl taskStackListenerImpl, int i) {
        this.f$0 = taskStackListenerImpl;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onActivityRequestedOrientationChanged$0(this.f$1, (ActivityManager.RunningTaskInfo) obj);
    }
}
