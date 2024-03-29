package com.android.systemui.dump;

import android.content.Context;
import android.os.SystemClock;
import android.os.Trace;
import com.android.systemui.CoreStartable;
import com.android.systemui.R$array;
import com.android.systemui.R$string;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Provider;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DumpHandler.kt */
public final class DumpHandler {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final Context context;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final LogBufferEulogizer logBufferEulogizer;
    @NotNull
    public final Map<Class<?>, Provider<CoreStartable>> startables;
    @NotNull
    public final UncaughtExceptionPreHandlerManager uncaughtExceptionPreHandlerManager;

    public DumpHandler(@NotNull Context context2, @NotNull DumpManager dumpManager2, @NotNull LogBufferEulogizer logBufferEulogizer2, @NotNull Map<Class<?>, Provider<CoreStartable>> map, @NotNull UncaughtExceptionPreHandlerManager uncaughtExceptionPreHandlerManager2) {
        this.context = context2;
        this.dumpManager = dumpManager2;
        this.logBufferEulogizer = logBufferEulogizer2;
        this.startables = map;
        this.uncaughtExceptionPreHandlerManager = uncaughtExceptionPreHandlerManager2;
    }

    public final void init() {
        this.uncaughtExceptionPreHandlerManager.registerHandler(new DumpHandler$init$1(this));
    }

