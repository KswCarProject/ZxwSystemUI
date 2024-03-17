package com.android.systemui.screenrecord;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class RecordingController implements CallbackController<RecordingStateChangeCallback> {
    public BroadcastDispatcher mBroadcastDispatcher;
    public CountDownTimer mCountDownTimer = null;
    public boolean mIsRecording;
    public boolean mIsStarting;
    public CopyOnWriteArrayList<RecordingStateChangeCallback> mListeners = new CopyOnWriteArrayList<>();
    @VisibleForTesting
    public final BroadcastReceiver mStateChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "com.android.systemui.screenrecord.UPDATE_STATE".equals(intent.getAction())) {
                if (intent.hasExtra("extra_state")) {
                    RecordingController.this.updateState(intent.getBooleanExtra("extra_state", false));
                    return;
                }
                Log.e("RecordingController", "Received update intent with no state");
            }
        }
    };
    public PendingIntent mStopIntent;
    @VisibleForTesting
    public final BroadcastReceiver mUserChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            RecordingController.this.stopRecording();
        }
    };
    public UserContextProvider mUserContextProvider;

    public interface RecordingStateChangeCallback {
        void onCountdown(long j) {
        }

        void onCountdownEnd() {
        }

        void onRecordingEnd() {
        }

        void onRecordingStart() {
        }
    }

    public RecordingController(BroadcastDispatcher broadcastDispatcher, UserContextProvider userContextProvider) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mUserContextProvider = userContextProvider;
    }

    public ScreenRecordDialog createScreenRecordDialog(Context context, Runnable runnable) {
        return new ScreenRecordDialog(context, this, this.mUserContextProvider, runnable);
    }

    public void startCountdown(long j, long j2, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        this.mIsStarting = true;
        this.mStopIntent = pendingIntent2;
        final PendingIntent pendingIntent3 = pendingIntent;
        AnonymousClass3 r1 = new CountDownTimer(j, j2) {
            public void onTick(long j) {
                Iterator it = RecordingController.this.mListeners.iterator();
                while (it.hasNext()) {
                    ((RecordingStateChangeCallback) it.next()).onCountdown(j);
                }
            }

            public void onFinish() {
                RecordingController.this.mIsStarting = false;
                RecordingController.this.mIsRecording = true;
                Iterator it = RecordingController.this.mListeners.iterator();
                while (it.hasNext()) {
                    ((RecordingStateChangeCallback) it.next()).onCountdownEnd();
                }
                try {
                    pendingIntent3.send();
                    RecordingController.this.mBroadcastDispatcher.registerReceiver(RecordingController.this.mUserChangeReceiver, new IntentFilter("android.intent.action.USER_SWITCHED"), (Executor) null, UserHandle.ALL);
                    RecordingController.this.mBroadcastDispatcher.registerReceiver(RecordingController.this.mStateChangeReceiver, new IntentFilter("com.android.systemui.screenrecord.UPDATE_STATE"), (Executor) null, UserHandle.ALL);
                    Log.d("RecordingController", "sent start intent");
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RecordingController", "Pending intent was cancelled: " + e.getMessage());
                }
            }
        };
        this.mCountDownTimer = r1;
        r1.start();
    }

    public void cancelCountdown() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        } else {
            Log.e("RecordingController", "Timer was null");
        }
        this.mIsStarting = false;
        Iterator<RecordingStateChangeCallback> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onCountdownEnd();
        }
    }

    public boolean isStarting() {
        return this.mIsStarting;
    }

    public synchronized boolean isRecording() {
        return this.mIsRecording;
    }

    public void stopRecording() {
        try {
            PendingIntent pendingIntent = this.mStopIntent;
            if (pendingIntent != null) {
                pendingIntent.send();
            } else {
                Log.e("RecordingController", "Stop intent was null");
            }
            updateState(false);
        } catch (PendingIntent.CanceledException e) {
            Log.e("RecordingController", "Error stopping: " + e.getMessage());
        }
    }

    public synchronized void updateState(boolean z) {
        if (!z) {
            if (this.mIsRecording) {
                this.mBroadcastDispatcher.unregisterReceiver(this.mUserChangeReceiver);
                this.mBroadcastDispatcher.unregisterReceiver(this.mStateChangeReceiver);
            }
        }
        this.mIsRecording = z;
        Iterator<RecordingStateChangeCallback> it = this.mListeners.iterator();
        while (it.hasNext()) {
            RecordingStateChangeCallback next = it.next();
            if (z) {
                next.onRecordingStart();
            } else {
                next.onRecordingEnd();
            }
        }
    }

    public void addCallback(RecordingStateChangeCallback recordingStateChangeCallback) {
        this.mListeners.add(recordingStateChangeCallback);
    }

    public void removeCallback(RecordingStateChangeCallback recordingStateChangeCallback) {
        this.mListeners.remove(recordingStateChangeCallback);
    }
}
