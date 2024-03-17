package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ActionReceiver.kt */
public final class ActionReceiver extends BroadcastReceiver implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final AtomicInteger index = new AtomicInteger(0);
    @NotNull
    public final String action;
    @NotNull
    public final ArraySet<String> activeCategories = new ArraySet<>();
    @NotNull
    public final Executor bgExecutor;
    @NotNull
    public final BroadcastDispatcherLogger logger;
    @NotNull
    public final ArraySet<ReceiverData> receiverDatas = new ArraySet<>();
    @NotNull
    public final Function2<BroadcastReceiver, IntentFilter, Unit> registerAction;
    public boolean registered;
    @NotNull
    public final Function2<BroadcastReceiver, Integer, Boolean> testPendingRemovalAction;
    @NotNull
    public final Function1<BroadcastReceiver, Unit> unregisterAction;
    public final int userId;

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        if (printWriter instanceof IndentingPrintWriter) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        printWriter.println(Intrinsics.stringPlus("Registered: ", Boolean.valueOf(getRegistered())));
        printWriter.println("Receivers:");
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        for (ReceiverData receiver : this.receiverDatas) {
            printWriter.println(receiver.getReceiver());
        }
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
        printWriter.println(Intrinsics.stringPlus("Categories: ", CollectionsKt___CollectionsKt.joinToString$default(this.activeCategories, ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null)));
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }

    public ActionReceiver(@NotNull String str, int i, @NotNull Function2<? super BroadcastReceiver, ? super IntentFilter, Unit> function2, @NotNull Function1<? super BroadcastReceiver, Unit> function1, @NotNull Executor executor, @NotNull BroadcastDispatcherLogger broadcastDispatcherLogger, @NotNull Function2<? super BroadcastReceiver, ? super Integer, Boolean> function22) {
        this.action = str;
        this.userId = i;
        this.registerAction = function2;
        this.unregisterAction = function1;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
        this.testPendingRemovalAction = function22;
    }

    /* compiled from: ActionReceiver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final boolean getRegistered() {
        return this.registered;
    }

    public final void addReceiverData(@NotNull ReceiverData receiverData) throws IllegalArgumentException {
        if (receiverData.getFilter().hasAction(this.action)) {
            ArraySet<String> arraySet = this.activeCategories;
            Iterator<String> categoriesIterator = receiverData.getFilter().categoriesIterator();
            Sequence<T> asSequence = categoriesIterator == null ? null : SequencesKt__SequencesKt.asSequence(categoriesIterator);
            if (asSequence == null) {
                asSequence = SequencesKt__SequencesKt.emptySequence();
            }
            boolean addAll = CollectionsKt__MutableCollectionsKt.addAll(arraySet, asSequence);
            if (this.receiverDatas.add(receiverData) && this.receiverDatas.size() == 1) {
                this.registerAction.invoke(this, createFilter());
                this.registered = true;
            } else if (addAll) {
                this.unregisterAction.invoke(this);
                this.registerAction.invoke(this, createFilter());
            }
        } else {
            throw new IllegalArgumentException("Trying to attach to " + this.action + " without correct action,receiver: " + receiverData.getReceiver());
        }
    }

    public final boolean hasReceiver(@NotNull BroadcastReceiver broadcastReceiver) {
        ArraySet<ReceiverData> arraySet = this.receiverDatas;
        if ((arraySet instanceof Collection) && arraySet.isEmpty()) {
            return false;
        }
        for (ReceiverData receiver : arraySet) {
            if (Intrinsics.areEqual((Object) receiver.getReceiver(), (Object) broadcastReceiver)) {
                return true;
            }
        }
        return false;
    }

    public final IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter(this.action);
        for (String addCategory : this.activeCategories) {
            intentFilter.addCategory(addCategory);
        }
        return intentFilter;
    }

    public final void removeReceiver(@NotNull BroadcastReceiver broadcastReceiver) {
        if (CollectionsKt__MutableCollectionsKt.removeAll(this.receiverDatas, new ActionReceiver$removeReceiver$1(broadcastReceiver)) && this.receiverDatas.isEmpty() && this.registered) {
            this.unregisterAction.invoke(this);
            this.registered = false;
            this.activeCategories.clear();
        }
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) throws IllegalStateException {
        if (Intrinsics.areEqual((Object) intent.getAction(), (Object) this.action)) {
            int andIncrement = index.getAndIncrement();
            this.logger.logBroadcastReceived(andIncrement, this.userId, intent);
            this.bgExecutor.execute(new ActionReceiver$onReceive$1(this, intent, context, andIncrement));
            return;
        }
        throw new IllegalStateException("Received intent for " + intent.getAction() + " in receiver for " + this.action + '}');
    }
}
