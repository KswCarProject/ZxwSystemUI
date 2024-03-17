package com.android.systemui.statusbar.policy;

public interface DevicePostureController extends CallbackController<Callback> {

    public interface Callback {
        void onPostureChanged(int i);
    }

    int getDevicePosture();

    static String devicePostureToString(int i) {
        if (i == 0) {
            return "DEVICE_POSTURE_UNKNOWN";
        }
        if (i == 1) {
            return "DEVICE_POSTURE_CLOSED";
        }
        if (i == 2) {
            return "DEVICE_POSTURE_HALF_OPENED";
        }
        if (i == 3) {
            return "DEVICE_POSTURE_OPENED";
        }
        if (i == 4) {
            return "DEVICE_POSTURE_FLIPPED";
        }
        return "UNSUPPORTED POSTURE posture=" + i;
    }
}
