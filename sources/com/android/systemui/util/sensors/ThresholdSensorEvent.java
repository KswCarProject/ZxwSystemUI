package com.android.systemui.util.sensors;

import java.util.Locale;

public class ThresholdSensorEvent {
    public final boolean mBelow;
    public final long mTimestampNs;

    public ThresholdSensorEvent(boolean z, long j) {
        this.mBelow = z;
        this.mTimestampNs = j;
    }

    public boolean getBelow() {
        return this.mBelow;
    }

    public long getTimestampNs() {
        return this.mTimestampNs;
    }

    public String toString() {
        return String.format((Locale) null, "{near=%s, timestamp_ns=%d}", new Object[]{Boolean.valueOf(this.mBelow), Long.valueOf(this.mTimestampNs)});
    }
}
