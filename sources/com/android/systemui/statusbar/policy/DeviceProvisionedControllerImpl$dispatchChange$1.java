package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* compiled from: DeviceProvisionedControllerImpl.kt */
public final class DeviceProvisionedControllerImpl$dispatchChange$1 implements Runnable {
    public final /* synthetic */ Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> $callback;
    public final /* synthetic */ ArrayList<DeviceProvisionedController.DeviceProvisionedListener> $listenersCopy;

    public DeviceProvisionedControllerImpl$dispatchChange$1(ArrayList<DeviceProvisionedController.DeviceProvisionedListener> arrayList, Function1<? super DeviceProvisionedController.DeviceProvisionedListener, Unit> function1) {
        this.$listenersCopy = arrayList;
        this.$callback = function1;
    }

    public final void run() {
        ArrayList<DeviceProvisionedController.DeviceProvisionedListener> arrayList = this.$listenersCopy;
        Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> function1 = this.$callback;
        Iterator<T> it = arrayList.iterator();
        while (it.hasNext()) {
            function1.invoke(it.next());
        }
    }
}
