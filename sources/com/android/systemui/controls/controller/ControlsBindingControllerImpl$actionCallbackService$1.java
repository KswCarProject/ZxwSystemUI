package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.IControlsActionCallback;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsBindingControllerImpl.kt */
public final class ControlsBindingControllerImpl$actionCallbackService$1 extends IControlsActionCallback.Stub {
    public final /* synthetic */ ControlsBindingControllerImpl this$0;

    public ControlsBindingControllerImpl$actionCallbackService$1(ControlsBindingControllerImpl controlsBindingControllerImpl) {
        this.this$0 = controlsBindingControllerImpl;
    }

    public void accept(@NotNull IBinder iBinder, @NotNull String str, int i) {
        this.this$0.backgroundExecutor.execute(new ControlsBindingControllerImpl.OnActionResponseRunnable(iBinder, str, i));
    }
}
