package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BroadcastDispatcher.kt */
public class BroadcastDispatcher implements Dumpable {
    @NotNull
    public final Executor bgExecutor;
    @NotNull
    public final Looper bgLooper;
    @NotNull
    public final Context context;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final BroadcastDispatcher$handler$1 handler;
    @NotNull
    public final BroadcastDispatcherLogger logger;
    @NotNull
    public final SparseArray<UserBroadcastDispatcher> receiversByUser = new SparseArray<>(20);
    @NotNull
    public final PendingRemovalStore removalPendingStore;
    @NotNull
    public final UserTracker userTracker;

    public final void registerReceiver(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter) {
        registerReceiver$default(this, broadcastReceiver, intentFilter, (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }

    public final void registerReceiver(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter, @Nullable Executor executor) {
        registerReceiver$default(this, broadcastReceiver, intentFilter, executor, (UserHandle) null, 0, (String) null, 56, (Object) null);
    }

    public final void registerReceiver(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter, @Nullable Executor executor, @Nullable UserHandle userHandle) {
        registerReceiver$default(this, broadcastReceiver, intentFilter, executor, userHandle, 0, (String) null, 48, (Object) null);
    }

    public final void registerReceiverWithHandler(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter, @NotNull Handler handler2) {
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler2, (UserHandle) null, 0, (String) null, 56, (Object) null);
    }

    public final void registerReceiverWithHandler(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter, @NotNull Handler handler2, @NotNull UserHandle userHandle) {
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler2, userHandle, 0, (String) null, 48, (Object) null);
    }

    public BroadcastDispatcher(@NotNull Context context2, @NotNull Looper looper, @NotNull Executor executor, @NotNull DumpManager dumpManager2, @NotNull BroadcastDispatcherLogger broadcastDispatcherLogger, @NotNull UserTracker userTracker2, @NotNull PendingRemovalStore pendingRemovalStore) {
        this.context = context2;
        this.bgLooper = looper;
        this.bgExecutor = executor;
        this.dumpManager = dumpManager2;
        this.logger = broadcastDispatcherLogger;
        this.userTracker = userTracker2;
        this.removalPendingStore = pendingRemovalStore;
        this.handler = new BroadcastDispatcher$handler$1(this, looper);
    }

    public final void initialize() {
        this.dumpManager.registerDumpable(getClass().getName(), this);
    }

    public static /* synthetic */ void registerReceiverWithHandler$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler2, UserHandle userHandle, int i, String str, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 8) != 0) {
                userHandle = broadcastDispatcher.context.getUser();
            }
            UserHandle userHandle2 = userHandle;
            if ((i2 & 16) != 0) {
                i = 2;
            }
            int i3 = i;
            if ((i2 & 32) != 0) {
                str = null;
            }
            broadcastDispatcher.registerReceiverWithHandler(broadcastReceiver, intentFilter, handler2, userHandle2, i3, str);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiverWithHandler");
    }

    public void registerReceiverWithHandler(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter, @NotNull Handler handler2, @NotNull UserHandle userHandle, int i, @Nullable String str) {
        registerReceiver(broadcastReceiver, intentFilter, new HandlerExecutor(handler2), userHandle, i, str);
    }

    public static /* synthetic */ void registerReceiver$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i, String str, int i2, Object obj) {
        if (obj == null) {
            broadcastDispatcher.registerReceiver(broadcastReceiver, intentFilter, (i2 & 4) != 0 ? null : executor, (i2 & 8) != 0 ? null : userHandle, (i2 & 16) != 0 ? 2 : i, (i2 & 32) != 0 ? null : str);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiver");
    }

    public void registerReceiver(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter, @Nullable Executor executor, @Nullable UserHandle userHandle, int i, @Nullable String str) {
        checkFilter(intentFilter);
        if (executor == null) {
            executor = this.context.getMainExecutor();
        }
        Executor executor2 = executor;
        if (userHandle == null) {
            userHandle = this.context.getUser();
        }
        this.handler.obtainMessage(0, i, 0, new ReceiverData(broadcastReceiver, intentFilter, executor2, userHandle, str)).sendToTarget();
    }

    public final void checkFilter(IntentFilter intentFilter) {
        StringBuilder sb = new StringBuilder();
        if (intentFilter.countActions() == 0) {
            sb.append("Filter must contain at least one action. ");
        }
        if (intentFilter.countDataAuthorities() != 0) {
            sb.append("Filter cannot contain DataAuthorities. ");
        }
        if (intentFilter.countDataPaths() != 0) {
            sb.append("Filter cannot contain DataPaths. ");
        }
        if (intentFilter.countDataSchemes() != 0) {
            sb.append("Filter cannot contain DataSchemes. ");
        }
        if (intentFilter.countDataTypes() != 0) {
            sb.append("Filter cannot contain DataTypes. ");
        }
        if (intentFilter.getPriority() != 0) {
            sb.append("Filter cannot modify priority. ");
        }
        if (!TextUtils.isEmpty(sb)) {
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void unregisterReceiver(@NotNull BroadcastReceiver broadcastReceiver) {
        this.removalPendingStore.tagForRemoval(broadcastReceiver, -1);
        this.handler.obtainMessage(1, broadcastReceiver).sendToTarget();
    }

    @NotNull
    @VisibleForTesting
    public UserBroadcastDispatcher createUBRForUser(int i) {
        return new UserBroadcastDispatcher(this.context, i, this.bgLooper, this.bgExecutor, this.logger, this.removalPendingStore);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println("Broadcast dispatcher:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        int size = this.receiversByUser.size();
        for (int i = 0; i < size; i++) {
            indentingPrintWriter.println(Intrinsics.stringPlus("User ", Integer.valueOf(this.receiversByUser.keyAt(i))));
            this.receiversByUser.valueAt(i).dump(indentingPrintWriter, strArr);
        }
        indentingPrintWriter.println("Pending removal:");
        this.removalPendingStore.dump(indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
    }
}
