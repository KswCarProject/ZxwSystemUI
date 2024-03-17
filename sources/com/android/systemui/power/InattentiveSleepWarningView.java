package com.android.systemui.power;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import java.util.Objects;

public class InattentiveSleepWarningView extends FrameLayout {
    public boolean mDismissing;
    public Animator mFadeOutAnimator;
    public final WindowManager mWindowManager = ((WindowManager) this.mContext.getSystemService(WindowManager.class));
    public final IBinder mWindowToken = new Binder();

    public static /* synthetic */ boolean lambda$new$0(View view, int i, KeyEvent keyEvent) {
        return true;
    }

    public InattentiveSleepWarningView(Context context) {
        super(context);
        LayoutInflater.from(this.mContext).inflate(R$layout.inattentive_sleep_warning, this, true);
        setFocusable(true);
        setOnKeyListener(new InattentiveSleepWarningView$$ExternalSyntheticLambda0());
        Animator loadAnimator = AnimatorInflater.loadAnimator(getContext(), 17498113);
        this.mFadeOutAnimator = loadAnimator;
        loadAnimator.setTarget(this);
        this.mFadeOutAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                InattentiveSleepWarningView.this.removeView();
            }

            public void onAnimationCancel(Animator animator) {
                InattentiveSleepWarningView.this.mDismissing = false;
                InattentiveSleepWarningView.this.setAlpha(1.0f);
                InattentiveSleepWarningView.this.setVisibility(0);
            }
        });
    }

    public final void removeView() {
        if (this.mDismissing) {
            setVisibility(4);
            this.mWindowManager.removeView(this);
        }
    }

    public void show() {
        if (getParent() == null) {
            setAlpha(1.0f);
            setVisibility(0);
            this.mWindowManager.addView(this, getLayoutParams(this.mWindowToken));
            announceForAccessibility(getContext().getString(R$string.inattentive_sleep_warning_message));
        } else if (this.mFadeOutAnimator.isStarted()) {
            this.mFadeOutAnimator.cancel();
        }
    }

    public void dismiss(boolean z) {
        if (getParent() != null) {
            this.mDismissing = true;
            if (z) {
                Animator animator = this.mFadeOutAnimator;
                Objects.requireNonNull(animator);
                postOnAnimation(new InattentiveSleepWarningView$$ExternalSyntheticLambda1(animator));
                return;
            }
            removeView();
        }
    }

    public final WindowManager.LayoutParams getLayoutParams(IBinder iBinder) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2006, 256, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.setTitle("InattentiveSleepWarning");
        layoutParams.token = iBinder;
        return layoutParams;
    }
}