    public final void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        Trace.beginSection("DumpManager#dump()");
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            ParsedArgs parseArgs = parseArgs(strArr);
            String dumpPriority = parseArgs.getDumpPriority();
            if (Intrinsics.areEqual((Object) dumpPriority, (Object) "CRITICAL")) {
                dumpCritical(printWriter, parseArgs);
            } else if (Intrinsics.areEqual((Object) dumpPriority, (Object) "NORMAL")) {
                dumpNormal(printWriter, parseArgs);
            } else {
                dumpParameterized(printWriter, parseArgs);
            }
            printWriter.println();
            printWriter.println("Dump took " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms");
            Trace.endSection();
        } catch (ArgParseException e) {
            printWriter.println(e.getMessage());
        }
    }

    public final void dumpParameterized(PrintWriter printWriter, ParsedArgs parsedArgs) {
        String command = parsedArgs.getCommand();
        if (command != null) {
            switch (command.hashCode()) {
                case -1354792126:
                    if (command.equals("config")) {
                        dumpConfig(printWriter);
                        return;
                    }
                    break;
                case -1353714459:
                    if (command.equals("dumpables")) {
                        dumpDumpables(printWriter, parsedArgs);
                        return;
                    }
                    break;
                case -1045369428:
                    if (command.equals("bugreport-normal")) {
                        dumpNormal(printWriter, parsedArgs);
                        return;
                    }
                    break;
                case 3198785:
                    if (command.equals("help")) {
                        dumpHelp(printWriter);
                        return;
                    }
                    break;
                case 227996723:
                    if (command.equals("buffers")) {
                        dumpBuffers(printWriter, parsedArgs);
                        return;
                    }
                    break;
                case 842828580:
                    if (command.equals("bugreport-critical")) {
                        dumpCritical(printWriter, parsedArgs);
                        return;
                    }
                    break;
            }
        }
        dumpTargets(parsedArgs.getNonFlagArgs(), printWriter, parsedArgs);
    }

    public final void dumpCritical(PrintWriter printWriter, ParsedArgs parsedArgs) {
        this.dumpManager.dumpDumpables(printWriter, parsedArgs.getRawArgs());
        dumpConfig(printWriter);
    }

    public final void dumpNormal(PrintWriter printWriter, ParsedArgs parsedArgs) {
        this.dumpManager.dumpBuffers(printWriter, parsedArgs.getTailLength());
        this.logBufferEulogizer.readEulogyIfPresent(printWriter);
    }

    public final void dumpDumpables(PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (parsedArgs.getListOnly()) {
            this.dumpManager.listDumpables(printWriter);
        } else {
            this.dumpManager.dumpDumpables(printWriter, parsedArgs.getRawArgs());
        }
    }

    public final void dumpBuffers(PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (parsedArgs.getListOnly()) {
            this.dumpManager.listBuffers(printWriter);
        } else {
            this.dumpManager.dumpBuffers(printWriter, parsedArgs.getTailLength());
        }
    }

    public final void dumpTargets(List<String> list, PrintWriter printWriter, ParsedArgs parsedArgs) {
        if (!list.isEmpty()) {
            for (String dumpTarget : list) {
                this.dumpManager.dumpTarget(dumpTarget, printWriter, parsedArgs.getRawArgs(), parsedArgs.getTailLength());
            }
        } else if (parsedArgs.getListOnly()) {
            printWriter.println("Dumpables:");
            this.dumpManager.listDumpables(printWriter);
            printWriter.println();
            printWriter.println("Buffers:");
            this.dumpManager.listBuffers(printWriter);
        } else {
            printWriter.println("Nothing to dump :(");
        }
    }

    public final void dumpConfig(PrintWriter printWriter) {
        printWriter.println("SystemUiServiceComponents configuration:");
        printWriter.print("vendor component: ");
        printWriter.println(this.context.getResources().getString(R$string.config_systemUIVendorServiceComponent));
        Iterable<Class> keySet = this.startables.keySet();
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(keySet, 10));
        for (Class simpleName : keySet) {
            arrayList.add(simpleName.getSimpleName());
        }
        List mutableList = CollectionsKt___CollectionsKt.toMutableList(arrayList);
        mutableList.add(this.context.getResources().getString(R$string.config_systemUIVendorServiceComponent));
        Object[] array = mutableList.toArray(new String[0]);
        if (array != null) {
            dumpServiceList(printWriter, "global", (String[]) array);
            dumpServiceList(printWriter, "per-user", R$array.config_systemUIServiceComponentsPerUser);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
    }

    public final void dumpServiceList(PrintWriter printWriter, String str, int i) {
        dumpServiceList(printWriter, str, this.context.getResources().getStringArray(i));
    }

    public final void dumpServiceList(PrintWriter printWriter, String str, String[] strArr) {
        printWriter.print(str);
        printWriter.print(": ");
        if (strArr == null) {
            printWriter.println("N/A");
            return;
        }
        printWriter.print(strArr.length);
        printWriter.println(" services");
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            printWriter.print("  ");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.println(strArr[i]);
        }
    }

    public final void dumpHelp(PrintWriter printWriter) {
        printWriter.println("Let <invocation> be:");
        printWriter.println("$ adb shell dumpsys activity service com.android.systemui/.SystemUIService");
        printWriter.println();
        printWriter.println("Most common usage:");
        printWriter.println("$ <invocation> <targets>");
        printWriter.println("$ <invocation> NotifLog");
        printWriter.println("$ <invocation> StatusBar FalsingManager BootCompleteCacheImpl");
        printWriter.println("etc.");
        printWriter.println();
        printWriter.println("Special commands:");
        printWriter.println("$ <invocation> dumpables");
        printWriter.println("$ <invocation> buffers");
        printWriter.println("$ <invocation> bugreport-critical");
        printWriter.println("$ <invocation> bugreport-normal");
        printWriter.println();
        printWriter.println("Targets can be listed:");
        printWriter.println("$ <invocation> --list");
        printWriter.println("$ <invocation> dumpables --list");
        printWriter.println("$ <invocation> buffers --list");
        printWriter.println();
        printWriter.println("Show only the most recent N lines of buffers");
        printWriter.println("$ <invocation> NotifLog --tail 30");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0067, code lost:
        r1.setTailLength(((java.lang.Number) readArgument(r9, r2, com.android.systemui.dump.DumpHandler$parseArgs$2.INSTANCE)).intValue());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007f, code lost:
        r1.setListOnly(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x008b, code lost:
        r1.setCommand("help");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x009d, code lost:
        throw new com.android.systemui.dump.ArgParseException(kotlin.jvm.internal.Intrinsics.stringPlus("Unknown flag: ", r2));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.dump.ParsedArgs parseArgs(java.lang.String[] r9) {
        /*
            r8 = this;
            java.util.List r0 = kotlin.collections.ArraysKt___ArraysKt.toMutableList((T[]) r9)
            com.android.systemui.dump.ParsedArgs r1 = new com.android.systemui.dump.ParsedArgs
            r1.<init>(r9, r0)
            java.util.Iterator r9 = r0.iterator()
        L_0x000d:
            boolean r2 = r9.hasNext()
            r3 = 1
            r4 = 0
            if (r2 == 0) goto L_0x009e
            java.lang.Object r2 = r9.next()
            java.lang.String r2 = (java.lang.String) r2
            r5 = 2
            r6 = 0
            java.lang.String r7 = "-"
            boolean r4 = kotlin.text.StringsKt__StringsJVMKt.startsWith$default(r2, r7, r4, r5, r6)
            if (r4 == 0) goto L_0x000d
            r9.remove()
            int r4 = r2.hashCode()
            switch(r4) {
                case 1499: goto L_0x0083;
                case 1503: goto L_0x0077;
                case 1511: goto L_0x005f;
                case 1056887741: goto L_0x004b;
                case 1333069025: goto L_0x0042;
                case 1333192254: goto L_0x0039;
                case 1333422576: goto L_0x0030;
                default: goto L_0x002f;
            }
        L_0x002f:
            goto L_0x0092
        L_0x0030:
            java.lang.String r3 = "--tail"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0092
            goto L_0x0067
        L_0x0039:
            java.lang.String r4 = "--list"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x0092
            goto L_0x007f
        L_0x0042:
            java.lang.String r3 = "--help"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0092
            goto L_0x008b
        L_0x004b:
            java.lang.String r3 = "--dump-priority"
            boolean r4 = r2.equals(r3)
            if (r4 == 0) goto L_0x0092
            com.android.systemui.dump.DumpHandler$parseArgs$1 r2 = com.android.systemui.dump.DumpHandler$parseArgs$1.INSTANCE
            java.lang.Object r2 = r8.readArgument(r9, r3, r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setDumpPriority(r2)
            goto L_0x000d
        L_0x005f:
            java.lang.String r3 = "-t"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0092
        L_0x0067:
            com.android.systemui.dump.DumpHandler$parseArgs$2 r3 = com.android.systemui.dump.DumpHandler$parseArgs$2.INSTANCE
            java.lang.Object r2 = r8.readArgument(r9, r2, r3)
            java.lang.Number r2 = (java.lang.Number) r2
            int r2 = r2.intValue()
            r1.setTailLength(r2)
            goto L_0x000d
        L_0x0077:
            java.lang.String r4 = "-l"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x0092
        L_0x007f:
            r1.setListOnly(r3)
            goto L_0x000d
        L_0x0083:
            java.lang.String r3 = "-h"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0092
        L_0x008b:
            java.lang.String r2 = "help"
            r1.setCommand(r2)
            goto L_0x000d
        L_0x0092:
            com.android.systemui.dump.ArgParseException r8 = new com.android.systemui.dump.ArgParseException
            java.lang.String r9 = "Unknown flag: "
            java.lang.String r9 = kotlin.jvm.internal.Intrinsics.stringPlus(r9, r2)
            r8.<init>(r9)
            throw r8
        L_0x009e:
            java.lang.String r8 = r1.getCommand()
            if (r8 != 0) goto L_0x00c5
            r8 = r0
            java.util.Collection r8 = (java.util.Collection) r8
            boolean r8 = r8.isEmpty()
            r8 = r8 ^ r3
            if (r8 == 0) goto L_0x00c5
            java.lang.String[] r8 = com.android.systemui.dump.DumpHandlerKt.COMMANDS
            java.lang.Object r9 = r0.get(r4)
            boolean r8 = kotlin.collections.ArraysKt___ArraysKt.contains((T[]) r8, r9)
            if (r8 == 0) goto L_0x00c5
            java.lang.Object r8 = r0.remove(r4)
            java.lang.String r8 = (java.lang.String) r8
            r1.setCommand(r8)
        L_0x00c5:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.DumpHandler.parseArgs(java.lang.String[]):com.android.systemui.dump.ParsedArgs");
    }

    public final <T> T readArgument(Iterator<String> it, String str, Function1<? super String, ? extends T> function1) {
        if (it.hasNext()) {
            String next = it.next();
            try {
                T invoke = function1.invoke(next);
                it.remove();
                return invoke;
            } catch (Exception unused) {
                throw new ArgParseException("Invalid argument '" + next + "' for flag " + str);
            }
        } else {
            throw new ArgParseException(Intrinsics.stringPlus("Missing argument for ", str));
        }
    }

    /* compiled from: DumpHandler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
