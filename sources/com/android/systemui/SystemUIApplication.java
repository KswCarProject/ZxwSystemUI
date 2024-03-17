package com.android.systemui;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.Application;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Dumpable;
import android.util.DumpableContainer;
import android.util.Log;
import android.util.TimingsTraceLog;
import android.view.SurfaceControl;
import android.view.ThreadedRenderer;
import com.android.internal.protolog.common.ProtoLog;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.NotificationChannels;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Provider;

public class SystemUIApplication extends Application implements SystemUIAppComponentFactory.ContextInitializer, DumpableContainer {
    public BootCompleteCacheImpl mBootCompleteCache;
    public ContextComponentHelper mComponentHelper;
    public SystemUIAppComponentFactory.ContextAvailableCallback mContextAvailableCallback;
    public DumpManager mDumpManager;
    public final ArrayMap<String, Dumpable> mDumpables = new ArrayMap<>();
    public GlobalRootComponent mRootComponent;
    public CoreStartable[] mServices;
    public boolean mServicesStarted;
    public SysUIComponent mSysUIComponent;

    public SystemUIApplication() {
        Log.v("SystemUIService", "SystemUIApplication constructed.");
        ProtoLog.REQUIRE_PROTOLOGTOOL = false;
    }

    public void onCreate() {
        super.onCreate();
        Log.v("SystemUIService", "SystemUIApplication created.");
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096);
        timingsTraceLog.traceBegin("DependencyInjection");
        this.mContextAvailableCallback.onContextAvailable(this);
        this.mRootComponent = SystemUIFactory.getInstance().getRootComponent();
        SysUIComponent sysUIComponent = SystemUIFactory.getInstance().getSysUIComponent();
        this.mSysUIComponent = sysUIComponent;
        this.mComponentHelper = sysUIComponent.getContextComponentHelper();
        this.mBootCompleteCache = this.mSysUIComponent.provideBootCacheImpl();
        timingsTraceLog.traceEnd();
        Looper.getMainLooper().setTraceTag(4096);
        setTheme(R$style.Theme_SystemUI);
        if (Process.myUserHandle().equals(UserHandle.SYSTEM)) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
            intentFilter.setPriority(1000);
            int gPUContextPriority = SurfaceControl.getGPUContextPriority();
            Log.i("SystemUIService", "Found SurfaceFlinger's GPU Priority: " + gPUContextPriority);
            if (gPUContextPriority == ThreadedRenderer.EGL_CONTEXT_PRIORITY_REALTIME_NV) {
                Log.i("SystemUIService", "Setting SysUI's GPU Context priority to: " + ThreadedRenderer.EGL_CONTEXT_PRIORITY_HIGH_IMG);
                ThreadedRenderer.setContextPriority(ThreadedRenderer.EGL_CONTEXT_PRIORITY_HIGH_IMG);
            }
            try {
                ActivityManager.getService().enableBinderTracing();
            } catch (RemoteException e) {
                Log.e("SystemUIService", "Unable to enable binder tracing", e);
            }
            registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (!SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        if (Settings.System.getInt(SystemUIApplication.this.getContentResolver(), "airplane_mode_on", 0) == 0) {
                            SystemUIApplication.this.setAirplaneMode(false);
                        }
                        SystemUIApplication.this.unregisterReceiver(this);
                        SystemUIApplication.this.mBootCompleteCache.setBootComplete();
                        if (SystemUIApplication.this.mServicesStarted) {
                            for (CoreStartable onBootCompleted : SystemUIApplication.this.mServices) {
                                onBootCompleted.onBootCompleted();
                            }
                        }
                    }
                }
            }, intentFilter);
            registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction()) && SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        NotificationChannels.createAll(context);
                    }
                }
            }, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
            return;
        }
        String currentProcessName = ActivityThread.currentProcessName();
        ApplicationInfo applicationInfo = getApplicationInfo();
        if (currentProcessName != null) {
            if (currentProcessName.startsWith(applicationInfo.processName + ":")) {
                return;
            }
        }
        startSecondaryUserServicesIfNeeded();
    }

    public final void setAirplaneMode(boolean z) {
        Settings.Global.putInt(getContentResolver(), "airplane_mode_on", z ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.putExtra("state", z);
        sendBroadcast(intent);
    }

    public void startServicesIfNeeded() {
        String vendorComponent = SystemUIFactory.getInstance().getVendorComponent(getResources());
        TreeMap treeMap = new TreeMap(Comparator.comparing(new SystemUIApplication$$ExternalSyntheticLambda0()));
        treeMap.putAll(SystemUIFactory.getInstance().getStartableComponents());
        treeMap.putAll(SystemUIFactory.getInstance().getStartableComponentsPerUser());
        startServicesIfNeeded(treeMap, "StartServices", vendorComponent);
    }

    public void startSecondaryUserServicesIfNeeded() {
        TreeMap treeMap = new TreeMap(Comparator.comparing(new SystemUIApplication$$ExternalSyntheticLambda0()));
        treeMap.putAll(SystemUIFactory.getInstance().getStartableComponentsPerUser());
        startServicesIfNeeded(treeMap, "StartSecondaryServices", (String) null);
    }

    public final void startServicesIfNeeded(Map<Class<?>, Provider<CoreStartable>> map, String str, String str2) {
        if (!this.mServicesStarted) {
            this.mServices = new CoreStartable[(map.size() + (str2 == null ? 0 : 1))];
            if (!this.mBootCompleteCache.isBootComplete() && "1".equals(SystemProperties.get("sys.boot_completed"))) {
                this.mBootCompleteCache.setBootComplete();
            }
            this.mDumpManager = this.mSysUIComponent.createDumpManager();
            Log.v("SystemUIService", "Starting SystemUI services for user " + Process.myUserHandle().getIdentifier() + ".");
            TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096);
            timingsTraceLog.traceBegin(str);
            int i = 0;
            for (Map.Entry next : map.entrySet()) {
                String name = ((Class) next.getKey()).getName();
                timeInitialization(name, new SystemUIApplication$$ExternalSyntheticLambda2(this, i, name, next), timingsTraceLog, str);
                i++;
            }
            if (str2 != null) {
                timeInitialization(str2, new SystemUIApplication$$ExternalSyntheticLambda3(this, str2), timingsTraceLog, str);
            }
            for (int i2 = 0; i2 < this.mServices.length; i2++) {
                if (this.mBootCompleteCache.isBootComplete()) {
                    this.mServices[i2].onBootCompleted();
                }
                this.mDumpManager.registerDumpable(this.mServices[i2].getClass().getName(), this.mServices[i2]);
            }
            this.mSysUIComponent.getInitController().executePostInitTasks();
            timingsTraceLog.traceEnd();
            this.mServicesStarted = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startServicesIfNeeded$0(int i, String str, Map.Entry entry) {
        this.mServices[i] = startStartable(str, (Provider) entry.getValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startServicesIfNeeded$1(String str) {
        CoreStartable[] coreStartableArr = this.mServices;
        coreStartableArr[coreStartableArr.length - 1] = startAdditionalStartable(str);
    }

    public final void timeInitialization(String str, Runnable runnable, TimingsTraceLog timingsTraceLog, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        timingsTraceLog.traceBegin(str2 + " " + str);
        runnable.run();
        timingsTraceLog.traceEnd();
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (currentTimeMillis2 > 1000) {
            Log.w("SystemUIService", "Initialization of " + str + " took " + currentTimeMillis2 + " ms");
        }
    }

    public final CoreStartable startAdditionalStartable(String str) {
        try {
            return startStartable((CoreStartable) Class.forName(str).getConstructor(new Class[]{Context.class}).newInstance(new Object[]{this}));
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public final CoreStartable startStartable(String str, Provider<CoreStartable> provider) {
        return startStartable(provider.get());
    }

    public final CoreStartable startStartable(CoreStartable coreStartable) {
        coreStartable.start();
        return coreStartable;
    }

    public boolean addDumpable(Dumpable dumpable) {
        String dumpableName = dumpable.getDumpableName();
        if (this.mDumpables.containsKey(dumpableName)) {
            return false;
        }
        this.mDumpables.put(dumpableName, dumpable);
        this.mDumpManager.registerDumpable(dumpable.getDumpableName(), new SystemUIApplication$$ExternalSyntheticLambda1(dumpable));
        return true;
    }

    public boolean removeDumpable(Dumpable dumpable) {
        Log.w("SystemUIService", "removeDumpable(" + dumpable + "): not implemented");
        return false;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mServicesStarted) {
            this.mSysUIComponent.getConfigurationController().onConfigurationChanged(configuration);
            for (CoreStartable coreStartable : this.mServices) {
                if (coreStartable != null) {
                    coreStartable.onConfigurationChanged(configuration);
                }
            }
        }
    }

    public void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mContextAvailableCallback = contextAvailableCallback;
    }

    public static void overrideNotificationAppName(Context context, Notification.Builder builder, boolean z) {
        String str;
        Bundle bundle = new Bundle();
        if (z) {
            str = context.getString(17040872);
        } else {
            str = context.getString(17040871);
        }
        bundle.putString("android.substName", str);
        builder.addExtras(bundle);
    }
}
