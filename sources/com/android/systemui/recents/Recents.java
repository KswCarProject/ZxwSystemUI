package com.android.systemui.recents;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.statusbar.CommandQueue;
import java.io.PrintWriter;
import java.util.List;

public class Recents extends CoreStartable implements CommandQueue.Callbacks {
    public final CommandQueue mCommandQueue;
    public long mCurRecentClickCount = 0;
    public final Handler mHandler = new Handler();
    public final RecentsImplementation mImpl;

    public Recents(Context context, RecentsImplementation recentsImplementation, CommandQueue commandQueue) {
        super(context);
        this.mImpl = recentsImplementation;
        this.mCommandQueue = commandQueue;
    }

    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mImpl.onStart(this.mContext);
    }

    public void onBootCompleted() {
        this.mImpl.onBootCompleted();
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.mImpl.onConfigurationChanged(configuration);
    }

    public void appTransitionFinished(int i) {
        if (this.mContext.getDisplayId() == i) {
            this.mImpl.onAppTransitionFinished();
        }
    }

    public void showRecentApps(boolean z) {
        if (isUserSetup()) {
            this.mImpl.showRecentApps(z);
        }
    }

    public void hideRecentApps(boolean z, boolean z2) {
        if (isUserSetup()) {
            this.mImpl.hideRecentApps(z, z2);
        }
    }

    public void toggleRecentApps() {
        if (isUserSetup() && SystemClock.elapsedRealtime() - this.mCurRecentClickCount >= 500) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            if (Settings.System.getInt(contentResolver, "zxw.Launcher3.SplitSceen", 0) <= 0) {
                Settings.System.putInt(contentResolver, "zxw.Launcher3.SplitSceen", -1);
                Settings.System.putInt(contentResolver, "zxw.Launcher3.SplitSceen.taskid", 0);
                this.mImpl.toggleRecentApps();
                this.mHandler.postDelayed(new Recents$$ExternalSyntheticLambda0(contentResolver), 1000);
                this.mCurRecentClickCount = SystemClock.elapsedRealtime();
            }
        }
    }

    public void toggleSplitScreen() {
        if (isUserSetup() && SystemClock.elapsedRealtime() - this.mCurRecentClickCount >= 500) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            if (Settings.System.getInt(contentResolver, "zxw.Launcher3.SplitSceen", 0) == 0) {
                ActivityManager.getService();
                try {
                    List allRootTaskInfos = ActivityTaskManager.getService().getAllRootTaskInfos();
                    ActivityTaskManager.RootTaskInfo rootTaskInfo = null;
                    ActivityTaskManager.RootTaskInfo rootTaskInfo2 = null;
                    for (int i = 0; i < allRootTaskInfos.size(); i++) {
                        ActivityTaskManager.RootTaskInfo rootTaskInfo3 = (ActivityTaskManager.RootTaskInfo) allRootTaskInfos.get(i);
                        Log.i("Recents", " rootTaskInfo = " + rootTaskInfo3.toString());
                        if (rootTaskInfo3.visible) {
                            if (rootTaskInfo3.getConfiguration().windowConfiguration.getWindowingMode() == 3) {
                                rootTaskInfo = rootTaskInfo3;
                            } else if (rootTaskInfo3.getConfiguration().windowConfiguration.getWindowingMode() == 4) {
                                rootTaskInfo2 = rootTaskInfo3;
                            } else {
                                rootTaskInfo3.getConfiguration().windowConfiguration.getWindowingMode();
                            }
                        }
                    }
                    if (!(rootTaskInfo == null || rootTaskInfo2 == null)) {
                        Log.i("Recents", "in split mode");
                        toggleRecentApps();
                        return;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Settings.System.putInt(contentResolver, "zxw.Launcher3.SplitSceen", 1);
                this.mImpl.toggleRecentApps();
                this.mHandler.postDelayed(new Recents$$ExternalSyntheticLambda1(contentResolver), 1000);
                this.mCurRecentClickCount = SystemClock.elapsedRealtime();
            }
        }
    }

    public void preloadRecentApps() {
        if (isUserSetup()) {
            this.mImpl.preloadRecentApps();
        }
    }

    public void cancelPreloadRecentApps() {
        if (isUserSetup()) {
            this.mImpl.cancelPreloadRecentApps();
        }
    }

    public final boolean isUserSetup() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (Settings.Global.getInt(contentResolver, "device_provisioned", 0) == 0 || Settings.Secure.getInt(contentResolver, "user_setup_complete", 0) == 0) {
            return false;
        }
        return true;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        this.mImpl.dump(printWriter);
    }
}
