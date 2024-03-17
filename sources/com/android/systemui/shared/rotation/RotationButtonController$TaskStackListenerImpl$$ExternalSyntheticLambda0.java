package com.android.systemui.shared.rotation;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda0 implements Function {
    public final Object apply(Object obj) {
        return ((ActivityManagerWrapper) obj).getRunningTask();
    }
}
