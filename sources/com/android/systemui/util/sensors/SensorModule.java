package com.android.systemui.util.sensors;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.R$array;
import com.android.systemui.R$dimen;
import com.android.systemui.R$string;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.Lazy;
import java.util.Arrays;
import java.util.HashMap;

public class SensorModule {
    public static ThresholdSensor providePrimaryProximitySensor(SensorManager sensorManager, ThresholdSensorImpl.Builder builder) {
        try {
            return builder.setSensorDelay(3).setSensorResourceId(R$string.proximity_sensor_type, true).setThresholdResourceId(R$dimen.proximity_sensor_threshold).setThresholdLatchResourceId(R$dimen.proximity_sensor_threshold_latch).build();
        } catch (IllegalStateException unused) {
            Sensor defaultSensor = sensorManager.getDefaultSensor(8, true);
            return builder.setSensor(defaultSensor).setThresholdValue(defaultSensor != null ? defaultSensor.getMaximumRange() : 0.0f).build();
        }
    }

    public static ThresholdSensor provideSecondaryProximitySensor(ThresholdSensorImpl.Builder builder) {
        try {
            return builder.setSensorResourceId(R$string.proximity_sensor_secondary_type, true).setThresholdResourceId(R$dimen.proximity_sensor_secondary_threshold).setThresholdLatchResourceId(R$dimen.proximity_sensor_secondary_threshold_latch).build();
        } catch (IllegalStateException unused) {
            return builder.setSensor((Sensor) null).setThresholdValue(0.0f).build();
        }
    }

    public static ProximitySensor provideProximitySensor(Resources resources, Lazy<PostureDependentProximitySensor> lazy, Lazy<ProximitySensorImpl> lazy2) {
        if (hasPostureSupport(resources.getStringArray(R$array.proximity_sensor_posture_mapping))) {
            return lazy.get();
        }
        return lazy2.get();
    }

    public static ProximityCheck provideProximityCheck(ProximitySensor proximitySensor, DelayableExecutor delayableExecutor) {
        return new ProximityCheck(proximitySensor, delayableExecutor);
    }

    public static ThresholdSensor[] providePostureToProximitySensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, Resources resources) {
        return createPostureToSensorMapping(builderFactory, resources.getStringArray(R$array.proximity_sensor_posture_mapping), R$dimen.proximity_sensor_threshold, R$dimen.proximity_sensor_threshold_latch);
    }

    public static ThresholdSensor[] providePostureToSecondaryProximitySensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, Resources resources) {
        return createPostureToSensorMapping(builderFactory, resources.getStringArray(R$array.proximity_sensor_secondary_posture_mapping), R$dimen.proximity_sensor_secondary_threshold, R$dimen.proximity_sensor_secondary_threshold_latch);
    }

    public static ThresholdSensor[] createPostureToSensorMapping(ThresholdSensorImpl.BuilderFactory builderFactory, String[] strArr, int i, int i2) {
        ThresholdSensor[] thresholdSensorArr = new ThresholdSensor[5];
        Arrays.fill(thresholdSensorArr, builderFactory.createBuilder().setSensor((Sensor) null).setThresholdValue(0.0f).build());
        if (!hasPostureSupport(strArr)) {
            Log.e("SensorModule", "config doesn't support postures, but attempting to retrieve proxSensorMapping");
            return thresholdSensorArr;
        }
        HashMap hashMap = new HashMap();
        for (int i3 = 0; i3 < strArr.length; i3++) {
            try {
                String str = strArr[i3];
                if (hashMap.containsKey(str)) {
                    thresholdSensorArr[i3] = (ThresholdSensor) hashMap.get(str);
                } else {
                    ThresholdSensor build = builderFactory.createBuilder().setSensorType(strArr[i3], true).setThresholdResourceId(i).setThresholdLatchResourceId(i2).build();
                    thresholdSensorArr[i3] = build;
                    hashMap.put(str, build);
                }
            } catch (IllegalStateException unused) {
            }
        }
        return thresholdSensorArr;
    }

    public static boolean hasPostureSupport(String[] strArr) {
        if (!(strArr == null || strArr.length == 0)) {
            for (String isEmpty : strArr) {
                if (!TextUtils.isEmpty(isEmpty)) {
                    return true;
                }
            }
        }
        return false;
    }
}
