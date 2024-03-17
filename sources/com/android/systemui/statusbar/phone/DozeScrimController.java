package com.android.systemui.statusbar.phone;

import android.os.Handler;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.ScrimController;

public class DozeScrimController implements StatusBarStateController.StateListener {
    public static final boolean DEBUG = Log.isLoggable("DozeScrimController", 3);
    public final DozeLog mDozeLog;
    public final DozeParameters mDozeParameters;
    public boolean mDozing;
    public boolean mFullyPulsing;
    public final Handler mHandler = new Handler();
    public DozeHost.PulseCallback mPulseCallback;
    public final Runnable mPulseOut = new Runnable() {
        public void run() {
            DozeScrimController.this.mFullyPulsing = false;
            DozeScrimController.this.mHandler.removeCallbacks(DozeScrimController.this.mPulseOut);
            DozeScrimController.this.mHandler.removeCallbacks(DozeScrimController.this.mPulseOutExtended);
            if (DozeScrimController.DEBUG) {
                Log.d("DozeScrimController", "Pulse out, mDozing=" + DozeScrimController.this.mDozing);
            }
            if (DozeScrimController.this.mDozing) {
                DozeScrimController.this.pulseFinished();
            }
        }
    };
    public final Runnable mPulseOutExtended = new Runnable() {
        public void run() {
            DozeScrimController.this.mHandler.removeCallbacks(DozeScrimController.this.mPulseOut);
            DozeScrimController.this.mPulseOut.run();
        }
    };
    public int mPulseReason;
    public final ScrimController.Callback mScrimCallback = new ScrimController.Callback() {
        public void onDisplayBlanked() {
            if (DozeScrimController.DEBUG) {
                Log.d("DozeScrimController", "Pulse in, mDozing=" + DozeScrimController.this.mDozing + " mPulseReason=" + DozeLog.reasonToString(DozeScrimController.this.mPulseReason));
            }
            if (DozeScrimController.this.mDozing) {
                DozeScrimController.this.pulseStarted();
            }
        }

        public void onFinished() {
            if (DozeScrimController.DEBUG) {
                Log.d("DozeScrimController", "Pulse in finished, mDozing=" + DozeScrimController.this.mDozing);
            }
            if (DozeScrimController.this.mDozing) {
                if (!(DozeScrimController.this.mPulseReason == 1 || DozeScrimController.this.mPulseReason == 6)) {
                    DozeScrimController.this.mHandler.postDelayed(DozeScrimController.this.mPulseOut, (long) DozeScrimController.this.mDozeParameters.getPulseVisibleDuration());
                    DozeScrimController.this.mHandler.postDelayed(DozeScrimController.this.mPulseOutExtended, (long) DozeScrimController.this.mDozeParameters.getPulseVisibleDurationExtended());
                }
                DozeScrimController.this.mFullyPulsing = true;
            }
        }

        public void onCancelled() {
            DozeScrimController.this.pulseFinished();
        }
    };

    public void onStateChanged(int i) {
    }

    public DozeScrimController(DozeParameters dozeParameters, DozeLog dozeLog, StatusBarStateController statusBarStateController) {
        this.mDozeParameters = dozeParameters;
        statusBarStateController.addCallback(this);
        this.mDozeLog = dozeLog;
    }

    @VisibleForTesting
    public void setDozing(boolean z) {
        if (this.mDozing != z) {
            this.mDozing = z;
            if (!z) {
                cancelPulsing();
            }
        }
    }

    public void pulse(DozeHost.PulseCallback pulseCallback, int i) {
        if (pulseCallback == null) {
            throw new IllegalArgumentException("callback must not be null");
        } else if (!this.mDozing || this.mPulseCallback != null) {
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Pulse suppressed. Dozing: ");
                sb.append(this.mDozeParameters);
                sb.append(" had callback? ");
                sb.append(this.mPulseCallback != null);
                Log.d("DozeScrimController", sb.toString());
            }
            pulseCallback.onPulseFinished();
            if (!this.mDozing) {
                this.mDozeLog.tracePulseDropped("device isn't dozing");
                return;
            }
            DozeLog dozeLog = this.mDozeLog;
            dozeLog.tracePulseDropped("already has pulse callback mPulseCallback=" + this.mPulseCallback);
        } else {
            this.mPulseCallback = pulseCallback;
            this.mPulseReason = i;
        }
    }

    public void pulseOutNow() {
        if (this.mPulseCallback != null && this.mFullyPulsing) {
            this.mPulseOut.run();
        }
    }

    public boolean isPulsing() {
        return this.mPulseCallback != null;
    }

    public void extendPulse() {
        this.mHandler.removeCallbacks(this.mPulseOut);
    }

    public void cancelPendingPulseTimeout() {
        this.mHandler.removeCallbacks(this.mPulseOut);
        this.mHandler.removeCallbacks(this.mPulseOutExtended);
    }

    public final void cancelPulsing() {
        if (this.mPulseCallback != null) {
            if (DEBUG) {
                Log.d("DozeScrimController", "Cancel pulsing");
            }
            this.mFullyPulsing = false;
            this.mHandler.removeCallbacks(this.mPulseOut);
            this.mHandler.removeCallbacks(this.mPulseOutExtended);
            pulseFinished();
        }
    }

    public final void pulseStarted() {
        this.mDozeLog.tracePulseStart(this.mPulseReason);
        DozeHost.PulseCallback pulseCallback = this.mPulseCallback;
        if (pulseCallback != null) {
            pulseCallback.onPulseStarted();
        }
    }

    public final void pulseFinished() {
        this.mDozeLog.tracePulseFinish();
        DozeHost.PulseCallback pulseCallback = this.mPulseCallback;
        if (pulseCallback != null) {
            pulseCallback.onPulseFinished();
            this.mPulseCallback = null;
        }
    }

    public ScrimController.Callback getScrimCallback() {
        return this.mScrimCallback;
    }

    public void onDozingChanged(boolean z) {
        if (this.mDozing != z) {
            this.mDozeLog.traceDozingChanged(z);
        }
        setDozing(z);
    }
}
