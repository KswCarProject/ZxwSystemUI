package com.android.systemui.screenshot;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.accessibility.AccessibilityManager;

public class TimeoutHandler extends Handler {
    public final Context mContext;
    public int mDefaultTimeout = 6000;
    public Runnable mOnTimeout;

    public static /* synthetic */ void lambda$new$0() {
    }

    public TimeoutHandler(Context context) {
        super(Looper.getMainLooper());
        this.mContext = context;
        this.mOnTimeout = new TimeoutHandler$$ExternalSyntheticLambda0();
    }

    public void setOnTimeoutRunnable(Runnable runnable) {
        this.mOnTimeout = runnable;
    }

    public void handleMessage(Message message) {
        if (message.what == 2) {
            this.mOnTimeout.run();
        }
    }

    public void setDefaultTimeoutMillis(int i) {
        this.mDefaultTimeout = i;
    }

    public int getDefaultTimeoutMillis() {
        return this.mDefaultTimeout;
    }

    public void cancelTimeout() {
        removeMessages(2);
    }

    public void resetTimeout() {
        cancelTimeout();
        sendMessageDelayed(obtainMessage(2), (long) ((AccessibilityManager) this.mContext.getSystemService("accessibility")).getRecommendedTimeoutMillis(this.mDefaultTimeout, 4));
    }
}
