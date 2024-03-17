package com.android.systemui.util.sensors;

import android.util.Log;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;

public class PostureDependentProximitySensor extends ProximitySensorImpl {
    public final DevicePostureController.Callback mDevicePostureCallback;
    public final DevicePostureController mDevicePostureController;
    public final ThresholdSensor[] mPostureToPrimaryProxSensorMap;
    public final ThresholdSensor[] mPostureToSecondaryProxSensorMap;

    public PostureDependentProximitySensor(ThresholdSensor[] thresholdSensorArr, ThresholdSensor[] thresholdSensorArr2, DelayableExecutor delayableExecutor, Execution execution, DevicePostureController devicePostureController) {
        super(thresholdSensorArr[0], thresholdSensorArr2[0], delayableExecutor, execution);
        PostureDependentProximitySensor$$ExternalSyntheticLambda0 postureDependentProximitySensor$$ExternalSyntheticLambda0 = new PostureDependentProximitySensor$$ExternalSyntheticLambda0(this);
        this.mDevicePostureCallback = postureDependentProximitySensor$$ExternalSyntheticLambda0;
        this.mPostureToPrimaryProxSensorMap = thresholdSensorArr;
        this.mPostureToSecondaryProxSensorMap = thresholdSensorArr2;
        this.mDevicePostureController = devicePostureController;
        this.mDevicePosture = devicePostureController.getDevicePosture();
        devicePostureController.addCallback(postureDependentProximitySensor$$ExternalSyntheticLambda0);
        chooseSensors();
    }

    public void destroy() {
        super.destroy();
        this.mDevicePostureController.removeCallback(this.mDevicePostureCallback);
    }

    public final void chooseSensors() {
        int i = this.mDevicePosture;
        ThresholdSensor[] thresholdSensorArr = this.mPostureToPrimaryProxSensorMap;
        if (i < thresholdSensorArr.length) {
            ThresholdSensor[] thresholdSensorArr2 = this.mPostureToSecondaryProxSensorMap;
            if (i < thresholdSensorArr2.length) {
                ThresholdSensor thresholdSensor = thresholdSensorArr[i];
                ThresholdSensor thresholdSensor2 = thresholdSensorArr2[i];
                if (thresholdSensor != this.mPrimaryThresholdSensor || thresholdSensor2 != this.mSecondaryThresholdSensor) {
                    logDebug("Register new proximity sensors newPosture=" + DevicePostureController.devicePostureToString(this.mDevicePosture));
                    unregisterInternal();
                    ThresholdSensor thresholdSensor3 = this.mPrimaryThresholdSensor;
                    if (thresholdSensor3 != null) {
                        thresholdSensor3.unregister(this.mPrimaryEventListener);
                    }
                    ThresholdSensor thresholdSensor4 = this.mSecondaryThresholdSensor;
                    if (thresholdSensor4 != null) {
                        thresholdSensor4.unregister(this.mSecondaryEventListener);
                    }
                    this.mPrimaryThresholdSensor = thresholdSensor;
                    this.mSecondaryThresholdSensor = thresholdSensor2;
                    this.mInitializedListeners = false;
                    registerInternal();
                    return;
                }
                return;
            }
        }
        Log.e("PostureDependProxSensor", "unsupported devicePosture=" + this.mDevicePosture);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        if (this.mDevicePosture != i) {
            this.mDevicePosture = i;
            chooseSensors();
        }
    }

    public String toString() {
        return String.format("{posture=%s, proximitySensor=%s}", new Object[]{DevicePostureController.devicePostureToString(this.mDevicePosture), super.toString()});
    }
}
