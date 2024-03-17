package com.android.systemui.dump;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DumpsysTableLogger.kt */
public final class DumpsysTableLogger {
    @NotNull
    public final List<String> columns;
    @NotNull
    public final List<List<String>> rows;
    @NotNull
    public final String sectionName;

    public DumpsysTableLogger(@NotNull String str, @NotNull List<String> list, @NotNull List<? extends List<String>> list2) {
        this.sectionName = str;
        this.columns = list;
        this.rows = list2;
    }

    public final void printTableData(@NotNull PrintWriter printWriter) {
        printSectionStart(printWriter);
        printSchema(printWriter);
        printData(printWriter);
        printSectionEnd(printWriter);
    }

    public final void printSectionStart(PrintWriter printWriter) {
        printWriter.println(Intrinsics.stringPlus("SystemUI TableSection START: ", this.sectionName));
        printWriter.println("version 1");
    }

    public final void printSectionEnd(PrintWriter printWriter) {
        printWriter.println(Intrinsics.stringPlus("SystemUI TableSection END: ", this.sectionName));
    }

    public final void printSchema(PrintWriter printWriter) {
        printWriter.println(CollectionsKt___CollectionsKt.joinToString$default(this.columns, "|", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
    }

    public final void printData(PrintWriter printWriter) {
        int size = this.columns.size();
        ArrayList<List> arrayList = new ArrayList<>();
        for (Object next : this.rows) {
            if (((List) next).size() == size) {
                arrayList.add(next);
            }
        }
        for (List joinToString$default : arrayList) {
            printWriter.println(CollectionsKt___CollectionsKt.joinToString$default(joinToString$default, "|", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
        }
    }
}
