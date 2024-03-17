package com.android.systemui.assist;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public final class PhoneStateMonitor {
    public static final String[] DEFAULT_HOME_CHANGE_ACTIONS = {"android.intent.action.ACTION_PREFERRED_ACTIVITY_CHANGED", "android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_REMOVED"};
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public final Context mContext;
    public ComponentName mDefaultHome = getCurrentDefaultHome();
    public boolean mLauncherShowing;
    public final StatusBarStateController mStatusBarStateController;

    public final boolean isLauncherInAllApps() {
        return false;
    }

    public final boolean isLauncherInOverview() {
        return false;
    }

    public PhoneStateMonitor(Context context, BroadcastDispatcher broadcastDispatcher, Lazy<Optional<CentralSurfaces>> lazy, BootCompleteCache bootCompleteCache, StatusBarStateController statusBarStateController) {
        this.mContext = context;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mStatusBarStateController = statusBarStateController;
        bootCompleteCache.addListener(new PhoneStateMonitor$$ExternalSyntheticLambda0(this));
        IntentFilter intentFilter = new IntentFilter();
        for (String addAction : DEFAULT_HOME_CHANGE_ACTIONS) {
            intentFilter.addAction(addAction);
        }
        broadcastDispatcher.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                PhoneStateMonitor.this.mDefaultHome = PhoneStateMonitor.getCurrentDefaultHome();
            }
        }, intentFilter);
        this.mLauncherShowing = isLauncherShowing(ActivityManagerWrapper.getInstance().getRunningTask());
        TaskStackChangeListeners.getInstance().registerTaskStackListener(new TaskStackChangeListener() {
            public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
                PhoneStateMonitor phoneStateMonitor = PhoneStateMonitor.this;
                phoneStateMonitor.mLauncherShowing = phoneStateMonitor.isLauncherShowing(runningTaskInfo);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mDefaultHome = getCurrentDefaultHome();
    }

    public int getPhoneState() {
        if (isShadeFullscreen()) {
            return getPhoneLockscreenState();
        }
        if (this.mLauncherShowing) {
            return getPhoneLauncherState();
        }
        return 9;
    }

    public static ComponentName getCurrentDefaultHome() {
        ArrayList arrayList = new ArrayList();
        ComponentName homeActivities = PackageManagerWrapper.getInstance().getHomeActivities(arrayList);
        if (homeActivities != null) {
            return homeActivities;
        }
        int i = Integer.MIN_VALUE;
        Iterator it = arrayList.iterator();
        while (true) {
            ComponentName componentName = null;
            while (true) {
                if (!it.hasNext()) {
                    return componentName;
                }
                ResolveInfo resolveInfo = (ResolveInfo) it.next();
                int i2 = resolveInfo.priority;
                if (i2 > i) {
                    componentName = resolveInfo.activityInfo.getComponentName();
                    i = resolveInfo.priority;
                } else if (i2 == i) {
                }
            }
        }
    }

    public final int getPhoneLockscreenState() {
        if (isDozing()) {
            return 1;
        }
        if (isBouncerShowing()) {
            return 3;
        }
        return isKeyguardLocked() ? 2 : 4;
    }

    public final int getPhoneLauncherState() {
        if (isLauncherInOverview()) {
            return 6;
        }
        return isLauncherInAllApps() ? 7 : 5;
    }

    public final boolean isShadeFullscreen() {
        int state = this.mStatusBarStateController.getState();
        return state == 1 || state == 2;
    }

    public final boolean isDozing() {
        return this.mStatusBarStateController.isDozing();
    }

    public final boolean isLauncherShowing(ActivityManager.RunningTaskInfo runningTaskInfo) {
        ComponentName componentName;
        if (runningTaskInfo == null || (componentName = runningTaskInfo.topActivity) == null) {
            return false;
        }
        return componentName.equals(this.mDefaultHome);
    }

    public final boolean isBouncerShowing() {
        return ((Boolean) this.mCentralSurfacesOptionalLazy.get().map(new PhoneStateMonitor$$ExternalSyntheticLambda1()).orElse(Boolean.FALSE)).booleanValue();
    }

    public final boolean isKeyguardLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) this.mContext.getSystemService(KeyguardManager.class);
        return keyguardManager != null && keyguardManager.isKeyguardLocked();
    }
}
