package com.android.systemui.dump;

import android.content.Context;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.time.SystemClock;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import org.jetbrains.annotations.NotNull;

/* compiled from: LogBufferEulogizer.kt */
public final class LogBufferEulogizer {
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final Files files;
    @NotNull
    public final Path logPath;
    public final long maxLogAgeToDump;
    public final long minWriteGap;
    @NotNull
    public final SystemClock systemClock;

    public LogBufferEulogizer(@NotNull DumpManager dumpManager2, @NotNull SystemClock systemClock2, @NotNull Files files2, @NotNull Path path, long j, long j2) {
        this.dumpManager = dumpManager2;
        this.systemClock = systemClock2;
        this.files = files2;
        this.logPath = path;
        this.minWriteGap = j;
        this.maxLogAgeToDump = j2;
    }

    public LogBufferEulogizer(@NotNull Context context, @NotNull DumpManager dumpManager2, @NotNull SystemClock systemClock2, @NotNull Files files2) {
        this(dumpManager2, systemClock2, files2, Paths.get(context.getFilesDir().toPath().toString(), new String[]{"log_buffers.txt"}), LogBufferEulogizerKt.MIN_WRITE_GAP, LogBufferEulogizerKt.MAX_AGE_TO_DUMP);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x00a0, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r7, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00a4, code lost:
        throw r2;
     */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <T extends java.lang.Exception> T record(@org.jetbrains.annotations.NotNull T r15) {
        /*
            r14 = this;
            java.lang.String r0 = "ms"
            java.lang.String r1 = "Buffer eulogy took "
            com.android.systemui.util.time.SystemClock r2 = r14.systemClock
            long r2 = r2.uptimeMillis()
            java.lang.String r4 = "BufferEulogizer"
            java.lang.String r5 = "Performing emergency dump of log buffers"
            android.util.Log.i(r4, r5)
            java.nio.file.Path r5 = r14.logPath
            long r5 = r14.getMillisSinceLastWrite(r5)
            long r7 = r14.minWriteGap
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r7 >= 0) goto L_0x0037
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r0 = "Cannot dump logs, last write was only "
            r14.append(r0)
            r14.append(r5)
            java.lang.String r0 = " ms ago"
            r14.append(r0)
            java.lang.String r14 = r14.toString()
            android.util.Log.w(r4, r14)
            return r15
        L_0x0037:
            r5 = 0
            com.android.systemui.util.io.Files r7 = r14.files     // Catch:{ Exception -> 0x00a5 }
            java.nio.file.Path r8 = r14.logPath     // Catch:{ Exception -> 0x00a5 }
            r9 = 2
            java.nio.file.OpenOption[] r9 = new java.nio.file.OpenOption[r9]     // Catch:{ Exception -> 0x00a5 }
            java.nio.file.StandardOpenOption r10 = java.nio.file.StandardOpenOption.CREATE     // Catch:{ Exception -> 0x00a5 }
            r11 = 0
            r9[r11] = r10     // Catch:{ Exception -> 0x00a5 }
            r10 = 1
            java.nio.file.StandardOpenOption r12 = java.nio.file.StandardOpenOption.TRUNCATE_EXISTING     // Catch:{ Exception -> 0x00a5 }
            r9[r10] = r12     // Catch:{ Exception -> 0x00a5 }
            java.io.BufferedWriter r7 = r7.newBufferedWriter(r8, r9)     // Catch:{ Exception -> 0x00a5 }
            r8 = 0
            java.io.PrintWriter r9 = new java.io.PrintWriter     // Catch:{ all -> 0x009e }
            r9.<init>(r7)     // Catch:{ all -> 0x009e }
            java.text.SimpleDateFormat r10 = com.android.systemui.dump.LogBufferEulogizerKt.DATE_FORMAT     // Catch:{ all -> 0x009e }
            com.android.systemui.util.time.SystemClock r12 = r14.systemClock     // Catch:{ all -> 0x009e }
            long r12 = r12.currentTimeMillis()     // Catch:{ all -> 0x009e }
            java.lang.Long r12 = java.lang.Long.valueOf(r12)     // Catch:{ all -> 0x009e }
            java.lang.String r10 = r10.format(r12)     // Catch:{ all -> 0x009e }
            r9.println(r10)     // Catch:{ all -> 0x009e }
            r9.println()     // Catch:{ all -> 0x009e }
            java.lang.String r10 = "Dump triggered by exception:"
            r9.println(r10)     // Catch:{ all -> 0x009e }
            r15.printStackTrace(r9)     // Catch:{ all -> 0x009e }
            com.android.systemui.dump.DumpManager r10 = r14.dumpManager     // Catch:{ all -> 0x009e }
            r10.dumpBuffers(r9, r11)     // Catch:{ all -> 0x009e }
            com.android.systemui.util.time.SystemClock r14 = r14.systemClock     // Catch:{ all -> 0x009e }
            long r5 = r14.uptimeMillis()     // Catch:{ all -> 0x009e }
            long r5 = r5 - r2
            r9.println()     // Catch:{ all -> 0x009e }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x009e }
            r14.<init>()     // Catch:{ all -> 0x009e }
            r14.append(r1)     // Catch:{ all -> 0x009e }
            r14.append(r5)     // Catch:{ all -> 0x009e }
            r14.append(r0)     // Catch:{ all -> 0x009e }
            java.lang.String r14 = r14.toString()     // Catch:{ all -> 0x009e }
            r9.println(r14)     // Catch:{ all -> 0x009e }
            kotlin.Unit r14 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x009e }
            kotlin.io.CloseableKt.closeFinally(r7, r8)     // Catch:{ Exception -> 0x00a5 }
            goto L_0x00ab
        L_0x009e:
            r14 = move-exception
            throw r14     // Catch:{ all -> 0x00a0 }
        L_0x00a0:
            r2 = move-exception
            kotlin.io.CloseableKt.closeFinally(r7, r14)     // Catch:{ Exception -> 0x00a5 }
            throw r2     // Catch:{ Exception -> 0x00a5 }
        L_0x00a5:
            r14 = move-exception
            java.lang.String r2 = "Exception while attempting to dump buffers, bailing"
            android.util.Log.e(r4, r2, r14)
        L_0x00ab:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r14.append(r1)
            r14.append(r5)
            r14.append(r0)
            java.lang.String r14 = r14.toString()
            android.util.Log.i(r4, r14)
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.LogBufferEulogizer.record(java.lang.Exception):java.lang.Exception");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0059, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        kotlin.jdk7.AutoCloseableKt.closeFinally(r5, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void readEulogyIfPresent(@org.jetbrains.annotations.NotNull java.io.PrintWriter r6) {
        /*
            r5 = this;
            java.lang.String r0 = "BufferEulogizer"
            java.nio.file.Path r1 = r5.logPath     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            long r1 = r5.getMillisSinceLastWrite(r1)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            long r3 = r5.maxLogAgeToDump     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            int r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0030
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            r5.<init>()     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.lang.String r6 = "Not eulogizing buffers; they are "
            r5.append(r6)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.util.concurrent.TimeUnit r6 = java.util.concurrent.TimeUnit.HOURS     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            long r1 = r6.convert(r1, r3)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            r5.append(r1)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.lang.String r6 = " hours old"
            r5.append(r6)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            android.util.Log.i(r0, r5)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            return
        L_0x0030:
            com.android.systemui.util.io.Files r1 = r5.files     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.nio.file.Path r5 = r5.logPath     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.util.stream.Stream r5 = r1.lines(r5)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            java.lang.AutoCloseable r5 = (java.lang.AutoCloseable) r5     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            r1 = 0
            r2 = r5
            java.util.stream.Stream r2 = (java.util.stream.Stream) r2     // Catch:{ all -> 0x0057 }
            r6.println()     // Catch:{ all -> 0x0057 }
            r6.println()     // Catch:{ all -> 0x0057 }
            java.lang.String r3 = "=============== BUFFERS FROM MOST RECENT CRASH ==============="
            r6.println(r3)     // Catch:{ all -> 0x0057 }
            com.android.systemui.dump.LogBufferEulogizer$readEulogyIfPresent$1$1 r3 = new com.android.systemui.dump.LogBufferEulogizer$readEulogyIfPresent$1$1     // Catch:{ all -> 0x0057 }
            r3.<init>(r6)     // Catch:{ all -> 0x0057 }
            r2.forEach(r3)     // Catch:{ all -> 0x0057 }
            kotlin.Unit r6 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0057 }
            kotlin.jdk7.AutoCloseableKt.closeFinally(r5, r1)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            goto L_0x0064
        L_0x0057:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x0059 }
        L_0x0059:
            r1 = move-exception
            kotlin.jdk7.AutoCloseableKt.closeFinally(r5, r6)     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
            throw r1     // Catch:{ IOException -> 0x0064, UncheckedIOException -> 0x005e }
        L_0x005e:
            r5 = move-exception
            java.lang.String r6 = "UncheckedIOException while dumping the core"
            android.util.Log.e(r0, r6, r5)
        L_0x0064:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.LogBufferEulogizer.readEulogyIfPresent(java.io.PrintWriter):void");
    }

    public final long getMillisSinceLastWrite(Path path) {
        BasicFileAttributes basicFileAttributes;
        FileTime lastModifiedTime;
        try {
            basicFileAttributes = this.files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
        } catch (IOException unused) {
            basicFileAttributes = null;
        }
        long currentTimeMillis = this.systemClock.currentTimeMillis();
        long j = 0;
        if (!(basicFileAttributes == null || (lastModifiedTime = basicFileAttributes.lastModifiedTime()) == null)) {
            j = lastModifiedTime.toMillis();
        }
        return currentTimeMillis - j;
    }
}
