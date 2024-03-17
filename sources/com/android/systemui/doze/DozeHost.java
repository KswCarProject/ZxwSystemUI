package com.android.systemui.doze;

public interface DozeHost {

    public interface Callback {
        void onAlwaysOnSuppressedChanged(boolean z) {
        }

        void onNotificationAlerted(Runnable runnable) {
        }

        void onPowerSaveChanged(boolean z) {
        }
    }

    public interface PulseCallback {
        void onPulseFinished();

        void onPulseStarted();
    }

    void addCallback(Callback callback);

    void cancelGentleSleep();

    void dozeTimeTick();

    void extendPulse(int i);

    boolean isAlwaysOnSuppressed();

    boolean isPowerSaveActive();

    boolean isProvisioned();

    boolean isPulsingBlocked();

    void onIgnoreTouchWhilePulsing(boolean z);

    void onSlpiTap(float f, float f2);

    void prepareForGentleSleep(Runnable runnable);

    void pulseWhileDozing(PulseCallback pulseCallback, int i);

    void removeCallback(Callback callback);

    void setAnimateWakeup(boolean z);

    void setAodDimmingScrim(float f) {
    }

    void setDozeScreenBrightness(int i);

    void startDozing();

    void stopDozing();
}
