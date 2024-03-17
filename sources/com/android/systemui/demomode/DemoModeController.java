package com.android.systemui.demomode;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.settings.GlobalSettings;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DemoModeController.kt */
public final class DemoModeController implements CallbackController<DemoMode>, Dumpable {
    @NotNull
    public final DemoModeController$broadcastReceiver$1 broadcastReceiver;
    @NotNull
    public final Context context;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final GlobalSettings globalSettings;
    public boolean initialized;
    public boolean isInDemoMode;
    @NotNull
    public final Map<String, List<DemoMode>> receiverMap;
    @NotNull
    public final List<DemoMode> receivers = new ArrayList();
    @NotNull
    public final DemoModeController$tracker$1 tracker;

    public DemoModeController(@NotNull Context context2, @NotNull DumpManager dumpManager2, @NotNull GlobalSettings globalSettings2) {
        this.context = context2;
        this.dumpManager = dumpManager2;
        this.globalSettings = globalSettings2;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterable<String> iterable = DemoMode.COMMANDS;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (String put : iterable) {
            arrayList.add((List) linkedHashMap.put(put, new ArrayList()));
        }
        this.receiverMap = linkedHashMap;
        this.tracker = new DemoModeController$tracker$1(this, this.context);
        this.broadcastReceiver = new DemoModeController$broadcastReceiver$1(this);
    }

    public final boolean isInDemoMode() {
        return this.isInDemoMode;
    }

    public final boolean isAvailable() {
        return this.tracker.isDemoModeAvailable();
    }

    public final void initialize() {
        if (!this.initialized) {
            this.initialized = true;
            this.dumpManager.registerDumpable("DemoModeController", this);
            this.tracker.startTracking();
            this.isInDemoMode = this.tracker.isInDemoMode();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.systemui.demo");
            this.context.registerReceiverAsUser(this.broadcastReceiver, UserHandle.ALL, intentFilter, "android.permission.DUMP", (Handler) null, 2);
            return;
        }
        throw new IllegalStateException("Already initialized");
    }

    public void addCallback(@NotNull DemoMode demoMode) {
        for (String str : demoMode.demoCommands()) {
            if (this.receiverMap.containsKey(str)) {
                List<DemoMode> list = this.receiverMap.get(str);
                Intrinsics.checkNotNull(list);
                list.add(demoMode);
            } else {
                throw new IllegalStateException("Command (" + str + ") not recognized. See DemoMode.java for valid commands");
            }
        }
        synchronized (this) {
            this.receivers.add(demoMode);
        }
        if (this.isInDemoMode) {
            demoMode.onDemoModeStarted();
        }
    }

    public void removeCallback(@NotNull DemoMode demoMode) {
        synchronized (this) {
            for (String str : demoMode.demoCommands()) {
                List<DemoMode> list = this.receiverMap.get(str);
                Intrinsics.checkNotNull(list);
                list.remove(demoMode);
            }
            this.receivers.remove(demoMode);
        }
    }

    public final void setIsDemoModeAllowed(boolean z) {
        if (this.isInDemoMode && !z) {
            requestFinishDemoMode();
        }
    }

    public final void enterDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = true;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver onDemoModeStarted : list) {
            onDemoModeStarted.onDemoModeStarted();
        }
    }

    public final void exitDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = false;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver onDemoModeFinished : list) {
            onDemoModeFinished.onDemoModeFinished();
        }
    }

    public final void dispatchDemoCommand(@NotNull String str, @NotNull Bundle bundle) {
        Assert.isMainThread();
        if (isAvailable()) {
            if (Intrinsics.areEqual((Object) str, (Object) "enter")) {
                enterDemoMode();
            } else if (Intrinsics.areEqual((Object) str, (Object) "exit")) {
                exitDemoMode();
            } else if (!this.isInDemoMode) {
                enterDemoMode();
            }
            List<DemoMode> list = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list);
            for (DemoMode dispatchDemoCommand : list) {
                dispatchDemoCommand.dispatchDemoCommand(str, bundle);
            }
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        List<DemoModeCommandReceiver> list;
        printWriter.println("DemoModeController state -");
        printWriter.println(Intrinsics.stringPlus("  isInDemoMode=", Boolean.valueOf(this.isInDemoMode)));
        printWriter.println(Intrinsics.stringPlus("  isDemoModeAllowed=", Boolean.valueOf(isAvailable())));
        printWriter.print("  receivers=[");
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            printWriter.print(Intrinsics.stringPlus(" ", demoModeCommandReceiver.getClass().getSimpleName()));
        }
        printWriter.println(" ]");
        printWriter.println("  receiverMap= [");
        for (String str : this.receiverMap.keySet()) {
            printWriter.print("    " + str + " : [");
            List<DemoMode> list2 = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list2);
            Iterable<DemoMode> iterable = list2;
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (DemoMode demoMode : iterable) {
                arrayList.add(demoMode.getClass().getSimpleName());
            }
            printWriter.println(Intrinsics.stringPlus(CollectionsKt___CollectionsKt.joinToString$default(arrayList, ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null), " ]"));
        }
    }

    public final void requestSetDemoModeAllowed(boolean z) {
        setGlobal("sysui_demo_allowed", z ? 1 : 0);
    }

    public final void requestStartDemoMode() {
        setGlobal("sysui_tuner_demo_on", 1);
    }

    public final void requestFinishDemoMode() {
        setGlobal("sysui_tuner_demo_on", 0);
    }

    public final void setGlobal(String str, int i) {
        this.globalSettings.putInt(str, i);
    }
}
