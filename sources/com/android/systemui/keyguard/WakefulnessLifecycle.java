package com.android.systemui.keyguard;

import android.app.IWallpaperManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Trace;
import android.util.DisplayMetrics;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import java.io.PrintWriter;

public class WakefulnessLifecycle extends Lifecycle<Observer> implements Dumpable {
    public final Context mContext;
    public final DisplayMetrics mDisplayMetrics;
    public Point mLastSleepOriginLocation = null;
    public int mLastSleepReason = 0;
    public Point mLastWakeOriginLocation = null;
    public int mLastWakeReason = 0;
    public int mWakefulness = 2;
    public final IWallpaperManager mWallpaperManagerService;

    public interface Observer {
        void onFinishedGoingToSleep() {
        }

        void onFinishedWakingUp() {
        }

        void onPostFinishedWakingUp() {
        }

        void onStartedGoingToSleep() {
        }

        void onStartedWakingUp() {
        }
    }

    public WakefulnessLifecycle(Context context, IWallpaperManager iWallpaperManager, DumpManager dumpManager) {
        this.mContext = context;
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mWallpaperManagerService = iWallpaperManager;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public int getWakefulness() {
        return this.mWakefulness;
    }

    public int getLastWakeReason() {
        return this.mLastWakeReason;
    }

    public int getLastSleepReason() {
        return this.mLastSleepReason;
    }

    public void dispatchStartedWakingUp(int i) {
        if (getWakefulness() != 1) {
            setWakefulness(1);
            this.mLastWakeReason = i;
            updateLastWakeOriginLocation();
            IWallpaperManager iWallpaperManager = this.mWallpaperManagerService;
            if (iWallpaperManager != null) {
                try {
                    Point point = this.mLastWakeOriginLocation;
                    iWallpaperManager.notifyWakingUp(point.x, point.y, new Bundle());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda4());
        }
    }

    public void dispatchFinishedWakingUp() {
        if (getWakefulness() != 2) {
            setWakefulness(2);
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda2());
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda3());
        }
    }

    public void dispatchStartedGoingToSleep(int i) {
        if (getWakefulness() != 3) {
            setWakefulness(3);
            this.mLastSleepReason = i;
            updateLastSleepOriginLocation();
            IWallpaperManager iWallpaperManager = this.mWallpaperManagerService;
            if (iWallpaperManager != null) {
                try {
                    Point point = this.mLastSleepOriginLocation;
                    iWallpaperManager.notifyGoingToSleep(point.x, point.y, new Bundle());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda0());
        }
    }

    public void dispatchFinishedGoingToSleep() {
        if (getWakefulness() != 0) {
            setWakefulness(0);
            dispatch(new WakefulnessLifecycle$$ExternalSyntheticLambda1());
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("WakefulnessLifecycle:");
        printWriter.println("  mWakefulness=" + this.mWakefulness);
    }

    public final void setWakefulness(int i) {
        this.mWakefulness = i;
        Trace.traceCounter(4096, "wakefulness", i);
    }

    public final void updateLastWakeOriginLocation() {
        this.mLastWakeOriginLocation = null;
        if (this.mLastWakeReason != 1) {
            this.mLastWakeOriginLocation = getDefaultWakeSleepOrigin();
        } else {
            this.mLastWakeOriginLocation = getPowerButtonOrigin();
        }
    }

    public final void updateLastSleepOriginLocation() {
        this.mLastSleepOriginLocation = null;
        if (this.mLastSleepReason != 4) {
            this.mLastSleepOriginLocation = getDefaultWakeSleepOrigin();
        } else {
            this.mLastSleepOriginLocation = getPowerButtonOrigin();
        }
    }

    public final Point getPowerButtonOrigin() {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        if (z) {
            return new Point(this.mDisplayMetrics.widthPixels, this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_power_button_center_screen_location_y));
        }
        return new Point(this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_power_button_center_screen_location_y), this.mDisplayMetrics.heightPixels);
    }

    public final Point getDefaultWakeSleepOrigin() {
        DisplayMetrics displayMetrics = this.mDisplayMetrics;
        return new Point(displayMetrics.widthPixels / 2, displayMetrics.heightPixels);
    }
}
