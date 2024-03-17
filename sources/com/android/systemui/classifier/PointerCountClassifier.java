package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import java.util.Locale;

public class PointerCountClassifier extends FalsingClassifier {
    public int mMaxPointerCount;

    public PointerCountClassifier(FalsingDataProvider falsingDataProvider) {
        super(falsingDataProvider);
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int i = this.mMaxPointerCount;
        if (motionEvent.getActionMasked() == 0) {
            this.mMaxPointerCount = motionEvent.getPointerCount();
        } else {
            this.mMaxPointerCount = Math.max(this.mMaxPointerCount, motionEvent.getPointerCount());
        }
        if (i != this.mMaxPointerCount) {
            FalsingClassifier.logDebug("Pointers observed:" + this.mMaxPointerCount);
        }
    }

    public FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        int i2 = 2;
        if (!(i == 0 || i == 2)) {
            i2 = 1;
        }
        return this.mMaxPointerCount > i2 ? falsed(1.0d, getReason(i2)) : FalsingClassifier.Result.passed(0.0d);
    }

    public final String getReason(int i) {
        return String.format((Locale) null, "{pointersObserved=%d, threshold=%d}", new Object[]{Integer.valueOf(this.mMaxPointerCount), Integer.valueOf(i)});
    }
}
