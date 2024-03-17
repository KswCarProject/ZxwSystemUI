package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UserBroadcastDispatcher.kt */
public class UserBroadcastDispatcher implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final AtomicInteger index = new AtomicInteger(0);
    @NotNull
    public final ArrayMap<ReceiverProperties, ActionReceiver> actionsToActionsReceivers = new ArrayMap<>();
    @NotNull
    public final Executor bgExecutor;
    @NotNull
    public final Handler bgHandler;
    @NotNull
    public final Looper bgLooper;
    @NotNull
    public final Context context;
    @NotNull
    public final BroadcastDispatcherLogger logger;
    @NotNull
    public final ArrayMap<BroadcastReceiver, Set<String>> receiverToActions = new ArrayMap<>();
    @NotNull
    public final PendingRemovalStore removalPendingStore;
    public final int userId;

    public static /* synthetic */ void getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        for (Map.Entry next : getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core().entrySet()) {
            ReceiverProperties receiverProperties = (ReceiverProperties) next.getKey();
            ActionReceiver actionReceiver = (ActionReceiver) next.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            sb.append(receiverProperties.getAction());
            sb.append(": ");
            sb.append(BroadcastDispatcherLogger.Companion.flagToString(receiverProperties.getFlags()));
            String str = "):";
            if (receiverProperties.getPermission() != null) {
                str = ':' + receiverProperties.getPermission() + str;
            }
            sb.append(str);
            printWriter.println(sb.toString());
            actionReceiver.dump(printWriter, strArr);
        }
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }

    public UserBroadcastDispatcher(@NotNull Context context2, int i, @NotNull Looper looper, @NotNull Executor executor, @NotNull BroadcastDispatcherLogger broadcastDispatcherLogger, @NotNull PendingRemovalStore pendingRemovalStore) {
        this.context = context2;
        this.userId = i;
        this.bgLooper = looper;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
        this.removalPendingStore = pendingRemovalStore;
        this.bgHandler = new Handler(looper);
    }

    /* compiled from: UserBroadcastDispatcher.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    /* compiled from: UserBroadcastDispatcher.kt */
    public static final class ReceiverProperties {
        @NotNull
        public final String action;
        public final int flags;
        @Nullable
        public final String permission;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ReceiverProperties)) {
                return false;
            }
            ReceiverProperties receiverProperties = (ReceiverProperties) obj;
            return Intrinsics.areEqual((Object) this.action, (Object) receiverProperties.action) && this.flags == receiverProperties.flags && Intrinsics.areEqual((Object) this.permission, (Object) receiverProperties.permission);
        }

        public int hashCode() {
            int hashCode = ((this.action.hashCode() * 31) + Integer.hashCode(this.flags)) * 31;
            String str = this.permission;
            return hashCode + (str == null ? 0 : str.hashCode());
        }

        @NotNull
        public String toString() {
            return "ReceiverProperties(action=" + this.action + ", flags=" + this.flags + ", permission=" + this.permission + ')';
        }

        public ReceiverProperties(@NotNull String str, int i, @Nullable String str2) {
            this.action = str;
            this.flags = i;
            this.permission = str2;
        }

        @NotNull
        public final String getAction() {
            return this.action;
        }

        public final int getFlags() {
            return this.flags;
        }

        @Nullable
        public final String getPermission() {
            return this.permission;
        }
    }

    @NotNull
    public final ArrayMap<ReceiverProperties, ActionReceiver> getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.actionsToActionsReceivers;
    }

    public final boolean isReceiverReferenceHeld$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull BroadcastReceiver broadcastReceiver) {
        boolean z;
        Iterable values = this.actionsToActionsReceivers.values();
        if (!((Collection) values).isEmpty()) {
            Iterator it = values.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((ActionReceiver) it.next()).hasReceiver(broadcastReceiver)) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        z = false;
        if (z || this.receiverToActions.containsKey(broadcastReceiver)) {
            return true;
        }
        return false;
    }

    public final void registerReceiver(@NotNull ReceiverData receiverData, int i) {
        handleRegisterReceiver(receiverData, i);
    }

    public final void unregisterReceiver(@NotNull BroadcastReceiver broadcastReceiver) {
        handleUnregisterReceiver(broadcastReceiver);
    }

    public final void handleRegisterReceiver(ReceiverData receiverData, int i) {
        Preconditions.checkState(this.bgLooper.isCurrentThread(), "This method should only be called from BG thread");
        ArrayMap<BroadcastReceiver, Set<String>> arrayMap = this.receiverToActions;
        BroadcastReceiver receiver = receiverData.getReceiver();
        Set<String> set = arrayMap.get(receiver);
        if (set == null) {
            set = new ArraySet<>();
            arrayMap.put(receiver, set);
        }
        Collection collection = set;
        Iterator<String> actionsIterator = receiverData.getFilter().actionsIterator();
        Sequence<T> asSequence = actionsIterator == null ? null : SequencesKt__SequencesKt.asSequence(actionsIterator);
        if (asSequence == null) {
            asSequence = SequencesKt__SequencesKt.emptySequence();
        }
        CollectionsKt__MutableCollectionsKt.addAll(collection, asSequence);
        Iterator<String> actionsIterator2 = receiverData.getFilter().actionsIterator();
        while (actionsIterator2.hasNext()) {
            String next = actionsIterator2.next();
            ArrayMap<ReceiverProperties, ActionReceiver> actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            ReceiverProperties receiverProperties = new ReceiverProperties(next, i, receiverData.getPermission());
            ActionReceiver actionReceiver = actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core.get(receiverProperties);
            if (actionReceiver == null) {
                actionReceiver = createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(next, receiverData.getPermission(), i);
                actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core.put(receiverProperties, actionReceiver);
            }
            actionReceiver.addReceiverData(receiverData);
        }
        this.logger.logReceiverRegistered(this.userId, receiverData.getReceiver(), i);
    }

    @NotNull
    public ActionReceiver createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull String str, @Nullable String str2, int i) {
        return new ActionReceiver(str, this.userId, new UserBroadcastDispatcher$createActionReceiver$1(this, str2, i), new UserBroadcastDispatcher$createActionReceiver$2(this, str), this.bgExecutor, this.logger, new UserBroadcastDispatcher$createActionReceiver$3(this.removalPendingStore));
    }

    public final void handleUnregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Preconditions.checkState(this.bgLooper.isCurrentThread(), "This method should only be called from BG thread");
        for (String str : (Iterable) this.receiverToActions.getOrDefault(broadcastReceiver, new LinkedHashSet())) {
            for (Map.Entry next : getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core().entrySet()) {
                ActionReceiver actionReceiver = (ActionReceiver) next.getValue();
                if (Intrinsics.areEqual((Object) ((ReceiverProperties) next.getKey()).getAction(), (Object) str)) {
                    actionReceiver.removeReceiver(broadcastReceiver);
                }
            }
        }
        this.receiverToActions.remove(broadcastReceiver);
        this.logger.logReceiverUnregistered(this.userId, broadcastReceiver);
    }
}
