package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceProvisionedControllerImpl.kt */
public /* synthetic */ class DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1 extends FunctionReferenceImpl implements Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> {
    public static final DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1 INSTANCE = new DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1();

    public DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1() {
        super(1, DeviceProvisionedController.DeviceProvisionedListener.class, "onDeviceProvisionedChanged", "onDeviceProvisionedChanged()V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((DeviceProvisionedController.DeviceProvisionedListener) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        deviceProvisionedListener.onDeviceProvisionedChanged();
    }
}
