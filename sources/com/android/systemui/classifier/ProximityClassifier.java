package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.Locale;

public class ProximityClassifier extends FalsingClassifier {
    public final DistanceClassifier mDistanceClassifier;
    public long mGestureStartTimeNs;
    public boolean mNear;
    public long mNearDurationNs;
    public final float mPercentCoveredThreshold;
    public float mPercentNear;
    public long mPrevNearTimeNs;

    public ProximityClassifier(DistanceClassifier distanceClassifier, FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mDistanceClassifier = distanceClassifier;
        this.mPercentCoveredThreshold = deviceConfigProxy.getFloat("systemui", "brightline_falsing_proximity_percent_covered_threshold", 0.1f);
    }

    public void onSessionStarted() {
        this.mPrevNearTimeNs = 0;
        this.mPercentNear = 0.0f;
    }

    public void onSessionEnded() {
        this.mPrevNearTimeNs = 0;
        this.mPercentNear = 0.0f;
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mGestureStartTimeNs = motionEvent.getEventTimeNano();
            if (this.mPrevNearTimeNs > 0) {
                this.mPrevNearTimeNs = motionEvent.getEventTimeNano();
            }
            FalsingClassifier.logDebug("Gesture start time: " + this.mGestureStartTimeNs);
            this.mNearDurationNs = 0;
        }
        if (actionMasked == 1 || actionMasked == 3) {
            update(this.mNear, motionEvent.getEventTimeNano());
            long eventTimeNano = motionEvent.getEventTimeNano() - this.mGestureStartTimeNs;
            FalsingClassifier.logDebug("Gesture duration, Proximity duration: " + eventTimeNano + ", " + this.mNearDurationNs);
            if (eventTimeNano == 0) {
                this.mPercentNear = this.mNear ? 1.0f : 0.0f;
            } else {
                this.mPercentNear = ((float) this.mNearDurationNs) / ((float) eventTimeNano);
            }
        }
    }

    public void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
        boolean covered = proximityEvent.getCovered();
        long timestampNs = proximityEvent.getTimestampNs();
        FalsingClassifier.logDebug("Sensor is: " + covered + " at time " + timestampNs);
        update(covered, timestampNs);
    }

    public FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        if (i == 0 || i == 10 || i == 12 || i == 15) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (this.mPercentNear <= this.mPercentCoveredThreshold) {
            return FalsingClassifier.Result.passed(0.5d);
        }
        FalsingClassifier.Result isLongSwipe = this.mDistanceClassifier.isLongSwipe();
        if (isLongSwipe.isFalse()) {
            return falsed(0.5d, getReason(isLongSwipe, this.mPercentNear, this.mPercentCoveredThreshold));
        }
        return FalsingClassifier.Result.passed(0.5d);
    }

    public static String getReason(FalsingClassifier.Result result, float f, float f2) {
        return String.format((Locale) null, "{percentInProximity=%f, threshold=%f, distanceClassifier=%s}", new Object[]{Float.valueOf(f), Float.valueOf(f2), result.getReason()});
    }

    public final void update(boolean z, long j) {
        long j2 = this.mPrevNearTimeNs;
        if (j2 != 0 && j > j2 && this.mNear) {
            this.mNearDurationNs += j - j2;
            FalsingClassifier.logDebug("Updating duration: " + this.mNearDurationNs);
        }
        if (z) {
            FalsingClassifier.logDebug("Set prevNearTimeNs: " + j);
            this.mPrevNearTimeNs = j;
        }
        this.mNear = z;
    }
}
