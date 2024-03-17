package com.android.systemui;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.os.BinderInternal;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class SystemUIService extends Service {
    public final BatteryStateNotifier mBatteryStateNotifier;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final DumpHandler mDumpHandler;
    public final LogBufferFreezer mLogBufferFreezer;
    public final Handler mMainHandler;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public SystemUIService(Handler handler, DumpHandler dumpHandler, BroadcastDispatcher broadcastDispatcher, LogBufferFreezer logBufferFreezer, BatteryStateNotifier batteryStateNotifier) {
        this.mMainHandler = handler;
        this.mDumpHandler = dumpHandler;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mLogBufferFreezer = logBufferFreezer;
        this.mBatteryStateNotifier = batteryStateNotifier;
    }

    public void onCreate() {
        super.onCreate();
        ((SystemUIApplication) getApplication()).startServicesIfNeeded();
        this.mLogBufferFreezer.attach(this.mBroadcastDispatcher);
        this.mDumpHandler.init();
        if (getResources().getBoolean(R$bool.config_showNotificationForUnknownBatteryState)) {
            this.mBatteryStateNotifier.startListening();
        }
        if (!Build.IS_DEBUGGABLE || !SystemProperties.getBoolean("debug.crash_sysui", false)) {
            if (Build.IS_DEBUGGABLE) {
                BinderInternal.nSetBinderProxyCountEnabled(true);
                BinderInternal.nSetBinderProxyCountWatermarks(1000, 900);
                BinderInternal.setBinderProxyCountCallback(new BinderInternal.BinderProxyLimitListener() {
                    public void onLimitReached(int i) {
                        Slog.w("SystemUIService", "uid " + i + " sent too many Binder proxies to uid " + Process.myUid());
                    }
                }, this.mMainHandler);
            }
            startServiceAsUser(new Intent(getApplicationContext(), SystemUIAuxiliaryDumpService.class), UserHandle.SYSTEM);
            return;
        }
        throw new RuntimeException();
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (strArr.length == 0) {
            strArr = new String[]{"--dump-priority", "CRITICAL"};
        }
        this.mDumpHandler.dump(printWriter, strArr);
    }
}
