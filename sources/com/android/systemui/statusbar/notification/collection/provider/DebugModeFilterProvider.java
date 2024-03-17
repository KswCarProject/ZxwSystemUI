package com.android.systemui.statusbar.notification.collection.provider;

import android.os.Build;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.Assert;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.ListenerSetKt;
import java.io.PrintWriter;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DebugModeFilterProvider.kt */
public final class DebugModeFilterProvider implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public List<String> allowedPackages = CollectionsKt__CollectionsKt.emptyList();
    @NotNull
    public final CommandRegistry commandRegistry;
    @NotNull
    public final ListenerSet<Runnable> listeners = new ListenerSet<>();

    public DebugModeFilterProvider(@NotNull CommandRegistry commandRegistry2, @NotNull DumpManager dumpManager) {
        this.commandRegistry = commandRegistry2;
        dumpManager.registerDumpable(this);
    }

    public final void registerInvalidationListener(@NotNull Runnable runnable) {
        Assert.isMainThread();
        if (Build.isDebuggable()) {
            boolean isEmpty = this.listeners.isEmpty();
            this.listeners.addIfAbsent(runnable);
            if (isEmpty) {
                this.commandRegistry.registerCommand("notif-filter", new DebugModeFilterProvider$registerInvalidationListener$1(this));
                Log.d("DebugModeFilterProvider", "Registered notif-filter command");
            }
        }
    }

    public final boolean shouldFilterOut(@NotNull NotificationEntry notificationEntry) {
        if (this.allowedPackages.isEmpty()) {
            return false;
        }
        return !this.allowedPackages.contains(notificationEntry.getSbn().getPackageName());
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("initialized: ", Boolean.valueOf(ListenerSetKt.isNotEmpty(this.listeners))));
        printWriter.println(Intrinsics.stringPlus("allowedPackages: ", Integer.valueOf(this.allowedPackages.size())));
        int i = 0;
        for (Object next : this.allowedPackages) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            printWriter.println("  [" + i + "]: " + ((String) next));
            i = i2;
        }
    }

    /* compiled from: DebugModeFilterProvider.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    /* compiled from: DebugModeFilterProvider.kt */
    public final class NotifFilterCommand implements Command {
        public NotifFilterCommand() {
        }

        public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
            String str = (String) CollectionsKt___CollectionsKt.firstOrNull(list);
            if (Intrinsics.areEqual((Object) str, (Object) "reset")) {
                if (list.size() > 1) {
                    invalidCommand(printWriter, "Unexpected arguments for 'reset' command");
                    return;
                }
                DebugModeFilterProvider.this.allowedPackages = CollectionsKt__CollectionsKt.emptyList();
            } else if (Intrinsics.areEqual((Object) str, (Object) "allowed-pkgs")) {
                DebugModeFilterProvider.this.allowedPackages = CollectionsKt___CollectionsKt.drop(list, 1);
            } else if (str == null) {
                invalidCommand(printWriter, "Missing command");
                return;
            } else {
                invalidCommand(printWriter, Intrinsics.stringPlus("Unknown command: ", CollectionsKt___CollectionsKt.firstOrNull(list)));
                return;
            }
            Log.d("DebugModeFilterProvider", Intrinsics.stringPlus("Updated allowedPackages: ", DebugModeFilterProvider.this.allowedPackages));
            if (DebugModeFilterProvider.this.allowedPackages.isEmpty()) {
                printWriter.print("Resetting allowedPackages ... ");
            } else {
                printWriter.print("Updating allowedPackages: " + DebugModeFilterProvider.this.allowedPackages + " ... ");
            }
            for (Runnable run : DebugModeFilterProvider.this.listeners) {
                run.run();
            }
            printWriter.println("DONE");
        }

        public final void invalidCommand(PrintWriter printWriter, String str) {
            printWriter.println(Intrinsics.stringPlus("Error: ", str));
            printWriter.println();
            help(printWriter);
        }

        public void help(@NotNull PrintWriter printWriter) {
            printWriter.println("Usage: adb shell cmd statusbar notif-filter <command>");
            printWriter.println("Available commands:");
            printWriter.println("  reset");
            printWriter.println("     Restore the default system behavior.");
            printWriter.println("  allowed-pkgs <package> ...");
            printWriter.println("     Hide all notification except from packages listed here.");
            printWriter.println("     Providing no packages is treated as a reset.");
        }
    }
}
