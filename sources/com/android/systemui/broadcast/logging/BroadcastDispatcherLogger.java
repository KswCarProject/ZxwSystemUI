package com.android.systemui.broadcast.logging;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final LogBuffer buffer;

    public BroadcastDispatcherLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    /* compiled from: BroadcastDispatcherLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final String flagToString(int i) {
            StringBuilder sb = new StringBuilder("");
            if ((i & 1) != 0) {
                sb.append("instant_apps,");
            }
            if ((i & 4) != 0) {
                sb.append("not_exported,");
            }
            if ((i & 2) != 0) {
                sb.append("exported");
            }
            if (sb.length() == 0) {
                sb.append(i);
            }
            return sb.toString();
        }
    }

    public final void logBroadcastReceived(int i, int i2, @NotNull Intent intent) {
        String intent2 = intent.toString();
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logBroadcastReceived$2 broadcastDispatcherLogger$logBroadcastReceived$2 = BroadcastDispatcherLogger$logBroadcastReceived$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logBroadcastReceived$2);
        obtain.setInt1(i);
        obtain.setInt2(i2);
        obtain.setStr1(intent2);
        logBuffer.commit(obtain);
    }

    public final void logBroadcastDispatched(int i, @Nullable String str, @NotNull BroadcastReceiver broadcastReceiver) {
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogLevel logLevel = LogLevel.DEBUG;
        BroadcastDispatcherLogger$logBroadcastDispatched$2 broadcastDispatcherLogger$logBroadcastDispatched$2 = BroadcastDispatcherLogger$logBroadcastDispatched$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logBroadcastDispatched$2);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setStr2(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logReceiverRegistered(int i, @NotNull BroadcastReceiver broadcastReceiver, int i2) {
        String broadcastReceiver2 = broadcastReceiver.toString();
        String flagToString = Companion.flagToString(i2);
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logReceiverRegistered$2 broadcastDispatcherLogger$logReceiverRegistered$2 = BroadcastDispatcherLogger$logReceiverRegistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logReceiverRegistered$2);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        obtain.setStr2(flagToString);
        logBuffer.commit(obtain);
    }

    public final void logTagForRemoval(int i, @NotNull BroadcastReceiver broadcastReceiver) {
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogLevel logLevel = LogLevel.DEBUG;
        BroadcastDispatcherLogger$logTagForRemoval$2 broadcastDispatcherLogger$logTagForRemoval$2 = BroadcastDispatcherLogger$logTagForRemoval$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logTagForRemoval$2);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logClearedAfterRemoval(int i, @NotNull BroadcastReceiver broadcastReceiver) {
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogLevel logLevel = LogLevel.DEBUG;
        BroadcastDispatcherLogger$logClearedAfterRemoval$2 broadcastDispatcherLogger$logClearedAfterRemoval$2 = BroadcastDispatcherLogger$logClearedAfterRemoval$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logClearedAfterRemoval$2);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logReceiverUnregistered(int i, @NotNull BroadcastReceiver broadcastReceiver) {
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logReceiverUnregistered$2 broadcastDispatcherLogger$logReceiverUnregistered$2 = BroadcastDispatcherLogger$logReceiverUnregistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logReceiverUnregistered$2);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logContextReceiverRegistered(int i, int i2, @NotNull IntentFilter intentFilter) {
        String str;
        String joinToString$default = SequencesKt___SequencesKt.joinToString$default(SequencesKt__SequencesKt.asSequence(intentFilter.actionsIterator()), ",", "Actions(", ")", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
        if (intentFilter.countCategories() != 0) {
            str = SequencesKt___SequencesKt.joinToString$default(SequencesKt__SequencesKt.asSequence(intentFilter.categoriesIterator()), ",", "Categories(", ")", 0, (CharSequence) null, (Function1) null, 56, (Object) null);
        } else {
            str = "";
        }
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logContextReceiverRegistered$2 broadcastDispatcherLogger$logContextReceiverRegistered$2 = BroadcastDispatcherLogger$logContextReceiverRegistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logContextReceiverRegistered$2);
        obtain.setInt1(i);
        if (!Intrinsics.areEqual((Object) str, (Object) "")) {
            joinToString$default = joinToString$default + 10 + str;
        }
        obtain.setStr1(joinToString$default);
        obtain.setStr2(Companion.flagToString(i2));
        logBuffer.commit(obtain);
    }

    public final void logContextReceiverUnregistered(int i, @NotNull String str) {
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logContextReceiverUnregistered$2 broadcastDispatcherLogger$logContextReceiverUnregistered$2 = BroadcastDispatcherLogger$logContextReceiverUnregistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logContextReceiverUnregistered$2);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
