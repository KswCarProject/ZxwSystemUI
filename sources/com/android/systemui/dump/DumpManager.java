package com.android.systemui.dump;

import android.util.ArrayMap;
import com.android.systemui.Dumpable;
import com.android.systemui.log.LogBuffer;
import java.io.PrintWriter;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: DumpManager.kt */
public class DumpManager {
    @NotNull
    public final Map<String, RegisteredDumpable<LogBuffer>> buffers = new ArrayMap();
    @NotNull
    public final Map<String, RegisteredDumpable<Dumpable>> dumpables = new ArrayMap();

    public final synchronized void registerDumpable(@NotNull String str, @NotNull Dumpable dumpable) {
        if (canAssignToNameLocked(str, dumpable)) {
            this.dumpables.put(str, new RegisteredDumpable(str, dumpable));
        } else {
            throw new IllegalArgumentException('\'' + str + "' is already registered");
        }
    }

    public final synchronized void registerDumpable(@NotNull Dumpable dumpable) {
        registerDumpable(dumpable.getClass().getSimpleName(), dumpable);
    }

    public final synchronized void unregisterDumpable(@NotNull String str) {
        this.dumpables.remove(str);
    }

    public final synchronized void registerBuffer(@NotNull String str, @NotNull LogBuffer logBuffer) {
        if (canAssignToNameLocked(str, logBuffer)) {
            this.buffers.put(str, new RegisteredDumpable(str, logBuffer));
        } else {
            throw new IllegalArgumentException('\'' + str + "' is already registered");
        }
    }

    public final synchronized void dumpTarget(@NotNull String str, @NotNull PrintWriter printWriter, @NotNull String[] strArr, int i) {
        for (RegisteredDumpable next : this.dumpables.values()) {
            if (StringsKt__StringsJVMKt.endsWith$default(next.getName(), str, false, 2, (Object) null)) {
                dumpDumpable(next, printWriter, strArr);
                return;
            }
        }
        for (RegisteredDumpable next2 : this.buffers.values()) {
            if (StringsKt__StringsJVMKt.endsWith$default(next2.getName(), str, false, 2, (Object) null)) {
                dumpBuffer(next2, printWriter, i);
                return;
            }
        }
    }

    public final synchronized void dumpDumpables(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        for (RegisteredDumpable<Dumpable> dumpDumpable : this.dumpables.values()) {
            dumpDumpable(dumpDumpable, printWriter, strArr);
        }
    }

    public final synchronized void listDumpables(@NotNull PrintWriter printWriter) {
        for (RegisteredDumpable<Dumpable> name : this.dumpables.values()) {
            printWriter.println(name.getName());
        }
    }

    public final synchronized void dumpBuffers(@NotNull PrintWriter printWriter, int i) {
        for (RegisteredDumpable<LogBuffer> dumpBuffer : this.buffers.values()) {
            dumpBuffer(dumpBuffer, printWriter, i);
        }
    }

    public final synchronized void listBuffers(@NotNull PrintWriter printWriter) {
        for (RegisteredDumpable<LogBuffer> name : this.buffers.values()) {
            printWriter.println(name.getName());
        }
    }

    public final synchronized void freezeBuffers() {
        for (RegisteredDumpable<LogBuffer> dumpable : this.buffers.values()) {
            ((LogBuffer) dumpable.getDumpable()).freeze();
        }
    }

    public final synchronized void unfreezeBuffers() {
        for (RegisteredDumpable<LogBuffer> dumpable : this.buffers.values()) {
            ((LogBuffer) dumpable.getDumpable()).unfreeze();
        }
    }

    public final void dumpDumpable(RegisteredDumpable<Dumpable> registeredDumpable, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println(Intrinsics.stringPlus(registeredDumpable.getName(), ":"));
        printWriter.println("----------------------------------------------------------------------------");
        registeredDumpable.getDumpable().dump(printWriter, strArr);
    }

    public final void dumpBuffer(RegisteredDumpable<LogBuffer> registeredDumpable, PrintWriter printWriter, int i) {
        printWriter.println();
        printWriter.println();
        printWriter.println("BUFFER " + registeredDumpable.getName() + ':');
        printWriter.println("============================================================================");
        registeredDumpable.getDumpable().dump(printWriter, i);
    }

    public final boolean canAssignToNameLocked(String str, Object obj) {
        RegisteredDumpable registeredDumpable = this.dumpables.get(str);
        Object obj2 = null;
        Object obj3 = registeredDumpable == null ? null : (Dumpable) registeredDumpable.getDumpable();
        if (obj3 == null) {
            RegisteredDumpable registeredDumpable2 = this.buffers.get(str);
            if (registeredDumpable2 != null) {
                obj2 = (LogBuffer) registeredDumpable2.getDumpable();
            }
        } else {
            obj2 = obj3;
        }
        return obj2 == null || Intrinsics.areEqual(obj, obj2);
    }
}
