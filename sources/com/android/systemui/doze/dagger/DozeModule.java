package com.android.systemui.doze.dagger;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import com.android.systemui.R$string;
import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeBrightnessHostForwarder;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeScreenStatePreventingAdapter;
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.doze.DozeSuppressor;
import com.android.systemui.doze.DozeSuspendScreenStatePreventingAdapter;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeWallpaperState;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public abstract class DozeModule {
    public static DozeMachine.Part[] providesDozeMachineParts(DozePauser dozePauser, DozeFalsingManagerAdapter dozeFalsingManagerAdapter, DozeTriggers dozeTriggers, DozeUi dozeUi, DozeScreenState dozeScreenState, DozeScreenBrightness dozeScreenBrightness, DozeWallpaperState dozeWallpaperState, DozeDockHandler dozeDockHandler, DozeAuthRemover dozeAuthRemover, DozeSuppressor dozeSuppressor) {
        return new DozeMachine.Part[]{dozePauser, dozeFalsingManagerAdapter, dozeTriggers, dozeUi, dozeScreenState, dozeScreenBrightness, dozeWallpaperState, dozeDockHandler, dozeAuthRemover, dozeSuppressor};
    }

    public static DozeMachine.Service providesWrappedService(DozeMachine.Service service, DozeHost dozeHost, DozeParameters dozeParameters) {
        return DozeSuspendScreenStatePreventingAdapter.wrapIfNeeded(DozeScreenStatePreventingAdapter.wrapIfNeeded(new DozeBrightnessHostForwarder(service, dozeHost), dozeParameters), dozeParameters);
    }

    public static WakeLock providesDozeWakeLock(DelayedWakeLock.Builder builder, Handler handler) {
        return builder.setHandler(handler).setTag("Doze").build();
    }

    public static Optional<Sensor>[] providesBrightnessSensors(AsyncSensorManager asyncSensorManager, Context context, DozeParameters dozeParameters) {
        String[] brightnessNames = dozeParameters.brightnessNames();
        if (brightnessNames.length != 0) {
            Optional<Sensor>[] optionalArr = new Optional[5];
            Arrays.fill(optionalArr, Optional.empty());
            HashMap hashMap = new HashMap();
            for (int i = 0; i < brightnessNames.length; i++) {
                String str = brightnessNames[i];
                if (!hashMap.containsKey(str)) {
                    hashMap.put(str, Optional.ofNullable(DozeSensors.findSensor(asyncSensorManager, context.getString(R$string.doze_brightness_sensor_type), brightnessNames[i])));
                }
                optionalArr[i] = (Optional) hashMap.get(str);
            }
            return optionalArr;
        }
        return new Optional[]{Optional.ofNullable(DozeSensors.findSensor(asyncSensorManager, context.getString(R$string.doze_brightness_sensor_type), (String) null))};
    }
}
