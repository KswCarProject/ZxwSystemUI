package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.util.IndentingPrintWriter;
import android.util.SparseSetArray;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.PrintWriter;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

/* compiled from: PendingRemovalStore.kt */
public final class PendingRemovalStore implements Dumpable {
    @NotNull
    public final BroadcastDispatcherLogger logger;
    @NotNull
    public final SparseSetArray<BroadcastReceiver> pendingRemoval = new SparseSetArray<>();

    public PendingRemovalStore(@NotNull BroadcastDispatcherLogger broadcastDispatcherLogger) {
        this.logger = broadcastDispatcherLogger;
    }

    public final void tagForRemoval(@NotNull BroadcastReceiver broadcastReceiver, int i) {
        this.logger.logTagForRemoval(i, broadcastReceiver);
        synchronized (this.pendingRemoval) {
            this.pendingRemoval.add(i, broadcastReceiver);
        }
    }

    public final boolean isPendingRemoval(@NotNull BroadcastReceiver broadcastReceiver, int i) {
        boolean z;
        synchronized (this.pendingRemoval) {
            z = this.pendingRemoval.contains(i, broadcastReceiver) || this.pendingRemoval.contains(-1, broadcastReceiver);
        }
        return z;
    }

    public final void clearPendingRemoval(@NotNull BroadcastReceiver broadcastReceiver, int i) {
        synchronized (this.pendingRemoval) {
            this.pendingRemoval.remove(i, broadcastReceiver);
        }
        this.logger.logClearedAfterRemoval(i, broadcastReceiver);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        synchronized (this.pendingRemoval) {
            if (printWriter instanceof IndentingPrintWriter) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            int size = this.pendingRemoval.size();
            int i = 0;
            while (i < size) {
                int i2 = i + 1;
                int keyAt = this.pendingRemoval.keyAt(i);
                printWriter.print(keyAt);
                printWriter.print("->");
                printWriter.println(this.pendingRemoval.get(keyAt));
                i = i2;
            }
            if (printWriter instanceof IndentingPrintWriter) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            Unit unit = Unit.INSTANCE;
        }
    }
}
