package com.android.systemui.util.sensors;

public interface ThresholdSensor {

    public interface Listener {
        void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent);
    }

    boolean isLoaded();

    void pause();

    void register(Listener listener);

    void resume();

    void setDelay(int i);

    void setTag(String str);

    void unregister(Listener listener);
}
