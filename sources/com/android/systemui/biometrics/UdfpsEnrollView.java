package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.systemui.R$id;
import java.util.Objects;

public class UdfpsEnrollView extends UdfpsAnimationView {
    public final UdfpsEnrollDrawable mFingerprintDrawable = new UdfpsEnrollDrawable(this.mContext);
    public final UdfpsEnrollProgressBarDrawable mFingerprintProgressDrawable;
    public ImageView mFingerprintProgressView;
    public ImageView mFingerprintView;
    public final Handler mHandler;

    public UdfpsEnrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintProgressDrawable = new UdfpsEnrollProgressBarDrawable(context);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public void onFinishInflate() {
        this.mFingerprintView = (ImageView) findViewById(R$id.udfps_enroll_animation_fp_view);
        this.mFingerprintProgressView = (ImageView) findViewById(R$id.udfps_enroll_animation_fp_progress_view);
        this.mFingerprintView.setImageDrawable(this.mFingerprintDrawable);
        this.mFingerprintProgressView.setImageDrawable(this.mFingerprintProgressDrawable);
    }

    public UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }

    public void updateSensorLocation(Rect rect) {
        View findViewById = findViewById(R$id.udfps_enroll_accessibility_view);
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        layoutParams.width = rect.width();
        layoutParams.height = rect.height();
        findViewById.setLayoutParams(layoutParams);
        findViewById.requestLayout();
    }

    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mFingerprintDrawable.setEnrollHelper(udfpsEnrollHelper);
    }

    public void onEnrollmentProgress(int i, int i2) {
        this.mHandler.post(new UdfpsEnrollView$$ExternalSyntheticLambda0(this, i, i2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$0(int i, int i2) {
        this.mFingerprintProgressDrawable.onEnrollmentProgress(i, i2);
        this.mFingerprintDrawable.onEnrollmentProgress(i, i2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentHelp$1(int i, int i2) {
        this.mFingerprintProgressDrawable.onEnrollmentHelp(i, i2);
    }

    public void onEnrollmentHelp(int i, int i2) {
        this.mHandler.post(new UdfpsEnrollView$$ExternalSyntheticLambda1(this, i, i2));
    }

    public void onLastStepAcquired() {
        Handler handler = this.mHandler;
        UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = this.mFingerprintProgressDrawable;
        Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
        handler.post(new UdfpsEnrollView$$ExternalSyntheticLambda2(udfpsEnrollProgressBarDrawable));
    }
}
