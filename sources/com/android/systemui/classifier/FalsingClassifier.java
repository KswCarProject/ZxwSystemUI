package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.plugins.FalsingManager;
import java.util.List;

public abstract class FalsingClassifier {
    public final FalsingDataProvider mDataProvider;
    public final FalsingDataProvider.MotionEventListener mMotionEventListener;

    public abstract Result calculateFalsingResult(int i, double d, double d2);

    public void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
    }

    public void onSessionEnded() {
    }

    public void onSessionStarted() {
    }

    public void onTouchEvent(MotionEvent motionEvent) {
    }

    public FalsingClassifier(FalsingDataProvider falsingDataProvider) {
        FalsingClassifier$$ExternalSyntheticLambda0 falsingClassifier$$ExternalSyntheticLambda0 = new FalsingClassifier$$ExternalSyntheticLambda0(this);
        this.mMotionEventListener = falsingClassifier$$ExternalSyntheticLambda0;
        this.mDataProvider = falsingDataProvider;
        falsingDataProvider.addMotionEventListener(falsingClassifier$$ExternalSyntheticLambda0);
    }

    public String getFalsingContext() {
        return getClass().getSimpleName();
    }

    public Result falsed(double d, String str) {
        return Result.falsed(d, getFalsingContext(), str);
    }

    public List<MotionEvent> getRecentMotionEvents() {
        return this.mDataProvider.getRecentMotionEvents();
    }

    public List<MotionEvent> getPriorMotionEvents() {
        return this.mDataProvider.getPriorMotionEvents();
    }

    public MotionEvent getFirstMotionEvent() {
        return this.mDataProvider.getFirstRecentMotionEvent();
    }

    public MotionEvent getLastMotionEvent() {
        return this.mDataProvider.getLastMotionEvent();
    }

    public boolean isHorizontal() {
        return this.mDataProvider.isHorizontal();
    }

    public boolean isRight() {
        return this.mDataProvider.isRight();
    }

    public boolean isVertical() {
        return this.mDataProvider.isVertical();
    }

    public boolean isUp() {
        return this.mDataProvider.isUp();
    }

    public float getAngle() {
        return this.mDataProvider.getAngle();
    }

    public int getWidthPixels() {
        return this.mDataProvider.getWidthPixels();
    }

    public int getHeightPixels() {
        return this.mDataProvider.getHeightPixels();
    }

    public float getXdpi() {
        return this.mDataProvider.getXdpi();
    }

    public float getYdpi() {
        return this.mDataProvider.getYdpi();
    }

    public void cleanup() {
        this.mDataProvider.removeMotionEventListener(this.mMotionEventListener);
    }

    public Result classifyGesture(int i, double d, double d2) {
        return calculateFalsingResult(i, d, d2);
    }

    public static void logDebug(String str) {
        BrightLineFalsingManager.logDebug(str);
    }

    public static void logInfo(String str) {
        BrightLineFalsingManager.logInfo(str);
    }

    public static class Result {
        public final double mConfidence;
        public final String mContext;
        public final boolean mFalsed;
        public final String mReason;

        public Result(boolean z, double d, String str, String str2) {
            this.mFalsed = z;
            this.mConfidence = d;
            this.mContext = str;
            this.mReason = str2;
        }

        public boolean isFalse() {
            return this.mFalsed;
        }

        public double getConfidence() {
            return this.mConfidence;
        }

        public String getReason() {
            return String.format("{context=%s reason=%s}", new Object[]{this.mContext, this.mReason});
        }

        public static Result falsed(double d, String str, String str2) {
            return new Result(true, d, str, str2);
        }

        public static Result passed(double d) {
            return new Result(false, d, (String) null, (String) null);
        }
    }
}
