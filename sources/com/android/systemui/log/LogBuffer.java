package com.android.systemui.log;

import android.os.Trace;
import android.util.Log;
import com.android.systemui.util.collection.RingBuffer;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LogBuffer.kt */
public final class LogBuffer {
    @NotNull
    public final RingBuffer<LogMessageImpl> buffer;
    @Nullable
    public final BlockingQueue<LogMessage> echoMessageQueue;
    public boolean frozen;
    @NotNull
    public final LogcatEchoTracker logcatEchoTracker;
    public final int maxSize;
    @NotNull
    public final String name;
    public final boolean systrace;

    /* compiled from: LogBuffer.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[LogLevel.values().length];
            iArr[LogLevel.VERBOSE.ordinal()] = 1;
            iArr[LogLevel.DEBUG.ordinal()] = 2;
            iArr[LogLevel.INFO.ordinal()] = 3;
            iArr[LogLevel.WARNING.ordinal()] = 4;
            iArr[LogLevel.ERROR.ordinal()] = 5;
            iArr[LogLevel.WTF.ordinal()] = 6;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public LogBuffer(@NotNull String str, int i, @NotNull LogcatEchoTracker logcatEchoTracker2, boolean z) {
        this.name = str;
        this.maxSize = i;
        this.logcatEchoTracker = logcatEchoTracker2;
        this.systrace = z;
        this.buffer = new RingBuffer<>(i, LogBuffer$buffer$1.INSTANCE);
        ArrayBlockingQueue arrayBlockingQueue = logcatEchoTracker2.getLogInBackgroundThread() ? new ArrayBlockingQueue(10) : null;
        this.echoMessageQueue = arrayBlockingQueue;
        if (logcatEchoTracker2.getLogInBackgroundThread() && arrayBlockingQueue != null) {
            ThreadsKt.thread$default(true, false, (ClassLoader) null, Intrinsics.stringPlus("LogBuffer-", str), 5, new Function0<Unit>(this) {
                public final /* synthetic */ LogBuffer this$0;

                {
                    this.this$0 = r1;
                }

                public final void invoke() {
                    while (true) {
                        try {
                            LogBuffer logBuffer = this.this$0;
                            logBuffer.echoToDesiredEndpoints((LogMessage) logBuffer.echoMessageQueue.take());
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }, 6, (Object) null);
        }
    }

    public final boolean getMutable() {
        return !this.frozen && this.maxSize > 0;
    }

    @NotNull
    public final synchronized LogMessageImpl obtain(@NotNull String str, @NotNull LogLevel logLevel, @NotNull Function1<? super LogMessage, String> function1) {
        if (!getMutable()) {
            return LogBufferKt.FROZEN_MESSAGE;
        }
        LogMessageImpl advance = this.buffer.advance();
        advance.reset(str, logLevel, System.currentTimeMillis(), function1);
        return advance;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:11|12|13|14) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0019 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void commit(@org.jetbrains.annotations.NotNull com.android.systemui.log.LogMessage r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.getMutable()     // Catch:{ all -> 0x0022 }
            if (r0 != 0) goto L_0x0009
            monitor-exit(r1)
            return
        L_0x0009:
            java.util.concurrent.BlockingQueue<com.android.systemui.log.LogMessage> r0 = r1.echoMessageQueue     // Catch:{ all -> 0x0022 }
            if (r0 == 0) goto L_0x001d
            int r0 = r0.remainingCapacity()     // Catch:{ all -> 0x0022 }
            if (r0 <= 0) goto L_0x001d
            java.util.concurrent.BlockingQueue<com.android.systemui.log.LogMessage> r0 = r1.echoMessageQueue     // Catch:{ InterruptedException -> 0x0019 }
            r0.put(r2)     // Catch:{ InterruptedException -> 0x0019 }
            goto L_0x0020
        L_0x0019:
            r1.echoToDesiredEndpoints(r2)     // Catch:{ all -> 0x0022 }
            goto L_0x0020
        L_0x001d:
            r1.echoToDesiredEndpoints(r2)     // Catch:{ all -> 0x0022 }
        L_0x0020:
            monitor-exit(r1)
            return
        L_0x0022:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.log.LogBuffer.commit(com.android.systemui.log.LogMessage):void");
    }

    public final void echoToDesiredEndpoints(LogMessage logMessage) {
        echo(logMessage, this.logcatEchoTracker.isBufferLoggable(this.name, logMessage.getLevel()) || this.logcatEchoTracker.isTagLoggable(logMessage.getTag(), logMessage.getLevel()), this.systrace);
    }

    public final synchronized void dump(@NotNull PrintWriter printWriter, int i) {
        int i2 = 0;
        if (i > 0) {
            i2 = Math.max(0, this.buffer.getSize() - i);
        }
        int size = this.buffer.getSize();
        while (i2 < size) {
            int i3 = i2 + 1;
            dumpMessage(this.buffer.get(i2), printWriter);
            i2 = i3;
        }
    }

    public final synchronized void freeze() {
        if (!this.frozen) {
            LogMessageImpl obtain = obtain("LogBuffer", LogLevel.DEBUG, LogBuffer$freeze$2.INSTANCE);
            obtain.setStr1(this.name);
            commit(obtain);
            this.frozen = true;
        }
    }

    public final synchronized void unfreeze() {
        if (this.frozen) {
            LogMessageImpl obtain = obtain("LogBuffer", LogLevel.DEBUG, LogBuffer$unfreeze$2.INSTANCE);
            obtain.setStr1(this.name);
            commit(obtain);
            this.frozen = false;
        }
    }

    public final void dumpMessage(LogMessage logMessage, PrintWriter printWriter) {
        printWriter.print(LogBufferKt.DATE_FORMAT.format(Long.valueOf(logMessage.getTimestamp())));
        printWriter.print(" ");
        printWriter.print(logMessage.getLevel().getShortString());
        printWriter.print(" ");
        printWriter.print(logMessage.getTag());
        printWriter.print(": ");
        printWriter.println(logMessage.getPrinter().invoke(logMessage));
    }

    public final void echo(LogMessage logMessage, boolean z, boolean z2) {
        if (z || z2) {
            String invoke = logMessage.getPrinter().invoke(logMessage);
            if (z2) {
                echoToSystrace(logMessage, invoke);
            }
            if (z) {
                echoToLogcat(logMessage, invoke);
            }
        }
    }

    public final void echoToSystrace(LogMessage logMessage, String str) {
        Trace.instantForTrack(4096, "UI Events", this.name + " - " + logMessage.getLevel().getShortString() + ' ' + logMessage.getTag() + ": " + str);
    }

    public final void echoToLogcat(LogMessage logMessage, String str) {
        switch (WhenMappings.$EnumSwitchMapping$0[logMessage.getLevel().ordinal()]) {
            case 1:
                Log.v(logMessage.getTag(), str);
                return;
            case 2:
                Log.d(logMessage.getTag(), str);
                return;
            case 3:
                Log.i(logMessage.getTag(), str);
                return;
            case 4:
                Log.w(logMessage.getTag(), str);
                return;
            case 5:
                Log.e(logMessage.getTag(), str);
                return;
            case 6:
                Log.wtf(logMessage.getTag(), str);
                return;
            default:
                return;
        }
    }
}
