package androidx.mediarouter.media;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class MediaRouterActiveScanThrottlingHelper {
    public boolean mActiveScan;
    public long mCurrentTime;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public long mSuppressActiveScanTimeout;
    public final Runnable mUpdateDiscoveryRequestRunnable;

    public MediaRouterActiveScanThrottlingHelper(Runnable runnable) {
        this.mUpdateDiscoveryRequestRunnable = runnable;
    }

    public void reset() {
        this.mSuppressActiveScanTimeout = 0;
        this.mActiveScan = false;
        this.mCurrentTime = SystemClock.elapsedRealtime();
        this.mHandler.removeCallbacks(this.mUpdateDiscoveryRequestRunnable);
    }

    public void requestActiveScan(boolean z, long j) {
        if (z) {
            long j2 = this.mCurrentTime;
            if (j2 - j < 30000) {
                this.mSuppressActiveScanTimeout = Math.max(this.mSuppressActiveScanTimeout, (j + 30000) - j2);
                this.mActiveScan = true;
            }
        }
    }

    public boolean finalizeActiveScanAndScheduleSuppressActiveScanRunnable() {
        if (this.mActiveScan) {
            long j = this.mSuppressActiveScanTimeout;
            if (j > 0) {
                this.mHandler.postDelayed(this.mUpdateDiscoveryRequestRunnable, j);
            }
        }
        return this.mActiveScan;
    }
}
