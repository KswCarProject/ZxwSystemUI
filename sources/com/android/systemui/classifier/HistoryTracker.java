package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HistoryTracker {
    public static final double HISTORY_DECAY = Math.pow(10.0d, (Math.log10(0.1d) / 10000.0d) * 100.0d);
    public final List<BeliefListener> mBeliefListeners = new ArrayList();
    public DelayQueue<CombinedResult> mResults = new DelayQueue<>();
    public final SystemClock mSystemClock;

    public interface BeliefListener {
        void onBeliefChanged(double d);
    }

    public HistoryTracker(SystemClock systemClock) {
        this.mSystemClock = systemClock;
    }

    public double falseBelief() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return 0.5d;
        }
        return ((Double) this.mResults.stream().map(new HistoryTracker$$ExternalSyntheticLambda0(this.mSystemClock.uptimeMillis())).reduce(Double.valueOf(0.5d), new HistoryTracker$$ExternalSyntheticLambda1())).doubleValue();
    }

    public double falseConfidence() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return 0.0d;
        }
        return 1.0d - Math.sqrt(((Double) this.mResults.stream().map(new HistoryTracker$$ExternalSyntheticLambda4(((Double) this.mResults.stream().map(new HistoryTracker$$ExternalSyntheticLambda2()).reduce(Double.valueOf(0.0d), new HistoryTracker$$ExternalSyntheticLambda3())).doubleValue() / ((double) this.mResults.size()))).reduce(Double.valueOf(0.0d), new HistoryTracker$$ExternalSyntheticLambda3())).doubleValue() / ((double) this.mResults.size()));
    }

    public void addResults(Collection<FalsingClassifier.Result> collection, long j) {
        double d = 0.0d;
        for (FalsingClassifier.Result next : collection) {
            d += ((next.isFalse() ? 0.5d : -0.5d) * next.getConfidence()) + 0.5d;
        }
        double size = d / ((double) collection.size());
        if (size == 1.0d) {
            size = 0.99999d;
        } else if (size == 0.0d) {
            size = 1.0E-5d;
        }
        double d2 = size;
        do {
        } while (this.mResults.poll() != null);
        this.mResults.add(new CombinedResult(j, d2));
        this.mBeliefListeners.forEach(new HistoryTracker$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addResults$3(BeliefListener beliefListener) {
        beliefListener.onBeliefChanged(falseBelief());
    }

    public void addBeliefListener(BeliefListener beliefListener) {
        this.mBeliefListeners.add(beliefListener);
    }

    public void removeBeliefListener(BeliefListener beliefListener) {
        this.mBeliefListeners.remove(beliefListener);
    }

    public class CombinedResult implements Delayed {
        public final long mExpiryMs;
        public final double mScore;

        public CombinedResult(long j, double d) {
            this.mExpiryMs = j + 10000;
            this.mScore = d;
        }

        public double getDecayedScore(long j) {
            return ((this.mScore - 0.5d) * Math.pow(HistoryTracker.HISTORY_DECAY, ((double) (10000 - (this.mExpiryMs - j))) / 100.0d)) + 0.5d;
        }

        public double getScore() {
            return this.mScore;
        }

        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.mExpiryMs - HistoryTracker.this.mSystemClock.uptimeMillis(), TimeUnit.MILLISECONDS);
        }

        public int compareTo(Delayed delayed) {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            return Long.compare(getDelay(timeUnit), delayed.getDelay(timeUnit));
        }
    }
}
