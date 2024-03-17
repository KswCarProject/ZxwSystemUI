package com.android.systemui.statusbar.commandline;

import android.content.Context;
import com.android.systemui.Prefs;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: CommandRegistry.kt */
public final class PrefsCommand implements Command {
    @NotNull
    public final Context context;

    public PrefsCommand(@NotNull Context context2) {
        this.context = context2;
    }

    public void help(@NotNull PrintWriter printWriter) {
        printWriter.println("usage: prefs <command> [args]");
        printWriter.println("Available commands:");
        printWriter.println("  list-prefs");
        printWriter.println("  set-pref <pref name> <value>");
    }

    public void execute(@NotNull PrintWriter printWriter, @NotNull List<String> list) {
        if (list.isEmpty()) {
            help(printWriter);
        } else if (Intrinsics.areEqual((Object) list.get(0), (Object) "list-prefs")) {
            listPrefs(printWriter);
        } else {
            help(printWriter);
        }
    }

    public final void listPrefs(PrintWriter printWriter) {
        printWriter.println("Available keys:");
        Field[] declaredFields = Prefs.Key.class.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            i++;
            printWriter.print("  ");
            printWriter.println(field.get(Prefs.Key.class));
        }
    }
}
