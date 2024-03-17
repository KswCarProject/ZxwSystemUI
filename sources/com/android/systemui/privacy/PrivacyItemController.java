package com.android.systemui.privacy;

import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final DelayableExecutor bgExecutor;
    @NotNull
    public final List<WeakReference<Callback>> callbacks = new ArrayList();
    @Nullable
    public Runnable holdingRunnableCanceler;
    @NotNull
    public final MyExecutor internalUiExecutor;
    public boolean listening;
    @NotNull
    public final PrivacyLogger logger;
    @NotNull
    public final Runnable notifyChanges;
    @NotNull
    public final PrivacyItemController$optionsCallback$1 optionsCallback;
    @NotNull
    public final PrivacyConfig privacyConfig;
    @NotNull
    public final PrivacyItemController$privacyItemMonitorCallback$1 privacyItemMonitorCallback;
    @NotNull
    public final Set<PrivacyItemMonitor> privacyItemMonitors;
    @NotNull
    public List<PrivacyItem> privacyList = CollectionsKt__CollectionsKt.emptyList();
    @NotNull
    public final SystemClock systemClock;
    @NotNull
    public final Runnable updateListAndNotifyChanges;

    /* compiled from: PrivacyItemController.kt */
    public interface Callback extends PrivacyConfig.Callback {
        void onPrivacyItemsChanged(@NotNull List<PrivacyItem> list);
    }

    @VisibleForTesting
    public static /* synthetic */ void getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public PrivacyItemController(@NotNull DelayableExecutor delayableExecutor, @NotNull DelayableExecutor delayableExecutor2, @NotNull PrivacyConfig privacyConfig2, @NotNull Set<PrivacyItemMonitor> set, @NotNull PrivacyLogger privacyLogger, @NotNull SystemClock systemClock2, @NotNull DumpManager dumpManager) {
        this.bgExecutor = delayableExecutor2;
        this.privacyConfig = privacyConfig2;
        this.privacyItemMonitors = set;
        this.logger = privacyLogger;
        this.systemClock = systemClock2;
        this.internalUiExecutor = new MyExecutor(delayableExecutor);
        this.notifyChanges = new PrivacyItemController$notifyChanges$1(this);
        this.updateListAndNotifyChanges = new PrivacyItemController$updateListAndNotifyChanges$1(this, delayableExecutor);
        PrivacyItemController$optionsCallback$1 privacyItemController$optionsCallback$1 = new PrivacyItemController$optionsCallback$1(this);
        this.optionsCallback = privacyItemController$optionsCallback$1;
        this.privacyItemMonitorCallback = new PrivacyItemController$privacyItemMonitorCallback$1(this);
        dumpManager.registerDumpable("PrivacyItemController", this);
        privacyConfig2.addCallback((PrivacyConfig.Callback) privacyItemController$optionsCallback$1);
    }

    @VisibleForTesting
    /* compiled from: PrivacyItemController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @VisibleForTesting
        public static /* synthetic */ void getTIME_TO_HOLD_INDICATORS$annotations() {
        }

        private Companion() {
        }
    }

    @NotNull
    public final synchronized List<PrivacyItem> getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return CollectionsKt___CollectionsKt.toList(this.privacyList);
    }

    public final boolean getMicCameraAvailable() {
        return this.privacyConfig.getMicCameraAvailable();
    }

    public final boolean getLocationAvailable() {
        return this.privacyConfig.getLocationAvailable();
    }

    public final boolean getAllIndicatorsAvailable() {
        return getMicCameraAvailable() && getLocationAvailable() && this.privacyConfig.getMediaProjectionAvailable();
    }

    public final void update() {
        this.bgExecutor.execute(new PrivacyItemController$update$1(this));
    }

    public final void setListeningState() {
        boolean z = !this.callbacks.isEmpty();
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                for (PrivacyItemMonitor startListening : this.privacyItemMonitors) {
                    startListening.startListening(this.privacyItemMonitorCallback);
                }
                update();
                return;
            }
            for (PrivacyItemMonitor stopListening : this.privacyItemMonitors) {
                stopListening.stopListening();
            }
            update();
        }
    }

    public final void addCallback(WeakReference<Callback> weakReference) {
        this.callbacks.add(weakReference);
        if ((!this.callbacks.isEmpty()) && !this.listening) {
            this.internalUiExecutor.updateListeningState();
        } else if (this.listening) {
            this.internalUiExecutor.execute(new NotifyChangesToCallback((Callback) weakReference.get(), getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        }
    }

    public final void removeCallback(WeakReference<Callback> weakReference) {
        this.callbacks.removeIf(new PrivacyItemController$removeCallback$1(weakReference));
        if (this.callbacks.isEmpty()) {
            this.internalUiExecutor.updateListeningState();
        }
    }

    public final void addCallback(@NotNull Callback callback) {
        addCallback((WeakReference<Callback>) new WeakReference(callback));
    }

    public final void removeCallback(@NotNull Callback callback) {
        removeCallback((WeakReference<Callback>) new WeakReference(callback));
    }

    public final void updatePrivacyList() {
        Runnable runnable = this.holdingRunnableCanceler;
        if (runnable != null) {
            runnable.run();
            Unit unit = Unit.INSTANCE;
            this.holdingRunnableCanceler = null;
        }
        if (!this.listening) {
            this.privacyList = CollectionsKt__CollectionsKt.emptyList();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (PrivacyItemMonitor activePrivacyItems : this.privacyItemMonitors) {
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, activePrivacyItems.getActivePrivacyItems());
        }
        this.privacyList = processNewList(CollectionsKt___CollectionsKt.distinct(arrayList));
    }

    public final List<PrivacyItem> processNewList(List<PrivacyItem> list) {
        Object obj;
        this.logger.logRetrievedPrivacyItemsList(list);
        long elapsedRealtime = this.systemClock.elapsedRealtime() - 5000;
        ArrayList arrayList = new ArrayList();
        Iterator it = getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core().iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            PrivacyItem privacyItem = (PrivacyItem) next;
            if (privacyItem.getTimeStampElapsed() <= elapsedRealtime || isIn(privacyItem, list)) {
                z = false;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        if (!arrayList.isEmpty()) {
            this.logger.logPrivacyItemsToHold(arrayList);
            Iterator it2 = arrayList.iterator();
            if (!it2.hasNext()) {
                obj = null;
            } else {
                Object next2 = it2.next();
                if (!it2.hasNext()) {
                    obj = next2;
                } else {
                    long timeStampElapsed = ((PrivacyItem) next2).getTimeStampElapsed();
                    do {
                        Object next3 = it2.next();
                        long timeStampElapsed2 = ((PrivacyItem) next3).getTimeStampElapsed();
                        if (timeStampElapsed > timeStampElapsed2) {
                            next2 = next3;
                            timeStampElapsed = timeStampElapsed2;
                        }
                    } while (it2.hasNext());
                }
                obj = next2;
            }
            Intrinsics.checkNotNull(obj);
            long timeStampElapsed3 = ((PrivacyItem) obj).getTimeStampElapsed() - elapsedRealtime;
            this.logger.logPrivacyItemsUpdateScheduled(timeStampElapsed3);
            this.holdingRunnableCanceler = this.bgExecutor.executeDelayed(this.updateListAndNotifyChanges, timeStampElapsed3);
        }
        ArrayList arrayList2 = new ArrayList();
        for (Object next4 : list) {
            if (!((PrivacyItem) next4).getPaused()) {
                arrayList2.add(next4);
            }
        }
        return CollectionsKt___CollectionsKt.plus(arrayList2, arrayList);
    }

    public final boolean isIn(PrivacyItem privacyItem, List<PrivacyItem> list) {
        boolean z;
        Iterable<PrivacyItem> iterable = list;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            for (PrivacyItem privacyItem2 : iterable) {
                if (privacyItem2.getPrivacyType() == privacyItem.getPrivacyType() && Intrinsics.areEqual((Object) privacyItem2.getApplication(), (Object) privacyItem.getApplication()) && privacyItem2.getTimeStampElapsed() == privacyItem.getTimeStampElapsed()) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    /* compiled from: PrivacyItemController.kt */
    public static final class NotifyChangesToCallback implements Runnable {
        @Nullable
        public final Callback callback;
        @NotNull
        public final List<PrivacyItem> list;

        public NotifyChangesToCallback(@Nullable Callback callback2, @NotNull List<PrivacyItem> list2) {
            this.callback = callback2;
            this.list = list2;
        }

        public void run() {
            Callback callback2 = this.callback;
            if (callback2 != null) {
                callback2.onPrivacyItemsChanged(this.list);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a3, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a7, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a8, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ac, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dump(@org.jetbrains.annotations.NotNull java.io.PrintWriter r3, @org.jetbrains.annotations.NotNull java.lang.String[] r4) {
        /*
            r2 = this;
            android.util.IndentingPrintWriter r3 = com.android.systemui.util.DumpUtilsKt.asIndenting(r3)
            java.lang.String r0 = "PrivacyItemController state:"
            r3.println(r0)
            r3.increaseIndent()
            java.lang.String r0 = "Listening: "
            boolean r1 = r2.listening     // Catch:{ all -> 0x00ad }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ all -> 0x00ad }
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r1)     // Catch:{ all -> 0x00ad }
            r3.println(r0)     // Catch:{ all -> 0x00ad }
            java.lang.String r0 = "Privacy Items:"
            r3.println(r0)     // Catch:{ all -> 0x00ad }
            r3.increaseIndent()     // Catch:{ all -> 0x00ad }
            java.util.List r0 = r2.getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core()     // Catch:{ all -> 0x00a8 }
            java.lang.Iterable r0 = (java.lang.Iterable) r0     // Catch:{ all -> 0x00a8 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x00a8 }
        L_0x002d:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x00a8 }
            if (r1 == 0) goto L_0x0041
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x00a8 }
            com.android.systemui.privacy.PrivacyItem r1 = (com.android.systemui.privacy.PrivacyItem) r1     // Catch:{ all -> 0x00a8 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00a8 }
            r3.println(r1)     // Catch:{ all -> 0x00a8 }
            goto L_0x002d
        L_0x0041:
            r3.decreaseIndent()     // Catch:{ all -> 0x00ad }
            java.lang.String r0 = "Callbacks:"
            r3.println(r0)     // Catch:{ all -> 0x00ad }
            r3.increaseIndent()     // Catch:{ all -> 0x00ad }
            java.util.List<java.lang.ref.WeakReference<com.android.systemui.privacy.PrivacyItemController$Callback>> r0 = r2.callbacks     // Catch:{ all -> 0x00a3 }
            java.lang.Iterable r0 = (java.lang.Iterable) r0     // Catch:{ all -> 0x00a3 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x00a3 }
        L_0x0054:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x00a3 }
            if (r1 == 0) goto L_0x0071
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x00a3 }
            java.lang.ref.WeakReference r1 = (java.lang.ref.WeakReference) r1     // Catch:{ all -> 0x00a3 }
            java.lang.Object r1 = r1.get()     // Catch:{ all -> 0x00a3 }
            com.android.systemui.privacy.PrivacyItemController$Callback r1 = (com.android.systemui.privacy.PrivacyItemController.Callback) r1     // Catch:{ all -> 0x00a3 }
            if (r1 != 0) goto L_0x0069
            goto L_0x0054
        L_0x0069:
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00a3 }
            r3.println(r1)     // Catch:{ all -> 0x00a3 }
            goto L_0x0054
        L_0x0071:
            r3.decreaseIndent()     // Catch:{ all -> 0x00ad }
            java.lang.String r0 = "PrivacyItemMonitors:"
            r3.println(r0)     // Catch:{ all -> 0x00ad }
            r3.increaseIndent()     // Catch:{ all -> 0x00ad }
            java.util.Set<com.android.systemui.privacy.PrivacyItemMonitor> r2 = r2.privacyItemMonitors     // Catch:{ all -> 0x009e }
            java.lang.Iterable r2 = (java.lang.Iterable) r2     // Catch:{ all -> 0x009e }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x009e }
        L_0x0084:
            boolean r0 = r2.hasNext()     // Catch:{ all -> 0x009e }
            if (r0 == 0) goto L_0x0094
            java.lang.Object r0 = r2.next()     // Catch:{ all -> 0x009e }
            com.android.systemui.privacy.PrivacyItemMonitor r0 = (com.android.systemui.privacy.PrivacyItemMonitor) r0     // Catch:{ all -> 0x009e }
            r0.dump(r3, r4)     // Catch:{ all -> 0x009e }
            goto L_0x0084
        L_0x0094:
            r3.decreaseIndent()     // Catch:{ all -> 0x00ad }
            r3.decreaseIndent()
            r3.flush()
            return
        L_0x009e:
            r2 = move-exception
            r3.decreaseIndent()     // Catch:{ all -> 0x00ad }
            throw r2     // Catch:{ all -> 0x00ad }
        L_0x00a3:
            r2 = move-exception
            r3.decreaseIndent()     // Catch:{ all -> 0x00ad }
            throw r2     // Catch:{ all -> 0x00ad }
        L_0x00a8:
            r2 = move-exception
            r3.decreaseIndent()     // Catch:{ all -> 0x00ad }
            throw r2     // Catch:{ all -> 0x00ad }
        L_0x00ad:
            r2 = move-exception
            r3.decreaseIndent()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.PrivacyItemController.dump(java.io.PrintWriter, java.lang.String[]):void");
    }

    /* compiled from: PrivacyItemController.kt */
    public final class MyExecutor implements Executor {
        @NotNull
        public final DelayableExecutor delegate;
        @Nullable
        public Runnable listeningCanceller;

        public MyExecutor(@NotNull DelayableExecutor delayableExecutor) {
            this.delegate = delayableExecutor;
        }

        public void execute(@NotNull Runnable runnable) {
            this.delegate.execute(runnable);
        }

        public final void updateListeningState() {
            Runnable runnable = this.listeningCanceller;
            if (runnable != null) {
                runnable.run();
            }
            this.listeningCanceller = this.delegate.executeDelayed(new PrivacyItemController$MyExecutor$updateListeningState$1(PrivacyItemController.this), 0);
        }
    }
}
